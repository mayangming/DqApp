package com.meetqs.qingchat.carema.player;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author: dukangkang
 * @date: 2018/6/29 14:55.
 * @description: todo ...
 */
public abstract class AbsPlayerController extends FrameLayout {

    public static final int MSG_UPDATE_PROGRESS = 1;
    public static final int MSG_HIDE_CONTROL = 2;
    public static final int MSG_RELEASE = 3;

    public Context mContext = null;

    public AbsPlayerController(Context context) {
        this(context, null);
    }

    public AbsPlayerController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsPlayerController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    protected abstract void initView();

    protected abstract void initListener();

    // 播放状态
    public abstract void onPlayStateChanged(int playState);

    // 更新播放状态
    public abstract void updateProgress(int progress);

    // 重置
    public abstract void reset();

    // 显示状态栏
    public abstract void showControl();

    // 隐藏状态栏
    public abstract void hideControl();

}
