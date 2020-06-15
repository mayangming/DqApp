package com.wd.daquan.chat.group.activity;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.chat.group.adapter.GroupMemberForbidDoRPAdapter;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.SpannableStringUtils;
import com.wd.daquan.model.log.DqToast;
import com.da.library.listener.DialogListener;
import com.wd.daquan.model.bean.UserBean;
import com.da.library.view.CommDialog;
import com.da.library.widget.CommTitle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 禁止收发红包成员列表
 */
public class GroupMemberForbidDoRPActivity extends DqBaseActivity<ChatPresenter, DataBean> implements View.OnClickListener {

    private String groupId;

    private ListView mDispatcherLv;
    private TextView mAddOtherMember;
    private TextView mDispatcherTv;
    private TextView mDispatcherNoneTv;

    private GroupMemberForbidDoRPAdapter mAdapter;
    private CommTitle mCommTitle;
    public static final int DISPATCHER = 2001;

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.group_member_forbid_do_rp_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mCommTitle = findViewById(R.id.forbidDoRPActivityTitle);
        mDispatcherLv = findViewById(R.id.dispatcher_lv);
        mAddOtherMember = findViewById(R.id.add_other_member);
        mDispatcherTv = findViewById(R.id.dispatcher_tv);
        mDispatcherNoneTv = findViewById(R.id.dispatcher_none_tv);

        mCommTitle.setTitle(getString(R.string.dispatcher_red));
        mAdapter = new GroupMemberForbidDoRPAdapter(this);
        mDispatcherLv.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        mAddOtherMember.setOnClickListener(this);
        mAdapter.setListener(mDispatcherListener);
    }

    @Override
    public void initData() {
        groupId = getIntent().getStringExtra(KeyValue.GROUP_ID);
        if (null != groupId) {
            initForbidSelect();
        }
    }

    private void initForbidSelect() {

        Map<String, String> map = new HashMap<>();
        map.put(KeyValue.GROUP_ID, groupId);
        mPresenter.getForbidRedpacketSelect(DqUrl.url_forbidRedpacket_select, map);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.add_other_member://添加
                NavUtils.gotoGroupMemberForbidDoRPMemberActivity(this, groupId);
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if(null == entity) return;
        DqUtils.bequit(entity, this);
    }

    @Override
    public void onSuccess(String url, int code, DataBean dataEntity) {
        //查询已加入禁止收发红包成员列表
        if (0 == code) {
            if (DqUrl.url_forbidRedpacket_select.equals(url)) {
                if (null != dataEntity.data) {
                    mDispatcherTv.setVisibility(View.VISIBLE);
                    mDispatcherNoneTv.setVisibility(View.GONE);
                    mDispatcherLv.setVisibility(View.VISIBLE);

                    List<UserBean> userEntities = (List<UserBean>) dataEntity.data;
                    mAdapter.replace(userEntities);

                } else {
                    mDispatcherTv.setVisibility(View.GONE);
                    mDispatcherNoneTv.setVisibility(View.VISIBLE);
                    mDispatcherLv.setVisibility(View.GONE);
                }

            } else if (DqUrl.url_forbidRedpacket_joinAndRemove.equals(url)) {//移除成员
                DqToast.showShort(dataEntity.content);
                if (null != groupId) {
                    initForbidSelect();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DISPATCHER && resultCode == RESULT_OK) {
            if (null != groupId) {
                initForbidSelect();
            }
        }
    }


    private CommDialog mDialog = null;
    private GroupMemberForbidDoRPAdapter.DispatcherListener mDispatcherListener = (String userId, String userName) -> {

        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (null != userId) {
            mDialog = new CommDialog(activity);
            mDialog.setTitleGone();
            mDialog.setDescSpannableString(SpannableStringUtils.addTextColor(getString(R.string.move_dispatcher, userName),
                    2, userName.length() + 2, getResources().getColor(R.color.color_ff0000)));
            mDialog.setOkTxt(getString(R.string.confirm));
            mDialog.setOkTxtColor(getResources().getColor(R.color.app_theme_color));
            mDialog.setWidth();
            mDialog.show();

            mDialog.setDialogListener(new DialogListener() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onOk() {
                    removeMember(userId);
                }
            });
        }
    };

    //移除不能领红包的成员
    private void removeMember(String groupUid) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("action", KeyValue.ZERO_STRING);
        hashMap.put(KeyValue.GROUP_ID, groupId);
        hashMap.put("group_uid", groupUid);
        mPresenter.gotoForbidRedPacketJoinAndRemove(DqUrl.url_forbidRedpacket_joinAndRemove, hashMap);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mDialog) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
