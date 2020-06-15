package com.wd.daquan.common.listener;

/**
 * 识别二维码回调
 */
public interface QrCodeListener {

    public void notFound();

    public void resume();

    public void finish();

    public void retryScan();
}