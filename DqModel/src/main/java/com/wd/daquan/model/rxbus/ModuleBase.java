package com.wd.daquan.model.rxbus;

/**
 * 所有逻辑模块的接口
 * Created by Kind on 2018/10/10.
 */

public interface ModuleBase {

    /**
     * 主线程
     */
    void init();

    void release();
}
