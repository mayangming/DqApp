package com.wd.daquan.imui.type.chat_layout;


import androidx.annotation.IntDef;

@IntDef({ChatLayoutParentType.LEFT,ChatLayoutParentType.RIGHT,ChatLayoutParentType.SYSTEM,ChatLayoutParentType.UN_KNOWN})
public @interface ChatLayoutParentType {
    int LEFT = 0;//左侧布局
    int RIGHT = 1;//右侧布局
    int SYSTEM = 2;//系统布局
    int UN_KNOWN = 3;//未知布局
}