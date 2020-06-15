package com.wd.daquan.sdk;

import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.da.library.tools.Utils;
import com.da.library.widget.CommTitle;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.OpenSdkLoginBean;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;

import java.util.HashMap;
import java.util.Map;


public class DqSdkLoginActivity extends BaseDqSdkShareActivity implements View.OnClickListener, QCObserver {

    private ImageView img_logo;
    private TextView mLogin;
    private TextView txt_appName;
    private String appId;
    private String appSecret;
    private CommTitle mCommTitle;
    private OpenSdkLoginBean appInfo;


    @Override
    protected void setContentView() {
        setContentView(R.layout.sdk_activity_main);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mCommTitle = findViewById(R.id.sdk_title);
        mCommTitle.setTitle(getString(R.string.app_name));
        //授权界面
        txt_appName = findViewById(R.id.authActivityAppName);
        img_logo = findViewById(R.id.authActivityAppLogo);
        //登录
        mLogin = findViewById(R.id.authActivityAuth);
    }

    @Override
    protected void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        MsgMgr.getInstance().attach(this);
        mLogin.setOnClickListener(view -> {
            if (Utils.isFastDoubleClick(500)) {
                return;
            }
            requestAppCode();

        });
    }

    @Override
    public void initData() {
        super.initData();
        verifyAppId();
//        testBroadCast();
    }

    private void testBroadCast(){
        Intent intent = new Intent("com.opensdk.qingchat");
        intent.setComponent(new ComponentName("com.example.qcsdk","com.opensdk.qingchat.utils.ReceiveCNBroadcastReceiver"));
        intent.putExtra("code","asdb");
        sendBroadcast(intent);
    }
    private void verifyAppId() {
        if(mShareBean == null) {
            return;
        }
        appId = mShareBean.appId;
        appSecret =  mShareBean.appSecret;
        String uid = ModuleMgr.getCenterMgr().getUID();
//        String token = ModuleMgr.getCenterMgr().getToken();
        HashMap<String,String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("appId", appId);
        map.put("appSecret", appSecret);
        mPresenter.verifyAppid(DqUrl.url_get_app_info, map);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
        }
    }


    private void requestAppCode(){
        String uid = ModuleMgr.getCenterMgr().getUID();
        String token = ModuleMgr.getCenterMgr().getToken();
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("appId", appId);
        hashMap.put("uid", uid);
        hashMap.put("token", token);
        mPresenter.getAppCode(DqUrl.url_get_app_code, hashMap);
    }

    private void requestAccessToken(String code){
        String uid = ModuleMgr.getCenterMgr().getUID();
        String token = ModuleMgr.getCenterMgr().getToken();
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("appId", appId);
        hashMap.put("appSecret", appSecret);
        hashMap.put("code", code);
        hashMap.put("uid", uid);
        hashMap.put("token", token);
        mPresenter.getAccessToken(DqUrl.url_get_accessToken, hashMap);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        DqLog.e("YM,数据返回:"+url);
        if (DqUrl.url_sdk_app_info.equals(url)) {//获取other用户app基本信息
            try {
                appInfo = (OpenSdkLoginBean)entity.data;
                txt_appName.setText(appInfo.name);
                GlideUtils.load(this, appInfo.logo, img_logo);
                if (!TextUtils.isEmpty(appInfo.code)){
                    sendAuthCode(appInfo.openid,appInfo.code,"onSuccess",entity.result);
                }
            }catch (Exception e){
                e.printStackTrace();
                MsgMgr.getInstance().sendMsg(KeyValue.SDK_MAIN_FINISH, null);
                finish();
            }
        } else if (DqUrl.url_sdk_accessToken.equals(url)) {//获取accessToken
            try {
                OpenSdkLoginBean data = (OpenSdkLoginBean)entity.data;
                if (data == null)return;
                sendThirdBroadcast(data.access_token, data.openid, data.refresh_token, Integer.parseInt(data.code), "onSuccess");

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(DqUrl.url_sdk_app_code.equals(url)) {
            OpenSdkLoginBean data = (OpenSdkLoginBean)entity.data;
//            requestAccessToken(data.code);
//            DqLog.e("YM,acess_token获取到code:"+data.code);
            DqLog.e("YM------->获取的code:"+data.code);
            appInfo = data;
            sendAuthCode(data.openid,data.code,"onSuccess",entity.result);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        DqUtils.bequit(entity, this);
        if (DqUrl.url_get_accessToken.equals(url)){
            sendThirdBroadcast("", "", "", KeyValue.SDKShare.FAIL, "onSuccess");
        }
    }

    //给第三方返回accessToken
    private void sendThirdBroadcast(String accessToken, String openId, String refreshToken, int code, String msg){
        try {
            Intent intent = new Intent(KeyValue.RECEIVER_ACTION);
            intent.setComponent(new ComponentName(appInfo.packageName, KeyValue.RECEIVER_PATH));
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("openId", openId);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("code", code);
            intent.putExtra("msg", msg);
            intent.putExtra("type", 1);//1登录2分享
            sendBroadcast(intent);
            MsgMgr.getInstance().sendMsg(KeyValue.SDK_MAIN_FINISH, null);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //给第三方返回授权code
    private void sendAuthCode(String openId, String code, String msg, int resultCode){
        try {

            Intent intent1 = new Intent(KeyValue.RECEIVER_ACTION);
            intent1.setComponent(new ComponentName(appInfo.packageName, KeyValue.RECEIVER_PATH));
//            intent1.setComponent(new ComponentName(appInfo.packageName, "com.opensdk.qingchat.utils.ReceiveCNBroadcastReceiver"));
            intent1.putExtra("openId", openId);
            intent1.putExtra("code", code);
            intent1.putExtra("resultCode", resultCode);
            intent1.putExtra("msg", msg);
            intent1.putExtra("type", 1);//1登录2分享
            sendBroadcast(intent1);
            MsgMgr.getInstance().sendMsg(KeyValue.SDK_MAIN_FINISH, null);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }

    @Override
    public void onMessage(String key, Object value) {
        if(MsgType.MT_App_Login.equals(key)) {
            boolean isLogin = (boolean) value;
            if(isLogin) {
                verifyAppId();
            }
        }
    }
}
