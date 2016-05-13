package fiit.baranek.tomas.mtaa;

import android.content.Context;
import android.provider.ContactsContract;

import org.json.JSONObject;

import java.net.URL;

import fiit.baranek.tomas.mtaa.Database.DatabaseHandler;

/**
 * Created by matus on 15. 3. 2016.
 */
public class RequestParameters {
    public URL url;
    public String requestType;
    public int Type;
    public Boolean isOnline;
    public Context context;
    public DatabaseHandler db;
    public String carId;
    public JSONObject json;

    public RequestParameters(URL url, String requestType, int Type, Boolean isOnline, Context context,DatabaseHandler db, String carId, JSONObject json){
        this.isOnline = isOnline;
        this.url = url;
        this.requestType = requestType;
        this.Type = Type;
        this.context = context;
        this.carId = carId;
        this.json = json;
        this.db = db;
    }

    public RequestParameters(URL url, String requestType, int Type, Boolean isOnline, Context context, DatabaseHandler db, String carId){
        this.isOnline = isOnline;
        this.url = url;
        this.requestType = requestType;
        this.Type = Type;
        this.context = context;
        this.carId = carId;
        this.db = db;

    }

}
