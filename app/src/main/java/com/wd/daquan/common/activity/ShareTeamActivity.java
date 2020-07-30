package com.wd.daquan.common.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.da.library.listener.IOnItemClickListener;
import com.dq.im.type.ImType;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.wd.daquan.R;
import com.wd.daquan.chat.group.adapter.TeamListAdapter;
import com.wd.daquan.common.bean.ShareItemBean;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.TeamBean;
import com.wd.daquan.model.log.DqToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/25 10:41
 * @Description: 分享群组会话页面
 */
public class ShareTeamActivity extends ShareBaseActivity implements IOnItemClickListener<TeamBean> {

    private int mCurPage = 1;
    private List<TeamBean> mTeamList = new ArrayList<>();
    private TeamListAdapter mAdapter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_share_team);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitleName(getString(R.string.select_team_tips));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TeamListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        getTeamList();
    }

    private void getTeamList() {
        Map<String, String> hashMap = new HashMap<>();
//        hashMap.put("page", "" + mCurPage);
//        hashMap.put("length", "20");
        mPresenter.getTeamList(DqUrl.url_my_groups, hashMap);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAdapter.setListener(this);
    }


    @Override
    public void requestSuccess(String url, int code, DataBean entity) {
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
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurPage = 1;
        getTeamList();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mCurPage++;
        getTeamList();
    }


    @Override
    public void onItemClick(TeamBean teamBean, int position) {

        ShareItemBean shareItemBean = new ShareItemBean();
        shareItemBean.sessionId = teamBean.getGroup_id();
        shareItemBean.sessionName = teamBean.getGroup_name();
        shareItemBean.sessionType = ImType.Team;
        shareItemBean.sessionPortrait = teamBean.group_pic;
        shareMessage(shareItemBean);
    }
}
