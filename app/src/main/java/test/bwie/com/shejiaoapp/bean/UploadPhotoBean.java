package test.bwie.com.shejiaoapp.bean;

/**
 * Created by muhanxi on 17/7/6.
 */

public class UploadPhotoBean {


    /**
     * headImagePath : http://qhb.2dyt.com/MyInterface/images/4be27cc5-9a47-4775-9567-94dae488b560.jpg
     * result_message : 上传成功
     * result_code : 200
     */

    private String headImagePath;
    private String result_message;
    private int result_code;

    public String getHeadImagePath() {
        return headImagePath;
    }

    public void setHeadImagePath(String headImagePath) {
        this.headImagePath = headImagePath;
    }

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
}
