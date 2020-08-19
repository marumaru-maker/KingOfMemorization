package marumaru.v01.kingofmemorization;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import marumaru.v01.kingofmemorization.Request.JoinRequest;
import marumaru.v01.kingofmemorization.Request.ValidateRequest;
import marumaru.v01.kingofmemorization.domain.User;

public class JoinActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText et_userId, et_userPw, et_userEmail;
    Button btn_userId_validate, btn_join;

    AlertDialog.Builder alertBuilder;
    AlertDialog dialog;

    Boolean validation_id = false;
    String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join);

        // 툴바
        toolbar = findViewById(R.id.toolbar_join);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //
        et_userId = findViewById(R.id.et_userId_join);
        et_userPw = findViewById(R.id.et_userPw_join);
        et_userEmail = findViewById(R.id.et_userEmail_join);
        btn_userId_validate = findViewById(R.id.btn_userId_validate);
        btn_join = findViewById(R.id.btn_join);

        et_userId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validation_id = false;
                userId = "";
                btn_userId_validate.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                return;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                return;
            }
        }); // 아이디 입력된 것이 바뀌면 아이디 중복확인 필요

        btn_userId_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {         // 아이디 중복확인

                final String temp_userId = et_userId.getText().toString();

                if(temp_userId.length() < 6) {
                     alertBuilder = new AlertDialog.Builder(JoinActivity.this);
                     dialog = alertBuilder.setMessage("양식에 맞지 않는 아이디입니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("possible")){
                            validation_id = true; // 가능하다는 응답을 받으면 flag 값을 true 로 불가능하면 false
                            userId = temp_userId;
                            btn_userId_validate.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                            alertBuilder = new AlertDialog.Builder(JoinActivity.this);
                            dialog = alertBuilder.setMessage("사용 가능한 아이디입니다.")
                                    .setPositiveButton("확인", null)
                                    .create();
                            dialog.show();
                            return;
                        }
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                ValidateRequest validateRequest = new ValidateRequest(temp_userId, responseListener);
                queue.add(validateRequest);
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userPw = et_userPw.getText().toString();
                String userEmail = et_userEmail.getText().toString();

                alertBuilder = new AlertDialog.Builder(JoinActivity.this);

                // 아이디 중복확인 Alert
                if(!validation_id){
                    dialog = alertBuilder.setMessage("아이디 중복확인이 필요합니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                // 입력 값이 양식에 맞지 않을 경우 Alert
                if(userId == null || userId.equals("") || userPw == null || userPw.equals("") || userEmail == null || userEmail.equals("")){
                    if(userPw.length() < 10 || userEmail.length() < 10) {

                        dialog = alertBuilder.setMessage("빠진 부분 없이 양식에 맞게 작성해주세요.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }
                }

                User user = new User(userId, userPw, userEmail);

                // 회원가입 서버 처리
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("info_type", "success");
                            intent.putExtra("info_content", "회원가입에 성공하였습니다.");
                            startActivity(intent);
                        }
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                JoinRequest joinRequest = new JoinRequest(user, responseListener);
                queue.add(joinRequest);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main_login, menu);

        return true;
    }
}
