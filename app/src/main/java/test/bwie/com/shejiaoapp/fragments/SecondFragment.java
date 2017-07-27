package test.bwie.com.shejiaoapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.activity.CChativity;
import test.bwie.com.shejiaoapp.activity.FanhuiActivity;
import test.bwie.com.shejiaoapp.adapter.Mylistadapter;
import test.bwie.com.shejiaoapp.base.BaseMvpFragment;
import test.bwie.com.shejiaoapp.bean.DataBean;
import test.bwie.com.shejiaoapp.bean.IndexBean;
import test.bwie.com.shejiaoapp.mview.FirstFragmentView;
import test.bwie.com.shejiaoapp.presenter.Seconfragmentpresent;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends BaseMvpFragment<FirstFragmentView,Seconfragmentpresent> implements FirstFragmentView {


    private View view;
    private ListView listview_id;
    private List<DataBean> data;
    private Mylistadapter mylistadapter;
    private SwipeRefreshLayout springview_indexfragment_id;

    @Override
    public Seconfragmentpresent initPresenter() {
        return new Seconfragmentpresent();
    }

    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_second, container, false);

        initview();


        return view;
    }

    private void data() {

        springview_indexfragment_id.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                presenter.getDataba();
                springview_indexfragment_id .setRefreshing(false);     //  false:设置为false是不要一直刷新

            }
        });
    }

    private void initview() {

        listview_id = (ListView) view.findViewById(R.id.listview_id_);
        springview_indexfragment_id = (SwipeRefreshLayout) view.findViewById(R.id.springview_indexfragment_id);

        presenter.getDataba();

        data();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences preferences = getActivity().getSharedPreferences("isfrit", Context.MODE_PRIVATE);
        boolean frit = preferences.getBoolean("frit", false);
        if (frit){
            presenter.getDataba();
        }else {
            startActivity(new Intent(getActivity(), FanhuiActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void success(IndexBean indexBean) {
        data = indexBean.getData();
        mylistadapter = new Mylistadapter(getActivity(),data);

        listview_id.setAdapter(mylistadapter);

        //点击联系人聊天
        listview_id.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), CChativity.class));
            }
        });

    }

    @Override
    public void failed(int code) {

    }


}
