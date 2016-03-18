package fiit.baranek.tomas.mtaa.Activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fiit.baranek.tomas.mtaa.AsyncResponse;
import fiit.baranek.tomas.mtaa.Car;
import fiit.baranek.tomas.mtaa.MyAsyncTask;
import fiit.baranek.tomas.mtaa.R;
import fiit.baranek.tomas.mtaa.RequestParameters;
import fiit.baranek.tomas.mtaa.ResponseParameters;


public class ShowAllCarsActivity extends ListActivity implements AsyncResponse {
    CarsListAdapter adapter;
    private final static String TAG = ShowAllCarsActivity.class
            .getSimpleName();




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_all_cars);
        this.setFinishOnTouchOutside(false);

        //this to set delegate/listener back to this class






    }

    public void getAllCars(){

        RequestParameters r = null;
        URL https = null;
        try {
            https = new URL("https://api.backendless.com/v1/data/Car");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        r = new RequestParameters(https, "GET");

        MyAsyncTask asyncTask =new MyAsyncTask();
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
         if(responseParameters.getType().equals("GET")) {                                       // add list od Cars do ListView adapter
             adapter.addList(responseParameters.getListOfCars());
             adapter.notifyDataSetChanged();
         }else{
             adapter.notifyDataSetChanged();
         }

    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        getListView().getItemAtPosition(position);

    }




    public boolean deleteCarById(String CarID){
      //  Toast.makeText(ShowAllCarsActivity.this, "toto je id" + CarID, Toast.LENGTH_SHORT).show();

        RequestParameters r = null;
        URL https = null;
        try {
            https = new URL("https://api.backendless.com/v1/data/Car/"+CarID);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        r = new RequestParameters(https, "DELETE");

        MyAsyncTask asyncTask =new MyAsyncTask();
        asyncTask.delegate = this;
        asyncTask.execute(r);
        return true;
    }

       private void showDialog(final String CarID) {
           new AlertDialog.Builder(this)
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

    private class CarsListAdapter extends BaseAdapter {// adapter for ListView showing all cars
        private ArrayList<Car> arrayListCars;
        private LayoutInflater mInflator;

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



            ImageButton deleteImageView = (ImageButton) view.findViewById(R.id.deleteButton);
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                showDialog(device.getObjectId());
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


