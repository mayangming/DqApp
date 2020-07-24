package com.wd.daquan.explore.type

/**
 * 发送动态页面的图片类型
 */
enum class DynamicSendPhotoType(val photoType :Int){
    NORMAL(0),//普通图片
    ADD(1),//添加图片
    REMOVE(1)//移除图片
}