package com.wd.daquan.third.session.action;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.util.Log;

import com.da.library.tools.Utils;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.wd.daquan.BuildConfig;
import com.wd.daquan.R;
import com.wd.daquan.imui.constant.IntentCode;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

/**
 * 拍照的选择
 */
public class CameraAction extends BaseAction{
    private Uri mCurrentPhotoPath;
    public CameraAction() {
        super(R.drawable.chat_expansion_img_selector, R.string.input_panel_image);
    }

    @Override
    public void onClick() {
        if (Utils.isFastDoubleClick(500)) {
            return;
        }
//        takeCamera();
        selectPhoto();
    }

    /**
     * 选择图片
     */
    protected void selectPhoto(){
        Matisse.from(getActivity())
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .capture(true)//拍照功能暂时取消
                .captureStrategy(
                        new CaptureStrategy(true, BuildConfig.APPLICATION_ID+".dqprovider", "capture")
                )
                .maxSelectable(1)
                .gridExpectedSize(
                        getContext().getResources().getDimensionPixelSize(R.dimen.grid_expected_size)
                )
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .forResult(makeRequestCode(IntentCode.REQUEST_CODE_CHOOSE));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("YM","拍照获取的数据:requestCode->"+requestCode+"-->resultCode:"+resultCode);
        if (requestCode == IntentCode.REQUEST_CODE_CHOOSE){
            List<Uri> picturePath = Matisse.obtainResult(data);//获取uri路径
            MsgMgr.getInstance().sendMsg(MsgType.CHAT_PICTURE, picturePath.get(0));
        }
    }
}