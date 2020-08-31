package marumaru.v01.kingofmemorization;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import marumaru.v01.kingofmemorization.Request.DeleteShareCardRequest;
import marumaru.v01.kingofmemorization.Request.ShareCardGetRequest;
import marumaru.v01.kingofmemorization.domain.Card_Item;

public class CardShareGetActivity extends AppCompatActivity {

    ArrayList<Card_Item> lists = null;
    ShareGetAdapter adapter;
    RecyclerView rv_share_get;
    LinearLayout ll_share_card_get_option;
    TextView tv_share_title, tv_share_regdate, tv_share_star, tv_share_category, tv_share_writer_cnt;

    String writer;
    Long sno;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cardshare_get);

        sno = getIntent().getLongExtra("sno", 0L);
        String cnos = getIntent().getStringExtra("cnos"); // 인텐트에서 cnos 값 받아오기
        String title = getIntent().getStringExtra("title");
        String reg_date = getIntent().getStringExtra("reg_date");
        String category = getIntent().getStringExtra("category");
        writer = getIntent().getStringExtra("writer");
        Long num_star = getIntent().getLongExtra("num_star", 0L);

        tv_share_title = findViewById(R.id.tv_share_title);
        tv_share_title.setText(title);
        tv_share_regdate = findViewById(R.id.tv_share_regdate);
        tv_share_regdate.setText(reg_date);
        tv_share_category = findViewById(R.id.tv_share_category);
        tv_share_category.setText(category);
        tv_share_star = findViewById(R.id.tv_share_star);
        tv_share_star.setText(num_star+" 개");
        tv_share_writer_cnt = findViewById(R.id.tv_share_writer_cnt);
        ll_share_card_get_option = findViewById(R.id.ll_share_card_get_option);

        // 툴바 처리
        Toolbar toolbar = findViewById(R.id.toolbar_share_get);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 작성자일 때 수정, 삭제 버튼
        if(writer.equals(UserSharedPreference.getUserId(CardShareGetActivity.this))){
            View card_option = getLayoutInflater().inflate(R.layout.card_share_option, null);

            card_option.findViewById(R.id.tv_share_get_option_del).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(CardShareGetActivity.this, "Delete this Post!", Toast.LENGTH_SHORT).show();

                    // Volley 로 해당 Request 보내기 sno 을 전달해야 한다;
                    AlertDialog.Builder builder = new AlertDialog.Builder(CardShareGetActivity.this);
                    AlertDialog dialog = builder.setMessage("정말로 삭제하시겠습니까?")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                                                        Response.Listener<String> listener = new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                //Log.d("response", response);
                                                                if(response.equals("Success")){
                                                                    Toast.makeText(CardShareGetActivity.this, "삭제 성공했습니다.", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(CardShareGetActivity.this, "삭제 실패했습니다.", Toast.LENGTH_SHORT).show();
                                                                }
                                                                finish();
                                                            }
                                                        };
                                                        DeleteShareCardRequest reqeust = new DeleteShareCardRequest(sno, listener);
                                                        queue.add(reqeust);
                                                    }
                                                })
                                                .setNegativeButton("취소", null)
                                                .create();
                    dialog.show();
                }
            });

            card_option.findViewById(R.id.tv_share_get_option_mod).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(CardShareGetActivity.this, "Modify this Post!", Toast.LENGTH_SHORT).show();
                }
            });

            ll_share_card_get_option.addView(card_option);
        }

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

                    tv_share_writer_cnt.setText("Made by " + writer + "(" + lists.size() + " 개)");

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
        rv_share_get.addItemDecoration(new CardShareListItemDeco());
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
