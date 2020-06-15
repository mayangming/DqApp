package com.dq.im.util.oss;

import java.util.List;

/**
 * 异步上传文件监听回调
 */
public interface UpLoadListener{
    void uploadComplete(String url);//单文件上传结果回调
    void uploadBatchComplete(List<UpLoadBean> upLoadBeans);//多文件上传结果回调
    void uploadFail();//上传失败结果回调
    void uploadBatchFail(List<UpLoadBean> upLoadBeans);//上传失败结果回调
}