package com.wd.daquan.mine.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.R;
import com.wd.daquan.common.utils.NavUtils;

/**
 * 引导用户开通VIP的弹框
 */
public class GuideOpenVipDialog extends AppCompatDialogFragment {
    private View closeIv;//关闭窗口
    private View openVipBtn;//开通VIP的窗口
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//需要这一行来解决对话框背景有白色的问题(颜色随主题变动)
        return inflater.inflate(R.layout.guide_open_vip_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closeIv = view.findViewById(R.id.guide_open_vip_close_iv);
        openVipBtn = view.findViewById(R.id.guide_open_vip_ll);
        initListener();
    }
    private void initListener(){
        closeIv.setOnClickListener(this::onClick);
        openVipBtn.setOnClickListener(this::onClick);
    }

    private void onClick(View view){
        switch (view.getId()){
            case R.id.guide_open_vip_close_iv:
                dismiss();
                break;
            case R.id.guide_open_vip_ll:
                NavUtils.gotoVipActivity(view.getContext());
                dismiss();
                break;
        }
    }
}