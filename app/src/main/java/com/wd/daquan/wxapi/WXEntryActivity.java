package com.wd.daquan.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wd.daquan.BuildConfig;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.util.CallBackUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.OkHttpUtil;
import http.callback.StringCallback;
import okhttp3.Call;

/**
 * 微信登录功能
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        DqLog.e("YM-----------WXEntryActivity>回调登录");
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
    public void onReq(BaseReq baseReq) {
        Log.e("YM", "onPayFinish, errCode = onReq");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        int errCode = baseResp.errCode;
        switch (errCode){
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) baseResp).code;
                //获取accesstoken
//                getAccessToken(code);
                getRefreshToken(code);
                Log.d("fantasychongwxlogin", code.toString()+ "");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
            default:
                finish();
                break;

        }
//        if(baseResp instanceof SendAuth.Resp){
//            SendAuth.Resp newResp = (SendAuth.Resp) baseResp;
//            //获取微信传回的code
//            String code = newResp.code;
//        }
    }

    private void getDqAccessToken(String code){
        Map<String,String> params = new HashMap<>();
        params.put("code",code);
        RetrofitHelp.getUserApi().getCommonRequestNoBody(DqUrl.url_oauth_wx_access_token,RetrofitHelp.getRequestBody(params)).enqueue(new DqCallBack<DataBean>(){
            @Override
            public void onSuccess(String url, int code, DataBean entity) {
                if (entity.result != 0){
                    DqToast.showShort(entity.content);
                    return;
                }
                String json = entity.data.toString();
                DqLog.e("YM------>获取的登录函数结果",json);
                CallBackUtils.getInstance().sendMessageSingle(MsgType.WX_LOGIN_RESULT,json);
            }

            @Override
            public void onFailed(String url, int code, DataBean entity) {
                DqToast.showShort(entity.content);
            }
        });
    }

    private void getAccessToken(String code) {
        DqLog.e("YM--------微信登录的code:"+code);
//        CallBackUtils.getInstance().sendMessageSingle(MsgType.WX_LOGIN_RESULT,"{'key':'value'}");
//        Map<String,String> params = new HashMap<>();
//        params.put("code",code);
//        RetrofitHelp.getUserApi().getCommonRequestNoBody(DqUrl.url_oauth_wx_access_token,RetrofitHelp.getRequestBody(params)).enqueue(new DqCallBack<DataBean>(){
//            @Override
//            public void onSuccess(String url, int code, DataBean entity) {
//                if (entity.result != 0){
//                    DqToast.showShort(entity.content);
//                    return;
//                }
//                String json = entity.data.toString();
//                DqLog.e("YM------>获取的登录函数结果",json);
//                CallBackUtils.getInstance().sendMessageSingle(MsgType.WX_LOGIN_RESULT,json);
//            }
//
//            @Override
//            public void onFailed(String url, int code, DataBean entity) {
//                DqToast.showShort(entity.content);
//            }
//        });
        //获取授权
        StringBuffer loginUrl = new StringBuffer();
        loginUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=")
                .append("wx02e1114117d39fed")
                .append("&secret=")
//                792C60DBBDF765BED53FBA82168F44D5
                .append("fdf5fc9a400df9226638b2ac125c0cde")
                .append("&code=")
                .append(code)
                .append("&grant_type=authorization_code");
        Log.d("urlurl", loginUrl.toString());
//        OkHttpUtil.postJson().url(URLUtil.getURL(url)).content(MessageJsonRequestUtil.createRequestJson(params)).build().execute(callBack);
        OkHttpUtil.simpleGet(loginUrl.toString(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                DqLog.e("YM---------->请求失败:");
                finish();
            }

            @Override
            public void onResponse(String response, int id) {
                DqLog.e("YM---------->getAccessToken:",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ModuleMgr.getCenterMgr().saveWxRefreshToken(jsonObject.optString("refresh_token",""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CallBackUtils.getInstance().sendMessageSingle(MsgType.WX_LOGIN_RESULT,response);
                finish();
            }
        });
//        https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
    }

    private void getRefreshToken(String code){
        String refreshToken = ModuleMgr.getCenterMgr().getWxRefreshToken();
        DqLog.e("YM---------->获取的refreshToken:",refreshToken);
        if (!TextUtils.isEmpty(refreshToken)){
            StringBuffer loginUrl = new StringBuffer();
            loginUrl.append("https://api.weixin.qq.com/sns/oauth2/refresh_token")
                    .append("?appid=")
                    .append("wx02e1114117d39fed")
                    .append("&refresh_token=")
//                792C60DBBDF765BED53FBA82168F44D5
                    .append(refreshToken)
                    .append("&grant_type=refresh_token");
            Log.d("urlurl", loginUrl.toString());
//        OkHttpUtil.postJson().url(URLUtil.getURL(url)).content(MessageJsonRequestUtil.createRequestJson(params)).build().execute(callBack);
            OkHttpUtil.simpleGet(loginUrl.toString(), new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    e.printStackTrace();
                    DqLog.e("YM---------->请求失败:");
                    finish();
                }

                @Override
                public void onResponse(String response, int id) {
                    DqLog.e("YM---------->getRefreshToken:",response);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        int errorCode = jsonObject.optInt("errcode",-1);
                        if (errorCode == 40030){
                            getAccessToken(code);
                            return;
                        }
                        ModuleMgr.getCenterMgr().saveWxRefreshToken(jsonObject.optString("refresh_token",""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
                    CallBackUtils.getInstance().sendMessageSingle(MsgType.WX_LOGIN_RESULT,response);
                }
            });
            return;
        }
        getAccessToken(code);
    }

}