package com.wd.daquan.explore.bean

import android.net.Uri
import com.wd.daquan.explore.type.DynamicSendPhotoType

/**
 * 发送动态页的数据类
 */
data class DynamicSendPhotoBean(var uri: Uri ?= null, //图片路径
                                var dynamicSendPhotoType :DynamicSendPhotoType = DynamicSendPhotoType.NORMAL,
                                var isRemove: Boolean = false//是否正在移除图片
)