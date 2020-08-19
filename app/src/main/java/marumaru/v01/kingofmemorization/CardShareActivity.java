package marumaru.v01.kingofmemorization;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import marumaru.v01.kingofmemorization.Request.GetSharedCardsRequest;
import marumaru.v01.kingofmemorization.Request.ShowCardsRequest;
import marumaru.v01.kingofmemorization.domain.Card;
import marumaru.v01.kingofmemorization.domain.CardPost;
import marumaru.v01.kingofmemorization.domain.Criteria;

public class CardShareActivity extends AppCompatActivity {

    ArrayList<CardPost> list_cards;
    RecyclerView rv_share_list;
    CardShareAdapter adapter_share_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cardshare);

        // 툴바
        Toolbar toolbar = findViewById(R.id.toolbar_share);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 서버에서 카드 받아오기
        list_cards = new ArrayList<>();

        /*
        CardPost card = new CardPost();
        card.setTitle("TOEIC VOCA 1000");
        card.setCategory("토익 | 단어 | 영어");
        card.setStar(7L);
        card.setReg_date("2020-01-13");
        list_cards.add(card);
        */

        // 리사이클러뷰 : 어댑터, 뷰홀더
        rv_share_list = findViewById(R.id.rv_share_list);

        LinearLayoutManager ll_manger = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        adapter_share_list = new CardShareAdapter(CardShareActivity.this, list_cards);

        rv_share_list.setLayoutManager(ll_manger);
        rv_share_list.addItemDecoration(new CardShareListItemDeco());

        rv_share_list.setAdapter(adapter_share_list);

        // 카드 검색 세부사항
        Criteria cri = new Criteria();

        sendRequest(cri);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share_list, menu);

        MenuItem menuItem = menu.findItem(R.id.action_register);

        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent = new Intent(getApplicationContext(), CardShareRegisterActivity.class);
                startActivity(intent);

                return true;
            }
        });

        return true;
    }

    private void sendRequest(Criteria cri) {

        // StringRequest ShowCardsRequest 를 작성하여 param 으로 userID 전달 - ResponseListener 에서 받은 Card 정보를 View 로 만들어 append - View 에는 리스너를 달아 눌렀을 때 액티비티 이동되게 할 것

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // ArrayList<CardPost> 받아오기
                list_cards = new ArrayList<>();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = (JSONObject) jsonArray.get(i);

                        CardPost post = new CardPost();
                        post.setReg_date(object.getString("reg_date"));
                        post.setTitle(object.getString("title"));
                        post.setStar(object.getLong("num_star"));
                        post.setCnos(object.getString("cnos"));
                        list_cards.add(post);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter_share_list = new CardShareAdapter(CardShareActivity.this, list_cards);
                rv_share_list.setAdapter(adapter_share_list);
            }
        };

        GetSharedCardsRequest request = new GetSharedCardsRequest(cri, listener);
        RequestQueue queue = Volley.newRequestQueue(CardShareActivity.this);
        queue.add(request);
    }
}
