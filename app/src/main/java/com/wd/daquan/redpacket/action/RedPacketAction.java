package com.wd.daquan.redpacket.action;

import android.app.Activity;
import android.content.Intent;

import com.dq.im.type.ImType;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.wd.daquan.R;
import com.wd.daquan.imui.activity.RedPackageSendActivity;


public class RedPacketAction extends BaseAction {

    public RedPacketAction() {
        super(R.drawable.chat_expansion_rdp_selector, R.string.red_packet);
    }

    private static final int CREATE_GROUP_RED_PACKET = 51;//群组红包
    private static final int CREATE_SINGLE_RED_PACKET = 10;//个人红包

    @Override
    public void onClick() {
        int requestCode;
        String chatBottomType;
        if (getContainer().sessionType == ImType.Team) {
            requestCode = makeRequestCode(CREATE_GROUP_RED_PACKET);
            chatBottomType = "2";
        } else if (getContainer().sessionType == ImType.P2P) {
            requestCode = makeRequestCode(CREATE_SINGLE_RED_PACKET);
            chatBottomType = "1";
        } else {
            return;
        }
//        NIMRedPacketClient.startSendRpActivity(getActivity(), getContainer().sessionType, getAccount(), requestCode);

        if (null != getContainer().activity){
            Intent intent = new Intent(getContainer().activity, RedPackageSendActivity.class);
            intent.putExtra(RedPackageSendActivity.RED_PACKAGE_TYPE,chatBottomType);
            intent.putExtra(RedPackageSendActivity.RED_PACKAGE_DATA,getContainer().account);
//        getContainer().activity.startActivityForResult(intent, IntentCode.REQUEST_RED_PACKAGE_SEND);
            getContainer().activity.startActivity(intent);
        }

        if (null != getContainer().fragment){
            Intent intent = new Intent(getContainer().fragment.getContext(), RedPackageSendActivity.class);
            intent.putExtra(RedPackageSendActivity.RED_PACKAGE_TYPE,chatBottomType);
            intent.putExtra(RedPackageSendActivity.RED_PACKAGE_DATA,getContainer().account);
//        getContainer().activity.startActivityForResult(intent, IntentCode.REQUEST_RED_PACKAGE_SEND);
//            getContainer().fragment.startActivityForResult(intent, requestCode);
            getContainer().fragment.startActivity(intent);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

//        sendRpMessage(data);
    }
}
