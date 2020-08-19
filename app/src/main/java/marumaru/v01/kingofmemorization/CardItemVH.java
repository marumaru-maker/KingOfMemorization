package marumaru.v01.kingofmemorization;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardItemVH extends RecyclerView.ViewHolder {

    View itemView;
    RelativeLayout rl_item;
    TextView tv_item_top, tv_item_bottom;
    Switch sw_key_value;

    public CardItemVH(@NonNull View itemView) {
        super(itemView);

        this.itemView = itemView;

        rl_item = itemView.findViewById(R.id.rl_item);
        tv_item_top = itemView.findViewById(R.id.tv_item_top);
        tv_item_bottom = itemView.findViewById(R.id.tv_item_bottom);
        sw_key_value = itemView.findViewById(R.id.sw_key_value);
    }


}
