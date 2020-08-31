package com.dq.sdk.ad.http.callback

import com.dq.sdk.ad.bean.RewardBean
import http.callback.Callback
import okhttp3.Response

abstract class RewardBeanCallBack : Callback<RewardBean>() {
    override fun parseNetworkResponse(response: Response?, id: Int): RewardBean {

        //Response.body().string()方法在调用后会关闭Response.body(),另外string()方法只能打印1M之内的内容,若打印内容超过1M需要使用流进行打印
        val result = response?.body?.string() ?: ""

        return RewardBean()
    }
}