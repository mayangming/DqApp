package com.wd.daquan.chat.group.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.da.library.holder.CommHolder;

/**
 * Created by zht on 2018/7/18.
 */

public class GroupMemberForbidDoRPHolder extends CommHolder {

    public ImageView mDispatcherHeadpicIv;
    public TextView mDispatcherNameTv;
    public Button mDispatcherRemoveBt;

    public GroupMemberForbidDoRPHolder(View view) {
        super(view);
        mDispatcherHeadpicIv = view.findViewById(R.id.dispatcher_headpic_iv);
        mDispatcherNameTv = view.findViewById(R.id.dispatcher_name_tv);
        mDispatcherRemoveBt = view.findViewById(R.id.dispatcher_remove_bt);

    }
}
