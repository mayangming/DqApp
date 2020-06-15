package com.wd.daquan.chat.group.activity;


import android.view.View;
import android.widget.Toast;

import com.da.library.constant.IConstant;
import com.da.library.listener.DialogListener;
import com.da.library.tools.Utils;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.CreateTeamEntity;
import com.wd.daquan.chat.friend.BaseSelectActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.contacts.helper.TransformFriendInfoHelper;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.db.helper.MemberDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.third.session.SessionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择好友
 * Created by Kind on 2018/9/25.
 */

public class SelectFriendsActivity extends BaseSelectActivity {

    private boolean isFromSingle;//从单聊界面
    private Friend singleFriend;//从单聊界面过来的
    private boolean isDeleteGroupMember;
    private boolean isAddGroupMember;
    private String groupId;
    // 是否是群主
    private boolean isMaster = false;


    @Override
    public void initData() {
        super.initData();
        singleFriend = getIntent().getParcelableExtra("singleFriend");
        isFromSingle = getIntent().getBooleanExtra("isFromSingle", false);
        groupId = getIntent().getStringExtra(KeyValue.GROUPID);
        isAddGroupMember = getIntent().getBooleanExtra(KeyValue.IS_ADD_GROUP_MEMBER, false);
        isDeleteGroupMember = getIntent().getBooleanExtra(KeyValue.IS_DELETE_GROUP_MEMBER, false);
        isMaster = getIntent().getBooleanExtra(KeyValue.IS_MASTERS, false);

        if (isFromSingle && singleFriend != null) {//单聊
            setTitle(DqApp.getStringById(R.string.create_a_new_chat));
            mSelectTeamRlyt.setVisibility(View.GONE);
            List<Friend> allFriend = FriendDbHelper.getInstance().getAllFriendAndSelf();
            if(allFriend.size() > 0) {
                updateData(allFriend);
            }else {
                getFriendList();
            }
        } else {
            initTeam();
        }
        //isAddGroupMember 判断是不是要添加群成员

    }

    private void initTeam() {
        if (isAddGroupMember) {
            setTitle(DqApp.getStringById(R.string.add_friend));
            List<Friend> allFriend = FriendDbHelper.getInstance().getAllFriendAndSelf();
            if(allFriend.size() > 0) {
                mFriendList.clear();
                mFriendList.addAll(allFriend);
                getTeamMembers();
            }else {
                getFriendList();
            }
        } else if(isDeleteGroupMember){
            setTitle(DqApp.getStringById(R.string.title_select_friend_delete));
            getTeamMembers();
        }

        if (isDeleteGroupMember || isAddGroupMember) {
            mSelectTeamRlyt.setVisibility(View.GONE);
        } else {
            mSelectTeamRlyt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onTitleRightClick() {
        onSubmit();
    }


    /**
     * 更新数据
     */
    @Override
    protected void updateData(List<Friend> friendList) {

        //添加拼音
        List<Friend> list = TransformFriendInfoHelper.getInstance().setPinYin(friendList);
        if(null != list) {
            mFriendList.clear();
            mFriendList.addAll(list);
        }

        if(mFriendList.size() <= 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        }else {
            mEmptyView.setVisibility(View.GONE);
        }

        //按拼音排序
        Collections.sort(mFriendList, (o1, o2) -> {
            if (o1.pinYin.equals("@") || o2.pinYin.equals("#")) {
                return -1;
            } else if (o1.pinYin.equals("#") || o2.pinYin.equals("@")) {
                return 1;
            } else {
                return o1.pinYin.compareTo(o2.pinYin);
            }
        });

        if(isFromSingle) {
            String uid = ModuleMgr.getCenterMgr().getUID();
            for (Friend friend : mFriendList) {
                if(friend == null) {
                    continue;
                }
                if(friend.getUid().equals(uid) || friend.getUid().equals(singleFriend.getUid())) {
                    mNoSelectList.add(friend);
                }
            }
        }

        mFriendAdapter.setNotSelectMember(mNoSelectList);
        mFriendAdapter.update(mFriendList);
    }


    private void onSubmit() {
        Utils.hideSoftInput(this, mSearchEditText.getEditText());
        if (mSelectList.size() > 0) {
            if(isFromSingle) {
                mSelectList.add(singleFriend);
                createGroup();
            }else {
                if(isAddGroupMember) {
                    requestAddGroupMember();
                }else if(isDeleteGroupMember) {
                    requestDeleteGroupMember();
                }
            }
        } else {
            Toast.makeText(SelectFriendsActivity.this, "无数据", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 群组删除人
     */
    private void requestDeleteGroupMember() {
        String groupUids = TransformFriendInfoHelper.getInstance().jointId(mSelectList, false);
        String groupNames = TransformFriendInfoHelper.getInstance().jointNames(mSelectList, false);
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(KeyValue.Group.GROUP_UID, groupUids); // 成员ID
        hashMap.put(KeyValue.Group.GROUP_NAME, groupNames); // 成员name
        hashMap.put(KeyValue.GROUP_ID, groupId);

        mPresenter.jsonGroup(DqUrl.url_kick_group, hashMap);
    }

    /**
     * 群组添加人
     */
    private void requestAddGroupMember() {
        String groupUids = TransformFriendInfoHelper.getInstance().jointId(mSelectList, false);
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(KeyValue.Group.GROUP_UID, groupUids); // 成员ID
        hashMap.put("type", "1");//1邀请加入 2二维码
        hashMap.put(KeyValue.GROUP_ID, groupId);

        mPresenter.jsonGroup(DqUrl.url_join_group, hashMap);
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        DqToast.showShort(entity == null ? "请求出错！" : entity.getContent());
    }

    @Override
    public void requestData() {
        if(isFromSingle) {
            getFriendList();
        }else {
            getTeamMembers();
        }
    }

    private void getTeamMembers() {
        Map<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("group_id", groupId);
        mPresenter.getTeamMember(DqUrl.url_select_group_user, hashMap);
    }

    private void getFriendList() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.Contact.WHETHER_HELPER, "1");
        hashMap.put(IConstant.Contact.SHOW_SELF, "1");
        hashMap.put(IConstant.Contact.LAST_TIME, "1");
        mPresenter.getFriendList(DqUrl.url_friend_list, hashMap);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if(entity == null) return;
        if (!entity.isSuccess()) {
            DqToast.showShort(entity.getContent());
            return;
        }

        if (url.equals(DqUrl.url_friend_list)) {//好友列表接口
            List<Friend> friendList = (List<Friend>) entity.data;
            if(friendList != null && friendList.size() > 0) {
                if(isFromSingle) {
                    updateData(friendList);
                }else if(isAddGroupMember){
                    mFriendList.clear();
                    mFriendList.addAll(friendList);
                    getTeamMembers();
                }
            }
        } else if (DqUrl.url_kick_group.equals(url)) {//批量退群
            //群成员变更
            MsgMgr.getInstance().sendMsg(MsgType.MT_GROUP_SETTING_MEMBER_CHANGE, null);
            List<String> idList = new ArrayList<>();
            for (Friend friend : mSelectList) {
                String key = groupId + friend.uid;
                idList.add(key);
            }
            MemberDbHelper.getInstance().delete(idList);
            DqToast.showShort("移除成功");
            finish();
        } else if (url.equals(DqUrl.url_select_group_user)) {
            //查询群成员
            initGroupMenber(entity);
        } else if (url.equals(DqUrl.url_join_group)) {//批量添加人
            CreateTeamEntity teamEntity = (CreateTeamEntity) entity.data;
            if (teamEntity != null && teamEntity.hasRefuse()) {
                DialogUtils.showRefuesInviteDialog(this, teamEntity.getRefuseName(), teamEntity.getRefuseCount()+"", new DialogListener() {
                    @Override
                    public void onCancel() {}

                    @Override
                    public void onOk() {
                        //群成员变更
                        MsgMgr.getInstance().sendMsg(MsgType.MT_GROUP_SETTING_MEMBER_CHANGE, null);
                        finish();
                    }
                });
            } else {
                //群成员变更
                MsgMgr.getInstance().sendMsg(MsgType.MT_GROUP_SETTING_MEMBER_CHANGE, null);
                finish();
            }
        } else if (url.equals(DqUrl.url_create_group)) {//创建组
            DqToast.showShort("群组创建成功");
            CreateTeamEntity teamEntity = (CreateTeamEntity) entity.data;
            if (teamEntity == null) {
                return;
            }
            if (teamEntity.hasRefuse()) {
                DialogUtils.showRefuesInviteDialog(this, teamEntity.getRefuseName(), teamEntity.getRefuseCount()+"", new DialogListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onOk() {
                        startTeam(teamEntity);
                    }
                });
            } else {
                startTeam(teamEntity);
            }
        }
    }

    private void initGroupMenber(DataBean entity) {
        List<Friend> friendList = (List<Friend>) entity.data;
        if(friendList != null && friendList.size() > 0) {
            if (isAddGroupMember && mFriendList.size() > 0) {
                for(Friend friend : mFriendList){
                    for(Friend tmp : friendList){
                        if (tmp.uid.equals(friend.uid)) {
                            mNoSelectList.add(friend);
                            break;
                        }
                    }
                }
            } else if(isDeleteGroupMember){
                mFriendList.clear();
                //非添加好友到群聊
                for(Friend friend : friendList){
                    // 跳过群主
                    if (friend.isGroupMaster()) {
                        mNoSelectList.add(friend);
                        continue;
                    }

                    // 非群主操作，跳过管理员
                    if (!isMaster && friend.isAdmin()) {
                        mNoSelectList.add(friend);
                        continue;
                    }
                    mFriendList.add(friend);
                }
            }
            updateData(mFriendList);
        }
    }

    private void startTeam(CreateTeamEntity entity) {
        SessionHelper.startTeamSession(this, entity.group_id);
        setResult(KeyValue.RESULTCODE_CREATE_GROUP);
        finish();
    }


    //创建群组
    private void createGroup() {
        String groupUids = TransformFriendInfoHelper.getInstance().jointId(mSelectList, true);
        String groupNames = TransformFriendInfoHelper.getInstance().jointNames(mSelectList, true);
        String groupPics = TransformFriendInfoHelper.getInstance().joinUserHead(mSelectList);

        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put(KeyValue.Group.GROUP_UID, groupUids); // 成员ID
        hashMap.put(KeyValue.Group.GROUP_NAME, groupNames); // 成员昵称
        hashMap.put(KeyValue.Group.GROUP_PIC, groupPics); // 群成员头像
        mPresenter.createTeam(DqUrl.url_create_group, hashMap);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.hideSoftInput(this, mSearchEditText.getEditText());
    }

}