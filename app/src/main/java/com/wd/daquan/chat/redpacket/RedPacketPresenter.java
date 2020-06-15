package com.wd.daquan.chat.redpacket;

import com.wd.daquan.chat.bean.AlipayBindingEntity;
import com.wd.daquan.chat.bean.AlipayEntity;
import com.wd.daquan.chat.bean.ForbidEntity;
import com.wd.daquan.chat.bean.RobRpEntity;
import com.wd.daquan.chat.bean.RpDetailEntity;
import com.wd.daquan.common.bean.AliOssResp;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.net.RequestHelper;
import com.wd.daquan.net.callback.ObjectCallback;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * @author: dukangkang
 * @date: 2018/9/12 17:26.
 * @description: todo ...
 */
public class RedPacketPresenter extends BasePresenter implements Serializable {

    /**
     *
     * @param url
     * @param hashMap
     */
    public void isBindAlipay(String url, Map<String, String> hashMap) {
        isBindAlipay(url, hashMap, null);
    }

    public void isBindAlipay(String url, Map<String, String> hashMap, ObjectCallback objectCallback) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<AliOssResp>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<AliOssResp> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
                if(objectCallback != null){
                    objectCallback.onSuccess(call, url, code, result);
                }
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<AliOssResp> result, Exception e) {
                super.onFailed(call, url, code, result, e);
                failed(url, code, result);
                if(objectCallback != null){
                    objectCallback.onFailed(call, url, code, result, e);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }


    /**
     * 判断是否在禁止名单内
     * @param url
     * @param hashMap
     */
    public void checkForbidRedpacket(String url, LinkedHashMap<String, String> hashMap)  {
        checkForbidRedpacket(url, hashMap, null);
    }

    public void checkForbidRedpacket(String url, LinkedHashMap<String, String> hashMap, ObjectCallback objectCallback)  {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<ForbidEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<ForbidEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
                if(objectCallback != null){
                    objectCallback.onSuccess(call, url, code, result);
                }
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<ForbidEntity> result, Exception e) {
                super.onFailed(call, url, code, result, e);
                failed(url, code, result);
                if(objectCallback != null){
                    objectCallback.onFailed(call, url, code, result, e);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    /**
     * 绑定支付宝
     * @param url
     * @param hashMap
     */
    public void getConfigInfo(String url, LinkedHashMap<String, String> hashMap)  {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<AlipayBindingEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<AlipayBindingEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<AlipayBindingEntity> result, Exception e) {
                super.onFailed(call, url, code, result, e);
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    /**
     * 支付宝授权登录
     * @param url
     * @param hashMap
     */
    public void alipayAuthLogin(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean result, Exception e) {
                super.onFailed(call, url, code, result, e);
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    /***
     *发放支付宝红包接口
     */
    public void getGiveRedPacket(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<AlipayEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<AlipayEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<AlipayEntity> result, Exception e) {
                super.onFailed(call, url, code, result, e);
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    /**
     * 抢支付宝红包
     * @param url
     * @param hashMap
     */
    public void robAlipayRedPacket(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<RobRpEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<RobRpEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<RobRpEntity> result, Exception e) {
                super.onFailed(call, url, code, result, e);
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    /**
     * 支付宝红包-开，领取
     * @param url
     * @param hashMap
     */
    public void openRedpacket(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<RobRpEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<RobRpEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<RobRpEntity> result, Exception e) {
                super.onFailed(call, url, code, result, e);
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    /**
     * 支付宝红包领取明细查询接口
     * @param url
     * @param hashMap
     */
    public void redpacketDetail(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<RpDetailEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<RpDetailEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<RpDetailEntity> result, Exception e) {
                super.onFailed(call, url, code, result, e);
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

//    /**
//     * 转帐
//     * @param amount 转账金额
//     * @param to_uid 接受转账人
//     * @param pwd_pay 支付密码
//     */
//    public void onTransfer(String amount, String greetings, String to_uid, String pwd_pay) {
//        showLoading();
//        Map<String, String> stringMap = new HashMap<>();
//        stringMap.put(KeyValue.Transfer.AMOUNT, amount);
//        stringMap.put(KeyValue.Transfer.GREETINGS, greetings);
//        stringMap.put(KeyValue.Transfer.TO_UID, to_uid);
//        stringMap.put(KeyValue.Transfer.PWD_PAY, pwd_pay);
//
//        stringMap.put(KeyValue.Transfer.FRMS_IMEI,ModuleMgr.getAppMgr().getIMEI());
//        stringMap.put(KeyValue.Transfer.SOURCE, "ANDROID");//设备来源（ANDROID、IOS、WEB、WAP）
//        stringMap.put(KeyValue.Transfer.PKG_NAME, ModuleMgr.getAppMgr().getPackageName());
//        stringMap.put(KeyValue.Transfer.APP_NAME, ModuleMgr.getAppMgr().getAppName());
//
//        RequestHelper.request(DqUrl.url_pay_thbhh_oeutu, stringMap, new ObjectCallback<DataBean<Transfer>>() {
//            @Override
//            public void onSuccess(Call call, String url, int code, DataBean<Transfer> result) {
//                super.onSuccess(call, url, code, result);
//                success(url, code, result);
//            }
//
//            @Override
//            public void onFailed(Call call, String url, int code, DataBean<Transfer> result, Exception e) {
//                super.onFailed(call, url, code, result, e);
//                failed(url, code, result);
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                hideLoading();
//            }
//        });
//    }

    /**
     * 验证转帐
     * @param verify_code
     * @param verify_token
     */
    public void verifyTransfer(String verify_code, String verify_token) {
        showLoading();
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(KeyValue.Transfer.VERIFY_CODE, verify_code);
        stringMap.put(KeyValue.Transfer.VERIFY_TOKEN, verify_token);

        RequestHelper.request(DqUrl.url_pay_thbhh_slkst, stringMap, new ObjectCallback<DataBean<RpDetailEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<RpDetailEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<RpDetailEntity> result, Exception e) {
                super.onFailed(call, url, code, result, e);
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    /**
     * 接受转帐
     * @param own_order_id 转账订单号
     * @param suffix  数据表唯一值
     */
    public void acceptTransfer(String own_order_id, String suffix) {
        showLoading();
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(KeyValue.Transfer.OWN_ORDER_ID, own_order_id);
        stringMap.put(KeyValue.Transfer.SUFFIX, suffix);

        RequestHelper.request(DqUrl.url_pay_thbhh_lbgcw, stringMap, new ObjectCallback<DataBean<RpDetailEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<RpDetailEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<RpDetailEntity> result, Exception e) {
                super.onFailed(call, url, code, result, e);
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    /**
     * 创建红包订单
     * @param type 1、个人红包；2、群红包
     * @param group_id 1、个人红包；2、群红包
     * @param style 1、随机红包；2、普通红包
     * @param receive_uids 指定抢红包的用户，多个uid之间用英文状态的逗号连接
     * @param greetings 红包名称
     * @param amount 红包金额，必须是两位小数
     * @param num 红包数量
     * @param redpacket_extra 专属红包扩展字段（客户端传字符串，服务端默认为空字符串）
     * @param pwd_pay 支付密码
     */
//    public void createRedEnvelope(String type, String group_id, String style, String receive_uids,
//                                  String greetings, String amount, String num, String redpacket_extra,
//                                  String pwd_pay) {
//        showLoading();
//        Map<String, String> stringMap = new HashMap<>();
//        stringMap.put(KeyValue.RedPacket.TYPE, type);
//        stringMap.put(KeyValue.RedPacket.GROUP_ID, group_id);
//        stringMap.put(KeyValue.RedPacket.STYLE, style);
//        stringMap.put(KeyValue.RedPacket.RECEIVE_UIDS, receive_uids);
//        stringMap.put(KeyValue.RedPacket.GREETINGS, greetings);
//        stringMap.put(KeyValue.RedPacket.AMOUNT, amount);
//        stringMap.put(KeyValue.RedPacket.NUM, num);
//        stringMap.put(KeyValue.RedPacket.REDPACKET_EXTRA, redpacket_extra);
//        stringMap.put(KeyValue.RedPacket.PWD_PAY, pwd_pay);
//
////        stringMap.put(KeyValue.RedPacket.FRMS_IMEI,ModuleMgr.getAppMgr().getIMEI());
//        stringMap.put(KeyValue.RedPacket.SOURCE, "ANDROID");//设备来源（ANDROID、IOS、WEB、WAP）
//        stringMap.put(KeyValue.RedPacket.PKG_NAME, ModuleMgr.getAppMgr().getPackageName());
//        stringMap.put(KeyValue.RedPacket.APP_NAME, ModuleMgr.getAppMgr().getAppName());
//
//        RequestHelper.request(DqUrl.url_pay_selis_tfces, stringMap, new ObjectCallback<DataBean<RpDetailEntity>>() {
//            @Override
//            public void onSuccess(Call call, String url, int code, DataBean<RpDetailEntity> result) {
//                super.onSuccess(call, url, code, result);
//                success(url, code, result);
//            }
//
//            @Override
//            public void onFailed(Call call, String url, int code, DataBean<RpDetailEntity> result, Exception e) {
//                super.onFailed(call, url, code, result, e);
//                failed(url, code, result);
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                hideLoading();
//            }
//        });
//    }





}
