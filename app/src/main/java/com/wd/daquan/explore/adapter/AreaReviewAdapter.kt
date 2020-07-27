package com.wd.daquan.explore.adapter

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dq.im.type.ImType
import com.wd.daquan.R
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.dialog.DialogExploreComment
import com.wd.daquan.explore.fragment.ExploreAreaBottomFragment
import com.wd.daquan.explore.type.ExploreOperatorType
import com.wd.daquan.explore.type.ReviewCommentType
import com.wd.daquan.explore.viewholder.AreaReviewViewHolder
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.model.bean.FindUserDynamicDescBean
import com.wd.daquan.model.bean.UserDynamicCommentDataListBean
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.model.mgr.ModuleMgr

/**
 * 朋友圈动态评论
 */
class AreaReviewAdapter() : RecycleBaseAdapter<AreaReviewViewHolder>() {

    var dynamicsBean = FindUserDynamicDescBean() //该评论所处的朋友圈动态
    var delCommonListener: ((FindUserDynamicDescBean) -> Unit?) ?= null // 删除评论的监听
    private var exploreAreaCallBack : ExploreAreaCallBack?= null
    var dynamicCommentDataListBeanList: MutableList<UserDynamicCommentDataListBean> = ArrayList<UserDynamicCommentDataListBean>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    constructor(dynamicCommentDataListBeanList: MutableList<UserDynamicCommentDataListBean>) : this(){
        this.dynamicCommentDataListBeanList = dynamicCommentDataListBeanList
    }


    /**
     * 设置删除评论的监听
     */
    fun setOnDelCommonListener(delCommonListener : (FindUserDynamicDescBean) -> Unit){
        this.delCommonListener = delCommonListener
    }

    override fun getItemCount() = dynamicCommentDataListBeanList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaReviewViewHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_area_review,parent,false)
        return AreaReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: AreaReviewViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val dynamicCommentDataListBean = dynamicCommentDataListBeanList[position]
        val type = dynamicCommentDataListBean.type //评论类型
        if (type == ReviewCommentType.COMMENT.type){//评论
            initSpannableComment(holder.itemAreaReviewContent,dynamicCommentDataListBean)
        }else{
            initSpannableReview(holder.itemAreaReviewContent,dynamicCommentDataListBean)
        }
        holder.itemAreaReviewContent.setOnLongClickListener {
            if(dynamicCommentDataListBean.userId == ModuleMgr.getCenterMgr().uid){
                dialogExploreComment(dynamicCommentDataListBean)
            }
            true
        }
    }

    private fun initSpannableComment(text: TextView,dynamicCommentDataListBean: UserDynamicCommentDataListBean = UserDynamicCommentDataListBean()){
        val content = "${dynamicCommentDataListBean.userNick} : ${dynamicCommentDataListBean.desc}"
        val startIndex1 = content.indexOf(dynamicCommentDataListBean.userNick)
        val endIndex1 = content.indexOf(dynamicCommentDataListBean.userNick) + dynamicCommentDataListBean.userNick.length
        val color = text.resources.getColor(R.color.color_49649D)
        val spannable = SpannableStringBuilder(content)
        val colorSpan = ForegroundColorSpan(color)
        val colorSpan2 = ForegroundColorSpan(text.resources.getColor(R.color.app_txt_666666))//内容黑色
        spannable.setSpan(CustomClickAble(dynamicCommentDataListBean.userId,dynamicCommentDataListBean),startIndex1,endIndex1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(CustomClickAble("",dynamicCommentDataListBean),endIndex1,content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(colorSpan, startIndex1,endIndex1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(colorSpan2, endIndex1,content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.text = spannable
        text.movementMethod = LinkMovementMethod.getInstance() //加上这句话才有效果
        text.highlightColor = ContextCompat.getColor(text.context,R.color.transparent) //去掉点击后的背景颜色为透明
    }

    private fun initSpannableReview(text: TextView,dynamicCommentDataListBean: UserDynamicCommentDataListBean = UserDynamicCommentDataListBean()){
        val content = "${dynamicCommentDataListBean.userNick} 回复 ${dynamicCommentDataListBean.friendNick}: ${dynamicCommentDataListBean.desc}"
        val startIndex1 = content.indexOf(dynamicCommentDataListBean.userNick)
        val endIndex1 = startIndex1 + dynamicCommentDataListBean.userNick.length
        val startIndex2 = content.indexOf("回复 ")+"回复 ".length
        val endIndex2 = startIndex2 + dynamicCommentDataListBean.friendNick.length
        val color = text.resources.getColor(R.color.color_49649D)
        val spannable = SpannableStringBuilder(content)
        val colorSpan = ForegroundColorSpan(color)
        val colorSpan1 = ForegroundColorSpan(color)
        val colorSpan2 = ForegroundColorSpan(text.resources.getColor(R.color.app_txt_666666))//内容黑色
        spannable.setSpan(CustomClickAble(dynamicCommentDataListBean.userId,dynamicCommentDataListBean),startIndex1,endIndex1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(CustomClickAble(dynamicCommentDataListBean.friendId,dynamicCommentDataListBean),startIndex2,endIndex2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(CustomClickAble("",dynamicCommentDataListBean),endIndex2,content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(colorSpan, startIndex1,endIndex1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(colorSpan1, startIndex2,endIndex2, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        spannable.setSpan(colorSpan2, endIndex2,content.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        text.text = spannable
        text.movementMethod = LinkMovementMethod.getInstance() //加上这句话才有效果
        text.highlightColor = ContextCompat.getColor(text.context,R.color.transparent) //去掉点击后的背景颜色为透明
    }

    /**
     * 回复评论输入框
     */
    private fun exploreAreaBottomFragment(dynamicCommentDataListBean :UserDynamicCommentDataListBean){
        val bottomReviewFragment = ExploreAreaBottomFragment()
        val act = context as FragmentActivity
        val bundle = Bundle()
        bundle.putSerializable(ExploreAreaBottomFragment.DYNAMIC_BEAN,dynamicsBean)
        bundle.putSerializable(ExploreAreaBottomFragment.DYNAMIC_COMMENT_BEAN,dynamicCommentDataListBean)
        bottomReviewFragment.arguments = bundle
        bottomReviewFragment.show(act.supportFragmentManager,"reviewBottom")
        bottomReviewFragment.setOnExploreAreaCallBack(ExploreReviewCommentResultCallBack())
    }

    /**
     * 评论操作输入框
     */
    private fun dialogExploreComment(dynamicCommentDataListBean :UserDynamicCommentDataListBean){
        val act = context as FragmentActivity
        val dialogExploreComment = DialogExploreComment()
        val bundle = Bundle()
        bundle.putString(DialogExploreComment.ACTION_COMMENT_ID,dynamicCommentDataListBean.commentId.toString())
        bundle.putString(DialogExploreComment.ACTION_DYNAMIC_ID,dynamicCommentDataListBean.dynamicId.toString())
        bundle.putInt(DialogExploreComment.ACTION_TYPE, ExploreOperatorType.TYPE_COMMENT.type)
        dialogExploreComment.arguments = bundle
        dialogExploreComment.show(act.supportFragmentManager,"delComment")
        dialogExploreComment.setOnDelCommentListener {
            delCommonListener?.invoke(it)
        }
    }

    /**
     * 回复评论
     */
    inner class ExploreReviewCommentResultCallBack : ExploreAreaBottomFragment.ExploreAreaCallBack{
        override fun onSuccess(dynamicDescBean: FindUserDynamicDescBean) {
            exploreAreaCallBack?.onSuccess(dynamicDescBean)
        }

        override fun onFail() {
            exploreAreaCallBack?.onFail()
        }
    }

    /**
     * 自定义点击事件
     */
    inner class CustomClickAble(val uid :String, private val dynamicCommentDataListBean :UserDynamicCommentDataListBean) : ClickableSpan(){

        override fun onClick(widget: View) {
//            DqToast.showCenterShort("用户Id:${uid}")
            if (!TextUtils.isEmpty(uid)){
                NavUtils.gotoUserInfoActivity(context, uid, ImType.P2P.value)
            }else{
                exploreAreaBottomFragment(dynamicCommentDataListBean)
            }
        }


        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ds.linkColor
            ds.isUnderlineText = false   //去除超链接的下划线
        }
    }

    fun setOnExploreAreaCallBack(exploreAreaCallBack : ExploreAreaCallBack){
        this.exploreAreaCallBack = exploreAreaCallBack
    }

    /**
     * 底部评论输入框回调
     */
    interface ExploreAreaCallBack{
        fun onSuccess(dynamicDescBean: FindUserDynamicDescBean)
        fun onFail()
    }
}