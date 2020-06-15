package com.wd.daquan.login.activity;

import android.Manifest;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.common.utils.NavUtils;
import com.da.library.constant.IConstant;
import com.wd.daquan.glide.GlideUtils;
import com.da.library.listener.DialogListener;
import com.da.library.listener.ICommListDialogListener;
import com.wd.daquan.login.helper.CommDialogHelper;
import com.da.library.widget.CommonListDialog;
import com.netease.nim.uikit.support.permission.MPermission;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 方志
 * @Time: 2018/9/11 15:36
 * @Description: 密码快捷登录
 */
public class LoginPasswordQuickActivity extends BaseLoginActivity{

    private TextView mPhoneNumberTv;
    private EditText mPasswordEt;
    private Button mLoginBtn;
    private TextView mLoginCodeTv;
    private ImageView mPortraitTv;
    private TextView mForgetPasswordTv;
    private TextView mLoginMoreTv;
    private CommonListDialog mListDialog;
    private View mBackIv;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_login_pwd_again);
    }

    @Override
    protected void init() {
        requestBasicPermission();
    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.back_iv);
        mScrollView = findViewById(R.id.scroll_view);
        mTranslationContainerRl = findViewById(R.id.translation_container_rl);
        mPortraitTv = findViewById(R.id.login_again_portrait_iv);
        mPhoneNumberTv = findViewById(R.id.login_again_phone_number_tv);
        mPasswordEt = findViewById(R.id.login_again_password_again_et);
        mLoginBtn = findViewById(R.id.login_again_password_btn);
        mLoginCodeTv = findViewById(R.id.login_again_code_tv);
        mForgetPasswordTv = findViewById(R.id.login_again_forget_password_tv);
        mLoginMoreTv = findViewById(R.id.login_again_more_tv);
    }

    @Override
    protected void initData() {
        super.initData();
        String phone = mUserInfoSp.getString(EBSharedPrefUser.phone, "");
        String portrait = mUserInfoSp.getString(EBSharedPrefUser.headpic, "");
        if (!TextUtils.isEmpty(phone)) {
            mPhoneNumberTv.setText(phone);
        }
        GlideUtils.loadHeader(this, portrait, mPortraitTv);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBackIv.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mLoginCodeTv.setOnClickListener(this);
        mForgetPasswordTv.setOnClickListener(this);
        mLoginMoreTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.login_again_password_btn:
                //登录
                login(mPhoneNumberTv, mPasswordEt, "1");
                break;
            case R.id.login_again_code_tv:
                //QcToastUtil.showToast(this, "使用验证码登录");
                NavUtils.gotoLoginCodeAgainActivity(this);
                finish();
                break;
            case R.id.login_again_forget_password_tv:
//                QcToastUtil.showToast(this, "忘记密码");
                NavUtils.gotoForgetLoginPasswordActivity(this);
                break;
            case R.id.login_again_more_tv:
                showListDialog();
                break;
        }
    }


    private void showListDialog() {
        mListDialog = new CommonListDialog(this);
        List<String> items = new ArrayList<>();
        items.add(getString(R.string.switch_account));
        items.add(getString(R.string.register));
        //items.add(getString(R.string.security_center));

        mListDialog.setItems(items);
        mListDialog.show();

        mListDialog.setListener(mListDialogListener);
    }


    /**
     * 基本权限管理
     */

    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private void requestBasicPermission() {
        MPermission.with(this)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    /**
     * 更多dialog监听事件
     */
    private ICommListDialogListener mListDialogListener = (item, position) -> {
        switch (position) {
            case 0://切换账号
                //Toast.makeText(LoginPasswordQuickActivity.this, item, Toast.LENGTH_SHORT).show();
                NavUtils.gotoLoginPasswordActivity(this);
                break;
            case 1:
//                Toast.makeText(LoginPasswordQuickActivity.this, item, Toast.LENGTH_SHORT).show();
                NavUtils.gotoRegisterActivity(this);
                break;
            //case 2:
            //   Toast.makeText(LoginPasswordQuickActivity.this, item, Toast.LENGTH_SHORT).show();//安全中心
            //   break;
        }
    };


    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        switch (code) {
            case IConstant.Code.LOGIN_PASSWORD_ERROR_CODE:
                CommDialogHelper.getInstance().showPasswordErrorDialog(this, entity.content)
                        .setDialogListener(new DialogListener() {
                            @Override
                            public void onCancel() {
                                //login(mPhoneNumberTv, mPasswordEt, "1");
                                mPasswordEt.setText("");
                            }

                            @Override
                            public void onOk() {
                                NavUtils.gotoForgetLoginPasswordActivity(getActivity());
                            }
                        });
                break;
            case IConstant.Code.LOGIN_PASSWORD_ERROR_MAX_CODE:
                CommDialogHelper.getInstance().showPasswordErrorMaxDialog(this, entity.content)
                        .setDialogListener(new DialogListener() {
                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onOk() {
                                NavUtils.gotoLoginCodeAgainActivity(getActivity());
                            }
                        });
                break;
            default:
                DqUtils.bequit(entity, this);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mListDialog) {
            mListDialog.dismiss();
            mListDialog = null;
        }
        CommDialogHelper.getInstance().onDestroy();
    }
}
