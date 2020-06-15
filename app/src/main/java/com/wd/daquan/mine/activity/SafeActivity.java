package com.wd.daquan.mine.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.QCBroadcastManager;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.view.CommSwitchButton;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.da.library.widget.CommTitle;

import java.util.LinkedHashMap;

public class SafeActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener {

    private CommSwitchButton switch_phone, switch_qingChatNum, switch_addFriend;
    // 是否允许直接被添加至群聊
    private CommSwitchButton mTeamInviteSwitch;
    private LinearLayout layout_login;
    private LinearLayout layout_black;
    private LinearLayout layout_safeCenter;
    private ImageView img_redDot;
    private CommTitle mCommTitle;
    // 是否允许斗圈号搜索
    private EBSharedPrefUser mSharedPrefUser = null;
    private QCBroadcastManager broadcastManager = null;
    @Override
    protected void setContentView() {
        setContentView(R.layout.safe_activity);
    }

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.safeActivityTitle);
        switch_phone =  findViewById(R.id.safeActivitySwitchPhone);
        switch_qingChatNum = findViewById(R.id.safeActivitySwitchQingChatNum);
        switch_addFriend= findViewById(R.id.safeActivitySwitchAddFriend);
        layout_login =  findViewById(R.id.safeActivityLayoutLogin);
        layout_black =  findViewById(R.id.safeActivityLayoutBlack);
        layout_safeCenter =  findViewById(R.id.safeActivityLayoutSafe);
        img_redDot= findViewById(R.id.safeActivityImgRedDot);
        mCommTitle.setTitle(getString(R.string.setting_safe));

        mTeamInviteSwitch = this.findViewById(R.id.safe_team_invite_switchbtn);
    }

    @Override
    public void initListener() {
        layout_login.setOnClickListener(this);
        layout_black.setOnClickListener(this);
        layout_safeCenter.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);

        //允许通过手机号搜索
        switch_phone.setOnSwChangedListener(new CommSwitchButton.OnSwChangedListener() {
            @Override
            public void onChanged(int id, View v, boolean isChecked) {
                switch_phone.setCheckedNoEvent(isChecked);
                LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                linkedHashMap.put("allow_search", isChecked ? KeyValue.ZERO_STRING : KeyValue.ONE_STRING);
                mPresenter.setUserSwitch(DqUrl.url_set_allow_search, linkedHashMap);
            }
        });

        //添加好友是否需要认证
        switch_addFriend.setOnSwChangedListener(new CommSwitchButton.OnSwChangedListener() {
            @Override
            public void onChanged(int id, View v, boolean isChecked) {
                switch_addFriend.setCheckedNoEvent(isChecked);
                LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                linkedHashMap.put("added_friend_whether_validate", isChecked ? KeyValue.ZERO_STRING : KeyValue.ONE_STRING);
                mPresenter.setUserSwitch(DqUrl.url_set_added_friend_whether_validate, linkedHashMap);
            }
        });

        //允许通过项目号搜索
        switch_qingChatNum.setOnSwChangedListener(new CommSwitchButton.OnSwChangedListener() {
            @Override
            public void onChanged(int id, View v, boolean isChecked) {
                switch_qingChatNum.setCheckedNoEvent(isChecked);
                LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
                hashMap.put("allow_douquan_search", isChecked ? KeyValue.ZERO_STRING : KeyValue.ONE_STRING);
                mPresenter.setUserSwitch(DqUrl.url_set_allow_douquan_search, hashMap);
            }
        });

        mTeamInviteSwitch.setOnSwChangedListener(new CommSwitchButton.OnSwChangedListener() {
            @Override
            public void onChanged(int id, View v, boolean isChecked) {
                mTeamInviteSwitch.setCheckedNoEvent(isChecked);
                LinkedHashMap<String,String> hashMap = new LinkedHashMap<>();
                hashMap.put("group_validate", isChecked ? KeyValue.ZERO_STRING : KeyValue.ONE_STRING);
                mPresenter.setUserSwitch(DqUrl.url_allow_group, hashMap);
            }
        });
    }

    @Override
    public void initData() {
        QCSharedPrefManager sharedPrefManager = QCSharedPrefManager.getInstance();
        mSharedPrefUser = sharedPrefManager.getKDPreferenceUserInfo();

        String isAllowSearch = mSharedPrefUser.getString(EBSharedPrefUser.allow_search, "");
        String whether = mSharedPrefUser.getString(EBSharedPrefUser.whether_validate, "");
        if (!TextUtils.isEmpty(whether)) {
            if (whether.equals("0")) {
                switch_addFriend.setCheckedNoEvent(true);
            } else if (whether.equals("1")) {
                switch_addFriend.setCheckedNoEvent(false);
            }
        }
//        if (passwords.equals("0")) {
//            img_redDot.setVisibility(View.VISIBLE);
//        } else if (passwords.equals("1")) {
//            img_redDot.setVisibility(View.GONE);
//        }
        if (isAllowSearch.equals("0")) {
            switch_phone.setCheckedNoEvent(true);
        } else if (isAllowSearch.equals("1")) {
            switch_phone.setCheckedNoEvent(false);
        }

        // 设置允许被添加到群组
        mTeamInviteSwitch.setCheckedNoEvent(ModuleMgr.getCenterMgr().enableTeamInvite());

        String isQSNumSearch = mSharedPrefUser.getString(EBSharedPrefUser.ALLOW_QINGCHAT_SEARCH, "");
        switch_qingChatNum.setCheckedNoEvent("0".equals(isQSNumSearch));
//        broadcastManager = new QCBroadcastManager();
//        broadcastManager.addAction(KeyValue.PASSWORD, new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                img_redDot.setVisibility(View.GONE);
//                broadcastManager.destroy(KeyValue.PASSWORD);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.safeActivityLayoutLogin://登录密码设置界面
                String pwd = ModuleMgr.getCenterMgr().getPwd();
                if (TextUtils.isEmpty(pwd)){
                    NavUtils.gotoForgetLoginPasswordActivity(this);
                }else{
                    NavUtils.gotoLoginPwdModifyActivity(this);
                }
                break;
            case R.id.safeActivityLayoutBlack:
                NavUtils.gotoBlackListActivity(this);
                break;
            case R.id.safeActivityLayoutSafe:
                NavUtils.gotoSafeCenterActivity(this);
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if (null == entity) {
            DqToast.showShort("操作失败");
            return;
        }

        DqUtils.bequit(entity, this);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (null == entity) {
            return;
        }
        if (0 != code) {
            DqToast.showShort(entity.content);
            return;
        }
        if (DqUrl.url_set_allow_douquan_search.equals(url)) {
            //允许斗圈号搜索
            String str = switch_qingChatNum.isChecked() ? KeyValue.ZERO_STRING : KeyValue.ONE_STRING;
            ModuleMgr.getCenterMgr().putString(EBSharedPrefUser.ALLOW_QINGCHAT_SEARCH, str);
        } else if (DqUrl.url_set_allow_search.equals(url)) {
            //设置是否可以通过手机号搜索自己
            String str = switch_phone.isChecked() ? KeyValue.ZERO_STRING : KeyValue.ONE_STRING;
            ModuleMgr.getCenterMgr().putString(EBSharedPrefUser.allow_search, str);
        } else if (DqUrl.url_set_added_friend_whether_validate.equals(url)) {
            //加朋友
            String str = switch_addFriend.isChecked() ? KeyValue.ZERO_STRING : KeyValue.ONE_STRING;
            ModuleMgr.getCenterMgr().putString(EBSharedPrefUser.whether_validate, str);
        } else if (DqUrl.url_allow_group.equals(url)) {//是否允许被邀请入群
            String str = mTeamInviteSwitch.isChecked() ? KeyValue.ZERO_STRING : KeyValue.ONE_STRING;
            ModuleMgr.getCenterMgr().saveTeamInvite(str);
        }
    }
}