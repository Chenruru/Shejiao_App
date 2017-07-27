package test.bwie.com.shejiaoapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;

import java.io.Serializable;

import cn.bluemobi.dylan.photoview.library.PhotoView;
import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.bean.DataBean;

public class PhotoviewActivity extends AppCompatActivity {

    private PhotoView photo_view_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);
        initview();
    }

    private void initview() {
        photo_view_id = (PhotoView) findViewById(R.id.photo_view_id);
        Intent intent = getIntent();
        String photo = intent.getStringExtra("photo");
        Glide.with(this).load(photo).into(photo_view_id);
    }
}
