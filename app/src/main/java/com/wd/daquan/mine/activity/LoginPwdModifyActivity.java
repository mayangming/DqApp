package com.wd.daquan.mine.activity;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.DqUtils;
import com.da.library.constant.IConstant;
import com.da.library.listener.DialogListener;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.da.library.tools.MD5;

import java.util.LinkedHashMap;
public class LoginPwdModifyActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener{
    private EditText edit_newPwd;
    private EditText edit_newPwdAgain;
    private EditText edit_oldPwd;
    private TextView btn_done;
    private String encryPwd;

    /**
     * 密码错误1-4次dialog监听
     */
    private DialogListener mPasswordErrorListener = new DialogListener() {
        @Override
        public void onCancel() {

        }

        @Override
        public void onOk() {
            // TODO: 2019/1/12 需要修改 ... 
            NavUtils.gotoForgetLoginPasswordActivity(LoginPwdModifyActivity.this);
        }
    };

    private Activity mActivity;
    /**
     * 密码错误提示得dialog
     */
    private Dialog mPasswordErrorDialog = null;

    @Override
    protected void setContentView() {
        setContentView(R.layout.login_pwd_modify_activity);
    }

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        edit_oldPwd= findViewById(R.id.loginPwdModifyEtOldPwd);
        edit_newPwd= findViewById(R.id.loginPwdModifyEtNewPwd);
        edit_newPwdAgain= findViewById(R.id.loginPwdModifyEtNewPwdAgain);
        btn_done= findViewById(R.id.loginPwdModifyTxtConfirm);
    }

    @Override
    public void initListener() {
        toolbarBack();
        btn_done.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginPwdModifyTxtConfirm:
               String etNewpwd = edit_newPwd.getText().toString().trim();
                String etNewpwdAgain = edit_newPwdAgain.getText().toString().trim();
                String oldpwd=edit_oldPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(etNewpwd)&!TextUtils.isEmpty(etNewpwdAgain)&!TextUtils.isEmpty(oldpwd)) {
                    if (etNewpwdAgain.length()>=6&etNewpwd.length()>=6) {
                        if (etNewpwd.equals(etNewpwdAgain)) {
                            //验证手机号，验证码
                            encryPwd = MD5.encrypt(IConstant.Password.PWD_MD5 + etNewpwd, true);
                            String encryOldPwd = MD5.encrypt(IConstant.Password.PWD_MD5 + oldpwd, true);
                            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                            linkedHashMap.put("phone", "");
                            linkedHashMap.put("msg", "");
                            linkedHashMap.put("pwd", encryPwd);
                            linkedHashMap.put("type", KeyValue.ONE_STRING);
                            linkedHashMap.put("old_pwd", encryOldPwd);
                            mPresenter.getSetPwd(DqUrl.url_set_pwd, linkedHashMap);
                        } else {
                            DqToast.showShort(getString(R.string.setting_login_pwd_pwd_inconformity));
                        }
                    }else{
                        DqToast.showShort(getString(R.string.setting_login_pwd_pwd_atleast));
                    }
                }else{
                    DqToast.showShort(getString(R.string.password_is_null));
                }
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        loginError(entity, code);
    }

    /**
     * 原始密码错误
     * @param entity 数据
     */
    private void loginError(DataBean entity, int code) {
        if(KeyValue.RESET_LOGIN_PASSWORD_ERROR_CODE == code) {
            DialogUtils.showNoBindWXDialog(this, "", entity.content, mPasswordErrorListener);
        }else {
            DqUtils.bequit(entity,this);
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_set_pwd.equals(url)) {//设置密码
            ModuleMgr.getCenterMgr().putPwd(encryPwd);
            DqToast.showShort(getString(R.string.setting_login_pwd_pwd_set_success));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mPasswordErrorDialog) {
            mPasswordErrorDialog.dismiss();
            mPasswordErrorDialog = null;
        }

        if(null != mActivity) {
            mActivity = null;
        }
    }
}
