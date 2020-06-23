package com.wd.daquan.login.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.da.library.tools.ActivitysManager;
import com.da.library.tools.DensityUtil;
import com.da.library.tools.MD5;
import com.da.library.widget.CommTitle;
import com.da.library.widget.CountDownTimerUtils;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.login.helper.LoginHelper;
import com.wd.daquan.login.presenter.LoginPresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.LoginBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/27 19:09
 * @Description:
 */
public class BaseLoginActivity extends DqBaseActivity<LoginPresenter, DataBean> implements View.OnClickListener {

    protected CommTitle mTitleLayout;
    protected EditText mPhoneNumberEt;
    protected EBSharedPrefUser mUserInfoSp;
    protected TextView mGetCodeTv;
    protected NestedScrollView mScrollView;
    protected RelativeLayout mTranslationContainerRl;
    protected int keyHeight;
    private String encryptPassword;
    protected ImageView mClearPhoneIv;
    protected ImageView mClearPwdIv;
    protected EditText mPasswordEt;
    protected boolean isScrollLayout = true;
    private CountDownTimerUtils countDownTimerUtils;
    /**
     * 登录，注册，或确认按钮
     */
    protected Button mConfirmBtn;
    protected String mSdkLogin = null;

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setStatusBarColor(getResources().getColor(R.color.app_page_bg));
//        super.onCreate(savedInstanceState);
//    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void setContentView() {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mSdkLogin = getIntent().getStringExtra(KeyValue.SDKLogin.SDK_LOGIN_KEY);
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        keyHeight = screenHeight / 3;//弹起高度为屏幕高度的1/3

        mUserInfoSp = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo();
        if (null != mTitleLayout) {
            mTitleLayout.setTitleTextColor(getResources().getColor(R.color.text_blue));
        }
    }

    @Override
    protected void initListener() {
        if (null != mTitleLayout) {
            mTitleLayout.getLeftIv().setOnClickListener(this);
        }

        if(mConfirmBtn != null) {
            mConfirmBtn.setOnClickListener(this);
        }

        if (null != mPhoneNumberEt) {
            mPhoneNumberEt.addTextChangedListener(mPhoneTextWatcher);
        }

        if(mPasswordEt != null) {
            mPasswordEt.addTextChangedListener(mPwdTextWatcher);
        }


        if(mClearPhoneIv != null && mPhoneNumberEt != null) {
            mClearPhoneIv.setOnClickListener(v -> mPhoneNumberEt.setText(""));
        }

        if(mClearPwdIv != null && mPasswordEt != null) {
            mClearPwdIv.setOnClickListener(v -> mPasswordEt.setText(""));
        }

        if(isScrollLayout) {
            translationAnimation();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void translationAnimation() {

        if (mScrollView != null) {
            mScrollView.setNestedScrollingEnabled(false);
            mScrollView.addOnLayoutChangeListener(mLayoutChangeListener);
        }
    }


    @Override
    public void onClick(View v) {
        if (null != mTitleLayout) {
            if (v.getId() == mTitleLayout.getLeftIvId()) {
                ActivitysManager.getInstance().finish(this);
            }
        }
    }

    /**
     * 获取验证码
     */
    protected void getVerificationCode(TextView textView, String type,String captcha) {
        if (null != textView) {
            String number = textView.getText().toString().replace(" ", "");
            if (DqUtils.validatePhoneNumber(number)) {
                String key = IConstant.Login.CHATDQ + number;
                String token_key = MD5.encrypt(key).toLowerCase();
                //Log.e("fz", "key : " + key + "token_key ： " + token_key);

                Map<String, String> hashMap = new HashMap<>();
                hashMap.put(IConstant.Login.PHONE, number);
                if (!TextUtils.isEmpty(type)) {
                    hashMap.put(IConstant.Login.TYPE, type);
                }
                hashMap.put(IConstant.Login.TOKEN_KEY, token_key);
                hashMap.put(IConstant.Login.CAPTCHA, captcha);
                countDownTimerUtils = new CountDownTimerUtils(mGetCodeTv, IConstant.TIME_60000,
                        IConstant.TIME_1000, this);
                countDownTimerUtils.start();
                mPresenter.getVerificationCode(DqUrl.url_get_phone_msg, hashMap);
            }
        }
    }

    /**
     * 登录
     *
     * @param accountView  账号
     * @param passwordView 密码
     * @param type         账号登录或密码登录
     */
    protected void login(TextView accountView, TextView passwordView, String type) {
        if (TextUtils.isEmpty(mUserInfoSp.getString(EBSharedPrefUser.DEVICE_ID, ""))) {
            DqUtils.device();
        }

        if (null != accountView && null != passwordView) {
            String phoneNumber = accountView.getText().toString().replace(" ", "");
            String password = passwordView.getText().toString().trim();

            if (TextUtils.isEmpty(phoneNumber)) {
                DqToast.showShort(getString(R.string.mobile_phone_number_cannot_be_empty));
                return;
            }

            String device_id = mUserInfoSp.getString(EBSharedPrefUser.DEVICE_ID, "");
            if (TextUtils.isEmpty(device_id)) {
                device_id = DqUtils.getDeviceId();
            }

            Map<String, String> hashMap = new HashMap<>();
            hashMap.put(IConstant.Login.PHONE, phoneNumber);
            hashMap.put(IConstant.Login.IMEI, device_id);
            hashMap.put(IConstant.Login.TYPE, type);

            if ("1".equals(type)) {
                if (TextUtils.isEmpty(password)) {
                    DqToast.showShort(getString(R.string.password_is_null));
                    return;
                }
                encryptPassword = MD5.encrypt((IConstant.Password.PWD_MD5 + password), true);
                hashMap.put(IConstant.Login.MSG, encryptPassword);
                mPresenter.loginPassword(DqUrl.url_pwd_login, hashMap);
            } else {
                if (TextUtils.isEmpty(password)) {
                    DqToast.showShort(getString(R.string.verification_code_cannot_be_empty));
                    return;
                }
                hashMap.put(IConstant.Login.MSG, password);
                mPresenter.loginPassword(DqUrl.url_msg_login, hashMap);
            }
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (null == entity) return;

        //密码登录
        if (DqUrl.url_pwd_login.equals(url)) {

            LoginBean loginBean = (LoginBean) entity.data;
            LoginHelper.saveCurrentUserInfo(loginBean, encryptPassword);
//            ViewModelProviders.of(this).get(ApplicationViewModel.class).initRoomDataBase(loginBean.uid);
            if(TextUtils.isEmpty(mSdkLogin)) {
                LoginHelper.login(this, loginBean.uid, loginBean.imToken);
            }else{
                LoginHelper.gotoMain(this);
                finish();
            }
        } else if (DqUrl.url_msg_login.equals(url)) {
            //验证码登录
            LoginBean loginBean = (LoginBean) entity.data;
            LoginHelper.saveCurrentUserInfo(loginBean, "");
//            ViewModelProviders.of(this).get(ApplicationViewModel.class).initRoomDataBase(loginBean.uid);
            if(TextUtils.isEmpty(mSdkLogin)) {
                LoginHelper.login(this, loginBean.uid, loginBean.imToken);
            }else{
                LoginHelper.gotoMain(this);
                finish();
            }
        } else if (DqUrl.url_get_phone_msg.equals(url)) {
            if (null != mGetCodeTv) {
                if (entity.result != 0){
                    countDownTimerUtils.cancel();
                    countDownTimerUtils.onFinish();
                }
                DqToast.showShort(entity.content);
            }
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if (null == entity) return;
        if (DqUrl.url_get_phone_msg.equals(url)) {
            if (entity.result != 0){
                countDownTimerUtils.cancel();
                countDownTimerUtils.onFinish();
            }
        }
        if (code == IConstant.Code.LOGIN_PASSWORD_ERROR_CODE || code == IConstant.Code.LOGIN_PASSWORD_ERROR_MAX_CODE) {
            return;
        }
        DqUtils.bequit(entity, this);
    }

    /**
     * 手机号输入监听
     */
    private TextWatcher mPhoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (null != mPhoneNumberEt) {
                String contents = s.toString();
                int length = contents.length();
                if (length == 4) {
                    if (contents.substring(3).equals(" ")) { // -
                        contents = contents.substring(0, 3);
                        mPhoneNumberEt.setText(contents);
                        mPhoneNumberEt.setSelection(contents.length());
                    } else { // +
                        contents = contents.substring(0, 3) + " " + contents.substring(3);
                        mPhoneNumberEt.setText(contents);
                        mPhoneNumberEt.setSelection(contents.length());
                    }
                } else if (length == 9) {
                    if (contents.substring(8).equals(" ")) { // -
                        contents = contents.substring(0, 8);
                        mPhoneNumberEt.setText(contents);
                        mPhoneNumberEt.setSelection(contents.length());
                    } else {// +
                        contents = contents.substring(0, 8) + " " + contents.substring(8);
                        mPhoneNumberEt.setText(contents);
                        mPhoneNumberEt.setSelection(contents.length());
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(mClearPhoneIv != null) {
                mClearPhoneIv.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
            setConfirmBtnState();
        }
    };

    private void setConfirmBtnState(){
        if(mConfirmBtn != null ) {
            if(mPhoneNumberEt != null && mPasswordEt != null) {
                String phone = mPhoneNumberEt.getText().toString().trim();
                String pwd = mPasswordEt.getText().toString().trim();
                if(phone.length() > 5 && pwd.length() > 3) {
                    mConfirmBtn.setEnabled(true);
                }else {
                    mConfirmBtn.setEnabled(false);
                }
            }else if(mPasswordEt != null) {
                String pwd = mPasswordEt.getText().toString().trim();
                mConfirmBtn.setEnabled(pwd.length() > 3);
            }
        }
    }

    /**
     * 登录密码或验证码输入监听
     */
    private TextWatcher mPwdTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            if(mClearPwdIv != null) {
                mClearPwdIv.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
            setConfirmBtnState();
        }
    };


    private View.OnLayoutChangeListener mLayoutChangeListener = (v, left, top, right, bottom,
                                                                 oldLeft, oldTop, oldRight, oldBottom) -> {
        if (mTranslationContainerRl != null) {
                  /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
                  现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
            if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                int dist = mTranslationContainerRl.getBottom() - bottom;
                if (dist > 0) {
                    int translationY = DensityUtil.dip2px(getActivity(), 10);
                    ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat
                            (mTranslationContainerRl, "translationY", 0.0f, - (dist + translationY));
                    mAnimatorTranslateY.setDuration(300);
                    mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                    mAnimatorTranslateY.start();
                }

            } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                if ((mTranslationContainerRl.getBottom() - oldBottom) > 0) {
                    ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mTranslationContainerRl,
                            "translationY", mTranslationContainerRl.getTranslationY(), 0);
                    mAnimatorTranslateY.setDuration(300);
                    mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                    mAnimatorTranslateY.start();
                }
            }
        }
    };

    protected String format(String phoneNum) {
        int len = phoneNum.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(phoneNum.charAt(i));
            if (i == 2) {
                sb.append(" ");
            }
            if (i == 6) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
