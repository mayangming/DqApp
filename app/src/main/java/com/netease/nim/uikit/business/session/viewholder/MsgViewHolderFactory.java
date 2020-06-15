package com.netease.nim.uikit.business.session.viewholder;

import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.MessageType;
import com.wd.daquan.third.session.viewholder.QcTextViewHolder;
import com.wd.daquan.third.session.viewholder.QcUnknownViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 消息项展示ViewHolder工厂类。
 */
public class MsgViewHolderFactory {

    private static HashMap<Class<? extends IMContentDataModel>, Class<? extends MsgViewHolderBase>> viewHolders = new HashMap<>();

    private static Class<? extends MsgViewHolderBase> tipMsgViewHolder;

    static {
        // built in
//        register(ImageAttachment.class, MsgViewHolderPicture.class);
//        register(VideoAttachment.class, MsgViewHolderVideo.class);
//        register(LocationAttachment.class, MsgViewHolderLocation.class);
//        register(NotificationAttachment.class, MsgViewHolderNotification.class);
//        register(RobotAttachment.class, MsgViewHolderRobot.class);
    }

    public static void register(Class<? extends IMContentDataModel> attach, Class<? extends MsgViewHolderBase> viewHolder) {
        viewHolders.put(attach, viewHolder);
    }

    public static void registerTipMsgViewHolder(Class<? extends MsgViewHolderBase> viewHolder) {
        tipMsgViewHolder = viewHolder;
    }

    public static Class<? extends MsgViewHolderBase> getViewHolderByType(ImMessageBaseModel message) {
        if (MessageType.typeOfValue(message.getMsgType()) == MessageType.TEXT) {
            return QcTextViewHolder.class;
        } else {
            Class<? extends MsgViewHolderBase> viewHolder = null;
            if (message.getContentData() != null) {
                Class<? extends IMContentDataModel> clazz = message.getContentData().getClass();
                while (viewHolder == null && clazz != null) {
                    viewHolder = viewHolders.get(clazz);
                    if (viewHolder == null) {
                        clazz = getSuperClass(clazz);
                    }
                }
            }
            return viewHolder == null ? QcUnknownViewHolder.class : viewHolder;
        }
    }

    private static Class<? extends IMContentDataModel> getSuperClass(Class<? extends IMContentDataModel> derived) {
        Class sup = derived.getSuperclass();
        if (sup != null && IMContentDataModel.class.isAssignableFrom(sup)) {
            return sup;
        } else {
            for (Class itf : derived.getInterfaces()) {
                if (IMContentDataModel.class.isAssignableFrom(itf)) {
                    return itf;
                }
            }
        }
        return null;
    }

    public static List<Class<? extends MsgViewHolderBase>> getAllViewHolders() {
        List<Class<? extends MsgViewHolderBase>> list = new ArrayList<>();
        list.addAll(viewHolders.values());
        if (tipMsgViewHolder != null) {
            list.add(tipMsgViewHolder);
        }
//        list.add(QcUnknownViewHolder.class);
        list.add(QcTextViewHolder.class);

        return list;
    }
}
