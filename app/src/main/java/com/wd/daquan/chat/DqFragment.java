package com.wd.daquan.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.da.library.utils.BigDecimalUtils;
import com.da.library.utils.DateUtil;
import com.dq.im.util.SoundPoolUtils;
import com.red.libary.ipc.FallingOnClickIpc;
import com.red.libary.weight.FallingLayout;
import com.red.libary.weight.OpenGiftDialog;
import com.wd.daquan.BuildConfig;
import com.wd.daquan.DqApp;
import com.wd.daquan.MainActivity;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.Constants;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.helper.MainTitleRightHelper;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.login.helper.LogoutHelper;
import com.wd.daquan.mine.dialog.GuideOpenVipDialog;
import com.wd.daquan.mine.dialog.RedPackageTipDialog;
import com.wd.daquan.mine.dialog.VipExchangeResultDialog;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.RedEnvelopBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ConfigManager;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.third.fragment.ConversationListFragment;
import com.wd.daquan.third.fragment.MainTabFragment;
import com.wd.daquan.third.session.extension.RedRainSystemAttachment;
import com.wd.daquan.util.AdConfig;
import com.wd.daquan.util.TTAdManagerHolder;
import com.wd.daquan.util.TToast;

import java.util.List;

import cn.iwgang.countdownview.CountdownView;

/**
 */
public class DqFragment extends MainTabFragment implements View.OnClickListener, QCObserver {

    //    同时在线的其他端的信息
//    private List<OnlineClient> onlineClients;
    private View redPackageContent;//活动布局
    private View mNotifyBar;
    private View mMultiportBar;
    private TextView mNotifyBarText;
    private TextView redRainTv;
    private ImageView mTitleRightSearch;
    private ImageView mTitleLeftExtra;
    private View redPackageRain;//红包雨
    private View turntableLottery;//转盘抽奖
    private FallingLayout mFallingLayout;
    private View mFallingContent;
    private View mFallingClose;
    private ViewGroup rootView;
    private OpenGiftDialog openGiftDialog;
    private RedPackageTipDialog redPackageTipDialog;
    private GuideOpenVipDialog guideOpenVipDialog;//引导用户去开通VIP的窗口
    private long redRainStartTime = -1;//红包雨开始时间
    private long redRainEndTime = -1;//红包雨结束时间
    private long redRainDuration;//红包雨持续时间
    private boolean redRainIsStart = false;
    private boolean isFont;//是否在前台
    private String countdownContent = "倒计时";
    private CountdownView countdownView;
    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private boolean isStopRedRain = true;//是否手动停掉红包雨
    @Override
    public void setContentView() {
        setContentView(R.layout.qc_qingchat_fragment);

    }

    @Override
    protected void onInit() {
        initView();
        initListener();

        addRecentContactsFragment();
        initAd();
//        initExpressAd();
//        loadExpressAd(AdConfig.expressAdCode);
        loadAd(AdConfig.rewardAdCode,TTAdConstant.VERTICAL);
    }

    private void initView() {
        if(getView() == null) {
            return;
        }
        TextView title = getView().findViewById(R.id.toolbar_title);
        String titleContent = DqApp.getStringById(R.string.app_name);
        if (BuildConfig.IS_DUBUG){
            titleContent =  titleContent.concat("测试版");
        }
        title.setText(titleContent);
        mTitleLeftExtra = getView().findViewById(R.id.toolbar_left_extra);
        mTitleLeftExtra.setVisibility(View.GONE);
        ImageView titleRightExtra = getView().findViewById(R.id.toolbar_right_extra);
        mTitleRightSearch = getView().findViewById(R.id.toolbar_right_search);
        MainTitleRightHelper.getInstance().init(getActivity(), titleRightExtra);

        redPackageContent = getView().findViewById(R.id.red_package_content);
        mNotifyBar = getView().findViewById(R.id.status_notify_bar);
        mNotifyBarText = getView().findViewById(R.id.status_desc_label);
        mNotifyBar.setVisibility(View.GONE);

        mMultiportBar = getView().findViewById(R.id.multiport_notify_bar);
        mMultiportBar.setVisibility(View.GONE);
        countdownView = getView().findViewById(R.id.count_down_view);
        redPackageRain = getView().findViewById(R.id.main_red_package_rain);
        turntableLottery = getView().findViewById(R.id.main_turntable_lottery);
        redRainTv = getView().findViewById(R.id.red_rain_tv);
//        mRedPacketViewHelper = new RedPacketViewHelper(getCurrentActivity());
        openGiftDialog = new OpenGiftDialog();
        redPackageTipDialog = new RedPackageTipDialog();
        guideOpenVipDialog = new GuideOpenVipDialog();
        addRedRain();

        Log.e("YM","设置红包雨开关");
        if ("0".equals(ConfigManager.getInstance().getRedEnvelopedRainSwitch())){//红包雨是否打开
            redPackageContent.setVisibility(View.VISIBLE);
        }else {
            redPackageContent.setVisibility(View.GONE);
        }
//        countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
//            @Override
//            public void onEnd(CountdownView cv) {//倒计时结束
//                Log.e("YM","倒计时结束");
//                if (isFont){
//                    countdownView.setVisibility(View.GONE);
//                    redRainTv.setText("进行中");
//                    startRain();
//                }
//            }
//        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        isFont = true;
        Log.e("YM","onStart()");
//        MsgMgr.getInstance().sendMsg(MsgType.APPLICATION_RED_RAIN_START, redRainSystemAttachment);
//        startRain();
        checkRedRainContent();
    }

    /**
     * 检查红包雨内容
     * 假如有的话就发送消息，否则不发送
     */
    private void checkRedRainContent(){
//        String content = ModuleMgr.getCenterMgr().getRedRainContent();//获取红包雨内容
//
//        if(TextUtils.isEmpty(content)){
//            return;
//        }
        if (isStopRedRain){
            return;
        }
//        JSONObject jsonObject = JSON.parseObject(content);
//        RedRainSystemAttachment redRainSystemAttachment = new RedRainSystemAttachment();
//        redRainSystemAttachment.fromJson(jsonObject);
//        MsgMgr.getInstance().sendMsg(MsgType.APPLICATION_RED_RAIN_START, redRainSystemAttachment);
        startRain2();
    }

    //    private void startRain(){
//        long currentTime = System.currentTimeMillis();
//        Log.e("YM","红包雨开始时间:"+redRainStartTime);
//        Log.e("YM","当前时间:"+currentTime);
////        if (currentTime < redRainStartTime || redRainStartTime == -1){
//        if (mFallingLayout.isRedRainIng()){
//            return;
//        }
//        if (redRainStartTime < currentTime && currentTime < redRainEndTime){
//            mFallingLayout.setRedPackageCoundSpeed(300);
//            mFallingLayout.refreshSpeed(5000);
//            mFallingContent.setVisibility(View.VISIBLE);
//            mFallingLayout.startRedRain();
//            countdownView.setVisibility(View.GONE);
//            redRainTv.setText("进行中");
//        }
//    }
    private void startRain2(){
        long currentTime = System.currentTimeMillis();
        Log.e("YM","红包雨开始时间:"+redRainStartTime);
        Log.e("YM","当前时间:"+currentTime);
//        if (currentTime < redRainStartTime || redRainStartTime == -1){
        if (mFallingLayout.isRedRainIng()){
            return;
        }
        mFallingLayout.setRedPackageCoundSpeed(300);
        mFallingLayout.refreshSpeed(5000);
        mFallingContent.setVisibility(View.VISIBLE);
        mFallingLayout.startRedRain();
        redRainTv.setText("进行中");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.e("YM","onStop()");
        isFont = false;
        if (mFallingLayout.isRedRainIng()){
            mFallingLayout.stopRedRain();
            mFallingContent.setVisibility(View.GONE);
        }
    }

    private void initListener() {

        mNotifyBarText.setOnClickListener(this);
        mMultiportBar.setOnClickListener(this);
        mTitleLeftExtra.setOnClickListener(this);
        mTitleRightSearch.setOnClickListener(this);
        MsgMgr.getInstance().attach(this);
        redPackageRain.setOnClickListener(this);
        turntableLottery.setOnClickListener(this);

    }

    /**
     * 添加红包雨
     */
    private void addRedRain(){
        rootView = (ViewGroup) getCurrentActivity().getWindow().getDecorView();
//        mFallingLayout = new FallingLayout(getContext());
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//        mFallingLayout.setLayoutParams(layoutParams);
        mFallingContent = getLayoutInflater().inflate(R.layout.falling_layout,rootView,false);
        mFallingLayout = mFallingContent.findViewById(R.id.red_package_falling);
        mFallingClose = mFallingContent.findViewById(R.id.red_package_falling_close);
        rootView.addView(mFallingContent);
        mFallingLayout.setFallingOnClickIpc(new FallingOnClickIpc() {
            @Override
            public void onClickFalling() {
//                Toast.makeText(getContext(),"点击了漂浮物",Toast.LENGTH_SHORT).show();
//                if (!ModuleMgr.getCenterMgr().isVip()){
//                    Toast.makeText(getContext(),"本活动仅限VIP会员参加!",Toast.LENGTH_LONG).show();
//                    return;
//                }
                if (!ModuleMgr.getCenterMgr().isVip()){
//                    DialogUtils.showCommonDialogForBoth(getContext(), "抱歉您还不是VIP会员，无法参与此活动！", "去开通VIP", new DialogListener() {
//                        @Override
//                        public void onCancel() {
//                        }
//
//                        @Override
//                        public void onOk() {
//                            NavUtils.gotoVipActivity(getActivity());
//                        }
//                    });
                    showGuideOpenVip();
                    return;
                }
                openRedPackage();
            }
        });
        mFallingClose.setOnClickListener(this);
        mFallingContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //防止半透明的窗口点击穿透
            }
        });
    }

    // 注销
    private void onLogout() {
        LogoutHelper.logout(getActivity(), DqApp.getStringById(R.string.user_kickout_tips));
    }

    // 将最近联系人列表fragment动态集成进来。 开发者也可以使用在xml中配置的方式静态集成。
    private void addRecentContactsFragment() {
        ConversationListFragment fragment = new ConversationListFragment();
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.messages_fragment, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_right_search:
                NavUtils.gotoSearchConversationActivity(getActivity());
                break;
            case R.id.toolbar_left_extra:
//                if(mNavigationClickListener != null) {
//                    mNavigationClickListener.open();
//                }
                DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
                break;
            case R.id.status_desc_label:
                login();
                break;
            case R.id.multiport_notify_bar:
                //电脑端在线
                //MultiportActivity.startActivity(getActivity(), onlineClients);
                NavUtils.gotoMultiportActivity(getActivity(), null, Constants.Web.Logout);
                break;
            case R.id.main_red_package_rain:

//                if (!ModuleMgr.getCenterMgr().isVip()){
//                    Toast.makeText(getContext(),"本活动仅限VIP会员参见!",Toast.LENGTH_LONG).show();
//                    return;
//                }
//                long currentTime = System.currentTimeMillis();
//                if (redRainStartTime == -1 || (redRainStartTime > currentTime || currentTime > redRainEndTime)){//时间没到打开红包雨提示规则
//                    showRedPackageTip();
//                    return;
//                }
                isStopRedRain = false;
                startRain2();
                break;
            case R.id.main_turntable_lottery:
//                showVipExchangeResultDialog();
                SoundPoolUtils.getInstance(getContext()).playMayWait();
                break;
            case R.id.red_package_falling_close:
                isStopRedRain = true;
                mFallingLayout.stopRedRain();
                mFallingContent.setVisibility(View.GONE);
                break;
        }
    }

    private void showVipExchangeResultDialog(){
        VipExchangeResultDialog exchangeResultDialog = new VipExchangeResultDialog();
        exchangeResultDialog.show(getFragmentManager(),"");
    }

    private void login() {
        EBSharedPrefUser userInfoSp = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo();
        String uid = userInfoSp.getString(EBSharedPrefUser.uid, "");
        String im_token = userInfoSp.getString(EBSharedPrefUser.im_token, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
        if (mTTAd != null) {
            mTTAd.destroy();
        }
    }

    @Override
    public void onMessage(String key, Object value) {
        if(MsgType.MT_App_Login.equals(key)) {
            Boolean isLogin = (Boolean) value;
            if(isLogin) {
                onInit();
            }
        } else if (MsgType.MT_WEB_NOTICE.equals(key)) {
            String type = (String) value;
            if (Constants.Web.Login.equals(type)) {
                mMultiportBar.setVisibility(View.VISIBLE);
            } else {
                mMultiportBar.setVisibility(View.GONE);
            }
        }else if (MsgType.APPLICATION_RED_RAIN_START.equals(key)){
            //红包雨
//            ApplicationDialog.getInstance().showRedRainStartTipDialog();
            RedRainSystemAttachment redRainSystemAttachment = (RedRainSystemAttachment) value;
            long currentTime = System.currentTimeMillis();
            redRainStartTime = DateUtil.string2Long(redRainSystemAttachment.getRedEnvelopeActiveNoticeStartTime());
            redRainEndTime = DateUtil.string2Long(redRainSystemAttachment.getRedEnvelopeActiveNoticeEndTime());
//            if (redRainStartTime < currentTime){
//                redRainStartTime = currentTime;
//                redRainIsStart = false;
//            }else {
//                redRainIsStart = true;
//            }
            redRainDuration = redRainEndTime - redRainStartTime;
            Log.e("YM","收到红包雨redRainStartTime:"+redRainStartTime);
            Log.e("YM","收到红包雨redRainEndTime:"+redRainEndTime);
            Log.e("YM","收到红包雨redRainDuration:"+redRainDuration);
            if (redRainEndTime <= currentTime){
                return;
            }
            long redRainDiff = redRainStartTime - currentTime;//还有多久红包雨开始,这个字段延迟100ms进行红包雨动画
            long temp = redRainStartTime - currentTime;//还有多久红包雨开始，这个时间进行计算整体红包雨活动消失的时间
            if (redRainDiff <= 0){
                redRainDiff = 100;
            }
//            getHandler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (isFont){
//                        startRain();
//                    }
//                }
//            },redRainDiff);
            countdownView.setVisibility(View.VISIBLE);
            countdownView.start(redRainDiff); // 毫秒
            redRainTv.setText("即将开始");
            getHandler().removeCallbacksAndMessages(null);
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mFallingLayout.isRedRainIng()){
                        mFallingLayout.stopRedRain();
                        mFallingContent.setVisibility(View.GONE);
                    }
                    redRainStartTime = -1;
                    redRainEndTime = -1;
                    redRainDuration = 0;
                    redRainTv.setText("每日红包雨");
                    Log.e("YM","红包雨结束活动结束");
                }
            },temp + redRainDuration);//重置的时间为剩余时间 + 红包雨持续时间
        }else if (MsgType.UPDATE_READ_PACKAGE_RAIN_LABEL.equals(key)){
            if ("0".equals(ConfigManager.getInstance().getRedEnvelopedRainSwitch())){//红包雨是否打开
                redPackageContent.setVisibility(View.VISIBLE);
            }else {
                redPackageContent.setVisibility(View.GONE);
            }
        }
    }

    public interface NavigationClickListener{
        void open();
    }

    private NavigationClickListener mNavigationClickListener;

    public void setNavigationClickListener(NavigationClickListener listener) {
        this.mNavigationClickListener = listener;
    }

    @Override
    public void onDestroyView() {
        mFallingLayout.stopRedRain();
        mFallingContent.setVisibility(View.GONE);
        MainTitleRightHelper.getInstance().onDestroy();
        super.onDestroyView();
    }

    /**
     * 打开中奖的dialog
     * @param amount 中奖金额
     * @param isLuck 是否中奖
     */
    private void showOpenGift(String amount,boolean isLuck){
        OpenGiftDialog openGiftDialog = new OpenGiftDialog();
        Bundle bundle = new Bundle();
        bundle.putString(OpenGiftDialog.ACTION_AMOUNT,amount);
        if (isLuck){
            bundle.putInt(OpenGiftDialog.ACTION_TYPE,OpenGiftDialog.LUCK);
        }else {
            bundle.putInt(OpenGiftDialog.ACTION_TYPE,OpenGiftDialog.NO_LUCK);
        }
        openGiftDialog.setArguments(bundle);
        openGiftDialog.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.MyMinDialogWidth);
        openGiftDialog.show(getFragmentManager(),"对话框");
    }

    /**
     * 打开红包雨规则提示

     */
    private void showRedPackageTip(){
        Bundle bundle = new Bundle();
        bundle.putString(RedPackageTipDialog.ACTION_TIP_CONTENT,ModuleMgr.getCenterMgr().getRedEnvelopedRainRemind());
        redPackageTipDialog.setArguments(bundle);
        redPackageTipDialog.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.MyMinDialogWidth);
        redPackageTipDialog.show(getFragmentManager(),"对话框");
    }
    /**
     * 引导用户开通VIP
     */
    private void showGuideOpenVip(){
        Bundle bundle = new Bundle();
        guideOpenVipDialog.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.MyMinDialogWidth);
        guideOpenVipDialog.show(getFragmentManager(),"对话框");
    }


    private boolean openRedPackageIng;//上次打开红包的时间
    /**
     * 打开红包接口
     */
    private void openRedPackage(){
        if (openRedPackageIng){
            Toast.makeText(getContext(),"同一时间只能打开一个红包",Toast.LENGTH_LONG).show();
            return;
        }
        openRedPackageIng = true;
        RetrofitHelp.getUserApi().getUserRedEnvelope(DqUrl.url_user_open_red_envelope,RetrofitHelp.getRequestBody(null)).enqueue(new DqCallBack<DataBean<RedEnvelopBean>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<RedEnvelopBean> entity) {
                openRedPackageIng = false;
                RedEnvelopBean redEnvelopBean = entity.data;
                String amount;
                if (redEnvelopBean.isFlag()){
                    amount  = BigDecimalUtils.penny2Dollar(redEnvelopBean.getAmount()).toPlainString();
                }else {
//                    amount = "您没有中奖~";
//                    addCSJAd();
//                    loadExpressAd(AdConfig.expressAdCode);
//                    renderExpressAd();
                    showRewardAdCode();
                    return;
                }
                if (0 == redEnvelopBean.getAmount()){
//                    addCSJAd();
//                    loadExpressAd(AdConfig.expressAdCode);
//                    renderExpressAd();
                    showRewardAdCode();
                    return;
                }
                showOpenGift(amount,redEnvelopBean.isFlag());

            }

            @Override
            public void onFailed(String url, int code, DataBean<RedEnvelopBean> entity) {
                Toast.makeText(getContext(),entity.content,Toast.LENGTH_LONG).show();
                openRedPackageIng = false;
            }
        });
    }

    private void initExpressAd(){
        mTTAdNative = TTAdManagerHolder.get().createAdNative(getContext());
    }

    private void loadExpressAd(String codeId) {
        float expressViewWidth = 350;
        float expressViewHeight = 350;
        try{
            expressViewWidth = Float.parseFloat("200");
            expressViewHeight = Float.parseFloat("300");
        }catch (Exception e){
            expressViewHeight = 0; //高度设置为0,则高度会自适应
        }
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth,expressViewHeight) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640,320 )//这个参数设置即可，不影响模板广告的size
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
//                TToast.show(getContext(), "load error : " + code + ", " + message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0){
                    return;
                }
                mTTAd = ads.get(0);
                bindAdListener(mTTAd);
//                startTime = System.currentTimeMillis();
//                mTTAd.render();
            }
        });
    }

    /**
     * 开始渲染插屏广告
     */
    private void renderExpressAd(){
        startTime = System.currentTimeMillis();
        mTTAd.render();
    }

    private long startTime = 0;
    private boolean mHasShowDownloadActive = false;
    private TTNativeExpressAd mTTAd;
    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
            @Override
            public void onAdDismiss() {
//                TToast.show(getContext(), "广告关闭");
            }

            @Override
            public void onAdClicked(View view, int type) {
//                TToast.show(getContext(), "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
//                TToast.show(getContext(), "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("ExpressView","render fail:"+(System.currentTimeMillis() - startTime));
//                TToast.show(getContext(), msg+" code:"+code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("ExpressView","render suc:"+(System.currentTimeMillis() - startTime));
                //返回view的宽高 单位 dp
//                TToast.show(getContext(), "渲染成功");
                mTTAd.showInteractionExpressAd(getActivity());
                loadExpressAd(AdConfig.expressAdCode);
            }
        });

        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD){
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
//                TToast.show(getContext(), "点击开始下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
//                    TToast.show(getContext(), "下载中，点击暂停", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(getContext(), "下载暂停，点击继续", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(getContext(), "下载失败，点击重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onInstalled(String fileName, String appName) {
//                TToast.show(getContext(), "安装完成，点击图片打开", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                TToast.show(getContext(), "点击安装", Toast.LENGTH_LONG);
            }
        });
    }

    private void initAd(){
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
//        TTAdManagerHolder.get().requestPermissionIfNecessary(getContext());//非激励视频是不需要加这个
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(getContext());
    }
    private TTFullScreenVideoAd mttFullVideoAd;

    /**
     * 先加载广告再播放广告
     */
    private void showAd(){
        //展示广告，并传入广告展示的场景
        mttFullVideoAd.showFullScreenVideoAd(getActivity(),TTAdConstant.RitScenes.GAME_GIFT_BONUS,null);
        mttFullVideoAd = null;
    }
    /**
     * 先加载激励视频广告再播放广告
     */
    private void showRewardAdCode(){
        //展示激励视频广告，并传入广告展示的场景
        mttRewardVideoAd.showRewardVideoAd(getActivity(),TTAdConstant.RitScenes.CUSTOMIZE_SCENES,"scenes_test");
        mttRewardVideoAd = null;
    }
    /**
     * 加载穿山甲广告
     */
    private void addCSJAd(){
        //设置广告参数
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(AdConfig.verticalFullScreenAdCode)//代码位
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setOrientation(TTAdConstant.VERTICAL)//垂直
                .build();
        //加载全屏视频
        mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int code, String message) {
//                TToast.show(getContext(), message);
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
//                TToast.show(getContext(), "FullVideoAd loaded");
                mttFullVideoAd = ad;
                mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {

                    @Override
                    public void onAdShow() {
//                        TToast.show(getContext(), "FullVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
//                        TToast.show(getContext(), "FullVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
//                        TToast.show(getContext(), "FullVideoAd close");
                    }

                    @Override
                    public void onVideoComplete() {
//                        TToast.show(getContext(), "FullVideoAd complete");
                    }

                    @Override
                    public void onSkippedVideo() {
//                        TToast.show(getContext(), "FullVideoAd skipped");

                    }

                });
            }

            @Override
            public void onFullScreenVideoCached() {
//                TToast.show(getContext(), "FullVideoAd video cached");
                showAd();
            }
        });
    }

    private void loadAd(String codeId, int orientation) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(3)  //奖励的数量
                .setUserID("user123")//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.e("YM","错误码："+code+"-->错误信息:"+message);
//                TToast.show(getContext(), "错误码："+code+"-->错误信息:"+message);
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
//                TToast.show(getContext(), "rewardVideoAd video cached");
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
//                TToast.show(getContext(), "rewardVideoAd loaded");
                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
//                        TToast.show(getContext(), "rewardVideoAd show");
                        loadAd(AdConfig.rewardAdCode,TTAdConstant.VERTICAL);
                        //开始播放时候进行下一个的缓存
                    }

                    @Override
                    public void onAdVideoBarClick() {
//                        TToast.show(getContext(), "rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
//                        TToast.show(getContext(), "rewardVideoAd close");
                        startRain2();//广告播放完成后进行判断红包雨是否还在进行中
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
//                        TToast.show(getContext(), "rewardVideoAd complete");

                    }

                    @Override
                    public void onVideoError() {
//                        TToast.show(getContext(), "rewardVideoAd error");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
//                        TToast.show(getContext(), "verify:" + rewardVerify + " amount:" + rewardAmount +
//                                " name:" + rewardName);
                    }

                    @Override
                    public void onSkippedVideo() {
//                        TToast.show(getContext(), "rewardVideoAd has onSkippedVideo");
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            TToast.show(getContext(), "下载中，点击下载区域暂停", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        TToast.show(getContext(), "下载暂停，点击下载区域继续", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        TToast.show(getContext(), "下载失败，点击下载区域重新下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        TToast.show(getContext(), "下载完成，点击下载区域重新下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        TToast.show(getContext(), "安装完成，点击下载区域打开", Toast.LENGTH_LONG);
                    }
                });
            }
        });
    }

}
