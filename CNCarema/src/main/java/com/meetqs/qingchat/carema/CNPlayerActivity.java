//package com.aides.brother.brotheraides.carema;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import com.meetqs.qingchat.carema.listener.ControlListener;
//import com.meetqs.qingchat.carema.player.CNPlayerController;
//import com.meetqs.qingchat.carema.player.CNVideoPlayer;
//
///**
// * @author: dukangkang
// * @date: 2018/6/29 11:46.
// * @description: todo ...
// */
//public class CNPlayerActivity extends Activity {
//
//    private String mUrl = "";
////    private String url = "/storage/emulated/0/JCamera/video_1530172910109.mp4";
////    private String url = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";
////    private String url = "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4";
//    private CNVideoPlayer mVideoPlayer = null;
//    private CNPlayerController mPlayerController = null;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.cn_player_activity);
//
//        initIntent();
//
//        initView();
//    }
//
//    private void initIntent() {
//        Intent intent = getIntent();
//        if (null != intent) {
//            Bundle bundle = intent.getExtras();
//            mUrl = bundle.getString(CNKeyValue.VIDEO_URL);
//        }
//
//        Log.w("xxxx", "播放地址：" + mUrl);
//    }
//
//    private void initView() {
//        mPlayerController = new CNPlayerController(this);
//        mPlayerController.setControlListener(mControlListener);
//
//        this.mVideoPlayer = this.findViewById(R.id.cn_player_videoplayer);
//        mVideoPlayer.setController(mPlayerController);
//
//        if (TextUtils.isEmpty(mUrl)) {
//            Toast.makeText(this, "播放地址不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        mVideoPlayer.setVideoUrl(mUrl, null);
//        mVideoPlayer.start();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
////        mPlayerController.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
////        mPlayerController.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mVideoPlayer.release();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        } else {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(option);
//        }
//    }
//
//    private ControlListener mControlListener = new ControlListener() {
//        @Override
//        public void onFinish() {
//            finish();
//        }
//    };
//}
