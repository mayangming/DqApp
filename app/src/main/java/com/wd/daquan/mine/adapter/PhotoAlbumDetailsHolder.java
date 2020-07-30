package com.wd.daquan.mine.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.wd.daquan.R;

public class PhotoAlbumDetailsHolder extends RecyclerView.ViewHolder{

    public ImageView img;

    public PhotoAlbumDetailsHolder(View view) {
        super(view);
        img = view.findViewById(R.id.photoAlbumDetailsItemImg);
    }

}
