package com.wd.daquan.chat.watch;

import android.os.Parcel;
import android.os.Parcelable;

import com.dq.im.model.ImMessageBaseModel;

/**
 * @author: dukangkang
 * @date: 2018/7/5 14:49.
 * @description: todo ...
 */
public class PictureEntity implements Parcelable {
    // 消息类型
    public int type;
//    // 消息ID
    public String messageId;
    // 发送时间
    public long sentTime;
    // 视频文件大小
    public long fileSize;

//    public Uri thumbUri;
//
//    public Uri largeImageUri;

    public String path;

    public String thumbPath;

    public String localPath;

    public String fileWebUrl;

    public String fileWebHttpUrl;

//    public String firstFrameImage; // 此字段废弃

    public ImMessageBaseModel message;

    public interface MessageType {
        // 图片
        public int MESSAGE_IMAGE = 1;
        // 视频
        public int MESSAGE_VIDEO = 2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.messageId);
        dest.writeLong(this.sentTime);
        dest.writeLong(this.fileSize);
        dest.writeString(this.path);
        dest.writeString(this.thumbPath);
        dest.writeString(this.localPath);
        dest.writeString(this.fileWebUrl);
        dest.writeString(this.fileWebHttpUrl);
        dest.writeSerializable(this.message);
    }

    public PictureEntity() {
    }

    protected PictureEntity(Parcel in) {
        this.type = in.readInt();
        this.messageId = in.readString();
        this.sentTime = in.readLong();
        this.fileSize = in.readLong();
        this.path = in.readString();
        this.thumbPath = in.readString();
        this.localPath = in.readString();
        this.fileWebUrl = in.readString();
        this.fileWebHttpUrl = in.readString();
        this.message = (ImMessageBaseModel) in.readSerializable();
    }

    public static final Creator<PictureEntity> CREATOR = new Creator<PictureEntity>() {
        @Override
        public PictureEntity createFromParcel(Parcel source) {
            return new PictureEntity(source);
        }

        @Override
        public PictureEntity[] newArray(int size) {
            return new PictureEntity[size];
        }
    };
}
