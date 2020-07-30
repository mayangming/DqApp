package com.wd.daquan.mine.dialog;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.utils.SpannableStringUtils;

/**
 * VIP弹窗结果页
 *
 */
public class VipExchangeResultDialog extends AppCompatDialogFragment implements View.OnClickListener {
    public static final String VIP_EXCHANGE_RESULT_TYPE = "vipExchangeResultType";
    public static final String VIP_EXCHANGE_RESULT_COUNT = "vipExchangeResultCount";
    public static final String VIP_EXCHANGE_RESULT_TIME = "vipExchangeResultTime";
    private TextView vipExchangeResultTitle;
    private ImageView vipExchangeResultIcon;
    private View vipExchangeResultFailContent;
    private View vipExchangeResultSuccessContent;
    private View vipExchangeResultSure;
    private TextView vipExchangeResultSuccessTimeTv;
    private TextView vipExchangeResultSuccessCountTv;
    private int vipType = 0;//0 失败 1 成功
    private int exchangeCount = 0;//兑换数量
    private int exchangeTime = 0;//兑换天数
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_vip_exchange_result,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        switchContent(vipType);
    }

    /**
     * 初始化控件
     */
    private void initView(){
        vipExchangeResultTitle = getView().findViewById(R.id.dialog_vip_exchange_result_title);
        vipExchangeResultIcon = getView().findViewById(R.id.dialog_vip_exchange_result_icon);
        vipExchangeResultFailContent = getView().findViewById(R.id.dialog_vip_exchange_result_fail_content);
        vipExchangeResultSuccessContent = getView().findViewById(R.id.dialog_vip_exchange_result_success_content);
        vipExchangeResultSure = getView().findViewById(R.id.dialog_vip_exchange_result_sure);
        vipExchangeResultSuccessTimeTv = getView().findViewById(R.id.dialog_vip_exchange_result_success_time_tv);
        vipExchangeResultSuccessCountTv = getView().findViewById(R.id.dialog_vip_exchange_result_success_count_tv);
        vipExchangeResultSure.setOnClickListener(this);
    }

    private void initData(){
        Bundle bundle = getArguments();
        if (null == bundle) {
            return;
        }
        vipType = bundle.getInt(VIP_EXCHANGE_RESULT_TYPE,0);
        exchangeCount = bundle.getInt(VIP_EXCHANGE_RESULT_COUNT,0);
        exchangeTime = bundle.getInt(VIP_EXCHANGE_RESULT_TIME,0);
    }

    /**
     * 根据类型切换内容
     */
    private void switchContent(int vipType){
        if (0 == vipType){
            vipExchangeResultTitle.setText("兑换失败");
            vipExchangeResultIcon.setImageResource(R.mipmap.dialog_vip_exchange_fail);
            vipExchangeResultFailContent.setVisibility(View.VISIBLE);
            vipExchangeResultSuccessContent.setVisibility(View.GONE);

        }else if (1 == vipType){
            vipExchangeResultTitle.setText("兑换成功");
            vipExchangeResultIcon.setImageResource(R.mipmap.dialog_vip_exchange_success);
            vipExchangeResultFailContent.setVisibility(View.GONE);
            vipExchangeResultSuccessContent.setVisibility(View.VISIBLE);
            String timeResult = exchangeTime+"天";
            String countContent = getString(R.string.vip_exchange_count,exchangeCount);
            String timeContent = getString(R.string.vip_exchange_time,timeResult);
            int start = timeContent.lastIndexOf(timeResult);
            int end = start + timeResult.length();
            vipExchangeResultSuccessTimeTv.setText(SpannableStringUtils.addTextColor(timeContent,start,end,getResources().getColor(R.color.color_FF4A4A)));
            vipExchangeResultSuccessCountTv.setText(countContent);

        }
    }

    @Override
    public void onClick(View v) {
//        Toast.makeText(v.getContext(),"你点击成功了",Toast.LENGTH_LONG).show();
        dismiss();
    }
}