package test.bwie.com.shejiaoapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jakewharton.rxbinding2.view.RxView;
import com.lljjcoder.citypickerview.widget.CityPickerView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.base.IActivity;
import test.bwie.com.shejiaoapp.base.IApplication;
import test.bwie.com.shejiaoapp.bean.RegisterBean;
import test.bwie.com.shejiaoapp.cipher.Md5Utils;
import test.bwie.com.shejiaoapp.core.JNICore;
import test.bwie.com.shejiaoapp.core.SortUtils;
import test.bwie.com.shejiaoapp.network.BaseObserver;
import test.bwie.com.shejiaoapp.network.RetrofitManager;
import test.bwie.com.shejiaoapp.utils.PreferencesUtils;
import test.bwie.com.shejiaoapp.widget.MyToast;


public class NichengActivity extends IActivity implements View.OnClickListener{

    private TextView text_jianjie;
    private TextView text_city;
    private TextView text_age;
    private TextView text_sex;
    private EditText edittext_password;
    private EditText edittext_nicheng;
    private Button btn_next;
    private InputMethodManager imm;
    private String phone1;
    private ImageView image_id_fan;
    double latitude;
    double accuracy;
    //声明AMapLocationClient类对象
    AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nicheng);

        //查找资源id
        initview();
        phone1 = getIntent().getStringExtra("user");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


        String userids = PreferencesUtils.getValueByKey(this, "userids", "");
        String pwd = PreferencesUtils.getValueByKey(this, "pwd", "");

        try {
            EMClient.getInstance().createAccount(userids, pwd);//同步方法
        } catch (HyphenateException e) {
            e.printStackTrace();
        }

    }


    private void initview() {
        text_jianjie = (TextView) findViewById(R.id.text_jianjie);  //自我描述
        text_city = (TextView) findViewById(R.id.text_city);  //城市
        text_age = (TextView) findViewById(R.id.text_age);     //年龄
        text_sex = (TextView) findViewById(R.id.text_sex);    //性别
        edittext_password = (EditText) findViewById(R.id.edittext_password);   //密码
        edittext_nicheng = (EditText) findViewById(R.id.edittext_nicheng);   //昵称
        btn_next = (Button) findViewById(R.id.btn_next);
        image_id_fan = (ImageView) findViewById(R.id.image_id_fan);

        text_jianjie.setOnClickListener(this);
        text_city.setOnClickListener(this);
        text_age.setOnClickListener(this);
        text_sex.setOnClickListener(this);
        edittext_password.setOnClickListener(this);
        edittext_nicheng.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        image_id_fan.setOnClickListener(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            //自我描述
            case R.id.text_jianjie:
                break;
            //返回
            case R.id.image_id_fan:
                startActivity(new Intent(NichengActivity.this,ZhuceActivity.class));
                break;
            //输入密码
            case R.id.edittext_password:
                break;
            //所在城市
            case R.id.text_city:
                showLocal();

                break;
            //年龄
            case R.id.text_age:
                showAgeDialog();
                break;
            //性别
            case R.id.text_sex:
                showSexChooseDialog();
                break;
            //昵称
            case R.id.edittext_nicheng:
                break;
            //下一步
            case R.id.btn_next:
                /*String nicheng = edittext_nicheng.getText().toString().trim();  //用户输入的昵称
        String password = edittext_password.getText().toString().trim();  //用户输入的密码
        String jianjie = text_jianjie.getText().toString().trim();  //自我描述
        String sex = text_sex.getText().toString().trim();  //性别
        String age = text_age.getText().toString().trim();  //年龄
        String city = text_city.getText().toString().trim(); //城市
                */

                getData(phone1,
                        edittext_nicheng.getText().toString().trim(),
                        text_sex.getText().toString().trim()
                        , text_age.getText().toString().trim()
                        , text_city.getText().toString().trim()
                        , text_jianjie.getText().toString().trim(),
                        Md5Utils.getMD5(edittext_password.getText().toString().trim()));



                if (TextUtils.isEmpty(edittext_nicheng.getText().toString()) ){
                    Toast.makeText(NichengActivity.this,"请输入昵称",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (TextUtils.isEmpty(edittext_password.getText().toString()) ){
                Toast.makeText(NichengActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                break;
            }
                if (TextUtils.isEmpty(text_sex.getText().toString()) ){
                    Toast.makeText(NichengActivity.this,"请选择性别",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (TextUtils.isEmpty(text_age.getText().toString()) ){
                    Toast.makeText(NichengActivity.this,"请选择年龄",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (TextUtils.isEmpty(text_city.getText().toString()) ){
                    Toast.makeText(NichengActivity.this,"请输入城市",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (TextUtils.isEmpty(text_jianjie.getText().toString()) ){
                    Toast.makeText(NichengActivity.this,"请输入简介",Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    startActivity(new Intent(NichengActivity.this,PhotoActivity.class));
                }

//                toData();
                break;
        }
    }

    //城市弹出框的三级列表
    private void showLocal() {

        CityPickerView cityPickerView = new CityPickerView(NichengActivity.this);
        cityPickerView.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];
               // Toast.makeText(MainActivity.this,province+"-"+city+"-"+district,Toast.LENGTH_LONG).show();
                text_city.setText(province+"-"+city+"-"+district);

            }
        });
        cityPickerView.show();

    }

    //性别弹出框
    private String[] sexArry = new String[]{"女", "男"};
    private void showSexChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NichengActivity.this);
        builder.setTitle("请选择性别");
        builder.setItems(sexArry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                text_sex.setText(sexArry[which]);
            }
        });
        builder.create().show();
    }


    //年龄弹出框
    AlertDialog.Builder builder;
    private void showAgeDialog() {
        if (builder == null) {
            final String[] ages = getResources().getStringArray(R.array.age);
            builder = new AlertDialog.Builder(NichengActivity.this);
            builder.setTitle("请选择年龄");
            builder.setItems(ages, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    text_age.setText(ages[which]);
                }
            });
        }

        builder.show();
    }

//    getData(phone1,
//            edittext_nicheng.getText().toString().trim(),
//    text_sex.getText().toString().trim()
//    , text_city.getText().toString().trim()
//    , text_jianjie.getText().toString().trim()
//    , text_age.getText().toString().trim(),
//    Md5Utils.getMD5(edittext_password.getText().toString().trim()));

    public void getData(String phone,  String nickname, String sex, String age, String area, String introduce,  String password){

        Map<String,String> map = new HashMap<String,String>();
        map.put("user.phone",phone);
        map.put("user.nickname",nickname);
        map.put("user.password",password);
        map.put("user.gender",sex);
        map.put("user.age",age);
        map.put("user.area",area);
        map.put("user.introduce",introduce);
//        map.put("user.lat", String.valueOf(latitude));
//        map.put("user.lng", String.valueOf(accuracy));

        //Map进行排序，，因为hashmap是无序的，要有无序返回到有序
        //吧传入的参数进行排序
        //map拼接成字符串，返回值为string
        //MD5的一个编码
        //吧验签也存入map里面去
        String sign =  JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map))) ;
        map.put("user.sign",sign);

        RetrofitManager.post("http://qhb.2dyt.com/MyInterface/userAction_add.action", map, new BaseObserver() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RegisterBean registerBean = gson.fromJson(result, RegisterBean.class);
                if(registerBean.getResult_code() == 200){
                    PreferencesUtils.addConfigInfo(IApplication.getApplication(),"phone",registerBean.getData().getPhone());
                    PreferencesUtils.addConfigInfo(IApplication.getApplication(),"password",registerBean.getData().getPassword());
                    PreferencesUtils.addConfigInfo(IApplication.getApplication(),"yxpassword",registerBean.getData().getYxpassword());
                    PreferencesUtils.addConfigInfo(IApplication.getApplication(),"uid",registerBean.getData().getUserId());
                    PreferencesUtils.addConfigInfo(IApplication.getApplication(),"nickname",registerBean.getData().getNickname());
                    MyToast.makeText(NichengActivity.this,"注册成功",Toast.LENGTH_SHORT);

                }else{

                }
            }
            @Override
            public void onFailed(int code) {
                MyToast.makeText(NichengActivity.this,"手机号已存在",Toast.LENGTH_SHORT);
            }
        });

    }

    /**
     * 判断所有的参数 非空
     * 注册 添加 草稿功能
     */
    private void toData() {


        RxView.clicks(btn_next).throttleFirst(1, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {

                        System.out.println("o = " + o);

                    }
                });



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
