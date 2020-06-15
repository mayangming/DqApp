package com.wd.daquan.contacts.listener;

import com.wd.daquan.model.bean.TeamInviteBean;

/**
 * @author: dukangkang
 * @date: 2019/2/22 18:03.
 * @description: todo ...
 */
public interface ITeamInviteListener {
    void onAgreeClick(int position, TeamInviteBean teamInviteBean);

    void onIngoreClick(int position, TeamInviteBean teamInviteBean);

    void onLongClick(int position, TeamInviteBean teamInviteBean);
}
