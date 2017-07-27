package test.bwie.com.shejiaoapp.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import test.bwie.com.shejiaoapp.R;


public class MyToast {

    private  static Toast mToast;
    private static TextView textView;

    private MyToast(Context context, CharSequence text, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.ctoast, null);
        textView = (TextView) v.findViewById(R.id.textView1);
        textView.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(v);
    }


    public static void makeText(Context context, CharSequence text, int duration) {
        if(mToast == null){
            new MyToast(context, text, duration);
        }else {
            textView.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();

    }


}