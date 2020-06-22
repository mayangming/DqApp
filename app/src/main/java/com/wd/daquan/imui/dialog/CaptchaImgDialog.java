package com.wd.daquan.imui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.util.TToast;

import java.util.HashMap;

/**
 * 图形验证码对话框
 */
public class CaptchaImgDialog extends BaseDialog {
    public static final String PHONE_NUMBER = "phoneNumber";
    private ImageView captchaImg;//图形验证码
    private EditText captchaEdt;//图形验证码输入框
    private TextView btnCancel,btnSure,replaceCaptchaImg;
    private String phone;
    private String captchaValue;//获取的验证码
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Operator){
            operator = (Operator)context;
        }else {
            throw new RuntimeException(context.toString() + " must implement ChatBottomOperatorIml");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_captcha_img,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        getCaptchaImg();
    }

    private void initData(){
        phone = getArguments().getString(PHONE_NUMBER);
        phone = phone.replaceAll(" ", "");
    }

    private void initView(View view){
        captchaImg = view.findViewById(R.id.captcha_img);
        captchaEdt = view.findViewById(R.id.captcha_edt);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnSure = view.findViewById(R.id.btn_sure);
        replaceCaptchaImg = view.findViewById(R.id.replace_captcha_img);
        btnCancel.setOnClickListener(this::onClick);
        btnSure.setOnClickListener(this::onClick);
        replaceCaptchaImg.setOnClickListener(this::onClick);

    }

    private void onClick(View view){
        switch (view.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.replace_captcha_img:
                getCaptchaImg();
                break;
            case R.id.btn_sure:
                captchaValue = captchaEdt.getText().toString();
                if (TextUtils.isEmpty(captchaValue)){//数据为空的时候取消图形验证码的框
                    Toast.makeText(getContext(),"图形验证码不能为空!",Toast.LENGTH_SHORT).show();
                    return;
                }
                operator.sure(captchaValue);
                dismiss();
                break;
        }
    }


    /**
     * 获取验证码图片
     */
    private void getCaptchaImg(){
        Log.e("YM","获取验证码数据,手机号--->phone:"+phone);
        HashMap<String,String> paramsRetrofit = new HashMap<>();
        paramsRetrofit.put("phone", phone);
        RetrofitHelp.getUserApi().getCommonRequest(DqUrl.url_get_captcha_img,RetrofitHelp.getRequestBodyByFromData(paramsRetrofit)).enqueue(new DqCallBack<DataBean<String>>(){
            @Override
            public void onSuccess(String url, int code, DataBean<String> entity) {
                Log.e("YM","获取的数据结果"+entity.toString());
                if (0 != entity.result){
                    TToast.show(getContext(),entity.content);
                    dismiss();
                    return;
                }
                String imgUrl = entity.data;
                Log.e("YM","获取的数据结果"+imgUrl);
                updateUI(imgUrl);
            }

            @Override
            public void onFailed(String url, int code, DataBean<String> entity) {
                Log.e("YM","获取的数据失败:"+entity.toString());
                if (0 != entity.result){
                    TToast.show(getContext(),entity.content);
                    dismiss();
                    return;
                }
            }
        });
    }

    private void updateUI(String imgUrl){
//        GlideUtil.loadNormalImgByNet(getContext(),imgUrl,captchaImg);
        GlideUtils.loadRound(getContext(),imgUrl,captchaImg,10);
    }

    private Operator operator;

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public interface Operator{
        void cancel();
        void sure(String value);
    }

}