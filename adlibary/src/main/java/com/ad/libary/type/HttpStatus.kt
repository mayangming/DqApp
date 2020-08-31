package com.ad.libary.type

/**
 * 网络请求状态
 */
enum class HttpStatus(status :Int, message :String){
    SUCCESS(0,"数据请求成功"),
    FAIL(1,"数据请求失败");
}