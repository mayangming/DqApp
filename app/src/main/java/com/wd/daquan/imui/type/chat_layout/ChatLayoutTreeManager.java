package com.wd.daquan.imui.type.chat_layout;

import com.dq.im.type.MessageType;
import com.wd.daquan.imui.type.MsgSecondType;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天布局类型树形结构管理器
 */
public class ChatLayoutTreeManager {
    private static List<TreeNodeIml> treeChildNodes = new ArrayList<>();//顶级树形子节点集合
    private static ChatLayoutTreeManager chatLayoutTreeManager;
    private int treeDeep = 0;//树的深度
    private int treeSpan = 0;//树的广度
    private ChatLayoutTreeManager(){
        TreeNodeIml chatLayoutTreeNodeLeft = new ChatLayoutTreeNode();
        chatLayoutTreeNodeLeft.setNodeType(ChatLayoutParentType.LEFT);
        chatLayoutTreeNodeLeft.setMessageType(String.valueOf(ChatLayoutParentType.LEFT));
        TreeNodeIml chatLayoutTreeNodeRight = new ChatLayoutTreeNode();
        chatLayoutTreeNodeRight.setNodeType(ChatLayoutParentType.RIGHT);
        chatLayoutTreeNodeRight.setMessageType(String.valueOf(ChatLayoutParentType.RIGHT));
        TreeNodeIml chatLayoutTreeNodeSystem = new ChatLayoutTreeNode();
        chatLayoutTreeNodeSystem.setNodeType(ChatLayoutParentType.SYSTEM);
        chatLayoutTreeNodeSystem.setMessageType(String.valueOf(ChatLayoutParentType.SYSTEM));
        TreeNodeIml chatLayoutTreeNodeUnKnown = new ChatLayoutTreeNode();
        chatLayoutTreeNodeUnKnown.setNodeType(ChatLayoutParentType.UN_KNOWN);
        chatLayoutTreeNodeUnKnown.setMessageType(String.valueOf(ChatLayoutParentType.UN_KNOWN));

        initChatLayoutLeft(chatLayoutTreeNodeLeft);
        initChatLayoutRight(chatLayoutTreeNodeRight);
        initChatLayoutSystem(chatLayoutTreeNodeSystem);
        initChatLayoutUnknown(chatLayoutTreeNodeLeft);

        treeChildNodes.add(chatLayoutTreeNodeLeft);
        treeChildNodes.add(chatLayoutTreeNodeRight);
        treeChildNodes.add(chatLayoutTreeNodeSystem);
        treeChildNodes.add(chatLayoutTreeNodeUnKnown);
    }

    public static ChatLayoutTreeManager getInstance(){
        if (null == chatLayoutTreeManager){
            chatLayoutTreeManager = new ChatLayoutTreeManager();
        }
        return chatLayoutTreeManager;
    }

    public List<TreeNodeIml> getTreeChildNodes(){
        return treeChildNodes;
    }

    /**
     * 获取指定位置的节点
     * @param values 使用可变参数获取指定位置节点，可变参数的长度表示树的深度,参数为消息类型
     *              假如参数长度超过了树的深度，则返回所能查到的最后一个节点
     */
    public TreeNodeIml findChildNode(String... values){
        TreeNodeIml treeNodeIml = null;
        List<TreeNodeIml> tempTree = treeChildNodes;//临时树
        for (String msgType : values){
            for (TreeNodeIml treeNode : tempTree){
                if (treeNode.messageType().equals(msgType)){
                    treeNodeIml = treeNode;
                    tempTree = treeNode.getChildNode();
                    break;
                }
            }
        }

        return treeNodeIml;
    }

    /**
     * 添加左侧布局
     */
    private void initChatLayoutLeft(TreeNodeIml treeNodeIml){
        TreeNodeIml leftText = new ChatLayoutTreeNode();
        leftText.setNodeType(ChatLayoutChildType.LEFT_TEXT);
        leftText.setMessageType(MessageType.TEXT.getValue());
        TreeNodeIml leftImg = new ChatLayoutTreeNode();
        leftImg.setNodeType(ChatLayoutChildType.LEFT_IMG);
        leftImg.setMessageType(MessageType.PICTURE.getValue());
        TreeNodeIml leftVoice = new ChatLayoutTreeNode();
        leftVoice.setNodeType(ChatLayoutChildType.LEFT_VOICE);
        leftVoice.setMessageType(MessageType.VOICE.getValue());
        TreeNodeIml leftVideo = new ChatLayoutTreeNode();
        leftVideo.setNodeType(ChatLayoutChildType.LEFT_VIDEO);
        leftVideo.setMessageType(MessageType.VIDEO.getValue());
        TreeNodeIml leftRedPackage = new ChatLayoutTreeNode();
        leftRedPackage.setNodeType(ChatLayoutChildType.LEFT_RED_PACKAGE);
        leftRedPackage.setMessageType(MessageType.RED_PACKAGE.getValue());
//        TreeNodeIml leftLink = new ChatLayoutTreeNode();
//        leftLink.setNodeType(ChatLayoutChildType.LEFT_LINK);
//        leftLink.setMessageType(MessageType.TEXT_LINK.getValue());
        TreeNodeIml leftCard = new ChatLayoutTreeNode();
        leftCard.setNodeType(ChatLayoutChildType.LEFT_CARD);
        leftCard.setMessageType(MessageType.PERSON_CARD.getValue());

        treeNodeIml.addNode(leftText);
        treeNodeIml.addNode(leftImg);
        treeNodeIml.addNode(leftVoice);
        treeNodeIml.addNode(leftVideo);
        treeNodeIml.addNode(leftRedPackage);
//        treeNodeIml.addNode(leftLink);
        treeNodeIml.addNode(leftCard);
    }
    /**
     * 添加右侧布局
     */
    private void initChatLayoutRight(TreeNodeIml treeNodeIml){
        TreeNodeIml rightText = new ChatLayoutTreeNode();
        rightText.setNodeType(ChatLayoutChildType.RIGHT_TEXT);
        rightText.setMessageType(MessageType.TEXT.getValue());
        rightText.setNodeName("右侧文本节点");
        TreeNodeIml rightImg = new ChatLayoutTreeNode();
        rightImg.setNodeType(ChatLayoutChildType.RIGHT_IMG);
        rightImg.setMessageType(MessageType.PICTURE.getValue());
        TreeNodeIml rightVoice = new ChatLayoutTreeNode();
        rightVoice.setNodeType(ChatLayoutChildType.RIGHT_VOICE);
        rightVoice.setMessageType(MessageType.VOICE.getValue());
        TreeNodeIml rightVideo = new ChatLayoutTreeNode();
        rightVideo.setNodeType(ChatLayoutChildType.RIGHT_VIDEO);
        rightVideo.setMessageType(MessageType.VIDEO.getValue());
        TreeNodeIml rightRedPackage = new ChatLayoutTreeNode();
        rightRedPackage.setNodeType(ChatLayoutChildType.RIGHT_RED_PACKAGE);
        rightRedPackage.setMessageType(MessageType.RED_PACKAGE.getValue());
//        TreeNodeIml rightLink = new ChatLayoutTreeNode();
//        rightLink.setNodeType(ChatLayoutChildType.RIGHT_LINK);
//        rightLink.setMessageType(MessageType.TEXT_LINK.getValue());
        TreeNodeIml rightCard = new ChatLayoutTreeNode();
        rightCard.setNodeType(ChatLayoutChildType.RIGHT_CARD);
        rightCard.setMessageType(MessageType.PERSON_CARD.getValue());

        treeNodeIml.addNode(rightText);
        treeNodeIml.addNode(rightImg);
        treeNodeIml.addNode(rightVoice);
        treeNodeIml.addNode(rightVideo);
        treeNodeIml.addNode(rightRedPackage);
//        treeNodeIml.addNode(rightLink);
        treeNodeIml.addNode(rightCard);

    }
    /**
     * 添加系统布局
     */
    private void initChatLayoutSystem(TreeNodeIml treeNodeIml){
        TreeNodeIml normalText = new ChatLayoutTreeNode();
        normalText.setNodeType(ChatLayoutChildType.CENTER_TEXT);
        normalText.setMessageType(MsgSecondType.MSG_SECOND_TYPE_NORMAL.getType());
        TreeNodeIml transferText = new ChatLayoutTreeNode();
        transferText.setNodeType(ChatLayoutChildType.CENTER_TEXT);
        transferText.setMessageType(MsgSecondType.MSG_SECOND_TYPE_TRANSFER.getType());
        TreeNodeIml redPackageText = new ChatLayoutTreeNode();
        redPackageText.setNodeName("系统红包消息");
        redPackageText.setNodeType(ChatLayoutChildType.CENTER_TEXT);
        redPackageText.setMessageType(MsgSecondType.MSG_SECOND_TYPE_RED_PACKAGE.getType());
        TreeNodeIml unKnownText = new ChatLayoutTreeNode();
        unKnownText.setNodeType(ChatLayoutChildType.CENTER_TEXT);
        unKnownText.setMessageType(MsgSecondType.MSG_SECOND_TYPE_UN_KNOWN.getType());
        unKnownText.setNodeName("未识别的消息类型");

        treeNodeIml.addNode(normalText);
        treeNodeIml.addNode(transferText);
        treeNodeIml.addNode(redPackageText);
        treeNodeIml.addNode(unKnownText);

    }
    /**
     * 添加未知布局类型布局
     */
    private void initChatLayoutUnknown(TreeNodeIml treeNodeIml){
        TreeNodeIml unknownText = new ChatLayoutTreeNode();
        unknownText.setNodeType(ChatLayoutChildType.UNKNOWN);
        unknownText.setMessageType(MessageType.TEXT.getValue());
        treeNodeIml.addNode(unknownText);
    }

}