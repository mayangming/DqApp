package com.wd.daquan.chat.friend;

import android.app.Activity;
import android.content.Intent;

import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.da.library.listener.ISelectListener;

import java.util.LinkedHashMap;
import java.util.List;

/**
 */
public class SelectGroupMemberActivity extends BaseSelectActivity implements ISelectListener<Friend> {

    private boolean withoutSelf = false;
    private String mSelfHeadPic = "";
    private String mAccount = "";
    private String mSelectedUserIds = "";

    @Override
    protected void init() {
        mAccount = getIntent().getStringExtra(KeyValue.KEY_ACCOUNT);
        mSelectedUserIds = getIntent().getStringExtra(KeyValue.SelectFriend.KEY_SELECTED_USERIDS);
        withoutSelf = getIntent().getBooleanExtra(KeyValue.SelectFriend.KEY_WITHOUT_SELF, false);
    }

    @Override
    protected void initData() {
        super.initData();
        requestData();
    }

    @Override
    public void requestData() {
        if (mPresenter == null) {
            return;
        }

        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("group_id", "" + mAccount);
        mPresenter.getTeamMember(DqUrl.url_select_group_user, hashMap);
    }

    @Override
    protected void onTitleRightClick() {
        boolean isAll = mSelectList.size() == mFriendList.size();
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(KeyValue.SelectFriend.KEY_SELECTED_LIST, mSelectList);
        intent.putExtra(KeyValue.SelectFriend.KEY_IS_ALL_MEMBER, isAll);
        setResult(RESULT_OK, intent);
        finish();
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
        if(DqUrl.url_select_group_user.equals(url)) {
            List<Friend> friendList = (List<Friend>) entity.data;
            if (null != friendList && friendList.size() > 0) {
                // 是否包含自己
                for (Friend friend: friendList) {
                    if(withoutSelf) {
                        if (friend.uid.equals(ModuleMgr.getCenterMgr().getUID())) {
                            friendList.remove(friend);
                            break;
                        }
                    }
                }

                updateData(friendList);
            }
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
    }

}
