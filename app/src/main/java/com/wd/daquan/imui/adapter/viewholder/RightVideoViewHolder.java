package com.wd.daquan.imui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.wd.daquan.R;


/**
 * 右侧视频布局
 */
public class RightVideoViewHolder extends BaseRightViewHolder{
    public View itemRightVideoContainer;
    public AppCompatImageView rightVideo;
    public AppCompatImageView rightVideoBg;
    public ProgressBar videoLoading;
    public RightVideoViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void initView() {
        super.initView();
        rightVideo = itemView.findViewById(R.id.item_right_video_iv);
        rightVideoBg = itemView.findViewById(R.id.item_right_video_bg);
        videoLoading = itemView.findViewById(R.id.video_loading);
        itemRightVideoContainer = itemView.findViewById(R.id.item_right_video_container);
    }
    @Override
    protected View childView(ViewGroup parent) {
        View rightVideoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right_video,parent,false);
        rightVideoView.setTag("chatRightVideo");
        View content = addRootView(rightVideoView);
        return content;
    }

    @Override
    protected Class<? extends BaseRightViewHolder> getClassType() {
        return getClass();
    }
}