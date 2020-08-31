package com.dq.sdk.ad

import com.dq.sdk.ad.config.DqAdConfig

class DqAdSdk{
    companion object{
        lateinit var config : DqAdConfig
        fun init(config :DqAdConfig){
            this.config = config
        }
    }
}