package com.wd.daquan.mine.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.R;
import com.wd.daquan.model.bean.VipCommodityEntity;

public class VipCardAdapter extends CommRecyclerViewAdapter<VipCommodityEntity, VipCardHolder> {
    @Override
    protected VipCardHolder onBindView(ViewGroup parent, int viewType) {
        return new VipCardHolder(mInflater.inflate(R.layout.vip_item_card,parent,false));
    }

    @Override
    protected void onBindData(@NonNull VipCardHolder holder, int position) {
        super.onBindData(holder, position);
        VipCommodityEntity vipCardEntity = allList.get(position);
        holder.vipCardTitle.setText(vipCardEntity.getSubject());
        holder.vipCardPrice.setText("￥".concat(vipCardEntity.getShowAmount()));
        holder.vipCardRemark.setText(vipCardEntity.getRemark());
        if (vipCardEntity.isSelected()){
            holder.itemView.setBackgroundResource(R.mipmap.vip_price_checked);
        }else {
            holder.itemView.setBackgroundResource(R.mipmap.vip_price_uncheck);
        }
    }
}