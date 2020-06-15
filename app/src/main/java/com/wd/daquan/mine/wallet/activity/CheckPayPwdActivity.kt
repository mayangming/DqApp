package com.wd.daquan.mine.wallet.activity

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.wd.daquan.DqApp
import com.wd.daquan.R
import com.wd.daquan.model.mgr.ModuleMgr
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.common.utils.DqUtils
import com.wd.daquan.mine.wallet.helper.WalletHelper
import com.netease.nim.uikit.common.util.string.StringUtil
import kotlinx.android.synthetic.main.check_pay_pwd_activity.*


/**
 * @Author: 方志
 * @Time: 2019/5/15 11:51
 * @Description: 修改支付密码检查原密码 ， 或忘记支付密码，短信验证
 *              备注：连连支付忘记密码不走此处
 */
class CheckPayPwdActivity : WalletBaseActivity() {

    private var mEnterType: String? = ""//1修改支付密码，2忘记支付密码

    override fun setContentView() {
        setContentView(R.layout.check_pay_pwd_activity)
    }

    override fun initView() {
        mTitleLayout = check_pay_pwd_title_layout
    }

    override fun initData() {
        mEnterType = intent?.getStringExtra(KeyValue.ENTER_TYPE)

        if (KeyValue.Wallet.MODIFY_PWD == mEnterType){
            mTitleLayout.title = DqApp.getStringById(R.string.modify_payment_password)
            check_pay_pwd_origin_pwd_ll?.visibility = View.VISIBLE
            check_pay_pwd_forgot_pwd_ll?.visibility = View.GONE
        }else if(KeyValue.Wallet.FORGOT_PWD == mEnterType){
            mTitleLayout.title = DqApp.getStringById(R.string.forgot_payment_password)
            check_pay_pwd_origin_pwd_ll?.visibility = View.GONE
            check_pay_pwd_forgot_pwd_ll?.visibility = View.VISIBLE

            val phoneNumber = ModuleMgr.getCenterMgr().phoneNumber
            val phoneHint = "请输入手机" + phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, 11) + "收到的短信验证码"
            check_pay_pwd_phone_number_tv?.text = phoneHint
        }
    }

    override fun initListener() {
        super.initListener()
        if (KeyValue.Wallet.MODIFY_PWD == mEnterType){
            //原支付密码输入监听
            check_pay_pwd_input_view?.addTextChangedListener(mTextWatcher)
        }

        if (KeyValue.Wallet.FORGOT_PWD == mEnterType){
            //获取验证码
            check_pay_pwd_get_code_tv?.setOnClickListener{
               // getSmsCode(ModuleMgr.getCenterMgr().phoneNumber,check_pay_pwd_get_code_tv)
            }
            //确认验证码
            check_pay_pwd_confirm_tv?.setOnClickListener {
                onConfirm()
            }
        }

    }

    //确认信息
    private fun onConfirm() {
//        val name = check_pay_pwd_name_et?.text.toString().trim()
//        val idCard = check_pay_pwd_id_card_et?.text.toString().trim()
//        val phone = check_pay_pwd_phone_et?.text.toString().trim()
//        val smsCode = check_pay_pwd_get_code_et?.text.toString().trim()

//        if (TextUtils.isEmpty(name)) {
//            DqToast.showShort("姓名不能为空")
//            return
//        }
//        if (TextUtils.isEmpty(idCard)) {
//            DqToast.showShort("身份证号不能为空")
//            return
//        }
//        if (TextUtils.isEmpty(phone)) {
//            DqToast.showShort("手机号不能为空")
//            return
//        }
//        if (TextUtils.isEmpty(smsCode)) {
//            DqToast.showShort("验证码不能为空")
//            return
//        }
//        val hashMap = HashMap<String, String>()
//        hashMap[KeyValue.Wallet.MSG_CODE] = smsCode
//        mPresenter.getValidateCode(DqUrl.url_validate, hashMap)
    }


    private val mTextWatcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            val inputPwd = check_pay_pwd_input_view?.pwd
            if (inputPwd?.length == 6) {
                val jsonParam = mPresenter.getJsonParam()
                jsonParam.put(KeyValue.Wallet.PWD_PAY,  StringUtil.getPwdData(inputPwd))
                jsonParam.put(KeyValue.Wallet.FLAG_CHECK, KeyValue.ZERO_STRING)
                mPresenter.getVerificationCode(DqUrl.url_check_pay_pwd, encryptJson(jsonParam))
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)
        when (url) {
            DqUrl.url_check_pay_pwd -> {
                //检验原支付密码
                val pwd = check_pay_pwd_input_view?.text.toString()
                NavUtils.gotoSetPayPwdActivity(this, pwd, "1")
            }
//            DqUrl.url_validate -> {
//                //确认短信验证码
//                val smsCode = check_pay_pwd_get_code_et?.text.toString().trim()
//                NavUtils.gotoSetPayPwdActivity(this, smsCode, "2")
//            }
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        if (DqUrl.url_check_pay_pwd == url) {
            //检验原支付密码
            check_pay_pwd_input_view?.clear()
            pwdError(code, entity)
        } else {
            DqUtils.bequitRedPay(this, code, entity?.content)
        }
        Log.e("TAG", "onFailed code : $code")
    }

    private fun pwdError(code: Int, entity: DataBean<Any>?) {
        WalletHelper.get().showPwdErrDialog(this, entity?.content)
        if (KeyValue.CHECK_PAY_PASSWORD_ERROR_CODE == code) {
            //检验原支付密码
            WalletHelper.get().showPwdErrDialog(this, entity?.content)
        } else {
            DqUtils.bequit(entity, this)
        }
    }

}