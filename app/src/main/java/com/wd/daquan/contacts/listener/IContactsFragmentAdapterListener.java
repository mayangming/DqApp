package com.wd.daquan.contacts.listener;

import com.da.library.widget.DragPointView;

/**
 * @Author: 方志
 * @Time: 2018/9/6 15:00
 * @Description: 联系人数据监听
 */
public interface IContactsFragmentAdapterListener {
    void onItemClick(int position);

    void onFresh();

    void onHeaderNewFriend(DragPointView mNewFriendNotify);
}
