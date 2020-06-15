package com.wd.daquan.third.session.viewholder;

import com.wd.daquan.R;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * 零钱红包
 * Created by Kind on 2019-05-20.
 */
public class QcChangeMsgViewHolder extends MsgViewHolderBase {

    public QcChangeMsgViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.change_msg_viewholder;
    }

    @Override
    protected void inflateContentView() {

    }

    @Override
    protected void bindContentView() {
        if(isReceivedMessage()){

        }
    }

    @Override
    protected int rightBackground() {
        return R.color.transparent;
    }

    @Override
    protected int leftBackground() {
        return R.color.transparent;
    }
}
