package com.wd.daquan.mine.wallet.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.wd.daquan.DqApp
import com.wd.daquan.R
import com.wd.daquan.model.mgr.ModuleMgr
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.common.utils.DqUtils
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.model.rxbus.MsgMgr
import com.wd.daquan.model.rxbus.MsgType
import com.da.library.tools.ActivitysManager
import kotlinx.android.synthetic.main.pay_sms_code_activity.*
import org.json.JSONObject

/**
 * @Author: 方志
 * @Time: 2019/5/25 14:56
 * @Description:支付短信验证码验证
 */
class PaySmsCodeActivity : WalletBaseActivity(){
    private var mEnterType : String? = ""
    private var mVerifyToken : String? = ""
    private var mOrderId : String? = ""
    private var mRandomValue : String? = ""


    override fun initView() {
        mTitleLayout = pay_sms_code_title_layout
        mConfirmTv = pay_sms_code_confirm_tv
        confirmEnabled(false)
    }

    override fun initData() {
        mEnterType = intent?.getStringExtra(KeyValue.ENTER_TYPE)
        mVerifyToken = intent?.getStringExtra(KeyValue.Wallet.VERIFY_TOKEN)
        mOrderId = intent?.getStringExtra(KeyValue.Wallet.OWN_ORDER_ID)
        mPhone = intent?.getStringExtra(KeyValue.Wallet.BIND_MOBILE)
        mRandomValue = intent?.getStringExtra(KeyValue.Wallet.RANDOM_VALUE)

        if (TextUtils.isEmpty(mPhone)){
            mPhone = ModuleMgr.getCenterMgr().walletPhone
        }
        getSmsCode(pay_sms_code_get_code_tv)
        if (!TextUtils.isEmpty(mPhone)){
            val encryptPhone = mPhone.toString().replaceRange(3, 7, "****")
            pay_sms_code_phone_hint_tv.text = String.format(DqApp.getStringById(R.string.please_input_receive_sms_code), encryptPhone)
        }
    }

    override fun setContentView() {
        setContentView(R.layout.pay_sms_code_activity)
    }

    override fun initListener() {
        super.initListener()

//        pay_sms_code_get_code_tv.setOnClickListener {
//            when(mEnterType){
//                KeyValue.Wallet.SMS_CODE_BIND_CARD_INFO ->{
//                    //绑卡
//                    getSmsCode(DqUrl.url_add_bank_card_get_code, pay_sms_code_get_code_tv)
//                }
//                KeyValue.Wallet.SMS_CODE_RECHARGE->{
//                    //充值
//                    getSmsCode(DqUrl.url_recharge_get_code, pay_sms_code_get_code_tv)
//                }
//                KeyValue.Wallet.SMS_CODE_FORGOT_PWD ->{
//                    //忘记密码
//                    getSmsCode(DqUrl.url_forgot_pwd_get_code, pay_sms_code_get_code_tv)
//                }
//            }
//        }

        pay_sms_code_get_code_et.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val length = s.toString().length
                confirmEnabled(length == 6)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        mConfirmTv?.setOnClickListener {
            onConfirmClick()
        }
    }

    private fun onConfirmClick() {

        when(mEnterType){
            KeyValue.Wallet.SMS_CODE_BIND_CARD_INFO ->{
                //绑卡
                val jsonParam =  JSONObject()
                jsonParam.put(KeyValue.Wallet.VERIFY_TOKEN, mVerifyToken.toString())
                jsonParam.put(KeyValue.Wallet.VERIFY_CODE, pay_sms_code_get_code_et.text.toString().trim())
                mPresenter.getValidateCode(DqUrl.url_add_bank_card_get_code, encryptJson(jsonParam))
            }
            KeyValue.Wallet.SMS_CODE_RECHARGE->{
                //充值
                val jsonParam =  JSONObject()
                jsonParam.put(KeyValue.Wallet.VERIFY_TOKEN, mVerifyToken.toString())
                jsonParam.put(KeyValue.Wallet.VERIFY_CODE, pay_sms_code_get_code_et.text.toString().trim())
                jsonParam.put(KeyValue.Wallet.OWN_ORDER_ID, mOrderId.toString())
                mPresenter.getValidateCode(DqUrl.url_recharge_get_code, encryptJson(jsonParam))
            }
            KeyValue.Wallet.SMS_CODE_FORGOT_PWD ->{
                //忘记密码
                val jsonParam = mPresenter.getJsonParam()
                jsonParam.put(KeyValue.Wallet.VERIFY_TOKEN, mVerifyToken)
                jsonParam.put(KeyValue.Wallet.VERIFY_CODE, pay_sms_code_get_code_et.text.toString().trim())
                jsonParam.put(KeyValue.Wallet.RANDOM_VALUE, mRandomValue)
                mPresenter.getValidateCode(DqUrl.url_forgot_pwd_get_code, encryptJson(jsonParam))
            }
        }
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        when(url){
            DqUrl.url_add_bank_card_get_code->{
                MsgMgr.getInstance().sendMsg(MsgType.MT_WALLET_BIND_CARD, true)
                //绑卡
                ActivitysManager.getInstance().finishMore(
                        AddBankCardActivity::class.java,
                        WriteBankCardInfoActivity::class.java,
                        this::class.java)
                DqToast.showShort(entity?.content)
            }
            DqUrl.url_recharge_get_code->{
                MsgMgr.getInstance().sendMsg(MsgType.MT_WALLET_RECHARGE, true)
                DqToast.showShort(entity?.content)
                //充值
                ActivitysManager.getInstance().finishMore(RechargeActivity::class.java, this::class.java)
            }
            DqUrl.url_forgot_pwd_get_code ->{
                DqToast.showShort(entity?.content)
                //忘记密码
                ActivitysManager.getInstance().finishMore(
                        AddBankCardActivity::class.java,
                        ForgotSetPayPwdActivity::class.java,
                        this::class.java)
            }
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        DqUtils.bequit(entity, this)
        finish()
    }
}