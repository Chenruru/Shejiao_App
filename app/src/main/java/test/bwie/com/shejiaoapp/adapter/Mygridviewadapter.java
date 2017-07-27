package test.bwie.com.shejiaoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.bean.UserBean;

/**
 * date: 2017/7/14
 * author:陈茹
 * 类的用途:Mygridviewadapter
 */


public class Mygridviewadapter extends RecyclerView.Adapter<Mygridviewadapter.viewholder> {
    private Context context;
    List<String> list;

    public Mygridviewadapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vh = LayoutInflater.from(context).inflate(R.layout.grid_item,
                null);
        viewholder root = new viewholder(vh);

        return root;
    }

    @Override
    public void onBindViewHolder(viewholder holder, int position) {
        holder.text1.setText(list.get(position));
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView text1;


        public viewholder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.text_ziliao);


        }
    }
}