package test.bwie.com.shejiaoapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.bean.DataBean;
import test.bwie.com.shejiaoapp.bean.IndexBean;

/**
 * date: 2017/7/14
 * author:陈茹
 * 类的用途:
 */

public class Mylistadapter extends BaseAdapter {

    private Context context;
    private List<DataBean> data;
    private ImageView indexfragment_face_id;
    private TextView indexfragment_nickname;
    private TextView indexfragment_agesex;
    private TextView indexfragment_des;


    public Mylistadapter (Context context,List<DataBean> data){
        this.context=context;
        this.data=data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){

            convertView=View.inflate(context,R.layout.second_item,null);
            indexfragment_face_id = (ImageView) convertView.findViewById(R.id.indexfragment_face_id);
            indexfragment_nickname = (TextView) convertView.findViewById(R.id.indexfragment_nickname_id);
             indexfragment_agesex =   (TextView) convertView.findViewById(R.id.indexfragment_agesex_id);
            indexfragment_des = (TextView) convertView.findViewById(R.id.indexfragment_des_id);

            Glide.with(context).load(data.get(position).getImagePath()).into(indexfragment_face_id);
            indexfragment_nickname.setText(data.get(position).getNickname());
            indexfragment_agesex.setText(data.get(position).getAge());
            indexfragment_des.setText(data.get(position).getIntroduce());

        }
        return convertView;
    }
}
