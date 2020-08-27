package com.wd.daquan.imui.adapter.viewholderbind;

import androidx.lifecycle.LifecycleObserver;
import android.net.Uri;
import android.os.Build;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;

import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.type.ImType;
import com.dq.im.util.download.HttpDownFileUtils;
import com.dq.im.util.download.OnFileDownListener;
import com.dq.im.viewmodel.P2PMessageViewModel;
import com.dq.im.viewmodel.TeamMessageViewModel;
import com.google.gson.Gson;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.imui.adapter.viewholder.RightImgViewHolder;
import com.wd.daquan.util.FileUtils;

import java.io.File;

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
        p2PMessageViewModel  = new ViewModelProvider(activity).get(P2PMessageViewModel.class);
        String content = p2PMessageBaseModel.getSourceContent();
        MessagePhotoBean messagePhotoBean = gson.fromJson(content,MessagePhotoBean.class);
//        DocumentFile videoDocumentFile = DocumentFile.fromSingleUri(rightImgViewHolder.itemView.getContext(), photoUri);
//        boolean fileExists = videoDocumentFile.exists();
        boolean fileExists = FileUtils.fileExists(messagePhotoBean.getLocalUriString());
        Log.e("YM","Right图片是否存在:"+fileExists+"--->路径:"+messagePhotoBean.getLocalUriString());
        if (fileExists){
//            GlideUtil.loadNormalImgByNet(rightImgViewHolder.itemView.getContext(),photoUri,rightImgViewHolder.itemRightImgIv);
            if (Build.VERSION.SDK_INT>=29){//android 10
                Uri photoUri = Uri.parse(messagePhotoBean.getLocalUriString());
                GlideUtils.loadRound(rightImgViewHolder.itemView.getContext(),photoUri,rightImgViewHolder.itemRightImgIv,10);
            }else {
                GlideUtils.loadRound(rightImgViewHolder.itemView.getContext(),messagePhotoBean.getLocalUriString(),rightImgViewHolder.itemRightImgIv,10);
            }
        }else {
//            AliOssUtil.getInstance().downMusicVideoPicFromService(messagePhotoBean.getDescription(), rightImgViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
//                @Override
//                public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
//                    if (status == 1){
//                        Uri uri = (Uri) object;
//                        Log.e("YM","下载的图片路径:"+uri.toString());
//                        messagePhotoBean.setLocalUriString(uri.toString());
////                        GlideUtil.loadNormalImgByNet(rightImgViewHolder.itemView.getContext(),uri,rightImgViewHolder.itemRightImgIv);
//                        GlideUtils.loadRound(rightImgViewHolder.itemView.getContext(),uri,rightImgViewHolder.itemRightImgIv,10);
//                        String source = gson.toJson(messagePhotoBean);
//                        p2PMessageBaseModel.setSourceContent(source);
//                        p2PMessageViewModel.update(p2PMessageBaseModel);
//                    }
//                }
//            });
            HttpDownFileUtils.getInstance().downFileFromServiceToPublicDir(messagePhotoBean.getDescription(), rightImgViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
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
                        Log.e("YM","下载的图片路径:"+localPath);
                        messagePhotoBean.setLocalUriString(localPath);
                        if (null == activity || activity.isDestroyed()){
                            return;
                        }
                        if (object instanceof File){
                            GlideUtils.loadRound(activity,localPath,rightImgViewHolder.itemRightImgIv,10);
                        }else if (object instanceof Uri){
                            Uri uri = (Uri) object;
                            GlideUtils.loadRound(activity,uri,rightImgViewHolder.itemRightImgIv,10);
                        }
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
        teamMessageViewModel = new ViewModelProvider(activity).get(TeamMessageViewModel.class);
        String content = teamMessageBaseModel.getSourceContent();
        MessagePhotoBean messagePhotoBean = gson.fromJson(content,MessagePhotoBean.class);
        boolean fileExists = FileUtils.fileExists(messagePhotoBean.getLocalUriString());
        if (fileExists){
            if (Build.VERSION.SDK_INT>=29){//android 10
                Uri photoUri = Uri.parse(messagePhotoBean.getLocalUriString());
                GlideUtils.loadRound(rightImgViewHolder.itemView.getContext(),photoUri,rightImgViewHolder.itemRightImgIv,10);
            }else {
                GlideUtils.loadRound(rightImgViewHolder.itemView.getContext(),messagePhotoBean.getLocalUriString(),rightImgViewHolder.itemRightImgIv,10);
            }
        }else {
//            AliOssUtil.getInstance().downMusicVideoPicFromService(messagePhotoBean.getDescription(), rightImgViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
//                @Override
//                public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
//                    if (status == 1){
//                        Uri uri = (Uri) object;
//                        messagePhotoBean.setLocalUriString(uri.toString());
////                        GlideUtil.loadNormalImgByNet(rightImgViewHolder.itemView.getContext(),uri,rightImgViewHolder.itemRightImgIv);
//                        GlideUtils.loadRound(rightImgViewHolder.itemView.getContext(),photoUri,rightImgViewHolder.itemRightImgIv,10);
//                        String source = gson.toJson(messagePhotoBean);
//                        teamMessageBaseModel.setSourceContent(source);
//                        teamMessageViewModel.update(teamMessageBaseModel);
//                    }
//                }
//            });
            HttpDownFileUtils.getInstance().downFileFromServiceToPublicDir(messagePhotoBean.getDescription(), rightImgViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
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
                        Log.e("YM","下载的图片路径:"+localPath);
                        messagePhotoBean.setLocalUriString(localPath);
                        if (object instanceof File){
                            GlideUtils.loadRound(rightImgViewHolder.itemView.getContext(),localPath,rightImgViewHolder.itemRightImgIv,10);
                        }else if (object instanceof Uri){
                            Uri uri = (Uri) object;
                            GlideUtils.loadRound(rightImgViewHolder.itemView.getContext(),uri,rightImgViewHolder.itemRightImgIv,10);
                        }
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