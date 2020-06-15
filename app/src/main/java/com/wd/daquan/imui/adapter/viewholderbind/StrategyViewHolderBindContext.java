package com.wd.daquan.imui.adapter.viewholderbind;


import android.arch.lifecycle.LifecycleObserver;

import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder;
import com.wd.daquan.imui.type.chat_layout.ChatLayoutChildType;

import java.util.HashMap;
import java.util.Map;

/**
 * 中心仓库
 * 仅作为布局数据填充
 */
public class StrategyViewHolderBindContext {
    private static Map<Integer, RecycleBaseViewHolder> viewHolderMap = new HashMap<>();
    private static Map<Integer, Class<? extends ChatViewHolderBindStrategy>> strategyMap = new HashMap<>();
    private static StrategyViewHolderBindContext strategyViewHolderContext;
    public static StrategyViewHolderBindContext getInstance(){
        if (null == strategyViewHolderContext){
            strategyViewHolderContext = new StrategyViewHolderBindContext();
        }
        return strategyViewHolderContext;
    }

    private StrategyViewHolderBindContext() {
        addStrategy(ChatLayoutChildType.RIGHT_TEXT,RightTextViewHolderBind.class);
        addStrategy(ChatLayoutChildType.RIGHT_IMG, RightImgViewHolderBind.class);
        addStrategy(ChatLayoutChildType.RIGHT_VOICE, RightVoiceViewHolderBind.class);
        addStrategy(ChatLayoutChildType.RIGHT_VIDEO, RightVideoViewHolderBind.class);
        addStrategy(ChatLayoutChildType.LEFT_TEXT, LeftTextViewHolderBind.class);
        addStrategy(ChatLayoutChildType.LEFT_IMG, LeftImgViewHolderBind.class);
        addStrategy(ChatLayoutChildType.LEFT_VOICE, LeftVoiceChatViewHolderBind.class);
        addStrategy(ChatLayoutChildType.LEFT_VIDEO, LeftVideoViewHolderBind.class);
        addStrategy(ChatLayoutChildType.LEFT_RED_PACKAGE, LeftRedPackageViewHolderBind.class);
        addStrategy(ChatLayoutChildType.RIGHT_RED_PACKAGE, RightRedPackageViewHolderBind.class);
        addStrategy(ChatLayoutChildType.LEFT_CARD, LeftCardViewHolderBind.class);
        addStrategy(ChatLayoutChildType.RIGHT_CARD, RightCardViewHolderBind.class);
        addStrategy(ChatLayoutChildType.CENTER_TEXT, CenterTextViewHolderBind.class);
        addStrategy(ChatLayoutChildType.UNKNOWN, CenterUnknownViewHolderBind.class);
//        addStrategy(ChatBaseAdapter.CENTER_RED_PACKAGE_UNRECEIVED, CenterRedPackageTextViewHolderBind.class);
//        addStrategy(ChatBaseAdapter.CENTER_RED_PACKAGE_RECEIVED,CenterRedPackageTextViewHolderBind.class);
//        addStrategy(ChatBaseAdapter.CENTER_RED_PACKAGE_RECEIVED_COMPLETE,CenterRedPackageTextViewHolderBind.class);
//        addStrategy(ChatBaseAdapter.CENTER_RED_PACKAGE_CALL_BACK,CenterRedPackageTextViewHolderBind.class);
//        addStrategy(ChatBaseAdapter.CENTER_RED_PACKAGE_EXPIRED,CenterRedPackageTextViewHolderBind.class);
    }

    public void addViewHolder(int type, RecycleBaseViewHolder recycleBaseViewHolder){
        viewHolderMap.put(type,recycleBaseViewHolder);
    }

    public void addStrategy(int type,Class<? extends ChatViewHolderBindStrategy> chatViewHolderStrategy){
        strategyMap.put(type, chatViewHolderStrategy);
    }

    /**
     * 填充数据
     * 返回生命周期状态，这样某些感知生命周期的事件可以及时处理
     *
     */
    public LifecycleObserver onBindViewHolder(RecycleBaseViewHolder holder, ImMessageBaseModel imMessageBaseModel, ImType chatType, int layoutType){
        ChatViewHolderBindStrategy chatViewHolderStrategy = null;
        try {
            if (strategyMap.containsKey(layoutType)){
                chatViewHolderStrategy = strategyMap.get(layoutType).newInstance();
            }else {//没有添加的类型统一按照未识别类型处理
                chatViewHolderStrategy = CenterUnknownViewHolderBind.class.newInstance();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
       return chatViewHolderStrategy.bindViewHolder(holder,imMessageBaseModel,chatType);
    }
}