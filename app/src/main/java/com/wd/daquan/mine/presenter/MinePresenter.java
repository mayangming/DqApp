package com.wd.daquan.mine.presenter;


import com.wd.daquan.chat.emotion.MyEmotionEntity;
import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.mine.bean.AlipayAuthEntity;
import com.wd.daquan.mine.bean.AlipayRedDetailsEntity;
import com.wd.daquan.mine.bean.AlipayRedListEntity;
import com.wd.daquan.mine.bean.FreezeTypeEntity;
import com.wd.daquan.mine.bean.SafeEntity;
import com.wd.daquan.mine.wallet.bean.WalletSatausBean;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.bean.UpdateEntity;
import com.wd.daquan.model.bean.UserBean;
import com.wd.daquan.model.bean.WXLoginEntity;
import com.wd.daquan.model.bean.WxBindBean;
import com.wd.daquan.model.bean.WxPayBody;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.net.RequestHelper;
import com.wd.daquan.net.callback.ObjectCallback;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MinePresenter<V extends Presenter.IView<DataBean>> extends BasePresenter<V> {

    /***
     *意见反馈接口
     */
    public void submitFeedback(String url, LinkedHashMap<String, String> linkedHashMap) {
        showLoading();
        RequestHelper.request(url, linkedHashMap, new ObjectCallback<DataBean>(){
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

    /**
     * 微信授权登陆：获取聊得来用户的微信绑定状态
     */
    public void getBindWeixinStatus(String url, LinkedHashMap<String, String> linkedHashMap){
        RetrofitHelp.getUserApi().getWxBindStatus(url, getRequestBody(linkedHashMap)).enqueue(
                new DqCallBack<DataBean<WXLoginEntity>>() {
                    @Override
                    public void onSuccess(String url, int code, DataBean<WXLoginEntity> entity) {
                        hideLoading();
                        success(url, code, entity);
                    }

                    @Override
                    public void onFailed(String url, int code, DataBean<WXLoginEntity> entity) {
                        hideLoading();
                        failed(url, code, entity);
                    }
                });
//        RequestHelper.request(url, linkedHashMap, new ObjectCallback<DataBean<WXLoginEntity>>(){
//            @Override
//            public void onSuccess(Call call, String url, int code, DataBean<WXLoginEntity> result) {
//                super.onSuccess(call, url, code, result);
//                success(url, code, result);
//            }
//
//            @Override
//            public void onFailed(Call call, String url, int code, DataBean<WXLoginEntity> result, Exception e) {
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
    }

    /**
     * 微信授权登陆：重新绑定微信
     */
    public void bindWXAgain(String url, LinkedHashMap<String, String> linkedHashMap) {
        showLoading();
        RetrofitHelp.getUserApi().getWxBindStatus(url,getRequestBody(linkedHashMap)).enqueue(new DqCallBack<DataBean<WXLoginEntity>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<WXLoginEntity> entity) {
                hideLoading();
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<WXLoginEntity> entity) {
                hideLoading();
                failed(url, code, entity);
            }
        });
    }

    /**
     * 微信授权登陆：用户解绑微信
     */
    public void cancelBindWeixin(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RetrofitHelp.request(url, hashMap, new DqCallBack() {
            @Override
            public void onSuccess(String url, int code, DataBean entity) {
                hideLoading();
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean entity) {
                hideLoading();
                failed(url, code, entity);
            }
        });
    }

    /***
     *退出登录
     */
    public void logout(String url) {
        showLoading();
        RetrofitHelp.request(url, null, new DqCallBack<DataBean>(){

            @Override
            public void onSuccess(String url, int code, DataBean entity) {
                hideLoading();
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean entity) {
                hideLoading();
                failed(url, code, entity);
            }
        });
    }

    //设置新消息通知
    public void setNewMessageNotify(String url, LinkedHashMap<String, String> linkedHashMap){
        setUserSwitch(url, linkedHashMap);
    }

    /**
     * 设置斗圈号是否允许被搜索
     * @param url
     * @param hashMap
     */
    public void qingChatNumSearch(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<SafeEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<SafeEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<SafeEntity> result, Exception e) {
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
     * 设置是否可以通过手机号搜索自己
     *  设置被加为好友时是否需要验证接口
     *  设置个人用户的开关
     */
    public void setUserSwitch(String url, Map<String, String> hashMap) {
        showLoading();
//        RetrofitHelp.request(url, hashMap, new DqCallBack() {
//            @Override
//            public void onSuccess(String url, int code, DataBean entity) {
//                hideLoading();
//                success(url, code, entity);
//            }
//
//            @Override
//            public void onFailed(String url, int code, DataBean entity) {
//                hideLoading();
//                success(url, code, entity);
//            }
//        });
        RetrofitHelp.getUserApi().userSwitch(url, getRequestBody(hashMap)).enqueue(
                new DqCallBack<DataBean>() {
                    @Override
                    public void onSuccess(String url, int code, DataBean entity) {
                        hideLoading();
                        success(url, code, entity);
                    }

                    @Override
                    public void onFailed(String url, int code, DataBean entity) {
                        hideLoading();
                        failed(url, code, entity);
                    }
                });
    }

    /**
     * 设置被加为好友时是否需要验证接口
     */
    public void getValidateWhether(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<SafeEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<SafeEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<SafeEntity> result, Exception e) {
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
     * 验证码
     */
    public void getPhoneCode(String url, LinkedHashMap<String, String> hashMap) {
        RetrofitHelp.request(url, hashMap, new DqCallBack<DataBean>() {

            @Override
            public void onSuccess(String url, int code, DataBean entity) {
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean entity) {
                failed(url, code, entity);
            }
        });
//        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean>() {
//            @Override
//            public void onSuccess(Call call, String url, int code, DataBean result) {
//                super.onSuccess(call, url, code, result);
//                success(url, code, result);
//            }
//
//            @Override
//            public void onFailed(Call call, String url, int code, DataBean result, Exception e) {
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
    }

    /***
     * 设置密码
     */
    public void getSetPwd(String url, LinkedHashMap<String, String> hashMap) {
        getPhoneCode(url, hashMap);
//        showLoading();
//        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean>() {
//            @Override
//            public void onSuccess(Call call, String url, int code, DataBean result) {
//                super.onSuccess(call, url, code, result);
//                success(url, code, result);
//            }
//
//            @Override
//            public void onFailed(Call call, String url, int code, DataBean result, Exception e) {
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
    }

    /***
     * 黑名单列表接口
     */
    public void getBlacklist(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RetrofitHelp.getUserApi().getFriendList(url, getRequestBody(hashMap)).enqueue(
                new DqCallBack<DataBean<List<Friend>>>() {
                    @Override
                    public void onSuccess(String url, int code, DataBean<List<Friend>> entity) {
                        hideLoading();
                        success(url, code, entity);
                    }

                    @Override
                    public void onFailed(String url, int code, DataBean<List<Friend>> entity) {
                        hideLoading();
                        failed(url, code, entity);
                    }
                });
    }
    /***
     * 移除黑名单
     */
    public void doRemoveBlacklist(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();

        RetrofitHelp.request(url, hashMap, new DqCallBack<DataBean<List<Friend>>>() {
            @Override
            public void onSuccess(String url, int code, DataBean entity) {
                hideLoading();
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean entity) {
                hideLoading();
                failed(url, code, entity);
            }
        });
    }

    /***
     * 查询是否可一键冻结/解冻接口
     */
    public void getWhether_block(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<FreezeTypeEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<FreezeTypeEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<FreezeTypeEntity> result, Exception e) {
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
     * 自主冻结用户/解冻
     */
    public void doFreezeAndUnFreeze(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<FreezeTypeEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<FreezeTypeEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<FreezeTypeEntity> result, Exception e) {
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
     * 检查版本升级
     * @param url
     *  请求地址
     * @param hashMap
     *  请求参数
     */
    public void checkVersion(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RetrofitHelp.getUserApi().checkVersion(url,getRequestBody(hashMap)).enqueue(new DqCallBack<DataBean<UpdateEntity>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<UpdateEntity> entity) {
                hideLoading();
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<UpdateEntity> entity) {
                hideLoading();
                failed(url, code, entity);
            }
        });
//        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<UpdateEntity>>() {
//            @Override
//            public void onSuccess(Call call, String url, int code, DataBean<UpdateEntity> result) {
//                super.onSuccess(call, url, code, result);
//                success(url, code, result);
//            }
//
//            @Override
//            public void onFailed(Call call, String url, int code, DataBean<UpdateEntity> result, Exception e) {
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
    }

    /***
     * 保存性别 昵称
     */
    public void saveUserInfoRequest(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RetrofitHelp.getUserApi().getFriend(url, getRequestBody(hashMap)).enqueue(
                new DqCallBack<DataBean<Friend>>() {
                    @Override
                    public void onSuccess(String url, int code, DataBean<Friend> entity) {
                        hideLoading();
                        success(url, code, entity);
                    }

                    @Override
                    public void onFailed(String url, int code, DataBean<Friend> entity) {
                        hideLoading();
                        failed(url, code, entity);
                    }
                });
//        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<UpdateEntity>>() {
//            @Override
//            public void onSuccess(Call call, String url, int code, DataBean<UpdateEntity> result) {
//                super.onSuccess(call, url, code, result);
//                success(url, code, result);
//            }
//
//            @Override
//            public void onFailed(Call call, String url, int code, DataBean<UpdateEntity> result, Exception e) {
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
    }
    /**
     * 设置斗圈号
     * @param url
     * @param hashMap
     */
    public void setQSNum(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RetrofitHelp.request(url, hashMap, new DqCallBack() {
            @Override
            public void onSuccess(String url, int code, DataBean entity) {
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean entity) {
                failed(url, code, entity);
            }
        });
//        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean>() {
//            @Override
//            public void onSuccess(Call call, String url, int code, DataBean result) {
//                super.onSuccess(call, url, code, result);
//                success(url, code, result);
//            }
//
//            @Override
//            public void onFailed(Call call, String url, int code, DataBean result, Exception e) {
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
    }

    /**
     * 设置头像
     */
    public void setUserInfoAvatar(String url, Map<String, String> hashMap, File file, String fileKey) {
        showLoading();
        RequestHelper.request(url, hashMap, file, fileKey, new ObjectCallback<DataBean<UserBean>>() {

            @Override
            public void onSuccess(Call call, String url, int code, DataBean<UserBean> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<UserBean> result, Exception e) {
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
     * 设置头像
     */
    public void setUserInfoAvatar(String url, Map<String, File> fileMap) {
        showLoading();

        RetrofitHelp.getUserApi().updateHeadpic(url, getParams(), getFileBody(fileMap)).enqueue(
            new DqCallBack<DataBean<UserBean>>() {
                @Override
                public void onSuccess(String url, int code, DataBean<UserBean> entity) {
                    hideLoading();
                    success(url, code, entity);
                }

                @Override
                public void onFailed(String url, int code, DataBean<UserBean> entity) {
                    hideLoading();
                    failed(url, code, entity);
                }
        });
//        RequestHelper.request(url, hashMap, file, fileKey, new ObjectCallback<DataBean<UserBean>>() {
//
//            @Override
//            public void onSuccess(Call call, String url, int code, DataBean<UserBean> result) {
//                success(url, code, result);
//            }
//
//            @Override
//            public void onFailed(Call call, String url, int code, DataBean<UserBean> result, Exception e) {
//                failed(url, code, result);
//        }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                hideLoading();
//            }
//        });
    }


    /***
     * 是否已经绑定支付宝账户接口
     */
    public void getIsBindAlipay(String url, LinkedHashMap<String, String> hashMap) {
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
     * 获取支付宝授权登陆所需配置信息
     */
    public void getConfigInfo(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<AlipayAuthEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<AlipayAuthEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<AlipayAuthEntity> result, Exception e) {
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
     * 支付宝授权登录
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
    /**
     * 获取支付宝红包明细
     */
    public void getMyAliRedpacket(String url, Map<String, String> hashMap) {
        //showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<AlipayRedListEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<AlipayRedListEntity> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<AlipayRedListEntity> result, Exception e) {
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
     * 取消绑定支付宝
     */
    public void getCancelBindAliPay(String url, Map<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean result, Exception e) {
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
     * 支付宝红包领取明细查询接口
     */
    public void getRedPacketDetail(String url, Map<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<AlipayRedDetailsEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<AlipayRedDetailsEntity> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<AlipayRedDetailsEntity> result, Exception e) {
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
     * 添加好友
     * @param url
     *  请求地址
     * @param hashMap
     *  请求参数
     */
    public void addFriend(String url, LinkedHashMap<String, String> hashMap) {
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


    public void getTeamInfo(String url, Map<String, String> hashMap) {

        RetrofitHelp.getGroupApi().getGroupInfo(url, getRequestBody(hashMap)).enqueue(
                new DqCallBack<DataBean<GroupInfoBean>>() {
                    @Override
                    public void onSuccess(String url, int code, DataBean<GroupInfoBean> entity) {
                        success(url, code, entity);
                    }

                    @Override
                    public void onFailed(String url, int code, DataBean<GroupInfoBean> entity) {
                        failed(url, code, entity);
                    }
                });
    }

    //我的自定义表情列表
    public void getMyEmotion(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<List<MyEmotionEntity>>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<List<MyEmotionEntity>> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<List<MyEmotionEntity>> result, Exception e) {
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
    //添加表情
    public void addEmotion(String url, LinkedHashMap<String, String> hashMap, File file,String key) {
        //网络层
        RequestHelper.request(url, hashMap, file, key, new ObjectCallback<DataBean<MyEmotionEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<MyEmotionEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<MyEmotionEntity> result, Exception e) {
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
    //删除表情
    public void deleteEmotion(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<List<MyEmotionEntity>>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<List<MyEmotionEntity>> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<List<MyEmotionEntity>> result, Exception e) {
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
     * 设置是否开启戳一下消息
     * @param url
     * @param linkedHashMap
     */
    public void setPoke(String url, LinkedHashMap<String, String> linkedHashMap){
        setUserSwitch(url, linkedHashMap);
    }

    /***
     * 是否允许被添加至群聊
     */
    public void setTeamInvite(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<SafeEntity>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<SafeEntity> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<SafeEntity> result, Exception e) {
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
     * 用户钱包状态查询
     */
    public void getWalletStatus(String url, LinkedHashMap<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<WalletSatausBean>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<WalletSatausBean> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<WalletSatausBean> result, Exception e) {
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
     * 获取支付宝订单信息
     */
    public void getAliPayOrderInfo(String url, Map<String, String> hashMap) {
        showLoading();
        RetrofitHelp.request(url, hashMap, new DqCallBack<DataBean<String>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<String> entity) {
                hideLoading();
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<String> entity) {
                hideLoading();
                failed(url, code, entity);
            }
        });
    }
    /***
     * 获取微信订单信息
     */
    public void getWeChatPayOrderInfo(String url, Map<String, String> hashMap) {
        showLoading();
        RetrofitHelp.getUserApi().getWeChatPayOrder(url,getRequestBodyByFromData(hashMap)).enqueue(new DqCallBack<DataBean<WxPayBody>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<WxPayBody> entity) {
                DqLog.e("YM","成功------："+url);
                hideLoading();
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<WxPayBody> entity) {
                hideLoading();
                failed(url, code, entity);
                DqLog.e("YM","失败------");
            }
        });
    }

    /**
     * 获取用户信息
     * 不需要传递任何信息，uid基类已经获取到了，可以获取用户信息
     */
    public void requestUserInfo(String url, Map<String, String> hashMap) {

        RetrofitHelp.getUserApi().wxBindPhone(url,getRequestBody(hashMap)).enqueue(new DqCallBack<DataBean<WxBindBean>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<WxBindBean> entity) {
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<WxBindBean> entity) {
                failed(url, code, entity);
            }
        });
    }

    /**
     * 注销账号
     * @param url
     * @param hashMap
     */
    public void requestLogOff(String url, Map<String, String> hashMap){
        RetrofitHelp.request(url, hashMap, new DqCallBack() {
            @Override
            public void onSuccess(String url, int code, DataBean entity) {
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean entity) {
                failed(url, code, entity);
            }
        });
    }

}
