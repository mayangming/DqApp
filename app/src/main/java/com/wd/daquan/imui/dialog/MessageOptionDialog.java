package com.wd.daquan.imui.dialog;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * 消息操作对话框
 */
public
class MessageOptionDialog extends BaseDialog{
    private TextView messageCopy;//消息复制
    private String content;//复制的内容
    public static final String MESSAGE_OPTION_KEY = "messageOptionKey";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_message_option,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view){
        messageCopy = view.findViewById(R.id.message_copy);
        messageCopy.setOnClickListener(this::onClick);
    }

    private void initData(){
        content = getArguments().getString(MESSAGE_OPTION_KEY);
    }

    private void onClick(View view){
        switch (view.getId()){
            case R.id.message_copy:
                if (null != messageOptionIpc){
                    messageOptionIpc.result(content);
                }
                dismiss();
                break;
        }
    }

    MessageOptionIpc messageOptionIpc;

    public void setMessageOptionIpc(MessageOptionIpc messageOptionIpc) {
        this.messageOptionIpc = messageOptionIpc;
    }

    public interface MessageOptionIpc{
        void result(String content);//返回的结果
    }
}