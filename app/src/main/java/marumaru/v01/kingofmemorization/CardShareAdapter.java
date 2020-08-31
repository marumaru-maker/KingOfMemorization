package marumaru.v01.kingofmemorization;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import marumaru.v01.kingofmemorization.Request.PutInMyCardListRequest;
import marumaru.v01.kingofmemorization.domain.Card;
import marumaru.v01.kingofmemorization.domain.CardPost;

public class CardShareAdapter extends RecyclerView.Adapter<CardShareVH> {

    Context mContext;
    ArrayList<CardPost> list_cards;
    CardPost card;
    String result;
    TextView tv_share_star;

    CardShareAdapter(Context context, ArrayList<CardPost> list_cards){
        mContext = context;
        this.list_cards = list_cards;
    }

    @NonNull
    @Override
    public CardShareVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = View.inflate(mContext, R.layout.post_card, null);

        return new CardShareVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardShareVH holder, int position) {

        card = list_cards.get(position);

        holder.tv_share_title.setText(card.getTitle());
        holder.tv_share_category.setText(card.getCategory());
        holder.tv_share_star.setText(card.getStar() + " 개");
        holder.tv_share_regdate.setText(card.getReg_date());
        holder.ib_share_import.setTag(card.getCnos());
        holder.rl_share_option.setTag(card.getSno());

        // 레이아웃 클릭 시 해당 카드 목록으로 이동 리스너
        holder.ll_share_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "ll_share_card Clicked", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, CardShareGetActivity.class);
                intent.putExtra("sno", card.getSno());
                intent.putExtra("cnos", card.getCnos()+"");
                intent.putExtra("reg_date", card.getReg_date());
                intent.putExtra("title", card.getTitle());
                intent.putExtra("num_star", card.getStar());
                intent.putExtra("category", card.getCategory());
                intent.putExtra("writer", card.getWriter());
                mContext.startActivity(intent);
            }
        });

        // 공유 받기 버튼 리스너
        holder.ib_share_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String e_cnos = (String) view.getTag();
                View view_parent = (View)view.getParent();
                String sno = view_parent.getTag()+"";

                //Toast.makeText(mContext, sno +"번 공유 게시물인 " + e_cnos + "를 나의 암기카드에 등록하였습니다.", Toast.LENGTH_SHORT).show();

                // sno 를 request 에 보내서 해당 번호 카드들을 나의 카드에 insert 한다;
                sendShareRequest(e_cnos, card.getTitle(), sno, view_parent);

            }
        });

    }

    private void sendShareRequest(String e_cnos, String title, String sno, View view_parent) {
        // 눌린 카드의 별 숫자 텍스트뷰
        tv_share_star = view_parent.findViewById(R.id.tv_share_star);

        // 발리로 큐 만들어서 리퀘스트 넣기
        RequestQueue queue = Volley.newRequestQueue(mContext);


        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response", response);

                // 삽입 완료 여부 띄우기
                if(response.equals("Success")){
                    Toast.makeText(mContext, "성공적으로 등록하였습니다.", Toast.LENGTH_SHORT).show();

                    // 뷰에 있는 별 숫자를 바로 갱신하는 방법 : 1. 리스트 값을 바꾼 후 어댑터에 데이터 바뀐거 알리기 2. 여기서 바로 setText로 바꾸기
                    int star_num = Integer.parseInt(tv_share_star.getText().toString().split(" ")[0])+1;
                    tv_share_star.setText(star_num +" 개");

                } else {
                    Toast.makeText(mContext, "처리하는데 문제가 있습니다.", Toast.LENGTH_SHORT).show();
                }
                result = response;
            }
        };

        queue.add(new PutInMyCardListRequest(e_cnos, UserSharedPreference.getUserId(mContext), title, sno, listener));
    }

    @Override
    public int getItemCount() {
        return list_cards.size();
    }
}
