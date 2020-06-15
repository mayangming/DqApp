package com.wd.daquan.contacts.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.TeamInviteBean;
import com.wd.daquan.contacts.holder.TeamInviteHolder;
import com.wd.daquan.contacts.listener.ITeamInviteListener;
import com.wd.daquan.glide.GlideUtils;

import org.jetbrains.annotations.NotNull;

/**
 */
public class TeamInviteAdapter extends CommRecyclerViewAdapter<TeamInviteBean, TeamInviteHolder> {

    private ITeamInviteListener mITeamInviteListener = null;

    @Override
    protected TeamInviteHolder onBindView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_invite_item,
                parent, false);
        return new TeamInviteHolder(view);
    }

    @Override
    protected void onBindData(@NotNull TeamInviteHolder holder, int position) {
        TeamInviteBean inviteBean = getItem(position);
        if (inviteBean == null) {
            return;
        }

        GlideUtils.loadHeader(mContext, inviteBean.icon, holder.mAvatar);
        holder.mTeamName.setText(inviteBean.groupName);
        holder.mInviteName.setText("邀请人：" + inviteBean.inviterName);
        if(KeyValue.ZERO_STRING.equals(inviteBean.responseStatus)){
            holder.mBtnLlyt.setVisibility(View.VISIBLE);
            holder.mStatus.setVisibility(View.GONE);
        }else if(KeyValue.ONE_STRING.equals(inviteBean.responseStatus)){
            holder.mBtnLlyt.setVisibility(View.GONE);
            holder.mStatus.setVisibility(View.VISIBLE);
            holder.mStatus.setText("已同意");
        }else if(KeyValue.TWO_STRING.equals(inviteBean.responseStatus)){
            holder.mBtnLlyt.setVisibility(View.GONE);
            holder.mStatus.setVisibility(View.VISIBLE);
            holder.mStatus.setText("已忽略");
        }

        holder.mAgree.setOnClickListener(v -> {
            if (mITeamInviteListener != null) {
                mITeamInviteListener.onAgreeClick(position, inviteBean);
            }
        });

        holder.mIgnore.setOnClickListener(v -> {
            if (mITeamInviteListener != null) {
                mITeamInviteListener.onIngoreClick(position, inviteBean);
            }
        });

        holder.mView.setOnLongClickListener(v -> {
            if (mITeamInviteListener != null) {
                mITeamInviteListener.onLongClick(position, inviteBean);
            }
            return true;
        });
    }

    public void setITeamInviteListener(ITeamInviteListener ITeamInviteListener) {
        mITeamInviteListener = ITeamInviteListener;
    }
}
