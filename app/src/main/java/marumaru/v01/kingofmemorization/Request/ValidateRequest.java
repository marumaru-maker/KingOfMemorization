package marumaru.v01.kingofmemorization.Request;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {

    private static final String url = "http://rkskekahdid.cafe24.com/ValidateRequest.php";
    private Map<String, String> param;

    public ValidateRequest(String userId, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);

        param = new HashMap<>();
        param.put("userId", userId);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return param;
    }
}
