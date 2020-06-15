package com.wd.daquan.discover;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wd.daquan.R;

//目前用于图片和视频
public class DiscoverViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout layout;
    public ImageView img;
    public TextView txt;

    public DiscoverViewHolder(View view) {
        super(view);
        layout = view.findViewById(R.id.discoverFragmentLayout);
        img = view.findViewById(R.id.discoverFragmentImg);
        txt = view.findViewById(R.id.discoverFragmentTxt);
    }
}
