package com.meetqs.qingchat.carema.listener;

import android.media.MediaPlayer;

/**
 * @author: dukangkang
 * @date: 2018/6/28 14:57.
 * @description: todo ...
 */
public interface PlayerListener {
    // 播放完成
    void onCompletion(MediaPlayer mp);

    void onSeekComplete(MediaPlayer mp);

    void onError(MediaPlayer mp, int what, int extra);
}
