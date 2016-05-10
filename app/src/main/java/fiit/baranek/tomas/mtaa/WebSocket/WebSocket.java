package fiit.baranek.tomas.mtaa.WebSocket;

import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fiit.baranek.tomas.mtaa.Car;

/**
 * Created by TomasPC on 10.5.2016.
 */
public class WebSocket {

    public WebSocket(){

    }

    public String createNew(Car car) {
        IO.Options opts = new IO.Options();
        opts.secure = false;
        opts.port = 1341;
        opts.reconnection = true;
        opts.forceNew = true;
        opts.timeout = 5000;
        Socket socket = null;
        try {
            socket = IO.socket("http://sandbox.touch4it.com:1341/?__sails_io_sdk_version=0.12.1", opts);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        JSONObject js = new JSONObject();
        try {
            UUID uid = UUID.fromString("f14f9190-c21b-446a-9b84-9c9ea2c1dc76");
            js.put("url", "/data/" + uid.toString());
            JSONObject obj = car.getJSON();
            js.put("data", new JSONObject().put("data", obj));
            //js.put("user",R.string.user_uuid);
            //js.put("id","f9df2caa-aaf3-4399-ad20-0a8519921647");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("post", js, new Ack() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject) args[0];
                System.out.println("Resposne create:" + response);
            }
        });
        return "";
    }

    public Car GETONE(String ID) {
        final Car car= new Car();
        IO.Options opts = new IO.Options();
        opts.secure = false;
        opts.port = 1341;
        opts.reconnection = true;
        opts.forceNew = true;
        opts.timeout = 5000;
        Socket socket = null;
        final boolean[] koniec = {false};
        try {
            socket = IO.socket("http://sandbox.touch4it.com:1341/?__sails_io_sdk_version=0.12.1", opts);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        JSONObject js = new JSONObject();
        try {
            UUID uid = UUID.fromString("f14f9190-c21b-446a-9b84-9c9ea2c1dc76");
            js.put("url", "/data/" + uid.toString() + "/" + ID);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                koniec[0] = true;
            }
        });

        while (!koniec[0]){

        }
        System.out.println("Totoka sa mi vrati z get: " + car.getC_phoneNumber());
        return car;
    }


    public List<Car> GET(){
        final List<Car> cars = new ArrayList<Car>();
        final boolean[] koniec = {false};

        IO.Options opts = new IO.Options();
        opts.secure = false;
        opts.port = 1341;
        opts.reconnection = true;
        opts.forceNew = true;
        opts.timeout = 5000;
        Socket socket = null;
        try {
            socket = IO.socket("http://sandbox.touch4it.com:1341/?__sails_io_sdk_version=0.12.1", opts);
            socket.connect();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        JSONObject js = new JSONObject();
        try {
            UUID uid = UUID.fromString("f14f9190-c21b-446a-9b84-9c9ea2c1dc76");
            js.put("url", "/data/" + uid.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert socket != null;
        socket.emit("get", js, new Ack() {
            @Override
            public void call(Object... args) {
                System.out.println("Sme sem");
                try {
                    JSONObject response = (JSONObject) args[0];
                    JSONObject arr = response.getJSONObject("body");
                    //JSONObject statusCode = response.getJSONObject("statusCode");
                    JSONArray data = arr.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject autko = data.getJSONObject(i);
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
                        cars.add(carFromJson);
                    }
                    koniec[0] = true;
                    //System.out.println("Totoka je v JSONayrra:" + data.toString());
                    //JSONArray pole = data.getJSONArray("data");
                    System.out.println("Status kod je: " + response.getInt("statusCode"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println("Výpis JSONa:"+response);
                }
        });
        while (!koniec[0]) {

        }
        return cars;
    }

}
