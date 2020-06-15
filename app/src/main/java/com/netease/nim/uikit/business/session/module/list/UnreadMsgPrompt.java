package com.netease.nim.uikit.business.session.module.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;

/**
 * 未消息提醒模块
 * Created by by docking
 */
public class UnreadMsgPrompt {
    // 底部新消息提示条
    private View rootView;
    private TextView unreadTips;

    private Context context;
    private View view;

    public UnreadMsgPrompt(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    // 显示底部新信息提示条
    public void show(int msgCount) {
        if (rootView == null) {
            init();
        }
        String text = "";
        if (msgCount > 99) {
            text = String.format(DqApp.getStringById(R.string.unread_message_tips), "100+");
        } else {
            text = String.format(DqApp.getStringById(R.string.unread_message_tips), ""+msgCount);
        }
        unreadTips.setText(text);
        rootView.setVisibility(View.VISIBLE);
    }

    // 初始化底部新信息提示条
    private void init() {
        ViewGroup containerView = (ViewGroup) view.findViewById(R.id.message_activity_list_view_container);
        View.inflate(context, R.layout.nim_unread_message_tip_layout, containerView);
        rootView = containerView.findViewById(R.id.qc_unread_message_tip_layout);
        unreadTips = rootView.findViewById(R.id.qc_unread_message_count);
        rootView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnUnreadMsgListener != null) {
                    mOnUnreadMsgListener.unreadMsgClick();
                }
                rootView.setVisibility(View.GONE);
            }
        });
    }

    private OnUnreadMsgListener mOnUnreadMsgListener = null;

    public interface OnUnreadMsgListener {
        public void unreadMsgClick();
    }

    public void setOnUnreadMsgListener(OnUnreadMsgListener onUnreadMsgListener) {
        mOnUnreadMsgListener = onUnreadMsgListener;
    }
}
