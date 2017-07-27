package test.bwie.com.shejiaoapp.model;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.HashMap;
import java.util.Map;

import test.bwie.com.shejiaoapp.base.IApplication;
import test.bwie.com.shejiaoapp.bean.RegisterBean;
import test.bwie.com.shejiaoapp.core.JNICore;
import test.bwie.com.shejiaoapp.core.SortUtils;
import test.bwie.com.shejiaoapp.network.BaseObserver;
import test.bwie.com.shejiaoapp.network.RetrofitManager;
import test.bwie.com.shejiaoapp.utils.PreferencesUtils;

/**
 * date: 2017/7/7
 * author:陈茹
 * 类的用途:
 */

public class RegisterInforFragmentModelImpl implements RegisterInforFragmentModel{
    @Override
    public void getData(String phone, String nickname, String sex, String age, String area, String introduce, String password, final RegisterInforFragmentDataListener listener) {
        Map<String,String> map=new HashMap<String,String>();
        map.put("user.phone",phone);
        map.put("user.nickname",nickname);
        map.put("user.password",password);
        map.put("user.sex",sex);
        map.put("user.age",age);
        map.put("user.area",area);
        map.put("user.introduce",introduce);
        String sign =  JNICore.getSign(SortUtils.getMapResult(SortUtils.sortString(map))) ;
        map.put("user.sign",sign);

        RetrofitManager.post("http://qhb.2dyt.com/MyInterface/userAction_add.action", map, new BaseObserver() {

        @Override

        public void onSuccess(String result) {

            System.out.println("myresult"+result);


            Gson gson = new Gson();

            RegisterBean registerBean = gson.fromJson(result, RegisterBean.class);

            IApplication.getApplication().eLogin();

            if(registerBean.getResult_code() == 200){


                PreferencesUtils.addConfigInfo(IApplication.getApplication(),"phone",registerBean.getData().getPhone());

                PreferencesUtils.addConfigInfo(IApplication.getApplication(),"password",registerBean.getData().getPassword());

                PreferencesUtils.addConfigInfo(IApplication.getApplication(),"yxpassword",registerBean.getData().getYxpassword());

                PreferencesUtils.addConfigInfo(IApplication.getApplication(),"uid",registerBean.getData().getUserId());

                PreferencesUtils.addConfigInfo(IApplication.getApplication(),"nickname",registerBean.getData().getNickname());



                final String yxpassword = registerBean.getData().getYxpassword();
                final int userId = registerBean.getData().getUserId();


                IApplication.getApplication().eLogin();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().createAccount(userId+"",yxpassword);  //同步的方法
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            listener.onSuccess(registerBean);

        }

        @Override

        public void onFailed(int code) {

            listener.onFailed(code);

        }


    });

}
}
