package com.da.library.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.da.library.view.CommBottomDialog;
import com.da.library.R;

/**
 * @author: dukangkang
 * @date: 2018/8/22 11:49.
 * @description: todo ...
 */
public class AliPayDialog extends CommBottomDialog implements View.OnClickListener {

    private View mView = null;

    public AliPayDialog(Activity activity) {
        super(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.mine_alipay_redpacket, null);
        TextView authTv = mView.findViewById(R.id.mine_alipay_auth);
        TextView cancelTv = mView.findViewById(R.id.mine_alipay_cancel);
        authTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);

        setView(mView);
        build();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (null == mOnAliPlayClick) {
            dismiss();
            return;
        }

        if (id == R.id.mine_alipay_auth) {
            mOnAliPlayClick.onAuth();
        } else if (id == R.id.mine_alipay_cancel) {
            mOnAliPlayClick.onCancel();
        }

        dismiss();
    }

    private onAliPlayClick mOnAliPlayClick = null;

    public void setOnAliPlayClick(onAliPlayClick onAliPlayClick) {
        mOnAliPlayClick = onAliPlayClick;
    }

    public interface onAliPlayClick {
        void onCancel();
        void onAuth();
    }
}
