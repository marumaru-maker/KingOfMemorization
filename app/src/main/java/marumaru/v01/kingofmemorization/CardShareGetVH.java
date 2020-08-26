package marumaru.v01.kingofmemorization;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardShareGetVH extends RecyclerView.ViewHolder {

    public TextView tv_share_get_key;
    public TextView tv_share_get_value;

    public CardShareGetVH(@NonNull View itemView) {
        super(itemView);

        tv_share_get_key = itemView.findViewById(R.id.tv_share_get_key);
        tv_share_get_value = itemView.findViewById(R.id.tv_share_get_value);

    }
}
