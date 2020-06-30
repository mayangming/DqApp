package com.wd.daquan.contacts.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.da.library.tools.DensityUtil;
import com.da.library.view.DqToolbar;
import com.da.library.widget.CommonListDialog;
import com.dq.im.type.ImType;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.bean.ShareBean;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.contacts.presenter.ContactPresenter;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.third.friend.SetFriendInfoHelper;
import com.wd.daquan.third.helper.UserInfoHelper;
import com.wd.daquan.third.session.SessionHelper;
import com.wd.daquan.third.session.extension.CardAttachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/12 20:01
 * @Description: 个人信息页面
 */
public class UserInfoActivity extends DqBaseActivity<ContactPresenter, DataBean>
        implements View.OnClickListener, QCObserver {

    /**
     * 用户不存在显示
     */
    private TextView mExistTv;
    /**
     * 头像名称
     */
    private ImageView mMineHeaderIv;
    private View vipHeadOutline;
    private TextView mMineNameTv;
    private ImageView mMineSexIv;
    private TextView mOriginalNicknameTv;
    private TextView mGroupNickName;
    private TextView mChuiniuNumTv;
    /**
     * item
     */
    private RelativeLayout mSetRemarkRl;
    private RelativeLayout mPersonalInfoRl;
    private LinearLayout mIntoGroupWayLl;
    private TextView mIntoGroupWayTv;

    private TextView mSendMessageTv;

    private TextView mUserdetailRemarksNameTv;
    private TextView mRemarkPhoneTv;
    private TextView mRemarkPhoneNumberTv;

    private View mUserdetailRemarksLin;
    private TextView mUserdetailRemarksTv;
    private ImageView mUserdetailRemarksImg;
    /**
     * 群组开启保护模式显示
     */
    private TextView mTipsTv;
    private Friend mFriend = null;
    /**
     * 会话类型 0单聊 1群聊
     */
    private ImType mSessionType = ImType.P2P;
    private boolean isMaster;
    //搜索进入
    private boolean isSearch;
    //退群成员进入
    private boolean isExitMember;
    private String mGroupId;
    //名片分享进入
    private boolean isCard;
    private String mUserId;
    //好友来源类型， 默认搜索
    private String resourceType = "1";
    private DqToolbar mToolbar;
    private SetFriendInfoHelper mInfoHelper;


    @Override
    protected ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_info);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mExistTv = findViewById(R.id.user_info_exist_tv);

        // 头像名称
        mMineHeaderIv = findViewById(R.id.mine_header_iv);
        vipHeadOutline = findViewById(R.id.vip_head_outline);
        mMineNameTv = findViewById(R.id.mine_name_tv);
        mMineSexIv = findViewById(R.id.mine_sex_iv);
        mGroupNickName = findViewById(R.id.group_nickname_tv);
        mOriginalNicknameTv = findViewById(R.id.original_nickname_tv);
        mChuiniuNumTv = findViewById(R.id.user_detail_chuiniunum_tv);

        mRemarkPhoneTv = findViewById(R.id.set_remark_phone_tv);
        mRemarkPhoneNumberTv = findViewById(R.id.set_remark_phone_bumber_tv);

        mUserdetailRemarksLin = findViewById(R.id.userdetail_remarks_lin);
        mUserdetailRemarksNameTv = findViewById(R.id.userdetail_remarks_name_tv);
        mUserdetailRemarksImg = findViewById(R.id.userdetail_remarks_img);
        mUserdetailRemarksTv = findViewById(R.id.userdetail_remarks_tv);

        // item
        mSetRemarkRl = findViewById(R.id.set_remark_rl);
        mPersonalInfoRl = findViewById(R.id.rl_personal_info);
//        mSendCardRl = findViewById(R.id.send_card_rl);
//        mBlacklistRl = findViewById(R.id.add_blacklist_rl);
//        mBlacklistTv = findViewById(R.id.tv_blacklist);
//        mDeleteFriendRl = findViewById(R.id.rl_delete_friend);
        mIntoGroupWayLl = findViewById(R.id.into_group_way_ll);
        mIntoGroupWayTv = findViewById(R.id.into_group_way_tv);

        mSendMessageTv = findViewById(R.id.send_message_tv);
        mTipsTv = findViewById(R.id.user_detail_tips_tv);
    }

    @Override
    protected void initData() {
        getIntentData();
        initTitle();
        getUserInfoFormNet();
        if(isCard = true) {
            resourceType = "2";
        }

        mInfoHelper = new SetFriendInfoHelper(this);
    }

    private void getUserInfoFormNet() {
        if (mSessionType == ImType.P2P) {
            // 私聊
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put(IConstant.UserInfo.OTHER_UID, mUserId);
            mPresenter.getUserInfo(DqUrl.url_get_userinfo, hashMap, "2");
        } else {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put(IConstant.UserInfo.OTHER_UID, mUserId);
            hashMap.put(IConstant.UserInfo.GROUP_ID, mGroupId);
            mPresenter.getGroupUserInfo(DqUrl.url_select_group_member, hashMap, false);
        }
    }

    private void getIntentData() {
//        mFriend = getIntent().getParcelableExtra(IConstant.UserInfo.FRIEND);

        mUserId = getIntent().getStringExtra(IConstant.UserInfo.UID);
        String imType = getIntent().getStringExtra(IConstant.UserInfo.SESSION_TYPE);
        mGroupId = getIntent().getStringExtra(IConstant.UserInfo.GROUP_ID);
        isSearch = getIntent().getBooleanExtra(IConstant.UserInfo.IS_SEARCH, false);
        isMaster = getIntent().getBooleanExtra(IConstant.UserInfo.IS_MASTER, false);
        isCard = getIntent().getBooleanExtra(IConstant.UserInfo.IS_CARD, false);
        isExitMember = getIntent().getBooleanExtra(IConstant.UserInfo.IS_EXIT_MEMBER, false);
        mSessionType = ImType.typeOfValue(imType);
    }

    private void initTitle() {
        mToolbar.setRightIv(R.mipmap.title_right_more);
    }

    @Override
    protected void initListener() {
        toolbarBack();
        toolbarRightIvOnClick(view -> showTitleMore());
        mMineHeaderIv.setOnClickListener(this);
        mSendMessageTv.setOnClickListener(this);
//        mBlacklistRl.setOnClickListener(this);
//        mDeleteFriendRl.setOnClickListener(this);
        mSetRemarkRl.setOnClickListener(this);
//        mSendCardRl.setOnClickListener(this);
        mPersonalInfoRl.setOnClickListener(this);
        mUserdetailRemarksLin.setOnClickListener(this);
        MsgMgr.getInstance().attach(this);
    }

    private CommonListDialog mListDialog = null;
    private void showTitleMore() {
        if(mFriend == null) {
            return;
        }
        if(mListDialog == null) {
            mListDialog = new CommonListDialog(this);
        }
        mListDialog.clear();
        List<String> itemList = new ArrayList<>();
//        itemList.add(DqApp.getStringById(R.string.friend_fa));
        if(mFriend.isWhether_black()) {
            itemList.add(DqApp.getStringById(R.string.remove_blacklist));
        }else {
            itemList.add(DqApp.getStringById(R.string.add_blacklist));
        }
        itemList.add(DqApp.getStringById(R.string.friend_delete));
        mListDialog.setItems(itemList);
        mListDialog.show();

        mListDialog.setListener((item, position) -> {
            switch (position) {

//                case  0://发送名片
//                    sendCard();
//                    break;
                case  0://添加/移除黑名单
                    if(mFriend.isWhether_black()) {
                        mInfoHelper.removeBlack(mUserId);
                    }else {
                        mInfoHelper.showAddBlackDialog(mUserId);
                    }
                    break;
                case  1://删除好友
                    String userName = mMineNameTv.getText().toString();
                    mInfoHelper.deleteFriend(mUserId, userName);
                    break;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_header_iv:
                //QcToastUtil.showToast(this, "头像");
                NavUtils.gotoLookBigPhotoActivity(this, mFriend.headpic);
                break;
            case R.id.set_remark_rl:
            case R.id.userdetail_remarks_lin:
                //QcToastUtil.showToast(this, "设置备注");
                if (!"1".equals(mUserId) && mFriend != null) {
                    String userName = mMineNameTv.getText().toString();
                    NavUtils.gotoSetRemarkNameActivity(this, mSessionType.getValue(), mUserId, userName,
                            mFriend.phones, mFriend.descriptions, mFriend.card, mGroupId);
                }
                break;
            case R.id.rl_personal_info:
                //QcToastUtil.showToast(this, "用户信息");
                NavUtils.gotoPersonalInfoActivity(this, mUserId, mGroupId);
                break;
            case R.id.send_message_tv:
                //发消息/添加好友
                sendMessageOrAddFriendClick();
                break;
        }
    }

    private void sendCard() {
        CardAttachment cardAttachment = new CardAttachment();
        cardAttachment.nickName = UserInfoHelper.getUserNickName(mUserId);
        cardAttachment.userId = mUserId;
        cardAttachment.headPic = UserInfoHelper.getHeadPic(mUserId);

        ShareBean shareBean = new ShareBean();
        shareBean.enterType = IConstant.Share.SHARE;
        shareBean.attachment = cardAttachment;
        NavUtils.gotoShareActivity(this, shareBean);

    }


    private void sendMessageOrAddFriendClick() {
        if (null != mFriend) {
            if ("0".equals(mFriend.whether_friend)) {
                SessionHelper.startP2PSession(getActivity(), mUserId);
                finish();
            } else {
                mInfoHelper.addFriend(mUserId);
            }
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (entity == null) return;
        if (DqUrl.url_get_userinfo.equals(url)) {
            //获取用户信息
            loadUserInfo(code, entity);
        } else if (DqUrl.url_select_group_member.equals(url)) {//获取群指定个人信息
            //加载群组数据
            loadGroupUserInfo(entity);
        }
    }

    private void loadGroupUserInfo(DataBean entity) {
        Friend friend = (Friend) entity.data;
        if (null != friend) {
            mFriend = friend;
            GlideUtils.loadHeader(getActivity(), friend.headpic, mMineHeaderIv);
            updateUserInfo(friend);
            updateUI(friend);
        }
    }


    private void loadUserInfo(int code, DataBean entity) {
        if (0 == code) {
            Friend friend = (Friend) entity.data;
            if (null != friend) {
                mFriend = friend;
                GlideUtils.loadHeader(getActivity(), friend.headpic, mMineHeaderIv);

                updateUserInfo(friend);
                updateUI(friend);
            } else {
                DqToast.showShort(entity.content);
            }
        } else if (101102 == code) {
            mExistTv.setVisibility(View.VISIBLE);
        } else {
            DqToast.showShort(entity.content);
        }
    }

    /**
     * 判断好友关系
     */
    @SuppressLint("SetTextI18n")
    private void updateUI(Friend friend) {
        showPhoneDesc(friend.descriptions, friend.phones, friend.card);
        showItem(friend);
        if (View.VISIBLE == mGroupNickName.getVisibility() // 群昵称
                && View.VISIBLE == mChuiniuNumTv.getVisibility() // 斗圈号
                && View.VISIBLE == mOriginalNicknameTv.getVisibility()) { // 昵称
            mChuiniuNumTv.setVisibility(View.GONE);
        }
        showPersonInfo();
    }

    private void showItem(Friend friend) {
        // 获取是否被保护,如果是来自名片点击，则不做任何保护
        boolean isProtect = !isCard && !TextUtils.isEmpty(friend.is_protect_groupuser) && !"0".equals(friend.is_protect_groupuser);

        if (ModuleMgr.getCenterMgr().getUID().equals(friend.uid)) {
            mSetRemarkRl.setVisibility(View.GONE);
            mChuiniuNumTv.setVisibility(View.VISIBLE);
            mSendMessageTv.setVisibility(View.GONE);
            mTipsTv.setVisibility(View.GONE);
            mToolbar.setRightIvVisible(false);
        } else {

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIntoGroupWayLl.getLayoutParams();

            boolean isFriend = friend.isFriend();
            if (isFriend) {
                mToolbar.setRightIvVisible(true);
                mSetRemarkRl.setVisibility(View.VISIBLE);
                mSendMessageTv.setVisibility(View.VISIBLE);
                mSendMessageTv.setText(getResources().getString(R.string.send_message));
                params.setMargins(0, 0, 0, 0);
                mIntoGroupWayLl.setLayoutParams(params);
            } else {
                mToolbar.setRightIvVisible(false);
                mSetRemarkRl.setVisibility(View.VISIBLE);
                boolean otherMaster = "2".equals(friend.getTarget_master()) || "1".equals(friend.getTarget_master());
                // (群主、管理员) || 未开启群保护则显示添加好友
                boolean ownMaster = "1".equals(friend.getOwn_master()) || "2".equals(friend.getOwn_master());
                if (isMaster || otherMaster || ownMaster || !isProtect) {
                    mSendMessageTv.setVisibility(View.VISIBLE);
                    mSendMessageTv.setText(getResources().getString(R.string.add_friend));
                    mTipsTv.setVisibility(View.GONE);
                } else {
                    mSendMessageTv.setVisibility(View.GONE);
                    mTipsTv.setVisibility(View.VISIBLE);
                }
                params.setMargins(0, DensityUtil.px2dip(this, getResources().getDimension(R.dimen.dd_dp35)),
                        0, 0);
                mIntoGroupWayLl.setLayoutParams(params);
            }

            // 搜索进来 或者 有斗圈号&&好友 则显示
            boolean hasChuiniuNunm = TextUtils.isEmpty(friend.dq_num);
            if (isSearch || !hasChuiniuNunm && !isFriend && !isProtect) {
                mChuiniuNumTv.setVisibility(View.VISIBLE);
            } else {
                mChuiniuNumTv.setVisibility(View.GONE);
            }

            // 来源方式
            if (!TextUtils.isEmpty(friend.source)) {
                mIntoGroupWayTv.setText(friend.source);
                mIntoGroupWayLl.setVisibility(View.VISIBLE);
            } else {
                mIntoGroupWayLl.setVisibility(View.GONE);
            }
        }
    }

    private void showPersonInfo() {
        if (mSessionType == ImType.P2P) {//单聊
            mPersonalInfoRl.setVisibility(View.GONE);
        } else {
            if (isExitMember) {
                mPersonalInfoRl.setVisibility(View.GONE);
            } else {
                //mPersonalInfoRl.setVisibility(View.VISIBLE);
                //暂时关闭
                mPersonalInfoRl.setVisibility(View.GONE);
            }

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPersonalInfoRl.getLayoutParams();
            if (View.VISIBLE == mSetRemarkRl.getVisibility()) {
                params.setMargins(0, 0, 0, 0);
            } else {
                params.setMargins(0, DensityUtil.px2dip(this, getResources().getDimension(R.dimen.dd_dp35)),
                        0, 0);
            }
            mPersonalInfoRl.setLayoutParams(params);
        }
    }

    //用户信息
    @SuppressLint("SetTextI18n")
    private void updateUserInfo(Friend friend) {

        // 设置性别头像
        if ("1".equals(friend.sex)) {
            mMineSexIv.setImageResource(R.mipmap.icon_male);
        } else if ("2".equals(friend.sex)) {
            mMineSexIv.setImageResource(R.mipmap.icon_female);
        }

        // 设置斗圈号
        if (!TextUtils.isEmpty(friend.dq_num)) {
            String chuiniuNum = String.format(this.getResources().getString(R.string.mine_chuiniu_num), friend.dq_num);
            mChuiniuNumTv.setText(chuiniuNum);
        }

        // 显示顺序(有则显示)
        // 群组：1.备注，2.群昵称，3.斗圈号(非加好友)
        // 好友：1.备注，2.昵称，3.斗圈号
        if (mSessionType == ImType.P2P) {//单聊
            // 如果备注为空，使用昵称填充
            updateNickName(friend.nickname, friend.friend_remarks);
        } else {//群组
            if (TextUtils.isEmpty(friend.remarks)) {
                mGroupNickName.setVisibility(View.GONE);
            } else {
                mGroupNickName.setVisibility(View.VISIBLE);
                mGroupNickName.setText("群昵称：" + friend.remarks);
            }
            updateNickName(friend.nickname, friend.friend_remarks);
        }

        if (friend.isVip()){
            vipHeadOutline.setVisibility(View.VISIBLE);
        }else {
            vipHeadOutline.setVisibility(View.GONE);
        }

    }

    @SuppressLint("SetTextI18n")
    private void showPhoneDesc(String descriptions, List<String> phones, String card) {
        mSetRemarkRl.setVisibility(View.GONE);
        mUserdetailRemarksLin.setVisibility(View.GONE);
        String phoneNumber = "";
        if (phones != null && phones.size() > 0) {
            phoneNumber = phones.get(0);
        }

        //全部为空
        if (TextUtils.isEmpty(descriptions) && TextUtils.isEmpty(card) && TextUtils.isEmpty(phoneNumber)) {
            mSetRemarkRl.setVisibility(View.VISIBLE);
            mUserdetailRemarksLin.setVisibility(View.GONE);
            return;
        }

        //备注，图片，电话全有
        if (!TextUtils.isEmpty(descriptions) && (!TextUtils.isEmpty(card) || !TextUtils.isEmpty(phoneNumber))) {
            mSetRemarkRl.setVisibility(View.VISIBLE);
            mUserdetailRemarksLin.setVisibility(View.VISIBLE);

            mRemarkPhoneTv.setText(getString(R.string.telephone_number));
            mRemarkPhoneNumberTv.setText(phoneNumber);

            mUserdetailRemarksNameTv.setText(getString(R.string.describe) + "\t\t\t\t\t\t");
            mUserdetailRemarksImg.setVisibility(TextUtils.isEmpty(card) ? View.GONE : View.VISIBLE);
            mUserdetailRemarksTv.setText(descriptions);
            return;
        }

        //只有电话有内容
        if (!TextUtils.isEmpty(phoneNumber) && (TextUtils.isEmpty(card) || TextUtils.isEmpty(descriptions))) {
            mSetRemarkRl.setVisibility(View.VISIBLE);
            mUserdetailRemarksLin.setVisibility(View.GONE);

            mRemarkPhoneTv.setText(getString(R.string.telephone_number));
            mRemarkPhoneNumberTv.setText(phoneNumber);
            return;
        }

        //只有备注或图片有内容
        if (TextUtils.isEmpty(phoneNumber) && (!TextUtils.isEmpty(card) || !TextUtils.isEmpty(descriptions))) {
            mSetRemarkRl.setVisibility(View.GONE);
            mUserdetailRemarksLin.setVisibility(View.VISIBLE);

            mUserdetailRemarksNameTv.setText(getString(R.string.describe) + "\t\t\t\t\t\t");
            mUserdetailRemarksImg.setVisibility(TextUtils.isEmpty(card) ? View.GONE : View.VISIBLE);
            mUserdetailRemarksTv.setText(descriptions);
        }
    }

    /**
     * 更新是否显示昵称
     *
     * @param nickName 昵称
     * @param target   目标名称
     */
    @SuppressLint("SetTextI18n")
    private void updateNickName(String nickName, String target) {
        if (TextUtils.isEmpty(target)) {
            if (!TextUtils.isEmpty(nickName)) {
                mMineNameTv.setText(nickName);
            }
            mOriginalNicknameTv.setVisibility(View.GONE);
            mOriginalNicknameTv.setText("昵称：" + nickName);
        } else {
            mMineNameTv.setText(target);
            //mFriend.remarks = target;
            mOriginalNicknameTv.setText("昵称：" + nickName);
            mOriginalNicknameTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if(DqUrl.url_get_userinfo.contains(url)) {
            mFriend = FriendDbHelper.getInstance().getFriend(mUserId);

            GlideUtils.loadHeader(getActivity(), mFriend.headpic, mMineHeaderIv);
            updateUserInfo(mFriend);
            updateUI(mFriend);
        }else {
            DqUtils.bequit(entity, this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            String remarkName = data.getStringExtra(IConstant.UserInfo.REMARKS);
            ArrayList<String> phones = data.getStringArrayListExtra(KeyValue.Remark.PHONES);
            String desc = data.getStringExtra(KeyValue.Remark.DESCRIPTIONS);
            String webHttpUrl = data.getStringExtra(KeyValue.Remark.CARD);

            if (null != mFriend) {
                mFriend.friend_remarks = remarkName;
                mFriend.descriptions = desc;
                mFriend.phones = phones;
                mFriend.card = webHttpUrl;
                updateUserInfo(mFriend);
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
        if(MsgType.MT_FRIEND_ADD_BLACK_LIST.equals(key)) {
            mFriend.whether_black = "0";
        }else if(MsgType.MT_FRIEND_REMOVE_BLACK_LIST.equals(key)) {
            mFriend.whether_black = "1";
        }else if(MsgType.MT_FRIEND_REMOVE_FRIEND.equals(key)) {
            finish();
        }else if(MsgType.MT_FRIEND_ADD_FRIEND.equals(key)) {
            SessionHelper.startP2PSession(getActivity(), mUserId);
        }
    }
}
