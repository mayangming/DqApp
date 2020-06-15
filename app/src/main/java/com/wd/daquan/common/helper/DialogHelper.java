package com.wd.daquan.common.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.da.library.constant.IConstant;
import com.da.library.listener.DialogListener;
import com.da.library.tools.AESHelper;
import com.da.library.view.CommDialog;
import com.da.library.widget.MainLoading;
import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.bean.im.MessageTextBean;
import com.dq.im.model.IMContentDataModel;
import com.dq.im.type.ImType;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.bean.ShareItemBean;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.third.session.SessionHelper;

/**
 * @author: dukangkang
 * @date: 2018/9/18 18:51.
 * @description: todo ...
 */
public class DialogHelper {
    private CommDialog mCommDialog;
    private MainLoading mProgressDialog;

    private static class DialogHolder {
        static DialogHelper INSTANCE = new DialogHelper();
    }

    public static DialogHelper getInstance() {
        return DialogHolder.INSTANCE;
    }

    /**
     * 只有一个确定按钮
     * @param context
     * @param message
     */
    public static void showDialog(Context context, String message) {
        CommDialog mCommDialog = new CommDialog(context);
        mCommDialog.setTitleVisible(false);
        mCommDialog.setDesc(message);
        mCommDialog.setSingleBtn();
        mCommDialog.setOkTxt("确定");
        mCommDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                mCommDialog.dismiss();
            }
        });
        mCommDialog.show();
    }

    /**
     * 授权对话框
     * @param context
     * @param message
     */
    public static void showDialogForAuth(Context context, String message) {
        CommDialog mCommDialog = new CommDialog(context);
        mCommDialog.setTitleVisible(false);
        mCommDialog.setDesc(message);
        mCommDialog.setCancelTxt("取消");
        mCommDialog.setOkTxt("立刻授权");
        mCommDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                NavUtils.gotoAlipayBindingActivity(context);
            }
        });
        mCommDialog.setOkTxtColor(DqApp.sContext.getResources().getColor(R.color.color_5495e8));
        mCommDialog.show();
    }

    /**
     * 分享自定义消息到单个会话的dialog
     */
    public void showShareDialog(@NonNull Context context, @NonNull ShareItemBean shareItem, String enterType) {
        mEnterType = enterType;
        String desc = shareItem.sessionName;
        createShareDialog(context, desc, shareItem.shareType, shareItem.sessionPortrait);
        DqLog.e("YM----------->sessionId:"+shareItem.sessionId);
        if(null != mCommDialog) {
            mCommDialog.setDialogListener(new DialogListener() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onOk() {
                    DqLog.e("YM----------->DialogHelper--->115:");
                }
            });
        }
    }

    private String mEnterType;
    /**
     * 分享消息到单个会话的dialog
     */
    IMContentDataModel contentDataModel;
    public void showShareDialog(@NonNull Context context, @NonNull  ShareItemBean shareItem, @NonNull String enterType,
                                @NonNull String textOrImage, @NonNull String path) {
        mEnterType = enterType;

        DqLog.e("YM----------->sessionId:"+shareItem.sessionId);
        if(IConstant.Share.TEXT.equals(textOrImage)) {
            shareItem.shareType = "文本";
//            if (shareItem.shareMessage != null) {
//                message = MessageBuilder.createForwardMessage(shareItem.shareMessage, shareItem.sessionId, shareItem.sessionType);
//            } else {
//                message = MessageBuilder.createTextMessage(shareItem.sessionId, shareItem.sessionType, AESHelper.encryptString(path));
//            }
            MessageTextBean messageTextBean = new MessageTextBean();
            messageTextBean.setDescription(AESHelper.encryptString(path));
            contentDataModel = messageTextBean;
        }else if(IConstant.Share.IMAGE.equals(textOrImage)) {
            shareItem.shareType = "图片";
//            if (shareItem.shareMessage != null) {
//                message = MessageBuilder.createForwardMessage(shareItem.shareMessage, shareItem.sessionId, shareItem.sessionType);
//            } else {
//                message = MessageBuilder.createImageMessage(shareItem.sessionId, shareItem.sessionType, new File(path));
//            }
            MessagePhotoBean messagePhotoBean = new MessagePhotoBean();
            messagePhotoBean.setLocalUriString(path);
            contentDataModel = messagePhotoBean;
        }

        String desc = shareItem.sessionName;
        createShareDialog(context, desc, shareItem.shareType, shareItem.sessionPortrait);
//        IMMessage finalMessage = message;

        if(null != mCommDialog) {
            mCommDialog.setDialogListener(new DialogListener() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onOk() {
                    Log.e("YM","通用对话框成功");
//                    createProgressDialog(context);
//                    NIMClient.getService(MsgService.class).sendMessage(finalMessage, true)
//                            .setCallback(mRequestCallBack);
//
//                    MessageListPanelHelper.getInstance().notifyAddMessage(finalMessage);
                    if (shareItem.sessionType == ImType.P2P){
                        SessionHelper.startP2PSession(context,shareItem.sessionId,contentDataModel);
                    }else {
                        SessionHelper.startTeamSession(context,shareItem.sessionId,contentDataModel);
                    }
                }
            });
        }
    }

//    /**
//     * 分享自定义消息到多个会话的dialog
//     */
//    public void showShareMoreDialog(@NonNull Context context, @NonNull List<ShareItemBean> shareList, String enterType) {
//        mEnterType = enterType;
//        StringBuilder stringBuilder = new StringBuilder();
//        for (ShareItemBean itemBean: shareList) {
//            stringBuilder.append(itemBean.sessionName).append(",");
//        }
//        String str = stringBuilder.toString();
//        String desc = str.substring(0, str.length() - 1);
//        String shareType = shareList.get(0).shareType;
//        String sessionPortrait = shareList.get(0).sessionPortrait;
//
//        createShareDialog(context, desc, shareType, sessionPortrait);
//
//        if(null != mCommDialog) {
//            mCommDialog.setDialogListener(new DialogListener() {
//                @Override
//                public void onCancel() {
//
//                }
//
//                @Override
//                public void onOk() {
//                    createProgressDialog(context);
//                    sendMessage(shareList);
//                }
//            });
//        }
//    }
//
//    //发送多条消息
//    private void sendMessage(List<ShareItemBean> shareList) {
//        for (ShareItemBean shareItem : shareList) {
//            IMMessage content;
//            if (shareItem.shareMessage != null) {
//                content = MessageBuilder.createForwardMessage(shareItem.shareMessage, shareItem.sessionId, shareItem.sessionType);
//            } else {
//                content = MessageBuilder.createCustomMessage(shareItem.sessionId,
//                        shareItem.sessionType, shareItem.messageDesc, shareItem.attachment);
//            }
//            NIMClient.getService(MsgService.class).sendMessage(content, true)
//                    .setCallback(mRequestCallBack);
//        }
//    }

    public void onDestroy() {
        if(null != mCommDialog) {
            mCommDialog.dismiss();
            mCommDialog = null;
        }

        if(null != mProgressDialog) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

//    /**
//     * 多个item分享的dialog
//     */
//    public void showShareMoreDialog(@NonNull Context context, @NonNull List<ShareItemBean> shareList, String enterType,
//                                    @NonNull String textOrImage, @NonNull String path) {
//        mEnterType = enterType;
//        List<IMMessage> messageList = new ArrayList<>();
//
//        StringBuilder stringBuilder = new StringBuilder();
//        for (ShareItemBean shareItem: shareList) {
//            stringBuilder.append(shareItem.sessionName).append(",");
//            IMMessage content = null;
//            if(IConstant.Share.TEXT.equals(textOrImage)) {
//                shareItem.shareType = "文本";
//                content = MessageBuilder.createTextMessage(shareItem.sessionId, shareItem.sessionType, path);
//            }else if(IConstant.Share.IMAGE.equals(textOrImage)) {
//                shareItem.shareType = "图片";
//                content = MessageBuilder.createImageMessage(shareItem.sessionId, shareItem.sessionType, new File(path));
//            }
//            messageList.add(content);
//        }
//        String str = stringBuilder.toString();
//        String desc = str.substring(0, str.length() - 1);
//
//        String shareType = shareList.get(0).shareType;
//        String sessionPortrait = shareList.get(0).sessionPortrait;
//
//        createShareDialog(context, desc, shareType, sessionPortrait);
//
//        if(null != mCommDialog) {
//            mCommDialog.setDialogListener(new DialogListener() {
//                @Override
//                public void onCancel() {
//
//                }
//
//                @Override
//                public void onOk() {
//
//                    createProgressDialog(context);
//
//                    for (IMMessage imMessage : messageList) {
//                        if(null == imMessage) {
//                            continue;
//                        }
//                        NIMClient.getService(MsgService.class).sendMessage(imMessage, true)
//                                .setCallback(mRequestCallBack);
//
//                    }
//                }
//            });
//        }
//    }

    /**
     * 创建分享进度dialog
     */
    private void createProgressDialog(@NonNull Context context) {
        MainLoading.Builder builder = new MainLoading.Builder(context);
        builder.setMessage("加载中...");
        mProgressDialog = builder.create();
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    /**
     * 创建分享dialog
     */
    private void createShareDialog(@NonNull Context context, String desc, String shareType, String portrait) {
        mCommDialog = new CommDialog(context);
        mCommDialog.setTitleVisible(true);
        mCommDialog.setTitle("发送给");
        mCommDialog.setSharePortrait(portrait);
        mCommDialog.setDesc(desc);
        mCommDialog.setShareTypeLineVisible(true);
        mCommDialog.setShareTypeTvVisible(true);
        mCommDialog.setShareTypeTv(shareType);
        mCommDialog.setCancelTxt("取消");
        mCommDialog.setOkTxt("确定");
        mCommDialog.setOkTxtColor(DqApp.sContext.getResources().getColor(R.color.color_5495e8));
        mCommDialog.show();
    }

    /**
     * 分享自定义消息到单个会话的dialog
     */
    public CommDialog showSDKShareDialog(@NonNull Context context, @NonNull ShareItemBean shareItem, String title) {
        CommDialog commDialog = createSDKShareDialog(context, shareItem, title);
        return commDialog;
    }
    /**
     * 创建分享dialog
     */
    private CommDialog createSDKShareDialog(@NonNull Context context, ShareItemBean shareItem, String content) {
        mCommDialog = new CommDialog(context);
        mCommDialog.setTitleVisible(true);
        mCommDialog.setTitle("发送给");
        mCommDialog.setSharePortrait(shareItem.sessionPortrait);
        mCommDialog.setDesc(shareItem.sessionName);
        mCommDialog.setShareTypeLineVisible(true);
        mCommDialog.setShareTypeTvVisible(true);
        mCommDialog.setShareTypeTv(content);
        mCommDialog.setCancelTxt("取消");
        if("-1".equals(shareItem.shareType)){
            mCommDialog.setOkTxt(context.getString(R.string.send));
        }else {
            mCommDialog.setOkTxt(context.getString(R.string.share));
        }
        mCommDialog.setOkTxtColor(DqApp.sContext.getResources().getColor(R.color.color_5495e8));
        return mCommDialog;
    }
}
