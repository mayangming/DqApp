package com.wd.daquan.imui.adapter;

import android.arch.lifecycle.LifecycleObserver;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.type.ImType;
import com.dq.im.type.MessageSendType;
import com.dq.im.type.MessageType;
import com.wd.daquan.imui.adapter.viewholder.BaseChatViewHolder;
import com.wd.daquan.imui.adapter.viewholder.BaseLeftViewHolder;
import com.wd.daquan.imui.adapter.viewholder.BaseRightViewHolder;
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder;
import com.wd.daquan.imui.adapter.viewholder.StrategyViewHolderContext;
import com.wd.daquan.imui.adapter.viewholderbind.StrategyViewHolderBindContext;
import com.wd.daquan.imui.type.chat_layout.ChatLayoutChildType;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人聊天页面布局适配器
 */
public class ChatP2PAdapter extends ChatBaseAdapter<RecycleBaseViewHolder> {

    private List<P2PMessageBaseModel> p2PMessageBeans = new ArrayList<>();
    public ChatP2PAdapter(Fragment fragmentActivity) {
        super(fragmentActivity);
    }

    public void setData(List<P2PMessageBaseModel> p2PMessageBeans){
        this.p2PMessageBeans = p2PMessageBeans;
        notifyDataSetChanged();
    }

    public void addData(P2PMessageBaseModel p2PMessageBean){
        p2PMessageBeans.add(p2PMessageBean);
        notifyDataSetChanged();
    }

    public void updateMessageStatus(P2PMessageBaseModel p2PMessageBean){
        int position = p2PMessageBeans.indexOf(p2PMessageBean);
        notifyItemChanged(position);
    }

    public void addDataAll(List<P2PMessageBaseModel> p2PMessageBeans){
        this.p2PMessageBeans.addAll(0,p2PMessageBeans);
        notifyDataSetChanged();
    }

    public List<P2PMessageBaseModel> getData(){
        return p2PMessageBeans;
    }

    public void removeData(P2PMessageBaseModel p2PMessageBean){
        p2PMessageBeans.remove(p2PMessageBean);
        notifyDataSetChanged();
    }

    /**
     * 获取指定位置的数据
     * @param position
     * @return
     */
    public P2PMessageBaseModel getData(int position){
        return p2PMessageBeans.get(position);
    }

    @NonNull
    @Override
    public RecycleBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent,viewType);
        return StrategyViewHolderContext.getInstance().createViewHolder(viewType,parent);
    }

    /**
     * 全局刷新
     */
    @Override
    public void onBindViewHolder(@NonNull RecycleBaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        int layoutType = getItemViewType(position);
        long currentTime;
        long lastTime = 0;
        P2PMessageBaseModel p2PMessageBean = p2PMessageBeans.get(position);
        currentTime = p2PMessageBean.getCreateTime();//当前时间
        if (0 != position){
            P2PMessageBaseModel lastMessageBean = p2PMessageBeans.get(position - 1);
            lastTime = lastMessageBean.getCreateTime();
        }
        BaseChatViewHolder baseChatViewHolder = (BaseChatViewHolder) holder;
        BaseLeftViewHolder baseLeftViewHolder;
        BaseRightViewHolder baseRightViewHolder;
        //设置用户的头像
        if (!MessageType.SYSTEM.getValue().equals(p2PMessageBean.getMsgType())) {
            boolean isRightUser = ModuleMgr.getCenterMgr().getUID().equals(p2PMessageBean.getFromUserId());
            if (isRightUser) {
                baseRightViewHolder = (BaseRightViewHolder) holder;
                initRightUserData(baseRightViewHolder.rightHeadIcon, baseRightViewHolder.rightHeadIconVip);
//                updateRightUserName(baseRightViewHolder.rightUserName);
                updateMessageStatus(baseRightViewHolder, p2PMessageBean);//右侧需要进行刷新消息发送状态
                baseRightViewHolder.rightUserName.setVisibility(View.GONE);
            } else {
                baseLeftViewHolder = (BaseLeftViewHolder) holder;
                String friendId = getLeftFriendId(p2PMessageBean);
                updateLeftHeadPic(friendId, baseLeftViewHolder.leftHeadIcon);
                updateLeftVipStatus(friendId,baseLeftViewHolder.leftVipIcon);
//                updateLeftUserName(friendId,baseLeftViewHolder.leftUserName);
                baseLeftViewHolder.leftUserName.setVisibility(View.GONE);
            }
        }

        setMsgTime(lastTime,currentTime,baseChatViewHolder.chatTime);
        if (layoutType == ChatLayoutChildType.LEFT_VOICE || layoutType == ChatLayoutChildType.RIGHT_VOICE){//这里面要监听生命周期以便页面停止后释放音频资源所以设置生命周期监听
            LifecycleObserver lifecycleObserver =
                    StrategyViewHolderBindContext.getInstance().onBindViewHolder(holder, p2PMessageBean, ImType.P2P, layoutType);
            fragmentActivity.getLifecycle().addObserver(lifecycleObserver);
        }else {
            StrategyViewHolderBindContext.getInstance().onBindViewHolder(holder, p2PMessageBean, ImType.P2P, layoutType);
        }
    }

    /**
     * 局部刷新
     */
    @Override
    public void onBindViewHolder(@NonNull RecycleBaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        //参考链接：https://blog.csdn.net/LVXIANGAN/article/details/91867671

    }

    private void updateMessageStatus(BaseRightViewHolder baseRightViewHolder, P2PMessageBaseModel p2PMessageBaseModel){
        switch (MessageSendType.typeOfValue(p2PMessageBaseModel.getMessageSendStatus())){
            case SEND_LOADING:
                baseRightViewHolder.messageLoadingView.setVisibility(View.VISIBLE);
                baseRightViewHolder.messageFailView.setVisibility(View.GONE);
                break;
            case SEND_SUCCESS:
                baseRightViewHolder.messageLoadingView.setVisibility(View.GONE);
                baseRightViewHolder.messageFailView.setVisibility(View.GONE);
                break;
            case SEND_FAIL:
                baseRightViewHolder.messageLoadingView.setVisibility(View.GONE);
                baseRightViewHolder.messageFailView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return p2PMessageBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return parserLayoutType(p2PMessageBeans.get(position));
    }

}