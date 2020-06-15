package com.da.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.da.library.tools.DensityUtil;
import com.da.library.R;


/**
 * 右侧的字母索引View
 * 2018/5/29 增加viewpager和sidebar冲突问题
 * @author fangzhi。
 */

public class SideBar extends View {

    /**
     * 触摸事件监听
     */
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    /**
     * 26个字母加3个特殊符号
     */
    public static String[] mLetter = {"↑", "☆", "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
    };
    /**
     * 选中的角标
     */
    private int choose = -1;

    /**
     * 画笔
     */
    private Paint mPaint = new Paint();

    /**
     * 中间放大显示
     */
    private TextView mTextDialog;
    /**
     * 字母默认字体颜色，默认黑色
     */
    private int mTextDefaultColor = Color.parseColor("#2f2f2f");

    /**
     * 字母选中字体颜色，默认白色
     */
    private int mTextSelectColor = Color.WHITE;

    /**
     * 文字大小
     */
    private int mTextSize = 30;

    /**
     * 背景矩形框
     */
    private RectF rectF;
    /**
     * 透明颜色
     */
    private int mColorTrans = Color.TRANSPARENT;
    /**
     * 选中背景
     */
    private int mColorBar = Color.parseColor("#A8A8A8");

    /**
     * 画矩形画笔
     */
    private Paint mPaintRect = new Paint();

    /**
     * viewpager是否滑动
     */
    private boolean isViewPagerScroll = false;

    /**
     * 禁止触摸滑动
     */
    private boolean isBanTouch = false;

    /**
     * 为SideBar显示字母的TextView
     *
     * @param mTextDialog 中间显示的textview
     */
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    /**
     * 给字母重新设置颜色
     *
     * @param color 颜色
     */
    public void setTextDefaultColor(int color) {
        mTextDefaultColor = color;
    }

    /**
     * 给选中字母重新设置颜色
     *
     * @param color 颜色
     */
    public void setTextSelectColor(int color) {
        mTextSelectColor = color;
    }

    /**
     * 给字体重新设置大小
     *
     * @param size 尺寸
     */
    public void setTextSize(int size) {
        mTextSize = size;
    }

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    /**
     * 初始化自定义属性
     *
     * @param context 上下文
     * @param attrs   属性
     */
    @SuppressLint("CustomViewStyleable")
    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.App_SideBar);
        mTextDefaultColor = typedArray.getColor(R.styleable.App_SideBar_sbTextDefaultColor, mTextDefaultColor);
        mTextSelectColor = typedArray.getColor(R.styleable.App_SideBar_sbTextSelectColor, mTextSelectColor);
        mTextSize = typedArray.getInt(R.styleable.App_SideBar_sbTextSize, mTextSize);
        mColorBar = typedArray.getColor(R.styleable.App_SideBar_sbRectSelectColor, mColorBar);
        typedArray.recycle();

        rectF = new RectF();
        mTextSize = DensityUtil.dip2px(getContext(), 12);
    }

    /**
     * 重写的onDraw的方法
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //获取对应的高度
        int height = getHeight();
        //获取对应的宽度
        int width = getWidth();
        //获取每一个字母的高度
        int singleHeight = height / mLetter.length;
        //绘制一个矩形背景
        resetRect(0, 0, width, height, choose == -1 ? mColorTrans : mColorBar);
        canvas.drawRect(rectF, mPaintRect);

        for (int i = 0; i < mLetter.length; i++) {
            mPaint.setColor(mTextDefaultColor);  // 所有字母的默认颜色
            mPaint.setFakeBoldText(true);//设置是否为粗体文字
            mPaint.setTypeface(Typeface.DEFAULT);//(右侧字体样式)
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(mTextSize); //(右侧字体大小)
            //选中的状态
            if (i == choose) {
                mPaint.setColor(mTextSelectColor); //选中字母的颜色
            }
            //x坐标等于=中间-字符串宽度的一般
            float xPos = width / 2 - mPaint.measureText(mLetter[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(mLetter[i], xPos, yPos, mPaint);
            mPaint.reset();//重置画笔
        }
    }

    /**
     * 重置背景颜色
     */
    private void resetRect(int left, int top, int right, int bottom, int color) {
        rectF.set(left, top, right, bottom);
        mPaintRect.setColor(color);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if(isBanTouch) return true;

        //初始x, y坐标
        float startY = event.getY();
        int oldChoose = choose;
        int newChoose = (int) (startY / getHeight() * mLetter.length);//点击y坐标所占高度的比例*b数组的长度就等于点击b中的个数

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                resetSideBar();
                break;
            default:
                showSelectText(oldChoose, newChoose);
                break;
        }
        return true;
    }



    /**
     * 还原SideBar
     */
    private void resetSideBar() {
        setBackground(new ColorDrawable(0x00000000));//设置背景颜色
        choose = -1;
        invalidate();
        if (mTextDialog != null) {
            mTextDialog.setVisibility(View.INVISIBLE);
        }
    }

    /**
     *
     * @param oldChoose 旧角标
     * @param c 新角标
     */
    private void showSelectText(int oldChoose, int c) {
        //setBackgroundResource(R.drawable.qc_sidebar_background); // 点击字母条的背景颜色
        if (oldChoose != c) {
            if (c >= 0 && c < mLetter.length) {
                if (onTouchingLetterChangedListener != null) {
                    onTouchingLetterChangedListener.onTouchingLetterChanged(mLetter[c]);
                }

                if (mTextDialog != null) {
                    if(isViewPagerScroll) {
                        mTextDialog.setVisibility(View.INVISIBLE);
                    }else {
                        mTextDialog.setText(mLetter[c]);
                        mTextDialog.setVisibility(View.VISIBLE);
                    }
                }

                choose = c;
                invalidate();
            }
        }
    }


    /**
     * 向外松开的方法
     *
     * @param onTouchingLetterChangedListener listener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * sidebar和viewpager滑动冲突问题
     * @param b 是否在滑动
     */
    public void refreshUI(boolean b) {
        this.isViewPagerScroll = b;
        if(b) {
            this.setVisibility(INVISIBLE);
        }else {
            this.setVisibility(VISIBLE);
            resetSideBar();
        }
    }

    /**
     * 接口
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

    public void isBanTouch(boolean isBanTouch){
        this.isBanTouch = isBanTouch;
    }
}
