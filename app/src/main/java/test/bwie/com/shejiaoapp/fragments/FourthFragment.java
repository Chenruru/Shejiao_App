package test.bwie.com.shejiaoapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.activity.FanhuiActivity;
import test.bwie.com.shejiaoapp.activity.ZiliaoActivity;
import test.bwie.com.shejiaoapp.adapter.Mygridviewadapter;
import test.bwie.com.shejiaoapp.utils.GridDivider;


/**
 * A simple {@link Fragment} subclass.
 */
public class FourthFragment extends Fragment implements View.OnClickListener{


    private RecyclerView gridivew_id;
    private List<String> list = new ArrayList<>();;
    private TextView textview_1;
    private TextView textview_2;
    private LinearLayoutManager mManagerColor;
    public FourthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_fourth, container, false);
        gridivew_id = (RecyclerView) view.findViewById(R.id.gridivew_id);

        textview_1 = (TextView) view.findViewById(R.id.textview_1);
        textview_2 = (TextView) view.findViewById(R.id.textview_2);
        textview_1.setOnClickListener(this);
        textview_2.setOnClickListener(this);

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences preferences = getActivity().getSharedPreferences("isfrit", Context.MODE_PRIVATE);
        boolean frit = preferences.getBoolean("frit", false);
        if (frit){
            data();
        }else {
            startActivity(new Intent(getActivity(), FanhuiActivity.class));
            getActivity().finish();
        }
    }
    private void data() {
        String [] name={"我的资料","个性资料","诚信等级","我的相册","征友条件","谁看过我"
                ,"设置","通知广播","恋爱无忧","帮助&反馈","咕咪有缘悦读会","咕咪音乐会员专区"
        };

        for (int i = 0; i <12 ; i++) {

            list.add(name[i]+"");
        }
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);//设置为一个3列的纵向网格布局
        gridivew_id.setLayoutManager(mLayoutManager);

        Mygridviewadapter mygridviewadapter=new Mygridviewadapter(getActivity(),list);
        gridivew_id.setAdapter(mygridviewadapter);
        gridivew_id.addItemDecoration(new GridDivider(getActivity(), 20, this.getResources().getColor(R.color.color)));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textview_1:

                break;
            case R.id.textview_2:

                break;
        }
    }
}
