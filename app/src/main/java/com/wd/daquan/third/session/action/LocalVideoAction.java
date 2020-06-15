package com.wd.daquan.third.session.action;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.da.library.tools.Utils;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.wd.daquan.R;
import com.wd.daquan.imui.constant.IntentCode;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

/**
 * 本地视频的选择
 */
public class LocalVideoAction extends BaseAction{
    private Uri mCurrentPhotoPath;
    public LocalVideoAction() {
        super(R.drawable.chat_expansion_video_selector, R.string.input_panel_local_video);
    }

    @Override
    public void onClick() {
        if (Utils.isFastDoubleClick(500)) {
            return;
        }
//        takeCamera();
        selectVideoIntent();
    }

    /**
     * 选择本地视频
     */
    private void selectVideoIntent(){




//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(intent, IntentCode.REQUEST_VIDEO_CAPTURE);
        Intent intent = new Intent();
        /* 开启Pictures画面Type设定为image */
        //intent.setType("image/*");
        // intent.setType("audio/*"); //选择音频
//        intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）

        // intent.setType("video/*;image/*");//同时选择视频和图片

        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,"video/*");
        getActivity().startActivityForResult(intent, makeRequestCode(IntentCode.REQUEST_VIDEO_CAPTURE));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("YM","本地视频获取的数据:requestCode->"+requestCode+"-->resultCode:"+resultCode);
        if (requestCode == IntentCode.REQUEST_VIDEO_CAPTURE){
            Uri videoUri = data.getData();
            MsgMgr.getInstance().sendMsg(MsgType.CHAT_VIDEO, videoUri);
        }
    }
}