package mRequest;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abimanyu on 5/15/2017.
 */

public class InsertLatlngRequest extends StringRequest{
    private static final String LATLNG_REQUEST_URL = "http://waitingtrack.pe.hu/InsertLatlngWT.php";
    private Map<String, String> params;

    public InsertLatlngRequest(String username, String latlng, String time, Response.Listener<String> listener) {
        super(Method.POST, LATLNG_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("username", username);
        params.put("latlng", latlng);
        params.put("time", time);

    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}