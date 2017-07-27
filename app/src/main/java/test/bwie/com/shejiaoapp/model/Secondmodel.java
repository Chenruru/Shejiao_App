package test.bwie.com.shejiaoapp.model;

import test.bwie.com.shejiaoapp.bean.IndexBean;

/**
 * date: 2017/7/14
 * author:陈茹
 * 类的用途:
 */

public interface Secondmodel {

    public void getdatesecond(secondDataListener secondDataListener);

    public interface secondDataListener{
        public void onSuccess(IndexBean indexBean);
        public void onFailed(int code);
    }
}
