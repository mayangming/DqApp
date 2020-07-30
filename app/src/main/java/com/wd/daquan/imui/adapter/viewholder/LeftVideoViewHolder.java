package com.wd.daquan.imui.adapter.viewholder;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.wd.daquan.R;


/**
 * 左侧视频布局
 */
public class LeftVideoViewHolder extends BaseLeftViewHolder{
    public View leftVideoContainer;
    public AppCompatImageView leftVideo;
    public AppCompatImageView leftVideoBg;
    public ProgressBar videoLoading;
    public LeftVideoViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void initView() {
        super.initView();
        leftVideo = itemView.findViewById(R.id.item_left_video_iv);
        leftVideoBg = itemView.findViewById(R.id.item_left_video_bg);
        videoLoading = itemView.findViewById(R.id.video_loading);
        leftVideoContainer = itemView.findViewById(R.id.item_left_video_container);
    }

    @Override
    protected View childView(ViewGroup parent) {
        View leftVideoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_video,parent,false);
        leftVideoView.setTag("leftChatVideo");
        View content = addRootView(leftVideoView);
        return content;
    }

    @Override
    protected Class<? extends BaseLeftViewHolder> getClassType() {
        return getClass();
    }
}