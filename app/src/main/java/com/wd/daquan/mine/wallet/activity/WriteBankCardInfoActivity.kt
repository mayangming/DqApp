package com.wd.daquan.mine.wallet.activity

import android.text.TextUtils
import com.wd.daquan.R
import com.wd.daquan.model.mgr.ModuleMgr
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.common.utils.DqUtils
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.mine.wallet.bean.BankCardBean
import com.wd.daquan.mine.wallet.bean.VericationCodeBean
import com.wd.daquan.mine.wallet.helper.OptionsHelper
import kotlinx.android.synthetic.main.write_bank_card_info_activity.*

/**
 * @Author: 方志
 * @Time: 2019/5/17 9:47
 * @Description: 填写银行卡绑定的个人信息
 */
@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class WriteBankCardInfoActivity : WalletBaseActivity(){
    private var mBankCard : BankCardBean? = null
    private var mCardNumber : String? = ""

    override fun setContentView() {
        setContentView(R.layout.write_bank_card_info_activity)
    }

    override fun initView() {
        mTitleLayout = write_bank_card_title_layout
        mConfirmTv = write_bank_card_confirm_tv
        confirmEnabled(false)
        mTitleLayout.setTitleTextLength(10)
    }

    override fun initData() {
        mBankCard = intent?.getParcelableExtra(KeyValue.Wallet.BANK_CARD_BEAN)
        mCardNumber = intent.getStringExtra(KeyValue.Wallet.CARD_NO)

        write_bank_card_bank_name_tv.text = mBankCard?.bank
        write_bank_card_user_name_tv.text = ModuleMgr.getCenterMgr().realName
        write_bank_card_id_number_tv.text = ModuleMgr.getCenterMgr().idNumber

        confirmEnabled(false)
    }

    override fun initListener() {
        super.initListener()
        write_bank_card_check.setOnCheckedChangeListener { buttonView, isChecked ->
            confirmEnabled(isChecked)
        }

        mConfirmTv?.setOnClickListener {
            onConfirm()
        }

        write_bank_card_agreement_tv.setOnClickListener {
            NavUtils.gotoWebviewActivity(this, DqUrl.url_register_agreement, getString(R.string.user_service_agreement))
        }

        setPhoneInput(write_bank_card_phone_et, write_bank_card_clear_phone_iv)

    }

    private fun onConfirm() {
        val bankName = write_bank_card_bank_name_tv.text.toString().trim()
        val name = write_bank_card_user_name_tv.text.toString().trim()
        val idCard = write_bank_card_id_number_tv.text.toString().trim()
        val phone =  replaceSpacing(write_bank_card_phone_et.text.toString().trim())

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

        val jsonObject = mPresenter.getJsonParam()
        jsonObject.put(KeyValue.Wallet.CARD_NO,  mCardNumber.toString())
        jsonObject.put(KeyValue.Wallet.BIND_MOBILE,  phone)

        mPresenter.getVerificationCode(DqUrl.url_add_bank_card_bind_info, encryptJson(jsonObject))
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        if (DqUrl.url_add_bank_card_bind_info == url){
            val codeBean = entity?.data as? VericationCodeBean
            val phone = replaceSpacing(write_bank_card_phone_et.text.toString().trim())
            NavUtils.gotoPaySmsCodeActivity(this, codeBean?.verify_token, phone, KeyValue.Wallet.SMS_CODE_BIND_CARD_INFO)
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        DqUtils.bequit(entity, this)
    }


    override fun onDestroy() {
        super.onDestroy()
        OptionsHelper.get().clear()
    }
}