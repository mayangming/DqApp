package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dq.im.model.IMContentDataModel;
import com.netease.nim.uikit.business.sdkparser.IMContentDataModelParser;
import com.wd.daquan.redpacket.extension.RdpRefundAttachment;
import com.wd.daquan.redpacket.extension.RedPacketAttachment;
import com.wd.daquan.redpacket.extension.RedPacketOpenedAttachment;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public class CustomAttachParser implements IMContentDataModelParser {

    private static final String KEY_OBJECTNAME = "objectName";
    private static final String KEY_DATA = "intentUrl";

    @Override
    public IMContentDataModel parse(String json) {
        CustomAttachment attachment = null;
        try {
            JSONObject object = JSON.parseObject(json);
            String objectName = object.getString(KEY_OBJECTNAME);

            if (CustomAttachmentType.CARD_MSG.equals(objectName)) {
                attachment = new CardAttachment();
            } else if (CustomAttachmentType.QC_VIDEO.equals(objectName)) {
                attachment = new DqVideoAttachment();
            }
            else if (CustomAttachmentType.CONTACT_MSG.equals(objectName)) {
                attachment = new QcContactNoticeAttachment();
            } else if (CustomAttachmentType.TEAM_SYSTEM_MSG.equals(objectName)) {
                attachment = new TeamSystemAttachment();
            } else if (CustomAttachmentType.SDK_SHARE_MSG.equals(objectName)) {
                attachment = new SdkShareAttachment();
            }else if (CustomAttachmentType.USER_SYSTEM_MSG.equals(objectName)) {
                attachment = new UserSysTemAttachment();
            } else if (CustomAttachmentType.JRMF_RDP_MSG.equals(objectName)) {
                attachment = new RedPacketAttachment();
            } else if (CustomAttachmentType.JRMF_RDP_OPEN_MSG.equals(objectName)) {
                attachment = new RedPacketOpenedAttachment();
            } else if (CustomAttachmentType.RDP_REFUND_MSG.equals(objectName)) {
                attachment = new RdpRefundAttachment();
            } else {
                attachment = new QcUnknowAttachment();
            }

            attachment.fromJson(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return attachment;
    }

    public static String packData(String objectName, JSONObject data) {
        data.put(KEY_OBJECTNAME, objectName);
        return data.toJSONString();
    }
}
