package marumaru.v01.kingofmemorization;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import marumaru.v01.kingofmemorization.domain.Card_Item;

public class ViewPager2CardItemAdapter extends RecyclerView.Adapter<CardItemVH> {

    private Boolean flag_sw_key_value;

    private ArrayList<Card_Item> list_card_items;
    private Context mContext;

    public ViewPager2CardItemAdapter(ArrayList<Card_Item> list_card_items, Context context, Boolean flag_sw_key_value) {

        this.mContext = context;
        this.list_card_items = list_card_items;

        this.flag_sw_key_value = flag_sw_key_value;
    }

    @NonNull
    @Override
    public CardItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.vp2_card_item, parent, false);

        //View view = View.inflate(mContext, R.layout.vp2_card_item, null); // 이렇게 View.inflate 하면 안 되고 위에 처럼 Inflater.inflate 해야 한다. 차이는 parent layout parameter 로 초기화를 한다는 점.

        return new CardItemVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardItemVH holder, int position) {

        Card_Item card_item = list_card_items.get(position);

        holder.tv_item_top.setText(card_item.getItem_key());
        holder.tv_item_bottom.setText(card_item.getItem_value());

        holder.rl_item.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!flag_sw_key_value){ // 스위치가 꺼져 있어야 작동

                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN :
                            holder.tv_item_bottom.setVisibility(View.VISIBLE);
                            //Toast.makeText(getApplicationContext(), "DOWN", Toast.LENGTH_SHORT).show();
                            break;

                        case MotionEvent.ACTION_UP :    // ACTION_UP 할려면 대상에 clickable true 를 해줘야 한다.
                        case MotionEvent.ACTION_CANCEL :
                            holder.tv_item_bottom.setVisibility(View.INVISIBLE);
                            //Toast.makeText(getApplicationContext(), "UP", Toast.LENGTH_SHORT).show();
                            break;

                        default:
                            break;
                    }
                }

                return true;
            }
        });

        // 스위치가 켜져 있으면 다 보인다 스크린 터치 이벤트가 생략
        holder.sw_key_value.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    flag_sw_key_value = true;
                    holder.tv_item_bottom.setVisibility(View.VISIBLE);
                } else {
                    flag_sw_key_value = false;
                    holder.tv_item_bottom.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_card_items.size();
    }

}
