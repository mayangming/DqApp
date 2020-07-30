package com.wd.daquan.imui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.MessageType;
import com.wd.daquan.R;
import com.wd.daquan.imui.adapter.PhotoDetailsAdapter;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * 消息中的多媒体详情
 */
public class MediaDetailsActivity extends BaseActivity{
    public static final String PHOTO_URL = "photoUrl";//图片网络地址
    public static final String MEDIA_DATA = "mediaData";//图片数据
    public static final String MEDIA_DATA_CURRENT = "mediaDataCurrent";//当前图片数据
    private ViewPager photoDetailsVp;
    private PhotoDetailsAdapter photoDetailsAdapter;
    private ArrayList<ImMessageBaseModel> imMessageBaseModels = new ArrayList<>();
    private ImMessageBaseModel currentModel;
    private int currentIndex;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_details);
        initView();
        initData();
        initAdapter();
    }

    private void initView(){
        photoDetailsVp = findViewById(R.id.media_details_vp);
    }

    private void initAdapter(){
        photoDetailsAdapter = new PhotoDetailsAdapter(getSupportFragmentManager(),imMessageBaseModels);
        photoDetailsVp.setAdapter(photoDetailsAdapter);
        photoDetailsVp.setCurrentItem(currentIndex);
    }

    private void initData(){
        currentIndex = getIntent().getIntExtra(MEDIA_DATA_CURRENT,-1);
        imMessageBaseModels = (ArrayList<ImMessageBaseModel>)getIntent().getSerializableExtra(MEDIA_DATA);
        currentModel  = imMessageBaseModels.get(currentIndex);
        Iterator<ImMessageBaseModel> iterator = imMessageBaseModels.listIterator();
        while (iterator.hasNext()){
            ImMessageBaseModel model = iterator.next();
            if (!(MessageType.PICTURE.getValue().equals(model.getMsgType()) || MessageType.VIDEO.getValue().equals(model.getMsgType())) ){
                iterator.remove();
            }
        }
        currentIndex = imMessageBaseModels.indexOf(currentModel);
    }

}