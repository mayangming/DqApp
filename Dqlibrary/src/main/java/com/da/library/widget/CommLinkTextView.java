package com.da.library.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by DELL on 2018/9/10.
 */

public class CommLinkTextView extends ClickableSpan {
    private Context context;
    private OnCommLinkTextViewClick mOnCommLinkTextViewClick;
    public CommLinkTextView(Context context) {
        this.context = context;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        //设置文本的颜色
        ds.setColor(Color.parseColor("#60b284"));
        //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线,其实默认也是true，如果要下划线的话可以不设置
        ds.setUnderlineText(false);
    }

    //点击事件，自由操作
    @Override
    public void onClick(View widget) {
        if(mOnCommLinkTextViewClick != null){
            mOnCommLinkTextViewClick.onLinkTextViewClick();
        }
    }

    public void setOnCommLinkTextViewClick(OnCommLinkTextViewClick mOnCommLinkTextViewClick){
        this.mOnCommLinkTextViewClick = mOnCommLinkTextViewClick;
    }
    public interface OnCommLinkTextViewClick{
        void onLinkTextViewClick();
    }
}
