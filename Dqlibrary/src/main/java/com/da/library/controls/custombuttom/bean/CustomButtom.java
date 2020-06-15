package com.da.library.controls.custombuttom.bean;

/**
 * 弹出dialog的数据源
 * Created by Kind on 2019/3/22.
 */
public class CustomButtom {

    public int id;  //id
    public String text; //名称
    public int text_color;//名称颜色
    public int icon;//图片

    public CustomButtom() {
        super();
    }

    public CustomButtom(int id, String text) {
        super();
        this.id = id;
        this.text = text;
        this.text_color = 0;
    }

    public CustomButtom(int id, String text, int text_color) {
        super();
        this.id = id;
        this.text = text;
        this.text_color = text_color;
    }

    public CustomButtom(String text, int id, int icon) {
        this.text = text;
        this.id = id;
        this.icon = icon;
    }
}
