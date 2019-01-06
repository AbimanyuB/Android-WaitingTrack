package mRequest;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by Abimanyu on 5/11/2017.
 */

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://waitingtrack.pe.hu/RegisterWT.php";
    private Map<String, String> params;

    public RegisterRequest(String username, String name, String password, String phone, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("name", name);
        params.put("password", password);
        params.put("phone", phone);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
