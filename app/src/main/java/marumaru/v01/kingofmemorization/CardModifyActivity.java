package marumaru.v01.kingofmemorization;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import marumaru.v01.kingofmemorization.Request.GetCardItemsRequest;
import marumaru.v01.kingofmemorization.Request.GetCurrentCardItemsRequest;
import marumaru.v01.kingofmemorization.Request.ModifyCardRequest;
import marumaru.v01.kingofmemorization.Request.RegisterCardRequest;
import marumaru.v01.kingofmemorization.domain.Card_Item;


public class CardModifyActivity extends AppCompatActivity {

    private LinearLayout ll_card_items;
    private Button btn_add, btn_reset, btn_register, btn_cancel;
    private EditText et_title, et_key, et_value;
    private TextView tv_remove;
    private AlertDialog dialog;

    private static int id_num = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cardregsiter);

        id_num = 0;

        ll_card_items = findViewById(R.id.ll_card_items);
        btn_add = findViewById(R.id.btn_add);
        btn_reset = findViewById(R.id.btn_reset);
        tv_remove = findViewById(R.id.tv_remove);
        et_title = findViewById(R.id.et_title);
        et_key = findViewById(R.id.et_key);
        et_value = findViewById(R.id.et_value);
        btn_register = findViewById(R.id.btn_register);
        btn_cancel = findViewById(R.id.btn_cancel);

        // 기존에 있는 내용 받아와서 뿌리기
        final Long current_cno = getIntent().getLongExtra("cno", -1);
        String current_title = getIntent().getStringExtra("title");
        Log.d("current_cno: ", current_cno+"");
        Log.d("current_title: ", current_title);

        // Volley 로 해당 cno 의 title, item_key, item_value 받아오기 (기존 GetCardItemRequest 사용)
        if(current_title != null || !current_title.equals(""))
            et_title.setText(current_title);

        if(current_cno != -1){

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Log.d("item_key", jsonObject.getString("item_key"));
                            Log.d("item_value", jsonObject.getString("item_value"));

                            // UI 로 append
                            appendCardItem(jsonObject.getString("item_key"), jsonObject.getString("item_value"));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            GetCurrentCardItemsRequest request = new GetCurrentCardItemsRequest(current_cno, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);
        }


        // key-value item add button clicked!
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String key = et_key.getText().toString();
                String value = et_value.getText().toString();

                if(key == null || value == null || key.equals("") || value.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(CardModifyActivity.this);
                    dialog = builder.setMessage("빠진 내용이 있습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();

                    return;
                }

                appendCardItem(key, value);

                // 초기화
                et_key.setText("");
                et_value.setText("");

            }
        });

        // reset button clicked!
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 초기화
                et_key.setText("");
                et_value.setText("");
            }
        });

        // register button clicked!
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DB 에 title, cardList 보내서 insert
                // AysncTask 와 Volley 로 StringRequest, ResponseListener 작성하여 처리
                // 데이터를 보낼 때 Map<String, String> 타입으로 보내야 하기 때문에 json (=String) 을 보낸다.
                // ex) {"title":"TOEIC", "userID":"maru", "items":[{"item_key":"like", "item_value":"좋아하다"}, ...]}
                String title = et_title.getText().toString();

                if(title == null || title.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(CardModifyActivity.this);
                    dialog = builder.setMessage("제목을 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();

                    return;
                }

                TextView tv_key = null;
                TextView tv_value = null;

                JSONObject object = new JSONObject();
                JSONArray jsonArray = new JSONArray();

                try {
                    object.put("title", title);
                    object.put("userID", "maru");
                    object.put("cno", current_cno);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // DYNAMIC + 1 ~ id_num 까지 확인하면서 JSONObject 만들어서 JSONArray 에 넣기
                for(int i= 1; i <= id_num; i++){
                    Log.d("i", i+" 번째");

                    View item = ll_card_items.findViewWithTag("item" + i);

                    if(item == null)
                        continue;

                    tv_key = item.findViewById(R.id.tv_key);
                    tv_value = item.findViewById(R.id.tv_value);

                    if(tv_key == null || tv_value == null)
                        continue;

                    JSONObject object_item = new JSONObject();

                    Log.d("item_key", tv_key.getText().toString());
                    Log.d("item_value", tv_value.getText().toString());

                    try {
                        object_item.put("item_key", tv_key.getText().toString());
                        object_item.put("item_value", tv_value.getText().toString());
                        jsonArray.put(object_item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    object.put("items", jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("test", jsonArray.toString());
                Log.d("test2", object.toString());

                Response.Listener<String> responseListener =new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //JSONObject reponse = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), "수정 완료", Toast.LENGTH_SHORT).show();

                            finish(); // 근데 이렇게 끝내고 돌아가면 이전 액티비티에서 onCreate 를 다시 하지 않네
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };

                ModifyCardRequest modifyCardRequest = new ModifyCardRequest(object.toString(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(CardModifyActivity.this);
                queue.add(modifyCardRequest);

                //finish();
            }
        });

        // cancel button clicked!
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 뒤로 보내기
                finish();
            }
        });

    }

    // card item 을 View 로 만들어 추가
    private void appendCardItem(String key, String value) {

        id_num++;

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view_card = inflater.inflate(R.layout.card_item, ll_card_items, false);
        view_card.setTag("item" + id_num);

        TextView tv_key = view_card.findViewById(R.id.tv_key);
        tv_key.setText(key);
        tv_key.setTag("tv_key" + id_num);
        TextView tv_value = view_card.findViewById(R.id.tv_value);
        tv_value.setText(value);
        tv_value.setTag("tv_value" + id_num);

        tv_remove = view_card.findViewById(R.id.tv_remove);

        // key-value item remove button clicked!
        tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View view_item_remove = (View) view.getParent();
                View view_item = (View) view_item_remove.getParent();

                ll_card_items.removeView(view_item);

                Toast.makeText(getApplicationContext(), "삭제 완료", Toast.LENGTH_SHORT).show();
            }
        });

        ll_card_items.addView(view_card);
    }
}
