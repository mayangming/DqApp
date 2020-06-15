package com.wd.daquan.third.session.extension;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public interface CustomAttachmentType {
    // 消息类型：默认使用文本／图片／音频
    String DEFAULT = "0";    // 默认

    String CARD_MSG = "CardMsg";      // 名片
    String USER_SYSTEM_MSG = "UserSystemMsg";// 个人用户系统通知
    String TEAM_SYSTEM_MSG = "TeamSystemMsg";// 群组相关操作，创建、删除、解散、退出、转让、设置管理员
    String JRMF_RDP_MSG = "JrmfRdpMsg";// 金融魔方红包
    String JRMF_RDP_OPEN_MSG = "JrmfRdpOpenMsg";// 红包领取消息
    String RDP_REFUND_MSG = "RdpRefundMsg";// 红包退款消息
    String SDK_SHARE_MSG = "SdkShareMsg";// SDK分享

    //通知提示
    String CONTACT_APPLY_MSG = "ContactApplyMsg";// 添加好友申请
    String GROUP_INVITE_MSG = "GroupInviteMsg";// 邀请进群消息类型
    String RED_RAIN_TIP = "cloudRedEnvelopeActiveNoticeMsg";//红包雨提醒
    String DELETE_FRIEND_TIP = "DeleteContactApplyMsg";//好友删除提醒


    String QC_IMAGE = "2";    // 默认
    String QC_VIDEO = "VideoNtf";      // 视频
    String QC_EXPRESSION = "ExpressionNtf";// 表情
    String QC_SYSTEM = "GeneralNtf";// 系统通知消息
    String QC_SYSTEM_RED_RAIN = "redRain";// 系统通知红包雨
    String CONTACT_MSG = "ContactMsg";// 添加好友成功
    String QC_TEAM_AUDIT = "GrpAuditNtf";// 入群审核
    String QC_REDPACKET_NOTICE = "RedPacketRecordNtf";// 红包领取通知
    String QC_ALIPAY_REDPACKET = "RedPacketNtf";// 支付宝红包
    String QC_ASSISTANT_SYSTEM = "AssistantNtf";// 全局小助手广播消息
    String QC_WEBLOGIN = "WebLoginNtf";  //WebLoginNtf

    String QC_REDPACKET_REFUND_NOTICE = "RedPacketRefundNtf";// 支付宝过期通知
    String QC_POKE = "PokeNtf";// 戳一下消息类型
    String QC_TRANSFER_ATTACHMENT = "TransferNtf";// 转账消息类型
    String QC_TRANSFER_A = "TransferNstf";// 零钱红包消息类型

    //Custom
//    case im_video = "video"  //VideoNtf
//            case webpageMessage = "webpageMessage" // WebpageShareNtf
//            case expressionMessage = "expressionMessage"//ExpressionNtf
//            case cardShareMessage = "businessCardShareMessage" //CardNtf
//            case contactMessage = "ContactAddedNtf"
//            case contactNotice = "ContactNtf"
//            case fileMsg = "fileMsg"  //FileNtf
//            case WebLoginNtf = "WebLoginNtf"  //WebLoginNtf
//    objectName=RedPacketRefundNtf

}
