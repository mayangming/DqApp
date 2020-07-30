package com.wd.daquan.mine.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.mine.bean.AlipayRedListEntity;

import org.jetbrains.annotations.NotNull;

/**
 * Created by DELL on 2018/9/13.
 */

public class AlipayRedListAdapter extends CommRecyclerViewAdapter<AlipayRedListEntity.ListBean, RecyclerView.ViewHolder> {
    private Context mContext;
    private OnAlipayDetailsListener mOnAlipayDetailsListener;
    private String type;

    @Override
    protected RecyclerView.ViewHolder onBindView(ViewGroup parent, int viewType) {
        return new AlipayRedListHolder(mInflater.inflate(R.layout.item_alipay_details_list, parent, false));
    }

    @Override
    protected void onBindData(@NotNull RecyclerView.ViewHolder holder, int position) {
        AlipayRedListEntity.ListBean data = allList.get(position);
        AlipayRedListHolder alipayDetailsHolder = (AlipayRedListHolder) holder;
        alipayDetailsHolder.name.setText(data.nickname);
        alipayDetailsHolder.money.setText(data.amount + "元");
        if(KeyValue.ONE_STRING.equals(type)) {
            alipayDetailsHolder.time.setText(data.create_time);
        }else if(KeyValue.TWO_STRING.equals(type)) {
            alipayDetailsHolder.time.setText(data.receive_time);
        }

        alipayDetailsHolder.layout.setOnClickListener(view -> {
            if (null != mOnAlipayDetailsListener){
                mOnAlipayDetailsListener.onLayout(data);
            }
        });
    }

    public void setType(String type){
        this.type = type;
    }
    public void setOnAlipayDetailsListener(OnAlipayDetailsListener mOnAlipayDetailsListener){
        this.mOnAlipayDetailsListener = mOnAlipayDetailsListener;
    }
    public interface OnAlipayDetailsListener{
        void onLayout(AlipayRedListEntity.ListBean data);
    }
}
