package marumaru.v01.kingofmemorization.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import marumaru.v01.kingofmemorization.domain.CardPost;
import marumaru.v01.kingofmemorization.domain.User;

public class ShareCardRequest extends StringRequest {

    private static final String url = "http://rkskekahdid.cafe24.com/ShareCardRequest.php";
    private Map<String, String> param;

    public ShareCardRequest(CardPost cardPost, String cnos, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);

        param = new HashMap<>();
        param.put("title", cardPost.getTitle());
        param.put("category", cardPost.getCategory());
        param.put("writer", cardPost.getWriter());
        param.put("cnos", cnos);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return param;
    }
}
