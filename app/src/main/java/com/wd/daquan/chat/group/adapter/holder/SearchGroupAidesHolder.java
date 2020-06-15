package com.wd.daquan.chat.group.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.wd.daquan.R;

/**
 * @Author: 方志
 * @Time: 2019/1/7 11:16
 * @Description:
 */
public class SearchGroupAidesHolder extends RecyclerView.ViewHolder {

    public ImageView portrait;
    public TextView name;
    public TextView desc;
    public TextView add;

    public SearchGroupAidesHolder(View view) {
        super(view);
        portrait = view.findViewById(R.id.item_aides_icon_iv);
        name = view.findViewById(R.id.item_aides_name_tv);
        desc = view.findViewById(R.id.item_aides_describe_tv);
        add = view.findViewById(R.id.item_aides_add_tv);

        desc.setMaxEms(14);
    }
}
