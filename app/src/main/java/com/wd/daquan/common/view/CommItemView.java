package com.wd.daquan.common.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * @author: dukangkang
 * @date: 2018/10/24 10:11.
 * @description: todo ...
 */
public class CommItemView extends RelativeLayout {

    private int resourceId = -1;
    private String text = "";
    private String tipsText = "";
    // 是否显示红点
    private boolean showRed = false;
    // 是否显示箭头，默认显示箭头
    private boolean showAllow = true;
    // 是否显示底部线，默认显示底线
    private boolean showLine = true;
    // 是否显示文本提示
    private boolean showTips = false;
    // 是否显示图标,默认显示图标
    private boolean showIcon= true;
    // 是否显示标题旁边的Tips,比如推荐
    private boolean showTextTips = false;
    // 显示文本提示的颜色
    private int tipsColor = 0;
    // 容器的高度
    private int height = 0;

    private View mItemLine;
    public TextView mItemTv;
    private TextView mItemTips;
    private ImageView mItemIv;
    private ImageView mItemRedIv;
    private ImageView mItemAllowIv;
    //item标题
    private TextView mItemNameTv;
    // 标题旁边的tips, eg: 推荐
    private TextView mItemTextTipsTv;

    private LinearLayout mRootView;

    public CommItemView(Context context) {
        this(context, null);
    }

    public CommItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttribute(context, attrs);
        initView();
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommItemView);
        if (typedArray != null) {
            resourceId = typedArray.getResourceId(R.styleable.CommItemView_itemSrc, -1);
            text = typedArray.getString(R.styleable.CommItemView_itemText);
            showRed = typedArray.getBoolean(R.styleable.CommItemView_itemRedVisible, false);
            showAllow = typedArray.getBoolean(R.styleable.CommItemView_itemArrowVisible, true);
            showLine = typedArray.getBoolean(R.styleable.CommItemView_itemLineVisible, true);
            showTips = typedArray.getBoolean(R.styleable.CommItemView_itemTipsVisible, false);
            showIcon = typedArray.getBoolean(R.styleable.CommItemView_itemIconVisible, true);
            showTextTips = typedArray.getBoolean(R.styleable.CommItemView_itemTextTipsVisible, false);
            tipsColor = typedArray.getColor(R.styleable.CommItemView_itemTipsColor, getResources().getColor(R.color.text_blue_pressed));
            height = typedArray.getDimensionPixelSize(R.styleable.CommItemView_itemHeight, 0);
            tipsText = typedArray.getString(R.styleable.CommItemView_itemTipsText);

            typedArray.recycle();
        }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.comm_item_view, this);
        mItemLine = this.findViewById(R.id.comm_item_line);
        mItemIv = this.findViewById(R.id.comm_item_icon);
        mItemTv = this.findViewById(R.id.comm_item_text);
        mItemTips = this.findViewById(R.id.comm_item_tips);
        mItemRedIv = this.findViewById(R.id.comm_item_reddot);
        mItemAllowIv = this.findViewById(R.id.comm_item_allow);

        mItemNameTv = this.findViewById(R.id.comm_item_name);

        mRootView = this.findViewById(R.id.comm_item_llyt);

        mItemTextTipsTv = this.findViewById(R.id.comm_item_text_tips);

        if(resourceId != -1) {
            mItemIv.setImageResource(resourceId);
        }

        if (!TextUtils.isEmpty(text)) {
            mItemTv.setText(text);
        }
        // 设置Item文字颜色
        mItemTips.setTextColor(tipsColor);
        if (!TextUtils.isEmpty(tipsText)) {
            mItemTips.setText(tipsText);
        }

        if (height != 0) {
            LayoutParams lp = (LayoutParams) mRootView.getLayoutParams();
            lp.height = height;
            mRootView.setLayoutParams(lp);
        }

        setVisible(mItemRedIv, showRed);
        setVisible(mItemAllowIv, showAllow);
        setVisible(mItemLine, showLine);
        setVisible(mItemTips, showTips);
        setVisible(mItemIv, showIcon);
        setVisible(mItemTextTipsTv, showTextTips);
    }

    private void setVisible(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    /**
     * 设置显示红点
     * @param visible
     */
    public void showRedDot(boolean visible) {
        setVisible(mItemRedIv, visible);
    }

    /**
     * 设置显示底线
     * @param visible
     */
    public void showBtmLine(boolean visible) {
        setVisible(mItemLine, visible);
    }

    /**
     * 设置显示底线
     * @param visible
     */
    public void showIcon(boolean visible) {
        setVisible(mItemIv, visible);
    }

    /**
     * 设置显示箭头
     * @param visible
     */
    public void showAllow(boolean visible) {
        setVisible(mItemAllowIv, visible);
    }

    public void setItemText(String text) {
        mItemTv.setText(text);
    }

    public void setItemTextSize(int size) {
        mItemTv.setTextSize(size);
    }

    public void setItemTextColor(int color) {
        mItemTv.setTextColor(color);
    }

    public void setItemTitle(String text) {
        mItemNameTv.setVisibility(VISIBLE);
        mItemNameTv.setText(text);
    }

    /**
     * 设置Itemtips
     * @param tips
     */
    public void setItemTips(String tips) {
        mItemTips.setText(tips);
    }

    public String getItemTips() {
        return mItemTips.getText().toString();
    }

    public void setTipsColor(int color) {
        mItemTips.setTextColor(color);
    }

    public TextView getItemNameTv() {
        return mItemNameTv;
    }

    public void setItemNameTv(String text) {
        mItemNameTv.setText(text);
    }

    public void setItemNameTextSize(int size) {
        mItemNameTv.setTextSize(size);
    }

    public TextView getItemDescTv() {
        return mItemTv;
    }

    public void showItemName(boolean visible) {
        setVisible(mItemNameTv, visible);
    }
}
