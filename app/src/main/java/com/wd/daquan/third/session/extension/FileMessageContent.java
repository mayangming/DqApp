package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.common.constant.KeyValue;

/**
 * 文件消息base
 */
public abstract class FileMessageContent extends CustomAttachment {
    /**
     * 文件名
      */
    public String name;
    /**
     * 文件大小
     */
    public long file_size;
    /**
     * 文件类型
     */
    public String type;
    /**
     * 文件地址-(不包含域名)
     */
    public String fileWebUrl;
    /**
     * 文件地址-(包含域名)
     */
    public String fileWebHttpUrl;
    /**
     * 空间名
     */
    public String bucket;
    /**
     * 网络资源唯一标示,IOS使用
     */
    public String etag;
    /**
     * 附加信息
     */
    public String extra;
    /**
     * 当前进度
     */
    public long progress;
    /**
     * 最大进度
     */
    public long maxProgress;

    /**
     * 是否android平台，前提是自己判断fileWebHttpUrl是否为空
     * @return
     */
    public boolean isAndroidPlatform(){
        return fileWebHttpUrl.contains("android");
    }

    public FileMessageContent(String attachmentType) {
        super(attachmentType);
    }

    @Override
    protected void parseData(JSONObject data) {
        file_size = data.getLong(KeyValue.VideoMessage.FILE_SIZE);
        bucket = data.getString(KeyValue.VideoMessage.BUCKET);
        fileWebUrl = data.getString(KeyValue.VideoMessage.FILE_WEBURL);
        fileWebHttpUrl = data.getString(KeyValue.VideoMessage.FILE_WEB_HTTPURL);
        etag = data.getString(KeyValue.VideoMessage.ETAG);
        extra = data.getString(KeyValue.VideoMessage.EXTRA);

        getData(data);
    }

    protected abstract void getData(JSONObject data);

    @Override
    protected JSONObject packData() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KeyValue.VideoMessage.FILE_SIZE, file_size);
        jsonObj.put(KeyValue.VideoMessage.BUCKET, bucket);
        jsonObj.put(KeyValue.VideoMessage.FILE_WEBURL, fileWebUrl);
        jsonObj.put(KeyValue.VideoMessage.FILE_WEB_HTTPURL, fileWebHttpUrl);
        jsonObj.put(KeyValue.VideoMessage.ETAG, etag);
        jsonObj.put(KeyValue.VideoMessage.EXTRA, extra);

        putData(jsonObj);
        return jsonObj;
    }

    protected abstract void putData(JSONObject jsonObj);

    /**
     * 收藏时候拼接的数据
     *
     * @return
     */
    public String stitchingCollectionJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("file_size", file_size);
            putCollectionJson(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    protected void putCollectionJson(JSONObject json){}


    public String getFileWebUrl() {
        return fileWebUrl == null ? "" : fileWebUrl;
    }

    public String getFileWebHttpUrl() {
        return fileWebHttpUrl == null ? "" : fileWebHttpUrl;
    }
}
