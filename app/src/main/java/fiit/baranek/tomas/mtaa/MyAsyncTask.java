package fiit.baranek.tomas.mtaa;

import android.os.AsyncTask;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by matus on 15. 3. 2016.
 */
public class MyAsyncTask extends AsyncTask<RequestParameters, Integer, String> {

    public AsyncResponse delegate = null;
    Car cars;



    @Override
    protected String doInBackground(RequestParameters... params){
        URL url;
        HttpURLConnection MyUrlConnection = null;
        String json = null;
        RequestParameters requestParameters = params[0];



        try {
            url = requestParameters.url;


            MyUrlConnection = (HttpURLConnection) url
                    .openConnection();
            MyUrlConnection.setRequestMethod("GET");
            MyUrlConnection.setRequestProperty("application-id", "1AF9A17F-4152-8B23-FF2C-C25040E38A00");
            MyUrlConnection.setRequestProperty( "secret-key","953B4A54-64D4-4FA9-FFC5-B9DA0CC18800");
            MyUrlConnection.setRequestProperty("application-type", "REST");
            Gson gson = new Gson();
            InputStream in = MyUrlConnection.getInputStream();
            InputStreamReader is = new InputStreamReader(in);
          //  cars = gson.fromJson(is,Car.class);

            json = readStream(in);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (MyUrlConnection != null) {
                MyUrlConnection.disconnect();
            }
        }

        return json;
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
    public void onPostExecute(String result) {
        delegate.processFinish(result);
    }

}