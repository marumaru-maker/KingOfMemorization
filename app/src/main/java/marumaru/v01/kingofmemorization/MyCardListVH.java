package marumaru.v01.kingofmemorization;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyCardListVH extends RecyclerView.ViewHolder {

    RelativeLayout rl_case;
    TextView tv_title, tv_date, tv_remove, tv_modify;
    Context mContext;
    View itemView;
    CardAdapter adapter;

    public MyCardListVH(@NonNull View itemView, final Context mContext, CardAdapter adapter) {
        super(itemView);

        this.mContext = mContext;
        this.itemView = itemView;
        this.adapter = adapter;

        rl_case = itemView.findViewById(R.id.rl_case);
        tv_title = itemView.findViewById(R.id.tv_title);
        tv_date = itemView.findViewById(R.id.tv_date);
        tv_remove = itemView.findViewById(R.id.tv_remove);
        tv_modify = itemView.findViewById(R.id.tv_modify);

    }

}
