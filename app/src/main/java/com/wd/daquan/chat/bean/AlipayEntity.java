package com.wd.daquan.chat.bean;

/**
 * @author: dukangkang
 * @date: 2018/9/13 14:58.
 * @description: todo ...
 */
public class AlipayEntity {

    /**
     * out_order_no : 51920180913145713164249
     * out_request_no : 5192018091314571363015
     * order_title : 恭喜发财,大吉大利
     * amount : 0.01
     * pay_timeout : 1h
     * redpacket_id : 1
     * signature : sy506124ea2c:19_201809
     * pid : 2088921558152273
     * appId : 2018010901730693
     * callback : http://t.api.sy.qingic.com/aliRedpacket/aliPayCallback
     * orderinfo : app_id=2018010901730693&biz_content=%7B%22out_order_no%22%3A%2251920180913145713164249%22%2C%22out_request_no%22%3A%225192018091314571363015%22%2C%22order_title%22%3A%22%5Cu606d%5Cu559c%5Cu53d1%5Cu8d22%2C%5Cu5927%5Cu5409%5Cu5927%5Cu5229%22%2C%22amount%22%3A%220.01%22%7D&charset=utf-8&method=alipay.fund.coupon.order.app.pay&notify_url=http%3A%2F%2Ft.api.sy.qingic.com%2FaliRedpacket%2FaliPayCallback&sign=YvOYSfLKbXLQbQJj0EhFvcLmQVGNncwN60651rDxiGdAPSvZUaCP%2BkDjfGvnePAVNEr%2FG6CKgL1wIzbQdc%2Bqgn8mNPyjLMT6h21NqM1X3ZXt0lpONerv7tsR8af2cXE2Gr8mv2b1e9jdj2BPk86BlXhX5ew3G9RSzvmGLo6ILTg6ErYerHDHY9cvVq7LH32gOdpcCxFXz5HK%2F1fdF5rmbEUunPZ3lY0ZxFHsNSYIe64l07qqqU6uDCVBCmUXor%2F40vm26Cjc0EF3DwP0TtgYPzWK6EwhmjVLBJsi11xCNr7cU%2FNqYz%2Bc2zkQJ49lXDvNNl6LPUZ9T593TV0tc5v1aw%3D%3D&sign_type=RSA2&timestamp=2018-09-13+14%3A57%3A13&version=1.0
     * api_return_data : {"redpacket_id":1,"blessing":"恭喜发财,大吉大利","receive_id":"","receive_name":null,"receive_headpic":"http://t.file.sy.qingic.com","create_uid":"519","create_nickname":null,"create_headpic":"http://t.file.sy.qingic.com","redpacket_extra":"","create":{"uid":null,"headpic":"http://t.file.sy.qingic.com","nickName":null},"receive":{"uid":null,"headpic":"http://t.file.sy.qingic.com","nickName":null},"type":"2"}
     */

    public String out_order_no;
    public String out_request_no;
    public String order_title;
    public String amount;
    public String pay_timeout;
    public String redpacket_id;
    public String signature;
    public String pid;
    public String appid;
    public String callback;
    public String orderinfo;
//    public ApiReturnDataEntity api_return_data;

//    public static class ApiReturnDataEntity {
//        /**
//         * redpacket_id : 1
//         * blessing : 恭喜发财,大吉大利
//         * receive_id :
//         * receive_name : null
//         * receive_headpic : http://t.file.sy.qingic.com
//         * create_uid : 519
//         * create_nickname : null
//         * create_headpic : http://t.file.sy.qingic.com
//         * redpacket_extra :
//         * create : {"uid":null,"headpic":"http://t.file.sy.qingic.com","nickName":null}
//         * receive : {"uid":null,"headpic":"http://t.file.sy.qingic.com","nickName":null}
//         * type : 2
//         */
//
//        public String redpacket_id;
//        public String blessing;
//        public String receive_id;
//        public String receive_name;
//        public String receive_headpic;
//        public String create_uid;
//        public String create_nickname;
//        public String create_headpic;
//        public String redpacket_extra;
//        public CreateEntity create;
//        public ReceiveEntity receive;
//        public String type;
//
//        public static class CreateEntity {
//            /**
//             * uid : null
//             * headpic : http://t.file.sy.qingic.com
//             * nickName : null
//             */
//
//            public String uid;
//            public String headpic;
//            public String nickName;
//        }
//
//        public static class ReceiveEntity {
//            /**
//             * uid : null
//             * headpic : http://t.file.sy.qingic.com
//             * nickName : null
//             */
//
//            public String uid;
//            public String headpic;
//            public String nickName;
//
//        }
//    }
}
