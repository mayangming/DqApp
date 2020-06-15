package com.wd.daquan.contacts.holder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.da.library.widget.DragPointView;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.ShareUtil;
import com.wd.daquan.model.bean.UnreadNotifyEntity;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

/**
 * @Author: 方志
 * @Time: 2018/9/6 13:36
 * @Description: 联系人头部
 */
public class ContactsListHeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public DragPointView mNewFriendNotify;
    private RelativeLayout mNewFriend;
//    private RelativeLayout mAddFriend;
    public LinearLayout mInviteWeiXinContact;
    private RelativeLayout mMobileContact;
    private RelativeLayout mSaveGroup;

    // 手机权限
    private final String[] mPermission = new String[]{
            Manifest.permission.READ_CONTACTS
    };

    public ContactsListHeaderHolder(View itemView) {
        super(itemView);
        mNewFriend = itemView.findViewById(R.id.rl_new_friends);
        mNewFriendNotify = itemView.findViewById(R.id.new_friend_notify);
//        mAddFriend = itemView.findViewById(R.id.rl_add_friends);
        mInviteWeiXinContact = itemView.findViewById(R.id.ll_invite_wei_xin_contact);
        mMobileContact = itemView.findViewById(R.id.rl_mobile_contact);
        mSaveGroup = itemView.findViewById(R.id.rl_save_group);

        mNewFriend.setOnClickListener(this);
//        mAddFriend.setOnClickListener(this);
        mInviteWeiXinContact.setOnClickListener(this);
        mMobileContact.setOnClickListener(this);
        mSaveGroup.setOnClickListener(this);
        mNewFriendNotify.setDrag(false);
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        switch (v.getId()) {
            case R.id.rl_new_friends:
                //Toast.makeText(context, "新的朋友", Toast.LENGTH_SHORT).show();
                NavUtils.gotoNewFriendActivity(context);
                mNewFriendNotify.setText("");
                mNewFriendNotify.setVisibility(View.GONE);
//                MsgMgr.getInstance().sendMsg(MsgType.MT_CONTACT_UNREAD, "");
//                ModuleMgr.getAppManager().clearContactUnread();

                ModuleMgr.getAppManager().clearUnreadNotify(UnreadNotifyEntity.UNREAD_ADD_FRIEND);
                MsgMgr.getInstance().sendMsg(MsgType.MT_CONTACT_NOTIFY, "");
                break;
//            case R.id.rl_add_friends:
//                //Toast.makeText(context, "添加朋友", Toast.LENGTH_SHORT).show();
//                NavUtils.gotoAddFriendActivity(mNewFriend.getContext());
//                break;
            case R.id.ll_invite_wei_xin_contact:
                //Toast.makeText(context, "邀请微信联系人", Toast.LENGTH_SHORT).show();
                DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
                ShareUtil.openWEIXINShare((Activity) context, DqUrl.url_weChat_share, DqApp.getStringById(R.string.app_name),
                        context.getString(R.string.we_chat_share), DqUrl.url_CN_logo);
                break;
            case R.id.rl_mobile_contact:
                //Toast.makeText(context, "邀请手机联系人", Toast.LENGTH_SHORT).show();
                DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
//                if(DqUtils.checkPermissions((Activity) context, mPermission)) {
//                    NavUtils.gotoMobileContactsListActivity(context);
//                }
                break;
            case R.id.rl_save_group:
                //Toast.makeText(context, "已保存群聊", Toast.LENGTH_SHORT).show();
                NavUtils.gotoSavedTeamsActivity(context);
                break;
        }
    }

}
