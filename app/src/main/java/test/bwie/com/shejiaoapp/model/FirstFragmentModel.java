package test.bwie.com.shejiaoapp.model;

import test.bwie.com.shejiaoapp.bean.IndexBean;

/**
 * date: 2017/7/11
 * author:陈茹
 * 类的用途:
 */

public interface FirstFragmentModel {

    public void getData(DataListener dataListener);




    public interface DataListener{
        public void onSuccess(IndexBean indexBean);
        public void onFailed(int code);
    }
}
