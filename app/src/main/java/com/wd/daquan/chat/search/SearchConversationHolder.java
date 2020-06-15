package com.wd.daquan.chat.search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * @author: dukangkang
 * @date: 2018/9/26 19:13.
 * @description: todo ...
 */
public class SearchConversationHolder extends RecyclerView.ViewHolder {

    public RelativeLayout rlyt;
    public ImageView portrait = null;
    public TextView name = null;
    public TextView nameNum = null;

    public SearchConversationHolder(View view) {
        super(view);
        this.portrait = view.findViewById(R.id.search_conversation_item_portrait_iv);
        this.name = view.findViewById(R.id.search_conversation_item_name_tv);
        this.nameNum = view.findViewById(R.id.search_conversation_item_name_num_tv);
        this.rlyt = view.findViewById(R.id.search_conversation_item_container_rl);
    }
}
