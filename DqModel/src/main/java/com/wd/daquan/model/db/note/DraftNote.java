package com.wd.daquan.model.db.note;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 聊天记忆
 *  草稿
 */
@Entity
public class DraftNote{
    @Id(autoincrement = true)
    public Long id;//主键

    /**
     * unique唯一标识
     */
    @Index(unique = true)
    public String sessionID;

    public String sessionType;
    public String content;//草案文本
    public long time;
    @Generated(hash = 2014505170)
    public DraftNote(Long id, String sessionID, String sessionType, String content,
            long time) {
        this.id = id;
        this.sessionID = sessionID;
        this.sessionType = sessionType;
        this.content = content;
        this.time = time;
    }
    @Generated(hash = 1394114966)
    public DraftNote() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSessionID() {
        return this.sessionID;
    }
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
    public String getSessionType() {
        return this.sessionType;
    }
    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
}
