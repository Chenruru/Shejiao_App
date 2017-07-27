package test.bwie.com.shejiaoapp.core;

import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by muhanxi on 17/7/4.
 */

public class SortUtils {


    /**
     * 把map 拼接成了字符串
     * @param map
     * @return
     */
    public static String getMapResult(Map map) {
        String result = "" ;
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            sb.append(entry.getKey()+"=");
            sb.append(entry.getValue()+"&");
        }
        result = sb.toString().substring(0, sb.toString().length()-1) ;
        return  result ;

    }


    /**
     * map 进行排序
     * @param map
     * @return
     */
    public static Map<String,String> sortString(Map<String,String> map){
        //有序
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
        // str timer username password
//        0 = "password"
//        1 = "timer"
//        2 = "username"
        i = 0 ;
        Comparator<Object> comp = Collator.getInstance(java.util.Locale.CHINESE);
        Arrays.sort(str, comp);
        if(str != null && str.length != 0){
            for(int j=0;j<str.length;j++){
//                str[j] map key
//                map.get(str[j])) map value
                if(!isChineseChar(map.get(str[j]))){
                    mapResult.put(str[j], map.get(str[j]));
                }
            }
        }
        return mapResult ;
    }

    //判断是否是汉字
    public static boolean isChineseChar(String str) {
        boolean temp = false;
        try {
            if(str == null || "".equals(str) ){
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



}
