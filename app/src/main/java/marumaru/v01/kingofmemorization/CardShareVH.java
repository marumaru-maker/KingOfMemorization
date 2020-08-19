package marumaru.v01.kingofmemorization;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class CardShareVH extends RecyclerView.ViewHolder {

    LinearLayout ll_share_card;
    ImageButton ib_share_import;
    TextView tv_share_title, tv_share_category, tv_share_star, tv_share_regdate;

    public CardShareVH(@NonNull View itemView) {
        super(itemView);

        ll_share_card = itemView.findViewById(R.id.ll_share_card);
        ib_share_import = itemView.findViewById(R.id.ib_share_import);
        tv_share_title = itemView.findViewById(R.id.tv_share_title);
        tv_share_category = itemView.findViewById(R.id.tv_share_category);
        tv_share_star = itemView.findViewById(R.id.tv_share_star);
        tv_share_regdate = itemView.findViewById(R.id.tv_share_regdate);





    }
}
