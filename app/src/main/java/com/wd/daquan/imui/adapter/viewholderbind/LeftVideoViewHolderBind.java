package com.wd.daquan.imui.adapter.viewholderbind;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProviders;

import android.net.Uri;
import androidx.fragment.app.FragmentActivity;

import com.dq.im.bean.im.MessageVideoBean;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.type.ImType;
import com.dq.im.util.download.OnFileDownListener;
import com.dq.im.util.oss.AliOssUtil;
import com.dq.im.viewmodel.P2PMessageViewModel;
import com.dq.im.viewmodel.TeamMessageViewModel;
import com.google.gson.Gson;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.imui.adapter.viewholder.LeftVideoViewHolder;
import com.wd.daquan.util.FileUtils;

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
    }
}