package com.wd.daquan.chat.group.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.da.library.adapter.CommRecyclerViewAdapter;
import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;
import com.wd.daquan.R;
import com.wd.daquan.chat.group.adapter.holder.BaseSearchChatMessageHolder;
import com.wd.daquan.chat.group.bean.SearchChatBean;
import com.wd.daquan.chat.group.holder.SearchGroupTradeHolder;
import com.wd.daquan.third.session.extension.QcAlipayRpAttachment;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: 方志
 * @Time: 2019/5/13 19:22
 * @Description:
 */
public class SearchGroupTradeAdapter extends CommRecyclerViewAdapter<SearchChatBean, BaseSearchChatMessageHolder> {

    /**
     * 没有数据的item兼容后续扩展字段
     */
    private static final int NO_DATA = -1;
    /**
     * 交易，红包消息
     */
    private static final int ITEM_TRADE = 0;



    @Override
    public int getItemViewType(int position) {
        SearchChatBean searchChatBean = getItem(position);
        if (searchChatBean == null) return NO_DATA;
        ImMessageBaseModel imMessage = searchChatBean.imMessage;
        if (imMessage == null) return NO_DATA;

        IMContentDataModel attachment = imMessage.getContentData();
        if(attachment instanceof QcAlipayRpAttachment) {
            return ITEM_TRADE;
        }
        return NO_DATA;
    }

    @Override
    protected BaseSearchChatMessageHolder onBindView(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TRADE) {
            new SearchGroupTradeHolder(mInflater.inflate(R.layout.group_search_trade_item, parent, false));
        }
        return new BaseSearchChatMessageHolder(mInflater.inflate(R.layout.no_data_adapter_item, parent, false));
    }

    @Override
    protected void onBindData(@NotNull @NonNull BaseSearchChatMessageHolder holder, int position) {
        super.onBindData(holder, position);
        switch (getItemViewType(position)) {
            case ITEM_TRADE:
                SearchGroupTradeHolder tradeHolder = (SearchGroupTradeHolder) holder;
                tradeHolder.setInput(input);
                tradeHolder.bindData(getItem(position), position);
                break;
        }
    }

    private String input;

    public void setInput(String input) {
        this.input = input;
    }
}
