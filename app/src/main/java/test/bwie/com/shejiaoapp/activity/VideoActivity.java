package test.bwie.com.shejiaoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.EMNoActiveCallException;
import com.hyphenate.exceptions.EMServiceNotReadyException;
import com.hyphenate.media.EMCallSurfaceView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.base.IActivity;

import static com.superrtc.sdk.VideoView.EMCallViewScaleMode.EMCallViewScaleModeAspectFill;

public class VideoActivity extends IActivity {
    @BindView(R.id.surface_big)
    EMCallSurfaceView surfaceBig;
    @BindView(R.id.surface_small)
    EMCallSurfaceView surfaceSmall;
    @BindView(R.id.tel_activity_hint)
    TextView telActivityHint;
    @BindView(R.id.tel_activity_accept)
    ImageView telActivityAccept;
    @BindView(R.id.tel_activity_handup)
    ImageView telActivityHandup;
    @BindView(R.id.tel_activity_disaccept)
    ImageView telActivityDisaccept;
    private int  uid;
    private int type;
    //开始聊天界面
    //int type : 1 拨打视频电话  2 接受视频电话
    public static void startTelActivity(int type, String uid, Context context) {

        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("uid", uid);
        context.startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ButterKnife.bind(this);

        //得到id
        uid = getIntent().getExtras().getInt("uid");
        type = getIntent().getExtras().getInt("type");

        //让自己的图像  显示在最上面
        surfaceSmall.getHolder().setFormat(PixelFormat.TRANSPARENT);
        surfaceSmall.setZOrderOnTop(true);


        //自己头像的监听事件
        surfaceSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //满屏
                surfaceSmall.setScaleMode(EMCallViewScaleModeAspectFill);

            }
        });


        if (1 == type){
            //拨打电话
            telActivityAccept.setVisibility(View.GONE);
            telActivityDisaccept.setVisibility(View.GONE);
            telActivityHandup.setVisibility(View.VISIBLE);

            //拨打视频电话
            try {
                EMClient.getInstance().callManager().makeVideoCall(uid+"");
            } catch (EMServiceNotReadyException e) {
                e.printStackTrace();
            }
        }else {
            telActivityAccept.setVisibility(View.VISIBLE);
            telActivityDisaccept.setVisibility(View.VISIBLE);
            telActivityHandup.setVisibility(View.GONE);
        }

        EMClient.getInstance().callManager().setSurfaceView(surfaceSmall,surfaceBig);
        connectState();
    }
    public void connectState(){
        EMClient.getInstance().callManager().addCallStateChangeListener(new EMCallStateChangeListener() {
            @Override
            public void onCallStateChanged(CallState callState, CallError error) {
                switch (callState) {
                    case CONNECTING: // 正在连接对方

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                telActivityHint.setText("正在连接");

                            }
                        });
                        break;
                    case CONNECTED: // 双方已经建立连接

                        break;


                    case ACCEPTED: // 电话接通成功
                        //用线程来连接
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                telActivityHint.setText("通话中");

                            }
                        });


                        break;
                    case DISCONNECTED: // 电话断了
                        Toast.makeText(VideoActivity.this,"通话已结束",Toast.LENGTH_SHORT).show();
                        finish();

                        break;
                    case NETWORK_UNSTABLE: //网络不稳定
                        if(error == CallError.ERROR_NO_DATA){
                            //无通话数据
                        }else{
                        }
                        break;
                    case NETWORK_NORMAL: //网络恢复正常
                        Toast.makeText(VideoActivity.this,"网络恢复正常",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @OnClick({R.id.tel_activity_accept, R.id.tel_activity_handup, R.id.tel_activity_disaccept})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tel_activity_accept:   //别人给打过来的接听
                try {
                    EMClient.getInstance().callManager().answerCall();
                } catch (EMNoActiveCallException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.tel_activity_handup:   //别人给打过的挂断
                try {
                    //挂断
                    EMClient.getInstance().callManager().endCall();
                } catch (EMNoActiveCallException e) {
                    e.printStackTrace();
                }
                finish();
                break;
            case R.id.tel_activity_disaccept:   //给别人拨打视屏的挂断
                try {
                    EMClient.getInstance().callManager().rejectCall();
                } catch (EMNoActiveCallException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // finish();
    }
}
