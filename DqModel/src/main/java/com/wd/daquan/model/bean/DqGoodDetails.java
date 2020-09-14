package com.wd.daquan.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商城详情
 */
public class DqGoodDetails implements Serializable {

    /**
     * 商品列表
     */
    private List<DqGoodsEntity> list = new ArrayList<>();
    /**
     * 滚动提示内容
     */
    private List<DqGoodChangeEntity> changeList = new ArrayList<>();

    /**
     * 斗币数额
     */
    private int dbMoney = 0;

    public List<DqGoodsEntity> getList() {
        return list;
    }

    public void setList(List<DqGoodsEntity> list) {
        this.list = list;
    }

    public List<DqGoodChangeEntity> getChangeList() {
        return changeList;
    }

    public void setChangeList(List<DqGoodChangeEntity> changeList) {
        this.changeList = changeList;
    }

    public int getDbMoney() {
        return dbMoney;
    }

    public void setDbMoney(int dbMoney) {
        this.dbMoney = dbMoney;
    }
}