package com.wd.daquan.imui.fragment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 基础Fragment
 */
public class BaseFragment extends Fragment{
    protected void initVerticalRecycleView(RecyclerView recyclerView){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * 网格布局
     * @param recyclerView
     */
    protected void initGridRecycleView(RecyclerView recyclerView){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(),4,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * 选择图片
     */
    protected void selectPhoto(){
//        Matisse.from(this)
//                .choose(MimeType.ofImage())
//                .countable(true)
//                .maxSelectable(1)
//                .capture(true)//拍照功能暂时取消
//                .captureStrategy(
//                        new CaptureStrategy(true, BuildConfig.APPLICATION_ID+".fileprovider", "capture")
//                )
//                .maxSelectable(1)
//                .gridExpectedSize(
//                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size)
//                )
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//                .thumbnailScale(0.85f)
//                .imageEngine(new GlideEngine())
//                .showSingleMediaType(true)
//                .originalEnable(true)
//                .maxOriginalSize(10)
//                .autoHideToolbarOnSingleTap(true)
//                .forResult(IntentCode.REQUEST_CODE_CHOOSE);
    }

}