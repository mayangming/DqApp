package com.wd.daquan.chat.group.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.da.library.holder.CommHolder;
import com.wd.daquan.R;

/**
 * @author: dukang
 * @date: 2018/4/19 16:41.
 * @description: 组织信息内 Grid的ItemHolder
 */
public class MemberHolder extends CommHolder {

    // 名称
    public TextView name;
    // 头像
    public ImageView portrait;
    // 删除
    public ImageView delete;
    // 群主
    public ImageView master;
    public View vipHeadOutLine;

    public MemberHolder(View view) {
        super(view);
        name = view.findViewById(R.id.group_detail_item_name);
        portrait = view.findViewById(R.id.group_detail_item_portrait);
        delete = view.findViewById(R.id.group_detail_item_delete);
        master = view.findViewById(R.id.group_detail_item_master);
        vipHeadOutLine = view.findViewById(R.id.vip_head_outline);
    }
}
