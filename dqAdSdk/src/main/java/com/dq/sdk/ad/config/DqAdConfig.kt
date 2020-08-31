package com.dq.sdk.ad.config

class DqAdConfig {
    var appId = ""
    var appName = ""

    fun getBuild():Build{
        return Build()
    }

    /**
     * 建造者
     */
    inner class Build{
        fun setAppId(appId :String):Build{
            this@DqAdConfig.appId = appId
            return this
        }
        fun setAppName(appName :String):Build{
            this@DqAdConfig.appName = appName
            return this
        }

        fun build() = this@DqAdConfig
    }

}