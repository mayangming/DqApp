package com.dq.im.third_system;

import android.support.annotation.StringDef;

@StringDef({ThirdSystemType.XIAO_MI,ThirdSystemType.HUA_WEI,ThirdSystemType.VIVO,ThirdSystemType.OPPO})
public @interface ThirdSystemType {
    String XIAO_MI = "MI";
    String HUA_WEI = "HW";
    String VIVO = "VV";
    String OPPO = "Oppo";
}