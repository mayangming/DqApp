package com.wd.daquan.imui.adapter.viewholderbind;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.dq.im.bean.im.MessagePhotoBean;
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
import com.wd.daquan.imui.adapter.viewholder.LeftImgViewHolder;
import com.wd.daquan.util.FileUtils;
import com.wd.daquan.util.GlideUtil;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * 左侧图像内容填充
 */
public class LeftImgViewHolderBind extends BaseLeftViewHolderBind<LeftImgViewHolder> {
    private Gson gson = new Gson();
    private P2PMessageViewModel p2PMessageViewModel;
    private TeamMessageViewModel teamMessageViewModel;
    FragmentActivity activity;
    @Override
    public LifecycleObserver bindViewHolder(LeftImgViewHolder imgViewHolder, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        activity = (FragmentActivity)imgViewHolder.itemView.getContext();
        if (ImType.P2P == chatType) {
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) imMessageBaseModel;
            setP2PLeftImgData(imgViewHolder, p2PMessageBaseModel);
        } else if (ImType.Team == chatType) {
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) imMessageBaseModel;
            setTeamLeftImgData(imgViewHolder, teamMessageBaseModel);
        } else {//什么都不做

        }
        return super.bindViewHolder(imgViewHolder, imMessageBaseModel, chatType);
    }

    private void setP2PLeftImgData(LeftImgViewHolder leftImgViewHolder, P2PMessageBaseModel p2PMessageBaseModel){
        p2PMessageViewModel  = ViewModelProviders.of(activity).get(P2PMessageViewModel.class);
        String content = p2PMessageBaseModel.getSourceContent();
        MessagePhotoBean messagePhotoBean = gson.fromJson(content,MessagePhotoBean.class);
        Uri photoUri = Uri.parse(messagePhotoBean.getLocalUriString());
        boolean fileExists = FileUtils.fileExists(messagePhotoBean.getLocalUriString());
        if (fileExists){
            GlideUtils.loadRound(leftImgViewHolder.itemView.getContext(),photoUri,leftImgViewHolder.leftImgContent,10);
        }else {
            AliOssUtil.getInstance().downMusicVideoPicFromService(messagePhotoBean.getDescription(), leftImgViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
                @Override
                public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
                    if (status == 1){
                        Uri uri = (Uri) object;
                        messagePhotoBean.setLocalUriString(uri.toString());
                        GlideUtils.loadRound(leftImgViewHolder.itemView.getContext(),uri,leftImgViewHolder.leftImgContent,10);
                        String source = gson.toJson(messagePhotoBean);
                        p2PMessageBaseModel.setSourceContent(source);
                        p2PMessageViewModel.update(p2PMessageBaseModel);
                    }
                }
            });
        }
//        leftImgViewHolder.leftImgContent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), PhotoDetailsActivity.class);
//                intent.putExtra(PhotoDetailsActivity.PHOTO_URL,messagePhotoBean.getDescription());
//                v.getContext().startActivity(intent);
//            }
//        });
    }

    private void setTeamLeftImgData(LeftImgViewHolder leftImgViewHolder, TeamMessageBaseModel teamMessageBaseModel){
        teamMessageViewModel = ViewModelProviders.of(activity).get(TeamMessageViewModel.class);
        String content = teamMessageBaseModel.getSourceContent();
        MessagePhotoBean messagePhotoBean = gson.fromJson(content,MessagePhotoBean.class);
        Uri photoUri = Uri.parse(messagePhotoBean.getLocalUriString());
        boolean fileExists = FileUtils.fileExists(messagePhotoBean.getLocalUriString());
        if (fileExists){
            GlideUtil.loadNormalImgByNet(leftImgViewHolder.itemView.getContext(),photoUri,leftImgViewHolder.leftImgContent);
        }else {
            AliOssUtil.getInstance().downMusicVideoPicFromService(messagePhotoBean.getDescription(), leftImgViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
                @Override
                public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
                    if (status == 1){
                        Uri uri = (Uri) object;
                        messagePhotoBean.setLocalUriString(uri.toString());
                        GlideUtil.loadNormalImgByNet(leftImgViewHolder.itemView.getContext(),uri,leftImgViewHolder.leftImgContent);
                        String source = gson.toJson(messagePhotoBean);
                        teamMessageBaseModel.setSourceContent(source);
                        teamMessageViewModel.update(teamMessageBaseModel);
                    }
                }
            });
        }
//        leftImgViewHolder.leftImgContent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), PhotoDetailsActivity.class);
//                intent.putExtra(PhotoDetailsActivity.PHOTO_URL,messagePhotoBean.getDescription());
//                v.getContext().startActivity(intent);
//            }
//        });
    }
}