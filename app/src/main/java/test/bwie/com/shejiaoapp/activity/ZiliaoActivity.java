package test.bwie.com.shejiaoapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.adapter.Myreclerviewadapter;
import test.bwie.com.shejiaoapp.bean.DataBean;
import test.bwie.com.shejiaoapp.bean.IndexBean;
import test.bwie.com.shejiaoapp.bean.InfoUser;
import test.bwie.com.shejiaoapp.bean.UserBean;
import test.bwie.com.shejiaoapp.core.JNICore;
import test.bwie.com.shejiaoapp.core.SortUtils;
import test.bwie.com.shejiaoapp.network.BaseObserver;
import test.bwie.com.shejiaoapp.network.RetrofitManager;
import test.bwie.com.shejiaoapp.utils.FirstFragmentDaoUtils;

public class ZiliaoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageview_xiangqing;
    private TextView text_name_id;
    private TextView text_age_id;
    private TextView text_city_id;
    private ImageView imageView_id_;
    private ImageView imagefview_fanhui;
    private RecyclerView recyclerview_id;

    private List<UserBean> others = new ArrayList<>();
    private DataBean image;
    private ScrollView scrollview_id;
    private Button btn_id_;
    private int userId;
    private int name1;
    private InfoUser.DataBean data;
    private Button btn_id_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ziliao);

        initview();
        data();


        recyclerview_id.setLayoutManager(new LinearLayoutManager(this));
        Myreclerviewadapter myreclerviewadapter = new Myreclerviewadapter(ZiliaoActivity.this, others);
        recyclerview_id.setAdapter(myreclerviewadapter);


    }


    private void data() {
        Intent intent = getIntent();
        name1 = intent.getIntExtra("name",0);
        getuserbyid(name1);



        String[] name = {" 我的资料", "昵称", "年龄", "星座", "居住地"
                , "身高", "体重", "血型", "籍贯", "职业", "学历"
                , "收入", "魅力部位", "婚姻状况", "住房情况", "接收异地恋", "喜欢的异性"
                , "接收亲密行为", "和父母同住", "是否要小孩", "我的标签"};

        String[] ziliao = {"", " 高贵的银耳汤", "24岁", "摩羯座", "北京市-市区", " 0cm"
                , " 0斤", "保密", "保密", "保密", "保密", "保密"
                , "保密", "保密", "保密", "保密", "保密", "保密"
                , "保密", "保密", ""};
        for (int i = 0; i < 21; i++) {
            //一定要记住吧数组里的[i]加上
            UserBean userBean = new UserBean();
            userBean.setName(name[i] + "");
            userBean.setZiliao(ziliao[i] + "");
            others.add(userBean);
        }

    }

    private void initview() {

        //查找资源id
        imageview_xiangqing = (ImageView) findViewById(R.id.imageview_xiangqing);  //大头像
        text_name_id = (TextView) findViewById(R.id.text_name_id);   //昵称
        text_age_id = (TextView) findViewById(R.id.text_age_id);    //年龄
        text_city_id = (TextView) findViewById(R.id.text_city_id);  //城市
        imageView_id_ = (ImageView) findViewById(R.id.imageView_id_);  //小头像
        imagefview_fanhui = (ImageView) findViewById(R.id.imageview_fanhui_);  //返回
        recyclerview_id = (RecyclerView) findViewById(R.id.recyclerview_id);
        scrollview_id = (ScrollView) findViewById(R.id.scrollview_id);
        btn_id_ = (Button) findViewById(R.id.btn_id_); //添加好友
        btn_id_2 = (Button) findViewById(R.id.btn_id_2);  //发消息


        //监听
        imagefview_fanhui.setOnClickListener(this);
        imageview_xiangqing.setOnClickListener(this);
        imageView_id_.setOnClickListener(this);
        btn_id_.setOnClickListener(this);
        btn_id_2.setOnClickListener(this);
        //scrollview嵌套不在最顶层，设置这句话在最顶层
        scrollview_id.smoothScrollTo(0, 20);
    }

    //监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_fanhui_:
                finish();
                break;

            case R.id.imageview_xiangqing:

                Intent intent = new Intent(this, PhotoviewActivity.class);
                intent.putExtra("photo", data.getImagePath());
                startActivity(intent);
                break;
            case R.id.imageView_id_:

                Intent intent2 = new Intent(this, PhotoviewActivity.class);
                intent2.putExtra("photo", data.getImagePath());
                startActivity(intent2);
                break;
            case R.id.btn_id_:   //添加好友

                gettianjia(name1);
                Toast.makeText(ZiliaoActivity.this,"添加成功",Toast.LENGTH_SHORT).show();

                break;
            case R.id.btn_id_2:   //发消息

                Intent intent3= new Intent(this, CChativity.class);
                 intent3.putExtra("id",name1);  //通过uid发送
                startActivity(intent3);

                break;
        }

    }


    //打招呼添加好友
    public void gettianjia(int uid) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("relationship.friendId", uid + "");

        //MD5加密
        String sign = JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map)));
        map.put("user.sign", sign);

        RetrofitManager.get("http://qhb.2dyt.com/MyInterface/userAction_addFriends.action", map, new BaseObserver() {
            @Override
            public void onSuccess(String result) {

                //long currentime =    infoBean.getData().get(infoBean.getData().size()-1).getCreatetime();
                try {
                    Gson gson = new Gson();
                    IndexBean indexBean = gson.fromJson(result, IndexBean.class);


                    FirstFragmentDaoUtils.insert(indexBean.getData());


                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int code) {

            }
        });
    }




    public void getuserbyid(int userId){
        Map<String, String> map = new HashMap<String, String>();
        map.put("user.userId", userId+"");
        //MD5加密
        String sign = JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map)));
        map.put("user.sign", sign);

        RetrofitManager.get("http://qhb.2dyt.com/MyInterface/userAction_selectUserById.action", map, new BaseObserver() {

            @Override
            public void onSuccess(String result) {

                //long currentime =    infoBean.getData().get(infoBean.getData().size()-1).getCreatetime();
                try {
                    Gson gson = new Gson();
                    InfoUser dataBean = gson.fromJson(result, InfoUser.class);
                    data = dataBean.getData();



                    Glide.with(ZiliaoActivity.this).load(data.getImagePath()).into(imageview_xiangqing);
                        Glide.with(ZiliaoActivity.this).load(data.getImagePath()).into(imageView_id_);
                        text_name_id.setText(data.getNickname());
                        text_city_id.setText(data.getArea());

                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int code) {

            }
        });

    }



}
