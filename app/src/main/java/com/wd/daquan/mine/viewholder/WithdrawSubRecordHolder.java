package com.wd.daquan.mine.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * 交易记录的子列表
 */
public class WithdrawSubRecordHolder extends RecyclerView.ViewHolder{
    public TextView withdrawSubRecordNname;
    public TextView withdrawSubRecordDate;
    public TextView withdrawSubRecordAmount;
    public TextView withdrawSubRecordStatus;
    public WithdrawSubRecordHolder(View itemView) {
        super(itemView);
        withdrawSubRecordNname = itemView.findViewById(R.id.withdraw_sub_record_name);
        withdrawSubRecordDate = itemView.findViewById(R.id.withdraw_sub_record_date);
        withdrawSubRecordAmount = itemView.findViewById(R.id.withdraw_sub_record_amount);
        withdrawSubRecordStatus = itemView.findViewById(R.id.withdraw_sub_record_status);
    }
}