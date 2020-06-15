package com.wd.daquan.common.alioss.callback;

import com.dq.im.model.ImMessageBaseModel;
import com.wd.daquan.common.bean.CNOSSFileBean;
import com.wd.daquan.third.session.extension.FileMessageContent;

import java.io.File;

/**
 * @Author: 方志
 * @Time: 2019/5/6 19:14
 * @Description:
 */
public class FileCallBack<T extends FileMessageContent> extends BaseCallback<T>{


    public FileCallBack(ImMessageBaseModel message) {
        super(message);
    }

    public FileCallBack() {
        super(null);
    }

    @Override
    protected void onSuccess(T messageContent, File file, CNOSSFileBean bean, long totalSize) {
        if (messageContent != null){
            messageContent.etag = bean.eTag;
            messageContent.fileWebUrl = bean.fileName;
            messageContent.fileWebHttpUrl = bean.host + bean.fileName;
        }

        if(mMessage != null) {
            mMessage.setContentData(messageContent);
        }
    }

    @Override
    protected void onProgress(T messageContent, long curSize, long totalSize) {
        if (messageContent != null){
            messageContent.progress = curSize;
            messageContent.maxProgress = totalSize;
        }

    }
}
