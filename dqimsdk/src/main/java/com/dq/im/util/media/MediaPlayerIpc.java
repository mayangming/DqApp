package com.dq.im.util.media;

import android.media.MediaPlayer;

public
interface MediaPlayerIpc {
    boolean onError(String tag, MediaPlayer mp, int what, int extra);//播放错误
    void onCompletion(String tag, MediaPlayer mp);//播放完成
    void onPrepared(String tag, MediaPlayer mp);//准备完成
    void onStop(String tag);//停止播放
}