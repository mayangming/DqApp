
@file:JvmName("CheckAdUtil")
@file:JvmMultifileClass
package com.dq.sdk.ad.util
/**
 * 检查广告是否符合规则
 */
fun isFullWindow(screenW :Int, screenH :Int, adViewW :Int, adViewH :Int): Boolean{
    val diff = 300
    return (screenW - adViewW <= diff && screenH - adViewH <= diff)
}