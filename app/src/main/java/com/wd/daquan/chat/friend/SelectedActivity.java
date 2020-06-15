package com.wd.daquan.chat.friend;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.da.library.constant.IConstant;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.contacts.helper.TransformFriendInfoHelper;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.bean.CreateTeamEntity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.log.DqToast;
import com.da.library.listener.DialogListener;
import com.da.library.listener.ISelectListener;
import com.wd.daquan.third.session.SessionHelper;
import com.da.library.tools.Utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * c创建群组
 */
public class SelectedActivity extends BaseSelectActivity implements ISelectListener<Friend> {

    public static final int REQUEST_CODE = 1234;

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void initView() {
        super.initView();
        mSelectTeamRlyt.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(DqApp.getStringById(R.string.create_a_new_chat));
        FriendDbHelper.getInstance().getAllFriend(friends -> {
            if(friends != null && friends.size() > 0) {//没有数据时候不再请求
                updateData(friends);
            }
//            else {
//                requestData();
//            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        mSelectTeamRlyt.setOnClickListener(this);

    }

    @Override
    protected void onTitleRightClick() {
        if (mSelectList == null || 0 >= mSelectList.size()) {
            DqToast.showShort("请至少选择一位好友");
            return;
        }

        int size = mSelectList.size();
        if (1 == size) { // 直接进入会话
            Friend friend = mSelectList.get(0);
            SessionHelper.startP2PSession(this, friend.uid);
            finish();
        } else { // 创建群组
            createTeam();
        }
    }

    @Override
    public void requestData() {
        if (!Utils.isNetworkConnected(DqApp.sContext)) {
            DqToast.showShort(DqApp.getStringById(R.string.comm_network_error));
            return;
        }

        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.Contact.WHETHER_HELPER, "1");
        hashMap.put(IConstant.Contact.SHOW_SELF, "1");
        hashMap.put(IConstant.Contact.LAST_TIME, "0");
        mPresenter.getFriendList(DqUrl.url_friend_list, hashMap);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if(entity == null) {
            return;
        }
        if(DqUrl.url_friend_list.equals(url)) {
            List<Friend> friendList = (List<Friend>) entity.data;
            if (null != friendList && friendList.size() > 0) {
                updateData(friendList);
            }
        } else if (DqUrl.url_create_group.equals(url)) {
            if (entity.isSuccess()) {
                DqToast.showShort("群组创建成功");
                CreateTeamEntity teamEntity = (CreateTeamEntity) entity.data;
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
            } else {
                DqToast.showShort(entity.content);
            }
        }
    }

    private void startTeam(CreateTeamEntity teamEntity) {
        SessionHelper.startTeamSession(this, teamEntity.group_id);
        finish();
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if(entity != null && !TextUtils.isEmpty(entity.content)) DqToast.showShort(entity.content);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == mSelectTeamRlyt.getId()) {
            NavUtils.gotoTeamListActivity(this);
        }
    }

    private void createTeam() {

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                finish();
            }
        }
    }
}
