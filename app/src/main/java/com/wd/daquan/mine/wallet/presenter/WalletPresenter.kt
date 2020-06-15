package com.wd.daquan.mine.wallet.presenter

import com.wd.daquan.DqApp
import com.wd.daquan.R
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.net.RequestHelper
import com.wd.daquan.common.presenter.BasePresenter
import com.wd.daquan.common.presenter.Presenter
import com.wd.daquan.mine.wallet.bean.*
import com.wd.daquan.net.callback.ObjectCallback
import com.da.library.tools.AppInfoUtils
import okhttp3.Call
import org.json.JSONObject

/**
 * @Author: 方志
 * @Time: 2019/5/14 20:29
 * @Description: 钱包,金钱数据i请求
 */
class WalletPresenter : BasePresenter<Presenter.IView<DataBean<Any>>>() {

    /**
     * 获取零钱
     */
    fun get(url: String?, hashMap: HashMap<String, String>?) {
        showLoading()
        RequestHelper.request(url, hashMap, object : ObjectCallback<DataBean<Any>>() {
            override fun onSuccess(call: Call?, url: String?, code: Int, result: DataBean<Any>?) {
                success(url, code, result)
            }

            override fun onFailed(call: Call?, url: String?, code: Int, result: DataBean<Any>?, e: Exception?) {
                failed(url, code, result)
            }

            override fun onFinish() {
                hideLoading()
            }
        })
    }

    /**
     * 验证短信验证码
     */
    fun getValidateCode(url: String?, hashMap: HashMap<String, String>?) {
        get(url, hashMap)
    }

    /**
     * 设置密码
     */
    fun setPayPwd(url: String?, hashMap: HashMap<String, String>?) {
        get(url, hashMap)
    }

    /**
     * 读取用户绑定的银行卡列表
     */
    fun getBankList(url: String?, hashMap: HashMap<String, String>?) {
        get(url, hashMap)
    }

    /**
     * 解绑银行卡
     */
    fun unbindBankCard(url: String?, hashMap: HashMap<String, String>?) {
        get(url, hashMap)
    }

    /**
     * 获取支付密码key
     */
    fun getPayPwdKey(url: String?, hashMap: HashMap<String, String>?) {
        showLoading()
        RequestHelper.request(url, hashMap, object : ObjectCallback<DataBean<PayPwdKeyBean>>() {
            override fun onSuccess(call: Call?, url: String?, code: Int, result: DataBean<PayPwdKeyBean>?) {
                success(url, code, result)
            }

            override fun onFailed(call: Call?, url: String?, code: Int, result: DataBean<PayPwdKeyBean>?, e: Exception?) {
                failed(url, code, result)
            }

            override fun onFinish() {
                hideLoading()
            }
        })
    }

    /**
     * 设置密码
     */
    fun getBankCardInfo(url: String?, hashMap: HashMap<String, String>?) {
        showLoading()
        RequestHelper.request(url, hashMap, object : ObjectCallback<DataBean<BankCardBean>>() {
            override fun onSuccess(call: Call?, url: String?, code: Int, result: DataBean<BankCardBean>?) {
                success(url, code, result)
            }

            override fun onFailed(call: Call?, url: String?, code: Int, result: DataBean<BankCardBean>?, e: Exception?) {
                failed(url, code, result)
            }

            override fun onFinish() {
                hideLoading()
            }
        })
    }

    /**
     * 读取用户绑定的银行卡列表
     */
    fun createRechargeOrder(url: String?, hashMap: HashMap<String, String>) {
        showLoading()
        RequestHelper.request(url, hashMap, object : ObjectCallback<DataBean<RechargeBean>>() {
            override fun onSuccess(call: Call?, url: String?, code: Int, result: DataBean<RechargeBean>?) {
                success(url, code, result)
            }

            override fun onFailed(call: Call?, url: String?, code: Int, result: DataBean<RechargeBean>?, e: Exception?) {
                failed(url, code, result)
            }

            override fun onFinish() {
                hideLoading()
            }
        })
    }

    /**
     * 认证 获取验证码
     */
    fun getVerificationCode(url: String?, hashMap: HashMap<String, String>?) {
        showLoading()
        RequestHelper.request(url, hashMap, object : ObjectCallback<DataBean<VericationCodeBean>>() {
            override fun onSuccess(call: Call?, url: String?, code: Int, result: DataBean<VericationCodeBean>?) {
                success(url, code, result)
            }

            override fun onFailed(call: Call?, url: String?, code: Int, result: DataBean<VericationCodeBean>?, e: Exception?) {
                failed(url, code, result)
            }

            override fun onFinish() {
                hideLoading()
            }
        })
    }

    /**
     * 认证 校验验证码
     */
    fun verfiyVerificationCode(url: String?, hashMap: HashMap<String, String>?) {
        showLoading()
        RequestHelper.request(url, hashMap, object : ObjectCallback<DataBean<VericationCodeBean>>() {
            override fun onSuccess(call: Call?, url: String?, code: Int, result: DataBean<VericationCodeBean>?) {
                success(url, code, result)
            }

            override fun onFailed(call: Call?, url: String?, code: Int, result: DataBean<VericationCodeBean>?, e: Exception?) {
                failed(url, code, result)
            }

            override fun onFinish() {
                hideLoading()
            }
        })
    }

    /***
     * 零钱明细接口
     */
    fun getLooseDetail(url: String?, hashMap: Map<String, String>?) {
        showLoading()
        RequestHelper.request(url, hashMap, object : ObjectCallback<DataBean<LooseDetailsItemBean>>() {
            override fun onSuccess(call: Call, url: String, code: Int, result: DataBean<LooseDetailsItemBean>?) {
                success(url, code, result)
            }

            override fun onFailed(call: Call, url: String, code: Int, result: DataBean<LooseDetailsItemBean>?, e: Exception) {
                failed(url, code, result)
            }

            override fun onFinish() {
                hideLoading()
            }
        })
    }

    /***
     * 红包收入记录接口
     */
    fun getIn_redpacket_record(url: String?, hashMap: Map<String, String>?) {
        showLoading()
        RequestHelper.request(url, hashMap, object : ObjectCallback<DataBean<RedDetailsBean>>() {
            override fun onSuccess(call: Call, url: String, code: Int, result: DataBean<RedDetailsBean>?) {
                success(url, code, result)
            }

            override fun onFailed(call: Call, url: String, code: Int, result: DataBean<RedDetailsBean>?, e: Exception) {
                failed(url, code, result)
            }

            override fun onFinish() {
                hideLoading()
            }
        })
    }

    /***
     * 红包支出记录接口
     */
    fun getOut_redpacket_record(url: String?, hashMap: Map<String, String>?) {
        showLoading()
        RequestHelper.request(url, hashMap, object : ObjectCallback<DataBean<RedDetailsBean>>() {
            override fun onSuccess(call: Call, url: String, code: Int, result: DataBean<RedDetailsBean>?) {
                success(url, code, result)
            }

            override fun onFailed(call: Call, url: String, code: Int, result: DataBean<RedDetailsBean>?, e: Exception) {
                failed(url, code, result)
            }

            override fun onFinish() {
                hideLoading()
            }
        })
    }

    /**
     * 获取零钱的状态
     */
    fun getWalletStatus(url: String?, hashMap: HashMap<String, String>?) {
        showLoading()
        RequestHelper.request(url, hashMap, object : ObjectCallback<DataBean<WalletSatausBean>>() {

            override fun onSuccess(call: Call?, url: String?, code: Int, result: DataBean<WalletSatausBean>?) {
                success(url, code, result)
            }

            override fun onFailed(call: Call?, url: String?, code: Int, result: DataBean<WalletSatausBean>?, e: Exception?) {
                failed(url, code, result)
            }

            override fun onFinish() {
                hideLoading()
            }
        })
    }

    /**
     * 钱包部分接口需要的基本信息
     */
    fun getWalletParam(): HashMap<String, String> {
        val hashMap = HashMap<String, String>()

//        hashMap[KeyValue.Wallet.FRMS_IMEI] = DqUtils.getDevicesId()//"手机设备id"
        hashMap[KeyValue.Wallet.SOURCE] = KeyValue.ANDROID
        hashMap[KeyValue.PKG_NAME] = DqApp.sApplication.packageName
        hashMap[KeyValue.APP_NAME] = DqApp.getStringById(R.string.app_name)

        return hashMap
    }
    /**
     * 钱包部分接口需要的基本信息
     */
    fun getJsonParam(): JSONObject {

        val jsonObject = JSONObject()
        jsonObject.put(KeyValue.Wallet.FRMS_IMEI, AppInfoUtils.getPhoneImei())//"手机设备id"
        jsonObject.put(KeyValue.Wallet.SOURCE, KeyValue.ANDROID)//资原类型
        jsonObject.put(KeyValue.PKG_NAME, DqApp.sApplication.packageName)//包名
        jsonObject.put(KeyValue.APP_NAME, DqApp.getStringById(R.string.app_name))//app名
        jsonObject.put(KeyValue.Wallet.FRMS_MAC_ADDR, AppInfoUtils.getMacAddress(DqApp.sContext))
        jsonObject.put(KeyValue.Wallet.FRMS_MECHINE_ID, AppInfoUtils.getPseudoUniqueID())
        return jsonObject
    }

    /**
     * 提现计算手续费
     */
    fun cashOutFee(url: String?, hashMap: java.util.HashMap<String, String>) {
        showLoading()
        RequestHelper.request(url, hashMap, object : ObjectCallback<DataBean<CashOutBean>>() {

            override fun onSuccess(call: Call?, url: String?, code: Int, result: DataBean<CashOutBean>?) {
                success(url, code, result)
            }

            override fun onFailed(call: Call?, url: String?, code: Int, result: DataBean<CashOutBean>?, e: Exception?) {
                failed(url, code, result)
            }

            override fun onFinish() {
                hideLoading()
            }
        })
    }

    /**  * 提现创建订单
     */
    fun cashOutCreateOrder(url: String?, hashMap: java.util.HashMap<String, String>) {
        cashOutFee(url, hashMap)
    }

}