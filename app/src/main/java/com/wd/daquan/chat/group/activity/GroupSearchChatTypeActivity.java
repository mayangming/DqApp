package com.wd.daquan.chat.group.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dq.im.model.ImMessageBaseModel;
import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.chat.group.adapter.SearchGroupTradeAdapter;
import com.wd.daquan.chat.group.bean.GroupSearchChatType;
import com.wd.daquan.chat.group.bean.SearchChatBean;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.SpannableStringUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.third.session.SessionHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索制定类型的消息记录
 *
 * 红包无数据，暂时未测
 */
public class GroupSearchChatTypeActivity extends DqBaseActivity<ChatPresenter, DataBean> implements TextWatcher {

    private EditText mSearchEditText;
    private RecyclerView mRecyclerView;
    private TextView mNoData;
    private String mGroupId;
    private List<SearchChatBean> mSearchChatList = new ArrayList<>();
    private ImMessageBaseModel emptyMsg;
    private SearchGroupTradeAdapter mAdapter;
    private GroupSearchChatType.SearchChatType mSearchType;
    private TextView mSearchTypeTv;
    private boolean isLoading;

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.group_search_chat_type_activity);
    }

    @Override
    protected void initView() {
        mSearchTypeTv = findViewById(R.id.group_search_trade_search_hint_tv);
        mSearchEditText = findViewById(R.id.group_search_trade_input_et);
        mRecyclerView = findViewById(R.id.group_search_trade_recycler_view);
        mNoData = findViewById(R.id.group_search_trade_no_data);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchGroupTradeAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void initData() {
        GroupSearchChatType mGroupSearchChatType = getIntent().getParcelableExtra(KeyValue.ENTER_TYPE);
        mGroupId = mGroupSearchChatType.getGroupId();
        mSearchType = mGroupSearchChatType.searchChatType;

        mSearchTypeTv.setText(mSearchType.value);
        reset();
        getMessageList(false);
    }

    @Override
    public void initListener() {
        findViewById(R.id.group_search_trade_title_right_tv).setOnClickListener(this);
        mAdapter.setListener((searchChatBean, position) -> {
            if(searchChatBean == null) {
                return;
            }
            ImMessageBaseModel imMessage = searchChatBean.imMessage;
            if(imMessage == null) {
                return;
            }
            SessionHelper.startTeamSession(getActivity(), imMessage.getMsgIdServer(), imMessage);
        });

        mSearchEditText.addTextChangedListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(!isLoading && !recyclerView.canScrollVertically(1)){
                    isLoading = true;
                    getMessageList(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private List<SearchChatBean> filter(String str){
        List<SearchChatBean> lists = new ArrayList<>();
        for (SearchChatBean chatBean : mSearchChatList) {
            if(chatBean == null) {
                continue;
            }
            ImMessageBaseModel imMessage = chatBean.imMessage;
//            if(imMessage instanceof QcAlipayRpAttachment) {
//                QcAlipayRpAttachment attachment = (QcAlipayRpAttachment) imMessage.getAttachment();
//                if(chatBean.getRemark().contains(str) || attachment.getSendName().contains(str)
//                    || attachment.getBlessing().contains(str)) {
//                    lists.add(chatBean);
//                }
//            }
        }
        return lists;
    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.group_search_trade_title_right_tv) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     *
     */
    private void getMessageList(boolean isSearchMore){
        ImMessageBaseModel anchor = (mSearchChatList.size() > 0 ? mSearchChatList.get(mSearchChatList.size() - 1).imMessage : emptyMsg);

        //没有用到，暂时不分类型搜索
//        MsgTypeEnum typeEnum;
//        if(mSearchType == null) {
//            return;
//
//        }
//
//        switch (mSearchType) {
//            case Trade:
//                typeEnum = MsgTypeEnum.custom;
//                break;
//        }
    }


    /**
     * 刷新数据
     */
    private void updateData(List<SearchChatBean> searchChatBeans, boolean isSearchMore){
        if(mSearchChatList.size() > 0){
            mNoData.setVisibility(View.GONE);
            if(isSearchMore) {
                mAdapter.update(searchChatBeans);
            }else {
                mAdapter.addLists(searchChatBeans);
            }
        }else{
            if(mSearchType == null) {
                return;
            }
            noData(mSearchType.value);
        }
        isLoading = false;
    }

    private void reset() {
        mSearchChatList.clear();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String input = s.toString();
        if(TextUtils.isEmpty(input)) {
            mAdapter.update(mSearchChatList);
            if(mSearchType == null) {
                return;
            }
            noData(mSearchType.value);
            isLoading = false;
        }else {
            isLoading = true;
//            List<SearchChatBean> filterList = filter(input);
            List<SearchChatBean> filterList = new ArrayList<>();
            Log.e("YM","GroupSearchChatTypeActivity------->暂时不可用");
            if(filterList.size() > 0) {
                mAdapter.update(filterList);
                mNoData.setVisibility(View.GONE);
            }else {
                noData(input);
            }
        }
    }

    private void noData(String input) {
        mNoData.setVisibility(View.VISIBLE);
        SpannableString spannableString = SpannableStringUtils.addTextColor(getString(R.string.group_search_chat_no_content, input),
        3, input.length() + 5, getResources().getColor(R.color.color_4768f3));
        mNoData.setText(spannableString);
    }
}
