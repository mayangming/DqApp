package com.wd.daquan.contacts.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.TeamBean;
import com.wd.daquan.contacts.holder.SavedTeamsAdapter;
import com.wd.daquan.contacts.listener.IAdapterItemClickListener;
import com.wd.daquan.contacts.presenter.ContactPresenter;
import com.da.library.listener.ITextChangedListener;
import com.wd.daquan.third.session.SessionHelper;
import com.da.library.widget.CommSearchEditText;
import com.da.library.widget.CommTitle;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/20 20:01
 * @Description: 已保存的群组
 */
public class SavedTeamsActivity extends DqBaseActivity<ContactPresenter, DataBean> implements
        View.OnClickListener, OnRefreshListener, ITextChangedListener, IAdapterItemClickListener {

    private CommTitle mTitleLayout;
    private CommSearchEditText mSearchLayout;
    private RecyclerView mRecyclerView;
    private View mNoDataTv;
    private SavedTeamsAdapter mAdapter;
    private SmartRefreshLayout mRefreshLayout;
    private List<TeamBean> mTeamList = new ArrayList<>();

    @Override
    protected ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_saved_teams);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mTitleLayout = findViewById(R.id.saved_team_title_layout);
        mSearchLayout = findViewById(R.id.saved_team_search_layout);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.saved_team_recycler_view);
        mNoDataTv = findViewById(R.id.no_data_tv);

    }

    @Override
    protected void initData() {
        initTitle();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SavedTeamsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        gerDataFormNet();

    }

    private void gerDataFormNet() {
        Map<String, String> hashMap = new HashMap<>();
        mPresenter.getTeamsList(DqUrl.url_get_saved_groups, hashMap);
    }

    private void initTitle() {
        mTitleLayout.setTitle(getString(R.string.saved_group_chat));
        mTitleLayout.getRightTv().setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        mTitleLayout.getLeftIv().setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        mSearchLayout.setChangedListener(this);
        mAdapter.setListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mTitleLayout.getLeftIvId()) {
            finish();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        gerDataFormNet();
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        mRefreshLayout.finishRefresh();
        if(DqUrl.url_get_saved_groups.equals(url)) {
            List<TeamBean> teamBeanList = (List<TeamBean>) entity.data;
            if(teamBeanList != null && teamBeanList.size() > 0) {
                loadTeams(teamBeanList);
            }
        }
    }

    private void loadTeams(List<TeamBean> teamBeanList) {
        if(null != teamBeanList) {
            mTeamList.clear();
            mTeamList.addAll(teamBeanList);
        }

        if (mTeamList.size() > 0) {
            mNoDataTv.setVisibility(View.GONE);
        } else {
            mNoDataTv.setVisibility(View.VISIBLE);
        }
        Collections.reverse(mTeamList);
        mAdapter.update(mTeamList);
    }

    /**
     * 搜索监听
     */
    @Override
    public void textChanged(String content) {
        List<TeamBean> filterList = filter(content);
        mAdapter.update(filterList);

        if (TextUtils.isEmpty(content)) {
            mAdapter.update(mTeamList);
        }

    }

    /**
     * 搜索
     */
    private List<TeamBean> filter(String str) {
        List<TeamBean> list = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return list;
        }
        for (TeamBean bean : mTeamList) {
            if (bean.group_name.contains(str)){
                list.add(bean);
            }
        }
        return list;
    }

    @Override
    public void beforeChange() {

    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        mRefreshLayout.finishRefresh();
        if(null == entity) return;
        DqUtils.bequit(entity, this);
    }

    @Override
    public void onItemClick(int position) {
        TeamBean item = mAdapter.getItem(position);
        if(null != item) {
            SessionHelper.startTeamSession(getActivity(), item.group_id);
        }
    }
}
