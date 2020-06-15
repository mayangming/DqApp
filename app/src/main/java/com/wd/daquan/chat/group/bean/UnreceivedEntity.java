package com.wd.daquan.chat.group.bean;


import com.wd.daquan.common.constant.RedpacketType;
import com.wd.daquan.model.interfaces.ISelect;

import java.util.List;

/**
 * @author: dukangkang
 * @date: 2018/8/28 11:55.
 * @description:
 *  未领取红包
 */
public class UnreceivedEntity {


    /**
     * info : 超过10分钟未领完红包在此显示
     * list : [{"group_id":2903,"create_uid":128,"nickName":"摸摸摸","headpic":"http://t.file.sy.qingic.com/upload/head_pic_8351530077175.png","type":1,"redpacket_id":3014,"signature":null,"greetings":"恭喜发财,大吉大利","redpacket_extra":[],"create_time":"2018-08-28 11:51:56","is_receive":0},{"group_id":2903,"create_uid":128,"nickName":"摸摸摸","headpic":"http://t.file.sy.qingic.com/upload/head_pic_8351530077175.png","type":2,"redpacket_id":2,"signature":"sy1a96ac6b56:28_201808","greetings":"恭喜发财,大吉大利","redpacket_extra":[],"create_time":"2018-08-28 11:51:23","is_receive":0},{"group_id":2903,"create_uid":519,"nickName":"dkk1","headpic":"http://t.file.sy.qingic.com/upload/head_pic_5771529750494.png","type":1,"redpacket_id":3013,"signature":null,"greetings":"恭喜发财,大吉大利","redpacket_extra":[],"create_time":"2018-08-28 11:51:17","is_receive":0},{"group_id":2903,"create_uid":519,"nickName":"dkk1","headpic":"http://t.file.sy.qingic.com/upload/head_pic_5771529750494.png","type":1,"redpacket_id":3012,"signature":null,"greetings":"恭喜发财,大吉大利","redpacket_extra":[],"create_time":"2018-08-28 11:50:57","is_receive":0}]
     */

    public String info;
    public List<PacketEntity> list;

    public static class PacketEntity implements ISelect {
        /**
         * group_id : 2903
         * create_uid : 128
         * nickName : 摸摸摸
         * headpic : http://t.file.sy.qingic.com/upload/head_pic_8351530077175.png
         * type : 1
         * redpacket_id : 3014
         * signature : null
         * greetings : 恭喜发财,大吉大利
         * redpacket_extra : []
         * create_time : 2018-08-28 11:51:56
         * is_receive : 0
         */
        public String group_id;
        public String create_uid;
        public String nickname;
        public String headpic;
        public String type; // 1:斗圈 2:支付宝 3:云红包
        public String redpacket_id;
        public String signature;
        public String greetings;
        public String create_time;
        public String is_receive;
        public List<ExtraEntity> redpacket_extra;
        public boolean isReceive() {
            return "1".equals(is_receive);
        }

        public String getTypeName() {
            if (RedpacketType.CHUINIU_REDPACKET.equals(type)) {
                return "斗圈红包";
            } else if (RedpacketType.ALIPAY_REDPACKET.equals(type)) {
                return "支付宝红包";
            } else if (RedpacketType.JRMF_REDPACKET.equals(type)) {
                return "云红包";
            }
            return "";
        }

        @Override
        public boolean isSelected() {
            return false;
        }

        @Override
        public void setSelected(boolean selected) {

        }
    }

    public static class ExtraEntity {
        public String nickname;
        public String uid;
    }
}
