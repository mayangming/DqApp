package com.wd.daquan.mine.activity;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dq.im.viewmodel.ApplicationViewModel;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.NetWorkUtils;
import com.wd.daquan.login.helper.LogoutHelper;
import com.wd.daquan.mine.dialog.NormalDialog;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;

import java.util.HashMap;

public class SettingActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener {
    private LinearLayout layout_accountBind;
    private LinearLayout layout_newMsg;
    private LinearLayout layout_safe;
    private LinearLayout layout_fontSize;
    private LinearLayout layout_chatBg;
    private LinearLayout layout_cache;
    private LinearLayout layout_complain;
    private LinearLayout layout_about;
    private TextView btn_logOut;
    private View settingLogOutBtn;
    private ImageView img_redDot;
    private EBSharedPrefUser ebSharedPrefUser;
    /**
     * 退出登录dialog
     */
    private Dialog mLogoutDialog = null;
    private DialogUtils.BottomDialogButtonListener mLogoutListener = id -> {
        switch (id) {
            case R.id.tv_confirm:
                    if(mPresenter != null){
                        mPresenter.logout(DqUrl.url_logout);
                    }
                break;
        }
    };

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.setting_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        layout_accountBind = findViewById(R.id.settingActivityBindAccountLayout);
        layout_newMsg = findViewById(R.id.settingActivityNewMessageLayout);
        layout_safe = findViewById(R.id.settingActivitySafeLayout);
        layout_fontSize = findViewById(R.id.settingActivityFontSizeLayout);
        layout_chatBg = findViewById(R.id.settingActivityChatBgLayout);
        layout_cache = findViewById(R.id.settingActivityCacheLayout);
        layout_about = findViewById(R.id.settingActivityAboutLayout);
        layout_complain = findViewById(R.id.complaintLayout);
        btn_logOut = findViewById(R.id.settingActivityBtn);
        settingLogOutBtn = findViewById(R.id.settingLogOutBtn);
        img_redDot = findViewById(R.id.settingActivityAboutRedDot);
    }

    @Override
    public void initListener() {
        toolbarBack();
        layout_accountBind.setOnClickListener(this);
        layout_newMsg.setOnClickListener(this);
        layout_safe.setOnClickListener(this);
        layout_fontSize.setOnClickListener(this);
        layout_chatBg.setOnClickListener(this);
        layout_cache.setOnClickListener(this);
        layout_about.setOnClickListener(this);
        btn_logOut.setOnClickListener(this);
        settingLogOutBtn.setOnClickListener(this);
        layout_complain.setOnClickListener(this);
    }

    @Override
    public void initData() {
        ebSharedPrefUser = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(KeyValue.ONE_STRING.equals(ebSharedPrefUser.getString(EBSharedPrefUser.UPDATE_STATUS, KeyValue.ZERO_STRING))){
            img_redDot.setVisibility(View.VISIBLE);
        }else{
            img_redDot.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingActivityBindAccountLayout://绑定账号
                NavUtils.gotoAccountBlindActivity(SettingActivity.this);
                break;
            case R.id.settingActivityNewMessageLayout://新消息通知
                NavUtils.gotoNewMsgNotifyActivity(this);
                break;
            case R.id.settingActivitySafeLayout://安全与隐私
                NavUtils.gotoSafeActivity(this);
                break;
            case R.id.settingActivityFontSizeLayout://字体大小
                NavUtils.gotoTextSizeActivity(this);
                break;
            case R.id.settingActivityChatBgLayout://聊天背景
                NavUtils.gotoChatBGSettingActivity(this);
                break;
            case R.id.settingActivityCacheLayout://缓存设置 清除聊天记录
//                NavUtils.gotoChatBg(this);
                DialogUtils.showBottomDialog(SettingActivity.this, KeyValue.THREE, id -> {
                    if(R.id.tv_confirm == id){
                        DqToast.showShort(R.string.setting_clear_msg_history_success);
                    }
                }).show();
                break;
            case R.id.settingActivityAboutLayout://关于斗圈
                NavUtils.gotoAboutQSActivity(this);
                break;
            case R.id.settingActivityBtn://退出登录
                if (mLogoutDialog == null) {
                    mLogoutDialog = DialogUtils.showBottomDialog(this, 2, mLogoutListener);
                }
                if (!mLogoutDialog.isShowing()) {
                    mLogoutDialog.show();
                }
                break;
            case R.id.settingLogOutBtn://注销账号
                logOutDialog();
                break;
            case R.id.complaintLayout://投诉举报
                NavUtils.gotoComplaintActivity(this);
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if(null == entity) return;
        if (NetWorkUtils.isNetworkAvailable(this)) {
//            Utils.quit(this);
//            NavUtils.gotoBackRegisterLoginActivity(this);
        } else {
            DqToast.showShort(getString(R.string.net_error));
        }
        DqUtils.bequit(entity, this);

    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_logout.equals(url)) {//退出登陆
            LogoutHelper.logout(this);
            ViewModelProviders.of(this).get(ApplicationViewModel.class).closeRoomDataBase();
        }else if (DqUrl.url_user_cancellation.equals(url)){//注销账号
            LogoutHelper.logoff(this);
            ViewModelProviders.of(this).get(ApplicationViewModel.class).closeRoomDataBase();
        }
    }

    /**
     * 注销账号对话框
     */
    NormalDialog logOutDialog;
    private void logOutDialog(){
        if (null == logOutDialog){
            logOutDialog = new NormalDialog();
        }
        NormalDialog.Build build = logOutDialog.getBuild();
        build.setTitle("注销账号");
        build.setContent("您是否要注销账号?");
        logOutDialog.setBuild(build);
        logOutDialog.show(getSupportFragmentManager(),"");
        logOutDialog.setDialogOperatorIpc(new NormalDialog.DialogOperatorIpc() {
            @Override
            public void sure() {
                Toast.makeText(SettingActivity.this,"确定注销",Toast.LENGTH_LONG).show();
                mPresenter.requestLogOff(DqUrl.url_user_cancellation,new HashMap<>());
            }

            @Override
            public void cancel() {
                logOutDialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLogoutDialog != null) {
            mLogoutDialog.dismiss();
            mLogoutDialog = null;
        }
    }
}
