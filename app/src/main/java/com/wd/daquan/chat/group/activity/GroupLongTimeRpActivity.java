package com.wd.daquan.chat.group.activity;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.chat.bean.RobRpEntity;
import com.wd.daquan.chat.group.adapter.GroupLongTimeAdapter;
import com.wd.daquan.chat.group.bean.UnreceivedEntity;
import com.wd.daquan.chat.redpacket.RedPacketHelper;
import com.wd.daquan.chat.redpacket.SingleRedpacketCallback;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.mine.bean.AlipayRedListEntity;
import com.wd.daquan.third.session.extension.QcAlipayRpAttachment;
import com.da.library.view.CommDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author: dukangkang
 * @date: 2019/1/20 14:39.
 * @description: todo ...
 */
public class GroupLongTimeRpActivity extends DqBaseActivity<TeamPresenter, DataBean> implements OnRefreshListener, OnLoadMoreListener, GroupLongTimeAdapter.OnLongTimeListener {

    public static final int MAX_NUM = 20;
    private int mCurPage = 1;

    private String mGroupId = "";

    private TextView mTips = null;
    private TextView mEmptyTips = null;
    private RecyclerView mRecyclerView = null;
    private LinearLayout mEmptyLlyt = null;
    private SmartRefreshLayout mRefreshLayout = null;

    private GroupLongTimeAdapter mAdapter = null;

    private RedPacketHelper mRedPacketHelper = null;

    @Override
    protected TeamPresenter createPresenter() {
        return new TeamPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.group_longtime_rp_activity);
    }

    @Override
    protected void init() {
        mGroupId = getIntent().getStringExtra(KeyValue.GroupManager.GROUP_ID);
    }

    @Override
    protected void initView() {
        mTips = getView(R.id.longtime_rp_tips);
        mEmptyTips = getView(R.id.longtime_rp_empty_tips);
        mRecyclerView = getView(R.id.longtime_rp_recyclerview);
        mEmptyLlyt = getView(R.id.longtime_rp_empty_llyt);
        mRefreshLayout = getView(R.id.longtime_rp_refreshlyt);
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadMore(true);
    }

    @Override
    protected void initData() {
        setTitle("长时间未领红包");
        setBackView();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new GroupLongTimeAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRedPacketHelper = new RedPacketHelper(this);
        mRedPacketHelper.setRedPacketCallback(new RobRedpacketCallback());

        requestData();
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        mAdapter.setOnLongTimeListener(this);
    }

    /**
     * 请求数据
     */
    private void requestData() {
        if (mPresenter == null) {
            return;
        }
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("group_id", mGroupId);
        hashMap.put("page", "" + mCurPage);
        hashMap.put("length", "" + MAX_NUM);
        mPresenter.getLongTimeRp(DqUrl.url_group_redpacket, hashMap);

//        String target = AssetsHelper.getData("longtime1.json");
//        UnreceivedEntity dataEntity = GsonUtils.fromJson(target, UnreceivedEntity.class);
//        test(dataEntity);
    }

//    private void test(UnreceivedEntity unreceivedEntity) {
//        Activity activity = getActivity();
//        if (activity == null) {
//            return;
//        }
//        mTips.setText(unreceivedEntity.info);
//        mEmptyTips.setText(unreceivedEntity.info);
//
//        List<UnreceivedEntity.PacketEntity> list = unreceivedEntity.list;
//        if (null != list && 0 < list.size()) {
//            hideEmpty();
//            if (1 == mCurPage) {
//                mAdapter.update(list);
//            } else {
//                mAdapter.addLists(list);
//            }
//        } else {
//            showEmpty();
//        }
//    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();

        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if(entity == null) {
            return;
        }

        if (0 == entity.result) {
            UnreceivedEntity unreceivedEntity = (UnreceivedEntity) entity.data;
            mTips.setText(unreceivedEntity.info);
            mEmptyTips.setText(unreceivedEntity.info);

            List<UnreceivedEntity.PacketEntity> list = unreceivedEntity.list;
            if (null != list && 0 < list.size()) {
                hideEmpty();
                if (1 == mCurPage) {
                    mAdapter.update(list);
                } else {
                    mAdapter.addLists(list);
                }
            } else {
                int count = mAdapter.getItemCount();
                if (0 >= count) {
                    showEmpty();
                } else {
                    mRefreshLayout.setEnableLoadMore(false);
                }
            }
        } else {
            DqToast.showShort(entity.content);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        if (DqUrl.url_group_redpacket.equals(url)) {
            if (mAdapter != null && 0 >= mAdapter.getItemCount()) {
                showEmpty();
            }
            showWarning(entity.content);
        }
    }

    private void showWarning(String content) {
        CommDialog mCommDialog = new CommDialog(this);
        mCommDialog.setTitleVisible(false);
        mCommDialog.setSingleBtn();
        mCommDialog.setDesc(content);
        mCommDialog.setOkTxt(getString(R.string.comm_ok));
        mCommDialog.setOkTxtColor(getResources().getColor(R.color.text_blue));
        mCommDialog.show();
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mEmptyLlyt.setVisibility(View.GONE);
        mCurPage = 1;
        requestData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mEmptyLlyt.setVisibility(View.GONE);
        ++mCurPage;
        requestData();
    }

    private void showEmpty() {
        mEmptyLlyt.setVisibility(View.VISIBLE);
        mTips.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.GONE);
    }

    private void hideEmpty() {
        mEmptyLlyt.setVisibility(View.GONE);
        mTips.setVisibility(View.VISIBLE);
        mRefreshLayout.setVisibility(View.VISIBLE);
    }

    private QcAlipayRpAttachment mAlipayRpAttachment = null;
    @Override
    public void clickLongTimeRp(UnreceivedEntity.PacketEntity packetEntity) {
        QcAlipayRpAttachment alipayRpAttachment = new QcAlipayRpAttachment();
        alipayRpAttachment.sendId = packetEntity.create_uid;
        alipayRpAttachment.signature = packetEntity.signature;
        alipayRpAttachment.blessing = packetEntity.greetings;
        alipayRpAttachment.sendPic = packetEntity.headpic;
        alipayRpAttachment.sendName = packetEntity.nickname;
        alipayRpAttachment.redpacketId = packetEntity.redpacket_id;

        this.mAlipayRpAttachment = alipayRpAttachment;
        // 检查红包是否禁止收发
        mRedPacketHelper.checkForbidRedpacket(mGroupId);
        // 如果支持则，继续发送

    }

    private class RobRedpacketCallback extends SingleRedpacketCallback {
        @Override
        public void allowForbidRedpacket() {
            super.allowForbidRedpacket();
            DqApp.getInstance().runInUIThread(new Runnable() {
                @Override
                public void run() {
                    QcAlipayRpAttachment alipayRpAttachment = mAlipayRpAttachment;
                    if (alipayRpAttachment == null) {
                        return;
                    }
                    mRedPacketHelper.robRedpacket(alipayRpAttachment.redpacketId, alipayRpAttachment.signature);
                }
            });
        }

        @Override
        public void robRedPacket(RobRpEntity robRpEntity) {
            super.robRedPacket(robRpEntity);
            DqApp.getInstance().runInUIThread(new Runnable() {
                @Override
                public void run() {
                    robRedpacket(robRpEntity);
                }
            });
        }
    }
    /**
     * 抢红包成功
     *
     * @param robRpEntity
     */
    private void robRedpacket(RobRpEntity robRpEntity) {
        if (robRpEntity == null) {
            return;
        }

        QcAlipayRpAttachment alipayRpAttachment = mAlipayRpAttachment;
        if (alipayRpAttachment == null) {
            return;
        }

        if ("3".equals(robRpEntity.status)) { // 红包已过期
            DqToast.showShort("红包已过期");
            return;
        }

        // 群组
        if (KeyValue.ONE_STRING.equals(robRpEntity.whether_receive)) {//红包不可领取直接进入详情
            AlipayRedListEntity.ListBean listBean = new AlipayRedListEntity.ListBean();
            listBean.signature = alipayRpAttachment.signature;
            listBean.redpacket_id = alipayRpAttachment.redpacketId;
            listBean.sessionType = KeyValue.ONE_STRING;//team
            NavUtils.gotoAlipayRedDetailsActivity(this, listBean);

            // 更新红包领取状态
            robRpEntity.is_receive = "0";
            mAdapter.notifyDataSetChanged();
        } else {
            String redpacketExtra = alipayRpAttachment.redpacket_extra;
            if (robRpEntity.number > 0) { // 有未领取红包
                if (TextUtils.isEmpty(redpacketExtra)) {//普通红包
                    NavUtils.gotoRedpacketRobActivity(this, mAlipayRpAttachment, true, "2", "group", mGroupId);
                } else {//专属红包
                    if (redpacketExtra.contains(ModuleMgr.getCenterMgr().getUID())) {//包含自己的话可以直接抢
                        NavUtils.gotoRedpacketRobActivity(this, mAlipayRpAttachment, true, "2", "group", mGroupId);
                    } else {
                        NavUtils.gotoRedpacketExclusiveRobActivity(this, mAlipayRpAttachment, mGroupId);
                    }
                }
            } else { // 红包已领完
                NavUtils.gotoRedpacketRobActivity(this, mAlipayRpAttachment, false, "2", "group", mGroupId);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case KeyValue.RedpacktRob.RESPONSE:
                String mRedPacktId = data.getStringExtra(KeyValue.RedpacktRob.UNRECEIVE);
                mAdapter.addUnreceive(mRedPacktId);
                break;
        }
    }
}
