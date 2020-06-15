package com.wd.daquan.contacts.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.da.library.listener.DialogListener;
import com.da.library.listener.ITextChangedListener;
import com.da.library.view.CommDialog;
import com.da.library.widget.CommSearchEditText;
import com.da.library.widget.CommTitle;
import com.da.library.widget.SideBar;
import com.dq.im.type.ImType;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.contacts.adapter.MobileContactsListAdapter;
import com.wd.daquan.contacts.bean.MobileContactBean;
import com.wd.daquan.contacts.helper.MobileContactPinYinHelper;
import com.wd.daquan.contacts.helper.QueryMobileContactHelper;
import com.wd.daquan.contacts.listener.IMobileContactsListAdapterClickListener;
import com.wd.daquan.contacts.listener.IQueryMobileContactListener;
import com.wd.daquan.contacts.presenter.ContactPresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.model.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/17 13:15
 * @Description: 手机通讯录匹配
 */
public class MobileContactsListActivity extends DqBaseActivity<ContactPresenter, DataBean>
        implements View.OnClickListener, ITextChangedListener {

    private CommTitle mTitleLayout;
    private CommSearchEditText mSearchLayout;
    private RecyclerView mRecyclerView;
    private SideBar mSideBar;
    private TextView mLetterTv;
    private MobileContactsListAdapter mAdapter = null;
    private QueryMobileContactHelper mQueryHelper = null;
    private List<MobileContactBean> mContactList = new ArrayList<>();
    private boolean isShowRequestDialog = false;
    private CommDialog mAddFriendDialog = null;
    private MobileContactBean contactBean;
    private View mNoDataLl;
    private View mInviteTv;

    @Override
    protected ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_mobile_contacts_list);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initView() {
        mTitleLayout = findViewById(R.id.mobile_phone_list_title_layout);
        mSearchLayout = findViewById(R.id.mobile_phone_list_search_layout);
        mRecyclerView = findViewById(R.id.mobile_phone_list_recycler_view);
        mSideBar = findViewById(R.id.mobile_phone_list_side_bar);
        mLetterTv = findViewById(R.id.mobile_phone_list_letter_tv);
        mNoDataLl = findViewById(R.id.mobile_phone_list_no_data_ll);
        mInviteTv = findViewById(R.id.mobile_phone_list_invite_tv);
    }

    @Override
    protected void initData() {
        initTitle();
        initAdapter();

        mQueryHelper = new QueryMobileContactHelper(MobileContactsListActivity.this);
        mQueryHelper.setListener(mQueryMobileContactListener);

    }

    private void initTitle() {

        mTitleLayout.setTitleTextLength(8);
        mTitleLayout.setTitle(getString(R.string.mobile_contact_matching));
        mTitleLayout.getRightTv().setVisibility(View.GONE);
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MobileContactsListAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }

    private void getDataFormNet(String data) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.Mobile.DATA, data);
        mPresenter.getMobileContactList(DqUrl.url_match_phone_address_book, hashMap);
    }

    @Override
    protected void initListener() {
        mTitleLayout.getLeftIv().setOnClickListener(this);
        mInviteTv.setOnClickListener(this);
        mSideBar.setOnTouchingLetterChangedListener(mSideBarListener);
        mSearchLayout.setChangedListener(this);
        mAdapter.setListener(mItemClickListener);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mTitleLayout.getLeftIvId()) {
            finish();
        }else if(v.getId() == mInviteTv.getId()) {
            NavUtils.gotoInviteMobileContactActivity(this, "1", -1);
        }


    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (null == entity) return;
        if (IConstant.OK.equals(entity.status)) {
            if (DqUrl.url_match_phone_address_book.equals(url)) {
                String data = GsonUtils.toJson(entity.data);
                List<MobileContactBean> contactList = GsonUtils.fromJsonList(data, MobileContactBean.class);
                loadContactListData(contactList);
            } else if (DqUrl.url_friend_request.equals(url)) {
                //发送好友申请
                if (isShowRequestDialog) {
                    DqToast.showShort(getResources().getString(R.string.add_friend_re_txt));
                } else {
                    DqToast.showShort(getResources().getString(R.string.add_successful));
                }
                finish();
            }
        } else {
            DqToast.showShort(entity.content);
        }
    }

    /**
     * 加载群成员数据
     */
    private void loadContactListData(List<MobileContactBean> contactBeans) {
        //添加拼音
        List<MobileContactBean> list = MobileContactPinYinHelper.getInstance().setPinYin(contactBeans);
        if (null != list) {
            mContactList.clear();
            mContactList.addAll(list);
        }

        if (mContactList.size() > 0) {
            mNoDataLl.setVisibility(View.GONE);
            mSideBar.isBanTouch(false);
        } else {
            mNoDataLl.setVisibility(View.VISIBLE);
            mSideBar.isBanTouch(true);
        }

        //按拼音排序
        Collections.sort(mContactList, (o1, o2) -> {
            if (o1.pinYin.equals("@") || o2.pinYin.equals("#")) {
                return -1;
            } else if (o1.pinYin.equals("#") || o2.pinYin.equals("@")) {
                return 1;
            } else {
                return o1.pinYin.compareTo(o2.pinYin);
            }
        });

        mAdapter.update(mContactList);
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if (entity == null) return;
        if (DqUrl.url_friend_request.equals(url)) {
            if (KeyValue.Code.ADD_FRIEND_ERR == code) {
                isShowRequestDialog = true;
                showAddFriend();
            } else {
                DqToast.showShort(entity.content);
            }
        } else {
            DqUtils.bequit(entity, this);
        }
    }

    private void showAddFriend() {

        if (contactBean == null) return;
        EBSharedPrefUser userInfoSp = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo();
        mAddFriendDialog = new CommDialog(this);
        mAddFriendDialog.setSearchLlVisibility();
        mAddFriendDialog.setSearchEt("我是" + userInfoSp.getString(EBSharedPrefUser.nickname, ""));
        mAddFriendDialog.setOkTxt(getString(R.string.determine));
        mAddFriendDialog.setOkTxtColor(getResources().getColor(R.color.text_blue));
        mAddFriendDialog.show();

        mAddFriendDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("groupId", contactBean.uid);
                hashMap.put("to_say", mAddFriendDialog.getSearchEt().getText().toString());
                mPresenter.getFriendRequest(DqUrl.url_friend_request, hashMap);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mQueryHelper) {
            mQueryHelper.onDestroy();
            mQueryHelper = null;
        }
    }

    /**
     * 搜索监听
     */
    @Override
    public void textChanged(String content) {
        List<MobileContactBean> filterList = filter(content);
        mAdapter.update(filterList);

        if (TextUtils.isEmpty(content)) {
            mAdapter.update(mContactList);
        }

    }

    /**
     * 搜索
     */
    private List<MobileContactBean> filter(String str) {
        List<MobileContactBean> list = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return list;
        }
        for (MobileContactBean bean : mContactList) {
            if (bean.userName.contains(str) ||
                    bean.pinYin.contains(str) ||
                    bean.phone.contains(str)) {
                list.add(bean);
            }
        }
        return list;
    }

    @Override
    public void beforeChange() {

    }

    /**
     * adapter的点击事件
     */
    private IMobileContactsListAdapterClickListener mItemClickListener = new IMobileContactsListAdapterClickListener() {
        @Override
        public void onItemClick(int position, MobileContactBean item) {
            //item点击事件
            if (null == item) return;
            NavUtils.gotoUserInfoActivity(MobileContactsListActivity.this, item.uid, ImType.P2P.getValue());
        }

        @Override
        public void onAgreeClick(int position, MobileContactBean item) {
            //item点击事件
            if (null == item) return;

            contactBean = item;

            Map<String, String> hashMap = new HashMap<>();
            hashMap.put(IConstant.UserInfo.TO_UID, item.uid);
            hashMap.put("type", "0");
            mPresenter.getFriendRequest(DqUrl.url_friend_request, hashMap);
        }
    };
    /**
     * sidebar监听事件
     */
    private SideBar.OnTouchingLetterChangedListener mSideBarListener = letter -> {
        mSideBar.setTextView(mLetterTv);
        //该字母首次出现的位置
        int position = mAdapter.getPositionForSection(letter.charAt(0));

        if (position != -1) {
            mRecyclerView.scrollToPosition(position);
            LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            mLayoutManager.scrollToPositionWithOffset(position, 0);
        }
    };
    private IQueryMobileContactListener mQueryMobileContactListener = contactBeans ->{
        JSONArray jsonArray = new JSONArray();
        if(contactBeans.size() > 0) {
            try {
                for (MobileContactBean bean : contactBeans) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userName", bean.userName);
                    jsonObject.put("userPhoneNum", bean.phone);
                    jsonArray.put(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getDataFormNet(jsonArray.toString());
    };
}
