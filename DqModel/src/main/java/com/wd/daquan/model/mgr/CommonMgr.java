package com.wd.daquan.model.mgr;

import android.text.TextUtils;

import com.wd.daquan.net.RequestHelper;
import com.wd.daquan.model.bean.CommonConfig;
import com.wd.daquan.model.bean.ConfigBean;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.rxbus.ModuleBase;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.net.callback.ObjectCallback;
import com.wd.daquan.model.utils.GsonUtils;

import java.util.HashMap;

import okhttp3.Call;

/**
 * 通用
 * Created by Kind on 2019/1/8.
 */
public class CommonMgr implements ModuleBase, QCObserver {

    private ConfigBean configBean;//最初的配置信息
    private CommonConfig commonConfig;//服务器静态配置
//    private List<ChatBg> chatBgs;//bg所有图

    @Override
    public void init() {
        MsgMgr.getInstance().attach(this);
    }

    @Override
    public void release() {
        MsgMgr.getInstance().detach(this);
    }

    /**
     * 配置接口
     * @param url 地址
     */
    public void reqServerConfig(String url) {
        RequestHelper.request(url, new HashMap<String, String>(), new ObjectCallback<DataBean<ConfigBean>>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean<ConfigBean> entity) {
                super.onSuccess(call, url, code, entity);
                ConfigBean configBean = entity.data;
                if (configBean != null) {
                    ConfigManager.getInstance().saveServerConfig(entity.getJson());
                    setConfigBean(configBean);
                }
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean<ConfigBean> result, Exception e) {
                super.onFailed(call, url, code, result, e);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }


//    /**
//     * 配置接口 聊天列表扩展banner
//     */
//    public void reqNewsServerConfig(ObjectCallback objectCallback) {
//        RequestHelper.request(NewSceneUrl.NEWS_SERVER_CONFIG, new HashMap<>(),
//                new ObjectCallback<DataBean<CommonConfig>>() {
//                    @Override
//                    public void onSuccess(Call call, String url, int code, DataBean<CommonConfig> result) {
//                        super.onSuccess(call, url, code, result);
//                        if (objectCallback != null) {
//                            objectCallback.onSuccess(call, url, code, result);
//                        }
//                        CommonConfig commonConfig = result.data;
//                        MsgMgr.getInstance().sendMsg(MsgType.MT_App_Config, commonConfig != null);
//                        if (commonConfig != null) {
//                            ConfigManager.getInstance().saveNewsServerConfig(result.getJson());
//                            setCommonConfig(commonConfig);
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(Call call, String url, int code, DataBean<CommonConfig> result, Exception e) {
//                        super.onFailed(call, url, code, result, e);
//                        if (objectCallback != null) {
//                            objectCallback.onFailed(call, url, code, result, e);
//                        }
//                        MsgMgr.getInstance().sendMsg(MsgType.MT_App_Config, false);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        super.onFinish();
//                    }
//                });
//    }

    public ConfigBean getConfigBean() {
        if (configBean == null) {
            String serverConfig = ConfigManager.getInstance().getServerConfig();
            if (TextUtils.isEmpty(serverConfig)) {
                return new ConfigBean();
            }
            configBean = GsonUtils.fromJson(serverConfig, ConfigBean.class);
            if (configBean == null) {
                return new ConfigBean();
            }
        }
        return configBean;
    }

    public CommonConfig getCommonConfig() {
        if (commonConfig == null) {
            String serverConfig = ConfigManager.getInstance().getNewsServerConfig();
            if (TextUtils.isEmpty(serverConfig)) {
                return new CommonConfig();
            }
            commonConfig = GsonUtils.fromJson(serverConfig, CommonConfig.class);

            if (commonConfig == null) {
                return new CommonConfig();
            }
        }
        return commonConfig;
    }

    private void setConfigBean(ConfigBean configBean) {
        this.configBean = configBean;
    }

    public void setCommonConfig(CommonConfig commonConfig) {
        this.commonConfig = commonConfig;
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            default:
                break;
        }
    }

}
