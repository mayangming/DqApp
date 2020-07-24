package com.wd.daquan.imui.adapter;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.bean.im.MessageVideoBean;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.MessageType;
import com.wd.daquan.R;
import com.wd.daquan.imui.fragment.PhotoDetailsFragment;
import com.wd.daquan.imui.fragment.VideoDetailsFragment;
import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPagerAdapter
 */
//public
//class PhotoDetailsAdapter extends PagerAdapter{
//    private List<ImMessageBaseModel> imMessageBaseModels = new ArrayList<>();
//    private LayoutInflater inflater;
//    public PhotoDetailsAdapter(List<ImMessageBaseModel> imMessageBaseModels) {
//        this.imMessageBaseModels = imMessageBaseModels;
//    }
//
//    @Override
//    public int getCount() {
//        return imMessageBaseModels.size();
//    }
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        if (null == inflater){
//            inflater = LayoutInflater.from(container.getContext());
//        }
//        ImMessageBaseModel model = imMessageBaseModels.get(position);
//        View content;
//        if (MessageType.PICTURE.getValue().equals(model.getMsgType())){
//            content = loadPicture(container,model);
//        }else {
//            content = loadVideo(container,model);
//        }
//        return content;
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
//        return view == o;
//    }
//
//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        container.removeView((View)object);
//    }
//
//    private View loadPicture(@NonNull ViewGroup container,ImMessageBaseModel model){
//        View content = inflater.inflate(R.layout.item_photo_details,container,false);
//        ImageView photo_details = content.findViewById(R.id.photo_details);
//        MessagePhotoBean messagePhotoBean = GsonUtils.fromJson(model.getSourceContent(),MessagePhotoBean.class);
//        if (Build.VERSION.SDK_INT>=29) {//android 10
//            Uri localUri = Uri.parse(messagePhotoBean.getLocalUriString());
//            GlideUtil.loadNormalImgByNet(container.getContext(),localUri,photo_details);
//        }else {
//            GlideUtil.loadNormalImgByNet(container.getContext(),messagePhotoBean.getLocalUriString(),photo_details);
//        }
//        container.addView(content);
//        content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Activity activity = (Activity) v.getContext();
//                activity.finish();
//            }
//        });
//        return content;
//    }
//}

public class PhotoDetailsAdapter extends FragmentPagerAdapter{
    private List<ImMessageBaseModel> imMessageBaseModels = new ArrayList<>();
    public PhotoDetailsAdapter(FragmentManager fm) {
        super(fm);
    }

    public PhotoDetailsAdapter(FragmentManager fm, List<ImMessageBaseModel> imMessageBaseModels) {
        super(fm);
        this.imMessageBaseModels = imMessageBaseModels;
    }

    public List<ImMessageBaseModel> getImMessageBaseModels() {
        return imMessageBaseModels;
    }

    public void setImMessageBaseModels(List<ImMessageBaseModel> imMessageBaseModels) {
        this.imMessageBaseModels = imMessageBaseModels;
    }

    @Override
    public Fragment getItem(int i) {
        return getFragment(imMessageBaseModels.get(i));
    }

    private Fragment getFragment(ImMessageBaseModel imMessageBaseModel){
        Fragment fragment;
        Bundle bundle = new Bundle();
        if (MessageType.PICTURE.getValue().equals(imMessageBaseModel.getMsgType())){
            fragment = new PhotoDetailsFragment();
            bundle.putSerializable(PhotoDetailsFragment.DATA,imMessageBaseModel);
        }else {
            fragment = new VideoDetailsFragment();
            bundle.putSerializable(VideoDetailsFragment.DATA,imMessageBaseModel);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return imMessageBaseModels.size();
    }

}