package com.dq.sdk.ad.config

import com.dq.sdk.ad.BuildConfig


class UrlConfig{

    companion object{

        private const val SERVER_TYPE = 1 // 0 :本地 1: 测试 2:生产

        /**
         * 本地服务器地址
         */
        private val LOCAL_SERVER = "http://192.168.72.202:8096/"
        /**
         * 本地服务器地址
         */
//        private val LOCAL_SERVER = "http://192.168.72.191:8096/"

        /**
         * 测试服务器地址
         */
        private val TEST_SERVER = "http://39.106.158.176:8096/"
        /**
         * 生产服务器地址
         */
        private val ONLINE_SERVER = "http://39.106.158.176:8096/"
        /**
         * 获取广告内容
         */
        val AD_VERTISEMENT = "advertisement/getad"

        /**
         * 校验广告有效性
         */
        val UPDATE_HISTORY = "advertisement/updateHistory"

        /**
         * 下载应用统计
         */
        val UPDATE_SAVEDOWN = "advertisement/saveDown"

        /**
         * 获取用户接口
         */
        fun getURL(url: String): String? {
            return getServer() + url
        }

        private fun getServer(): String = BuildConfig.SERVER_SDK
//        private fun getServer(): String = when(SERVER_TYPE){
//            0 -> LOCAL_SERVER
//            1 -> TEST_SERVER
//            else -> ONLINE_SERVER
//        }
    }
}