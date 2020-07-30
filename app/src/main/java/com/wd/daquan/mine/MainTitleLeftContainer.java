package com.wd.daquan.mine;

import android.Manifest;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.da.library.tools.Utils;
import com.wd.daquan.DqApp;
import com.wd.daquan.MainActivity;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;

public class MainTitleLeftContainer implements View.OnClickListener, QCObserver {

    private LinearLayout mHeadLl;
    private ImageView mHeadIv;
    private TextView mNameTv;
    private TextView mDqNumTv;
    private LinearLayout mScan;
    private LinearLayout mAlipay;
    private LinearLayout mCollection;
    private LinearLayout mSetting;
    private LinearLayout mHelp;
    private LinearLayout mFeedback;
//    private LinearLayout layoutCollection;
    protected String[] needPermissions = {Manifest.permission.CAMERA};
//    private LinearLayout mMineWalletLayout;
    private MainActivity mActivity;
//    private DrawerLayout mDrawerLayout;
//    private NavigationView mMenu;

    public MainTitleLeftContainer(MainActivity activity, DrawerLayout view) {
        mActivity = activity;
        view.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        mDrawerLayout = view;
        initView(view);
        initData();
        initListener();
    }

    private void initView(View view) {
        NavigationView navigationView = view.findViewById(R.id.nv_main_drawer);
        View headerView = navigationView.getHeaderView(0);

        mHeadLl = headerView.findViewById(R.id.main_title_left_head_ll);
        mHeadIv = headerView.findViewById(R.id.main_title_left_head_iv);
        mNameTv = headerView.findViewById(R.id.main_title_left_name_tv);
        mDqNumTv = headerView.findViewById(R.id.main_title_left_dq_num_tv);

        mScan = headerView.findViewById(R.id.main_title_left_scan);
        mAlipay = headerView.findViewById(R.id.main_title_left_alipay);
        mCollection = headerView.findViewById(R.id.main_title_left_collection);
        mSetting = headerView.findViewById(R.id.main_title_left_setting);
        mHelp = headerView.findViewById(R.id.main_title_left_help);
        mFeedback = headerView.findViewById(R.id.main_title_left_feedback);
    }

    private void initData() {
        updateUserInfo();
    }

    private void updateUserInfo(){
        String nickName = ModuleMgr.getCenterMgr().getNickName();
        String headPic = ModuleMgr.getCenterMgr().getAvatar();
        String qingChatNum = ModuleMgr.getCenterMgr().getDqNum();
        if(!TextUtils.isEmpty(nickName)){
            mNameTv.setText(nickName);
        }
        if (!TextUtils.isEmpty(headPic)) {
            GlideUtils.loadHeader(DqApp.sContext, headPic + DqUrl.url_avatar_suffix, mHeadIv);
        }
        if (!TextUtils.isEmpty(qingChatNum)) {
            mDqNumTv.setText(String.format(DqApp.sContext.getString(R.string.mine_chuiniu_num), qingChatNum));
            mDqNumTv.setVisibility(View.VISIBLE);
        } else {
            mDqNumTv.setVisibility(View.GONE);
        }
    }


    private void initListener() {

        MsgMgr.getInstance().attach(this);
        mHeadLl.setOnClickListener(this);
        mScan.setOnClickListener(this);
        mAlipay.setOnClickListener(this);
        mCollection.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mHelp.setOnClickListener(this);
        mFeedback.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(Utils.isFastDoubleClick(500)) {
            return;
        }
        switch (view.getId()) {
            case R.id.main_title_left_head_ll://个人信息
                NavUtils.gotoPersonalInfoActivity(mActivity);
                break;
            case R.id.main_title_left_collection://收藏
                //NavUtils.gotoCollectionActivity(mActivity);
                DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
                break;
            case R.id.main_title_left_setting://设置
                NavUtils.gotoSettingActivity(mActivity);
                break;
            case R.id.main_title_left_scan://扫一扫
                if (DqUtils.checkPermissions(mActivity, needPermissions)) {
                    NavUtils.gotoScanQRCodeActivity(mActivity);
                }
                break;
            case R.id.main_title_left_alipay://支付宝红包
                //mPresenter.getIsBindAlipay(DqUrl.url_isBindAlipay, new LinkedHashMap<>());
                break;

            case R.id.main_title_left_help://帮助
                DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
                //NavUtils.gotoWebviewActivity(mActivity, DqUrl.url_help, DqApp.getStringById(R.string.mine_help));
                break;
            case R.id.main_title_left_feedback://意见反馈
                DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
                //NavUtils.gotoFeedBack(mActivity);
                break;
            default:
                break;
        }

    }

    @Override
    public void onMessage(String key, Object value) {
        if(MsgType.MT_CENTER_PERSONALINFO_CHANGE.equals(key)){
            updateUserInfo();
//            NIMRedPacketClient.updateMyInfo(null);
        }
    }

    public void onDestroy() {
        MsgMgr.getInstance().detach(this);
    }

}
