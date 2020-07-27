package com.wd.daquan.explore.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.wd.daquan.model.log.DqLog

class DividerItemDecoration (context: Context) : RecyclerView.ItemDecoration(){
    private var mOrientation: Int? = null
    private var dividerLine: Drawable? = null

    init {
        val typedArray = context.obtainStyledAttributes(ATTRS)
        dividerLine = typedArray.getDrawable(0)
        typedArray.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        drawHorizontalLine(c, parent, state)
        drawVerticalLine(c, parent, state)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val spanCount = getSpanCount(parent)
        val childCount = parent.adapter?.itemCount ?: 0
        val itemPosition = ((view.layoutParams)as RecyclerView.LayoutParams).viewLayoutPosition
        if (isLastRow(parent, itemPosition, spanCount, childCount)){
            outRect.set(0, 0, dividerLine!!.intrinsicWidth, 0)
        }else if (isLastColum(parent, itemPosition, spanCount, childCount)){
            outRect.set(0, 0, 0, dividerLine!!.intrinsicHeight)
        }else{
            outRect.set(0, 0, dividerLine!!.intrinsicWidth, dividerLine!!.intrinsicHeight)
        }

    }

    /**
     * 画竖线
     */
    private fun  drawVerticalLine(c: Canvas, parent: RecyclerView?, state: RecyclerView.State?) {
        for (i in 0..(parent!!.childCount - 1)){
            val child: View = parent.getChildAt(i)

            //获取child布局参数
            val params: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + dividerLine!!.intrinsicWidth
            val top = child.top - params.topMargin
            val bottom = child.bottom + params.bottomMargin
            dividerLine!!.setBounds(left, top, right, bottom)
            dividerLine!!.draw(c)
        }
    }

    /**
     * 画横线
     */
    private fun  drawHorizontalLine(c: Canvas, parent: RecyclerView?, state: RecyclerView.State?) {
        for (i in 0..(parent!!.childCount - 1)){
            val child: View = parent.getChildAt(i)

            val params: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
            val left = child.left - params.leftMargin
            val right = child.right + params.rightMargin+ dividerLine!!.intrinsicWidth
            val top = child.bottom + params.bottomMargin
            val bottom = top + 1
            dividerLine!!.setBounds(left, top, right, bottom)
            dividerLine!!.draw(c)
        }
    }

    /**
     * 获取列数
     */
    private fun getSpanCount(parent: RecyclerView?): Int{
        var spanCount: Int = -1
        val layoutManager: RecyclerView.LayoutManager? = parent?.layoutManager
        if (layoutManager is GridLayoutManager){
            spanCount = layoutManager.spanCount
        }else if(layoutManager is StaggeredGridLayoutManager){
            spanCount = layoutManager.spanCount
        }
        return spanCount
    }

    /**
     * 判定是否为最后一列
     */
    private fun isLastColum(parent: RecyclerView?, pos: Int, spanCount: Int, childCount: Int): Boolean{
        val layoutManager: RecyclerView.LayoutManager? = parent?.layoutManager
        if (layoutManager is GridLayoutManager){
            if ((pos + 1)% spanCount == 0) {//如果是最后一列，则不在绘制右边
                return true
            }
        }else if(layoutManager is StaggeredGridLayoutManager){
            val orientation = layoutManager.orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL){
                if ((pos + 1)% spanCount == 0) {//如果是最后一列，则不在绘制右边
                    return true
                }
            }else{
                val childNum = childCount - childCount % spanCount
                if (pos >= childNum) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 判定是否为最后一行
     */
    private fun isLastRow(parent: RecyclerView?, pos: Int, spanCount: Int, childCount: Int): Boolean{
        val layoutManager: RecyclerView.LayoutManager? = parent?.layoutManager
        if (layoutManager is GridLayoutManager){
            val childNum = if(childCount % spanCount == 0) childCount - spanCount else childCount - childCount % spanCount
            if (pos >= childNum) {//如果是最后一行，则不在绘制底边
                return true
            }
        }else if(layoutManager is StaggeredGridLayoutManager){
            val orientation = layoutManager.orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL){//纵向滚动
                val childNum = childCount - childCount % spanCount
                if (pos >= childNum) {//如果是最后一行，则不在绘制底边
                    return true
                }
            }else{//横向滚动
                if ((pos + 1) % spanCount == 0) {
                    return true
                }
            }
        }
        return false
    }

    companion object{
        private var ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}