package com.netease.nim.uikit.business.session.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.da.library.tools.ActivitysManager;
import com.da.library.tools.Utils;
import com.da.library.view.DqToolbar;
import com.da.library.widget.CommTitle;
import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.BaseChatMessageFragment;
import com.netease.nim.uikit.business.session.fragment.ChatTeamFragment;
import com.netease.nim.uikit.business.session.helper.ScreenHelper;
import com.wd.daquan.DqApp;
import com.wd.daquan.MainActivity;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.model.db.DbSubscribe;
import com.wd.daquan.model.db.helper.MemberDbHelper;
import com.wd.daquan.model.db.helper.TeamDbHelper;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.model.sp.QCSharedPreTeamInfo;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.third.helper.UserInfoHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群聊界面
 * <p/>
 * Created by huangjun on 2015/3/5.
 */
public class TeamMessageActivity extends BaseMessageActivity implements View.OnClickListener, QCObserver, Presenter.IView<DataBean> {

    private QCSharedPreTeamInfo mSharedPreTeamInfo;
    private CommTitle mCommTitle = null;

    // model
    private GroupInfoBean mGroupInfo;

    private ChatTeamFragment fragment;

    private LinearLayout mBannerLlyt;
    private TextView mBannerTitle;
    private TextView mBannerDetail;
    private ImageView mBannerClose;

    private Class<? extends Activity> backToClass;
    private ChatPresenter mPresenter;
    private DqToolbar mToolbar;
    private UserInfoObserver userInfoObserver;//这个监听只保存了账号，所以和用户信息不存在冲突
    public static void start(Context context, String tid, SessionCustomization customization,
                             Class<? extends Activity> backToClass, ImMessageBaseModel anchor,IMContentDataModel sendContent) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, tid);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        intent.putExtra(Extras.EXTRA_BACK_TO_CLASS, backToClass);
        intent.putExtra(Extras.EXTRA_IM_CONTENT, sendContent);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, TeamMessageActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.w("fz", "TeamId ： " + tid);
        context.startActivity(intent);
    }

    protected void findViews() {
        mBannerLlyt = this.findView(R.id.nim_teammessage_banner);
        mBannerTitle = this.findView(R.id.conversation_banner_title_tv);
        mBannerClose = this.findView(R.id.conversation_banner_close_iv);
        mBannerDetail = this.findView(R.id.conversation_banner_detail_tv);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        initCommTitle();

        backToClass = (Class<? extends Activity>) getIntent().getSerializableExtra(Extras.EXTRA_BACK_TO_CLASS);

        findViews();

        QCSharedPrefManager mSharedPrefManager = QCSharedPrefManager.getInstance();
        mSharedPreTeamInfo = mSharedPrefManager.getKDPreferenceTeamInfo();

        initListener();
        ActivitysManager.getInstance().finishAllFilterMore(TeamMessageActivity.class, MainActivity.class);
        requestTeamInfo();
        registerUserInfoObserver();
    }

    private void initCommTitle() {
        mCommTitle = this.findView(R.id.nim_teammessage_commtitle);
        mCommTitle.setLeftVisible(View.VISIBLE);
        mCommTitle.setRightVisible(View.VISIBLE);
        mCommTitle.getCommTitleNum().setVisibility(View.VISIBLE);
        mCommTitle.setRightImageResource(R.mipmap.main_title_plus_create_group);
        mCommTitle.setTitleTextLength(10);
        mCommTitle.setTitlePadding(0);
    }

    private void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        mCommTitle.getRightIv().setOnClickListener(this);
//        mCommTitle.getLeftTipsTv().setOnClickListener(this);
        mBannerClose.setOnClickListener(this);
        mBannerDetail.setOnClickListener(this);
        MsgMgr.getInstance().attach(this);
    }
    private void registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver = new UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        requestBuddyInfo();
                    }
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, true);
    }

    private void unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, false);
        }
    }

    private void requestBuddyInfo() {
        String userTitleName = UserInfoHelper.getUserTitleName(sessionId, ImType.Team);
        mCommTitle.setTitle(userTitleName);
        mCommTitle.setTitleTextLength(10);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null) {
            mPresenter.detachView();
        }
        unregisterUserInfoObserver();
        MsgMgr.getInstance().detach(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenHelper.getInstance().init(this, sessionId, true);
    }

    /**
     * 请求群基本信息
     */
    private void requestTeamInfo() {
        if(mPresenter == null) {
            mPresenter = new ChatPresenter();
            mPresenter.attachView(this);
        }

        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(KeyValue.GROUP_ID, sessionId);
        hashMap.put(KeyValue.Group.SHOW_GROUP_MEMBER, "0");
        mPresenter.getTeamInfo(DqUrl.url_select_group, hashMap);
    }

    /**
     * 更新群信息
     *
     */
    @SuppressLint("SetTextI18n")
    private void updateTeamInfo(final GroupInfoBean groupInfo, boolean isUpdate) {

        // 判断是否在群组内
        if (groupInfo.isWithIn()) {
            mCommTitle.setRightVisible(View.VISIBLE);
            if(isUpdate) {
                DqApp.getInstance().getSingleThread(() -> {
                    TeamDbHelper.getInstance().update(groupInfo, null);
                    List<GroupMemberBean> memberList = groupInfo.getMemberList();
                    if(memberList != null && memberList.size() > 0) {
                        MemberDbHelper.getInstance().update(sessionId, memberList, null);
                    }
                });
            }

        }else {
            mCommTitle.setRightGone();
            if(isUpdate) {
                TeamDbHelper.getInstance().delete(sessionId);
                MemberDbHelper.getInstance().delete(sessionId);
            }
        }

        mGroupInfo = groupInfo;
        DqLog.e("YM___群名:"+groupInfo.getGroup_name());
        DqLog.e("YM___群成员数量:"+groupInfo.group_member_num);
        if (!TextUtils.isEmpty(groupInfo.getGroup_name())){
            mCommTitle.setTitle(groupInfo.getGroup_name());
            mCommTitle.getCommTitleNum().setText("(" + groupInfo.group_member_num + ")");
        }

//        if (!TextUtils.isEmpty(groupInfo.banner_url) && !TextUtils.isEmpty(mGroupInfo.banner_title)) {
//            int bannerId = mSharedPreTeamInfo.getInt(getBannerKey(), 0);
//            mBannerTitle.setText(groupInfo.banner_title);
//            mBannerLlyt.setVisibility(bannerId == -11 ? View.GONE : View.VISIBLE);
//        }
        fragment.setTeam(groupInfo);
        updateUnreadUI();
    }

    @NonNull
    private String getBannerKey() {
        return QCSharedPreTeamInfo.CONVERSATION_BANNER + sessionId + mGroupInfo.banner_id;
    }

    /**
     * 更新未读消息UI
     */
    private void updateUnreadUI() {
//        mCommTitle.getLeftTipsTv().setVisibility(View.GONE);
//        int unread = ModuleMgr.getAppManager().getMessageUnread();
//        String target = Utils.formatUnread(unread);
//        if (TextUtils.isEmpty(target)) {
//            String unreadTips = DqApp.getStringById(R.string.app_name);
//            mCommTitle.setLeftTips(unreadTips);
//        } else {
//            String unreadTips = String.format(DqApp.getStringById(com.wd.daquan.R.string.unread_message_title_tips), target);
//            mCommTitle.setLeftTips(unreadTips);
//        }
    }


    @Override
    protected BaseChatMessageFragment fragment() {
        // 添加fragment
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, ImType.Team);
        fragment = new ChatTeamFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parseIntent();
        init();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_team_message_activity;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (backToClass != null) {
            Intent intent = new Intent();
            intent.setClass(this, backToClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (Utils.isFastDoubleClick(500)) {
            return;
        }
        int id = v.getId();
        if (id == mCommTitle.getLeftIvId() || id == mCommTitle.getLeftTipsId()) {
            showKeyboard(false);
            this.finish();
        } else if (id == mCommTitle.getRightIvId()) {
            NavUtils.gotoGroupChatDetailsAct(this, sessionId);
        } else if (id == mBannerClose.getId()) {
            if (mGroupInfo != null) {
                mSharedPreTeamInfo.saveInt(getBannerKey(), -11);
                mBannerLlyt.setVisibility(View.GONE);
            }
        } else if (id == mBannerDetail.getId()) {
            if (mGroupInfo == null) {
                return;
            }

            if(!TextUtils.isEmpty(mGroupInfo.banner_url)) {
                mSharedPreTeamInfo.saveInt(getBannerKey(), -11);
                mBannerLlyt.setVisibility(View.GONE);
                NavUtils.gotoWebviewActivity(this, mGroupInfo.banner_url, "帮助");
            }
        }
    }

    @Override
    public void onMessage(String key, Object value) {
        if (MsgType.MT_GROUP_SETTING_NAME.equals(key)) {
            String name = (String) value;
            mCommTitle.setTitle(name);
        }else if(MsgType.MT_TEAM_UPDATE_TEAM_INGO.equals(key)) {
            requestTeamInfo();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == KeyValue.IntentCode.REQUEST_CODE_EXIT){
            switch (resultCode){
                case RESULT_OK:
                    this.finish();
                    break;
            }
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showLoading(String tipMessage) {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if(DqUrl.url_select_group.equals(url)) {
           if(code == KeyValue.Code.DISMISS_GROUP_ERR) {//群解散不再移除群消息
//               TeamDbHelper.getInstance().delete(sessionId);
//               MemberDbHelper.getInstance().delete(sessionId);
               mCommTitle.setRightGone();
               mCommTitle.getCommTitleNum().setText("(群组已解散)");
               fragment.setTeam(null);
           }else {
              TeamDbHelper.getInstance().getTeam(sessionId, new DbSubscribe<GroupInfoBean>() {
                   @Override
                   public void complete(GroupInfoBean groupInfoBean) {
                       updateTeamInfo(groupInfoBean, false);
                   }
               });
           }
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if(DqUrl.url_select_group.equals(url)) {
            GroupInfoBean groupInfoBean = (GroupInfoBean) entity.data;
            Log.e("YM","群组内容:"+groupInfoBean.toString());
            if(groupInfoBean != null) {
                updateTeamInfo(groupInfoBean, true);
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intentUrl) {
//        super.onActivityResult(requestCode, resultCode, intentUrl);
//
//        if(null != intentUrl && resultCode == RESULT_OK) {
//            switch (requestCode) {
//                //魔方红包领取回调数据
//                case IConstants.JRMF.REQ_SINGLE:
//                case IConstants.JRMF.REQ_GROUP:
//                    getUserInfo();
//                    EnvelopeBean singleRpbean = JrmfRpClient.getEnvelopeInfo(intentUrl);
//                    CNJrmfRedMessage cnJrmfRedMessage = CNJrmfRedMessage.obtained("99", mTargetId, mUid,
//                            mNickName, mHeadPic, singleRpbean.getEnvelopesID(), singleRpbean.getEnvelopeMessage(),
//                            String.valueOf(singleRpbean.getEnvelopeType()));
//                    Message content = Message.obtain(mTargetId, mConversationType, cnJrmfRedMessage);
//                    sendMessage(content);
//                    break;
//                case PictureConfig.CHOOSE_REQUEST:
//                    sendImageVudeoMessage(intentUrl);
//                    break;
//                case KeyValue.IntentCode.REQUEST_CODE_EXIT:
//                    NavUtils.gotoMainActivity(this);
//                    break;
//            }
//        }
//    }
}
