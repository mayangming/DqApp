package com.wd.daquan.mine.wallet.activity

import android.text.TextUtils
import com.wd.daquan.DqApp
import com.wd.daquan.R
import com.wd.daquan.model.mgr.ModuleMgr
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.mine.wallet.bean.BankCardBean
import com.wd.daquan.mine.wallet.bean.PayPwdKeyBean
import com.wd.daquan.mine.wallet.bean.VericationCodeBean
import com.wd.daquan.mine.wallet.helper.PayPwdHelper
import kotlinx.android.synthetic.main.forgot_set_pay_pwd_activity.*

/**
 * @Author: 方志
 * @Time: 2019/5/25 11:02
 * @Description: 忘记支付密码设置密码
 */
class ForgotSetPayPwdActivity : WalletBaseActivity() {


    private var mBankCard : BankCardBean? = null
    private var mCardNumber : String? = ""


    override fun setContentView(){
        setContentView(R.layout.forgot_set_pay_pwd_activity)
    }

    override fun initView() {
        mTitleLayout = forgot_set_pwd_title_layout
        mConfirmTv = forgot_set_pwd_confirm_tv
        confirmEnabled(false)
        mTitleLayout.setTitleTextLength(10)
    }

    override fun initData() {
        mBankCard = intent?.getParcelableExtra(KeyValue.Wallet.BANK_CARD_BEAN)
        mCardNumber = intent.getStringExtra(KeyValue.Wallet.CARD_NO)

        forgot_set_pwd_bank_name_tv.text = mBankCard?.bank
        forgot_set_pwd_user_name_tv.text = ModuleMgr.getCenterMgr().realName
        forgot_set_pwd_id_number_tv.text = ModuleMgr.getCenterMgr().idNumber

        confirmEnabled(false)

        PayPwdHelper.get().initKey(object : PayPwdHelper.PayPwdListener {
            override fun onSuccess(data: PayPwdKeyBean?) {
//                PayPwdHelper.get().initPassGuard(forgot_set_pwd_et, intentUrl)
//                PayPwdHelper.get().initPassGuard(forgot_set_pwd_again_et, intentUrl)
            }
        })

    }

    override fun initListener() {
        super.initListener()
        setPhoneInput(forgot_set_pwd_phone_et, forgot_set_pwd_clear_phone_iv)
        setPwdInput(forgot_set_pwd_et, forgot_set_pwd_again_et)
        forgot_set_pwd_confirm_tv.setOnClickListener {
            requestData()
        }
    }


    fun requestData(){
        val bankName = forgot_set_pwd_bank_name_tv.text.toString().trim()
        val name = forgot_set_pwd_user_name_tv.text.toString().trim()
        val idCard = forgot_set_pwd_id_number_tv.text.toString().trim()
        val phone = replaceSpacing(forgot_set_pwd_phone_et.text.toString().trim())
        val pwd =  forgot_set_pwd_et.text.toString().trim()
        val pwdAgain =  forgot_set_pwd_again_et.text.toString().trim()

//        val encryptPwd =  forgot_set_pwd_et.rsaaesCiphertext
        val encryptPwd =  forgot_set_pwd_et.text.toString().trim()

        if (TextUtils.isEmpty(bankName)) {
            DqToast.showShort("银行名称不能为空")
            return
        }
        if (TextUtils.isEmpty(name)) {
            DqToast.showShort("姓名不能为空")
            return
        }
        if (TextUtils.isEmpty(idCard)) {
            DqToast.showShort("身份证号不能为空")
            return
        }
        if (TextUtils.isEmpty(phone)) {
            DqToast.showShort("手机号不能为空")
            return
        }
        if (pwd != pwdAgain) {
            DqToast.showShort(DqApp.getStringById(R.string.input_pay_pwd_diff))
            return
        }

        val jsonParam = mPresenter.getJsonParam()
        jsonParam.put(KeyValue.Wallet.CARD_NO, mCardNumber.toString())
        jsonParam.put(KeyValue.Wallet.BIND_MOBILE, phone)
        jsonParam.put(KeyValue.Wallet.PWD_PAY, encryptPwd)
        jsonParam.put(KeyValue.Wallet.RANDOM_VALUE,  PayPwdHelper.get().getRandomValue())

        mPresenter.getVerificationCode(DqUrl.url_forgot_pwd_bind_info, encryptJson(jsonParam))
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        if (DqUrl.url_forgot_pwd_bind_info == url){
            val codeBean = entity?.data as? VericationCodeBean
            val phone =  replaceSpacing(forgot_set_pwd_phone_et.text.toString().trim())
            NavUtils.gotoPaySmsCodeActivity(this, codeBean?.verify_token, phone, KeyValue.Wallet.SMS_CODE_FORGOT_PWD)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PayPwdHelper.get().onDestroy()
    }

}