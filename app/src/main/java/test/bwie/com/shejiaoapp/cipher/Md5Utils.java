package test.bwie.com.shejiaoapp.cipher;

import java.security.MessageDigest;

/**
 * Created by muhanxi on 17/7/2.
 */

public class Md5Utils {


    // 32位   16位是从 32 位中间截取了16位，去掉前8后8
    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return getHashString(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }


}
