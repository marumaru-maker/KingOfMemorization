package marumaru.v01.kingofmemorization.Request;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ShareCardGetRequest extends StringRequest {

    private static final String url = "http://rkskekahdid.cafe24.com/ShareCardGetRequest.php";

    Map<String, String> param;

    public ShareCardGetRequest(String cnos, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);

        param = new HashMap<String, String>();

        param.put("cnos", cnos+"");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return param;
    }
}
