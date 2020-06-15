package com.wd.daquan.chat.group.bean;

import com.wd.daquan.model.interfaces.ISelect;
import java.io.Serializable;

/**
 * @author: dukangkang
 * @date: 2018/9/19 20:31.
 * @description: todo ...
 */
public class TeamListEntity implements Serializable, ISelect {
    public int total_num;
    public String group_id;
    public String group_name;
    public String announcement;
    public String group_pic;

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void setSelected(boolean selected) {

    }
}
