package com.da.library.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.da.library.R;

/**
 * 自定义的toolbar
 */
@SuppressLint("CustomViewStyleable")
public class DqToolbar extends Toolbar {

    private String rightText;
    private boolean mShowRight = false;
    private boolean mShowBack = true;
    private String title;

    private ImageView mBackIv;
    private TextView mTitle;
    private TextView mRightTv;
    private ImageView mRightIv;

    public DqToolbar(Context context) {
        this(context, null);
    }

    public DqToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DqToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Dq_Toolbar);
        if (typedArray != null) {
            title = typedArray.getString(R.styleable.Dq_Toolbar_dqtTitle);
            rightText = typedArray.getString(R.styleable.Dq_Toolbar_dqtRightTv);
            mShowRight = typedArray.getBoolean(R.styleable.Dq_Toolbar_dqtShowRightTv, mShowRight);
            mShowBack = typedArray.getBoolean(R.styleable.Dq_Toolbar_dqtShowBack, mShowBack);
            typedArray.recycle();
        }

        initView(context);
        initData();

    }

    private void initView(Context context) {
        inflate(context, R.layout.dq_toolbar, this);
        mBackIv = findViewById(R.id.toolbar_back_iv);
        mTitle = findViewById(R.id.toolbar_title_tv);
        mRightTv = findViewById(R.id.toolbar_right_tv);
        mRightIv = findViewById(R.id.toolbar_right_iv);
    }

    private void initData() {
        mBackIv.setVisibility(mShowBack ? VISIBLE : GONE);
        setTitle(title);
        mRightTv.setVisibility(mShowRight ? VISIBLE : GONE);
        if(!TextUtils.isEmpty(rightText)) {
            setRightTvVisible();
            setRightTv(rightText);
        }
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setRightTv(String title) {
        mRightTv.setText(title);
    }

    public void setRightTvVisible() {
        mRightTv.setVisibility(VISIBLE);
        mRightIv.setVisibility(GONE);
    }
    public void setRightIvVisible(boolean isShow) {
        mRightIv.setVisibility(isShow ? VISIBLE : GONE);
        mRightTv.setVisibility(GONE);
    }
    public void setRightTv(int title) {
        mRightTv.setText(title);
    }

    public ImageView getBackIv() {
        return mBackIv;
    }

    public void setRightTvClick(boolean b) {
        mRightTv.setEnabled(b);
    }

    public void setRightTxtColor(int color) {
        mRightTv.setTextColor(color);
    }

    public void setRightIv(int id) {
        mRightIv.setImageResource(id);
    }

    public void setLeftIv(int id) {
        mBackIv.setImageResource(id);
    }

    public void setRightTvEnabled(boolean b) {
        mRightTv.setEnabled(b);
    }

    public void setTitlePadding(int i) {

        mTitle.setPadding(i, i, i , i);
    }
}
