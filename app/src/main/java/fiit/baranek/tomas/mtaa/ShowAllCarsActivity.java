package fiit.baranek.tomas.mtaa;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ShowAllCarsActivity extends ListActivity {


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
    }


    public void getCars(){
        new GetCar().execute();
    }

    private class GetCar extends AsyncTask<Long, Integer, Integer>{
        Car[] car;


        @Override
        protected Integer doInBackground(Long... params) {
            URL url;
            HttpURLConnection MyUrlConnection = null;
            TextView t = (TextView) findViewById(R.id.button);

            try {
                url = new URL("https://api.backendless.com/v1/data/Car/D25779F9-165C-478F-FF86-9806836E8B00");


                MyUrlConnection = (HttpURLConnection) url
                        .openConnection();
                MyUrlConnection.setRequestMethod("GET");
                MyUrlConnection.setRequestProperty("application-id", "1AF9A17F-4152-8B23-FF2C-C25040E38A00");
                MyUrlConnection.setRequestProperty( "secret-key","953B4A54-64D4-4FA9-FFC5-B9DA0CC18800");
                MyUrlConnection.setRequestProperty("application-type", "REST");
                int code = MyUrlConnection.getResponseCode();
                Gson gson = new Gson();
                InputStream in = MyUrlConnection.getInputStream();
                InputStreamReader is = new InputStreamReader(in);
                car = gson.fromJson(is,Car[].class);




            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (MyUrlConnection != null) {
                    MyUrlConnection.disconnect();
                }
            }

            return null;
        }

        @Override
        public void onPostExecute(Integer result) {
            TextView t = (TextView) findViewById(R.id.textView);
        //    t.setText(String.format("%s",car.c_location));

        }

    }


}
