package com.wd.daquan.common.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.wd.daquan.R;
import com.da.library.constant.IConstant;
import com.wd.daquan.glide.GlideUtils;
import com.meetqs.qingchat.imagepicker.photoview.PhotoView;

/**
 * @Author: 方志
 * @Time: 2018/9/28 14:52
 * @Description: 查看单张大图
 */
public class LookBigPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private PhotoView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_look_big_photo);

        mPhotoView = findViewById(R.id.look_big_photo_view);

        String phonePath = getIntent().getStringExtra(IConstant.UserInfo.HEADPIC);

        GlideUtils.load(this, phonePath, mPhotoView);

        mPhotoView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
