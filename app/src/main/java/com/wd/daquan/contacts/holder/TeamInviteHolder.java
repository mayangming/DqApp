package com.wd.daquan.contacts.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * @author: dukangkang
 * @date: 2019/2/22 17:55.
 * @description: todo ...
 */
public class TeamInviteHolder extends RecyclerView.ViewHolder {

    public View mView;
    // 头像
    public ImageView mAvatar;
    // 同意
    public TextView mAgree;
    // 忽略
    public TextView mIgnore;
    // 已添加/已忽略
    public TextView mStatus;
    // 群组名称
    public TextView mTeamName;
    // 邀请人
    public TextView mInviteName;

    // 按钮布局
    public LinearLayout mBtnLlyt;

    public TeamInviteHolder(View view) {
        super(view);
        this.mView = view;
        mAvatar = view.findViewById(R.id.team_invite_avatar);
        mAgree = view.findViewById(R.id.team_invite_agree);
        mIgnore = view.findViewById(R.id.team_invite_ignore);
        mStatus = view.findViewById(R.id.team_invite_status);
        mBtnLlyt = view.findViewById(R.id.team_invite_btnllyt);
        mTeamName = view.findViewById(R.id.team_invite_teamname);
        mInviteName = view.findViewById(R.id.team_invite_invitename);
    }

}
