package fiit.baranek.tomas.mtaa;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matus on 15. 3. 2016.
 */
public class MyAsyncTask extends AsyncTask<RequestParameters, Integer, List<Car>> {

    public AsyncResponse delegate = null;
    Car cars;



    @Override
    protected List<Car> doInBackground(RequestParameters... params){
        URL url;
        HttpURLConnection MyUrlConnection = null;
        String jsonString = null;
        RequestParameters requestParameters = params[0];
        List<Car> cars = null;


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

            jsonString = readStream(in);
            cars = getCarsFromString(jsonString);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (MyUrlConnection != null) {
                MyUrlConnection.disconnect();
            }
        }

        return cars;
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

    public List<Car> getCarsFromString(String backenlessString) throws JSONException {
        final List<Car> cars = new ArrayList<Car>();
        JSONObject response = new JSONObject(backenlessString);
        Car car;
        JSONArray data = response.getJSONArray("data");
        for(int i=0;i<data.length();i++) {
            car = new Car();
            JSONObject item = (JSONObject) data.get(i);
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
        Log.i("Sprava o velkosti", "velkost" + cars.size());
        return cars;
    }
    @Override
    public void onPostExecute(List<Car> result) {
        delegate.processFinish(result);
    }

}