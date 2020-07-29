package com.wd.daquan.model.bean;

/**
 * 滑动的图形验证码
 */
public class CaptchaBean {
    private String smallImage;
    private String bigImage;
    private int oriImageWidth;//源图
    private int oriImageHeight;
    private int templateWidth;//滑块
    private int templateHeight;
    private int yHeight;

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getBigImage() {
        return bigImage;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }

    public int getyHeight() {
        return yHeight;
    }

    public void setyHeight(int yHeight) {
        this.yHeight = yHeight;
    }

    public int getOriImageWidth() {
        return oriImageWidth;
    }

    public void setOriImageWidth(int oriImageWidth) {
        this.oriImageWidth = oriImageWidth;
    }

    public int getOriImageHeight() {
        return oriImageHeight;
    }

    public void setOriImageHeight(int oriImageHeight) {
        this.oriImageHeight = oriImageHeight;
    }

    public int getTemplateWidth() {
        return templateWidth;
    }

    public void setTemplateWidth(int templateWidth) {
        this.templateWidth = templateWidth;
    }

    public int getTemplateHeight() {
        return templateHeight;
    }

    public void setTemplateHeight(int templateHeight) {
        this.templateHeight = templateHeight;
    }
}