package com.wd.daquan.imui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.MessageType;
import com.wd.daquan.R;
import com.wd.daquan.imui.adapter.PhotoDetailsAdapter;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * 图片详情
 */
public class PhotoDetailsActivity extends BaseActivity{
    public static final String PHOTO_URL = "photoUrl";//图片网络地址
    public static final String PHOTO_DATA = "photoData";//图片数据
    public static final String PHOTO_DATA_CURRENT = "photoDataCurrent";//当前图片数据
    private ViewPager photoDetailsVp;
    private String photoUrl = "";
    private PhotoDetailsAdapter photoDetailsAdapter;
    private ArrayList<ImMessageBaseModel> imMessageBaseModels = new ArrayList<>();
    private ImMessageBaseModel currentModel;
    private int currentIndex;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);
        initView();
        initData();
        initAdapter();
    }

    private void initView(){
        photoDetailsVp = findViewById(R.id.photo_details_vp);
    }

    private void initAdapter(){
        photoDetailsAdapter = new PhotoDetailsAdapter(imMessageBaseModels);
        photoDetailsVp.setAdapter(photoDetailsAdapter);
        photoDetailsVp.setCurrentItem(currentIndex);
    }

    private void initData(){
        photoUrl = getIntent().getStringExtra(PHOTO_URL);
        currentIndex = getIntent().getIntExtra(PHOTO_DATA_CURRENT,-1);
        imMessageBaseModels = (ArrayList<ImMessageBaseModel>)getIntent().getSerializableExtra(PHOTO_DATA);
        currentModel  = imMessageBaseModels.get(currentIndex);
        Iterator<ImMessageBaseModel> iterator = imMessageBaseModels.listIterator();
        while (iterator.hasNext()){
            ImMessageBaseModel model = iterator.next();
            if (!MessageType.PICTURE.getValue().equals(model.getMsgType())){
                iterator.remove();
            }
        }
        currentIndex = imMessageBaseModels.indexOf(currentModel);
    }

}