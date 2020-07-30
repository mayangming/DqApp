package com.wd.daquan.mine.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.glide.GlideUtils;

import org.jetbrains.annotations.NotNull;

/**
 * Created by DELL on 2018/9/13.
 */

public class PhotoAlbumDetailsAdapter extends CommRecyclerViewAdapter<String, RecyclerView.ViewHolder> {
    private OnPhotoAlbumDetailsListener mPhotoAlbumDetailsListener;

    @Override
    protected RecyclerView.ViewHolder onBindView(ViewGroup parent, int viewType) {
        return new PhotoAlbumDetailsHolder(mInflater.inflate(R.layout.photo_album_details_item, parent, false));
    }

    @Override
    protected void onBindData(@NotNull RecyclerView.ViewHolder holder, int position) {
        String imgPath = allList.get(position);
        PhotoAlbumDetailsHolder photoAlbumDetailsHolder = (PhotoAlbumDetailsHolder) holder;
        GlideUtils.loadCenterCrop(mContext, imgPath, photoAlbumDetailsHolder.img);
        photoAlbumDetailsHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoAlbumDetailsListener.onLayout(imgPath, position);
            }
        });
    }

    public void setPhotoAlbumDetailsListener(OnPhotoAlbumDetailsListener mPhotoAlbumListListener){
        this.mPhotoAlbumDetailsListener = mPhotoAlbumListListener;
    }
    public interface OnPhotoAlbumDetailsListener{
        void onLayout(String imgPath, int position);
    }
}
