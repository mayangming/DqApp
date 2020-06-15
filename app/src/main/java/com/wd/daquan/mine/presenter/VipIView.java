package com.wd.daquan.mine.presenter;

import com.wd.daquan.chat.redpacket.pay.PayResult;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.model.bean.WxPayBody;

public interface VipIView<T> extends Presenter.IView<T> {
    void aliPaySuccess(PayResult payResult);
    void weChatPaySuccess(WxPayBody payResult);
}