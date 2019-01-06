package mRequest;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abimanyu on 5/15/2017.
 */

public class UserRequest extends StringRequest{
    private static final String USER_REQUEST_URL = "http://waitingtrack.pe.hu/UserWT.php";
    private Map<String, String> params;

    public UserRequest(Response.Listener<String> listener) {
        super(Method.GET, USER_REQUEST_URL, listener, null);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}



