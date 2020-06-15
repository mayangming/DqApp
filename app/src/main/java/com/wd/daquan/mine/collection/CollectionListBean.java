package com.wd.daquan.mine.collection;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 2018/7/3.
 */

public class CollectionListBean implements Serializable {
    private static final long serialVersionUID = -1L;
    //上传
    public List<CollectionUploadMsgBean> list;
    public String uri;


}
