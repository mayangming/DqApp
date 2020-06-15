package com.wd.daquan.mine.wallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wd.daquan.R;
import com.wd.daquan.mine.wallet.bean.LooseDetailsItemBean;
import com.wd.daquan.mine.wallet.holder.LooseChangeHolder;

import java.util.List;

public class LooseListAdapter extends BaseQuickAdapter<LooseDetailsItemBean, LooseChangeHolder> {

    Context contexts;
    List<LooseDetailsItemBean> data;
    public LooseListAdapter(Context context, List<LooseDetailsItemBean> data) {
        super(data);
        this.contexts = context;
    }

    @Override
    protected LooseChangeHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return new LooseChangeHolder(LayoutInflater.from(contexts).inflate(R.layout.loose_change_list_item, parent));
    }

    @Override
    protected void convert(LooseChangeHolder holder, LooseDetailsItemBean item) {
        if(holder == null || item == null){
            return;
        }
        switch (Integer.parseInt(item.oid_biz)){
            case 110001:
                holder.title.setText("个人红包");
                break;
            case 101001:
                holder.title.setText("群红包");
                break;
            case 202000:
                holder.title.setText("转账");
                break;
            case 301000:
                holder.title.setText("提现");
                break;
            case 301001:
                holder.title.setText("充值");
                break;
            case 301002:
                holder.title.setText("红包过期退回");
                break;
        }
        holder.time.setText(item.dt_billtrans);
        switch (Integer.parseInt(item.flag_dc)){
            case 1:
                holder.money.setTextColor(contexts.getResources().getColor(R.color.color_d84e43));
                holder.money.setText("+" + item.amt_inoccur);
                break;
            case 2:
                holder.money.setTextColor(contexts.getResources().getColor(R.color.app_theme_color));
                holder.money.setText("-" + item.amt_outoccur);
                break;
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(contexts, LooseChangeDetailActivity.class);
//                intent.putExtra(Constants.LOOSERESP,listcache.get(position));
//                contexts.startActivity(intent);
//                AnimUtils.enterAnimForActivity(contexts);
            }
        });
    }


    public void refreshLoose(List<LooseDetailsItemBean> cacheSetResp){
        data.clear();
        data.addAll(cacheSetResp);
    }
    public void addLoose(List<LooseDetailsItemBean> cacheSetResp){
        data.addAll(cacheSetResp);
    }
}
