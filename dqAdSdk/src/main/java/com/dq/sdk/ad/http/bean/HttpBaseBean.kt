package com.dq.sdk.ad.http.bean

import com.google.gson.JsonElement

/**
 * HTTP返回数据的基本结构
 */
class HttpBaseBean<T> {
    @JvmField
    var result //请求结果状态码
            = 0
    @JvmField
    var msg //请求结果描述内容
            : String? = null
    @JvmField
    var data //该字段不解析,写成这样的话，该字段就不会解析了，使用toString()既可以获取到原来的json内容
            : JsonElement? = null
    var obj //解析后的对象
            : T? = null

    override fun toString(): String {
        return "HttpBaseBean{" +
                "status=" + result +
                ", msg='" + msg + '\'' +
                ", data='" + data.toString() + '\'' +
                '}'
    }
}