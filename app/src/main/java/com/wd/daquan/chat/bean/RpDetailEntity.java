package com.wd.daquan.chat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: dukangkang
 * @date: 2018/9/18 11:20.
 * @description: todo ...
 */
public class RpDetailEntity implements Serializable {
    /**
     * list : [{"receive_uid":101,"amount":"0.01","receive_time":"2018-09-17 19:42:22","uid":101,"nickName":"斗圈官方团队","headpic":"http://toss.meetsn.com/upload/5817bb0b434e6eb7d2d45d3b551e8400.jpeg"}]
     * received_num : 1
     * create_uid : 101
     * num : 1
     * amount : 0.01
     * received_amount : 0.01
     * type : 2
     * greetings : 恭喜发财 ,大吉大利
     * group_id : 1395635834
     * status : 2
     * nickName : 斗圈官方团队
     * headpic : http://toss.meetsn.com/upload/5817bb0b434e6eb7d2d45d3b551e8400.jpeg
     * self_amount : 0.01
     * myaliuser : 181****7428
     */

    public String received_num;
    public String create_uid;
    public String num;
    public String amount;
    public String received_amount;
    public String type;
    public String greetings;
    public String group_id;
    public String status;
    public String nickname;
    public String headpic;
    public String self_amount;
    public String myaliuser;
    public List<ListEntity> list;
    public static class ListEntity {
        /**
         * receive_uid : 101
         * amount : 0.01
         * receive_time : 2018-09-17 19:42:22
         * uid : 101
         * nickName : 斗圈官方团队
         * headpic : http://toss.meetsn.com/upload/5817bb0b434e6eb7d2d45d3b551e8400.jpeg
         */

        public String receive_uid;
        public String amount;
        public String receive_time;
        public String uid;
        public String nickname;
        public String headpic;
    }

}
