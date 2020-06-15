package com.wd.daquan.mine.bean;

import java.io.Serializable;
import java.util.List;


public class AlipayRedListEntity implements Serializable {
    private static final long serialVersionUID = -1L;
    public int is_all_data;
    public int num;
    public String sum;
    public String nickname;
    public String payee_logon_id;
    public List<ListBean> list;
    public static class ListBean implements Serializable{
        private static final long serialVersionUID = -1L;
        /**
         * greetings : 恭喜发财, 大吉大利
         * type : 1
         * style : 1
         * amount : 0.01
         * recive_time : 2017-12-16 03:49:53
         * redpacket_id : 1
         * nickName : 绘画1
         * signature : sywew232ads:1_201804
         */

        public String greetings;
        public String type;//代表红包类型 个人是1 群组是2
        public String style;
        public String amount;
        public String receive_time;
        public String create_time;
        public String redpacket_id;
        public String nickname;
        public String signature;
        public String sessionType;
    }
}
