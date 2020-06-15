package com.meetqs.qingchat.carema.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import com.meetqs.qingchat.carema.R;
import com.meetqs.qingchat.carema.listener.PlayerListener;
import com.meetqs.qingchat.carema.listener.SurfaceCallback;
import com.meetqs.qingchat.carema.util.LogUtil;
import com.meetqs.qingchat.carema.util.ScreenUtils;

import java.io.IOException;

/**
 * @author: dukangkang
 * @date: 2018/6/28 13:20.
 * @description: todo ...
 */
public class VideoPlayer extends FrameLayout implements SurfaceHolder.Callback, MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener {
    private Context mContext = null;
    private VideoView mVideoView = null;
    private ImageView mPhotoView = null;
    private MediaPlayer mMediaPlayer = null;
    private SurfaceCallback mSurfaceCallback = null;
    private PlayerListener mPlayerListener = null;
    private boolean isLoop = false;
    private boolean isPrepared = false;

    public VideoPlayer(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.cn_record_videoview, this);
        mVideoView = this.findViewById(R.id.video_preview);
        mPhotoView = this.findViewById(R.id.image_photo);

        mVideoView.getHolder().addCallback(this);
    }

    public void setSurfaceCallback(SurfaceCallback surfaceCallback) {
        mSurfaceCallback = surfaceCallback;
    }

    public void setPlayerListener(PlayerListener playerListener) {
        mPlayerListener = playerListener;
    }

    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    public SurfaceHolder getHolder() {
        return mVideoView.getHolder();
    }

    public int getVideoWidth() {
        return mVideoView.getMeasuredWidth();
    }

    public int getVideoHeight() {
        return mVideoView.getMeasuredHeight();
    }

    public void setVideoLayout() {
        mVideoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void showPhoto(Bitmap bitmap, boolean isVertical) {
        if (isVertical) {
            mPhotoView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            mPhotoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        mPhotoView.setImageBitmap(bitmap);
        mPhotoView.setVisibility(VISIBLE);
    }

    public void hidePhoto() {
        mPhotoView.setVisibility(View.INVISIBLE);
    }

    /**
     * 播放
     * @param url
     */
    public void play(String url) {
        if (TextUtils.isEmpty(url)) {
            LogUtil.i("播放地址不能为空");
            return;
        }
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            } else {
                mMediaPlayer.reset();
            }
            mMediaPlayer.setSurface(mVideoView.getHolder().getSurface());
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);

//            mMediaPlayer.setOnSeekCompleteListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setLooping(isLoop);
            try {
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 销毁
     */
    public void release() {
        isPrepared = false;
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    /**
     * 开始
     */
    public void resume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public boolean isPreared() {
        return isPrepared;
    }

    public boolean isPlaying() {
        return  mMediaPlayer.isPlaying();
    }

    public int getDuration() {
        if (null != mMediaPlayer) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    public void seekTo(int second) {
        mMediaPlayer.seekTo(second);
    }

    public int getCurrentPosition() {
        if (null != mMediaPlayer) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * 更新视频容器大小
     * @param videoWidth
     * @param videoHeight
     */
    private void updateVideoViewSize(float videoWidth, float videoHeight) {
        if (videoWidth > videoHeight) {
            LayoutParams videoViewParam;
            int height = (int) ((videoHeight / videoWidth) * ScreenUtils.getScreenWidth(mVideoView.getContext()));
            videoViewParam = new LayoutParams(LayoutParams.MATCH_PARENT, height);
            videoViewParam.gravity = Gravity.CENTER;
            mVideoView.setLayoutParams(videoViewParam);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (null != mSurfaceCallback) {
            mSurfaceCallback.surfaceCreated(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (null != mSurfaceCallback) {
            mSurfaceCallback.surfaceChanged(holder, format, width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null != mSurfaceCallback) {
            mSurfaceCallback.surfaceDestroyed(holder);
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        updateVideoViewSize(mMediaPlayer.getVideoWidth(), mMediaPlayer
                .getVideoHeight());
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
        isPrepared = true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        isPrepared = false;
        if (null != mPlayerListener) {
            mPlayerListener.onCompletion(mp);
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        isPrepared = false;
        if (null != mPlayerListener) {
            mPlayerListener.onSeekComplete(mp);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        LogUtil.i("======>>>>> onError <<<<<");
        isPrepared = false;
        if (null != mPlayerListener) {
            mPlayerListener.onError(mp, what, extra);
        }
        return false;
    }
}
