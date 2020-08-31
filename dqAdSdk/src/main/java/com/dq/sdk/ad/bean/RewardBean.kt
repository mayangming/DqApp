package com.dq.sdk.ad.bean

import java.io.Serializable

data class RewardBean(
        val adName :String = "", //广告名称
        val description :String = "", //广告描述
        val adTime :Int = 30, //广告总时长,单位 秒
        val adEndTime :Int = 3, //广告过多久后，就允许跳过广告
        val adUrl :String = "", //广告图片,视频url
        val adLogo :String = "", //广告logo
        val adKPS :Float = 0f, //广告应用评分
        val adMany :String = "", //广告多少人评论
        val returnUrl :String = "", //广告回调url
        val downUrl :String = "", //广告下载url
        val historyId :String = "", //广告历史编号
        val appName :String = "", //下载的应用名字
        val adType :MutableList<AdTypeBean> = mutableListOf() //广告类型
):Serializable