package com.wd.daquan.chat.redpacket.transfer;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.chat.redpacket.RedPacketPresenter;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.third.session.extension.QcTransferAttachment;
import com.da.library.widget.CommTitle;

/**
 * 收款
 * Created by Kind on 2019-05-25.
 */
public class TransferP2PReceiveAct extends DqBaseActivity<RedPacketPresenter, DataBean> {

    private QcTransferAttachment attachment;
    private String transferStatus;

    private ImageView receiveImg;
    private TextView receiveTitle, receiveAmount, receiveConfirm, receiveConfirmPrompt, receiveTranferTime, receiveReceiveTime;

    @Override
    protected RedPacketPresenter createPresenter() {
        return new RedPacketPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.transfer_p2p_receive_act);
    }

    @Override
    protected void initView() {
        setTitle();

        receiveImg = findViewById(R.id.transfer_p2p_receive_img);
        receiveTitle = findViewById(R.id.transfer_p2p_receive_title);
        receiveAmount = findViewById(R.id.transfer_p2p_receive_amount);
        receiveConfirm = findViewById(R.id.transfer_p2p_receive_confirm);
        receiveConfirmPrompt = findViewById(R.id.transfer_p2p_receive_confirm_prompt);

        receiveTranferTime = findViewById(R.id.transfer_p2p_receive_tranfer_time);
        receiveReceiveTime = findViewById(R.id.transfer_p2p_receive_receive_time);


    }

    /**
     * 标题
     */
    private void setTitle() {
        CommTitle baseTitle = findViewById(R.id.base_title);
        baseTitle.setTitle(getString(R.string.transfer_transfer));
        baseTitle.setLeftVisible(View.VISIBLE);
        baseTitle.getLeftIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferP2PReceiveAct.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        attachment = getIntent().getParcelableExtra(KeyValue.Transfer.ATTACHMENT);
        transferStatus = getIntent().getStringExtra(KeyValue.Transfer.TRANSFERSTATUS);

        receiveAmount.setText("￥ " + getAttachment().amount);
        String uid = ModuleMgr.getCenterMgr().getUID();
        if (KeyValue.ONE_STRING.equals(transferStatus)) {//等待接收
            if (!TextUtils.isEmpty(uid) && uid.equals(attachment.receiveId)) {//接收转账
                receiveTitle.setText(getAttachment().receiveName + "待确认收款");
            } else {
                receiveTitle.setText("待确认收款");
            }
            receiveConfirmPrompt.setText(R.string.transfer_receive_complete_prompt);
            receiveImg.setImageResource(R.mipmap.icon_pay_wait);
            receiveTranferTime.setText("转账时间：" + getAttachment().create_time);
        } else if (KeyValue.TWO_STRING.equals(transferStatus)) {//已领取"待确认收款"
            if (!TextUtils.isEmpty(uid) && uid.equals(attachment.receiveId)) {//接收转账
                receiveTitle.setText(getAttachment().receiveName + "已收钱");

            } else {
                receiveTitle.setText("已收钱");
            }
            receiveConfirmPrompt.setVisibility(View.GONE);
            receiveImg.setImageResource(R.mipmap.icon_pay_success);
            receiveTranferTime.setText("转账时间："+ getAttachment().create_time);
            receiveReceiveTime.setText("到账时间："+ getAttachment().receive_time);
            receiveConfirm.setVisibility(View.GONE);
        } else {
            receiveTitle.setText("已过期");
            receiveConfirmPrompt.setVisibility(View.GONE);
            receiveImg.setImageResource(R.mipmap.icon_pay_wait);
            receiveTranferTime.setText("转账时间："+ getAttachment().expire_time);
            receiveConfirm.setVisibility(View.GONE);

        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        receiveConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.acceptTransfer(getAttachment().transferId, "2");
            }
        });
    }


    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if (DqUrl.url_pay_thbhh_lbgcw.equals(url)) {

        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_pay_thbhh_lbgcw.equals(url)) {

        }
    }

    public QcTransferAttachment getAttachment() {
        if (attachment == null) {
            attachment = new QcTransferAttachment();
        }
        return attachment;
    }
}
