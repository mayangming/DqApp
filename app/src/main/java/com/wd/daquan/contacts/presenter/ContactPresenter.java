package com.wd.daquan.contacts.presenter;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.contacts.bean.MobileContactBean;
import com.wd.daquan.model.bean.CommRespEntity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.model.bean.NewFriendBean;
import com.wd.daquan.model.bean.TeamBean;
import com.wd.daquan.model.bean.TeamInviteBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.net.RequestHelper;
import com.wd.daquan.net.callback.ObjectCallback;

import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * @Author: 方志
 * @Time: 2018/9/12 17:22
 * @Description:
 */
public class ContactPresenter extends BasePresenter<Presenter.IView<DataBean>> {

    /**
     * 获取联系人列表
     */
    public void getContacts(String url, Map<String, String> hashMap, boolean isShowLoading) {

        if(isShowLoading) {
            showLoading();
        }



        RetrofitHelp.getUserApi().getFriendList(url, getRequestBody(hashMap)).enqueue(
            new DqCallBack<DataBean<List<Friend>>>() {
                @Override
                public void onSuccess(String url, int code, DataBean<List<Friend>> entity) {
                    if(isShowLoading) {
                        hideLoading();
                    }
                    success(url, code, entity);
                }

                @Override
                public void onFailed(String url, int code, DataBean<List<Friend>> entity) {
                    if(isShowLoading) {
                        hideLoading();
                    }
                    failed(url, code, entity);
                }
        });
    }

    /***
     * 获取其他用户信息
     */
    public void getUserInfo(String url, Map<String, String> hashMap, String type) {
        if("1".equals(type)) {
            requestNewFriend(url, hashMap);
        }else {
            requestFriend(url, hashMap);
        }

    }

    private void requestNewFriend(String url, Map<String, String> hashMap) {

        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<NewFriendBean>>(){
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<NewFriendBean> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<NewFriendBean> result, Exception e) {
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

    private void requestFriend(String url, Map<String, String> hashMap) {
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
    }

    /**
     * 搜索添加好友
     */
    public void getSearchFriend(String url, Map<String, String> hashMap) {
        requestFriend(url, hashMap);
    }

    /**
     * 添加好友
     */
    public void getFriendRequest(String url, Map<String, String> hashMap) {
        requestFriend(url, hashMap);
    }

    /**
     * 新的朋友申请列表
     */
    public void getFriendRequestList(String url, Map<String, String> hashMap, boolean isShow) {
        if(isShow) {
            showLoading();
        }

        RetrofitHelp.getUserApi().getFriendRequestList(url, getRequestBody(hashMap)).enqueue(
                new DqCallBack<DataBean<List<NewFriendBean>>>() {
                    @Override
                    public void onSuccess(String url, int code, DataBean<List<NewFriendBean>> entity) {
                        if(isShow) {
                            hideLoading();
                        }

                        success(url, code, entity);
                    }

                    @Override
                    public void onFailed(String url, int code, DataBean<List<NewFriendBean>> entity) {
                        if(isShow) {
                            hideLoading();
                        }
                        failed(url, code, entity);
                    }
                });
    }

    /**
     * 添加好友应答
     */
    public void getFriendResponse(String url, Map<String, String> hashMap) {
        showLoading();
        RetrofitHelp.request(url, hashMap, new DqCallBack<DataBean>(){

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
     * 添加好友应答
     */
    public void getFriendInviteResponse(String url, Map<String, String> hashMap) {
        showLoading();
        RetrofitHelp.getUserApi().getFriendInvite(url, getRequestBody(hashMap)).enqueue(new DqCallBack<DataBean<CommRespEntity>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<CommRespEntity> entity) {
                hideLoading();
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<CommRespEntity> entity) {
                hideLoading();
                failed(url, code, entity);
            }
        });
    }
    /**
     * 群组邀请应答策略
     */
    public void getTeamResponse(String url, Map<String, String> hashMap) {
        showLoading();
//        RetrofitHelp.request(url, hashMap, new DqCallBack<DataBean<CommRespEntity>>(){
//
//            @Override
//            public void onSuccess(String url, int code, DataBean<CommRespEntity> entity) {
//                hideLoading();
//                success(url, code, entity);
//            }
//
//            @Override
//            public void onFailed(String url, int code, DataBean<CommRespEntity> entity) {
//                hideLoading();
//                failed(url, code, entity);
//            }
//        });
        RetrofitHelp.getGroupApi().getTeamInvite(url, getRequestBody(hashMap)).enqueue(new DqCallBack<DataBean<CommRespEntity>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<CommRespEntity> entity) {
                                        hideLoading();
                        success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<CommRespEntity> entity) {
                        hideLoading();
                        failed(url, code, entity);
            }
        });
    }

    /**
     * 获取手机联系人
     */
    public void getMobileContactList(String url, Map<String, String> hashMap) {

        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<List<MobileContactBean>>>(){
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<List<MobileContactBean>> result) {
                super.onSuccess(call, url, code, result);
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<List<MobileContactBean>> result, Exception e) {
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
     * 设置备注
     */
    public void setRemarkName(String url, Map<String, String> hashMap) {

        getFriendResponse(url, hashMap);
    }

    /***
     * 添加/移除黑名单
     */
    public void getRemoveBlacklist(String url, Map<String, String> hashMap) {
        if (!DqUtils.isNetworkConnected(DqApp.sContext)) {
            DqToast.showShort(DqApp.sContext.getString(R.string.network_err_please_check_and_try_again));
            return;
        }
        getFriendResponse(url, hashMap);
    }

    /**
     * 获取已保存的群组列表
     */
    public void getTeamsList(String url, Map<String, String> hashMap) {

        showLoading();
        RetrofitHelp.getGroupApi().getTeamList(url, getRequestBody(hashMap)).enqueue(
                new DqCallBack<DataBean<List<TeamBean>>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<List<TeamBean>> entity) {
                hideLoading();
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<List<TeamBean>> entity) {
                hideLoading();
                failed(url, code, entity);
            }
        });
    }

    /**
     * 获取群组个人信息
     * @param url 网址
     * @param hashMap 参数
     * @param isGetDesc 是否获取描述信息
     */
    public void getGroupUserInfo(String url, Map<String, String> hashMap, boolean isGetDesc) {
        showLoading();

        if(isGetDesc) {
            RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<GroupMemberBean>>() {

                public void onSuccess(Call call, String url, int code, DataBean<GroupMemberBean> result) {
                    success(url, code, result);
                }

                @Override
                public void onFailed(Call call, String url, int code, DataBean<GroupMemberBean> result, Exception e) {
                    failed(url, code, result);
                }

                @Override
                public void onFinish() {
                    hideLoading();
                }
            });
        }else {
            requestFriend(url, hashMap);
        }

    }

    /***
     * 获取群组内被邀请的列表数据
     */
    public void getTeamInviteList(String url) {
        RetrofitHelp.getGroupApi().getInviteTeamList(url, getRequestBody(null))
                .enqueue(new DqCallBack<DataBean<List<TeamInviteBean>>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<List<TeamInviteBean>> entity) {
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<List<TeamInviteBean>> entity) {
                failed(url, code, entity);
            }
        });
    }
    /** 发送短信 */
    public void senInvitationSms(String url,Map<String,String> params){
        RetrofitHelp.request(url,params,new DqCallBack<DataBean>() {
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

}
