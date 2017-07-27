package test.bwie.com.shejiaoapp.cipher;

import android.text.TextUtils;


import java.security.MessageDigest;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * 请求传输加密
 *  把map 参数 排序，去掉中文, 拼接成字符串  调用 jni （jni 在参数基础之上 ） md5（appkey＝111&key1=value1）
 *
 */
public class DesEncrypt {

	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	
	/**
	 * 加密
	 * @param encryptString
	 * @param encryptKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptDES(String encryptString, String encryptKey)
			throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
		return Base64.encode(encryptedData);
	}
	
	/**
	 * 解密
	 * @param decryptString
	 * @param decryptKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptDES(String decryptString, String decryptKey)
            throws Exception {
        byte[] byteMi = Base64.decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);

        return new String(decryptedData);
    }
	
	
	/**
	 * 
	 * @param appKey
	 * @param
	 * @param
	 * @returnmd5(key=123456&mobile=18611&type=11)

	 */
	public static String getSing(String appKey, Map<String,String> map){
		String result = "" ;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("appkey=");
			sb.append(appKey.trim()+"&");
			Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				sb.append(entry.getKey()+"=");
				sb.append(entry.getValue()+"&");
			}
			result = sb.toString().substring(0, sb.toString().length()-1).replaceAll("'","‘" ).replaceAll("\"","“").replaceAll("<", "＜").replace(">", "＞") ;
			result =  getMD5(result.trim());

		} catch (Exception e) {
			result = null ;
			e.printStackTrace();
		}finally{
			return result.toLowerCase();
		}
	}
	
	/**
	 * 
	 * @return 所有参数名称  加入
	 */
	public static Map<String,String> sortString(Map<String,String> map){
		Map<String,String> mapResult = new TreeMap<String, String>();
		if(map == null){
			return mapResult;
		}
		if(map.size() ==0){
			return mapResult;
		}
		String[] str = new String[map.size()];
		int i= 0;
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			str[i] = entry.getKey();
			i = i +1 ;
		}
		i = 0 ;
		Comparator<Object> comp = Collator.getInstance(java.util.Locale.CHINESE);
		Arrays.sort(str, comp);
		if(str != null && str.length != 0){
			for(int j=0;j<str.length;j++){
				if(!isChineseChar(map.get(str[j]))){
					mapResult.put(str[j], map.get(str[j]));
				}
			}
		}
//		System.out.println("result map:"+mapResult.toString());
		return mapResult ;
	}
	
	//判断是否是汉字
	public static boolean isChineseChar(String str) {
		boolean temp = false;
		try {
			if(TextUtils.isEmpty(str) ){
				return false;
			}
			Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
			Matcher m = p.matcher(str);
			if (m.find()) {
                temp = true;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}
	
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
