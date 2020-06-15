package com.wd.daquan.third.session.extension;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;

/**
 * 转账消息
 * Created by Kind on 2019-05-27.
 */
public class QcTransferAttachment extends CustomAttachment implements Parcelable {

    public String receiveId;
    public String receiveName;
    public String receivePic;
    public String sendId;
    public String sendName;
    public String sendPic;
    public String transferId;
    public String blessing;//转账备注
    public String amount;
    public String create_time;
    public String receive_time;
    public String expire_time;

    public QcTransferAttachment() {
        super(CustomAttachmentType.QC_TRANSFER_ATTACHMENT);
    }

    @Override
    protected void parseData(JSONObject jsonStr) {
        receiveId = jsonStr.getString("receiveId");
        receiveName = jsonStr.getString("receiveName");
        receivePic = jsonStr.getString("receivePic");
        sendId = jsonStr.getString("sendId");
        sendName = jsonStr.getString("sendName");
        sendPic = jsonStr.getString("sendPic");
        transferId = jsonStr.getString("transferId");

        blessing = jsonStr.getString("blessing");
        amount = jsonStr.getString("amount");

    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("receiveId", receiveId);
            jsonObject.put("receiveName", receiveName);
            jsonObject.put("receivePic", receivePic);
            jsonObject.put("sendId", sendId);
            jsonObject.put("sendName", sendName);

            jsonObject.put("sendPic", sendPic);
            jsonObject.put("transferId", transferId);
            jsonObject.put("blessing", blessing);

            jsonObject.put("amount", amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.receiveId);
        dest.writeString(this.receiveName);
        dest.writeString(this.receivePic);
        dest.writeString(this.sendId);
        dest.writeString(this.sendName);
        dest.writeString(this.sendPic);
        dest.writeString(this.transferId);
        dest.writeString(this.blessing);
        dest.writeString(this.amount);
    }

    protected QcTransferAttachment(Parcel in) {
        this();
        this.receiveId = in.readString();
        this.receiveName = in.readString();
        this.receivePic = in.readString();
        this.sendId = in.readString();
        this.sendName = in.readString();
        this.sendPic = in.readString();
        this.transferId = in.readString();
        this.blessing = in.readString();
        this.amount = in.readString();
    }

    public static final Parcelable.Creator<QcTransferAttachment> CREATOR = new Parcelable.Creator<QcTransferAttachment>() {
        @Override
        public QcTransferAttachment createFromParcel(Parcel source) {
            return new QcTransferAttachment(source);
        }

        @Override
        public QcTransferAttachment[] newArray(int size) {
            return new QcTransferAttachment[size];
        }
    };
}
