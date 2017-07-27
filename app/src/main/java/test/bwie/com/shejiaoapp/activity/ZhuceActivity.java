package test.bwie.com.shejiaoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import test.bwie.com.shejiaoapp.R;

public class ZhuceActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edittext_shoujihao_id;
    private EditText edittext_yanzhengma_id;
    private Button btn_queding;
    private ImageView image_id;
    private Button button_id_yanzheng;
    private CountDownTimer cdt;//计时器
    private CountDownTimer countDownTimer;//计时器
    private EventHandler eh;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhu_ce);
        //查找资源id

        initview();

        initdata();



    }

    //计时器
    private void initdata() {


        /**
         * 初始化事件接收器
         */
        //回调完成
        // 提交验证码成功
       //页面跳转
     //获取验证码成功
//返回支持发送验证码的国家列表
        eh = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成

                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码成功

                        startActivity(new Intent(ZhuceActivity.this, NichengActivity.class)); //页面跳转

                        finish();
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){ //获取验证码成功

                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){ //返回支持发送验证码的国家列表

                    }
                }else{
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调

    }


    /**
     * 正则匹配手机号码
     * @param tel
     * @return
     */
    public boolean checkTel(String tel){
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher matcher = p.matcher(tel);
        return matcher.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //短信SMSSDK
        SMSSDK.unregisterEventHandler(eh);
    }

    private void initview() {
        edittext_shoujihao_id = (EditText) findViewById(R.id.edittext_shoujihao_id);
        edittext_yanzhengma_id = (EditText) findViewById(R.id.edittext_yanzhengma_id);
        image_id = (ImageView) findViewById(R.id.image_id);
        button_id_yanzheng = (Button) findViewById(R.id.button_id_yanzheng);
        btn_queding = (Button) findViewById(R.id.zhu_ce_numble);

        //监听
        edittext_shoujihao_id.setOnClickListener(this);
        edittext_yanzhengma_id.setOnClickListener(this);
        image_id.setOnClickListener(this);
        button_id_yanzheng.setOnClickListener(this);
        btn_queding.setOnClickListener(this);
        button_id_yanzheng.setClickable(false);

        //强制隐藏键盘
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    //获取验证码
    private void getData() {
        String phone = edittext_shoujihao_id.getText().toString().trim();
        SMSSDK.getVerificationCode("86", phone);
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        String code = edittext_yanzhengma_id.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;

        }


        // 创建EventHandler对象
        eh = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {

            }
        };

        // 注册监听器
        SMSSDK.registerEventHandler(eh);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //请输入手机号
            case R.id.edittext_shoujihao_id:
                break;
            //请输入验证码
            case R.id.edittext_yanzhengma_id:

                break;



            //获取验证码
            case R.id.zhu_ce_numble:

               getData();
                button_id_yanzheng.setClickable(true);

                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss");
                countDownTimer = new CountDownTimer(30000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String format = simpleDateFormat.format(millisUntilFinished);
                        btn_queding.setText("重新发送"+format+"s");
                        btn_queding.setEnabled(false);
                    }

                    @Override
                    public void onFinish() {
                        btn_queding.setText("重新加载");
                        btn_queding.setEnabled(true);
                    }
                };
                countDownTimer.start();


                break;
            //点击确定
            case R.id.button_id_yanzheng:
                SMSSDK.getSupportedCountries();//获取短信目前支持的国家列表
                if(!edittext_shoujihao_id.getText().toString().trim().equals("")){
                    if (checkTel(edittext_shoujihao_id.getText().toString().trim())) {
                        SMSSDK.getVerificationCode("+86",edittext_shoujihao_id.getText().toString());//获取验证码
                        // countDownTimer.start();
                        String phone = edittext_shoujihao_id.getText().toString().trim();
                        Intent intent=new Intent(ZhuceActivity.this,NichengActivity.class);
                        intent.putExtra("user",phone);
                        startActivity(intent);

                    }else  {
                        Toast.makeText(ZhuceActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ZhuceActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                }

                break;
            //点击返回
            case R.id.image_id:
                Intent intent2=new Intent(ZhuceActivity.this,FanhuiActivity.class);
                startActivity(intent2);
                finish();
                break;

        }
    }



}
