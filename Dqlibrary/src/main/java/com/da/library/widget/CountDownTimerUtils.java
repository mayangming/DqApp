package com.da.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.da.library.R;

public class CountDownTimerUtils extends CountDownTimer {
    private int color;
    private TextView mTextView;
    private Context context;

    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval, Context context) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
        this.context = context;
        color = context.getResources().getColor(R.color.text_blue_pressed);
    }

    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval, Context context, int color) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
        this.context = context;
        this.color = color;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setEnabled(false);
        mTextView.setClickable(false); //设置不可点击
        mTextView.setTextColor(color);
        mTextView.setText(millisUntilFinished / 1000 + "秒后可重新发送");  //设置倒计时时间
//        /**
//         * 超链接 URLSpan
//         * 文字背景颜色 BackgroundColorSpan
//         * 文字颜色 foregroundcolorspan
//         * 字体大小 AbsoluteSizeSpan
//         * 粗体、斜体 StyleSpan
//         * 删除线 StrikethroughSpan
//         * 下划线 UnderlineSpan
//         * 图片 ImageSpan
//         * http://blog.csdn.net/ah200614435/article/details/7914459
//         */
//        SpannableString spannableString = new SpannableString(mTextView.getText().toString());  //获取按钮上的文字
//        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
//        /**
//         * public void setSpan(Object what, int start, int end, int flags) {
//         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
//         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
//         */
//        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
//        mTextView.setText(spannableString);
    }

    @Override
    public void onFinish() {
        mTextView.setText("重新获取验证码");
        mTextView.setClickable(true);//重新获得点击
        mTextView.setEnabled(true);
        mTextView.setTextColor(context.getResources().getColorStateList(R.color.text_color_selector));
    }
}
