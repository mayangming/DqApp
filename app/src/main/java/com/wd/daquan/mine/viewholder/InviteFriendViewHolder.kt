package com.wd.daquan.mine.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wd.daquan.R
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder

/**
 * 邀请好友的奖励列表
 */
class InviteFriendViewHolder(itemView: View) : RecycleBaseViewHolder(itemView) {
    val inviteAvatar : ImageView = itemView.findViewById(R.id.invite_avatar)
    val inviteUserName : TextView = itemView.findViewById(R.id.invite_user_name)
    val inviteContent : TextView = itemView.findViewById(R.id.invite_user_content)
    val inviteMoney : TextView = itemView.findViewById(R.id.invite_money)
}