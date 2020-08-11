package com.wd.daquan.mine.scan;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.da.library.camera.CameraManager;
import com.da.library.camera.Config;
import com.da.library.camera.InactivityTimer;
import com.da.library.listener.DialogListener;
import com.da.library.view.ViewfinderView;
import com.google.zxing.Result;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.helper.QrcodeHelper;
import com.wd.daquan.common.listener.QrCodeListener;
import com.wd.daquan.common.scancode.ScanCodeHandler;
import com.wd.daquan.common.scancode.ScanCodeListener;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;

import java.io.IOException;

/**
 * 扫一扫
 */
public class ScanQRCodeActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener, QCObserver {

    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;

    private boolean hasSurface = false;
    private boolean playBeep = false;
    private boolean vibrate = false;

    private ScanCodeHandler mHandler;
    private SurfaceView mSurfaceView = null;
    private ViewfinderView mFinderView = null;
    // 返回
    private ImageView mBackIv = null;
    // 图片
    private TextView mPicTv = null;
    // 灯光
    private TextView mLightTv = null;


    private Camera mCamera = null;

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    private InactivityTimer mInactivityTimer;

    @Override
    public MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void initStatusBar() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.scan_qrcode_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        CameraManager.init(getApplication());

        mSurfaceView = this.findViewById(R.id.scan_code_surfaceview);
        mFinderView = this.findViewById(R.id.scan_code_finderview);
        mBackIv = this.findViewById(R.id.scan_code_back);
        mPicTv = this.findViewById(R.id.scan_code_picture);
        mPicTv.setVisibility(View.GONE);
        mLightTv = this.findViewById(R.id.scan_code_light);
    }

    @Override
    public void initListener() {
        mBackIv.setOnClickListener(this);
        mPicTv.setOnClickListener(this);
        mLightTv.setOnClickListener(this);
        MsgMgr.getInstance().attach(this);
    }

    @Override
    public void initData() {
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mInactivityTimer = new InactivityTimer(this);
        qrcodeHelper = new QrcodeHelper(getActivity());
        qrcodeHelper.setQrCodeListener(mQrCodeListener);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            int cameraId = 0;
            int orientation = 1;
            Config.cameraId = cameraId;
            Config.orientation = orientation;
            CameraManager.get().openDriver(surfaceHolder, cameraId);
        } catch (Exception e) {
            DialogUtils.showSettingQCNumDialog(this, "", getString(R.string.add_authority), getString(R.string.cancel),
                    getString(R.string.comm_setting), new DialogListener() {
                        @Override
                        public void onCancel() {
                            finish();
                        }
                        @Override
                        public void onOk() {
                            finish();
                            NavUtils.startAppSettings(ScanQRCodeActivity.this);
                        }
                    });
            mSurfaceView.setVisibility(View.GONE);
            return;
        }
        if (mHandler == null) {
            mHandler = new ScanCodeHandler(this, mFinderView, null, null);
            mHandler.setScanCodeListener(mScanCodeListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    @Override
    protected void onDestroy() {
        mInactivityTimer.shutdown();
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }

    public void pause() {
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        CameraManager.get().closeDriver();
    }

    public void resume() {
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(mCallback);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        playBeep = null == mAudioManager || mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
        initBeepSound();
        vibrate = true;
    }

    private void initBeepSound() {
        if (playBeep && mMediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mMediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                mMediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mMediaPlayer != null) {
            mMediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            assert vibrator != null;
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * 未识别，重新扫描
     */
    private void retryScan() {
        DqToast.showShort(getString(R.string.qr_code_no_find));
        mBackIv.postDelayed(this::resume, KeyValue.TWO_THOUSAND_MILLISECONDS);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBackIv.getId()) {
            onBackPressed();
        } else if (id == mPicTv.getId()) {
            NavUtils.gotoQCPhotoAlbumListActivity(ScanQRCodeActivity.this);
        } else if (id == mLightTv.getId()) {
            mCamera = CameraManager.getCamera();
            if (null == mCamera) {
                return;
            }
            Camera.Parameters mParameters = mCamera.getParameters();
            String str = mLightTv.getText().toString().trim();
            if (str.equals(getString(R.string.qr_code_light_open))) {
                mLightTv.setText(getString(R.string.qr_code_light_close));
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(mParameters);
            } else if (str.equals(getString(R.string.qr_code_light_close))) {  // 关灯
                mLightTv.setText(getString(R.string.qr_code_light_open));
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(mParameters);
            }
        }
    }

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mMediaPlayer.seekTo(0);
        }
    };

    private QrcodeHelper qrcodeHelper;
    private ScanCodeListener mScanCodeListener = new ScanCodeListener() {

        @Override
        public void invalidate() {
            if (null != mFinderView) {
                mFinderView.drawViewfinder();
            }
        }

        @Override
        public void decodeSucceeded(Result result, Bitmap bitmap) {
            mInactivityTimer.onActivity();
            playBeepSoundAndVibrate();
            pause();
            String url = result.getText();
            Log.e("YM","解码结果:"+url);
            if(qrcodeHelper == null) {
                qrcodeHelper = new QrcodeHelper(getActivity());
                qrcodeHelper.setQrCodeListener(mQrCodeListener);
            }

            qrcodeHelper.distinguishQrcode(getActivity(), url);
        }
    };

    private QrCodeListener mQrCodeListener = new QrCodeListener() {
        @Override
        public void notFound() {
            ScanQRCodeActivity.this.retryScan();
        }

        @Override
        public void resume() {
            ScanQRCodeActivity.this.resume();
        }

        @Override
        public void finish() {
            ScanQRCodeActivity.this.finish();
        }

        @Override
        public void retryScan() {
            ScanQRCodeActivity.this.retryScan();
        }
    };

    private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (!hasSurface) {
                hasSurface = true;
                initCamera(holder);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            hasSurface = false;
            if (mCamera != null) {
                CameraManager.get().stopPreview();
            }
        }
    };

    @Override
    public void onMessage(String key, Object value) {
        if(MsgType.MT_QR_CODE.equals(key)) {
            String result = (String) value;
            Log.e("YM","扫码onMessage的结果:"+result);
            if(qrcodeHelper != null) {
                qrcodeHelper.distinguishQrcode(getActivity(), result);
            }
        }
    }
}