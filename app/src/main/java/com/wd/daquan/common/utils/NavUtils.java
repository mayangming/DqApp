package com.wd.daquan.common.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import androidx.fragment.app.FragmentActivity;
import android.text.TextUtils;

import com.da.library.constant.IConstant;
import com.da.library.widget.AnimUtils;
import com.dq.im.type.ImType;
import com.wd.daquan.MainActivity;
import com.wd.daquan.R;
import com.wd.daquan.chat.ait.AitActivity;
import com.wd.daquan.chat.card.CardActivity;
import com.wd.daquan.chat.emotion.MyEmotionActivity;
import com.wd.daquan.chat.friend.SelectGroupMemberActivity;
import com.wd.daquan.chat.friend.SelectedActivity;
import com.wd.daquan.chat.group.activity.AddGroupActivity;
import com.wd.daquan.chat.group.activity.ExitGroupMembersActivity;
import com.wd.daquan.chat.group.activity.GroupAidesActivity;
import com.wd.daquan.chat.group.activity.GroupAidesDetailsActivity;
import com.wd.daquan.chat.group.activity.GroupChatNameActivity;
import com.wd.daquan.chat.group.activity.GroupCopyNewActivity;
import com.wd.daquan.chat.group.activity.GroupDetailsActivity;
import com.wd.daquan.chat.group.activity.GroupLongTimeRpActivity;
import com.wd.daquan.chat.group.activity.GroupManagerActivity;
import com.wd.daquan.chat.group.activity.GroupManagerSettingActivity;
import com.wd.daquan.chat.group.activity.GroupMemberForbidDoRPActivity;
import com.wd.daquan.chat.group.activity.GroupMemberForbidDoRPMemberActivity;
import com.wd.daquan.chat.group.activity.GroupMembersActivity;
import com.wd.daquan.chat.group.activity.GroupNoticeActivity;
import com.wd.daquan.chat.group.activity.GroupNumberActivity;
import com.wd.daquan.chat.group.activity.GroupPersonalInfosActivity;
import com.wd.daquan.chat.group.activity.GroupSearchChatActivity;
import com.wd.daquan.chat.group.activity.GroupSearchChatTypeActivity;
import com.wd.daquan.chat.group.activity.SearchGroupAidesActivity;
import com.wd.daquan.chat.group.activity.SelectFriendsActivity;
import com.wd.daquan.chat.group.activity.TeamListActivity;
import com.wd.daquan.chat.group.activity.TotalGroupMemberActivity;
import com.wd.daquan.chat.group.bean.GroupAssistAuth;
import com.wd.daquan.chat.group.bean.GroupSearchChatType;
import com.wd.daquan.chat.group.bean.PluginBean;
import com.wd.daquan.chat.group.bean.QrEntity;
import com.wd.daquan.chat.redpacket.RedPacketGroupSendAct;
import com.wd.daquan.chat.redpacket.RedPacketPersonalSendAct;
import com.wd.daquan.chat.redpacket.activity.AlipayBindingActivity;
import com.wd.daquan.chat.redpacket.activity.AlipayP2pActivity;
import com.wd.daquan.chat.redpacket.activity.AlipayTeamActivity;
import com.wd.daquan.chat.redpacket.activity.RedpacketExclusiveRobActivity;
import com.wd.daquan.chat.redpacket.activity.RedpacketP2pDetailActivity;
import com.wd.daquan.chat.redpacket.activity.RedpacketRobActivity;
import com.wd.daquan.chat.redpacket.transfer.TransferP2PReceiveAct;
import com.wd.daquan.chat.search.SearchConversationActivity;
import com.wd.daquan.chat.single.SingleDetailsActivity;
import com.wd.daquan.common.activity.LookBigPhotoActivity;
import com.wd.daquan.common.activity.ShareActivity;
import com.wd.daquan.common.activity.ShareP2pOrNewTeamActivity;
import com.wd.daquan.common.activity.ShareTeamActivity;
import com.wd.daquan.common.activity.WebViewActivity;
import com.wd.daquan.common.bean.ShareBean;
import com.wd.daquan.common.constant.Constants;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.RequestCode;
import com.wd.daquan.contacts.activity.AddFriendActivity;
import com.wd.daquan.contacts.activity.GroupPersonalInfoActivity;
import com.wd.daquan.contacts.activity.InviteMobileContactActivity;
import com.wd.daquan.contacts.activity.MobileContactsListActivity;
import com.wd.daquan.contacts.activity.NewFriendActivity;
import com.wd.daquan.contacts.activity.NewFriendDetailActivity;
import com.wd.daquan.contacts.activity.RemarksImgZoomAct;
import com.wd.daquan.contacts.activity.SavedTeamsActivity;
import com.wd.daquan.contacts.activity.SearchAddFriendActivity;
import com.wd.daquan.contacts.activity.SearchFriendActivity;
import com.wd.daquan.contacts.activity.SetRemarkNameActivity;
import com.wd.daquan.contacts.activity.UserInfoActivity;
import com.wd.daquan.explore.activity.ApplyRefundActivity;
import com.wd.daquan.explore.activity.FriendAreaActivity;
import com.wd.daquan.explore.activity.DynamicMediaDetailsActivity;
import com.wd.daquan.explore.activity.DynamicSendActivity;
import com.wd.daquan.explore.activity.MakeMoneyActivity;
import com.wd.daquan.explore.activity.RefundResultActivity;
import com.wd.daquan.explore.activity.RefundDetailsActivity;
import com.wd.daquan.explore.activity.ReleaseTaskCompleteActivity;
import com.wd.daquan.explore.activity.SendTaskActivity;
import com.wd.daquan.explore.activity.TaskDetailsActivity;
import com.wd.daquan.explore.activity.SendTaskManagerActivity;
import com.wd.daquan.explore.activity.TaskMineActivity;
import com.wd.daquan.explore.activity.UnReadMsgActivity;
import com.wd.daquan.explore.type.SearchType;
import com.wd.daquan.imui.activity.VideoDetailsActivity;
import com.wd.daquan.imui.constant.IntentCode;
import com.wd.daquan.login.activity.BindPhoneActivity;
import com.wd.daquan.login.activity.DownAppActivity;
import com.wd.daquan.login.activity.ForgetLoginPasswordActivity;
import com.wd.daquan.login.activity.LoginCodeActivity;
import com.wd.daquan.login.activity.LoginCodeQuickActivity;
import com.wd.daquan.login.activity.LoginPasswordActivity;
import com.wd.daquan.login.activity.LoginPasswordQuickActivity;
import com.wd.daquan.login.activity.LoginRegisterActivity;
import com.wd.daquan.login.activity.RegisterActivity;
import com.wd.daquan.login.activity.SetLoginPasswordActivity;
import com.wd.daquan.login.activity.SetUserInfoActivity;
import com.wd.daquan.login.multiport.MultiportActivity;
import com.wd.daquan.mine.activity.AboutQSActivity;
import com.wd.daquan.mine.activity.BindWxActivity;
import com.wd.daquan.mine.activity.BlackListActivity;
import com.wd.daquan.mine.activity.ChatBGSettingDetailsActivity;
import com.wd.daquan.mine.activity.ChatBGSettingInActivity;
import com.wd.daquan.mine.activity.ComplaintActivity;
import com.wd.daquan.mine.activity.FeedbackActivity;
import com.wd.daquan.mine.activity.IntegralExchangeDetailActivity;
import com.wd.daquan.mine.activity.IntegralExchangeRecordActivity;
import com.wd.daquan.mine.activity.IntegralMallActivity;
import com.wd.daquan.mine.activity.LoginPwdModifyActivity;
import com.wd.daquan.mine.activity.LoginPwdSettingActivity;
import com.wd.daquan.mine.activity.NewMsgNotifyActivity;
import com.wd.daquan.mine.activity.PayPasswordActivity;
import com.wd.daquan.mine.activity.SafeActivity;
import com.wd.daquan.mine.activity.SettingActivity;
import com.wd.daquan.mine.activity.SignUpDetailActivity;
import com.wd.daquan.mine.activity.TextSizeActivity;
import com.wd.daquan.mine.activity.VipActivity;
import com.wd.daquan.mine.activity.VipExchangeActivity;
import com.wd.daquan.mine.activity.WalletCloudActivity;
import com.wd.daquan.mine.activity.WithdrawActivity;
import com.wd.daquan.mine.activity.WithdrawRecordActivity;
import com.wd.daquan.mine.activity.WithdrawResultActivity;
import com.wd.daquan.mine.alipay.AccountBlindActivity;
import com.wd.daquan.mine.alipay.AliPayWebviewActivity;
import com.wd.daquan.mine.alipay.AlipayAuthActivity;
import com.wd.daquan.mine.alipay.AlipayPayListActivity;
import com.wd.daquan.mine.alipay.AlipayRedDetailsActivity;
import com.wd.daquan.mine.bean.AlipayRedListEntity;
import com.wd.daquan.mine.collection.CollectionActivity;
import com.wd.daquan.mine.safe.AboutFreezeActivity;
import com.wd.daquan.mine.safe.DoAboutFreezeActivity;
import com.wd.daquan.mine.safe.DoFreezeNextActivity;
import com.wd.daquan.mine.safe.FreezeTypeActivity;
import com.wd.daquan.mine.safe.SafeCenterActivity;
import com.wd.daquan.mine.scan.QCPhotoAlbumListActivity;
import com.wd.daquan.mine.scan.QRCodeActivity;
import com.wd.daquan.mine.scan.ScanQRCodeActivity;
import com.wd.daquan.mine.user.PersonalAvatarSettingActivity;
import com.wd.daquan.mine.user.PersonalInfoActivity;
import com.wd.daquan.mine.user.PersonalInfoSettingActivity;
import com.wd.daquan.mine.wallet.activity.AddBankCardActivity;
import com.wd.daquan.mine.wallet.activity.BankCardDetailsActivity;
import com.wd.daquan.mine.wallet.activity.CashOutActivity;
import com.wd.daquan.mine.wallet.activity.CheckPayPwdActivity;
import com.wd.daquan.mine.wallet.activity.ChoiceProfessionalActivity;
import com.wd.daquan.mine.wallet.activity.ForgotSetPayPwdActivity;
import com.wd.daquan.mine.wallet.activity.LooseChangeTransationListActivity;
import com.wd.daquan.mine.wallet.activity.MyBankCardActivity;
import com.wd.daquan.mine.wallet.activity.PaySmsCodeActivity;
import com.wd.daquan.mine.wallet.activity.RechargeActivity;
import com.wd.daquan.mine.wallet.activity.RedTransationListActivity;
import com.wd.daquan.mine.wallet.activity.SetPayPwdActivity;
import com.wd.daquan.mine.wallet.activity.VerificationCodeActivity;
import com.wd.daquan.mine.wallet.activity.WalletActivity;
import com.wd.daquan.mine.wallet.activity.WalletVerifyActivity;
import com.wd.daquan.mine.wallet.activity.WriteBankCardInfoActivity;
import com.wd.daquan.mine.wallet.bean.BankCardBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.bean.NewFriendBean;
import com.wd.daquan.model.bean.SignUpEntity;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.sdk.DqSdkLoginActivity;
import com.wd.daquan.sdk.DqSdkP2pOrNewTeamShareActivity;
import com.wd.daquan.sdk.DqSdkShareActivity;
import com.wd.daquan.sdk.DqSdkTeamActivity;
import com.wd.daquan.sdk.bean.SdkShareBean;
import com.wd.daquan.third.session.extension.CustomAttachment;
import com.wd.daquan.third.session.extension.QcTransferAttachment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2018/9/10.
 */

public class NavUtils {

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    public static void startAppSettings(Activity context) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivityForResult(intent, IConstant.PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 打开H5
     */
    public static void gotoWebviewActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.WEBVIEW_URL, url);
        intent.putExtra(Constants.WEBVIEW_TITLE, title);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 打开H5 传递参数
     */
    public static void gotoWebviewActivity(Context context, String url, String title, GroupAssistAuth groupAssistAuth) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.WEBVIEW_URL, url);
        intent.putExtra(Constants.WEBVIEW_TITLE, title);
        intent.putExtra(Constants.GROUP_ASSIST_AUTH, groupAssistAuth);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    //意见反馈
    public static void gotoFeedBack(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    //设置
    public static void gotoSettingActivity(Context activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }
    //Vip页面
    public static void gotoVipActivity(Context activity) {
        Intent intent = new Intent(activity, VipActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }
    //朋友圈页面
    public static void gotoFriendAreaActivity(Context activity, String friendId, SearchType searchType) {
        Intent intent = new Intent(activity, FriendAreaActivity.class);
        intent.putExtra(FriendAreaActivity.USER_ID,friendId);
        intent.putExtra(FriendAreaActivity.SEARCH_TYPE,searchType);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }
    //从未读消息页面过来
    public static void gotoFriendAreaActivity(Context activity, String friendId, SearchType searchType,String dynamicId) {
        Intent intent = new Intent(activity, FriendAreaActivity.class);
        intent.putExtra(FriendAreaActivity.USER_ID,friendId);
        intent.putExtra(FriendAreaActivity.SEARCH_TYPE,searchType);
        intent.putExtra(FriendAreaActivity.DYNAMIC_ID,dynamicId);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }
    //零钱页面
    public static void gotoWalletCloudActivity(Context activity) {
        Intent intent = new Intent(activity, WalletCloudActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    /**
     * 提现页面
     * @param balance 全部余额
     */
    public static void gotoWithdrawActivity(Context activity,long balance) {
        Intent intent = new Intent(activity, WithdrawActivity.class);
        intent.putExtra(WithdrawActivity.WITHDRAW_BALANCE,balance);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    //提现结果页面
    public static void gotoWithdrawResultActivity(Context activity,int resultCode,String resultMessage) {
        Intent intent = new Intent(activity, WithdrawResultActivity.class);
        intent.putExtra(WithdrawResultActivity.WITHDRAW_RESULT_STATUS,resultCode);
        intent.putExtra(WithdrawResultActivity.WITHDRAW_RESULT_MESSAGE,resultMessage);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    //提现记录页面
    public static void gotoWithdrawRecordActivity(Context activity) {
        Intent intent = new Intent(activity, WithdrawRecordActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    //设置零钱支付密码/修改零钱支付密码
    public static void gotoPayPasswordActivityActivity(Context activity) {
        Intent intent = new Intent(activity, PayPasswordActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    //绑定微信页
    public static void gotoBindWxActivity(Context activity) {
        Intent intent = new Intent(activity, BindWxActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    //Vip兑换页面
    public static void gotoVipExchangeActivity(Context activity) {
        Intent intent = new Intent(activity, VipExchangeActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    //绑定账号
    public static void gotoAccountBlindActivity(Context activity) {
        Intent intent = new Intent(activity, AccountBlindActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    //新消息通知
    public static void gotoNewMsgNotifyActivity(Context activity) {
        Intent intent = new Intent(activity, NewMsgNotifyActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    //安全与隐私
    public static void gotoSafeActivity(Context activity) {
        Intent intent = new Intent(activity, SafeActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    //设置登录密码
    public static void gotoLoginPwdSettingActivity(Context activity) {
        Intent intent = new Intent(activity, LoginPwdSettingActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    //修改登录密码
    public static void gotoLoginPwdModifyActivity(Context activity) {
        Intent intent = new Intent(activity, LoginPwdModifyActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    //黑名单
    public static void gotoBlackListActivity(Context context) {
        Intent intent = new Intent(context, BlackListActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    //安全中心
    public static void gotoSafeCenterActivity(Context context) {
        Intent intent = new Intent(context, SafeCenterActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    //关于冻结/解冻
    public static void gotoAboutFreezeActivity(Context context, String type) {
        Intent intent = new Intent(context, AboutFreezeActivity.class);
        intent.putExtra(KeyValue.ABOUT_FREEZE_TYPE, type);
        ((Activity) context).startActivityForResult(intent, KeyValue.IntentCode.REQUEST_CODE_EXIT);
        AnimUtils.enterAnimForActivity(context);
    }

    //do冻结
    public static void gotoDoAboutFreezeActivity(Activity context, String type) {
        Intent intent = new Intent(context, DoAboutFreezeActivity.class);
        intent.putExtra(KeyValue.ABOUT_FREEZE_TYPE, type);
        context.startActivityForResult(intent, KeyValue.IntentCode.REQUEST_CODE_EXIT);
        AnimUtils.enterAnimForActivity(context);
    }

    //选择冻结的类型
    public static void gotoFreezeTypeActivity(Activity context, String phone) {
        Intent intent = new Intent(context, FreezeTypeActivity.class);
        intent.putExtra(KeyValue.PHONE, phone);
        context.startActivityForResult(intent, KeyValue.IntentCode.REQUEST_CODE_EXIT);
        AnimUtils.enterAnimForActivity(context);
    }

    //冻结/接送的下一步
    public static void gotoDoFreezeNextActivity(Activity context, String phone, String type) {
        Intent intent = new Intent(context, DoFreezeNextActivity.class);
        intent.putExtra(KeyValue.PHONE, phone);
        intent.putExtra(KeyValue.ABOUT_FREEZE_TYPE, type);
        context.startActivityForResult(intent, KeyValue.IntentCode.REQUEST_CODE_EXIT);
        AnimUtils.enterAnimForActivity(context);
    }

    //    //字体大小
    public static void gotoTextSizeActivity(Context activity) {
        Intent intent = new Intent(activity, TextSizeActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    //聊天背景
    public static void gotoChatBGSettingActivity(Activity context) {
        Intent intent = new Intent(context, ChatBGSettingInActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    //聊天背景详情
    public static void gotoChatBGSettingDetailsActivity(Activity context, String type) {
        Intent intent = new Intent(context, ChatBGSettingDetailsActivity.class);
        intent.putExtra(KeyValue.CHAT_BG_TYPE, type);
        context.startActivityForResult(intent, KeyValue.PHOTO_REQUEST_LOCAL);
        AnimUtils.enterAnimForActivity(context);
    }

    public static void gotoUpdateActivity(Context context, String url, int updatetyle, String version) {
//        Intent intent=new Intent(context, UpStateActivity.class);
//        intent.putExtra("url",url);
//        intent.putExtra("updatetyle",updatetyle);
//        intent.putExtra("version",version);
//        context.startActivity(intent);
//        AnimUtils.enterAnimForActivity(context);
    }

    public static void gotoPersonalInfoSettingActivity(Context context, String type) {
        Intent intent = new Intent(context, PersonalInfoSettingActivity.class);
        intent.putExtra(KeyValue.PERSONAL_SETTING_TYPE, type);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    //个人信息
    public static void gotoPersonalInfoActivity(Context activity) {
        Intent intent = new Intent(activity, PersonalInfoActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    //    //修改头像
    public static void gotoPersonalAvatarSettingActivity(Context activity) {
        Intent intent = new Intent(activity, PersonalAvatarSettingActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    //    //关于斗圈
    public static void gotoAboutQSActivity(Context activity) {
        Intent intent = new Intent(activity, AboutQSActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    // 投诉举报
    public static void gotoComplaintActivity(Context activity) {
        Intent intent = new Intent(activity, ComplaintActivity.class);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }


    /**
     * 注册完成更新用户数据
     */
    public static void gotoUpdatePersonalDataActivity(Context context) {
        Intent intent = new Intent(context, SetUserInfoActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 主页
     */
    public static void gotoMainActivity(Context context) {
//        DqToast.showLong("gotoMainActivity"+ context.toString());
//        Log.e("MainActivity" , "MainActivity : " +  context.toString());
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 选择登录注册页面
     */
    public static void gotoLoginRegisterActivity(Context context) {
        Intent intent = new Intent(context, LoginRegisterActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 设置登录密码
     */
    public static void gotoSetPassword(Context context) {
        Intent intent = new Intent(context, SetLoginPasswordActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 密码登录
     */
    public static void gotoLoginPasswordActivity(Context context) {
        DqLog.e("YM------->密码登陆");
        Intent intent = new Intent(context, LoginPasswordActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 密码登录
     */
    public static void gotoLoginPasswordActivity(Context context, String key) {
        Intent intent = new Intent(context, LoginPasswordActivity.class);
        intent.putExtra(KeyValue.SDKLogin.SDK_LOGIN_KEY, key);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 注册页面
     */
    public static void gotoRegisterActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 快捷密码登录页面
     */
    public static void gotoLoginPwdAgainActivity(Context context) {
        Intent intent = new Intent(context, LoginPasswordQuickActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 验证码登录
     */
    public static void gotoLoginCodeActivity(Context context, String key) {
        Intent intent = new Intent(context, LoginCodeActivity.class);
        intent.putExtra(KeyValue.SDKLogin.SDK_LOGIN_KEY, key);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 验证码快捷登录
     */
    public static void gotoLoginCodeAgainActivity(Context context) {
        Intent intent = new Intent(context, LoginCodeQuickActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 忘记登录密码页面
     */
    public static void gotoForgetLoginPasswordActivity(Context context) {
        Intent intent = new Intent(context, ForgetLoginPasswordActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 微信登录绑定手机号
     */
    public static void gotoBindPhoneActivity(Context context, Map<String, String> wxMap) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        intent.putExtra(IConstant.WX.WXMAP, (Serializable) wxMap);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    public static void gotoUserInfoActP2P(Context context, String userId) {
        gotoUserInfoActivity(context, userId, ImType.P2P.getValue(), "", false, false, false, false);
    }

    public static void gotoUserInfoActTeam(Context context, String groupID, String userId, Boolean isMaster) {
        gotoUserInfoActivity(context, userId, ImType.Team.getValue(), groupID, isMaster, false, false, false);
    }

    /**
     * 个人详情资料页面
     */
    public static void gotoUserInfoActivity(Context context, String userId, String value) {
        gotoUserInfoActivity(context, userId, value, "", false, false, false, false);
    }

    /**
     * 个人详情资料页面，从群进去
     */
    public static void gotoUserInfoActivity(Context context, String userId, String value, String groupId, Boolean isMaster) {
        gotoUserInfoActivity(context, userId, value, groupId, isMaster, false, false, false);
    }

    /**
     * 个人详情资料页面
     *
     * @param userId      用户id
     * @param sessionType 会话类型 0单 1群
     * @param groupId     群id
     * @param isMaster    是否群主
     * @param isExit      是否已经退出的群成员
     * @param isCard      是否是名片
     * @param isSearch    是否是搜索
     */
    public static void gotoUserInfoActivity(Context context, String userId, String sessionType,
                                            String groupId, boolean isMaster, boolean isExit, boolean isCard, boolean isSearch) {
        Intent intent = new Intent(context, UserInfoActivity.class);
//        intent.putExtra(IConstant.UserInfo.FRIEND, friend);
        intent.putExtra(IConstant.UserInfo.UID, userId);
        intent.putExtra(IConstant.UserInfo.SESSION_TYPE, sessionType);
        intent.putExtra(IConstant.UserInfo.GROUP_ID, groupId);
        intent.putExtra(IConstant.UserInfo.IS_MASTER, isMaster);
        intent.putExtra(IConstant.UserInfo.IS_EXIT_MEMBER, isExit);
        intent.putExtra(IConstant.UserInfo.IS_CARD, isCard);
        intent.putExtra(IConstant.UserInfo.IS_SEARCH, isSearch);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 支付宝红包群组页
     */
    public static void gotoAlipayTeamActivity(Context context, int requestCode, String account) {
        Intent intent = new Intent(context, AlipayTeamActivity.class);
        intent.putExtra(KeyValue.KEY_ACCOUNT, account);
        ((Activity) context).startActivityForResult(intent, requestCode);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 支付宝红包群组页-@头像专属红包
     *
     * @param context
     * @param requestCode
     * @param account
     */
    public static void gotoAlipayTeamActivity(Context context, int requestCode, String account, String exclusiveName, String exclusiveId) {
        Intent intent = new Intent(context, AlipayTeamActivity.class);
        intent.putExtra(KeyValue.KEY_ACCOUNT, account);
        intent.putExtra(KeyValue.Exclusive.EXCLUSIVE_NAME, exclusiveName);
        intent.putExtra(KeyValue.Exclusive.EXCLUSIVE_ID, exclusiveId);
        ((Activity) context).startActivityForResult(intent, requestCode);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 支付宝红包个人页
     */
    public static void gotoAlipayP2pActivity(Context context, int requestCode, String account) {
        Intent intent = new Intent(context, AlipayP2pActivity.class);
        intent.putExtra(KeyValue.KEY_ACCOUNT, account);
        ((Activity) context).startActivityForResult(intent, requestCode);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 跳转到支部宝绑定页面
     *
     * @param context
     */
    public static void gotoAlipayBindingActivity(Context context) {
        Intent intent = new Intent(context, AlipayBindingActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 添加好友界面
     */
    public static void gotoAddFriendActivity(Context context) {
        Intent intent = new Intent(context, AddFriendActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 搜索添加好友页面
     */
    public static void gotoSearchAddFriendActivity(Context context) {
        Intent intent = new Intent(context, SearchAddFriendActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 新的朋友页面
     */
    public static void gotoNewFriendActivity(Context context) {
        Intent intent = new Intent(context, NewFriendActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    //支付宝授权
    public static void gotoAlipayAuthActivity(Context context) {
        Intent intent = new Intent(context, AlipayAuthActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    //支付宝红包列表
    public static void gotoAlipayPayListActivity(Context context) {
        Intent intent = new Intent(context, AlipayPayListActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    //支付宝详情
    public static void gotoAlipayRedDetailsActivity(Context context, AlipayRedListEntity.ListBean data) {
        Intent intent = new Intent(context, AlipayRedDetailsActivity.class);
        intent.putExtra("redDetails", data);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }


    /**
     * 手机通讯录匹配
     */
    public static void gotoMobileContactsListActivity(Context context) {
        Intent intent = new Intent(context, MobileContactsListActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 邀请手机联系人
     */
    public static void gotoInviteMobileContactActivity(Activity context, String enterType, int requestCode) {
        Intent intent = new Intent(context, InviteMobileContactActivity.class);
        intent.putExtra(IConstant.ENTER_TYPE, enterType);
        context.startActivityForResult(intent, requestCode);
        AnimUtils.enterAnimForActivity(context);
    }


    /**
     * 跳转到抢红包页面
     */
    public static void gotoRedpacketRobActivity(Context context, CustomAttachment attachment, boolean open, String pay, String type, String targetId) {
        Intent intent = new Intent(context, RedpacketRobActivity.class);
        intent.putExtra(KeyValue.RedpacktRob.ATTACHMENT, attachment);
        intent.putExtra(KeyValue.RedpacktRob.IS_OPEN, open);
        intent.putExtra(KeyValue.RedpacktRob.TYPE, type);
        intent.putExtra(KeyValue.RedpacktRob.PAY, pay);
        intent.putExtra(KeyValue.RedpacktRob.TARGET_ID, targetId);
//        context.startActivity(intent);
        ((Activity) context).startActivityForResult(intent, KeyValue.RedpacktRob.RESPONSE);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 查看大图
     */
    public static void gotoLookBigPhotoActivity(Context context, String photoPath) {
        Intent intent = new Intent(context, LookBigPhotoActivity.class);
        intent.putExtra(IConstant.UserInfo.HEADPIC, photoPath);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 设置好友备注
     *
     * @param enterType 个人/群组
     * @param userId    用户id
     * @param userName  用户名
     */
    public static void gotoSetRemarkNameActivity(Activity context, String enterType, String userId,
                                                 String userName, ArrayList<String> phones, String desc, String card, String groupId) {
        Intent intent = new Intent(context, SetRemarkNameActivity.class);
        intent.putExtra(IConstant.UserInfo.SESSION_TYPE, enterType);
        intent.putExtra(IConstant.UserInfo.UID, userId);
        intent.putExtra(IConstant.UserInfo.UNREMARK, userName);
        intent.putStringArrayListExtra(KeyValue.Remark.PHONES, phones);
        intent.putExtra(KeyValue.Remark.DESCRIPTIONS, desc);
        intent.putExtra(KeyValue.Remark.CARD, card);
        intent.putExtra(IConstant.UserInfo.GROUP_ID, groupId);
        context.startActivityForResult(intent, 100);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 图片放大
     * @param activity
     * @param toUid
     * @param remarksImg
     */
    public static void gotoRemarksImgZoomAct(FragmentActivity activity, String toUid, String remarksImg) {
        Intent intent = new Intent(activity, RemarksImgZoomAct.class);
        intent.putExtra(KeyValue.Remark.TO_UID, toUid);
        intent.putExtra(KeyValue.Remark.CARD, remarksImg);
        activity.startActivityForResult(intent, KeyValue.HeadPortrait.TYPE_DEL_IMG);
        AnimUtils.enterAnimForActivity(activity);
    }

    /**
     * 跳转到专属红包
     */
    public static void gotoRedpacketExclusiveRobActivity(Context context, CustomAttachment attachment, String targetId) {
        Intent intent = new Intent(context, RedpacketExclusiveRobActivity.class);
        intent.putExtra(KeyValue.RedpacktRob.ATTACHMENT, attachment);
        intent.putExtra(KeyValue.RedpacktRob.TARGET_ID, targetId);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 支付宝单人详情页
     */
    public static void gotoRedpacketP2pDetailActivity(Context context, CustomAttachment attachment) {
        Intent intent = new Intent(context, RedpacketP2pDetailActivity.class);
        intent.putExtra(KeyValue.RedpacktRob.ATTACHMENT, attachment);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

//    /**
//     * 支付宝群组详情页
//     */
//    public static void gotoRedpacketTeamDetailActivity(Context context, IMMessage content) {
//        Intent intent = new Intent(context, RedpacketTeamDetailActivity.class);
//        intent.putExtra(KeyValue.RedpacktRob.MESSAGE, content);
//        context.startActivity(intent);
//        AnimUtils.enterAnimForActivity(context);
//    }

//    /**
//     * P2P转帐
//     * @param context
//     * @param uid
//     */
//    public static void gotoTransferP2PAct(Context context, String uid) {
//        Intent intent = new Intent(context, TransferP2PAct.class);
//        intent.putExtra(KeyValue.Transfer.UID, uid);
//        context.startActivity(intent);
//        AnimUtils.enterAnimForActivity(context);
//    }

    /**
     * P2P接收转账
     * @param context
     * @param attachment
     */
    public static void gotoTransferP2PReceiveAct(Context context, QcTransferAttachment attachment, String transferStatus) {
        Intent intent = new Intent(context, TransferP2PReceiveAct.class);
        intent.putExtra(KeyValue.Transfer.ATTACHMENT, (Parcelable) attachment);
        intent.putExtra(KeyValue.Transfer.TRANSFERSTATUS, transferStatus);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }



    /**
     * 个人红包
     * @param context
     * @param uid
     */
    public static void gotoRedPacketPersonalSendAct(Context context, String uid) {
        Intent intent = new Intent(context, RedPacketPersonalSendAct.class);
        intent.putExtra(KeyValue.Transfer.UID, uid);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 群红包
     * @param context
     * @param uid
     */
    public static void gotoRedPacketGroupSendAct(Context context, String uid, Friend friend) {
        Intent intent = new Intent(context, RedPacketGroupSendAct.class);
        intent.putExtra(KeyValue.Transfer.UID, uid);
        intent.putExtra(KeyValue.Transfer.FRIEND, friend);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 选择
     * @param activity
     * @param selected_userids
     */
    public static void gotoSelectFriendsActivity(FragmentActivity activity, String selected_userids) {
        Intent intent = new Intent(activity, SelectFriendsActivity.class);
        intent.putExtra(KeyValue.SelectFriend.KEY_SELECTED_USERIDS, selected_userids);
        activity.startActivityForResult(intent, KeyValue.IntentCode.RESPONSE_CODE_SELECT_FRIEND);
        AnimUtils.enterAnimForActivity(activity);
    }


    /**
     * 下载app
     */
    public static void gotoDownAppActivity(Context context, String url, String updateType, String version) {
        Intent intent = new Intent(context, DownAppActivity.class);
        intent.putExtra(IConstant.Update.URL, url);
        intent.putExtra(IConstant.Update.UPDATETYPE, updateType);
        intent.putExtra(IConstant.Update.VERSION, version);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    public static void gotoGroupChatDetailsAct(Activity activity, String groupID) {
        gotoGroupChatDetailsAct(activity, groupID, null);
    }

    /**
     * 群聊设置页
     */
    public static void gotoGroupChatDetailsAct(Activity activity, String groupID, String groupName) {
        Intent intent = new Intent(activity, GroupDetailsActivity.class);
        intent.putExtra(KeyValue.GROUPID, groupID);
        if (!TextUtils.isEmpty(groupName)) {
            intent.putExtra(KeyValue.GROUPNAME, groupName);
        }
        activity.startActivityForResult(intent, KeyValue.IntentCode.REQUEST_CODE_EXIT);
        AnimUtils.enterAnimForActivity(activity);
    }

    /**
     *  P2P设置页
     */

    public static void gotoSingleChatDetailsAct(Activity activity, String uid) {
        Intent intent = new Intent(activity, SingleDetailsActivity.class);
        intent.putExtra("uid", uid);
        activity.startActivity(intent);
    }

    /**
     * 选择好友
     */
    public static void gotoSelectFriendActivity(Activity context, String targetId, String userIds, boolean withoutSelf) {
        Intent intent = new Intent(context, SelectGroupMemberActivity.class);
        intent.putExtra(KeyValue.KEY_ACCOUNT, targetId);
        intent.putExtra(KeyValue.SelectFriend.KEY_WITHOUT_SELF, withoutSelf);
        if (!TextUtils.isEmpty(userIds)) {
            intent.putExtra(KeyValue.SelectFriend.KEY_SELECTED_USERIDS, userIds);
        }

        context.startActivityForResult(intent, 101);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 选择人员，直接单聊／群聊，创建群聊
     *
     * @param context
     */
    public static void gotoSelectedActivity(Activity context) {
        Intent intent = new Intent(context, SelectedActivity.class);
        context.startActivityForResult(intent, 101);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 选择成员，点击会话
     *
     * @param context
     */
    public static void gotoTeamListActivity(Activity context) {
        Intent intent = new Intent(context, TeamListActivity.class);
        context.startActivityForResult(intent, 1234);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 二维码
     *
     * @param context
     */
    public static void gotoQRCodeActivity(Context context) {
        gotoQRCodeActivity(context, null);
    }

    /**
     * @param context
     * @param qrEntity 群二维码
     */
    public static void gotoQRCodeActivity(Context context, QrEntity qrEntity) {
        Intent intent = new Intent(context, QRCodeActivity.class);
        if (qrEntity != null) {
            intent.putExtra(KeyValue.QR_ENTITY, qrEntity);
        }
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 跳转网页
     *
     * @param context
     * @param url
     */
    public static void gotobrowser(Context context, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            if (context instanceof Application) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            AnimUtils.enterAnimForActivity(context);
        } catch (Exception e) {
            DqToast.showShort(context.getString(R.string.browser));
        }
    }

    /**
     * 借助网页跳转到支付宝app
     */
    public static void gotoAliPayWebviewActivity(Context context, String url) {
        Intent intent = new Intent(context, AliPayWebviewActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(KeyValue.APLAY_URL, url);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 二维码扫一扫
     */
    public static void gotoScanQRCodeActivity(Context context) {
        Intent intent = new Intent(context, ScanQRCodeActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    //    相册列表
    public static void gotoQCPhotoAlbumListActivity(Context context) {
        Intent intent = new Intent(context, QCPhotoAlbumListActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 已保存的群聊
     */
    public static void gotoSavedTeamsActivity(Context context) {
        Intent intent = new Intent(context, SavedTeamsActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 分享/转发
     *
     * @param shareBean //需要分享的消息数据
     */
    public static void gotoShareActivity(Context context, ShareBean shareBean) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(IConstant.Share.SHARE_BAEN, shareBean);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }


    /**
     * 修改群昵称
     *
     */
    public static void gotoGroupChatNameActivity(Activity activity, String groupid, String groupName) {
        Intent intent = new Intent(activity, GroupChatNameActivity.class);
        intent.putExtra(KeyValue.GROUPID, groupid);
        intent.putExtra(KeyValue.GROUPNAME, groupName);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    /**
     * 群通知
     */
    public static void gotoGroupNoticeActivity(Activity activity, boolean isCreated,
                                               String groupId, String time, String content) {
        Intent intent = new Intent(activity, GroupNoticeActivity.class);
        intent.putExtra(KeyValue.GROUPID, groupId);
        intent.putExtra(KeyValue.GROUP_MODIFY_ANNOUNCEMENT, isCreated);
        intent.putExtra(KeyValue.GROUP_NOTICE_MODIFY_TIME, time);
        intent.putExtra(KeyValue.GROUP_ANNOUNCEMENT, content);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    /**
     * 分享到个人
     */
    public static void gotoShareP2pOrNewTeamActivity(Context context, ShareBean shareBean) {
        Intent intent = new Intent(context, ShareP2pOrNewTeamActivity.class);
        intent.putExtra(IConstant.Share.SHARE_BAEN, shareBean);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 分享到群组
     */
    public static void gotoShareTeamActivity(Context context, ShareBean shareBean) {
        Intent intent = new Intent(context, ShareTeamActivity.class);
        intent.putExtra(IConstant.Share.SHARE_BAEN, shareBean);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 群个人信息
     *
     */
    public static void gotoGroupPersonalInfosActivity(Activity activity, String groupId) {
        Intent intent = new Intent(activity, GroupPersonalInfosActivity.class);
        intent.putExtra(KeyValue.GROUPID, groupId);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    /**
     * 群组内添加人
     *
     */
    public static void gotoSelectFriendsActivity(Activity activity, String groupId) {
        Intent intent = new Intent(activity, SelectFriendsActivity.class);
        intent.putExtra(KeyValue.IS_ADD_GROUP_MEMBER, true);
        intent.putExtra(KeyValue.GROUPID, groupId);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    /**
     * 群组内删人
     */
    public static void gotoSelectFriendsActivity(Activity activity, String groupId,
                                                 boolean isMaster) {
        Intent intent = new Intent(activity, SelectFriendsActivity.class);
        intent.putExtra(KeyValue.IS_DELETE_GROUP_MEMBER, true);
        intent.putExtra(KeyValue.GROUPID, groupId);
        intent.putExtra(KeyValue.IS_MASTERS, isMaster);
        activity.startActivity(intent);
        AnimUtils.enterAnimForActivity(activity);
    }

    /**
     * p2p创建
     *
     * @param activity
     */
    public static void gotoCreateFriendsActivity(FragmentActivity activity, Friend friend) {
        Intent intent = new Intent(activity, SelectFriendsActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("singleFriend", friend);
        intent.putExtras(mBundle);
        intent.putExtra("isFromSingle", true);
        activity.startActivityForResult(intent, KeyValue.REQUESTCODE_CREATE_GROUP);
        AnimUtils.enterAnimForActivity(activity);
    }

    /**
     * 跳转到@页面
     *
     * @param context
     * @param groupId
     */
    public static void gotoAitActivity(Context context, String groupId) {
        Intent intent = new Intent(context, AitActivity.class);
        intent.putExtra(KeyValue.GROUP_ID, groupId);
        ((Activity) context).startActivityForResult(intent, RequestCode.AIT_TEAM);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 群个人资料
     */
    public static void gotoPersonalInfoActivity(Context context, String uid, String groupId) {
        Intent intent = new Intent(context, GroupPersonalInfoActivity.class);
        intent.putExtra(IConstant.UserInfo.UID, uid);
        intent.putExtra(IConstant.UserInfo.GROUP_ID, groupId);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 新朋友列表详情
     */
    public static void gotoNewFriendDetailActivity(Context context, NewFriendBean newFriendBean) {
        Intent intent = new Intent(context, NewFriendDetailActivity.class);
        intent.putExtra(IConstant.UserInfo.NEW_FRIEND_BEAN, newFriendBean);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 搜索联系人页面
     */
    public static void gotoSearchFriendActivity(Context context) {
        Intent intent = new Intent(context, SearchFriendActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }


    /**
     * 名片选择界面
     */
    public static void gotoCardActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, CardActivity.class);
        activity.startActivityForResult(intent, requestCode);
        AnimUtils.enterAnimForActivity(activity);
    }

    public static void gotoMyEmotionActivity(Context context) {
        Intent intent = new Intent(context, MyEmotionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }


    /**
     * 会话搜索页
     *
     * @param context
     */
    public static void gotoSearchConversationActivity(Context context) {
        Intent intent = new Intent(context, SearchConversationActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 群管理
     *
     * @param context
     * @param group_id
     */
    public static void gotoGroupManager(Context context, String group_id) {
        Intent intent = new Intent(context, GroupManagerActivity.class);
        intent.putExtra(KeyValue.GROUPID, group_id);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 设置群聊号
     *
     * @param context
     * @param groupId
     */
    public static void gotoGroupSetMarkActivity(Activity context, String groupId) {
        Intent intent = new Intent(context, GroupNumberActivity.class);
        intent.putExtra(KeyValue.GROUPID, groupId);
        context.startActivityForResult(intent, 102);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 设置管理员
     *
     * @param context
     * @param group_id
     */
    public static void gotoGroupSetMangerActivity(Context context, String group_id) {
        Intent intent = new Intent(context, GroupManagerSettingActivity.class);
        intent.putExtra(KeyValue.GROUPID, group_id);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 设置管理员，选择页面
     *
     * @param context
     * @param groupId
     * @param fromType
     * @param requestCode
     * @param type        type=1 添加管理员；type=2 选择新群主
     */
    public static void gotoGroupMembersActivity(Activity context, String groupId, boolean fromType, int requestCode, int type) {
        Intent intent = new Intent(context, GroupMembersActivity.class);
        intent.putExtra(KeyValue.GROUPID, groupId);
        intent.putExtra(KeyValue.FROM_TYPE_FRIENDS, fromType);
        intent.putExtra("type", type);
        context.startActivityForResult(intent, requestCode);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 跳转退群成员列表页面
     *
     * @param context  引用对象
     * @param group_id 群id
     */
    public static void gotoExitGroupMembersActivity(Context context, String group_id) {
        context.startActivity(new Intent(context, ExitGroupMembersActivity.class)
                .putExtra(KeyValue.GROUP_ID, group_id)
        );
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 群助手
     * @param pluginBean 群助手数据
     */
    public static void gotoGroupAidesActivity(Context context, PluginBean pluginBean, String mGroupId) {
        Intent intent = new Intent(context, GroupAidesActivity.class);
        intent.putExtra(KeyValue.aides.PLUGIN_BEAN, pluginBean);
        intent.putExtra(KeyValue.GROUP_ID, mGroupId);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 群聊天记录
     *
     * @param context
     * @param groupId
     */
    public static void gotoGroupSearchChatActivity(Context context, String groupId) {
        Intent intent = new Intent(context, GroupSearchChatActivity.class);
        intent.putExtra(KeyValue.GROUP_ID, groupId);
//        intent.putExtra(KeyValue.GROUP_ID, groupId);

        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 跳转到更多群成员界面
     *
     * @param context
     * @param groupId
     * @param groupResp 群组返回数据对象
     * @param remark    昵称
     * @param isMaster  是否是群主
     * @param isAdmin   是否是管理员
     */
    public static void gotoTotalGroup(Context context, String groupId, GroupInfoBean groupResp,
                                      String remark, boolean isMaster, boolean isAdmin) {
        Intent intent = new Intent(context, TotalGroupMemberActivity.class);
        intent.putExtra(KeyValue.GROUP_ID, groupId);
        intent.putExtra(KeyValue.GROUP_RESPONSE, (Parcelable) groupResp);
        intent.putExtra(KeyValue.GROUP_REMARK, remark);
        intent.putExtra(KeyValue.AddUserDetail.IS_MASTER, isMaster);
        intent.putExtra(KeyValue.AddUserDetail.IS_ADMIN, isAdmin);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 跳转到加入群聊
     *
     * @param bean 群组信息
     * @param sourceUid 邀请人id
     */
    public static void gotoAddGroupActivity(Context context, GroupInfoBean bean, String sourceUid) {
        Intent intent = new Intent(context, AddGroupActivity.class);
        intent.putExtra(KeyValue.QR.Add_GROUP_BEAN, bean);
        intent.putExtra(KeyValue.QR.SOURCE_UID, sourceUid);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 跳转确认登录页面页面
     * @param context 引用对象
     * @param qruuid 登陆人id
     */
    public static void gotoMultiportActivity(Context context, String qruuid, String type) {
        Intent intent = new Intent(context, MultiportActivity.class);
        intent.putExtra(KeyValue.QR.KEY_WEB_QRUUID, qruuid);
        intent.putExtra(KeyValue.QR.KEY_WEB_TYPE, type);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * sdk分享------------------------------------------------------------------------------------------
     */

    public static void gotoRecentlyContactsListActivity(Context context, SdkShareBean mShareBean) {
        Intent intent = new Intent(context, DqSdkShareActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(SdkShareBean.SDK_SHARE_BEAN, mShareBean);
        intent.putExtras(mBundle);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    public static void gotoQCOpenLoginSdkActivity(Context context, SdkShareBean mShareBean) {
        Intent intent = new Intent(context, DqSdkLoginActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(SdkShareBean.SDK_SHARE_BEAN, mShareBean);
        intent.putExtras(mBundle);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    public static void gotoCreateNewChatActivity(Context context, SdkShareBean mShareBean) {
        Intent intent = new Intent(context, DqSdkP2pOrNewTeamShareActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(SdkShareBean.SDK_SHARE_BEAN, mShareBean);
        intent.putExtras(mBundle);
        ((Activity)context).startActivityForResult(intent, 1);
        AnimUtils.enterAnimForActivity(context);
    }

    public static void gotoQCOpenShareTeamActivity(Context context, SdkShareBean mShareBean) {
        Intent intent = new Intent(context, DqSdkTeamActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(SdkShareBean.SDK_SHARE_BEAN, mShareBean);
        intent.putExtras(mBundle);
        ((Activity)context).startActivityForResult(intent, 1);
        AnimUtils.enterAnimForActivity(context);
    }


    /**
     * sdk分享------------------------------------------------------------------------------------------
     */

    /**
     * 跳转到长时间未领红包界面
     * @param context
     * @param groupId
     */
    public static void gotoGroupLongTimeRpActivity(Context context, String groupId) {
        Intent intent = new Intent(context, GroupLongTimeRpActivity.class);
        intent.putExtra(KeyValue.GroupManager.GROUP_ID, groupId);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 搜索群助手
     */
    public static void gotoSearchGroupAidesActivity(Context context, String groupId, String pluginId) {
        Intent intent = new Intent(context, SearchGroupAidesActivity.class);
        intent.putExtra(KeyValue.GROUP_ID, groupId);
        intent.putExtra(KeyValue.aides.PLUGIN_ID, pluginId);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 群助手
     * @param pluginBean 群助手数据
     * @param isExit 已经存在
     */
    public static void gotoGroupAidesDetailsActivity(Context context, PluginBean pluginBean,
                                                     String mGroupId, boolean isExit) {
        Intent intent = new Intent(context, GroupAidesDetailsActivity.class);
        intent.putExtra(KeyValue.aides.PLUGIN_BEAN, pluginBean);
        intent.putExtra(KeyValue.GROUP_ID, mGroupId);
        intent.putExtra(KeyValue.aides.PLUGIN_IS_EXIT, isExit);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    //收藏
    public static void gotoCollectionActivity(Context context) {
        Intent intent = new Intent(context, CollectionActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    /**
     * 跳转到禁止收发红包界面
     */
    public static void gotoGroupMemberForbidDoRPActivity(Context context, String groupId) {
        Intent intent = new Intent(context, GroupMemberForbidDoRPActivity.class);
        intent.putExtra(KeyValue.GROUP_ID, groupId);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    /**
     * 跳转到禁止收发红包界面 选人
     */
    public static void gotoGroupMemberForbidDoRPMemberActivity(Context context, String groupId) {
        Intent intent = new Intent(context, GroupMemberForbidDoRPMemberActivity.class);
        intent.putExtra(KeyValue.GROUP_ID, groupId);
        ((Activity)context).startActivityForResult(intent, GroupMemberForbidDoRPActivity.DISPATCHER);
        AnimUtils.enterAnimForActivity(context);
    }
    /**
     * 复制群
     */
    public static void gotoGroupCopyNewActivity(Context context, String group_id) {
        Intent intent = new Intent(context, GroupCopyNewActivity.class);
        intent.putExtra(KeyValue.GROUPID, group_id);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 我的零钱
     */
    public static void gotoWalletActivity(Context context) {
        Intent intent = new Intent(context, WalletActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }


    /**
     * 搜索群聊天记录列表
     */
    public static void gotoGroupSearchTradeActivity(Context context, GroupSearchChatType chatType) {
        Intent intent = new Intent(context, GroupSearchChatTypeActivity.class);
        intent.putExtra(KeyValue.ENTER_TYPE, chatType);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 检测原密码，或短信验证
     */
    public static void gotoCheckPayPwdActivity(Context context, String type) {
        Intent intent = new Intent(context, CheckPayPwdActivity.class);
        intent.putExtra(KeyValue.ENTER_TYPE, type);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 设置支付密码
     */
    public static void gotoSetPayPwdActivity(Context context, String pwd, String type) {
        Intent intent = new Intent(context, SetPayPwdActivity.class);
        intent.putExtra(KeyValue.PASSWORD, pwd);
        intent.putExtra(KeyValue.ENTER_TYPE, type);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 我的银行卡
     */
    public static void gotoMyBankCardActivity(Context context, String type) {
        Intent intent = new Intent(context, MyBankCardActivity.class);
//        intent.putExtra(KeyValue.PASSWORD, pwd);
        intent.putExtra(KeyValue.ENTER_TYPE, type);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 我的银行卡
     */
    public static void gotoMyBankCardActivityForResult(Activity activity, String type, int requestCode) {
        Intent intent = new Intent(activity, MyBankCardActivity.class);
        intent.putExtra(KeyValue.ENTER_TYPE, type);
        activity.startActivityForResult(intent, requestCode);
        AnimUtils.enterAnimForActivity(activity);
    }

    /**
     * 添加银行卡
     */
    public static void gotoAddBankCardActivity(Context context, String type) {
        Intent intent = new Intent(context, AddBankCardActivity.class);
        intent.putExtra(KeyValue.ENTER_TYPE, type);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 银行卡详情
     */
    public static void gotoBankCardDetailsActivity(Context context, BankCardBean bankCardBean) {
        Intent intent = new Intent(context, BankCardDetailsActivity.class);
        intent.putExtra(KeyValue.Wallet.BANK_CARD_BEAN, bankCardBean);
//        intent.putExtra(KeyValue.ENTER_TYPE, type);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    /**
     * 填写银行卡信息
     */
    public static void gotoWriteBankCardInfoActivity(Context context, BankCardBean bankCardBean, String cardNo) {
        Intent intent = new Intent(context, WriteBankCardInfoActivity.class);
        intent.putExtra(KeyValue.Wallet.BANK_CARD_BEAN, bankCardBean);
        intent.putExtra(KeyValue.Wallet.CARD_NO, cardNo);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    /**
     * 忘记密码设置密码，连连支付单独样式
     */
    public static void gotoForgotSetPayPwdActivity(Context context, BankCardBean bankCardBean, String cardNumber) {
        Intent intent = new Intent(context, ForgotSetPayPwdActivity.class);
        intent.putExtra(KeyValue.Wallet.BANK_CARD_BEAN, bankCardBean);
        intent.putExtra(KeyValue.Wallet.CARD_NO, cardNumber);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 选择职业
     */
    public static void gotoChoiceProfessionalActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ChoiceProfessionalActivity.class);
//        intent.putExtra(KeyValue.ENTER_TYPE, type);
        activity.startActivityForResult(intent, requestCode);
        AnimUtils.enterAnimForActivity(activity);
    }
    /**
     * 提现
     */
    public static void gotoCashOutActivity(Context context) {
        Intent intent = new Intent(context, CashOutActivity.class);
//        intent.putExtra(KeyValue.ENTER_TYPE, type);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 充值
     */
    public static void gotoRechargeActivity(Context context) {
        Intent intent = new Intent(context, RechargeActivity.class);
//        intent.putExtra(KeyValue.ENTER_TYPE, type);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    //钱包认证 短信
    public static void gotoVerificationCodeActivity(Context context) {
        Intent intent = new Intent(context, VerificationCodeActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    //钱包认证
    public static void gotoWalletVerifyActivity(Activity context, String params) {
        Intent intent = new Intent(context, WalletVerifyActivity.class);
        intent.putExtra("params", params);
        context.startActivityForResult(intent, KeyValue.ONE);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 支付短信验证码  忘记密码短信验证，绑定银行卡短信验证
     */
    public static void gotoPaySmsCodeActivity(Context context, String verifyToken, String phone, String type) {
        gotoPaySmsCodeActivity(context, verifyToken, phone,  "", type);
    }

    /**
     * 支付短信验证码  充值订单入库
     * @param verifyToken 验证码token
     * @param orderId 订单
     * @param type 入口
     */
    public static void gotoPaySmsCodeActivity(Context context, String verifyToken, String phone, String orderId, String type) {
        Intent intent = new Intent(context, PaySmsCodeActivity.class);
        intent.putExtra(KeyValue.ENTER_TYPE, type);
        intent.putExtra(KeyValue.Wallet.VERIFY_TOKEN, verifyToken);
        intent.putExtra(KeyValue.Wallet.OWN_ORDER_ID, orderId);
        intent.putExtra(KeyValue.Wallet.BIND_MOBILE, phone);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    //钱包 零钱明细
    public static void gotoLooseChangeTransationListActivity(Activity context) {
        Intent intent = new Intent(context, LooseChangeTransationListActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    //钱包 红包明细
    public static void gotoRedTransationListActivity(Activity context) {
        Intent intent = new Intent(context, RedTransationListActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    //发送朋友圈动态
    public static void gotoDynamicSendActivity(Activity context, ArrayList<Uri> photos) {
        Intent intent = new Intent(context, DynamicSendActivity.class);
        intent.putExtra(DynamicSendActivity.ACTION_PICS,photos);
        context.startActivityForResult(intent, IntentCode.REQUEST_DYNAMIC_SEND);
        AnimUtils.enterAnimForActivity(context);
    }


    //查看多媒体详情
    public static void gotoDynamicMediaDetailsActivity(Activity context, List<String> mediaData, int currentIndex) {
        Intent intent = new Intent(context, DynamicMediaDetailsActivity.class);
        intent.putExtra(DynamicMediaDetailsActivity.MEDIA_DATA,(Serializable) mediaData);
        intent.putExtra(DynamicMediaDetailsActivity.MEDIA_DATA_CURRENT,currentIndex);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    //赚钱页面
    public static void gotoMakeMoneyActivity(Activity context) {
        Intent intent = new Intent(context, MakeMoneyActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    //任务详情页面
    public static void gotoTaskDetailsActivity(Context context,String taskId) {
        Intent intent = new Intent(context, TaskDetailsActivity.class);
        intent.putExtra(TaskDetailsActivity.KEY_TASK_ID,taskId);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    public static void playVideo(Context context,String videoUrl){
        Intent intent = new Intent(context, VideoDetailsActivity.class);
        intent.putExtra(VideoDetailsActivity.VIDEO_PATH_ACTION,videoUrl);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 跳转到任务管理页面
     * @param context
     */
    public static void gotoTaskMineActivity(Context context){
        Intent intent = new Intent(context, TaskMineActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    /**
     * 跳转到未读消息页面
     * @param context
     */
    public static void gotoUnReadMsgActivity(Context context){
        Intent intent = new Intent(context, UnReadMsgActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 跳转到发布管理页面
     * @param context
     */
    public static void gotoSendTaskManagerActivity(Context context){
        Intent intent = new Intent(context, SendTaskManagerActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 跳转到发布广告页面
     * @param context
     */
    public static void gotoSendTaskActivity(Context context){
        gotoSendTaskActivity(context,"");
    }
    /**
     * 跳转到发布广告页面
     * @param context
     */
    public static void gotoSendTaskActivity(Context context,String taskId){
        Intent intent = new Intent(context, SendTaskActivity.class);
        intent.putExtra(SendTaskActivity.KEY_TASK_ID,taskId);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 跳转到完成任务的详情
     * @param context
     */
    public static void gotoReleaseTaskCompleteActivity(Context context,String taskId){
        Intent intent = new Intent(context, ReleaseTaskCompleteActivity.class);
        intent.putExtra(ReleaseTaskCompleteActivity.KEY_TASK_ID,taskId);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    /**
     * 跳转到退款详情
     * @param context
     */
    public static void gotoRefundDetailsActivity(Context context,String taskId){
        Intent intent = new Intent(context, RefundDetailsActivity.class);
        intent.putExtra(RefundDetailsActivity.KEY_TASK_ID,taskId);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    /**
     * 跳转到退款页面
     * @param context
     */
    public static void gotoApplyRefundActivity(Context context,String taskId){
        Intent intent = new Intent(context, ApplyRefundActivity.class);
        intent.putExtra(ApplyRefundActivity.KEY_TASK_ID,taskId);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    /**
     * 跳转到退款成功页面
     * @param context
     */
    public static void gotoRefundActivity(Context context){
        Intent intent = new Intent(context, RefundResultActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 跳转到积分商城页面
     * @param context
     */
    public static void gotoIntegralMallActivity(Context context){
        Intent intent = new Intent(context, IntegralMallActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    /**
     * 跳转到积分兑换记录页面
     * @param context
     */
    public static void gotoIntegralExchangeRecordActivity(Context context){
        Intent intent = new Intent(context, IntegralExchangeRecordActivity.class);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
    /**
     * 跳转到积分明细记录页面
     * @param context
     */
    public static void gotoIntegralExchangeDetailActivity(Context context,int money){
        Intent intent = new Intent(context, IntegralExchangeDetailActivity.class);
        intent.putExtra(IntegralExchangeDetailActivity.KEY_MONEY,money);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }

    /**
     * 积分详情
     * @param context
     */
    public static void gotoSignUpDetailActivity(Context context, SignUpEntity signUpEntity){
        Intent intent = new Intent(context, SignUpDetailActivity.class);
        intent.putExtra(SignUpDetailActivity.KEY_ACTION,signUpEntity);
        context.startActivity(intent);
        AnimUtils.enterAnimForActivity(context);
    }
}

