package com.meetqs.qingchat.imagepicker.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.PopupWindow;

import com.meetqs.qingchat.imagepicker.R;
import com.meetqs.qingchat.imagepicker.adapter.PictureAlbumDirectoryAdapter;
import com.meetqs.qingchat.imagepicker.decoration.RecycleViewDivider;
import com.meetqs.qingchat.imagepicker.entity.LocalMedia;
import com.meetqs.qingchat.imagepicker.entity.LocalMediaFolder;
import com.meetqs.qingchat.imagepicker.tools.ScreenUtils;

import java.util.List;

/**
 * author：fangzhi
 * project：
 * package：com.luck.picture.lib.widget
 * email：
 * data：2018/6/27 去掉模拟微信
 */

public class FolderPopWindow extends PopupWindow implements View.OnClickListener {
    private View masker;
    private RecyclerView recyclerView;
    private PictureAlbumDirectoryAdapter adapter;
    private int mimeType;

    public FolderPopWindow(Context context, int mimeType) {
        this.mimeType = mimeType;
        View view = LayoutInflater.from(context).inflate(R.layout.picture_window_folder, null);

        masker = view.findViewById(R.id.masker);
        masker.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.folder_list);
        adapter = new PictureAlbumDirectoryAdapter(context);
        recyclerView.getLayoutParams().height = (int) (ScreenUtils.getScreenHeight(context) * 0.75);
        recyclerView.addItemDecoration(new RecycleViewDivider(
                context, LinearLayoutManager.HORIZONTAL, ScreenUtils.dip2px(context, 0), ContextCompat.getColor(context, R.color.transparent)));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);  //如果不设置，就是 AnchorView 的宽度
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0x55000000));
        setAnimationStyle(0);
    }

    public void bindFolder(List<LocalMediaFolder> folders) {
        adapter.setMimeType(mimeType);
        adapter.bindFolderData(folders);
    }

    public void setOnItemClickListener(PictureAlbumDirectoryAdapter.OnItemClickListener onItemClickListener) {
        adapter.setOnItemClickListener(onItemClickListener);
    }

    /**
     * 设置选中状态
     */
    public void notifyDataCheckedStatus(List<LocalMedia> medias) {
        try {
            // 获取选中图片
            List<LocalMediaFolder> folders = adapter.getFolderData();
            for (LocalMediaFolder folder : folders) {
                folder.setCheckedNum(0);
            }
            if (medias.size() > 0) {
                for (LocalMediaFolder folder : folders) {
                    int num = 0;// 记录当前相册下有多少张是选中的
                    List<LocalMedia> images = folder.getImages();
                    for (LocalMedia media : images) {
                        String path = media.getPath();
                        for (LocalMedia m : medias) {
                            if (path.equals(m.getPath())) {
                                num++;
                                folder.setCheckedNum(num);
                            }
                        }
                    }
                }
            }
            adapter.bindFolderData(folders);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.masker) {
            dismiss();
        }
    }

    public void enterAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(masker, "alpha", 0, 1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(recyclerView, "translationY", recyclerView.getHeight(), 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.playTogether(alpha, translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    @Override
    public void dismiss() {
        exitAnimator();
    }

    private void exitAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(masker, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(recyclerView, "translationY", 0, recyclerView.getHeight());
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.playTogether(alpha, translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                FolderPopWindow.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        set.start();
    }
}
