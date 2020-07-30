package com.da.library.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.da.library.R;
import com.da.library.adapter.CommonListDialogAdapter;
import com.da.library.listener.ICommListDialogAdapterListener;
import com.da.library.listener.ICommListDialogListener;

import java.util.List;

/**
 * Author: 方志
 * Time: 2018/5/23 16:06
 * Description: 列表的dialog
 */
public class CommonListDialog extends Dialog {

    private RecyclerView mRecyclerView;
    private CommonListDialogAdapter mAdapter = null;
    private ICommListDialogListener mListener = null;

    public CommonListDialog(@NonNull Context context) {
        this(context, R.style.common_dialog);
    }

    public CommonListDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mAdapter = new CommonListDialogAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comm_list_dialog);
        mRecyclerView = findViewById(R.id.recycler_view);

        Window dialogWindow = this.getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        mRecyclerView.measure(0, 0);
        lp.width = getContext().getResources().getDisplayMetrics().widthPixels / 2;
        dialogWindow.setAttributes(lp);

        initData();
        initListener();
    }


    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 添加数据
     *
     * @param list 集合数据
     */
    public void setItems(List<String> list) {
        mAdapter.update(list);
    }

    public void clear() {
        mAdapter.clear();
    }

    /**
     * 添加数据
     *
     * @param str 单个数据
     */
    public void setItem(String str) {
        mAdapter.add(str);
    }

    /**
     * 通知数据刷新
     */
    public void notifyChange() {
        mAdapter.notifyDataSetChanged();
    }


    private void initListener() {
        mAdapter.setListener(new ICommListDialogAdapterListener() {
            @Override
            public void onItemClick(String item, int position) {
                if(null != mListener) {
                    mListener.onItemClick(item, position);
                }
                dismiss();
            }
        });
    }


    public void show() {
        if (isValidContext() && !this.isShowing()) {
            super.show();
        }
    }

    private boolean isValidContext() {
        if (getContext() instanceof Activity) {
            if (((Activity) getContext()).isFinishing()) {
                return false;
            }
        }
        return true;
    }

    public void dismiss() {
        if (isValidContext()) {
            super.dismiss();
        }
    }

    public void setListener(ICommListDialogListener mListener) {
        this.mListener = mListener;
    }
}
