package com.wd.daquan.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.dq.im.bean.im.MessageRedPackageBean;
import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.business.session.activity.P2PMessageActivity;
import com.netease.nim.uikit.business.session.activity.TeamMessageActivity;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.wd.daquan.imui.activity.RedPackageDetailsActivity;
import com.wd.daquan.third.session.SessionHelper;

/**
 * Intent跳转类
 */
public class IntentUtils{

    /**
     * 跳转到个人页面
     */
    public static Intent getP2PChat(Context context, String contactId){
        return getP2PChat(context,contactId, SessionHelper.getP2pCustomization(contactId),null,null);
    }
    public static Intent getP2PChat(Context context, String contactId, SessionCustomization customization, ImMessageBaseModel anchor, IMContentDataModel sendContent){
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        intent.putExtra(Extras.EXTRA_IM_CONTENT, sendContent);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, P2PMessageActivity.class);
        return intent;
    }

    public static Intent getTeamChat(Context context, String tid){
        return getTeamChat(context,tid,SessionHelper.getTeamCustomization(tid),null,null,null);
    }

    public static Intent getTeamChat(Context context, String tid, SessionCustomization customization,
                                     Class<? extends Activity> backToClass, ImMessageBaseModel anchor, IMContentDataModel sendContent){
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, tid);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        intent.putExtra(Extras.EXTRA_BACK_TO_CLASS, backToClass);
        intent.putExtra(Extras.EXTRA_IM_CONTENT, sendContent);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, TeamMessageActivity.class);
        return intent;
    }

    /**
     * 跳转到红包详情页面
     */
    public static void goRedPackageDetails(Context context, MessageRedPackageBean messageRedPackageBean, ImMessageBaseModel imMessageBaseModel){
        Intent intent = new Intent(context, RedPackageDetailsActivity.class);
        intent.putExtra(RedPackageDetailsActivity.RED_PACKAGE_ID,messageRedPackageBean.getCouponId());
        intent.putExtra(RedPackageDetailsActivity.MESSAGE_DATA,imMessageBaseModel);
        context.startActivity(intent);
    }
}