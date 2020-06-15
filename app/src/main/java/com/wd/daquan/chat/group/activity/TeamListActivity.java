package com.wd.daquan.chat.group.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.da.library.listener.IOnItemClickListener;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.group.adapter.TeamListAdapter;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.TeamBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.third.session.SessionHelper;
import com.da.library.tools.Utils;
import com.da.library.widget.CommTitle;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author: dukangkang
 * @date: 2018/9/19 19:46.
 * 选择一个群组界面
 */
public class TeamListActivity extends DqBaseActivity<TeamPresenter, DataBean> implements
        View.OnClickListener, OnRefreshListener, OnLoadMoreListener, IOnItemClickListener<TeamBean> {

    private int mCurPage = 1;

    private RecyclerView mRecyclerView = null;
    private CommTitle mCommTitle = null;
    private RefreshLayout mRefreshLayout = null;
    private TeamListAdapter mAdapter = null;

    @Override
    protected TeamPresenter createPresenter() {
        return new TeamPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.teamlist_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mCommTitle = this.findViewById(R.id.teamlist_commtitle);
        mRecyclerView = this.findViewById(R.id.teamlist_recyclerview);
        mRefreshLayout = this.findViewById(R.id.teamlist_refreshlayout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TeamListAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mCommTitle.setTitle("选择一个群聊天");
        requestData();
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
        mAdapter.setListener(this);
    }

    private void requestData() {
        if (!Utils.isNetworkConnected(DqApp.sContext)) {
            DqToast.showShort(DqApp.getStringById(R.string.comm_network_error));
            return;
        }
        if (mPresenter == null) {
            return;
        }
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
//        hashMap.put("page", "" + mCurPage);
//        hashMap.put("length", "" + 20);
        mPresenter.getTeamList(DqUrl.url_my_groups, hashMap);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mCommTitle.getLeftIvId()) {
            finish();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurPage = 1;
        requestData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        Log.w("qc_log", "mCurPage = " + mCurPage);
//        mCurPage = ++;
//        requestData();
    }

    private List<TeamBean> mTeamList = new ArrayList<>();

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if(DqUrl.url_my_groups.equals(url)) {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.finishRefresh();
            List<TeamBean> teamBeanList = (List<TeamBean>) entity.data;

            if (null != teamBeanList && teamBeanList.size() > 0) {
                if (mCurPage == 1) {
                    mTeamList.clear();
                    mTeamList.addAll(teamBeanList);
                } else {
                    mTeamList.addAll(teamBeanList);
                }

                mAdapter.update(mTeamList);

            } else {
                DqToast.showShort("没有更多数据了");
            }
        }

    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.finishRefresh();
    }


    @Override
    public void onItemClick(TeamBean teamBean, int position) {
        SessionHelper.startTeamSession(this, teamBean.group_id);
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }
}
