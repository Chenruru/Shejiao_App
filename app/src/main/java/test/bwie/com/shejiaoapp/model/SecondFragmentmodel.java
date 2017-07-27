package test.bwie.com.shejiaoapp.model;



import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import test.bwie.com.shejiaoapp.bean.DataBean;
import test.bwie.com.shejiaoapp.bean.IndexBean;
import test.bwie.com.shejiaoapp.core.JNICore;
import test.bwie.com.shejiaoapp.core.SortUtils;
import test.bwie.com.shejiaoapp.network.BaseObserver;
import test.bwie.com.shejiaoapp.network.RetrofitManager;

/**
 * date: 2017/7/14
 * author:陈茹
 * 类的用途:
 */

public class SecondFragmentmodel implements Secondmodel {
    @Override
    public void getdatesecond(final  secondDataListener secondDataListener) {
        Map<String,String> map = new HashMap<String, String>();
        map.put("user.currenttimer",System.currentTimeMillis()+"");

        String sign =  JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map))) ;
        map.put("user.sign", sign);


        RetrofitManager.post("http://qhb.2dyt.com/MyInterface/userAction_selectAllUserAndFriend.action",
                map, new BaseObserver() {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println("myresult"+result);

                        Gson gson=new Gson();
                        IndexBean indexbean = gson.fromJson(result, IndexBean.class);

                        secondDataListener.onSuccess(indexbean);


                    }

                    @Override
                    public void onFailed(int code) {
                        secondDataListener.onFailed(code);
                    }
                });
    }
}
