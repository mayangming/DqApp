package com.wd.daquan.contacts.bean;

import com.wd.daquan.model.interfaces.ISelect;

/**
 * @Author: 方志
 * @Time: 2018/9/17 13:39
 * @Description: 手机联系人数据
 */
public class MobileContactBean implements ISelect {


    /**
     * uid : 1
     * nickName : u18122874
     * phone : 18612030336
     * headpic : http://api.sy.lemoncc.com/headpic_default.jpg
     * whether_friend : 0
     * userName : 大侄子
     */

    public String uid;
    public String nickname;
    public String phone;
    public String headpic;
    public String whether_friend;
    public String userName;
    public String pinYin;
    private boolean isSelect;

    @Override
    public boolean isSelected() {
        return isSelect;
    }

    @Override
    public void setSelected(boolean selected) {
        this.isSelect = selected;
    }
}
