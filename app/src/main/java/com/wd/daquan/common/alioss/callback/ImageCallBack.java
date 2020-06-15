package com.wd.daquan.common.alioss.callback;

import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.model.ImMessageBaseModel;
import com.wd.daquan.common.bean.CNOSSFileBean;

import java.io.File;

/**
 * @Author: 方志
 * @Time: 2019/5/6 14:55
 * @Description:
 */
public class ImageCallBack extends BaseCallback<MessagePhotoBean> {

    public ImageCallBack(ImMessageBaseModel message) {
        super(message);
    }

    protected ImageCallBack() {
        super(null);
    }

    @Override
    protected void onSuccess(MessagePhotoBean messageContent, File file, CNOSSFileBean bean, long totalSize) {

    }

    @Override
    protected void onProgress(MessagePhotoBean messageContent, long curSize, long totalSize) {

    }
}
