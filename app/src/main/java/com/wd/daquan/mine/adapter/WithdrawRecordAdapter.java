package com.wd.daquan.mine.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.da.library.adapter.CommRecyclerViewAdapter;
import com.da.library.utils.BigDecimalUtils;
import com.wd.daquan.R;
import com.wd.daquan.model.bean.WithdrawRecordBean;
import com.wd.daquan.model.bean.WithrawCountEntity;

import java.util.Locale;
import java.util.Map;

/**
 * 提现列表
 */
public class WithdrawRecordAdapter extends CommRecyclerViewAdapter<WithdrawRecordBean, WithdrawRecordHolder> {
    @Override
    protected WithdrawRecordHolder onBindView(ViewGroup parent, int viewType) {
        return new WithdrawRecordHolder(mInflater.inflate(R.layout.item_withdraw_record,parent,false));
    }

    @Override
    protected void onBindData(@NonNull WithdrawRecordHolder holder, int position) {
        super.onBindData(holder, position);
        WithdrawRecordBean recordBean = allList.get(position);
        Map<String, WithrawCountEntity> withrawCountEntityMap = recordBean.getWithrawCountEntityMap();
        String date = String.valueOf(recordBean.getYear()).concat("年").concat(String.valueOf(recordBean.getMonth())).concat("月");
        String dateStr = String.valueOf(recordBean.getYear()).concat("-").concat(String.format(Locale.CHINA,"%02d",recordBean.getMonth()));
        WithrawCountEntity withrawCountEntity = withrawCountEntityMap.get(dateStr);
        String expenditureAmountStr = BigDecimalUtils.penny2Dollar(withrawCountEntity.getExpenditure()).toPlainString();
        String incomeAmountStr = BigDecimalUtils.penny2Dollar(withrawCountEntity.getIncome()).toPlainString();
        String expenditureAmount = "支出￥ ".concat(expenditureAmountStr).concat("元");
        String incomeAmount = "收入￥ ".concat(incomeAmountStr).concat("元");
        holder.withdrawDate.setText(date);
        holder.withdrawExpenditureAmount.setText(expenditureAmount);
        holder.withdrawIncomeAmount.setText(incomeAmount);
        holder.withdrawDateLayout.setOnClickListener(v -> itemOnClickForView.itemOnClickForView(position,recordBean,v.getId()));
        holder.withdrawSub.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        WithdrawSubRecordAdapter withdrawSubRecordAdapter = new WithdrawSubRecordAdapter();
        withdrawSubRecordAdapter.addLists(recordBean.getRecordSubBeans());
        holder.withdrawSub.setAdapter(withdrawSubRecordAdapter);
    }
}