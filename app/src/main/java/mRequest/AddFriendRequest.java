package mRequest;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abimanyu on 5/15/2017.
 */

public class AddFriendRequest extends StringRequest{
    private static final String USER_REQUEST_URL = "http://waitingtrack.pe.hu/AddFriendWT.php";
    private Map<String, String> params;

    public AddFriendRequest(String username, String userfriend, Response.Listener<String> listener) {
        super(Method.POST, USER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("userfriend", userfriend);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}



