package com.wd.daquan.login.multiport;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.Constants;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.da.library.listener.DialogListener;
import com.wd.daquan.login.helper.LogoutHelper;
import com.da.library.view.CommDialog;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: dukangkang
 * @date: 2018/10/31 13:58.
 * @description: todo ...
 */
public class MultiportActivity extends DqBaseActivity<MultiportPresenter, DataBean> implements View.OnClickListener {
    private TextView mClosetv;
    private ImageView mWindowsStateIv;
    private TextView mWindowsStateTv;
    private TextView mLoginExitTv;
    private TextView mLoginCancelTv;
    /**
     * 是登录还是退出
     */
    private String type = null;
    /**
     * 网页登录人id
     */
    private String mQRUid = null;
    /**
     * 退出网页登录确认dialog
     */
    private CommDialog mExitDialog = null;

    @Override
    public MultiportPresenter createPresenter() {
        return new MultiportPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.multiport_activity);
    }

    @Override
    protected void init() {
        initIntent();
    }

    @Override
    public void initView() {
        mClosetv = findViewById(R.id.close_web_login_confirm_tv);
        mWindowsStateIv = findViewById(R.id.windows_login_state_iv);
        mWindowsStateTv = findViewById(R.id.windows_login_state_tv);
        mLoginExitTv = findViewById(R.id.login_or_exit_tv);
        mLoginCancelTv = findViewById(R.id.login_cancel_tv);
    }

    @Override
    public void initListener() {
        mClosetv.setOnClickListener(this);
        mWindowsStateIv.setOnClickListener(this);
        mWindowsStateTv.setOnClickListener(this);
        mLoginExitTv.setOnClickListener(this);
        mLoginCancelTv.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (Constants.Web.Login.equals(type)) {
            loginState();
        } else {
            exitState();
        }
        mWindowsStateIv.setImageResource(R.mipmap.multiport_web_computer);
    }

    /**
     * 获取传递数据
     */
    public void initIntent() {
        type = getIntent().getStringExtra(KeyValue.QR.KEY_WEB_TYPE);
        mQRUid = getIntent().getStringExtra(KeyValue.QR.KEY_WEB_QRUUID);
    }

    /**
     * 退出登录状态
     */
    private void exitState() {
        mWindowsStateTv.setText(getString(R.string.web_login_tips));
        mLoginExitTv.setText(getString(R.string.web_logout));
        mLoginCancelTv.setVisibility(View.GONE);
    }

    private void loginState() {
        mWindowsStateTv.setText(getString(R.string.web_login_confirm));
        mLoginExitTv.setText(getString(R.string.login));
        mLoginCancelTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (KeyValue.Code.TOKEN_ERR == code) {
            LogoutHelper.logout(activity, activity.getResources().getString(R.string.auth_fail));
        } else {
            DqToast.showShort(entity.content);
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        if (KeyValue.OK.equals(entity.status)) {
            if (DqUrl.url_web_scan_login.equals(url)) {
                finish();
            } else if (DqUrl.url_web_logout.equals(url)) {
                MsgMgr.getInstance().sendMsg(MsgType.MT_WEB_NOTICE, Constants.Web.Logout);
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //关闭
            case R.id.close_web_login_confirm_tv:
                finish();
                break;
            //登录或退出登录
            case R.id.login_or_exit_tv:
                if (Constants.Web.Login.equals(type)) {
                    if (!TextUtils.isEmpty(mQRUid)) {
                        Map<String, String> map = new HashMap<>();
                        map.put(KeyValue.QR.KEY_WEB_QRUUID, mQRUid);
                        map.put(KeyValue.QR.CLIENT_TYPE, KeyValue.QR.APP);
                        mPresenter.webConfirmLogin(DqUrl.url_web_scan_login, map);
                    }
                } else if (Constants.Web.Logout.equals(type)) {
                    exitWeb();
                }

                break;
            //取消登录
            case R.id.login_cancel_tv:
                finish();
                break;
        }
    }

    /**
     * 退出网页登录
     */
    private void exitWeb() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        mExitDialog = new CommDialog(activity);
        mExitDialog.setTitleVisible(false);
        mExitDialog.setDesc(getString(R.string.web_logout_tips));
        mExitDialog.setCancelTxt(getString(R.string.cancel));
        mExitDialog.setOkTxt(getString(R.string.confirm));
        mExitDialog.setOkTxtColor(getResources().getColor(R.color.color_3c74ba));
        mExitDialog.setWidth();
        mExitDialog.show();

        mExitDialog.setDialogListener(mExitWebListener);
    }

    /**
     * 退出网页登录确认dialog监听
     */
    private DialogListener mExitWebListener = new DialogListener() {
        @Override
        public void onCancel() {

        }

        @Override
        public void onOk() {
            //CNToastUtil.showToast(mActivity, "退出登录成功");
            Map<String, String> map = new HashMap<>();
            mPresenter.getWebLogout(DqUrl.url_web_logout, map);
        }
    };

    @Override
    protected void onDestroy() {
        if (null != mExitDialog) {
            mExitDialog.dismiss();
            mExitDialog = null;
        }
        super.onDestroy();
    }
}
