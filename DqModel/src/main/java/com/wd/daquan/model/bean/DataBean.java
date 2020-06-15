package com.wd.daquan.model.bean;


import android.text.TextUtils;

import com.wd.daquan.model.R;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.net.bean.ResponseEntity;

import java.io.Serializable;

/**
 * 数据基类
 */
public class DataBean<D> extends ResponseEntity implements Serializable {

    /**
     * 状态码
     */
    public int result = -1;
    /**‘
     * 子类数据结构
     */
    public D data = null;  //
    /**
     * 特殊接口专用的标签
     */
    public String tag = "";
    /**
     * ok/err
     */
    public String status = "";
    /**
     * 请求描述，例如请求成功，请求失败
     */
    public String content = "";
    /**
     * 消息接口扩展字段
     */
    public String msg = "";

    public String getContent() {
        return TextUtils.isEmpty(content) ? "请求出错，请重试！" : content;
    }

    public boolean isSuccess() {
        return "ok".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "result=" + result +
                ", data=" + data +
                ", tag='" + tag + '\'' +
                ", status='" + status + '\'' +
                ", content='" + content + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    /**
     * 提示
     */
    public void bequit() {
        if ("10009".equals(status)) {
            DqToast.showShort(R.string.token_err);
        } else {
            if (!TextUtils.isEmpty(msg)) {
                DqToast.showShort(msg);
            }
        }
    }

}
