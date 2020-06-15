package com.wd.daquan.chat.group.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.wd.daquan.R;
import com.da.library.holder.CommHolder;

/**
 * @author: dukangkang
 * @date: 2018/4/20 11:37.
 * @description: todo ...
 */
public class SearchMemberHolder extends CommHolder {

    public ImageView portrait;

    public TextView title;

    public TextView id;

    public SearchMemberHolder(View view) {
        super(view);
        this.portrait = view.findViewById(R.id.comm_search_item_portrait);
        this.title = view.findViewById(R.id.comm_search_item_title);
        this.id = view.findViewById(R.id.comm_search_item_id);
    }
}
