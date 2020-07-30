package com.wd.daquan.mine.dialog;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.SpannableStringUtils;

/**
 * 首页进入的协议对话框
 */
public class ProtocolDialog extends AppCompatDialogFragment{
    private DialogOperatorIpc dialogOperatorIpc;
    private TextView protocolContentTv;//协议提示内容
    private View protocolDisagree;//不同意按钮
    private View protocolAgree;//同意按钮
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_protocol,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        protocolContentTv = view.findViewById(R.id.protocol_content);
        protocolDisagree = view.findViewById(R.id.protocol_disagree);
        protocolAgree = view.findViewById(R.id.protocol_agree);
        protocolDisagree.setOnClickListener(this::onClick);
        protocolAgree.setOnClickListener(this::onClick);
        initProtocolContent();
        setCancelable(false);
    }

    public void setDialogOperatorIpc(DialogOperatorIpc dialogOperatorIpc) {
        this.dialogOperatorIpc = dialogOperatorIpc;
    }

    private void onClick(View view){
        switch (view.getId()){
            case R.id.protocol_disagree:
                if (null != dialogOperatorIpc){
                    dialogOperatorIpc.cancel();
                }
                dismiss();
                break;
            case R.id.protocol_agree:
                if (null != dialogOperatorIpc){
                    dialogOperatorIpc.sure();
                }
                dismiss();
                break;
        }
    }

    /**
     * 初始化协议内容
     */
    private void initProtocolContent(){
        String protocolContent = getResources().getString(R.string.protocol_content_splash);
        String userServiceContent = "《用户服务协议》";
        String userPrivacyContent = "《用户隐私协议》";
        int userServiceStartIndex = protocolContent.indexOf(userServiceContent);
        int userServiceEndIndex = userServiceStartIndex + userServiceContent.length();
        int userPrivacyStartIndex = protocolContent.indexOf(userPrivacyContent);
        int userPrivacyEndIndex = userPrivacyStartIndex + userPrivacyContent.length();
        SpannableStringBuilder sb =  SpannableStringUtils.addTextColor(protocolContent,userServiceStartIndex,userServiceEndIndex,userPrivacyStartIndex,userPrivacyEndIndex,R.color.color_5680FA);
        sb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                NavUtils.gotoWebviewActivity(widget.getContext(), DqUrl.url_register_agreement, getString(R.string.serviceUser));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                //去掉可点击文字的下划线
                ds.setUnderlineText(false);
            }
        },userServiceStartIndex,userServiceEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                NavUtils.gotoWebviewActivity(widget.getContext(), DqUrl.url_privacy_agreement, getString(R.string.privacyUser));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                //去掉可点击文字的下划线
                ds.setUnderlineText(false);
            }
        },userPrivacyStartIndex,userPrivacyEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        protocolContentTv.setText(sb);
        // 设置此方法后，点击事件才能生效
        protocolContentTv.setMovementMethod(LinkMovementMethod.getInstance());
    }


    /**
     * 操作监听
     */
    public interface DialogOperatorIpc{
        void sure();
        void cancel();
    }
}