package com.wd.daquan.imui.adapter.viewholderbind;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.dq.im.bean.im.MessageVoiceBean;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.type.ImType;
import com.dq.im.util.download.HttpDownFileUtils;
import com.dq.im.util.download.OnFileDownListener;
import com.dq.im.util.media.MediaPlayerIpc;
import com.dq.im.util.media.MediaPlayerUtil;
import com.dq.im.util.oss.AliOssUtil;
import com.dq.im.viewmodel.P2PMessageViewModel;
import com.dq.im.viewmodel.TeamMessageViewModel;
import com.google.gson.Gson;
import com.wd.daquan.imui.adapter.viewholder.LeftVoiceViewHolder;
import com.wd.daquan.util.FileUtils;
import com.wd.daquan.util.TToast;

import java.io.File;
import java.util.UUID;

import static android.os.Environment.DIRECTORY_MUSIC;

/**
 * 左侧音频内容填充
 */
public class LeftVoiceChatViewHolderBind extends BaseLeftViewHolderBind<LeftVoiceViewHolder> implements MediaPlayerIpc {
    private P2PMessageViewModel p2PMessageViewModel;
    private TeamMessageViewModel teamMessageViewModel;
    private FragmentActivity activity;
    private Gson gson = new Gson();
    private AnimationDrawable anim;
    private String uuid;
    private MediaPlayerUtil mediaPlayerUtil;
    @Override
    public LifecycleObserver bindViewHolder(LeftVoiceViewHolder voiceViewHolder, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        activity = (FragmentActivity)voiceViewHolder.itemView.getContext();
        if (ImType.P2P == chatType) {
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) imMessageBaseModel;
            setP2PLeftVoiceData(voiceViewHolder, p2PMessageBaseModel);
        } else if (ImType.Team == chatType) {
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) imMessageBaseModel;
            setTeamLeftVoiceData(voiceViewHolder, teamMessageBaseModel);
        } else {

        }
        mediaPlayerUtil = MediaPlayerUtil.getInstance(activity);
        uuid = UUID.randomUUID().toString();
        anim = (AnimationDrawable) voiceViewHolder.leftVoice.getDrawable();
        return super.bindViewHolder(voiceViewHolder, imMessageBaseModel, chatType);
    }
    private void setP2PLeftVoiceData(LeftVoiceViewHolder leftVoiceViewHolder, P2PMessageBaseModel p2PMessageBaseModel){
        p2PMessageViewModel  = ViewModelProviders.of(activity).get(P2PMessageViewModel.class);
        String content = p2PMessageBaseModel.getSourceContent();
        MessageVoiceBean messageVoiceBean = gson.fromJson(content,MessageVoiceBean.class);
        long second = messageVoiceBean.getDuration() / 1000;
        leftVoiceViewHolder.duration.setText(second+"s");

        if (0 == messageVoiceBean.getReadStatus()){
            leftVoiceViewHolder.voiceUnread.setVisibility(View.VISIBLE);
        }else {
            leftVoiceViewHolder.voiceUnread.setVisibility(View.GONE);
        }

        leftVoiceViewHolder.itemView.setOnClickListener(v -> {
            boolean fileExists = FileUtils.fileExists(messageVoiceBean.getLocalUriString());
            Log.e("YM","文件是否存在:"+fileExists);
            if (fileExists){
                mediaPlayerUtil.addMediaPlayerListener(uuid, LeftVoiceChatViewHolderBind.this);
                mediaPlayerUtil.playVoice(uuid,messageVoiceBean.getLocalUriString());
            }else {
                leftVoiceViewHolder.progressBar.setVisibility(View.VISIBLE);
                HttpDownFileUtils.getInstance().downFileFromServiceToPublicDir(messageVoiceBean.getDescription(), leftVoiceViewHolder.itemView.getContext(), DIRECTORY_MUSIC, new OnFileDownListener() {
                    @Override
                    public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
                        if (status == 1){
                            String localPath = "";//10.0之上是uri，10.0之下是本地路径
                            if (object instanceof File){
                                File file = (File) object;
                                localPath = file.getAbsolutePath();
                            }else if (object instanceof Uri){
                                Uri uri = (Uri) object;
                                localPath = uri.toString();
                            }
                            messageVoiceBean.setLocalUriString(localPath);
                            messageVoiceBean.setReadStatus(1);
                            String source = gson.toJson(messageVoiceBean);
                            p2PMessageBaseModel.setSourceContent(source);
                            p2PMessageViewModel.update(p2PMessageBaseModel);
                            mediaPlayerUtil.addMediaPlayerListener(uuid, LeftVoiceChatViewHolderBind.this);
                            if (object instanceof File){
                                mediaPlayerUtil.initVoice(uuid,localPath);
                            }else if (object instanceof Uri){
                                Uri uri = (Uri) object;
                                mediaPlayerUtil.initVoice(uuid,uri);
                            }
                            leftVoiceViewHolder.itemView.post(new Runnable() {
                                @Override
                                public void run() {
                                    leftVoiceViewHolder.voiceUnread.setVisibility(View.GONE);
                                    leftVoiceViewHolder.progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                });
            }
//            playVoice(messageVoiceBean.getDescription());
        });
    }

    private void setTeamLeftVoiceData(LeftVoiceViewHolder leftVoiceViewHolder, TeamMessageBaseModel teamMessageBaseModel){
        teamMessageViewModel = ViewModelProviders.of(activity).get(TeamMessageViewModel.class);
        String content = teamMessageBaseModel.getSourceContent();
        MessageVoiceBean messageVoiceBean = gson.fromJson(content,MessageVoiceBean.class);
        long second = messageVoiceBean.getDuration() / 1000;
        leftVoiceViewHolder.duration.setText(second+"s");
        if (0 == messageVoiceBean.getReadStatus()){
            leftVoiceViewHolder.voiceUnread.setVisibility(View.VISIBLE);
        }else {
            leftVoiceViewHolder.voiceUnread.setVisibility(View.GONE);
        }
        leftVoiceViewHolder.itemView.setOnClickListener(v -> {
            boolean fileExists = FileUtils.fileExists(messageVoiceBean.getLocalUriString());
            Log.e("YM","文件是否存在:"+fileExists);
            if (fileExists){
                mediaPlayerUtil.addMediaPlayerListener(uuid, LeftVoiceChatViewHolderBind.this);
                mediaPlayerUtil.playVoice(uuid,messageVoiceBean.getLocalUriString());
            }else {
                leftVoiceViewHolder.progressBar.setVisibility(View.VISIBLE);
                HttpDownFileUtils.getInstance().downFileFromServiceToPublicDir(messageVoiceBean.getDescription(), leftVoiceViewHolder.itemView.getContext(), DIRECTORY_MUSIC, new OnFileDownListener() {
                    @Override
                    public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
                        if (status == 1){
                            String localPath = "";//10.0之上是uri，10.0之下是本地路径
                            if (object instanceof File){
                                File file = (File) object;
                                localPath = file.getAbsolutePath();
                            }else if (object instanceof Uri){
                                Uri uri = (Uri) object;
                                localPath = uri.toString();
                            }
                            messageVoiceBean.setLocalUriString(localPath);
                            messageVoiceBean.setReadStatus(1);
                            String source = gson.toJson(messageVoiceBean);
                            teamMessageBaseModel.setSourceContent(source);
                            teamMessageViewModel.update(teamMessageBaseModel);
                            mediaPlayerUtil.addMediaPlayerListener(uuid, LeftVoiceChatViewHolderBind.this);
                            if (object instanceof File){
                                mediaPlayerUtil.initVoice(uuid,localPath);
                            }else if (object instanceof Uri){
                                Uri uri = (Uri) object;
                                mediaPlayerUtil.initVoice(uuid,uri);
                            }
                            leftVoiceViewHolder.itemView.post(new Runnable() {
                                @Override
                                public void run() {
                                    leftVoiceViewHolder.voiceUnread.setVisibility(View.GONE);
                                    leftVoiceViewHolder.progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                });
            }
//            playVoice(messageVoiceBean.getDescription());
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop(){
        mediaPlayerUtil.onStop();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(){
        mediaPlayerUtil.onDestroy();
        mediaPlayerUtil.removeMediaPlayerListener(uuid);
    }


    @Override
    public boolean onError(String tag, MediaPlayer mp, int what, int extra) {
        anim.stop();
        anim.selectDrawable(0);
        return false;
    }

    @Override
    public void onCompletion(String tag, MediaPlayer mp) {
        anim.stop();
        anim.selectDrawable(0);
    }

    @Override
    public void onPrepared(String tag, MediaPlayer mp) {
        anim.stop();
        anim.start();
    }

    @Override
    public void onStop(String tag) {
        anim.stop();
        anim.selectDrawable(0);
    }
}