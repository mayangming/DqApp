package com.wd.daquan.chat.group.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suke.widget.SwitchButton;
import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.chat.group.adapter.GroupManagerAdapter;
import com.wd.daquan.model.bean.GroupManagerEntity;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.QCLog;
import com.wd.daquan.common.view.CommSwitchButton;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.da.library.NestedListView;
import com.da.library.listener.DialogListener;
import com.da.library.view.CommDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * 群管理
 * Created by Kind on 2018/9/26.
 */

public class GroupManagerActivity extends DqBaseActivity<ChatPresenter, DataBean> implements
        GroupManagerAdapter.ButtonClickListener, View.OnClickListener, CommSwitchButton.OnSwChangedListener, QCObserver {

    private String group_id;
    private GroupManagerAdapter adapter;
    private LinearLayout layout_setManager;
//    private LinearLayout layout_setMark;
//    private LinearLayout layout_transfer;
//    private LinearLayout mDispatcherGroup;
    private TextView txt_setManager;
//    private TextView txt_setMark;
    /**
     * 关闭群组认证dialog
     */
    private Dialog mGroupPurseDialog = null;
    private String groupMark = "";
    private boolean isOperater = true;

    private CommSwitchButton mTeamExamine;
    private CommDialog mCommDialog = null;
//    private LinearLayout mExitGroupMemberLl;
//    private LinearLayout mAssistantLl;
//    private LinearLayout mCopyNewGroupLayout;

    //长时间未领取红包开关
    private boolean isGroupMaster;
    private NestedScrollView mScrollView;
//    private View mLongTimeRedLl;

//    private PluginBean plugin;

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_group_manager);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        setTitle(getString(R.string.group_managed));
        setBackView();
        mScrollView = findViewById(R.id.group_manager_scroll_view);
        mTeamExamine = findViewById(R.id.csb_team_examine);
        layout_setManager = findViewById(R.id.groupManagerActivityManagerLayout);
//        layout_setMark = findViewById(R.id.groupManagerActivityMarkLayout);
//        layout_transfer = findViewById(R.id.groupManagerActivityTransferLayout);
//        mDispatcherGroup = findViewById(R.id.dispatcher_group_member_ll);
        txt_setManager = findViewById(R.id.groupManagerActivityManagerText);
//        txt_setMark = findViewById(R.id.groupManagerActivityMarkText);
        NestedListView lv_manager = findViewById(R.id.lv_manager);
//        mExitGroupMemberLl = findViewById(R.id.exit_group_member_ll);
//        mAssistantLl = findViewById(R.id.group_assistant_ll);
//        mCopyNewGroupLayout = findViewById(R.id.groupManagerActivityCopyNewGroupLayout);

//        mProtectSv = findViewById(R.id.group_manager_protect);
//        mLongTimeRedLl = findViewById(R.id.long_time_red_ll);
//        mLongTimeRedSv = findViewById(R.id.group_manager_long_time_red_sv);

        adapter = new GroupManagerAdapter(this);
        lv_manager.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        group_id = getIntent().getStringExtra(KeyValue.GROUPID);
        getGroupManagerData();
//        sw_group_manager.setClickable(false);
    }

    @Override
    protected void initListener() {
        layout_setManager.setOnClickListener(this);
//        layout_setMark.setOnClickListener(this);
//        layout_transfer.setOnClickListener(this);
//        mDispatcherGroup.setOnClickListener(this);
//        mExitGroupMemberLl.setOnClickListener(this);
//        mAssistantLl.setOnClickListener(this);
//        mCopyNewGroupLayout.setOnClickListener(this);
//
        mTeamExamine.setOnSwChangedListener(this);
//        mProtectSv.setOnSwChangedListener(this);
//        mLongTimeRedSv.setOnSwChangedListener(this);

        MsgMgr.getInstance().attach(this);
    }

    private void getGroupManagerData() {
        mPresenter.getGroupManagerData(group_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.groupManagerActivityManagerLayout://设置管理员
                NavUtils.gotoGroupSetMangerActivity(this, group_id);
                break;
            case R.id.groupManagerActivityMarkLayout://设置群聊号
                //判断是否是群主 或者是否设置了群聊号
                if (!TextUtils.isEmpty(groupMark) || !isGroupMaster) {
                    return;
                }
                NavUtils.gotoGroupSetMarkActivity(this, group_id);
                break;
            case R.id.groupManagerActivityTransferLayout://群主管理权转让
                NavUtils.gotoGroupMembersActivity(this, group_id, false, 103, 2);
                break;
            //退群成员列表
            case R.id.exit_group_member_ll:
                NavUtils.gotoExitGroupMembersActivity(this, group_id);
                break;
            case R.id.group_assistant_ll:
//                //群助手
//                if (plugin != null) {
//                    NavUtils.gotoGroupAidesActivity(this, plugin, group_id);
//                }
                break;
            case R.id.dispatcher_group_member_ll://禁止收发红包功能
                NavUtils.gotoGroupMemberForbidDoRPActivity(this, group_id);
                break;
            case R.id.groupManagerActivityCopyNewGroupLayout://一键复制群
                NavUtils.gotoGroupCopyNewActivity(this, group_id);
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if (DqUrl.url_group_admin.equals(url)) {
            isOperater = false;
        }
        DqToast.showShort(entity == null ? "请求出错！" : entity.getContent());
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (entity == null) return;
        if (DqUrl.url_group_admin.equals(url)) {
            if (entity.data instanceof GroupManagerEntity) {
                GroupManagerEntity managerEntity = (GroupManagerEntity) entity.data;
//                    plugin = managerEntity.plugin;

                isOperater = true;
                initGroupManagerData(managerEntity);
                //长时间未领取红包
                //initLongTimeRedData(managerEntity);
            }
        } else if (DqUrl.url_apply_response.equals(url)) {
            getGroupManagerData();
        } else if (DqUrl.url_edit_group_info.equals(url)) {
            QCLog.e("fz", "群管理设置成功");
        }
    }

    @SuppressLint("SetTextI18n")
    private void initGroupManagerData(GroupManagerEntity managerEntity) {
        // 群认证
        mTeamExamine.setCheckedNoEvent(managerEntity.isExamine());

        // 群管理
//        if (!TextUtils.isEmpty(managerEntity.is_protect_groupuser)) {
//            if ("0".equals(managerEntity.is_protect_groupuser)) {
//                mProtectSv.setCheckedNoEvent(true);
//            } else if ("1".equals(managerEntity.is_protect_groupuser)) {
//                mProtectSv.setCheckedNoEvent(false);
//            }
//        }

        // 被邀请人列表
        if (null != managerEntity.apply_list) {
            adapter.refreshList(managerEntity.apply_list);
        }

        // 群管理员人数
        if (managerEntity.isHasAdmin()) {
            txt_setManager.setText("未设置");
        } else {
            txt_setManager.setText(managerEntity.admin_num + "人");
        }

//        // 群聊号
//        groupMark = managerEntity.group_number;
//        if (TextUtils.isEmpty(managerEntity.group_number)) {
//            txt_setMark.setText("未设置");
//        } else {
//            txt_setMark.setText(managerEntity.group_number);
//        }

        // 判断是否是群主
        if (KeyValue.Role.MASTER.equals(managerEntity.role)) {
            isGroupMaster = true;
//            layout_setManager.setVisibility(View.VISIBLE);
//            layout_transfer.setVisibility(View.GONE);
//            mAssistantLl.setVisibility(View.GONE);
//            mCopyNewGroupLayout.setVisibility(View.GONE);
        } else {
            isGroupMaster = false;
//            layout_setManager.setVisibility(View.GONE);
//            layout_transfer.setVisibility(View.GONE);
//            mAssistantLl.setVisibility(View.GONE);
//            mCopyNewGroupLayout.setVisibility(View.GONE);
        }
        layout_setManager.setVisibility(View.GONE);

        mScrollView.scrollTo(0,0);
    }

    /**
     * 长时间未领取红包开关
     */
    private void initLongTimeRedData(@NonNull GroupManagerEntity entity) {
//        mLongTimeRedSv.setVisibility(View.GONE);
//        if (KeyValue.Role.MASTER.equals(entity.role)) {
//            mLongTimeRedSv.setVisibility(View.VISIBLE);
//        } else {
//            mLongTimeRedSv.setVisibility(View.GONE);
//        }
//
//        if (KeyValue.Role.ADMIN.equals(entity.is_allow_receive_redpacket)) {
//            mLongTimeRedSv.setCheckedNoEvent(true);
//        } else {
//            mLongTimeRedSv.setCheckedNoEvent(false);
//        }
//
//        /**
//         * 存储长时间未领红包状态
//         */
//        TeamManager.getInstance().saveLongTimeRp(group_id, entity.is_allow_receive_redpacket);
    }

    /**
     * 开启群认证, 群成员保护
     *
     * @param key   设置的开关
     * @param value 1开启/ 0关闭
     */
    private void manageGroup(String key, String value) {
        if (null != group_id) {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put(KeyValue.GROUP_ID, group_id);
            hashMap.put(key, value);
            mPresenter.setGroupManagerInfo(DqUrl.url_edit_group_manager_info, hashMap);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGroupPurseDialog != null) {
            mGroupPurseDialog.dismiss();
            mGroupPurseDialog = null;
        }

        if (null != mCommDialog) {
            mCommDialog.dismiss();
            mCommDialog = null;
        }
        MsgMgr.getInstance().detach(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            groupMark = data.getStringExtra(KeyValue.GROUP_REMARK);
//            txt_setMark.setText(groupMark);
        } else if (requestCode == 103 && resultCode == RESULT_OK) {//群主转让
            finish();
        }
    }

    /**
     *
     */
    @Override
    public void click(String request_id, String agree) {
        setGroupApply(request_id, agree);
    }

    /**
     * 设置入群人员申请
     */
    private void setGroupApply(String request_id, String states) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(KeyValue.REQUEST_ID, request_id);
        hashMap.put(KeyValue.GROUP_ID, group_id);
        hashMap.put(KeyValue.STATUS, states);
        mPresenter.setGroupApply(DqUrl.url_apply_response, hashMap);
    }

    /**
     * 开启
     */
//    @Override
//    public void toggleToOn(SwitchView view) {
//        if (!isOperater) return;
//        view.setOpened(true);
//        if (view.getId() == sw_group_manager.getId()) {
//            //群认证
//            manageGroup(KeyValue.EXAMINE, KeyValue.ONE_STRING);
//        } else if (view.getId() == mProtectSv.getId()) {
//            //群保护
//            manageGroup(KeyValue.IS_PROTECT_GROUPUSER, KeyValue.ONE_STRING);
//        } else if (view.getId() == mLongTimeRedSv.getId()) {
//            //长时间未领取红包
//            manageGroup(KeyValue.IS_ALLOW_RECEIVE_REDPACKET, KeyValue.ONE_STRING);
//        }
//    }

    /**
     * 关闭
     */
//    @Override
//    public void toggleToOff(SwitchView view) {
//        if (!isOperater) return;
//        if (view.getId() == sw_group_manager.getId()) {
//            //群认证
////            initCommDialog(view, getString(R.string.guanmanager));
//        } else if (view.getId() == mProtectSv.getId()) {
//            //群保护
////            initCommDialog(view, getString(R.string.group_member_protect_warning));
//        } else if (view.getId() == mLongTimeRedSv.getId()) {
//            //长时间未领取红包
////            initCommDialog(view, getString(R.string.long_time_did_not_get_red_envelope_dialog_desc));
//        }
//    }
    private void initCommDialog(int id, View view, String desc) {
        SwitchButton switchButton = (SwitchButton) view;
//        switchButton.setCheckedNoEvent(true);
        switchButton.setChecked(true);
        mCommDialog = new CommDialog(this);
        mCommDialog.setTitleVisible(false);
        mCommDialog.setDesc(desc);
        mCommDialog.setCancelTxt(getString(R.string.cancel));
        mCommDialog.setOkTxt(getString(R.string.comm_ok));
        mCommDialog.setOkTxtColor(getResources().getColor(R.color.text_blue));
        mCommDialog.show();

        mCommDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {
//                switchButton.setCheckedNoEvent(true);
                switchButton.setChecked(true);
//                view.setOpened(true);
            }

            @Override
            public void onOk() {
//                switchButton.setCheckedNoEvent(false);
                switchButton.setChecked(true);
//                view.setOpened(false);
//                if (id == mProtectSv.getId()) {
//                    manageGroup(KeyValue.IS_PROTECT_GROUPUSER, KeyValue.ZERO_STRING);
//                } else if (id == sw_group_manager.getId()) {
//                    manageGroup(KeyValue.EXAMINE, KeyValue.ZERO_STRING);
//                } else if (id == mLongTimeRedSv.getId()) {
//                    manageGroup(KeyValue.IS_ALLOW_RECEIVE_REDPACKET, KeyValue.ZERO_STRING);
//                }
            }
        });
    }


    @Override
    public void onChanged(int id, View view, boolean isChecked) {
        if (!isOperater) return;
        switch (id) {
            case R.id.csb_team_examine:
                if (isChecked) {
                    //群认证
                    manageGroup(KeyValue.EXAMINE, "0");
                } else {
                    //群认证
                    //initCommDialog(id, view, getString(R.string.guanmanager));
                    manageGroup(KeyValue.EXAMINE, "1");
                }
                break;
//            case R.id.group_manager_protect:
//                if (isChecked) {
//                    //群保护
//                    manageGroup(KeyValue.IS_PROTECT_GROUPUSER, KeyValue.ONE_STRING);
//                } else {
//                    //群保护
//                    initCommDialog(id, view, getString(R.string.group_member_protect_warning));
//                }
//                break;
//            case R.id.group_manager_long_time_red_sv:
//                if (isChecked) {
//                    //长时间未领取红包
//                    manageGroup(KeyValue.IS_ALLOW_RECEIVE_REDPACKET, KeyValue.ONE_STRING);
//                } else {
//                    //长时间未领取红包
//                    initCommDialog(id, view, getString(R.string.long_time_did_not_get_red_envelope_dialog_desc));
//                }
//                break;
        }
    }

    @Override
    public void onMessage(String key, Object value) {
        if (MsgType.MT_GROUP_UPDATE_AIDES.equals(key) || MsgType.MT_GROUP_DELETE_AIDES.equals(key)) {
            getGroupManagerData();
        }
    }
}