package com.wd.daquan.contacts.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * @Author: 方志
 * @Time: 2018/9/6 13:36
 * @Description: 联系人底部
 */
public class ContactsListFootHolder extends RecyclerView.ViewHolder {

    public TextView itemNumber;

    public ContactsListFootHolder(View view) {
        super(view);

        itemNumber = view.findViewById(R.id.item_number);
    }
}
