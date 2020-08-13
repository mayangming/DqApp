package com.wd.daquan.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单的未读消息内容
 */
public class AreaUnReadSimpleBean implements Serializable{
    private int count = 1;
    private ArrayList<String> headPics = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<String> getHeadPics() {
        return headPics;
    }

    public void setHeadPics(ArrayList<String> headPics) {
        this.headPics = headPics;
    }
}