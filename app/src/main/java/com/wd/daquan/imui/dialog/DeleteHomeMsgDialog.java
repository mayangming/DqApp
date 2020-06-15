package com.wd.daquan.imui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dq.im.model.HomeImBaseMode;
import com.dq.im.viewmodel.HomeMessageViewModel;
import com.wd.daquan.R;

/**
 * 首页删除消息的对话框
 */
public class DeleteHomeMsgDialog extends BaseDialog{
    public static final String MSG = "msg";
    private HomeImBaseMode homeImBaseMode;
    private View deleteBtn;
    private HomeMessageViewModel homeMessageViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_delete_home_msg,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initViewModel();
        initData();
    }

    private void initView(View view){
        deleteBtn = view.findViewById(R.id.delete_msg);
        deleteBtn.setOnClickListener(this::onClick);
    }

    private void initViewModel(){
        homeMessageViewModel = ViewModelProviders.of(this).get(HomeMessageViewModel.class);
    }

    private void initData(){
        homeImBaseMode = (HomeImBaseMode)getArguments().getSerializable(MSG);
    }

    private void onClick(View view){
        switch (view.getId()){
            case R.id.delete_msg:
                homeMessageViewModel.deleteForUserId(homeImBaseMode);
                dismiss();
                break;
        }
    }
}