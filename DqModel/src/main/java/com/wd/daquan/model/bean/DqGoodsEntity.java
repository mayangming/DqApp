package com.wd.daquan.model.bean;

import java.io.Serializable;

/**
 * 商品详情
 */
public class DqGoodsEntity implements Serializable {

    /**
     * id : 712924267364220928
     * commoditiesPrice : 100
     * commoditiesNmae : 测试任务
     * commmoditiesType : 0
     * commoditiesPic :
     * commoditiesCreateTime : 1599723195415
     * oncePrice : 100
     * onceTime : 100
     */

    private long id;
    private int commoditiesPrice;
    private String commoditiesNmae;
    private int commmoditiesType;
    private String commoditiesPic;
    private long commoditiesCreateTime;
    private int oncePrice;
    private int onceTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCommoditiesPrice() {
        return commoditiesPrice;
    }

    public void setCommoditiesPrice(int commoditiesPrice) {
        this.commoditiesPrice = commoditiesPrice;
    }

    public String getCommoditiesNmae() {
        return commoditiesNmae;
    }

    public void setCommoditiesNmae(String commoditiesNmae) {
        this.commoditiesNmae = commoditiesNmae;
    }

    public int getCommmoditiesType() {
        return commmoditiesType;
    }

    public void setCommmoditiesType(int commmoditiesType) {
        this.commmoditiesType = commmoditiesType;
    }

    public String getCommoditiesPic() {
        return commoditiesPic;
    }

    public void setCommoditiesPic(String commoditiesPic) {
        this.commoditiesPic = commoditiesPic;
    }

    public long getCommoditiesCreateTime() {
        return commoditiesCreateTime;
    }

    public void setCommoditiesCreateTime(long commoditiesCreateTime) {
        this.commoditiesCreateTime = commoditiesCreateTime;
    }

    public int getOncePrice() {
        return oncePrice;
    }

    public void setOncePrice(int oncePrice) {
        this.oncePrice = oncePrice;
    }

    public int getOnceTime() {
        return onceTime;
    }

    public void setOnceTime(int onceTime) {
        this.onceTime = onceTime;
    }
}
