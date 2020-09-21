package com.wd.daquan.mine.activity

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ad.libary.AdSdkCompact
import com.ad.libary.AdSdkCompact.AdSwitchResultIpc
import com.ad.libary.bean.AdCodeInfoBean
import com.ad.libary.compat.RewardVideoAdCompat
import com.ad.libary.config.SDKAdBuild
import com.ad.libary.simple_iml.RewardVideoListenerIpc
import com.ad.libary.type.AdType
import com.da.library.constant.IConstant
import com.da.library.utils.BigDecimalUtils
import com.netease.nim.uikit.common.ui.recyclerview.decoration.SpacingDecoration
import com.red.libary.weight.OpenGiftDialog
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.mine.adapter.SignUpAdapter
import com.wd.daquan.mine.dialog.SignDialog
import com.wd.daquan.mine.presenter.IntegralMallPresenter
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.RedEnvelopBean
import com.wd.daquan.model.bean.SignUpEntity
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.util.DobbleClickUtils
import kotlinx.android.synthetic.main.activity_sign_detail.*

/**
 * 签到详情页面
 */
class SignUpDetailActivity : DqBaseActivity<IntegralMallPresenter, DataBean<Any>>(){
    private var rewardVideoAdCompat: RewardVideoAdCompat? = null
    private var signAdapter = SignUpAdapter()
    private var signUpEntity = SignUpEntity()
    companion object{
        const val KEY_ACTION = "signUpEntity"
    }

    override fun createPresenter() = IntegralMallPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_sign_detail)
    }

    override fun initView() {
        initTitle()
        initRecycleView()
    }

    override fun initData() {
        signUpEntity = intent.getSerializableExtra(KEY_ACTION) as SignUpEntity
        updateUi()
        switchAd(IConstant.AD.REWARD_ID)
    }

    private fun initTitle(){
        sign_detail_title.title = "签到详情"
        sign_detail_title.leftIv.setOnClickListener {
            finish()
        }
    }

    override fun initListener() {
        super.initListener()
        open_red_package.setOnClickListener {
            val redCount = sign_red_package_count.text
            if(redCount == "0"){
               DqToast.showCenterShort("已经没有剩余次数了,请明天签到后再来观看~")
                return@setOnClickListener
            }
            if (DobbleClickUtils.isFastClick()){
                return@setOnClickListener
            }
            switchAd(IConstant.AD.REWARD_ID)
            rewardVideoAdCompat!!.showAd(activity)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun initRecycleView() {
        sign_record_brv.apply {
            layoutManager = GridLayoutManager(this@SignUpDetailActivity, 7)
            adapter = signAdapter
        }
        sign_record_brv.addItemDecoration(SpacingDecoration(15, 10, false))
    }

    private fun updateUi(){
        DqLog.e("YM---------->签到规则",signUpEntity.dbRule)
        sign_record.text = "已签${signUpEntity.dbUserSign.signNum}/${signUpEntity.list.size}次"
        signAdapter.signUpEntity = signUpEntity
        sign_rule_content.text =  Html.fromHtml(signUpEntity.dbRule)
        sign_red_package_count.text = "${signUpEntity.dbUserSign.canRedCount}"
        if (signUpEntity.isSign){
            val currentDay = signUpEntity.dbUserSign.signNum//当前次数
            val money = signUpEntity.list[currentDay - 1].dbaward.toString()
//            val content = "斗币+${money}，看视频得红包+${signUpEntity.dbUserSign.canRedCount}"
            val content = "斗币+${money}，看视频得红包+3"
            showSignDialog(content)
        }
    }

    /**
     * 显示签到兑换
     */
    private fun showSignDialog(content: String){
        val arguments = Bundle()
        arguments.putString(SignDialog.KEY_ACTION, content)
        arguments.putInt(SignDialog.KEY_TYPE, 0)
        val taskTypeDialog = SignDialog()
        taskTypeDialog.arguments = arguments
        taskTypeDialog.show(supportFragmentManager, "")
    }
    private fun switchAd(codeId: String) {
        val sdkAdBuild = SDKAdBuild()
        sdkAdBuild.mAppId = IConstant.AD.APP_ID
        sdkAdBuild.mAppName = IConstant.AD.APP_NAME
        sdkAdBuild.codeId = codeId
        AdSdkCompact.getInstance().switchAd(sdkAdBuild, object : AdSwitchResultIpc {
            override fun switchSuccess(adCodeInfoBean: AdCodeInfoBean, type: AdType) {
                Log.e("YM", "切换成功type:" + type.type)
                Log.e("YM", "切换成功adName:" + type.adName)
                SDKAdBuild.rewardAdCode = adCodeInfoBean.codeId.toString()
                rewardVideoAd(adCodeInfoBean.codeId.toString())
                //                switch (type){
//                    case AD_DQ:
//                        rewardVideoAd(String.valueOf(adCodeInfoBean.getCodeId()));
//                        break;
//                    case AD_TT:
//                        break;
//                    case AD_GDT:
//                        break;
//                }
            }

            override fun switchFail() {
                DqToast.showShort("网络不佳,请稍后再试")
            }
        })
    }

    private fun rewardVideoAd(codeId: String) {
        if (null == rewardVideoAdCompat) {
            rewardVideoAdCompat = RewardVideoAdCompat(this)
        }
        //        rewardVideoAdCompat.loadAd(SDKAdBuild.rewardAdCode, RewardVideoAdCompat.VERTICAL,new RewardVideoListenerIpc(){
        rewardVideoAdCompat?.loadAd(codeId, RewardVideoAdCompat.VERTICAL, object : RewardVideoListenerIpc() {
            override fun rewardVideoOnError(i: Int, s: String) {
                Log.e("YM", "加载失败:$s")
                DqToast.showShort("网络不佳,请稍后再试")
            }

            override fun rewardVideoOnRewardVideoAdLoad() {
                Log.e("YM--------->", "rewardVideoOnRewardVideoAdLoad")
            }

            override fun rewardVideoOnRewardVideoAdLoad(view: View) {
                Log.e("YM--------->", "广告请求结束")
                //                rewardVideoAdCompat.showAd(getActivity());
            }

            override fun rewardVideoOnRewardVideoCached() { //使用缓存加载
                Log.e("YM--------->", "rewardVideoOnRewardVideoCached")
                //                rewardVideoAdCompat.showAd(getActivity());
            }

            override fun rewardVideoComplete() {
                Log.e("YM--------->", "视频播放结束")
            }

            override fun rewardAdClose() {
                val params2 = hashMapOf<String, String>()
                mPresenter.getSignRed(DqUrl.url_get_signRed, params2)
            }
        })
    }

    /**
     * 打开中奖的dialog
     * @param amount 中奖金额
     * @param isLuck 是否中奖
     */
    private fun showOpenGift(amount: String, isLuck: Boolean) {
        val openGiftDialog = OpenGiftDialog()
        val bundle = Bundle()
        bundle.putString(OpenGiftDialog.ACTION_AMOUNT, amount)
        if (isLuck) {
            bundle.putInt(OpenGiftDialog.ACTION_TYPE, OpenGiftDialog.LUCK)
        } else {
            bundle.putInt(OpenGiftDialog.ACTION_TYPE, OpenGiftDialog.NO_LUCK)
        }
        openGiftDialog.arguments = bundle
        openGiftDialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyMinDialogWidth)
        openGiftDialog.show(supportFragmentManager, "对话框")
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)
        if (null == entity){
            return
        }

        if (url == DqUrl.url_get_getUserRedCount){
            val result = entity.data as Double
            val value = result.toInt()
            sign_red_package_count.text = "$value"
        }

        if(url == DqUrl.url_get_signRed){
            DqLog.e("YM----------->${entity.data.toString()}")
            val redEnvelopBean = entity.data as RedEnvelopBean
            val amount = BigDecimalUtils.penny2Dollar(redEnvelopBean.amount.toLong()).toPlainString()
            showOpenGift(amount, redEnvelopBean.isFlag)
            val params1 = hashMapOf<String, String>()
            mPresenter.getUserRedCount(DqUrl.url_get_getUserRedCount, params1)
        }


    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onFailed(url, code, entity)
    }

}