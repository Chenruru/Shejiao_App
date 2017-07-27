package test.bwie.com.shejiaoapp.base;

import android.os.Bundle;

import test.bwie.com.shejiaoapp.R;

//<V,T>泛型里的之所以是v和t，因为我们不知道所需要什么参数，所以用v和t代用
public abstract class BaseMvpActivity<V,T extends BasePresenter<V>> extends IActivity {


    public T presenter;
    public abstract T initPresenter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_mvp);
        presenter=initPresenter();
        presenter.attach((V) this);
    }

    //获取焦点的时候当前的视图
    @Override
    protected void onResume() {
        super.onResume();

    }

    //销毁的时候
    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }
}
