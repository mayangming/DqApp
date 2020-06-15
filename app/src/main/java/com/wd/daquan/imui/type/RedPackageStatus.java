package com.wd.daquan.imui.type;

/**
 * 红包状态
 */
public enum RedPackageStatus {
    RED_PACKAGE_UN_KNOWN("未识别类型","-1"),
    RED_PACKAGE_UN_RECEIVE("未领取","01"),
    RED_PACKAGE_RECEIVED("已领取","02"),
    RED_PACKAGE_RECEIVE_END("已领完","03"),
    RED_PACKAGE_BACK("已退回","04"),
    RED_PACKAGE_EXPIRED("已过期","05"),
    ;

    public String description;
    public String status;
    /**
     * @param description 红包描述信息
     * @param status 红包状态
     */
    RedPackageStatus(String description,String status){
        this.description = description;
        this.status = status;
    }

    public static String getRedPackageStatusDescription(String status){
        String description = "未定义红包类型";
        for (RedPackageStatus redPackageStatus : values()){
            if (redPackageStatus.status.equals(status)){
                description = redPackageStatus.description;
                break;
            }
        }
        return description;
    }

    public static RedPackageStatus getRedPackageStatus(String status){
        RedPackageStatus result = RedPackageStatus.RED_PACKAGE_UN_KNOWN;
        for (RedPackageStatus redPackageStatus : values()){
            if (redPackageStatus.status.equals(status)){
                result = redPackageStatus;
                break;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "RedPackageStatus{" +
                "description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}