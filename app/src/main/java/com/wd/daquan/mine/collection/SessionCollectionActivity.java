package com.wd.daquan.mine.collection;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.da.library.listener.DialogListener;
import com.da.library.view.CommDialog;
import com.da.library.widget.CommTitle;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.dq.im.type.MessageType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.bean.ShareItemBean;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.helper.DialogHelper;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.bean.UserBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.third.session.extension.DqVideoAttachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2018/6/27.  会话页面用 只负责显示和发送，没有别的功能
 */

public class SessionCollectionActivity extends DqBaseActivity<QCCollectionPresenter, DataBean> implements View.OnClickListener {
    private CommTitle mCommtitle;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private CollectionAdapter mCollectionAdapter;
    private List<CollectionUploadMsgBean> collectionLists = new ArrayList<>();
    private CollectionListBean collectionData = new CollectionListBean();
    private RelativeLayout mNoContentLayout;
    private GroupInfoBean groupEntity;
    private UserBean userEntity;
    private String account;
    private ImType sessionType;

    // 转发视频
    public static final int MSG_TRANSFER_VIDEO = 1;
    // 转发图片
    public static final int MSG_TRANSFER_IMAGE = 2;
    private Handler mMainHandler = new Handler();
    private Handler mHandler = null;
    private HandlerThread mHandlerThread = null;

    @Override
    public QCCollectionPresenter createPresenter() {
        return new QCCollectionPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.session_collection_activity);
    }

    @Override
    protected void init() {
        account = getIntent().getStringExtra("sessionAccount");
        sessionType = (ImType) getIntent().getSerializableExtra("sessionType");
    }

    @Override
    public void initView() {
        mCommtitle = findViewById(R.id.sessionCollectionActivityTitle);
        mRefreshLayout = findViewById(R.id.sessionCollectionActivityRefreshLayout);
        mNoContentLayout = findViewById(R.id.sessionCollectionActivityNoContentLayout);
        mRecyclerView = findViewById(R.id.sessionCollectionActivityRecyclerView);
        mCommtitle.setTitle(getString(R.string.collection));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(8));

        mHandlerThread = new HandlerThread("conversation_collection_list");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
//                if (MSG_TRANSFER_VIDEO == msg.what) {
//                    CollectionUploadMsgBean data = (CollectionUploadMsgBean) msg.obj;
////                    try {
//                    IMMessage videoMessage = getCNVideoMessage(data).imMessage;
//                    sendVideoMessage(videoMessage);
//                } else if (MSG_TRANSFER_IMAGE == msg.what) {
//                    IMMessage message = (IMMessage) msg.obj;
//                    showDialog(message);
//                }
            }
        };
    }


    @Override
    public void initListener() {
        mCommtitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (ImType.Team == sessionType) {
            if (null != mPresenter) {
                // 获取群组信息
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put(KeyValue.GROUP_ID, account);
                hashMap.put(KeyValue.Group.SHOW_GROUP_MEMBER, "0");
                mPresenter.getTeamInfo(DqUrl.url_select_group, hashMap);
            }

        } else {
            if (null != mPresenter) {
                // 获取单人信息
                LinkedHashMap<String, String> userHashMap = new LinkedHashMap<>();
                userHashMap.put("other_uid", account);
                mPresenter.getUserInfo(DqUrl.url_get_userinfo, userHashMap);
            }
        }
        if (null != mPresenter) {
            LinkedHashMap<String, String> collectionListMap = new LinkedHashMap<>();
            collectionListMap.put("page", "1");
            collectionListMap.put("length", "999");
            mPresenter.getCNCollection(DqUrl.url_collection_list, collectionListMap);
        }
        mCollectionAdapter = new CollectionAdapter( KeyValue.ONE);
        mRecyclerView.setAdapter(mCollectionAdapter);
        mCollectionAdapter.setOnClickCollectionListener(new CollectionAdapter.OnClickCollectionListener() {
            @Override
            public void onCollectionListener(CollectionUploadMsgBean data, int position, ImageView view) {//文字 图片和视频
//                tranferMessage(data);
            }

            @Override
            public void onCollectionLongClickListener(CollectionUploadMsgBean data, int position) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v == mCommtitle.getLeftIv()) {
            finish();
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (DqUrl.url_collection_list.equals(url)) {//列表
            if (code != 0) {
                DqToast.showShort(entity.msg);
                return;
            }
//                mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            collectionData = (CollectionListBean) entity.data;
            collectionLists = collectionData.list;
            if (collectionLists == null || collectionLists.size() <= 0) {
                mRefreshLayout.setVisibility(View.GONE);
                mNoContentLayout.setVisibility(View.VISIBLE);
                return;
            }
            mRefreshLayout.setVisibility(View.VISIBLE);
            mNoContentLayout.setVisibility(View.GONE);
            mCollectionAdapter.setBaseUrl(collectionData.uri);
            mCollectionAdapter.update(collectionLists);
        } else if (DqUrl.url_select_group.equals(url)) {// 获取群组成员
            if (!entity.isSuccess()) {
                return;
            }
            groupEntity = (GroupInfoBean) entity.data;
        } else if (DqUrl.url_get_userinfo.equals(url)) { // 获取单人信息
            if (!entity.isSuccess()) {
                return;
            }
            userEntity = (UserBean) entity.data;
        }
    }

//    private void tranferMessage(CollectionUploadMsgBean data) {
//        if (KeyValue.ONE_STRING.equals(data.type)) {//文本
//            IMMessage message = MessageBuilder.createTextMessage(account, sessionType,
//                    data.content);
//            showDialog(message);
//        } else if (KeyValue.THREE_STRING.equals(data.type)) {//图片
////            new getImageCacheAsyncTask(this).execute(collectionData.uri + intentUrl.content);
//            new GetGlideLocalFile().execute(collectionData.uri + data.content);
//        } else if (KeyValue.FOUR_STRING.equals(data.type)) {//视频
//            if (null != mHandler) {
//                showLoading();
//                mHandler.obtainMessage(MSG_TRANSFER_VIDEO, data).sendToTarget();
//            }
//        } else if (KeyValue.TWO_STRING.equals(data.type)) {//语音
//            DqToast.showShort(getString(R.string.voice_no_transfer));
//        }
//
//    }

    private void showSendDialog(final Context mContext, final String targetId, final String avatarUrl, final String conversationTitle,
                                final ImType mConversationType, final ImMessageBaseModel msg) {
        DqApp.getInstance().runInUIThread(new Runnable() {
            @Override
            public void run() {
                ShareItemBean shareItemBean = new ShareItemBean();
                shareItemBean.sessionName = conversationTitle;
                shareItemBean.sessionPortrait = avatarUrl;
                String msgType = "";
                if (MessageType.TEXT == MessageType.typeOfValue(msg.getMsgType())) {
                    msgType = "文本";
                } else if (MessageType.PICTURE == MessageType.typeOfValue(msg.getMsgType())) {
                    msgType = "图片";
                } else if (msg.getContentData() instanceof DqVideoAttachment) {
                    msgType = "视频";
                }
                shareItemBean.shareType = "-1";//区分 发送的来源
                CommDialog mCommDialog = DialogHelper.getInstance().showSDKShareDialog(SessionCollectionActivity.this,
                        shareItemBean, msgType);
                mCommDialog.show();
                mCommDialog.setDialogListener(new DialogListener() {
                    @Override
                    public void onCancel() {
                        mCommDialog.dismiss();
                    }

                    @Override
                    public void onOk() {
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        AudioPlayManager.getInstance().stopPlay();
        super.onDestroy();
        if (null != mHandlerThread) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }

        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        mMainHandler = null;
    }
}
