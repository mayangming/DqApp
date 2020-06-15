package com.wd.daquan.mine.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.mine.adapter.BlackListAdapter;
import com.da.library.widget.CommTitle;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.third.friend.SetFriendInfoHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/***
 * 黑名单列表
 */
public class BlackListActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener, QCObserver {


    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerview;
    private TextView txt_noBlackList;
    private CommTitle mCommTitle;
    private ClassicsFooter mClassicsFooter;
    private BlackListAdapter mBlackListAdapter;
    private Friend removeBlackItem;
    private List<Friend> mListDatas = new ArrayList<>();
    private SetFriendInfoHelper mInfoHelper;

    @Override
    protected void setContentView() {
        setContentView(R.layout.black_list_activity);
    }

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.blackActivityTitle);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerview = findViewById(R.id.comm_recycler_view);
        mClassicsFooter = findViewById(R.id.comm_recycler_view_footer);
        txt_noBlackList = findViewById(R.id.blackActivityTextNo);
        mClassicsFooter.setVisibility(View.GONE);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommTitle.setTitle(getString(R.string.black_list));
    }
    @Override
    public void initData() {
        mBlackListAdapter = new BlackListAdapter();
        mRecyclerview.setAdapter(mBlackListAdapter);
        requestData();
        mInfoHelper = new SetFriendInfoHelper(this);
    }

    @Override
    public void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        txt_noBlackList.setOnClickListener(v->requestData());
        mBlackListAdapter.setOnBlackListListener(new BlackListAdapter.OnBlackListListener() {
            @Override
            public void onLayout(Friend data) {//进入个人详情

            }

            @Override
            public void onRemove(Friend data) {//移除
                removeBlackItem = data;
                mInfoHelper.removeBlack(data.uid);
            }
        });

        MsgMgr.getInstance().attach(this);

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestData();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }
        });
    }

    private void requestData(){
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("page", "1");
        linkedHashMap.put("length", "9999");
        mPresenter.getBlacklist(DqUrl.url_get_black_list, linkedHashMap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        mRefreshLayout.finishRefresh();
        if (DqUrl.url_get_black_list.equals(url)){
            useCacheData();
        }
        DqUtils.bequit(entity,this);
    }

    private void useCacheData() {
        List<Friend> blackList = FriendDbHelper.getInstance().getBlackList();
        if(blackList.size() > 0) {
            mListDatas.clear();
            mListDatas.addAll(blackList);
            mBlackListAdapter.update(mListDatas);
            txt_noBlackList.setVisibility(View.GONE);
        }else {
            txt_noBlackList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        mRefreshLayout.finishRefresh();
        if (DqUrl.url_get_black_list.equals(url)) {//黑名单列表接口
            List<Friend> data = (List<Friend>) entity.data;
            if(data != null && data.size() > 0) {
                mListDatas.clear();
                mListDatas.addAll(data);
                mBlackListAdapter.update(mListDatas);
               txt_noBlackList.setVisibility(View.GONE);
            }else {
                txt_noBlackList.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(mInfoHelper != null) {
            mInfoHelper.detach();
        }
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }

    @Override
    public void onMessage(String key, Object value) {
        if(MsgType.MT_FRIEND_REMOVE_BLACK_LIST.equals(key)) {
            if(removeBlackItem == null) {
                return;
            }
            mListDatas.remove(removeBlackItem);
            mBlackListAdapter.update(mListDatas);
            txt_noBlackList.setVisibility(mListDatas.size() > 0 ? View.GONE : View.VISIBLE);
            removeBlackItem.whether_friend = "0";
            removeBlackItem.whether_black = "1";
            FriendDbHelper.getInstance().update(removeBlackItem, null);
        }
    }
}
