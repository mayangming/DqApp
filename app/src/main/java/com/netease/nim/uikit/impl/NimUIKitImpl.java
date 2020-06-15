package com.netease.nim.uikit.impl;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.netease.nim.uikit.api.UIKitInitStateListener;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.api.model.location.LocationProvider;
import com.netease.nim.uikit.api.model.main.CustomPushContentProvider;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObservable;
import com.netease.nim.uikit.api.model.main.OnlineStateContentProvider;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.session.SessionEventListener;
import com.netease.nim.uikit.api.model.user.UserInfoObservable;
import com.netease.nim.uikit.business.preference.UserPreferences;
import com.netease.nim.uikit.business.session.activity.P2PMessageActivity;
import com.netease.nim.uikit.business.session.activity.TeamMessageActivity;
import com.netease.nim.uikit.business.session.emoji.StickerManager;
import com.netease.nim.uikit.business.session.module.MsgForwardFilter;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderFactory;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.storage.StorageType;
import com.netease.nim.uikit.common.util.storage.StorageUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.impl.customization.DefaultP2PSessionCustomization;
import com.netease.nim.uikit.impl.customization.DefaultTeamSessionCustomization;
import com.netease.nim.uikit.support.glide.ImageLoaderKit;

/**
 * UIKit能力实现类。
 */
public final class NimUIKitImpl {

    // context
    private static Context context;

    // 自己的用户帐号
    private static String account;

    private static UIKitOptions options;

    // 用户信息提供者
//    private static IUserInfoProvider userInfoProvider;

//    // 通讯录信息提供者
//    private static ContactProvider contactProvider;

    // 地理位置信息提供者
    private static LocationProvider locationProvider;

    // 图片加载、缓存与管理组件
    private static ImageLoaderKit imageLoaderKit;

    // 会话窗口消息列表一些点击事件的响应处理函数
    private static SessionEventListener sessionListener;

//    // 通讯录列表一些点击事件的响应处理函数
//    private static ContactEventListener contactEventListener;

    // 转发消息过滤器
    private static MsgForwardFilter msgForwardFilter;

    // 自定义推送配置
    private static CustomPushContentProvider customPushContentProvider;

    // 单聊界面定制
    private static SessionCustomization commonP2PSessionCustomization;

//    // 群聊界面定制
    private static SessionCustomization commonTeamSessionCustomization;

    // 在线状态展示内容
    private static OnlineStateContentProvider onlineStateContentProvider;

    // 在线状态变化监听
    private static OnlineStateChangeObservable onlineStateChangeObservable;

    // userInfo 变更监听
    private static UserInfoObservable userInfoObservable;

//    // contact 变化监听
//    private static ContactChangedObservable contactChangedObservable;



//    //智能机器人提供者
//    private static RobotInfoProvider robotInfoProvider;

//    // 聊天室提供者
//    private static ChatRoomProvider chatRoomProvider;
//
//    // 聊天室成员变更通知
//    private static ChatRoomMemberChangedObservable chatRoomMemberChangedObservable;

    // 缓存构建成功
    private static boolean buildCacheComplete = false;

    //初始化状态监听
    private static UIKitInitStateListener initStateListener;

    /*
     * ****************************** 初始化 ******************************
     */

    public static void init(Context context) {
        init(context, new UIKitOptions());
    }

    public static void init(Context context, UIKitOptions options) {
        NimUIKitImpl.context = context.getApplicationContext();
        NimUIKitImpl.options = options;
        // init tools
        StorageUtil.init(context, options.appCacheDir);
        ScreenUtil.init(context);

        if (options.loadSticker) {
            StickerManager.getInstance().init();
        }

        // init log
        String path = StorageUtil.getDirectoryByDirType(StorageType.TYPE_LOG);
        LogUtil.init(path, Log.DEBUG);

        NimUIKitImpl.imageLoaderKit = new ImageLoaderKit(context);

        if (!options.independentChatRoom) {
//            initUserInfoProvider(userInfoProvider);
//            initContactProvider(contactProvider);
            initDefaultSessionCustomization();
//            initDefaultContactEventListener();
            // init intentUrl cache
//            DataCacheManager.observeSDKDataChanged(true);
        }

//        ChatRoomCacheManager.initCache();
        if (!TextUtils.isEmpty(getAccount())) {
            if (options.initAsync) {
//                DataCacheManager.buildDataCacheAsync(); // build intentUrl cache on auto login
            } else {
//                DataCacheManager.buildDataCache(); // build intentUrl cache on auto login
                buildCacheComplete = true;
            }
            getImageLoaderKit().buildImageCache(); // build image cache on auto login
        }
    }

    public static boolean isInitComplete() {
        return !options.initAsync || TextUtils.isEmpty(account) || buildCacheComplete;
    }

    public static void setInitStateListener(UIKitInitStateListener listener) {
        initStateListener = listener;
    }

    public static void notifyCacheBuildComplete() {
        buildCacheComplete = true;
        if (initStateListener != null) {
            initStateListener.onFinish();
        }
    }

    /*
    * ****************************** 登录登出 ******************************
    */

    public static void loginSuccess(String account) {
        setAccount(account);
//        DataCacheManager.buildDataCache();
        buildCacheComplete = true;
        getImageLoaderKit().buildImageCache();
    }

    public static void logout() {
//        DataCacheManager.clearDataCache();
//        ChatRoomCacheManager.clearCache();
        getImageLoaderKit().clear();
    }

//    public static void enterChatRoomSuccess(EnterChatRoomResultData intentUrl, boolean independent) {
//        ChatRoomInfo roomInfo = intentUrl.getRoomInfo();
//
//        if (independent) {
//            setAccount(intentUrl.getAccount());
//            DataCacheManager.buildRobotCacheIndependent(roomInfo.getRoomId());
//        }
//
////        //存储 member
////        ChatRoomMember member = intentUrl.getMember();
////        member.setRoomId(roomInfo.getRoomId());
////        ChatRoomCacheManager.saveMyMember(member);
//    }
//
//    public static void exitedChatRoom(String roomId) {
//        ChatRoomCacheManager.clearRoomCache(roomId);
//    }

    public static UIKitOptions getOptions() {
        return options;
    }

//    // 初始化用户信息提供者
//    private static void initUserInfoProvider(IUserInfoProvider userInfoProvider) {
//
//        if (userInfoProvider == null) {
//            userInfoProvider = new DefaultUserInfoProvider();
//        }
//
//        NimUIKitImpl.userInfoProvider = userInfoProvider;
//    }

//    // 初始化联系人信息提供者
//    private static void initContactProvider(ContactProvider contactProvider) {
//        if (contactProvider == null) {
//            contactProvider = new DefaultContactProvider();
//        }
//
//        NimUIKitImpl.contactProvider = contactProvider;
//    }

    // 初始化会话定制，P2P、Team、ChatRoom
    private static void initDefaultSessionCustomization() {
        if (commonP2PSessionCustomization == null) {
            commonP2PSessionCustomization = new DefaultP2PSessionCustomization();
        }

        if (commonTeamSessionCustomization == null) {
            commonTeamSessionCustomization = new DefaultTeamSessionCustomization();
        }
    }

//    // 初始化联系人点击事件
//    private static void initDefaultContactEventListener() {
//        if (contactEventListener == null) {
//            contactEventListener = new DefaultContactEventListener();
//        }
//    }

//    public static void startP2PSession(Context context, String account) {
//        startP2PSession(context, account, null);
//    }
//
//    public static void startP2PSession(Context context, String account, IMMessage anchor) {
//        NimUIKitImpl.startChatting(context, account, SessionTypeEnum.P2P, commonP2PSessionCustomization, anchor);
//    }
//
//    public static void startTeamSession(Context context, String tid) {
//        startTeamSession(context, tid, null);
//    }

//    public static void startTeamSession(Context context, String tid, IMMessage anchor) {
//        NimUIKitImpl.startChatting(context, tid, SessionTypeEnum.Team, commonTeamSessionCustomization, anchor);
//    }

    public static void startTeamSession(Context context, String tid, SessionCustomization sessionCustomization, ImMessageBaseModel anchor, IMContentDataModel sendContent) {
        NimUIKitImpl.startChatting(context, tid, ImType.Team, sessionCustomization, anchor,sendContent);
    }

    public static void startChatting(Context context, String id, ImType sessionType, SessionCustomization
            customization, ImMessageBaseModel anchor, IMContentDataModel sendContent) {
        if (sessionType == ImType.P2P) {
            P2PMessageActivity.start(context, id, customization, anchor,sendContent);
//            startP2pMessageActivity(context, id, customization, anchor);
        } else if (sessionType == ImType.Team) {
//            startTeamMessageActivity(context, id, customization, null, anchor);
            TeamMessageActivity.start(context, id, customization, null, anchor,sendContent);

        }
    }

    public static void startChatting(Context context, String id, ImType sessionType, SessionCustomization customization,
                                     Class<? extends Activity> backToClass, ImMessageBaseModel anchor, IMContentDataModel sendContent) {
        if (sessionType == ImType.Team) {
//            startTeamMessageActivity(context, id, customization, backToClass, anchor);
            TeamMessageActivity.start(context, id, customization, backToClass, anchor,sendContent);

        }
    }

//    private static void startP2pMessageActivity(Context context, String id, SessionCustomization
//            customization, IMMessage anchor) {
//        ComponentName componentName = new ComponentName("com.meetqs.qingchat", "com.netease.nim.activity.P2PMessageActivity");
//        Intent intent = new Intent();
//        intent.putExtra(Extras.EXTRA_ACCOUNT, id);
//        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
//        if (anchor != null) {
//            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
//        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setComponent(componentName);
//        context.startActivity(intent);
//    }
//
//    public static void startTeamMessageActivity(Context context, String tid, SessionCustomization customization,
//                             Class<? extends Activity> backToClass, IMMessage anchor) {
//        ComponentName componentName = new ComponentName("com.meetqs.qingchat", "com.netease.nim.activity.P2PMessageActivity");
//        Intent intent = new Intent();
//        intent.putExtra(Extras.EXTRA_ACCOUNT, tid);
//        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
//        intent.putExtra(Extras.EXTRA_BACK_TO_CLASS, backToClass);
//        if (anchor != null) {
//            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
//        }
//        intent.setComponent(componentName);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        context.startActivity(intent);
//    }
//
//
//    public static void startContactSelector(Context context, ContactSelectActivity.Option option, int requestCode) {
//        ContactSelectActivity.startActivityForResult(context, option, requestCode);
//    }
//
    public static void startTeamInfo(Context context, String teamId) {
//        Team team = NimUIKit.getTeamProvider().getTeamById(teamId);
//        if (team == null) {
//            return;
//        }
//        if (team.getType() == TeamTypeEnum.Advanced) {
//            AdvancedTeamInfoActivity.start(context, teamId); // 启动固定群资料页
//        } else if (team.getType() == TeamTypeEnum.Normal) {
//            NormalTeamInfoActivity.start(context, teamId); // 启动讨论组资料页
//        }
    }

//    public static IUserInfoProvider getUserInfoProvider() {
//        return userInfoProvider;
//    }

    public static UserInfoObservable getUserInfoObservable() {
        if (userInfoObservable == null) {
            userInfoObservable = new UserInfoObservable(context);
        }
        return userInfoObservable;
    }


    public static LocationProvider getLocationProvider() {
        return locationProvider;
    }

    public static ImageLoaderKit getImageLoaderKit() {
        return imageLoaderKit;
    }

    public static void setLocationProvider(LocationProvider locationProvider) {
        NimUIKitImpl.locationProvider = locationProvider;
    }

    public static void setCommonP2PSessionCustomization(SessionCustomization commonP2PSessionCustomization) {
        NimUIKitImpl.commonP2PSessionCustomization = commonP2PSessionCustomization;
    }

    public static void setCommonTeamSessionCustomization(SessionCustomization commonTeamSessionCustomization) {
        NimUIKitImpl.commonTeamSessionCustomization = commonTeamSessionCustomization;
    }

    public static void registerMsgItemViewHolder(Class<? extends IMContentDataModel> attach, Class<? extends MsgViewHolderBase> viewHolder) {
        MsgViewHolderFactory.register(attach, viewHolder);
    }

    public static void setAccount(String account) {
        NimUIKitImpl.account = account;
    }

    public static SessionEventListener getSessionListener() {
        return sessionListener;
    }

    public static void setSessionListener(SessionEventListener sessionListener) {
        NimUIKitImpl.sessionListener = sessionListener;
    }

    public static void setMsgForwardFilter(MsgForwardFilter msgForwardFilter) {
        NimUIKitImpl.msgForwardFilter = msgForwardFilter;
    }

    public static MsgForwardFilter getMsgForwardFilter() {
        return msgForwardFilter;
    }


    public static CustomPushContentProvider getCustomPushContentProvider() {
        return customPushContentProvider;
    }

    public static void setCustomPushContentProvider(CustomPushContentProvider mixPushCustomConfig) {
        NimUIKitImpl.customPushContentProvider = mixPushCustomConfig;
    }

    /*
    * ****************************** 在线状态 ******************************
    */

    public static void setOnlineStateContentProvider(OnlineStateContentProvider onlineStateContentProvider) {
        NimUIKitImpl.onlineStateContentProvider = onlineStateContentProvider;
    }

    public static OnlineStateContentProvider getOnlineStateContentProvider() {
        return onlineStateContentProvider;
    }

    public static OnlineStateChangeObservable getOnlineStateChangeObservable() {
        if (onlineStateChangeObservable == null) {
            onlineStateChangeObservable = new OnlineStateChangeObservable(context);
        }
        return onlineStateChangeObservable;
    }

    public static boolean enableOnlineState() {
        return onlineStateContentProvider != null;
    }

    public static boolean getEarPhoneModeEnable() {
        return UserPreferences.isEarPhoneModeEnable();
    }

    /*
    * ****************************** basic ******************************
    */
    public static Context getContext() {
        return context;
    }

    public static String getAccount() {
        return account;
    }
}
