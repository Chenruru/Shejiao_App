package test.bwie.com.shejiaoapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.base.IActivity;
import test.bwie.com.shejiaoapp.base.IApplication;
import test.bwie.com.shejiaoapp.fragments.FirstFragment;
import test.bwie.com.shejiaoapp.fragments.FourthFragment;
import test.bwie.com.shejiaoapp.fragments.SecondFragment;
import test.bwie.com.shejiaoapp.fragments.ThirdFragment;
import test.bwie.com.shejiaoapp.widget.ButtomLayout;

//创建四个button的框架
public class TabActivity extends IActivity implements ButtomLayout.OnSelectListener{
    private ButtomLayout buttomLayout;
    private FragmentManager fragmentManager;

    private List<Fragment> fragments = new ArrayList<Fragment>();
    private CallReceiver callReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        fragmentManager = getSupportFragmentManager();

      createFragment(savedInstanceState);



        buttomLayout = (ButtomLayout) findViewById(R.id.buttom_layout);
        buttomLayout.setOnSelectListener(this);


        switchFragment(0);
        incoming(); //监听视频来电

    }

    public void createFragment(Bundle savedInstanceState){

        FirstFragment firstFragment = (FirstFragment) fragmentManager.findFragmentByTag("FirstFragment");
        SecondFragment secondFragment = (SecondFragment) fragmentManager.findFragmentByTag("SecondFragment");
        ThirdFragment thirdFragment = (ThirdFragment) fragmentManager.findFragmentByTag("ThirdFragment");
        FourthFragment fourthFragment = (FourthFragment) fragmentManager.findFragmentByTag("FourthFragment");

        if(firstFragment == null){
            firstFragment = new FirstFragment();
        }

        if(secondFragment == null){
            secondFragment = new SecondFragment();
        }
        if(thirdFragment == null){
            thirdFragment = new ThirdFragment();
        }
        if(fourthFragment == null){
            fourthFragment = new FourthFragment();
        }


        fragments.add(firstFragment);
        fragments.add(secondFragment);
        fragments.add(thirdFragment);
        fragments.add(fourthFragment);


    }


    public void switchFragment(int pos){

        switchIFragment(pos,fragments,R.id.container);

    }



    /**
     * 底部导航 点击 回调
     * @param index
     */
    @Override
    public void onSelect(int index) {
        switchFragment(index);
    }



    //判断别人有没有打过来视屏，，就在主导页进行监听视屏来电
    //监听视频来电
    public void incoming() {
        callReceiver = new CallReceiver();
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        registerReceiver(callReceiver, callFilter);
    }

    //监听视屏电话
    private class CallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 拨打方username
            String from = intent.getStringExtra("from");
            // call type
            String type = intent.getStringExtra("type");
            //跳转到通话页面

            IApplication.ring();

            //去调跳转的聊天界面
            VideoActivity.startTelActivity(2, from, TabActivity.this);


        }
    }

    //销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(callReceiver);
    }
}
