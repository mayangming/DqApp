//package com.netease.nim.uikit.business.session.viewholder;
//
//import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
//import com.wd.daquan.R;
//import com.wd.daquan.common.utils.NavUtils;
//
///**
// * Created by zhoujianghua on 2015/8/4.
// */
//public class MsgViewHolderPicture extends MsgViewHolderThumbBase {
//
//    public MsgViewHolderPicture(BaseMultiItemFetchLoadAdapter adapter) {
//        super(adapter);
//    }
//
//    @Override
//    protected int getContentResId() {
//        return com.netease.nim.uikit.R.layout.nim_message_item_picture;
//    }
//
//    @Override
//    protected void onItemClick() {
//        NavUtils.gotoWatchImageAndVideoActivity(context, message);
//    }
//
//    @Override
//    protected String thumbFromSourceFile(String path) {
//        return path;
//    }
//
//    @Override
//    protected int leftBackground() {
//        return R.color.transparent;
//    }
//
//    @Override
//    protected int rightBackground() {
//        return R.color.transparent;
//    }
//}
