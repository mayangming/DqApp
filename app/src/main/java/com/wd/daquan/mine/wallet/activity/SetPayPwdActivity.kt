package com.wd.daquan.mine.wallet.activity

import com.wd.daquan.DqApp
import com.wd.daquan.R
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.common.utils.DqUtils
import com.wd.daquan.model.log.DqToast
import com.da.library.tools.ActivitysManager
import com.netease.nim.uikit.common.util.string.StringUtil
import kotlinx.android.synthetic.main.set_pay_pwd_activity.*

/**
 * @Author: 方志
 * @Time: 2019/5/15 11:51
 * @Description: 设置支付密码
 */
class SetPayPwdActivity : WalletBaseActivity(){
    private var mEnterType: String? = ""//1修改支付密码，2忘记支付密码, 3开通账号设置密码
    private var mOldPwd: String? = ""//之前的旧密码

    override fun setContentView() {
        setContentView(R.layout.set_pay_pwd_activity)
    }

    override fun initView() {
        mTitleLayout = set_pay_pwd_title_layout
        mConfirmTv = set_pay_pwd_confirm_tv
        confirmEnabled(false)
    }

    override fun initData() {
        mEnterType = intent?.getStringExtra(KeyValue.ENTER_TYPE)
        mOldPwd = intent?.getStringExtra(KeyValue.PASSWORD)

        mTitleLayout?.title = DqApp.getStringById(R.string.set_pay_pwd)

    }

    override fun initListener() {
        super.initListener()
        setPwdInput(set_pay_pwd_et, set_pay_pwd_again_et)

        set_pay_pwd_confirm_tv.setOnClickListener {
            requestPwd()
        }
    }

    private fun requestPwd() {
        val pwd = set_pay_pwd_et.text.toString().trim()
        val pwdAgain = set_pay_pwd_again_et.text.toString().trim()
        if (pwd != pwdAgain) {
            DqToast.showShort(DqApp.getStringById(R.string.input_pay_pwd_diff))
            return
        }

        when (mEnterType) {
            "1" -> {//修改支付密码
                val jsonParam = mPresenter.getJsonParam()
                jsonParam.put(KeyValue.Wallet.PWD_PAY_NEW, StringUtil.getPwdData(pwd))
                jsonParam.put(KeyValue.Wallet.PWD_PAY, StringUtil.getPwdData(mOldPwd))
                mPresenter.setPayPwd(DqUrl.url_modify_pay_pwd, encryptJson(jsonParam))
            }

        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        if (DqUrl.url_modify_pay_pwd == url || DqUrl.url_forgot_pwd_bind_info == url) {
            DqUtils.bequit(entity, this)
        } else {
            DqUtils.bequitRedPay(this, code, entity?.content)
        }
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        when (url) {
            DqUrl.url_modify_pay_pwd -> {
                DqToast.showShort("修改成功")
                ActivitysManager.getInstance().finishMore(CheckPayPwdActivity::class.java, this::class.java)
            }
//            DqUrl.url_forgot_pwd_bind_info -> {
//                DqToast.showShort("重置成功")
//                ActivitysManager.getInstance().finishMore(CheckPayPwdActivity::class.java, this::class.java)
//            }
//            DqUrl.url_wallet_verification_get_code -> {
//                DqToast.showShort("设置成功")
//                finish()
//            }
        }
    }

}