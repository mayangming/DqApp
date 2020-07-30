package com.netease.nim.uikit.business.session.module.list;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.dq.im.model.ImMessageBaseModel;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.module.Container;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderFactory;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by huangjun on 2016/12/21.
 */
public class MsgAdapter extends BaseMultiItemFetchLoadAdapter<ImMessageBaseModel, BaseViewHolder> {

    private Map<Class<? extends MsgViewHolderBase>, Integer> holder2ViewType;

    private ViewHolderEventListener eventListener;
    private Map<String, Float> progresses; // 有文件传输，需要显示进度条的消息ID map
    private String messageId;
    private Container container;

    MsgAdapter(RecyclerView recyclerView, List<ImMessageBaseModel> data, Container container) {
        super(recyclerView, data);

        timedItems = new HashSet<>();
        progresses = new HashMap<>();

        // view type, view holder
        holder2ViewType = new HashMap<>();
        List<Class<? extends MsgViewHolderBase>> holders = MsgViewHolderFactory.getAllViewHolders();
        int viewType = 0;
        for (Class<? extends MsgViewHolderBase> holder : holders) {
            viewType++;
            addItemType(viewType, R.layout.nim_message_item, holder);
            holder2ViewType.put(holder, viewType);
        }

        this.container = container;
//        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onChanged() {
//                Log.e("TAG","AdapterData Change....");
//            }
//        });
    }

    @Override
    protected int getViewType(ImMessageBaseModel message) {
        return holder2ViewType.get(MsgViewHolderFactory.getViewHolderByType(message));
    }

    @Override
    protected String getItemKey(ImMessageBaseModel item) {
        return item.getMsgIdServer();
    }

    public void setEventListener(ViewHolderEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public ViewHolderEventListener getEventListener() {
        return eventListener;
    }

    public float getProgress(ImMessageBaseModel message) {
        Float progress = progresses.get(message.getMsgIdServer());
        return progress == null ? 0 : progress;
    }

    /**
     * *********************** 时间显示处理 ***********************
     */

    private Set<String> timedItems; // 需要显示消息时间的消息ID

    public interface ViewHolderEventListener {
        // 长按事件响应处理
        boolean onViewHolderLongClick(View clickView, View viewHolderView, ImMessageBaseModel item);

        // 发送失败或者多媒体文件下载失败指示按钮点击响应处理
        void onFailedBtnClick(ImMessageBaseModel resendMessage);

        // viewholder footer按钮点击，如机器人继续会话
        void onFooterClick(ImMessageBaseModel message);

        boolean onAvatarLongClicked(Context context, ImMessageBaseModel message);
    }

    public void setUuid(String messageId) {
        this.messageId = messageId;
    }

    public String getUuid() {
        return messageId;
    }

    public Container getContainer() {
        return container;
    }
}
