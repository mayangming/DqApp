package com.wd.daquan.chat.group.activity;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.da.library.listener.DialogListener;
import com.da.library.utils.NoDoubleClickListener;
import com.da.library.view.CommDialog;
import com.da.library.widget.NonScrollGridView;
import com.dq.im.type.ImType;
import com.dq.im.viewmodel.HomeMessageViewModel;
import com.dq.im.viewmodel.TeamMessageViewModel;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.chat.group.adapter.GroupGridAdapter;
import com.wd.daquan.chat.group.bean.QrEntity;
import com.wd.daquan.chat.group.inter.GroupGridListener;
import com.wd.daquan.chat.helper.MuteHelper;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.view.CommSwitchButton;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.model.db.helper.MemberDbHelper;
import com.wd.daquan.model.db.helper.TeamDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.manager.TeamManager;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.model.sp.QCSharedPreTeamInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群聊详情页
 * Created by Kind on 2018/9/10.
 */
public class GroupDetailsActivity extends DqBaseActivity<ChatPresenter, DataBean>
        implements QCObserver, CommSwitchButton.OnSwChangedListener {

    private TextView mTvGroupMemberSize;
    private TextView mTvGroupMemberOnlinestatus;
    private TextView mTvGroupName;
    private TextView mTvGroupAnnouncement;
    private Button mBtnGroupQuit;
    private Button mBtnGroupDismiss;

    // 截屏通知
    private CommSwitchButton mScreenShortSw;
    // 置顶聊天
    private CommSwitchButton mStickSw;
    // 消息免打扰
    private CommSwitchButton mMuteSw;
    // 通讯录
    private CommSwitchButton mMailListSw;
    //阅后即焚
    private CommSwitchButton mBurnSw;

    private LinearLayout mLlGroupManaged;
    private RelativeLayout mRlGroupMemberSize;

    /**
     * 群内存储配置
     */
    private QCSharedPreTeamInfo mSharedPreTeamInfo = null;

    /**
     * 群成员展示适配器
     */
    private GroupGridAdapter mGroupGridAdapter = null;
    /**
     * 群会话id
     */
    private String groupID;
    /**
     * 群信息
     */
    private GroupInfoBean mGroupResp = new GroupInfoBean();
    private TeamMessageViewModel teamMessageViewModel;
    private HomeMessageViewModel homeMessageViewModel;
    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.cn_activity_detail_group);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        setTitle(getString(R.string.group_info));
        setBackView();

        if (getSharedPreferences(KeyValue.CONFIG, Context.MODE_PRIVATE).getBoolean(KeyValue.IS_DEBUG, false)) {
            mTvGroupMemberOnlinestatus.setVisibility(View.VISIBLE);
        }

        NonScrollGridView mGridView = this.findViewById(R.id.grid_view);
        mTvGroupMemberSize = this.findViewById(R.id.tv_group_member_size);
        mRlGroupMemberSize = this.findViewById(R.id.rl_group_member_size);
        mTvGroupMemberOnlinestatus = this.findViewById(R.id.tv_group_member_onlinestatus);
        mTvGroupName = this.findViewById(R.id.tv_group_name);
        mTvGroupAnnouncement = this.findViewById(R.id.tv_group_announcement);
        mLlGroupManaged = this.findViewById(R.id.ll_group_managed);
        mBtnGroupQuit = this.findViewById(R.id.btn_group_quit);


        mScreenShortSw = this.findViewById(R.id.group_detail_screenshot);
        mStickSw = this.findViewById(R.id.group_detail_stick);
        mMuteSw = this.findViewById(R.id.group_detail_mute);
        mMailListSw = this.findViewById(R.id.group_detail_maillist);
        mBurnSw = this.findViewById(R.id.group_detail_burn);

        mBurnSw.setVisibility(View.GONE);
        mScreenShortSw.setVisibility(View.GONE);

        mGroupGridAdapter = new GroupGridAdapter(this);
        mGridView.setAdapter(mGroupGridAdapter);
    }

    @Override
    protected void initListener() {
        MsgMgr.getInstance().attach(this);

        mScreenShortSw.setOnSwChangedListener(this);
        mStickSw.setOnSwChangedListener(this);
        mMuteSw.setOnSwChangedListener(this);
        mMailListSw.setOnSwChangedListener(this);
        mBurnSw.setOnSwChangedListener(this);

        mRlGroupMemberSize.setOnClickListener(doubleClickListener);
        findViewById(R.id.ll_group_name).setOnClickListener(doubleClickListener);
        findViewById(R.id.ll_group_code).setOnClickListener(doubleClickListener);
        findViewById(R.id.ll_group_announcement).setOnClickListener(doubleClickListener);
        findViewById(R.id.ll_group_personal_info).setOnClickListener(doubleClickListener);
        mLlGroupManaged.setOnClickListener(doubleClickListener);
        findViewById(R.id.ll_group_clean).setOnClickListener(doubleClickListener);
        mBtnGroupQuit.setOnClickListener(doubleClickListener);
//        mBtnGroupDismiss.setOnClickListener(doubleClickListener);
        findViewById(R.id.ll_group_search_chat).setOnClickListener(doubleClickListener);
        findViewById(R.id.no_brought_group_ll).setOnClickListener(doubleClickListener);

        mGroupGridAdapter.setGridListener(mGroupGridListener);
    }

    @Override
    protected void initData() {
        //群组会话界面点进群组详情
        groupID = getIntent().getStringExtra(KeyValue.GROUPID);
//        GroupInfoBean team = TeamDbHelper.getInstance().getTeam(groupID);
//        if(team != null) {
//            initViewData(team);
//        }else {
//            requestData(true);
//        }

        requestData(true);
        teamMessageViewModel = ViewModelProviders.of(this).get(TeamMessageViewModel.class);
        homeMessageViewModel = ViewModelProviders.of(this).get(HomeMessageViewModel.class);
    }

    private void requestData(boolean b) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(KeyValue.GROUP_ID, groupID);
        hashMap.put(KeyValue.Group.SHOW_GROUP_MEMBER, b?"0":"1");
        mPresenter.getTeamInfo(DqUrl.url_select_group, hashMap);
    }

    /**
     * 数据初始化
     */
    private void initViewData(GroupInfoBean groupInfo) {
        if(groupInfo == null) {
            return;
        }
        mGroupResp = groupInfo;

        initGroupMemberData(groupInfo.getMemberList());
        mTvGroupName.setText(groupInfo.getGroup_name());
        mTvGroupAnnouncement.setText(groupInfo.announcement);

        mLlGroupManaged.setVisibility(isLordOrAdmin() ? View.VISIBLE : View.GONE);

        mTvGroupName.setText(groupInfo.getGroup_name());

        //群主显示解绑，群成员显示退出
        mBtnGroupQuit.setVisibility(View.VISIBLE);
        if (isLord()) {
            mBtnGroupQuit.setText("解绑并删除");
        } else {
            mBtnGroupQuit.setText("删除并退出");
        }

        /**
         * 界面上对应的true为静音
         */
        mMuteSw.setCheckedImmediatelyNoEvent(!MuteHelper.getInstance().isGroupNotify(groupInfo.getGroup_id()));

        /**
         * 通讯录初始化
         */
        mMailListSw.setCheckedImmediatelyNoEvent(groupInfo.isSaveTeam());

        /**
         * 截屏初始化
         */
        mScreenShortSw.setCheckedImmediatelyNoEvent(groupInfo.isScreenshot_notify());

        /**
         * 存储长时间未领红包状态
         */
        TeamManager.getInstance().saveLongTimeRp(groupInfo.group_id, groupInfo.is_allow_receive_redpacket);

        if (!StringUtil.isEmpty(groupInfo.announcement)) {
            mTvGroupAnnouncement.setText(getTextMessageContent(mGroupResp.announcement));
        }
    }

    /**
     * 设置消息免打扰
     *
     * @param groupInfo
     */
    private void setMessageNotify(GroupInfoBean groupInfo) {
        boolean isNotify = MuteHelper.getInstance().isGroupNotify(groupInfo.getGroup_id());
        mMuteSw.setCheckedImmediatelyNoEvent(isNotify);

        MuteHelper.getInstance().setGroupNotify(groupInfo.getGroup_id(), !isNotify);
    }

    /**
     * 显示群组成员信息
     */
    private void initGroupMemberData(@NonNull List<GroupMemberBean> memberList) {
        int size = memberList.size();
        if (size <= 0) {
//            List<GroupMemberBean> beanList = MemberDbHelper.getInstance().getAll(groupID);
//            if(beanList.size() <= 0) {
//                return;
//            }
//            memberList.addAll(beanList);
            return;
        }

        setTitle(getString(R.string.group_info) + "(" + size + ")");
        mTvGroupMemberSize.setText(getString(R.string.group_detail_more_member));

        if (size > 14) {
            List<GroupMemberBean> list = new ArrayList<>();
            for (int i = 0; i < 14; i++) {
                list.add(memberList.get(i));
            }
            mRlGroupMemberSize.setVisibility(View.VISIBLE);
            mGroupGridAdapter.setMaster(getYourSelf().getRoleType());
            mGroupGridAdapter.replace(list);
        } else {
            mRlGroupMemberSize.setVisibility(View.GONE);
            mGroupGridAdapter.setMaster(getYourSelf().getRoleType());
            mGroupGridAdapter.replace(memberList);
        }
    }

    /**
     * 取群公告文本
     *
     * @param content 文本
     * @return SpannableStringBuilder
     */
    public SpannableStringBuilder getTextMessageContent(String content) {
        SpannableStringBuilder s;
        SpannableStringBuilder spannable = new SpannableStringBuilder(content);
        // AndroidEmoji.ensure(spannable);
        s = spannable;
        return s;
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if(DqUrl.url_select_group.equals(url)) {
            GroupInfoBean team = TeamDbHelper.getInstance().getTeam(groupID);
            initViewData(team);
        }else {
            DqUtils.bequit(entity, this);
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if(DqUrl.url_select_group.equals(url)) {
            GroupInfoBean groupInfoBean = (GroupInfoBean) entity.data;
            initViewData(groupInfoBean);
        }else if (DqUrl.url_edit_group_info.equals(url)) {
            //群设置
            modifyGroupSetting(entity);
        } else if (DqUrl.url_user_quit_group.equals(url) || DqUrl.url_remove_group.equals(url)) {
            //删除群信息
            TeamDbHelper.getInstance().delete(groupID);
            //删除群所有成员
            MemberDbHelper.getInstance().delete(groupID);

            MsgMgr.getInstance().sendMsg(MsgType.MT_GROUP_SETTING_QUIT, mGroupResp.getGroup_id());
            teamMessageViewModel.deleteMessageForGroupId(groupID);
            homeMessageViewModel.deleteForGroupId(groupID);
            DqToast.showShort(isLord() ? getString(R.string.dismiss_success) : getString(R.string.quit_success));
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * 修改群设置
     *
     * @param dataEntity
     */
    private void modifyGroupSetting(DataBean dataEntity) {
        if (!dataEntity.isSuccess()) {
            return;
        }
        switch (dataEntity.tag) {
            //截屏通知
            case KeyValue.SCREENSHOT_NOTIFY:
                mScreenShortSw.setCheckedImmediatelyNoEvent(mScreenShortSw.isChecked());

                //DqToast.showShort(getString(mScreenShortSw.isChecked() ? R.string.set_screenshot : R.string.cancel_screenshot));
                break;
            //保存到通讯录
            case KeyValue.Group.IS_ADDADDRESS_BOOK:
                mMailListSw.setCheckedImmediatelyNoEvent(mMailListSw.isChecked());
                GroupInfoBean team = TeamDbHelper.getInstance().getTeam(groupID);
                if(team != null) {
                    team.saved_team = mMailListSw.isChecked() ? KeyValue.ZERO_STRING : KeyValue.ONE_STRING;
                    TeamDbHelper.getInstance().update(team, null);
                }
                //DqToast.showShort(getString(mMailListSw.isChecked() ? R.string.save_contacts_success : R.string.remove_contacts_success));
                break;
        }
    }

    NoDoubleClickListener doubleClickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                //所以群成员
                case R.id.rl_group_member_size: {
                    GroupMemberBean.GrouppostType grouppostType = getYourSelf().getRoleType();
                    NavUtils.gotoTotalGroup(getActivity(), mGroupResp.getGroup_id(), mGroupResp, mGroupResp.getGroup_name(),
                            grouppostType == GroupMemberBean.GrouppostType.LORD, grouppostType == GroupMemberBean.GrouppostType.ADMIN);
                    break;
                }
                //群昵称
                case R.id.ll_group_name:
                    modifyGroupName();
                    break;
                //群二维码
                case R.id.ll_group_code:
                    onGroupQRCodeClick();
                    break;
                //群公告
                case R.id.ll_group_announcement:
                    NavUtils.gotoGroupNoticeActivity(getActivity(), isLord(),
                            mGroupResp.getGroup_id(), mGroupResp.am_update_time, mGroupResp.announcement);
                    break;
                //群个人信息
                case R.id.ll_group_personal_info:
                    NavUtils.gotoGroupPersonalInfosActivity(getActivity(), mGroupResp.getGroup_id());
                    break;
                //群管理
                case R.id.ll_group_managed:
                    NavUtils.gotoGroupManager(getActivity(), mGroupResp.getGroup_id());
                    break;
                //清空群历史聊天记录
                case R.id.ll_group_clean:
                    showCommDialog();
                    break;
                //群成员退出群
                case R.id.btn_group_quit:
                    showButtomGroupDialog();
                    break;
//                //群主解散群
//                case R.id.btn_group_dismiss:
//                    break;
                //群聊天记录搜索
                case R.id.ll_group_search_chat:
                    NavUtils.gotoGroupSearchChatActivity(getActivity(), groupID);
                    break;
                // 长时间未领红包
                case R.id.no_brought_group_ll:
                    boolean flag = TeamManager.getInstance().enableLongTimeRp(groupID);
                    if (flag) {
                        // 直接打开
                        NavUtils.gotoGroupLongTimeRpActivity(GroupDetailsActivity.this, groupID);
                    } else {
                        boolean enable = isLord();
                        if (enable) {
                            // 直接打开
                            NavUtils.gotoGroupLongTimeRpActivity(GroupDetailsActivity.this, groupID);
                        } else {
                            // 弹框提示
                            showWarning();
                        }
                    }
                    break;
            }
        }
    };

    private void showWarning() {
        CommDialog mCommDialog = new CommDialog(this);
        mCommDialog.setTitleVisible(false);
        mCommDialog.setSingleBtn();
        mCommDialog.setDesc(getString(R.string.longtime_rp_warning));
        mCommDialog.setOkTxt(getString(R.string.comm_ok));
        mCommDialog.setOkTxtColor(getResources().getColor(R.color.text_blue));
        mCommDialog.show();
    }

    /**
     * 是否群主,管理员
     */
    private boolean isLordOrAdmin() {
        return isLord() || "1".equals(mGroupResp.role);
    }

    /**
     * 是否群主
     */
    private boolean isLord() {
        if (null == mGroupResp){
            return false;
        }
        return "2".equals(mGroupResp.role);
    }

    private void modifyGroupName() {
        if (!isLordOrAdmin()) {
            DqToast.showShort(getString(R.string.only_group_master_and_manage_can_open));
            return;
        }

        NavUtils.gotoGroupChatNameActivity(this, mGroupResp.getGroup_id(), mGroupResp.getGroup_name());
    }

    private GroupMemberBean getYourSelf() {
        String uid = ModuleMgr.getCenterMgr().getUID();
        for (GroupMemberBean tmp : mGroupResp.getMemberList()) {
            if (uid.equals(tmp.getUid())) {
                return tmp;
            }
        }
        return new GroupMemberBean();
    }

    /**
     * 获取群成员前9名的头像
     *
     * @return
     */
    private List<String> getmUserHeadList() {
        List<String> mUserHeadList = new ArrayList<>();
        for (GroupMemberBean temp : mGroupResp.getMemberList()) {
            if (mUserHeadList.size() > 9) {
                break;
            }
            mUserHeadList.add(temp.headpic);
        }
        return mUserHeadList;
    }

    /**
     * 在群成员中位置索引
     *
     * @return
     */
    private int getGroupPos() {
        List<GroupMemberBean> memberInfos = mGroupResp.getMemberList();
        for (int i = 0; i < memberInfos.size(); i++) {
            if (ModuleMgr.getCenterMgr().getUID().equals(memberInfos.get(i).getUid())) {
                return i;
            }
        }
        return -1;
    }


    private Dialog dialog;

    private void showButtomGroupDialog() {
        int mType = 1;
        if(isLord()) {
            mType = 2;
        }
        dialog = DialogUtils.showButtomGroupDialog(this, mType, type -> {
            String url;
            if(type == 1) {
                url = DqUrl.url_user_quit_group;
            }else {
                url = DqUrl.url_remove_group;
            }
            mPresenter.exitGroup(url, groupID);
        });

        dialog.show();
    }

    private void dismisButtomGroupDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }


    private CommDialog commDialog = null;

    /**
     * 清除聊天记录
     */
    private void showCommDialog() {
        dismissCommDialog();
        commDialog = new CommDialog(this);
        commDialog.setTitleVisible(false);
        commDialog.setDescCenter();
        commDialog.setDesc(getString(R.string.clean_group_chat_history));
        commDialog.setOkTxt(getString(R.string.comm_ok));
        commDialog.setOkTxtColor(getResources().getColor(R.color.color_ff0000));
        commDialog.setCancelTxt(getString(R.string.comm_cancel));
        commDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                teamMessageViewModel.deleteMessageForGroupId(mGroupResp.group_id);
                homeMessageViewModel.deleteForGroupId(mGroupResp.group_id);
                mPresenter.clearHistory(mGroupResp.group_id, ImType.Team);
            }
        });
        commDialog.show();
    }

    private void dismissCommDialog() {
        if (commDialog != null) {
            commDialog.dismiss();
            commDialog = null;
        }
    }

    /**
     * 组员操作事件监听
     */
    private GroupGridListener mGroupGridListener = new GroupGridListener() {
        @Override
        public void addMember(ArrayList<String> list) {
            if (null == mGroupResp || getActivity() == null) {
                return;
            }

            NavUtils.gotoSelectFriendsActivity(getActivity(), mGroupResp.getGroup_id());
        }

        @Override
        public void removeMember() {
            if (null == mGroupResp || getActivity() == null) {
                return;
            }

            NavUtils.gotoSelectFriendsActivity(getActivity(), mGroupResp.getGroup_id(), isLord());
        }

        @Override
        public void clickMember(String id) {
            if (null != id && null != groupID) {
                NavUtils.gotoUserInfoActivity(getActivity(), id, ImType.Team.getValue(),
                        groupID, isLordOrAdmin());
            }
        }
    };

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_GROUP_SETTING_NAME://修改群昵称
                String groupName = (String) value;
                mTvGroupName.setText(groupName);
                break;
            case MsgType.MT_GROUP_SETTING_MY_GROUP_INFO://我在群的信息
//            DbMgr.getDbMgr().getHttpGroupMemberAll(groupID, new ChatInterface.GroupInfoComplete() {
//                @Override
//                public void onReqCompleteGroup(boolean succes, GroupInfoBean groupInfo) {
//                    if (groupInfo != null) {
//                        initViewData(groupInfo);
//                    }
//                }
//            });
                break;
            case MsgType.MT_GROUP_SETTING_ANNOUNCEMENT:
                //TODO 需要重新请求接口，获取群公告信息
//                String announcement = (String) value;
//                mTvGroupAnnouncement.setText(announcement);
                requestData(true);
                break;
            case MsgType.MT_GROUP_SETTING_MEMBER_CHANGE:
//                List<GroupMemberBean> list = MemberDbHelper.getInstance().getAll(groupID);
//                initGroupMemberData(list);
                requestData(true);
                break;
            case MsgType.MT_GROUP_SETTING_NEW_LORD_CHANGE:
            case MsgType.MT_GROUP_SETTING_ADMIN_CHANGE:
            case MsgType.MT_GROUP_NOTICE://群通知
                requestData(true);
                //TeamGetInfoHelper.getInstance().getTeamInfo(groupID, true);
                break;
            case MsgType.MT_FRIEND_REMARKS_CHANGE://好友备注变更
                if (mGroupGridAdapter != null) {
                    mGroupGridAdapter.notifyDataSetChanged();
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        MsgMgr.getInstance().detach(this);
        super.onDestroy();

        dismissCommDialog();
        dismisButtomGroupDialog();
    }

    private void onGroupQRCodeClick() {
        if (mGroupResp != null ) {
            QrEntity qrEntity = new QrEntity();
            qrEntity.conversationType = String.valueOf(ImType.Team.getValue());
            qrEntity.targetId = mGroupResp.group_id;
            qrEntity.groupName = mGroupResp.getGroup_name();
            qrEntity.groupPic = mGroupResp.group_pic;
            qrEntity.examine = mGroupResp.examine;

            Activity activity = getActivity();
            if (activity == null) {
                return;
            }
            NavUtils.gotoQRCodeActivity(activity, qrEntity);
        }
    }

    @Override
    public void onChanged(int id, View v, boolean isChecked) {
        // 截屏通知
        if (id == mScreenShortSw.getId()) {
//            GroupMemberBean.GrouppostType grouppostType = getYourSelf().getRoleType();
//            if (grouppostType == GroupMemberBean.GrouppostType.LORD || grouppostType == GroupMemberBean.GrouppostType.ADMIN) {
//                mPresenter.setGroupInfo(mGroupResp.getGroup_id(), KeyValue.SCREENSHOT_NOTIFY,
//                        isChecked ? KeyValue.ONE_STRING : KeyValue.ZERO_STRING);
//            } else {//非群主操作
//                mScreenShortSw.setCheckedImmediatelyNoEvent(!isChecked);
//                DqToast.showShort(getString(R.string.only_group_master_and_manage_can_open));
//            }
            DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
            mScreenShortSw.setChecked(false);
        } else if (id == mStickSw.getId()) { // 置顶聊天
        } else if (id == mMuteSw.getId()) { // 消息免打扰
            setMessageNotify(mGroupResp);
        } else if (id == mMailListSw.getId()) { // 通讯录
            mPresenter.setGroupInfo(mGroupResp.getGroup_id(), KeyValue.Group.IS_ADDADDRESS_BOOK,
                    isChecked ? KeyValue.ZERO_STRING : KeyValue.ONE_STRING);
        }else if(id == mBurnSw.getId()) {
            DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
            mBurnSw.setChecked(false);
        }
    }
}
