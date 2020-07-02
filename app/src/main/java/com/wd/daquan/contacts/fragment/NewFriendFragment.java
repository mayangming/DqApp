package com.wd.daquan.contacts.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.da.library.constant.IConstant;
import com.da.library.widget.CommonListDialog;
import com.dq.im.type.ImType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.fragment.BaseFragment;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.contacts.adapter.NewFriendAdapter;
import com.wd.daquan.contacts.listener.INewFriendAdapterClickListener;
import com.wd.daquan.contacts.presenter.ContactPresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.NewFriendBean;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class NewFriendFragment extends BaseFragment<ContactPresenter, DataBean> implements OnRefreshListener {

    private View mNoDataTv;
    private NewFriendAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefreshLayout;

    private CommonListDialog mListDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.new_friend_fragment;
    }

    @Override
    public ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        mRefreshLayout = view.findViewById(R.id.new_friend_refreshlayout);
        mRecyclerView = view.findViewById(R.id.new_friend_recyclerview);
        mNoDataTv = view.findViewById(R.id.new_friend_empty);
    }

    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter.setListener(mClickListener);
    }

    @Override
    public void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mAdapter = new NewFriendAdapter();
        mRecyclerView.setAdapter(mAdapter);
        requestData(true);
    }

    private void requestData(boolean isShow) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.Contact.PAGE, "1");
        hashMap.put(IConstant.Contact.LENGTH, "999");
        mPresenter.getFriendRequestList(DqUrl.url_friend_request_list, hashMap, isShow);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        mRefreshLayout.finishRefresh();
        if (null == entity) {
            return;
        }
        if (DqUrl.url_friend_request_list.equals(url)) {
            List<NewFriendBean> friendBeans = (List<NewFriendBean>) entity.data;
            if (null != friendBeans && friendBeans.size() > 0) {
                mNoDataTv.setVisibility(View.GONE);
                mAdapter.update(friendBeans);
            } else {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                mNoDataTv.setVisibility(View.VISIBLE);
            }
        } else if (DqUrl.url_friend_response.equals(url)) {
            //QcToastUtil.showToast(this, entity.content);

            requestData(false);
            // 后台未返回对应标示数据
//            DqToast.showShort(entity.content);
            if(mItem != null) {
                Friend friend = new Friend();
                friend.uid = mItem.uid;
                friend.headpic = mItem.headpic;
                friend.nickname = mItem.nickname;
                friend.phone = mItem.phone;
                friend.whether_friend = "0";
                friend.whether_black = "1";

                FriendDbHelper.getInstance().update(friend, null);
                MsgMgr.getInstance().sendMsg(MsgType.MT_FRIEND_ADD_FRIEND, "");
            }
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        mRefreshLayout.finishRefresh();
        if (null == entity) return;
        DqUtils.bequit(entity, this.getActivity());
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        requestData(true);
    }

    private NewFriendBean mItem;
    private INewFriendAdapterClickListener mClickListener = new INewFriendAdapterClickListener() {

        @Override
        public void onItemClick(int position, NewFriendBean item) {
            //item点击事件
            if (null == item) return;
            String status = item.response_status;
            if ("0".equals(item.whether_friend)) {
                if ("1".equals(status)) {//在当前界面点击同意
                    NavUtils.gotoUserInfoActivity(getActivity(), item.uid, ImType.P2P.getValue());
                } else {
                    NavUtils.gotoNewFriendDetailActivity(getActivity(), item);
                }
            } else {
                NavUtils.gotoUserInfoActivity(getActivity(), item.uid, ImType.P2P.getValue());
            }
        }

        @Override
        public void onAgreeClick(int position, NewFriendBean item) {
            if (null == item) return;
            String status = item.response_status;
            mItem = item;
            if ("0".equals(status)) {
                requestFriend(item, "1");
            }
        }

        @Override
        public void onItemLongClick(int position, NewFriendBean item) {
            //QcToastUtil.showToast(DqApp.sContext, "删除 ： " + position);
            if (null == item) return;
            showListDialog(item);
        }
    };

    private void showListDialog(NewFriendBean newFriendBean) {
        mListDialog = new CommonListDialog(this.getActivity());
        mListDialog.setItem(getString(R.string.delete));
        mListDialog.show();

        mListDialog.setListener((item, position) -> requestFriend(newFriendBean, "4"));
    }

    private String getTips(String tag) {
        if (KeyValue.ONE_STRING.equals(tag)) {
            return "已添加";
        } else if (KeyValue.TWO_STRING.equals(tag)) {
            return "已拒绝";
        } else if (KeyValue.THREE_STRING.equals(tag)) {
            return "已忽略";
        } else if (KeyValue.FOUR_STRING.equals(tag)) {
            return "已删除";
        }
        return "";
    }

    /**
     *
     * @param item
     * @param status
     * 1: 添加好友 4:删除当前数据
     */
    private void requestFriend(NewFriendBean item, String status) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.UserInfo.REQUEST_ID, item.request_id);
        hashMap.put(IConstant.UserInfo.STATUS, status);
        mPresenter.getFriendInviteResponse(DqUrl.url_friend_response, hashMap);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListDialog != null) {
            mListDialog.dismiss();
            mListDialog = null;
        }
    }
}
