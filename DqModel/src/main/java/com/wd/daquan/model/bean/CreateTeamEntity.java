package com.wd.daquan.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 */
public class CreateTeamEntity implements Serializable {

    /**
     * group_id : 1399204438
     * group_pic : http://toss.meetsn.com/upload/5bf1062e9203373a7bcebeeaeb71e58f.jpeg
     * group_name : 测试一下
     */
    public String group_id;
    public String group_pic;
    public String group_name;
    /**
     * 需要认证的人员集合
     */
    public List<RefuseInvite> refuse;

    public class RefuseInvite {
        public String id;
        public String nickName;
    }

    public boolean hasRefuse() {
        if (refuse == null) {
            return false;
        }

        return refuse.size() > 0;
    }

    public String getRefuseName() {
        if (hasRefuse()) {
            return refuse.get(0).nickName;
        }
        return "";
    }

    public int getRefuseCount() {
        if (hasRefuse()) {
            return refuse.size();
        }
        return 0;
    }
}
