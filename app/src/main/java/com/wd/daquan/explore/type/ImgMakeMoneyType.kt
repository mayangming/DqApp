package com.wd.daquan.explore.type

/**
 * 密封类
 * 实验使用
 */
sealed class ImgMakeMoneyType {
    //res资源
    data class ImgRes(val value :String) :ImgMakeMoneyType()

    //任何资源
    data class ImgAny(val value :String) :ImgMakeMoneyType()
}