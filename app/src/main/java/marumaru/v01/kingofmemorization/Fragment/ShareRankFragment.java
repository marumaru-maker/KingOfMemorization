package marumaru.v01.kingofmemorization.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import marumaru.v01.kingofmemorization.R;
import marumaru.v01.kingofmemorization.Request.GetShareRankRequest;
import marumaru.v01.kingofmemorization.domain.UserRank;

public class ShareRankFragment extends Fragment {

    ArrayList<UserRank> userRanks;
    LinearLayout ll_share_rank;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Toast.makeText(getContext(), "Share Rank Fragment", Toast.LENGTH_SHORT).show();

        // 서버에서 순위 받아오기
        userRanks = new ArrayList<>();

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        UserRank userRank = new UserRank();
                        userRank.setRank(object.getInt("rank"));
                        userRank.setUserID(object.getString("userID"));
                        userRank.setCnt(object.getLong("cnt"));

                        userRanks.add(userRank);

                        Log.d("row", userRank.getUserID());

                        LinearLayout row_rank = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.row_rank, null, false);

                        TextView tv_rank_rank = row_rank.findViewById(R.id.tv_rank_rank);
                        tv_rank_rank.setText(userRank.getRank()+" 등");

                        TextView tv_rank_userID = row_rank.findViewById(R.id.tv_rank_userID);
                        tv_rank_userID.setText(userRank.getUserID());

                        TextView tv_rank_cnt = row_rank.findViewById(R.id.tv_rank_cnt);
                        tv_rank_cnt.setText(userRank.getCnt()+" 개");

                        ll_share_rank.addView(row_rank);

                        /*






                         */
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        GetShareRankRequest getShareRankRequest = new GetShareRankRequest(listener);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(getShareRankRequest);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_share_rank, container, false);

        ll_share_rank = view.findViewById(R.id.ll_share_rank);

        return view;
    }
}
