package mRequest;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abimanyu on 5/15/2017.
 */

public class FriendRequest extends StringRequest{
    private static final String USER_REQUEST_URL = "http://waitingtrack.pe.hu/FriendWT.php";
    private Map<String, String> params;

    public FriendRequest(String username, Response.Listener<String> listener) {
        super(Method.POST, USER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}



