package test.bwie.com.shejiaoapp.presenter;


import test.bwie.com.shejiaoapp.base.BasePresenter;
import test.bwie.com.shejiaoapp.bean.RegisterBean;
import test.bwie.com.shejiaoapp.model.RegisterInforFragmentModel;
import test.bwie.com.shejiaoapp.model.RegisterInforFragmentModelImpl;
import test.bwie.com.shejiaoapp.mview.RegisterInforFragmentView;

/**
 * Created by muhanxi on 17/7/5.
 */

public class RegisterInforFragmentPresenter extends BasePresenter<RegisterInforFragmentView> {


    private RegisterInforFragmentModel registerInforFragmentModel ;
    public RegisterInforFragmentPresenter(){
        registerInforFragmentModel = new RegisterInforFragmentModelImpl();
    }



    public void vaildInfor(String phone,String nickname,String sex,String age,String area,String introduce,String password){


        //非空判断

        registerInforFragmentModel.getData(phone, nickname, sex, age, area, introduce, password, new RegisterInforFragmentModel.RegisterInforFragmentDataListener() {
            @Override
            public void onSuccess(RegisterBean registerBean) {


                view.registerSuccess(registerBean);

            }

            @Override
            public void onFailed(int code) {

                view.registerFailed(code);
            }
        });






    }


}
