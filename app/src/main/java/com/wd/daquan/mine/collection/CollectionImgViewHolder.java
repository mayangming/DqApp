package com.wd.daquan.mine.collection;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;

//目前用于图片和视频
public class CollectionImgViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout layout_img;
    public ImageView collectionImg;
    public ImageView collectionImgIcon;
    public TextView txtImgImgName;
    public TextView txtImgTime;
    public TextView txtImgName;

    public CollectionImgViewHolder(View view) {
        super(view);
        layout_img = view.findViewById(R.id.collectionItemImgLayout);
        collectionImg = view.findViewById(R.id.collectionItemImg);
        collectionImgIcon = view.findViewById(R.id.collectionItemImgIcon);
        txtImgTime = view.findViewById(R.id.collectionItemImgTime);
        txtImgName = view.findViewById(R.id.collectionItemImgName);
        txtImgImgName = view.findViewById(R.id.collectionItemImgImgName);

    }
}
