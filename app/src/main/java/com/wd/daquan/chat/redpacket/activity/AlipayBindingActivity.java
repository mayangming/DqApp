package com.wd.daquan.chat.redpacket.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.da.library.widget.CommTitle;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.bean.AlipayBindingEntity;
import com.wd.daquan.chat.redpacket.RedPacketPresenter;
import com.wd.daquan.chat.redpacket.pay.AuthResult;
import com.wd.daquan.chat.redpacket.pay.OrderInfoUtil2_0;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.ResponseCode;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: dukangkang
 * @date: 2018/9/13 10:38.
 * @description: todo ...
 */
public class AlipayBindingActivity extends DqBaseActivity<RedPacketPresenter, DataBean> implements View.OnClickListener {

    private static final int SDK_AUTH_FLAG = 2;

    private AlipayBindingEntity mAlipayBindingEntity;
    private CommTitle mCommTitle = null;
    private TextView mBindTv;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == SDK_AUTH_FLAG) {
                AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                String resultStatus = authResult.getResultStatus();

                // 判断resultStatus 为“9000”且result_code
                // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                    // 获取alipay_open_id，调支付时作为参数extern_token 的value
                    // 传入，则支付账户为该授权账户
                    if (mPresenter != null) {
                        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
                        hashMap.put("auth_code", authResult.getAuthCode());
                        mPresenter.alipayAuthLogin(DqUrl.url_index, hashMap);
                    }
                } else {
                    // 其他状态值则为授权失败
                    DqToast.showShort(authResult.getMemo());
                }
            }
        }
    };

    @Override
    protected RedPacketPresenter createPresenter() {
        return new RedPacketPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.alipay_binding_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mCommTitle = this.findViewById(R.id.alpay_binding_commtitle);
        mCommTitle.setTitle("绑定支付宝");
        mBindTv = this.findViewById(R.id.alpay_binding_btn);
    }

    @Override
    protected void initData() {
        if (mPresenter != null) {
            mPresenter.getConfigInfo(DqUrl.url_getConfigInfo, null);
        }
    }

    @Override
    protected void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        mBindTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mCommTitle.getLeftIvId()) {
            finish();
        } else if (id == mBindTv.getId()) {
            binding();
        }
    }

    private void binding() {
        if (mAlipayBindingEntity == null) {
            DqToast.showShort(DqApp.getStringById(R.string.comm_network_error));
            return;
        }

        if (TextUtils.isEmpty(mAlipayBindingEntity.appid) || TextUtils.isEmpty(mAlipayBindingEntity.pid) || TextUtils.isEmpty(mAlipayBindingEntity.rsa_private_key)) {
            DqToast.showShort(DqApp.getStringById(R.string.comm_network_error));
            return;
        }

        /*
         * authInfo的获取必须来自服务端；
         */
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(mAlipayBindingEntity.pid, mAlipayBindingEntity.appid, df.format(new Date()), true);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
        final String authInfo = info + "&" + "sign=" + mAlipayBindingEntity.rsa_private_key;
        Runnable authRunnable = () -> {
            // 构造AuthTask 对象
            AuthTask authTask = new AuthTask(AlipayBindingActivity.this);
            // 调用授权接口，获取授权结果
            Map<String, String> result = authTask.authV2(authInfo, true);

            Message msg = new Message();
            msg.what = SDK_AUTH_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if (entity == null) {
            return;
        }
        if (code == ResponseCode.EXPIRY_AUTH) {
            DqToast.showShort(DqApp.getStringById(R.string.expiry_auth));
            // TODO: 2018/9/13 退出
        } else {
            DqToast.showShort(entity.content);
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (entity == null) {
            return;
        }

        if (DqUrl.url_getConfigInfo.equals(url)) {
            mAlipayBindingEntity = (AlipayBindingEntity) entity.data;
        } else if (DqUrl.url_index.equals(url)) {
            if (code == 0) {
                DqToast.showShort("成功授权");
                finish();
            }
        }
    }
}
