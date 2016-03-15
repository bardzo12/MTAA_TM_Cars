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
                RequestParameters r = null;
                try {
                   r = new RequestParameters(new URL("https://api.backendless.com/v1/data/Car/2A0844D5-5C5C-90CE-FF17-806CCBA3C400"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                new MyAsyncTask().execute(r);



            }
        });

        setListAdapter(adapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        adapter = new LeDeviceListAdapter();
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
