package com.wd.daquan.mine.dialog;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.daquan.R;

/**
 * 通用对话框
 */
public class NormalDialog extends AppCompatDialogFragment implements View.OnClickListener {
    private TextView title;
    private TextView content;
    private View cancelBtn;
    private View sureBtn;

    private DialogOperatorIpc dialogOperatorIpc;
    private Build build;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_normal,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.dialog_normal_title);
        content = view.findViewById(R.id.dialog_normal_content);
        cancelBtn = view.findViewById(R.id.dialog_cancel);
        sureBtn = view.findViewById(R.id.dialog_sure);
        if (null == build){
            return;
        }
        title.setText(build.title);
        content.setText(build.content);
        initOnclick();
    }

    private void initOnclick(){
        cancelBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
    }

    public void setBuild(Build build) {
        this.build = build;
    }

    public Build getBuild(){
        return new Build();
    }

    public void setDialogOperatorIpc(DialogOperatorIpc dialogOperatorIpc) {
        this.dialogOperatorIpc = dialogOperatorIpc;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.dialog_cancel:
                if (null == dialogOperatorIpc){
                    break;
                }
                dialogOperatorIpc.cancel();
                break;
            case  R.id.dialog_sure:
                if (null == dialogOperatorIpc){
                    break;
                }
                dialogOperatorIpc.sure();
                break;
        }
    }

    /**
     * 操作监听
     */
    public interface DialogOperatorIpc{
        void sure();
        void cancel();
    }

    public class Build{
        private String title;//标题
        private String content;//内容

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}