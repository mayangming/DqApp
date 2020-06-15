package com.wd.daquan.contacts.listener;

import com.wd.daquan.contacts.bean.MobileContactBean;

/**
 * @Author: 方志
 * @Time: 2018/9/17 18:33
 * @Description:
 */
public interface IMobileContactsListAdapterClickListener {
    void onItemClick(int position, MobileContactBean item);

    void onAgreeClick(int position, MobileContactBean item);
}
