package com.wd.daquan.mine.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * 交易记录的列表
 */
public class WithdrawRecordHolder extends RecyclerView.ViewHolder{
    View withdrawDateLayout;
    TextView withdrawDate;
    TextView withdrawExpenditureAmount;
    TextView withdrawIncomeAmount;
    RecyclerView withdrawSub;
    public WithdrawRecordHolder(View itemView) {
        super(itemView);
        withdrawDateLayout = itemView.findViewById(R.id.withdraw_date_layout);
        withdrawDate = itemView.findViewById(R.id.withdraw_date);
        withdrawExpenditureAmount = itemView.findViewById(R.id.withdraw_expenditure_amount);
        withdrawIncomeAmount = itemView.findViewById(R.id.withdraw_income_amount);
        withdrawSub = itemView.findViewById(R.id.withdraw_sub);
    }
}