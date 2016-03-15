package fiit.baranek.tomas.mtaa.Activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
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


public class ShowAllCarsActivity extends ListActivity implements AsyncResponse {
    LeDeviceListAdapter adapter;
    MyAsyncTask asyncTask =new MyAsyncTask();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_all_cars);
        this.setFinishOnTouchOutside(false);

        //this to set delegate/listener back to this class
        asyncTask.delegate = this;


        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RequestParameters r = null;
                URL https = null;
                try {
                    https = new URL("https://api.backendless.com/v1/data/Car");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                r = new RequestParameters(https);


              asyncTask.execute(r);



            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();
        adapter = new LeDeviceListAdapter();
        setListAdapter(adapter);
    }

    @Override
    public void processFinish(List<Car> output) {
        TextView t = (TextView ) findViewById(R.id.textView);

        t.setText(output.get(0).getC_location());
        Log.i("Sprava", "velkost"+ output.size());
        adapter.addList(output);
      //  adapter.addDevice(output.get(0));
        adapter.notifyDataSetChanged();
    }


    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<Car> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<Car>();
            mInflator = ShowAllCarsActivity.this.getLayoutInflater();
        }

        public void addDevice(Car car) {
            if(!mLeDevices.contains(car)) {
                mLeDevices.add(car);
                Log.i("Sprava", "som tu");
            }
        }

        public void addList(List<Car> cars){
            mLeDevices.clear();
            mLeDevices = (ArrayList<Car>) cars;

        }

        public Car getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
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
                viewHolder.car_fuel = (TextView) view.findViewById(R.id.car_fuel);
                viewHolder.car_model = (TextView) view.findViewById(R.id.car_model);
                viewHolder.car_price = (TextView) view.findViewById(R.id.car_price);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Car device = mLeDevices.get(i);
            final String deviceName = device.getC_model();
            if (deviceName != null && deviceName.length() > 0) {
                viewHolder.car_model.setText(device.getC_model());
                viewHolder.car_brand.setText(String.format(String.valueOf(device.getC_categoryBrand())));
                viewHolder.car_fuel.setText(String.format(String.valueOf(device.getC_categoryFuel())));
                viewHolder.car_price.setText(String.format(String.valueOf(device.getC_price())));
            }
            Log.i("Sprava", "vraciam view");

            return view;
        }


    }
    static class ViewHolder {
        TextView car_brand;
        TextView car_model;
        TextView car_price;
        TextView car_fuel;
    }
        /*

         */


    }


