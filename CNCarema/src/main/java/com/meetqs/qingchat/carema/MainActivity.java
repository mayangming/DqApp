//package com.aides.brother.brotheraides.carema;
//
//import android.Manifest;
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.view.View;
//import android.widget.Toast;
//
//import com.meetqs.qingchat.carema.listener.ErrorListener;
//import com.meetqs.qingchat.carema.listener.JCameraListener;
//import com.meetqs.qingchat.carema.util.LogUtil;
//import com.meetqs.qingchat.carema.view.JCameraView;
//
//import java.io.File;
//
//public class MainActivity extends Activity {
//
//    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码
//    private boolean granted = false;
//
//    private JCameraView mCameraView = null;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mCameraView = (JCameraView) findViewById(R.id.jcameraview);
//
//        //设置视频保存路径
//        mCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");
//        mCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
//        mCameraView.setTip("test");
//        mCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
//        mCameraView.setJCameraLisenter(new JCameraListener() {
//            @Override
//            public void captureSuccess(Bitmap bitmap) {
//                LogUtil.i("===>>>> bitmap = " + bitmap);
//            }
//
//            @Override
//            public void recordSuccess(String url, Bitmap firstFrame) {
//                LogUtil.i("===>>> url = " + url);
//                LogUtil.i("===>>> firstFrame = " + firstFrame);
//
//            }
//        });
//
//        mCameraView.setErrorLisenter(new ErrorListener() {
//            @Override
//            public void onError() {
//
//            }
//
//            @Override
//            public void AudioPermissionError() {
//
//            }
//        });
//
//        //6.0动态权限获取
//        getPermissions();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
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
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (granted) {
//            mCameraView.onResume();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mCameraView.onPause();
//    }
//
//    /**
//     * 获取权限
//     */
//    private void getPermissions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                //具有权限
//                granted = true;
//            } else {
//                //不具有获取权限，需要进行权限申请
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.RECORD_AUDIO,
//                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
//                granted = false;
//            }
//        }
//    }
//
//    @TargetApi(23)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == GET_PERMISSION_REQUEST) {
//            int size = 0;
//            if (grantResults.length >= 1) {
//                int writeResult = grantResults[0];
//                //读写内存权限
//                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
//                if (!writeGranted) {
//                    size++;
//                }
//                //录音权限
//                int recordPermissionResult = grantResults[1];
//                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
//                if (!recordPermissionGranted) {
//                    size++;
//                }
//                //相机权限
//                int cameraPermissionResult = grantResults[2];
//                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
//                if (!cameraPermissionGranted) {
//                    size++;
//                }
//                if (size == 0) {
//                    granted = true;
//                    mCameraView.onResume();
//                }else{
//                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//        }
//    }
//}
