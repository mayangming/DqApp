//package com.netease.nim.uikit.business.session.actions;
//
//import com.netease.nim.uikit.R;
//import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
//import com.netease.nimlib.sdk.content.MessageBuilder;
//import com.netease.nimlib.sdk.content.constant.SessionTypeEnum;
//import com.netease.nimlib.sdk.content.model.IMMessage;
//
//import java.io.File;
//
///**
// * Created by hzxuwen on 2015/6/12.
// */
//public class ImageAction extends PickImageAction {
//
//    public ImageAction() {
//        super(R.drawable.nim_message_plus_photo_selector, R.string.input_panel_photo, true);
//    }
//
//    @Override
//    protected void onPicked(File file) {
//        IMMessage content;
//        if (getContainer() != null && getContainer().sessionType == SessionTypeEnum.ChatRoom) {
//            content = ChatRoomMessageBuilder.createChatRoomImageMessage(getAccount(), file, file.getName());
//        } else {
//            content = MessageBuilder.createImageMessage(getAccount(), getSessionType(), file, file.getName());
//        }
//        sendMessage(content);
//    }
//}
//
