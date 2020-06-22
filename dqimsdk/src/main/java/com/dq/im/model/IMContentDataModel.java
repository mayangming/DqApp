package com.dq.im.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * IM聊天中的具体实现类
 */
public class IMContentDataModel implements Serializable{
    private int mentionedType = 0;
    private int type = 0;
    private int[] mentionedTargets = new int[0];
    public String toJson(boolean var1){
        return "";
    }
}