package com.wd.daquan.mine.collection;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;

//目前用于语音 文件
public class CollectionVoiceViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout layout_voice;
    public ImageView img_voice;
    public TextView txtVoiceName;
    public TextView txtVoiceVoiceTime;//音频时长
    public TextView txtVoiceTime;

    public CollectionVoiceViewHolder(View view) {
        super(view);
        layout_voice = view.findViewById(R.id.collectionItemVoiceLayout);
        img_voice = view.findViewById(R.id.collectionItemVoiceIcon);
        txtVoiceName = view.findViewById(R.id.collectionItemVoiceName);
        txtVoiceVoiceTime = view.findViewById(R.id.collectionItemVoiceVoiceTime);
        txtVoiceTime = view.findViewById(R.id.collectionItemVoiceTime);
    }
}
