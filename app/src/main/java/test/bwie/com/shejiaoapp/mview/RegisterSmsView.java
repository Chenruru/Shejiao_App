package test.bwie.com.shejiaoapp.mview;

/**
 * Created by muhanxi on 17/7/5.
 */

public interface RegisterSmsView {


    /**
     * 1 表示手机号码为空
     * 2 手机号码 不合法
     * @param type
     */
    public void phoneError(int type);



    // 显示倒计时
    public void showTimer();


    //下一个页面
    public void toNextPage();

}
