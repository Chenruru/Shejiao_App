package test.bwie.com.shejiaoapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.activity.DengluActivity;
import test.bwie.com.shejiaoapp.activity.FanhuiActivity;
import test.bwie.com.shejiaoapp.base.IFragment;
import test.bwie.com.shejiaoapp.speex.SpeexPlayer;
import test.bwie.com.shejiaoapp.speex.SpeexRecorder;
import test.bwie.com.shejiaoapp.utils.PreferencesUtils;
import test.bwie.com.shejiaoapp.utils.SDCardUtils;


/**
 * A simple {@link } subclass.
 */
public class ThirdFragment extends IFragment {
    @BindView(R.id.textview_id_fourth)
    TextView textviewIdFourth;
    Unbinder unbinder;
    @BindView(R.id.btn_recoder)
    Button btnRecoder;
    @BindView(R.id.btn_unrecoder)
    Button btnUnrecoder;
    private SpeexRecorder recorderInstance;
    private String fileName;

    public ThirdFragment() {
        // Required empty public constructor
    }


    Handler handler = new Handler(){


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

            }
        }
    } ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_third, container, false);
        unbinder = ButterKnife.bind(this, view);

        String nickName = PreferencesUtils.getValueByKey(getActivity(), "nickname", "");

        textviewIdFourth.setText(nickName);


        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_recoder, R.id.btn_unrecoder, R.id.btn_playrecoder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_recoder:

                //先获取sd卡路径
                String filePath = Environment.getExternalStorageDirectory() + File.separator + SDCardUtils.DLIAO;
                System.out.println("filePath:" + filePath);
                //创建一个文件夹
                File file = new File(filePath  + "/");
                System.out.println("file:" + file);
                if (!file.exists()) {
                    file.mkdirs();
                }

                fileName = file + File.separator + System.currentTimeMillis() + ".spx";
                System.out.println("保存文件名：＝＝ " + fileName);
                recorderInstance = new SpeexRecorder(fileName, handler);
                Thread th = new Thread(recorderInstance);
                th.start();
                recorderInstance.setRecording(true);


                break;
            case R.id.btn_unrecoder:

                recorderInstance.setRecording(false);

                System.out.println("fileName = " + new File(fileName).length());


                break;
            case R.id.btn_playrecoder:

                SpeexPlayer player = new SpeexPlayer(fileName,handler);
                player.startPlay();

                break;
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences preferences = getActivity().getSharedPreferences("isfrit", Context.MODE_PRIVATE);
        boolean frit = preferences.getBoolean("frit", false);
        if (frit){

        }else {
            startActivity(new Intent(getActivity(), FanhuiActivity.class));
            getActivity().finish();
        }
    }



}
