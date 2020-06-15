package com.wd.daquan.mine.wallet.activity

import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.wd.daquan.DqApp
import com.wd.daquan.R
import com.wd.daquan.model.mgr.ModuleMgr
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.common.utils.DqUtils
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.mine.wallet.bean.WalletSatausBean
import com.wd.daquan.mine.wallet.helper.OptionsHelper
import com.wd.daquan.mine.wallet.helper.WalletHelper
import com.da.library.view.PayKeyboardView
import kotlinx.android.synthetic.main.cash_out_activity.*
import java.util.*

/**
 * @Author: 方志
 * @Time: 2019/5/17 11:20
 * @Description:提现页面
 */
class CashOutActivity : WalletBaseActivity(), PayKeyboardView.OnPopWindowClickListener {

    //零钱余额
    private var mBalance : String = "100"
    //今天剩余提现金额
    private var mTodayBalance : String = "1000"
    private var isDeposit : Boolean = false

    override fun setContentView() {
        setContentView(R.layout.cash_out_activity)
    }

    override fun initView() {
        mTitleLayout = cash_out_title_layout
        mConfirmTv = cash_out_confirm_tv
        confirmEnabled(false)
    }

    override fun initData() {
        val city = ModuleMgr.getCenterMgr().city
        if (TextUtils.isEmpty(city)){
            cash_out_area_item.itemTips = DqApp.getStringById(R.string.beijing)
        }else{
            cash_out_area_item.itemTips = city
        }

        mPresenter.getWalletStatus(DqUrl.url_wallet_status, HashMap())
        cash_out_current_amount_tv.text = String.format(DqApp.getStringById(R.string.current_balance_amount), mBalance)
        cash_out_today_balance_tv.text = String.format(DqApp.getStringById(R.string.today_can_be_cash_out), mTodayBalance)
        OptionsHelper.get().initJsonData()

    }

    override fun initListener() {
        super.initListener()
        //提现到银行卡
        cash_out_bank_item.setOnClickListener {
            NavUtils.gotoMyBankCardActivityForResult(this, KeyValue.Wallet.CASH_OUT_CHOICE_BANK_CARD,
                    KeyValue.Wallet.REQUEST_CODE_CASH_OUT_CHOICE_BANK)
        }
        //选择开户行城市
        cash_out_area_item.setOnClickListener {
            OptionsHelper.get().showPickerView(this, object : OptionsHelper.OnCityChoiceListener{
                override fun getCity(province: String, city: String, area : String) {
                    cash_out_area_item.itemTips = city
                    ModuleMgr.getCenterMgr().putCity(city)
                }
            })
        }
        //全部提现
        cash_out_all_amount_tv.setOnClickListener {
            //零钱余额
            if ("0.00" == mBalance){
                DqToast.showShort(DqApp.getStringById(R.string.balance_is_zero_not_cash_out))
            }else{
                cash_out_amount_et.setText(mBalance)
            }
        }

        cash_out_confirm_tv.setOnClickListener {
            DqToast.showShort("确认提现")
            val hashMap = HashMap<String, String>()
            hashMap[KeyValue.Wallet.TOTAL_AMOUNT] =  cash_out_amount_et.text.toString().trim()
            mPresenter.cashOutFee(DqUrl.url_cash_out_fee, hashMap)

//            val amount = DqApp.getStringById(R.string.rd_comm_money_symbol) + DqUtils.moneyformat(text)
//            WalletHelper.get().showPayKeyBoard(this, amount, "提现", this)
        }

        cash_out_amount_et.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                textChange(s)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun textChange(s: Editable?) {
        if (TextUtils.isEmpty(s.toString())) {
            cash_out_all_amount_tv.visibility = View.VISIBLE
            cash_out_current_amount_tv.text = String.format(DqApp.getStringById(R.string.current_balance_amount), mBalance)
            cash_out_current_amount_tv.setTextColor(DqApp.getColorById(R.color.app_txt_999999))
        } else {
            // 输入金额
            val inputAmount = s.toString().toDouble()
            //零钱余额
            val balance = mBalance.toDouble()
            val isNotDeposit = inputAmount < 10.6
            if (isNotDeposit) {
                confirmEnabled(false)
                cash_out_all_amount_tv.visibility = View.GONE
//                cash_out_current_amount_tv.text = "额外扣除0.4% + 0.5/笔手续费"
                cash_out_current_amount_tv.text = String.format(DqApp.getStringById(R.string.extra_deduct_service_fee), "0.4% + 0.5/")
                cash_out_current_amount_tv.setTextColor(Color.RED)
            } else {
                if (inputAmount > balance) {
                    confirmEnabled(false)
                    cash_out_current_amount_tv.text = DqApp.getStringById(R.string.cash_out_amount_is_not_more_than_balance)
                    cash_out_all_amount_tv.visibility = View.GONE
                    cash_out_current_amount_tv.setTextColor(Color.RED)
                } else {
                    confirmEnabled(true)
                    cash_out_all_amount_tv.visibility = View.VISIBLE
                    cash_out_current_amount_tv.text = String.format(DqApp.getStringById(R.string.current_balance_amount), mBalance)
                    cash_out_current_amount_tv.setTextColor(DqApp.getColorById(R.color.app_txt_999999))
                }
            }
        }
    }

//    private fun setCashOutStates(text: String, isDeposit: Boolean) {
//        cash_out_bank_item.itemTips = text
//        confirmEnabled(isDeposit)
//        var isDeposit = isDeposit
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == KeyValue.Wallet.REQUEST_CODE_CASH_OUT_CHOICE_BANK){
            cash_out_bank_item.itemTips = ""
        }
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)
        when(url){
            DqUrl.url_wallet_status ->{
                //获取钱包状态
                //DqToast.showShort(entity?.content)
                val walletBean = entity?.data as? WalletSatausBean
                mBalance = walletBean?.balance!!
                mTodayBalance = walletBean.cashout_amt
                cash_out_current_amount_tv.text = String.format(DqApp.getStringById(R.string.current_balance_amount), mBalance)
                cash_out_today_balance_tv.text = String.format(DqApp.getStringById(R.string.today_can_be_cash_out), mTodayBalance)
            }

            DqUrl.url_cash_out_fee ->{
                //提现计算手续费
               // val cashOutBean = entity?.intentUrl as? CashOutBean
            }
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onFailed(url, code, entity)
        when(url){
            DqUrl.url_wallet_status ->{
                //获取钱包状态
                DqToast.showShort(entity?.content)
//                mBalance = "0.00"
//                mTodayBalance = "0.00"
                cash_out_current_amount_tv.text = String.format(DqApp.getStringById(R.string.current_balance_amount), mBalance)
                cash_out_today_balance_tv.text = String.format(DqApp.getStringById(R.string.today_can_be_cash_out), mTodayBalance)
            }
            DqUrl.url_cash_out_fee ->{
                //提现计算手续费
                WalletHelper.get().showFeeDialog(activity, object : WalletHelper.CashOutFeeDialogListener{

                    override fun ok() {
                        val text = cash_out_amount_et.text.toString().trim()
                        val amount = DqApp.getStringById(R.string.rd_comm_money_symbol) + DqUtils.moneyformat(text)
                        WalletHelper.get().showPayKeyBoard(
                                activity,
                                amount,
                                "提现",
                                this@CashOutActivity
                        )
                    }
                })
            }
        }
    }

    /**
     * 密码输入
     */
    override fun onPopWindowClickListener(psw: String, complete: Boolean) {
        if (complete){
            DqToast.showShort("完成")
            val param = mPresenter.getWalletParam()
            param[KeyValue.Wallet.CARDID] =  "cardid"
            param[KeyValue.Wallet.CITYCODE] =  "citycode"
            param[KeyValue.Wallet.BANKNAME] =  "bankname"
            param[KeyValue.Wallet.PWD_PAY] =  psw
            mPresenter.cashOutCreateOrder(DqUrl.url_cash_out_create_order, param)
        }
    }

}