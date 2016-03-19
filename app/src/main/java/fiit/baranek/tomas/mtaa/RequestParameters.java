package fiit.baranek.tomas.mtaa;

import java.net.URL;

/**
 * Created by matus on 15. 3. 2016.
 */
public class RequestParameters {
    public URL url;
    public String requestType;
    public int Type;

    public RequestParameters(URL url, String requestType, int Type){
        this.url = url;
        this.requestType = requestType;
        this.Type = Type;
    }

}
