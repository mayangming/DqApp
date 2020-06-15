package com.wd.daquan.mine.wallet.helper

import android.util.Log
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.model.bean.DataBean
import com.da.library.tools.AESHelper
import com.wd.daquan.common.presenter.Presenter
import com.wd.daquan.mine.wallet.bean.PayPwdKeyBean
import com.wd.daquan.mine.wallet.presenter.WalletPresenter

/**
 * @Author: 方志
 * @Time: 2019/6/6 10:44
 * @Description:
 */
class PayPwdHelper() : Presenter.IView<DataBean<Any>> {

    //饿汉式单例
    companion object {
        private var instance: PayPwdHelper? = null
            get() {
                if (field == null) {
                    field = PayPwdHelper()
                }
                return field
            }
        @Synchronized
        fun get(): PayPwdHelper {
            return instance!!
        }
    }

    private var mPresenter: WalletPresenter? = null
    private var mPayPwdKeyBean : PayPwdKeyBean? = null
    private var mListener : PayPwdListener? = null

    init {
        this.mPresenter = WalletPresenter()
        mPresenter?.attachView(this)
        Log.e("TAG", " System.loadLibrary(\"PassGuard\")")
        System.loadLibrary("PassGuard")
    }


    fun initKey(listener : PayPwdListener){
        this.mListener = listener
        var jsonParam = mPresenter?.getJsonParam()
        val hashMap = HashMap<String, String>()
        hashMap[KeyValue.DATA] = AESHelper.encryptString(jsonParam.toString())
        mPresenter?.getPayPwdKey(DqUrl.url_get_pay_pwd_key, hashMap)
    }

    fun getRandomValue() : String{
        return mPayPwdKeyBean?.random_value.toString()
    }
//
//    /**
//     * 获取密码
//     * @param edit 输入密码控件
//     * @param scrollView 密码控件的父布局
//     */
//    fun initPassGuard(edit: PassGuardEdit, scrollView : View?, payPwdKeyBean : PayPwdKeyBean?) {
//        PassGuardEdit.setLicense(payPwdKeyBean?.license)
//        edit.setCipherKey(payPwdKeyBean?.random_value)
//        edit.setPublicKey(payPwdKeyBean?.rsa_public_content)
//        edit.setMaxLength(6)
//        edit.setClip(false)
//        edit.setButtonPress(false)
//        edit.setButtonPressAnim(false)
//        edit.setWatchOutside(false)
//        edit.useNumberPad(true)
//        edit.setInputRegex("[a-zA-Z0-9@_\\.]")
//        edit.initPassGuardKeyBoard()
//         //设置这个后，键盘显示时，若会遮挡编辑框，则scrollView会上推，使不挡住输入框
//        edit.needScrollView(true)
//        edit.setScrollView(scrollView)
//        edit.initPassGuardKeyBoard()
//    }

//    /**
//     * 获取密码
//     * @param edit 输入密码控件
//     * @param payPwdKeyBean 数据
//     */
//    fun initPassGuard(edit: PassGuardEdit , payPwdKeyBean : PayPwdKeyBean?) {
//        PassGuardEdit.setLicense(payPwdKeyBean?.license)
//        edit.setCipherKey(payPwdKeyBean?.random_value)
//        edit.setPublicKey(payPwdKeyBean?.rsa_public_content)
//        edit.setMaxLength(6)
//        edit.setClip(false)
//        edit.setButtonPress(false)
//        edit.setButtonPressAnim(false)
//        edit.setWatchOutside(false)
//        edit.useNumberPad(true)
//        edit.setInputRegex("[a-zA-Z0-9@_\\.]")
//        edit.initPassGuardKeyBoard()
//    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        mPayPwdKeyBean =  entity?.data as? PayPwdKeyBean
        mListener?.onSuccess(mPayPwdKeyBean)

        Log.e("TAG", "license: " + mPayPwdKeyBean?.license)
        Log.e("TAG", "random_value: " + mPayPwdKeyBean?.random_value)
        Log.e("TAG", "random_key: " + mPayPwdKeyBean?.random_key)
        Log.e("TAG", "rsa_public_content: " + mPayPwdKeyBean?.rsa_public_content)
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        Log.e("TAG", "onFailed ： ")
    }


    fun onDestroy(){
        mPresenter?.detachView()
    }

    interface PayPwdListener{
        fun onSuccess(data : PayPwdKeyBean?)
    }

}