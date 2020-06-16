package com.wd.daquan.imui.adapter.viewholderbind;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.dq.im.bean.im.MessageVideoBean;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.type.ImType;
import com.dq.im.util.download.HttpDownFileUtils;
import com.dq.im.util.download.OnFileDownListener;
import com.dq.im.util.oss.AliOssUtil;
import com.dq.im.viewmodel.P2PMessageViewModel;
import com.dq.im.viewmodel.TeamMessageViewModel;
import com.google.gson.Gson;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.imui.activity.VideoDetailsActivity;
import com.wd.daquan.imui.adapter.viewholder.LeftVideoViewHolder;
import com.wd.daquan.util.FileUtils;

import java.io.File;

import static android.os.Environment.DIRECTORY_MOVIES;
import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * 左侧视频内容填充
 */
public class LeftVideoViewHolderBind extends BaseLeftViewHolderBind<LeftVideoViewHolder> {
    private P2PMessageViewModel p2PMessageViewModel;
    private TeamMessageViewModel teamMessageViewModel;
    private FragmentActivity activity;
    private Gson gson = new Gson();

    @Override
    public LifecycleObserver bindViewHolder(LeftVideoViewHolder videoViewHolder, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        activity = (FragmentActivity)videoViewHolder.itemView.getContext();
        if (ImType.P2P == chatType) {
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) imMessageBaseModel;
            setP2PLeftVideoData(videoViewHolder, p2PMessageBaseModel);
        } else if (ImType.Team == chatType) {
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) imMessageBaseModel;
            setTeamLeftVideoData(videoViewHolder, teamMessageBaseModel);
        } else {

        }

        return super.bindViewHolder(videoViewHolder, imMessageBaseModel, chatType);
    }

    private void setP2PLeftVideoData(LeftVideoViewHolder leftVideoViewHolder, P2PMessageBaseModel p2PMessageBaseModel){
        p2PMessageViewModel  = ViewModelProviders.of(activity).get(P2PMessageViewModel.class);
        String content = p2PMessageBaseModel.getSourceContent();
        MessageVideoBean messageVideoBean = gson.fromJson(content,MessageVideoBean.class);
        Uri photoUri = Uri.parse(messageVideoBean.getThumbLocalPath());
        boolean fileExists = FileUtils.fileExists(messageVideoBean.getThumbLocalPath());
        if (fileExists){
//            GlideUtil.loadNormalImgByNet(leftVideoViewHolder.itemView.getContext(),photoUri,leftVideoViewHolder.leftVideoBg);
            GlideUtils.loadRound(leftVideoViewHolder.itemView.getContext(),photoUri,leftVideoViewHolder.leftVideoBg,10);
        }else {
            AliOssUtil.getInstance().downMusicVideoPicFromService(messageVideoBean.getThumbPath(), leftVideoViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
                @Override
                public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
                    if (status == 1){
                        Uri uri = (Uri) object;
                        messageVideoBean.setThumbLocalPath(uri.toString());
//                        GlideUtil.loadNormalImgByNet(leftVideoViewHolder.itemView.getContext(),uri,leftVideoViewHolder.leftVideoBg);
                        GlideUtils.loadRound(leftVideoViewHolder.itemView.getContext(),uri,leftVideoViewHolder.leftVideoBg,10);
                        String source = gson.toJson(messageVideoBean);
                        p2PMessageBaseModel.setSourceContent(source);
                        p2PMessageViewModel.update(p2PMessageBaseModel);
                    }
                }
            });
        }
//        GlideUtil.loadNormalImgByNet(leftVideoViewHolder.itemView.getContext(),messageVideoBean.getThumbPath(),leftVideoViewHolder.leftVideoBg);
        leftVideoViewHolder.leftVideoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri videoUri = Uri.parse(messageVideoBean.getVideoLocalPath());
                boolean fileExists = FileUtils.fileExists(messageVideoBean.getVideoLocalPath());
                Log.e("YM","文件是否存在:"+fileExists);
                if (fileExists){
//                    initVoice(videoUri);
                    playVideo(v.getContext(),videoUri.toString());
                }else {
                    leftVideoViewHolder.videoLoading.setVisibility(View.VISIBLE);
                    leftVideoViewHolder.leftVideo.setVisibility(View.GONE);
                    Log.e("YM","文件开始进行下载");
//                    AliOssUtil.getInstance().downMusicVideoPicFromService(messageVideoBean.getVideoPath(), leftVideoViewHolder.itemView.getContext(), DIRECTORY_MOVIES, new OnFileDownListener() {
//                        @Override
//                        public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
//                            if (status == 1){
//                                Uri uri = (Uri) object;
//                                messageVideoBean.setVideoLocalPath(uri.toString());
//                                String source = gson.toJson(messageVideoBean);
//                                p2PMessageBaseModel.setSourceContent(source);
//                                p2PMessageViewModel.update(p2PMessageBaseModel);
//                                playVideo(v.getContext(),uri.toString());
//                                leftVideoViewHolder.itemView.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        leftVideoViewHolder.videoLoading.setVisibility(View.GONE);
//                                        leftVideoViewHolder.leftVideo.setVisibility(View.VISIBLE);
//                                    }
//                                });
//                            }
//                        }
//                    });
                    HttpDownFileUtils.getInstance().downFileFromServiceToPublicDir(messageVideoBean.getVideoPath(), leftVideoViewHolder.itemView.getContext(), DIRECTORY_MOVIES, new OnFileDownListener() {
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
                                messageVideoBean.setVideoLocalPath(localPath);
                                String source = gson.toJson(messageVideoBean);
                                p2PMessageBaseModel.setSourceContent(source);
                                p2PMessageViewModel.update(p2PMessageBaseModel);
                                playVideo(v.getContext(),localPath);
                                leftVideoViewHolder.itemView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        leftVideoViewHolder.videoLoading.setVisibility(View.GONE);
                                        leftVideoViewHolder.leftVideo.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    });
                }
//                playVideo(v.getContext(),messageVideoBean.getVideoPath());
            }
        });
    }
    private void setTeamLeftVideoData(LeftVideoViewHolder leftVideoViewHolder, TeamMessageBaseModel teamMessageBaseModel){
        teamMessageViewModel = ViewModelProviders.of(activity).get(TeamMessageViewModel.class);
        String content = teamMessageBaseModel.getSourceContent();
        MessageVideoBean messageVideoBean = gson.fromJson(content,MessageVideoBean.class);
        Uri photoUri = Uri.parse(messageVideoBean.getThumbLocalPath());
        boolean fileExists = FileUtils.fileExists(messageVideoBean.getThumbLocalPath());
        if (fileExists){
//            GlideUtil.loadNormalImgByNet(leftVideoViewHolder.itemView.getContext(),photoUri,leftVideoViewHolder.leftVideoBg);
            GlideUtils.loadRound(leftVideoViewHolder.itemView.getContext(),photoUri,leftVideoViewHolder.leftVideoBg,10);
        }else {
            AliOssUtil.getInstance().downMusicVideoPicFromService(messageVideoBean.getThumbPath(), leftVideoViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
                @Override
                public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
                    if (status == 1){
                        Uri uri = (Uri) object;
                        messageVideoBean.setThumbLocalPath(uri.toString());
//                        GlideUtil.loadNormalImgByNet(leftVideoViewHolder.itemView.getContext(),uri,leftVideoViewHolder.leftVideoBg);
                        GlideUtils.loadRound(leftVideoViewHolder.itemView.getContext(),uri,leftVideoViewHolder.leftVideoBg,10);
                        String source = gson.toJson(messageVideoBean);
                        teamMessageBaseModel.setSourceContent(source);
                        teamMessageViewModel.update(teamMessageBaseModel);
                    }
                }
            });
        }
//        GlideUtil.loadNormalImgByNet(leftVideoViewHolder.itemView.getContext(),messageVideoBean.getThumbPath(),leftVideoViewHolder.leftVideoBg);
        leftVideoViewHolder.leftVideoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri videoUri = Uri.parse(messageVideoBean.getVideoLocalPath());
                boolean fileExists = FileUtils.fileExists(messageVideoBean.getVideoLocalPath());
                Log.e("YM","文件是否存在:"+fileExists);
                if (fileExists){
//                    initVoice(videoUri);
                    playVideo(v.getContext(),videoUri.toString());
                }else {
                    Log.e("YM","文件开始进行下载");
                    leftVideoViewHolder.videoLoading.setVisibility(View.VISIBLE);
                    leftVideoViewHolder.leftVideo.setVisibility(View.GONE);
//                    AliOssUtil.getInstance().downMusicVideoPicFromService(messageVideoBean.getVideoPath(), leftVideoViewHolder.itemView.getContext(), DIRECTORY_MOVIES, new OnFileDownListener() {
//                        @Override
//                        public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
//                            if (status == 1){
//                                Uri uri = (Uri) object;
//                                messageVideoBean.setVideoLocalPath(uri.toString());
//                                String source = gson.toJson(messageVideoBean);
//                                teamMessageBaseModel.setSourceContent(source);
//                                teamMessageViewModel.update(teamMessageBaseModel);
//                                playVideo(v.getContext(),uri.toString());
//                                leftVideoViewHolder.itemView.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        leftVideoViewHolder.videoLoading.setVisibility(View.GONE);
//                                        leftVideoViewHolder.leftVideo.setVisibility(View.VISIBLE);
//                                    }
//                                });
//                            }
//                        }
//                    });
                    HttpDownFileUtils.getInstance().downFileFromServiceToPublicDir(messageVideoBean.getVideoPath(), leftVideoViewHolder.itemView.getContext(), DIRECTORY_MOVIES, new OnFileDownListener() {
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
                                messageVideoBean.setVideoLocalPath(localPath);
                                String source = gson.toJson(messageVideoBean);
                                teamMessageBaseModel.setSourceContent(source);
                                teamMessageViewModel.update(teamMessageBaseModel);
                                playVideo(v.getContext(),localPath);
                                leftVideoViewHolder.itemView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        leftVideoViewHolder.videoLoading.setVisibility(View.GONE);
                                        leftVideoViewHolder.leftVideo.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    });
                }
//                playVideo(v.getContext(),messageVideoBean.getVideoPath());
            }
        });
    }

    /**
     * 播放视频文件
     */
    private void playVideo(Context context,String videoPath){
        Log.e("YM","开始播放视频:"+videoPath);
        Intent intent = new Intent(context, VideoDetailsActivity.class);
        intent.putExtra(VideoDetailsActivity.VIDEO_PATH_ACTION,videoPath);
        context.startActivity(intent);
    }
}