package mRequest;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abimanyu on 5/15/2017.
 */

public class LatlngRequest extends StringRequest{
    private static final String LATLNG_REQUEST_URL = "http://waitingtrack.pe.hu/LatlngWT.php";
    private Map<String, String> params;

    public LatlngRequest(String username, Response.Listener<String> listener) {
        super(Request.Method.POST, LATLNG_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("username", username);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}