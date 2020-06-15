package com.meetqs.qingchat.carema.state;

import android.view.Surface;
import android.view.SurfaceHolder;

import com.meetqs.qingchat.carema.CameraInterface;
import com.meetqs.qingchat.carema.util.LogUtil;
import com.meetqs.qingchat.carema.view.JCameraView;

public class BorrowVideoState implements State {
    private final String TAG = "BorrowVideoState";
    private CameraMachine machine;

    public BorrowVideoState(CameraMachine machine) {
        this.machine = machine;
    }

    @Override
    public void start(SurfaceHolder holder, float screenProp) {
        CameraInterface.getInstance().doStartPreview(holder, screenProp);
        machine.setState(machine.getPreviewState());
    }

    @Override
    public void stop() {

    }

    @Override
    public void foucs(float x, float y, CameraInterface.FocusCallback callback) {

    }


    @Override
    public void swtich(SurfaceHolder holder, float screenProp) {

    }

    @Override
    public void restart() {

    }

    @Override
    public void capture() {

    }

    @Override
    public void record(Surface surface, float screenProp) {
        LogUtil.i("video record");
    }

    @Override
    public void stopRecord(boolean isShort, long time) {
        LogUtil.i("video stopRecord");
    }

    @Override
    public void cancle(SurfaceHolder holder, float screenProp) {
        LogUtil.i("video cancle");

        machine.getView().resetState(JCameraView.TYPE_VIDEO);
        machine.setState(machine.getPreviewState());
    }

    @Override
    public void confirm() {
        LogUtil.i("video confirm");
        machine.getView().confirmState(JCameraView.TYPE_VIDEO);
        machine.setState(machine.getPreviewState());
    }

    @Override
    public void zoom(float zoom, int type) {
        LogUtil.i(TAG, "zoom");
    }

    @Override
    public void flash(String mode) {

    }
}
