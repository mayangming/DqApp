package com.wd.daquan.imui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.MediaController;
import android.widget.VideoView;

import com.wd.daquan.R;


/**
 * 视频播放详情页
 */
public class VideoDetailsActivity extends BaseActivity{
    public static final String VIDEO_PATH_ACTION = "videoPathAction";
    private VideoView videoDetails;
    private String videoPath = "";//文件网络路径
//    private Uri videoPath;//文件网络路径

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
        videoDetails.setVideoPath(videoPath);
//        videoDetails.setVideoURI(videoPath);
        videoDetails.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoDetails.stopPlayback();//停止播放视频,并且释放
//        mVideoView.suspend();//在任何状态下释放媒体播放器
    }
}