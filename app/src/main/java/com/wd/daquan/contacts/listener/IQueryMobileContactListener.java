package com.wd.daquan.contacts.listener;

import com.wd.daquan.contacts.bean.MobileContactBean;

import java.util.List;

/**
 * @Author: 方志
 * @Time: 2018/9/17 14:49
 * @Description:
 */
public interface IQueryMobileContactListener {
    void onQueryComplete(List<MobileContactBean> data);
}
