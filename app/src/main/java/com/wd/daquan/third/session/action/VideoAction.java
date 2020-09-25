package com.wd.daquan.third.session.action;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.da.library.tools.Utils;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.wd.daquan.R;
import com.wd.daquan.common.alioss.AliOssHelper;
import com.wd.daquan.imui.constant.IntentCode;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

import java.util.List;

/**
 * @author: dukangkang
 * @date: 2018/9/7 18:07.
 * @description: todo ...
 */
public class VideoAction extends BaseAction {

    private AliOssHelper mAliOssHelper = null;
    protected String[] needPermissions = {Manifest.permission.CAMERA};

    /**
     * 构造函数
     */
    public VideoAction() {
        super(R.drawable.chat_expansion_catch_video_selector, R.string.input_panel_video);
    }

    @Override
    public void onClick() {
        if (Utils.isFastDoubleClick(500)) {
            return;
        }

        // TODO: 2018/9/11 权限检查
//        NavUtils.gotoCaremaActivity(getActivity(), getAccount(), getSessionType(), makeRequestCode(RequestCode.CAPTURE_VIDEO));
        checkPermissions();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        Uri videoUri = data.getData();
        MsgMgr.getInstance().sendMsg(MsgType.CHAT_VIDEO, videoUri);
//        String imagePath = data.getStringExtra(KeyValue.Carema.KEY_IMAGE_PATH);
//        String videoPath = data.getStringExtra(KeyValue.Carema.KEY_VIDEO_PATH);
//        if (!TextUtils.isEmpty(imagePath)) {
//            IMMessage message = MessageBuilder.createImageMessage(getAccount(), getSessionType(), new File(imagePath), "图片");
//            sendMessage(message);
//        } else if (!TextUtils.isEmpty(videoPath)) {
//            long duration = data.getLongExtra(KeyValue.Carema.KEY_VIDEO_DURATION, 0);
////            int rotate = intentUrl.getIntExtra(KeyValue.Carema.KEY_VIDEO_ROTATE, 90);
////            long fileSize = intentUrl.getLongExtra(KeyValue.Carema.KEY_VIDEO_FILE_SIZE, 0);
////            AliOssHelper mAliOssHelper = new AliOssHelper();
////            IMMessage inertMessage = MessageManager.getVideoMessage(getAccount(), getSessionType(), duration, videoPath, fileSize, ""+rotate);
////            insertMessage(inertMessage);
////            mAliOssHelper.setPath(videoPath).uploadFile().setCallback(new FileCallBack<DqVideoAttachment>(inertMessage) {
////                @Override
////                protected void onSuccess(DqVideoAttachment messageContent, File file, CNOSSFileBean bean, long totalSize) {
////                    super.onSuccess(messageContent, file, bean, totalSize);
////                    send(mMessage);
////                }
////            });
//            File videoFile = new File(videoPath);
//            GlideUtils.loadBitmapListener(getActivity(), videoFile, bitmap -> {
//                IMMessage message = MessageBuilder.createVideoMessage(getAccount(),
//                        getSessionType(), videoFile, duration,
//                        bitmap.getWidth(), bitmap.getHeight(), videoFile.getName());
//                Log.e("dq", "getWidth ： " +  bitmap.getWidth()
//                        + " , getHeight : " + bitmap.getHeight());
//                sendMessage(message);
//            });
//
//        }
    }


    /** 检查权限 */
    private void checkPermissions(){
//        RxPermissions rxPermissions = new RxPermissions(getActivity());
//        Disposable disposable = rxPermissions.request(Manifest.permission.CAMERA)
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean aBoolean) throws Exception {
//                        if (aBoolean){
//                            dispatchTakeVideoIntent();
//                        }
//                    }
//                });
        XXPermissions.with(getActivity())
                // 不适配 Android 11 可以这样写
                //.permission(Permission.Group.STORAGE)
                // 适配 Android 11 需要这样写，这里无需再写 Permission.Group.STORAGE
                .permission(Permission.CAMERA)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            dispatchTakeVideoIntent();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean never) {
                        if (never) {
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(getActivity(), denied);
                        }
                    }
                });
    }

    /**
     * 调起相机进行视频拍摄
     * https://www.jianshu.com/p/c46d05766d09
     */
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);    // MediaStore.EXTRA_VIDEO_QUALITY 表示录制视频的质量，从 0-1，越大表示质量越好，同时视频也越大
//        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);    // 表示录制完后保存的录制，如果不写，则会保存到默认的路径，在onActivityResult()的回调，通过intent.getData中返回保存的路径
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);   // 设置视频录制的最长时间,单位为秒
        if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
            getActivity().startActivityForResult(takeVideoIntent, makeRequestCode(IntentCode.REQUEST_VIDEO_CAPTURE));
        }
    }

}
