package com.wd.daquan.mine.wallet.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.wd.daquan.R;


public class RedDetailsHeaderHolder extends BaseViewHolder{
    public LinearLayout timeLayout;
    public TextView time;
    public ImageView avatar;
    public TextView name;
    public TextView money;
    public TextView redCount;
    public TextView redType;

    public RedDetailsHeaderHolder(View view) {
        super(view);
        timeLayout = view.findViewById(R.id.redDetdetailsTimeLayout);
        time = view.findViewById(R.id.redDetdetailsTimeTxt);
        avatar = view.findViewById(R.id.redDetdetailsAvatarImg);
        name = view.findViewById(R.id.redDetdetailsNameTxt);
        money = view.findViewById(R.id.redDetdetailsMoneyTxt);
        redType = view.findViewById(R.id.redDetdetailsRedGetTxt);
        redCount = view.findViewById(R.id.redDetdetailsRedGetCountTxt);
    }
}