package marumaru.v01.kingofmemorization.Request;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import marumaru.v01.kingofmemorization.domain.User;

public class LoginRequest extends StringRequest {

    private static final String url = "http://rkskekahdid.cafe24.com/LoginRequest.php";
    private Map<String, String> param;

    public LoginRequest(User user, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);

        param = new HashMap<>();
        param.put("userId", user.getUserId());
        param.put("userPw", user.getUserPw());

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return param;
    }
}
