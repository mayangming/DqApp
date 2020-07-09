package com.wd.daquan.third.session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.da.library.tools.Utils;
import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.session.SessionEventListener;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.helper.MessageListPanelHelper;
import com.netease.nim.uikit.business.session.module.MsgForwardFilter;
import com.netease.nim.uikit.business.team.model.TeamExtras;
import com.netease.nim.uikit.business.team.model.TeamRequestCode;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.popupmenu.NIMPopupMenu;
import com.netease.nim.uikit.common.ui.popupmenu.PopupMenuItem;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.model.db.helper.MemberDbHelper;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.redpacket.action.RedPacketAction;
import com.wd.daquan.third.session.action.CameraAction;
import com.wd.daquan.third.session.action.LocalVideoAction;
import com.wd.daquan.third.session.action.QcCardAction;
import com.wd.daquan.third.session.action.VideoAction;
import com.wd.daquan.third.session.extension.CardAttachment;
import com.wd.daquan.third.session.viewholder.QcCardMsgViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * UIKit自定义消息界面用法展示类
 */
public class SessionHelper {

    private static final int ACTION_HISTORY_QUERY = 0;
    private static final int ACTION_SEARCH_MESSAGE = 1;
    private static final int ACTION_CLEAR_MESSAGE = 2;

    private static SessionCustomization p2pCustomization;
//    private static SessionCustomization normalTeamCustomization;
    private static SessionCustomization advancedTeamCustomization;
    private static SessionCustomization myP2pCustomization;
    private static SessionCustomization robotCustomization;

    private static NIMPopupMenu popupMenu;
    private static List<PopupMenuItem> menuItemList;

    public static final boolean USE_LOCAL_ANTISPAM = true;


    public static void init() {

        // 注册各种扩展消息类型的显示ViewHolder
        registerViewHolders();

        // 设置会话中点击事件响应处理
        setSessionListener();

        // 注册消息转发过滤器
        registerMsgForwardFilter();
//
//        NimUIKit.setCommonP2PSessionCustomization(getP2pCustomization(null));
//
//        NimUIKit.setCommonTeamSessionCustomization(getTeamCustomization(null));

    }

    public static void startP2PSession(Context context, String account) {
        startP2PSession(context, account, null,null);
    }
    public static void startP2PSession(Context context, String account, IMContentDataModel sendContent) {
        startP2PSession(context, account, null,sendContent);
    }

    public static void startP2PSession(Context context, String account, ImMessageBaseModel anchor, IMContentDataModel sendContent) {
        if (!ModuleMgr.getCenterMgr().getUID().equals(account)) {
            NimUIKitImpl.startChatting(context, account, ImType.P2P, getP2pCustomization(account), anchor,sendContent);
        } else {
            NimUIKit.startChatting(context, account, ImType.P2P, getMyP2pCustomization(account), anchor,sendContent);
        }
    }

    public static void startTeamSession(Context context, String tid,IMContentDataModel imContentDataModel) {
        startTeamSession(context, tid, null,imContentDataModel);
    }
    public static void startTeamSession(Context context, String tid,ImMessageBaseModel anchor) {
        startTeamSession(context, tid, anchor,null);
    }

    public static void startTeamSession(Context context, String tid) {
        startTeamSession(context, tid, null,null);
    }

    public static void startTeamSession(Context context, String tid, ImMessageBaseModel anchor,IMContentDataModel imContentDataModel) {
        NimUIKit.startTeamSession(context, tid, getTeamCustomization(tid), anchor,imContentDataModel);
    }

    // 定制化单聊界面。如果使用默认界面，返回null即可
    public static SessionCustomization getP2pCustomization(String account) {
        SessionCustomization p2pCustomization = new SessionCustomization() {
            // 由于需要Activity Result， 所以重载该函数。
            @Override
            public void onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
                super.onActivityResult(activity, requestCode, resultCode, data);

            }

            @Override
            public boolean isAllowSendMessage(ImMessageBaseModel message) {
                return true;
            }

//                @Override
//                public MsgAttachment createStickerAttachment(String category, String item) {
//                    return new StickerAttachment(category, item);
//                }
        };

        Log.w("xxxx", "getP2pCustomization() 1");
        // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
        p2pCustomization.actions = getBaseActions();
        p2pCustomization.withSticker = true;

        return p2pCustomization;
    }

    private static SessionCustomization getMyP2pCustomization(String account) {
        if (myP2pCustomization == null) {
            myP2pCustomization = new SessionCustomization() {
                // 由于需要Activity Result， 所以重载该函数。
                @Override
                public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                    if (requestCode == TeamRequestCode.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                        String result = data.getStringExtra(TeamExtras.RESULT_EXTRA_REASON);
                        if (result == null) {
                            return;
                        }
                        if (result.equals(TeamExtras.RESULT_EXTRA_REASON_CREATE)) {
                            String tid = data.getStringExtra(TeamExtras.RESULT_EXTRA_DATA);
                            if (TextUtils.isEmpty(tid)) {
                                return;
                            }

                            startTeamSession(activity, tid);
                            activity.finish();
                        }
                    }
                }

                @Override
                public boolean isAllowSendMessage(ImMessageBaseModel message) {
                    return true;
                }

//                @Override
//                public MsgAttachment createStickerAttachment(String category, String item) {
//                    return new StickerAttachment(category, item);
//                }
            };

            Log.w("xxxx", "getMyP2pCustomization() 2");
            // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
            myP2pCustomization.actions = getBaseActions();
            myP2pCustomization.withSticker = true;
        }
        return myP2pCustomization;
    }

    /**
     * 设置定义群组会话
     * @param tid
     * @return
     *  由于需要动态处理action的个数，故不能使用静态类存储
     */
    public static SessionCustomization getTeamCustomization(String tid) {
        SessionTeamCustomization.SessionTeamCustomListener listener = new SessionTeamCustomization.SessionTeamCustomListener() {
            @Override
            public void initPopupWindow(Context context, View view, String sessionId, ImType sessionTypeEnum) {
                initPopuptWindow(context, view, sessionId, sessionTypeEnum);
            }

            @Override
            public void onSelectedAccountsResult(ArrayList<String> selectedAccounts) {
//                    avChatAction.onSelectedAccountsResult(selectedAccounts);
            }

            @Override
            public void onSelectedAccountFail() {
//                    avChatAction.onSelectedAccountFail();
            }
        };
        SessionTeamCustomization normalTeamCustomization = new SessionTeamCustomization(listener) {
            @Override
            public boolean isAllowSendMessage(ImMessageBaseModel message) {
                return true;
            }
        };

        Log.w("xxxx", "getTeamCustomization() 2");
        normalTeamCustomization.actions = getBaseActions();

        return normalTeamCustomization;
    }

    @NonNull
    private static ArrayList<BaseAction> getBaseActions() {
        ArrayList<BaseAction> actions = new ArrayList<>();
//        actions.add(new ImageAction());
        actions.add(new CameraAction());
        actions.add(new VideoAction());
        actions.add(new LocalVideoAction());
//        actions.add(new RedPacketAction());

//        if(!"1".equals(account)) {
//            actions.add(new QcAliPayAction());
//        }
        actions.add(new QcCardAction());
//
//        if (ConfigManager.getInstance().enablePoke()) {
//            actions.add(new QcPokeAction());
//        }
//        actions.add(new QcCollectionAction());
//
//        if(SessionTypeEnum.Team.equals(sessionType)) {
//            actions.add(new QcAssistantAction());
//        }
//        actions.add(new AliPaymentCodeAction());
//        if(!SessionTypeEnum.Team.equals(sessionType)) {
//            actions.add(new TransferAction());
//        }
//        actions.add(new ChangeRedEnvelopeAction());

        return actions;
    }

    private static void registerViewHolders() {

//        NimUIKit.registerMsgItemViewHolder(CustomAttachment.class, QcUnknownViewHolder.class);
        // TODO: 2018/9/10 斗圈新增
        NimUIKit.registerMsgItemViewHolder(CardAttachment.class, QcCardMsgViewHolder.class);
//        NimUIKit.registerTipMsgViewHolder(QcTipViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(SdkShareAttachment.class, SdkShareMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(RedPacketAttachment.class, MsgViewHolderRedPacket.class);
//        NimUIKit.registerMsgItemViewHolder(RedPacketOpenedAttachment.class, MsgViewHolderOpenRedPacket.class);
//        NimUIKit.registerMsgItemViewHolder(UserSysTemAttachment.class, UserSystemMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(RdpRefundAttachment.class, MsgViewHolderRdpRefund.class);
//        NimUIKit.registerMsgItemViewHolder(QcFileAttachment.class, QcFileMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(QcImageAttachment.class, QcImageMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(DqVideoAttachment.class, QcVideoMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(QcExpressionAttachment.class, QcExpressionMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(QcSystemAttachment.class, QcSystemMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(QcAlipayRpAttachment.class, QcAlipayRdMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(QcContactAttachment.class, QcContactMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(QcContactNoticeAttachment.class, QcContactNoticeMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(QcRpNoticeAttachment.class, QcRpNoticeMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(ImageAttachment.class, QcImageMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(QcAssistantAttachment.class, QcAssistantMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(QcMultiportAttachment.class, QcMultiportViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(QcRpRefundAttachment.class, QcRpRefundViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(QcContactCertification.class, QcCertificationMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(QcPokeAttachment.class, QcPokeMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(QcTransferAttachment.class, QcTransferMsgViewHolder.class);
//        NimUIKit.registerMsgItemViewHolder(QcChangeRedEnvelopeAttachment.class, QcChangeMsgViewHolder.class);
    }

    private static void setSessionListener() {
        SessionEventListener listener = new SessionEventListener() {
            @Override
            public void onAvatarClicked(Context context, ImMessageBaseModel message) {
                if (message == null) {
                    return;
                }

                if (Utils.isFastDoubleClick(500)) {
                    return;
                }
                // 小助手不允许点击
                if ("1".equals(message.getFromUserId())) {
                    return;
                }
                if (ImType.typeOfValue(message.getMsgType()) == ImType.P2P) {
                    NavUtils.gotoUserInfoActivity(context,  message.getFromUserId(), message.getMsgType());
                } else {
                    //查询自己在群组的信息
                    boolean isAdmin = false;
                    GroupMemberBean groupMemberInfo =  MemberDbHelper.getInstance().getTeamMember(message.getMsgIdServer(), ModuleMgr.getCenterMgr().getUID());
                    if(groupMemberInfo != null) {
                        isAdmin = groupMemberInfo.isHighRole();
                    }
                    NavUtils.gotoUserInfoActivity(context, message.getFromUserId(), message.getMsgType(), message.getMsgIdServer(), isAdmin);
                }
            }

            @Override
            public void onAvatarLongClicked(Context context, ImMessageBaseModel message) {
                // 一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
//                Toast.makeText(DqApp.sContext, "长按头像", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAckMsgClicked(Context context, ImMessageBaseModel message) {
                // 已读回执事件处理，用于群组的已读回执事件的响应，弹出消息已读详情
//                AckMsgInfoActivity.start(context, content);
            }
        };

        NimUIKit.setSessionListener(listener);
    }


    /**
     * 消息转发过滤器
     */
    private static void registerMsgForwardFilter() {
        NimUIKit.setMsgForwardFilter(new MsgForwardFilter() {
            @Override
            public boolean shouldIgnore(ImMessageBaseModel message) {
                return false;
            }
        });
    }


    private static void initPopuptWindow(Context context, View view, String sessionId, ImType sessionTypeEnum) {
        if (popupMenu == null) {
            menuItemList = new ArrayList<>();
            popupMenu = new NIMPopupMenu(context, menuItemList, listener);
        }
        menuItemList.clear();
        menuItemList.addAll(getMoreMenuItems(context, sessionId, sessionTypeEnum));
        popupMenu.notifyData();
        popupMenu.show(view);
    }

    private static NIMPopupMenu.MenuItemClickListener listener = new NIMPopupMenu.MenuItemClickListener() {
        @Override
        public void onItemClick(final PopupMenuItem item) {
            switch (item.getTag()) {
                case ACTION_HISTORY_QUERY:
//                    MessageHistoryActivity.start(item.getContext(), item.getSessionId(), item.getSessionTypeEnum()); // 漫游消息查询
                    break;
                case ACTION_SEARCH_MESSAGE:
//                    SearchMessageActivity.start(item.getContext(), item.getSessionId(), item.getSessionTypeEnum());
                    break;
                case ACTION_CLEAR_MESSAGE:
                    EasyAlertDialogHelper.createOkCancelDiolag(item.getContext(), null, "确定要清空吗？", true, new EasyAlertDialogHelper.OnDialogActionListener() {
                        @Override
                        public void doCancelAction() {

                        }

                        @Override
                        public void doOkAction() {
                            MessageListPanelHelper.getInstance().notifyClearMessages(item.getSessionId());
                        }
                    }).show();
                    break;
            }
        }
    };

    private static List<PopupMenuItem> getMoreMenuItems(Context context, String sessionId, ImType sessionTypeEnum) {
        List<PopupMenuItem> moreMenuItems = new ArrayList<PopupMenuItem>();
        moreMenuItems.add(new PopupMenuItem(context, ACTION_HISTORY_QUERY, sessionId,
                sessionTypeEnum, DqApp.sContext.getString(R.string.message_history_query)));
        moreMenuItems.add(new PopupMenuItem(context, ACTION_SEARCH_MESSAGE, sessionId,
                sessionTypeEnum, DqApp.sContext.getString(R.string.message_search_title)));
        moreMenuItems.add(new PopupMenuItem(context, ACTION_CLEAR_MESSAGE, sessionId,
                sessionTypeEnum, DqApp.sContext.getString(R.string.message_clear)));
        return moreMenuItems;
    }
}
