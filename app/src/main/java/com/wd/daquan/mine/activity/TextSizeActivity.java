package com.wd.daquan.mine.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.da.library.widget.CommTitle;

import io.haydar.csb.CustomSeekBar;


public class TextSizeActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener {

    private CustomSeekBar seekbarone;
    private CustomSeekBar seekbartwo;
    private CustomSeekBar seekbarthree;
    private CustomSeekBar seekbarfour;
    private CustomSeekBar seekbarzero;
    private ImageView img_avatar;
    private TextView txt_text_see;
    private TextView txt_textSet;
    private CommTitle mCommTitle;
    private QCSharedPrefManager manager;
    private CustomSeekBar[] cusbar;
    private int strs;

    @Override
    protected MinePresenter createPresenter() {
        return null;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.text_size_activity);
    }

    @Override
    protected void init() {
        manager = QCSharedPrefManager.getInstance();
    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.textSizeActivityTitle);
        seekbarone = findViewById(R.id.cusSeekBarone);
        seekbartwo = findViewById(R.id.cusSeekBartwo);
        seekbarthree = findViewById(R.id.cusSeekBarthree);
        seekbarfour = findViewById(R.id.cusSeekBarfour);
        seekbarzero = findViewById(R.id.cusSeekBarzero);
        img_avatar = findViewById(R.id.textSizeActivityImgAvatar);
        txt_text_see = findViewById(R.id.textSizeActivityTxtText);
        txt_textSet = findViewById(R.id.textSizeActivityTxtTextSet);
        cusbar = new CustomSeekBar[]{seekbarzero, seekbarone, seekbartwo, seekbarthree, seekbarfour};
        mCommTitle.setTitle(getString(R.string.text_size_size));
        mCommTitle.getRightTv().setVisibility(View.VISIBLE);
        mCommTitle.setRightTxt(getString(R.string.save));
    }

    @Override
    public void initListener() {
        valuechanged(seekbarone);
        valuechanged(seekbartwo);
        valuechanged(seekbarthree);
        valuechanged(seekbarfour);
        valuechanged(seekbarzero);
        mCommTitle.getLeftIv().setOnClickListener(this);
        mCommTitle.getRightTv().setOnClickListener(this);
    }

    private void valuechanged(CustomSeekBar seekbarones) {
        seekbarones.setOnValueChanged(value -> {
            switch (value) {
                case 0:
                    txt_text_see.setTextSize(12);
                    txt_textSet.setTextSize(12);
                    strs = 12;
                    break;
                case 1:
                    txt_text_see.setTextSize(14);
                    txt_textSet.setTextSize(14);
                    strs = 14;
                    break;
                case 2:
                    txt_text_see.setTextSize(16);
                    txt_textSet.setTextSize(16);
                    strs = 16;
                    break;
                case 3:
                    txt_text_see.setTextSize(18);
                    txt_textSet.setTextSize(18);
                    strs = 18;
                    break;
                case 4:
                    txt_text_see.setTextSize(20);
                    txt_textSet.setTextSize(20);
                    strs = 20;
                    break;
            }
        });
    }

    void visible(int select) {
        for (int i = 0; i < cusbar.length; i++) {
            if (select == i) {
                cusbar[i].setVisibility(View.VISIBLE);
            } else {
                cusbar[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void initData() {
        String headPic = manager.getKDPreferenceUserInfo().getString(EBSharedPrefUser.headpic, "");
        int textsize = manager.getKDPreferenceUserInfo().getInt(EBSharedPrefUser.TEXTSIZE, 0);
//        GlideUtils.load(this, headPic, img_avatar);
        GlideUtils.loadHeader(this, headPic, img_avatar);
        if (textsize != 0) {
            txt_text_see.setTextSize(textsize);
            txt_textSet.setTextSize(textsize);
            switch (textsize) {
                case 12:
                    visible(0);
                    break;
                case 14:
                    visible(1);
                    break;
                case 16:
                    visible(2);
                    break;
                case 18:
                    visible(3);
                    break;
                case 20:
                    visible(4);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.comm_right_tv:
                if (strs != 0) {
                    manager.getKDPreferenceUserInfo().saveInt(EBSharedPrefUser.TEXTSIZE, strs);
                    DqApp.getInstance().setChatTextSize(strs);
                } else {
                    DqToast.showShort(getString(R.string.text_size_select_size));
                }
                finish();
        }
    }

}
