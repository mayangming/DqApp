package com.meetqs.qingchat.carema.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.meetqs.qingchat.carema.R;
import com.meetqs.qingchat.carema.CameraInterface;
import com.meetqs.qingchat.carema.CameraView;
import com.meetqs.qingchat.carema.listener.CaptureListener;
import com.meetqs.qingchat.carema.listener.ErrorListener;
import com.meetqs.qingchat.carema.listener.JCameraListener;
import com.meetqs.qingchat.carema.listener.SurfaceCallback;
import com.meetqs.qingchat.carema.listener.TypeListener;
import com.meetqs.qingchat.carema.state.CameraMachine;
import com.meetqs.qingchat.carema.util.LogUtil;
import com.meetqs.qingchat.carema.util.ScreenUtils;
import com.da.library.tools.FileUtils;

public class JCameraView extends FrameLayout implements CameraInterface.CameraOpenOverCallback/*, SurfaceHolder.Callback*/, CameraView, SurfaceCallback, View.OnClickListener {

    //Camera状态机
    private CameraMachine machine;

    //闪关灯状态
    private static final int TYPE_FLASH_AUTO = 0x021;
    private static final int TYPE_FLASH_ON = 0x022;
    private static final int TYPE_FLASH_OFF = 0x023;
    private int type_flash = TYPE_FLASH_OFF;

    //拍照浏览时候的类型
    public static final int TYPE_PICTURE = 0x001;
    public static final int TYPE_VIDEO = 0x002;
    public static final int TYPE_SHORT = 0x003;
    public static final int TYPE_DEFAULT = 0x004;

    //录制视频比特率
    public static final int MEDIA_QUALITY_HIGH = 24 * 100000;
    public static final int MEDIA_QUALITY_MIDDLE = 20 * 100000;
    public static final int MEDIA_QUALITY_LOW = 16 * 100000;
    public static final int MEDIA_QUALITY_POOR = 12 * 100000;
    public static final int MEDIA_QUALITY_FUNNY = 8 * 100000;
    public static final int MEDIA_QUALITY_DESPAIR = 4 * 100000;
    public static final int MEDIA_QUALITY_SORRY = 80000;


    public static final int BUTTON_STATE_ONLY_CAPTURE = 0x101;      //只能拍照
    public static final int BUTTON_STATE_ONLY_RECORDER = 0x102;     //只能录像
    public static final int BUTTON_STATE_BOTH = 0x103;              //两者都可以


    //回调监听
    private JCameraListener jCameraLisenter;
    private CNCaptureListener mCNCaptureListener = null;

    private Context mContext;
    private ImageView mSwitchIv; // 切换摄像头
    private ImageView mCloseIv; // 关闭页面
    private CaptureLayout mCaptureLayout;
    private FoucsView mFoucsView;
    private VideoPlayer mVideoPlayer = null;

    private int layout_width;
    private float screenProp = 0f;
    private int rotate = 0;

    private Bitmap captureBitmap;   //捕获的图片
    private Bitmap firstFrame;      //第一帧图片
    private String videoUrl;        //视频URL

    //切换摄像头按钮的参数
//    private int iconSize = 0;       //图标大小
//    private int iconMargin = 0;     //右上边距
//    private int iconSrc = 0;        //图标资源
//    private int iconLeft = 0;       //左图标
//    private int iconRight = 0;      //右图标

    private final int DURATION = 11 * 1000;       //录制时间

    //缩放梯度
    private int zoomGradient = 0;

    private boolean firstTouch = true;
    private float firstTouchLength = 0;

    public JCameraView(Context context) {
        this(context, null);
    }

    public JCameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initData();
        initView();
        initListener();
    }

    private void initData() {
        layout_width = ScreenUtils.getScreenWidth(mContext);
        //缩放梯度
        zoomGradient = (int) (layout_width / 16f);
        LogUtil.i("zoom = " + zoomGradient);
        machine = new CameraMachine(getContext(), this, this);
    }

    private void initView() {
        setWillNotDraw(false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.cn_camera_view, this);
        // 切换摄像头
        mSwitchIv = view.findViewById(R.id.carema_switch);
        // 拍照／录制按钮
        mCaptureLayout = view.findViewById(R.id.carema_capture_layout);
        mCaptureLayout.setDuration(DURATION);
        // 聚焦
        mFoucsView = view.findViewById(R.id.carema_foucs_view);

        // 播放器
        mVideoPlayer = this.findViewById(R.id.carema_video_player);
        mVideoPlayer.setSurfaceCallback(this);
        // 支持循环播放
        mVideoPlayer.setLoop(true);
        // 关闭按钮
        mCloseIv = this.findViewById(R.id.carema_close);

        mCNCaptureListener = new CNCaptureListener();
    }

    private void initListener() {
        mCloseIv.setOnClickListener(this);
        mSwitchIv.setOnClickListener(this);
        //拍照 录像
        mCaptureLayout.setCaptureLisenter(mCNCaptureListener);

//        mCaptureLayout.setCaptureLisenter(new CaptureListener() {
//            @Override
//            public void takePictures() {
//                mSwitchIv.setVisibility(INVISIBLE);
//                machine.capture();
//            }
//
//            @Override
//            public void recordStart() {
//                mSwitchIv.setVisibility(INVISIBLE);
//                machine.record(mVideoPlayer.getHolder().getSurface(), screenProp);
//            }
//
//            @Override
//            public void recordShort(final long time) {
//                LogUtil.i("录制时间短： time = " + time);
//                mSwitchIv.setVisibility(VISIBLE);
//                machine.stopRecord(true, time);
//            }
//
//            @Override
//            public void recordEnd(long time) {
//                machine.stopRecord(false, time);
//            }
//
//            @Override
//            public void recordZoom(float zoom) {
//                LogUtil.i("recordZoom");
//                machine.zoom(zoom, CameraInterface.TYPE_RECORDER);
//            }
//
//            @Override
//            public void recordError() {
//                if (errorLisenter != null) {
//                    errorLisenter.AudioPermissionError();
//                }
//            }
//        });

        //确认 取消
        mCaptureLayout.setTypeLisenter(new TypeListener() {
            @Override
            public void cancel() {
                machine.cancle(mVideoPlayer.getHolder(), screenProp);
            }

            @Override
            public void confirm() {
                machine.confirm();
            }

            @Override
            public void finish() {
                // 处理关闭
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float widthSize = mVideoPlayer.getVideoWidth();
        float heightSize = mVideoPlayer.getVideoHeight();

        if (screenProp == 0) {
            screenProp = heightSize / widthSize;
        }
    }

    @Override
    public void cameraHasOpened() {
        CameraInterface.getInstance().doStartPreview(mVideoPlayer.getHolder(), screenProp);
    }

    //生命周期onResume
    public void onResume() {
        LogUtil.i("JCameraView onResume");
        if (CaptureButton.STATE_BAN == mCaptureLayout.getState()){
//            Log.w("xxxx", "------ 禁止中不操作 onResume -----");
            mVideoPlayer.resume();
        } else {
            setRecordPermission(true);
            resetState(TYPE_DEFAULT); //重置状态
            CameraInterface.getInstance().registerSensorManager(mContext);
            CameraInterface.getInstance().setSwitchView(mSwitchIv);
            machine.start(mVideoPlayer.getHolder(), screenProp);
        }
    }

    //生命周期onPause
    public void onPause() {
        LogUtil.i("JCameraView onPause");
        if(CaptureButton.STATE_RECORDERING == mCaptureLayout.getState()) {
//            Log.w("xxxx", "------ 录制中直接关闭-----");
            resetState(TYPE_PICTURE);
            resetState(TYPE_VIDEO);
            CameraInterface.getInstance().isPreview(false);
            CameraInterface.getInstance().unregisterSensorManager(mContext);
        } else if (CaptureButton.STATE_BAN == mCaptureLayout.getState()){
//            Log.w("xxxx", "------ 禁止中不操作 onPause -----");
            mVideoPlayer.pause();
        } else {
            stopVideo();
            resetState(TYPE_PICTURE);
            CameraInterface.getInstance().isPreview(false);
            CameraInterface.getInstance().unregisterSensorManager(mContext);
        }
    }

    public void onDestroy() {
        LogUtil.i("JCameraView onDestroy");
        CameraInterface.destroyCameraInterface();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.i("carema view ontouchevent");
                if (event.getPointerCount() == 1) {
                    //显示对焦指示器
                    setFocusViewWidthAnimation(event.getX(), event.getY());
                }
                if (event.getPointerCount() == 2) {
                    Log.i("CJT", "ACTION_DOWN = " + 2);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    firstTouch = true;
                }
                if (event.getPointerCount() == 2) {
                    //第一个点
                    float point_1_X = event.getX(0);
                    float point_1_Y = event.getY(0);
                    //第二个点
                    float point_2_X = event.getX(1);
                    float point_2_Y = event.getY(1);

                    float result = (float) Math.sqrt(Math.pow(point_1_X - point_2_X, 2) + Math.pow(point_1_Y -
                            point_2_Y, 2));

                    if (firstTouch) {
                        firstTouchLength = result;
                        firstTouch = false;
                    }
                    if ((int) (result - firstTouchLength) / zoomGradient != 0) {
                        firstTouch = true;
                        machine.zoom(result - firstTouchLength, CameraInterface.TYPE_CAPTURE);
                    }
//                    Log.i("CJT", "result = " + (result - firstTouchLength));
                }
                break;
            case MotionEvent.ACTION_UP:
                firstTouch = true;
                break;
        }
        return true;
    }

    //对焦框指示器动画
    private void setFocusViewWidthAnimation(float x, float y) {
        machine.foucs(x, y, new CameraInterface.FocusCallback() {
            @Override
            public void focusSuccess() {
                mFoucsView.setVisibility(INVISIBLE);
            }
        });
    }

    public void setRecordPermission(boolean recordPermission) {
        if (null != mCaptureLayout) {
            mCaptureLayout.setRecordPermission(recordPermission);
        }
    }

    /**************************************************
     * 对外提供的API                     *
     **************************************************/

    public void setSaveVideoPath(String path) {
        CameraInterface.getInstance().setSaveVideoPath(path);
    }

    /**
     * 设置保存唯一标示：默认UID
     * @param unique
     */
    public void setSaveUnique(String unique) {
        CameraInterface.getInstance().setUnique(unique);
    }

    public void setJCameraLisenter(JCameraListener jCameraLisenter) {
        this.jCameraLisenter = jCameraLisenter;
    }


    private ErrorListener errorLisenter;

    //启动Camera错误回调
    public void setErrorLisenter(ErrorListener errorLisenter) {
        this.errorLisenter = errorLisenter;
        CameraInterface.getInstance().setErrorLinsenter(errorLisenter);
    }

    //设置CaptureButton功能（拍照和录像）
    public void setFeatures(int state) {
        this.mCaptureLayout.setButtonFeatures(state);
    }

    //设置录制质量
    public void setMediaQuality(int quality) {
        CameraInterface.getInstance().setMediaQuality(quality);
    }

    @Override
    public void resetState(int type) {
        switch (type) {
            case TYPE_VIDEO:
                stopVideo();    //停止播放
                //初始化VideoView
                FileUtils.deleteFile(videoUrl);
                mVideoPlayer.setVideoLayout();
                machine.start(mVideoPlayer.getHolder(), screenProp);
                break;
            case TYPE_PICTURE:
                mVideoPlayer.hidePhoto();
                break;
            case TYPE_SHORT:
                break;
            case TYPE_DEFAULT:
                mVideoPlayer.setVideoLayout();
                break;
        }
        mSwitchIv.setVisibility(VISIBLE);
        mCaptureLayout.resetCaptureLayout();
    }

    @Override
    public void confirmState(int type) {
        switch (type) {
            case TYPE_VIDEO:
                int duration = mVideoPlayer.getDuration();
                stopVideo();    //停止播放
                mVideoPlayer.setVideoLayout();
                machine.start(mVideoPlayer.getHolder(), screenProp);
                if (jCameraLisenter != null) {
                    jCameraLisenter.recordSuccess(videoUrl, firstFrame,  duration, rotate);
                }
                break;
            case TYPE_PICTURE:
                long startTime = System.currentTimeMillis();
                mVideoPlayer.hidePhoto();
                if (jCameraLisenter != null) {
                    jCameraLisenter.captureSuccess(captureBitmap);
                }
                Log.w("xxxx", "确认时间查.... " + (System.currentTimeMillis() - startTime));
                break;
            case TYPE_SHORT:
                break;
            case TYPE_DEFAULT:
                break;
        }
        mCaptureLayout.resetCaptureLayout();
    }

    @Override
    public void showPicture(Bitmap bitmap, boolean isVertical) {
        mVideoPlayer.showPhoto(bitmap, isVertical);
        captureBitmap = bitmap;
        mCaptureLayout.startAlphaAnimation();
        mCaptureLayout.startTypeBtnAnimator();
    }

    @Override
    public void playVideo(Bitmap firstFrame, final String url, int rotate) {
        videoUrl = url;
        LogUtil.i("播放地址：" + url);
        this.firstFrame = firstFrame;
        this.rotate = rotate;
        mVideoPlayer.play(url);
    }

    @Override
    public void stopVideo() {
        mVideoPlayer.release();
    }

    @Override
    public void setTip(String tip) {
        mCaptureLayout.setTip(tip);
    }

    @Override
    public void startPreviewCallback() {
        LogUtil.i("startPreviewCallback");
        handlerFoucs(mFoucsView.getWidth() / 2, mFoucsView.getHeight() / 2);
    }

    @Override
    public boolean handlerFoucs(float x, float y) {
        if (y > mCaptureLayout.getTop()) {
            return false;
        }
        mFoucsView.setVisibility(VISIBLE);
        if (x < mFoucsView.getWidth() / 2) {
            x = mFoucsView.getWidth() / 2;
        }
        if (x > layout_width - mFoucsView.getWidth() / 2) {
            x = layout_width - mFoucsView.getWidth() / 2;
        }
        if (y < mFoucsView.getWidth() / 2) {
            y = mFoucsView.getWidth() / 2;
        }
        if (y > mCaptureLayout.getTop() - mFoucsView.getWidth() / 2) {
            y = mCaptureLayout.getTop() - mFoucsView.getWidth() / 2;
        }
        mFoucsView.setX(x - mFoucsView.getWidth() / 2);
        mFoucsView.setY(y - mFoucsView.getHeight() / 2);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mFoucsView, "scaleX", 1, 0.6f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mFoucsView, "scaleY", 1, 0.6f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mFoucsView, "alpha", 1f, 0.4f, 1f, 0.4f, 1f, 0.4f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleX).with(scaleY).before(alpha);
        animSet.setDuration(400);
        animSet.start();
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtil.i("JCameraView SurfaceCreated");
        CameraInterface.getInstance().doOpenCamera(JCameraView.this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.i("JCameraView SurfaceDestroyed");
        CameraInterface.getInstance().doDestroyCamera();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mSwitchIv.getId()) { // 切换摄像头
            machine.swtich(mVideoPlayer.getHolder(), screenProp);
        } else if (id == mCloseIv.getId()) { // 关闭
            if (null != jCameraLisenter) {
                jCameraLisenter.caremaFinish();
            }
        }
    }

    private class CNCaptureListener implements CaptureListener  {

        @Override
        public void takePictures() {
            mSwitchIv.setVisibility(INVISIBLE);
            machine.capture();
        }

        @Override
        public void recordStart() {
            mSwitchIv.setVisibility(INVISIBLE);
            machine.record(mVideoPlayer.getHolder().getSurface(), screenProp);
        }

        @Override
        public void recordShort(final long time) {
            LogUtil.i("录制时间短： time = " + time);
            mSwitchIv.setVisibility(VISIBLE);
            machine.stopRecord(true, time);
        }

        @Override
        public void recordEnd(long time) {
            machine.stopRecord(false, time);
        }

        @Override
        public void recordZoom(float zoom) {
            LogUtil.i("recordZoom");
            machine.zoom(zoom, CameraInterface.TYPE_RECORDER);
        }

        @Override
        public void recordError() {
            if (errorLisenter != null) {
                errorLisenter.AudioPermissionError();
            }
        }
    }
}
