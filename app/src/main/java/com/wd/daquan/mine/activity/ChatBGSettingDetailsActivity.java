package com.wd.daquan.mine.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.da.library.widget.CommTitle;

public class ChatBGSettingDetailsActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener {
    private ImageView img_big;
    private TextView btn_cancel;
    private TextView  btn_set;
    private CommTitle mCommTitle;
    private String type;
    private QCSharedPrefManager manager;

    @Override
    protected MinePresenter createPresenter() {
        return null;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.chat_bg_details_activity);
    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.chatBGSettingDetailsCommtitle);
        img_big = findViewById(R.id.chatBGSettingDetailsImg);
        btn_cancel = findViewById(R.id.chatBGSettingDetailsCancel);
        btn_set = findViewById(R.id.chatBGSettingDetailsSet);
        mCommTitle.setTitle(getString(R.string.chat_bg_bg));
    }

    @Override
    public void initListener() {
        btn_cancel.setOnClickListener(this);
        btn_set.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void initData() {
        manager = QCSharedPrefManager.getInstance();
        String from_type = getIntent().getStringExtra(KeyValue.CHAT_BG_TYPE);
        switch (from_type){
            case KeyValue.ONE_STRING:
                type = KeyValue.ONE_STRING;
                img_big.setBackgroundColor(this.getResources().getColor(R.color.app_page_bg));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.chatBGSettingDetailsCancel:
                finish();
                break;
            case R.id.chatBGSettingDetailsSet:
                manager.getKDPreferenceUserInfo().saveString(EBSharedPrefUser.filePath, type);
                Intent intent = new Intent();
                intent.putExtra("bgDetails",type);
                setResult(KeyValue.PHOTO_REQUEST_LOCAL,intent);
                finish();
                break;
        }
    }
}
