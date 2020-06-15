package com.wd.daquan.common.constant;

/**
 * 群组系统消息类型
 */
public interface TeamOptionType {
    String CREATE = "Create"; // 创建群组
    String ADD = "Add";
    String QRCODE_ADD = "QrCode_Add";
    String DISMISS = "Dismiss"; // 解散群组
    String QUIT = "Quit"; // 退出群组
    String KICKED = "Kicked"; // 踢人
    String RENAME = "Rename";   // 修改群名称
    String BURN= "Burn"; //阅后即焚
    String TRANSFER_MASTER = "transferMaster";// 群主转让
    String ADD_ADMIN = "addAdmin"; // 设置管理员
}
