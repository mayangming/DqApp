package com.wd.daquan.chat.redpacket;

import android.content.Context;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.bean.ForbidEntity;
import com.wd.daquan.chat.bean.RobRpEntity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.ResponseCode;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.helper.DialogHelper;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.model.log.DqToast;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * @author: dukangkang
 * @date: 2018/9/12 17:25.
 * @description: todo ...
 * 红包相关的帮助类
 */
public class RedPacketHelper implements Presenter.IView<DataBean>, Serializable {
    private int mRequestCode;
    private Context mContext = null;
    private RedPacketPresenter mPresenter = null;

    public RedPacketHelper(Context context) {
        this(context, 1234);
    }

    public RedPacketHelper(Context context, int requestCode) {
        this.mContext = context;
        this.mRequestCode = requestCode;
        mPresenter = new RedPacketPresenter();
        mPresenter.attachView(this);
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

    /**
     * 抢红包
     * @param redpacketId
     * @param signature
     */
    public void robRedpacket(String redpacketId, String signature) {
        if (mPresenter != null) {
            LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
            hashMap.put("redpacket_id", redpacketId);
            hashMap.put("signature", signature);
            mPresenter.robAlipayRedPacket(DqUrl.url_reciveRedPacket, hashMap);
        }
    }

    /**
     * 开红包
     * @param redpacketId
     * @param signature
     */
    public void openRedpacket(String redpacketId, String signature) {
        if (mPresenter != null) {
            LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
            hashMap.put("redpacket_id", redpacketId);
            hashMap.put("signature", signature);
            mPresenter.checkForbidRedpacket(DqUrl.url_openRedpacket, hashMap);
        }
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
                if (entity != null) {
                    DialogHelper.showDialogForAuth(mContext, entity.content);
                }
                break;
            case ResponseCode.EXPIRY_AUTH:
                if (entity != null) {
                    DqToast.showShort(DqApp.getStringById(R.string.expiry_auth));
                    // TODO: 2018/9/13  退出登录
                }
                break;
            case ResponseCode.ALIAPY_RP_EMPTY:
                if (mRedPacketCallback != null) {
                    mRedPacketCallback.redpacketEmpty();
                }
                break;
                default:
                    if (entity != null) {
                        DqToast.showShort(entity.content);
                    }
                    break;
        }

        if (mRedPacketCallback != null) {
            mRedPacketCallback.onFailed(code);
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if(entity == null) return;
        if (DqUrl.url_forbidRedpacket_check.equals(url)) { //判断是否在禁止名单内
                ForbidEntity forbidEntity = (ForbidEntity) entity.data;
                if (null != forbidEntity) {
                    if ("1".equals(forbidEntity.exists)) {
//                        DialogUtil.showRedpocket(context, context.getResources().getString(R.string.dispatcher_contact)).show();
                        DqToast.showShort(DqApp.sContext.getResources().getString(R.string.forbid_redpacket_tips));
                    } else {
                        if (mRedPacketCallback != null) {
                            mRedPacketCallback.allowForbidRedpacket();
                        }
                    }
                }
        } else if (DqUrl.url_reciveRedPacket.equals(url)) { // 抢支付宝红包
            if (code != 0) {
                return;
            }
            RobRpEntity robRpEntity = (RobRpEntity) entity.data;
            if (mRedPacketCallback != null) {
                mRedPacketCallback.robRedPacket(robRpEntity);
            }
        } else if (DqUrl.url_openRedpacket.equals(url)) { // 开支付宝红包
            if (mRedPacketCallback != null) {
                mRedPacketCallback.openRedPacket(code, entity.content);
            }
        }
    }

    private RedPacketCallback mRedPacketCallback = null;

    public void setRedPacketCallback(RedPacketCallback redPacketCallback) {
        mRedPacketCallback = redPacketCallback;
    }


}
