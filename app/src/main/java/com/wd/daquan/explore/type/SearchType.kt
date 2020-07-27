package com.wd.daquan.explore.type

import java.io.Serializable

/**
 * 获取朋友圈内容的类型
 */
enum class SearchType (val searchType :String): Serializable{
    ALL("0"),//所有人
    PERSON("1")//指定某个人
}