package com.wd.daquan.chat;

import android.text.TextUtils;

import com.dq.im.type.ImType;
import com.netease.nim.uikit.business.session.helper.MessageListPanelHelper;
import com.wd.daquan.chat.group.bean.GroupAssistAuth;
import com.wd.daquan.chat.group.bean.GroupCopyNew;
import com.wd.daquan.chat.group.bean.GroupExitMemberEntity;
import com.wd.daquan.chat.group.bean.GroupManagersAllResp;
import com.wd.daquan.chat.group.bean.SearchGroupAidesBean;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.presenter.BasePresenter;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.model.bean.CreateTeamEntity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.bean.GroupManagerEntity;
import com.wd.daquan.model.bean.UserBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.net.RequestHelper;
import com.wd.daquan.net.callback.ObjectCallback;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 聊天
 * Created by Kind on 2018/9/11.
 */

public class ChatPresenter extends BasePresenter<Presenter.IView<DataBean>> implements Serializable {

    /**
     * 清空聊天记录
     *
     * @param uid
     * @param typeEnum
     */
    public void clearHistory(String uid, ImType typeEnum) {
        MessageListPanelHelper.getInstance().notifyClearMessages(uid);
    }

    /***
     * 单聊截屏通知设置
     */
    public void getScreenShort(String to_uid, String typeScreen) {
        getScreenShort(to_uid, null, typeScreen);
    }

    /***
     * 单聊阅后即焚设置接口 截屏通知设置
     */
    public void getScreenShort(String to_uid, String typeBurn, String typeScreen) {
        showLoading();
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("groupId", to_uid);
        if (!TextUtils.isEmpty(typeBurn)) {
            hashMap.put("type", typeBurn);
        }

        if (!TextUtils.isEmpty(typeScreen)) {
            hashMap.put("screenshot_notify", typeScreen);
        }

        RequestHelper.request(DqUrl.url_burn_set, hashMap, new ObjectCallback<DataBean<GroupInfoBean>>() {

            @Override
            public void onSuccess(Call call, String url, int code, DataBean<GroupInfoBean> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<GroupInfoBean> result, Exception e) {
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
     * 单聊界面设置查询接口
     */
    public void geSettingChat(String to_uid) {
        getSettingBurn(to_uid, null, null);
    }


    /***
     * 阅后即焚状态查询接口
     */
    public void getSettingBurn(String to_uid, String type, String group_id) {
        showLoading();
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("groupId", to_uid);
        if (!TextUtils.isEmpty(type)) {
            hashMap.put("type", type);
        }
        if (!TextUtils.isEmpty(group_id)) {
            hashMap.put("group_id", group_id);
        }

        RequestHelper.request(DqUrl.url_setting_chat, hashMap,
                new ObjectCallback<DataBean<GroupInfoBean>>() {

                    @Override
                    public void onSuccess(Call call, String url, int code,
                                          DataBean<GroupInfoBean> result) {
                        success(url, code, result);
                    }

                    @Override
                    public void onFailed(Call call, String url, int code,
                                         DataBean<GroupInfoBean> result, Exception e) {
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
     * 获取退群成员
     *
     * @param url     网址
     * @param hashMap 参数
     */
    public void getExitGroupMembers(String url, Map<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<List<GroupExitMemberEntity>>>() {

            @Override
            public void onSuccess(Call call, String url, int code, DataBean<List<GroupExitMemberEntity>> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<List<GroupExitMemberEntity>> result, Exception e) {
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
     * 跟新群组个人用户信息
     *
     * @param url     网址
     * @param hashMap 参数
     */
    public void upLoadGroupPersonalInfo(String url, Map<String, String> hashMap) {
        requestString(url, hashMap, "");
    }

    /**
     * 数据请求
     *
     * @param url     网址
     * @param hashMap 参数
     */
    private void requestString(String url, Map<String, String> hashMap, String tag) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean>() {

            public void onSuccess(Call call, String url, int code, DataBean result) {
                result.tag = tag;
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean result, Exception e) {
                result.tag = tag;
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }

    /**
     * 退群
     */
    public void exitGroup(String url, String groupID) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(KeyValue.GROUP_ID, groupID);

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
    }

    /**
     * 群聊开关设置，包含保存到通讯录，截屏通知
     */
    public void setGroupInfo(String groupID, String key, String tag) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(KeyValue.GROUP_ID, groupID);
        hashMap.put(key, tag);

        showLoading();
        RetrofitHelp.request(DqUrl.url_edit_group_info, hashMap, new DqCallBack<DataBean>() {

            @Override
            public void onSuccess(String url, int code, DataBean entity) {
                hideLoading();
                entity.tag = key;
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
     * 入群认证
     */
    public void setGroupApply(String url, Map<String, String> hashMap) {
        request(url, hashMap);
    }

    private void request(String url, Map<String, String> hashMap) {
        showLoading();
        RetrofitHelp.request(url, hashMap, new DqCallBack<DataBean>() {

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
     *群认证开关 设置群聊号
     */
    public void setGroupManagerInfo(String group_id, String examine, int type) {
        String params;
        if (1 == type) {//群认证开关
            params = "examine";
        } else {//设置群聊号
            params = "group_number";
        }

        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("group_id", group_id);
        hashMap.put(params, examine);

        setGroupManagerInfo(DqUrl.url_edit_group_info, hashMap);
    }

    /**
     * 设置群管理
     */
    public void setGroupManagerInfo(String url, Map<String, String> hashMap) {
        request(url, hashMap);
    }

    /***
     *设置群组信息接口
     */
    public void getSetGroupInfo(String group_id, String group_name) {
        showLoading();
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("group_id", group_id);
        hashMap.put("group_name", group_name);
        RequestHelper.request(DqUrl.url_edit_group_info, hashMap, new ObjectCallback<DataBean<GroupInfoBean>>() {

            @Override
            public void onSuccess(Call call, String url, int code, DataBean<GroupInfoBean> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<GroupInfoBean> result, Exception e) {
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
     * 设置群组公告
     */
    public void getSetGroupNotice(String group_id, String announcement) {
        showLoading();
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("group_id", group_id);
        hashMap.put("announcement", announcement);
        RequestHelper.request(DqUrl.url_edit_group_info, hashMap, new ObjectCallback<DataBean<GroupInfoBean>>() {

            @Override
            public void onSuccess(Call call, String url, int code, DataBean<GroupInfoBean> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<GroupInfoBean> result, Exception e) {
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
     * 加入群组接口
     */
    public void getJoinGroup(String group_uid, String group_id, String type, String source_uid, String userHead) {
        showLoading();
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("group_uid", group_uid);
        hashMap.put("group_id", group_id);
        hashMap.put("type", type);
        hashMap.put("source_uid", source_uid);
        hashMap.put("userHead", userHead);
        RequestHelper.request(DqUrl.url_join_group, hashMap, new ObjectCallback<DataBean<CreateTeamEntity>>() {

            @Override
            public void onSuccess(Call call, String url, int code, DataBean<CreateTeamEntity> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<CreateTeamEntity> result, Exception e) {
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
     *创建群组接口
     */
    public void getCreateGroup(String group_uid, String group_name, String userHead) {
        showLoading();
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("group_uid", group_uid);
        hashMap.put("group_name", group_name);
        hashMap.put("userHead", userHead);
        RequestHelper.request(DqUrl.url_create_group, hashMap, new ObjectCallback<DataBean<CreateTeamEntity>>() {

            @Override
            public void onSuccess(Call call, String url, int code, DataBean<CreateTeamEntity> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<CreateTeamEntity> result, Exception e) {
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
     * 好友列表接口 使用缓存
     */
    public void getFriendList(int page, int length, String whether_helper) {//helper 是否返回斗圈小助手0不1返
        showLoading();
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("page", String.valueOf(page));
        hashMap.put("length", String.valueOf(length));
        hashMap.put("whether_helper", whether_helper);
        RequestHelper.request(DqUrl.url_friend_list, hashMap, new ObjectCallback<DataBean<List<Friend>>>() {

            @Override
            public void onSuccess(Call call, String url, int code, DataBean<List<Friend>> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<List<Friend>> result, Exception e) {
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
     * 查询群成员接口
     */
    public void getSelectGroupUser(String group_id) {
        showLoading();
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("group_id", group_id);
        RequestHelper.request(DqUrl.url_select_group_user, hashMap, new ObjectCallback<DataBean<List<Friend>>>() {

            @Override
            public void onSuccess(Call call, String url, int code, DataBean<List<Friend>> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<List<Friend>> result, Exception e) {
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
     * 获取群管理数据
     */
    public void getGroupManagerData(String group_id) {
        showLoading();
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put(KeyValue.GROUP_ID, group_id);
        RetrofitHelp.getGroupApi().getTeamAdmin(DqUrl.url_group_admin, getRequestBody(hashMap))
                .enqueue(new DqCallBack<DataBean<GroupManagerEntity>>() {
            @Override
            public void onSuccess(String url, int code, DataBean<GroupManagerEntity> entity) {
                hideLoading();
                success(url, code, entity);
            }

            @Override
            public void onFailed(String url, int code, DataBean<GroupManagerEntity> entity) {
                hideLoading();
                failed(url, code, entity);
            }
        });
    }

    /***
     *群管理人员
     */
    public void getGroupAdminList(String group_id) {
        showLoading();
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put(KeyValue.GROUP_ID, group_id);
        RequestHelper.request(DqUrl.url_group_managers_list, hashMap, new ObjectCallback<DataBean<GroupManagersAllResp>>() {

            @Override
            public void onSuccess(Call call, String url, int code, DataBean<GroupManagersAllResp> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<GroupManagersAllResp> result, Exception e) {
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
     *设置群管理员
     */
    public void setGroupManagers(String group_id, int type, String admin_uids) {//type 1:添加管理员 2:删除管理员 admin_uids需转成json数组
        showLoading();
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put(KeyValue.GROUP_ID, group_id);
        hashMap.put("type", String.valueOf(type));
        hashMap.put("admin_uid", admin_uids);

        RequestHelper.request(DqUrl.url_group_set_managers, hashMap, new ObjectCallback<DataBean<GroupManagersAllResp>>() {

            @Override
            public void onSuccess(Call call, String url, int code, DataBean<GroupManagersAllResp> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<GroupManagersAllResp> result, Exception e) {
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
     *转让群主
     */
    public void transferGroupMaster(String group_id, String transfer_uids) {
        showLoading();
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put(KeyValue.GROUP_ID, group_id);
        hashMap.put("owner_uid", transfer_uids);

        RequestHelper.request(DqUrl.url_group_transfer_master, hashMap, new ObjectCallback<DataBean>() {

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

    /**
     * 添加表情文件
     * @param url 地址
     * @param hashMap 请求参数
     * @param file 上传的文件
     * @param fileKey 文件key
     */
    public void getAddEmotion(String url, Map<String, String> hashMap, File file, String fileKey) {
        showLoading();
        RequestHelper.request(url, hashMap, file, fileKey, new ObjectCallback<DataBean>(){

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
                hideLoading();
            }
        });
    }

    /**
     * 增删改群助手
     */
    public void setGroupAidesInfo(String url, Map<String, String> hashMap) {
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean>() {

            public void onSuccess(Call call, String url, int code, DataBean result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean result, Exception e) {
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }

    /**
     * 获取群助手列表数据
     */
    public void getGroupAidesInfo(String url, Map<String, String> hashMap) {
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<SearchGroupAidesBean>>() {

            public void onSuccess(Call call, String url, int code, DataBean<SearchGroupAidesBean> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<SearchGroupAidesBean> result, Exception e) {
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }

    /**
     * 查询已加入禁止收发红包成员列表
     */
    public void getForbidRedpacketSelect(String url, Map<String, String> hashMap){
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<List<UserBean>>>() {

            public void onSuccess(Call call, String url, int code, DataBean<List<UserBean>> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<List<UserBean>> result, Exception e) {
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }


    /**
     * 加入、移除群员到禁止收发红包名单
     */
    public void gotoForbidRedPacketJoinAndRemove(String url, Map<String, String> hashMap){
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean>() {

            public void onSuccess(Call call, String url, int code, DataBean result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean result, Exception e) {
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }

    /**
     * 获取群成员列表，[仅限禁止收发红包功能使用]
     */
    public void getFrobidRedPacketSelectMember(String url, Map<String, String> hashMap) {
        showLoading();
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean<List<Friend>>>() {

            public void onSuccess(Call call, String url, int code, DataBean<List<Friend>> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<List<Friend>> result, Exception e) {
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }

    /**
     * 复制新群
     * @param group_id
     */
    public void reqCopyNewGroup(String group_id ) {
        showLoading();

        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("group_id", group_id);
        RequestHelper.request(DqUrl.url_group_copy, hashMap, new ObjectCallback<DataBean<GroupCopyNew>>() {

            public void onSuccess(Call call, String url, int code, DataBean<GroupCopyNew> result) {
                success(url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<GroupCopyNew> result, Exception e) {
                failed(url, code, result);
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }

    /**
     * 群助手授权
     * @param targetId
     * @param plugin_id
     */
    public void groupAuth(String targetId, String plugin_id, ObjectCallback objectCallback) {
        showLoading();
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("group_id", targetId);
        linkedHashMap.put("plugin_id", plugin_id);

        RequestHelper.request(DqUrl.url_group_auth, linkedHashMap, new ObjectCallback<DataBean<GroupAssistAuth>>() {

            public void onSuccess(Call call, String url, int code, DataBean<GroupAssistAuth> result) {
                success(url, code, result);
                if(objectCallback != null){
                    objectCallback.onSuccess(call, url, code, result);
                }
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<GroupAssistAuth> result, Exception e) {
                failed(url, code, result);
                if(objectCallback != null){
                    objectCallback.onFailed(call, url, code, result, e);
                }
            }

            @Override
            public void onFinish() {
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

    public void getUserInfo(String url, Map<String, String> hashMap) {
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
}
