package test.bwie.com.shejiaoapp.cipher.rsa;

import android.content.Context;


import java.util.Random;


public class RsaUtils {

    public static RsaUtils rsaUtils ;

    private  RsaUtils(){

    }

    public  static RsaUtils getInstance(){
        if (rsaUtils == null) {
            rsaUtils = new RsaUtils();
        }
        return rsaUtils;
    }


    public String encryptContent(Context context,String content) {

        String afterEncrypt = Rsa.encryptByPublic(content);




        return afterEncrypt;
    }


    /**
     * 产生随机16位数
     * @return
     */
    public  String createRandom(){
        return getStringRandom(16);
    }

    public String createRsaSecret(Context context,String random){
        long currentTimer = System.currentTimeMillis();
        String afterEncrypt = Rsa.encryptByPublic(String.valueOf(random));
        System.out.println("afterEncrypt = " + afterEncrypt);
        afterEncrypt = afterEncrypt.replaceAll("\\r|\\n","").trim();
        return afterEncrypt;
    }


    public static  String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }


}
