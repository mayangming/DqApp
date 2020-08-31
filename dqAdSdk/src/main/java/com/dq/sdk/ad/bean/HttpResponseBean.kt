package com.dq.sdk.ad.bean

import com.dq.sdk.ad.type.HttpStatus
import java.lang.Exception
import java.lang.NullPointerException

/**
 * 网络响应状态
 */
class HttpResponseBean {
    var httpStatus : HttpStatus = HttpStatus.SUCCESS
    var error :Exception = NullPointerException("模拟的空指针")
}