package com.wd.daquan.contacts.listener;

import androidx.annotation.NonNull;

import com.wd.daquan.model.bean.Friend;

/**
 * @Author: 方志
 * @Time: 2018/9/26 17:01
 * @Description:
 */
public interface ISearchFriendAdapterItemClickListener {
    void onItemClick(@NonNull Friend friend);
}
