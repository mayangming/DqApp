package com.wd.daquan.contacts.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.da.library.widget.CommTitle;
import com.dq.im.type.ImType;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.contacts.presenter.ContactPresenter;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.NewFriendBean;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.third.friend.SetFriendInfoHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/26 14:04
 * @Description: 新朋友详情
 */
public class NewFriendDetailActivity extends DqBaseActivity<ContactPresenter, DataBean> implements View.OnClickListener, QCObserver {

    private CommTitle mTitleLayout;
    private ImageView mHeaderIv;
    private View vipHeadIcon;
    private ImageView mSexIv;
    private TextView mNameTv;
    private TextView mDescTv;
    private TextView mAgreeTv;
    private TextView mAddBlackListTv;
    private NewFriendBean mNewFriend;
    private SetFriendInfoHelper mInfoHelper;

    @Override
    protected ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_new_friend_detail);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mTitleLayout = findViewById(R.id.new_friend_detail_title_layout);
        mHeaderIv = findViewById(R.id.new_friend_detail_header_iv);
        vipHeadIcon = findViewById(R.id.vip_head_outline);
        mNameTv = findViewById(R.id.new_friend_detail_name_tv);
        mSexIv = findViewById(R.id.new_friend_detail_sex_iv);
        mDescTv = findViewById(R.id.new_friend_detail_desc_tv);
        mAgreeTv = findViewById(R.id.new_friend_detail_agree_tv);
        mAddBlackListTv = findViewById(R.id.new_friend_detail_black_list_tv);

    }

    @Override
    protected void initData() {
        mNewFriend = getIntent().getParcelableExtra(IConstant.UserInfo.NEW_FRIEND_BEAN);

        mTitleLayout.setTitle(getString(R.string.detail_info));
        mTitleLayout.getRightTv().setVisibility(View.GONE);
        if(null != mNewFriend) {
            updateUI(mNewFriend);
        }

        mInfoHelper = new SetFriendInfoHelper(this);
    }

    @Override
    protected void initListener() {
        mTitleLayout.getLeftIv().setOnClickListener(this);
        mAgreeTv.setOnClickListener(this);
        mAddBlackListTv.setOnClickListener(this);
        MsgMgr.getInstance().attach(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mTitleLayout.getLeftIvId()) {
            finish();
        }

        if(mNewFriend == null ) return;
        switch (v.getId()) {
            case  R.id.new_friend_detail_agree_tv:
                setNewFriendStatus();
                break;
            case  R.id.new_friend_detail_black_list_tv:
                addBlackList();
                break;
        }
    }

    /**
     * 加入/删除黑名单
     */
    private void addBlackList() {
        if(mNewFriend == null) {
            return;
        }
        if ("0".equals(mNewFriend.whether_black)) {
            //移除
            mInfoHelper.removeBlack(mNewFriend.uid);
        } else {
            //添加
            mInfoHelper.showAddBlackDialog(mNewFriend.uid);
        }
    }

    /**
     * 添加好友
     */
    private void setNewFriendStatus() {
        if(null != mNewFriend) {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put(IConstant.UserInfo.REQUEST_ID, mNewFriend.request_id);
            hashMap.put(IConstant.UserInfo.STATUS, "1");
            mPresenter.getFriendResponse(DqUrl.url_friend_response, hashMap);
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if(null == entity) return;
        if(entity.isSuccess()) {
            if (DqUrl.url_friend_response.equals(url)) {
                //同意好友请求
                DqToast.showShort(getString(R.string.add_successful));
                if(null != mNewFriend) {
                    NavUtils.gotoUserInfoActivity(this, mNewFriend.uid, ImType.P2P.getValue());
                    finish();
                }
            }
        }
    }

    private void updateUI(NewFriendBean friend){

        GlideUtils.loadHeader(this, friend.headpic, mHeaderIv);
        mNameTv.setText(friend.nickname);

        if("1".equals(friend.sex)){
            mSexIv.setImageResource(R.mipmap.icon_male);
        }else{
            mSexIv.setImageResource(R.mipmap.icon_female);
        }

        if(TextUtils.isEmpty(friend.to_say)) {
            mDescTv.setText(getString(R.string.not_set_remark));
        }else {
            mDescTv.setText(friend.to_say);
        }

//        if ("0".equals(friend.whether_black)) {
//            mAddBlackListTv.setText(getString(R.string.remove_blacklist));
//        } else {
//            mAddBlackListTv.setText(getString(R.string.add_blacklist));
//        }
        if (friend.isVip()){
            DqLog.e("YM---->是vip:");
            vipHeadIcon.setVisibility(View.VISIBLE);
        }else {
            DqLog.e("YM---->不是vip:");
            vipHeadIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if(null == entity) return;
        DqUtils.bequit(entity, this);
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
        if(MsgType.MT_FRIEND_ADD_BLACK_LIST.equals(key)) {
            //加入黑名单
            mAddBlackListTv.setText(getString(R.string.remove_blacklist));
            if(null != mNewFriend) {
                mNewFriend.whether_black = "0";
                Friend friend = new Friend();
                friend.uid = mNewFriend.uid;
                friend.headpic = mNewFriend.headpic;
                friend.nickname = mNewFriend.nickname;
                friend.phone = mNewFriend.phone;
                friend.whether_friend = "1";
                friend.whether_black = "0";
                FriendDbHelper.getInstance().update(friend, null);
                MsgMgr.getInstance().sendMsg(MsgType.MT_FRIEND_ADD_BLACK_LIST, "");
            }
        }else if(MsgType.MT_FRIEND_REMOVE_BLACK_LIST.equals(key)) {
            //移除黑名单
            mAddBlackListTv.setText(getString(R.string.add_blacklist));
            if(null != mNewFriend) {
                mNewFriend.whether_black = "1";
                Friend friend = new Friend();
                friend.uid = mNewFriend.uid;
                friend.headpic = mNewFriend.headpic;
                friend.nickname = mNewFriend.nickname;
                friend.phone = mNewFriend.phone;
                friend.whether_friend = "1";
                friend.whether_black = "0";
                FriendDbHelper.getInstance().update(friend, null);
                MsgMgr.getInstance().sendMsg(MsgType.MT_FRIEND_ADD_BLACK_LIST, "");
            }
        }
    }
}
