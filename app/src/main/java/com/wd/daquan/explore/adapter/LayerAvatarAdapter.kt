package com.wd.daquan.explore.adapter

import android.view.View
import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.explore.viewholder.LayerAvatarViewHolder
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.adapter.RecycleBaseAdapter
import com.wd.daquan.model.log.DqLog

/**
 * 层叠头像Adapter
 */
class LayerAvatarAdapter(): RecycleBaseAdapter<LayerAvatarViewHolder>() {
    var avatarList = arrayListOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun getItemCount() =  if (avatarList.size > 3) 3 else avatarList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayerAvatarViewHolder {
        super.onCreateViewHolder(parent, viewType)
        val view = inflater.inflate(R.layout.item_layer_avatar,parent,false);
        return LayerAvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: LayerAvatarViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if(position == avatarList.size - 1){
            setMargins(holder.root,0,0,0,0)
        }
        GlideUtils.loadCircle(context,avatarList[position],holder.avatar)
    }
    private fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = v.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(l, t, r, b)
//            v.layoutParams = p
            v.requestLayout()
        }
    }
}