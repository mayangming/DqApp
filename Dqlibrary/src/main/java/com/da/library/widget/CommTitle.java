package com.da.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.da.library.R;

/**
 * @author: dukangkang
 * @date: 2018/5/14 17:36.
 * @description: todo ...
 */
public class CommTitle extends RelativeLayout {
    private String title = "";
    private int leftResourceId;         // 左边返回按钮资源ID, 左边图片暂时不处理，默认返回按钮
    private int rightResourceId;        // 右边图片资源ID
    private boolean showLeft = true;    // 左边返回按钮
    private boolean showRight = false;  // 右边图片按钮

    private Context mContext;

    // 标题
    private TextView mCommTitle;
    // 标题后面跟的数量
    private TextView mCommTitleNum;
    // 左边文字（一般是"退出"）
    private TextView mLeftTv;
    // 右边文字
    private TextView mRightTv;
    // 左边返回按钮图片后紧跟提示（一般是返回）
    private TextView mLeftTipsTv;
    // 左边图片
    private ImageView mLeftIv;
    // 右边图片
    private ImageView mRightIv;
    // 右边图片左边的图片
    private ImageView mRightIvLeft;
    // 右边图片左边的图片
    private RelativeLayout mRightIvLeftLayout;
    //标题view
    private RelativeLayout mTitleLayout;
    //底部线条
    private View mBottomLine;

    public CommTitle(Context context) {
        super(context);
        init(context, null);
    }

    public CommTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CommTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context.getApplicationContext();
        initAttributeSet(context, attrs);
        initView();
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommTitle);
        if (typedArray != null) {
            title = typedArray.getString(R.styleable.CommTitle_title);
            leftResourceId = typedArray.getResourceId(R.styleable.CommTitle_leftSrc, -1);
            rightResourceId = typedArray.getResourceId(R.styleable.CommTitle_rightSrc, -1);
            showLeft = typedArray.getBoolean(R.styleable.CommTitle_leftVisible, true);
            showRight = typedArray.getBoolean(R.styleable.CommTitle_rightVisible, false);
            typedArray.recycle();
        }
    }

    private void initView() {
        inflate(mContext, R.layout.comm_title, this);
        mTitleLayout = findViewById(R.id.rl_title);
        mCommTitle = this.findViewById(R.id.comm_title);
        mCommTitleNum = this.findViewById(R.id.comm_title_num);

        mLeftTv = this.findViewById(R.id.comm_left_tv);
        mRightTv = this.findViewById(R.id.comm_right_tv);
        mRightIvLeft = this.findViewById(R.id.comm_right_iv_left);
        mRightIvLeftLayout = findViewById(R.id.comm_right_iv_left_layout);

        mLeftIv = this.findViewById(R.id.comm_left_iv);
        mRightIv = this.findViewById(R.id.comm_right_iv);
        mLeftTipsTv = this.findViewById(R.id.comm_left_tips);

        mBottomLine = this.findViewById(R.id.bottom_line);

        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

        if (rightResourceId != -1) {
            setRightImageResource(rightResourceId);
        }

        if (!showLeft) {
            setLeftGone();
        }

        if (!showRight) {
            setRightGone();
        } else {
            // 默认右边图片显示
            setRightVisible(View.VISIBLE);
        }
    }

    private void setVisible(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 设置左边为空白
     */
    public void setLeftGone() {
        setVisible(mLeftTv, false);
        setVisible(mLeftIv, false);
    }

    /**
     * 设置右边空白
     */
    public void setRightGone() {
        setVisible(mRightTv, false);
        setVisible(mRightIv, false);
        setVisible(mRightIvLeftLayout, false);
    }

    /**
     * 设置左边可见
     * @param visible
     * visible: View.visible 即显示图片, 反之文字可见
     */
    public void setLeftVisible(int visible) {
        if (View.VISIBLE == visible) {
            setVisible(mLeftIv, true);
            setVisible(mLeftTv, false);
        } else {
            setVisible(mLeftIv, false);
            setVisible(mLeftTv, true);
        }
    }

    /**
     * 设置右边可见
     * @param visible
     * visible: View.visible 即显示图片, 反之文字可见
     */
    public void setRightVisible(int visible) {
        if (View.VISIBLE == visible) {
            setVisible(mRightIv, true);
            setVisible(mRightTv, false);
        } else {
            setVisible(mRightIv, false);
            setVisible(mRightTv, true);
        }
    }

    public void setLeftSrcVisible() {

    }

    public void setRightSrcVisible() {

    }


    public void setRightIv(int id) {
        mRightIv.setImageResource(id);
    }

    public void setRightTvClick(boolean clickable) {
        mRightTv.setSelected(clickable);
        mRightTv.setClickable(clickable);
    }

    public int getLeftIvId() {
        return mLeftIv.getId();
    }

    public int getLeftTvId() {
        return mLeftTv.getId();
    }

    public int getRightIvId() {
        return mRightIv.getId();
    }

    public int getRightTvId() {
        return mRightTv.getId();
    }

    public int getLeftTipsId() {
        return mLeftTipsTv.getId();
    }

    public TextView getLeftTipsTv() {
        return mLeftTipsTv;
    }

    public TextView getCommTitleNum() {
        return mCommTitleNum;
    }

    public ImageView getLeftIv() {
        return mLeftIv;
    }

    public ImageView getRightIv() {
        return mRightIv;
    }

    public TextView getLeftTv() {
        return mLeftTv;
    }

    public TextView getRightTv() {
        return mRightTv;
    }


    public void setTitle(String title) {
        mCommTitle.setText(title);
    }


    public String getTitle() {
        return mCommTitle.getText().toString();
    }

    public void setTitleTextColor(int color) {
        mCommTitle.setTextColor(color);
    }

    public String getTitleName() {
        return mCommTitle.getText().toString();
    }

    public void setTitle(int id) {
        mCommTitle.setText(mContext.getResources().getString(id));
    }

    public void setRightTxt(String txt) {
        mRightTv.setText(txt);
    }

    public void setTitleTextLength(int length) {
        mCommTitle.setMaxEms(length);
    }
    public void setTitlePadding(int i) {
        mCommTitle.setPadding(i,i,i,i);
    }

    public void setRightTxt(String txt, OnClickListener clickListener) {
        mRightTv.setVisibility(VISIBLE);
        mRightTv.setText(txt);
        mRightTv.setOnClickListener(clickListener);
    }

    public void setRightTxtColor(int id) {
        mRightTv.setTextColor(id);
    }

    public void setRightTxt(int id) {
        mRightTv.setText(mContext.getResources().getString(id));
    }

    public void setLeftTxt(String txt) {
        mLeftTv.setText(txt);
    }

    public void setLeftTxt(String txt, OnClickListener clickListener) {
        setLeftTxt(txt, -1, clickListener);
    }

    public void setLeftTxt(String txt, int color, OnClickListener clickListener) {
        mLeftTv.setText(txt);
        if(color != -1){
            mLeftTv.setTextColor(color);
        }
        mLeftTv.setOnClickListener(clickListener);
    }

    public void setLeftTxt(int id) {
        mLeftTv.setText(mContext.getResources().getString(id));
    }

    public void setLeftTips(int id) {
        mLeftTipsTv.setText(mContext.getResources().getString(id));
    }

    public void setLeftTips(String tips) {
        mLeftTipsTv.setText(tips);
    }

    public void setTitleLayoutBackgroundColor(int color){
        mTitleLayout.setBackgroundColor(color);
    }

    public void setIvBackgroundColor(int id){
        mLeftIv.setBackgroundResource(id);
        mRightIv.setBackgroundResource(id);
        mRightTv.setBackgroundResource(id);
    }

    /**
     * 设置右边按钮图片
     * @param id
     */
    public void setRightImageResource(int id) {
        setRightImageResource(id, null);
    }

    public void setRightImageResource(int id, OnClickListener clickListener) {
        mRightIv.setImageResource(id);
        if (clickListener != null) {
            mRightIv.setOnClickListener(clickListener);
        }
    }

    /**
     * 设置左边按钮图片
     * @param id
     */
    public void setLeftImageResource(int id) {
        mLeftIv.setImageResource(id);
    }

    public void setRightIvLeft(int id) {
        mRightIvLeft.setImageResource(id);
    }

    public int getRightIvLeftId() {
        return mRightIvLeft.getId();
    }

    public ImageView getRightIvLeft() {
        return mRightIvLeft;
    }

    public RelativeLayout getRightIvLeftLayout() {
        return mRightIvLeftLayout;
    }

    public void setRightIvLeftLayoutGone() {
        setVisible(mRightIvLeftLayout, false);
    }


    public void setBottomLineVisible(boolean isShow) {
        this.mBottomLine.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
    public void setBottomLineColor(int color) {
        this.mBottomLine.setBackgroundColor(color);
    }
}
