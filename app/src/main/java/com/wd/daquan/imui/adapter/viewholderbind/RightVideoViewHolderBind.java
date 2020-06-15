package com.wd.daquan.imui.adapter.viewholderbind;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.dq.im.bean.im.MessageVideoBean;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.type.ImType;
import com.dq.im.util.oss.AliOssUtil;
import com.dq.im.util.oss.OnFileDownListener;
import com.dq.im.viewmodel.P2PMessageViewModel;
import com.dq.im.viewmodel.TeamMessageViewModel;
import com.google.gson.Gson;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.imui.activity.VideoDetailsActivity;
import com.wd.daquan.imui.adapter.viewholder.RightVideoViewHolder;
import com.wd.daquan.util.FileUtils;

import static android.os.Environment.DIRECTORY_MOVIES;
import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * 右侧视频内容填充
 */
public class RightVideoViewHolderBind extends BaseRightViewHolderBind<RightVideoViewHolder> {
    private P2PMessageViewModel p2PMessageViewModel;
    private TeamMessageViewModel teamMessageViewModel;
    private FragmentActivity activity;
    private Gson gson = new Gson();

    @Override
    public LifecycleObserver bindViewHolder(RightVideoViewHolder videoViewHolder, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        activity = (FragmentActivity)videoViewHolder.itemView.getContext();
        if (ImType.P2P == chatType) {
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) imMessageBaseModel;
            setP2PRightVideoData(videoViewHolder, p2PMessageBaseModel);
        } else if (ImType.Team == chatType) {
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) imMessageBaseModel;
            setTeamRightVideoData(videoViewHolder, teamMessageBaseModel);
        }
        return super.bindViewHolder(videoViewHolder, imMessageBaseModel, chatType);
    }

    private void setP2PRightVideoData(RightVideoViewHolder rightVideoViewHolder, P2PMessageBaseModel p2PMessageBaseModel){
        p2PMessageViewModel  = ViewModelProviders.of(activity).get(P2PMessageViewModel.class);
        String content = p2PMessageBaseModel.getSourceContent();

        MessageVideoBean messageVideoBean = gson.fromJson(content,MessageVideoBean.class);
        if (TextUtils.isEmpty(messageVideoBean.getThumbLocalPath())){
            return;
        }
        Uri photoUri = Uri.parse(messageVideoBean.getThumbLocalPath());
        boolean fileExists = FileUtils.fileExists(messageVideoBean.getThumbLocalPath());
        if (fileExists){
//            GlideUtil.loadNormalImgByNet(rightVideoViewHolder.itemView.getContext(),photoUri,rightVideoViewHolder.rightVideoBg);
            GlideUtils.loadRound(rightVideoViewHolder.itemView.getContext(),photoUri,rightVideoViewHolder.rightVideoBg,10);
        }else {
            AliOssUtil.getInstance().downMusicVideoPicFromService(messageVideoBean.getThumbPath(), rightVideoViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
                @Override
                public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
                    if (status == 1){
                        Uri uri = (Uri) object;
                        messageVideoBean.setThumbLocalPath(uri.toString());
//                        GlideUtil.loadNormalImgByNet(rightVideoViewHolder.itemView.getContext(),uri,rightVideoViewHolder.rightVideoBg);
                        GlideUtils.loadRound(rightVideoViewHolder.itemView.getContext(),uri,rightVideoViewHolder.rightVideoBg,10);
                        String source = gson.toJson(messageVideoBean);
                        p2PMessageBaseModel.setSourceContent(source);
                        p2PMessageViewModel.update(p2PMessageBaseModel);
                    }
                }
            });
        }
//        GlideUtil.loadNormalImgByNet(rightVideoViewHolder.itemView.getContext(),messageVideoBean.getThumbPath(),rightVideoViewHolder.rightVideoBg);
        rightVideoViewHolder.itemRightVideoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri videoUri = Uri.parse(messageVideoBean.getVideoLocalPath());
                boolean fileExists = FileUtils.fileExists(messageVideoBean.getVideoLocalPath());
                Log.e("YM","文件是否存在:"+fileExists);
                if (fileExists){
//                    initVoice(videoUri);
                    playVideo(v.getContext(),videoUri.toString());
                }else {
                    rightVideoViewHolder.videoLoading.setVisibility(View.VISIBLE);
                    rightVideoViewHolder.rightVideo.setVisibility(View.GONE);
                    AliOssUtil.getInstance().downMusicVideoPicFromService(messageVideoBean.getVideoPath(), rightVideoViewHolder.itemView.getContext(), DIRECTORY_MOVIES, new OnFileDownListener() {
                        @Override
                        public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
                            if (status == 1){
                                Uri uri = (Uri) object;
                                messageVideoBean.setVideoLocalPath(uri.toString());
                                String source = gson.toJson(messageVideoBean);
                                p2PMessageBaseModel.setSourceContent(source);
                                p2PMessageViewModel.update(p2PMessageBaseModel);
                                playVideo(v.getContext(),uri.toString());
                                rightVideoViewHolder.itemView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        rightVideoViewHolder.videoLoading.setVisibility(View.GONE);
                                        rightVideoViewHolder.rightVideo.setVisibility(View.VISIBLE);
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

    private void setTeamRightVideoData(RightVideoViewHolder rightVideoViewHolder, TeamMessageBaseModel teamMessageBaseModel){
        teamMessageViewModel = ViewModelProviders.of(activity).get(TeamMessageViewModel.class);
        String content = teamMessageBaseModel.getSourceContent();
        MessageVideoBean messageVideoBean = gson.fromJson(content,MessageVideoBean.class);
        Uri photoUri = Uri.parse(messageVideoBean.getThumbLocalPath());
        boolean fileExists = FileUtils.fileExists(messageVideoBean.getThumbLocalPath());
        if (fileExists){
//            GlideUtil.loadNormalImgByNet(rightVideoViewHolder.itemView.getContext(),photoUri,rightVideoViewHolder.rightVideoBg);
            GlideUtils.loadRound(rightVideoViewHolder.itemView.getContext(),photoUri,rightVideoViewHolder.rightVideoBg,10);
        }else {
            AliOssUtil.getInstance().downMusicVideoPicFromService(messageVideoBean.getThumbPath(), rightVideoViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
                @Override
                public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
                    if (status == 1){
                        Uri uri = (Uri) object;
                        messageVideoBean.setThumbLocalPath(uri.toString());
//                        GlideUtil.loadNormalImgByNet(rightVideoViewHolder.itemView.getContext(),uri,rightVideoViewHolder.rightVideoBg);
                        GlideUtils.loadRound(rightVideoViewHolder.itemView.getContext(),uri,rightVideoViewHolder.rightVideoBg,10);
                        String source = gson.toJson(messageVideoBean);
                        teamMessageBaseModel.setSourceContent(source);
                        teamMessageViewModel.update(teamMessageBaseModel);
                    }
                }
            });
        }
//        GlideUtil.loadNormalImgByNet(rightVideoViewHolder.itemView.getContext(),messageVideoBean.getThumbPath(),rightVideoViewHolder.rightVideoBg);
        rightVideoViewHolder.itemRightVideoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri videoUri = Uri.parse(messageVideoBean.getVideoLocalPath());
                boolean fileExists = FileUtils.fileExists(messageVideoBean.getVideoLocalPath());
                Log.e("YM","文件是否存在:"+fileExists);
                if (fileExists){
//                    initVoice(videoUri);
                    playVideo(v.getContext(),videoUri.toString());
                }else {
                    rightVideoViewHolder.videoLoading.setVisibility(View.VISIBLE);
                    rightVideoViewHolder.rightVideo.setVisibility(View.GONE);
                    AliOssUtil.getInstance().downMusicVideoPicFromService(messageVideoBean.getVideoPath(), rightVideoViewHolder.itemView.getContext(), DIRECTORY_MOVIES, new OnFileDownListener() {
                        @Override
                        public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
                            if (status == 1){
                                Uri uri = (Uri) object;
                                messageVideoBean.setVideoLocalPath(uri.toString());
                                String source = gson.toJson(messageVideoBean);
                                teamMessageBaseModel.setSourceContent(source);
                                teamMessageViewModel.update(teamMessageBaseModel);
                                playVideo(v.getContext(),uri.toString());
                                rightVideoViewHolder.itemView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        rightVideoViewHolder.videoLoading.setVisibility(View.GONE);
                                        rightVideoViewHolder.rightVideo.setVisibility(View.VISIBLE);
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
        Intent intent = new Intent(context, VideoDetailsActivity.class);
        intent.putExtra(VideoDetailsActivity.VIDEO_PATH_ACTION,videoPath);
        context.startActivity(intent);
    }
}