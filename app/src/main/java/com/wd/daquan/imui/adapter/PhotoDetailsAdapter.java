package com.wd.daquan.imui.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.model.ImMessageBaseModel;
import com.wd.daquan.R;
import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPagerAdapter
 */
public
class PhotoDetailsAdapter extends PagerAdapter{
    private List<ImMessageBaseModel> imMessageBaseModels = new ArrayList<>();
    private LayoutInflater inflater;
    public PhotoDetailsAdapter(List<ImMessageBaseModel> imMessageBaseModels) {
        this.imMessageBaseModels = imMessageBaseModels;
    }

    @Override
    public int getCount() {
        return imMessageBaseModels.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (null == inflater){
            inflater = LayoutInflater.from(container.getContext());
        }
        ImMessageBaseModel model = imMessageBaseModels.get(position);
        View content = inflater.inflate(R.layout.item_photo_details,container,false);
        ImageView photo_details = content.findViewById(R.id.photo_details);
        MessagePhotoBean messagePhotoBean = GsonUtils.fromJson(model.getSourceContent(),MessagePhotoBean.class);
        Uri localUri = Uri.parse(messagePhotoBean.getLocalUriString());
        GlideUtil.loadNormalImgByNet(container.getContext(),localUri,photo_details);
        container.addView(content);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) v.getContext();
                activity.finish();
            }
        });
        return content;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}