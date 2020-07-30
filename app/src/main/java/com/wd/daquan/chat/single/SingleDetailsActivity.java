package com.wd.daquan.chat.single;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.da.library.listener.DialogListener;
import com.da.library.tools.ActivitysManager;
import com.da.library.view.CommDialog;
import com.dq.im.type.ImType;
import com.dq.im.viewmodel.HomeMessageViewModel;
import com.dq.im.viewmodel.P2PMessageViewModel;
import com.netease.nim.uikit.business.session.activity.P2PMessageActivity;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.chat.helper.MuteHelper;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.view.CommSwitchButton;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;

/**
 * 单聊详情页
 * Created by Kind on 2018/9/10.
 */

public class SingleDetailsActivity extends DqBaseActivity<ChatPresenter, DataBean>
        implements View.OnClickListener, QCObserver, CommSwitchButton.OnSwChangedListener {

    // 截屏通知
    private CommSwitchButton mScreenshotSw;
    // 置顶聊天
    private CommSwitchButton mStickSw;
    // 消息免打扰
    private CommSwitchButton mMuteSw;

    // 清除聊天记录
    private LinearLayout mCleanLlyt;

    private Friend mFriend;
    private RecyclerView mRecyclerView;
    private SingleChatDetailsAdapter mAdapter;
    //阅后即焚
    private CommSwitchButton mBurn;
    private String uid;
    private P2PMessageViewModel p2PMessageViewModel;//单人聊天
    private HomeMessageViewModel homeMessageViewModel;//首页聊天内容
    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_single_chat);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initView() {

        mRecyclerView = findViewById(R.id.recycler_view);
        mCleanLlyt = findViewById(R.id.single_clean);
        mMuteSw = findViewById(R.id.single_mute);
        mStickSw = findViewById(R.id.single_stick);
        mScreenshotSw = findViewById(R.id.single_screenshot);
        mBurn = findViewById(R.id.single_burn);

        mBurn.setVisibility(View.GONE);
        mScreenshotSw.setVisibility(View.GONE);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        mAdapter = new SingleChatDetailsAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        toolbarBack();
        mCleanLlyt.setOnClickListener(this);
        mMuteSw.setOnSwChangedListener(this);
        mStickSw.setOnSwChangedListener(this);
        mScreenshotSw.setOnSwChangedListener(this);
        mBurn.setOnSwChangedListener(this);

        MsgMgr.getInstance().attach(this);

        mAdapter.setListener((friend, position) -> {
            if (position == 0) {
                //用户信息界面
                NavUtils.gotoUserInfoActP2P(getActivity(), mFriend.getUid());
            } else {
                NavUtils.gotoCreateFriendsActivity(SingleDetailsActivity.this, mFriend);
            }
        });
    }

    @Override
    protected void initData() {
        uid = getIntent().getStringExtra("uid");
        mFriend = getIntent().getParcelableExtra("friend");

        p2PMessageViewModel = ViewModelProviders.of(this).get(P2PMessageViewModel.class);
        homeMessageViewModel = ViewModelProviders.of(this).get(HomeMessageViewModel.class);
        if (mFriend == null) {
            mFriend = FriendDbHelper.getInstance().getFriend(uid);
            setData();
        } else {
            setData();
        }
    }

    private void setData() {

        mAdapter.update(mFriend);

        //mPresenter.geSettingChat(mFriend.getUid());

        /**
         * 是否提醒该用户发来的消息，false 为静音（不提醒）
         */
        mMuteSw.setCheckedNoEvent(!MuteHelper.getInstance().isNeedMessageNotify(mFriend.getUid()));

        //Log.w("fz", "mFriend.isScreenshot_notify() = " + mFriend.isScreenshot_notify());
        mStickSw.setCheckedNoEvent(mFriend.isTop());
        mScreenshotSw.setCheckedNoEvent(mFriend.isScreenshot_notify());
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        DqToast.showShort(entity != null ? entity.getContent() : "请求出错！");

        if (DqUrl.url_burn_set.equals(url)) {//设置
            mScreenshotSw.setCheckedNoEvent(!mScreenshotSw.isChecked());
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if(entity == null) return;
        if (!entity.isSuccess()) {
            DqToast.showShort(entity.getContent());
            return;
        }

        if (DqUrl.url_setting_chat.equals(url)) {
            GroupInfoBean groupInfo = (GroupInfoBean) entity.data;
            if(groupInfo != null) {
                mScreenshotSw.setCheckedNoEvent(groupInfo.isScreenshot_notify());
            }
        } else if (DqUrl.url_burn_set.equals(url)) {//设置
            GroupInfoBean groupInfo = (GroupInfoBean) entity.data;
            if (!TextUtils.isEmpty(groupInfo.screenshot_notify)) {
                mScreenshotSw.setCheckedNoEvent(groupInfo.isScreenshot_notify());
            }
            DqToast.showShort(entity.getContent());
        }
    }

    /**
     * 消息置顶
     */
    private void top(boolean isChecked) {
        Log.e("YM","暂时不支持置顶功能");
//        RecentContact recentContact = NIMClient.getService(MsgService.class).queryRecentContact(uid, SessionTypeEnum.P2P);
//        if (recentContact != null) {
//
//            mFriend.top = isChecked ? "0" : "1";
//            //缓存到数据库
//            FriendDbHelper.getInstance().update(mFriend, null);
//            mStickSw.setCheckedNoEvent(isChecked);
//            StickHelpler.toggle(recentContact, isChecked);
//            MsgMgr.getInstance().sendMsg(MsgType.MT_GROUP_SETTING_TOP_CHANGE, mFriend.uid);
//        }
    }

    /**
     * 清空历史消息Dialog
     */
    private CommDialog mCommDialog = null;

    private void dismisCommDialog() {
        if (mCommDialog != null) {
            mCommDialog.dismiss();
            mCommDialog = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mCleanLlyt.getId()) {
            dismisCommDialog();
            mCommDialog = new CommDialog(this);
            mCommDialog.setTitleVisible(false);
            mCommDialog.setDescCenter();
            mCommDialog.setDesc(getString(R.string.clean_private_chat_history));
            mCommDialog.setOkTxt(getString(R.string.comm_ok));
            mCommDialog.setOkTxtColor(getResources().getColor(R.color.color_ff0000));
            mCommDialog.setCancelTxt(getString(R.string.comm_cancel));
            mCommDialog.setDialogListener(new DialogListener() {
                @Override
                public void onCancel() {
                }

                @Override
                public void onOk() {
                    mPresenter.clearHistory(mFriend.getUid(), ImType.P2P);
                    p2PMessageViewModel.deleteMessageForFriendId(mFriend.getUid());
                    homeMessageViewModel.deleteForFriendId(mFriend.getUid());
                    MsgMgr.getInstance().sendMsg(MsgType.CLEAR_MSG,null);
                    finish();
                }
            });
            mCommDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == KeyValue.REQUESTCODE_CREATE_GROUP && resultCode == KeyValue.RESULTCODE_CREATE_GROUP){
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismisCommDialog();
        MsgMgr.getInstance().detach(this);
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_FRIEND_REMOVE_FRIEND:
            case MsgType.MT_FRIEND_ADD_BLACK_LIST:
                ActivitysManager.getInstance().finish(this);
                ActivitysManager.getInstance().finish(P2PMessageActivity.class);
                break;
        }
    }

    @Override
    public void onChanged(int id, View v, boolean isChecked) {
        if (id == mScreenshotSw.getId()) {
            DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
            mScreenshotSw.setChecked(false);
//            mPresenter.getScreenShort(mFriend.getUid(), isChecked ? "1" : "0");
        } else if (id == mStickSw.getId()) {
            top(isChecked);
        } else if (id == mMuteSw.getId()) {
            mute(isChecked);
        }else if(id == mBurn.getId()) {
            DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
            mBurn.setChecked(false);
//            if(isChecked) {
//                burn(System.currentTimeMillis());
//            }else {
//                burn(0);
//            }
        }
    }

    //阅后即焚
    private void burn(long burnTime) {
        mFriend.burn = String.valueOf(burnTime);
        FriendDbHelper.getInstance().update(mFriend, null);
    }

    //设置消息免打扰
    private void mute(boolean isChecked) {
        mMuteSw.setCheckedNoEvent(isChecked);

        MuteHelper.getInstance().setNeedMessageNotify(mFriend.getUid(), isChecked);
    }
}
