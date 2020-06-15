package com.wd.daquan.chat.group.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.chat.group.adapter.SearchGroupAidesAdapter;
import com.wd.daquan.chat.group.bean.PluginBean;
import com.wd.daquan.chat.group.bean.SearchGroupAidesBean;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.da.library.listener.DialogListener;
import com.da.library.listener.ITextChangedListener;
import com.da.library.tools.Utils;
import com.da.library.view.CommDialog;
import com.da.library.widget.CommSearchEditText;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2019/1/4 16:07
 * @Description:
 */
public class SearchGroupAidesActivity extends DqBaseActivity<ChatPresenter, DataBean<SearchGroupAidesBean>>
        implements OnLoadMoreListener, OnRefreshListener, QCObserver, View.OnClickListener, ITextChangedListener {

    private EditText mSearchInputEt;
    private CommSearchEditText mSearchLayout;
    private SmartRefreshLayout mRefreshLayout;
    private View mSearchHintTv;
    private int mPage = 1;
    private SearchGroupAidesAdapter mAdapter;
    private String mGroupId;
    private PluginBean mPluginBean;
    private String mPluginId;
    private CommDialog mAddAidesDialog;

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_search_group_aides);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mSearchLayout = findViewById(R.id.search_group_assist_title_layout);
        mSearchInputEt = mSearchLayout.getEditText();

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mSearchHintTv = findViewById(R.id.search_group_aides_search_hint_tv);

        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchGroupAidesAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mGroupId = getIntent().getStringExtra(KeyValue.GROUP_ID);
        mPluginId = getIntent().getStringExtra(KeyValue.aides.PLUGIN_ID);

        mAdapter.setPluginId(mPluginId);
    }

    //获取列表数据
    private void requestListData() {
        String searchContent = mSearchInputEt.getText().toString().trim();

        if(TextUtils.isEmpty(searchContent)) {
            DqToast.showShort("搜索内容不能为空");
            return;
        }
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(KeyValue.GROUP_ID, mGroupId);
        hashMap.put(KeyValue.PAGE, String.valueOf(mPage));
        hashMap.put(KeyValue.LENGTH, "10");
        hashMap.put(KeyValue.aides.PLUGIN_NAME, searchContent);
        mPresenter.getGroupAidesInfo(DqUrl.url_search_group_aides, hashMap);
    }

    @Override
    protected void initListener() {
        mSearchLayout.getSearchConfirmTv().setOnClickListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setOnRefreshListener(this);

        mAdapter.setAidesAdapterListener(new SearchGroupAidesAdapter.ISearchGroupAidesAdapterListener() {
            @Override
            public void onItemClick(PluginBean pluginBean) {
                boolean isAdd = pluginBean.getPlugin_id().equals(mPluginId);
                NavUtils.gotoGroupAidesDetailsActivity(SearchGroupAidesActivity.this, pluginBean, mGroupId, isAdd);
            }

            @Override
            public void onAddClick(PluginBean pluginBean) {
                mPluginBean = pluginBean;
                showAddAidesDialog(pluginBean);
            }
        });

        MsgMgr.getInstance().attach(this);
        mSearchLayout.setChangedListener(this);
//        mSearchLayout.setChangedListener(this);
    }

    private void showAddAidesDialog(PluginBean pluginBean) {
        mAddAidesDialog = new CommDialog(this);
        mAddAidesDialog.setTitleGone();
        mAddAidesDialog.setDesc("是否确认将" + pluginBean.getPlugin_name() + "设置为群助手？");
        mAddAidesDialog.setDescTextSize(16);
        mAddAidesDialog.setOkTxt(getString(R.string.confirm1));
        mAddAidesDialog.setOkTxtColor(getResources().getColor(R.color.text_blue_pressed));
        mAddAidesDialog.show();

        mAddAidesDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                requestAddAides(pluginBean.getPlugin_id());
            }
        });

    }


    //添加群助手
    private void requestAddAides(String pluginId) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(KeyValue.GROUP_ID, mGroupId);
        hashMap.put(KeyValue.aides.PLUGIN_ID, pluginId);
        mPresenter.setGroupAidesInfo(DqUrl.url_group_aides_pligin_set, hashMap);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mSearchLayout.getSearchConfirmTv().getId()) {
            requestListData();
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if(notNet()) {
            return;
        }
        mPage++;
        requestListData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if(notNet()) {
            return;
        }
        mPage = 1;
        requestListData();
    }

    public boolean notNet(){
        if(Utils.isNetworkConnected(this)) {
            return false;
        }
        mRefreshLayout.finishRefresh(500);
        mRefreshLayout.finishLoadMore(500);
        DqToast.showShort(R.string.net_error);
        return true;
    }

    @Override
    public void onSuccess(String url, int code, DataBean<SearchGroupAidesBean> entity) {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        if(entity == null) return;

        if(DqUrl.url_search_group_aides.equals(url)) {

            SearchGroupAidesBean data = entity.data;
            if(data != null) {
                mSearchHintTv.setVisibility(View.GONE);
                List<PluginBean> pluginList = data.getList();
                if(mPage == 1) {
                    mAdapter.update(pluginList);
                    if(pluginList.size() <= 0) {
                        DqToast.showShort("未检索到相关群助手");
                    }
                }else {
                    mAdapter.addLists(pluginList);
                }
            }else {
                if(mPage == 1) {
                    mSearchHintTv.setVisibility(View.VISIBLE);
                }
            }
        }else if(DqUrl.url_group_aides_pligin_set.equals(url)) {
            MsgMgr.getInstance().sendMsg(MsgType.MT_GROUP_UPDATE_AIDES, mPluginBean);
            finish();
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean<SearchGroupAidesBean> entity) {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        if(entity == null) return;
        entity.bequit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
        if(mAddAidesDialog != null) {
            mAddAidesDialog.dismiss();
            mAddAidesDialog = null;
        }
    }

    @Override
    public void onMessage(String key, Object value) {
        if(MsgType.MT_GROUP_UPDATE_AIDES.equals(key)) {
            finish();
        }
    }

    @Override
    public void textChanged(String content) {
        if(TextUtils.isEmpty(content)) {
            mSearchHintTv.setVisibility(View.VISIBLE);
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
            mRefreshLayout.setEnableRefresh(false);
            mRefreshLayout.setEnableLoadMore(false);
        }else {
            mSearchHintTv.setVisibility(View.GONE);
            mRefreshLayout.setEnableRefresh(true);
            mRefreshLayout.setEnableLoadMore(true);
        }
    }

    @Override
    public void beforeChange() {

    }
}
