package com.wd.daquan.mine.bean;


import java.io.Serializable;
import java.util.List;

/**
 * <功能详细描述>
 */
public class AlipayRedDetailsEntity implements Serializable {
    private static final long serialVersionUID = 3330369656982792155L;
    public List<AlipayRedDetailsItemEntity> list;
    public String num;
    public String amount;
    public String received_amount;
    public String received_num;
    public String self_amount;
    public String nickname;
    public String headpic;
    public String greetings;
    public String create_uid;
    public String group_id;
    public String myaliuser;
    public String sessionType;

}
