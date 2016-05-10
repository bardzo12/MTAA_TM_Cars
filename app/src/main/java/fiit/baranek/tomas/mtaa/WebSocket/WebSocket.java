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

import fiit.baranek.tomas.mtaa.Car;
import fiit.baranek.tomas.mtaa.R;

/**
 * Created by TomasPC on 10.5.2016.
 */
public class WebSocket {

    public WebSocket(){

    }


    public List<Car> GET(){
        List<Car> cars = new ArrayList<Car>();

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
            js.put("url", "/data/TMCars2");
            js.put("user", R.string.user_uuid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("get", js, new Ack() {
            @Override
            public void call(Object... args) {
                System.out.println("Sme sem");
                try {
                    JSONObject response = (JSONObject)args[0];
                    JSONObject arr = response.getJSONObject("body");
                    //JSONObject statusCode = response.getJSONObject("statusCode");
                    System.out.println("Status kod je: " + response.getInt("statusCode"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println("Výpis JSONa:"+response);
            }
        });

        return cars;
    }

}
