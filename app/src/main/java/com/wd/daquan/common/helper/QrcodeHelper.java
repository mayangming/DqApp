package com.wd.daquan.common.helper;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.da.library.tools.Utils;
import com.dq.im.type.ImType;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.Constants;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.listener.QrCodeListener;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.common.utils.CNLog;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.mine.bean.ScanCodeEntity;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.third.session.SessionHelper;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


/**
 * @author: dukangkang
 * @date: 2018/5/21 13:40.
 * @description: todo ...
 */
public class QrcodeHelper implements Presenter.IView<DataBean> {

    private String mUid = null;

    private MinePresenter mMainPresenter = null;

    private QrCodeListener mQrCodeListener = null;

    private WeakReference<Activity> mWeakReference = null;
    public QrcodeHelper(Activity activity) {
        mWeakReference = new WeakReference<>(activity);
        mMainPresenter = new MinePresenter();
        mMainPresenter.attachView(this);
    }

    public void setQrCodeListener(QrCodeListener qrCodeListener) {
        mQrCodeListener = qrCodeListener;
    }

    /**
     * 截取二维码内容
     * @param url
     * @return
     */
    private String getQrContent(String url) {
        String json = "";
        String unique = KeyValue.QR.KEY_UNIQUE + "?" + KeyValue.QR.DQ;
        int index = url.indexOf(unique);
        if (-1 != index) {
            String qrContent = url.substring(index + unique.length());
            try {
                String target = URLDecoder.decode(qrContent, "utf-8");
                CNLog.w("xxxx", "qrhelper decode: " + target);
//                json = EasyAES.decryptString(target);
                json = target;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    /**
     * 识别二维码
     *
     * @param url
     */
    public void distinguishQrcode(Context context, String url) {
        CNLog.e("fz", "helper 识别二维码：" + url);
        // 地址为空
        if (TextUtils.isEmpty(url)) {
            if (null != mQrCodeListener) {
                mQrCodeListener.notFound();
            }
            return;
        }

        // 外部网页地址
        url = url.toLowerCase();
        if (!QrcodeManager.isQrcodeUrl(url)) {
            if (url.startsWith("http")) {
                NavUtils.gotobrowser(context, url);
                if (null != mQrCodeListener) {
                    mQrCodeListener.finish();
                }
            } else {
                if (null != mQrCodeListener) {
                    mQrCodeListener.notFound();
                }
            }
            return;
        }

        // 无网络
        if (!Utils.isNetworkConnected(context)) {
            DqToast.showShort(context.getString(R.string.comm_no_network));
            if (null != mQrCodeListener) {
                mQrCodeListener.resume();
            }
            return;
        }

        // 添加好友、H5打开跳转打开斗圈／下载斗圈
        if (url.contains(KeyValue.QR.KEY_UNIQUE)) {
            String json = getQrContent(url);
            CNLog.w("xxxx", "解码：" + json);
            if (TextUtils.isEmpty(json)) {
                if (null != mQrCodeListener) {
                    mQrCodeListener.notFound();
                }
                return;
            }

            ScanCodeEntity scanCodeEntity = GsonUtils.fromJson(json, ScanCodeEntity.class);
            if (null == scanCodeEntity) {
                if (null != mQrCodeListener) {
                    mQrCodeListener.notFound();
                }
                return;
            }

            // 添加好友
            if (KeyValue.QRType.PERSION.equals(scanCodeEntity.type)) {

                if (ModuleMgr.getCenterMgr().getUID().equals(scanCodeEntity.uid)) {
                    DqToast.showShort(context.getString(R.string.qr_code_self_warning));
                    if (null != mQrCodeListener) {
                        mQrCodeListener.resume();
                    }
                    return;
                }
                Activity activity = mWeakReference.get();
                if (activity == null) {
                    return;
                }
                NavUtils.gotoUserInfoActivity(activity, scanCodeEntity.uid, ImType.P2P.getValue());

            } else if (KeyValue.QRType.GROUP.equals(scanCodeEntity.type)) { // 加入群聊

                mUid = scanCodeEntity.source_id;
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put(KeyValue.GROUP_ID, scanCodeEntity.group_id);
                hashMap.put(KeyValue.Group.SHOW_GROUP_MEMBER, "1");
                mMainPresenter.getTeamInfo(DqUrl.url_select_group, hashMap);
            }
        } else {
            NavUtils.gotobrowser(context, url);
            if (null != mQrCodeListener) {
                mQrCodeListener.retryScan();
            }
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showLoading(String tipMessage) {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if (null != mQrCodeListener) {
            mQrCodeListener.notFound();
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (null == entity) {
            return;
        }

        Activity activity = mWeakReference.get();
        if (activity == null) {
            return;
        }
         if (DqUrl.url_select_group.equals(url)) { // 加入群聊
            doAddGroup(entity, activity);
        }
    }

    /**
     * 添加好友
     */
    private void doAddFriend(Activity activity) {
        NavUtils.gotoUserInfoActivity(activity, mUid, ImType.P2P.getValue());
        activity.finish();
    }

    /**
     * 处理添加好友
     * @param entity
     *  数据实体
     * @param activity
     *  上下文
     */
    private void doAddGroup(DataBean entity, Activity activity) {
        if (null == entity) {
            if (null != mQrCodeListener) {
                mQrCodeListener.notFound();
            }
            return;
        }

        GroupInfoBean infoBean = (GroupInfoBean) entity.data;
        if (null == infoBean) {
            if (null != mQrCodeListener) {
                mQrCodeListener.notFound();
            }
            CNLog.w("xxxx", "groupBaseResp = null");
            return;
        }

        switch (infoBean.getStatus()) {
            case KeyValue.Team.ADD: // 加入群聊
                if (infoBean.isWithIn()) {
                    SessionHelper.startTeamSession(activity, infoBean.group_id);
                    if(mQrCodeListener != null) {
                        mQrCodeListener.finish();
                    }
                } else  {
                    NavUtils.gotoAddGroupActivity(activity, infoBean, mUid);
                    if(mQrCodeListener != null) {
                        mQrCodeListener.finish();
                    }
                }
                break;
            case KeyValue.Team.DISBANDED: // 群组已解散
                DqToast.showShort(DqApp.getStringById(R.string.qrcode_disolve));
                break;
            case KeyValue.Team.BLOCKED: // 群已封禁
                DqToast.showShort(DqApp.getStringById(R.string.qrcode_block));
                break;
        }
    }

    /**
     * 获取真实UID
     * @param uid
     * @return
     */
    public static String getRealUid(String uid) {
        int uidInt = Integer.parseInt(uid);
        int realUid = uidInt - Constants.TARGET_VALUE;
        return "" + realUid;
    }
}
