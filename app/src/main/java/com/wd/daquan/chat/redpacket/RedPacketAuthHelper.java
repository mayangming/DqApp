package com.wd.daquan.chat.redpacket;

import android.app.Activity;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.bean.ForbidEntity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.RequestCode;
import com.wd.daquan.common.constant.ResponseCode;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.log.DqToast;
import com.da.library.listener.DialogListener;
import com.wd.daquan.login.helper.LogoutHelper;
import com.da.library.tools.Utils;
import com.da.library.view.CommDialog;

import java.util.LinkedHashMap;

/**
 * @author: dukangkang
 * @date: 2018/9/7 18:06.
 * @description:
 *  长按头像，发送支付宝专属红包，工具类
 */
public class RedPacketAuthHelper implements Presenter.IView<DataBean> {

    private RedPacketPresenter mPresenter = null;
    private Activity mActivity = null;
    private String mAccount = "";
    private String mFromAccount = "";
    private String mFromName = "";

    /**
     * 构造函数
     */
    public RedPacketAuthHelper(Activity activity, String account) {
        this.mActivity = activity;
        this.mAccount = account;
    }

    public void setFromAccount(String fromAccount) {
        mFromAccount = fromAccount;
    }

    public void setFromName(String fromName) {
        mFromName = fromName;
    }

    public void request() {
        if (Utils.isFastDoubleClick(500)) {
            return;
        }

        if (!Utils.isNetworkConnected(DqApp.sContext)) {
            DqToast.showShort(DqApp.getStringById(R.string.comm_no_network));
            return;
        }

        mPresenter = new RedPacketPresenter();
        mPresenter.attachView(this);

        checkForbidRedpacket(mAccount);
    }

    /**
     * 绑定支付宝
     */
    private void requestBindAlipay() {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        mPresenter.isBindAlipay(DqUrl.url_isBindAlipay, linkedHashMap);
    }

    /**
     * 检查红包是否被禁止收发
     */
    public void checkForbidRedpacket(String groupId) {
        if (mPresenter != null) {
            LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
            hashMap.put("group_id", groupId);
            mPresenter.checkForbidRedpacket(DqUrl.url_forbidRedpacket_check, hashMap);
        }
    }

    private void startActivity() {
        NavUtils.gotoAlipayTeamActivity(mActivity, RequestCode.ALIAPY_TEAM, mAccount, mFromName, mFromAccount);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        switch (code) {
            case ResponseCode.ALIAPY_AUTH:
                showDialog(entity.content);
                break;
            case ResponseCode.EXPIRY_AUTH:
                DqToast.showShort(DqApp.getStringById(R.string.expiry_auth));
                if (null != mActivity) {
                    LogoutHelper.logout(mActivity);
                }
                break;
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if(entity == null) return;
        if (DqUrl.url_isBindAlipay.equals(url)) {
            startActivity();
        } else if (DqUrl.url_forbidRedpacket_check.equals(url)) { //判断是否在禁止名单内
            ForbidEntity forbidEntity = (ForbidEntity) entity.data;
            if (null != forbidEntity) {
                if ("1".equals(forbidEntity.exists)) {
                    DqToast.showShort(DqApp.sContext.getResources().getString(R.string.forbid_redpacket_tips));
                } else {
                    requestBindAlipay();
                }
            }
        }
    }

    private void showDialog(String message) {
        CommDialog mCommDialog = new CommDialog(mActivity);
        mCommDialog.setTitleVisible(false);
        mCommDialog.setDesc(message);
        mCommDialog.setCancelTxt("取消");
        mCommDialog.setOkTxt("立刻授权");
        mCommDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                NavUtils.gotoAlipayBindingActivity(mActivity);
            }
        });
        mCommDialog.setOkTxtColor(DqApp.sContext.getResources().getColor(R.color.color_5495e8));
        mCommDialog.show();
    }
}
