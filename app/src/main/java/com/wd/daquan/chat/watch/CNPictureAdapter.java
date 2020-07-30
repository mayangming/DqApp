package com.wd.daquan.chat.watch;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.chat.watch.fragment.ImageFragment;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dukangkang
 * @date: 2018/7/5 15:43.
 * @description: todo ...
 */
public class CNPictureAdapter extends FragmentStatePagerAdapter {
    private String mCurrentMessageId;
    private List<PictureEntity> mList = new ArrayList<>();
    private SparseArray<BaseFragment> mSparseArray = new SparseArray();
    private Activity mActivity;

    public CNPictureAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.mActivity = activity;
    }

    public void setCurrentMessageId(String currentMessageId) {
        mCurrentMessageId = currentMessageId;
    }

    /**
     * 更新数据
     * @param list
     */
    public void replace(List<PictureEntity> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     * @param newImages
     * @param isFirstTime
     * @param direction
     */
    public void addData(ArrayList<PictureEntity> newImages, boolean isFirstTime, boolean direction) {
        if (newImages == null || newImages.size() == 0)
            return;
        if (mList.size() == 0) {
            mList.addAll(newImages);
            notifyDataSetChanged();
        } else if (direction && !isFirstTime && !isDuplicate(newImages.get(0).messageId)) {
            ArrayList<PictureEntity> temp = new ArrayList<>();
            temp.addAll(mList);
            mList.clear();
            mList.addAll(newImages);
            mList.addAll(mList.size(), temp);
            notifyDataSetChanged();
        } else if (!isFirstTime && !isDuplicate(newImages.get(0).messageId)) {
            mList.addAll(mList.size(), newImages);
            notifyDataSetChanged();
        } else {
            Log.e("clll", "bu xu yao bian geng lie biao");
        }
    }

    private boolean isDuplicate(String messageId) {
        for (PictureEntity entity : mList) {
            if (entity.messageId.equals(messageId))
                return true;
        }
        return false;
    }

    ImageFragment mImageFragment = null;

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        PictureEntity entity = mList.get(position);
        if (PictureEntity.MessageType.MESSAGE_IMAGE == entity.type) {
            mImageFragment = new ImageFragment();
            fragment = mImageFragment;
        } else {
            fragment = new Fragment();
        }
        mSparseArray.put(position, (BaseFragment) fragment);

//        Log.w("chuiniu", "getItem position = " + position);

        Bundle bundle = new Bundle();
        bundle.putParcelable(KeyValue.Picture.KEY_ENTITY, entity);
        bundle.putInt(KeyValue.Picture.KEY_POSITION, position);
        bundle.putString(KeyValue.Picture.KEY_CURRENT_MESSAGEID, mCurrentMessageId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public PictureEntity getPictureEntity(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (object == null) {
            return false;
        }
        return ((Fragment) object).getView() == view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mSparseArray.remove(position);
        super.destroyItem(container, position, object);
    }

    public BaseFragment getCurFragment(int position) {
        return mSparseArray.get(position);
    }

    public List<PictureEntity> getList() {
        return mList;
    }
}
