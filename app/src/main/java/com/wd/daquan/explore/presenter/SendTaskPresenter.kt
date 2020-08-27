package com.wd.daquan.explore.presenter

import android.content.Context
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.wd.daquan.BuildConfig
import com.wd.daquan.common.presenter.BasePresenter
import com.wd.daquan.common.presenter.Presenter
import com.wd.daquan.model.bean.*
import com.wd.daquan.model.interfaces.DqCallBack
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.retrofit.RetrofitHelp
import com.wd.daquan.wxapi.WxPayUtils

/**
 * 发布任务页面的数据解析层
 */
class SendTaskPresenter: BasePresenter<Presenter.IView<DataBean<Any>>>() {
    /**
     * 获取任务列表
     */
    fun getSendTaskBeanList(url :String, hashMap :Map<String, Any>){
        RetrofitHelp.getTaskApi().getSendTaskBeanList(url, getRequestBodyByObject(hashMap)).enqueue(object : DqCallBack<DataBean<List<SendTaskBean>>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<List<SendTaskBean>>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<List<SendTaskBean>>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }

    /**
     * 创建任务
     */
    fun getCreateTask(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getTaskApi().getCreateTask(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<SendTaskBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<SendTaskBean>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<SendTaskBean>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }

    /**
     * 更改任务内容
     */
    fun changeTask(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getTaskApi().changeTask(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<SendTaskBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<SendTaskBean>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<SendTaskBean>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }
    /**
     * 更改任务时间
     */
    fun changeTime(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getTaskApi().changeTime(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<SendTaskBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<SendTaskBean>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<SendTaskBean>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }

    /**
     * 检查任务完成情况
     */
    fun checkTask(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getTaskApi().checkTask(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<ReleaseCompleteDetailsBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<ReleaseCompleteDetailsBean>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<ReleaseCompleteDetailsBean>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }

    /**
     * 获取任务类型
     * url_task_changeStatus
     */
    fun changeStatus(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getTaskApi().changeStatus(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<SendTaskBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<SendTaskBean>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<SendTaskBean>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }


    /**
     * 任务报名
     */
    fun getTaskRegistration(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getTaskApi().findTaskRegistration(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<TaskDetailsBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<TaskDetailsBean>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<TaskDetailsBean>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }
    /**
     * 申请退款
     */
    fun drawback(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getTaskApi().drawback(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<Any>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<Any>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }
    /**
     * 获取退款的信息
     */
    fun refundMoney(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getTaskApi().refundMoney(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<String>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<String>) {

//                        hideLoading();
                success(url, code, entity)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<String>) {

//                        hideLoading();
                failed(url, code, entity)
            }
        })
    }

    var iwxapi: IWXAPI? = null

    /**
     * 获取零钱用户信息
     */
    fun getUserCloudWallet(url: String?, params: Map<String?, String?>?) {
        RetrofitHelp.getUserApi().getUserCloudWallet(url, getRequestBody(params)).enqueue(object : DqCallBack<DataBean<UserCloudWallet?>?>() {
            override fun onSuccess(url: String, code: Int, entity: DataBean<UserCloudWallet?>?) {
                success(url, code, entity)
            }

            override fun onFailed(url: String, code: Int, entity: DataBean<UserCloudWallet?>?) {
                failed(url, code, entity)
            }
        })
    }

    /***
     * 获取微信订单信息
     */
    fun getWeChatPayOrderInfo(url: String?, hashMap: Map<String?, String?>?) {
        showLoading()
        RetrofitHelp.getUserApi().getWeChatPayOrder(url, getRequestBodyByFromData(hashMap)).enqueue(object : DqCallBack<DataBean<WxPayBody?>?>() {
            override fun onSuccess(url: String, code: Int, entity: DataBean<WxPayBody?>?) {
                DqLog.e("YM", "成功------：$url")
                hideLoading()
                success(url, code, entity)
            }

            override fun onFailed(url: String, code: Int, entity: DataBean<WxPayBody?>?) {
                hideLoading()
                failed(url, code, entity)
                DqLog.e("YM", "失败------")
            }
        })
    }

    /**
     * 微信支付
     */
    fun startWeChatPay(context: Context?, wxPayModel: WxPayBody) {
        if (null == iwxapi) {
            iwxapi = WXAPIFactory.createWXAPI(context, null)
            iwxapi?.registerApp(BuildConfig.WX_PAY_APPID)
        }
        val request = PayReq()
        request.appId = wxPayModel.appid
        request.partnerId = wxPayModel.mch_id
        request.prepayId = wxPayModel.prepay_id
        request.packageValue = BuildConfig.WX_PAY_PACKAGE
        request.nonceStr = wxPayModel.nonce_str
        request.timeStamp = wxPayModel.timestamp.toString()
        request.extData = "taskType" //红包类型
        request.sign = WxPayUtils.createSign(request)
        iwxapi!!.sendReq(request)
    }


}