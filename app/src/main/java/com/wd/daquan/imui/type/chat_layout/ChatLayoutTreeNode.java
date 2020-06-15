package com.wd.daquan.imui.type.chat_layout;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天布局类型树形结构
 */
public class ChatLayoutTreeNode implements TreeNodeIml{
    List<TreeNodeIml> treeChildNodes = new ArrayList<>();//树形子节点集合
    int nodeId = 0;
    int nodeType = 0;
    String nodeName = "";
    String messageType = "";
    @Override
    public void setNodeId(int nodeId) {
       this.nodeId = nodeId;
    }

    @Override
    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public void addNode(TreeNodeIml treeNode) {
        treeChildNodes.add(treeNode);
    }

    @Override
    public void removeNode(int nodeId) {

    }

    @Override
    public int nodeId() {
        return this.nodeId;
    }

    @Override
    public int nodeType() {
        return this.nodeType;
    }

    @Override
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String messageType() {
        return messageType;
    }

    @Override
    public String nodeName() {
        return this.nodeName;
    }

    @Override
    public int childNodeCount() {
        return this.treeChildNodes.size();
    }

    @Override
    public List<TreeNodeIml> getChildNode() {
        return treeChildNodes;
    }
}