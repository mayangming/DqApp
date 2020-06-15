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
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * 对于红包活动的提示
 */
public class RedPackageTipDialog extends AppCompatDialogFragment {
    public final static String ACTION_TIP_CONTENT = "tipContent";
    private TextView tipContent;
    private View redPackageTipLogo;
    private View redPackageClose;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//需要这一行来解决对话框背景有白色的问题(颜色随主题变动)
        return inflater.inflate(R.layout.red_package_tip_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tipContent = view.findViewById(R.id.red_package_content);
        redPackageTipLogo = view.findViewById(R.id.red_package_tip_logo);
        redPackageClose = view.findViewById(R.id.red_package_close);
        Bundle bundle = getArguments();
        if (null != bundle){
            String tipContentStr = bundle.getString(ACTION_TIP_CONTENT,"无法获取最新的红包规则");
            tipContent.setText(tipContentStr);
        }

        redPackageTipLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        redPackageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}