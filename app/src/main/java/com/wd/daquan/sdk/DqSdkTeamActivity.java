package com.wd.daquan.sdk;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.da.library.widget.CommTitle;
import com.dq.im.type.ImType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.daquan.R;
import com.wd.daquan.chat.group.adapter.TeamListAdapter;
import com.wd.daquan.common.bean.ShareItemBean;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.TeamBean;
import com.wd.daquan.model.log.DqToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2018/11/20.
 */

public class DqSdkTeamActivity extends BaseDqSdkShareActivity implements View.OnClickListener,
        OnRefreshListener, OnLoadMoreListener {

    private CommTitle mCommTitle;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mNoDataTv;
    private TeamListAdapter mAdapter;
    private int mCurPage = 1;
    private List<TeamBean> mTeamList = new ArrayList<>();

    @Override
    protected void setContentView() {
        setContentView(R.layout.sdk_share_team_activity);
    }

    @Override
    protected void initView() {
        mCommTitle = findViewById(R.id.sdkShareTeamtitle);
        mRefreshLayout = findViewById(R.id.sdkShareTeamRefreshLayout);
        mRecyclerView = findViewById(R.id.sdkShareTeamRecyclerView);
        mNoDataTv = findViewById(R.id.sdkShareTeamNoDataTxt);
        mCommTitle.setTitle(getString(R.string.select_team_tips));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter = new TeamListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        getTeamList();
        mAdapter.setListener((teamBean, position) -> {
            ShareItemBean shareItemBean = new ShareItemBean();
            shareItemBean.sessionId = teamBean.group_id;
            shareItemBean.sessionName = teamBean.group_name;
            shareItemBean.sessionType = ImType.Team;
            shareItemBean.sessionPortrait = teamBean.group_pic;
            mSdkHelper.showDialog(DqSdkTeamActivity.this, shareItemBean, mShareBean);
        });
    }

    private void getTeamList() {
        Map<String, String> hashMap = new HashMap<>();
//        hashMap.put("page", "" + mCurPage);
//        hashMap.put("length", "20");
        mPresenter.getTeamList(DqUrl.url_my_groups, hashMap);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mCommTitle.getLeftIvId()) {
            finish();
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if(entity == null) return;
        if (entity.isSuccess()) {
            if (DqUrl.url_my_groups.equals(url)) {
                List<TeamBean> teamListEntity = (List<TeamBean>) entity.data;
                if (null != teamListEntity && teamListEntity.size() > 0) {
                    if (mCurPage == 1) {
                        mTeamList.clear();
                        mTeamList.addAll(teamListEntity);
                    } else {
                        mTeamList.addAll(teamListEntity);
                    }
                    mAdapter.update(mTeamList);
                } else {
                    DqToast.showShort("没有更多数据了");
                }
            }
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        DqUtils.bequit(entity, this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurPage = 1;
        getTeamList();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mCurPage ++;
        getTeamList();
    }
}
