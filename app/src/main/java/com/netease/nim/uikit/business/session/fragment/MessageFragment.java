package com.netease.nim.uikit.business.session.fragment;

import android.app.Activity;

import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dq.im.bean.MediaExtraBean;
import com.dq.im.bean.im.MessageCardBean;
import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.bean.im.MessageRedPackageBean;
import com.dq.im.bean.im.MessageTextBean;
import com.dq.im.bean.im.MessageVideoBean;
import com.dq.im.bean.im.MessageVoiceBean;
import com.dq.im.constants.Constants;
import com.dq.im.constants.URLUtil;
import com.dq.im.model.HomeImBaseMode;
import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.parser.ImTransformUtils;
import com.dq.im.type.ImType;
import com.dq.im.type.MessageSendType;
import com.dq.im.type.MessageType;
import com.dq.im.util.MediaUtils;
import com.dq.im.util.SoftKeyBoardListener;
import com.dq.im.util.oss.AliOssUtil;
import com.dq.im.util.oss.SimpleUpLoadListener;
import com.dq.im.util.oss.UpLoadBean;
import com.dq.im.viewmodel.HomeMessageViewModel;
import com.dq.im.viewmodel.P2PMessageViewModel;
import com.google.gson.Gson;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.business.ait.AitManager;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.module.Container;
import com.netease.nim.uikit.business.session.module.ModuleProxy;
import com.netease.nim.uikit.business.session.module.input.InputPanel;
import com.netease.nim.uikit.common.util.sys.ClipboardUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.wd.daquan.R;
import com.wd.daquan.bean.ImSendMessageResultBean;
import com.wd.daquan.http.HttpBaseBean;
import com.wd.daquan.http.HttpResultResultCallBack;
import com.wd.daquan.http.ImSdkHttpUtils;
import com.wd.daquan.http.SocketMessageUtil;
import com.wd.daquan.imui.activity.MediaDetailsActivity;
import com.wd.daquan.imui.adapter.ChatP2PAdapter;
import com.wd.daquan.imui.adapter.RecycleItemOnClickForChildViewListenerCompat;
import com.wd.daquan.imui.adapter.RecycleItemOnClickListener;
import com.wd.daquan.imui.adapter.RecycleItemOnLongClickListener;
import com.wd.daquan.imui.bean.ChatOfSystemMessageBean;
import com.wd.daquan.imui.bean.VoiceBean;
import com.wd.daquan.imui.bean.im.DqImBaseBean;
import com.wd.daquan.imui.convert.CommonImConvertDqIm;
import com.wd.daquan.imui.convert.DqImParserUtils;
import com.wd.daquan.imui.dialog.MessageOptionDialog;
import com.wd.daquan.imui.dialog.OpenRedPackageDialog;
import com.wd.daquan.imui.type.MsgSecondType;
import com.wd.daquan.imui.type.RedPackageStatus;
import com.wd.daquan.imui.type.chat_layout.ChatLayoutChildType;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.util.AESUtil;
import com.wd.daquan.util.FileUtils;
import com.wd.daquan.util.TToast;
import com.wd.daquan.util.message_manager.SocketMessageManager;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 个人聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class MessageFragment extends BaseChatMessageFragment implements ModuleProxy, QCObserver {
    private SwipeRefreshLayout chatContainer;
    private View rootView;

    private SessionCustomization customization;
    private IMContentDataModel sendContent;
    protected static final String TAG = "MessageActivity";

    // 聊天对象
    protected String sessionId; // p2p对方Account或者群id
    private int pageSize = 20;//数据条数
    protected ImType sessionType;

    // modules
    protected InputPanel inputPanel;
//    protected MessageListPanelEx messageListPanel;

    protected AitManager aitManager;
    private Gson gson = new Gson();

    private P2PMessageViewModel p2PMessageViewModel;
    private HomeMessageViewModel homeMessageViewModel;
    public ImType getSessionType() {
        return sessionType;
    }
    private ChatP2PAdapter chatP2PAdapter;//聊天适配器

    private RecyclerView chatMessageRv;
    private long createTime = System.currentTimeMillis();//上一条消息的时间
    private boolean isFirstLoadData = true;//是不是第一次加载数据，第一次加载数据会滚动到底部，下拉刷新则不会
    private long timestampDiff = 24 * 60 * 60 * 1000;//时间差值，因为服务器的消息时间会跟当前系统时间有偏差，会晚于当前系统时间，所以在当前系统时间后面延迟一天
    private CommonImConvertDqIm commonImConvertDqIm = new CommonImConvertDqIm();
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MsgMgr.getInstance().attach(this);
    }

    @Override
    protected TeamMessageBaseModel createTeamMessage(MessageType messageType, String localPath, String description, String groupId) {
        return super.createTeamMessage(messageType, localPath, description, groupId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.nim_message_fragment, container, false);
        return rootView;
    }

    @Override
    public void onAttachFragment(@NotNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof OpenRedPackageDialog){
            OpenRedPackageDialog openRedPackageDialog = (OpenRedPackageDialog) childFragment;
            openRedPackageDialog.setOpenRedPackageResultListener(new OpenRedPackageResult());
        }else if (childFragment instanceof MessageOptionDialog){
            MessageOptionDialog messageOptionDialog = (MessageOptionDialog) childFragment;
            messageOptionDialog.setMessageOptionIpc(new MessageOption());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setChatBG(rootView);
        parseIntent();
        initView(view);
        initChatMessageData();
        queryLocalAllMessage(System.currentTimeMillis()+timestampDiff);
        sendThirdMessage();
        onKeyBoardListener();
    }
    private void sendThirdMessage(){
        if (sendContent instanceof MessagePhotoBean){
            Log.e("YM","图片消息");
            MessagePhotoBean messagePhotoBean = (MessagePhotoBean)sendContent;
            Uri uri = Uri.parse(messagePhotoBean.getLocalUriString());
            uploadPhoto(uri);
        }else if (sendContent instanceof MessageTextBean){
            Log.e("YM","文本消息");
        }
    }
    /**
     * ***************************** life cycle *******************************
     */

    @Override
    public void onPause() {
        super.onPause();
        inputPanel.onPause();
//        messageListPanel.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
//        messageListPanel.onResume();
        getActivity().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL); // 默认使用听筒播放
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        messageListPanel.onDestroy();
        if (inputPanel != null) {
            inputPanel.onDestroy();
        }
        if (aitManager != null) {
            aitManager.reset();
        }
        MsgMgr.getInstance().detach(this);
    }
    //监听软件盘是否弹起
    private void onKeyBoardListener() {
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                if (null != chatMessageRv){
                    chatMessageRv.scrollToPosition(chatP2PAdapter.getData().size() - 1);
                }
            }

            @Override
            public void keyBoardHide(int height) {
            }
        });
    }

    private void initView(View view){
        chatMessageRv = view.findViewById(R.id.messageListView);
        chatContainer = view.findViewById(R.id.chat_container);
        chatContainer.setOnRefreshListener(() -> {
//                requestNetMessageHistory();
            long time;
            if (null == chatP2PAdapter.getData() || chatP2PAdapter.getData().size() == 0){//假如列表为空，则用当前时间
                createTime = System.currentTimeMillis();//当前时间
                time = createTime;
            }else {//假如列表不为空，则用第一条数据的时间
                List<P2PMessageBaseModel> baseModels = chatP2PAdapter.getData();
                time = baseModels.get(0).getCreateTime();
            }

            queryLocalAllMessage(time);
        });
        initRecycleView(chatMessageRv);
    }

    private void initChatMessageData(){
        chatP2PAdapter = new ChatP2PAdapter(this);
        chatP2PAdapter.setRecycleItemOnClickListener(new RecycleItemOnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                P2PMessageBaseModel model = chatP2PAdapter.getData(position);
                int type = chatP2PAdapter.getItemViewType(position);
                if (ChatLayoutChildType.LEFT_RED_PACKAGE == type){//左侧红包
                    String content = model.getSourceContent();
                    MessageRedPackageBean messageRedPackageBean = gson.fromJson(content,MessageRedPackageBean.class);
                    redPackageRoutePager(view.getContext(),messageRedPackageBean,model);
                }else if (ChatLayoutChildType.RIGHT_RED_PACKAGE == type){//右侧红包
                    String content = model.getSourceContent();
                    MessageRedPackageBean messageRedPackageBean = gson.fromJson(content,MessageRedPackageBean.class);
                    redPackageRoutePager(view.getContext(),messageRedPackageBean,model);
                }
                Log.e("YM","点击文件类型:"+model.getMsgType());
                if (MessageType.PICTURE.getValue().equals(model.getMsgType())){//假如是图片消息或者视频消息的时候
                    ArrayList<P2PMessageBaseModel> temp = (ArrayList<P2PMessageBaseModel>)chatP2PAdapter.getData();
                    Intent intent = new Intent(view.getContext(), MediaDetailsActivity.class);
                    intent.putExtra(MediaDetailsActivity.MEDIA_DATA,temp);
                    intent.putExtra(MediaDetailsActivity.MEDIA_DATA_CURRENT,position);
                    startActivity(intent);
                }

                if (MessageType.VIDEO.getValue().equals(model.getMsgType())){//假如是图片消息或者视频消息的时候
                    ArrayList<P2PMessageBaseModel> temp = (ArrayList<P2PMessageBaseModel>)chatP2PAdapter.getData();
                    Intent intent = new Intent(view.getContext(), MediaDetailsActivity.class);
                    intent.putExtra(MediaDetailsActivity.MEDIA_DATA,temp);
                    intent.putExtra(MediaDetailsActivity.MEDIA_DATA_CURRENT,position);
                    startActivity(intent);
                }

            }
        });
        chatP2PAdapter.setRecycleItemOnLongClickListener(new RecycleItemOnLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                MessageOptionDialog messageOptionDialog = new MessageOptionDialog();
                P2PMessageBaseModel model = chatP2PAdapter.getData(position);
                if (model.getMsgType().equals(MessageType.TEXT.getValue())){//文本消息
                    String content = model.getSourceContent();
                    MessageTextBean messageTextBean = gson.fromJson(content,MessageTextBean.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(MessageOptionDialog.MESSAGE_OPTION_KEY,messageTextBean.getDescription());
                    messageOptionDialog.setArguments(bundle);
                    messageOptionDialog.show(getChildFragmentManager(),"messageOption");
                }
                return false;
            }
        });
        RecycleItemOnClickForChildViewListenerCompat.setRecycleItemOnClickForChildViewListener(new RecycleItemOnClickForChildViewListenerCompat.SimpleChildViewListener() {
            @Override
            public void onItemForChildClick(View view,ImMessageBaseModel imMessageBaseModel) {
                P2PMessageBaseModel model = (P2PMessageBaseModel)imMessageBaseModel;
                switch (view.getId()){
                    case R.id.message_fail:
                        Toast.makeText(view.getContext(),"消息发送失败",Toast.LENGTH_SHORT).show();
                        sendFailMessage(model);
                        break;
                }
            }
        });
        chatMessageRv.setAdapter(chatP2PAdapter);
    }

    private void sendFailMessage(P2PMessageBaseModel model){
        removeMessage(model);
        String content = model.getSourceContent();
        //附加文件消息和非文件消息要分开处理，对于文件消息，这里会进行再次上传
        //重新发送消息的话需要把之前的消息删除掉，然后重新生成新的
        MessageType messageType = MessageType.typeOfValue(model.getMsgType());
        IMContentDataModel contentDataModel = gson.fromJson(content,MessageType.getClassByType(model.getMsgType()));
        switch (messageType){
            case TEXT:
                sendMessage(contentDataModel,MessageType.typeOfValue(model.getMsgType()));
                break;
            case PICTURE:
                MessagePhotoBean messagePhotoBean = (MessagePhotoBean) contentDataModel;
                Uri photoUri = Uri.parse(messagePhotoBean.getLocalUriString());
                uploadPhoto(photoUri);
                break;
            case VOICE:
                MessageVoiceBean messageVoiceBean = (MessageVoiceBean) contentDataModel;
                uploadVoice(messageVoiceBean.getLocalUriString(),messageVoiceBean.getDuration());
                break;
            case VIDEO:
                MessageVideoBean messageVideoBean = (MessageVideoBean) contentDataModel;
                Uri videoUri = Uri.parse(messageVideoBean.getVideoLocalPath());
                uploadVideo(videoUri);
                break;
            case RED_PACKAGE:
                MessageRedPackageBean messageRedPackageBean = (MessageRedPackageBean) contentDataModel;
                sendMessage(messageRedPackageBean,MessageType.RED_PACKAGE);
                break;
            case PERSON_CARD:
                MessageCardBean messageCardBean = (MessageCardBean) contentDataModel;
                sendMessage(messageCardBean,MessageType.PERSON_CARD);
                break;
        }

    }

    /**
     * 本地消息ID
     */
    private void removeMessage(P2PMessageBaseModel model){
        Log.e("YM","删除的文本id:"+model.getMsgIdClient());
        p2PMessageViewModel.deleteForMessageClientId(model.getMsgIdClient());
        chatP2PAdapter.removeData(model);
    }

    public boolean onBackPressed() {
        if (inputPanel.collapse(true)) {
            return true;
        }

//        if (messageListPanel.onBackPressed()) {
//            return true;
//        }
        return false;
    }

    public void refreshMessageList() {
//        if(messageListPanel != null) {
//            messageListPanel.refreshMessageList();
//        }
    }

    private void parseIntent() {
        sessionId = getArguments().getString(Extras.EXTRA_ACCOUNT);

        sessionType = (ImType) getArguments().getSerializable(Extras.EXTRA_TYPE);
//        IMMessage anchor = (IMMessage) getArguments().getSerializable(Extras.EXTRA_ANCHOR);

        customization = (SessionCustomization) getArguments().getSerializable(Extras.EXTRA_CUSTOMIZATION);
        sendContent = (IMContentDataModel) getArguments().getSerializable(Extras.EXTRA_IM_CONTENT);
        Container container = new Container(this, sessionId, sessionType, this);

//        if (messageListPanel == null) {
//            messageListPanel = new MessageListPanelEx(container, rootView, anchor, false, false);
//        } else {
//            messageListPanel.reload(container, anchor);
//        }

        if (inputPanel == null) {
            inputPanel = new InputPanel(container, rootView, getActionList());
            inputPanel.setCustomization(customization);
        } else {
            inputPanel.reload(container, customization);
        }
        initData();
        initAitManager();

//        inputPanel.switchRobotMode(NimUIKitImpl.getRobotInfoProvider().getRobotByAccount(sessionId) != null);
        inputPanel.switchRobotMode(false);

//        if (customization != null) {
//            messageListPanel.setChattingBackground(customization.backgroundUri, customization.backgroundColor);
//        }
        commonImConvertDqIm.setUserId(ModuleMgr.getCenterMgr().getUID());
        commonImConvertDqIm.setToken(ModuleMgr.getCenterMgr().getToken());
    }

    private void initAitManager() {
        UIKitOptions options = NimUIKitImpl.getOptions();
        if (options.aitEnable) {
            aitManager = new AitManager(getContext(), options.aitTeamMember && sessionType == ImType.Team ? sessionId : null, options.aitIMRobot);
            inputPanel.addAitTextWatcher(aitManager);
            aitManager.setTextChangeListener(inputPanel);
        }
    }
    private void initData(){
        Log.e("YM","好友的sessionId:"+sessionId);
        Log.e("YM","自己的sessionId:"+ModuleMgr.getCenterMgr().getUID());
        p2PMessageViewModel = ViewModelProviders.of(this).get(P2PMessageViewModel.class);
        homeMessageViewModel = ViewModelProviders.of(this).get(HomeMessageViewModel.class);
        homeMessageViewModel.updateTeFriendUnReadNumber(sessionId,0);
    }


    /**
     * ************************* 消息收发 **********************************
     */

    /**
     * ********************** implements ModuleProxy *********************
     */
    @Override
    public boolean sendMessage(IMContentDataModel message) {

        if (message instanceof MessageTextBean){
            MessageTextBean messageTextBean = (MessageTextBean)message;
            sendMessage(messageTextBean,MessageType.TEXT);
        }else if (message instanceof MessageCardBean){
            MessageCardBean messageCardBean = (MessageCardBean)message;
            sendMessage(messageCardBean,MessageType.PERSON_CARD);
        }

//        switch (message.getMsgType()){
//            case text:
//                Log.e("YM","文本消息");
//                String textContent = message.getContent();
//                MessageTextBean messageTextBean = new MessageTextBean();
//                messageTextBean.setDescription(textContent);
//                sendMessage(messageTextBean,MessageType.TEXT);
//                break;
//            case custom:
//                Log.e("YM","自定义消息");
//                MsgAttachment msgAttachment = message.getAttachment();
//                if (msgAttachment instanceof CardAttachment){//名片消息
//                    CardAttachment cardAttachment = (CardAttachment) msgAttachment;
//                    MessageCardBean cardBean = new MessageCardBean();
//                    cardBean.setExtra(cardAttachment.extra);
//                    cardBean.setHeadPic(cardAttachment.headPic);
//                    cardBean.setNickName(cardAttachment.nickName);
//                    cardBean.setUserId(cardAttachment.userId);
//                    sendMessage(cardBean,MessageType.PERSON_CARD);
//                }
//                break;
//        }


//        messageListPanel.onMsgSend(message);

        if (aitManager != null) {
            aitManager.reset();
        }
        return true;
    }

    @Override
    public void onInputPanelExpand() {
//        messageListPanel.scrollToBottom();
    }

    @Override
    public void shouldCollapseInputPanel() {
        inputPanel.collapse(false);
    }

    @Override
    public boolean isLongClickEnabled() {
        return true;
    }

//    /**
//     * 显示长按头像
//     * @param context
//     * @param content
//     */
//    private void showAvatarDialog(Context context, IMMessage content) {
//        AvatarClickedDialog mAvatarDialog = new AvatarClickedDialog((Activity) context);
//        mAvatarDialog.setListener(new AvatarClickedDialog.MessageLongListener() {
//            @Override
//            public void onAit(IMMessage content) {
//                if (content == null) {
//                    return;
//                }
//
//                if (Utils.isFastDoubleClick(500)) {
//                    return;
//                }
//
//                Friend friend = DbMgr.getDbMgr().getUserInfo(content.getFromAccount());
//                if (aitManager != null && friend != null) {
//                    aitManager.insertAitMemberInner(friend);
//                } else {
//                    QcToastUtil.makeText(context, "@成员出错");
//                }
//            }
//
//            @Override
//            public void onAlipay(IMMessage content) {
//                if (content == null) {
//                    return;
//                }
//
//                if (Utils.isFastDoubleClick(500)) {
//                    return;
//                }
//
//                RedPacketAuthHelper mAuthHelper = new RedPacketAuthHelper((Activity) context, sessionId);
//                mAuthHelper.setFromAccount(content.getFromAccount());
//                mAuthHelper.setFromName(content.getFromNick());
//                mAuthHelper.request();
//            }
//        });
//        mAvatarDialog.show(content);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (aitManager != null) {
            aitManager.onActivityResult(requestCode, resultCode, data);
        }
        inputPanel.onActivityResult(requestCode, resultCode, data);
//        messageListPanel.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 操作面板集合
     *  直接取自定义面板
     * @return
     */
    protected List<BaseAction> getActionList() {
        List<BaseAction> actions = new ArrayList<>();
        if (customization != null && customization.actions != null) {
            actions.addAll(customization.actions);
        }
        return actions;
    }

    @Override
    public void onMessage(String key, Object value) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (MsgType.P2P_MESSAGE_CONTENT.equals(key)){//个人消息
            Log.e("YM","收到消息:");
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) value;
            parserOtherMsg(p2PMessageBaseModel);//处理对方消息
            if (MessageType.SYSTEM.getValue().equals(p2PMessageBaseModel.getMsgType())){//不插入数据库，因为系统消息没有发消息用户的Id
                return;
            }
            if (!sessionId.equals(p2PMessageBaseModel.getFromUserId())){//假如不是当前好友的消息,则不处理。因为首页收到个人消息后都会传到这个页面
                return;
            }
            p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_SUCCESS.getValue());
            homeMessageViewModel.updateTeFriendUnReadNumber(sessionId,0);//消息从ImMainActivity传入，所以先设置未读消息数，进入里面再重置为0
            saveMsgSuccess(p2PMessageBaseModel);
        }else if(MsgType.MESSAGE_RECEIVE_CALL_BACK.equals(key)){//消息发送后，服务器把消息回传给客户端的内容
            ImMessageBaseModel imMessageBaseModel = (ImMessageBaseModel)value;
            SocketMessageManager.getInstance().removeMessage(imMessageBaseModel.getMsgIdClient());
            for (P2PMessageBaseModel messageBaseModel : chatP2PAdapter.getData()){
                if (imMessageBaseModel.getMsgIdClient().equals(messageBaseModel.getMsgIdClient())){
                    messageBaseModel.setMsgIdServer(imMessageBaseModel.getMsgIdServer());
                    messageBaseModel.setSourceContent(imMessageBaseModel.getSourceContent());
                    messageBaseModel.setMessageSendStatus(imMessageBaseModel.getMessageSendStatus());
                    p2PMessageViewModel.updateP2PMessageByClientId(messageBaseModel);
                    chatP2PAdapter.updateMessageStatus(messageBaseModel);
                    Log.e("YM","更新消息发送状态");
                    return;
                }
            }
        }else if (MsgType.MESSAGE_RECEIVE_CALL_BACK_TIMEOUT.equals(key)){
            String clientId = value.toString();
            for (P2PMessageBaseModel messageBaseModel : chatP2PAdapter.getData()){
                if (clientId.equals(messageBaseModel.getMsgIdClient())){
                    messageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
                    p2PMessageViewModel.updateP2PMessageByClientId(messageBaseModel);
                    chatP2PAdapter.updateMessageStatus(messageBaseModel);
                    Log.e("YM","更新消息发送状态为失败");
                    return;
                }
            }
        }else if (MsgType.TEAM_MESSAGE_CONTENT.equals(key)){//群组消息

        }else if (MsgType.CHAT_RED_PACKAGE.equals(key)){//发送红包的消息

            inputPanel.switchRobotMode(false);
            inputPanel.collapse(false);
            MessageRedPackageBean messageRedPackageBean = (MessageRedPackageBean) value;
            sendMessage(messageRedPackageBean,MessageType.RED_PACKAGE);
        }else if (MsgType.CHAT_PICTURE.equals(key)){
            List<Uri> picturePath = (List<Uri>) value;
            for (Uri uri : picturePath){
                uploadPhoto(uri);
            }
        }else if (MsgType.CHAT_VIDEO.equals(key)){
            Log.e("YM","Fragment收到的消息");
            Uri uri = (Uri) value;
            uploadVideo(uri);
        }else if (MsgType.CHAT_VOICE.equals(key)){
            VoiceBean voiceBean = (VoiceBean) value;
            Log.e("YM","Fragment收到的音频消息:"+voiceBean.toString());
            uploadVoice(voiceBean.localString,voiceBean.voiceDuration);
        }else if (MsgType.CLEAR_MSG.equals(key)){
            getActivity().finish();
        }
    }

    private void uploadPhoto(Uri uri){
        DocumentFile documentFile = DocumentFile.fromSingleUri(getContext(), uri);
        Log.e("YM","上传的的图片的名字:"+documentFile.getName());
        Log.e("YM","上传的的图片的路径:"+uri.toString());
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            byte[] photoData = FileUtils.toByteArray(inputStream);
            P2PMessageBaseModel p2PMessageBaseModel = createP2PMessage(MessageType.PICTURE,uri.toString(),"",sessionId);
            saveMsgSuccess(p2PMessageBaseModel);
            uploadFile(uri.toString(),photoData,Constants.getImgName().concat(FileUtils.getFileSuffix(documentFile.getName())),MessageType.PICTURE,p2PMessageBaseModel);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private void uploadVoice(String localPath,long duration){
        File file = new File(localPath);
        Log.e("YM","上传的音频文件名字:"+file.getName());
        try {
            byte[] audioData = FileUtils.toByteArray(localPath);
//                        uploadFile(audioData,audioAttachment.getFileName(), MessageType.VOICE);
            P2PMessageBaseModel p2PMessageBaseModel = createP2PVoiceMessage(MessageType.VOICE,localPath,duration,"",sessionId);
            saveMsgSuccess(p2PMessageBaseModel);
            uploadFile(localPath,audioData,file.getName(),MessageType.VOICE,p2PMessageBaseModel);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void uploadVideo(Uri uri){
        MediaUtils mediaUtils = new MediaUtils();
        MediaExtraBean mediaExtraBean = mediaUtils.getRingDuring(uri);
//        Uri thumbUri = Uri.parse(mediaExtraBean.getImagePath());
        DocumentFile videoDocumentFile = DocumentFile.fromSingleUri(getContext(), uri);
        DocumentFile thumbDocumentFile = DocumentFile.fromSingleUri(getContext(), mediaExtraBean.getThumbPath());
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            byte[] videoData = new byte[0];
            try {
                videoData = FileUtils.toByteArray(inputStream);
            }catch (OutOfMemoryError error){
                error.printStackTrace();
                TToast.show(getContext(),"视频文件过大，请选择低于50兆的视频资源!");
                return;
            }

            inputStream = getContext().getContentResolver().openInputStream(mediaExtraBean.getThumbPath());
            byte[] thumbData = FileUtils.toByteArray(inputStream);
//                uploadFile(photoData,documentFile.getName(),MessageType.VIDEO);
            List<UpLoadBean> upLoadBeans = new ArrayList<>();
            UpLoadBean videoBean = new UpLoadBean();
            videoBean.setType(1);
            videoBean.setFileName(Constants.getVideoName().concat(FileUtils.getFileSuffix(videoDocumentFile.getName())));
            videoBean.setFileData(videoData);
            videoBean.setLocalPath(uri.toString());
            upLoadBeans.add(videoBean);//加载视频信息
            UpLoadBean thumbBean = new UpLoadBean();
            thumbBean.setType(2);
            thumbBean.setFileName(Constants.getImgName().concat(FileUtils.getFileSuffix(thumbDocumentFile.getName())));
            thumbBean.setFileData(thumbData);
            thumbBean.setLocalPath(mediaExtraBean.getThumbPath().toString());
            upLoadBeans.add(thumbBean);//加载缩略图信息
            inputStream.close();

            MessageVideoBean messageVideoBean = new MessageVideoBean();
            messageVideoBean.setVideoLocalPath(uri.toString());
            messageVideoBean.setThumbLocalPath(mediaExtraBean.getThumbPath().toString());
            P2PMessageBaseModel p2PMessageBaseModel = createP2PMessage(MessageType.VIDEO,messageVideoBean,sessionId);
            saveMsgSuccess(p2PMessageBaseModel);

            AliOssUtil.getInstance().putObjectArr(upLoadBeans,new SimpleUpLoadListener(){
                @Override
                public void uploadBatchComplete(List<UpLoadBean> upLoadBeans) {
                    super.uploadBatchComplete(upLoadBeans);
//                    MessageVideoBean videoBean = new MessageVideoBean();
                    String videoUrl = "";
                    String thumbUrl = "";
                    String localVideoPath = "";
                    String localThumbPath = "";
                    for (UpLoadBean upLoadBean : upLoadBeans){
                        if (1 == upLoadBean.getType()){//视频
                            videoUrl = upLoadBean.getNetUrl();
                            localVideoPath = upLoadBean.getLocalPath();
                        }else if (2 == upLoadBean.getType()){//缩略图信息
                            thumbUrl = upLoadBean.getNetUrl();
                            localThumbPath = upLoadBean.getLocalPath();
                        }
                    }
//                    messageVideoBean.setVideoLocalPath(localVideoPath);
//                    messageVideoBean.setThumbLocalPath(localThumbPath);
                    messageVideoBean.setVideoPath(videoUrl);
                    messageVideoBean.setThumbPath(thumbUrl);
                    Log.e("YM","上传的视频包含的信息:"+messageVideoBean.toString());
                    p2PMessageBaseModel.setContentData(messageVideoBean);
                    p2PMessageBaseModel.setSourceContent(gson.toJson(messageVideoBean));
                    uploadVideoSuccess(p2PMessageBaseModel);
                }

                @Override
                public void uploadBatchFail(List<UpLoadBean> upLoadBeans) {
                    Log.e("YM","上传的视频包含的信息:"+videoBean.toString());
                    uploadVideoFail(p2PMessageBaseModel);
                }
            });
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //设置聊天背景
    private void setChatBG(View view){
        String filePath = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo().getString(EBSharedPrefUser.filePath,"");
        RecyclerView messageListView = view.findViewById(R.id.messageListView);
        if (!TextUtils.isEmpty(filePath)) {
            if (filePath.length() > 5) {
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                Drawable drawable = new BitmapDrawable(bitmap);
                messageListView.setBackground(drawable);
            } else {
                switch (filePath) {
                    case "1":
                        messageListView.setBackgroundColor(this.getResources().getColor(R.color.app_theme_color));
                        break;
                }
            }
        } else {
            messageListView.setBackgroundColor(this.getResources().getColor(R.color.app_theme_color));
        }
    }

    /**
     * 视频文件上传失败
     */
    public void uploadVideoFail(P2PMessageBaseModel p2PMessageBaseModel){
//        P2PMessageBaseModel p2PMessageBaseModel = createP2PMessage(messageType,imContentDataModel,sessionId);
        p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
//        saveMsgSuccess(p2PMessageBaseModel);
        updateMsgStatus(p2PMessageBaseModel);
    }

    /**
     * 视频文件上传成功
     */
    public void uploadVideoSuccess(P2PMessageBaseModel p2PMessageBaseModel) {
//        P2PMessageBaseModel p2PMessageBaseModel = createP2PMessage(messageType,imContentDataModel,sessionId);
//        saveMsgSuccess(p2PMessageBaseModel);
        DqImBaseBean dqImBaseBean = commonImConvertDqIm.commonImConvertDqIm(p2PMessageBaseModel);
        boolean isSuccess = SocketMessageUtil.sendMessage(dqImBaseBean);
        if (!isSuccess){
            p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
            updateMsgStatus(p2PMessageBaseModel);
        }
//        ImSdkHttpUtils.postJson(URLUtil.USER_SEND_MSG,p2PMessageBaseModel,new HttpResultResultCallBack<HttpBaseBean>() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                Log.e("YM","链接问题:"+e.getMessage());
//                e.printStackTrace();
//                p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
////                saveMsgSuccess(p2PMessageBaseModel);
//                updateMsgStatus(p2PMessageBaseModel);
//            }
//
//            @Override
//            public void onResponse(HttpBaseBean response, int id) {
//                int status = response.status;
//                String msgServerId = response.data.toString();
//                if (0 == status){
//                    p2PMessageBaseModel.setMsgIdServer(msgServerId);
//                    p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_SUCCESS.getValue());
//                }else {
//                    Toast.makeText(getContext(),"消息发送失败:"+response.msg,Toast.LENGTH_SHORT).show();
//                    p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
//                }
//                updateMsgStatus(p2PMessageBaseModel);
//            }
//        });
    }

    @Override
    public void uploadFileFail(String localPath, MessageType messageType,ImMessageBaseModel imMessageBaseModel) {
//        P2PMessageBaseModel p2PMessageBaseModel = createMessage(messageType,localPath,"",sessionId);
        P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel)imMessageBaseModel;
        p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
        saveMsgSuccess(p2PMessageBaseModel);
    }

    @Override
    public void uploadFileSuccess(String localPath, String netUrl, MessageType messageType,ImMessageBaseModel imMessageBaseModel) {
//        P2PMessageBaseModel p2PMessageBaseModel = createMessage(messageType,localPath,netUrl,sessionId);
        P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel)imMessageBaseModel;
//        saveMsgSuccess(p2PMessageBaseModel);
        DqImBaseBean dqImBaseBean = commonImConvertDqIm.commonImConvertDqIm(p2PMessageBaseModel);
        boolean isSuccess = SocketMessageUtil.sendMessage(dqImBaseBean);
        if (!isSuccess){
            p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
            updateMsgStatus(p2PMessageBaseModel);
        }
//        ImSdkHttpUtils.postJson(URLUtil.USER_SEND_MSG,p2PMessageBaseModel,new HttpResultResultCallBack<HttpBaseBean>() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                Log.e("YM","链接问题:"+e.getMessage());
//                e.printStackTrace();
//                p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
////                saveMsgSuccess(p2PMessageBaseModel);
//                updateMsgStatus(p2PMessageBaseModel);
//            }
//
//            @Override
//            public void onResponse(HttpBaseBean response, int id) {
//                int status = response.status;
//                String msgServerId = response.data.toString();
//                if (0 == status){
//                    p2PMessageBaseModel.setMsgIdServer(msgServerId);
//                    p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_SUCCESS.getValue());
//                }else {
//                    Toast.makeText(getContext(),"消息发送失败:"+response.msg,Toast.LENGTH_SHORT).show();
//                    p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
//                }
//                updateMsgStatus(p2PMessageBaseModel);
//            }
//        });
    }

    @Override
    public String pageType() {
        return ImType.P2P.getValue();
    }

    /**
     * 接收到对方消息时候进行处理,比如对方已读这些操作
     */
    private void parserOtherMsg(P2PMessageBaseModel messageBaseModel){
        Log.e("YM","收到消息:"+messageBaseModel.toString());
        MessageType messageType = MessageType.typeOfValue(messageBaseModel.getMsgType());
        if (MessageType.SYSTEM == messageType){//假如是系统消息
            Log.e("YM","系统消息------->");
            MsgSecondType messageSendType = MsgSecondType.getMsgSecondTypeByValue(messageBaseModel.getMsgSecondType());
            switch (messageSendType){
                case MSG_SECOND_TYPE_RED_COMPLETE:
                    String content = messageBaseModel.getSourceContent();
                    ChatOfSystemMessageBean chatOfSystemMessageBean = gson.fromJson(content,ChatOfSystemMessageBean.class);
                    String couponId = chatOfSystemMessageBean.getCouponId();//红包Id
                    parserRedPackageOpened(couponId);
                    break;
            }
        }
    }

    /**
     * 根据红包Id，更改红包消息打开状态
     */
    private void parserRedPackageOpened(String couponId){
        List<P2PMessageBaseModel> messageBaseModels = chatP2PAdapter.getData();
        for (P2PMessageBaseModel model : messageBaseModels){
            MessageType messageType = MessageType.typeOfValue(model.getMsgType());
            if (messageType == MessageType.RED_PACKAGE){//红包消息
                String content = model.getSourceContent();
                MessageRedPackageBean messageTextBean = gson.fromJson(content,MessageRedPackageBean.class);
                if (couponId.equals(messageTextBean.getCouponId())){//假如红包ID相同的话，则改变该状态
                    messageTextBean.setStatus(RedPackageStatus.RED_PACKAGE_RECEIVED.status);
                    model.setContentData(messageTextBean);
                    model.setSourceContent(gson.toJson(messageTextBean));
                    updateMsgStatus(model);
                    break;
                }
            }
        }
    }

    /**
     * 消息发送后的状态
     */
    private void saveMsgSuccess(P2PMessageBaseModel messageBaseModel){
        p2PMessageViewModel.insert(messageBaseModel);
        HomeImBaseMode homeImBaseMode = ImTransformUtils.p2pImModelTransformHomeImModel(messageBaseModel);
        homeMessageViewModel.updateHomeMessage(homeImBaseMode,false);
        chatP2PAdapter.addData(messageBaseModel);
        chatMessageRv.scrollToPosition(chatP2PAdapter.getData().size() - 1);
    }

    /**
     * 更改发送结果
     * @param messageBaseModel
     */
    private void updateMsgStatus(P2PMessageBaseModel messageBaseModel){
        p2PMessageViewModel.insert(messageBaseModel);
        HomeImBaseMode homeImBaseMode = ImTransformUtils.p2pImModelTransformHomeImModel(messageBaseModel);
        homeMessageViewModel.updateHomeMessage(homeImBaseMode,false);
        chatP2PAdapter.updateMessageStatus(messageBaseModel);
        Log.e("YM","更改状态后的个人内容:"+messageBaseModel.toString());
        Log.e("YM","更改状态后的首页个人内容:"+homeImBaseMode.toString());
//        chatMessageRv.scrollToPosition(chatP2PAdapter.getData().size() - 1);
    }
    /**
     * 发送聊天消息
     */
    private void sendMessage(IMContentDataModel messageRedPackageBean, MessageType messageType){
        P2PMessageBaseModel p2PMessageBaseModel = createP2PMessage(messageType,messageRedPackageBean,sessionId);
        saveMsgSuccess(p2PMessageBaseModel);
        DqImBaseBean dqImBaseBean = commonImConvertDqIm.commonImConvertDqIm(p2PMessageBaseModel);
        boolean isSuccess = SocketMessageUtil.sendMessage(dqImBaseBean);
        if (!isSuccess){
            p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
            updateMsgStatus(p2PMessageBaseModel);
        }
//        ImSdkHttpUtils.postJson(URLUtil.USER_SEND_MSG,p2PMessageBaseModel,new HttpResultResultCallBack<HttpBaseBean>() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                e.printStackTrace();
//                p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
//                updateMsgStatus(p2PMessageBaseModel);
//            }
//
//            @Override
//            public void onResponse(HttpBaseBean response, int id) {
//                int status = response.status;
//                String data = response.data.toString();
//                if (0 == status){
//                    IMContentDataModel resultContentModel = parserSendMessageResult(data,messageRedPackageBean,p2PMessageBaseModel);
//                    String resultContent = gson.toJson(resultContentModel);
//                    p2PMessageBaseModel.setSourceContent(resultContent);
//                    p2PMessageBaseModel.setContentData(resultContentModel);
//                    p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_SUCCESS.getValue());
//                }else {
//                    Toast.makeText(getContext(),"消息发送失败:"+response.msg,Toast.LENGTH_SHORT).show();
//                    p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
//                }
//                updateMsgStatus(p2PMessageBaseModel);
//            }
//        });
    }

    /**
     * 解析消息发送接口后的数据，不同的消息类型返回结果可能会不同，所以返回结果需要根据消息类型进行解析
     */
    private IMContentDataModel parserSendMessageResult(String data, IMContentDataModel messageRedPackageBean, ImMessageBaseModel imMessageBaseModel){
        IMContentDataModel result = messageRedPackageBean;
        ImSendMessageResultBean imSendMessageResultBean = gson.fromJson(data, ImSendMessageResultBean.class);
        imMessageBaseModel.setMsgIdServer(imSendMessageResultBean.getMsgId());
        if (MessageType.RED_PACKAGE.getValue().equals( imSendMessageResultBean.getMsgType())){
            MessageRedPackageBean redPackageBean = (MessageRedPackageBean)messageRedPackageBean;
            redPackageBean.setCouponId(imSendMessageResultBean.getCouponId());
            result = redPackageBean;
        }
        return result;
    }

    //基于这个时间之前的数据最多查出20条
    //不使用LiveData动态刷新功能，因为每次向数据库插入数据的时候都会触发全局刷新功能
    private void queryLocalAllMessage(long time){
        Log.e("YM","开始查询的时间:"+time);
        new Thread(() -> {
            List<P2PMessageBaseModel> p2PMessageBaseModels = p2PMessageViewModel.getP2PMessageByFromIdAndToId(sessionId,pageSize,time);
            handler.post(() -> {
                if (null == p2PMessageBaseModels || p2PMessageBaseModels.size() == 0){//假如本地没有数据，则从云端获取
                    requestNetMessageHistory(time+"");
                    return;
                }

                Log.e("YM","检索的本地消息长度:"+p2PMessageBaseModels.size());

                for (P2PMessageBaseModel messageBaseModel : p2PMessageBaseModels){
//                    if (MessageType.TEXT.getValue().equals(messageBaseModel.getMsgType())){
//                        MessageTextBean messageTextBean = gson.fromJson(messageBaseModel.getSourceContent(),MessageTextBean.class);
//                        String content = AESHelper.decryptString(messageTextBean.getSearchableContent());
//                    }
                    Log.e("YM","查出来的数据:"+p2PMessageBaseModels.toString());
                }
                Collections.reverse(p2PMessageBaseModels);
                chatP2PAdapter.addDataAll(p2PMessageBaseModels);
                chatContainer.setRefreshing(false);
                if (!isFirstLoadData){
                    return;
                }
                if (null != chatP2PAdapter.getData() && chatP2PAdapter.getData().size() > 0) {//假如列表为空，则用当前时间
                    chatMessageRv.scrollToPosition(p2PMessageBaseModels.size() - 1);
                    isFirstLoadData = false;
                }
            });
        }).start();
    }
    /**
     * 获取云端聊天信息接口
     * 所有参数都是可选的
     */
    private void requestNetMessageHistory(String time){
        Map<String,Object> map = new HashMap<>();
        map.put("pageSize",pageSize);
        map.put("createTime","");//选填，填写的话会正序查询
        map.put("fromUserId", ModuleMgr.getCenterMgr().getUID());
        map.put("toUserId",sessionId);
        map.put("groupId","");
        map.put("endTime",time);//选填，填写的话会倒序查询
        map.put("likeMsg","");
        ImSdkHttpUtils.postJson(URLUtil.USER_MSG_LIST,map,new HttpResultResultCallBack<HttpBaseBean>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                chatContainer.setRefreshing(false);
            }

            @Override
            public void onResponse(HttpBaseBean response, int id) {
                chatContainer.setRefreshing(false);
                String content = response.data.toString();
                List<P2PMessageBaseModel> messageBean = DqImParserUtils.getP2PMessageModels(content);
                if (messageBean.size() == 0){
                    Toast.makeText(getContext(),"没有更多数据了",Toast.LENGTH_SHORT).show();
                    return;
                }
                Collections.reverse(messageBean);
                chatP2PAdapter.addDataAll(messageBean);
                if (!isFirstLoadData){
                    return;
                }
                handler.postDelayed(() -> chatMessageRv.scrollToPosition(chatP2PAdapter.getData().size() - 1),300);
                isFirstLoadData = false;
            }
        });
    }
    /**
     * 红包打开结果
     */
    class OpenRedPackageResult implements OpenRedPackageDialog.OpenRedPackageResultListener{
        @Override
        public void redPackageResult(ImMessageBaseModel imMessageBaseModel) {//打开结果回调
            P2PMessageBaseModel p2PMessageBaseModel = (P2PMessageBaseModel) imMessageBaseModel;
            updateMsgStatus(p2PMessageBaseModel);
        }
    }

    /**
     * 消息操作
     */
    class MessageOption implements MessageOptionDialog.MessageOptionIpc {
        @Override
        public void result(String content) {
//            String result = AESHelper.decryptString(content);
            String result = AESUtil.decode(content);

            ClipboardUtil.clipboardCopyText(getActivity(),result);
            TToast.show(getActivity(),"已经复制到粘贴板~");
        }
    }

}
