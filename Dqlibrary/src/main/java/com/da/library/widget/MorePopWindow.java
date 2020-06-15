package com.da.library.widget;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.da.library.R;

public class MorePopWindow extends PopupWindow implements View.OnClickListener {
    private OnItemClick mOnItemClick;

    @SuppressLint("InflateParams")
    public MorePopWindow(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.comm_popup_more, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(content);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        RelativeLayout teamRlyt = content.findViewById(R.id.comm_team);
        RelativeLayout addfriendRlyt = content.findViewById(R.id.comm_add_friend);
        RelativeLayout scancodeRlyt = content.findViewById(R.id.comm_scancode);
        RelativeLayout mindQrcode = content.findViewById(R.id.comm_mine_qrcode);
        mindQrcode.setVisibility(View.GONE);

        //发起群聊
        teamRlyt.setOnClickListener(this);
        addfriendRlyt.setOnClickListener(this);
        scancodeRlyt.setOnClickListener(this);
        mindQrcode.setOnClickListener(this);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
       this.mOnItemClick = onItemClick;
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
        int id = v.getId();
        int type = 0;
        if (id == R.id.comm_team) {
            type = ItemType.TEAM;
        } else if (id == R.id.comm_add_friend) {
            type = ItemType.ADD_FRIEND;
        } else if (id == R.id.comm_scancode) {
            type = ItemType.SCAN_CODE;
        } else if (id == R.id.comm_mine_qrcode) {
            type = ItemType.MIND_QRCODE;
        }

        if (mOnItemClick != null) {
            mOnItemClick.onItem(type);
        }

    }

    public interface OnItemClick {
        void onItem(int type);
    }

    public interface ItemType {
        int TEAM = 1;
        int ADD_FRIEND = 2;
        int SCAN_CODE = 3;
        int MIND_QRCODE = 4;
    }
}
