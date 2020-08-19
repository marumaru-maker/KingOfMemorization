package marumaru.v01.kingofmemorization;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import marumaru.v01.kingofmemorization.Request.GetCardItemsRequest;
import marumaru.v01.kingofmemorization.domain.Card_Item;

public class CardActivity extends AppCompatActivity {

    private Boolean flag_sw_key_value;
    private ViewPager2 vp2_card_items;
    private RelativeLayout rl_item;
    private TextView tv_item_top, tv_item_bottom;
    private Switch sw_key_value;

    private ArrayList<Card_Item> list_card_items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card);

        vp2_card_items = findViewById(R.id.vp2_card_item);

        //vp2_card_items.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)); match_parent 하라고 해서 이렇게 했지만 통하지 않는다. inflater.inflate 를 안 해서 그렇다. (View.inflate를 사용함)

        list_card_items = new ArrayList<>();

        vp2_card_items.setAdapter(new ViewPager2CardItemAdapter(list_card_items, getApplicationContext(), flag_sw_key_value));

        flag_sw_key_value = false;

        Intent intent = getIntent();

        Long cno = intent.getExtras().getLong("cno");

        sendRequest(cno);

    }

    private void sendRequest(Long cno) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    list_card_items = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Card_Item item = new Card_Item();

                        item.setIno(jsonObject.getLong("ino"));
                        item.setItem_key(jsonObject.getString("item_key"));
                        item.setItem_value(jsonObject.getString("item_value"));

                        Log.d("item_key", jsonObject.getString("item_key"));
                        Log.d("item_value", jsonObject.getString("item_value"));

                        list_card_items.add(item);
                    }

                    final ViewPager2CardItemAdapter cardItemAdapter = new ViewPager2CardItemAdapter(list_card_items, getApplicationContext(), flag_sw_key_value);

                    vp2_card_items.setAdapter(cardItemAdapter);

                    /*

                    vp2_card_items.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                            //Toast.makeText(CardActivity.this, "onPageScrolled", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);


                            cardItemAdapter.notifyDataSetChanged();

                            Toast.makeText(CardActivity.this, "onPageSelected:" + position, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
                            super.onPageScrollStateChanged(state);

                            //Toast.makeText(CardActivity.this, "onPageScrollStateChanged", Toast.LENGTH_SHORT).show();
                        }
                    });
                    */


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetCardItemsRequest request = new GetCardItemsRequest(cno, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

}

