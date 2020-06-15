package com.wd.daquan.mine.wallet.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.wd.daquan.DqApp
import com.wd.daquan.R
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.model.rxbus.MsgMgr
import com.wd.daquan.model.rxbus.MsgType
import com.wd.daquan.model.rxbus.QCObserver
import com.wd.daquan.mine.wallet.adapter.MyBankCardAdapter
import com.wd.daquan.mine.wallet.bean.BankCardBean
import com.wd.daquan.mine.wallet.helper.WalletHelper
import com.wd.daquan.model.utils.GsonUtils
import com.da.library.view.PayKeyboardView
import com.da.library.widget.CommonListDialog
import com.netease.nim.uikit.common.util.string.StringUtil
import kotlinx.android.synthetic.main.my_bank_card_activity.*
import org.json.JSONObject

/**
 * @Author: 方志
 * @Time: 2019/5/16 10:37
 * @Description:我的银行卡列表, 选择银行卡，添加银行卡页面
 */
@Suppress("UNUSED_ANONYMOUS_PARAMETER")
open class MyBankCardActivity : WalletBaseActivity(), QCObserver {

    private var mEnterType: String? = "1"//1我的银行卡进入，2充值选择银行卡, 3提现选择银行卡

    private var mAdapter : MyBankCardAdapter? = null

    override fun setContentView() {
        setContentView(R.layout.my_bank_card_activity)
    }

    override fun initView() {
        mTitleLayout = my_bank_card_title_layout

        my_bank_card_recycler_view.layoutManager = LinearLayoutManager(this)
        mAdapter = MyBankCardAdapter()
        my_bank_card_recycler_view.adapter = mAdapter
    }

    override fun initData() {

        mEnterType = intent?.getStringExtra(KeyValue.ENTER_TYPE)

        mAdapter?.isShowUnBind(mEnterType == KeyValue.Wallet.MY_BANK_CARD)

        requestData()
    }

    private fun requestData() {
        val jsonParam =  JSONObject()
        jsonParam.put(KeyValue.TYPE, mEnterType.toString())
        mPresenter.getBankList(DqUrl.url_get_bank_card_list, encryptJson(jsonParam))
    }

    override fun initListener() {
        super.initListener()
        my_bank_card_add_card_btn.setOnClickListener {
            DqToast.showShort("添加银行卡")
            NavUtils.gotoAddBankCardActivity(this, KeyValue.Wallet.ADD_CARD_NONE)
        }

        mAdapter?.setBankCardListener(object : MyBankCardAdapter.OnItemClickListener{
            override fun onUnBindBank(bankCardBean: BankCardBean) {
                val listDialog = CommonListDialog(activity)
                listDialog.setItem(DqApp.getStringById(R.string.remove_bank_card))
                listDialog.show()
                listDialog.setListener { item, position ->
                    unBindBankCard(bankCardBean)
                }
            }

            override fun onAddBank() {
                //DqToast.showShort("添加银行卡")
                NavUtils.gotoAddBankCardActivity(activity, KeyValue.Wallet.ADD_CARD_NONE)
            }
        })

        MsgMgr.getInstance().attach(this)
    }

    private fun unBindBankCard(item: BankCardBean?) {

        WalletHelper.get().showPayKeyBoard(activity, "", item?.cardno.toString(),
                PayKeyboardView.OnPopWindowClickListener { pwd, complete ->
                    if (complete){
                        if (mPresenter == null){
                            return@OnPopWindowClickListener
                        }

                        val jsonParam = mPresenter.getJsonParam()
                        jsonParam.put(KeyValue.Wallet.PWD_PAY, StringUtil.getPwdDatas(pwd))
                        jsonParam.put(KeyValue.Wallet.CARDID, item?.cardid.toString())
                        mPresenter.unbindBankCard(DqUrl.url_unbind_bank_card, encryptJson(jsonParam))
                    }
                })
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        if(DqUrl.url_get_bank_card_list == url){
            val cardList = GsonUtils.fromJsonList(entity?.json, BankCardBean::class.java)
            my_bank_card_no_data_ll.visibility = if(cardList.size > 0) View.GONE else View.VISIBLE
            mAdapter?.update(cardList)
        }else if (DqUrl.url_unbind_bank_card == url){
            DqToast.showShort(entity?.content)
            requestData()
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onFailed(url, code, entity)
        if (DqUrl.url_get_bank_card_list == url){
            my_bank_card_no_data_ll.visibility = View.VISIBLE
        }
    }

    override fun onMessage(key: String?, value: Any?) {
       if (MsgType.MT_WALLET_BIND_CARD == key){
           requestData()
       }
    }
}