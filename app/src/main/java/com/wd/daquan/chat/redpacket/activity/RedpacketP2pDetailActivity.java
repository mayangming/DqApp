package com.wd.daquan.chat.redpacket.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.bean.RpDetailEntity;
import com.wd.daquan.chat.redpacket.RedPacketPresenter;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.ResponseCode;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.common.utils.QCBroadcastManager;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.third.session.extension.QcAlipayRpAttachment;
import com.da.library.tools.Utils;
import com.da.library.widget.CommTitle;

import java.util.LinkedHashMap;

/**
 * @author: dukangkang
 * @date: 2018/9/18 10:09.
 * @description:
 *  支付宝单人详情页
 */
public class RedpacketP2pDetailActivity extends DqBaseActivity<RedPacketPresenter, DataBean> implements View.OnClickListener {

    private String mRedpacketId = "0";

    private String mSignture = "";

    private CommTitle mCommTitle = null;
    private ImageView mIcon = null;
    private TextView mName = null;
    private TextView mMonkey = null;
    private TextView mBlessing = null;
//    private TextView mContent = null;
    private TextView mPayPurpose = null;
    private TextView mPayHistory = null;

//    private IMMessage mMessage = null;

    @Override
    protected RedPacketPresenter createPresenter() {
        return new RedPacketPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.redpacket_p2p_detail_activity);
    }

    @Override
    protected void init() {
        mRedpacketId = getIntent().getStringExtra(KeyValue.RedPacket.REDPACKET_ID);
        mSignture = getIntent().getStringExtra(KeyValue.RedPacket.SIGNATURE);
    }

    @Override
    protected void initView() {
        mCommTitle = this.findViewById(R.id.redpacket_p2p_detail_commtitle);
        mIcon = this.findViewById(R.id.redpacket_p2p_detail_icon);
        mName = this.findViewById(R.id.redpacket_p2p_detail_name);
        mMonkey = this.findViewById(R.id.redpacket_p2p_detail_money);
        mBlessing = this.findViewById(R.id.redpacket_p2p_detail_blessing);
//        mContent = this.findViewById(R.id.redpacket_p2p_detail_content);
        mPayPurpose = this.findViewById(R.id.redpacket_p2p_detail_pay_purpose);
        mPayHistory = this.findViewById(R.id.redpacket_p2p_detail_payhistory);
    }

    @Override
    protected void initData() {
        mCommTitle.setTitle("支付宝红包详情");
        if ("0".equals(mRedpacketId) || TextUtils.isEmpty(mRedpacketId)) {
//            mMessage = (IMMessage) getIntent().getSerializableExtra(KeyValue.RedPacket.MESSAGE);
//            QcAlipayRpAttachment alipayRpAttachment = (QcAlipayRpAttachment) mMessage.getAttachment();
            QcAlipayRpAttachment alipayRpAttachment = (QcAlipayRpAttachment) getIntent().getSerializableExtra(KeyValue.RedpacktRob.ATTACHMENT);
            if (null != alipayRpAttachment) {
                mName.setText(alipayRpAttachment.sendName);
                mBlessing.setText(alipayRpAttachment.blessing);
                mPayHistory.setVisibility(View.VISIBLE);
                GlideUtils.loadHeader(DqApp.sContext, alipayRpAttachment.sendPic, mIcon);
                request(alipayRpAttachment.redpacketId, alipayRpAttachment.signature);
            }
        } else {
            EBSharedPrefUser userInfoSp = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo();
            String headpic = userInfoSp.getString(EBSharedPrefUser.headpic, "");
            String nickname = userInfoSp.getString(EBSharedPrefUser.nickname, "");
            mName.setText(nickname);
            mPayHistory.setVisibility(View.GONE);
            GlideUtils.loadHeader(DqApp.sContext, headpic, mIcon);
            request(""+mRedpacketId, mSignture);
        }
    }

    private void request(String redpacketId, String sigture) {
        if (mPresenter != null) {
            LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
            hashMap.put("redpacket_id", "" + redpacketId);
            hashMap.put("signature", "" + sigture);
            mPresenter.redpacketDetail(DqUrl.url_redPacketDetail, hashMap);
        }
    }

    @Override
    protected void initListener() {
        mPayHistory.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (Utils.isFastDoubleClick(500)) {
            return;
        }

        int id = v.getId();
        if (id == mPayHistory.getId()) {

        } else if (id == mCommTitle.getLeftIvId()) {
            finish();
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (entity == null) {
            return;
        }

        if (DqUrl.url_redPacketDetail.equals(url)) {
            RpDetailEntity detailEntity = (RpDetailEntity) entity.data;
            String remarks = ""; // 从friend中取
            if (!TextUtils.isEmpty(detailEntity.myaliuser)) {
                mPayPurpose.setVisibility(View.VISIBLE);
                String myaliuser = getString(R.string.rd_detail_pay_money) + detailEntity.myaliuser;
                mPayPurpose.setText(myaliuser);
            }
            QCBroadcastManager manager = new QCBroadcastManager();
            manager.sendBroadcast(KeyValue.REDTYPE);
            if (!TextUtils.isEmpty(remarks)) {
                mName.setText(remarks);
            } else {
                mName.setText(detailEntity.nickname);
            }

            mMonkey.setText(detailEntity.self_amount);
            GlideUtils.loadHeader(DqApp.sContext, detailEntity.headpic, mIcon);
        } else {
            DqToast.showShort( entity.content);
        }

    }
    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if (entity == null) {
            return;
        }

        if (code == ResponseCode.EXPIRY_AUTH) {
            DqToast.showShort(DqApp.getStringById(R.string.expiry_auth));
//            退出
//            quit(activity);
        } else {
            DqToast.showShort(entity.content);
        }
    }
}
