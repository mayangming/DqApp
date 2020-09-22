package com.wd.daquan.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.daquan.BuildConfig;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

/**
 * 微信支付结果页
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DqLog.e("YM----------->回调登录");
        api = WXAPIFactory.createWXAPI(this, BuildConfig.WX_PAY_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.e("YM", "onPayFinish, errCode = onReq");
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("YM", "onPayFinish, errCode = " + resp.errCode);
        Log.e("YM", "type = " + resp.getType());
        PayResp payResp = (PayResp) resp;
        if (ConstantsAPI.COMMAND_PAY_BY_WX == resp.getType()){
            if (0 == resp.errCode){
                String extData = payResp.extData;
                Log.e("YM", "微信支付成功后的拓展信息： " + extData);
                if ("redPackage".equals(extData)){
                    Log.e("YM", "微信支付成功后的拓展信息--->红包信息改动： " + extData);
//                    MsgMgr.getInstance().sendMsg(MsgType.RED_PACKAGE_PAY, "");
                }else if ("taskType".equals(extData)){//创建任务后的事件监听
                    MsgMgr.getInstance().sendMsg(MsgType.TASK_PAY_RESULT, "");
                }else {
                    MsgMgr.getInstance().sendMsg(MsgType.VIP_EXCHANGE_CHANGE, "");
                }
            }
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}