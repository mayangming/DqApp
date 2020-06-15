package com.wd.daquan.chat.group.inter;

import java.util.ArrayList;

/**
 * @author: dukang
 * @date: 2018/4/19 17:52.
 * @description: 群组信息-回调监听
 */
public interface GroupGridListener {
    /**
     * 添加成员
     * @param list 目前没有用到
     */
    void addMember(ArrayList<String> list);

    /**
     * 删除成员
     */
    void removeMember();

    /**
     * 点击成员
     * @param id
     */
    void clickMember(String id);
}
