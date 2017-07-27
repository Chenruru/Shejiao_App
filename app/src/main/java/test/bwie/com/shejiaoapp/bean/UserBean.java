package test.bwie.com.shejiaoapp.bean;

/**
 * date: 2017/7/13
 * author:陈茹
 * 类的用途:
 */

public class UserBean {
    private String name;
    private String ziliao;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZiliao() {
        return ziliao;
    }

    public void setZiliao(String ziliao) {
        this.ziliao = ziliao;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "name='" + name + '\'' +
                ", ziliao='" + ziliao + '\'' +
                '}';
    }
}
