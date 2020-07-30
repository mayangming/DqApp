package com.wd.daquan.imui.adapter;

import androidx.lifecycle.LifecycleObserver;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import android.view.View;
import android.view.ViewGroup;

import com.dq.im.model.TeamMessageBaseModel;
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
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.util.ArrayList;
import java.util.List;

/**
 * 群组聊天页面布局适配器
 */
public class ChaTeamAdapter extends ChatBaseAdapter<RecycleBaseViewHolder> {
    private List<TeamMessageBaseModel> p2PMessageBeans = new ArrayList<>();

    public ChaTeamAdapter(Fragment fragmentActivity) {
        super(fragmentActivity);
    }

    public void setData(List<TeamMessageBaseModel> p2PMessageBeans){
        this.p2PMessageBeans = p2PMessageBeans;
        notifyDataSetChanged();
    }

    public void addData(TeamMessageBaseModel p2PMessageBean){
        p2PMessageBeans.add(p2PMessageBean);
        notifyDataSetChanged();
    }

    public void updateMessageStatus(TeamMessageBaseModel p2PMessageBean){
        int position = p2PMessageBeans.indexOf(p2PMessageBean);
        notifyItemChanged(position);
    }
    public void removeData(TeamMessageBaseModel p2PMessageBean){
        p2PMessageBeans.remove(p2PMessageBean);
        notifyDataSetChanged();
    }
    public void addDataAll(List<TeamMessageBaseModel> p2PMessageBeans){
        this.p2PMessageBeans.addAll(0,p2PMessageBeans);
        notifyDataSetChanged();
    }

    public List<TeamMessageBaseModel> getData(){
        return p2PMessageBeans;
    }

    /**
     * 获取指定位置的数据
     * @param position
     * @return
     */
    public TeamMessageBaseModel getData(int position){
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
        TeamMessageBaseModel teamMessageBaseModel = p2PMessageBeans.get(position);
        currentTime = teamMessageBaseModel.getCreateTime();
        if (0 != position){
            TeamMessageBaseModel lastMessage = p2PMessageBeans.get(position - 1);
            lastTime = lastMessage.getCreateTime();
        }
        BaseChatViewHolder baseChatViewHolder = (BaseChatViewHolder) holder;
        BaseLeftViewHolder baseLeftViewHolder = null;
        BaseRightViewHolder baseRightViewHolder = null;
        //设置用户的头像
        if (!MessageType.SYSTEM.getValue().equals(teamMessageBaseModel.getMsgType())){
            boolean isRightUser = ModuleMgr.getCenterMgr().getUID().equals(teamMessageBaseModel.getFromUserId());
            if (isRightUser){
                baseRightViewHolder = (BaseRightViewHolder) holder;
                initRightUserData(baseRightViewHolder.rightHeadIcon,baseRightViewHolder.rightHeadIconVip);
                updateRightUserName(baseRightViewHolder.rightUserName);
                updateMessageStatus(baseRightViewHolder,teamMessageBaseModel);//右侧需要刷新消息发送状态
                baseRightViewHolder.rightUserName.setVisibility(View.VISIBLE);
            }else {
                baseLeftViewHolder = (BaseLeftViewHolder) holder;
                String friendId = getLeftFriendId(teamMessageBaseModel);
                updateLeftHeadPic(friendId,baseLeftViewHolder.leftHeadIcon);
                updateLeftVipStatus(friendId,baseLeftViewHolder.leftVipIcon);
                updateLeftUserName(friendId,baseLeftViewHolder.leftUserName);
                baseLeftViewHolder.leftUserName.setVisibility(View.VISIBLE);
            }
        }
        setMsgTime(lastTime,currentTime,baseChatViewHolder.chatTime);
        if (layoutType == ChatLayoutChildType.LEFT_VOICE || layoutType == ChatLayoutChildType.RIGHT_VOICE){//这里面要监听生命周期以便页面停止后释放音频资源所以设置生命周期监听
            LifecycleObserver lifecycleObserver =
                    StrategyViewHolderBindContext.getInstance().onBindViewHolder(holder, teamMessageBaseModel, ImType.Team, layoutType);
            fragmentActivity.getLifecycle().addObserver(lifecycleObserver);
        }else {
            StrategyViewHolderBindContext.getInstance().onBindViewHolder(holder, teamMessageBaseModel, ImType.Team, layoutType);
        }
    }

    /**
     * 局部刷新
     */
    @Override
    public void onBindViewHolder(@NonNull RecycleBaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        //参考链接：https://blog.csdn.net/LVXIANGAN/article/details/91867671
        if (payloads.isEmpty()){
           return;
        }
        Object object = payloads.get(0);
        Friend friend = null;
        BaseLeftViewHolder baseLeftViewHolder = null;
        if (object instanceof Friend){
            friend = (Friend) object;
        }
        if (holder instanceof BaseLeftViewHolder){
            baseLeftViewHolder = (BaseLeftViewHolder) holder;
        }
        if (null == friend || null == baseLeftViewHolder){
            return;
        }
        updateLeftHeadPic(friend.uid,baseLeftViewHolder.leftHeadIcon);
        updateLeftVipStatus(friend.uid,baseLeftViewHolder.leftVipIcon);
        updateLeftUserName(friend.uid,baseLeftViewHolder.leftUserName);
    }

    private void updateMessageStatus(BaseRightViewHolder baseRightViewHolder, TeamMessageBaseModel teamMessageBaseModel){
        switch (MessageSendType.typeOfValue(teamMessageBaseModel.getMessageSendStatus())){
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