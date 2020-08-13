package com.wd.daquan.explore.adapter

import android.view.View
import android.view.ViewGroup
import com.netease.nim.uikit.common.util.sys.TimeUtil
import com.wd.daquan.R
import com.wd.daquan.explore.type.AboutMeDynamicType
import com.wd.daquan.explore.viewholder.AreaUnReadViewHolder
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.model.bean.AreaUnReadBean
import com.wd.daquan.model.log.DqLog

class AreaUnReadAdapter() : RecycleBaseAdapter<AreaUnReadViewHolder>() {
    var areaUnReadBeanList = ArrayList<AreaUnReadBean>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    fun addData(dataList :ArrayList<AreaUnReadBean>){
        this.areaUnReadBeanList = dataList
    }

    override fun getItemCount() = areaUnReadBeanList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaUnReadViewHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_area_unread,parent,false)
        return AreaUnReadViewHolder(view)
    }

    override fun onBindViewHolder(holder: AreaUnReadViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val bean = areaUnReadBeanList[position]
        val sendUser = bean.sendUser
        val toUser = bean.toUser
        holder.userName.text = sendUser.nickName
        holder.time.text =  TimeUtil.getTimeShowString(bean.createTime, false)
        GlideUtils.loadRound(context,bean.imgUrl,holder.areaImg,5)
        GlideUtils.loadRound(context,sendUser.headpic,holder.userAvatar,5)
        if (bean.msgType == AboutMeDynamicType.DING.type){//点赞
            holder.zanImg.visibility = View.VISIBLE
            holder.commentContent.visibility = View.GONE
        }else{
            holder.commentContent.visibility = View.VISIBLE
            holder.zanImg.visibility = View.GONE
            if (null != toUser){
                holder.commentContent.text = "回复${toUser.nickName}: ${bean.dynamicCommendMsg}"
            }else{
                holder.commentContent.text = bean.dynamicCommendMsg
            }
        }
    }
}