package com.wd.daquan.imui.adapter.viewholder;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wd.daquan.R;


/**
 * 右侧声音布局
 */
public class RightVoiceViewHolder extends BaseRightViewHolder{
    public ImageView rightVoice;
    public ProgressBar progressBar;//语音下载进度条
    public TextView duration;
    public RightVoiceViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void initView() {
        super.initView();
        rightVoice = itemView.findViewById(R.id.item_right_voice_tv);
        progressBar = itemView.findViewById(R.id.voice_loading);
        duration = itemView.findViewById(R.id.duration);
    }
    @Override
    protected View childView(ViewGroup parent) {
        View rightVoiceView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right_voice,parent,false);
        rightVoiceView.setTag("chatRightVideo");
        View content = addRootView(rightVoiceView);
        return content;
    }

    @Override
    protected Class<? extends BaseRightViewHolder> getClassType() {
        return getClass();
    }
}