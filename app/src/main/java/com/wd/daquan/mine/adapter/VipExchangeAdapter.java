package com.wd.daquan.mine.adapter;

import androidx.annotation.NonNull;
import android.view.ViewGroup;

import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.R;
import com.wd.daquan.model.bean.ExchangeVipListBean;

/**
 * Vip兑换适配器
 */
public class VipExchangeAdapter extends CommRecyclerViewAdapter<ExchangeVipListBean, VipExchangeHolder> {
    @Override
    protected VipExchangeHolder onBindView(ViewGroup parent, int viewType) {
        return new VipExchangeHolder(mInflater.inflate(R.layout.item_vip_exchange,parent,false));
    }

    @Override
    protected void onBindData(@NonNull VipExchangeHolder holder, int position) {
        super.onBindData(holder, position);
        ExchangeVipListBean vipExchangeItemBean = allList.get(position);
        holder.vipExchangeTitle.setText(vipExchangeItemBean.getExchangeName());
        holder.vipExchangeCount.setText("-".concat(String.valueOf(vipExchangeItemBean.getBeExchangeNum()).concat("人数")));
        holder.vipExchangeBtn.setOnClickListener(v -> itemOnClickForView.itemOnClickForView(position,vipExchangeItemBean,v.getId()));
    }
}
