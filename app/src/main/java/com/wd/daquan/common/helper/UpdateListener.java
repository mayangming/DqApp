package com.wd.daquan.common.helper;


import com.wd.daquan.model.bean.UpdateEntity;

/**
 * @author: dukangkang
 * @date: 2018/7/16 18:14.
 * @description: todo ...
 */
public interface UpdateListener {
    void updateError();

    void updateFailed(String msg);

    void updateSucceed(UpdateEntity updateEntity);

    void updateUI(String status);
}
