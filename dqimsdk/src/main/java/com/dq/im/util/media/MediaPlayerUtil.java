package com.dq.im.util.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 播放器管理类
 * 对全局的MediaPlayer进行管理控制
 */
public
class MediaPlayerUtil{
    private MediaPlayer player;
    private static MediaPlayerUtil mediaPlayerUtil;
    private Map<String,MediaPlayerIpc> ipcMap = new HashMap<>();
    private Context context;
    private String lastTag;//上次播放的音频tag
    private MediaPlayerUtil(Context context) {
        this.context = context;
    }
    public static MediaPlayerUtil getInstance(Context context){
        if (null == mediaPlayerUtil){
            mediaPlayerUtil = new MediaPlayerUtil(context);
        }
        return mediaPlayerUtil;
    }
    public void onStop(){
        if (null != player && player.isPlaying()){
            player.stop();
            player.reset();
        }
    }
    public void onDestroy(){
        if (null != player){
            player.release();
            player = null;
        }
    }

    public void playVoice(String tag,String voicePath){
        if (Build.VERSION.SDK_INT>=29) {//android 10
            Uri voiceUri = Uri.parse(voicePath);
            initVoice(tag,voiceUri);
        }else {
            initVoice(tag,voicePath);
        }
    }

    /**
     * 播放音频文件
     */
    public void initVoice(String tag,String voicePath){
        if (null == player){
            player = new MediaPlayer();
        }
        if (tag.equals(lastTag) && player.isPlaying()){//假如这次跟上次点击的是同一个item,且正在播放中，则停止该次的播放
            notifyStop(tag);
            player.reset();
            return;
        }
        if (player.isPlaying()){
            player.reset();
        }
        try {
            player.setDataSource(voicePath);
        }catch (IOException e){
            e.printStackTrace();
        }
        notifyStop(tag);
        playVoice(tag);
    }
    /**
     * 播放音频文件
     */
    public void initVoice(String tag,Uri voicePath){
        if (null == player){
            player = new MediaPlayer();
        }

        if (tag.equals(lastTag) && player.isPlaying()){//假如这次跟上次点击的是同一个item,且正在播放中，则停止该次的播放
            notifyStop(tag);
            player.reset();
            return;
        }

        if (player.isPlaying()){
            player.reset();
        }
        try {
            player.setDataSource(context,voicePath);
        }catch (IOException e){
            e.printStackTrace();
        }

        notifyStop(tag);
        playVoice(tag);
    }

    private void notifyStop(String tag){
        if (!TextUtils.isEmpty(lastTag)){
            MediaPlayerIpc mediaPlayerIpc = ipcMap.get(lastTag);
            if (null != mediaPlayerIpc){
                mediaPlayerIpc.onStop(lastTag);
            }
        }
        lastTag = tag;
    }

    private void playVoice(String tag){
        player.prepareAsync();
        player.setOnPreparedListener(mediaPlayer -> {
            mediaPlayer.start();
//            anim.stop();
//            anim.start();
            MediaPlayerIpc mediaPlayerIpc = ipcMap.get(tag);
            if (null != mediaPlayerIpc){
                mediaPlayerIpc.onPrepared(tag,mediaPlayer);
            }
        });
        //播完之后都重置
        player.setOnCompletionListener(mp -> {
            player.reset();
            MediaPlayerIpc mediaPlayerIpc = ipcMap.get(tag);
            if (null != mediaPlayerIpc){
                mediaPlayerIpc.onCompletion(tag,mp);
            }
        });
        //播放错误之后都重置
        player.setOnErrorListener((mp, what, extra) -> {
            player.reset();
            MediaPlayerIpc mediaPlayerIpc = ipcMap.get(tag);
            if (null != mediaPlayerIpc){
                mediaPlayerIpc.onError(tag,mp, what, extra);
            }
            return false;
        });
    }

    public void addMediaPlayerListener(String tag,MediaPlayerIpc mediaPlayerIpc){
        Log.e("YM","设置的监听Tag:"+tag);
        ipcMap.put(tag,mediaPlayerIpc);
    }

    public void removeMediaPlayerListener(String tag){
        ipcMap.remove(tag);
    }

    /**
     * 停止播放音频
     */
    public void stopVoice(){
        if (null == player){
            player = new MediaPlayer();
        }
        if (player.isPlaying()){
            player.reset();
        }
        notifyStop(lastTag);
        lastTag = "";
    }
}