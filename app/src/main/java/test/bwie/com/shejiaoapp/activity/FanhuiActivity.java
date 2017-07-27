package test.bwie.com.shejiaoapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import test.bwie.com.shejiaoapp.R;

public class FanhuiActivity extends AppCompatActivity  implements View.OnClickListener{
    private Button btin_login;
    private Button btin_zhuce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fanhui);
        initview();

        //给登录注册的两个按钮设置透明度
        AlphaAnimation animation=new AlphaAnimation(0.1f,1.0f);
        animation.setDuration(3000);
        btin_login.startAnimation(animation);
        btin_zhuce.startAnimation(animation);
    }
    private void initview() {
        btin_login = (Button) findViewById(R.id.btin_login);
        btin_zhuce = (Button) findViewById(R.id.btin_zhuce);
        btin_login.setOnClickListener(this);
        btin_zhuce.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //登录
            case R.id.btin_login:
                startActivity(new Intent(FanhuiActivity.this,DengluActivity.class));
                finish();

                break;

            //注册
            case R.id.btin_zhuce:
                startActivity(new Intent(FanhuiActivity.this,ZhuceActivity.class));
                finish();
                break;
        }
    }
    }

