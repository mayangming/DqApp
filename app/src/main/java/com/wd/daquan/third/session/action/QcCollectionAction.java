package com.wd.daquan.third.session.action;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.da.library.tools.Utils;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.wd.daquan.model.log.DqToast;

public class QcCollectionAction extends BaseAction {

    /**
     * 构造函数
     */
    public QcCollectionAction() {
        super(R.drawable.chat_expansion_collection_selector, R.string.collection);
    }
    @Override
    public void onClick() {
        if (Utils.isFastDoubleClick(500)) {
            return;
        }
        DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
        //NavUtils.gotoSessionCollectionActivity(getActivity(), getAccount(), getSessionType());
    }
}
