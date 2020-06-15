package com.wd.daquan.mine.wallet.activity

import android.view.View
import com.wd.daquan.DqApp
import com.wd.daquan.R
import com.wd.daquan.model.log.DqToast
import com.da.library.widget.CommonListDialog
import kotlinx.android.synthetic.main.bank_card_details_activity.*

/**
 * @Author: 方志
 * @Time: 2019/5/16 14:46
 * @Description:银行卡详情
 */
class BankCardDetailsActivity : WalletBaseActivity() {

    override fun setContentView() {
        setContentView(R.layout.bank_card_details_activity)
    }

    override fun initView() {
        mTitleLayout = bank_card_details_title_layout
        mTitleLayout?.setRightVisible(View.VISIBLE)
        mTitleLayout?.setRightIv(R.mipmap.title_right_more)
    }

    override fun initData() {

    }

    override fun initListener() {
        super.initListener()
        mTitleLayout?.rightIv?.setOnClickListener {
            val listDialog = CommonListDialog(this)
            listDialog.setItem(DqApp.getStringById(R.string.remove_bank_card))
            listDialog.show()

            listDialog.setListener { _, _ ->
                DqToast.showShort("移除银行卡")
            }
        }
    }
}