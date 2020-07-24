package com.netease.nim.uikit.business.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.da.library.tools.ActivitysManager;
import com.da.library.tools.Utils;
import com.da.library.widget.CommTitle;
import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.business.session.helper.ScreenHelper;
import com.wd.daquan.MainActivity;
import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.third.friend.SetFriendInfoHelper;
import com.wd.daquan.third.helper.UserInfoHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 点对点聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class P2PMessageActivity extends BaseMessageActivity implements View.OnClickListener, QCObserver, Presenter.IView<DataBean> {

    // 询问添加好友模块
    private TextView mTipsName = null;
    private RelativeLayout mTipsRlyt = null;
    private ImageView mTipsHeadpic = null;
//    private Button mTipsBlack = null;
    private Button mTipsAdd = null;

    private CommTitle mCommTitle = null;

    private MessageFragment fragment = null;
    private SetFriendInfoHelper mFriendHelper;
    private ChatPresenter mPresenter;
    private IMContentDataModel sendContent;
    public static void start(Context context, String contactId, SessionCustomization customization, ImMessageBaseModel anchor, IMContentDataModel sendContent) {
        //Log.e("fz", "contactId : " + contactId);
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        intent.putExtra(Extras.EXTRA_IM_CONTENT, sendContent);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, P2PMessageActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        Log.w("dq", "p2pId ： " + contactId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        // 单聊特例话数据，包括个人信息，
        requestBuddyInfo();
        registerObservers(true);
        initListener();
        requestUserInfo();
        ActivitysManager.getInstance().finishAllFilterMore(P2PMessageActivity.class, MainActivity.class);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parseIntent();
        initView();
        // 单聊特例话数据，包括个人信息，
        requestBuddyInfo();
        registerObservers(true);
        initListener();
        requestUserInfo();
    }

    private void initView() {
        initCommTitle();
        mTipsRlyt = this.findView(R.id.p2p_message_addfriend_lyt);
        mTipsHeadpic = this.findView(R.id.p2p_message_tips_headpic);
        mTipsName = this.findView(R.id.p2p_message_tips_name);
//        mTipsBlack = this.findView(R.id.p2p_message_tips_addblack);
        mTipsAdd = this.findView(R.id.p2p_message_tips_addfriend);

        mFriendHelper = new SetFriendInfoHelper(this);
    }

    private void initCommTitle() {
        mCommTitle = this.findView(R.id.nim_message_commtitle);
        mCommTitle.setRightVisible(View.VISIBLE);
        mCommTitle.setRightImageResource(R.mipmap.p2p_message_details);
        mCommTitle.setTitleTextLength(10);
        mCommTitle.setTitlePadding(0);
    }

    private void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        mCommTitle.getRightIv().setOnClickListener(this);
//        mCommTitle.getLeftTipsTv().setOnClickListener(this);
//        mTipsBlack.setOnClickListener(this);
        mTipsAdd.setOnClickListener(this);
        mFriendHelper.setFriendListener(mFriendListener);
        MsgMgr.getInstance().attach(this);
    }

    @Override
    protected void onDestroy() {
        //阅后即焚
        FriendDbHelper.getInstance().getFriend(sessionId, friend -> {
            if(friend != null) {
                long burnTime = friend.getBurn();
                if(burnTime > 0) {
                }
            }
        });


        if(mFriendHelper != null) {
            mFriendHelper.detach();
        }

        super.onDestroy();
        registerObservers(false);
        MsgMgr.getInstance().detach(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenHelper.getInstance().init(this, sessionId, false);
        if (null != fragment) {
            fragment.refreshMessageList();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 请求群基本信息
     */
    private void requestUserInfo() {
        FriendDbHelper.getInstance().getFriend(sessionId, friend -> {
            if(friend != null) {
                updateUserInfo(friend);
            }else {
                getUserInfo();
            }
        });

    }

    private void updateUserInfo(Friend friend) {
        // 小助手
        if ("1".equals(sessionId)) {
            mTipsRlyt.setVisibility(View.GONE);
            mCommTitle.setRightGone();
            return;
        }

        if (friend.uid.equals(ModuleMgr.getCenterMgr().getUID())) {
            mTipsRlyt.setVisibility(View.GONE);
            return;
        }
        // 非好友，黑名单
        if (!friend.isWhether_friend() || friend.isWhether_black()) {
            mTipsRlyt.setVisibility(View.VISIBLE);
//            mCommTitle.setRightGone();
            GlideUtils.loadHeader(this, friend.headpic, mTipsHeadpic);
        } else {
            // 好友
            mTipsRlyt.setVisibility(View.GONE);
        }

        updateUnreadUI();
    }

    /**
     * 更新未读消息UI
     */
    private void updateUnreadUI() {
        mCommTitle.getLeftTipsTv().setVisibility(View.GONE);
//        int unread = ModuleMgr.getAppManager().getMessageUnread();
//        String target = Utils.formatUnread(unread);
//        if (TextUtils.isEmpty(target)) {
//            String unreadTips = DqApp.getStringById(R.string.app_name);
//            mCommTitle.setLeftTips(unreadTips);
//        } else {
//            String unreadTips = String.format(DqApp.getStringById(R.string.unread_message_title_tips), target);
//            mCommTitle.setLeftTips(unreadTips);
//        }
    }

    private void requestBuddyInfo() {
        String userTitleName = UserInfoHelper.getUserTitleName(sessionId, ImType.P2P);
        mCommTitle.setTitle(userTitleName);
        mCommTitle.setTitleTextLength(10);
    }

    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
    }

    private UserInfoObserver uinfoObserver;

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        requestBuddyInfo();
                    }
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, true);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, false);
        }
    }

    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, ImType.P2P);
        fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_message_activity;
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
        } else
        if (id == mCommTitle.getRightIvId()) {
            NavUtils.gotoSingleChatDetailsAct(this, sessionId);
        }
//        else if (id == mTipsBlack.getId()) {
//            mFriendHelper.setFriendUid(sessionId);
//            mFriendHelper.addBlackList();
//        }
        else if (id == mTipsAdd.getId()) {
            mFriendHelper.addFriend(sessionId);
        }
    }

    private SetFriendInfoHelper.FriendListener mFriendListener = new SetFriendInfoHelper.FriendListener() {

        @Override
        public void onAddFriend(boolean success) {
            Log.e("YM","mFriendListener()--------->好友添加成功:"+success);
            if(success) {
                mTipsRlyt.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_FRIEND_REMOVE_FRIEND:
            case MsgType.MT_FRIEND_ADD_BLACK_LIST:
//                ActivitysManager.getInstance().finish(this);
                break;
            case MsgType.MT_FRIEND_ADD_FRIEND:
                Log.e("YM","onMessage()--------->好友添加成功");
                mTipsRlyt.setVisibility(View.GONE);
                getUserInfo();
                break;
        }
    }

    private void getUserInfo() {
        if(mPresenter == null) {
            mPresenter = new ChatPresenter();
            mPresenter.attachView(this);
        }
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.UserInfo.OTHER_UID, sessionId);
        mPresenter.getUserInfo(DqUrl.url_get_userinfo, hashMap);
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

    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if(DqUrl.url_get_userinfo.equals(url)) {
            Friend friend = (Friend) entity.data;
            updateUserInfo(friend);
        }
    }
}
