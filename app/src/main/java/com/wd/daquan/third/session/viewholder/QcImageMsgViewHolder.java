//package com.wd.daquan.third.session.viewholder;
//
//import com.wd.daquan.R;
//import com.wd.daquan.common.utils.NavUtils;
//import com.da.library.tools.Utils;
//import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderThumbBase;
//import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
//
///**
// * @author: dukangkang
// * @date: 2018/9/10 14:17.
// * @description: todo ...
// */
//public class QcImageMsgViewHolder extends MsgViewHolderThumbBase {
//
//
//    public QcImageMsgViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
//        super(adapter);
//    }
//
//    @Override
//    protected int getContentResId() {
//        return R.layout.image_viewholder_item;
//    }
//
//    @Override
//    protected void onItemClick() {
//        if (Utils.isFastDoubleClick(500)) {
//            return;
//        }
//        NavUtils.gotoWatchImageAndVideoActivity(context, message);
//    }
//
//    @Override
//    protected String thumbFromSourceFile(String path) {
//        return path;
//    }
//
//    @Override
//    protected int rightBackground() {
//        return R.color.transparent;
//    }
//
//    @Override
//    protected int leftBackground() {
//        return R.color.transparent;
//    }
//}
