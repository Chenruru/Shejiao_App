package test.bwie.com.shejiaoapp.presenter;

import test.bwie.com.shejiaoapp.base.BasePresenter;
import test.bwie.com.shejiaoapp.bean.IndexBean;
import test.bwie.com.shejiaoapp.model.FirstFragmentModel;
import test.bwie.com.shejiaoapp.model.FirstFragmentModelImpl;
import test.bwie.com.shejiaoapp.mview.FirstFragmentView;


/**
 * date: 2017/7/11
 * author:陈茹
 * 类的用途:
 */

public class FirstFragmentPresenter extends BasePresenter<FirstFragmentView> {

    private FirstFragmentModel firstFragmentModel ;

    public FirstFragmentPresenter(){
        firstFragmentModel = new FirstFragmentModelImpl();
    }

    public void getData(){

        firstFragmentModel.getData(new FirstFragmentModel.DataListener() {
            @Override
            public void onSuccess(IndexBean indexBean) {
                try {
                    view.success(indexBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int code) {

            }
        });

    }

}
