package com.wd.daquan;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.da.library.constant.IConstant;
import com.da.library.tools.Utils;
import com.da.library.utils.DateUtil;
import com.da.library.widget.DragPointView;
import com.da.library.widget.MainLoading;
import com.dq.im.DqWebSocketClient;
import com.dq.im.bean.im.MessageTextBean;
import com.dq.im.constants.URLUtil;
import com.dq.im.ipc.DqWebSocketListener;
import com.dq.im.model.HomeImBaseMode;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.TeamMessageBaseModel;
import com.dq.im.model.TeamModel;
import com.dq.im.model.UserModel;
import com.dq.im.parser.ImParserUtils;
import com.dq.im.parser.ImTransformUtils;
import com.dq.im.third_system.HwPushManager;
import com.dq.im.third_system.OppoPushManager;
import com.dq.im.third_system.ThirdSystemRegisterResultIml;
import com.dq.im.third_system.ThirdSystemType;
import com.dq.im.third_system.ViVoPushManager;
import com.dq.im.third_system.XiaoMiPushManager;
import com.dq.im.type.ImType;
import com.dq.im.type.MessageSendType;
import com.dq.im.type.MessageType;
import com.wd.daquan.explore.ExploreFragment;
import com.wd.daquan.imui.type.SourceType;
import com.dq.im.util.Rom;
import com.dq.im.util.SoundPoolUtils;
import com.dq.im.util.VibratorUtil;
import com.dq.im.util.notification.NotificationUtil;
import com.dq.im.viewmodel.ApplicationViewModel;
import com.dq.im.viewmodel.HomeMessageViewModel;
import com.dq.im.viewmodel.P2PMessageViewModel;
import com.dq.im.viewmodel.TeamMessageViewModel;
import com.google.gson.Gson;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.support.permission.MPermission;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionNeverAskAgain;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.chat.DqFragment;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.helper.UpdateHelper;
import com.wd.daquan.common.helper.UpdateListener;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.ForegroundCallbacks;
import com.wd.daquan.contacts.fragment.ContactsFragment;
import com.wd.daquan.contacts.listener.ISidebarListener;
import com.wd.daquan.http.HttpBaseBean;
import com.wd.daquan.http.HttpResultResultCallBack;
import com.wd.daquan.http.ImSdkHttpUtils;
import com.wd.daquan.imui.bean.MessageSystemBean;
import com.wd.daquan.imui.convert.DqImParserUtils;
import com.wd.daquan.imui.type.DqMessageSendType;
import com.wd.daquan.mine.MainTitleLeftContainer;
import com.wd.daquan.mine.fragment.MineFragment;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.model.bean.UpdateEntity;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.db.helper.MemberDbHelper;
import com.wd.daquan.model.db.helper.TeamDbHelper;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.mgr.ConfigManager;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.third.helper.TeamHelper;
import com.wd.daquan.third.helper.UserInfoHelper;
import com.wd.daquan.third.reminder.ReminderId;
import com.wd.daquan.third.reminder.ReminderItem;
import com.wd.daquan.third.reminder.ReminderManager;
import com.wd.daquan.third.session.extension.CustomAttachmentType;
import com.wd.daquan.third.session.extension.RedRainSystemAttachment;
import com.wd.daquan.util.AESUtil;
import com.wd.daquan.util.ApplicationDialog;
import com.wd.daquan.util.IntentUtils;
import com.wd.daquan.util.TToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MainActivity extends DqBaseActivity<ChatPresenter, DataBean> implements View.OnClickListener, ViewPager.OnPageChangeListener, ReminderManager.UnreadNumChangedCallback, DragPointView.OnDragListencer, QCObserver, UpdateListener {

    public static final String TAG = "qc_log";
    private Handler handler = new Handler(Looper.getMainLooper());

    private List<Fragment> mFragmentList = new ArrayList<>();

    //    private CommTitle mCommTitle;
    private ImageView mMineRed;
    private TextView mTextChats;
    private TextView mTextContact;
    private TextView mTextDiscover;
    private TextView mTextShop;

    private ViewPager mViewPager = null;
    private DqFragment mDqFragment = null;
    private ContactsFragment mContactsFragment = null;
    //    private NewsFragment mDiscoverFragment = null;
//    private ShoppingFragment mShoppingFragment = null;
    private MineFragment mMineFragment = null;
    private ExploreFragment exploreFragment = null;

    private DragPointView mQingchatUnread = null;
    private DragPointView mContactUnread = null;
    private DrawerLayout mDrawerLayout;
    private MainTitleLeftContainer mLeftContainer;
    private UpdateHelper mUpdateHelper;//版本更新
    private SoundPool mSoundPool;
    /**
     * 基本权限管理
     */
    protected String[] needPermissions = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储卡写入权限
            android.Manifest.permission.READ_EXTERNAL_STORAGE,//存储卡读取权限
            Manifest.permission.READ_PHONE_STATE,//手机状态
    };

    private DqWebSocketClient dqWebSocketClient;
    private P2PMessageViewModel p2PMessageViewModel;
    private TeamMessageViewModel teamMessageViewModel;
    private HomeMessageViewModel homeMessageViewModel;
    private UserModel userModel;
    private TeamModel teamModel;
    private List<String> requestUserIds = new ArrayList<>();//临时存储正在请求的用户数据,防止同时多次请求同一个用户的数据
    private List<String> requestTeamIds = new ArrayList<>();//临时存储正在请求的群组数据,防止同时多次请求同一个群组的数据
    private VibratorUtil vibratorUtil;
    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void init() {
        requestBasicPermission();
        initIntent();
        // 同步数据
//        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        Log.e("YM","获取设备唯一Android_ID:"+androidId);
        // 检查版本升级
        mUpdateHelper = new UpdateHelper(this);
        mUpdateHelper.setUpdateListener(this);
        if (DqUtils.checkPermissions(this, needPermissions)) {
            mUpdateHelper.checkVersion();
            Log.e("YM","检查更新");
        }
        initViewModel();
        startSocket();
        initVibratorUtil();
        requestLogin(ModuleMgr.getCenterMgr().getUID());
        parserIntent();
    }

    private void initViewModel(){
        new ViewModelProvider(this).get(ApplicationViewModel.class).initRoomDataBase(ModuleMgr.getCenterMgr().getUID());
        teamMessageViewModel = new ViewModelProvider(this).get(TeamMessageViewModel.class);
        homeMessageViewModel = new ViewModelProvider(this).get(HomeMessageViewModel.class);
        p2PMessageViewModel = new ViewModelProvider(this).get(P2PMessageViewModel.class);
    }

    private void parserIntent(){
        String sourceType = getIntent().getStringExtra(Extras.EXTRA_SOURCE);
        clearNotification(sourceType);
    }
    private void clearNotification(String sourceType){
        if (!SourceType.SOURCE_TYPE_NOTIFICATION.equals(sourceType)){
            return;
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    /**
     * 初始化震动
     */
    private void initVibratorUtil(){
        vibratorUtil = new VibratorUtil((Vibrator)getSystemService(Service.VIBRATOR_SERVICE));
    }
    private void initIntent() {
        // TODO: 2018/9/6 处理分享／消息推送数据，直接跳转页面
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // TODO: 2018/9/6 处理再次打开首页逻辑
        setIntent(intent);
        init();
        initData();
        initListener();
        if(mContactsFragment != null) {
            //mContactsFragment.getContacts(false);
            mContactsFragment.useCacheData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    @Override
    public void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mViewPager = findViewById(R.id.main_viewpager);

//        mCommTitle = findViewById(R.id.main_commtitle);
//        mCommTitle.setRightImageResource(R.mipmap.qc_comm_add);
//        mCommTitle.getLeftIv().setVisibility(View.VISIBLE);
//        mCommTitle.setLeftImageResource(R.mipmap.qc_comm_contacts);

        mTextChats = findViewById(R.id.tab_text_chats);
        mTextContact = findViewById(R.id.tab_text_contact);
        mTextShop = findViewById(R.id.tab_text_me);
        mTextDiscover = findViewById(R.id.tab_text_news);
        mMineRed = findViewById(R.id.mine_red);

        mQingchatUnread = findViewById(R.id.tab_text_chats_unread);
        mContactUnread = findViewById(R.id.tab_text_contact_unread);

        initFragment();

        mLeftContainer = new MainTitleLeftContainer(this, mDrawerLayout);

    }

    private void initFragment() {
        mDqFragment = new DqFragment();
        mContactsFragment = new ContactsFragment();
        mMineFragment = new MineFragment();
//        mDiscoverFragment = new NewsFragment();
//        mShoppingFragment = new ShoppingFragment();
        exploreFragment = new ExploreFragment();
        mFragmentList.clear();
        mFragmentList.add(mDqFragment);
        mFragmentList.add(mContactsFragment);
        mFragmentList.add(exploreFragment);
//        mFragmentList.add(mDiscoverFragment);
//        mFragmentList.add(mShoppingFragment);
        mFragmentList.add(mMineFragment);

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        //已过时
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(fragmentPagerAdapter.getCount());

    }

    @Override
    public void initListener() {
        findViewById(R.id.seal_chat).setOnClickListener(this);
        findViewById(R.id.seal_contact_list).setOnClickListener(this);
        findViewById(R.id.seal_discover).setOnClickListener(this);
        findViewById(R.id.seal_me).setOnClickListener(this);

        mQingchatUnread.setDragListencer(this);

        registerObserver();

        MsgMgr.getInstance().attach(this);
        mDqFragment.setNavigationClickListener(() -> mDrawerLayout.openDrawer(Gravity.LEFT));
    }

    @Override
    public void initData() {
        changeTextViewColor();
        changeSelectedTabState(0);
//        ModuleMgr.getCommonMgr().reqServerConfig(DqUrl.url_server_config);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.seal_chat == id) {
            int currentItemIndex = mViewPager.getCurrentItem();
            if (0 == currentItemIndex){
                goChatUnReadMessage();
            }else {
                mViewPager.setCurrentItem(0, false);
            }
        } else if (R.id.seal_contact_list == id) {
            mViewPager.setCurrentItem(1, false);
        }
        else if (R.id.seal_discover == id) {
            mViewPager.setCurrentItem(2, false);
        }
        else if (R.id.seal_me == id) {
            mViewPager.setCurrentItem(3, false);
            mMineRed.setVisibility(View.GONE);
        }

//        else if (mCommTitle.getRightIvId() == id) {
//            showMoreDialog();
//        } else if (mCommTitle.getLeftIvId() == id) {
//
//            mDrawerLayout.openDrawer(Gravity.LEFT);
//        }
    }

    /**
     * 跳转到未读消息条目
     */
    private void goChatUnReadMessage(){
        mDqFragment.goChatUnReadMessage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerMsgUnreadInfoObserver(false);
        MsgMgr.getInstance().detach(this);
        if (null != mLoading) {
            mLoading.dismiss();
        }
        if(mLeftContainer != null) {
            mLeftContainer.onDestroy();
        }
        SoundPoolUtils.getInstance(this).releaseSoundPool();
    }


    private void registerObserver() {
        registerMsgUnreadInfoObserver(true);
    }

    private MainLoading mLoading = null;


    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int BASIC_PERMISSION_REQUEST_CODE = 100;
    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(MainActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
//        try {
//            Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        try {
            Toast.makeText(this, "未全部授权，部分功能可能无法正常运行！", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTextViewColor();
        changeSelectedTabState(position);
    }


    /**
     * 解决viewpager和联系人中sidebar冲突问题，接口回调
     * @param status 滑动的状态
     */
    private ISidebarListener mSidebarListener;
    public void setSidebarListener(ISidebarListener listener) {
        this.mSidebarListener = listener;
    }
    @Override
    public void onPageScrollStateChanged(int state) {
        if(null == mSidebarListener) return;

        switch (state) {
            //空闲
            case ViewPager.SCROLL_STATE_IDLE:
                //滑动结束
            case ViewPager.SCROLL_STATE_SETTLING:
                mSidebarListener.scroll(false);
                break;
            //开始滑动
            case ViewPager.SCROLL_STATE_DRAGGING:
                mSidebarListener.scroll(true);
                break;
        }
    }

    private void changeTextViewColor() {
        mTextChats.setSelected(false);
        mTextContact.setSelected(false);
        mTextShop.setSelected(false);
        mTextDiscover.setSelected(false);
    }

    private void changeSelectedTabState(int position) {
        switch (position) {
            case 0:
                // TODO: 2018/9/5 之后替换成，根据域名显示提示信息
                String appName = checkUrl(getResources().getString(R.string.main_tab_chat));
//                mCommTitle.setTitle(appName);
                mTextChats.setSelected(true);
                mViewPager.setCurrentItem(0, false);
//                mCommTitle.getLeftIv().setVisibility(View.VISIBLE);
//                mCommTitle.getRightIv().setVisibility(View.VISIBLE);
//                mCommTitle.setRightImageResource(R.mipmap.qc_comm_add);
                break;
            case 1:
//                mCommTitle.setTitle(getString(R.string.main_tab_contacts));
                mTextContact.setSelected(true);
                mViewPager.setCurrentItem(1, false);
//                mCommTitle.getRightIv().setVisibility(View.VISIBLE);
//                mCommTitle.getLeftIv().setVisibility(View.GONE);
//                mCommTitle.setRightImageResource(R.mipmap.qc_comm_add);
                break;
            case 2:
//                mCommTitle.setTitle(getString(R.string.main_tab_discover));
//                mCommTitle.getRightIv().setVisibility(View.GONE);
                mViewPager.setCurrentItem(2, false);
                mTextDiscover.setSelected(true);
                break;
//            case 2:
////                mCommTitle.setTitle("资讯");
////                mCommTitle.getRightIv().setVisibility(View.GONE);
////                mCommTitle.getLeftIv().setVisibility(View.GONE);
//                mTextDiscover.setSelected(true);
//                break;
            case 3:
//                mCommTitle.setTitle(getString(R.string.main_tab_mine));
//                mCommTitle.getRightIv().setVisibility(View.GONE);
//                mCommTitle.getLeftIv().setVisibility(View.GONE);
                mTextShop.setSelected(true);
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    private String checkUrl(String appName) {
        if (!DqUrl.SERVER.equals("https://x1.meetsn.com/")) {
            return appName + "(测试)";
        } else {
            return appName;
        }
    }


    /**
     * 注册未读消息数量观察者
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
        }
    }


    @Override
    public void onUnreadNumChanged(ReminderItem item) {
//        Log.w(TAG, "onUnreadNumChanged = " + item.toString());
        if (ReminderId.SESSION == item.getId()) {
//            int unread = item.unread();
//            if (unread <= 0) {
//                mQingchatUnread.setText("");
//                mQingchatUnread.setVisibility(View.INVISIBLE);
//                return;
//            } else if (unread < 99) {
//                mQingchatUnread.setVisibility(View.VISIBLE);
//                mQingchatUnread.setText(""+unread);
//            } else {
//                mQingchatUnread.setVisibility(View.VISIBLE);
//                mQingchatUnread.setText("99+");
//            }
            int unread = item.unread();
            String target = Utils.formatUnread(unread);
            if (TextUtils.isEmpty(target)) {
                mQingchatUnread.setText("");
                mQingchatUnread.setVisibility(View.INVISIBLE);
            } else {
                mQingchatUnread.setText(target);
                mQingchatUnread.setVisibility(View.VISIBLE);
            }
            ModuleMgr.getAppManager().setMessageUnread(unread);
        } else if (ReminderId.CONTACT == item.getId()) {

        }
    }

    /**
     * 更新联系人显示状态
     */
    private void updateContacts() {
        if (mContactsFragment == null) {
            return;
        }
        int unread = ModuleMgr.getAppManager().getUnreadNotifyCount();
        String target = Utils.formatUnread(unread);
        if (TextUtils.isEmpty(target)) {
            mContactUnread.setText("");
            mContactsFragment.updateNotify("");
            mContactUnread.setVisibility(View.INVISIBLE);
        } else {
            mContactUnread.setText(target);
            mContactsFragment.updateNotify(target);
            mContactUnread.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDragOut() {
        homeMessageViewModel.updateAllUnReadNumber(0);
    }

    @Override
    public void onMessage(String key, Object value) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (MsgType.MT_CONTACT_UNREAD.equals(key)) {
            if (mContactUnread != null) {
                mContactUnread.setText("");
                mContactUnread.setVisibility(View.INVISIBLE);
            }
        } else if (MsgType.MT_CONTACT_NOTIFY.equals(key)) {
            updateContacts();
        } else if (KeyValue.SDK_MAIN_FINISH.equals(key)) {
            Log.d("clll", "main----finish");
            finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            startHome();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void startHome() {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateError() {

    }

    @Override
    public void updateFailed(String msg) {
    }

    @Override
    public void updateSucceed(UpdateEntity updateEntity) {
        ConfigManager.getInstance().saveRedEnvelopedRainSwitch(updateEntity.redEnvelopedRainSwitch);
        MsgMgr.getInstance().sendMsg(MsgType.UPDATE_READ_PACKAGE_RAIN_LABEL, null);
    }

    @Override
    public void updateUI(String status) {

    }
    private void startSocket(){
        Log.e("YM","开始链接");
        dqWebSocketClient = DqWebSocketClient.getInstance2();
        if (null == dqWebSocketClient){
            dqWebSocketClient = DqWebSocketClient.createSocketInstance(ModuleMgr.getCenterMgr().getUID());
        }else {
            dqWebSocketClient.switchUserId(ModuleMgr.getCenterMgr().getUID());
        }
        initThirdSystemPush();
        dqWebSocketClient.setDqWebSocketListener(new DqWebSocketListener() {
            @Override
            public void connectFail(String fileConnect) {
            }

            @Override
            public void connectSuccess() {

            }

            @Override
            public void connectClose() {
            }

            @Override
            public void onMessageReceiver(final String message) {
//                        Toast.makeText(MainActivity.this,"接收的消息:"+message,Toast.LENGTH_LONG).show();
                Log.e("YM","MainActivity接收的原始消息:"+message);
//                ImMessageBaseModel notificationModel = ImParserUtils.getBaseMessageModel(message);
                ImMessageBaseModel notificationModel = DqImParserUtils.getBaseMessageModel(message);
                Log.e("YM","MainActivity转换的类型:"+notificationModel.toString());
                if (ImType.P2P.getValue().equals(notificationModel.getType())){
                    String dqImContentJson = GsonUtils.toJson(notificationModel);
                    Log.e("YM","转换过的JSON消息格式:"+dqImContentJson);
                    if (isSameUser(dqImContentJson)){//消息来源是当前用户则不再向下传递
                        return;
                    }
                    if (!notificationModel.getMsgType().equals(MessageType.SYSTEM.getValue())){
                        showNotification(notificationModel);
                    }
                    P2PMessageBaseModel p2PMessageBaseModel = ImParserUtils.getP2PMessageBaseModel(dqImContentJson);
                    p2PMessageBaseModel.setToUserId(ModuleMgr.getCenterMgr().getUID());
                    p2PMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_SUCCESS.getValue());
                    HomeImBaseMode homeImBaseMode = ImTransformUtils.p2pImModelTransformHomeImModel(p2PMessageBaseModel);
                    insertUser(p2PMessageBaseModel.getFromUserId());
                    if (!MessageType.SYSTEM.getValue().equals(p2PMessageBaseModel.getMsgType())){//不插入数据库，因为系统消息没有发消息用户的Id
                        p2PMessageViewModel.insert(p2PMessageBaseModel);
                        homeMessageViewModel.updateHomeMessage(homeImBaseMode,true);
                    }

//                    if (MessageType.TEXT.getValue().equals(p2PMessageBaseModel.getMsgType())){
//                        MessageTextBean messageTextBean = GsonUtils.fromJson(p2PMessageBaseModel.getSourceContent(),MessageTextBean.class);
//                        Log.e("YM","MainActivity收到的文本消息内容:"+p2PMessageBaseModel.getSourceContent());
////                        String content = AESHelper.decryptString(messageTextBean.getDescription());
//                        String content = AESUtil.decode(messageTextBean.getDescription());
//                        Log.e("YM","MainActivity接收的文本消息:"+content);
//                    }

//                    EventBusBean eventBusBean = new EventBusBean();
//                    eventBusBean.setStatus(EventBusBeanType.TYPE_P2P);
//                    eventBusBean.setContent(p2PMessageBaseModel);
//                    EventBus.getDefault().post(eventBusBean);
                    MsgMgr.getInstance().sendMsg(MsgType.P2P_MESSAGE_CONTENT, p2PMessageBaseModel);
                }else if (ImType.Team.getValue().equals(notificationModel.getType())){
                    String dqImContentJson = GsonUtils.toJson(notificationModel);
                    Log.e("YM","转换过的JSON消息格式:"+dqImContentJson);
                    if (isSameUser(dqImContentJson)){//消息来源是当前用户则不再向下传递
                        return;
                    }

                    TeamMessageBaseModel teamMessageBaseModel = ImParserUtils.getTeamMessageBaseModel(dqImContentJson);

                    if (showNotificationByTeam(teamMessageBaseModel.getMsgType(),teamMessageBaseModel)){
                        return;
                    }
                    showNotification(teamMessageBaseModel);
                    teamMessageBaseModel.setToUserId(ModuleMgr.getCenterMgr().getUID());
                    teamMessageBaseModel.setMessageSendStatus(MessageSendType.SEND_SUCCESS.getValue());
                    HomeImBaseMode homeImBaseMode = ImTransformUtils.teamImModelTransformHomeImModel(teamMessageBaseModel);
                    insertUser(teamMessageBaseModel.getFromUserId());
                    insertTeam(teamMessageBaseModel.getGroupId());
                    if (!MessageType.SYSTEM.getValue().equals(teamMessageBaseModel.getMsgType())){//不插入数据库，因为系统消息没有发消息用户的Id
                        teamMessageViewModel.insert(teamMessageBaseModel);
                        homeMessageViewModel.updateHomeMessage(homeImBaseMode,true);
                        Log.e("YM","数据插入,更新群消息:");
                    }
//                            Intent intent = new Intent(MainActivity.this, ChatTeamActivity.class);
//                            intent.putExtra(ChatTeamActivity.TEAM_ID,teamMessageBaseModel.getGroupId());
//                            startActivity(intent);
//                    if (MessageType.TEXT.getValue().equals(teamMessageBaseModel.getMsgType())){
//                        MessageTextBean messageTextBean = GsonUtils.fromJson(teamMessageBaseModel.getSourceContent(),MessageTextBean.class);
//                        Log.e("YM","MainActivity收到的群组文本消息内容:"+teamMessageBaseModel.getSourceContent());
////                        String content = AESHelper.decryptString(messageTextBean.getDescription());
//                        String content = AESUtil.decode(messageTextBean.getDescription());
//                        Log.e("YM","MainActivity接收的群组文本消息:"+content);
//                    }
                    MsgMgr.getInstance().sendMsg(MsgType.TEAM_MESSAGE_CONTENT, teamMessageBaseModel);
                }else if (ImType.System_All.getValue().equals(notificationModel.getType())){
                    String result = parserSystemMessage(notificationModel);
                    showNotification(result);
                }else if (ImType.RECEIVE_MESSAGE_CALLBACK.getValue().equals(notificationModel.getType())){//消息发送后，服务器将消息状态回传过来并附加其他信息
                    homeMessageViewModel.updateHomeMessageByClientId(notificationModel);
                    if (notificationModel.getMsgSecondType().equals(ImType.P2P.getValue())){
                        p2PMessageViewModel.updateP2PMessageByClientId(notificationModel);
                    }else {
                        teamMessageViewModel.updateTeamPMessageByClientId(notificationModel);
                    }
                    int sendStatus = notificationModel.getMessageSendStatus();
                    DqMessageSendType dqMessageSendType = DqMessageSendType.typeOfValue(sendStatus);
                    MessageSendType messageSendType = MessageSendType.SEND_LOADING;
                    switch (dqMessageSendType){
                        case SEND_SUCCESS:
                            messageSendType = MessageSendType.SEND_SUCCESS;
                            break;
                        case SEND_FAIL:
                            messageSendType = MessageSendType.SEND_FAIL;
                            TToast.show(MainActivity.this,"被移除了群组，发送消息失败！");
                            break;
                    }
                    notificationModel.setMessageSendStatus(messageSendType.getValue());
                    MsgMgr.getInstance().sendMsg(MsgType.MESSAGE_RECEIVE_CALL_BACK, notificationModel);
                }
            }
        });
        dqWebSocketClient.build();
    }

    /**
     * 初始化第三方厂商推送
     */
    private void initThirdSystemPush(){
        XiaoMiPushManager.getXiaoMiPushManager().setThirdSystemRegisterResultIml(new ThirdSystemRegisterResultIml() {
            @Override
            public void registerResult(String thirdType, String regId) {
                uploadThirdRegisterMessage(ThirdSystemType.XIAO_MI,regId);
            }
        });
        HwPushManager.getHwPushManager().setThirdSystemRegisterResultIml(new ThirdSystemRegisterResultIml() {
            @Override
            public void registerResult(String thirdType, String regId) {
                uploadThirdRegisterMessage(ThirdSystemType.HUA_WEI,regId);
            }
        });
        ViVoPushManager.getViVoPushManager().setThirdSystemRegisterResultIml(new ThirdSystemRegisterResultIml() {
            @Override
            public void registerResult(String thirdType, String regId) {
                uploadThirdRegisterMessage(ThirdSystemType.VIVO,regId);
            }
        });
        OppoPushManager.getOppoPushManager().setThirdSystemRegisterResultIml(new ThirdSystemRegisterResultIml() {
            @Override
            public void registerResult(String thirdType, String regId) {
                uploadThirdRegisterMessage(ThirdSystemType.OPPO,regId);
            }
        });
        if (XiaoMiPushManager.getXiaoMiPushManager().isSupport()){
            XiaoMiPushManager.getXiaoMiPushManager().registerXiaoMiSystemReceiver(BuildConfig.MI_APP_ID,BuildConfig.MI_KEY);
        }
        if (HwPushManager.getHwPushManager().canHuaWeiPush()){
            HwPushManager.getHwPushManager().registerHwSystemReceiver();
        }
        if (ViVoPushManager.getViVoPushManager().isSupport()){
            ViVoPushManager.getViVoPushManager().register(BuildConfig.VIVO_APP_ID,BuildConfig.VIVO_APP_KEY,BuildConfig.VIVO_APP_SECRET);
            ViVoPushManager.getViVoPushManager().turnOnPush();
        }
        if (Rom.isOppo()){
            OppoPushManager.getOppoPushManager().register(BuildConfig.OPPO_APP_KEY,BuildConfig.OPPO_APP_SECRET);
        }
    }


    /**
     * 是否弹出通知
     * @param msgType
     * @return
     */
    private boolean showNotificationByTeam(String msgType,TeamMessageBaseModel teamMessageBaseModel){
        boolean isShowNotification;
        MessageType messageType = MessageType.typeOfValue(msgType);
        String content = "";
        String ignore = "ignore";//忽视掉
        MessageSystemBean messageSystemBean = GsonUtils.fromJson(teamMessageBaseModel.getSourceContent(),MessageSystemBean.class);
        switch (messageType){
            case GROUP_CREATE:
                content = "创建群组";
//                ModuleMgr.getAppManager().addTeamInviteUnread(teamMessageBaseModel.getGroupId());
//                MsgMgr.getInstance().sendMsg(MsgType.MT_CONTACT_NOTIFY, null);
                break;
            case GROUP_INVITE:
                content = "群组邀请通知";
                ModuleMgr.getAppManager().addTeamInviteUnread(teamMessageBaseModel.getGroupId());
                MsgMgr.getInstance().sendMsg(MsgType.MT_CONTACT_NOTIFY, null);
                break;
            case GROUP_KICK_OUT:
                content = "ignore";
                if (!ModuleMgr.getCenterMgr().getUID().equals(messageSystemBean.getOperator())){//把谁移除了群组，倘若移除群组的人和当前用户不同，则不予处理
                    break;
                }
                content = "您被踢出了群组";
//                homeMessageViewModel.deleteForGroupId(teamMessageBaseModel.getGroupId());
//                teamMessageViewModel.deleteMessageForGroupId(teamMessageBaseModel.getGroupId());
//                TeamDbHelper.getInstance().delete(teamMessageBaseModel.getFromUserId());
//                MsgMgr.getInstance().sendMsg(MsgType.GROUP_REMOVE, null);
                break;
            case GROUP_EXIT:
                content = "退出了群组";
                break;
            case GROUP_REMOVE:
                content = "群组已解散";
                homeMessageViewModel.deleteForGroupId(teamMessageBaseModel.getGroupId());
                teamMessageViewModel.deleteMessageForGroupId(teamMessageBaseModel.getGroupId());
                TeamDbHelper.getInstance().delete(teamMessageBaseModel.getFromUserId());
                MsgMgr.getInstance().sendMsg(MsgType.GROUP_REMOVE, null);
                break;
            case UPDATE_GROUP_NAME:
                content = "修改了群组名称";
                break;
            default:
                break;
        }
        if (!TextUtils.isEmpty(content)){
            isShowNotification = true;
            if (!ignore.equals(content)){
                showNotification(content);
            }
        }else {
            isShowNotification = false;
        }

        return isShowNotification;
    }

    private void showNotification(String content){
        Intent intent = new Intent(this,MainActivity.class);
        NotificationUtil.NotificationUtilBuild notificationUtilBuild = NotificationUtil.getInstance().createBuild();
        notificationUtilBuild.context = MainActivity.this;
        notificationUtilBuild.intent = intent;
        notificationUtilBuild.title = "系统通知";
        if (!TextUtils.isEmpty(content)){//不为空时候弹出通知
            notificationUtilBuild.content = content;
            NotificationUtil.getInstance().showNotification(notificationUtilBuild);
            SoundPoolUtils.getInstance(getApplicationContext()).playMayWait();
            if (vibratorUtil.isVibrate()){
                vibratorUtil.stopVibrate();
            }
            vibratorUtil.setVibratePattern(1000);
            vibratorUtil.vibrate(VibratorUtil.Companion.getINTERRUPT());
        }
    }

    /**
     * 判断消息来源是不是当前消息，因为群发消息的时候服务器会把当前用户发送的消息再发送回来
     */
    private boolean isSameUser(String message){
        boolean isSameUser = false;//消息来源是否是当前用户
        if (message.equals("OK")){//每次向服务器发消息，服务器都会回一个"OK"
            return false;
        }
        try {
            JSONObject jsonObject = new JSONObject(message);
            if (!jsonObject.has("fromUserId")){
                return false;
            }
            String msgIdServer = new JSONObject(message).getString("fromUserId");
            return ModuleMgr.getCenterMgr().getUID().equals(msgIdServer);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 上传第三方信息
     */
    private void uploadThirdRegisterMessage(String thirdModeType,String regId){
        Map<String,String> params = new HashMap<>();
        params.put("userId",ModuleMgr.getCenterMgr().getUID());
        params.put("system", thirdModeType);
        params.put("usertoken",regId);
        RetrofitHelp.request(DqUrl.url_user_system, params, new DqCallBack() {
            @Override
            public void onSuccess(String url, int code, DataBean entity) {
                Log.e("YM","请求结果(成功):"+entity.toString());
            }

            @Override
            public void onFailed(String url, int code, DataBean entity) {
                Log.e("YM","请求结果(失败):"+entity.toString());
            }
        });
    }

    /**
     * 获取用户网络数据
     */
    private void requestUserInfo(String friendId){
//        Map<String,Object> params = new HashMap<>();
//        params.put("userId",friendId);
//        ImSdkHttpUtils.postJson(URLUtil.USER_INFO,params,new HttpResultResultCallBack<HttpBaseBean>() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(HttpBaseBean response, int id) {
//                if (0 != response.status){
//                    return;
//                }
//                String content = response.data.toString();
//                userModel = gson.fromJson(content,UserModel.class);
//                userViewModel.insert(userModel);
//            }
//        });
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.UserInfo.OTHER_UID, friendId);
        mPresenter.getUserInfo(DqUrl.url_get_userinfo, hashMap);
    }

    /**
     * 获取群组网络数据
     */
    private void requestTeamInfo(String teamId){
        Map<String,Object> params = new HashMap<>();
        params.put("groupId",teamId);
//        ImSdkHttpUtils.postJson(URLUtil.GROUP_INFO,params,new HttpResultResultCallBack<HttpBaseBean>() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(HttpBaseBean response, int id) {
//                if (0 != response.status){
//                    return;
//                }
//                String content = response.data.toString();
//                teamModel = gson.fromJson(content,TeamModel.class);
//                teamViewModel.insert(teamModel);
//            }
//        });
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(KeyValue.GROUP_ID, teamId);
        hashMap.put(KeyValue.Group.SHOW_GROUP_MEMBER, "0");
        mPresenter.getTeamInfo(DqUrl.url_select_group, hashMap);
    }
    private void insertUser(String userId){
        Friend friend = FriendDbHelper.getInstance().getFriend(userId);
        DqLog.e("YM","用户ID:"+userId);
        if (null != friend && !TextUtils.isEmpty(friend.headpic)){
            return;
        }
        if (requestUserIds.contains(userId)){
            return;
        }
        requestUserIds.add(userId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestUserInfo(userId);
                    }
                });
            }
        }).start();

    }

    private void insertTeam(String teamId){
        GroupInfoBean groupInfoBean = TeamDbHelper.getInstance().getTeam(teamId);
        if (null != groupInfoBean){
            return;
        }
        if (requestTeamIds.contains(teamId)){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestTeamInfo(teamId);
                    }
                });

            }
        }).start();

    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (entity == null) return;
        if (DqUrl.url_select_group.equals(url)) {
            GroupInfoBean groupInfoBean = (GroupInfoBean) entity.data;
            if(groupInfoBean != null) {
                DqApp.getInstance().getSingleThread(() -> {
                    TeamDbHelper.getInstance().update(groupInfoBean, null);
                    List<GroupMemberBean> memberList = groupInfoBean.getMemberList();
                    if(memberList != null && memberList.size() > 0) {
                        MemberDbHelper.getInstance().update(groupInfoBean.group_id, memberList, null);
                    }
                });
                MsgMgr.getInstance().sendMsg(MsgType.HOME_UPDATE_MSG, groupInfoBean);
            }
        }else if (DqUrl.url_get_userinfo.equals(url)){
            Friend friend = (Friend) entity.data;
            FriendDbHelper.getInstance().update(friend, null);
            MsgMgr.getInstance().sendMsg(MsgType.HOME_UPDATE_MSG, friend);
        }
    }

    /**
     * 获取网络数据
     * 这里只是登录下账号让后台知道已经登录了，所以不对后台数据进行处理
     */
    private void requestLogin(String userId){

        Gson gson = new Gson();
        Map<String,Object> params = new HashMap<>();
        params.put("userId", userId);
        ImSdkHttpUtils.postJson(false, URLUtil.USER_LOGIN,params,new HttpResultResultCallBack<HttpBaseBean>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(HttpBaseBean response, int id) {
                String content = response.data.toString();
//                List<TeamModel> userModels = gson.fromJson(content,new TypeToken<List<TeamModel>>(){}.getType());
                if (0 == response.status){
//                    JSONObject jsonObject;
//                    String user = "";
//                    String token = "";
//                    UserBean userBean;
//                    try {
//                        jsonObject = new JSONObject(content);
//                        token = jsonObject.getString("token");
//                        user = jsonObject.getString("user");
//                        userBean = gson.fromJson(user,UserBean.class);
//                        initUserSpData(userBean,token);
//                        Intent imSdkIntent = new Intent(LoginActivity.this, ImMainActivity.class);
//                        startActivity(imSdkIntent);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }else {
//                    Toast.makeText(LoginActivity.this,"登陆失败:"+response.status,Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void showNotification(ImMessageBaseModel imMessageBaseModel){
        NotificationUtil.NotificationUtilBuild notificationUtilBuild = NotificationUtil.getInstance().createBuild();
        ImType imType = ImType.typeOfValue(imMessageBaseModel.getType());
        if (!DqApp.getInstance().getNotificationFilter().filter()){//假如程序在后台,则在通知栏弹出消息
            notificationUtilBuild.context = this;
            boolean isShowNotification = getNotificationContent(imMessageBaseModel,notificationUtilBuild);
            if (isShowNotification){
                NotificationUtil.getInstance().showNotification(notificationUtilBuild);
                Log.e("YM","播放声音");
                SoundPoolUtils.getInstance(this).playMayWait();
                if (vibratorUtil.isVibrate()){
                    vibratorUtil.stopVibrate();
                }
                vibratorUtil.setVibratePattern(1000);
                vibratorUtil.vibrate(VibratorUtil.Companion.getINTERRUPT());
            }
        }
    }

    /**
     * 获取通知栏弹出内容
     * @return 返回通知栏显示的内容
     */
    private boolean getNotificationContent(ImMessageBaseModel imMessageBaseModel,NotificationUtil.NotificationUtilBuild notificationUtilBuild){
        boolean isShowNotification = true;
        String content = "";
        ImType imType = ImType.typeOfValue(imMessageBaseModel.getType());
//        switch (imType){
//            case P2P:
//                Log.e("YM","个人消息");
//                break;
//            case Team:
//                Log.e("YM","群组消息");
//                break;
//            case System:
//                Log.e("YM","系统消息");
//                break;
//        }
        MessageType messageType = MessageType.typeOfValue(imMessageBaseModel.getMsgType());
        switch (messageType){
            case TEXT:
                MessageTextBean messageTextBean = GsonUtils.fromJson(imMessageBaseModel.getSourceContent(),MessageTextBean.class);
                String description = messageTextBean.getDescription();
//                content = AESHelper.decryptString(description);
                content = AESUtil.decode(description);
                break;
            case VOICE:
                content = "[音频消息]";
                break;
            case VIDEO:
                content = "[视频消息]";
                break;
            case PICTURE:
                content = "[图片消息]";
                break;
            case PERSON_CARD:
                content = "[名片消息]";
                break;
            case RED_PACKAGE:
                content = "[红包消息]";
                break;
            default:

        }
        notificationUtilBuild.content = content;
        initUserData(imMessageBaseModel,notificationUtilBuild);
        return isShowNotification;
    }
    /**
     * 更新用户数据
     */
    private void initUserData(ImMessageBaseModel messageBaseModel,NotificationUtil.NotificationUtilBuild notificationUtilBuild){
        String type = messageBaseModel.getType();
        if (ImType.P2P.getValue().equals(type)){
            String userId = ModuleMgr.getCenterMgr().getUID();
            String friendId = "";
            if (userId.equals(messageBaseModel.getFromUserId())){
                friendId = messageBaseModel.getToUserId();
            }else {
                friendId = messageBaseModel.getFromUserId();
            }
            notificationUtilBuild.title = UserInfoHelper.getUserDisplayName(friendId);
            Intent intent = IntentUtils.getP2PChat(this,friendId);
            intent.putExtra(Extras.EXTRA_SOURCE,SourceType.SOURCE_TYPE_NOTIFICATION);
            notificationUtilBuild.intent = intent;
        }else if (ImType.Team.getValue().equals(type)){
            TeamMessageBaseModel teamMessageBaseModel = (TeamMessageBaseModel)messageBaseModel;
            String groupId = teamMessageBaseModel.getGroupId();
            notificationUtilBuild.title = TeamHelper.getTeamName(groupId);
            Intent intent = IntentUtils.getTeamChat(this,groupId);
            intent.putExtra(Extras.EXTRA_SOURCE, SourceType.SOURCE_TYPE_NOTIFICATION);
            notificationUtilBuild.intent = intent;
        }else {
            Intent intent = new Intent(this,MainActivity.class);
            notificationUtilBuild.intent = intent;
        }
    }

    /**
     * 处理系统消息
     */
    private String parserSystemMessage(ImMessageBaseModel imMessageBaseModel){
        MessageType messageType = MessageType.typeOfValue(imMessageBaseModel.getMsgType());
        Log.e("YM","系统消息内容:"+imMessageBaseModel.getSourceContent());
        MessageSystemBean messageSystemBean = GsonUtils.fromJson(imMessageBaseModel.getSourceContent(),MessageSystemBean.class);
        String content = "";
        switch (messageType){
            case FRIEND_ADD://添加好友
                ModuleMgr.getAppManager().addFriendUnread(messageSystemBean.getFromUserId());
                MsgMgr.getInstance().sendMsg(MsgType.MT_CONTACT_NOTIFY, null);
                content = "你收到了一条好友请求消息";
                break;
            case GROUP_INVITE://群组邀请消息
                if (TextUtils.isEmpty(messageSystemBean.getGroupId())){
                    return "";
                }
                ModuleMgr.getAppManager().addTeamInviteUnread(messageSystemBean.getGroupId());
                MsgMgr.getInstance().sendMsg(MsgType.MT_CONTACT_NOTIFY, null);
                ModuleMgr.getAppManager().addFriendUnread(messageSystemBean.getFromUserId());
                MsgMgr.getInstance().sendMsg(MsgType.MT_CONTACT_NOTIFY, null);
                content = "你收到了一条群组邀请消息";
                break;
            case GROUP_APPLY_FOR://加群申请消息
                content = "你收到了一条加群申请消息";
                break;
            case FRIEND_DELETE://好友删除消息
                content = "你被好友删除了";
//                FriendDbHelper.getInstance().delete(imMessageBaseModel.getFromUserId());
                MsgMgr.getInstance().sendMsg(MsgType.MT_FRIEND_REMOVE_FRIEND, imMessageBaseModel.getFromUserId());
                break;
            case GROUP_EXIT://退群消息
                content = "你有一个群成员退出了群组";
                break;
            case GROUP_REMOVE://群解散消息
                content = "你有一个群组被解散了";
                homeMessageViewModel.deleteForGroupId(messageSystemBean.getGroupId());
                teamMessageViewModel.deleteMessageForGroupId(messageSystemBean.getGroupId());
                TeamDbHelper.getInstance().delete(messageSystemBean.getFromUserId());
                MsgMgr.getInstance().sendMsg(MsgType.GROUP_REMOVE, null);
                break;
            case SIGN_LOGIN://单点登陆登出
                content = "你被踢出了登陆";
                break;
            case FRIEND_RECEIVED://好友申请已经同意
                content = "好友申请已经同意";
                break;
            case GROUP_KICK_OUT://你被群组踢出了群主
                if (!ModuleMgr.getCenterMgr().getUID().equals(messageSystemBean.getOperator())){//把谁移除了群组，倘若移除群组的人和当前用户不同，则不予处理
                    break;
                }
                content = "你被踢出了群组";
                homeMessageViewModel.deleteForGroupId(messageSystemBean.getGroupId());
                teamMessageViewModel.deleteMessageForGroupId(messageSystemBean.getGroupId());
                TeamDbHelper.getInstance().delete(messageSystemBean.getFromUserId());
                MsgMgr.getInstance().sendMsg(MsgType.GROUP_REMOVE, null);
                break;
            case RED_PACKAGE_SEND://红包发送
//                content = "你被踢出了群组";
                MsgMgr.getInstance().sendMsg(MsgType.RED_PACKAGE_PAY, "");
                break;
            case EXPAND_MSG://拓展字段
                parserSystemExpandMsg(imMessageBaseModel.getSourceContent());
                break;
            case RECEIVER_MESSAGE_CALLBACK_STATUS://接收的回传消息内容

                break;
        }
        return content;
    }

    /**
     * 解析拓展字段
     */
    private void parserSystemExpandMsg(String content){
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(content);
        object = object.getJSONObject("attach");
        Log.e("YM","系统拓展消息内容:"+object.toJSONString());
        String objectName = object.getString(KeyValue.SystemExpandContact.KEY_OBJECTNAME);
        if (CustomAttachmentType.RED_RAIN_TIP.equals(objectName)){//红包雨通知
            RedRainSystemAttachment redRainSystemAttachment = new RedRainSystemAttachment();
            redRainSystemAttachment.fromJson(object);
            long currentTime = System.currentTimeMillis();
            long redRainEndTime = DateUtil.string2Long(redRainSystemAttachment.getRedEnvelopeActiveNoticeEndTime());
            long redRainStartTime = DateUtil.string2Long(redRainSystemAttachment.getRedEnvelopeActiveNoticeStartTime());
            if (redRainEndTime <= currentTime){//假如结束时间小于当前时间，则不提示红包活动
                return;
            }
//            if (redRainStartTime <= currentTime && currentTime<= redRainEndTime){//假如结束时间小于当前时间，则不提示红包活动
//                return;
//            }
            MsgMgr.getInstance().sendMsg(MsgType.APPLICATION_RED_RAIN_START, redRainSystemAttachment);
            if (ForegroundCallbacks.get().isBackground()){//后台弹通知，前台弹对话框
                //后台的话不弹出红包雨到来的提示
                String message = "红包雨开始了,打开应用领取红包吧~";
                com.netease.nim.uikit.common.util.notification.NotificationUtil.showDefaultNotification(message);
            }else {
                if (currentTime < redRainStartTime - 10 * 1000){//只有时间小于这个时间时才弹出红包雨开始的提示框
                    ApplicationDialog.getInstance().showRedRainStartTipDialog();
                }
            }
            ModuleMgr.getCenterMgr().putRedRainContent(content);//保存红包雨内容
        }
    }

}