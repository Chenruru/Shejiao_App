package test.bwie.com.shejiaoapp.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by muhanxi on 17/7/10.
 */
@Entity
public class DataBean implements Serializable{

    /**
     * area : 安徽省-安庆市-枞阳县
     * picWidth : 510
     * createtime : 1499485141560
     * picHeight : 507
     * gender : 男
     * lng : 0
     * introduce : hhaha
     * imagePath : http://dyt-pict.oss-cn-beijing.aliyuncs.com/dliao/default_man.jpg
     * userId : 23
     * yxpassword : U383lo04
     * password :
     * lasttime : 1499485141560
     * phone : 18256127721
     * nickname : 安徽
     * age : 18
     * lat : 0
     */
    @Id(autoincrement = true)
    private Long id;
    
    private String area;
    private int picWidth;
    private long createtime;
    private int picHeight;
    private String gender;
    private double lng;
    private String introduce;
    private String imagePath;
    private int userId;
    private String yxpassword;
    private String password;
    private long lasttime;
    private String phone;
    private String nickname;
    private String age;
    private double lat;




    @Generated(hash = 825355436)
    public DataBean(Long id, String area, int picWidth, long createtime,
            int picHeight, String gender, double lng, String introduce,
            String imagePath, int userId, String yxpassword, String password,
            long lasttime, String phone, String nickname, String age, double lat) {
        this.id = id;
        this.area = area;
        this.picWidth = picWidth;
        this.createtime = createtime;
        this.picHeight = picHeight;
        this.gender = gender;
        this.lng = lng;
        this.introduce = introduce;
        this.imagePath = imagePath;
        this.userId = userId;
        this.yxpassword = yxpassword;
        this.password = password;
        this.lasttime = lasttime;
        this.phone = phone;
        this.nickname = nickname;
        this.age = age;
        this.lat = lat;
    }
    @Generated(hash = 908697775)
    public DataBean() {
    }




    public String getArea() {
        return this.area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public int getPicWidth() {
        return this.picWidth;
    }
    public void setPicWidth(int picWidth) {
        this.picWidth = picWidth;
    }
    public long getCreatetime() {
        return this.createtime;
    }
    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }
    public int getPicHeight() {
        return this.picHeight;
    }
    public void setPicHeight(int picHeight) {
        this.picHeight = picHeight;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public double getLng() {
        return this.lng;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public String getIntroduce() {
        return this.introduce;
    }
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
    public String getImagePath() {
        return this.imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public int getUserId() {
        return this.userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getYxpassword() {
        return this.yxpassword;
    }
    public void setYxpassword(String yxpassword) {
        this.yxpassword = yxpassword;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public long getLasttime() {
        return this.lasttime;
    }
    public void setLasttime(long lasttime) {
        this.lasttime = lasttime;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getAge() {
        return this.age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public double getLat() {
        return this.lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

  

}
