package marumaru.v01.kingofmemorization.Request;

import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import marumaru.v01.kingofmemorization.domain.Criteria;

public class GetSharedCardsRequest extends StringRequest {

    private final static String url = "http://rkskekahdid.cafe24.com/GetSharedCardsRequest.php";
    private Map<String, String> param;

    public GetSharedCardsRequest(Criteria cri, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);

        param = new HashMap<>();

        // cri
        String userId = cri.getUserId();
        String category = cri.getCategory();
        String type = "";

        boolean isStar_desc = cri.isStar_desc();
        boolean isTime_asc = cri.isTime_asc();
        boolean isWriter_only = cri.isWriter_only();

        type += (isStar_desc?"T":"F") + (isTime_asc?"T":"F") + (isWriter_only?"T":"F"); // 첫번째는 별순서, 두번째는 시간순서, 세번째는 작성자온리

        Log.d("param userId", userId);
        Log.d("param category", category);
        Log.d("param type", type);

        param.put("userId", userId);
        param.put("category", category);
        param.put("type", type);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return param;
    }
}
