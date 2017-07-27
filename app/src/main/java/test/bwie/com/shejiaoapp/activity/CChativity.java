package test.bwie.com.shejiaoapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.model.EaseDefaultEmojiconDatas;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenu;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenuBase;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.adapter.ChatMeAdapter;
import test.bwie.com.shejiaoapp.base.IActivity;
import test.bwie.com.shejiaoapp.base.IApplication;
import test.bwie.com.shejiaoapp.speex.SpeexPlayer;
import test.bwie.com.shejiaoapp.speex.SpeexRecorder;
import test.bwie.com.shejiaoapp.utils.emoj.EaseSmileUtils;
import test.bwie.com.shejiaoapp.utils.PreferencesUtils;
import test.bwie.com.shejiaoapp.utils.SDCardUtils;
import test.bwie.com.shejiaoapp.widget.KeyBoardHelper;

public class CChativity extends IActivity implements KeyBoardHelper.OnKeyBoardStatusChangeListener {
    @BindView(R.id.chat_back)
    ImageView chatBack;
    @BindView(R.id.chat_username)
    TextView chatUsername;
    @BindView(R.id.chat_listview)
    RecyclerView chatListview;
    @BindView(R.id.chat_voice)
    CheckBox chatVoice;
    @BindView(R.id.chat_et)
    EditText chatEt;

    @BindView(R.id.chat_express)
    CheckBox chatExpress;
    @BindView(R.id.chat_plus)
    CheckBox chatPlus;
    @BindView(R.id.chat_btn_sendtext)
    Button chatBtnSendtext;
    @BindView(R.id.chat_bt)
    Button chatBt;
    EaseEmojiconMenuBase emojiconMenu;
    @BindView(R.id.buttom_layout_view)
    LinearLayout buttomLayoutView;
    private ChatMeAdapter adapter;
    private String fileName;
    private String filePath;

    private List<EaseEmojiconGroupEntity> emojiconGroupList = new ArrayList<>();
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    //发送过来的消息
                    List<EMMessage> em = (List<EMMessage>) msg.obj;

                    //要进行循环
                    for (int i = 0; i < em.size(); i++) {

                        //吧循环的数据添加带集合去
                        list.add(em.get(i));
                        adapter.notifyDataSetChanged();   //在子线程刷新数据，，不能在主线程刷新
                    }
                    break;
            }
        }
    };
    private EMMessageListener msgListener;
    private List<EMMessage> list;
    private int kh;
    private int chatId;
    private int id;
    private int flag = 1;

    private SpeexRecorder recorderInstance;
    private EMMessage emMessage;

    private long startTimer;
    private LinearLayout liner_more;
    private ImageView imageview_vedio;
    private PopupWindow popupWindow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cchativity);
        ButterKnife.bind(this);


        //得到的id，id就是要聊天的对象
        final Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);


        liner_more = (LinearLayout) findViewById(R.id.liner_more);  //加载
        imageview_vedio = (ImageView) findViewById(R.id.imageview_vedio);   //视屏聊天


        list = new ArrayList<>();
        buttomLayoutView.setTag(2);

        receive();  //接收到的消息

        //布局管理器
        chatListview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatMeAdapter(this, list);  //设置适配器
        chatListview.setAdapter(adapter);

        initEmoje(null);  //表情
        initLisitener();


        //视屏聊天的监听
        imageview_vedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CChativity.this, VideoActivity.class);
                intent.putExtra("uid", id);  //吧id(id就是聊天的那个人)发送给 VideoActivity
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });


        KeyBoardHelper keyBoardHelper = new KeyBoardHelper(this);
        keyBoardHelper.onCreate();
        keyBoardHelper.setOnKeyBoardStatusChangeListener(this);

        //键盘的高度
        int keyHeight = PreferencesUtils.getValueByKey(this, "kh", 300);
        if (keyHeight == 300) {
            keyHeight = kh;
        }

        //加载表情的高度
        EaseEmojiconMenu.LayoutParams params = (EaseEmojiconMenu.LayoutParams) buttomLayoutView.getLayoutParams();
        params.height = keyHeight;
        buttomLayoutView.setLayoutParams(params);  //设置键盘和表情的高度


        //加载加号的高度
        EaseEmojiconMenu.LayoutParams param = (EaseEmojiconMenu.LayoutParams) liner_more.getLayoutParams();
        param.height = keyHeight;
        liner_more.setLayoutParams(param);  //设置键盘和表情的高度


        //输入聊天信息的输入框
        chatEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatExpress.isChecked() || chatPlus.isChecked()) {
                    chatExpress.setChecked(false);
                    chatPlus.setChecked(false);
                    setKeyBoardModelPan();  //设置键盘的类型
                    liner_more.setVisibility(View.GONE);
                } else {
                    setKeyBoardModelResize();
                }
            }
        });

        chatEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                chatBtnSendtext.setVisibility(View.VISIBLE);
                chatPlus.setVisibility(View.GONE);
                liner_more.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    chatBtnSendtext.setVisibility(View.GONE);
                    chatPlus.setVisibility(View.VISIBLE);
                    liner_more.setVisibility(View.GONE);
                }
            }
        });

        //表情的点击
        chatExpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setKeyBoardModelPan();
                if (chatVoice.isChecked()) {
                    chatVoice.setChecked(false);
                    chatBt.setVisibility(View.GONE);
                    chatEt.setVisibility(View.VISIBLE);
                }
                if (((CheckBox) v).isChecked()) {

                    // 显示表情
                    buttomLayoutView.setVisibility(View.VISIBLE);
                    buttomLayoutView.setTag(1);
                    chatPlus.setChecked(false);

                    liner_more.setVisibility(View.GONE); //加号加载更多隐藏
                    hidenKeyBoard(chatEt);
                } else {
                    //  显示键盘
                    showKeyBoard(chatEt);
                }

            }
        });


        //加号的监听
        chatPlus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                setKeyBoardModelPan();  //键盘的隐藏

                Log.d("ChatActivity", "((CheckBox) v).isChecked():" + ((CheckBox) v).isChecked());
                if (chatVoice.isChecked()) {
                    chatVoice.setChecked(false);  //按住说话为false
                    chatBt.setVisibility(View.GONE);  //按住说话隐藏
                    chatEt.setVisibility(View.VISIBLE); //edittext显示
                }
                if (chatPlus.isChecked()) {

                    liner_more.setVisibility(View.VISIBLE);  //显示加载更多
                    liner_more.setTag(3);
                    hidenKeyBoard(chatEt);
                    chatExpress.setChecked(false);  //表情false
                    buttomLayoutView.setVisibility(View.GONE); //表情隐藏
                } else {

                    liner_more.setVisibility(View.GONE);
                }

            }
        });

        setKeyBoardModelResize();
        String chatUserName = PreferencesUtils.getValueByKey(this, "chatUserName", "");
        chatUsername.setText(chatUserName);

//////////////////------------------------------------------------------------

        //点击的播放语音的监听事件
        adapter.setmOnItemClickListener(new ChatMeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SpeexPlayer player = new SpeexPlayer(fileName, handler);
                player.startPlay();
            }
        });

        //按住说话的触摸监听事件（录音）
        chatBt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   //按下
                        startPlay();   //开始录语音
                        chatBt.setBackgroundColor(Color.GRAY);  //按下的时候的背景颜色

                        showWindow(v);  //popupWindow开始

                        break;


                    case MotionEvent.ACTION_UP:   //抬起  //抬起的时候发送信息

                        popupWindow.dismiss(); //popupWindow关闭

                        recorderInstance.setRecording(false); //发送第一次语音可以，第二次就不可以，要添加这行代码
                        //filePath为语音文件路径，length为录音时间(秒)
                        //得到一个时间值，结束的时间值

                        long l = event.getEventTime() - event.getDownTime() / 1000;
                        //抬起的时间 减去 按下的时间 除以秒 得到语音的时间长
                        EMMessage message = EMMessage.createVoiceSendMessage(fileName, (int) l, id + "");  //id是给谁发送
                        EMClient.getInstance().chatManager().sendMessage(message);
                        EMClient.getInstance().chatManager().setVoiceMessageListened(message);
                        EMClient.getInstance().chatManager().updateMessage(message);

                        //写个监听发送语音消息
                        message.setMessageStatusCallback(new EMCallBack() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(int i, String s) {

                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                        //吧消息添加到list里面去，刷新适配器
                        list.add(message);
                        adapter.notifyDataSetChanged();

                        chatBt.setBackgroundColor(Color.WHITE);//抬起的时候的背景颜色
                        break;
                }

                return false;
            }
        });






    }


    //弹起popupWindow
    private void showWindow(View parent) {

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.layout_popupwindow, null);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            ImageView   imageyuyin_id = (ImageView) view.findViewById(R.id.imageyuyin_id);
            imageyuyin_id.setImageResource(R.drawable.animation_yuyin);
            AnimationDrawable animationDrawable = (AnimationDrawable)  imageyuyin_id.getDrawable();
            animationDrawable.start();



        }

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAtLocation(parent,Gravity.CENTER,0,0);


    }


    //开始录语音
    public void startPlay() {
        filePath = Environment.getExternalStorageDirectory() + File.separator + SDCardUtils.DLIAO;
        System.out.println("filePath:" + filePath);
        File file = new File(filePath + "/");
        System.out.println("file:" + file);
        if (!file.exists()) {
            file.mkdirs();
        }
        startTimer = System.currentTimeMillis();
        fileName = file + File.separator + System.currentTimeMillis() + ".spx";
        System.out.println("保存文件名：＝＝ " + fileName);
        //recorderInstance
        recorderInstance = new SpeexRecorder(fileName, handler);
        Thread th = new Thread(recorderInstance);
        th.start();   //开始录音
        recorderInstance.setRecording(true);   //true是luyin，false是暂停
    }

    //////////////////-----------------------------------------------

    //添加所有的表情
    private void initEmoje(List<EaseEmojiconGroupEntity> emojiconGroupList) {
        if (emojiconMenu == null) {


            emojiconMenu = (EaseEmojiconMenu) View.inflate(CChativity.this, R.layout.ease_layout_emojicon_menu, null);

            //动态修改底部view 的高度 (表情 符号 view 的高度)

            if (emojiconGroupList == null) {
                emojiconGroupList = new ArrayList<>();
                emojiconGroupList.add(new EaseEmojiconGroupEntity(R.mipmap.ee_1, Arrays.asList(EaseDefaultEmojiconDatas.getData())));

            }
            ((EaseEmojiconMenu) emojiconMenu).init(emojiconGroupList);
        }
        buttomLayoutView.addView(emojiconMenu);   //添加所有的表情
    }


    //添加表情
    private void initLisitener() {
        // emojicon menu
        emojiconMenu.setEmojiconMenuListener(new EaseEmojiconMenuBase.EaseEmojiconMenuListener() {

            @Override
            public void onExpressionClicked(EaseEmojicon emojicon) {
                if (emojicon.getType() != EaseEmojicon.Type.BIG_EXPRESSION) {
                    if (emojicon.getEmojiText() != null) {
                        chatEt.append(EaseSmileUtils.getSmiledText(CChativity.this, emojicon.getEmojiText()));
                    }
                }
            }

            @Override
            public void onDeleteImageClicked() {
                if (!TextUtils.isEmpty(chatEt.getText())) {
                    KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                    chatEt.dispatchKeyEvent(event);
                }
            }
        });

    }

    ////////////////////---------------------------------------------------------------

    @Override
    public void OnKeyBoardPop(int keyBoardheight) {
        int keyHeight = PreferencesUtils.getValueByKey(this, "kh", 300);
        if (keyHeight != keyBoardheight) {
            PreferencesUtils.addConfigInfo(this, "kh", keyBoardheight);
            kh = keyBoardheight;
        }
    }

    //关闭键盘
    @Override
    public void OnKeyBoardClose(int oldKeyBoardheight) {
        int tag = (int) buttomLayoutView.getTag();
        if ((tag == 1 && !chatPlus.isChecked() && !chatExpress.isChecked())) {
            buttomLayoutView.setVisibility(View.GONE);
            buttomLayoutView.setTag(2);
        }

        if (tag == 2) {
            chatExpress.setChecked(false);
            chatPlus.setChecked(false);
        }
    }


    //
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.out.println("chatTitle = onBack KEYCODE_BACK");
            if ((int) buttomLayoutView.getTag() == 1) {
                buttomLayoutView.setVisibility(View.GONE);
                buttomLayoutView.setTag(2);
                chatExpress.setChecked(false);
                chatPlus.setChecked(false);
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }

        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void setKeyBoardModelPan() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public void setKeyBoardModelResize() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }


    //隐藏键盘

    public void hidenKeyBoard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }

    public void showKeyBoard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

    }


///////////////-----------------------------------------------------------------

    //发送的消息
    public void setTextMessage() {
        // int userId = PreferencesUtils.getValueByKey(this, "userId", 1);

        //第三个参数是要给谁发的id
        chatId = PreferencesUtils.getValueByKey(this, "chatId", 1);
        Log.d("ChatActivity", "chatId:" + chatId);
        emMessage = EMMessage.createTxtSendMessage(chatEt.getText().toString(), id + "");
        //id + ""  就是添加好友传值过来的id
        EMClient.getInstance().chatManager().sendMessage(emMessage);

        Log.d("vvvvvvv", emMessage + "   ");
        emMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                System.out.println("emMessage = onSuccess ");
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        list.add(emMessage);
        chatListview.scrollToPosition(adapter.getItemCount() - 1);  //数据显示在最后一条,不在滑动显示出来
        adapter.notifyDataSetChanged();
    }


    ////////////////////////------------------------------------------------------

    //收消息
    public void receive() {
        //收到消息
        //收到透传消息
        //收到已读回执
        //收到已送达回执
        //消息状态变动
        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {

                //收到的消息不要再子线程中刷新
                //发送到主线程进行刷新
                Message msg = new Message();
                msg.what = 0;
                msg.obj = messages;
                handler.sendMessage(msg);


                //收到消息
                System.out.println("onMessageReceived messages = " + messages);

            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
                System.out.println("onCmdMessageReceived messages = " + messages);

            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
                System.out.println("onMessageRead messages = " + messages);

            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
                System.out.println("onMessageDelivered messages = " + message);

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
                System.out.println("onMessageChanged messages = " + message);

            }
        };

        EMClient.getInstance().chatManager().addMessageListener(msgListener);

    }

    @OnClick({R.id.chat_back, R.id.chat_voice, R.id.chat_btn_sendtext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chat_back:
                finish();
                break;

            case R.id.chat_voice:

                setKeyBoardModelResize();
                if (chatVoice.isChecked()) {
                    chatBt.setVisibility(View.VISIBLE);
                    chatEt.setVisibility(View.GONE);
                    hidenKeyBoard(chatEt);
                    if ((int) buttomLayoutView.getTag() == 1) {
                        buttomLayoutView.setVisibility(View.GONE);
                        buttomLayoutView.setTag(2);
                        chatExpress.setChecked(false);
                        chatPlus.setChecked(false);
                    }
                } else {
                    chatBt.setVisibility(View.GONE);
                    chatEt.setVisibility(View.VISIBLE);
                    showKeyBoard(chatEt);
                }

                break;


            //发送信息
            case R.id.chat_btn_sendtext:
                setTextMessage();
                chatEt.setText("");  //输入的信息之后，，输入框设置为空
                break;
        }
    }

    //销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);

    }


}
