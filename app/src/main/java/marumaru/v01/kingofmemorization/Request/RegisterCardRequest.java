package marumaru.v01.kingofmemorization.Request;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import marumaru.v01.kingofmemorization.domain.Card;

public class RegisterCardRequest extends StringRequest {

    private static final String url = "http://rkskekahdid.cafe24.com/RegisterCard.php";
    private Map<String, String> param;

    String card = null;

    public RegisterCardRequest(String card, Response.Listener<String> listener) {
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
