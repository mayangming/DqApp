package com.wd.daquan.imui.adapter;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.MessageType;
import com.dq.im.viewmodel.UserViewModel;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder;
import com.wd.daquan.imui.type.MsgSecondType;
import com.wd.daquan.imui.type.chat_layout.ChatLayoutParentType;
import com.wd.daquan.imui.type.chat_layout.ChatLayoutTreeManager;
import com.wd.daquan.imui.type.chat_layout.TreeNodeIml;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.third.helper.UserInfoHelper;

/**
 * 聊天基础适配器
 * @param <VH>
 */
public abstract class ChatBaseAdapter<VH extends RecycleBaseViewHolder> extends RecycleBaseAdapter<RecycleBaseViewHolder>{

    protected Fragment fragmentActivity;
    protected UserViewModel userViewModel;

    protected long lastMsgTime = 0;//上一条消息的时间,如果列表内容下拉刷新时候，需要进行重置
    protected long showMsgTimeDiff = 5 * 60 * 1000 ;//两条时间显示的间隔，暂定为5分钟

    public ChatBaseAdapter(Fragment fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        userViewModel = ViewModelProviders.of(fragmentActivity).get(UserViewModel.class);
    }

    /**
     * 更新右侧用户数据
     */
    protected void initRightUserData(ImageView headIcon,ImageView vipStatus){
        String userUrl = UserInfoHelper.getHeadPic(ModuleMgr.getCenterMgr().getUID());
        if (null == context){
            Log.e("YM","Context为null");
            return;
        }
        if (null == userUrl){
            Log.e("YM","userId为null");
            return;
        }
        if (null == headIcon){
            Log.e("YM","headIcon为null");
            return;
        }
        GlideUtils.loadRound(context,userUrl,headIcon,5);
        boolean isVip = UserInfoHelper.getUserVipStatus(ModuleMgr.getCenterMgr().getUID());
        vipStatus.setVisibility(isVip ? View.VISIBLE : View.GONE);
    }

    /**
     * 更新右侧用户昵称
     * @param rightUserName
     */
    protected void updateRightUserName(TextView rightUserName){
        String userName = UserInfoHelper.getUserDisplayName(ModuleMgr.getCenterMgr().getUID());
        rightUserName.setText(userName);
    }
    /**
     * 更新左侧用户头像数据
     */
    protected void updateLeftHeadPic(String userId, ImageView headIcon){
        String headPic = UserInfoHelper.getHeadPic(userId);
        GlideUtils.loadRound(context,headPic,headIcon,5);
    }
    /**
     * 更新左侧用户VIP标识数据
     */
    protected void updateLeftVipStatus(String userId, ImageView vipIcon){
        boolean isVip = UserInfoHelper.getUserVipStatus(userId);
        vipIcon.setVisibility(isVip ? View.VISIBLE : View.GONE);
    }
    /**
     * 更新左侧用户VIP标识数据
     */
    protected void updateLeftUserName(String userId, TextView userNameTv){
        String userName = UserInfoHelper.getUserDisplayName(userId);
        userNameTv.setText(userName);
    }

    /**
     * 获取左侧用户的Id
     * @param messageBaseModel
     * @return
     */
    protected String getLeftFriendId(ImMessageBaseModel messageBaseModel){
        String userId = ModuleMgr.getCenterMgr().getUID();
        String friendId = "";
        if (userId.equals(messageBaseModel.getFromUserId())){
            friendId = messageBaseModel.getToUserId();
        }else {
            friendId = messageBaseModel.getFromUserId();
        }
        return friendId;
    }

    /**
     * 解析布局类型
     * Context需要外部传入，
     * 因为RecycleBaseAdapter中定义的Context是在onCreateViewHolder中初始化的，但是RecyclerView.Adapter中的getItemViewType是在此之前调用的，
     * 所以有很大可能性导致此处使用的Context还没有初始化
     */
    protected int parserLayoutType(ImMessageBaseModel p2PMessageBean){
        String msgType = p2PMessageBean.getMsgType();
//        String userId = EasySP.init(DqApp.sContext).getString(UserSpConstants.USER_ID);
        String userId = ModuleMgr.getCenterMgr().getUID();
        int rootLayoutType;
        if (MessageType.SYSTEM.getValue().equals(msgType)){
            rootLayoutType = ChatLayoutParentType.SYSTEM;
        }else{
            if (p2PMessageBean.getFromUserId().equals(userId)){
                rootLayoutType = ChatLayoutParentType.RIGHT;
            }else if (!p2PMessageBean.getFromUserId().equals(userId)){
                rootLayoutType = ChatLayoutParentType.LEFT;
            }else {//未知类型，不支持的消息格式
                rootLayoutType = ChatLayoutParentType.UN_KNOWN;
            }
        }

        String rootLayoutTypeContent = String.valueOf(rootLayoutType);//根布局,表明是左、中、右哪种布局。如果是系统布局的话消息布局采用第二种布局
        String firstLayoutType = "";
        if (ChatLayoutParentType.SYSTEM == rootLayoutType){//如果是系统消息的话，就从二级类型获取参数
            firstLayoutType = p2PMessageBean.getMsgSecondType();//消息布局，表示布局是文本布局还是图片布局等等
            if (TextUtils.isEmpty(firstLayoutType)){//有时候二级类型没有，所以这里统一按未识别类型处理
                firstLayoutType = MsgSecondType.MSG_SECOND_TYPE_UN_KNOWN.type;
            }
        }else {
            firstLayoutType = p2PMessageBean.getMsgType();//消息布局，表示布局是文本布局还是图片布局等等
        }
        TreeNodeIml treeNodeIml = ChatLayoutTreeManager.getInstance().findChildNode(rootLayoutTypeContent,firstLayoutType);
        return treeNodeIml.nodeType();
    }

    /**
     * 设置消息展示的时间
     */
    protected void setMsgTime(long lastMsgTime,long currentTime,TextView textView){
        if (currentTime - lastMsgTime < showMsgTimeDiff){
            textView.setVisibility(View.GONE);
            return;
        }
        textView.setVisibility(View.VISIBLE);
        String text = TimeUtil.getTimeShowString(currentTime, false);
        textView.setText(text);
    }

}