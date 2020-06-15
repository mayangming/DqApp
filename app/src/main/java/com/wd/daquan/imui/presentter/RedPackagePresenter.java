package com.wd.daquan.imui.presentter;

import android.content.Context;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.daquan.BuildConfig;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.mine.presenter.WalletCloudIView;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.UserCloudWallet;
import com.wd.daquan.model.bean.WxPayBody;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.wxapi.WxPayUtils;

import java.util.Map;

/**
 * 红包页面数据解析
 */
public class RedPackagePresenter extends MinePresenter<WalletCloudIView<DataBean>> {
    IWXAPI iwxapi;

    /**
     * 获取零钱用户信息
     */
    public void getUserCloudWallet(String url, Map<String,String> params){
        RetrofitHelp.getUserApi().getUserCloudWallet(url,getRequestBody(params)).enqueue(new DqCallBack<DataBean<UserCloudWallet>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<UserCloudWallet> entity) {
                success(url,code,entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<UserCloudWallet> entity) {
                failed(url,code,entity);
            }
        });
    }
    /***
     * 获取微信订单信息
     */
    public void getWeChatPayOrderInfo(String url, Map<String, String> hashMap) {
        showLoading();
        RetrofitHelp.getUserApi().getWeChatPayOrder(url,getRequestBodyByFromData(hashMap)).enqueue(new DqCallBack<DataBean<WxPayBody>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<WxPayBody> entity) {
                DqLog.e("YM","成功------："+url);
                hideLoading();
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<WxPayBody> entity) {
                hideLoading();
                failed(url, code, entity);
                DqLog.e("YM","失败------");
            }
        });
    }

    /**
     * 微信支付
     */
    public void startWeChatPay(Context context, WxPayBody wxPayModel){
        if (null == iwxapi){
            iwxapi = WXAPIFactory.createWXAPI(context, null);
            iwxapi.registerApp(BuildConfig.WX_PAY_APPID);
        }
        PayReq request = new PayReq();
        request.appId = wxPayModel.getAppid();
        request.partnerId = wxPayModel.getMch_id();
        request.prepayId= wxPayModel.getPrepay_id();
        request.packageValue = BuildConfig.WX_PAY_PACKAGE;
        request.nonceStr= wxPayModel.getNonce_str();
        request.timeStamp= String.valueOf(wxPayModel.getTimestamp());
        request.extData = "redPackage";//红包类型
        request.sign= WxPayUtils.createSign(request);
        iwxapi.sendReq(request);
    }

}