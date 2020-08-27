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
import com.wd.daquan.imui.adapter.viewholder.LeftImgViewHolder;
import com.wd.daquan.util.FileUtils;

import java.io.File;

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
        p2PMessageViewModel  = new ViewModelProvider(activity).get(P2PMessageViewModel.class);
        String content = p2PMessageBaseModel.getSourceContent();
        MessagePhotoBean messagePhotoBean = gson.fromJson(content,MessagePhotoBean.class);
        boolean fileExists = FileUtils.fileExists(messagePhotoBean.getLocalUriString());
        Log.e("YM","文件是否存在:"+fileExists+"--->文件路径:"+messagePhotoBean.getLocalUriString());
        if (fileExists){
            if (Build.VERSION.SDK_INT>=29){//android 10
                Uri photoUri = Uri.parse(messagePhotoBean.getLocalUriString());
                GlideUtils.loadRound(leftImgViewHolder.itemView.getContext(),photoUri,leftImgViewHolder.leftImgContent,10);
            }else {
                GlideUtils.loadRound(leftImgViewHolder.itemView.getContext(),messagePhotoBean.getLocalUriString(),leftImgViewHolder.leftImgContent,10);
            }
        }else {
//            AliOssUtil.getInstance().downMusicVideoPicFromService(messagePhotoBean.getDescription(), leftImgViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
//                @Override
//                public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
//                    if (status == 1){
//                        Uri uri = (Uri) object;
//                        messagePhotoBean.setLocalUriString(uri.toString());
//                        GlideUtils.loadRound(leftImgViewHolder.itemView.getContext(),uri,leftImgViewHolder.leftImgContent,10);
//                        String source = gson.toJson(messagePhotoBean);
//                        p2PMessageBaseModel.setSourceContent(source);
//                        p2PMessageViewModel.update(p2PMessageBaseModel);
//                    }
//                }
//            });

            HttpDownFileUtils.getInstance().downFileFromServiceToPublicDir(messagePhotoBean.getDescription(), leftImgViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
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
                            GlideUtils.loadRound(activity,localPath,leftImgViewHolder.leftImgContent,10);
                        }else if (object instanceof Uri){
                            Uri uri = (Uri) object;
                            GlideUtils.loadRound(activity,uri,leftImgViewHolder.leftImgContent,10);
                        }
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
        teamMessageViewModel = new ViewModelProvider(activity).get(TeamMessageViewModel.class);
        String content = teamMessageBaseModel.getSourceContent();
        Log.e("YM","获取的图片json数据:"+content);
        MessagePhotoBean messagePhotoBean = gson.fromJson(content,MessagePhotoBean.class);
        boolean fileExists = FileUtils.fileExists(messagePhotoBean.getLocalUriString());
        if (fileExists){
//            GlideUtil.loadNormalImgByNet(leftImgViewHolder.itemView.getContext(),photoUri,leftImgViewHolder.leftImgContent);
            if (Build.VERSION.SDK_INT>=29){//android 10
                Uri photoUri = Uri.parse(messagePhotoBean.getLocalUriString());
                GlideUtils.loadRound(leftImgViewHolder.itemView.getContext(),photoUri,leftImgViewHolder.leftImgContent,10);
            }else {
                GlideUtils.loadRound(leftImgViewHolder.itemView.getContext(),messagePhotoBean.getLocalUriString(),leftImgViewHolder.leftImgContent,10);
            }
        }else {
//            AliOssUtil.getInstance().downMusicVideoPicFromService(messagePhotoBean.getDescription(), leftImgViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
//                @Override
//                public void onFileDownStatus(int status, Object object, int proGress, long currentDownProGress, long totalProGress) {
//                    if (status == 1){
//                        Uri uri = (Uri) object;
//                        messagePhotoBean.setLocalUriString(uri.toString());
//                        GlideUtil.loadNormalImgByNet(leftImgViewHolder.itemView.getContext(),uri,leftImgViewHolder.leftImgContent);
//                        String source = gson.toJson(messagePhotoBean);
//                        teamMessageBaseModel.setSourceContent(source);
//                        teamMessageViewModel.update(teamMessageBaseModel);
//                    }
//                }
//            });
            HttpDownFileUtils.getInstance().downFileFromServiceToPublicDir(messagePhotoBean.getDescription(), leftImgViewHolder.itemView.getContext(), DIRECTORY_PICTURES, new OnFileDownListener() {
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
                            GlideUtils.loadRound(leftImgViewHolder.itemView.getContext(),localPath,leftImgViewHolder.leftImgContent,10);
                        }else if (object instanceof Uri){
                            Uri uri = (Uri) object;
                            GlideUtils.loadRound(leftImgViewHolder.itemView.getContext(),uri,leftImgViewHolder.leftImgContent,10);
                        }
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