package com.wd.daquan.mine.wallet.activity

import android.annotation.SuppressLint
import com.wd.daquan.DqApp
import com.wd.daquan.R
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.model.rxbus.MsgMgr
import com.wd.daquan.model.rxbus.QCObserver
import kotlinx.android.synthetic.main.wallet_activity.*

/**
 * @Author: 方志
 * @Time: 2019/5/14 19:56
 * @Description: 我的零钱页面
 */
class WalletActivity : WalletBaseActivity(), QCObserver {


    override fun setContentView() {
        setContentView(R.layout.wallet_activity)
    }

    override fun initView() {
        mTitleLayout = wallet_title_layout
        wallet_title_layout?.title = DqApp.getStringById(R.string.my_money)
        wallet_title_layout?.setTitleLayoutBackgroundColor(DqApp.getColorById(R.color.color_d84e43))
        wallet_title_layout?.setIvBackgroundColor(R.drawable.recharge_btn_selector)
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
//        mWalletBean = intent?.getParcelableExtra(KeyValue.Wallet.WALLET_ENTITY)
//        if (mWalletBean == null){
//            wallet_money_tv?.text = "0.00"
//        }else{
//            wallet_money_tv?.text = mWalletBean?.balance
//        }
//
//        wallet_my_bank_card_item?.itemTips = mWalletBean?.bind_card_nums

//        if (dataSelf.getDefault_Bank_card()!=null) {
//            String cardno=dataSelf.getDefault_Bank_card().getCardno();
//            if(TextUtils.isEmpty(cardno))return;
//            cardBank = dataSelf.getDefault_Bank_card().getCardbank()+"("+cardno.substring(cardno.length()-4,cardno.length())+")"+"::"+dataSelf.getDefault_Bank_card().getId();
//        }
//        payzhi=mWalletBean?.pwd

    }

    override fun initListener() {
        super.initListener()
        wallet_recharge_ll?.setOnClickListener {
            DqToast.showShort("充值")
            NavUtils.gotoRechargeActivity(this)
        }
        wallet_cash_out_ll?.setOnClickListener {
            DqToast.showShort("提现")
            NavUtils.gotoCashOutActivity(this)
        }
        wallet_money_details_item?.setOnClickListener {//零钱明细
            NavUtils.gotoLooseChangeTransationListActivity(this)
        }
        wallet_rdp_details_item?.setOnClickListener {//红包明细
            NavUtils.gotoRedTransationListActivity(this)
        }
        wallet_modify_payment_pwd_item?.setOnClickListener {
            DqToast.showShort("修改支付密码")
            NavUtils.gotoCheckPayPwdActivity(this, KeyValue.Wallet.MODIFY_PWD)
        }
        wallet_forgot_payment_pwd_item?.setOnClickListener {
            DqToast.showShort("忘记支付密码")
//            NavUtils.gotoCheckPayPwdActivity(this, KeyValue.Wallet.FORGOT_PWD)
            NavUtils.gotoAddBankCardActivity(this, KeyValue.Wallet.ADD_CARD_FORGOT_PASSWORD)
        }
        wallet_my_bank_card_item?.setOnClickListener {
            DqToast.showShort("我的银行卡")
            NavUtils.gotoMyBankCardActivity(this, KeyValue.Wallet.MY_BANK_CARD)
        }

        MsgMgr.getInstance().attach(this)
    }

    override fun onMessage(key: String?, value: Any) {
//        when(key){
//
//        }
    }

//    private fun requestBalance() {
//        val hashMap = HashMap<String, String>()
//        mPresenter?.getWalletStatus(DqUrl.url_wallet_status, hashMap)
//    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        if (DqUrl.url_wallet_status == url) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MsgMgr.getInstance().detach(this)
    }
}