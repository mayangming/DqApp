package com.wd.daquan.chat.group.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;

import com.wd.daquan.R;
import com.wd.daquan.model.db.helper.MemberDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.chat.group.adapter.GroupGridAdapter;
import com.wd.daquan.chat.group.inter.GroupGridListener;
import com.wd.daquan.chat.group.view.SearchMemberView;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.da.library.widget.CommSearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * 群成员更多
 * Created by Kind on 2018/9/25.
 */

public class TotalGroupMemberActivity extends DqBaseActivity<ChatPresenter, DataBean> implements QCObserver {

    private boolean isMaster = false;
    private boolean isAdmin = false;

    private String mGroupId;
    private String mNickName;

    private GridView mGridView;
    private GroupGridAdapter mAdapter;
    private GroupInfoBean mGroupResp;
    private ArrayList<GroupMemberBean> mMemberList;

    private CommSearchView mCommSearchView = null;
    private SearchMemberView mSearchMemberView = null;

    @Override
    protected ChatPresenter createPresenter() {
        return null;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_total_group_member);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initView() {
        setBackView();
        Intent intent = getIntent();
        if (null == intent) {
            return;
        }
        mGroupId = intent.getStringExtra(KeyValue.GROUP_ID);
        mGroupResp =  intent.getParcelableExtra(KeyValue.GROUP_RESPONSE);
        mNickName = intent.getStringExtra(KeyValue.GROUP_REMARK);
        isMaster = intent.getBooleanExtra(KeyValue.AddUserDetail.IS_MASTER, false);
        isAdmin = intent.getBooleanExtra(KeyValue.AddUserDetail.IS_ADMIN, false);

        if (mGroupResp == null || TextUtils.isEmpty(mGroupResp.group_id)) {
            DqToast.showShort("请求出错，请重试！");
            this.finish();
            return;
        }

        mGridView = findViewById(R.id.gridview);
        mCommSearchView = this.findViewById(R.id.comm_search_view);

        mAdapter = new GroupGridAdapter(this);
      //  mAdapter.setNickName(mNickName);
        GroupMemberBean.GrouppostType grouppostType;
        if(isMaster){
            grouppostType = GroupMemberBean.GrouppostType.LORD;
        }else if(isAdmin){
            grouppostType = GroupMemberBean.GrouppostType.ADMIN;
        }else {
            grouppostType = GroupMemberBean.GrouppostType.MEMBER;
        }
        mAdapter.setMaster(grouppostType);
        mGridView.setAdapter(mAdapter);

        mSearchMemberView = this.findViewById(R.id.total_group_searchmember_rlyt);
        mSearchMemberView.setNickName(mNickName);
    }

    @Override
    protected void initData() {
        mMemberList = new ArrayList<>();
        mMemberList.addAll(mGroupResp.memberList);
        mAdapter.replace(mMemberList);
        mSearchMemberView.setData(mMemberList);

        setTitle("群成员(" + mMemberList.size() + ")");
    }

    @Override
    protected void initListener() {
        MsgMgr.getInstance().attach(this);
        mAdapter.setGridListener(mGroupGridListener);
        mCommSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommSearchView.setVisibility(View.GONE);
                mSearchMemberView.showSoftInput();
            }
        });

        mSearchMemberView.setSearchMemberListener(new SearchMemberView.SearchMemberListener() {
            @Override
            public void onFinish() {
                mCommSearchView.setVisibility(View.VISIBLE);
                mSearchMemberView.hideSoftInput();
            }

            @Override
            public void onItemClick(String userId) {
                //查询自己在群组的信息
                boolean isAdmin = false;
                GroupMemberBean groupMemberInfo =  MemberDbHelper.getInstance().getTeamMember(mGroupId, ModuleMgr.getCenterMgr().getUID());
                if(groupMemberInfo != null) {
                    isAdmin = groupMemberInfo.isHighRole();
                }
                NavUtils.gotoUserInfoActTeam(TotalGroupMemberActivity.this, mGroupId, userId, isAdmin);
            }
        });
    }

    private GroupGridListener mGroupGridListener = new GroupGridListener() {
        @Override
        public void addMember(ArrayList<String> list) {
            NavUtils.gotoSelectFriendsActivity(getActivity(), mGroupResp.getGroup_id());
        }

        @Override
        public void removeMember() {
            NavUtils.gotoSelectFriendsActivity(getActivity(), mGroupResp.getGroup_id(), isMaster);
        }

        @Override
        public void clickMember(String id) {
            //查询自己在群组的信息
            boolean isAdmin = false;
            GroupMemberBean groupMemberInfo = MemberDbHelper.getInstance().getTeamMember(mGroupId, ModuleMgr.getCenterMgr().getUID());
            if(groupMemberInfo != null) {
                isAdmin = groupMemberInfo.isHighRole();
            }
            NavUtils.gotoUserInfoActTeam(TotalGroupMemberActivity.this, mGroupId, id, isAdmin);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            List<Friend> newMemberData = (List<Friend>) data.getSerializableExtra("newAddMember");
            List<Friend> deleMember = (List<Friend>) data.getSerializableExtra("deleteMember");
            if (newMemberData != null && newMemberData.size() > 0) {
                for (Friend friend : newMemberData) {
                    mMemberList.add(1, new GroupMemberBean(mGroupId, friend.uid, friend.nickname, friend.headpic));
                }
                if (null != mAdapter) {
                    mAdapter.replace(mMemberList);
                }
            } else if (deleMember != null && deleMember.size() > 0) {
                for (Friend friend : deleMember) {
                    for (GroupMemberBean member : mMemberList) {
                        if (member.uid.equals(friend.uid)) {
                            mMemberList.remove(member);
                            break;
                        }
                    }
                }
                if (null != mAdapter) {
                    mAdapter.replace(mMemberList);
                }
            }

            // 更新群名称
            setTitle("群成员(" + mMemberList.size() + ")");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);

    }

    @Override
    public void onMessage(String key, Object value) {
        if(key.equals(MsgType.MT_FRIEND_REMARKS_CHANGE)){////好友备注变更
            if(mAdapter != null){
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
