package com.wd.daquan.third.session.extension;

import com.alibaba.fastjson.JSONObject;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.TeamOptionType;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

/**
 */
public class TeamSystemAttachment extends CustomAttachment {
    /**
     * operation对应类型
     */

    public String operatorName;//操作人的昵称
//    public String receiverName;//被操作人的昵称
    public String type;//群组操作类型
    public String data;//json扩展数据，待定
    public String content;//消息描述
    public String extra;//扩展字段

    public TeamSystemAttachment() {
        super(CustomAttachmentType.TEAM_SYSTEM_MSG);
    }

    @Override
    protected void parseData(JSONObject jsonObject) {
        operatorName = jsonObject.getString(KeyValue.TeamMessage.OPERATORNAME);
//        receiverName = jsonObject.getString(KeyValue.TeamMessage.RECEIVERNAME);
        type = jsonObject.getString(KeyValue.TeamMessage.TYPE);
        data = jsonObject.getString(KeyValue.TeamMessage.DATA);
        content = jsonObject.getString(KeyValue.TeamMessage.CONTENT);
        extra = jsonObject.getString(KeyValue.TeamMessage.EXTRA);
    }


    @Override
    protected JSONObject packData() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KeyValue.TeamMessage.OPERATORNAME, operatorName);
//        jsonObj.put(KeyValue.TeamMessage.RECEIVERNAME, receiverName);
        jsonObj.put(KeyValue.TeamMessage.TYPE, type);
        jsonObj.put(KeyValue.TeamMessage.DATA, data);
        jsonObj.put(KeyValue.TeamMessage.CONTENT, content);
        jsonObj.put(KeyValue.TeamMessage.EXTRA, content);
        return jsonObj;
    }

    /**
     * 是否add
     */
    public boolean isAdd() {
        return TeamOptionType.ADD.equals(type);
    }

    /**
     * 是否需要处理
     * @return
     */
    public void isProcess() {
        MsgMgr.getInstance().sendMsg(MsgType.MT_GROUP_NOTICE, this);
        switch (type){
            case TeamOptionType.CREATE:


                break;
            case TeamOptionType.ADD:


                break;
            case TeamOptionType.DISMISS:


                break;
            case TeamOptionType.QUIT:


                break;
            case TeamOptionType.KICKED://踢出群组


                break;
            case TeamOptionType.RENAME:


                break;
            case TeamOptionType.BURN:


                break;
        }
    }

    @Override
    public String getType() {
        return type == null ? "" : type;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public String getOperatorName() {
        return operatorName == null ? "" : operatorName;
    }

//    public String getReceiverName() {
//        return receiverName == null ? "" : receiverName;
//    }

    public String getData() {
        return data == null ? "" : data;
    }

    public String getExtra() {
        return extra == null ? "" : extra;
    }
}
