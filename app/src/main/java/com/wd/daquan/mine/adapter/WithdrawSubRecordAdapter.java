package com.wd.daquan.mine.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.da.library.adapter.CommRecyclerViewAdapter;
import com.da.library.utils.BigDecimalUtils;
import com.da.library.utils.DateUtil;
import com.wd.daquan.R;
import com.wd.daquan.model.bean.WithdrawRecordBean;

/**
 * 提现子列表适配器
 */
public class WithdrawSubRecordAdapter extends CommRecyclerViewAdapter<WithdrawRecordBean.WithdrawRecordSubBean, WithdrawSubRecordHolder> {
    @Override
    protected WithdrawSubRecordHolder onBindView(ViewGroup parent, int viewType) {
        return new WithdrawSubRecordHolder(mInflater.inflate(R.layout.item_withdraw_record_sub,parent,false));
    }

    @Override
    protected void onBindData(@NonNull WithdrawSubRecordHolder holder, int position) {
        super.onBindData(holder, position);
        WithdrawRecordBean.WithdrawRecordSubBean withdrawRecordSubBean = allList.get(position);

        String amountResult;
        String amountStr = BigDecimalUtils.penny2Dollar(withdrawRecordSubBean.getTotalAmount()).toPlainString();
        if ("0".equals(withdrawRecordSubBean.getRecordType())){//存入
            amountResult = "+".concat(amountStr);
        }else {//取出
            amountResult = "-".concat(amountStr);
        }

        String payStatus;
        if ("1".equals(withdrawRecordSubBean.getStatus())){
            payStatus = "成功";
        }else if ("2".equals(withdrawRecordSubBean.getStatus())){
            payStatus = "处理中";
        }else {
            payStatus = "失败";
        }
        long timeLong = DateUtil.string2Long(withdrawRecordSubBean.getRecordTimeStr());
        String timeStr = DateUtil.getDateToString(timeLong,DateUtil.CN_FORMAT2);
        holder.withdrawSubRecordAmount.setText(amountResult);
        holder.withdrawSubRecordDate.setText(timeStr);
        holder.withdrawSubRecordNname.setText(withdrawRecordSubBean.getSubject());
        holder.withdrawSubRecordStatus.setText(payStatus);
    }
}