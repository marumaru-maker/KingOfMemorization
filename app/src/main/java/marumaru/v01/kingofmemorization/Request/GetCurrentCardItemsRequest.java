package marumaru.v01.kingofmemorization.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetCurrentCardItemsRequest extends StringRequest {

    private static final String url = "http://rkskekahdid.cafe24.com/GetCurrentCardItems.php";
    private Map<String, String> map;

    public GetCurrentCardItemsRequest(Long cno, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);

        map = new HashMap<>();
        map.put("cno", cno + "");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {

        return map;
    }
}
