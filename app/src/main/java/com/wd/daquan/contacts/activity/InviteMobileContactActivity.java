package com.wd.daquan.contacts.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.da.library.constant.IConstant;
import com.wd.daquan.contacts.adapter.SelectMobileContactAdapter;
import com.wd.daquan.contacts.bean.MobileContactBean;
import com.wd.daquan.contacts.helper.MobileContactPinYinHelper;
import com.wd.daquan.contacts.helper.QueryMobileContactHelper;
import com.wd.daquan.contacts.listener.IQueryMobileContactListener;
import com.wd.daquan.contacts.presenter.ContactPresenter;
import com.da.library.listener.IOnItemClickSelectListener;
import com.da.library.listener.ISelectListener;
import com.da.library.listener.ITextChangedListener;
import com.da.library.widget.CommSearchEditText;
import com.da.library.widget.CommTitle;
import com.da.library.widget.SideBar;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.retrofit.RetrofitHelp;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/18 13:31
 * @Description: 邀请手机联系人
 */
public class InviteMobileContactActivity extends DqBaseActivity<ContactPresenter, DataBean> implements
        View.OnClickListener, ISelectListener<MobileContactBean>, ITextChangedListener, IOnItemClickSelectListener<MobileContactBean> {

    private CommTitle mTitleLayout;
    private CommSearchEditText mSearchLayout;
    private RecyclerView mRecyclerView;
    private SideBar mSideBar;
    private TextView mLetterTv;
    private View mNoDataTv;
    private String mEnterType;
    private QueryMobileContactHelper mQueryHelper = null;
    private SelectMobileContactAdapter mAdapter = null;
    private List<MobileContactBean> mContactList = new ArrayList<>();
    private List<MobileContactBean> mSelectList = new ArrayList<>();


    @Override
    protected ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_invite_mobile_contact);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initView() {
        mTitleLayout = findViewById(R.id.invite_mobile_contact_title_layout);
        mSearchLayout = findViewById(R.id.invite_mobile_contact_search_layout);
        mRecyclerView = findViewById(R.id.invite_mobile_contact_recycler_view);
        mSideBar = findViewById(R.id.invite_mobile_contact_side_bar);
        mLetterTv = findViewById(R.id.invite_mobile_contact_letter_tv);
        mNoDataTv = findViewById(R.id.no_data_tv);
    }

    @Override
    protected void initData() {
        getIntentData();
        initTitle();
        initAdapter();
        mQueryHelper = new QueryMobileContactHelper(this);
    }

    private void getIntentData() {
        mEnterType = getIntent().getStringExtra(IConstant.ENTER_TYPE);
    }

    private void initTitle() {

        mTitleLayout.setTitleTextLength(8);
        if ("1".equals(mEnterType)) {
            mTitleLayout.setTitle(getString(R.string.inviter_contact));
            mTitleLayout.getRightTv().setText(getString(R.string.send));
            mTitleLayout.getRightTv().setClickable(false);
            mTitleLayout.getRightTv().setVisibility(View.VISIBLE);
            mTitleLayout.getRightTv().setTextColor(getResources().getColor(R.color.color_8c8c8c));
        } else if ("2".equals(mEnterType)) {
            mTitleLayout.setTitle(getString(R.string.mobile_contact_matching));
            mTitleLayout.getRightTv().setVisibility(View.GONE);
        }
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SelectMobileContactAdapter();
        mRecyclerView.setAdapter(mAdapter);

        if ("1".equals(mEnterType)) {
            mAdapter.setMode(IConstant.Select.MORE);

        } else if ("2".equals(mEnterType)) {
            mAdapter.setMode(IConstant.Select.SINGLE);
        }
    }

    @Override
    protected void initListener() {
        mTitleLayout.getLeftIv().setOnClickListener(this);
        mSideBar.setOnTouchingLetterChangedListener(mSideBarListener);
        mSearchLayout.setChangedListener(this);
        mQueryHelper.setListener(mQueryMobileContactListener);
        mAdapter.setSelectListener(this);
        if ("1".equals(mEnterType)) {
            mTitleLayout.getRightTv().setOnClickListener(this);
        }

        mAdapter.setItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mTitleLayout.getLeftIvId()) {
            finish();
        } else if (v.getId() == mTitleLayout.getRightTvId()) {
            //发送邀请消息
            sendInviteMessage();
//            QCToast.showShort(DqApp.getStringById(R.string.no_this_function));
        }
    }

    private void sendInviteMessage() {
        if (mSelectList.size() > 100){
            DqToast.showLong("一次最多只能邀请100人!");
            return;
        }
        if (mSelectList.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < mSelectList.size(); i++){
                MobileContactBean bean = mSelectList.get(i);
                stringBuilder.append(bean.phone);
                if (i != mSelectList.size() - 1){
                    stringBuilder.append(",");
                }
            }
            String mobile = stringBuilder.toString();
            Log.e("YM","邀请联系人的手机号:"+mobile);
            Map<String,String> params = new HashMap<>();
            params.put(KeyValue.INVITE_MOBILE,mobile);
            mPresenter.senInvitationSms(DqUrl.url_invitation_sms,params);

//            if (Build.MODEL.contains("vivo")) {
//
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setType("vnd.android-dir/mms-sms");
//                intent.putExtra("address", mobile);
//                //短信内容
//                intent.putExtra("sms_body", getResources().getString(R.string.sms));
//                startActivity(intent);
//            } else {
//                Uri smsToUri = Uri.parse("smsto:" + mobile);
//                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
//                //短信内容
//                intent.putExtra("sms_body", getResources().getString(R.string.sms));
//                startActivity(intent);
//            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSelectList(@NonNull List<MobileContactBean> list) {
        if (list.size() > 0) {
            mTitleLayout.getRightTv().setClickable(true);
            mTitleLayout.getRightTv().setTextColor(getResources().getColor(R.color.text_blue));
            mTitleLayout.getRightTv().setText(getString(R.string.send) + "(" + list.size() + ")");
        } else {
            mTitleLayout.getRightTv().setClickable(false);
            mTitleLayout.getRightTv().setTextColor(getResources().getColor(R.color.color_8c8c8c));
            mTitleLayout.getRightTv().setText(getString(R.string.send));
        }

        mSelectList.clear();
        mSelectList.addAll(list);
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

    @Override
    public void beforeChange() {

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
            mNoDataTv.setVisibility(View.GONE);
            mSideBar.isBanTouch(false);
        } else {
            mNoDataTv.setVisibility(View.VISIBLE);
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

    /**
     * 查询手机联系人数据监听
     */
    private IQueryMobileContactListener mQueryMobileContactListener = contactBeanList -> {
        if (null != contactBeanList) {
            mContactList.clear();
            mContactList.addAll(contactBeanList);

            loadContactListData(mContactList);
        }
    };

    /**
     * 单选item点击事件
     */
    @Override
    public void onItemClick(@NonNull MobileContactBean mobileContactBean) {
        if("2".equals(mEnterType)) {
            setResult(RESULT_OK, new Intent().putExtra(IConstant.Login.PHONE, mobileContactBean.phone));
            finish();
        }
    }
}
