package com.dq.im.util.oss;

import java.util.List;

/**
 * 异步上传回调的简单实现
 */
public class SimpleUpLoadListener implements UpLoadListener {
    @Override
    public void uploadComplete(String url) {

    }

    @Override
    public void uploadBatchComplete(List<UpLoadBean> upLoadBeans) {

    }

    @Override
    public void uploadFail() {

    }

    @Override
    public void uploadBatchFail(List<UpLoadBean> upLoadBeans) {

    }
}