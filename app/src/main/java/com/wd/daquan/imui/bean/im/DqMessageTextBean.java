package com.wd.daquan.imui.bean.im;

/**
 * 斗圈项目中文本消息内容
 */
public class DqMessageTextBean extends DqMessageBaseContent{
    private String searchableContent = "";

    public String getSearchableContent() {
        return searchableContent;
    }

    public void setSearchableContent(String searchableContent) {
        this.searchableContent = searchableContent;
    }
}