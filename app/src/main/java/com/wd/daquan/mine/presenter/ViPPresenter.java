package com.wd.daquan.mine.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.daquan.BuildConfig;
import com.wd.daquan.chat.redpacket.pay.PayResult;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.VipCommodityEntity;
import com.wd.daquan.model.bean.WxPayBody;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.wxapi.WxPayUtils;

import java.util.List;
import java.util.Map;

public class ViPPresenter extends MinePresenter<VipIView<DataBean>>{
    IWXAPI iwxapi;

    /**
     * 支付宝支付
     */
    public void startAliPay(Activity activity, String payInfo){
        new Thread(() -> {
            PayTask aliPayTask = new PayTask(activity);
            Map<String,String> result = aliPayTask.payV2(payInfo,true);
            PayResult payResult = new PayResult(result);
            Log.e("YM_支付结果","状态值:"+payResult.getResultStatus()+",结果:"+payResult.getResult());
            mView.aliPaySuccess(payResult);
        }).start();
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
        request.sign= WxPayUtils.createSign(request);
        iwxapi.sendReq(request);
    }
    /**
     * 获取VIP列表
     */
    public void getVipCommodityList(String url,Map<String,String> params){
        RetrofitHelp.getUserApi().vipCommodityList(url,getRequestBody(params)).enqueue(new DqCallBack<DataBean<List<VipCommodityEntity>>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<List<VipCommodityEntity>> entity) {
                success(url,code,entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<List<VipCommodityEntity>> entity) {
                failed(url,code,entity);
            }
        });
    }

}