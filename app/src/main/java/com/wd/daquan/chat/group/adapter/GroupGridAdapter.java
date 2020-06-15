package com.wd.daquan.chat.group.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.R;
import com.da.library.adapter.CommAdapter;
import com.wd.daquan.chat.group.adapter.holder.MemberHolder;
import com.wd.daquan.chat.group.inter.GroupGridListener;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.glide.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 群设置顶部成员
 * Created by Kind on 2018/9/10.
 */

public class GroupGridAdapter extends CommAdapter<GroupMemberBean, MemberHolder> {

    // 是否是群主
    private GroupMemberBean.GrouppostType grouppostType;

    private GroupGridListener mGridListener = null;

    public GroupGridAdapter(Context context) {
        super(context);
    }

    /**
     * 当前用户的权限
     *
     * @param grouppostType
     */
    public void setMaster(GroupMemberBean.GrouppostType grouppostType) {
        this.grouppostType = grouppostType;
    }

    public GroupMemberBean.GrouppostType isMaster() {
        return grouppostType;
    }

    public void setGridListener(GroupGridListener gridListener) {
        mGridListener = gridListener;
    }

    @Override
    public int getCount() {
        if (null == mList) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public void replace(List<GroupMemberBean> list) {
        //当前成员的个数
        int groupMemberInfo = 0;
        List<GroupMemberBean> rmList = new ArrayList<>();
        for (GroupMemberBean tmp : list) {
            if(TextUtils.isEmpty(tmp.uid)){
                rmList.add(tmp);
                continue;
            }

            boolean isAdd = false;
            if (isMaster() == GroupMemberBean.GrouppostType.LORD) {
                isAdd = !tmp.isGroupMaster();
            }else if(isMaster() == GroupMemberBean.GrouppostType.ADMIN){
                isAdd = !tmp.isHighRole();
            }

            if (isAdd) {
                groupMemberInfo += 1;
            }
        }

        if(rmList.size() > 0){
            list.removeAll(rmList);
        }

        list.add(new GroupMemberBean(2));
        if (isLordOrAdmin() && groupMemberInfo > 0) {
            list.add(new GroupMemberBean(3));
        }
        super.replace(list);
    }


    /**
     * 是否群主或管理员
     *
     * @return
     */
    private boolean isLordOrAdmin() {
        if (isMaster() == GroupMemberBean.GrouppostType.LORD || isMaster() == GroupMemberBean.GrouppostType.ADMIN) {
            return true;
        }
        return false;
    }

    @Override
    public MemberHolder onCreateViewHolder(View convertView, ViewGroup parent, int position) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.cn_group_detail_item, parent, false);
        return new MemberHolder(convertView);
    }

    @Override
    public void onBindViewHolder(MemberHolder memberHolder, int position) {
        if (mList == null || null == memberHolder) {
            return;
        }
        GroupMemberBean groupMember = mList.get(position);
        if (groupMember.tmpMemberShowStatus == 2) {
            memberHolder.name.setText("");
            memberHolder.delete.setVisibility(View.GONE);
            memberHolder.master.setVisibility(View.GONE);
            GlideUtils.load(mContext, R.mipmap.comm_add_btn, memberHolder.portrait);
            memberHolder.portrait.setOnClickListener(v -> {
                if (null != mGridListener) {
                    mGridListener.addMember(null);
                }
            });
        } else if (groupMember.tmpMemberShowStatus == 3) {
            memberHolder.name.setText("");
            memberHolder.delete.setVisibility(View.GONE);
            memberHolder.master.setVisibility(View.GONE);
            GlideUtils.load(mContext, R.mipmap.comm_delete_btn, memberHolder.portrait);
            memberHolder.portrait.setOnClickListener(v -> {
                if (null != mGridListener) {
                    mGridListener.removeMember();
                }
            });
        } else {
            if (groupMember.getRoleType() == GroupMemberBean.GrouppostType.LORD) {//群主
                memberHolder.master.setVisibility(View.VISIBLE);
                memberHolder.master.setImageResource(R.mipmap.team_master);
            } else if (groupMember.getRoleType() == GroupMemberBean.GrouppostType.ADMIN) {//管理员
                memberHolder.master.setVisibility(View.VISIBLE);
                memberHolder.master.setImageResource(R.mipmap.team_manager);
            } else {
                memberHolder.master.setVisibility(View.GONE);
            }
            memberHolder.name.setText(showName(groupMember));

            // 设置头像
            GlideUtils.loadHeader(mContext, groupMember.headpic, memberHolder.portrait);
            memberHolder.portrait.setOnClickListener(v -> {
                if (null != mGridListener) {
                    mGridListener.clickMember(groupMember.getUid());
                }
            });
        }

        if (groupMember.isVip()){
            memberHolder.vipHeadOutLine.setVisibility(View.VISIBLE);
        }else {
            memberHolder.vipHeadOutLine.setVisibility(View.INVISIBLE);
        }
    }

    private String showName(GroupMemberBean groupMember) {
        if (groupMember.uid.equals(ModuleMgr.getCenterMgr().getUID())) {
            return groupMember.getGroupDisplayName();
        }

        Friend temp = FriendDbHelper.getInstance().getFriend(groupMember.uid);
        if (temp != null && !TextUtils.isEmpty(temp.getRemarks())) {
            return temp.getRemarks();
        }
        return groupMember.getGroupDisplayName();
    }
}