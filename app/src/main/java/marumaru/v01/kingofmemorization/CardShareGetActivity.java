package marumaru.v01.kingofmemorization;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

import marumaru.v01.kingofmemorization.Request.ShareCardGetRequest;
import marumaru.v01.kingofmemorization.domain.Card_Item;

public class CardShareGetActivity extends AppCompatActivity {

    ArrayList<Card_Item> lists = null;
    ShareGetAdapter adapter;
    RecyclerView rv_share_get;
    TextView tv_share_title, tv_share_regdate, tv_share_star;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cardshare_get);

        String cnos = getIntent().getStringExtra("cnos"); // 인텐트에서 cnos 값 받아오기
        String title = getIntent().getStringExtra("title");
        String reg_date = getIntent().getStringExtra("reg_date");
        Long num_star = getIntent().getLongExtra("num_star", 0L);

        tv_share_title = findViewById(R.id.tv_share_title);
        tv_share_title.setText(title);
        tv_share_regdate = findViewById(R.id.tv_share_regdate);
        tv_share_regdate.setText(reg_date);
        tv_share_star = findViewById(R.id.tv_share_star);
        tv_share_star.setText(num_star+" 개");

        // 툴바 처리
        Toolbar toolbar = findViewById(R.id.toolbar_share_get);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // rv 에 어댑터 넣어주고 거기에다 뷰홀더 생성되게 할 것
        // 어댑터에 넣을 내용은 volley 로 받아오기
        // 뷰홀더에 넣을 레이아웃 짜기

        rv_share_get = findViewById(R.id.rv_share_get);

        // Volley 로 cno 에 해당하는 정보 받아오기

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response", response);

                lists = new ArrayList<>();

                // response 를 json 파싱해서 lists 에 넣어주기
                try {
                    JSONArray arr_json = new JSONArray(response);

                    for (int i=0; i < arr_json.length(); i++){
                        Card_Item item = new Card_Item();

                        JSONObject object = (JSONObject) arr_json.get(i);

                        item.setItem_key(object.getString("item_key"));
                        item.setItem_value(object.getString("item_value"));

                        lists.add(item);
                    }

                    adapter = new ShareGetAdapter(getApplicationContext(), lists);
                    rv_share_get.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ShareCardGetRequest request = new ShareCardGetRequest(cnos, listener);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

        lists = new ArrayList<>();
        adapter = new ShareGetAdapter(getApplicationContext(), lists);

        rv_share_get.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        rv_share_get.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share_get, menu);

        MenuItem menu_logout = menu.findItem(R.id.action_logout);
        MenuItem menu_home = menu.findItem(R.id.action_home);

        menu_logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                UserSharedPreference.removeUserId(getApplicationContext());

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                return true;
            }
        });

        menu_home.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                return true;
            }
        });

        return true;
    }
}
