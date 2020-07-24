package com.wd.daquan.explore.itemdecoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * 间距
 */
class SpacesItemDecoration(val space: Int, val count: Int = 3) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        //不是第一个的格子都设一个左边和底部的间距
        outRect.right = space
        outRect.bottom = space
    }
}