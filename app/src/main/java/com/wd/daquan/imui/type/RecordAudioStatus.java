package com.wd.daquan.imui.type;

/**
 * 录音状态
 */
public enum RecordAudioStatus {
    RECORD_INIT(-1),//准备中
    RECORD_START(1),//开始
    RECORD_STOP(2),//停止
    RECORD_CANCEL(3),//撤销
    ;
    private int status;
    RecordAudioStatus(int status){
        this.status = status;
    }
}