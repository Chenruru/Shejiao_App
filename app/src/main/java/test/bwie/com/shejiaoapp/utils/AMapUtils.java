package test.bwie.com.shejiaoapp.utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * date: 2017/7/11
 * author:陈茹
 * 类的用途:
 */

public class AMapUtils {
    public static final String LAT = "lat" ;
    public static final String LNG = "lng" ;

    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mlocationClient = null ;

    public static AMapUtils aMapUtils = null ;
    private AMapUtils(){

    }
    public static AMapUtils getInstance(){
        if(aMapUtils == null){
            aMapUtils = new AMapUtils();
        }
        return  aMapUtils ;
    }

    public void startUtils(final Context context){
        //声明mLocationOption对象
        mlocationClient = new AMapLocationClient(context);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
//                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        double lat =  amapLocation.getLatitude();//获取纬度
                        double lng =  amapLocation.getLongitude();//获取经度
//                        amapLocation.getAccuracy();//获取精度信息
                        PreferencesUtils.addConfigInfo(context,LAT,lat+"");
                        PreferencesUtils.addConfigInfo(context,LNG,lng+"");
                        Log.e("AmapError","lat :" + lat + "lng " + lng);
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                    mlocationClient.stopLocation();
                }
            }
        });
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mlocationClient.startLocation();

    }

}
