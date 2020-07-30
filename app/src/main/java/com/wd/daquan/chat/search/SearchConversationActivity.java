package com.wd.daquan.chat.search;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.da.library.listener.IOnItemClickSelectListener;
import com.da.library.listener.ITextChangedListener;
import com.da.library.tools.Utils;
import com.da.library.widget.CommSearchEditText;
import com.dq.im.type.ImType;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.wd.daquan.R;
import com.wd.daquan.chat.bean.RecentContactEntity;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.third.session.SessionHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dukangkang
 * @date: 2018/9/26 18:03.
 * @description: todo ...
 */
public class SearchConversationActivity extends DqBaseActivity<SearchPresenter, DataBean> implements IOnItemClickSelectListener<RecentContactEntity> {

    private List<RecentContactEntity> mRecentList = new ArrayList<>();

    private CommSearchEditText mSearchEditText;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private TextView mTips;
    private TextView mEmptyView;

    private SearchConversationAdapter mAdapter = null;

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.search_conversation_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mSearchEditText = this.findViewById(R.id.search_conversation_commsearch);
        mRefreshLayout = this.findViewById(R.id.search_conversation_refreshlayout);
        mRecyclerView = this.findViewById(R.id.search_conversation_recyclerview);
        mTips = this.findViewById(R.id.search_conversation_tips);
        mEmptyView = this.findViewById(R.id.search_conversation_emptyview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchConversationAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        requestData();
    }

    @Override
    protected void initListener() {
        mSearchEditText.setChangedListener(mTextChangedListener);
        mAdapter.setItemClickListener(this);
    }

    private void requestData() {
        //之前是云信
        Log.e("YM","查询最近的联系人数据");
    }

    private ITextChangedListener mTextChangedListener = new ITextChangedListener() {
        @Override
        public void beforeChange() {

        }

        @Override
        public void textChanged(String content) {
            List<RecentContactEntity> list = filterConverdsation(content);

            mEmptyView.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);

            if (TextUtils.isEmpty(content)) {
                mTips.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
            } else {
                mTips.setVisibility(View.GONE);
            }

            mAdapter.update(list);
        }
    };

    private List<RecentContactEntity> filterConverdsation(String str) {
        List<RecentContactEntity> list = new ArrayList<>();

        if(TextUtils.isEmpty(str)) {
            return list;
        }


        for (RecentContactEntity entity : mRecentList) {
            if (entity == null) {
                continue;
            }

            if (entity.sessionType == ImType.P2P || entity.sessionType == ImType.Team) {
                if (entity.fromNick.contains(str)) {
                    list.add(entity);
                }
            }
        }

        return list;
    }

    @Override
    public void onItemClick(@NonNull RecentContactEntity entity) {
        if (entity == null) {
            return;
        }

        if (Utils.isFastDoubleClick(500)) {
            return;
        }

        if (entity.sessionType == ImType.P2P) {
            SessionHelper.startP2PSession(getActivity(), entity.contactId);
        } else {
            SessionHelper.startTeamSession(getActivity(), entity.contactId);
        }

        finish();
    }
}
