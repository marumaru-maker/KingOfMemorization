package marumaru.v01.kingofmemorization;

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

import marumaru.v01.kingofmemorization.Request.GetASharedCardRequest;
import marumaru.v01.kingofmemorization.Request.ModifyShareCardRequest;
import marumaru.v01.kingofmemorization.Request.ShareCardRequest;
import marumaru.v01.kingofmemorization.Request.ShowCardsRequest;
import marumaru.v01.kingofmemorization.domain.CardPost;

public class CardShareModifyActivity extends AppCompatActivity {

    RecyclerView rv;
    EditText et_share_modify_title, et_share_modify_category;
    Button btn_share_modify_register, btn_share_modify_add;
    LinearLayout ll_share_modify_category;
    ArrayList<CardPost> list_cardPosts;
    ShareRegisterAdapter adapter;
    LinearLayoutManager manager;

    int max_category_num = 0;
    CardPost shared_card_post;
    String prev_category, prev_cnos;
    Long sno;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cardshare_modify);

        et_share_modify_title = findViewById(R.id.et_share_modify_title);
        et_share_modify_category = findViewById(R.id.et_share_modify_category);
        btn_share_modify_register = findViewById(R.id.btn_share_modify_register);
        btn_share_modify_add = findViewById(R.id.btn_share_modify_add);
        ll_share_modify_category = findViewById(R.id.ll_share_modify_category);

        sno = getIntent().getLongExtra("sno", 0L);

        // 현재 공유 카드
        shared_card_post = new CardPost();

        // 서버에서 공유 카드 정보 받아오기 : 이 안에서 초기화 처리까지
        sendGetASharedCardRequest(sno);

        // 툴바
        Toolbar toolbar = findViewById(R.id.toolbar_share_modify); // 수정 필요?
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle("");

        // 카테고리 추가
        btn_share_modify_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = et_share_modify_category.getText().toString();

                // 빈 내용 경고
                if(category == null || category.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CardShareModifyActivity.this);
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
                        ll_share_modify_category.removeView(view_category);
                    }
                });

                ll_share_modify_category.addView(category_item);

                return;
            }
        });

        // 제목, 카테고리, 선택된 카드가 없다면 Alert
        btn_share_modify_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_share_modify_title.getText().toString();
                String category = "없음"; // 현재 ',' 으로 구분해서 카테고리를 보낸다; 만약 이걸 'x' 모양을 둬서 삭제 가능한 형태로 한다면 어떻게 받아올 것인가?;
                // 카테고리 넣을 때마다 tag 를 지정하고 max_category 도 늘린다. 그리고 0~max_category 까지 루프를 돌면서 해당 카테고리가 아직 있다면 값을 받아온다.
                // 'x' 누르면 없어지는 리스너를 단다;

                for(int i=0; i<max_category_num; i++){
                    LinearLayout ll = ll_share_modify_category.findViewWithTag(i); // 태그로 카테고리 찾기
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(CardShareModifyActivity.this);
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
                Log.d("Checked cnos", cnos);

                if(cnos == null || cnos.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(CardShareModifyActivity.this);
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
                sendModifyRequest(cardPost, cnos);

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

    private void sendGetASharedCardRequest(Long sno) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Log.d("response", response);
                if(response == null || response.equals(""))
                    return;

                try {
                    JSONObject object = new JSONObject(response);
                    shared_card_post.setTitle(object.getString("title"));
                    shared_card_post.setCnos(object.getString("cnos"));
                    shared_card_post.setCategory(object.getString("category"));

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
        String prev_title = shared_card_post.getTitle();
        prev_category = shared_card_post.getCategory();
        prev_cnos = shared_card_post.getCnos();

        Log.d("prev_title", prev_title);
        Log.d("prev_category", prev_category);
        Log.d("prev_cnos", prev_cnos);
        Log.d("sno", sno+"");

        // 기존 제목 뷰 적용
        et_share_modify_title.setText(prev_title);

        // 기존 카테고리 뷰 적용
        String[] arr_prev_category = prev_category.split("\\|"); // '|' 로 나눌려면 '\\' 를 앞에 두지 않으면 boolean 과 같은 결과를 리턴한다;

        for(int i=0; i<arr_prev_category.length; i++){
            View category_item = getLayoutInflater().inflate(R.layout.category_item, null);
            category_item.setTag(max_category_num++);
            ((TextView)category_item.findViewById(R.id.tv_share_category_item)).setText(arr_prev_category[i]);

            category_item.findViewById(R.id.ib_share_category_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View view_category = (View) view.getParent();
                    ll_share_modify_category.removeView(view_category);
                }
            });
            ll_share_modify_category.addView(category_item);
        }

        // 데이터셋 받기
        list_cardPosts = new ArrayList<>();

        // 리사이클러뷰 : 어댑터, 뷰홀더 그리고 데이터셋 없으면 비었다는 내용은 TextView의 visibility 처리하기
        rv = findViewById(R.id.rv_share_modify);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);

        rv.setLayoutManager(manager);
        rv.addItemDecoration(new CardShareRegisterItemDeco());

        adapter = new ShareRegisterAdapter(getApplicationContext(), list_cardPosts, null);

        rv.setAdapter(adapter);

        sendGetListRequest(UserSharedPreference.getUserId(getApplicationContext()));
    }

    private void sendModifyRequest(CardPost cardPost, String cnos) {

        Response.Listener<String> responseListener =new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Toast.makeText(getApplicationContext(), "수정 완료", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ModifyShareCardRequest modifyShareCardRequest = new ModifyShareCardRequest(cardPost, cnos, sno, responseListener);
        RequestQueue queue = Volley.newRequestQueue(CardShareModifyActivity.this);
        queue.add(modifyShareCardRequest);
    }

    private void sendGetListRequest(String userId){

        // StringRequest ShowCardsRequest 를 작성하여 param 으로 userID 전달 - ResponseListener 에서 받은 Card 정보를 View 로 만들어 append - View 에는 리스너를 달아 눌렀을 때 액티비티 이동되게 할 것

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response", response);

                // ArrayList<CardPost> 받아오기
                list_cardPosts = new ArrayList<>();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = (JSONObject) jsonArray.get(i);

                        CardPost post = new CardPost();
                        post.setReg_date(object.getString("reg_date"));
                        post.setTitle(object.getString("title"));
                        post.setCno(object.getLong("cno"));
                        list_cardPosts.add(post);
                    }

                    if(list_cardPosts.size() != 0){
                        TextView tv_share_empty = findViewById(R.id.tv_share_empty);
                        tv_share_empty.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new ShareRegisterAdapter(CardShareModifyActivity.this, list_cardPosts, prev_cnos);
                rv.setAdapter(adapter);
            }
        };

        ShowCardsRequest request = new ShowCardsRequest(userId, listener);
        RequestQueue queue = Volley.newRequestQueue(CardShareModifyActivity.this);
        queue.add(request);

    }

}
