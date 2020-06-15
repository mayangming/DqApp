package com.wd.daquan.third.session.viewholder;

import com.wd.daquan.R;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * @author: dukangkang
 * @date: 2018/9/21 16:39.
 * @description: todo ...
 */
public class QcUnknownViewHolder extends MsgViewHolderBase {
    public QcUnknownViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.unknow_viewholder_item;
    }

    @Override
    protected void inflateContentView() {

    }

    @Override
    protected void bindContentView() {

    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }

    @Override
    protected boolean shouldDisplayReceipt() {
        return false;
    }
}
