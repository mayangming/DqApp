package com.wd.daquan.third.session.viewholder;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.da.library.tools.AESHelper;
import com.netease.nim.uikit.business.session.emoji.AndroidEmoji;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;

/**
 * @author: dukangkang
 * @date: 2018/9/21 16:27.
 * @description: todo ...
 */
public class QcTextViewHolder extends MsgViewHolderBase {
    protected TextView bodyTextView;

    public QcTextViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.text_viewholder_item;
    }

    @Override
    protected void inflateContentView() {
        bodyTextView = findViewById(R.id.text_viewholder_item_text);
    }

    @Override
    protected void bindContentView() {
        layoutDirection();
        bodyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });
        int textsize = DqApp.getInstance().getChatTextSize();
        bodyTextView.setTextSize((float) textsize);
        String displayText = AESHelper.decryptString(getDisplayText());
        SpannableStringBuilder spannable = new SpannableStringBuilder(displayText);
        AndroidEmoji.ensure(spannable);
        bodyTextView.setText(spannable);
//        MoonUtil.identifyFaceExpression(NimUIKit.getContext(), bodyTextView, displayText, ImageSpan.ALIGN_BOTTOM);
        bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        bodyTextView.setOnLongClickListener(longClickListener);
    }

    private void layoutDirection() {
        if (isReceivedMessage()) {
            bodyTextView.setBackgroundResource(NimUIKitImpl.getOptions().messageLeftBackground);
            bodyTextView.setTextColor(Color.BLACK);
            bodyTextView.setPadding(ScreenUtil.dip2px(15), ScreenUtil.dip2px(8), ScreenUtil.dip2px(10), ScreenUtil.dip2px(8));
        } else {
            bodyTextView.setBackgroundResource(NimUIKitImpl.getOptions().messageRightBackground);
            bodyTextView.setTextColor(Color.WHITE);
            bodyTextView.setPadding(ScreenUtil.dip2px(10), ScreenUtil.dip2px(8), ScreenUtil.dip2px(15), ScreenUtil.dip2px(8));
        }
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    protected String getDisplayText() {
        return message.getSourceContent();
    }
}
