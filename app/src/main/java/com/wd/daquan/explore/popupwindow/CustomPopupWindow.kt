package com.wd.daquan.explore.popupwindow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import com.wd.daquan.R
import com.wd.daquan.model.bean.FindUserDynamicDescBean
import com.wd.daquan.model.bean.UserDynamicLikeDataListBean
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.mgr.ModuleMgr

/**
 * 自定义弹框
 */
class CustomPopupWindow(private val layoutInflater: LayoutInflater) {
    var userDynamicDescBean: FindUserDynamicDescBean = FindUserDynamicDescBean()
    set(value) {
        field = value
        initLikeContent(value)
    }
    var index: Int = -1
    var isLiked = false //是否已经点过赞，false 没有
    var dynamicLike : UserDynamicLikeDataListBean ?= null
    lateinit var bubbleDingTv: TextView
    private var customPopupWindowCallBack :CustomPopupWindowCallBack ?= null
    private val popupWindow : PopupWindow by lazy<PopupWindow> {
        showReviewPopupWindow()
    }

    private fun showReviewPopupWindow() = PopupWindow().apply {
        val container = layoutInflater.inflate(R.layout.item_bubble_layout,null,false)
        val bubbleDingContainer = container.findViewById<View>(R.id.bubble_ding_container)
        val bubbleReviewContainer = container.findViewById<View>(R.id.bubble_review_container)
        bubbleDingTv = container.findViewById<TextView>(R.id.bubble_ding_tv)
        contentView = container
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isOutsideTouchable = true
        bubbleDingContainer.setOnClickListener {
            if(isLiked){
                dynamicLike?.let { it1 -> customPopupWindowCallBack?.delDingCallBack(it1) }
            }else{
                customPopupWindowCallBack?.dingCallBack(userDynamicDescBean,index)
            }

        }
        bubbleReviewContainer.setOnClickListener {
            customPopupWindowCallBack?.reviewCallBack(userDynamicDescBean,index)
        }
        isFocusable = true;//要先让popupwindow获得焦点，才能正确获取popupwindow的状态
    }
    fun showAsDropDown(anchor :View, xoff :Int, yoff : Int){
        popupWindow.contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popW = popupWindow.contentView.measuredWidth
        val resultW = - xoff - popW
        DqLog.e("YM---->结果宽度:${resultW}  弹窗宽度:${popW} 偏移值:${xoff}")
        popupWindow.showAsDropDown(anchor,resultW,yoff)
    }

    fun isShow() = popupWindow.isShowing

    fun dismiss(){
        popupWindow.dismiss()
    }

    /**
     * 初始化赞的内容
     */
    private fun initLikeContent(userDynamicDescBean: FindUserDynamicDescBean ){
        dynamicLike = userDynamicDescBean.userDynamicLikeDataList.find {
            it.userId == ModuleMgr.getCenterMgr().uid
        }
       if (null != dynamicLike){
           bubbleDingTv.text = "取消"
           isLiked = true
       }else{
           bubbleDingTv.text = "赞"
           isLiked = false
       }

    }

    private fun getWindowW(context :Context): Int{
        val wm: WindowManager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width: Int = wm.defaultDisplay.width
        val height: Int = wm.defaultDisplay.height
        return width;
    }

    fun setOnCustomPopupWindowCallBack(customPopupWindowCallBack :CustomPopupWindowCallBack){
        this.customPopupWindowCallBack = customPopupWindowCallBack
    }

    interface CustomPopupWindowCallBack{
        fun dingCallBack(dynamicDescBean: FindUserDynamicDescBean, index :Int)
        fun delDingCallBack(dynamicLike : UserDynamicLikeDataListBean)//取消点赞
        fun reviewCallBack(dynamicDescBean: FindUserDynamicDescBean, index :Int)
    }

}