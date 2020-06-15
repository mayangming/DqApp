package com.da.library.view.custom.keyboard;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.da.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义数字键盘
 * Created by Kind on 2019-05-22.
 */
public class KeyboardRecyclerView extends RecyclerView implements BaseQuickAdapter.OnItemClickListener {

    String[] keys = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "delete"};

    private KeyboardGridAdapter keyboardGridAdapter;
    private OnKeyboardClickListener clickListener;

    public KeyboardRecyclerView(Context context) {
        this(context, null);
    }

    public KeyboardRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setLayoutManager(new GridLayoutManager(context, 3));
        //设置分割线
        DividerItemDecoration dividerVertical = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        DividerItemDecoration dividerHorizontal = new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL);
        dividerVertical.setDrawable(context.getResources().getDrawable(R.drawable.divider_item_horizontal_shape));
        dividerHorizontal.setDrawable(context.getResources().getDrawable(R.drawable.divider_item_vertical_shape));
        addItemDecoration(dividerVertical);
        addItemDecoration(dividerHorizontal);

        keyboardGridAdapter = new KeyboardGridAdapter();
        setAdapter(keyboardGridAdapter);
        keyboardGridAdapter.setNewData(getKeybordModels());

        keyboardGridAdapter.setOnItemClickListener(this);
    }

    public void updateData() {
        keyboardGridAdapter.replaceData(getKeybordModels());
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        KeybordModel item = (KeybordModel) adapter.getItem(position);
        if(item == null || clickListener == null){
            return;
        }

        if ("delete".equals(item.key)) {
            clickListener.onKeyboardItemClick(KeyBordTypeEnum.delete, adapter, view, position);
        } else {
            clickListener.onKeyboardItemClick(KeyBordTypeEnum.comm, adapter, view, position);
        }
    }

    public void setOnKeyboardClickListener(@Nullable OnKeyboardClickListener clickListener) {
        this.clickListener = clickListener;
    }



    public List<KeybordModel> getKeybordModels() {
        List<KeybordModel> list = new ArrayList<>();
        for (String key : keys) {
            list.add(new KeybordModel(key));
        }
        return list;
    }

    public interface OnKeyboardClickListener {
        void onKeyboardItemClick(KeyBordTypeEnum typeEnum, BaseQuickAdapter adapter, View view, int position);
    }

    public enum KeyBordTypeEnum {
        /**
         * 普通
         */
        comm(1),
        /**
         * 删除
         */
        delete(2);

        private final int value;

        KeyBordTypeEnum(int value) {
            this.value = value;
        }
    }

}
