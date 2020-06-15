package com.da.library.view.custom.keyboard;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.da.library.R;

/**
 * 数字键盘
 * Created by Kind on 2019-05-21.
 */
public class KeyboardGridAdapter extends BaseQuickAdapter<KeybordModel, BaseViewHolder> {

    public KeyboardGridAdapter() {
        super(R.layout.keyboard_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, KeybordModel item) {
        if ("delete".equals(item.key)) {
            helper.setVisible(R.id.keyboard_item_delete, true);
            helper.setVisible(R.id.keyboard_item_key, false);
            helper.setBackgroundRes(helper.itemView.getId(), R.drawable.pay_key_board_delete_selector);
        } else if (TextUtils.isEmpty(item.key)) {
            helper.setGone(R.id.keyboard_item_delete, false);
            helper.setBackgroundRes(helper.itemView.getId(), R.drawable.pay_key_board_delete_selector);
            helper.setVisible(R.id.keyboard_item_key, false);
        } else {
            helper.setGone(R.id.keyboard_item_delete, false);
            helper.setBackgroundRes(helper.itemView.getId(), R.drawable.comm_item_selector);
            helper.setVisible(R.id.keyboard_item_key, true);
            helper.setText(R.id.keyboard_item_key,item.key);
        }
    }
}
