package test.bwie.com.shejiaoapp.model;

import test.bwie.com.shejiaoapp.bean.RegisterBean;

/**
 * date: 2017/7/7
 * author:陈茹
 * 类的用途:
 */

public interface RegisterInforFragmentModel {

    public void getData(String phone,String nickname,String sex,String age,String area,String introduce,String password,RegisterInforFragmentDataListener listener);


    public interface RegisterInforFragmentDataListener {


        public void onSuccess(RegisterBean registerBean);

        public void onFailed(int code);

    }

}
