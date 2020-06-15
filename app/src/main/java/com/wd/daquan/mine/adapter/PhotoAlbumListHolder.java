package com.wd.daquan.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;

public class PhotoAlbumListHolder extends RecyclerView.ViewHolder{

    public ImageView img;
    public TextView name;
    public TextView num;

    public PhotoAlbumListHolder(View view) {
        super(view);
        img = view.findViewById(R.id.photoAlbumListImg);
        name = view.findViewById(R.id.photoAlbumListTxtName);
        num = view.findViewById(R.id.photoAlbumListTxtNum);
    }

}
