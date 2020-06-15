package com.wd.daquan.mine.alipay;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.wd.daquan.R;
import com.wd.daquan.chat.redpacket.pay.AuthResult;
import com.wd.daquan.chat.redpacket.pay.OrderInfoUtil2_0;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.mine.bean.AlipayAuthEntity;
import com.da.library.widget.CommTitle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class AlipayAuthActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener{
    private TextView btn_nowBind;
    private AlipayAuthEntity alipayAuthEntity;
    private CommTitle mCommTitle;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SDK_AUTH_FLAG:
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
                        linkedHashMap.put("auth_code", authResult.getAuthCode());
                        mPresenter.alipayAuthLogin(DqUrl.url_index, linkedHashMap);
                    } else {
                        // 其他状态值则为授权失败
//                        Toast.makeText(PayRedpacketActivity.this,
//                                PayRedpacketActivity.this.getString(R.string.authorization_cancel), Toast.LENGTH_SHORT).show();
                        DqToast.showShort(authResult.getMemo());
                    }
                    break;
            }
        }
    };

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.alipay_auth_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.alipayAuthActivityCommtitle);
        btn_nowBind = findViewById(R.id.alipayAuthActivityBtn);
        mCommTitle.setTitle(getString(R.string.alipay_auth_bingding));
    }

    @Override
    public void initListener() {
        btn_nowBind.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void initData() {
        mPresenter.getConfigInfo(DqUrl.url_getConfigInfo, new LinkedHashMap<>());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
           case R.id.alipayAuthActivityBtn:
               authV2(v);
               break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        DqUtils.bequit(entity,this);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_getConfigInfo.equals(url)) {
            if (0 == code){
                alipayAuthEntity = (AlipayAuthEntity) entity.data;
            }
        }else if (DqUrl.url_index.equals(url)){
            if (0 == code){
                DqToast.showShort(getString(R.string.alipay_auth_suc));
                finish();
            }
        }
    }
    /*
     * 支付宝账户授权业务
     *
     *
     */
    public void authV2(View v) {
        if (alipayAuthEntity==null){
            DqToast.showShort(getString(R.string.comm_network_error));
            return;
        }
        if (TextUtils.isEmpty(alipayAuthEntity.pid) || TextUtils.isEmpty(alipayAuthEntity.appid)
                || (TextUtils.isEmpty(alipayAuthEntity.rsa_private_key))) {
            DqToast.showShort(getString(R.string.params_error));
            return;
        }

        /*
         * authInfo的获取必须来自服务端；
         */
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(alipayAuthEntity.pid, alipayAuthEntity.appid,df.format(new Date()) ,true);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
        final String authInfo = info + "&" + "sign=" + alipayAuthEntity.rsa_private_key;
        Runnable authRunnable = () -> {
            // 构造AuthTask 对象
            AuthTask authTask = new AuthTask(AlipayAuthActivity.this);
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
}
