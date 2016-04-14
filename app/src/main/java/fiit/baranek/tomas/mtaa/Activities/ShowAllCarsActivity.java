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
import fiit.baranek.tomas.mtaa.Enums.CategoryBrand;
import fiit.baranek.tomas.mtaa.Enums.CategoryFuel;
import fiit.baranek.tomas.mtaa.Enums.CategoryTransmission;
import fiit.baranek.tomas.mtaa.MyAsyncTask;
import fiit.baranek.tomas.mtaa.R;
import fiit.baranek.tomas.mtaa.RequestParameters;
import fiit.baranek.tomas.mtaa.ResponseParameters;


public class ShowAllCarsActivity extends ListActivity implements AsyncResponse {
    CarsListAdapter adapter;
    private final static String TAG = ShowAllCarsActivity.class
            .getSimpleName();




    DatabaseHandler db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list_all_cars);
        //   this.setFinishOnTouchOutside(false);
        db = new DatabaseHandler(this);

        ImageButton refreshImageView = (ImageButton) findViewById(R.id.imageRefresh);
        refreshImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(isOnline()) {
                    System.out.println("Vymazané auta offline: ");

                    List<Car> carList = db.getAllCarsDeleted();
                    for (Car auko : carList) {
                        delete(auko);
                    }
                    //delete if succes deleted from backendless
                    db.restartTableCarDeleted();

                    System.out.println("Update auta offline: ");
                    List<Car> carList2 = db.getAllCarsUpdated();
                    for (Car auko2 : carList2) {
                        update(auko2);
                    }
                    db.restartTableCarUpdated();
                    //getAllCars();
                }
                refresh();


            }
        });




        Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        toolbar.setTitle("TM CARS");
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);


    }

    public void update(Car auto){
        JSONObject car = new JSONObject();
        try {
            car.put("c_engine", auto.getC_engine());
            car.put("c_phoneNumber", auto.getC_phoneNumber());
            car.put("c_price", auto.getC_price());
            car.put("c_location", auto.getC_location());
            car.put("c_categoryBrand", auto.getC_categoryBrandInt()+1);
            car.put("c_yearOfProduction", String.valueOf(auto.getC_yearOfProduction()));
            car.put("c_model", auto.getC_model());
            car.put("c_mileAge", auto.getC_mileAge());
            car.put("c_photo", auto.getC_photo());
            car.put("c_categoryFuel", auto.getC_categoryFuelInt()+1);
            car.put("c_categoryTransmission", auto.getC_categoryTransmissionInt()+1);
            car.put("c_driveType", auto.getC_driveType());
            car.put("c_interiorColor", auto.getC_interiorColor());
            car.put("objectId", auto.getObjectId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(isOnline()) {
            RequestParameters r = null;
            URL https = null;
            try {
                https = new URL("https://api.backendless.com/v1/data/Car/" + auto.getObjectId());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            r = new RequestParameters(https, "PUT", 1, isOnline(), this, "", car);

            MyAsyncTask asyncTask = new MyAsyncTask(this);
            asyncTask.delegate = this;
            asyncTask.execute(r);
            //System.out.println(car.toString());
        }
    }
    public void delete(Car auto){
        RequestParameters r = null;

        URL https = null;
        try {
            https = new URL("https://api.backendless.com/v1/data/Car/" + auto.getObjectId());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        r = new RequestParameters(https, "DELETE", 1, isOnline(), this, auto.getObjectId());

        MyAsyncTask asyncTask = new MyAsyncTask(this);
        asyncTask.delegate = this;
        asyncTask.execute(r);
    }
    public void getAllCars(){
        RequestParameters r = null;
        URL https = null;
        try {
            https = new URL("https://api.backendless.com/v1/data/Car?pageSize=100");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        r = new RequestParameters(https, "GET", 1, isOnline(), this, "");

        MyAsyncTask asyncTask =new MyAsyncTask(this);
        asyncTask.delegate = this;
        asyncTask.execute(r);
    }



    @Override
    protected void onResume(){
        super.onResume();
        adapter = new CarsListAdapter();
        setListAdapter(adapter);
        getAllCars();



    }

    @Override
    public void processFinish(ResponseParameters responseParameters) {//this methods override method from interface AsyncResponse
        if(responseParameters.getResponseCode() == 200) {// add list od Cars do ListView adapter
            if(responseParameters.getType().equals("GET")) {
                adapter.addList(responseParameters.getListOfCars());
                db.addCars(responseParameters.getListOfCars());
                adapter.notifyDataSetChanged();
                System.out.println("13.4. GET");
            }else if(responseParameters.getType().equals("DELETE")){
                getAllCars();

            }

        }else if(responseParameters.getResponseCode() == 400){

            Toast.makeText(ShowAllCarsActivity.this, "BAD REQUEST ON DATABASE!", Toast.LENGTH_SHORT).show();

        }else if(responseParameters.getResponseCode() == 404){

            Toast.makeText(ShowAllCarsActivity.this, "ENTRY NOT FOUND!", Toast.LENGTH_SHORT).show();
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
                https = new URL("https://api.backendless.com/v1/data/Car/" + CarID.getObjectId());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            r = new RequestParameters(https, "DELETE", 1, isOnline(), this, CarID.getObjectId());

            MyAsyncTask asyncTask = new MyAsyncTask(this);
            asyncTask.delegate = this;
            asyncTask.execute(r);

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
                .setTitle("Delete car")
                .setMessage("Are you sure you want to delete this car?")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCarById(CarID);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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

        //if(isOnline()) {
        Intent intent = new Intent(this, DetailScreenActivity.class);
        intent.putExtra("CarID",car.getObjectId());
        intent.putExtra("CarBrand",car.getC_categoryBrandInt());
        intent.putExtra("CarTransmission",car.getC_categoryTransmissionInt());
        intent.putExtra("CarFuel",car.getC_categoryFuelInt());
        startActivity(intent);
       /* }
        else{

            Intent intent = new Intent(this, ConnectionErrorActivity.class);
            intent.putExtra("ActivityID", 2);
            intent.putExtra("CarID",CarID);
            startActivity(intent);
        }*/
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
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        else {
            return false;
        }
    }

    private class CarsListAdapter extends BaseAdapter {// adapter for ListView showing all cars
        private ArrayList<Car> arrayListCars;
        private LayoutInflater mInflator;


        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        public CarsListAdapter() {
            super();
            arrayListCars = new ArrayList<Car>();
            mInflator = ShowAllCarsActivity.this.getLayoutInflater();
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
    static class ViewHolder {// class used for creating view of one Car

        TextView car_brand;
        TextView car_model;
        TextView car_yearOgProduction;
        TextView car_price;
        TextView car_fuel;

    }


}


