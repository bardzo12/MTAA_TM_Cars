package fiit.baranek.tomas.mtaa.Activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fiit.baranek.tomas.mtaa.AsyncResponse;
import fiit.baranek.tomas.mtaa.Car;
import fiit.baranek.tomas.mtaa.Database.DatabaseHandler;
import fiit.baranek.tomas.mtaa.MyAsyncTask;
import fiit.baranek.tomas.mtaa.R;
import fiit.baranek.tomas.mtaa.RequestParameters;
import fiit.baranek.tomas.mtaa.ResponseParameters;
import fiit.baranek.tomas.mtaa.WebSocket.WebSocket;


public class ShowAllCarsActivity extends ListActivity implements AsyncResponse {

    private CarsListAdapter adapter;
    private DatabaseHandler db;
    private Car publicCar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_cars);
        db = new DatabaseHandler(this);

        ImageButton refreshImageView = (ImageButton) findViewById(R.id.imageRefresh);
        ImageButton insertImageView = (ImageButton) findViewById(R.id.imageaddCar);


        insertImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Ahoj tu som");
                createNew();

            }
        });


        refreshImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(isOnline()) {
                    //get cars for delete and deleted it
                    List<Car> carList = db.getAllCarsDeleted();
                    for (Car auko : carList) {
                        delete(auko);
                    }
                    db.restartTableCarDeleted();
                    //get cars for update and delete it
                    List<Car> carList2 = db.getAllCarsUpdated();
                    for (Car auko2 : carList2) {
                        publicCar = auko2;
                        System.out.println("Autko idčko: " + auko2.getObjectId());
                        update(auko2);
                    }
                    db.restartTableCarUpdated();

                    List<Car> carList3 = db.getAllCarsCreated();
                    for (Car auko2 : carList3) {
                        System.out.println("Autko ktore vytvaram idčko: " + auko2.getObjectId());
                        create(auko2);
                    }
                    db.restartTableCarCreated();
                }
                refresh();
            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        toolbar.setTitle("TM CARS");
    }

    private void createNew() {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }


    /**
     * offline create cars
     */
    public void create(Car auto){
        RequestParameters r = null;
        URL https = null;
        try {
            https = new URL("http://sandbox.touch4it.com:1341/?__sails_io_sdk_version=0.12.1");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        r = new RequestParameters(https, "POST", 1, isOnline(), this, db,"", auto.getJSON());

        MyAsyncTask asyncTask = new MyAsyncTask(this);
        asyncTask.delegate = this;
        asyncTask.execute(r);
        Toast.makeText(this,"Car was saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * offline update cars
     */
    public void update(Car auto){
        if(isOnline()) {
            RequestParameters r = null;
            URL https = null;
            try {
                https = new URL("http://sandbox.touch4it.com:1341/?__sails_io_sdk_version=0.12.1" );
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            r = new RequestParameters(https, "GET",2, isOnline(),this, db, auto.getObjectId());

            MyAsyncTask asyncTask =new MyAsyncTask(this);
            asyncTask.delegate = this;
            asyncTask.execute(r);
        }
    }

    /***
     * offline delete cars
     */
    public void delete(Car auto){
        RequestParameters r = null;

        URL https = null;
        try {
            https = new URL("http://sandbox.touch4it.com:1341/?__sails_io_sdk_version=0.12.1" );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        r = new RequestParameters(https, "DELETE", 1, isOnline(), this, db, auto.getObjectId());

        MyAsyncTask asyncTask = new MyAsyncTask(this);
        asyncTask.delegate = this;
        asyncTask.execute(r);
    }

    /**
     * online get all cars
     */
    public void getAllCars(){

        Log.i(ShowAllCarsActivity.class.getSimpleName(), "volanie get all");
        RequestParameters r = null;
        URL https = null;
        try {
            https = new URL("http://sandbox.touch4it.com:1341/?__sails_io_sdk_version=0.12.1");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        r = new RequestParameters(https, "GET", 1, isOnline(), this,  db, "");

        MyAsyncTask asyncTask =new MyAsyncTask(this);
        asyncTask.delegate = this;
        asyncTask.execute(r);
    }



    @Override
    protected void onResume(){

        Log.i(ShowAllCarsActivity.class.getSimpleName(), "navrat ku vsetkym");
        super.onResume();
        adapter = new CarsListAdapter();
        setListAdapter(adapter);
        getAllCars();



    }

    @Override
    public void processFinish(ResponseParameters responseParameters) {//this methods override method from interface AsyncResponse
        Log.i("Sprava", "cvratil som sa"+responseParameters.getResponseCode());
        if(responseParameters.getResponseCode() == 200) {// add list od Cars do ListView adapter

            if(responseParameters.getType().equals("GET")) {
                if (responseParameters.getListOfCars() != null) {

                    adapter.addList(responseParameters.getListOfCars());
                    db.addCars(responseParameters.getListOfCars());
                    adapter.notifyDataSetChanged();
                } else if(responseParameters.getCar() != null)
                {
                    //offline update
                    if(responseParameters.getCar().getC_update() < publicCar.getC_update()){
                        JSONObject car = new JSONObject();
                        try {
                            car.put("c_engine", publicCar.getC_engine());
                            car.put("c_phoneNumber", publicCar.getC_phoneNumber());
                            car.put("c_price", publicCar.getC_price());
                            car.put("c_location", publicCar.getC_location());
                            car.put("c_categoryBrand", publicCar.getC_categoryBrandInt()+1);
                            car.put("c_yearOfProduction", String.valueOf(publicCar.getC_yearOfProduction()));
                            car.put("c_model", publicCar.getC_model());
                            car.put("c_mileAge", publicCar.getC_mileAge());
                            car.put("c_photo", publicCar.getC_photo());
                            car.put("c_categoryFuel", publicCar.getC_categoryFuelInt()+1);
                            car.put("c_categoryTransmission", publicCar.getC_categoryTransmissionInt()+1);
                            car.put("c_driveType", publicCar.getC_driveType());
                            car.put("c_interiorColor", publicCar.getC_interiorColor());
                            car.put("objectId", publicCar.getObjectId());
                            car.put("c_update", publicCar.getC_update());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(isOnline()) {
                            RequestParameters r = null;
                            URL https = null;
                            try {
                                https = new URL("http://sandbox.touch4it.com:1341/?__sails_io_sdk_version=0.12.1" );
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            System.out.println("UPDATE ID: " + responseParameters.getCar().getObjectId());
                            r = new RequestParameters(https, "PUT", 1, isOnline(), this,db,  responseParameters.getCar().getObjectId(), car);

                            MyAsyncTask asyncTask = new MyAsyncTask(this);
                            asyncTask.delegate = this;
                            asyncTask.execute(r);
                            refresh();
                        }
                    }
            }

            }else if(responseParameters.getType().equals("DELETE")){
                getAllCars();

            }

        }else if(responseParameters.getResponseCode() == 400){
            System.out.println("Toto je ten zlý: " + responseParameters.getType());
            Toast.makeText(ShowAllCarsActivity.this, "BAD REQUEST ON DATABASE!", Toast.LENGTH_SHORT).show();

        }else if(responseParameters.getResponseCode() == 404){

            Toast.makeText(ShowAllCarsActivity.this, "ENTRY NOT FOUND!", Toast.LENGTH_SHORT).show();
            refresh();
        }else if(500<=responseParameters.getResponseCode() && responseParameters.getResponseCode()<=510){

            Toast.makeText(ShowAllCarsActivity.this, "Chyba serverovej časti aplikácie, dáta nie sú dostupné!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        DetailScreenActivity news = (DetailScreenActivity) getListAdapter().getItem(position);
        Intent i = new Intent(getApplicationContext(), DetailScreenActivity.class);
        startActivity(i);

    }




    public boolean deleteCarById(Car CarID){
        db.deleteCar(CarID);
        if (isOnline()) {
            RequestParameters r = null;

            URL https = null;
            try {
                https = new URL("http://sandbox.touch4it.com:1341/?__sails_io_sdk_version=0.12.1");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            r = new RequestParameters(https, "DELETE", 1, isOnline(), this,db, CarID.getObjectId());

            MyAsyncTask asyncTask = new MyAsyncTask(this);
            asyncTask.delegate = this;
            asyncTask.execute(r);

            getAllCars();

            return true;
        }
        else{
            db.addCarDeleted(CarID);
            getAllCars();
            return false;
        }
    }

    private void showDialog(final Car CarID) {
        new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("Vymazanie auta")
                .setMessage("Naozaj chcete vymazat toto auto?")
                .setPositiveButton("ANO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCarById(CarID);
                    }
                })
                .setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void showDetail(Car car) {
        login(car);
    }

    public void login(Car car){
        Intent intent = new Intent(this, DetailScreenActivity.class);
        intent.putExtra("CarID",car.getObjectId());
        intent.putExtra("CarBrand",car.getC_categoryBrandInt());
        intent.putExtra("CarTransmission",car.getC_categoryTransmissionInt());
        intent.putExtra("CarFuel",car.getC_categoryFuelInt());
        startActivity(intent);
    }

    public void refresh(){

        if(isOnline()) {
            getAllCars();
        }
        else{
            Intent intent = new Intent(this, ConnectionErrorActivity.class);
            intent.putExtra("ActivityID", 11);
            startActivity(intent);
        }
    }


    /**
     * Checks whether the network is available.
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * class used for creating view of one Car
     */
    static class ViewHolder {

        TextView car_brand;
        TextView car_model;
        TextView car_yearOgProduction;
        TextView car_price;
        TextView car_fuel;

    }

    private class CarsListAdapter extends BaseAdapter {// adapter for ListView showing all cars
        private ArrayList<Car> arrayListCars;
        private LayoutInflater mInflator;


        public CarsListAdapter() {
            super();
            arrayListCars = new ArrayList<Car>();
            mInflator = ShowAllCarsActivity.this.getLayoutInflater();
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        public void addCar(Car car) {
            if(!arrayListCars.contains(car)) {
                arrayListCars.add(car);

            }
        }

        public void deleteCar(String CarID){
            int size = arrayListCars.size();

            for(int i = 0 ; i < size ; i++){
                if(arrayListCars.get(i).getObjectId().equals(CarID)){
                    arrayListCars.remove(i);
                    return;
                }
            }
        }
        public void addList(List<Car> cars){
            arrayListCars.clear();
            arrayListCars = (ArrayList<Car>) cars;

        }

        public Car getCar(int position) {
            return arrayListCars.get(position);
        }

        public void clear() {
            arrayListCars.clear();
        }

        @Override
        public int getCount() {
            return arrayListCars.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayListCars.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_car, null);
                viewHolder = new ViewHolder();
                viewHolder.car_brand = (TextView) view.findViewById(R.id.car_brand);
                viewHolder.car_model = (TextView) view.findViewById(R.id.car_model);
                viewHolder.car_yearOgProduction = (TextView) view.findViewById(R.id.car_yearOfProduction);
                viewHolder.car_fuel = (TextView) view.findViewById(R.id.car_fuel);
                viewHolder.car_price = (TextView) view.findViewById(R.id.car_price);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final Car device = arrayListCars.get(i);
            final String deviceName = device.getC_model();
            if (deviceName != null && deviceName.length() > 0) {
                viewHolder.car_brand.setText(device.getC_categoryBrand()+"  ");
                viewHolder.car_model.setText(device.getC_model());
                viewHolder.car_yearOgProduction.setText(String.format(String.valueOf(device.getC_yearOfProduction()))+" | ");
                viewHolder.car_fuel.setText(device.getC_categoryFuel());
                viewHolder.car_price.setText(String.format(String.valueOf(device.getC_price()))+" € ");
            }


            RelativeLayout space = (RelativeLayout) view.findViewById(R.id.celyObsah);
            space.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                        showDetail(device);
                }
            });

            ImageButton deleteImageView = (ImageButton) view.findViewById(R.id.deleteButton);
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    showDialog(device);
                }
            });



            return view;
        }


    }


}


