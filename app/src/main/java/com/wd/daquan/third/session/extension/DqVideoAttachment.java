package com.wd.daquan.third.session.extension;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.common.constant.KeyValue;

/**
 * @author: dukangkang
 * @date: 2018/9/10 14:06.
 * @description: todo ...
 */
public class DqVideoAttachment extends FileMessageContent {

    public String groupId;

    /**
     * 视频总时长
     */
    public String duration;
    /**
     * 本地视频路径
     */
    public String localPath;

    public String rotate;

    public long progress;

    public long maxProgress;

    public DqVideoAttachment() {
        super(CustomAttachmentType.QC_VIDEO);
    }

    @Override
    protected void getData(JSONObject data) {
        groupId = data.getString(KeyValue.VideoMessage.GROUP_ID);
        duration = data.getString(KeyValue.VideoMessage.DURATION);
        localPath = data.getString(KeyValue.VideoMessage.LOCALPATH);
        rotate = data.getString(KeyValue.VideoMessage.ROTATE);
        if (TextUtils.isEmpty(rotate)) {
            rotate = "90";
        }
    }

    @Override
    protected void putData(JSONObject jsonObj) {
        jsonObj.put(KeyValue.VideoMessage.GROUP_ID, groupId);
        jsonObj.put(KeyValue.VideoMessage.DURATION, duration);
        jsonObj.put(KeyValue.VideoMessage.LOCALPATH, localPath);
        jsonObj.put(KeyValue.VideoMessage.ROTATE, rotate);
    }

    @Override
    protected void putCollectionJson(JSONObject json) {
        json.put("duration", duration);
        json.put("rotate", rotate);
    }
}
