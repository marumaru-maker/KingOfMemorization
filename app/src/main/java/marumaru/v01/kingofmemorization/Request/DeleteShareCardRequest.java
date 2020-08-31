package marumaru.v01.kingofmemorization.Request;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteShareCardRequest extends StringRequest {

    private static final String url = "http://rkskekahdid.cafe24.com/DeleteShareCardRequest.php";

    private HashMap<String, String> param;

    public DeleteShareCardRequest(Long sno, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);

        param = new HashMap<>();

        param.put("sno", sno+"");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return param;
    }
}
