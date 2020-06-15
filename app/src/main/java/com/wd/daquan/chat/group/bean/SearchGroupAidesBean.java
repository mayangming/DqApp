package com.wd.daquan.chat.group.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 方志
 * @Time: 2019/1/7 11:16
 * @Description:
 */
public class SearchGroupAidesBean {


    /**
     * total : 1
     * plugin_id : PG42nsndfdt43
     * list : [{"plugin_id":"PG42nsndfdt43","plugin_name":"小助手","logo":"","description":"我是简介","plugin_type":1,"update_time":1546594812,"company_name":"深圳市天天爱科技有限公司"}]
     * disclaimer : 本服务由#company_name#提供。相关服务和责任将由第三方承担。如有问题请咨询该公司客服。
     */

    public int total;
    public String plugin_id;
    public String disclaimer;
    public List<PluginBean> list;

    public int getTotal() {
        return total;
    }

    public String getPlugin_id() {
        return plugin_id;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public List<PluginBean> getList() {
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

}
