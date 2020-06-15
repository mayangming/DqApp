package com.dq.im.util.oss;

import java.io.Serializable;

/**
 * 数据上传的信息类
 */
public class UpLoadBean implements Serializable {
    private int type = 0;
    private String fileName;
    private byte[] fileData;
    private String netUrl;
    private String localPath;//本地路径
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getNetUrl() {
        return netUrl;
    }

    public void setNetUrl(String netUrl) {
        this.netUrl = netUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    @Override
    public String toString() {
        return "UpLoadBean{" +
                "type=" + type +
                ", fileName='" + fileName + '\'' +
                ", netUrl='" + netUrl + '\'' +
                '}';
    }
}