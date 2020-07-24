package com.wd.daquan.model.api;

import com.wd.daquan.model.bean.CloudWithdrawRecordEntity;
import com.wd.daquan.model.bean.CommRespEntity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.ExchangeRecordBean;
import com.wd.daquan.model.bean.ExchangeVipListBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.LoginBean;
import com.wd.daquan.model.bean.NewFriendBean;
import com.wd.daquan.model.bean.OpenRedPackageResultBean;
import com.wd.daquan.model.bean.RedEnvelopBean;
import com.wd.daquan.model.bean.UpdateEntity;
import com.wd.daquan.model.bean.UserBean;
import com.wd.daquan.model.bean.UserCloudWallet;
import com.wd.daquan.model.bean.VipCommodityEntity;
import com.wd.daquan.model.bean.VipExchangeResultBean;
import com.wd.daquan.model.bean.WXLoginEntity;
import com.wd.daquan.model.bean.WxBindBean;
import com.wd.daquan.model.bean.WxPayBody;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * QueryMap 通常用于get请求，参数拼接在url后
 * FieldMap 通常用于post请求，参数在请求体里面
 * 使用POST请求 使用到FieldMap Field属性，必须使用FormUrlEncoded
 */
public interface UserApi{

    /**
     * 登录，注册，验证码登录接口
     */
    @POST
    Call<DataBean<LoginBean>> login(@Url String url, @Body RequestBody requestBody);

    /**
     * 获取好友列表
     */
    @POST
    @Headers({"Domain-Name: DqSdk"}) // Add the Domain-Name header
    Call<DataBean<List<Friend>>> getFriendList(@Url String url, @Body RequestBody requestBody);

    /**
     * 获取好友信息
     */
    @POST
    @Headers({"Domain-Name: DqSdk"}) // Add the Domain-Name header
    Call<DataBean<Friend>> getFriend(@Url String url, @Body RequestBody requestBody);

    /**
     * 更新头像
     */
    @POST
    Call<DataBean<UserBean>> updateHeadpic(@Url String url, @QueryMap Map<String, Object> map,
                                           @Body MultipartBody requestBody);

    /**
     * 获取好友请求列表
     */
    @POST
    @Headers({"Domain-Name: DqSdk"}) // Add the Domain-Name header
    Call<DataBean<List<NewFriendBean>>> getFriendRequestList(@Url String url, @Body RequestBody requestBody);

    /**
     * 获取微信绑定状态
     */
    @POST
    Call<DataBean<WXLoginEntity>> getWxBindStatus(@Url String url, @Body RequestBody requestBody);

    /**
     * 微信绑定手机号
     */
    @POST
    Call<DataBean<WxBindBean>> wxBindPhone(@Url String url, @Body RequestBody requestBody);

    /**
     * 版本升级
     */
    @POST
    Call<DataBean<UpdateEntity>> checkVersion(@Url String url, @Body RequestBody requestBody);

    /**
     * 获取VIP列表
     */
    @POST
    Call<DataBean<List<VipCommodityEntity>>> vipCommodityList(@Url String url, @Body RequestBody requestBody);

    /**
     * 获取微信订单
     */
    @POST
    Call<DataBean<WxPayBody>> getWeChatPayOrder(@Url String url, @Body RequestBody requestBody);

    /**
     * 打开红包
     */
    @POST
    Call<DataBean<OpenRedPackageResultBean>> createCouponHistory(@Url String url, @Body RequestBody requestBody);

    /**
     * VIP兑换邀请人数
     */
    @POST
    Call<DataBean<ExchangeRecordBean>> getExchangeRecordBean(@Url String url, @Body RequestBody requestBody);

    /**
     * VIP获取兑换列表
     */
    @POST
    Call<DataBean<List<ExchangeVipListBean>>> getExchangeVipList(@Url String url, @Body RequestBody requestBody);

    /**
     * 获取零钱用户信息
     */
    @POST
    Call<DataBean<UserCloudWallet>> getUserCloudWallet(@Url String url, @Body RequestBody requestBody);

    /**
     * 零钱提现记录
     */
    @POST
    Call<DataBean<CloudWithdrawRecordEntity>> geUserCloudWalletTransactionRecord(@Url String url, @Body RequestBody requestBody);

    /**
     * 请求红包是否中奖
     */
    @POST
    Call<DataBean<RedEnvelopBean>> getUserRedEnvelope(@Url String url, @Body RequestBody requestBody);

    /**
     * vip兑换结果
     */
    @POST
    Call<DataBean<VipExchangeResultBean>> getVipExchangeResult(@Url String url, @Body RequestBody requestBody);

    /**
     * 通用请求
     */
    @POST
    Call<DataBean<String>> getCommonRequest(@Url String url, @Body RequestBody requestBody);

    /**
     * 好友请求应答策略
     */
    @POST
    @Headers({"Domain-Name: DqSdk"}) // Add the Domain-Name header
    Call<DataBean<CommRespEntity>> getFriendInvite(@Url String url, @Body RequestBody requestBody);

    /**
     * 设置好友备注
     */
    @POST
    @Headers({"Domain-Name: DqSdk"}) // Add the Domain-Name header
    Call<DataBean> setRemarkName(@Url String url, @Body RequestBody requestBody);

    /**
     * 删除好友
     */
    @POST
    @Headers({"Domain-Name: DqSdk"}) // Add the Domain-Name header
    Call<DataBean> delFriend(@Url String url, @Body RequestBody requestBody);

    /**
     * 聊天开关相关接口
     */
    @POST
    @Headers({"Domain-Name: DqSdk"}) // Add the Domain-Name header
    Call<DataBean> userSwitch(@Url String url, @Body RequestBody requestBody);
}
