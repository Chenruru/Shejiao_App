package test.bwie.com.shejiaoapp.presenter;

import test.bwie.com.shejiaoapp.base.BasePresenter;
import test.bwie.com.shejiaoapp.bean.IndexBean;
import test.bwie.com.shejiaoapp.model.SecondFragmentmodel;
import test.bwie.com.shejiaoapp.model.Secondmodel;
import test.bwie.com.shejiaoapp.mview.FirstFragmentView;

/**
 * date: 2017/7/14
 * author:陈茹
 * 类的用途:
 */

public class Seconfragmentpresent extends BasePresenter<FirstFragmentView> {

    private SecondFragmentmodel secondFragmentmodel;

    public Seconfragmentpresent(){

        secondFragmentmodel = new SecondFragmentmodel();
    }
    public void getDataba(){
        secondFragmentmodel.getdatesecond(new Secondmodel.secondDataListener() {
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

                view.failed(code);
            }
        });
    }
}
