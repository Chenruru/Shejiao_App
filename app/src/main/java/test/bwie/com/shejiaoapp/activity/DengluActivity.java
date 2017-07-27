package test.bwie.com.shejiaoapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.util.HashMap;
import java.util.Map;

import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.base.IApplication;
import test.bwie.com.shejiaoapp.bean.RegisterBean;
import test.bwie.com.shejiaoapp.cipher.Md5Utils;
import test.bwie.com.shejiaoapp.cipher.aes.JNCryptorUtils;
import test.bwie.com.shejiaoapp.cipher.rsa.RsaUtils;
import test.bwie.com.shejiaoapp.core.JNICore;
import test.bwie.com.shejiaoapp.core.SortUtils;
import test.bwie.com.shejiaoapp.network.BaseObserver;
import test.bwie.com.shejiaoapp.network.RetrofitManager;
import test.bwie.com.shejiaoapp.utils.Constants;
import test.bwie.com.shejiaoapp.utils.PreferencesUtils;
import test.bwie.com.shejiaoapp.widget.MyToast;

public class DengluActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView image_id_;
    private TextView btn_zhuce;
    private EditText edittext_shoujihao_id;
    private EditText edittext_mima_id;
    private Button btn_denglu;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denglu);

        initview();
        SharedPreferences sp = getSharedPreferences("isfrit", MODE_PRIVATE);
        edit = sp.edit();
    }

    private void initview() {
        image_id_ = (ImageView) findViewById(R.id.image_id_);
        btn_zhuce = (TextView) findViewById(R.id.btn_zhuce);
        edittext_shoujihao_id = (EditText) findViewById(R.id.edittext_shoujihao_id);
        edittext_mima_id = (EditText) findViewById(R.id.edittext_mima_id);
        btn_denglu = (Button) findViewById(R.id.btn_denglu);

        image_id_.setOnClickListener(this);
        btn_zhuce.setOnClickListener(this);
        btn_denglu.setOnClickListener(this);
    }


    //监听
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_id_://点击左上角返回
                startActivity(new Intent(DengluActivity.this,FanhuiActivity.class));
                break;
            case R.id.btn_zhuce://点击右上角注册
                startActivity(new Intent(DengluActivity.this,ZhuceActivity.class));
                break;
            case R.id.btn_denglu:   //登录按钮
                String phone = edittext_shoujihao_id.getText().toString().trim();
                String password = edittext_mima_id.getText().toString().trim();
                if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(password)) {
                    MyToast.makeText(DengluActivity.this, "请输入账号与密码", Toast.LENGTH_SHORT);
                } else {
                    getData(phone,password);  //登录成功
                    edit.putBoolean("frit",true);
                    edit.commit();
                }
                break;
        }
    }
    //tring
    private void getData(final String phone, final String password) {


        String randomKey = RsaUtils.getStringRandom(16);  //验签 随机生成的一个16位数
        String rsaRandomKey = RsaUtils.getInstance().createRsaSecret(this, randomKey);
        String cipherPhone = JNCryptorUtils.getInstance().encryptData(phone, this, randomKey);

        Map map = new HashMap<String, String>();
        map.put("user.phone", cipherPhone);
        map.put("user.password", Md5Utils.getMD5(password));
        map.put("user.secretkey", rsaRandomKey);
        //加密手机号，，秘钥生成的16位数
        String sign = JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map)));
        map.put("user.sign", sign);

        RetrofitManager.post(Constants.LOGIN_ACTION, map, new BaseObserver() {
            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                RegisterBean registerBean = gson.fromJson(result, RegisterBean.class);
                int result_code = registerBean.getResult_code();
                if (result_code == 200 &&registerBean.getData()!=null ) {

                    int userId = registerBean.getData().getUserId();
                    String yxpassword = registerBean.getData().getYxpassword();

                    EMClient.getInstance().login(userId+"",yxpassword,new EMCallBack() {
                        //回调
                        @Override public void onSuccess() {
                            Intent intent = new Intent(DengluActivity.this, TabActivity.class);
                            EMClient.getInstance().groupManager().loadAllGroups();
                            EMClient.getInstance().chatManager().loadAllConversations();
                            startActivity(intent);
                            finish();
                            Log.e("main", "登录聊天服务器成功！");
                        }
                        @Override public void onProgress(int progress, String status) {

                        } @Override public void onError(int code, String message) {
                            Log.e("main", "登录聊天服务器失败！");
                            Log.d("main", message.toString());                         }
                    });




                }
                if (result_code == 303) {
                    if (TextUtils.isEmpty(phone)) {
                        MyToast.makeText(DengluActivity.this, "账号不能为空", Toast.LENGTH_SHORT);
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        MyToast.makeText(DengluActivity.this, "密码不能为空", Toast.LENGTH_SHORT);
                        return;
                    }
                    MyToast.makeText(DengluActivity.this, "账号或密码错误", Toast.LENGTH_SHORT);
                }
            }


            @Override
            public void onFailed(int code) {

            }
        });
    }
    }
