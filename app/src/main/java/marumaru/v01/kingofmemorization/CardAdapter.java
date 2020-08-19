package marumaru.v01.kingofmemorization;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import marumaru.v01.kingofmemorization.domain.CardPost;

public class CardAdapter extends RecyclerView.Adapter<MyCardListVH> {

    Context mContext;
    ArrayList<CardPost> card_lists;
    AlertDialog dialog;
    AlertDialog.Builder dialog_builder;

    public CardAdapter(Context mContext, ArrayList<CardPost> card_lists) {
        this.mContext = mContext;
        this.card_lists = card_lists;
    }

    @NonNull
    @Override
    public MyCardListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v_card = View.inflate(mContext, R.layout.my_card, null);
        MyCardListVH vh = new MyCardListVH(v_card, mContext, this);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCardListVH holder, final int position) {

        final CardPost post = card_lists.get(position);

        if(position % 2  == 0)
            holder.rl_case.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen));

        holder.tv_title.setText(post.getTitle());
        holder.tv_date.setText(post.getReg_date());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "itemView Clicked" , Toast.LENGTH_SHORT).show();
                Log.d("card", "Clicked!!1" );
                Intent intent = new Intent(mContext, CardActivity.class);
                intent.putExtra("cno", post.getCno());
                mContext.startActivity(intent);
            }
        });

        holder.tv_remove.setOnClickListener(new View.OnClickListener() {

            class CardPostRemove extends AsyncTask<Void, Void, String> {

                String target;
                Long cno;
                View view;

                public CardPostRemove(Long cno, View view) {
                    this.cno = cno;
                    target = "http://rkskekahdid.cafe24.com/CardPostRemove.php?cno=" + cno;
                    this.view = view;

                    if(view == null)
                        Log.d("view", "null");
                }

                @Override
                protected String doInBackground(Void... voids) {

                    try {
                        URL url = new URL(target);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        InputStream is = connection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));

                        String result = br.readLine();

                        br.close();
                        is.close();
                        connection.disconnect();

                        if(result != null)
                            return result;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {

                    super.onPostExecute(result);

                    if(result != null && result.equals("Success")){
                        // 뷰에서 삭제하고 성공 메시지 띄우기

                        // 이런 식으로 삭제하면 안 된다. 뷰에 변경 없음.
                        //View view_parent = (View) view.getParent().getParent();
                        //rv_cardList.removeView(view_parent);

                        card_lists.remove(position);    // 어댑터를 만들면서 넣은 List 값에서 삭제된 데이터까지 없애줘야 한다. 그렇지 않으면 ViewHolder 가 notifyItem... 할 때 다시 사라졌던 내용을 밑에 추가해 버린다.

                        notifyItemRemoved(position);    // 이걸 해줘야 List에 변경이 있는 걸 캐치하고 뷰에도 변화를 준다.
                        notifyItemRangeChanged(position, getItemCount());   // 이걸 하지 않고 notifyRemoved 만 하면 위치 변경에 따른 색 변경이 되지 않는다.

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        dialog = builder.setMessage("성공적으로 삭제했습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;

                    } else {
                        // 에러 메세지 띄우기
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        dialog = builder.setMessage("삭제할 수 없습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }
                }
            }

            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "tv_remove Clicked!", Toast.LENGTH_SHORT).show();

                final View mView = view;

                dialog_builder = new AlertDialog.Builder(mContext);
                dialog = dialog_builder.setMessage("암기카드를 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // DB 에서 삭제
                                new CardPostRemove(post.getCno(), mView).execute();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .create();
                dialog.show();
                return;
            }
        });

        holder.tv_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 등록창처럼 이동하여 뿌려준다.
                Intent intent = new Intent(mContext, CardModifyActivity.class);
                intent.putExtra("cno", post.getCno());
                intent.putExtra("title", post.getTitle());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return card_lists.size();
    }

}
