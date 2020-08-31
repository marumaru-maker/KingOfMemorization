package marumaru.v01.kingofmemorization.Request;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PutInMyCardListRequest extends StringRequest {

    private static final String url = "http://rkskekahdid.cafe24.com/PutInMyCardListRequest.php";

    HashMap<String, String> param = null;

    public PutInMyCardListRequest(String cnos, String userID, String title, String sno, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);

        param = new HashMap<>();

        param.put("cnos", cnos);
        param.put("userID", userID);
        param.put("title", title);
        param.put("sno", sno);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return param;
    }
}
