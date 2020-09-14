package com.wd.daquan.view;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.wd.daquan.R;
import com.wd.daquan.model.bean.DqGoodChangeEntity;

import java.util.List;
import java.util.Locale;

/**
 * 上下滚动的 textView
 */
public class ScrollTextView extends LinearLayout {
    private TextView mBannerTV1;
    private TextView mBannerTV2;
    private Handler handler;
    private boolean isShow = false;
    private int startY1, endY1, startY2, endY2;
    private Runnable runnable;
    private List<DqGoodChangeEntity> list;
    private int position = 0;
    private int offsetY = 100;
    private boolean hasPostRunnable = false;

    public ScrollTextView(Context context) {
        this(context, null);
    }

    public ScrollTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(context).inflate(R.layout.widget_scroll_text_layout, this);
        mBannerTV1 = view.findViewById(R.id.tv_banner1);
        mBannerTV2 = view.findViewById(R.id.tv_banner2);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                isShow = !isShow;
                if (position == list.size() - 1) {
                    position = 0;
                }

                if (isShow) {
//                    mBannerTV1.setText(list.get(position++));
//                    mBannerTV2.setText(list.get(position));
                    updateUI(list.get(position++),mBannerTV1);
                    updateUI(list.get(position),mBannerTV2);
                } else {
//                    mBannerTV2.setText(list.get(position++));
//                    mBannerTV1.setText(list.get(position));
                    updateUI(list.get(position++),mBannerTV2);
                    updateUI(list.get(position),mBannerTV1);
                }

                startY1 = isShow ? 0 : offsetY;
                endY1 = isShow ? -offsetY : 0;
                ObjectAnimator.ofFloat(mBannerTV1, "translationY", startY1, endY1).setDuration(300).start();

                startY2 = isShow ? offsetY : 0;
                endY2 = isShow ? 0 : -offsetY;
                ObjectAnimator.ofFloat(mBannerTV2, "translationY", startY2, endY2).setDuration(300).start();

                handler.postDelayed(runnable, 3000);
            }
        };
    }

    public List<DqGoodChangeEntity> getList() {
        return list;
    }

    public void setList(List<DqGoodChangeEntity> list) {
        this.list = list;

        //处理最后一条数据切换到第一条数据 太快的问题
        if (list.size() > 1) {
            list.add(list.get(0));
        }
    }

    public void startScroll() {
//        mBannerTV1.setText(list.get(0));
        updateUI(list.get(0),mBannerTV1);
        if (list.size() > 1) {
            if(!hasPostRunnable) {
                hasPostRunnable = true;
                //处理第一次进入 第一条数据切换第二条 太快的问题
                handler.postDelayed(runnable,3000);
            }
        } else {
            //只有一条数据不进行滚动
            hasPostRunnable = false;
//            mBannerTV1.setText(list.get(0));
        }
    }

    public void stopScroll() {
        handler.removeCallbacks(runnable);
        hasPostRunnable = false;
    }

    private void updateUI(DqGoodChangeEntity entity,TextView textView){
        String name = "播报:恭喜用户%s兑换到了%s";
        String result = String.format(Locale.getDefault(),name,entity.getUserName(),entity.getCommoditiesNmae());
        int count = result.length();//总长度
        int startIndex1 = result.indexOf(entity.getUserName());
        int endIndex1 = startIndex1 + entity.getUserName().length();
        int startIndex2 = result.indexOf(entity.getCommoditiesNmae());
        int endIndex2 = startIndex2 + entity.getCommoditiesNmae().length();
        SpannableString sStr = new SpannableString(result);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#EF5B40"));
        sStr.setSpan(colorSpan1, startIndex1, endIndex1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#EF5B40"));
        sStr.setSpan(colorSpan2, startIndex2, endIndex2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(sStr);
    }

}