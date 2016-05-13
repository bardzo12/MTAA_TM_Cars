package fiit.baranek.tomas.mtaa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import fiit.baranek.tomas.mtaa.Database.DatabaseHandler;

/**
 * Created by matus on 15. 3. 2016.
 */
public class MyAsyncTask extends AsyncTask<RequestParameters, Integer, ResponseParameters> {

    public AsyncResponse delegate = null;
    private Activity activity;
    private ProgressDialog dialog;
    private Context context;
    private DatabaseHandler db;

    public MyAsyncTask(Activity activity){
        this.activity = activity;
        this.context = activity;
        this.dialog = new ProgressDialog(activity, R.style.AlertDialogCustom);
        this.dialog.setTitle("Loading");
        this.dialog.setMessage("Loading data from database ...");
        if(!this.dialog.isShowing()){
            this.dialog.show();
        }
    }


    @Override
    protected ResponseParameters doInBackground(RequestParameters... params){
        URL url;

        String jsonString = null;
        final ResponseParameters responseParameters = new ResponseParameters();
        final RequestParameters requestParameters = params[0];
        Boolean isOnline = requestParameters.isOnline;
        if(isOnline) {

            final boolean[] koniec = {false};
            IO.Options opts = new IO.Options();
            opts.secure = false;
            opts.port = 1341;
            opts.reconnection = true;
            opts.forceNew = true;
            opts.timeout = 5000;
            Socket socket = null;
            try {
                socket = IO.socket(requestParameters.url.toString(), opts);
                socket.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }




            try {


                if (requestParameters.requestType.equals("GET")) {


                        if (requestParameters.Type == 1) {
                            Log.i("Sprava", "get na vsetkz auta\n" );
                            JSONObject js = new JSONObject();
                            try {
                                UUID uid = UUID.fromString("150d9dac-d42a-491c-be06-0c63552972dc");
                                js.put("url", "/data/" + uid.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("Sprava", "stale get na vsetkz auta\n" );
                            assert socket != null;
                            socket.emit("get", js, new Ack() {

                                @Override
                                public void call(Object... args) {
                                    System.out.println("Sme sem");
                                    try {
                                        Log.i("Sprava", "stale get na vsetkz auta\n" );
                                        JSONObject response = (JSONObject) args[0];
                                        JSONObject arr = response.getJSONObject("body");
                                        //JSONObject statusCode = response.getJSONObject("statusCode");
                                        JSONArray data = arr.getJSONArray("data");
                          //              Log.i("Sprava", "get na vsetkz auta\n" +data.toString());
                                        List<Car> cars = getCarsFromString(data);
                                        responseParameters.setListOfCars(cars);
                                        responseParameters.setResponseCode(response.getInt("statusCode"));
                                        responseParameters.setType("GET");
                                        Log.i("Sprava", "code"+ cars.size());
                                        //System.out.println("Totoka je v JSONayrra:" + data.toString());
                                        //JSONArray pole = data.getJSONArray("data");
                                        System.out.println("Status kod je: " + response.getInt("statusCode"));
                                        koniec[0] = true;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    //System.out.println("Výpis JSONa:"+response);
                                }
                            });

                        } else {
                            JSONObject js = new JSONObject();
                            try {
                                UUID uid = UUID.fromString("150d9dac-d42a-491c-be06-0c63552972dc");
                                js.put("url", "/data/" + uid.toString() + "/" + requestParameters.carId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            assert socket != null;
                            socket.emit("get", js, new Ack() {
                                @Override
                                public void call(Object... args) {
                                    try {
                                        System.out.println("Som v get jedna");
                                        JSONObject response = (JSONObject) args[0];
                                        JSONObject body = response.getJSONObject("body");
                                        JSONObject data = body.getJSONObject("data");
                                        responseParameters.setCar(getCarFromString(data));
                                        responseParameters.getCar().setObjectId(body.getString("id"));
                                        System.out.println("Getnute idečko: " + responseParameters.getCar().getObjectId());
                                        responseParameters.setType("GET");
                                        responseParameters.setResponseCode(response.getInt("statusCode"));
                                        koniec[0] = true;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }


                } else if (requestParameters.requestType.equals("DELETE")) {
                    Log.i("Sprava", "Vzmayujem");
                    JSONObject js = new JSONObject();
                    try {
                        UUID uid = UUID.fromString("150d9dac-d42a-491c-be06-0c63552972dc");
                        js.put("url", "/data/" + uid.toString() + "/" + requestParameters.carId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    assert socket != null;
                    socket.emit("delete", js, new Ack() {
                        @Override
                        public void call(Object... args) {
                            JSONObject response = (JSONObject) args[0];
                            try {
                                responseParameters.setResponseCode(response.getInt("statusCode"));
                                koniec[0] = true;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    responseParameters.setType(requestParameters.requestType);
                }else if(requestParameters.requestType.equals("PUT")){
                    JSONObject js = new JSONObject();
                    try {
                        UUID uid = UUID.fromString("150d9dac-d42a-491c-be06-0c63552972dc");
                        js.put("url", "/data/" + uid.toString() + "/" + requestParameters.carId);
                        JSONObject obj = requestParameters.json;
                        js.put("data", new JSONObject().put("data", obj));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    assert socket != null;
                    socket.emit("put", js, new Ack() {
                        @Override
                        public void call(Object... args) {
                            JSONObject response = (JSONObject) args[0];
                            try {
                                responseParameters.setType(requestParameters.requestType);
                                responseParameters.setResponseCode(response.getInt("statusCode"));
                                koniec[0] = true;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }else if(requestParameters.requestType.equals("POST")){
                    Log.i("Sprava", "Pridavam");
                    JSONObject js = new JSONObject();
                    try {
                        UUID uid = UUID.fromString("150d9dac-d42a-491c-be06-0c63552972dc");
                        js.put("url", "/data/" + uid.toString());
                        JSONObject obj = requestParameters.json;
                        js.put("data", new JSONObject().put("data", obj));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socket.emit("post", js, new Ack() {
                        @Override
                        public void call(Object... args) {
                            JSONObject response = (JSONObject) args[0];
                            try {
                                responseParameters.setResponseCode(response.getInt("statusCode"));
                                koniec[0] = true;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    responseParameters.setType(requestParameters.requestType);
                }
             //   Log.i("Sprava", "URL: "+url);



            } catch (Exception e) {
                e.printStackTrace();
            }
            while (!koniec[0]) {

            }
            return responseParameters;
        }else{

            if (requestParameters.requestType.equals("GET")) {
                if (requestParameters.Type == 1) {
                    responseParameters.setResponseCode(200);
                    if(requestParameters.db != null) {
                        responseParameters.setType(requestParameters.requestType);
                        responseParameters.setListOfCars(requestParameters.db.getAllCars());
                        return responseParameters;
                    }
                    else
                        return null;
                } else {
                    responseParameters.setResponseCode(200);
                    DatabaseHandler db = new DatabaseHandler(requestParameters.context);
                    if(db != null){
                        responseParameters.setType(requestParameters.requestType);
                        responseParameters.setCar(db.getCar(requestParameters.carId));
                    }
                    return responseParameters;
                }
            } else if (requestParameters.requestType.equals("DELETE")){

            }
        }
        return null;
    }

    /**
     * Method reads stream and returns string value
     * @param stream
     * @return
     */
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

    /**
     * his method gets String value, and returns
     * List of Cars

     * @return
     * @throws JSONException
     */
    public List<Car> getCarsFromString(JSONArray data) throws JSONException {
        ArrayList<Car> cars = new ArrayList<Car>();
        for (int i = 0; i < data.length(); i++) {

            Car carFromJson = new Car();
            carFromJson.setObjectId(data.getJSONObject(i).getString("id"));
            carFromJson.setC_engine(data.getJSONObject(i).getJSONObject("data").getString("c_engine"));
            carFromJson.setC_location(data.getJSONObject(i).getJSONObject("data").getString("c_location"));
            carFromJson.setC_phoneNumber(data.getJSONObject(i).getJSONObject("data").getString("c_phoneNumber"));
            carFromJson.setC_price(Integer.parseInt(data.getJSONObject(i).getJSONObject("data").getString("c_price")));
            carFromJson.setC_categoryBrand(Integer.parseInt(data.getJSONObject(i).getJSONObject("data").getString("c_categoryBrand")));
            carFromJson.setC_yearOfProduction(Integer.parseInt(data.getJSONObject(i).getJSONObject("data").getString("c_yearOfProduction")));
            carFromJson.setC_model(data.getJSONObject(i).getJSONObject("data").getString("c_model"));
            carFromJson.setC_mileAge(Integer.parseInt(data.getJSONObject(i).getJSONObject("data").getString("c_mileAge")));
            carFromJson.setC_photo(data.getJSONObject(i).getJSONObject("data").getString("c_photo"));
            carFromJson.setC_categoryFuel(Integer.parseInt(data.getJSONObject(i).getJSONObject("data").getString("c_categoryFuel")));
            System.out.println(Integer.parseInt(data.getJSONObject(i).getJSONObject("data").getString("c_categoryTransmission")));
            carFromJson.setC_categoryTransmission(Integer.parseInt(data.getJSONObject(i).getJSONObject("data").getString("c_categoryTransmission")));
            carFromJson.setC_driveType(data.getJSONObject(i).getJSONObject("data").getString("c_driveType"));
            carFromJson.setC_interiorColor(data.getJSONObject(i).getJSONObject("data").getString("c_interiorColor"));
            carFromJson.setC_update(Long.parseLong(data.getJSONObject(i).getJSONObject("data").getString("c_update")));

            URL url;
            InputStream in;
            BufferedInputStream buf;
            ByteArrayOutputStream buffer = null;
            try {
                url = new URL(carFromJson.getC_photo());
                in = url.openStream();

                // Read the inputstream
                buf = new BufferedInputStream(in);

                buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data2 = new byte[16384];

                while ((nRead = in.read(data2, 0, data2.length)) != -1) {
                    buffer.write(data2, 0, nRead);
                }

                buffer.flush();
                // Convert the BufferedInputStream to a Bitmap
                Bitmap bMap = BitmapFactory.decodeStream(buf);
                if (in != null) {
                    in.close();
                }
                if (buf != null) {
                    buf.close();
                }

            } catch (Exception e) {
                Log.e("Error reading file", e.toString());
            }
            carFromJson.setC_image(buffer.toByteArray());

            cars.add(carFromJson);
        }
        return cars;
    }


    /**
     * this method gets String value, and returns

     * @return
     * @throws JSONException
     * @throws IOException
     */

    public Car getCarFromString(JSONObject data) throws JSONException, IOException {

        Car car = new Car();
        car.setC_engine(data.getString("c_engine"));
        car.setC_phoneNumber(data.getString("c_phoneNumber"));
        car.setC_price(data.getInt("c_price"));
        car.setC_location(data.getString("c_location"));
        car.setC_categoryBrand(data.getInt("c_categoryBrand"));
        car.setC_yearOfProduction(data.getInt("c_yearOfProduction"));
        car.setC_model(data.getString("c_model"));
        car.setC_mileAge(data.getInt("c_mileAge"));
        car.setC_photo(data.getString("c_photo"));
        car.setC_categoryFuel(data.getInt("c_categoryFuel"));
        car.setC_categoryTransmission(data.getInt("c_categoryTransmission"));
        car.setC_driveType(data.getString("c_driveType"));
        car.setC_interiorColor(data.getString("c_interiorColor"));
        car.setC_update(data.getInt("c_update"));
        URL url;
        InputStream in;
        BufferedInputStream buf;
        ByteArrayOutputStream buffer = null;
        try {
            url = new URL(car.getC_photo());
            in = url.openStream();

            // Read the inputstream
            buf = new BufferedInputStream(in);

            buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data2 = new byte[16384];

            while ((nRead = in.read(data2, 0, data2.length)) != -1) {
                buffer.write(data2, 0, nRead);
            }

            buffer.flush();
            System.out.println(buffer.toByteArray());
            // Convert the BufferedInputStream to a Bitmap
            Bitmap bMap = BitmapFactory.decodeStream(buf);
            if (in != null) {
                in.close();
            }
            if (buf != null) {
                buf.close();
            }

        } catch (Exception e) {
            Log.e("Error reading file", e.toString());
        }
        car.setC_image(buffer.toByteArray());
        return car;
    }

    @Override
    public void onPostExecute(ResponseParameters result) {

        this.dialog.dismiss();

        delegate.processFinish(result);

    }

}