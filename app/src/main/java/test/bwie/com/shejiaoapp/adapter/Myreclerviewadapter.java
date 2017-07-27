package test.bwie.com.shejiaoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.List;

import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.bean.UserBean;

/**
 * date: 2017/5/27
 * author:陈茹
 * 类的用途:
 */

public class Myreclerviewadapter extends RecyclerView.Adapter<Myreclerviewadapter.Myviewholder> {
    private Context context;
    List<UserBean> others;

    public Myreclerviewadapter(Context context,  List<UserBean> others) {
        this.context = context;
        this.others = others;
    }

    @Override
    public Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
       View vh= LayoutInflater.from(context).inflate(R.layout.item_recler,
                null);
        Myviewholder root = new Myviewholder(vh);

        return root;
    }

    @Override
    public void onBindViewHolder(Myviewholder holder, int position) {

        holder.text1.setText(others.get(position).getName());
        holder.text2.setText(others.get(position).getZiliao());
    }

    @Override
    public int getItemCount() {
        return others.size();
    }

    class Myviewholder extends RecyclerView.ViewHolder{
         TextView text1;
         TextView text2;

        public Myviewholder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.textview_name);
            text2 = (TextView) itemView.findViewById(R.id.text2);

        }
    }

}
