package com.wd.daquan.mine.wallet.activity

import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import com.wd.daquan.DqApp
import com.wd.daquan.R
import com.wd.daquan.model.mgr.ModuleMgr
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.common.utils.DqUtils
import com.wd.daquan.mine.wallet.bean.RechargeBean
import com.wd.daquan.mine.wallet.helper.MoneyInputFilter
import com.wd.daquan.mine.wallet.helper.WalletHelper
import com.da.library.view.PayKeyboardView
import kotlinx.android.synthetic.main.recharge_activity.*

/**
 * @Author: 方志
 * @Time: 2019/5/17 11:16
 * @Description: 充值页面
 */
class RechargeActivity : WalletBaseActivity(), PayKeyboardView.OnPopWindowClickListener {
    private var mMaxRechargeAmount : Int = 0

    override fun setContentView() {
        setContentView(R.layout.recharge_activity)
    }

    override fun initView() {
        mTitleLayout = recharge_title_layout
        mConfirmTv = recharge_confirm_tv
        confirmEnabled(false)
    }

    override fun initData() {

        recharge_current_amount_tv.text = String.format(DqApp.getStringById(R.string.current_balance_amount), "0.00")
    }


    override fun initListener() {
        super.initListener()
        //选择金额
        choiceAmount()
        //选择银行卡
        recharge_bank_item.setOnClickListener {
            NavUtils.gotoMyBankCardActivityForResult(this, KeyValue.Wallet.RECHARGE_CHOICE_BANK_CARD,
                    KeyValue.Wallet.REQUEST_CODE_RECHARGE_CHOICE_BANK)
        }
        //充值金额过滤
        recharge_amount_et.filters = arrayOf<InputFilter>(MoneyInputFilter(mMaxRechargeAmount))

        recharge_amount_et.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s.toString())){
                    confirmEnabled(false)
                }else{
                    val inputAmount = s.toString().toDouble()
                    confirmEnabled(inputAmount >= 10)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        //充值
        recharge_confirm_tv.setOnClickListener {
            val text = recharge_amount_et.text.toString().trim()
            val amount = DqApp.getStringById(R.string.rd_comm_money_symbol) + DqUtils.moneyformat(text)
            WalletHelper.get().showPayKeyBoard(this, amount, "充值", this)
        }
    }

    /**
     * 上一次点击金额的角标
     */
    private var index : Int = 0
    private fun choiceAmount() {
        val amountTvs = arrayOf(recharge_ten_yuan, recharge_fifty_yuan, recharge_one_hundred_yuan,
                recharge_five_hundred_yuan, recharge_one_thousand_yuan, recharge_other_amount)

        for (textView in amountTvs) {

            textView.setOnClickListener {
                val oldTv = amountTvs[index]
                oldTv?.isSelected = false
                textView.isSelected = true
                if (textView == amountTvs.last()){
                    recharge_amount_et.setText("")
                }else{
                    val text = textView.text.toString()
                    val amount = text.substring(0, text.length - 1)
                    recharge_amount_et.setText(amount)
                }

                index = amountTvs.indexOf(textView)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == KeyValue.Wallet.REQUEST_CODE_RECHARGE_CHOICE_BANK){
            recharge_bank_item.itemTips = ""
        }
    }

    override fun onPopWindowClickListener(psw: String?, complete: Boolean) {
        Log.e("TAG", "psw : $psw , complete : $complete")

        if(complete){
            val amount = recharge_amount_et.text.toString().trim()

            val jsonParam = mPresenter.getJsonParam()
            jsonParam.put("cardid", "银行卡号")
            jsonParam.put("money_order", amount)
            mPresenter.createRechargeOrder(DqUrl.url_recharge_create_order, encryptJson(jsonParam))
        }
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        if (DqUrl.url_recharge_create_order == url){
           // val rechargeBean = entity?.intentUrl as? RechargeBean

        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        if (DqUrl.url_recharge_create_order == url){
            val rechargeBean = entity?.data as? RechargeBean
            val phone = ModuleMgr.getCenterMgr().walletPhone
            NavUtils.gotoPaySmsCodeActivity(this, rechargeBean?.verify_token, phone,
                    rechargeBean?.own_order_id, KeyValue.Wallet.SMS_CODE_RECHARGE)
        }
    }
}