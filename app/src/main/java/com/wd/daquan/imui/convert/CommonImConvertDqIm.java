package com.wd.daquan.imui.convert;

import android.util.Log;

import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.type.ImType;
import com.wd.daquan.imui.bean.im.DqImBaseBean;
import com.wd.daquan.imui.bean.im.FirstContentBean;

/**
 * 通用Im基础斗圈特有的IM数据结构相互转换的工具类
 */
public class CommonImConvertDqIm {
    private String userId = "";
    private String token = "";

    public CommonImConvertDqIm() {
    }

    public CommonImConvertDqIm(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public DqImBaseBean commonImConvertDqIm(ImMessageBaseModel imMessageBaseModel){
        String groupId = "";
        if (imMessageBaseModel.getType().equals(ImType.Team.getValue())){
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) imMessageBaseModel;
            groupId = teamMessageBaseModel.getGroupId();
        }
        DqImBaseBean dqImBaseBean = new DqImBaseBean();
        dqImBaseBean.setToken(token);
        dqImBaseBean.setUserId(userId);

        FirstContentBean firstContentBean = new FirstContentBean();
        firstContentBean.setTimestamp(imMessageBaseModel.getCreateTime());
        firstContentBean.setMsgIdServer(imMessageBaseModel.getMsgIdServer());
        String type = "0";//0 单聊 1群聊
        if ("1".equals(imMessageBaseModel.getType())){//单聊
            type = "0";
        }else if ("2".equals(imMessageBaseModel.getType())){//群聊
            type = "1";
        }
        firstContentBean.setConversationType(type);
        firstContentBean.setFrom(imMessageBaseModel.getFromUserId());
        firstContentBean.setTarget(imMessageBaseModel.getToUserId());
        firstContentBean.setMsgType(Integer.parseInt(imMessageBaseModel.getMsgType()));
        firstContentBean.setMsgSecondType(imMessageBaseModel.getMsgSecondType());
        firstContentBean.setGroupId(groupId);
        firstContentBean.setMsgIdClient(imMessageBaseModel.getMsgIdClient());
        IMContentDataModel imContentDataModel = IMContentDataModelConvertDqImUtils.convertDqCommonContent(firstContentBean.getMsgType()+"",imMessageBaseModel.getContentData());
        firstContentBean.setContent(imContentDataModel);
        String content = IMContentDataModelConvertDqImUtils.convertDqCommonContentStr(firstContentBean.getMsgType()+"",imMessageBaseModel.getSourceContent());
        firstContentBean.setSourceContent(content);
        dqImBaseBean.setContent(firstContentBean);
        return dqImBaseBean;
    }

    public static ImMessageBaseModel imConvertCommonImDq(DqImBaseBean dqImBaseBean){
        FirstContentBean firstContentBean = dqImBaseBean.getContent();
        String type = firstContentBean.getConversationType();//0 单聊 1群聊
        ImMessageBaseModel imMessageBaseModel;
        if ("0".equals(firstContentBean.getConversationType())){
            imMessageBaseModel = new P2PMessageBaseModel();
        }else if ("1".equals(firstContentBean.getConversationType())){
            TeamMessageBaseModel teamMessageBaseModel = new TeamMessageBaseModel();
            teamMessageBaseModel.setGroupId(firstContentBean.getGroupId());
            imMessageBaseModel = teamMessageBaseModel;
        }else {
            imMessageBaseModel = new ImMessageBaseModel();
        }

        imMessageBaseModel.setCreateTime(firstContentBean.getTimestamp());
        imMessageBaseModel.setFromUserId(firstContentBean.getFrom());
        imMessageBaseModel.setToUserId(firstContentBean.getTarget());
        imMessageBaseModel.setMsgIdClient(firstContentBean.getMsgIdClient());
        imMessageBaseModel.setMsgIdServer(firstContentBean.getMsgIdServer());
        imMessageBaseModel.setMsgSecondType(firstContentBean.getMsgSecondType());
        imMessageBaseModel.setMsgType(firstContentBean.getMsgType()+"");
        String secondType = firstContentBean.getMsgSecondType();//0 单聊 1群聊 4红包领取的消息
        if ("0".equals(firstContentBean.getMsgSecondType())){//单聊
            secondType = "1";
        }else if ("1".equals(firstContentBean.getMsgSecondType())){//群聊
            secondType = "2";
        }
        if ("0".equals(firstContentBean.getConversationType())){//单聊
            type = "1";
        }else if ("1".equals(firstContentBean.getConversationType())){//群聊
            type = "2";
        }


        imMessageBaseModel.setType(type);
        imMessageBaseModel.setMsgSecondType(secondType);
        imMessageBaseModel.setContentData(IMContentDataModelConvertDqImUtils.convertCommonContent(imMessageBaseModel.getMsgType(),firstContentBean.getContent()));
        String content = IMContentDataModelConvertDqImUtils.convertCommonContentStr(imMessageBaseModel.getMsgType(),firstContentBean.getSourceContent());
        imMessageBaseModel.setSourceContent(content);
        return imMessageBaseModel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}