package test.bwie.com.shejiaoapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test.bwie.com.shejiaoapp.R;

public class Sexactivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    private RadioButton image_man;
    private RadioButton image_woman;
    private Button btn_queding;
    private ImageView image_id_;
    private TextView btn_denglu_id;
    private View leftImageView;
    private View rightImageView;
    private Button btMain;
    List<String> strings = new ArrayList<String>();
    private TextView tvMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sexactivity);
        //查找资源id
        initview();
        initdata();
    }

    private void initview() {
        image_man = (RadioButton) findViewById(R.id.image_man);
        image_woman = (RadioButton) findViewById(R.id.image_woman);
        image_id_ = (ImageView) findViewById(R.id.image_id_);
        btn_queding = (Button) findViewById(R.id.btn_queding);
        btn_denglu_id = (TextView) findViewById(R.id.btn_denglu_id_);
      //  hsMain = (HorizontalselectedView) findViewById(R.id.hd_main);
        //leftImageView = findViewById(R.id.iv_left);
       // rightImageView = findViewById(R.id.iv_right);

//        leftImageView.setOnClickListener(this);
//        rightImageView.setOnClickListener(this);

        //监听
        btn_queding.setOnClickListener(this);
        image_id_.setOnClickListener(this);
        image_woman.setOnCheckedChangeListener(this);
        image_man.setOnCheckedChangeListener(this);
        btn_denglu_id.setOnClickListener(this);
        btn_queding.setClickable(false);
    }

    private void initdata() {

        for (int i = 15; i < 50; i++) {
            strings.add(i + "岁");
        }
      //  hsMain.setData(strings);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击确定跳转到注册界面
            case R.id.btn_queding:
                startActivity(new Intent(Sexactivity.this,ZhuceActivity.class));

                finish();

                break;

            //点击返回
            case R.id.image_id_:
                startActivity(new Intent(Sexactivity.this,FanhuiActivity.class));
                finish();
                break;

            //右上角登录按钮
            case R.id.btn_denglu_id_:
                startActivity(new Intent(Sexactivity.this,ZhuceActivity.class));
                finish();
                break;

//            case R.id.iv_left:
//              //  hsMain.setAnLeftOffset();
//                break;
//            case R.id.iv_right:
//              //  hsMain.setAnRightOffset();
//                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        btn_queding.setPressed(true);
        if (isChecked){
            btn_queding.setClickable(true);

            image_woman.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_queding.setClickable(true);
                }
            });

        }
       // btn_queding.setPressed(true);
        if (isChecked){
            btn_queding.setClickable(true);

            image_man.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_queding.setClickable(true);
                }
            });

        }
    }


}
