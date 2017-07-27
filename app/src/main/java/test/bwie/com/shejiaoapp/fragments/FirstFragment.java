package test.bwie.com.shejiaoapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.activity.FanhuiActivity;
import test.bwie.com.shejiaoapp.activity.TabActivity;
import test.bwie.com.shejiaoapp.activity.ZiliaoActivity;
import test.bwie.com.shejiaoapp.adapter.IndexAdapter;
import test.bwie.com.shejiaoapp.base.BaseMvpFragment;
import test.bwie.com.shejiaoapp.base.IApplication;
import test.bwie.com.shejiaoapp.bean.DataBean;
import test.bwie.com.shejiaoapp.bean.IndexBean;
import test.bwie.com.shejiaoapp.bean.RegisterBean;
import test.bwie.com.shejiaoapp.core.JNICore;
import test.bwie.com.shejiaoapp.core.SortUtils;
import test.bwie.com.shejiaoapp.model.RegisterInforFragmentModel;
import test.bwie.com.shejiaoapp.mview.FirstFragmentView;
import test.bwie.com.shejiaoapp.network.BaseObserver;
import test.bwie.com.shejiaoapp.network.RetrofitManager;
import test.bwie.com.shejiaoapp.presenter.FirstFragmentPresenter;
import test.bwie.com.shejiaoapp.utils.PreferencesUtils;

/**
 *
 *  瀑布流 和列表 显示 距离 按照 墨迹天际 ui实现
 *  数据持久化到本地  采用 greendao
 *  服务器返回的数据 存储到本地数据库，第一次启动的时候， 先从本地数据库读取显示，服务器返回数据，显示最新数据
 *
 * A simple {@link Fragment} subclass.
 *
 * 第一个fragment
 */

public  class FirstFragment extends BaseMvpFragment<FirstFragmentView, FirstFragmentPresenter> implements FirstFragmentView {
   private TabActivity activity ;
    IndexAdapter adapter;
    @BindView(R.id.pub_title_leftbtn)
    Button pubTitleLeftbtn;
    @BindView(R.id.pub_title_rightbtn)
    Button pubTitleRightbtn;
    @BindView(R.id.recycleview_indexfragment)
    RecyclerView recycleviewIndexfragment;
    @BindView(R.id.springview_indexfragment)
    SpringView springviewIndexfragment;
    Unbinder unbinder;
    View view;
    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
   private HorizontalDividerItemDecoration horizontalDividerItemDecoration;
    int page = 1 ;
    List<DataBean> list;
    private ImageView image_id_fan;

    @Override
    public FirstFragmentPresenter initPresenter() {
        return new FirstFragmentPresenter();
    }

    public FirstFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       activity = (TabActivity) getActivity() ;
        View view =  inflater.inflate(R.layout.fragment_first, container, false);
        image_id_fan = (ImageView) view.findViewById(R.id.image_id_fan);
        unbinder = ButterKnife.bind(this, view);
         initView(view);

        return view ;
    }



    //    pubTitleRightbtn.tag
//     1 切换成 瀑布流 2 切换成 线性布局
    private void initView(View view) {

        pubTitleLeftbtn.setVisibility(View.GONE);
        pubTitleRightbtn.setVisibility(View.VISIBLE);
        pubTitleRightbtn.setTag(1);
        pubTitleRightbtn.setText("切换模式");

        pubTitleRightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag() ;
                if(tag == 1){
                    pubTitleRightbtn.setTag(2);
                    toStaggeredGridLayoutManager();


                } else {
                    pubTitleRightbtn.setTag(1);
                    toLinearLayoutManager();
                }
            }
        });
        horizontalDividerItemDecoration = new HorizontalDividerItemDecoration.Builder(getActivity()).build();
        adapter = new IndexAdapter(getActivity(),list);
        toLinearLayoutManager();

          presenter.getData();

//


        //点击返回到登录界面
        image_id_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),FanhuiActivity.class));
            }
        });


    }

    public void toLinearLayoutManager(){
        if(linearLayoutManager == null){
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        adapter.dataChange(1);
        recycleviewIndexfragment.setLayoutManager(linearLayoutManager);
        recycleviewIndexfragment.setAdapter(adapter);
        recycleviewIndexfragment.addItemDecoration(horizontalDividerItemDecoration);

    }

    public void toStaggeredGridLayoutManager(){
        if(staggeredGridLayoutManager == null){
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        }
        adapter.dataChange(2);
        recycleviewIndexfragment.setLayoutManager(staggeredGridLayoutManager);
        recycleviewIndexfragment.setAdapter(adapter);
        recycleviewIndexfragment.removeItemDecoration(horizontalDividerItemDecoration);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences preferences = getActivity().getSharedPreferences("isfrit", Context.MODE_PRIVATE);
        boolean frit = preferences.getBoolean("frit", false);
        if (frit){
            presenter.getData();

        }else {
            startActivity(new Intent(getActivity(), FanhuiActivity.class));
            getActivity().finish();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void success(final IndexBean indexBean) {
        adapter.setData(indexBean);


        springviewIndexfragment.setHeader(new AliHeader(getActivity()));
        springviewIndexfragment.setFooter(new AliFooter(getActivity()));

        springviewIndexfragment.onFinishFreshAndLoad();
        springviewIndexfragment.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {

                page=1;
               presenter.getData();


            }

            @Override
            public void onLoadmore() {
                presenter.getData();
            }
        });

        adapter.setmOnItemClickListener(new IndexAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent=new Intent(getActivity(),ZiliaoActivity.class);

                intent.putExtra("name",position);
                getActivity().startActivity(intent);
            }
        });

    }

    @Override
    public void failed(int code) {

    }





}
