package com.da.library.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.da.library.R;


/**
 * Author: 方志
 * Time: 2018/5/23 16:29
 * Description:
 */
public class ListDialogItemViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public View line;

    public ListDialogItemViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.item_name_tv);
        line = view.findViewById(R.id.item_line);
    }
}
