package com.wd.daquan.mine.wallet.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import com.wd.daquan.DqApp
import com.wd.daquan.R
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.common.utils.NavUtils
import com.da.library.listener.DialogListener
import com.da.library.view.CommDialog
import com.da.library.view.PayKeyboardView
import com.wd.daquan.model.utils.UIUtil

/**
 * @Author: 方志
 * @Time: 2019/5/15 19:24
 * @Description:
 */
@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class WalletHelper {
    //饿汉式单例
    companion object {
        private var instance: WalletHelper? = null
            get() {
                if (field == null) {
                    field = WalletHelper()
                }
                return field
            }

        @Synchronized
        fun get(): WalletHelper {
            return instance!!
        }
    }

    private lateinit var commDialog: CommDialog

    /**
     * 原支付密码错误
     */
    fun showPwdErrDialog(activity: Activity, desc: String?) {
        commDialog = CommDialog(activity)
        commDialog.setTitleGone()
        commDialog.setDesc(desc)
        commDialog.setDescCenter()
        commDialog.setOkTxt(DqApp.getStringById(R.string.forgot_password))
        commDialog.setCancelTxt(DqApp.getStringById(R.string.retry))
        commDialog.setOkTxtColor(DqApp.getColorById(R.color.app_theme_color))
        commDialog.show()

        commDialog.setDialogListener(object : DialogListener {
            override fun onCancel() {
            }

            override fun onOk() {
                //忘记支付密码
                NavUtils.gotoAddBankCardActivity(activity, KeyValue.Wallet.ADD_CARD_FORGOT_PASSWORD)
                activity.finish()
            }
        })
    }

    /**
     * 支付键盘
     */
    fun showPayKeyBoard(activity: Activity, money: String, title: String, listener: PayKeyboardView.OnPopWindowClickListener) {
        val view = PayKeyboardView(activity, money, title, listener)
        view.showAtLocation(activity.window.decorView, Gravity.BOTTOM, 0, 0)
    }

    fun dismiss() {
        try {
            commDialog.dismiss()
        } catch (e: Exception) {
        }
    }

    /**
     * 提现手续费
     */
    @SuppressLint("SetTextI18n")
    fun showFeeDialog(activity: Activity, listener : CashOutFeeDialogListener?) {
        @SuppressLint("InflateParams")
        val view = LayoutInflater.from(activity).inflate(R.layout.servicecharge_dialog, null)

        val cashOutAmountTv = view.findViewById<TextView>(R.id.cash_out_amount_tv)
        val serviceChargeHintTv = view.findViewById<TextView>(R.id.service_charge_hint_tv)
        val serviceChargeTv = view.findViewById<TextView>(R.id.service_charge_tv)
        val realAmountTv = view.findViewById<TextView>(R.id.real_amount_to_account_tv)

        cashOutAmountTv.text = "11" +  DqApp.getStringById(R.string.rd_comm_yuan)
        serviceChargeHintTv.text = String.format(DqApp.getStringById(R.string.service_charge_extra), "0.04")
        serviceChargeTv.text = "0.04"
        realAmountTv.text = "11" +  DqApp.getStringById(R.string.rd_comm_yuan)

        val cancelTv = view.findViewById<TextView>(R.id.cancel_tv)
        val cashOutTv = view.findViewById<TextView>(R.id.cash_out_tv)

        val dialog = Dialog(activity, R.style.common_dialog)
        dialog.setContentView(view)
        val dialogWindow = dialog.window
        val lp = dialogWindow!!.attributes
        view.measure(0, 0)
        lp.width = (UIUtil.getScreenWidth(activity) * 0.8).toInt()
        dialogWindow.attributes = lp

        dialog.show()

        cancelTv.setOnClickListener { dialog.dismiss() }
        cashOutTv.setOnClickListener {
            dialog.dismiss()
            listener?.ok()
        }
    }

    interface CashOutFeeDialogListener{
        fun ok()
    }

}