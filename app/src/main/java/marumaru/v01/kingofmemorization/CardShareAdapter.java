package marumaru.v01.kingofmemorization;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import marumaru.v01.kingofmemorization.domain.Card;
import marumaru.v01.kingofmemorization.domain.CardPost;

public class CardShareAdapter extends RecyclerView.Adapter<CardShareVH> {

    Context mContext;
    ArrayList<CardPost> list_cards;

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

        CardPost card = list_cards.get(position);

        holder.tv_share_title.setText(card.getTitle());
        holder.tv_share_category.setText(card.getCategory());
        holder.tv_share_star.setText(card.getStar() + " 개");
        holder.tv_share_regdate.setText(card.getReg_date());

        // 레이아웃 클릭 시 해당 카드 목록으로 이동 리스너
        holder.ll_share_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "ll_share_card Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // 공유 받기 버튼 리스너
        holder.ib_share_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "ib_share_import Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_cards.size();
    }
}
