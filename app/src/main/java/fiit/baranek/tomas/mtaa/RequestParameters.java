package fiit.baranek.tomas.mtaa;

import android.content.Context;

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

    public RequestParameters(URL url, String requestType, int Type, Boolean isOnline, Context context){
        this.isOnline = isOnline;
        this.url = url;
        this.requestType = requestType;
        this.Type = Type;
        this.context = context;
    }

}
