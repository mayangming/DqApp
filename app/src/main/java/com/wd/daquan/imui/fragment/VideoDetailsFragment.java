package com.wd.daquan.imui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.bean.im.MessageVideoBean;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.util.download.HttpDownFileUtils;
import com.dq.im.util.download.OnFileDownListener;
import com.wd.daquan.R;
import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.util.FileUtils;
import com.wd.daquan.util.GlideUtil;

import java.io.File;

import static android.os.Environment.DIRECTORY_MOVIES;

/**
 * 视频详情的Fragment
 */
public class VideoDetailsFragment extends BaseFragment{
    public static String DATA = "model";
    private ImMessageBaseModel imMessageBaseModel;//
    private VideoView videoDetails;
    private MessageVideoBean messageVideoBean;
    private MediaController localMediaController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_video_details,container,false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
    }

    private void initData(){
        Bundle bundle = getArguments();
        imMessageBaseModel = (ImMessageBaseModel)bundle.getSerializable(DATA);
        messageVideoBean = GsonUtils.fromJson(imMessageBaseModel.getSourceContent(),MessageVideoBean.class);
        boolean fileExists = FileUtils.fileExists(messageVideoBean.getVideoLocalPath());
        if (fileExists){
            initVideoView(messageVideoBean.getVideoLocalPath());
        }else {
            downloadVideo();
        }
    }

    /**
     * 初始化控件
     */
    private void initView(View view){
        videoDetails = view.findViewById(R.id.video_details);
    }



    private void initVideoView(String localPath){
        if (null == getContext()){
            return;
        }
        if (null == videoDetails){//有时候页面回收会触发程序控件回收
            return;
        }
        //设置有进度条可以拖动快进
        localMediaController = new MediaController(getContext());

        videoDetails.setMediaController(localMediaController);
//        Uri.
        videoDetails.setVideoPath(localPath);
//        videoDetails.setVideoURI(videoPath);
//        videoDetails.start();
    }

    /**
     * 下载视频
     */
    private void downloadVideo(){
        HttpDownFileUtils.getInstance().downFileFromServiceToPublicDir(messageVideoBean.getVideoPath(), getContext(), DIRECTORY_MOVIES, new OnFileDownListener() {
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
                    String source = GsonUtils.toJson(messageVideoBean);
                    imMessageBaseModel.setSourceContent(source);
                    initVideoView(localPath);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (null != localMediaController){
                localMediaController.show();
            }
        }else {
            if (null != localMediaController){
                localMediaController.hide();
                videoDetails.stopPlayback();
            }
        }
    }
}