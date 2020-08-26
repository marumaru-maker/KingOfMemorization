package marumaru.v01.kingofmemorization;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import marumaru.v01.kingofmemorization.domain.Card_Item;

public class ShareGetAdapter extends RecyclerView.Adapter<CardShareGetVH> {

    Context mContext;
    ArrayList<Card_Item> lists;

    public ShareGetAdapter (Context context, ArrayList<Card_Item> lists){
        mContext = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public CardShareGetVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //View view = View.inflate(mContext, R.layout.card_share_get_adapter, null);

        View view = LayoutInflater.from(mContext).inflate(R.layout.card_share_get_adapter, parent, false);

        return new CardShareGetVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardShareGetVH holder, int position) {
        Card_Item item = lists.get(position);

        holder.tv_share_get_key.setText(item.getItem_key());
        holder.tv_share_get_value.setText(item.getItem_value());

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
