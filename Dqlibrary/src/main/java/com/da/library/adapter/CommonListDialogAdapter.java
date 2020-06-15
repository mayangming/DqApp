package com.da.library.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.da.library.holder.ListDialogItemViewHolder;
import com.da.library.listener.ICommListDialogAdapterListener;
import com.da.library.R;

/**
 * Author: 方志
 * Time: 2018/5/23 16:27
 * Description:
 */
public class CommonListDialogAdapter extends CommRecyclerViewAdapter<String, ListDialogItemViewHolder> {
    private ICommListDialogAdapterListener mListener = null;

    @Override
    protected ListDialogItemViewHolder onBindView(ViewGroup parent, int viewType) {
        return new ListDialogItemViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_dialog_adapter_item, parent, false));
    }

    @Override
    protected void onBindData(ListDialogItemViewHolder holder, final int position) {
        if(position < allList.size() - 1) {
            holder.line.setVisibility(View.VISIBLE);
        }else {
            holder.line.setVisibility(View.GONE);
        }
        final String item = getItem(position);
        holder.name.setText(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mListener) {
                    mListener.onItemClick(item, position);
                }
            }
        });
    }

    public void setListener(ICommListDialogAdapterListener mListener) {
        this.mListener = mListener;
    }
}
