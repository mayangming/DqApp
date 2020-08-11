package com.wd.daquan.imui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.wd.daquan.R;
import com.wd.daquan.model.log.DqLog;


/**
 * 视频播放详情页
 */
public class VideoDetailsActivity extends BaseActivity{
    public static final String VIDEO_PATH_ACTION = "videoPathAction";
    private VideoView videoDetails;
    private String videoPath = "";//文件网络路径
    //    private Uri videoPath;//文件网络路径
    private View videoBgContainer;
    private View videoStart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);
        initView();
        initData();
        initVideoView();
    }

    private void initView(){
        videoDetails = findViewById(R.id.video_details);
        videoBgContainer = findViewById(R.id.video_details_bg_container);
        videoStart = findViewById(R.id.video_details_play);
        videoBgContainer.setVisibility(View.GONE);
        videoDetails.setVisibility(View.VISIBLE);
//        videoStart.setOnClickListener(this::onClick);
    }

    private void initData(){
        videoPath = getIntent().getStringExtra(VIDEO_PATH_ACTION);
//        videoPath = getIntent().getParcelableExtra(VIDEO_PATH_ACTION);
    }

    private void initVideoView(){
        //设置有进度条可以拖动快进
        MediaController localMediaController = new MediaController(this);
        videoDetails.setMediaController(localMediaController);
//        Uri.
        DqLog.e("YM-------->视频地址:"+videoPath);
        videoDetails.setVideoPath(videoPath);
//        videoDetails.setVideoURI(videoPath);
        videoDetails.start();
    }

//    private void onClick(View view){
//        switch (view.getId()){
//            case R.id.video_details_play:
//                videoBgContainer.setVisibility(View.GONE);
//                videoDetails.setVisibility(View.VISIBLE);
//                videoDetails.start();
//                break;
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoDetails.stopPlayback();//停止播放视频,并且释放
//        mVideoView.suspend();//在任何状态下释放媒体播放器
    }
}