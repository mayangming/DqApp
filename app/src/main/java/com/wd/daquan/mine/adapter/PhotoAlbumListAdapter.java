package com.wd.daquan.mine.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.mine.bean.PhotoAlbumEntity;

import org.jetbrains.annotations.NotNull;

/**
 * Created by DELL on 2018/9/13.
 */

public class PhotoAlbumListAdapter extends CommRecyclerViewAdapter<PhotoAlbumEntity, RecyclerView.ViewHolder> {
    private OnPhotoAlbumListListener mPhotoAlbumListListener;

    @Override
    protected RecyclerView.ViewHolder onBindView(ViewGroup parent, int viewType) {
        return new PhotoAlbumListHolder(mInflater.inflate(R.layout.photo_album_list_item, parent, false));
    }

    @Override
    protected void onBindData(@NotNull RecyclerView.ViewHolder holder, int position) {
        PhotoAlbumEntity data = allList.get(position);
        PhotoAlbumListHolder photoAlbumListHolder = (PhotoAlbumListHolder) holder;
        photoAlbumListHolder.name.setText(data.filename);
        photoAlbumListHolder.num.setText(data.filecontent.size() + "");
        GlideUtils.loadCenterCrop(mContext, data.filecontent.get(0), photoAlbumListHolder.img);
        photoAlbumListHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoAlbumListListener.onLayout(data, position);
            }
        });
    }

    public void setPhotoAlbumListListener(OnPhotoAlbumListListener mPhotoAlbumListListener){
        this.mPhotoAlbumListListener = mPhotoAlbumListListener;
    }
    public interface OnPhotoAlbumListListener{
        void onLayout(PhotoAlbumEntity data, int position);
    }
}
