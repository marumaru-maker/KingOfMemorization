package marumaru.v01.kingofmemorization.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ModifyCardRequest extends StringRequest {

    private static final String url = "http://rkskekahdid.cafe24.com/ModifyCard.php";
    private Map<String, String> param;

    String card = null;

    public ModifyCardRequest(String card, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);

        this.card = card;

        param = new HashMap<>();
        param.put("card", card);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return param;
    }


}
