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
import marumaru.v01.kingofmemorization.Request.GetASharedCardRequest;
import marumaru.v01.kingofmemorization.Request.ShareCardGetRequest;
import marumaru.v01.kingofmemorization.domain.CardPost;
import marumaru.v01.kingofmemorization.domain.Card_Item;

public class CardShareGetActivity extends AppCompatActivity {

    ArrayList<Card_Item> lists = null;
    ShareGetAdapter adapter;
    RecyclerView rv_share_get;
    LinearLayout ll_share_card_get_option;
    TextView tv_share_title, tv_share_regdate, tv_share_star, tv_share_category, tv_share_writer_cnt;

    CardPost shared_card_post;
    String writer, cnos, category, title;
    Long sno;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cardshare_get);

        // 처음 받아올 때는 intent 로 받아온다; 그리고 수정 후에 onRestart() 일 때는 서버에서 받아온다; 최대한 서버에 요청하는 양을 줄인다;

        sno = getIntent().getLongExtra("sno", 0L);
        cnos = getIntent().getStringExtra("cnos"); // 인텐트에서 cnos 값 받아오기
        title = getIntent().getStringExtra("title");
        String reg_date = getIntent().getStringExtra("reg_date");
        category = getIntent().getStringExtra("category");
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
                    // 수정 액티비티 만들기 - sno, cnos, title, category 등 전달 - 뷰에 뿌리기(제목, 카테고리, 선택한 암기카드) - 수정 버튼 클릭 시 서버에 수정 작업
                    Intent intent = new Intent(getApplicationContext(), CardShareModifyActivity.class);
                    intent.putExtra("sno", sno);
                    startActivity(intent);
                }
            });

            ll_share_card_get_option.addView(card_option);
        }

        rv_share_get = findViewById(R.id.rv_share_get);

        sendGetKeyValueRequest();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        shared_card_post = new CardPost();

        sendGetASharedCardRequest(sno);

        sendGetKeyValueRequest();
    }

    private void sendGetKeyValueRequest() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response", response);

                lists = new ArrayList<>();

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

    private void sendGetASharedCardRequest(Long sno) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response", response);
                if(response == null || response.equals(""))
                    return;

                try {
                    JSONObject object = new JSONObject(response);
                    shared_card_post.setTitle(object.getString("title"));
                    shared_card_post.setCnos(object.getString("cnos"));
                    shared_card_post.setCategory(object.getString("category"));
                    shared_card_post.setReg_date(object.getString("reg_date"));
                    shared_card_post.setStar(object.getLong("num_star"));

                    initSharedCard(shared_card_post);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetASharedCardRequest getASharedCardRequest = new GetASharedCardRequest(sno, listener);

        queue.add(getASharedCardRequest);
    }

    private void initSharedCard(CardPost shared_card_post) {

        tv_share_title.setText(shared_card_post.getTitle());
        tv_share_regdate.setText(shared_card_post.getReg_date());
        tv_share_category.setText(shared_card_post.getCategory());
        tv_share_star.setText(shared_card_post.getStar()+" 개");
        cnos = shared_card_post.getCnos();
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

                finish();

                return true;
            }
        });

        menu_home.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                finish();

                return true;
            }
        });

        return true;
    }
}
