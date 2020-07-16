package com.wd.daquan.imui.fragment;

import android.app.Activity;
import android.media.MediaPlayer;
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
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.log.DqLog;
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
    private ImMessageBaseModel imMessageBaseModel;
    private VideoView videoDetails;
    private ImageView videoPreview;//预览图
    private View videoPreviewContainer;//视频预览容器
    private View videoDetailsPlay;//播放按钮
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
        initView(view);
        initData();
    }

    private void initData(){
        Bundle bundle = getArguments();
        imMessageBaseModel = (ImMessageBaseModel)bundle.getSerializable(DATA);
        messageVideoBean = GsonUtils.fromJson(imMessageBaseModel.getSourceContent(),MessageVideoBean.class);
        boolean fileExists = FileUtils.fileExists(messageVideoBean.getVideoLocalPath());
        DqLog.e("YM","VideoDetailsFragment文件是否存在:"+fileExists+"---->文件地址:"+messageVideoBean.getVideoLocalPath());
        GlideUtils.loadRound(getContext(),messageVideoBean.getThumbPath(),videoPreview,10);
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
        videoPreviewContainer = view.findViewById(R.id.video_details_bg_container);
        videoDetailsPlay = view.findViewById(R.id.video_details_play);
        videoDetails = view.findViewById(R.id.video_details);
        videoPreview = view.findViewById(R.id.video_details_bg);
        videoDetailsPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoDetails.start();
                videoPreviewContainer.setVisibility(View.GONE);
                videoDetails.setVisibility(View.VISIBLE);
            }
        });
        videoPreviewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



    private void initVideoView(String localPath){
        if (null == getContext()){
            Log.e("YM","上下文为null:");
            return;
        }
        if (null == videoDetails){//有时候页面回收会触发程序控件回收
            Log.e("YM","videoDetails为null:");
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
        DqLog.e("YM","是否显示视频控制栏-------->"+isVisibleToUser);
        if (isVisibleToUser){
            if (null != localMediaController){
                localMediaController.show();
            }
        }else {
            if (null != localMediaController){
                localMediaController.hide();
                videoDetails.pause();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != localMediaController){
            localMediaController.hide();
//                videoDetails.stopPlayback();
            videoDetails.stopPlayback();
        }
    }
}