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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class ShowAllCarsActivity extends ListActivity {
    LeDeviceListAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_all_cars);
        this.setFinishOnTouchOutside(false);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getCars();


            }
        });

        setListAdapter(adapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        adapter = new LeDeviceListAdapter();
    }

    public void getCars(){
        new GetCar().execute();
    }

    public class CarsList {

        public List<Car> Movies;
    }

    private class GetCar extends AsyncTask<Long, Integer, Integer> {
        Car cars;



        @Override
        protected Integer doInBackground(Long... params) {
            URL url;
            HttpURLConnection MyUrlConnection = null;
            TextView t = (TextView) findViewById(R.id.button);

            try {
                url = new URL("https://api.backendless.com/v1/data/Car/2A0844D5-5C5C-90CE-FF17-806CCBA3C400");


                MyUrlConnection = (HttpURLConnection) url
                        .openConnection();
                MyUrlConnection.setRequestMethod("GET");
                MyUrlConnection.setRequestProperty("application-id", "1AF9A17F-4152-8B23-FF2C-C25040E38A00");
                MyUrlConnection.setRequestProperty( "secret-key","953B4A54-64D4-4FA9-FFC5-B9DA0CC18800");
                MyUrlConnection.setRequestProperty("application-type", "REST");
                Gson gson = new Gson();
                InputStream in = MyUrlConnection.getInputStream();

                readStream(in);


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (MyUrlConnection != null) {
                    MyUrlConnection.disconnect();
                }
            }

            return null;
        }

        public String readStream(InputStream stream){
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder sb = new StringBuilder();

            String line;

            try {
                while((line = reader.readLine()) != null){
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb.toString();
        }

        @Override
        public void onPostExecute(Integer result) {
TextView t = (TextView) findViewById(R.id.textView);
            t.setText(cars.c_location);
    /*        for(Car car : cars){

                adapter.addDevice(car);

            }
            adapter.notifyDataSetChanged();*/
        }

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
        TextView car_fuel;
        TextView car_brand;
        TextView car_model;
        TextView car_price;
    }


}
