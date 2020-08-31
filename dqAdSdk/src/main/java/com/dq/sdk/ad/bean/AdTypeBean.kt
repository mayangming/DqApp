package com.dq.sdk.ad.bean

import java.io.Serializable

/**
 * 广告类型
 */
data class AdTypeBean(
        val typeName :String = "",//广告类型名称
        val typeDescription :String = "",//广告类型描述
        val isForce :String = "",//是否强制观看 // 0: 强制观看 1:不强制观看
        val type :String = "",//广告类型: 图片or视频0:图片 1:视频
        val typeStyle :String = ""//广告样式
):Serializable