package com.dq.sdk.ad.provider

import android.content.Context
import androidx.core.content.FileProvider

class DqAdSdkFileProvider :FileProvider(){
    companion object{
        lateinit var dqAdSdkContext :Context
    }
    override fun onCreate(): Boolean {
        return super.onCreate()
        context?.let {
            dqAdSdkContext = it
        }

    }

}