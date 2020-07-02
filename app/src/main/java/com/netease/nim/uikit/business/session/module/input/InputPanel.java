package com.netease.nim.uikit.business.session.module.input;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.da.library.tools.AESHelper;
import com.dq.im.bean.im.MessageTextBean;
import com.dq.im.type.ImType;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.business.ait.AitTextChangeListener;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.emoji.AndroidEmoji;
import com.netease.nim.uikit.business.session.emoji.EmoticonPickerView;
import com.netease.nim.uikit.business.session.emoji.IEmoticonSelectedListener;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.session.module.Container;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.wd.daquan.imui.bean.VoiceBean;
import com.wd.daquan.imui.constant.Constant;
import com.wd.daquan.imui.type.RecordAudioStatus;
import com.wd.daquan.model.bean.Draft;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.DraftMgr;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.third.helper.UserInfoHelper;
import com.wd.daquan.util.AESUtil;
import com.wd.daquan.util.TToast;
import com.wd.daquan.util.audiorecord.AudioRecoderDialog;
import com.zlw.main.recorderlib.RecordManager;

import java.util.List;

/**
 * 底部文本编辑，语音等模块
 * Created by hzxuwen on 2015/6/16.
 */
public class InputPanel implements IEmoticonSelectedListener,
        AitTextChangeListener, QCObserver {

    private static final String TAG = "MsgSendLayout";

    private static final int SHOW_LAYOUT_DELAY = 200;

    protected Container container;
    protected View view;
    protected Handler uiHandler;

    private View actionPanelBottomLayout; // 更多布局
    private View assistantPanelBottomLayout; // 助手布局
    private LinearLayout messageActivityBottomLayout;
    private EditText messageEditText;// 文本消息编辑框
    private Button audioRecordBtn; // 录音按钮
    private FrameLayout textAudioSwitchLayout; // 切换文本，语音按钮布局
    private View switchToTextButtonInInputBar;// 文本消息选择按钮
    private View switchToAudioButtonInInputBar;// 语音消息选择按钮
    private View moreFuntionButtonInInputBar;// 更多消息选择按钮
    private View sendMessageButtonInInputBar;// 发送消息按钮
    private View emojiButtonInInputBar;// 发送消息按钮
    private View messageInputBar;


    private long downT;//按下的时间
    private AudioRecoderDialog recodeDialog;
    private RecordAudioStatus recordVoiceState = RecordAudioStatus.RECORD_INIT;//录音状态，-1，待准备，0，开始，1，停止，2撤销


    private SessionCustomization customization;

    // 表情
    private EmoticonPickerView emoticonPickerView;  // 贴图表情控件

    // 语音
    private Chronometer time;
    private TextView timerTip;
    private LinearLayout timerTipContainer;
    private boolean started = false;
    private boolean cancelled = false;
    private boolean touched = false; // 是否按着
    private boolean isKeyboardShowed = true; // 是否显示键盘

    // status
    private boolean actionPanelBottomLayoutHasSetup = false;
    private boolean assistantPanelBottomLayoutHasSetup = false;
    private boolean isTextAudioSwitchShow = true;

    // adapter
    private List<BaseAction> actions;

    // intentUrl
    private long typingTime = 0;

    private boolean isRobotSession;

    private TextWatcher aitTextWatcher;

    private Button assistBtn;
    private VoiceBean voiceBean = new VoiceBean();
    private InputPanel(Container container, View view, List<BaseAction> actions, boolean isTextAudioSwitchShow) {
        this.container = container;
        this.view = view;
        this.actions = actions;
        this.uiHandler = new Handler();
        this.isTextAudioSwitchShow = isTextAudioSwitchShow;
        initMediaVoiceRecord();
        init();
    }

    public InputPanel(Container container, View view, List<BaseAction> actions) {
        this(container, view, actions, true);
    }

    public void onPause() {
        // 退出界面的时候，保存草稿
        if (messageEditText != null) {
            String content = messageEditText.getText().toString();
            if (!TextUtils.isEmpty(content)) {
                Draft draft = new Draft();
                draft.sessionID = container.account;
                draft.sessionType = container.sessionType.name();
                draft.content = content;
                DraftMgr.getInstance().updateDraft(draft);
                MsgMgr.getInstance().sendMsg(MsgType.MT_DRAFT_STATUS, container.account);
            } else {
//                DraftMgr.getInstance().deleteDraft(container.account);
//                MsgMgr.getInstance().sendMsg(MsgType.MT_DRAFT_STATUS, container.account);
            }
        }
    }

    public void onDestroy() {
        // release
    }

    public boolean collapse(boolean immediately) {
        boolean respond = (emoticonPickerView != null && emoticonPickerView.getVisibility() == View.VISIBLE
                || actionPanelBottomLayout != null && actionPanelBottomLayout.getVisibility() == View.VISIBLE);

        hideAllInputLayout(immediately);

        return respond;
    }

    public void addAitTextWatcher(TextWatcher watcher) {
        aitTextWatcher = watcher;
    }

    private void init() {
        MsgMgr.getInstance().attach(this);
        initViews();
        initInputBarListener();
        initTextEdit();
        restoreText(false);
        initDraft();
        for (int i = 0; i < actions.size(); ++i) {
            actions.get(i).setIndex(i);
            actions.get(i).setContainer(container);
        }
    }


    /**
     * 录制音频的对话框
     */
    private void initMediaVoiceRecord(){
        recodeDialog = new AudioRecoderDialog(container.getContext());
        recodeDialog.setShowAlpha(0.98f);
        RecordManager.getInstance().setRecordSoundSizeListener(soundSize -> {
            if(null != recodeDialog) {
                recodeDialog.setLevel(soundSize);
                recodeDialog.setTime(System.currentTimeMillis() - downT);
            }
        });
        RecordManager.getInstance().setRecordResultListener(result -> {
            if (recordVoiceState == RecordAudioStatus.RECORD_STOP){
                voiceBean.localString = result.getAbsolutePath();
                Log.e("YM","录音结束,保存的文件:"+result.getAbsolutePath());

                if (voiceBean.voiceDuration < 1000){//
                    result.delete();
                    TToast.show(container.getContext(),"时间太短，发送失败！");
                    return;
                }
                if (result.length() < 100){
                    result.delete();
                    TToast.show(container.getContext(),"录音文件太小，发送失败！");
                    return;
                }
                MsgMgr.getInstance().sendMsg(MsgType.CHAT_VOICE, voiceBean);
            }else {//撤销时候删除
                result.delete();
            }
            recordVoiceState = RecordAudioStatus.RECORD_INIT;
        });
    }

    public void setCustomization(SessionCustomization customization) {
        this.customization = customization;
        if (customization != null) {
            emoticonPickerView.setWithSticker(customization.withSticker);
        }
    }

    public void reload(Container container, SessionCustomization customization) {
        this.container = container;
        setCustomization(customization);
    }

    private void initViews() {
        // input bar
        messageActivityBottomLayout = view.findViewById(R.id.messageActivityBottomLayout);
        messageInputBar = view.findViewById(R.id.textMessageLayout);
        switchToTextButtonInInputBar = view.findViewById(R.id.buttonTextMessage);
        switchToAudioButtonInInputBar = view.findViewById(R.id.buttonAudioMessage);
        moreFuntionButtonInInputBar = view.findViewById(R.id.buttonMoreFuntionInText);
        emojiButtonInInputBar = view.findViewById(R.id.emoji_button);
        sendMessageButtonInInputBar = view.findViewById(R.id.buttonSendMessage);
        messageEditText =  view.findViewById(R.id.editTextMessage);

        // 语音
        audioRecordBtn = view.findViewById(R.id.audioRecord);
        time = view.findViewById(R.id.timer);
        timerTip = view.findViewById(R.id.timer_tip);
        timerTipContainer = view.findViewById(R.id.timer_tip_container);

        // 表情
        emoticonPickerView = (EmoticonPickerView) view.findViewById(R.id.emoticon_picker_view);
        emoticonPickerView.setSessionData(container);
        // 显示录音按钮
        switchToTextButtonInInputBar.setVisibility(View.GONE);
        switchToAudioButtonInInputBar.setVisibility(View.VISIBLE);

        // 文本录音按钮切换布局
        textAudioSwitchLayout = (FrameLayout) view.findViewById(R.id.switchLayout);
        if (isTextAudioSwitchShow) {
            textAudioSwitchLayout.setVisibility(View.VISIBLE);
        } else {
            textAudioSwitchLayout.setVisibility(View.GONE);
        }
        audioRecordBtn.setOnTouchListener(this::onTouch);
    }

    private void initInputBarListener() {
        switchToTextButtonInInputBar.setOnClickListener(clickListener);
        switchToAudioButtonInInputBar.setOnClickListener(clickListener);
        emojiButtonInInputBar.setOnClickListener(clickListener);
        sendMessageButtonInInputBar.setOnClickListener(clickListener);
        moreFuntionButtonInInputBar.setOnClickListener(clickListener);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initTextEdit() {
        messageEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        messageEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                switchToTextLayout(true);
            }
            return false;
        });

        messageEditText.setOnFocusChangeListener((v, hasFocus) -> {
            messageEditText.setHint("");
            checkSendButtonEnable(messageEditText);
        });

        messageEditText.addTextChangedListener(new TextWatcher() {
            private int start;
            private int count;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.count = count;
                if (aitTextWatcher != null) {
                    aitTextWatcher.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (aitTextWatcher != null) {
                    aitTextWatcher.beforeTextChanged(s, start, count, after);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkSendButtonEnable(messageEditText);
                MoonUtil.replaceEmoticons(container.getContext(), s, start, count);

                int editEnd = messageEditText.getSelectionEnd();
                messageEditText.removeTextChangedListener(this);
                while (StringUtil.counterChars(s.toString()) > NimUIKitImpl.getOptions().maxInputTextLength && editEnd > 0) {
                    s.delete(editEnd - 1, editEnd);
                    editEnd--;
                }
                messageEditText.setSelection(editEnd);
                messageEditText.addTextChangedListener(this);

                if (aitTextWatcher != null) {
                    aitTextWatcher.afterTextChanged(s);
                }
            }
        });
    }


    private void initDraft() {

        String content = DraftMgr.getInstance().getContent(container.account);
        if(!TextUtils.isEmpty(content)) {
            messageEditText.setText(content);
        }
    }

    /**
     * ************************* 键盘布局切换 *******************************
     */
    long voiceStartTime = 0;//开始时间
    long voiceEndTime = 0;//结束时间
    float downY = 0;//按下的坐标
    float diffY = 200;//按下与抬起的Y轴偏差
    private boolean onTouch(View view, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startRecordVoice();
                downY = event.getRawY();
                return true;
            case MotionEvent.ACTION_UP:

                float upY = event.getRawY();//抬起的坐标

                float absDiff = Math.abs(downY - upY);
                if (absDiff > diffY){//取消录音
                    cancelRecordVoice();
                    return true;
                }
                completeRecordVoice();
                return true;
            case MotionEvent.ACTION_CANCEL://撤销
                cancelRecordVoice();
                return true;
            case MotionEvent.ACTION_MOVE:
                return true;
        }
        return false;
    }

    /**
     * 开始录音
     */
    private void startRecordVoice(){
        voiceStartTime = System.currentTimeMillis();
        recordVoiceState = RecordAudioStatus.RECORD_START;
        RecordManager.getInstance().changeRecordDir(Constant.getAudioRecordDir());
        RecordManager.getInstance().start();
        downT = System.currentTimeMillis();
        recodeDialog.showAtLocation(view, Gravity.CENTER, 0, 0);
//                chatBottomAudio.setBackgroundResource(R.drawable.shape_recoder_btn_recoding);
    }

    /**
     * 完成录音
     */
    private void completeRecordVoice(){
        recordVoiceState = RecordAudioStatus.RECORD_STOP;
        RecordManager.getInstance().stop();
        recodeDialog.dismiss();
//                chatBottomAudio.setBackgroundResource(R.drawable.shape_recoder_btn_normal);
        voiceEndTime = System.currentTimeMillis();
        voiceBean.voiceDuration = voiceEndTime - voiceStartTime;;
        voiceStartTime = 0;
        voiceEndTime = 0;
    }

    private void cancelRecordVoice(){
        Toast.makeText(container.getContext(),"取消录音",Toast.LENGTH_SHORT).show();
        recordVoiceState = RecordAudioStatus.RECORD_CANCEL;
        RecordManager.getInstance().stop();
        recodeDialog.dismiss();
//                chatBottomAudio.setBackgroundResource(R.drawable.shape_recoder_btn_normal);
        voiceStartTime = 0;
        voiceEndTime = 0;
        downY = 0;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!isFriend() && ImType.P2P == container.sessionType){//假如不是好友且不是单聊情况，则无法进行聊天操作
                DqToast.showShort("您与对方不是好友，无法进行聊天操作!");
                return;
            }
            if (v == switchToTextButtonInInputBar) {
                switchToTextLayout(true);// 显示文本发送的布局
            } else if (v == sendMessageButtonInInputBar) {
                onTextMessageSendButtonPressed();
            } else if (v == switchToAudioButtonInInputBar) {
                switchToAudioLayout();
            } else if (v == moreFuntionButtonInInputBar) {
                toggleActionPanelLayout();
            } else if (v == emojiButtonInInputBar) {
                toggleEmojiLayout();
            }
        }
    };

    /**
     * 是不是好友
     * @return true 是好友
     */
    private boolean isFriend(){
        return UserInfoHelper.isFriend(container.account);
    }
    // 点击edittext，切换键盘和更多布局
    private void switchToTextLayout(boolean needShowInput) {
        hideEmojiLayout();
        hideActionPanelLayout();
        hideAssistantPanelLayout();

        audioRecordBtn.setVisibility(View.GONE);
        messageEditText.setVisibility(View.VISIBLE);
        switchToTextButtonInInputBar.setVisibility(View.GONE);
        switchToAudioButtonInInputBar.setVisibility(View.VISIBLE);

        messageInputBar.setVisibility(View.VISIBLE);

        if (needShowInput) {
            uiHandler.postDelayed(showTextRunnable, SHOW_LAYOUT_DELAY);
        } else {
            hideInputMethod();
        }
    }

    // 发送文本消息
    private void onTextMessageSendButtonPressed() {
        String text = messageEditText.getText().toString();
        // 文本加密
//        text = AESHelper.encryptString(text);
        text = AESUtil.encode(text);
        MessageTextBean messageTextBean = new MessageTextBean();
        messageTextBean.setDescription(text);

        if (container.proxy.sendMessage(messageTextBean)) {
            restoreText(true);
        }
    }


    // 切换成音频，收起键盘，按钮切换成键盘
    private void switchToAudioLayout() {
        messageEditText.setVisibility(View.INVISIBLE);
        audioRecordBtn.setVisibility(View.VISIBLE);
        hideInputMethod();
        hideEmojiLayout();
        hideActionPanelLayout();
        hideAssistantPanelLayout();

        switchToAudioButtonInInputBar.setVisibility(View.GONE);
        switchToTextButtonInInputBar.setVisibility(View.VISIBLE);
    }

    // 点击“+”号按钮，切换更多布局和键盘
    private void toggleActionPanelLayout() {
        if (actionPanelBottomLayout == null || actionPanelBottomLayout.getVisibility() == View.GONE) {
            if (assistantPanelBottomLayout != null && assistantPanelBottomLayout.getVisibility() == View.VISIBLE) {
                hideActionPanelLayout();
                hideAssistantPanelLayout();
            } else {
                showActionPanelLayout();
            }
        } else {
            hideActionPanelLayout();
        }
    }

    // 点击表情，切换到表情布局
    private void toggleEmojiLayout() {
        if (emoticonPickerView == null || emoticonPickerView.getVisibility() == View.GONE) {
            showEmojiLayout();
        } else {
            hideEmojiLayout();
        }
    }

    // 隐藏表情布局
    private void hideEmojiLayout() {
        uiHandler.removeCallbacks(showEmojiRunnable);
        if (emoticonPickerView != null) {
            emoticonPickerView.setVisibility(View.GONE);
        }
    }

    // 隐藏更多布局
    private void hideActionPanelLayout() {
        uiHandler.removeCallbacks(showMoreFuncRunnable);
        if (actionPanelBottomLayout != null) {
            actionPanelBottomLayout.setVisibility(View.GONE);
        }
    }

    // 隐藏更多布局
    private void hideAssistantPanelLayout() {
        uiHandler.removeCallbacks(assistantFuncRunnable);
        if (assistantPanelBottomLayout != null) {
            assistantPanelBottomLayout.setVisibility(View.GONE);
        }
    }

    // 隐藏键盘布局
    private void hideInputMethod() {
        isKeyboardShowed = false;
        uiHandler.removeCallbacks(showTextRunnable);
        InputMethodManager imm = (InputMethodManager) container.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(messageEditText.getWindowToken(), 0);
        messageEditText.clearFocus();
    }

    // 隐藏语音布局
    private void hideAudioLayout() {
        audioRecordBtn.setVisibility(View.GONE);
        messageEditText.setVisibility(View.VISIBLE);
        switchToTextButtonInInputBar.setVisibility(View.VISIBLE);
        switchToAudioButtonInInputBar.setVisibility(View.GONE);
    }

    // 显示表情布局
    private void showEmojiLayout() {
        hideInputMethod();
        hideActionPanelLayout();
        hideAssistantPanelLayout();
        hideAudioLayout();

        messageEditText.requestFocus();
        uiHandler.postDelayed(showEmojiRunnable, 200);
        emoticonPickerView.setVisibility(View.VISIBLE);
        emoticonPickerView.show(this);
        container.proxy.onInputPanelExpand();
    }

    // 初始化更多布局
    private void addActionPanelLayout() {
        if (actionPanelBottomLayout == null) {
            View.inflate(container.getContext(), R.layout.nim_message_activity_actions_layout, messageActivityBottomLayout);
            actionPanelBottomLayout = view.findViewById(R.id.actionsLayout);
            actionPanelBottomLayoutHasSetup = false;
        }
        initActionPanelLayout();
    }

    /**
     * 群助手
     */
    private void addAssistantPanelLayout() {
//        if (assistantPanelBottomLayout == null) {
//            View.inflate(container.activity, R.layout.nim_message_activity_assistant_layout, messageActivityBottomLayout);
//            assistantPanelBottomLayout = view.findViewById(R.id.assistantLayout);
//            assistantPanelBottomLayout.setClickable(true);
//            assistBtn = assistantPanelBottomLayout.findViewById(R.id.conversationGroupAssistBtn);
//            assistBtn.setOnClickListener(view -> {
//                NavUtils.gotoSearchGroupAidesActivity(container.activity, container.account, "");
//                MsgMgr.getInstance().delay(() -> hideAssistantPanelLayout(), 500);
//            });
//            assistantPanelBottomLayoutHasSetup = false;
//            List<GroupMemberBean> groupMemberInfos = MemberDbHelper.getInstance().getAll(container.account);
//            if (groupMemberInfos.size() > 0 && isRole(groupMemberInfos)) {
//                assistBtn.setVisibility(View.VISIBLE);
//            }
//        }
//        initAssistantPanelLayout();
    }

    private boolean isRole(List<GroupMemberBean> groupMemberInfos) {
        boolean isRole = false;
        for (GroupMemberBean memberInfo : groupMemberInfos) {
            if (ModuleMgr.getCenterMgr().getUID().equals(memberInfo.uid) && memberInfo.isGroupMaster()) {
                isRole = true;
                break;
            }
        }
        return isRole;
    }

    // 显示键盘布局
    private void showInputMethod(EditText editTextMessage) {
        editTextMessage.requestFocus();
        //如果已经显示,则继续操作时不需要把光标定位到最后
        if (!isKeyboardShowed) {
            editTextMessage.setSelection(editTextMessage.getText().length());
            isKeyboardShowed = true;
        }

        InputMethodManager imm = (InputMethodManager) container.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextMessage, 0);

        container.proxy.onInputPanelExpand();
    }

    // 显示更多布局
    private void showActionPanelLayout() {
        addActionPanelLayout();
        hideAssistantPanelLayout();
        hideEmojiLayout();
        hideInputMethod();

        uiHandler.postDelayed(showMoreFuncRunnable, SHOW_LAYOUT_DELAY);
        container.proxy.onInputPanelExpand();
    }

    // 助手布局
    private void showAssistantPanelLayout() {
        addAssistantPanelLayout();
        hideEmojiLayout();
        hideInputMethod();
        hideActionPanelLayout();

        uiHandler.postDelayed(assistantFuncRunnable, SHOW_LAYOUT_DELAY);
        container.proxy.onInputPanelExpand();
    }

    // 初始化具体more layout中的项目
    private void initActionPanelLayout() {
        if (actionPanelBottomLayoutHasSetup) {
            return;
        }

        ActionsPanel.init(view, actions);
        actionPanelBottomLayoutHasSetup = true;
    }

    // 初始化具体more layout中的项目
    private void initAssistantPanelLayout() {
        if (assistantPanelBottomLayoutHasSetup) {
            return;
        }

//        ActionsPanel.init(view, actions);
        assistantPanelBottomLayoutHasSetup = true;
    }

    private Runnable showEmojiRunnable = new Runnable() {
        @Override
        public void run() {
            emoticonPickerView.setVisibility(View.VISIBLE);
        }
    };

    private Runnable showMoreFuncRunnable = new Runnable() {
        @Override
        public void run() {
            actionPanelBottomLayout.setVisibility(View.VISIBLE);
        }
    };

    private Runnable assistantFuncRunnable = new Runnable() {
        @Override
        public void run() {
            assistantPanelBottomLayout.setVisibility(View.VISIBLE);
        }
    };

    private Runnable showTextRunnable = new Runnable() {
        @Override
        public void run() {
            showInputMethod(messageEditText);
        }
    };

    private void restoreText(boolean clearText) {
        if (clearText) {
            messageEditText.setText("");
            // 发送完成后，清除草稿
            DraftMgr.getInstance().deleteDraft(container.account);
            MsgMgr.getInstance().sendMsg(MsgType.MT_DRAFT_STATUS, container.account);
        }

        checkSendButtonEnable(messageEditText);
    }

    /**
     * 显示发送或更多
     */
    private void checkSendButtonEnable(EditText editText) {
        if (isRobotSession) {
            return;
        }
        String textMessage = editText.getText().toString();
        if (!TextUtils.isEmpty(StringUtil.removeBlanks(textMessage)) && editText.hasFocus()) {
            moreFuntionButtonInInputBar.setVisibility(View.GONE);
            sendMessageButtonInInputBar.setVisibility(View.VISIBLE);
        } else {
            sendMessageButtonInInputBar.setVisibility(View.GONE);
            moreFuntionButtonInInputBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEmojiClick(String emoji) {
        Editable mEditable = messageEditText.getText();
        int start = messageEditText.getSelectionStart();
        int end = messageEditText.getSelectionEnd();
//        AndroidEmoji.ensure(emoji);
        mEditable.replace(start, end, AndroidEmoji.ensure(emoji));
//        messageEditText.getText().update(start, emoji);
    }

    @Override
    public void onDeleteClick() {
        messageEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }

    /**
     * *************** IEmojiSelectedListener ***************
     */
//    @Override
//    public void onEmojiSelected(String key) {
//        Editable mEditable = messageEditText.getText();
//        if (key.equals("/DEL")) {
//            messageEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
//        } else {
//            int start = messageEditText.getSelectionStart();
//            int end = messageEditText.getSelectionEnd();
//            start = (start < 0 ? 0 : start);
//            end = (start < 0 ? 0 : end);
//            mEditable.replace(start, end, key);
//        }
//    }


    private Runnable hideAllInputLayoutRunnable;

//    @Override
//    public void onStickerSelected(String category, String item) {
//        Log.i("InputPanel", "onStickerSelected, category =" + category + ", sticker =" + item);
//
////        if (customization != null) {
////            MsgAttachment shareMessage = customization.createStickerAttachment(category, item);
////            IMMessage stickerMessage = MessageBuilder.createCustomMessage(container.account, container.sessionType, "贴图消息", shareMessage);
////            container.proxy.sendMessage(stickerMessage);
////        }
//    }

    @Override
    public void onTextAdd(String content, int start, int length) {
        if (messageEditText.getVisibility() != View.VISIBLE ||
                (emoticonPickerView != null && emoticonPickerView.getVisibility() == View.VISIBLE)) {
            switchToTextLayout(true);
        } else {
            uiHandler.postDelayed(showTextRunnable, SHOW_LAYOUT_DELAY);
        }
        messageEditText.getEditableText().insert(start, content);
    }

    @Override
    public void onTextDelete(int start, int length) {
        if (messageEditText.getVisibility() != View.VISIBLE) {
            switchToTextLayout(true);
        } else {
            uiHandler.postDelayed(showTextRunnable, SHOW_LAYOUT_DELAY);
        }
        int end = start + length - 1;
        messageEditText.getEditableText().replace(start, end, "");
    }

    /**
     * 隐藏所有输入布局
     */
    private void hideAllInputLayout(boolean immediately) {
        if (hideAllInputLayoutRunnable == null) {
            hideAllInputLayoutRunnable = () -> {
                hideInputMethod();
                hideActionPanelLayout();
                hideEmojiLayout();
                hideAssistantPanelLayout();
            };
        }
        long delay = immediately ? 0 : ViewConfiguration.getDoubleTapTimeout();
        uiHandler.postDelayed(hideAllInputLayoutRunnable, delay);
    }


    // 上滑取消录音判断
    private static boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        if (event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth()
                || event.getRawY() < location[1] - 40) {
            return true;
        }

        return false;
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        int index = (requestCode << 16) >> 24;
        if (index != 0) {
            index--;
            if (index < 0 | index >= actions.size()) {
                LogUtil.d(TAG, "request code out of actions' range");
                return;
            }
            BaseAction action = actions.get(index);
            if (action != null) {
                action.onActivityResult(requestCode & 0xff, resultCode, data);
            }
        }
    }

    public void switchRobotMode(boolean isRobot) {
        isRobotSession = isRobot;
        if (isRobot) {
            textAudioSwitchLayout.setVisibility(View.GONE);
            emojiButtonInInputBar.setVisibility(View.GONE);
            sendMessageButtonInInputBar.setVisibility(View.VISIBLE);
            moreFuntionButtonInInputBar.setVisibility(View.GONE);
        } else {
            textAudioSwitchLayout.setVisibility(View.VISIBLE);
            emojiButtonInInputBar.setVisibility(View.VISIBLE);
            sendMessageButtonInInputBar.setVisibility(View.GONE);
            moreFuntionButtonInInputBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_CONVERSATION_GROUP_ASSIST:
                showAssistantPanelLayout();
                break;
            default:
                break;
        }
    }

}
