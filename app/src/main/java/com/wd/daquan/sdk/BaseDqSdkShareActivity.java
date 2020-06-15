package com.wd.daquan.sdk;

import android.text.TextUtils;
import android.util.Log;

import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.sdk.bean.SdkShareBean;

/**
 * sdk分享基类
 */
public abstract class BaseDqSdkShareActivity extends DqBaseActivity<SdkPresenter, DataBean> {


    protected OpenSdkHelper mSdkHelper;
    /**
     * 需要分享的数据
     */
    protected SdkShareBean mShareBean;

    @Override
    protected SdkPresenter createPresenter() {
        return new SdkPresenter();
    }

    @Override
    protected void init() {

    }


    @Override
    protected void initData() {
        String data = getIntent().getStringExtra(SdkShareBean.SDK_SHARE_DATA);
        Log.e("YM","获取的sdk参数:"+data);
        if(data != null) {
            mShareBean = GsonUtils.fromJson(data, SdkShareBean.class);
        }else {
            mShareBean = getIntent().getParcelableExtra(SdkShareBean.SDK_SHARE_BEAN);
        }
        Log.e("YM","接受到的appID:"+mShareBean.appId);
        if (mShareBean == null){
            return;
        }
        mSdkHelper = new OpenSdkHelper(this);

        String uid = ModuleMgr.getCenterMgr().getUID();
        if(TextUtils.isEmpty(uid)){
            //传给SDK登录
            NavUtils.gotoLoginPasswordActivity(this, KeyValue.SDKLogin.SDK_LOGIN_KEY);
        }
    }
}
