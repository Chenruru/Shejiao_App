package test.bwie.com.shejiaoapp.bean;

/**
 * Created by muhanxi on 17/7/5.
 */

public class RegisterBean {


    /**
     * result_message : success
     * <p>
     * result_code : 200
     * <p>
     * data : {"createtime":1499245917699,"phone":"18511085106","area":"安徽省 蚌埠市 蚌山区","yxpassword":"5a5X9vug","nickname":"tttt","userId":6,"introduce":"&77777","gender":"男","password":"eeeeeee"}
     */


    private String result_message;

    private int result_code;

    private DataBean data;


    public String getResult_message() {

        return result_message;

    }


    public void setResult_message(String result_message) {

        this.result_message = result_message;

    }


    public int getResult_code() {

        return result_code;

    }


    public void setResult_code(int result_code) {

        this.result_code = result_code;

    }


    public DataBean getData() {

        return data;

    }


    public void setData(DataBean data) {

        this.data = data;

    }


    public static class DataBean {

        /**
         * createtime : 1499245917699
         * <p>
         * phone : 18511085106
         * <p>
         * area : 安徽省 蚌埠市 蚌山区
         * <p>
         * yxpassword : 5a5X9vug
         * <p>
         * nickname : tttt
         * <p>
         * userId : 6
         * <p>
         * introduce : &77777
         * <p>
         * gender : 男
         * <p>
         * password : eeeeeee
         */


        private long createtime;

        private String phone;

        private String area;

        private String yxpassword;

        private String nickname;

        private int userId;

        private String introduce;

        private String gender;

        private String password;


        public long getCreatetime() {

            return createtime;

        }


        public void setCreatetime(long createtime) {

            this.createtime = createtime;

        }


        public String getPhone() {

            return phone;

        }


        public void setPhone(String phone) {

            this.phone = phone;

        }


        public String getArea() {

            return area;

        }


        public void setArea(String area) {

            this.area = area;

        }


        public String getYxpassword() {

            return yxpassword;

        }


        public void setYxpassword(String yxpassword) {

            this.yxpassword = yxpassword;

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


        public String getIntroduce() {

            return introduce;

        }


        public void setIntroduce(String introduce) {

            this.introduce = introduce;

        }


        public String getGender() {

            return gender;

        }


        public void setGender(String gender) {

            this.gender = gender;

        }


        public String getPassword() {

            return password;

        }


        public void setPassword(String password) {

            this.password = password;

        }


    }

}
