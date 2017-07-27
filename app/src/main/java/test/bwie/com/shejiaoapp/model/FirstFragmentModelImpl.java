package test.bwie.com.shejiaoapp.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Map;

import test.bwie.com.shejiaoapp.bean.IndexBean;
import test.bwie.com.shejiaoapp.core.JNICore;
import test.bwie.com.shejiaoapp.core.SortUtils;
import test.bwie.com.shejiaoapp.network.BaseObserver;
import test.bwie.com.shejiaoapp.network.RetrofitManager;
import test.bwie.com.shejiaoapp.utils.Constants;
import test.bwie.com.shejiaoapp.utils.FirstFragmentDaoUtils;

/**
 * date: 2017/7/11
 * author:陈茹
 * 类的用途: 第一个fragment的网络解析
 */

public class FirstFragmentModelImpl implements FirstFragmentModel {


    @Override
    public void getData( final DataListener listener) {

        Map<String,String> map = new HashMap<String, String>();
        map.put("user.currenttimer",System.currentTimeMillis()+"");


        //MD5加密
        String sign =  JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map))) ;
        map.put("user.sign",sign);

        RetrofitManager.get("http://qhb.2dyt.com/MyInterface/userAction_selectAllUser.action", map, new BaseObserver() {
            @Override
            public void onSuccess(String result) {

                //long currentime =    infoBean.getData().get(infoBean.getData().size()-1).getCreatetime();
                try {
                    Gson gson = new Gson();
                    IndexBean indexBean =   gson.fromJson(result, IndexBean.class);


                    listener.onSuccess(indexBean);
                    FirstFragmentDaoUtils.insert(indexBean.getData());


                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int code) {

               listener.onFailed(code);
            }
        });


    }
}
