package com.wd.daquan.imui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.model.ImMessageBaseModel;
import com.wd.daquan.R;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.util.GlideUtil;

/**
 * 图片详情的Fragment
 */
public class PhotoDetailsFragment extends BaseFragment{
    public static String DATA = "model";
    private ImMessageBaseModel imMessageBaseModel;//
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_photo_details,container,false);
    }
    private ImageView photo_details;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
    }

    private void initData(){
        Bundle bundle = getArguments();
        imMessageBaseModel = (ImMessageBaseModel)bundle.getSerializable(DATA);
    }

    /**
     * 初始化控件
     */
    private void initView(View view){
        photo_details = view.findViewById(R.id.photo_details);
        MessagePhotoBean messagePhotoBean = GsonUtils.fromJson(imMessageBaseModel.getSourceContent(),MessagePhotoBean.class);
        if (Build.VERSION.SDK_INT>=29) {//android 10
            Uri localUri = Uri.parse(messagePhotoBean.getLocalUriString());
            GlideUtil.loadNormalImgByNet(getContext(),localUri,photo_details);
        }else {
            GlideUtil.loadNormalImgByNet(getContext(),messagePhotoBean.getLocalUriString(),photo_details);
        }
        photo_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) v.getContext();
                activity.finish();
            }
        });
        photo_details.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (Build.VERSION.SDK_INT < 29) {//android 10
                    DqToast.showShort("图片已保存至"+messagePhotoBean.getLocalUriString());
                }
                return true;
            }
        });
    }
}