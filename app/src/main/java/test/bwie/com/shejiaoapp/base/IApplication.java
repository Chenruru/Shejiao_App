package test.bwie.com.shejiaoapp.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.getkeepsafe.relinker.ReLinker;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.util.NetUtils;
import com.mob.MobApplication;


import java.util.Iterator;
import java.util.List;
import java.util.Map;


import cn.smssdk.SMSSDK;
import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.db.DaoMaster;
import test.bwie.com.shejiaoapp.db.DaoSession;
import test.bwie.com.shejiaoapp.utils.AMapUtils;
import test.bwie.com.shejiaoapp.utils.PreferencesUtils;

/**
 * date: 2017/7/4
 * author:陈茹
 * 类的用途:
 */

public class IApplication extends MobApplication {



    public static IApplication application;
    private final static String dbName = "shejiaoapp.db";
    public static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        SMSSDK.initSDK(this, "1f32c26d9236f", "01c88f0d26ac4c26ebd115b6baf6614c");
        initJNI();
        aMap();
        initGreendao();


        //到个ReLinker依赖包
        //录音
        ReLinker.loadLibrary(this, "speex", new ReLinker.LoadListener() {
            @Override
            public void success() {
                System.out.println("播放成功oooooooooooooooooo" );
            }

            @Override
            public void failure(Throwable t) {
                System.out.println("播放失败oooooooooooooooooo" );
            }
        });


        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            //  Log.e(TAG, "enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        initEM();

    }



    public static IApplication getApplication() {
        if (application == null) {
            application = getApplication();
        }
        return application;
    }


    private void initJNI() {
        System.loadLibrary("core");
    }

    public void aMap() {
        AMapUtils.getInstance().startUtils(this);
    }


    public void initGreendao() {


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "dliao.db");
        DaoMaster master = new DaoMaster(helper.getWritableDatabase());
        //   加密
//        DaoMaster master = new DaoMaster(helper.getEncryptedWritableDb("1111"));

        daoSession = master.newSession();

    }

    public void initEM() {
        EMOptions options = new EMOptions();

        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        //        options.setAutoLogin(false);
        //初始化
        //        EMClient.getInstance().init(this, options);
        EaseUI.getInstance().init(this, options);

        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    //环信的关联
    public void eLogin(){
        String username = PreferencesUtils.getValueByKey(this,"uid",0) + "" ;
        String password = PreferencesUtils.getValueByKey(this,"yxpassword","0") ;

        EMClient.getInstance().login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                System.out.println("password = onSuccess" );
                Log.e("EMClient:======","环信关联成功");

                try{
                    Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();

                    for (int i=0;i<conversations.size();i++){
                        System.out.println("list = " + conversations.get(i));
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int i, String s) {
                System.out.println("password onError = " + i + " s " + s);
                Log.e("EMClient:======","环信关联失败");


            }

            @Override
            public void onProgress(int i, String s) {
                System.out.println("password onProgress = " + i);

            }
        });


    }
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }


    //环信的登录
    public void emLogin() {
        String username = PreferencesUtils.getValueByKey(this, "uid", 0) + "";
        String password = PreferencesUtils.getValueByKey(this, "yxpassword", "0");

        EMClient.getInstance().login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                System.out.println("password = onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                System.out.println("password onError = " + i + " s " + s);

            }

            @Override
            public void onProgress(int i, String s) {
                System.out.println("password onProgress = " + i);

            }
        });

    }


    //视屏来电的初始化
    public static void ring(){
        SoundPool soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC,0);

        soundPool.load(application, R.raw.avchat_ring,1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId,1, 1, 0, 0, 1);
            }
        });

    }


//    public static void callTo() {
//        SoundPool soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
//
//        soundPool.load(application, R.raw.avchat_connecting, 1);
//        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                soundPool.play(sampleId, 1, 1, 0, 0, 1);
//            }
//        });
//    }


}


