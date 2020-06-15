package com.wd.daquan.contacts.listener;

import com.wd.daquan.model.bean.NewFriendBean;

/**
 * @Author: 方志
 * @Time: 2018/9/14 11:57
 * @Description:
 */
public interface INewFriendAdapterClickListener {
    void onItemClick(int position, NewFriendBean item);

    void onAgreeClick(int position, NewFriendBean item);

    void onItemLongClick(int position, NewFriendBean item);
}
