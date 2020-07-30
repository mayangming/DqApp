package com.wd.daquan.chat.group.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dq.im.type.ImType;
import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.chat.group.adapter.ExitGroupMembersAdapter;
import com.wd.daquan.chat.group.bean.GroupExitMemberEntity;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 退群成员列表
 * Created by Kind on 2018/9/26.
 */

public class ExitGroupMembersActivity extends DqBaseActivity<ChatPresenter, DataBean> {

    private RecyclerView mRecyclerView;
    private TextView mNoDataTv;
    private String mGroupId;
    private List<GroupExitMemberEntity> mGroupMembers = new ArrayList<>();
    private ExitGroupMembersAdapter mAdapter = null;


    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.cn_exit_group_members_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        setTitle(getString(R.string.list_of_exit_group_member));
        setBackView();
        mRecyclerView = findViewById(R.id.recycler_view);
        mNoDataTv = findViewById(R.id.no_data_tv);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ExitGroupMembersAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mGroupId = getIntent().getStringExtra(KeyValue.GROUP_ID);

        if (!TextUtils.isEmpty(mGroupId)) {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put(KeyValue.GROUP_ID, mGroupId);
            mPresenter.getExitGroupMembers(DqUrl.url_group_quit_record, hashMap);
        }
    }

    @Override
    protected void initListener() {
        mAdapter.setListener((int position) -> {
            GroupExitMemberEntity item = mAdapter.getItem(position);
            if (item == null) return;
            NavUtils.gotoUserInfoActivity(ExitGroupMembersActivity.this, item.uid, ImType.P2P.getValue(),
                    "", false, true, false, false);
        });
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        DqToast.showShort(entity == null ? "请求出错！" : entity.getContent());

    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (entity == null) return;
        if (entity.isSuccess() && DqUrl.url_group_quit_record.equals(url)) {
            updateData(entity);
        }
    }

    /**
     * 更新数据
     *
     * @param dataEntity 数据
     */
    private void updateData(DataBean<List<GroupExitMemberEntity>> dataEntity) {
        List<GroupExitMemberEntity> list = dataEntity.data;
        if (null != list && list.size() > 0) {
            mGroupMembers.clear();
            mGroupMembers.addAll(list);
        }

        if (mGroupMembers.size() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            mNoDataTv.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoDataTv.setVisibility(View.GONE);
            mAdapter.initDatas(mGroupMembers);
        }
    }
}