package com.wd.daquan.mine.adapter

import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.mine.viewholder.InviteFriendViewHolder
import com.wd.daquan.model.bean.InvitePerActEntity

/**
 * 邀请好友的适配器
 */
class InviteFriendAdapter(): RecycleBaseAdapter<InviteFriendViewHolder>() {
    var invitePerActList = listOf<InvitePerActEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    constructor(invitePerActList :ArrayList<InvitePerActEntity>) :this(){
        this.invitePerActList = invitePerActList
    }

    override fun getItemCount() = invitePerActList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteFriendViewHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_invite_friend,parent,false)
        return InviteFriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: InviteFriendViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val bean = invitePerActList[position]
        GlideUtils.loadRound(context,bean.userPic,holder.inviteAvatar)
        val content = "再签到%d天+%s元，提现%s元+%s元"
        val award = bean.totalAward.toDouble() / 100
        val nextSignAward = bean.nextSignAward.toDouble() / 100
        val nextMoney = bean.nexWithdrawMoney.toDouble() / 100
        val nextAward = bean.nextWithdrawAward.toDouble() / 100
        holder.inviteUserName.text = bean.nickName
        holder.inviteContent.text = bean.returnWord
        holder.inviteMoney.text = "+${award}元"
    }


}