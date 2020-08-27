package com.wd.daquan.imui.adapter.viewholderbind;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

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
import com.dq.im.viewmodel.P2PMessageViewModel;
import com.dq.im.viewmodel.TeamMessageViewModel;
import com.google.gson.Gson;
import com.wd.daquan.imui.adapter.viewholder.RightVoiceViewHolder;
import com.wd.daquan.util.FileUtils;

import java.io.File;
import java.util.UUID;

import static android.os.Environment.DIRECTORY_MUSIC;

/**
 * 右侧音频内容填充
 */
public class RightVoiceViewHolderBind extends BaseRightViewHolderBind<RightVoiceViewHolder> implements MediaPlayerIpc {
    private P2PMessageViewModel p2PMessageViewModel;
    private TeamMessageViewModel teamMessageViewModel;
    private FragmentActivity activity;
    private Gson gson = new Gson();
    private AnimationDrawable anim;
    private MediaPlayerUtil mediaPlayerUtil;
    private String uuid;
    public RightVoiceViewHolderBind() {
    }

    @Override
    public LifecycleObserver bindViewHolder(RightVoiceViewHolder rightVoiceViewHolder, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        activity = (FragmentActivity)rightVoiceViewHolder.itemView.getContext();
        if (ImType.P2P == chatType) {
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) imMessageBaseModel;
            setP2PRightVoiceData(rightVoiceViewHolder, p2PMessageBaseModel);
        } else if (ImType.Team == chatType) {
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) imMessageBaseModel;
            setTeamRightVoiceData(rightVoiceViewHolder, teamMessageBaseModel);
        }
        mediaPlayerUtil = MediaPlayerUtil.getInstance(activity);
        uuid = UUID.randomUUID().toString();
        anim = (AnimationDrawable) rightVoiceViewHolder.rightVoice.getDrawable();
        return super.bindViewHolder(rightVoiceViewHolder, imMessageBaseModel, chatType);
    }

    private void setP2PRightVoiceData(RightVoiceViewHolder rightVoiceViewHolder, P2PMessageBaseModel p2PMessageBaseModel){
        p2PMessageViewModel  = new ViewModelProvider(activity).get(P2PMessageViewModel.class);
        String content = p2PMessageBaseModel.getSourceContent();
        MessageVoiceBean messageVoiceBean = gson.fromJson(content,MessageVoiceBean.class);
        Log.e("YM","音频文件地址:"+messageVoiceBean.toString());
        long second = messageVoiceBean.getDuration() / 1000;
        rightVoiceViewHolder.duration.setText(second+"s");
        rightVoiceViewHolder.itemView.setOnClickListener(v ->{
            boolean fileExists = FileUtils.fileExists(messageVoiceBean.getLocalUriString());
            Log.e("YM","文件是否存在:"+fileExists);
            if (fileExists){
                mediaPlayerUtil.addMediaPlayerListener(uuid, RightVoiceViewHolderBind.this);
                mediaPlayerUtil.playVoice(uuid,messageVoiceBean.getLocalUriString());
            }else {
                rightVoiceViewHolder.progressBar.setVisibility(View.VISIBLE);
                HttpDownFileUtils.getInstance().downFileFromServiceToPublicDir(messageVoiceBean.getDescription(), rightVoiceViewHolder.itemView.getContext(), DIRECTORY_MUSIC, new OnFileDownListener() {
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
                            String source = gson.toJson(messageVoiceBean);
                            p2PMessageBaseModel.setSourceContent(source);
                            p2PMessageViewModel.update(p2PMessageBaseModel);
                            mediaPlayerUtil.addMediaPlayerListener(uuid, RightVoiceViewHolderBind.this);
                            if (object instanceof File){
                                mediaPlayerUtil.initVoice(uuid,localPath);
                            }else if (object instanceof Uri){
                                Uri uri = (Uri) object;
                                mediaPlayerUtil.initVoice(uuid,uri);
                            }
                            rightVoiceViewHolder.itemView.post(new Runnable() {
                                @Override
                                public void run() {
                                    rightVoiceViewHolder.progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                });
            }
//            playVoice(messageVoiceBean.getDescription());
        });
    }

    private void setTeamRightVoiceData(RightVoiceViewHolder rightVoiceViewHolder, TeamMessageBaseModel teamMessageBaseModel){
        teamMessageViewModel = new ViewModelProvider(activity).get(TeamMessageViewModel.class);
        String content = teamMessageBaseModel.getSourceContent();
        MessageVoiceBean messageVoiceBean = gson.fromJson(content,MessageVoiceBean.class);
        long second = messageVoiceBean.getDuration() / 1000;
        rightVoiceViewHolder.duration.setText(second+"s");
        rightVoiceViewHolder.itemView.setOnClickListener(v ->{
            boolean fileExists = FileUtils.fileExists(messageVoiceBean.getLocalUriString());
            Log.e("YM","文件是否存在:"+fileExists);
            if (fileExists){
                mediaPlayerUtil.addMediaPlayerListener(uuid, RightVoiceViewHolderBind.this);
                mediaPlayerUtil.playVoice(uuid,messageVoiceBean.getLocalUriString());
            }else {
                rightVoiceViewHolder.progressBar.setVisibility(View.VISIBLE);
                HttpDownFileUtils.getInstance().downFileFromServiceToPublicDir(messageVoiceBean.getDescription(), rightVoiceViewHolder.itemView.getContext(), DIRECTORY_MUSIC, new OnFileDownListener() {
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
                            String source = gson.toJson(messageVoiceBean);
                            teamMessageBaseModel.setSourceContent(source);
                            teamMessageViewModel.update(teamMessageBaseModel);
                            mediaPlayerUtil.addMediaPlayerListener(uuid, RightVoiceViewHolderBind.this);
                            if (object instanceof File){
                                mediaPlayerUtil.initVoice(uuid,localPath);
                            }else if (object instanceof Uri){
                                Uri uri = (Uri) object;
                                mediaPlayerUtil.initVoice(uuid,uri);
                            }
                            rightVoiceViewHolder.itemView.post(new Runnable() {
                                @Override
                                public void run() {
                                    rightVoiceViewHolder.progressBar.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                });
            }
//            initVoice(messageVoiceBean.getDescription())
        });
    }

    //这种写法是java7的，如果java8的时候使用重写的方式。参考以下链接
    //https://developer.android.google.cn/reference/androidx/lifecycle/Lifecycle?hl=zh_cn
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop(){
        mediaPlayerUtil.onStop();
        anim.stop();
        anim.selectDrawable(0);
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