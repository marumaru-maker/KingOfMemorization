package marumaru.v01.kingofmemorization;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardShareRegisterVH extends RecyclerView.ViewHolder {

    RelativeLayout rl_share_register_card;
    TextView tv_share_register_card_date, tv_share_register_card_title;
    ToggleButton tb_share_register_card;

    public CardShareRegisterVH(@NonNull View itemView) {
        super(itemView);

        rl_share_register_card = itemView.findViewById(R.id.rl_share_register_card);
        tv_share_register_card_title = itemView.findViewById(R.id.tv_share_register_card_title);
        tv_share_register_card_date = itemView.findViewById(R.id.tv_share_register_card_date);
        tb_share_register_card = itemView.findViewById(R.id.tb_share_register_card);

    }

}
