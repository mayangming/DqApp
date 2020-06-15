package com.wd.daquan.imui.type.chat_layout;


import android.support.annotation.NonNull;

import java.util.List;

/**
 * 树的节点
 */
public interface TreeNodeIml {
    void setNodeId(int nodeId);//设置节点Id
    void setNodeType(int nodeType);//设置节点类型
    void setMessageType(String messageType);//消息类型
    void setNodeName(String nodeName);//设置节点名字
    void addNode(@NonNull TreeNodeIml treeNode);//添加节点
    void removeNode(int nodeId);//删除节点
    int nodeId();//节点Id
    int nodeType();//节点类型
    String messageType();//消息类型
    String nodeName();//节点名称
    int childNodeCount();//子节点数量
    @NonNull List<TreeNodeIml> getChildNode();//获取所有子节点
}