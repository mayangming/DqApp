package com.dq.im.parser;

import com.dq.im.model.HomeImBaseMode;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;

/**
 * 模型之间的相互转换。
 * 由于首页数据模型、单聊数据模型、群聊数据模型是三张表。
 * 所以在单聊消息和群聊消息产生时候需要同步更新首页数据模型的数据。
 * 所以当聊天消息产生的时候，需要将单聊消息和群聊消息转换为首页消息模型
 */
public class ImTransformUtils {

    /**
     * 个人消息模型
     * @return 首页聊天消息模型
     */
    public static HomeImBaseMode p2pImModelTransformHomeImModel(P2PMessageBaseModel p2PMessageBaseModel){
        HomeImBaseMode homeImBaseMode = new HomeImBaseMode();
        homeImBaseMode.setType(p2PMessageBaseModel.getType());
        homeImBaseMode.setMsgType(p2PMessageBaseModel.getMsgType());
        homeImBaseMode.setSourceContent(p2PMessageBaseModel.getSourceContent());
        homeImBaseMode.setCreateTime(p2PMessageBaseModel.getCreateTime());
        homeImBaseMode.setFromUserId(p2PMessageBaseModel.getFromUserId());
        homeImBaseMode.setToUserId(p2PMessageBaseModel.getToUserId());
        homeImBaseMode.setMsgIdClient(p2PMessageBaseModel.getMsgIdClient());
        homeImBaseMode.setMsgIdServer(p2PMessageBaseModel.getMsgIdServer());
        homeImBaseMode.setUnReadNumber(0);
        return homeImBaseMode;
    }
    /**
     * 群组消息模型
     * @return 首页聊天消息模型
     */
    public static HomeImBaseMode teamImModelTransformHomeImModel(TeamMessageBaseModel teamMessageBaseModel){
        HomeImBaseMode homeImBaseMode = new HomeImBaseMode();
        homeImBaseMode.setType(teamMessageBaseModel.getType());
        homeImBaseMode.setMsgType(teamMessageBaseModel.getMsgType());
        homeImBaseMode.setSourceContent(teamMessageBaseModel.getSourceContent());
        homeImBaseMode.setCreateTime(teamMessageBaseModel.getCreateTime());
        homeImBaseMode.setFromUserId(teamMessageBaseModel.getFromUserId());
        homeImBaseMode.setToUserId(teamMessageBaseModel.getToUserId());
        homeImBaseMode.setMsgIdClient(teamMessageBaseModel.getMsgIdClient());
        homeImBaseMode.setMsgIdServer(teamMessageBaseModel.getMsgIdServer());
        homeImBaseMode.setGroupId(teamMessageBaseModel.getGroupId());
        homeImBaseMode.setUnReadNumber(0);
        return homeImBaseMode;
    }
}