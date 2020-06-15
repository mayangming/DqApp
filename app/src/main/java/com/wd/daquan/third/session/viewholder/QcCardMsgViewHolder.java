package com.wd.daquan.third.session.viewholder;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.da.library.tools.Utils;
import com.dq.im.type.ImType;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.third.session.extension.CardAttachment;

/**
 * @author: dukangkang
 * @date: 2018/9/10 14:09.
 * @description: todo ...
 */
public class QcCardMsgViewHolder extends MsgViewHolderBase {

    private CardAttachment mCardAttachment;
    private RelativeLayout mRlyt;
    private ImageView mIcon;
    private TextView mName;
    private TextView mTips;

    public QcCardMsgViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.card_viewholder_item;
    }

    @Override
    protected void inflateContentView() {
        mRlyt = view.findViewById(R.id.card_item_llyt);
        mName = view.findViewById(R.id.card_item_name);
        mTips = view.findViewById(R.id.card_item_tips);
        mIcon = view.findViewById(R.id.card_item_icon);
    }

    @Override
    protected void bindContentView() {
        mCardAttachment = (CardAttachment) message.getContentData();

        GlideUtils.loadHeader(DqApp.sContext, mCardAttachment.headPic, mIcon);
        mName.setText(mCardAttachment.nickName);
        mTips.setText("个人名片");
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        if (message == null) {
            return;
        }

        if (Utils.isFastDoubleClick(500)) {
            return;
        }

        CardAttachment cardAttachment = (CardAttachment) message.getContentData();
        if (cardAttachment == null) {
            return;
        }

        if (ImType.typeOfValue(message.getType()) == ImType.P2P) {
            NavUtils.gotoUserInfoActivity(context, cardAttachment.userId, message.getMsgType());
        } else {
            NavUtils.gotoUserInfoActivity(context, cardAttachment.userId,  message.getMsgType(), message.getMsgIdServer(),
                false, false, true, false);

        }
    }

    @Override
    protected int leftBackground() {
        return R.color.transparent;
    }

    @Override
    protected int rightBackground() {
        return R.color.transparent;
    }
}
