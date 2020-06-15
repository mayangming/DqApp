package com.wd.daquan.mine.wallet.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.wd.daquan.R;
import com.da.library.controls.recyclerholder.ExBaseQuickAdapter;
import com.wd.daquan.mine.wallet.bean.RedDetailsBean;
import com.wd.daquan.mine.wallet.holder.RedDetailsHeaderHolder;
import com.wd.daquan.mine.wallet.holder.RedDetailsHolder;

import java.util.List;

public class RedDetailsListAdapter extends ExBaseQuickAdapter<RedDetailsBean, BaseViewHolder> {

    private Context contexts;
    private List<RedDetailsBean> data;
    private final int ITEM_HEADER = 1;
    private final int ITEM_BODY = 2;
    private OnRedDetailsListListener mOnRedDetailsListListener;
    public RedDetailsListAdapter(Context context, List<RedDetailsBean> data) {
        super(data);
        this.contexts = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_HEADER;
        } else if (position <= mData.size()) {
            return ITEM_BODY;
        }
        return ITEM_BODY;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (ITEM_HEADER == viewType) {
            return new RedDetailsHeaderHolder(LayoutInflater.from(contexts).inflate(R.layout.reddetails_listview_headview, parent));
        } else {
            return new RedDetailsHolder(LayoutInflater.from(contexts).inflate(R.layout.item_redpager_list, parent));
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, RedDetailsBean item) {
        if (holder == null || item == null) {
            return;
        }
        switch (getItemViewType(getParentPosition(item))) {
            case ITEM_HEADER:
                RedDetailsHeaderHolder headerHolder = (RedDetailsHeaderHolder) holder;
                setDetailsHeaderData(headerHolder);
                break;
            case ITEM_BODY:
                RedDetailsHolder redDetailsHolder = (RedDetailsHolder) holder;
                setDetailsData(redDetailsHolder, item);
                break;
        }
    }

    private void setDetailsHeaderData(RedDetailsHeaderHolder headerHolder){

    }

    private void setDetailsData(RedDetailsHolder holder, RedDetailsBean item){
        if(item == null) return;
        holder.name.setText(item.nickname);
        holder.money.setText(item.amount+"元");

        if(!TextUtils.isEmpty(item.receive_time)) {
            holder.time.setText(item.receive_time);
        }else {
            holder.time.setText(item.receive_time);
        }

        holder.layout.setOnClickListener( view -> {
//                Intent intented=new Intent(context, RedDetailsActivity.class);
//                intented.putExtra(Constants.RED_ID, item.getRedpacket_id());
//                intented.putExtra(Constants.AMOUNT, item.getAmount());
//                contexts.startActivity(intented);
//                AnimUtils.enterAnimForActivity(context);

        });
    }

    public void clearData(){
        mData.clear();
        notifyDataSetChanged();
    }

    public void setOnRedDetailsListListener(OnRedDetailsListListener mOnRedDetailsListListener){
        this.mOnRedDetailsListListener = mOnRedDetailsListListener;
    }

    public interface OnRedDetailsListListener{
        void headerListener(RedDetailsHeaderHolder headerHolder);
        void itemListener();
    }

}
