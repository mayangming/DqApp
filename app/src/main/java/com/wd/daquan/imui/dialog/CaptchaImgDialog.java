package com.wd.daquan.imui.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.da.library.constant.IConstant;
import com.da.library.tools.MD5;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.CaptchaBean;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.util.TToast;

import java.util.HashMap;

/**
 * 图形验证码对话框
 */
public class CaptchaImgDialog extends BaseDialog {
    public static final String PHONE_NUMBER = "phoneNumber";
    private ImageView captchaMask;//缺失的滑块
    private ImageView captchaBg;//滑块全图
    private TextView btnCancel,btnSure;
    private AppCompatSeekBar captchaSeek;
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
        captchaBg = view.findViewById(R.id.captcha_img);
        captchaMask = view.findViewById(R.id.captcha_mask);
        captchaSeek = view.findViewById(R.id.captcha_seek);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnSure = view.findViewById(R.id.btn_sure);
        btnCancel.setOnClickListener(this::onClick);
        btnSure.setOnClickListener(this::onClick);
        captchaSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateCaptchMaskLocation(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                DqLog.e("YM--->停止滑动:停留位置:"+seekBar.getProgress());
                verifyImageCode(seekBar.getProgress());
            }
        });

    }

    private void onClick(View view){
        switch (view.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
//            case R.id.replace_captcha_img:
//                getCaptchaImg();
//                break;
            case R.id.btn_sure:
//                captchaValue = captchaEdt.getText().toString();
                if (TextUtils.isEmpty(captchaValue)){//数据为空的时候取消图形验证码的框
                    Toast.makeText(getContext(),"图形验证码不能为空!",Toast.LENGTH_SHORT).show();
                    return;
                }
//                operator.sure(captchaValue);
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
        paramsRetrofit.put("userId",phone);
        RetrofitHelp.getUserApi().captchaImg(DqUrl.url_get_getImageVerifyCode,RetrofitHelp.getRequestBodyByFromData(paramsRetrofit)).enqueue(new DqCallBack<DataBean<CaptchaBean>>(){
                @Override
            public void onSuccess(String url, int code, DataBean<CaptchaBean> entity) {
                Log.e("YM","获取的数据结果"+entity.toString());
                if (0 != entity.result){
                    TToast.show(getContext(),entity.content);
//                    dismiss();
                    return;
                }
                CaptchaBean captchaBean = entity.data;
                Log.e("YM","获取的数据结果"+captchaBean.toString());
                updateUI(captchaBean);
            }

            @Override
            public void onFailed(String url, int code, DataBean<CaptchaBean> entity) {
                Log.e("YM","获取的数据失败:"+entity.toString());
                if (0 != entity.result){
                    TToast.show(getContext(),entity.content);
//                    dismiss();
                    return;
                }
            }
        });
    }

    private void verifyImageCode(int xWidth){
        String key = IConstant.Login.CHATDQ + phone;
        String token_key = MD5.encrypt(key).toLowerCase();
        HashMap<String,String> paramsRetrofit = new HashMap<>();
        paramsRetrofit.put("xWidth", xWidth+"");
        paramsRetrofit.put("userId",phone);
        paramsRetrofit.put(IConstant.Login.TOKEN_KEY, token_key);
        RetrofitHelp.getUserApi().verifyImageCode(DqUrl.url_get_verifyImageCode,RetrofitHelp.getRequestBodyByFromData(paramsRetrofit)).enqueue(new DqCallBack<DataBean>(){
            @Override
            public void onSuccess(String url, int code, DataBean entity) {
                Log.e("YM","获取的数据结果"+entity.toString());
                if (0 == entity.result){
//                    TToast.show(getContext(),entity.data.toString());
                    dismiss();
                    operator.sure();
                    return;
                }
            }

            @Override
            public void onFailed(String url, int code, DataBean entity) {
                Log.e("YM","获取的数据失败:"+entity.toString());
                if (0 != entity.result){
                    TToast.show(getContext(),entity.content);
                    return;
                }
            }
        });
    }

    /**
     * 修改滑块的位置
     */
    private void updateCaptchMaskLocation(int x){
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) captchaMask.getLayoutParams();
        layoutParams.leftMargin = x;
        captchaMask.setLayoutParams(layoutParams);
    }

    private void updateUI(CaptchaBean captchaBean){
//        GlideUtil.loadNormalImgByNet(getContext(),imgUrl,captchaImg);

        ViewGroup.MarginLayoutParams oriLayoutParams = (ViewGroup.MarginLayoutParams) captchaBg.getLayoutParams();
        oriLayoutParams.height = captchaBean.getOriImageHeight();
        oriLayoutParams.width = captchaBean.getOriImageWidth();
        captchaBg.setLayoutParams(oriLayoutParams);

        ViewGroup.MarginLayoutParams tempLayoutParams = (ViewGroup.MarginLayoutParams) captchaMask.getLayoutParams();
        tempLayoutParams.height = captchaBean.getTemplateHeight();
        tempLayoutParams.width = captchaBean.getTemplateWidth();
        tempLayoutParams.topMargin = captchaBean.getyHeight();
        captchaMask.setLayoutParams(tempLayoutParams);

        ViewGroup.MarginLayoutParams seekLayoutParams = (ViewGroup.MarginLayoutParams) captchaSeek.getLayoutParams();
        seekLayoutParams.width = captchaBean.getOriImageWidth();
        captchaSeek.setLayoutParams(seekLayoutParams);
        captchaSeek.setMax(captchaBean.getOriImageWidth() - captchaBean.getTemplateWidth());

        GlideUtils.load(getContext(),captchaBean.getBigImage(),captchaBg);
        GlideUtils.load(getContext(),captchaBean.getSmallImage(),captchaMask);
    }

    private Operator operator;

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public interface Operator{
        void cancel();
        void sure();
    }

}