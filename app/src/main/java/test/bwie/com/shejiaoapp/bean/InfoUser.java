package test.bwie.com.shejiaoapp.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * date: 2017/7/15
 * author:陈茹
 * 类的用途:
 */

public class InfoUser {

    /**
     * result_message : success
     * data : {"area":"上海 黄浦区","lasttime":1500093020233,"createtime":1500085840120,"gender":"男","introduce":"弄的","imagePath":"http://qhb.2dyt.com/MyInterface/images/7b593828-ee02-4847-a774-2a2e5b14e0dd.jpg","nickname":"噢耶屋头有在真","userId":47,"relation":0,"photolist":[]}
     * result_code : 200
     */

    private String result_message;
    private DataBean data;
    private int result_code;

    public static InfoUser objectFromData(String str) {

        return new Gson().fromJson(str, InfoUser.class);
    }

    public String getResult_message() {
        return result_message;
    }

    public void setResult_message(String result_message) {
        this.result_message = result_message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public static class DataBean {
        /**
         * area : 上海 黄浦区
         * lasttime : 1500093020233
         * createtime : 1500085840120
         * gender : 男
         * introduce : 弄的
         * imagePath : http://qhb.2dyt.com/MyInterface/images/7b593828-ee02-4847-a774-2a2e5b14e0dd.jpg
         * nickname : 噢耶屋头有在真
         * userId : 47
         * relation : 0
         * photolist : []
         */

        private String area;
        private long lasttime;
        private long createtime;
        private String gender;
        private String introduce;
        private String imagePath;
        private String nickname;
        private int userId;
        private int relation;
        private List<?> photolist;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public long getLasttime() {
            return lasttime;
        }

        public void setLasttime(long lasttime) {
            this.lasttime = lasttime;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getRelation() {
            return relation;
        }

        public void setRelation(int relation) {
            this.relation = relation;
        }

        public List<?> getPhotolist() {
            return photolist;
        }

        public void setPhotolist(List<?> photolist) {
            this.photolist = photolist;
        }
    }
}
