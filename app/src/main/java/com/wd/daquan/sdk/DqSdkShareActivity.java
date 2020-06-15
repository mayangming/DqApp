package com.wd.daquan.sdk;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.da.library.widget.CommTitle;
import com.wd.daquan.R;
import com.wd.daquan.common.adapter.ShareAdapter;
import com.wd.daquan.common.bean.ShareItemBean;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.sdk.bean.SdkShareBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分享到最近联系人
 */
public class DqSdkShareActivity extends BaseDqSdkShareActivity implements View.OnClickListener, QCObserver {
    private RecyclerView recyclerView;

    private TextView imgLoading;
    private CommTitle mCommTitle;
    private ShareAdapter mShareAdapter;
    private String appId;
    private String appSecret;
    private SdkShareBean.ShareType shareType;
    private List<ShareItemBean> mChatList = new ArrayList<>();

    @Override
    protected void setContentView() {
        setContentView(R.layout.recently_contacts_activity);
    }

    @Override
    protected void initView() {
        mCommTitle = findViewById(R.id.recentlyContactsTitle);
        imgLoading = findViewById(R.id.recentlyContactsImg);
        recyclerView = findViewById(R.id.recentlyContactsRecyclerView);
        mCommTitle.setTitle(getString(R.string.recent_contact_txt));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        findViewById(R.id.recentlyContactsListLayout).setOnClickListener(this);
        imgLoading.setOnClickListener(this);
        mShareAdapter = new ShareAdapter();
        recyclerView.setAdapter(mShareAdapter);
        mShareAdapter.setListener(shareItemBean -> {
            mSdkHelper.showDialog(DqSdkShareActivity.this, shareItemBean, mShareBean);
//                showDialog(shareItemBean);
        });
        MsgMgr.getInstance().attach(this);


    }
    /**
     * 分享统计
     * */
    private void requestShareStatistics(){
        String uid = ModuleMgr.getCenterMgr().getUID();
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("appId", appId);
        hashMap.put("type", shareType.value+"");
        hashMap.put("uid", uid);
        mPresenter.sdkShareRecord(DqUrl.url_sdk_get_share_statistics, hashMap);
    }

    @Override
    public void initData() {
        super.initData();
        getConversationList();
        appId = mShareBean.appId;
        appSecret =  mShareBean.appSecret;
        shareType = mShareBean.type;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                MsgMgr.getInstance().sendMsg(KeyValue.SDK_MAIN_FINISH, null);
                finish();
                break;
            case R.id.recentlyContactsImg:
                getConversationList();
            break;
            case R.id.recentlyContactsListLayout:
                NavUtils.gotoCreateNewChatActivity(DqSdkShareActivity.this, mShareBean);
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if(!url.equals(DqUrl.url_sdk_get_share_statistics)){
            DqToast.showShort(entity.content);
            finish();
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        requestShareStatistics();
//        if(DqUrl.url_get_app_info.equals(url)){
//            if (0 == code) {
//                OpenSdkLoginBean openSdkLoginBean = (OpenSdkLoginBean) entity.intentUrl;
//                String thirdUid = openSdkLoginBean.uid;
//                String userType = openSdkLoginBean.user_type;
//
//                if("1".equals(userType)){//闲来
//                    if(TextUtils.isEmpty(thirdUid)){
//                        DqToast.showShort("参数错误！！！");
//                        finish();
//                        return;
//                    }
//                    if(!ModuleMgr.getCenterMgr().getUID().equals(thirdUid)){
//                        CommDialog mCommDialog = DialogUtils.showChangeAcountDialog(this,
//                                getString(R.string.dialog_third_account_title),
//                                getString(R.string.dialog_third_account_count), getString(R.string.cancel),
//                                getString(R.string.dialog_third_account_switch), new DialogListener() {
//                                    @Override
//                                    public void onCancel() {
//                                        finish();
//                                    }
//
//                                    @Override
//                                    public void onOk() {
//                                        LogoutHelper.logout(DqSdkShareActivity.this);
//                                    }
//                                });
//                        mCommDialog.setCancelTxtColor(getResources().getColor(R.color.color_ff0000));
//                        mCommDialog.setOkTxtColor(getResources().getColor(R.color.color_5495e8));
//                        mCommDialog.show();
//                    }
//                }
//            }
//        }
    }
    //搜索会话
    public void getConversationList(){
        //获取会话列表
//        NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
//
//            @Override
//            public void onResult(int code, List<RecentContact> recents, Throwable exception) {
//                if (code != ResponseCode.RES_SUCCESS || recents == null){
//                    recyclerView.setVisibility(View.GONE);
//                    imgLoading.setVisibility(View.VISIBLE);
//
//                }else {
//                    if (null == mChatList) return;
//                    recyclerView.setVisibility(View.VISIBLE);
//                    imgLoading.setVisibility(View.GONE);
//                    mChatList.clear();
//
//                    StickHelpler.sortRecentContacts(recents);
//
//                    for (RecentContact contact : recents) {
//                        if (null == contact) {
//                            continue;
//                        }
//
//                        ShareItemBean shareItem = new ShareItemBean();
//                        shareItem.sessionId = contact.getContactId();
//                        shareItem.sessionType = contact.getSessionType();
//                        if (SessionTypeEnum.P2P == contact.getSessionType()) {
//                            shareItem.sessionName = UserInfoHelper.getUserDisplayName(contact.getContactId());
//                            shareItem.sessionPortrait = UserInfoHelper.getHeadPic(contact.getContactId());
//                        } else if (SessionTypeEnum.Team == contact.getSessionType()) {
//                            shareItem.sessionName = TeamHelper.getTeamName(contact.getContactId());
//                            shareItem.sessionPortrait = TeamHelper.getTeamHeadPic(contact.getContactId());
//                        }
//
//                        mChatList.add(shareItem);
//                    }
//                    mShareAdapter.update(mChatList);
//                }
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }

    @Override
    public void onMessage(String key, Object value) {
        if(MsgType.MT_RECENT_CONTACTS_FINISH.equals(key)){
            finish();
        }else if(MsgType.MT_App_Login.equals(key)) {
            boolean isLogin = (boolean) value;
            Log.e("dq", "isLogin ： " + isLogin);
            if(isLogin) {
                getConversationList();
            }
        }
    }
}
