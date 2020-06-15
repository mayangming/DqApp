package com.wd.daquan.mine.user;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;

/**
 * @author: dukangkang
 * @date: 2018/5/14 15:30.
 * @description:
 *      个人信息页面
 */
public class PersonalInfoActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener, QCObserver {

    private TextView mPhoneTv;
    private TextView mNickNameTv;
    private TextView mGenderTv;
    private TextView mCertifyTv;
    private TextView mQingchatTv;
    private ImageView mQingchatIv; // 斗圈号箭头
    private ImageView mPortraitIv;
    private View vipHeadIcon;

    private LinearLayout mPortraitLlyt;
    private LinearLayout mPhoneLlyt;
    private LinearLayout mNickNameLlyt;
//    private LinearLayout mQrcodeLlyt;
    private LinearLayout mGenderLlyt;
    private LinearLayout mQingchatLlyt;
    private LinearLayout LinearLayout;
    private View mQrCodeLlyt;

    @Override
    public MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.personal_info_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mPhoneTv = this.findViewById(R.id.personal_phone);
        mNickNameTv = this.findViewById(R.id.personal_nickname);
        mGenderTv = this.findViewById(R.id.personal_gender);
        mCertifyTv = this.findViewById(R.id.personal_certification);
        mPortraitIv = this.findViewById(R.id.personal_portrait);
        vipHeadIcon = this.findViewById(R.id.vip_head_outline);
        mQingchatTv = this.findViewById(R.id.personal_qingchat);
        mQingchatIv = this.findViewById(R.id.personal_qingchat_jiantou);

        mPortraitLlyt = this.findViewById(R.id.personal_portrait_llyt);
        mPhoneLlyt = this.findViewById(R.id.personal_phone_llyt);
        mNickNameLlyt = this.findViewById(R.id.personal_nickname_llyt);
//        mQrcodeLlyt = this.findViewById(R.id.personal_qrcode_llyt);
        mGenderLlyt = this.findViewById(R.id.personal_gender_llyt);
        mQingchatLlyt = this.findViewById(R.id.personal_qingchat_llyt);
        mQrCodeLlyt = findViewById(R.id.personal_qrcode_llyt);
    }

    @Override
    public void initListener() {
        toolbarBack();
        mPortraitLlyt.setOnClickListener(this);
        mPhoneLlyt.setOnClickListener(this);
        mNickNameLlyt.setOnClickListener(this);
        mGenderLlyt.setOnClickListener(this);
        mQingchatLlyt.setOnClickListener(this);
        mQrCodeLlyt.setOnClickListener(this);
        MsgMgr.getInstance().attach(this);
    }

    @Override
    public void initData() {
        updateUserInfo();
    }

    private void updateUserInfo() {
        String phoneNumber =  ModuleMgr.getCenterMgr().getPhoneNumber();
        String headPic =  ModuleMgr.getCenterMgr().getAvatar();
        String nickName =  ModuleMgr.getCenterMgr().getNickName();
        String gender = ModuleMgr.getCenterMgr().getSex();
        String qingChatNum = ModuleMgr.getCenterMgr().getDqNum();
        boolean isVip = ModuleMgr.getCenterMgr().isVip();
        mPhoneTv.setText(phoneNumber);
        switch (gender) {
            case "1":
                mGenderTv.setText(getString(R.string.personal_info_male));
                break;
            case "2":
                mGenderTv.setText(getString(R.string.personal_info_female));
                break;
            default:
                mGenderTv.setText("");
                break;
        }
        mNickNameTv.setText(nickName);
        if (!TextUtils.isEmpty(qingChatNum) && !"null".equals(qingChatNum)) {
            mQingchatTv.setText(qingChatNum);
            mQingchatIv.setVisibility(View.GONE);
            mQingchatLlyt.setClickable(false);
        } else {
            mQingchatTv.setText("未设置");
            mQingchatIv.setVisibility(View.VISIBLE);
        }

        if (isVip){
            vipHeadIcon.setVisibility(View.VISIBLE);
        }else {
            vipHeadIcon.setVisibility(View.GONE);
        }

        GlideUtils.loadHeader(DqApp.sContext, headPic + DqUrl.url_avatar_suffix, mPortraitIv);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mPortraitLlyt.getId()) {  // 头像界面
            NavUtils.gotoPersonalAvatarSettingActivity(this);
        } else if (id == mNickNameLlyt.getId()) { // 昵称界面
            NavUtils.gotoPersonalInfoSettingActivity(this, KeyValue.ONE_STRING);
        }
        else if (id == mGenderLlyt.getId()) { // 性别
            NavUtils.gotoPersonalInfoSettingActivity(this, KeyValue.TWO_STRING);
        } else if (id == mQingchatLlyt.getId()) {//斗圈号
            String chuiniuNum = ModuleMgr.getCenterMgr().getDqNum();
            if (TextUtils.isEmpty(chuiniuNum)) {
                NavUtils.gotoPersonalInfoSettingActivity(this, KeyValue.THREE_STRING);
            }
        }else if(id == mQrCodeLlyt.getId()) {
            NavUtils.gotoQRCodeActivity(this);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if (null == entity) {
            DqToast.showShort(getString(R.string.comm_error));
            return;
        }

        if (KeyValue.Code.TOKEN_ERR == code) {
            DqToast.showShort(this.getResources().getString(R.string.auth_fail));
            DqUtils.quit(this);
        }else {
            DqToast.showShort(entity.content);
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (null == entity) {
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }

    @Override
    public void onMessage(String key, Object value) {
        if(MsgType.MT_CENTER_PERSONALINFO_CHANGE.equals(key)) {
            updateUserInfo();
        }
    }
}
