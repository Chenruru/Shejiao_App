package test.bwie.com.shejiaoapp.base;

/**
 * date: 2017/7/4
 * author:陈茹
 * 类的用途:
 */

public  abstract  class BasePresenter<T>{


    public T view;

    public void attach(T view){

        this.view=view;
    }

    public void detach()
    {
        this.view=null;
    }

}
