package marumaru.v01.kingofmemorization;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import marumaru.v01.kingofmemorization.domain.CardPost;

public class ShareRegisterAdapter extends RecyclerView.Adapter<CardShareRegisterVH> {

    Context mContext;
    ArrayList<CardPost> list_cardPost;

    public ShareRegisterAdapter(Context context, ArrayList<CardPost> list_cardPost){
        mContext = context;
        this.list_cardPost = list_cardPost;

    }

    @NonNull
    @Override
    public CardShareRegisterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = View.inflate(mContext, R.layout.card_share_register_list_adapter, null);

        CardShareRegisterVH vh = new CardShareRegisterVH(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CardShareRegisterVH holder, int position) {

        final CardPost cardPost = list_cardPost.get(position);

        holder.tv_share_register_card_title.setText(cardPost.getTitle());
        holder.tv_share_register_card_date.setText(cardPost.getReg_date());

        //holder.rl_share_register_card.setTag(cardPost.getCno());
        holder.tb_share_register_card.setTag(cardPost.getCno());

        holder.tb_share_register_card.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    holder.tb_share_register_card.setBackground(mContext.getDrawable(R.drawable.checked));

                } else {
                    holder.tb_share_register_card.setBackground(mContext.getDrawable(R.drawable.notchecked));
                }
            }
        });


    }

    @Override
    public int getItemCount() {

        return list_cardPost.size();
    }
}
