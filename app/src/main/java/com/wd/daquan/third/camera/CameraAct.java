package com.wd.daquan.third.camera;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.View;
import com.wd.daquan.R;
import com.meetqs.qingchat.carema.listener.ErrorListener;
import com.meetqs.qingchat.carema.listener.JCameraListener;
import com.meetqs.qingchat.carema.view.JCameraView;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.presenter.Presenter;
import com.da.library.constant.DirConstants;
import com.wd.daquan.model.log.DqToast;
import com.da.library.tools.FileUtils;

import java.io.File;

/**
 * Created by Kind on 2019/4/16.
 */
public class CameraAct extends DqBaseActivity implements JCameraListener {
    public static final String KEY_IMAGE_PATH = "imagePath";

    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码
    private boolean granted = false;

    private String mTargetId = "";
    private JCameraView mCameraView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //6.0动态权限获取
        getPermissions();
    }

    @Override
    protected Presenter.IPresenter createPresenter() {
        return null;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.cn_carema_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mTargetId = getIntent().getStringExtra(KeyValue.ID);

        mCameraView = findViewById(R.id.jcameraview);

        //设置视频保存路径
        mCameraView.setSaveVideoPath(DirConstants.DIR_VIDEOS);
        mCameraView.setSaveUnique(mTargetId);
        mCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_CAPTURE);
        mCameraView.setTip("按下拍照");
//        mCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        mCameraView.setJCameraLisenter(this);
        mCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {

            }

            @Override
            public void AudioPermissionError() {

            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
    }

    public static String parseResult(Intent data) {
        return data.getStringExtra(KEY_IMAGE_PATH);
    }

    /**
     * 处理图片
     *
     * @param bitmap
     */
    private void doCapture(Bitmap bitmap) {
        // 保存图片，并存入相册
        String path = FileUtils.saveBitmap(DirConstants.DIR_CAMERA, mTargetId, bitmap);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));
        Intent intent = new Intent();
        intent.putExtra(KEY_IMAGE_PATH, path);
        setResult(RESULT_OK, intent);
        finish();
    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (granted) {
            mCameraView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraView.onPause();
    }

    /**
     * 获取权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //具有权限
                granted = true;
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(CameraAct.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
                granted = false;
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    granted = true;
                    mCameraView.onResume();
                } else {
                    DqToast.showShort("请到设置-权限管理中开启");
                    finish();
                }
            }
        }
    }

    @Override
    public void captureSuccess(Bitmap bitmap) {
        doCapture(bitmap);
    }

    @Override
    public void recordSuccess(String url, Bitmap firstFrame, int duration, int rotate) {

    }

    @Override
    public void caremaFinish() {
        this.finish();
    }

}
