package com.wd.daquan.imui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dq.im.bean.im.MessageRedPackageBean;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.wd.daquan.R;
import com.wd.daquan.bean.ChatBottomMoreBean;
import com.wd.daquan.imui.activity.RedPackageSendActivity;
import com.wd.daquan.imui.adapter.ChatBottomAdapter;
import com.wd.daquan.imui.constant.Constant;
import com.wd.daquan.imui.constant.IntentCode;
import com.wd.daquan.imui.type.RecordAudioStatus;
import com.wd.daquan.util.audiorecord.AudioRecoderDialog;
import com.zlw.main.recorderlib.RecordManager;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 聊天底部操作Fragment
 */
public class ChatBottomFragment extends BaseFragment{
    public static final String CHAT_BOTTOM_TYPE = "chatBottomType";//0 个人，1 群组
    private EditText chatEdt;//输入框
    private View chatSend;//发送内容
    private Button chatBottomMore;//更多按钮
    private TextView chatBottomAudio;//录音功能
    private RecyclerView chatBottomMoreRv;//聊天布局中的个更多内容，采用网格布局进行添加内容
    private long downT;//按下的时间
    private AudioRecoderDialog recodeDialog;
    private RecordAudioStatus recordVoiceState = RecordAudioStatus.RECORD_INIT;//录音状态，-1，待准备，0，开始，1，停止，2撤销
    private ChatBottomOperatorIml chatBottomOperatorIml;
    private ChatBottomAdapter chatBottomAdapter;
    private String chatBottomType = "1";//1 个人，2 群组
    @Override
    public void onAttach(@NonNull Context context) {//这里不能对上下文进行监听，否则的话，Activity的类必须实现该接口，而不能采取内部类的方式实现
        super.onAttach(context);
//        if (context instanceof ChatBottomOperatorIml){
//            chatBottomOperatorIml = (ChatBottomOperatorIml)context;
//        }else {
//            throw new RuntimeException(context.toString() + " must implement ChatBottomOperatorIml");
//        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_bottom,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initOnClickListener();
        initRecycleViewData();
        initAudioRecord();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData(){
        chatBottomType = getArguments().getString(CHAT_BOTTOM_TYPE,"1");
    }

    private void initView(View view){
        chatEdt = view.findViewById(R.id.chat_edt);
        chatSend = view.findViewById(R.id.chat_send);
        chatBottomMoreRv = view.findViewById(R.id.chat_bottom_more_rv);
        chatBottomMore = view.findViewById(R.id.chat_bottom_more);
        chatBottomAudio = view.findViewById(R.id.chat_bottom_audio);
        initGridRecycleView(chatBottomMoreRv);
    }

    private void initRecycleViewData(){
        chatBottomAdapter = new ChatBottomAdapter();
        chatBottomMoreRv.setAdapter(chatBottomAdapter);
        chatBottomAdapter.setChatBottomMoreBeans(createBottomData());
        chatBottomAdapter.setRecycleItemOnClickListener((view, position) -> {
            switch (position){
                case 0:
                    Toast.makeText(view.getContext(),"照片",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    checkPermissions();
                    break;
                case 2:
                    selectVideoIntent();
                    break;
                case 3:
                    sendRedPackage();
                    break;
            }
        });
    }

    private List<ChatBottomMoreBean> createBottomData(){
        List<ChatBottomMoreBean> chatBottomMoreBeans = Arrays.asList(new ChatBottomMoreBean(-1,"照片"),
                new ChatBottomMoreBean(-1,"录像"),
                new ChatBottomMoreBean(-1,"视频"),
                new ChatBottomMoreBean(-1,"红包")
        );
        return chatBottomMoreBeans;
    }

    private void initOnClickListener(){
        chatSend.setOnClickListener(this::onClick);
        chatBottomMore.setOnClickListener(this::onClick);
        chatBottomAudio.setOnTouchListener(this::onTouch);
    }
    /**
     * 录制音频的对话框
     */
    private void initAudioRecord(){
        recodeDialog = new AudioRecoderDialog(getContext());
        recodeDialog.setShowAlpha(0.98f);
        RecordManager.getInstance().setRecordSoundSizeListener(soundSize -> {
            if(null != recodeDialog) {
                recodeDialog.setLevel(soundSize);
                recodeDialog.setTime(System.currentTimeMillis() - downT);
            }
        });
        RecordManager.getInstance().setRecordResultListener(result -> {
            if (recordVoiceState == RecordAudioStatus.RECORD_STOP){
                if (null != chatBottomOperatorIml){
                    chatBottomOperatorIml.saveAudioFile(result);
                }
            }else {//撤销时候删除
                result.delete();
            }
            recordVoiceState = RecordAudioStatus.RECORD_INIT;
        });
    }
    private void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_send:
                if (null != chatBottomOperatorIml) {
                    chatBottomOperatorIml.sendTextMessage(chatEdt.getText().toString());
                    chatEdt.setText("");
                }
                break;
            case R.id.chat_bottom_more:
                if (View.VISIBLE == chatBottomMoreRv.getVisibility()){
                    chatBottomMoreRv.setVisibility(View.GONE);
                }else {
                    chatBottomMoreRv.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private boolean onTouch(View view, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                recordVoiceState = RecordAudioStatus.RECORD_START;
                RecordManager.getInstance().changeRecordDir(Constant.getAudioRecordDir());
                Log.e("YM","按下");
                RecordManager.getInstance().start();
                downT = System.currentTimeMillis();
                recodeDialog.showAtLocation(view, Gravity.CENTER, 0, 0);
                chatBottomAudio.setBackgroundResource(R.drawable.shape_recoder_btn_recoding);
                return true;
            case MotionEvent.ACTION_UP:
                recordVoiceState = RecordAudioStatus.RECORD_STOP;
                RecordManager.getInstance().stop();
                recodeDialog.dismiss();
                chatBottomAudio.setBackgroundResource(R.drawable.shape_recoder_btn_normal);
                return true;
            case MotionEvent.ACTION_CANCEL://撤销
                Toast.makeText(getContext(),"取消录音",Toast.LENGTH_SHORT).show();
                recordVoiceState = RecordAudioStatus.RECORD_CANCEL;
                RecordManager.getInstance().stop();
                recodeDialog.dismiss();
                chatBottomAudio.setBackgroundResource(R.drawable.shape_recoder_btn_normal);
                return true;
        }
        return false;
    }

    public void setOnChatBottomOperatorListener(ChatBottomOperatorIml chatBottomOperatorIml) {
        this.chatBottomOperatorIml = chatBottomOperatorIml;
    }

    /**
     * 底部操作的接口回调
     */
    public interface ChatBottomOperatorIml{
        /**
         * 选择图片后的文件uri
         * @param uri
         */
        void savePictureFile(Uri uri);

        /**
         * 录音成功后保存的文件
         */
        void saveAudioFile(File file);

        /**
         * 拍摄视频后返回的uri路径
         * @param uri
         */
        void saveVideoFile(Uri uri);

        /**
         * 发送文本消息时填写的内容
         */
        void sendTextMessage(String message);

        /**
         * 发红包的金额
         */
        void sendRedPackage(MessageRedPackageBean messageRedPackageBean);


    }

    //当使用getActivity作为Context启动onActivityForResult时候，Activity可以监听到onActivityResult，Fragment监听不到
    //当使用Fragment作为Context启动onActivityForResult时候，Fragment可以监听到onActivityResult，Activity监听到的requestCode为65539
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        Log.e("YM","Fragment返回的resultCode值:"+resultCode+"----->requestCode:"+requestCode);
        if (requestCode == IntentCode.REQUEST_CODE_CHOOSE) {
//            Matisse.obtainResult(data), Matisse.obtainPathResult(data);
//            List<String> picturePathString = Matisse.obtainPathResult(data);//获取string路径
//            List<Uri> picturePath = Matisse.obtainResult(data);//获取uri路径
//            if (null != chatBottomOperatorIml){
//                chatBottomOperatorIml.savePictureFile(picturePath.get(0));
//            }
        }else if (requestCode == IntentCode.REQUEST_VIDEO_CAPTURE) {
            Uri videoUri = data.getData();
            if (null != chatBottomOperatorIml){
                chatBottomOperatorIml.saveVideoFile(videoUri);
            }
//            MediaMetadataRetriever media = new MediaMetadataRetriever();
//            String videoPath = videoUri.getPath();            // 通过Uri获取绝对路径
//            media.setDataSource(videoPath);
//            Bitmap bitmap = media.getFrameAtTime();      // 视频的第一帧图片
        }else if (requestCode == IntentCode.REQUEST_RED_PACKAGE_SEND){
            MessageRedPackageBean redPackageAmount = (MessageRedPackageBean)data.getSerializableExtra(RedPackageSendActivity.RED_PACKAGE_DATA);
            if (null == redPackageAmount){
                return;
            }
            if (null != chatBottomOperatorIml){
                chatBottomOperatorIml.sendRedPackage(redPackageAmount);
            }
        }
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
            startActivityForResult(takeVideoIntent, IntentCode.REQUEST_VIDEO_CAPTURE);
        }
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
        intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）

        // intent.setType("video/*;image/*");//同时选择视频和图片

        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IntentCode.REQUEST_VIDEO_CAPTURE);
    }

    private void sendRedPackage(){
        Intent intent = new Intent(getContext(), RedPackageSendActivity.class);
        intent.putExtra(RedPackageSendActivity.RED_PACKAGE_TYPE,chatBottomType);
        startActivityForResult(intent,IntentCode.REQUEST_RED_PACKAGE_SEND);
    }

    /**
     * 获取音视频时长
     */
    public static String getRingDuring(String mUri){
        String duration=null;
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();

        try {
            if (mUri != null) {
                HashMap<String, String> headers=null;
                headers = new HashMap<>();
                headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                mmr.setDataSource(mUri, headers);
            }

            duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mmr.release();
        }
        Log.e("ryan","duration "+duration);
        return duration;
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
}