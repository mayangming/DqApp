package com.wd.daquan.contacts.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.fragment.BaseFragment;
import com.wd.daquan.common.utils.DqUtils;
import com.da.library.constant.IConstant;
import com.wd.daquan.contacts.adapter.TeamInviteAdapter;
import com.wd.daquan.contacts.bean.CommRespEntity;
import com.wd.daquan.model.bean.TeamInviteBean;
import com.wd.daquan.contacts.listener.ITeamInviteListener;
import com.wd.daquan.contacts.presenter.ContactPresenter;
import com.da.library.listener.DialogListener;
import com.da.library.view.CommDialog;
import com.da.library.widget.CommonListDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 群组邀请待确认的群组列表
 */
public class TeamInviteFragment extends BaseFragment<ContactPresenter, DataBean> implements OnRefreshListener {

    private View mNoDataTv;
    private TeamInviteAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.team_invite_fragment;
    }

    @Override
    public ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        mRefreshLayout = view.findViewById(R.id.team_invite_refreshlayout);
        mRecyclerView = view.findViewById(R.id.team_invite_recyclerview);
        mNoDataTv = view.findViewById(R.id.team_invite_empty);
    }

    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter.setITeamInviteListener(mClickListener);
    }

    @Override
    public void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mAdapter = new TeamInviteAdapter();
        mRecyclerView.setAdapter(mAdapter);
        requestData();
    }

    /**
     * 请求列表数据
     */
    private void requestData() {
        mPresenter.getTeamInviteList(DqUrl.url_group_invite_list);
    }

    /**
     * 处理邀请按钮事件
     * @param requestId
     * @param status
     * 1:同意 2:忽略 3:删除
     */
    private void doOptionInvite(String requestId, int status){
        LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put(IConstant.UserInfo.REQUEST_ID, requestId);
        linkedHashMap.put(IConstant.UserInfo.STATUS, status + "");
        mPresenter.getFriendResponse(DqUrl.url_group_invite_response, linkedHashMap);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        requestData();
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        mRefreshLayout.finishRefresh();

        if (DqUrl.url_group_invite_list.equals(url)) {
            List<TeamInviteBean> list = (List<TeamInviteBean>) entity.data;
            if (list.size() == 0) {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                mNoDataTv.setVisibility(View.VISIBLE);
            } else {
                mNoDataTv.setVisibility(View.GONE);
                mAdapter.update(list);
            }

        } else if (DqUrl.url_group_invite_response.equals(url)) {
            CommRespEntity commEntity = (CommRespEntity) entity.data;

            if (commEntity != null && commEntity.isExamine()) {
                showWarning(getActivity(), "该群已开启群认证，请等待管理员审核");
            }

            requestData();
        }
    }

    private void showWarning(Activity activity, String desc) {
        CommDialog commDialog = new CommDialog(activity);
        commDialog.setTitleVisible(false);
        commDialog.setDesc(desc);
        commDialog.setOkTxt(DqApp.getStringById(R.string.comm_ok));
        commDialog.show();

        commDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
            }
        });
    }

    private CommonListDialog deleteDlg = null;
    private void showDelete(String requestId) {
        deleteDlg = new CommonListDialog(this.getActivity());
        deleteDlg.setItem(getString(R.string.delete));
        deleteDlg.show();

        deleteDlg.setListener((item, position) -> doOptionInvite(requestId, 4));
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        mRefreshLayout.finishRefresh();
        if (null == entity) return;
        DqUtils.bequit(entity, this.getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (deleteDlg != null) {
            deleteDlg.dismiss();
            deleteDlg = null;
        }
    }

    private ITeamInviteListener mClickListener = new ITeamInviteListener() {

        @Override
        public void onAgreeClick(int position, TeamInviteBean teamInviteBean) {
            doOptionInvite(teamInviteBean.requestId, 1);
        }

        @Override
        public void onIngoreClick(int position, TeamInviteBean teamInviteBean) {
            doOptionInvite(teamInviteBean.requestId, 2);
        }

        @Override
        public void onLongClick(int position, TeamInviteBean teamInviteBean) {
            showDelete(teamInviteBean.requestId);
        }
    };
}
