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
 * 左侧声音布局
 */
public class LeftVoiceViewHolder extends BaseLeftViewHolder{
    public ImageView leftVoice;
    public ProgressBar progressBar;//语音下载进度条
    public TextView duration;
    public View voiceUnread;//未读消息数
    public LeftVoiceViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void initView() {
        super.initView();
        leftVoice = itemView.findViewById(R.id.item_left_voice_tv);
        progressBar = itemView.findViewById(R.id.voice_loading);
        duration = itemView.findViewById(R.id.duration);
        voiceUnread = itemView.findViewById(R.id.voice_unread);
    }

    @Override
    protected View childView(ViewGroup parent) {
        View leftVoiceView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_voice,parent,false);
        leftVoiceView.setTag("leftChatVoice");
        View content = addRootView(leftVoiceView);
        return content;
    }

    @Override
    protected Class<? extends BaseLeftViewHolder> getClassType() {
        return getClass();
    }

}