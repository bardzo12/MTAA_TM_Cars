package fiit.baranek.tomas.mtaa;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class ShowAllCarsActivity extends ListActivity implements  AsyncResponse{
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
                    https = new URL("https://api.backendless.com/v1/data/Car/2A0844D5-5C5C-90CE-FF17-806CCBA3C400");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                r = new RequestParameters(https);


              asyncTask.execute(r);



            }
        });

        setListAdapter(adapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        adapter = new LeDeviceListAdapter();
    }

    @Override
    public void processFinish(String output) {
        TextView t = (TextView ) findViewById(R.id.textView);

        t.setText(output);
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
            }
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

          /*  Car device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());
*/
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
        public List<Car> getCarsFromString(String backenlessString) throws JSONException {
            final List<Car> cars = new ArrayList<Car>();
            JSONObject response = new JSONObject(backenlessString);
            Car car = new Car();
            JSONArray data = response.getJSONArray("data");
            for(int i=0;i<response.length();i++) {
                car = new Car();
                JSONObject item = (JSONObject) data.get(1);
                car.setC_engine(item.optString("c_engine"));
                car.setCreated(item.optString("created"));
                car.setC_phoneNumber(item.optString("c_phoneNumber"));
                car.setC_price(item.optInt("c_price"));
                car.setC_location(item.optString("c_location"));
                car.setC_categoryBrand(item.optInt("c_categoryBrand"));
                car.setC_yearOfProduction(item.optInt("c_yearOfProduction"));
                car.setC_model(item.optString("c_model"));
                car.setC_mileAge(item.optInt("c_mileAge"));
                car.setC_photo(item.optString("c_photo"));
                car.setC_categoryFuel(item.optInt("c_categoryFuel"));
                car.setC_categoryTransmission(item.optInt("c_categoryTransmission"));
                car.setC_driveType(item.optString("c_driveType"));
                car.setC_interiorColor(item.optString("c_interiorColor"));
                car.setObjectId(item.optString("objectId"));
                cars.add(car);
            }
            return cars;
        }

    }


