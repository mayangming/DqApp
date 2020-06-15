package com.wd.daquan.third.session.action;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.wd.daquan.R;

/**
 * 转帐
 * Created by Kind on 2019-05-20.
 */
public class TransferAction extends BaseAction {

    public TransferAction() {
        super(R.drawable.chat_expansion_transfer_selector, R.string.input_panel_transfer);
    }

    @Override
    public void onClick() {
//        NavUtils.gotoTransferP2PAct(getActivity(), getAccount());
    }
}
