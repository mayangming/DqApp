package com.wd.daquan.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * 交易记录的子列表
 */
public class WithdrawSubRecordHolder extends RecyclerView.ViewHolder{
    TextView withdrawSubRecordNname;
    TextView withdrawSubRecordDate;
    TextView withdrawSubRecordAmount;
    TextView withdrawSubRecordStatus;
    public WithdrawSubRecordHolder(View itemView) {
        super(itemView);
        withdrawSubRecordNname = itemView.findViewById(R.id.withdraw_sub_record_name);
        withdrawSubRecordDate = itemView.findViewById(R.id.withdraw_sub_record_date);
        withdrawSubRecordAmount = itemView.findViewById(R.id.withdraw_sub_record_amount);
        withdrawSubRecordStatus = itemView.findViewById(R.id.withdraw_sub_record_status);
    }
}