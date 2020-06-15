package com.wd.daquan.common.alioss.callback;

import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;
import com.wd.daquan.common.alioss.AliOSS;
import com.wd.daquan.common.bean.CNOSSFileBean;
import com.wd.daquan.model.rxbus.MsgMgr;

import java.io.File;

public abstract class BaseCallback<T extends IMContentDataModel> implements AliOSS.AliOssCallback {

    public ImMessageBaseModel mMessage;

    public BaseCallback(ImMessageBaseModel message) {
        this.mMessage = message;
    }

    public BaseCallback() {

    }

    @Override
    public void onProgress(long curSize, long totalSize) {
        if (mMessage != null) {
            T customAttachment = (T) mMessage.getContentData();
            MsgMgr.getInstance().runOnUiThread(() ->  onProgress(customAttachment, curSize, totalSize));
        } else {
            MsgMgr.getInstance().runOnUiThread(() ->  onProgress(null, curSize, totalSize));
        }
    }

    @Override
    public void onSuccess(long totalSize, File file, CNOSSFileBean bean) {
        if (mMessage != null) {
            T customAttachment = (T) mMessage.getContentData();
            MsgMgr.getInstance().runOnUiThread(() ->  onSuccess(customAttachment, file, bean, totalSize));
        } else {
            MsgMgr.getInstance().runOnUiThread(() ->  onSuccess(null, file, bean, totalSize));
        }

        //Log.e("FZ", "file ： " + file.getName());
    }

    @Override
    public void onFailure() {
        MsgMgr.getInstance().runOnUiThread(() -> onFailed());

    }

    public void onFailed() {

    }

    protected abstract void onSuccess(T messageContent, File file, CNOSSFileBean bean, long totalSize);

    protected abstract void onProgress(T messageContent, long curSize, long totalSize);

}