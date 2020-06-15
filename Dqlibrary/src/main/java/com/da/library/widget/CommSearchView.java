package com.da.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.da.library.R;

/**
 * Created by docking on 2018/4/17 14:30.
 */
public class CommSearchView extends RelativeLayout {

//    private Context mContext = null;
    private TextView mTitleTv = null;

    public CommSearchView(Context context) {
        super(context);
        init(context);
    }

    public CommSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.comm_search_layout, this);
        mTitleTv = (TextView) this.findViewById(R.id.comm_search_title);
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    public void setTitle(int id) {
        mTitleTv.setText(getContext().getResources().getString(id));
    }

}
