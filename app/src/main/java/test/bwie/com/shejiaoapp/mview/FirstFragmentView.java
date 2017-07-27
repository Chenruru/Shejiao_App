package test.bwie.com.shejiaoapp.mview;

import test.bwie.com.shejiaoapp.bean.IndexBean;

/**
 * date: 2017/7/11
 * author:陈茹
 * 类的用途:
 */

public interface FirstFragmentView {

    public void success(IndexBean indexBean);
    public void failed(int code);


}
