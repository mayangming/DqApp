package com.meetqs.qingchat.imagepicker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetqs.qingchat.imagepicker.R;
import com.meetqs.qingchat.imagepicker.config.PictureMimeType;
import com.meetqs.qingchat.imagepicker.entity.LocalMedia;
import com.meetqs.qingchat.imagepicker.entity.LocalMediaFolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.adapter
 * email：893855882@qq.com
 * data：16/12/31
 */
public class PictureAlbumDirectoryAdapter extends RecyclerView.Adapter<PictureAlbumDirectoryAdapter.ViewHolder> {
    private Context mContext;
    private List<LocalMediaFolder> folders = new ArrayList<>();
    private int mimeType;

    public PictureAlbumDirectoryAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public void bindFolderData(List<LocalMediaFolder> folders) {
        this.folders = folders;
        notifyDataSetChanged();
    }

    public void setMimeType(int mimeType) {
        this.mimeType = mimeType;
    }

    public List<LocalMediaFolder> getFolderData() {
        if (folders == null) {
            folders = new ArrayList<>();
        }
        return folders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.picture_album_folder_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final LocalMediaFolder folder = folders.get(position);
        String name = folder.getName();
        int imageNum = folder.getImageNum();
        String imagePath = folder.getFirstImagePath();
        boolean isChecked = folder.isChecked();
        int checkedNum = folder.getCheckedNum();
        holder.select_folder_iv.setVisibility(isChecked == true ? View.VISIBLE : View.GONE);
        if (mimeType == PictureMimeType.ofAudio()) {
            holder.first_image.setImageResource(R.drawable.audio_placeholder);
        } else {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_placeholder)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(160, 160);
            Glide.with(mContext)
                    .asBitmap()
                    .load(imagePath)
                    .apply(options)
                    .into(new BitmapImageViewTarget(holder.first_image) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.
                                            create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCornerRadius(4);
                            holder.first_image.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }

        if(name.contains(mContext.getString(R.string.all_video))) {
            holder.video_play.setVisibility(View.VISIBLE);
        }else {
            holder.video_play.setVisibility(View.GONE);
        }
        holder.image_num.setText(imageNum + "张");
        holder.tv_folder_name.setText(name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    for (LocalMediaFolder mediaFolder : folders) {
                        mediaFolder.setChecked(false);
                    }
                    folder.setChecked(true);
                    notifyDataSetChanged();
                    onItemClickListener.onItemClick(folder.getName(), folder.getImages());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView first_image;
        ImageView video_play;
        ImageView select_folder_iv;
        TextView tv_folder_name;
        TextView image_num;

        public ViewHolder(View itemView) {
            super(itemView);
            first_image = (ImageView) itemView.findViewById(R.id.first_image);
            video_play = (ImageView) itemView.findViewById(R.id.video_play);
            select_folder_iv = (ImageView) itemView.findViewById(R.id.select_folder_iv);
            tv_folder_name = (TextView) itemView.findViewById(R.id.tv_folder_name);
            image_num = (TextView) itemView.findViewById(R.id.image_num);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String folderName, List<LocalMedia> images);
    }
}
