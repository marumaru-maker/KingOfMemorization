package marumaru.v01.kingofmemorization.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import marumaru.v01.kingofmemorization.domain.CardPost;

public class ModifyShareCardRequest extends StringRequest {

    private static final String url = "http://rkskekahdid.cafe24.com/ModifyShareCardRequest.php";
    private Map<String, String> param;

    public ModifyShareCardRequest(CardPost cardPost, String cnos, Long sno, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);

        param = new HashMap<>();
        param.put("title", cardPost.getTitle());
        param.put("category", cardPost.getCategory());
        param.put("writer", cardPost.getWriter());
        param.put("cnos", cnos);
        param.put("sno", sno+"");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return param;
    }
}
