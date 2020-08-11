package com.wd.daquan.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wd.daquan.model.log.DqLog

/**
 * 自定义基础的RecycleView
 * 内部实现了空视图
 */
class BaseRecycleView : RecyclerView{
    var emptyView: View ?= null
        set(value) {
            field = value
//            (this.rootView as ViewGroup).addView(value) //加入主界面布局
            val rootViewGroup = this.parent as ViewGroup
            rootViewGroup.addView(value)
        }

    /**
     * 数据监测中心
     */
   private val adapterDataObserver = object :AdapterDataObserver(){
        override fun onChanged() {
            adapter?.run {
                if(itemCount == 0){
                    emptyView?.visibility = View.VISIBLE
                    this@BaseRecycleView.visibility = GONE;
                }else{
                    emptyView?.visibility = View.GONE
                    this@BaseRecycleView.visibility = VISIBLE
                }
            }

        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            onChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            onChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            onChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            onChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            onChanged()
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        if (null == adapter) return
        adapter.registerAdapterDataObserver(adapterDataObserver);
        adapterDataObserver.onChanged();
    }
}