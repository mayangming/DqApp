package com.wd.daquan.imui.adapter.viewholder;

import android.view.View;

import com.dq.im.model.P2PMessageBaseModel;
import com.wd.daquan.imui.type.chat_layout.ChatLayoutChildType;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 中心仓库
 * 这里不允许操作Adapter中的数据，仅仅只作为布局展示
 */
public class StrategyViewHolderContext {
    private static Map<Integer, RecycleBaseViewHolder> viewHolderMap = new HashMap<>();
    private static Map<Integer, Class<? extends ChatViewHolderStrategy>> strategyMap = new HashMap<>();
    private static StrategyViewHolderContext strategyViewHolderContext;
    public static StrategyViewHolderContext getInstance(){
        if (null == strategyViewHolderContext){
            strategyViewHolderContext = new StrategyViewHolderContext();
        }
        return strategyViewHolderContext;
    }
    private StrategyViewHolderContext() {
        addStrategy(ChatLayoutChildType.RIGHT_TEXT,RightTextViewHolder.class);
        addStrategy(ChatLayoutChildType.RIGHT_IMG,RightImgViewHolder.class);
        addStrategy(ChatLayoutChildType.RIGHT_VOICE,RightVoiceViewHolder.class);
        addStrategy(ChatLayoutChildType.RIGHT_VIDEO,RightVideoViewHolder.class);
        addStrategy(ChatLayoutChildType.LEFT_TEXT,LeftTextViewHolder.class);
        addStrategy(ChatLayoutChildType.LEFT_IMG,LeftImgViewHolder.class);
        addStrategy(ChatLayoutChildType.LEFT_VOICE,LeftVoiceViewHolder.class);
        addStrategy(ChatLayoutChildType.LEFT_VIDEO,LeftVideoViewHolder.class);
        addStrategy(ChatLayoutChildType.LEFT_RED_PACKAGE,LeftRedPackageViewHolder.class);
        addStrategy(ChatLayoutChildType.RIGHT_RED_PACKAGE,RightRedPackageViewHolder.class);
        addStrategy(ChatLayoutChildType.LEFT_CARD,LeftCardMsgViewHolder.class);
        addStrategy(ChatLayoutChildType.RIGHT_CARD,RightCardMsgViewHolder.class);
        addStrategy(ChatLayoutChildType.CENTER_TEXT,CenterTextViewHolder.class);
    }

    public void addViewHolder(int type, RecycleBaseViewHolder recycleBaseViewHolder){
        viewHolderMap.put(type,recycleBaseViewHolder);
    }

    public void addStrategy(int type,Class<? extends ChatViewHolderStrategy> chatViewHolderStrategy){
        strategyMap.put(type, chatViewHolderStrategy);
    }

    /**
     * 动态获取指定的实体类
     */
    public RecycleBaseViewHolder createViewHolder(int type, View view){
        ChatViewHolderStrategy chatViewHolderStrategy = null;
        try {
            if (strategyMap.containsKey(type)){
                Constructor<? extends ChatViewHolderStrategy> cons = strategyMap.get(type).getConstructor(View.class);
                chatViewHolderStrategy = cons.newInstance(view);
            }else {//没有添加的类型统一按照未识别类型处理
                Constructor<? extends ChatViewHolderStrategy> cons = CenterUnknownViewHolder.class.getConstructor(View.class);
                chatViewHolderStrategy = cons.newInstance(view);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        RecycleBaseViewHolder recycleBaseViewHolder = chatViewHolderStrategy.createViewHolder(view);
//        addViewHolder(type,recycleBaseViewHolder);//添加到ViewHolder里面备用
        return recycleBaseViewHolder;
    }

    /**
     * 填充数据
     * @param type
     * @param p2PMessageBean
     */
    public void onBindViewHolder(int type, P2PMessageBaseModel p2PMessageBean){

    }
}