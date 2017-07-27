package test.bwie.com.shejiaoapp.mview;

import test.bwie.com.shejiaoapp.bean.RegisterBean;

/**
 * date: 2017/7/6
 * author:陈茹
 * 类的用途:
 */

public interface RegisterInforFragmentView {
    public void registerSuccess(RegisterBean registerBean);
    public void registerFailed(int code);
}
