package com.wd.daquan.mine.wallet.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.wd.daquan.R;


public class LooseChangeHolder extends BaseViewHolder{
    public LinearLayout layout;
    public TextView title;
    public TextView time;
    public TextView money;

    public LooseChangeHolder(View view) {
        super(view);
        layout = view.findViewById(R.id.looseChangeLayout);
        title = view.findViewById(R.id.looseChangeNameTxt);
        time = view.findViewById(R.id.looseChangeTimeTxt);
        money = view.findViewById(R.id.looseChangeMoneyTxt);
    }
}