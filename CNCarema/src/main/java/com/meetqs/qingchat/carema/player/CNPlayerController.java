package com.meetqs.qingchat.carema.player;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.meetqs.qingchat.carema.R;
import com.meetqs.qingchat.carema.listener.ControlListener;
import com.meetqs.qingchat.carema.util.CUtils;
import com.meetqs.qingchat.carema.view.CircleProgressView;

import java.lang.ref.WeakReference;

/**
 * @author: dukangkang
 * @date: 2018/6/29 15:08.
 * @description: todo ...
 */
public class CNPlayerController extends AbsPlayerController implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ImageView mCloseIv = null;
    private ImageView mOptioinIv = null; // 处理暂停开始
    private ImageView mCenterIv = null; // 中心开始按钮
    private ImageView mFirstFrameIv = null;
    private TextView mCurPositionTv = null; // 当前时间
    private TextView mDurationTv = null; // 总时长
    private SeekBar mSeekBar = null;
    private RelativeLayout mBottomRlyt = null; // 底部控制栏父容器

    private Handler mHandler;
    private CNVideoPlayer mVideoPlayer;
    private GestureDetector mGestureDetector = null;
    private ControlListener mControlListener = null;
    private CircleProgressView mProgressView = null;


    private static class CNHandler extends Handler {
        private final int DELAY_UPDATE_TIME = 50; //seekbar更新频率时间
        private WeakReference<CNPlayerController> controller;

        public CNHandler(CNPlayerController control) {
            controller = new WeakReference<CNPlayerController>(control);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == controller.get()) {
                removeCallbacksAndMessages(null);
                return;
            }
            switch (msg.what) {
                case MSG_UPDATE_PROGRESS:
                    controller.get().updateProgress(-1);
                    sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS, DELAY_UPDATE_TIME);
                    break;
                case MSG_HIDE_CONTROL:
                    controller.get().hideControl();
                    break;
                case MSG_RELEASE:
                    controller.get().release();
                    break;
            }
        }
    }

    public CNPlayerController(@NonNull Context context) {
        this(context, null);
    }

    public CNPlayerController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initView();
        initListener();
    }

    public void setControlListener(ControlListener controlListener) {
        mControlListener = controlListener;
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.cn_player_controller, this);
        mHandler = new CNHandler(this);
        mGestureDetector = new GestureDetector(mContext, new CNGestureListener());
    }

    public void setVideoPlayer(CNVideoPlayer videoPlayer) {
        mVideoPlayer = videoPlayer;
        mVideoPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    protected void initView() {
        mHandler = new CNHandler(this);

        mCloseIv = this.findViewById(R.id.cn_player_close);

        mCenterIv = this.findViewById(R.id.cn_player_center);
        // 底部容器
        mBottomRlyt = this.findViewById(R.id.cn_player_bottom_rlyt);
        mOptioinIv = this.findViewById(R.id.cn_player_option);
        mCurPositionTv = this.findViewById(R.id.cn_player_cur_position);
        mDurationTv = this.findViewById(R.id.cn_player_total_duration);
        mSeekBar = this.findViewById(R.id.cn_player_seakbar);

        mBottomRlyt.setOnClickListener(null);
        mProgressView = this.findViewById(R.id.cn_player_progress);

        mFirstFrameIv = this.findViewById(R.id.cn_player_firstframe);
    }

    @Override
    protected void initListener() {
        mCloseIv.setOnClickListener(this);
        mOptioinIv.setOnClickListener(this);
        mCenterIv.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case CNVideoPlayer.STATE_IDLE:
                break;
            case CNVideoPlayer.STATE_PREPARING:
//                mImage.setVisibility(View.GONE);
//                mLoading.setVisibility(View.VISIBLE);
//                mLoadText.setText("正在准备...");
//                mError.setVisibility(View.GONE);
//                mCompleted.setVisibility(View.GONE);
//                mTop.setVisibility(View.GONE);
//                mBottom.setVisibility(View.GONE);
//                mCenterStart.setVisibility(View.GONE);
//                mLength.setVisibility(View.GONE);

                //mCenterIv.setVisibility(View.GONE);
                break;
            case CNVideoPlayer.STATE_PREPARED:
//                startUpdateProgressTimer();
                //LogUtil.i("STATE_PREPARED ");
                startUpdate();
                break;
            case CNVideoPlayer.STATE_PLAYING:
//                mLoading.setVisibility(View.GONE);
//                mRestartPause.setImageResource(R.drawable.ic_player_pause);
//                startDismissTopBottomTimer();

                //LogUtil.i("--->>>> STATE_PLAYING ");
                startUpdate();
                mCenterIv.setVisibility(View.GONE);
                mFirstFrameIv.setVisibility(View.GONE);
                mOptioinIv.setSelected(false);
                break;
            case CNVideoPlayer.STATE_PAUSED:
//                mLoading.setVisibility(View.GONE);
//                mRestartPause.setImageResource(R.drawable.ic_player_start);
//                cancelDismissTopBottomTimer();

                //LogUtil.i("#####>>>> STATE_PAUSED ");
                stopUpdate();
                mCenterIv.setVisibility(View.VISIBLE);
                mOptioinIv.setSelected(true);
                break;
            case CNVideoPlayer.STATE_BUFFERING_PLAYING:
//                mLoading.setVisibility(View.VISIBLE);
//                mRestartPause.setImageResource(R.drawable.ic_player_pause);
//                mLoadText.setText("正在缓冲...");
//                startDismissTopBottomTimer();
                break;
            case CNVideoPlayer.STATE_BUFFERING_PAUSED:
//                mLoading.setVisibility(View.VISIBLE);
//                mRestartPause.setImageResource(R.drawable.ic_player_start);
//                mLoadText.setText("正在缓冲...");
//                cancelDismissTopBottomTimer();
                break;
            case CNVideoPlayer.STATE_ERROR:
//                cancelUpdateProgressTimer();
//                setTopBottomVisible(false);
//                mTop.setVisibility(View.VISIBLE);
//                mError.setVisibility(View.VISIBLE);

                mCenterIv.setVisibility(View.VISIBLE);
                mOptioinIv.setSelected(true);
//                mHandler.sendEmptyMessageDelayed(MSG_RELEASE, 1000);
                mHandler.obtainMessage(MSG_RELEASE).sendToTarget();
                break;
            case CNVideoPlayer.STATE_COMPLETED:
//                cancelUpdateProgressTimer();
//                setTopBottomVisible(false);
//                mImage.setVisibility(View.VISIBLE);
//                mCompleted.setVisibility(View.VISIBLE);
                try {
                    int duration = (int) mVideoPlayer.getDuration();
                    updateProgress(duration);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCenterIv.setVisibility(View.VISIBLE);
                mOptioinIv.setSelected(true);
                mHandler.obtainMessage(MSG_RELEASE).sendToTarget();
//                mHandler.sendEmptyMessageDelayed(MSG_RELEASE, 1000);
                break;
        }
    }

    /**
     * 设置中心按钮可见性
     * @param visiblity
     */
    public void setCenterVisiblity(int visiblity) {
        mCenterIv.setVisibility(visiblity);
        mOptioinIv.setSelected(visiblity == View.VISIBLE);
    }

    /**
     * 设置加载框可见性
     * @param visibility
     */
    public void setProgressViewVisible(int visibility) {
        mProgressView.setVisibility(visibility);
    }

    public ImageView getFirstFrameIv() {
        return mFirstFrameIv;
    }

    public CircleProgressView getProgressView() {
        return mProgressView;
    }

    public ImageView getSharedViewForPlay() {
        return mCenterIv;
    }

    @Override
    public void updateProgress(int progress) {
        if (mVideoPlayer == null) {
            return;
        }
        int curTime = 0;
        if (0 <= progress) {
            curTime = progress;
        } else {
            curTime = (int) mVideoPlayer.getCurrentPosition();
        }
        int duration = (int) mVideoPlayer.getDuration();
        if (null != mSeekBar) {
            mSeekBar.setMax(duration);
            mSeekBar.setProgress(curTime);
        }
//        Log.w("xxxx", "curTime = " + curTime);
//        Log.w("xxxx", "duration = " + duration);

        int curRond = (int) Math.floor(curTime / 1000f);
        int durationRond = (int) Math.floor(duration / 1000);
        mCurPositionTv.setText(CUtils.getPlayTime(curRond));
        mDurationTv.setText(CUtils.getPlayTime(durationRond));

//        LogUtil.i("updateProgress curTime = " + curTime);
//        LogUtil.i("updateProgress duration = " + duration);
    }

    public void updateDuration(long time) {

        try {
            mDurationTv.setText(CUtils.getPlayTime(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void showControl() {
        mCloseIv.setVisibility(View.VISIBLE);
        mBottomRlyt.setVisibility(View.VISIBLE);

        if (!mVideoPlayer.isCompleted() && !mVideoPlayer.isError()) {
            mHandler.removeMessages(MSG_HIDE_CONTROL);
            mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROL, 3000);
        }
    }

    @Override
    public void hideControl() {
        mCloseIv.setVisibility(View.GONE);
        mBottomRlyt.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mCenterIv.getId()) {
            mCenterIv.setVisibility(GONE);
            if (null != mControlListener) {
                mControlListener.onCenterPlay();
            }
//            mVideoPlayer.restart();
        } else if (id == mOptioinIv.getId()) {
//            if (mVideoPlayer.isPlaying() || mVideoPlayer.isBufferingPlaying()) {
//                mVideoPlayer.pause();
//                mOptioinIv.setSelected(true);
//            } else {
//                mVideoPlayer.restart();
//                mOptioinIv.setSelected(false);
//            }
            if (null != mControlListener) {
                mControlListener.onPauseOrResume();
            }
        } else if (id == mCloseIv.getId()) {
            if (null != mControlListener) {
                mControlListener.onFinish();
            }
        }
    }

    public void centerPlay() {
        mVideoPlayer.restart();
    }

    public void pauseOrResume() {
        if (mVideoPlayer.isPlaying() || mVideoPlayer.isBufferingPlaying()) {
            mVideoPlayer.pause();
            mOptioinIv.setSelected(true);
        } else {
            mVideoPlayer.restart();
            mOptioinIv.setSelected(false);
        }
    }

    public void onPause() {
        if (null == mVideoPlayer) {
            return;
        }
        mVideoPlayer.pause();
    }

    public void onResume() {
    }

    // 开始刷新进度条
    public void startUpdate() {
        mHandler.removeMessages(MSG_UPDATE_PROGRESS);
        mHandler.obtainMessage(MSG_UPDATE_PROGRESS).sendToTarget();
    }

    // 停止刷新进度条
    public void stopUpdate() {
        mHandler.removeMessages(MSG_UPDATE_PROGRESS);
    }

    public void release() {
        mProgressView.setValue(0);
        mProgressView.setMaxValue(0);
        mHandler.removeCallbacksAndMessages(null);
    }

    private void optionController() {
        if (mBottomRlyt.getVisibility() == View.GONE) {
            showControl();
        } else {
            hideControl();
        }
    }

    private class CNGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            optionController();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            Log.w("xxxx", "---------------onLongPress");
            if (null != mControlListener) {
                mControlListener.onLongClick();
            }
        }
    }

    // seekbar监听器
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        LogUtil.i("xxxx --> onProgressChanged + fromUser = " + fromUser);
        if (fromUser) {
            if (null == mVideoPlayer) {
                return;
            }
            int pos = seekBar.getProgress();
            if (mVideoPlayer.getCurrentPosition() != pos) {
                mVideoPlayer.seekTo(pos);
            }
            updateProgress(pos);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
//        LogUtil.i("xxxx -->, onStartTrackingTouch");
        if (null == mVideoPlayer) {
            return;
        }
        mVideoPlayer.forcePause();
        mHandler.removeMessages(MSG_HIDE_CONTROL);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (null == mVideoPlayer) {
            return;
        }

        if (null == mVideoPlayer) {
            return;
        }
        mVideoPlayer.restart();
        mHandler.removeMessages(MSG_HIDE_CONTROL);
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROL, 3000);
    }

    public void updateProgress(long curValue, long maxValue) {
        mSeekBar.setEnabled(false);
        mProgressView.setVisibility(View.VISIBLE);
        mProgressView.setMaxValue(maxValue);
        mProgressView.setValue(curValue);
    }

    public void onComplete() {
        mSeekBar.setEnabled(true);
        mProgressView.setVisibility(View.GONE);
    }
}
