package com.wd.daquan.mine.wallet.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.wd.daquan.DqApp
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.model.bean.DataBean
import com.da.library.tools.AESHelper
import com.wd.daquan.common.utils.DqUtils
import com.wd.daquan.mine.wallet.presenter.WalletPresenter
import com.da.library.widget.CountDownTimerUtils
import org.json.JSONObject

/**
 * @Author: 方志
 * @Time: 2019/5/16 15:12
 * @Description:
 */
abstract class WalletBaseActivity : DqBaseActivity<WalletPresenter, DataBean<Any>>() {

    protected var mConfirmTv : TextView? = null
    private var mCountDownTimer : CountDownTimerUtils? = null
    protected var mPhone : String? = ""

    override fun createPresenter(): WalletPresenter {
        return WalletPresenter()
    }


    /**
     * 确认按钮是否可点击
     */
    protected fun confirmEnabled(b: Boolean) {
        if (b){
            mConfirmTv?.isEnabled = true
            mConfirmTv?.isClickable = true
            mConfirmTv?.setBackgroundResource(R.drawable.comm_btn_4dp_corner_selector)
        }else{
            mConfirmTv?.isEnabled = false
            mConfirmTv?.isClickable = false
            mConfirmTv?.setBackgroundResource(R.drawable.unenabled_bg_4dp_shape)
        }
    }

//    protected fun getSmsCode(url : String, getCodeTv : TextView?) {
//
//        if (DqUtils.validatePhoneNumber(mPhone, this)) {
//            mCountDownTimer = CountDownTimerUtils(getCodeTv, 60000, 1000, this)
//            mCountDownTimer?.start()
//
//            val key = IConstant.Login.MEETQS + mPhone
//            val hashMap = HashMap<String, String>()
//            hashMap[IConstant.Login.PHONE] = mPhone.toString()
//            hashMap[IConstant.Login.TOKEN_KEY] = MD5.encrypt(key).toLowerCase()
//            mPresenter.getValidateCode(url, hashMap)
//        }
//    }
    protected fun getSmsCode(getCodeTv : TextView?) {
        mCountDownTimer = CountDownTimerUtils(getCodeTv, 60000, 1000, this,
                DqApp.getColorById(R.color.text_color_selector))
        mCountDownTimer?.start()
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {

    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        DqUtils.bequit(entity, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mCountDownTimer?.onFinish()
    }

    /**
     * 电话号码输入样式
     */
    protected fun setPhoneInput(phoneEt: EditText?, clearIv : ImageView?) {
        phoneEt?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    clearIv?.visibility = View.VISIBLE
                }else {
                    clearIv?.visibility = View.GONE
                }

                var contents = s.toString()
                val length = contents.length
                if (length == 4) {
                    if (contents.substring(3) == " ") {
                        contents = contents.substring(0, 3)
                        phoneEt.setText(contents)
                        phoneEt.setSelection(contents.length)
                    } else { // +
                        contents = contents.substring(0, 3) + " " + contents.substring(3)
                        phoneEt.setText(contents)
                        phoneEt.setSelection(contents.length)
                    }
                } else if (length == 9) {
                    if (contents.substring(8) == " ") { // -
                        contents = contents.substring(0, 8)
                        phoneEt.setText(contents)
                        phoneEt.setSelection(contents.length)
                    } else {// +
                        contents = contents.substring(0, 8) + " " + contents.substring(8)
                        phoneEt.setText(contents)
                        phoneEt.setSelection(contents.length)
                    }
                }
            }
        })

        clearIv?.setOnClickListener {
            phoneEt?.text = null
        }
    }

    /**
     * 密码输入监听
     */
    fun setPwdInput(pwdTv : TextView?, pwdConfirmTv : TextView?) {
        pwdTv?.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                checkPwd(pwdConfirmTv, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        pwdConfirmTv?.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                checkPwd(pwdTv, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun checkPwd(pwdTv: TextView?, input: String) {
        val pwd = pwdTv?.text.toString().trim()

        if (TextUtils.isEmpty(pwd) || pwd.length < 6 || TextUtils.isEmpty(input) || input.length < 6){
            confirmEnabled(false)
            return
        }
        confirmEnabled(true)
    }

    /**
     * 替换文字中空格
     */
    fun replaceSpacing(text : String): String{
        return text.replace(" ", "")
    }

    /**
     * 数据加密
     */
    fun encryptJson(jsonObject : JSONObject): HashMap<String, String>{
        val hashMap = HashMap<String, String>()
        hashMap[KeyValue.DATA] = AESHelper.encryptString(jsonObject.toString())
        return hashMap
    }
}