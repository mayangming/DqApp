package com.wd.daquan.chat.redpacket.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.db.helper.MemberDbHelper;
import com.wd.daquan.chat.redpacket.RedPacketHelper;
import com.wd.daquan.chat.redpacket.SingleRedpacketCallback;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.common.utils.FrameAnimation;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.QCBroadcastManager;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.mine.bean.AlipayRedListEntity;
import com.wd.daquan.third.session.extension.QcAlipayRpAttachment;
import com.da.library.tools.Utils;

/**
 * @author: dukangkang
 * @date: 2018/9/17 19:22.
 * @description: todo ...
 */
public class RedpacketRobActivity extends DqBaseActivity implements View.OnClickListener {
    private int[] mImgResIds = new int[]{
            R.mipmap.kai_1,
            R.mipmap.kai_2,
            R.mipmap.kai_3,
            R.mipmap.kai_4,
            R.mipmap.kai_5,
            R.mipmap.kai_6,
            R.mipmap.kai_7,
    };

    private boolean isOpen = false;
    private String mType = "";
    private String mPay = "";
    private String mTargetId = "";
//    private IMMessage mMessage = null;
    private QcAlipayRpAttachment mAlipayRpAttachment = null;

    private FrameAnimation mFrameAnimation;
//    private CommTitle mCommTitle = null;
    private ImageView mClose = null;
    private ImageView mHeadIcon = null;
    private ImageView mOpenIv = null;
    private TextView mName = null;
    private TextView mRandomTips = null;
    private TextView mBlessingTips = null;
    private TextView mLookUp = null;

    private RedPacketHelper mRedPacketHelper = null;

    @Override
    public Presenter.IPresenter createPresenter() {
        return null;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.redpacket_rob_activity);
    }

    @Override
    protected void init() {
        initIntent();
    }

    @Override
    protected void initView() {
//        mCommTitle = this.findViewById(R.id.redpacket_rob_commtitle);
        mClose = this.findViewById(R.id.redpacket_rob_close);
        mHeadIcon = this.findViewById(R.id.redpacket_rob_headicon);
        mName = this.findViewById(R.id.redpacket_rob_name);
        mRandomTips = this.findViewById(R.id.redpacket_rob_random_tips);
        mBlessingTips = this.findViewById(R.id.redpacket_rob_blessing);
        mOpenIv = this.findViewById(R.id.redpacket_rob_open);
        mLookUp = this.findViewById(R.id.redpacket_rob_lookup);
    }

    private void initIntent() {
        isOpen = getIntent().getBooleanExtra(KeyValue.RedpacktRob.IS_OPEN, false);
        mType = getIntent().getStringExtra(KeyValue.RedpacktRob.TYPE);
        mPay = getIntent().getStringExtra(KeyValue.RedpacktRob.PAY);//暂时没有用到

        mTargetId = getIntent().getStringExtra(KeyValue.RedpacktRob.TARGET_ID);

        mAlipayRpAttachment = (QcAlipayRpAttachment) getIntent().getSerializableExtra(KeyValue.RedpacktRob.ATTACHMENT);
//        mMessage = (IMMessage) getIntent().getSerializableExtra(KeyValue.RedpacktRob.MESSAGE);
//        if (mMessage != null) {
//            mAlipayRpAttachment = (QcAlipayRpAttachment) mMessage.getAttachment();
//        }
    }

    @Override
    public void initListener() {
        mClose.setOnClickListener(this);
        mOpenIv.setOnClickListener(this);
        mLookUp.setOnClickListener(this);
        mRedPacketHelper.setRedPacketCallback(new OpenCallback());
    }

    @Override
    public void initData() {
        if(mAlipayRpAttachment == null)return;
        mRedPacketHelper = new RedPacketHelper(this);
        GlideUtils.loadHeader(DqApp.sContext, mAlipayRpAttachment.sendPic, mHeadIcon);
        if ("private".equals(mType)) {
            mRandomTips.setText(DqApp.getStringById(R.string.redpacket_rob_private));
            mLookUp.setVisibility(View.INVISIBLE);
        } else if ("group".equals(mType)) {
            mRandomTips.setText(DqApp.getStringById(R.string.redpacket_random_tips));
            mLookUp.setVisibility(View.VISIBLE);
        }
        // TODO: 2018/9/17 从库中获取
        Friend friend = FriendDbHelper.getInstance().getFriend(mAlipayRpAttachment.sendId);
        if(friend != null){
            String remarks = friend.getRemarks();
            if(!TextUtils.isEmpty(remarks)){//好友备注
                mName.setText(remarks);
            }else{
                if("group".equals(mType)){//from team
                    GroupMemberBean groupMemberInfo =  MemberDbHelper.getInstance().getTeamMember(mTargetId, mAlipayRpAttachment.sendId);
                    if(groupMemberInfo != null){
                        if(!TextUtils.isEmpty(groupMemberInfo.getRemarks())){
                            mName.setText(groupMemberInfo.getRemarks());
                        } else{
                            setName();
                        }
                    }else{
                        setName();
                    }
                }else{//from p2p
                    setName();
                }
            }
        }

        mBlessingTips.setText(mAlipayRpAttachment.blessing);
        mOpenIv.setBackgroundResource(R.mipmap.kai_1);

        if (isOpen) {
            mLookUp.setVisibility(View.INVISIBLE);
        } else {
            robEmpty();
        }
    }

    private void setName() {
//        if ("group".equals(mType)) {
////            GroupUserInfo userInfos = RongUserInfoManager.getInstance().getGroupUserInfo(mTargetId, mRedMessage.getSendId());
////            if (userInfos != null) {
////                name.setText(userInfos.getNickname());
////            } else {
////                name.setText(mRedMessage.getSendName());
////            }
//        } else {
//            if (null != mAlipayRpAttachment) {
//                mName.setText(mAlipayRpAttachment.sendName);
//            }
//        }
        if (null != mAlipayRpAttachment) {
            mName.setText(mAlipayRpAttachment.sendName);
        }
    }

    private void robEmpty() {
        mLookUp.setVisibility(View.VISIBLE);
        mOpenIv.setVisibility(View.INVISIBLE);
        mRandomTips.setVisibility(View.GONE);
        mBlessingTips.setText(getResources().getString(R.string.redpacket_rob_empty));
        QCBroadcastManager broadcastManager = new QCBroadcastManager();
        broadcastManager.sendBroadcast(KeyValue.REDTYPE);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mClose.getId()) {
            stopAnim();
            finish();
        } else if (id == mOpenIv.getId()) {
            if (mFrameAnimation != null) {
                //如果正在转动，则直接返回
                return;
            }
            startAnim();
            if (mAlipayRpAttachment != null && mRedPacketHelper != null) {
                mRedPacketHelper.openRedpacket(mAlipayRpAttachment.redpacketId, mAlipayRpAttachment.signature);
            }
        } else if (id == mLookUp.getId()) {
            if (Utils.isFastDoubleClick(500)) {
                return;
            }
            AlipayRedListEntity.ListBean listBean = new AlipayRedListEntity.ListBean();
            if ("private".equals(mType)) {
                listBean.sessionType = KeyValue.ZERO_STRING;//p2p
            } else if ("group".equals(mType)) {
                listBean.sessionType = KeyValue.ONE_STRING;//team
            }
            listBean.signature = mAlipayRpAttachment.signature;
            listBean.redpacket_id = mAlipayRpAttachment.redpacketId;

            NavUtils.gotoAlipayRedDetailsActivity(this, listBean);

            finish();
        }
    }

    public void startAnim() {
        mFrameAnimation = new FrameAnimation(mOpenIv, mImgResIds, 80, true);
    }

    public void stopAnim() {
        if (mFrameAnimation != null) {
            mFrameAnimation.release();
            mFrameAnimation = null;
        }
    }

    private class OpenCallback extends SingleRedpacketCallback {
        @Override
        public void openRedPacket(int code, String message) {
            super.openRedPacket(code, message);
            DqApp.getInstance().runInUIThread(new Runnable() {
                @Override
                public void run() {
                    callback();
                    openRedpacket(code, message);
                }
            });
        }

        @Override
        public void redpacketEmpty() {
            super.redpacketEmpty();
            DqApp.getInstance().runInUIThread(new Runnable() {
                @Override
                public void run() {
                    callback();
                    robEmpty();

                }
            });
        }

        @Override
        public void onFailed(int code) {
            super.onFailed(code);
            stopAnim();
        }
    }

    /**
     * 开红包
     * @param code
     * @param message
     */
    private void openRedpacket(int code, String message) {
        stopAnim();
        if (code == 0) {
            if ("private".equals(mType)) {
                NavUtils.gotoRedpacketP2pDetailActivity(this, mAlipayRpAttachment);
            } else {
                AlipayRedListEntity.ListBean listBean = new AlipayRedListEntity.ListBean();
                listBean.signature = mAlipayRpAttachment.signature;
                listBean.redpacket_id = mAlipayRpAttachment.redpacketId;
                listBean.sessionType = KeyValue.ONE_STRING;//team
                NavUtils.gotoAlipayRedDetailsActivity(this, listBean);
            }

            Intent intent = new Intent();
            intent.putExtra(KeyValue.RedPacketMessage.REDPACKET_ID, mAlipayRpAttachment.redpacketId);
            setResult(RESULT_OK, intent);
            finish();
        } else if (code == 103002) {
            robEmpty();
        } else {
            DqToast.showShort(message);
        }
    }

    private void callback() {
        Intent intent = new Intent();
        intent.putExtra(KeyValue.RedpacktRob.UNRECEIVE, mAlipayRpAttachment.redpacketId);
        setResult(RESULT_OK, intent);
    }
}
