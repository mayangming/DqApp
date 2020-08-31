package com.dq.sdk.ad.slot

import com.dq.sdk.ad.DqAdSdk

/**
 * 广告代码位配置
 */
class DqAdSlot {
    private var appId = ""
    var codeId = ""
    inner class Build{
      fun setCodeId(codeId :String):Build{
          this@DqAdSlot.codeId = codeId
          return this
      }

      fun build():DqAdSlot{
          this@DqAdSlot.appId = DqAdSdk.config.appId
          return this@DqAdSlot
      }
    }
}