package com.wd.daquan.mine.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.mine.viewholder.AlipayRedDetailsHeaderHolder;
import com.wd.daquan.mine.viewholder.AlipayRedDetailsHolder;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.mine.bean.AlipayRedDetailsEntity;
import com.wd.daquan.mine.bean.AlipayRedDetailsItemEntity;

import org.jetbrains.annotations.NotNull;

/**
 * Created by DELL on 2018/9/13.
 */

public class AlipayRedDetailsAdapter extends CommRecyclerViewAdapter<AlipayRedDetailsItemEntity, RecyclerView.ViewHolder> {

    private AlipayRedDetailsEntity alipayRedDetailsEntity;
    public void setHeaderData(AlipayRedDetailsEntity alipayRedDetailsEntity){
        this.alipayRedDetailsEntity = alipayRedDetailsEntity;
    }

    @Override
    protected RecyclerView.ViewHolder onBindView(ViewGroup parent, int viewType) {
        if(TYPE_HEADER_VIEW == viewType){
            return new AlipayRedDetailsHeaderHolder(mInflater.inflate(R.layout.item_alipay_red_details_header, parent, false));
        }else{
            return new AlipayRedDetailsHolder(mInflater.inflate(R.layout.item_alipay_red_details, parent, false));
        }

    }

    @Override
    protected void onBindData(@NotNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof AlipayRedDetailsHeaderHolder){
            AlipayRedDetailsHeaderHolder headerHolder = (AlipayRedDetailsHeaderHolder) holder;
            GlideUtils.loadHeader(mContext, alipayRedDetailsEntity.headpic, headerHolder.avatar);
            headerHolder.name.setText(alipayRedDetailsEntity.nickname);
            headerHolder.blessing.setText(alipayRedDetailsEntity.greetings);
            headerHolder.money.setVisibility(View.VISIBLE);
            headerHolder.moneyIns.setVisibility(View.VISIBLE);
            if(KeyValue.ZERO_STRING.equals(alipayRedDetailsEntity.sessionType)){
                headerHolder.money.setText(alipayRedDetailsEntity.amount);
            }else{
                if(TextUtils.isEmpty(alipayRedDetailsEntity.self_amount)){
                    headerHolder.money.setVisibility(View.GONE);
                    headerHolder.moneyIns.setVisibility(View.GONE);
                }else{
                    headerHolder.money.setText(alipayRedDetailsEntity.self_amount);
                }
            }
            if(TextUtils.isEmpty(alipayRedDetailsEntity.myaliuser)){
                headerHolder.ins.setVisibility(View.GONE);
            }else {
                headerHolder.ins.setVisibility(View.VISIBLE);
                headerHolder.ins.setText(mContext.getString(R.string.alipay_red_details_account) + alipayRedDetailsEntity.myaliuser);
            }

            headerHolder.listTitle.setText(setListTitle());
        }else {
            AlipayRedDetailsHolder holder1 = (AlipayRedDetailsHolder) holder;
            AlipayRedDetailsItemEntity data = allList.get(position - 1);
            GlideUtils.loadHeader(mContext, data.headpic, holder1.avatar);
            holder1.name.setText(data.nickname);
            holder1.time.setText(data.receive_time);
            holder1.money.setText(data.amount + mContext.getString(R.string.rmb));
        }
    }

    public static final int TYPE_HEADER_VIEW = 2;
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return allList.size() + 1;
    }

    private String setListTitle(){
        String title;
        if (alipayRedDetailsEntity.received_num.equals(alipayRedDetailsEntity.num)) {
            title = alipayRedDetailsEntity.num + "个红包,共" + alipayRedDetailsEntity.amount + "元";
        } else {
            String uid = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo().getString(EBSharedPrefUser.uid,"");
            if (uid.equals(alipayRedDetailsEntity.create_uid)) {
                title = "已领取" + alipayRedDetailsEntity.received_num + "/" + alipayRedDetailsEntity.num + "个,共" +
                        alipayRedDetailsEntity.received_amount + "/" + alipayRedDetailsEntity.amount + "元";
            }else{
                title = "已领取" + alipayRedDetailsEntity.received_num + "/" + alipayRedDetailsEntity.num + "个";
            }
        }
        return title;
    }
}
