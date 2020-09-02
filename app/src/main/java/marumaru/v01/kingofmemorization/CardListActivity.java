package marumaru.v01.kingofmemorization;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

import marumaru.v01.kingofmemorization.Request.ShowCardsRequest;
import marumaru.v01.kingofmemorization.domain.CardPost;

public class CardListActivity extends AppCompatActivity {

    RecyclerView rv_cardList;
    ArrayList<CardPost> card_lists;
    //SearchView sv_cardList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardlist);

        //sv_cardList = findViewById(R.id.sv_cardList);
        rv_cardList = findViewById(R.id.rv_cardList);
        card_lists =  new ArrayList<>();

        // 툴바 등록
        Toolbar toolbar = findViewById(R.id.toolbar_cardList);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
        getSupportActionBar().setTitle(""); // 프로젝트 이름 나오는 거 공백 처리

        // 리사이클러뷰
        CardAdapter cardAdapter = new CardAdapter(CardListActivity.this, card_lists); // Response 리스너에서만 넣고 싶은데 지금 여기서 빈 어댑터를 set하지 않으면 에러 뜬다.
        rv_cardList.setAdapter(cardAdapter);
        rv_cardList.setLayoutManager(new LinearLayoutManager(this));

        sendRequest();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cardlist, menu);

        MenuItem menu_home = menu.findItem(R.id.action_home);
        MenuItem menu_register = menu.findItem(R.id.action_register);
        MenuItem menu_search = menu.findItem(R.id.action_search);
        MenuItem menu_logout = menu.findItem(R.id.action_logout);
        SearchView sv_search = (SearchView) menu_search.getActionView();

        // 홈버튼 클릭 리스너
        menu_home.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                //Toast.makeText(getApplicationContext(), "Home Clicked", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                return true;
            }
        });

        // 로그아웃 버튼 클릭 리스너
        menu_logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                UserSharedPreference.removeUserId(CardListActivity.this);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            }
        });

        // 암기카드 등록 버튼 리스너
        menu_register.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent = new Intent(getApplicationContext(), CardRegsiterActivity.class);
                startActivity(intent);

                return true;
            }
        });

        // 서치뷰 검색어 입력 리스너
        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.d("sv_cardList", "onQueryTextChange: " + newText);

                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        sendRequest();
    }

    private void sendRequest() {

        // StringRequest ShowCardsRequest 를 작성하여 param 으로 userID 전달 - ResponseListener 에서 받은 Card 정보를 View 로 만들어 append - View 에는 리스너를 달아 눌렀을 때 액티비티 이동되게 할 것
        String userID = UserSharedPreference.getUserId(getApplicationContext()); // user 등록 부분 만들고 수정 (세션 관리 등 학습 필요)

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // ArrayList<CardPost> 받아오기
                card_lists = new ArrayList<>();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = (JSONObject) jsonArray.get(i);

                        CardPost post = new CardPost();
                        post.setReg_date(object.getString("reg_date"));
                        post.setTitle(object.getString("title"));
                        post.setCno(object.getLong("cno"));
                        card_lists.add(post);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CardAdapter cardAdapter = new CardAdapter(CardListActivity.this, card_lists);
                rv_cardList.setAdapter(cardAdapter);
            }
        };

        ShowCardsRequest request = new ShowCardsRequest(userID, listener);
        RequestQueue queue = Volley.newRequestQueue(CardListActivity.this);
        queue.add(request);
    }
}
