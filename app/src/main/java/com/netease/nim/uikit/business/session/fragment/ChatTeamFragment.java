package com.netease.nim.uikit.business.session.fragment;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.provider.DocumentFile;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.da.library.tools.AESHelper;
import com.dq.im.bean.MediaExtraBean;
import com.dq.im.bean.im.MessageCardBean;
import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.bean.im.MessageRedPackageBean;
import com.dq.im.bean.im.MessageTextBean;
import com.dq.im.bean.im.MessageVideoBean;
import com.dq.im.bean.im.MessageVoiceBean;
import com.dq.im.constants.URLUtil;
import com.dq.im.model.HomeImBaseMode;
import com.dq.im.model.IMContentDataModel;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.parser.ImParserUtils;
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
import com.dq.im.viewmodel.TeamMessageViewModel;
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
import com.wd.daquan.imui.activity.PhotoDetailsActivity;
import com.wd.daquan.imui.adapter.ChaTeamAdapter;
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
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.util.AESUtil;
import com.wd.daquan.util.FileUtils;
import com.wd.daquan.util.TToast;

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
 * 群组聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class ChatTeamFragment extends BaseChatMessageFragment implements ModuleProxy, QCObserver {
    private SwipeRefreshLayout chatContainer;
    private View rootView;

    private SessionCustomization customization;

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

    private TeamMessageViewModel teamMessageViewModel;
    private HomeMessageViewModel homeMessageViewModel;
    public ImType getSessionType() {
        return sessionType;
    }
    private ChaTeamAdapter chaTeamAdapter;//聊天适配器

    private RecyclerView chatMessageRv;
    private long createTime = System.currentTimeMillis();//上一条消息的时间
    private GroupInfoBean mGroupInfo;
    private boolean isFirstLoadData = true;//是不是第一次加载数据，第一次加载数据会滚动到底部，下拉刷新则不会
    private IMContentDataModel sendContent;
    private CommonImConvertDqIm commonImConvertDqIm = new CommonImConvertDqIm();
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MsgMgr.getInstance().attach(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.nim_message_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setChatBG(rootView);
        parseIntent();
        initView(view);
        initChatMessageData();
        queryLocalAllMessage(System.currentTimeMillis());
        sendThirdMessage();
        onKeyBoardListener();
    }

    //监听软件盘是否弹起
    private void onKeyBoardListener() {
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                if (null != chatMessageRv){
                    chatMessageRv.scrollToPosition(chaTeamAdapter.getData().size() - 1);
                }
            }

            @Override
            public void keyBoardHide(int height) {
            }
        });
    }
    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof OpenRedPackageDialog){
            OpenRedPackageDialog openRedPackageDialog = (OpenRedPackageDialog) fragment;
            openRedPackageDialog.setOpenRedPackageResultListener(new OpenRedPackageResult());
        }else if (fragment instanceof MessageOptionDialog){
            MessageOptionDialog messageOptionDialog = (MessageOptionDialog) fragment;
            messageOptionDialog.setMessageOptionIpc(new MessageOption());
        }
    }

    /**
     * ***************************** life cycle *******************************
     */

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


    private void initView(View view){
        chatMessageRv = view.findViewById(R.id.messageListView);
        chatContainer = view.findViewById(R.id.chat_container);
        chatContainer.setOnRefreshListener(() -> {
//                requestNetMessageHistory();
            long time;
            if (null == chaTeamAdapter.getData() || chaTeamAdapter.getData().size() == 0){//假如列表为空，则用当前时间
                createTime = System.currentTimeMillis();//当前时间
                time = createTime;
            }else {//假如列表不为空，则用第一条数据的时间
                List<TeamMessageBaseModel> baseModels = chaTeamAdapter.getData();
                time = baseModels.get(0).getCreateTime();
            }

            queryLocalAllMessage(time);
        });
        initRecycleView(chatMessageRv);
    }

    private void initChatMessageData(){
        chaTeamAdapter = new ChaTeamAdapter(this);
        chaTeamAdapter.setRecycleItemOnClickListener(new RecycleItemOnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TeamMessageBaseModel model = chaTeamAdapter.getData(position);
                int type = chaTeamAdapter.getItemViewType(position);
                if (ChatLayoutChildType.LEFT_RED_PACKAGE == type){//左侧红包
                    String content = model.getSourceContent();
                    MessageRedPackageBean messageRedPackageBean = gson.fromJson(content,MessageRedPackageBean.class);
                    redPackageRoutePager(view.getContext(),messageRedPackageBean,model);
                }else if (ChatLayoutChildType.RIGHT_RED_PACKAGE == type){//右侧红包
                    String content = model.getSourceContent();
                    MessageRedPackageBean messageRedPackageBean = gson.fromJson(content,MessageRedPackageBean.class);
                    redPackageRoutePager(view.getContext(),messageRedPackageBean,model);
                }

                if (MessageType.PICTURE.getValue().equals(model.getMsgType())){//假如是图片消息
                    ArrayList<TeamMessageBaseModel> temp = (ArrayList<TeamMessageBaseModel>)chaTeamAdapter.getData();
                    Intent intent = new Intent(view.getContext(), PhotoDetailsActivity.class);
                    intent.putExtra(PhotoDetailsActivity.PHOTO_DATA,temp);
                    intent.putExtra(PhotoDetailsActivity.PHOTO_DATA_CURRENT,position);
                    startActivity(intent);
                }
            }
        });
        chaTeamAdapter.setRecycleItemOnLongClickListener(new RecycleItemOnLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                MessageOptionDialog messageOptionDialog = new MessageOptionDialog();
                TeamMessageBaseModel model = chaTeamAdapter.getData(position);
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
        chatMessageRv.setAdapter(chaTeamAdapter);
        RecycleItemOnClickForChildViewListenerCompat.setRecycleItemOnClickForChildViewListener(new RecycleItemOnClickForChildViewListenerCompat.SimpleChildViewListener() {
            @Override
            public void onItemForChildClick(View view,ImMessageBaseModel imMessageBaseModel) {
                TeamMessageBaseModel model = (TeamMessageBaseModel)imMessageBaseModel;
                switch (view.getId()){
                    case R.id.message_fail:
                        Toast.makeText(view.getContext(),"消息发送失败",Toast.LENGTH_SHORT).show();
                        sendFailMessage(model);
                        break;
                }
            }
        });
    }

    private void sendFailMessage(TeamMessageBaseModel model){
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
        }

    }
    /**
     * 本地消息ID
     */
    private void removeMessage(TeamMessageBaseModel model){
        Log.e("YM","删除的文本id:"+model.getMsgIdClient());
        teamMessageViewModel.deleteMessageForClientId(model.getMsgIdClient());
        chaTeamAdapter.removeData(model);
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
        initData();
        Log.e("YM","群组Id:"+sessionId);
        sessionType = (ImType) getArguments().getSerializable(Extras.EXTRA_TYPE);

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
        teamMessageViewModel = ViewModelProviders.of(this).get(TeamMessageViewModel.class);
        homeMessageViewModel = ViewModelProviders.of(this).get(HomeMessageViewModel.class);
        homeMessageViewModel.updateTeamUnReadNumber(sessionId,0);
    }


    public void setTeam(GroupInfoBean groupInfo) {
        this.mGroupInfo = groupInfo;
    }
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

//        messageListPanel.onMsgSend(message);

        if (aitManager != null) {
            aitManager.reset();
        }
        return true;
    }

    private void uploadPhoto(Uri uri){
        DocumentFile documentFile = DocumentFile.fromSingleUri(getContext(), uri);
        Log.e("YM","上传的的图片的名字:"+documentFile.getName());
        Log.e("YM","上传的的图片的路径:"+uri.toString());
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            byte[] photoData = FileUtils.toByteArray(inputStream);
            TeamMessageBaseModel p2PMessageBaseModel = createTeamMessage(MessageType.PICTURE,uri.toString(),"",sessionId);
            saveMsgSuccess(p2PMessageBaseModel);
            uploadFile(uri.toString(),photoData,documentFile.getName(),MessageType.PICTURE,p2PMessageBaseModel);
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
            TeamMessageBaseModel teamMessageBaseModel = createTeamVoiceMessage(MessageType.VOICE,localPath,duration,"",sessionId);
            saveMsgSuccess(teamMessageBaseModel);
            uploadFile(localPath,audioData,file.getName(),MessageType.VOICE,teamMessageBaseModel);
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
            videoBean.setFileName(videoDocumentFile.getName());
            videoBean.setFileData(videoData);
            videoBean.setLocalPath(uri.toString());
            upLoadBeans.add(videoBean);//加载视频信息
            UpLoadBean thumbBean = new UpLoadBean();
            thumbBean.setType(2);
            thumbBean.setFileName(thumbDocumentFile.getName());
            thumbBean.setFileData(thumbData);
            thumbBean.setLocalPath(mediaExtraBean.getThumbPath().toString());
            upLoadBeans.add(thumbBean);//加载缩略图信息
            inputStream.close();

            MessageVideoBean messageVideoBean = new MessageVideoBean();
            messageVideoBean.setVideoLocalPath(uri.toString());
            messageVideoBean.setThumbLocalPath(mediaExtraBean.getThumbPath().toString());
            TeamMessageBaseModel teamMessageBaseModel = createTeamMessage(MessageType.VIDEO,messageVideoBean,sessionId);
            saveMsgSuccess(teamMessageBaseModel);

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
                    teamMessageBaseModel.setContentData(messageVideoBean);
                    teamMessageBaseModel.setSourceContent(gson.toJson(messageVideoBean));
                    Log.e("YM","上传的视频包含的信息uploadBatchComplete:"+messageVideoBean.toString());
                    uploadVideoSuccess(teamMessageBaseModel,messageVideoBean,MessageType.VIDEO);
                }

                @Override
                public void uploadBatchFail(List<UpLoadBean> upLoadBeans) {
                    Log.e("YM","上传的视频包含的信息uploadBatchFail:"+messageVideoBean.toString());
                    uploadVideoFail(teamMessageBaseModel,messageVideoBean,MessageType.VIDEO);
                }
            });
        } catch (IOException e){
            e.printStackTrace();
        }
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
        Log.e("YM","Fragment返回的resultCode值:"+resultCode+"----->requestCode:"+requestCode);
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

        }else if (MsgType.TEAM_MESSAGE_CONTENT.equals(key)){//群组消息
            Log.e("YM","收到消息:");
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) value;
            parserOtherMsg(teamMessageBaseModel);//处理对方消息
            if (MessageType.SYSTEM.getValue().equals(teamMessageBaseModel.getMsgType())){//不插入数据库，因为系统消息没有发消息用户的Id
                return;
            }
            if (MessageType.SYSTEM.getValue().equals(teamMessageBaseModel.getMsgType())){//不插入数据库，因为系统消息没有发消息用户的Id
                return;
            }
            if (!sessionId.equals(teamMessageBaseModel.getGroupId())){//假如不是当前群的消息,则不处理。因为首页收到群组消息后都会传到这个页面
                return;
            }
//            teamMessageBaseModel.setToUserId(EasySP.init(this).getString(UserSpConstants.USER_ID));
            teamMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_SUCCESS.getValue());
            homeMessageViewModel.updateTeamUnReadNumber(sessionId,0);//消息从ImMainActivity传入，所以先设置未读消息数，进入里面再重置为0
            saveMsgSuccess(teamMessageBaseModel);
        }else if(MsgType.MESSAGE_RECEIVE_CALL_BACK.equals(key)){//消息发送后，服务器把消息回传给客户端的内容
            ImMessageBaseModel imMessageBaseModel = (ImMessageBaseModel)value;
            for (TeamMessageBaseModel messageBaseModel : chaTeamAdapter.getData()){
                if (imMessageBaseModel.getMsgIdClient().equals(messageBaseModel.getMsgIdClient())){
                    messageBaseModel.setMsgIdServer(imMessageBaseModel.getMsgIdServer());
                    messageBaseModel.setSourceContent(imMessageBaseModel.getSourceContent());
                    messageBaseModel.setMessageSendStatus(imMessageBaseModel.getMessageSendStatus());
                    chaTeamAdapter.updateMessageStatus(messageBaseModel);
                    Log.e("YM","更新消息发送状态");
                    return;
                }
            }
        }else if (MsgType.CHAT_RED_PACKAGE.equals(key)){//发送红包的消息
            inputPanel.switchRobotMode(false);
            inputPanel.collapse(false);
            MessageRedPackageBean messageRedPackageBean = (MessageRedPackageBean) value;
            sendMessage(messageRedPackageBean,MessageType.RED_PACKAGE);
        }else if (MsgType.CHAT_PICTURE.equals(key)){
            Uri uri = (Uri) value;
            uploadPhoto(uri);
        }else if (MsgType.CHAT_VIDEO.equals(key)){
            Uri uri = (Uri) value;
            uploadVideo(uri);
        }else if (MsgType.CHAT_VOICE.equals(key)){
            Log.e("YM","Fragment收到的音频消息");
            VoiceBean voiceBean = (VoiceBean) value;
            uploadVoice(voiceBean.localString,voiceBean.voiceDuration);
        }else if (MsgType.CLEAR_MSG.equals(key)){
            getActivity().finish();
        }else if (MsgType.GROUP_REMOVE.equals(key)){
            TToast.show(getContext(),"群已解散，无法再进行聊天");
            getActivity().finish();
        }else if (MsgType.MT_GROUP_SETTING_QUIT.equals(key)){
            TToast.show(getContext(),"已退出群聊");
            getActivity().finish();
        }
    }

    //设置聊天背景
    private void setChatBG(View view){
        String filePath = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo().getString(EBSharedPrefUser.filePath,"");
        RecyclerView messageListView = (RecyclerView) view.findViewById(R.id.messageListView);
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

    @Override
    public void uploadFileSuccess(String localPath, String netUrl, MessageType messageType,ImMessageBaseModel imMessageBaseModel) {
//        TeamMessageBaseModel teamMessageBaseModel = createTeamMessage(messageType,localPath,netUrl,sessionId);
        TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel)imMessageBaseModel;
//        saveMsgSuccess(teamMessageBaseModel);
        DqImBaseBean dqImBaseBean = commonImConvertDqIm.commonImConvertDqIm(teamMessageBaseModel);
        boolean isSuccess = SocketMessageUtil.sendMessage(dqImBaseBean);
        if (!isSuccess){
            teamMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
            updateMsgStatus(teamMessageBaseModel);
        }
//        ImSdkHttpUtils.postJson(URLUtil.USER_SEND_MSG,teamMessageBaseModel,new HttpResultResultCallBack<HttpBaseBean>() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                Log.e("YM","链接问题:"+e.getMessage());
//                e.printStackTrace();
//                teamMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
////                saveMsgSuccess(p2PMessageBaseModel);
//                updateMsgStatus(teamMessageBaseModel);
//            }
//
//            @Override
//            public void onResponse(HttpBaseBean response, int id) {
//                int status = response.status;
//                String msgServerId = response.data.toString();
//                if (0 == status){
//                    teamMessageBaseModel.setMsgIdServer(msgServerId);
//                    teamMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_SUCCESS.getValue());
//                }else {
//                    Toast.makeText(getContext(),"消息发送失败:"+response.msg,Toast.LENGTH_SHORT).show();
//                    teamMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
//                }
//                updateMsgStatus(teamMessageBaseModel);
//            }
//        });
    }

    @Override
    public void uploadFileFail(String localPath, MessageType messageType,ImMessageBaseModel imMessageBaseModel) {
//        TeamMessageBaseModel teamMessageBaseModel = createTeamMessage(messageType,localPath,"",sessionId);
        TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel)imMessageBaseModel;
        teamMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
        saveMsgSuccess(teamMessageBaseModel);
    }

    /**
     * 视频文件上传失败
     * @param imContentDataModel
     * @param messageType
     */
    public void uploadVideoFail(TeamMessageBaseModel teamMessageBaseModel,IMContentDataModel imContentDataModel, MessageType messageType){
//        TeamMessageBaseModel teamMessageBaseModel = createTeamMessage(messageType,imContentDataModel,sessionId);
        teamMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
//        saveMsgSuccess(teamMessageBaseModel);
        updateMsgStatus(teamMessageBaseModel);
    }

    /**
     * 视频文件上传成功
     * @param imContentDataModel
     * @param messageType
     */
    public void uploadVideoSuccess(TeamMessageBaseModel teamMessageBaseModel, IMContentDataModel imContentDataModel, MessageType messageType) {
//        TeamMessageBaseModel teamMessageBaseModel = createTeamMessage(messageType,imContentDataModel,sessionId);
//        saveMsgSuccess(teamMessageBaseModel);
        DqImBaseBean dqImBaseBean = commonImConvertDqIm.commonImConvertDqIm(teamMessageBaseModel);
        boolean isSuccess = SocketMessageUtil.sendMessage(dqImBaseBean);
        if (!isSuccess){
            teamMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
            updateMsgStatus(teamMessageBaseModel);
        }
//        ImSdkHttpUtils.postJson(URLUtil.USER_SEND_MSG,teamMessageBaseModel,new HttpResultResultCallBack<HttpBaseBean>() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                Log.e("YM","链接问题:"+e.getMessage());
//                e.printStackTrace();
//                teamMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
////                saveMsgSuccess(p2PMessageBaseModel);
//                updateMsgStatus(teamMessageBaseModel);
//            }
//
//            @Override
//            public void onResponse(HttpBaseBean response, int id) {
//                int status = response.status;
//                String msgServerId = response.data.toString();
//                if (0 == status){
//                    teamMessageBaseModel.setMsgIdServer(msgServerId);
//                    teamMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_SUCCESS.getValue());
//                }else {
//                    Toast.makeText(getContext(),"消息发送失败:"+response.msg,Toast.LENGTH_SHORT).show();
//                    teamMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
//                }
//                updateMsgStatus(teamMessageBaseModel);
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
    private void parserOtherMsg(TeamMessageBaseModel messageBaseModel){
        MessageType messageType = MessageType.typeOfValue(messageBaseModel.getMsgType());
        if (MessageType.SYSTEM == messageType){//假如是系统消息
            Log.e("YM","系统消息------->:"+messageBaseModel.getMsgSecondType());
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
     * @param couponId
     */
    private void parserRedPackageOpened(String couponId){
        List<TeamMessageBaseModel> messageBaseModels = chaTeamAdapter.getData();
        for (TeamMessageBaseModel model : messageBaseModels){
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
    private void saveMsgSuccess(TeamMessageBaseModel messageBaseModel){
        teamMessageViewModel.insert(messageBaseModel);
        HomeImBaseMode homeImBaseMode = ImTransformUtils.teamImModelTransformHomeImModel(messageBaseModel);
        homeMessageViewModel.updateHomeMessage(homeImBaseMode,false);
        chaTeamAdapter.addData(messageBaseModel);
        chatMessageRv.scrollToPosition(chaTeamAdapter.getData().size() - 1);
    }

    /**
     * 更改发送结果
     * @param messageBaseModel
     */
    private void updateMsgStatus(TeamMessageBaseModel messageBaseModel){
        teamMessageViewModel.insert(messageBaseModel);
        HomeImBaseMode homeImBaseMode = ImTransformUtils.teamImModelTransformHomeImModel(messageBaseModel);
        homeMessageViewModel.updateHomeMessage(homeImBaseMode,false);
        chaTeamAdapter.updateMessageStatus(messageBaseModel);
        Log.e("YM","更改状态后的个人内容:"+messageBaseModel.toString());
        Log.e("YM","更改状态后的首页个人内容:"+homeImBaseMode.toString());
//        chatMessageRv.scrollToPosition(chatP2PAdapter.getData().size() - 1);
    }
    /**
     * 发送聊天消息
     */
    private void sendMessage(IMContentDataModel messageRedPackageBean, MessageType messageType){
        TeamMessageBaseModel teamMessage = createTeamMessage(messageType,messageRedPackageBean,sessionId);
        saveMsgSuccess(teamMessage);
        DqImBaseBean dqImBaseBean = commonImConvertDqIm.commonImConvertDqIm(teamMessage);
        boolean isSuccess = SocketMessageUtil.sendMessage(dqImBaseBean);
        if (!isSuccess){
            teamMessage.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
            updateMsgStatus(teamMessage);
        }
//        ImSdkHttpUtils.postJson(URLUtil.USER_SEND_MSG,teamMessage,new HttpResultResultCallBack<HttpBaseBean>() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                e.printStackTrace();
//                teamMessage.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
//                updateMsgStatus(teamMessage);
//            }
//
//            @Override
//            public void onResponse(HttpBaseBean response, int id) {
//                int status = response.status;
//                String data = response.data.toString();
//                if (0 == status){
//                    IMContentDataModel resultContentModel = parserSendMessageResult(data,messageRedPackageBean,teamMessage);
//                    String resultContent = gson.toJson(resultContentModel);
//                    teamMessage.setSourceContent(resultContent);
//                    teamMessage.setContentData(resultContentModel);
//                    teamMessage.setMessageSendStatus(MessageSendType.SEND_SUCCESS.getValue());
//                }else {
//                    Toast.makeText(getContext(),"消息发送失败:"+response.msg,Toast.LENGTH_SHORT).show();
//                    teamMessage.setMessageSendStatus(MessageSendType.SEND_FAIL.getValue());
//                }
//                updateMsgStatus(teamMessage);
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
        if (MessageType.RED_PACKAGE.getValue().equals(imSendMessageResultBean.getMsgType())){
            MessageRedPackageBean redPackageBean = (MessageRedPackageBean)messageRedPackageBean;
            redPackageBean.setCouponId(imSendMessageResultBean.getCouponId());
            result = redPackageBean;
        }
        return result;
    }

    //基于这个时间之前的数据最多查出20条
    //不使用LiveData动态刷新功能，因为每次向数据库插入数据的时候都会触发全局刷新功能
    private void queryLocalAllMessage(long time){
        new Thread(() -> {
            List<TeamMessageBaseModel> p2PMessageBaseModels = teamMessageViewModel.getTeamMessageByFromGroupID(sessionId,pageSize,time);
            handler.post(() -> {
                if (null == p2PMessageBaseModels || p2PMessageBaseModels.size() == 0){//假如本地没有数据，则从云端获取
                    requestNetMessageHistory(time+"");
                    return;
                }
                Log.e("YM","检索的本地消息长度:"+p2PMessageBaseModels.size());
                for (TeamMessageBaseModel model : p2PMessageBaseModels){

                    if (MessageType.TEXT.getValue().equals(model.getMsgType())){
                        MessageTextBean messageTextBean = gson.fromJson(model.getSourceContent(),MessageTextBean.class);
                        if (null != messageTextBean){
                          String  content = messageTextBean.getDescription();
//                            content = AESHelper.decryptString(content);
                            String text = AESUtil.decode(content);
                        }
                    }

                }
                Collections.reverse(p2PMessageBaseModels);
                chaTeamAdapter.addDataAll(p2PMessageBaseModels);
                chatContainer.setRefreshing(false);
                if (!isFirstLoadData){
                    return;
                }
                if (null != chaTeamAdapter.getData() && chaTeamAdapter.getData().size() > 0) {//假如列表为空，则用当前时间
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
                List<TeamMessageBaseModel> messageBean = DqImParserUtils.getTeamMessageModels(content);
                if (messageBean.size() == 0){
                    Toast.makeText(getContext(),"没有更多数据了",Toast.LENGTH_SHORT).show();
                    return;
                }
                Collections.reverse(messageBean);
                chaTeamAdapter.addDataAll(messageBean);
                if (!isFirstLoadData){
                    return;
                }
                handler.postDelayed(() -> chatMessageRv.scrollToPosition(chaTeamAdapter.getData().size() - 1),300);
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
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel) imMessageBaseModel;
            updateMsgStatus(teamMessageBaseModel);
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