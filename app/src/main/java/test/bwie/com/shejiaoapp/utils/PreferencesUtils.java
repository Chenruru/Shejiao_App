package test.bwie.com.shejiaoapp.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * sharepreferences的工具类
 */
public class PreferencesUtils {

    public static  final String USER = "userinformation";


    public static <T> void addConfigInfo( Context context, String key, T value ) {
        try {
            if ( notNull( value ) ) {
                SharedPreferences mShareConfig =
                    context.getSharedPreferences( USER, Context.MODE_PRIVATE );
                Editor conEdit = mShareConfig.edit();
                if ( value instanceof String ) {
                    conEdit.putString( key.trim(), ( (String) value ).trim() );
                } else if ( value instanceof Long ) {
                    conEdit.putLong( key, (Long) value );
                } else if ( value instanceof Boolean ) {
                    conEdit.putBoolean( key, (Boolean) value );
                }else if(value instanceof Integer){
                    conEdit.putInt(key,  (Integer)value );
                }else if(value instanceof Float){
                    conEdit.putFloat(key, (Float) value);
                }
                conEdit.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T getValueByKey( Context context, String key, T defValue ) {
        T value = null;
        try {
            if ( notNull( key ) ) {
                SharedPreferences mShareConfig =
                    context.getSharedPreferences(USER, Context.MODE_PRIVATE);
                if ( null != mShareConfig ) {
                    if (defValue instanceof  String) {
                        value = (T) mShareConfig.getString(key, (String)defValue);
                    } else if (defValue instanceof Long) {
                        value = (T) Long.valueOf( mShareConfig.getLong(key, (Long) defValue) );
                    } else if (defValue instanceof Boolean) {
                        value = (T) Boolean.valueOf( mShareConfig.getBoolean(key, (Boolean) defValue) );
                    } else if (defValue instanceof Integer) {
                        value = (T) Integer.valueOf( mShareConfig.getInt(key, (Integer)defValue) );
                    }else if (defValue instanceof Float) {
                        value = (T) Float.valueOf( mShareConfig.getFloat(key, (Float) defValue) );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 设置一个集合
     *
     */
    public static void setMapKey(Context context,Map<String, Object> informations) {
        try {
            SharedPreferences mShareConfig =
                    context.getSharedPreferences(USER, Context.MODE_PRIVATE);
            Editor configEditor = mShareConfig.edit();
            Set<Map.Entry<String, Object>> entries = informations.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                Object obj = next.getValue();
                String key = next.getKey();
                if (key != null) {
                    if (obj instanceof String) {
                        configEditor.putString(key, obj.toString());
                    } else if (obj instanceof Boolean) {
                        configEditor.putBoolean(key, (Boolean) obj);
                    } else if (obj instanceof Integer) {
                        configEditor.putInt(key, (Integer) obj);
                    }else if (obj instanceof Float) {
                        configEditor.putFloat(key, (Float) obj);
                    }else if (obj instanceof Long) {
                        configEditor.putLong(key, (Long) obj);
                    }
                }
            }
            configEditor.commit();
            // 用完及时清空
            informations.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * object not null
     */
    public static boolean notNull( Object obj ) {
        if ( null != obj) {
            return true;
        }
        return false;
    }

//    /**
//     * save object to SharedPreferences file
//     * @param context
//     * @param clazz
//     * @param content
//     * 使用方法  serializeObject  saveObject
//     */
//    public static void saveObject(Context context,Class clazz,String content) {
//        mShareConfig =context.getSharedPreferences( USER, Context.MODE_PRIVATE );
//        Editor conEdit = mShareConfig.edit();
//        conEdit.putString(clazz.toString(), content);
//        conEdit.commit();
//    }
//
//    /**
//     * read object to memory
//     * @param context
//     * @param clazz
//     * @return
//     *  getObject deSerializationObject
//     */
//    public static String getObject(Context context,Class clazz) {
//        mShareConfig =context.getSharedPreferences( USER, Context.MODE_PRIVATE );
//        return mShareConfig.getString(clazz.toString(), null);
//    }


    /**
     * 保存实例对象
     * @param object
     * @return
     * @throws Exception
     */
    public static void saveSerializeObject(Context context,Object object){
        ByteArrayOutputStream byteArrayOutputStream = null ;
        ObjectOutputStream objectOutputStream = null;
        String serStr = "" ;
        try {

            byteArrayOutputStream = new ByteArrayOutputStream();

            objectOutputStream = new ObjectOutputStream(
                    byteArrayOutputStream);
            objectOutputStream.writeObject(object);

            serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");

            SharedPreferences mShareConfig =context.getSharedPreferences( USER, Context.MODE_PRIVATE );
            Editor conEdit = mShareConfig.edit();
            conEdit.putString(object.getClass().toString(), serStr);
            conEdit.commit();

        }catch (Exception e){
           e.printStackTrace();
        }finally {
            try {
                if(objectOutputStream != null){
                    objectOutputStream.close();
                }
                if(byteArrayOutputStream != null){
                    byteArrayOutputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();;
            }
        }

    }

    /**
     * 获取实例对象
     * @param clazz 对象的实例
     */
    public static Object getSerializationObject(Context context,Class clazz) {
        ByteArrayInputStream byteArrayInputStream = null ;
        ObjectInputStream objectInputStream = null ;
        Object object = null ;
        SharedPreferences mShareConfig =context.getSharedPreferences(USER, Context.MODE_PRIVATE );
        String str = mShareConfig.getString(clazz.toString(), null);
        if(TextUtils.isEmpty(str)){
            return object;
        }
        try{
            String redStr = java.net.URLDecoder.decode(str, "UTF-8");
            byteArrayInputStream = new ByteArrayInputStream(
                    redStr.getBytes("ISO-8859-1"));
            objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            object =  (Object) objectInputStream.readObject();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
           try {
               objectInputStream.close();
               byteArrayInputStream.close();
           }catch (Exception e){
               e.printStackTrace();
           }
            return object;
        }


    }


}
