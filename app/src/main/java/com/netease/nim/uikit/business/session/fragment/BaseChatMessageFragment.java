package com.netease.nim.uikit.business.session.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.bean.im.MessageRedPackageBean;
import com.dq.im.bean.im.MessageVoiceBean;
import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.type.ImType;
import com.dq.im.type.MessageSendType;
import com.dq.im.type.MessageType;
import com.dq.im.util.oss.AliOssUtil;
import com.dq.im.util.oss.SimpleUpLoadListener;
import com.google.gson.Gson;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.wd.daquan.imui.activity.RedPackageDetailsActivity;
import com.wd.daquan.imui.dialog.OpenRedPackageDialog;
import com.wd.daquan.imui.type.RedPackageStatus;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.util.UUID;

/**
 * 聊天基础类
 */
public abstract class BaseChatMessageFragment extends TFragment{
    private Gson gson = new Gson();
    protected Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected void initRecycleView(RecyclerView recyclerView){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * 创建群组消息模型
     * @param messageType 消息类型
     * @param description 内容信息，文本的话就是文本信息，图片是图片地址，诸如此类
     * @param groupId 群组ID
     */
    protected TeamMessageBaseModel createTeamMessage(MessageType messageType, String localPath,  String description, String groupId){
//        MessageTextBean messageTextBean = new MessageTextBean();
//        messageTextBean.setDescription(description);
//        return createTeamMessage(messageType,messageTextBean,groupId);
        TeamMessageBaseModel model = null;
        switch (messageType){
            case PICTURE:
                model = createTeamMessagePhoto(messageType,localPath,description,groupId);
                break;
        }
        return model;
    }
    /**
     * 创建群组消息模型
     * @param messageType 消息类型
     * @param description 内容信息，文本的话就是文本信息，图片是图片地址，诸如此类
     * @param groupId 群组ID
     */
    protected TeamMessageBaseModel createTeamVoiceMessage(MessageType messageType, String localPath,long duration, String description, String groupId){
        TeamMessageBaseModel model = null;
        model = createTeamMessageVoice(messageType,localPath,duration,description,groupId);
        return model;
    }
    /**
     * 创建个人消息模型
     * @param messageType 消息类型
     * @param description 内容信息，文本的话就是文本信息，图片是图片地址，诸如此类
     * @param friendId 好友Id
     */
    protected P2PMessageBaseModel createP2PMessage(MessageType messageType, String localPath, String description, String friendId){

        P2PMessageBaseModel model = null;
        switch (messageType){
            case PICTURE:
                model = createMessagePhoto(messageType,localPath,description,friendId);
                break;
        }
        return model;
    }

    /**
     * 创建个人消息模型
     * @param messageType 消息类型
     * @param description 内容信息，文本的话就是文本信息，图片是图片地址，诸如此类
     * @param friendId 好友Id
     */
    protected P2PMessageBaseModel createP2PVoiceMessage(MessageType messageType, String localPath,long duration, String description, String friendId){

        P2PMessageBaseModel model = null;
        model = createMessageVoice(messageType,localPath,duration,description,friendId);
        return model;
    }

    /**
     * 创建个人消息模型
     * @param messageType 消息类型
     * @param description 内容信息，文本的话就是文本信息，图片是图片地址，诸如此类
     * @param friendId 好友Id
     */
    protected P2PMessageBaseModel createMessagePhoto(MessageType messageType, String localPath, String description, String friendId){
        MessagePhotoBean messageTextBean = new MessagePhotoBean();
        messageTextBean.setDescription(description);
        messageTextBean.setLocalUriString(localPath);
        return createP2PMessage(messageType,messageTextBean,friendId);
    }
    /**
     * 创建个人消息模型
     * @param messageType 消息类型
     * @param description 内容信息，文本的话就是文本信息，图片是图片地址，诸如此类
     * @param friendId 好友Id
     */
    protected P2PMessageBaseModel createMessageVoice(MessageType messageType, String localPath,long duration, String description, String friendId){
        MessageVoiceBean messageVoiceBean = new MessageVoiceBean();
        messageVoiceBean.setDescription(description);
        messageVoiceBean.setLocalUriString(localPath);
        messageVoiceBean.setDuration(duration);
        return createP2PMessage(messageType,messageVoiceBean,friendId);
    }
    /**
     * 创建群组消息模型
     * @param messageType 消息类型
     * @param description 内容信息，文本的话就是文本信息，图片是图片地址，诸如此类
     * @param friendId 好友Id
     */
    protected TeamMessageBaseModel createTeamMessagePhoto(MessageType messageType, String localPath, String description, String friendId){
        MessagePhotoBean messageTextBean = new MessagePhotoBean();
        messageTextBean.setDescription(description);
        messageTextBean.setLocalUriString(localPath);
        return createTeamMessage(messageType,messageTextBean,friendId);
    }
    /**
     * 创建群组消息模型
     * @param messageType 消息类型
     * @param description 内容信息，文本的话就是文本信息，图片是图片地址，诸如此类
     * @param friendId 好友Id
     */
    protected TeamMessageBaseModel createTeamMessageVoice(MessageType messageType, String localPath,long duration, String description, String friendId){
        MessageVoiceBean messageVoiceBean = new MessageVoiceBean();
        messageVoiceBean.setDescription(description);
        messageVoiceBean.setLocalUriString(localPath);
        messageVoiceBean.setDuration(duration);
        return createTeamMessage(messageType,messageVoiceBean,friendId);
    }

    /**
     * 创建群组红包消息模型
     * @param messageType 消息类型
     * @param groupId 好友Id
     */
    protected TeamMessageBaseModel createTeamMessage(MessageType messageType, IMContentDataModel imContentDataModel, String groupId){
        String textContent = gson.toJson(imContentDataModel);
        TeamMessageBaseModel teamMessageBaseModel = new TeamMessageBaseModel();
        teamMessageBaseModel.setSourceContent(textContent);
        teamMessageBaseModel.setType(ImType.Team.getValue());
        teamMessageBaseModel.setMsgType(messageType.getValue());
        teamMessageBaseModel.setFromUserId(ModuleMgr.getCenterMgr().getUID());
        teamMessageBaseModel.setGroupId(groupId);
        teamMessageBaseModel.setToUserId(groupId);
        teamMessageBaseModel.setContentData(imContentDataModel);
        teamMessageBaseModel.setMsgIdClient(UUID.randomUUID().toString());
        teamMessageBaseModel.setCreateTime(System.currentTimeMillis());//消息生成的时间
        teamMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_LOADING.getValue());
        return teamMessageBaseModel;
    }

    /**
     * 创建个人红包消息模型
     * @param messageType 消息类型
     * @param imContentDataModel 内容信息，文本的话就是文本信息，图片是图片地址，诸如此类
     * @param friendId 好友Id
     */
    protected P2PMessageBaseModel createP2PMessage(MessageType messageType, IMContentDataModel imContentDataModel, String friendId){
        String textContent = gson.toJson(imContentDataModel);
        P2PMessageBaseModel p2PMessageBaseModel = new P2PMessageBaseModel();
        p2PMessageBaseModel.setSourceContent(textContent);
        p2PMessageBaseModel.setType(ImType.P2P.getValue());
        p2PMessageBaseModel.setMsgType(messageType.getValue());
        p2PMessageBaseModel.setFromUserId(ModuleMgr.getCenterMgr().getUID());
        p2PMessageBaseModel.setToUserId(friendId);
        p2PMessageBaseModel.setContentData(imContentDataModel);
        p2PMessageBaseModel.setMsgIdClient(UUID.randomUUID().toString());
        p2PMessageBaseModel.setCreateTime(System.currentTimeMillis());//消息生成的时间
        p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_LOADING.getValue());
        return p2PMessageBaseModel;
    }

//    /**
//     * 阿里云上传文件
//     * @param filePath 文件内容
//     * @param fileName
//     * @param messageType
//     */
//    protected void uploadFile(byte[] filePath, String fileName, MessageType messageType){
//
//        // 构造上传请求。
//        PutObjectRequest put = new PutObjectRequest(OssConfig.bucketName, fileName, filePath);
//
//// 异步上传时可以设置进度回调。
//        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
//            @Override
//            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
////                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
//            }
//        });
//
//        OSSAsyncTask task = AliOssUtil.getOss().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
//            @Override
//            public void onSuccess(PutObjectRequest request, PutObjectResult result) {//该接口是异步的，所以返回在子线程中
////                参考链接:https://help.aliyun.com/document_detail/32049.html?spm=5176.10695662.1996646101.searchclickresult.d6217d7bz6CMDi
////                String url = AliOssUtil.getOss().presignConstrainedObjectURL(OssConfig.bucketName, fileName, 30 60);//指定有效时长
//                String url = AliOssUtil.getOss().presignPublicObjectURL(OssConfig.bucketName, fileName);//永久时长
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        sendFile(url,messageType);
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                // 请求异常。
//                if (clientExcepion != null) {
//                    // 本地异常，如网络异常等。
//                    clientExcepion.printStackTrace();
//                }
//                if (serviceException != null) {
//                    // 服务异常。
//                    Log.e("ErrorCode", serviceException.getErrorCode());
//                    Log.e("RequestId", serviceException.getRequestId());
//                    Log.e("HostId", serviceException.getHostId());
//                    Log.e("RawMessage", serviceException.getRawMessage());
//                }
//            }
//        });
//// task.cancel(); // 可以取消任务。
//// task.waitUntilFinished(); // 等待任务完成。
//
//    }
    /**
     * 阿里云上传文件
     * @param fileData 单个文件内容
     * @param fileName
     * @param messageType
     */
    protected void uploadFile(byte[] fileData, String fileName, MessageType messageType){

        AliOssUtil.getInstance().asyncPutObject(fileName, fileData, new SimpleUpLoadListener() {
            @Override
            public void uploadComplete(String url) {
//                uploadFileFail(url,messageType);
            }
        });
//        List<UpLoadBean> upLoadBeans = new ArrayList<>();
//        UpLoadBean upLoadBean = new UpLoadBean();
//        upLoadBean.setFileName(fileName);
//        upLoadBean.setFileData(fileData);
//        upLoadBeans.add(upLoadBean);
//        AliOssUtil.getInstance().putObjectArr(upLoadBeans,new SimpleUpLoadListener(){
//            @Override
//            public void uploadBatchComplete(List<UpLoadBean> upLoadBeans) {
//                super.uploadBatchComplete(upLoadBeans);
//                Log.e("YM","返回的数据内容:"+upLoadBeans.toString());
//            }
//        });
    }
    /**
     * 阿里云上传文件
     * @param fileData 单个文件内容
     * @param fileName
     * @param messageType
     */
    protected void uploadFile(String localString,byte[] fileData, String fileName, MessageType messageType,ImMessageBaseModel imMessageBaseModel){

        AliOssUtil.getInstance().asyncPutObject(fileName, fileData, new SimpleUpLoadListener() {
            @Override
            public void uploadComplete(String url) {
                dealSuccessMessageModel(imMessageBaseModel,url);
                uploadFileSuccess(localString,url,messageType,imMessageBaseModel);
            }

            @Override
            public void uploadFail() {
                super.uploadFail();
                uploadFileFail(localString,messageType,imMessageBaseModel);
            }
        });

//        List<UpLoadBean> upLoadBeans = new ArrayList<>();
//        UpLoadBean upLoadBean = new UpLoadBean();
//        upLoadBean.setFileName(fileName);
//        upLoadBean.setFileData(fileData);
//        upLoadBeans.add(upLoadBean);
//        AliOssUtil.getInstance().putObjectArr(upLoadBeans,new SimpleUpLoadListener(){
//            @Override
//            public void uploadBatchComplete(List<UpLoadBean> upLoadBeans) {
//                super.uploadBatchComplete(upLoadBeans);
//                Log.e("YM","返回的数据内容:"+upLoadBeans.toString());
//            }
//        });
    }

    /**
     * 将返回的数据进行重新更改数据
     * @param imMessageBaseModel
     * @param url
     */
    private ImMessageBaseModel dealSuccessMessageModel(ImMessageBaseModel imMessageBaseModel,String url){
        if (imMessageBaseModel.getType().equals(ImType.P2P.getValue())){//个人
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel)imMessageBaseModel;
            switch (MessageType.typeOfValue(p2PMessageBaseModel.getMsgType())){
                case PICTURE:
                    MessagePhotoBean messagePhotoBean = (MessagePhotoBean)p2PMessageBaseModel.getContentData();
                    messagePhotoBean.setDescription(url);
                    String contentPhoto = gson.toJson(messagePhotoBean);
                    p2PMessageBaseModel.setSourceContent(contentPhoto);
                    p2PMessageBaseModel.setContentData(messagePhotoBean);
                    break;
                case VOICE:
                    MessageVoiceBean messageVoiceBean = (MessageVoiceBean)p2PMessageBaseModel.getContentData();
                    messageVoiceBean.setDescription(url);
                    String contentVoice = gson.toJson(messageVoiceBean);
                    p2PMessageBaseModel.setSourceContent(contentVoice);
                    p2PMessageBaseModel.setContentData(messageVoiceBean);
            }
            return p2PMessageBaseModel;
        }else{//群组
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel)imMessageBaseModel;
            switch (MessageType.typeOfValue(teamMessageBaseModel.getMsgType())){
                case PICTURE:
                    MessagePhotoBean messagePhotoBean = (MessagePhotoBean)teamMessageBaseModel.getContentData();
                    messagePhotoBean.setDescription(url);
                    String contentPhoto = gson.toJson(messagePhotoBean);
                    teamMessageBaseModel.setSourceContent(contentPhoto);
                    teamMessageBaseModel.setContentData(messagePhotoBean);
                    break;
                case VOICE:
                    MessageVoiceBean messageVoiceBean = (MessageVoiceBean)teamMessageBaseModel.getContentData();
                    messageVoiceBean.setDescription(url);
                    String contentVoice = gson.toJson(messageVoiceBean);
                    teamMessageBaseModel.setSourceContent(contentVoice);
                    teamMessageBaseModel.setContentData(messageVoiceBean);
            }
            return teamMessageBaseModel;
        }

    }

    /**
     * 上传文件失败回调
     * @param messageType
     */
    public abstract void uploadFileFail(String localString, MessageType messageType,ImMessageBaseModel imMessageBaseModel);

    /**
     * 上传文件成功回调
     * @param netUrl
     * @param messageType
     */
    public abstract void uploadFileSuccess(String localString, String netUrl, MessageType messageType,ImMessageBaseModel imMessageBaseModel);

    /**
     * 页面类型
     */
    public abstract String pageType();
    /**
     * 红包路由页面
     * @param context
     * @param messageRedPackageBean
     * @param imMessageBaseModel
     */
    protected void redPackageRoutePager(Context context, MessageRedPackageBean messageRedPackageBean, ImMessageBaseModel imMessageBaseModel){
        ImType imType = ImType.typeOfValue(imMessageBaseModel.getType());//消息类型
        String userId = ModuleMgr.getCenterMgr().getUID();
        RedPackageStatus statusContent = RedPackageStatus.getRedPackageStatus(messageRedPackageBean.getStatus());
        if (RedPackageStatus.RED_PACKAGE_UN_RECEIVE != statusContent){//如果红包是不是未领取状态直接进入红包详情页面
            Intent intent = new Intent(context, RedPackageDetailsActivity.class);
            intent.putExtra(RedPackageDetailsActivity.RED_PACKAGE_ID,messageRedPackageBean.getCouponId());
            intent.putExtra(RedPackageDetailsActivity.MESSAGE_DATA,imMessageBaseModel);
            context.startActivity(intent);
            return;
        }
        if (ImType.P2P == imType && userId.equals(imMessageBaseModel.getFromUserId())){//如果是个人模型下发送的红包消息直接进入红包详情页面
            Intent intent = new Intent(context, RedPackageDetailsActivity.class);
            intent.putExtra(RedPackageDetailsActivity.RED_PACKAGE_ID,messageRedPackageBean.getCouponId());
            intent.putExtra(RedPackageDetailsActivity.MESSAGE_DATA,imMessageBaseModel);
            context.startActivity(intent);
        }else {//否则显示打开红包页面
            FragmentActivity fragmentActivity = (FragmentActivity)context;
            OpenRedPackageDialog openRedPackageDialog = new OpenRedPackageDialog();
            Bundle bundle = new Bundle();
            bundle.putSerializable(OpenRedPackageDialog.RED_PACKAGE_DATA,imMessageBaseModel);
            openRedPackageDialog.setArguments(bundle);
            openRedPackageDialog.show(getChildFragmentManager(),"");
        }
    }
}