package com.wd.daquan.mine.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.da.library.tools.FileUtils;
import com.da.library.widget.CommTitle;


public class ChatBGSettingInActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener{
    private LinearLayout layout_selectFromPics;
    private ImageView img_chatBgOne;
    private CommTitle mCommTitle;
    private QCSharedPrefManager manager;
    private View chatBGsettingDefault;

    @Override
    protected MinePresenter createPresenter() {
        return null;
    }

    @Override
    protected void init() {
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.chat_bg_setting_activity);
    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.chatBGsettingActivityCommtitle);
        layout_selectFromPics = findViewById(R.id.chatBGsettingActivityLayoutSelect);
        img_chatBgOne = findViewById(R.id.chatBGsettingActivityImgOne);
        chatBGsettingDefault = findViewById(R.id.chatBGsettingDefault);
        mCommTitle.setTitle(getString(R.string.chat_bg_bg));
    }

    @Override
    public void initListener() {
        layout_selectFromPics.setOnClickListener(this);
        chatBGsettingDefault.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void initData() {
        manager = QCSharedPrefManager.getInstance();
        String filePath = manager.getKDPreferenceUserInfo().getString(EBSharedPrefUser.filePath, KeyValue.ONE_STRING);
        selected(filePath);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.chatBGsettingActivityLayoutSelect:
                if (DqUtils.isFastDoubleClick(500)) {
                    return;
                }
                Album();
                break;
            case R.id.chatBGsettingDefault://背景设置界面
                selected(KeyValue.ONE_STRING);
                break;
        }
    }

    private void Album() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, KeyValue.PHOTO_REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case KeyValue.PHOTO_REQUEST_GALLERY:
                    String filePath = FileUtils.getRealFilePath(this, data.getData());
                    selected(filePath);
                    break;
            }
        }
    }


    private void selected(String path) {
        if(KeyValue.ONE_STRING.equals(path) || TextUtils.isEmpty(path)) {
            manager.getKDPreferenceUserInfo().saveString(EBSharedPrefUser.filePath, KeyValue.ONE_STRING);
            ColorDrawable colorDrawable = new ColorDrawable(DqApp.getColorById(R.color.app_page_bg));
            GlideUtils.load(this, colorDrawable, img_chatBgOne);
        }else {
            GlideUtils.load(this, path, img_chatBgOne);
            manager.getKDPreferenceUserInfo().saveString(EBSharedPrefUser.filePath, path);
        }
    }
}
