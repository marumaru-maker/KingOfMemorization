package marumaru.v01.kingofmemorization;

import android.location.Criteria;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

import marumaru.v01.kingofmemorization.Request.RegisterCardRequest;
import marumaru.v01.kingofmemorization.Request.ShareCardRequest;
import marumaru.v01.kingofmemorization.Request.ShowCardsRequest;
import marumaru.v01.kingofmemorization.domain.CardPost;

public class CardShareRegisterActivity extends AppCompatActivity {

    RecyclerView rv;
    LinearLayout ll_share_register_category;
    EditText et_share_register_title,
            et_share_register_category;
    Button btn_share_register_register,
            btn_share_register_add;
    ArrayList<CardPost> list_cardPosts;
    ShareRegisterAdapter adapter;
    int max_category_num = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cardshare_register);

        et_share_register_title = findViewById(R.id.et_share_register_title);
        et_share_register_category = findViewById(R.id.et_share_register_category);
        btn_share_register_register = findViewById(R.id.btn_share_register_register);
        btn_share_register_add = findViewById(R.id.btn_share_register_add);
        ll_share_register_category = findViewById(R.id.ll_share_register_category);


        // 툴바
        Toolbar toolbar = findViewById(R.id.toolbar_share_register);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle("");

        // 데이터셋 받기
        list_cardPosts = new ArrayList<>();



        CardPost cardPost = new CardPost();
        cardPost.setTitle("토익 단어 100개");
        cardPost.setCno(2L);
        cardPost.setReg_date("2020.08.12");
        list_cardPosts.add(cardPost);

        cardPost = new CardPost();
        cardPost.setTitle("토플 단어 200개");
        cardPost.setCno(3L);
        cardPost.setReg_date("2020.08.14");
        list_cardPosts.add(cardPost);

        cardPost = new CardPost();
        cardPost.setTitle("토익 단어 200개");
        cardPost.setCno(4L);
        cardPost.setReg_date("2020.08.14");
        list_cardPosts.add(cardPost);

        cardPost = new CardPost();
        cardPost.setTitle("토플 단어 300개");
        cardPost.setCno(5L);
        cardPost.setReg_date("2020.08.14");
        list_cardPosts.add(cardPost);

        if(list_cardPosts.size() != 0){
            TextView tv_share_empty = findViewById(R.id.tv_share_empty);
            tv_share_empty.setVisibility(View.GONE);
        }

        // 리사이클러뷰 : 어댑터, 뷰홀더 그리고 데이터셋 없으면 비었다는 내용은 TextView의 visibility 처리하기
        rv = findViewById(R.id.rv_share_register);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);

        rv.setLayoutManager(manager);
        rv.addItemDecoration(new CardShareRegisterItemDeco());

        adapter = new ShareRegisterAdapter(getApplicationContext(), list_cardPosts);

        rv.setAdapter(adapter);

        sendGetListRequest(UserSharedPreference.getUserId(getApplicationContext()));

        // 카테고리 추가
        btn_share_register_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = et_share_register_category.getText().toString();

                if(category == null || category.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CardShareRegisterActivity.this);
                    AlertDialog dialog = builder.setMessage("입력된 내용이 없습니다.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }

                // 인플레이터로 뷰 생성
                View category_item = getLayoutInflater().inflate(R.layout.category_item, null);
                category_item.setTag(max_category_num++);
                ((TextView)category_item.findViewById(R.id.tv_share_category_item)).setText(category);

                category_item.findViewById(R.id.ib_share_category_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View view_category = (View) view.getParent();
                        ll_share_register_category.removeView(view_category);
                    }
                });

                ll_share_register_category.addView(category_item);

                /*
                if(tv_share_register_category.getText().toString().equals("없음")){
                    tv_share_register_category.setText(category);
                } else {
                    tv_share_register_category.append("," + category);
                }
                et_share_register_category.setText("");
                */

                // 입력된 카테고리 없애기 LinearLayout vertical horizontal 로 한 다음에 거기에다가 <TextView> <Button> 을 같이 넣어야 할듯

                return;
            }
        });

        // 제목, 카테고리, 선택된 카드가 없다면 Alert

        btn_share_register_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_share_register_title.getText().toString();
                String category = "없음"; // 현재 ',' 으로 구분해서 카테고리를 보낸다; 만약 이걸 'x' 모양을 둬서 삭제 가능한 형태로 한다면 어떻게 받아올 것인가?;
                // 카테고리 넣을 때마다 tag 를 지정하고 max_category 도 늘린다. 그리고 0~max_category 까지 루프를 돌면서 해당 카테고리가 아직 있다면 값을 받아온다.
                // 'x' 누르면 없어지는 리스너를 단다;

                for(int i=0; i<max_category_num; i++){
                    LinearLayout ll = ll_share_register_category.findViewWithTag(i); // 태그로 카테고리 찾기
                    if(ll == null) // null 이면 패스
                        continue;
                    if(i == 0){ // 첫 카테고리인 경우
                        category = ((TextView)ll.findViewById(R.id.tv_share_category_item)).getText()+"";
                    } else { // 그 외
                        category += ","+((TextView)ll.findViewById(R.id.tv_share_category_item)).getText();
                    }
                }
                //Log.d("category", category);

                // 빠진 데이터 처리
                if(title == null || title.equals("") || category.equals("없음")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(CardShareRegisterActivity.this);
                    AlertDialog dialog = builder.setMessage("제목 혹은 카테고리를 채워주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                // 데이터 담기
                CardPost cardPost = new CardPost();
                cardPost.setTitle(title);
                cardPost.setCategory(category);
                cardPost.setWriter(UserSharedPreference.getUserId(getApplicationContext()));

                // 체크된 togglebutton 의 tag 에서 cno 받아서 ","으로 연결하기
                int count_items = rv.getLayoutManager().getChildCount();
                String cnos = "";

                for (int i=0; i < count_items; i++){
                    RelativeLayout rv_item_view = (RelativeLayout) rv.getLayoutManager().getChildAt(i);
                    ToggleButton tb_share_register_card =  rv_item_view.findViewById(R.id.tb_share_register_card);

                    // 토글 버튼이 ON 이라면 추가
                    if(tb_share_register_card.isChecked())
                        cnos += (tb_share_register_card.getTag()+",");
                }

                if(cnos == null || cnos.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(CardShareRegisterActivity.this);
                    AlertDialog dialog = builder.setMessage("공유할 카드를 선택해 주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                // 마지막 콤마 처리
                if(cnos.endsWith(","))
                 cnos = cnos.substring(0, cnos.length()-1);

                // 서버 처리 : 사용자의 카드 정보 받아오기 앞에 썼던 거 쓰면 될 듯
                sendRegisterRequest(cardPost, cnos);

                return;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share_register, menu);

        return true;
    }

    private void sendRegisterRequest(CardPost cardPost, String cnos) {

        Response.Listener<String> responseListener =new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Toast.makeText(getApplicationContext(), "등록 완료", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        ShareCardRequest shareCardRequest = new ShareCardRequest(cardPost, cnos, responseListener);
        RequestQueue queue = Volley.newRequestQueue(CardShareRegisterActivity.this);
        queue.add(shareCardRequest);
    }

    private void sendGetListRequest(String userId){

        // StringRequest ShowCardsRequest 를 작성하여 param 으로 userID 전달 - ResponseListener 에서 받은 Card 정보를 View 로 만들어 append - View 에는 리스너를 달아 눌렀을 때 액티비티 이동되게 할 것

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // ArrayList<CardPost> 받아오기
                list_cardPosts = new ArrayList<>();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = (JSONObject) jsonArray.get(i);

                        CardPost post = new CardPost();
                        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                        post.setReg_date(date_format.format(new Date()));
                        post.setTitle(object.getString("title"));
                        post.setStar(object.getLong("num_star"));
                        post.setCno(object.getLong("cno"));
                        list_cardPosts.add(post);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new ShareRegisterAdapter(CardShareRegisterActivity.this, list_cardPosts);
                rv.setAdapter(adapter);
            }
        };

        ShowCardsRequest request = new ShowCardsRequest(userId, listener);
        RequestQueue queue = Volley.newRequestQueue(CardShareRegisterActivity.this);
        queue.add(request);

    }
}
