package com.dq.sdk.ad.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dq.sdk.ad.bean.HttpResponseBean
import com.dq.sdk.ad.bean.RewardBean
import com.dq.sdk.ad.config.UrlConfig
import com.dq.sdk.ad.http.DqAdSdkHttpUtils
import com.dq.sdk.ad.http.bean.HttpBaseBean
import com.dq.sdk.ad.http.callback.HttpBaseBeanCallBack
import com.dq.sdk.ad.listener.DqAppDownloadListener
import com.dq.sdk.ad.listener.RewardAdListener
import com.dq.sdk.ad.type.HttpStatus
import com.google.gson.Gson
import okhttp3.Call

class AdViewModel(appContext :Application) :AndroidViewModel(appContext){
    var rewardBean = MutableLiveData<RewardBean>(RewardBean())
    var httpResponse = MutableLiveData<HttpResponseBean>(HttpResponseBean())
    fun getAd(codeId :String){

        val params = mutableMapOf<String,Any>()
        params["codeId"] = codeId
        DqAdSdkHttpUtils.postJson(UrlConfig.AD_VERTISEMENT,params,object : HttpBaseBeanCallBack<String>(){
            override fun onError(call: Call?, e: Exception?, id: Int) {
                val httpBean = HttpResponseBean()
                httpBean.httpStatus = HttpStatus.FAIL
                httpResponse.postValue(httpBean)
            }

            override fun onResponse(response: HttpBaseBean<String>?, id: Int) {
                val data = response?.data ?: return
                val reward = Gson().fromJson<RewardBean>(data,RewardBean::class.java)
                val httpBean = HttpResponseBean()
                httpBean.httpStatus = HttpStatus.SUCCESS
                httpResponse.postValue(httpBean)
                rewardBean.postValue(reward)
            }
        })
    }

    /**
     * 请求的历史广告
     */
    fun verifyAdReward(historyId: String){
        val params = mutableMapOf<String,Any>()
        params["historyId"] = historyId
        params["status"] = "1" //是否有效 1：有效 2：无效
        DqAdSdkHttpUtils.postJson(UrlConfig.UPDATE_HISTORY,params,object : HttpBaseBeanCallBack<String>(){
            override fun onError(call: Call?, e: Exception?, id: Int) {
                val httpBean = HttpResponseBean()
                httpBean.httpStatus = HttpStatus.FAIL
                httpResponse.postValue(httpBean)
            }

            override fun onResponse(response: HttpBaseBean<String>?, id: Int) {
                val httpBean = HttpResponseBean()
                httpBean.httpStatus = HttpStatus.SUCCESS
                httpResponse.postValue(httpBean)
            }
        })
    }
    /**
     * 下载统计
     */
    fun downloadCount(historyId: String){
        val params = mutableMapOf<String,Any>()
        params["historyId"] = historyId
        params["status"] = "1" //是否有效 1：有效 2：无效
        DqAdSdkHttpUtils.postJson(UrlConfig.UPDATE_SAVEDOWN,params,object : HttpBaseBeanCallBack<String>(){
            override fun onError(call: Call?, e: Exception?, id: Int) {
                val httpBean = HttpResponseBean()
                httpBean.httpStatus = HttpStatus.FAIL
                httpResponse.postValue(httpBean)
            }

            override fun onResponse(response: HttpBaseBean<String>?, id: Int) {
                val httpBean = HttpResponseBean()
                httpBean.httpStatus = HttpStatus.SUCCESS
                httpResponse.postValue(httpBean)
            }
        })
    }

    private var rewardListener : RewardAdListener?= null

    fun getRewardAdListener() = rewardListener

    fun setRewardAdListener(rewardListener : RewardAdListener){
        this.rewardListener = rewardListener
    }


}