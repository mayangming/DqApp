package com.wd.daquan.login.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.da.library.constant.IConstant;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.login.helper.LoginHelper;
import com.wd.daquan.login.helper.WXLoginHelper;
import com.wd.daquan.login.listener.WXLoginListener;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.LoginBean;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/7 10:38
 * @Description: 登录注册，第三方登录选择页
 */
public class LoginRegisterActivity extends BaseLoginActivity implements WXLoginListener, QCObserver {

    private Button mPhoneLoginBtn;
    private Button mRegisterBtn;
    private ImageView mWechatLoginIv;
    private Map<String, String> mWxMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_login_regist);
    }

    @Override
    protected void initView() {
        mPhoneLoginBtn = findViewById(R.id.phone_login_btn);
        mRegisterBtn = findViewById(R.id.register_btn);
        mWechatLoginIv = findViewById(R.id.wechat_login_iv);
    }

    @Override
    protected void initData() {
        MsgMgr.getInstance().attach(this);
    }

    @Override
    protected void initListener() {
        mPhoneLoginBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        mWechatLoginIv.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        UMShareAPI.get(this).release();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone_login_btn:
                EBSharedPrefUser userInfoSp = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo();
                String phone = userInfoSp.getString(EBSharedPrefUser.phone, "");
                String password = userInfoSp.getString(EBSharedPrefUser.password, "");
                if (TextUtils.isEmpty(phone)) {
                    // 密码登录
                    NavUtils.gotoLoginPasswordActivity(this);
                } else {
                    if (!TextUtils.isEmpty(password)) {
                        // 密码确认登录
//                        startActivity(new Intent(this, LoginPwdAgainActivity.class));
                        NavUtils.gotoLoginPwdAgainActivity(this);
                    } else {
                        // 验证码登录
                        NavUtils.gotoLoginCodeActivity(this, null);
                    }
                }

                break;
            case R.id.register_btn:
                NavUtils.gotoRegisterActivity(LoginRegisterActivity.this);
                break;
            case R.id.wechat_login_iv:
                WXLoginHelper.umWxLogin(this, this);
                break;
        }
    }

    /**
     * 微信登录
     */
    @Override
    public void loginWX(Map<String, String> map) {
        String openId = map.get(IConstant.WX.OPENID);
        String accessToken = map.get(IConstant.WX.ACCESS_TOKEN);
        mWxMap = map;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.WX.ACCESS_TOKEN, accessToken);
        hashMap.put(IConstant.WX.OPENID, openId);
        //Log.e("fz", "hashMap : " + hashMap.toString());
        mPresenter.loginPassword(DqUrl.url_oauth_getUserInfo, hashMap);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if(null == entity) return;

        if(IConstant.OK.equals(entity.status)) {
            if(DqUrl.url_oauth_getUserInfo.equals(url)) {
                if(null == entity.data) {
                    NavUtils.gotoBindPhoneActivity(this, mWxMap);
                }else {
                    LoginBean loginBean = (LoginBean) entity.data;
                    Log.e("YM","微信登录LoginRegister:"+loginBean.password);
//                    ViewModelProviders.of(this).get(ApplicationViewModel.class).initRoomDataBase(loginBean.uid);
                    LoginHelper.saveCurrentUserInfo(loginBean, loginBean.password);
                    LoginHelper.login(this, loginBean.uid, loginBean.imToken);
                }
            }
        }
    }

    @Override
    public void onMessage(String key, Object value) {
        if(KeyValue.SDK_MAIN_FINISH.equals(key)){
//            int flag = (int)value;
//            if(flag == 1002){
            finish();
//            }
        }
    }
}
