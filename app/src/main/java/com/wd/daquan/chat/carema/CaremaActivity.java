package com.wd.daquan.chat.carema;

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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.da.library.constant.DirConstants;
import com.da.library.tools.FileUtils;
import com.meetqs.qingchat.carema.listener.ErrorListener;
import com.meetqs.qingchat.carema.listener.JCameraListener;
import com.meetqs.qingchat.carema.view.JCameraView;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;

import java.io.File;

public class CaremaActivity extends DqBaseActivity<CaremaPresenter, DataBean> implements JCameraListener {

    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码
    private boolean granted = false;

    private String localFilePath;
    private String mAccount = "";

    private JCameraView mCameraView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //6.0动态权限获取
        getPermissions();
    }

    @Override
    public CaremaPresenter createPresenter() {
        return new CaremaPresenter();
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
        initIntent();

        mCameraView = (JCameraView) findViewById(R.id.jcameraview);

        //设置视频保存路径
        mCameraView.setSaveVideoPath(DirConstants.DIR_RECORED);
        mCameraView.setSaveUnique(mAccount);
        mCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        mCameraView.setTip("轻触拍照，按住摄像");
        mCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_HIGH);
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

    private void initIntent() {
        mAccount = getIntent().getStringExtra(KeyValue.KEY_ACCOUNT);
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
                ActivityCompat.requestPermissions(CaremaActivity.this, new String[]{
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
                    Toast.makeText(DqApp.sContext, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @Override
    public void captureSuccess(Bitmap bitmap) {
        //Log.w("qc_log", "bitmap = " + bitmap);
        // 保存图片，并存入相册
        File file =  FileUtils.saveBitmap(this, bitmap, DirConstants.DIR_CAMERA);
        Uri uri = Uri.fromFile(file);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

        Intent intent = new Intent();
        intent.putExtra(KeyValue.Carema.KEY_IMAGE_PATH, file.getAbsolutePath());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void recordSuccess(String url, Bitmap firstFrame, int duration, int rotate) {

        Log.w("fz", "url = " + url);
        Log.w("fz", "录制时长duration = " + duration);

        // 存入相册
        Uri uri = Uri.fromFile(new File(url));
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

        localFilePath = url;

        File file = new File(url);

        long floor = (long) Math.floor(duration / 1000f);

        Intent intent = new Intent();
        intent.putExtra(KeyValue.Carema.KEY_VIDEO_DURATION, floor);
        intent.putExtra(KeyValue.Carema.KEY_VIDEO_PATH, localFilePath);
        intent.putExtra(KeyValue.Carema.KEY_VIDEO_FILE_SIZE, file.length());
        intent.putExtra(KeyValue.Carema.KEY_VIDEO_ROTATE, rotate);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void caremaFinish() {
        this.finish();
    }

//    public static void sendMessage(Message content) {
//        RongIM.getInstance().sendMessage(content, EBApplication.sContext.getResources().getString(R.string.push_txt), null, new IRongCallback.ISendMessageCallback() {
//
//            @Override
//            public void onAttached(io.rong.imlib.model.Message content) {
//
//            }
//
//            @Override
//            public void onSuccess(io.rong.imlib.model.Message content) {
//                CNLog.e("xxxx", "onSuccess : ");
////                RongIM.getInstance().deleteMessages(new int[]{content.getMessageId()}, null);
//                CNToastUtil.showToast(EBApplication.sContext, "发送成功");
//            }
//
//            @Override
//            public void onError(io.rong.imlib.model.Message content, RongIMClient.ErrorCode errorCode) {
//                CNLog.e("xxxx", "onError");
//                RongIM.getInstance().deleteMessages(new int[]{content.getMessageId()}, null);
//                CNToastUtil.showToast(EBApplication.sContext, "发送失败");
//                // TODO: 2018/8/13 .临时修改
////                finish();
//            }
//        });
//    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
//        if (url.equals(URLUtil.url_alioss_token)) {
//            CNLog.w("xxxx", "entity = " + entity.toString());
//            Message content = entity.content;
//            AliOssResp aliOssResp = (AliOssResp) entity.intentUrl;
//
//            if (aliOssResp != null) {
//                int index = localFilePath.lastIndexOf("/") + 1;
//                String fileName = localFilePath.substring(index);
//
//                CNLog.w("xxxx", "fileName = " + fileName);
//                CNLog.w("xxxx", "localFilePath = " + localFilePath);
//
//                CNAliOSSClient.getInstance(this).setOssData(aliOssResp);
//                CNOSSFileBean fileBean = new CNOSSFileBean();
//                fileBean.host = aliOssResp.OSS_WEB_SITE;
//                if (TextUtils.isEmpty(aliOssResp.media_directory)) {
//                    fileBean.fileName = KeyValue.OSS_DIRECTION + AppInfoUtils.getPhoneImei() + "/" + fileName;
//                } else {
//                    fileBean.fileName = aliOssResp.media_directory + AppInfoUtils.getPhoneImei() + "/" + fileName;
//                }
//                fileBean.localFile = localFilePath;
//
//                CNAliOSSClient.getInstance(this).uploadFile(aliOssResp.bucket, fileBean, new VideoCallback(content));
//            }
//        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
//        CNToastUtil.showToast(DqApp.sContext, "请求OSS授权失败");
    }


//    private static class VideoCallback implements CNAliOSSClient.IUploadMediaCallback {
//
//        private WeakReference<Message> mWeakReference = null;
////        private Message mMessage = null;
//
//        public VideoCallback(Message content) {
////            this.mMessage = content;
//            mWeakReference = new WeakReference<>(content);
//        }
//
//        @Override
//        public void onProgress(long curSize, long totalSize) {
////            Message content = mWeakReference.get();
////            if (null == content) {
////                return;
////            }
////
////            CNVideoMessage videoMessage = (CNVideoMessage) content.getContent();
////            videoMessage.progress = curSize;
////            videoMessage.maxProgress = totalSize;
////
////            UpdateEvent updateEvent = new UpdateEvent(content, curSize, totalSize);
////            RongContext.getInstance().getEventBus().post(updateEvent);
//        }
//
//        @Override
//        public void onSuccess(long totalSize, CNOSSFileBean bean) {
////            Message content = mWeakReference.get();
////            if (null == content) {
////                return;
////            }
////            CNVideoMessage videoMessage = (CNVideoMessage) content.getContent();
////            if (null != videoMessage) {
////                videoMessage.etag = bean.eTag;
////                videoMessage.fileWebUrl = bean.fileName;
////                videoMessage.fileWebHttpUrl = bean.host + bean.fileName;
////                CNLog.w("xxxx", "videoMessage.fileWebUrl = " + videoMessage.fileWebUrl);
////            }
////
////            UpdateEvent updateEvent = new UpdateEvent(content, totalSize, totalSize);
////            RongContext.getInstance().getEventBus().post(updateEvent);
////            sendMessage(content);
//        }
//
//        @Override
//        public void onFailure() {
////            Message content = mWeakReference.get();
////            if (null == content) {
////                return;
////            }
////            RongIM.getInstance().deleteMessages(new int[]{content.getMessageId()}, null);
////            CNLog.w("xxxx", "上传失败");
//        }
//    }

}