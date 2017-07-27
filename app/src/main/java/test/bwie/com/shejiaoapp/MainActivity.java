package test.bwie.com.shejiaoapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import test.bwie.com.shejiaoapp.activity.FanhuiActivity;
import test.bwie.com.shejiaoapp.activity.Sexactivity;
import test.bwie.com.shejiaoapp.activity.TabActivity;

public class MainActivity extends AppCompatActivity{

    //声明定位回调监听器
    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if ( aMapLocation !=null){

              Toast.makeText(MainActivity.this,"纬度"+aMapLocation.getLatitude()+"经度"+ aMapLocation.getLongitude(), Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(MainActivity.this,"为空", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private TextView textview_tiao;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mlocationClient = null;
    int time=3;

    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (time>0){
                time--;
                textview_tiao.setText(time+"");
                handler.sendEmptyMessageDelayed(0,1000);

            }else {
                Intent intent=new Intent(MainActivity.this,TabActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
    private ImageView imageview_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //查找资源id
        initview();


        //这里以ACCESS_COARSE_LOCATION为例
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);//自定义的code
        }

        data();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
    }


    /**
     * 定位信息展示
     */
    private void data() {
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mlocationClient = new AMapLocationClient(getApplicationContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(mLocationListener);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);

        //设置是否允许模拟位置,默认为false，不允许模拟位置
       // mLocationOption.setMockEnable(false);

        //给定位客户端对象设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //启动定位
        mlocationClient.startLocation();
    }

    private void initview() {

        textview_tiao = (TextView) findViewById(R.id.textview_tiao);
        imageview_id = (ImageView) findViewById(R.id.imageview_id);
        handler.sendEmptyMessage(0);
    }


    //欢迎页的动画心
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        imageview_id.setImageResource(R.drawable.animlist);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageview_id.getDrawable();
        animationDrawable.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlocationClient.onDestroy();
    }
}
