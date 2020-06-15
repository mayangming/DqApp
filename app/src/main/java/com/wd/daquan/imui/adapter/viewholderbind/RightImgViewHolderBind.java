package com.wd.daquan.imui.adapter.viewholderbind;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.provider.DocumentFile;

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
import com.wd.daquan.imui.adapter.viewholder.RightImgViewHolder;
import com.wd.daquan.util.FileUtils;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * 右侧图像内容填充
 */
public class RightImgViewHolderBind extends BaseRightViewHolderBind<RightImgViewHolder> {
    private Gson gson = new Gson();
    private P2PMessageViewModel p2PMessageViewModel;
    private TeamMessageViewModel teamMessageViewModel;
    private FragmentActivity activity;
    @Override
    public LifecycleObserver bindViewHolder(RightImgViewHolder rightImgViewHolder, ImMessageBaseModel imMessageBaseModel, ImType chatType) {
        activity = (FragmentActivity)rightImgViewHolder.itemView.getContext();
        if (ImType.P2P == chatType) {
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) imMessageBaseModel;
            setP2PRightImgData(rightImgViewHolder, p2PMessageBaseModel);
        } else if (ImType.Team == chatType) {
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) imMessageBaseModel;
            setTeamRightImgData(rightImgViewHolder, teamMessageBaseModel);
        }
        return super.bindViewHolder(rightImgViewHolder, imMessageBaseModel, chatType);
    }

    private void setP2PRightImgData(RightImgViewHolder rightImgViewHolder, P2PMessageBaseModel p2PMessageBaseModel){
        p2PMessageViewModel  = ViewModelProviders.of(activity).get(P2PMessageViewModel.class);
        String content = p2PMessageBaseModel.getSourceContent();
        MessagePhotoBean messagePhotoBean = gson.fromJson(content,MessagePhotoBean.class);
        Uri photoUri = Uri.parse(messagePhotoBean.getLocalUriString());
        DocumentFile videoDocumentFile = DocumentFile.fromSingleUri(rightImgViewHolder.itemView.getContext(), photoUri);
        boolean fileExists = videoDocumentFile.exists();
        if (fileExists){
//            GlideUtil.loadNormalImgByNet(rightImgViewHolder.itemView.getContext(),photoUri,rightImgViewHolder.itemRightImgIv);
            GlideUtils.loadRound(rightImgViewHolder.itemView.getContext(),photoUri,rightImgViewHolder.itemRightImgIv,10);
        }else {
            AliOssUtil.getInstance().downMusicVideoPicFromService(messagePhotoBean.getDescription(), rightImgViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
                @Override
                public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
                    if (status == 1){
                        Uri uri = (Uri) object;
                        messagePhotoBean.setLocalUriString(uri.toString());
//                        GlideUtil.loadNormalImgByNet(rightImgViewHolder.itemView.getContext(),uri,rightImgViewHolder.itemRightImgIv);
                        GlideUtils.loadRound(rightImgViewHolder.itemView.getContext(),uri,rightImgViewHolder.itemRightImgIv,10);
                        String source = gson.toJson(messagePhotoBean);
                        p2PMessageBaseModel.setSourceContent(source);
                        p2PMessageViewModel.update(p2PMessageBaseModel);
                    }
                }
            });
        }

//        GlideUtil.loadNormalImgByNet(rightImgViewHolder.itemView.getContext(),messagePhotoBean.getDescription(),rightImgViewHolder.itemRightImgIv);
//        rightImgViewHolder.itemRightImgIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), PhotoDetailsActivity.class);
//                intent.putExtra(PhotoDetailsActivity.PHOTO_URL,messagePhotoBean.getDescription());
//                v.getContext().startActivity(intent);
//            }
//        });
    }

    private void setTeamRightImgData(RightImgViewHolder rightImgViewHolder, TeamMessageBaseModel teamMessageBaseModel){
        teamMessageViewModel = ViewModelProviders.of(activity).get(TeamMessageViewModel.class);
        String content = teamMessageBaseModel.getSourceContent();
        MessagePhotoBean messagePhotoBean = gson.fromJson(content,MessagePhotoBean.class);
        Uri photoUri = Uri.parse(messagePhotoBean.getLocalUriString());
        boolean fileExists = FileUtils.fileExists(messagePhotoBean.getLocalUriString());
        if (fileExists){
//            GlideUtil.loadNormalImgByNet(rightImgViewHolder.itemView.getContext(),photoUri,rightImgViewHolder.itemRightImgIv);
            GlideUtils.loadRound(rightImgViewHolder.itemView.getContext(),photoUri,rightImgViewHolder.itemRightImgIv,10);
        }else {
            AliOssUtil.getInstance().downMusicVideoPicFromService(messagePhotoBean.getDescription(), rightImgViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
                @Override
                public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
                    if (status == 1){
                        Uri uri = (Uri) object;
                        messagePhotoBean.setLocalUriString(uri.toString());
//                        GlideUtil.loadNormalImgByNet(rightImgViewHolder.itemView.getContext(),uri,rightImgViewHolder.itemRightImgIv);
                        GlideUtils.loadRound(rightImgViewHolder.itemView.getContext(),photoUri,rightImgViewHolder.itemRightImgIv,10);
                        String source = gson.toJson(messagePhotoBean);
                        teamMessageBaseModel.setSourceContent(source);
                        teamMessageViewModel.update(teamMessageBaseModel);
                    }
                }
            });
        }
//        GlideUtil.loadNormalImgByNet(rightImgViewHolder.itemView.getContext(),messagePhotoBean.getDescription(),rightImgViewHolder.itemRightImgIv);
//        rightImgViewHolder.itemRightImgIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), PhotoDetailsActivity.class);
//                intent.putExtra(PhotoDetailsActivity.PHOTO_URL,messagePhotoBean.getDescription());
//                v.getContext().startActivity(intent);
//            }
//        });
    }
}