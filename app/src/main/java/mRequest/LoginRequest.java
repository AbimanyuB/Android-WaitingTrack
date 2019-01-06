package mRequest;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://waitingtrack.pe.hu/LoginWT.php";
    private Map<String, String> params;

    public LoginRequest(String username, String password, String latlng, String time, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("latlng", latlng);
        params.put("time", time);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
