package marumaru.v01.kingofmemorization;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import marumaru.v01.kingofmemorization.Request.LoginRequest;
import marumaru.v01.kingofmemorization.domain.User;


public class LoginActivity extends AppCompatActivity {

    AlertDialog.Builder alertBuilder;
    AlertDialog dialog;

    EditText et_userId, et_userPw;
    Button btn_login;
    TextView tv_join;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar_login);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        et_userId = findViewById(R.id.et_userId_login);
        et_userPw = findViewById(R.id.et_userPw_login);
        btn_login = findViewById(R.id.btn_login);
        tv_join = findViewById(R.id.tv_join_login);

        alertBuilder = new AlertDialog.Builder(LoginActivity.this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userId = et_userId.getText().toString();
                String userPw = et_userPw.getText().toString();

                // 입력 값이 양식에 맞지 않을 경우 Alert
                if(userId == null || userId.equals("") || userPw == null || userPw.equals("")){
                    if(userPw.length() < 10 || userId.length() < 6) {

                        dialog = alertBuilder.setMessage("잘못된 입력입니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }
                }

                User user = new User(userId, userPw, null);

                // LoginRequest 에서 해당 입력의 값이 있는지 확인하여 리턴하면 맞는 경우 로그인 아니면 Alert

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);

                        if(response.equals("success")){
                            UserSharedPreference.setUserId(userId, LoginActivity.this);

                            dialog = alertBuilder.setMessage("로그인에 성공하였습니다.")
                                    .setPositiveButton("확인", null)
                                    .create();
                            dialog.show();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            dialog = alertBuilder.setMessage("아이디 혹은 비밀번호가 일치하지 않습니다.")
                                    .setPositiveButton("확인", null)
                                    .create();
                            dialog.show();
                            return;
                        }
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(new LoginRequest(user, responseListener));

            }
        });

        // 회원가입 화면으로 보내기
        tv_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_login, menu);

        return true;
    }
}
