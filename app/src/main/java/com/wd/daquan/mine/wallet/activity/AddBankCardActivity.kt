package com.wd.daquan.mine.wallet.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.wd.daquan.R
import com.wd.daquan.model.mgr.ModuleMgr
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.common.utils.DqUtils
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.mine.wallet.bean.BankCardBean
import com.da.library.tools.AutoTextWatcher
import kotlinx.android.synthetic.main.add_bank_card_activity.*
import org.json.JSONObject

/**
 * @Author: 方志
 * @Time: 2019/5/16 14:46
 * @Description: 添加银行卡
 */
class AddBankCardActivity : WalletBaseActivity() {
    private var mEnterType : String? = ""

    override fun setContentView() {
        setContentView(R.layout.add_bank_card_activity)
    }

    override fun initView() {
         mTitleLayout = add_bank_card_title_layout
    }

    override fun initData() {
        mEnterType = intent?.getStringExtra(KeyValue.ENTER_TYPE)

        add_bank_card_name_tv?.text = ModuleMgr.getCenterMgr().realName
        //自控空格
        AutoTextWatcher.bind(add_bank_card_et)
    }

    override fun initListener() {
        super.initListener()
        add_bank_card_et?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0)
                    add_bank_card_clear_iv.visibility = View.VISIBLE
                else
                    add_bank_card_clear_iv.visibility = View.GONE
            }
        })

        add_bank_card_clear_iv?.setOnClickListener {
            add_bank_card_et?.text = null
        }

        add_bank_card_btn?.setOnClickListener {
            val inputNumber = add_bank_card_et.text.toString().trim()
            val cardNumber = inputNumber.replace(" ", "")
            if (TextUtils.isEmpty(cardNumber)){
                DqToast.showShort("卡号不能为空")
            }

            val jsonObject = JSONObject()
            jsonObject.put(KeyValue.Wallet.CARD_NO, cardNumber)
            mPresenter.getBankCardInfo(DqUrl.url_add_bank_card_get_info, encryptJson(jsonObject))
        }
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        if (DqUrl.url_add_bank_card_get_info == url){
            val bankCardBean = entity?.data as? BankCardBean
            val cardNumber = replaceSpacing(add_bank_card_et.text.toString().trim())
            if (KeyValue.Wallet.ADD_CARD_NONE == mEnterType){
                NavUtils.gotoWriteBankCardInfoActivity(this, bankCardBean, cardNumber)
            }else if (KeyValue.Wallet.ADD_CARD_FORGOT_PASSWORD == mEnterType){
                NavUtils.gotoForgotSetPayPwdActivity(this, bankCardBean, cardNumber)
            }
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        DqUtils.bequit(entity, this)
    }
}