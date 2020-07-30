package com.wd.daquan.explore.adapter

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.dq.im.type.ImType
import com.netease.nim.uikit.common.util.sys.TimeUtil
import com.wd.daquan.R
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.dialog.DialogExploreComment
import com.wd.daquan.explore.fragment.ExploreAreaBottomFragment
import com.wd.daquan.explore.popupwindow.CustomPopupWindow
import com.wd.daquan.explore.type.ExploreOperatorType
import com.wd.daquan.explore.viewholder.AreaViewHolder
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.FindUserDynamicDescBean
import com.wd.daquan.model.bean.UserDynamicCommentDataListBean
import com.wd.daquan.model.bean.UserDynamicLikeDataListBean
import com.wd.daquan.model.interfaces.DqCallBack
import com.wd.daquan.model.mgr.ModuleMgr
import com.wd.daquan.model.retrofit.RetrofitHelp
import com.wd.daquan.model.retrofit.RetrofitHelp.getRequestBody

/**
 * 朋友圈列表适配器
 */
class AreaAdapter() : RecycleBaseAdapter<AreaViewHolder>() {
    private val popupWindow : CustomPopupWindow by lazy {
        initPopupWindow()
    }
    var userDynamicDescBeanList = ArrayList<FindUserDynamicDescBean>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    constructor(userDynamicDescBeanList: ArrayList<FindUserDynamicDescBean>):this(){
        this.userDynamicDescBeanList = userDynamicDescBeanList
    }

    fun addDynamicDescBean(userDynamicDescBean :FindUserDynamicDescBean){
        userDynamicDescBeanList.add(0,userDynamicDescBean)
        notifyDataSetChanged()
    }

    /**
     * 批量添加数据
     */
    fun addDynamicDescBeanList(userDynamicDescBeanList :ArrayList<FindUserDynamicDescBean>, isAddData : Boolean = true){
        if (isAddData){
            this.userDynamicDescBeanList.addAll(userDynamicDescBeanList)
        }else{
            this.userDynamicDescBeanList = userDynamicDescBeanList
        }
        Log.e("YM","数据长度:${this.userDynamicDescBeanList.size}")
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = userDynamicDescBeanList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_area,parent,false)
        return AreaViewHolder(view)
    }
    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val dynamicDescBean = userDynamicDescBeanList[position]
        holder.dynamicReviewIv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        val mShowMorePopupWindowWidth: Int = holder.dynamicReviewIv.measuredWidth
        val mShowMorePopupWindowHeight: Int = holder.dynamicReviewIv.measuredHeight
        holder.userName.text = dynamicDescBean.userNickName
        holder.dynamicWord.text = dynamicDescBean.desc
        holder.dynamicTimeTv.text = TimeUtil.getTimeShowString(dynamicDescBean.createTime, false)
        GlideUtils.loadRound(context, dynamicDescBean.userHeadPic, holder.areaHeadIcon, 5)

        holder.dynamicReviewIv.setOnClickListener {
            if (popupWindow.isShow()){
                popupWindow.dismiss()
            }else{
                popupWindow.userDynamicDescBean = dynamicDescBean
                popupWindow.showAsDropDown(it,30,-mShowMorePopupWindowHeight)
            }

        }
        val likeUser = dynamicDescBean.userDynamicLikeDataList.joinToString(separator = ",") {
            it.userNick
        }
        if (!TextUtils.isEmpty(likeUser)){
            holder.areaDingTv.text = likeUser
            holder.areaDingTv.visibility = View.VISIBLE
        }else{
            holder.areaDingTv.visibility = View.GONE
        }
        val areaPhotoAdapter: AreaPhotoAdapter = AreaPhotoAdapter()
        holder.dynamicPhotoRv?.adapter = areaPhotoAdapter
        areaPhotoAdapter.photos = dynamicDescBean.pics

        val areaReviewAdapter: AreaReviewAdapter = AreaReviewAdapter()
        areaReviewAdapter.dynamicsBean = dynamicDescBean
        holder.areaReviewRv?.adapter = areaReviewAdapter
        areaReviewAdapter.dynamicCommentDataListBeanList = dynamicDescBean.userDynamicCommentDataList
        areaReviewAdapter.setOnExploreAreaCallBack(object :AreaReviewAdapter.ExploreAreaCallBack{
            override fun onSuccess(dynamicDescBean: FindUserDynamicDescBean) {
                val index = getNotifyDataSetChangedIndex(dynamicDescBean)
                if (-1 == index) return
//                notifyItemChanged(index,dynamicBean)
                userDynamicDescBeanList.removeAt(index)
                userDynamicDescBeanList.add(index,dynamicDescBean)
                this@AreaAdapter.notifyDataSetChanged()
            }

            override fun onFail() {
            }
        })
        areaReviewAdapter.setOnDelCommonListener {
            val index = getNotifyDataSetChangedIndex(it)
            Log.e("YM","删除评论成功:${index}")
            if (-1 != index){
                //notifyItemChanged(index,dynamicBean)
                userDynamicDescBeanList.removeAt(index)
                userDynamicDescBeanList.add(index,it)
                this@AreaAdapter.notifyDataSetChanged()
            }
        }
        if (TextUtils.isEmpty(likeUser) && dynamicDescBean.userDynamicCommentDataList.isEmpty()){
            holder.areaReviewContainer.visibility = View.GONE
        }else{
            holder.areaReviewContainer.visibility = View.VISIBLE
        }
        if (!TextUtils.isEmpty(likeUser) && dynamicDescBean.userDynamicCommentDataList.isNotEmpty()){
            holder.areaDividerV.visibility = View.VISIBLE
        }else{
            holder.areaDividerV.visibility = View.GONE
        }

        holder.areaDynamicDelTv.visibility = if (ModuleMgr.getCenterMgr().uid == dynamicDescBean.userId) View.VISIBLE else View.GONE
        holder.areaDynamicDelTv.setOnClickListener{
            dialogExploreOperator(dynamicDescBean.dynamicId.toString())
        }
        holder.areaHeadContainer.setOnClickListener {
            NavUtils.gotoUserInfoActivity(context, dynamicDescBean.userId,ImType.P2P.value)
        }

        holder.userName.setOnClickListener {
            NavUtils.gotoUserInfoActivity(context, dynamicDescBean.userId,ImType.P2P.value)
        }
    }

    /**
     * 增量刷新
     */
    override fun onBindViewHolder(holder: AreaViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
//        if (payloads.isNotEmpty()){
//            val dynamic
//        }

    }

    private fun initPopupWindow():CustomPopupWindow{
        val popupWindow = CustomPopupWindow(inflater)
        popupWindow.setOnCustomPopupWindowCallBack(object : CustomPopupWindow.CustomPopupWindowCallBack{
            override fun dingCallBack(dynamicDescBean: FindUserDynamicDescBean, index: Int) {
                popupWindow.dismiss()
                userLike(dynamicDescBean.dynamicId.toString())
            }

            override fun reviewCallBack(dynamicDescBean: FindUserDynamicDescBean, index: Int) {
                popupWindow.dismiss()
                val bottomReviewFragment = ExploreAreaBottomFragment()
                val act = context as FragmentActivity
                val bundle = Bundle()
                bundle.putSerializable(ExploreAreaBottomFragment.DYNAMIC_BEAN,dynamicDescBean)
                bottomReviewFragment.arguments = bundle
                bottomReviewFragment.show(act.supportFragmentManager,"reviewBottom")
                bottomReviewFragment.setOnExploreAreaCallBack(ExploreCommentResultCallBack())
            }

            override fun delDingCallBack(dynamicLike: UserDynamicLikeDataListBean) {
                popupWindow.dismiss()
                userDelLike(dynamicLike.dynamicId.toString(), dynamicLike.likeId.toString())
            }
        })
        return popupWindow
    }

    /**
     * 操作对话框
     */
    private fun dialogExploreOperator(dynamicId: String){
        val act = context as FragmentActivity
        val dialogExploreComment = DialogExploreComment()
        val bundle = Bundle()
        bundle.putString(DialogExploreComment.ACTION_DYNAMIC_ID,dynamicId)
        bundle.putInt(DialogExploreComment.ACTION_TYPE, ExploreOperatorType.TYPE_DYNAMIC.type)
        dialogExploreComment.arguments = bundle
        dialogExploreComment.show(act.supportFragmentManager,"delComment")
        dialogExploreComment.setOnDelDynamicListener {
            val index = getNotifyDataSetChangedIndex(it)
            if (-1 != index){
                userDynamicDescBeanList.removeAt(index)
                notifyDataSetChanged()
            }
        }
    }

    /**
     * 用户点赞
     * dynamicId:朋友圈Id
     * userId: 点赞用户的Id
     */
    private fun userLike(dynamicId :String){
        val params = hashMapOf<String,String>()
        params["dynamicId"] = dynamicId
        saveUserDynamicLike(DqUrl.url_dynamic_saveUserDynamicLike,params)
    }

    /**
     * 取消点赞
     * dynamicId:朋友圈Id
     * likeId: 点赞的Id
     */
    private fun userDelLike(dynamicId :String, likeId :String){
        val params = hashMapOf<String,String>()
        params["dynamicId"] = dynamicId
        params["likeId"] = likeId
        delUserDynamicLike(DqUrl.url_dynamic_delUserDynamicLike,params)
    }

    /**
     * 点赞
     */
    private fun saveUserDynamicLike(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getDynamicApi().saveUserDynamicLike(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<FindUserDynamicDescBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<FindUserDynamicDescBean>) {
//                        hideLoading();
                val dynamicBean = entity.data as FindUserDynamicDescBean
                val index = getNotifyDataSetChangedIndex(dynamicBean)
                if (-1 == index) return
//                notifyItemChanged(index,dynamicBean)
                userDynamicDescBeanList.removeAt(index)
                userDynamicDescBeanList.add(index,dynamicBean)
                notifyDataSetChanged()
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<FindUserDynamicDescBean>) {
//                        hideLoading();
            }
        })
    }

    /**
     * 取消点赞
     */
    private fun delUserDynamicLike(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getDynamicApi().delUserDynamicLike(url, getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<FindUserDynamicDescBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<FindUserDynamicDescBean>) {
//                        hideLoading();
                val dynamicBean = entity.data as FindUserDynamicDescBean
                val index = getNotifyDataSetChangedIndex(dynamicBean)
                if (-1 == index) return
//                notifyItemChanged(index,dynamicBean)
                userDynamicDescBeanList.removeAt(index)
                userDynamicDescBeanList.add(index,dynamicBean)
                notifyDataSetChanged()
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<FindUserDynamicDescBean>) {
//                        hideLoading();
            }
        })
    }

    /**
     * 获取改变的朋友圈内容的索引位置
     */
    private fun getNotifyDataSetChangedIndex(dynamicBean :FindUserDynamicDescBean): Int{
        var position = -1;
        userDynamicDescBeanList.forEachIndexed { index, dynamicModel ->
            if (dynamicModel.dynamicId == dynamicBean.dynamicId){
                position = index
                return position
            }
        }
        return position;
    }


    inner class ExploreCommentResultCallBack : ExploreAreaBottomFragment.ExploreAreaCallBack{
        override fun onSuccess(dynamicDescBean: FindUserDynamicDescBean) {
            val index = getNotifyDataSetChangedIndex(dynamicDescBean)
            if (-1 == index) return
//                notifyItemChanged(index,dynamicBean)
            userDynamicDescBeanList.removeAt(index)
            userDynamicDescBeanList.add(index,dynamicDescBean)
            notifyDataSetChanged()
        }

        override fun onFail() {
        }
    }
}