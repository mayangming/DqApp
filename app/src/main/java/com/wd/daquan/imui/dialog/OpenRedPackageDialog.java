package com.wd.daquan.imui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dq.im.bean.im.MessageRedPackageBean;
import com.dq.im.constants.URLUtil;
import com.dq.im.constants.UserSpConstants;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.google.gson.Gson;
import com.wd.daquan.R;
import com.wd.daquan.bean.RedPackageDetailsBean;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.http.HttpBaseBean;
import com.wd.daquan.http.HttpResultResultCallBack;
import com.wd.daquan.http.ImSdkHttpUtils;
import com.wd.daquan.imui.type.RedPackageStatus;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.OpenRedPackageResultBean;
import com.wd.daquan.model.interfaces.DqCallBack;
import com.wd.daquan.model.retrofit.RetrofitHelp;
import com.wd.daquan.third.helper.UserInfoHelper;
import com.wd.daquan.util.GlideUtil;
import com.wd.daquan.util.IntentUtils;
import com.wd.daquan.util.TToast;
import com.white.easysp.EasySP;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 抢红包打开页面
 */
public  class OpenRedPackageDialog extends BaseDialog {
    public static final String RED_PACKAGE_DATA = "redPackageData";
    private String redPackageId = "";
    private String otherId = "";//发送红包方的Id

    private ImageView headIcon;//用户头像
    private TextView userName;//用户名字
    private TextView redPackageTitle;//红包内容
    private TextView redPackageTip;//红包提示内容
    private ImageView openIv;//红包打开按钮
    private ImMessageBaseModel imMessageBaseModel;
    private MessageRedPackageBean messageRedPackageBean;
    private RedPackageDetailsBean redPackageDetailsBean;
    private OpenRedPackageResultBean openRedPackageResultBean;
    private Gson gson = new Gson();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_open_red_package,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        updateView();
    }

    private void openRedPackage(){
        if (ImType.P2P.getValue().equals(messageRedPackageBean.getRedMsgType())){//个人模式
            String userId = EasySP.init(getContext()).getString(UserSpConstants.USER_ID);
            if (userId.equals(imMessageBaseModel.getFromUserId())){//自己发的
                requestRedPackageDetails(redPackageId);
            }else {//好友发的
                openRedPackageDetails(redPackageId);
            }
        }else {//群组模式,群组模式都开启抢红包模式
            openRedPackageDetails(redPackageId);
        }
    }

    private void initView(View view){
        headIcon = view.findViewById(R.id.head_icon);
        userName = view.findViewById(R.id.user_name);
        openIv = view.findViewById(R.id.red_package_open_iv);
        redPackageTitle = view.findViewById(R.id.red_package_title);
        redPackageTip = view.findViewById(R.id.red_package_tip);

        openIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRedPackage();
            }
        });
        redPackageTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.goRedPackageDetails(v.getContext(),messageRedPackageBean,imMessageBaseModel);
            }
        });
    }

    private void initData(){
        imMessageBaseModel = (ImMessageBaseModel)getArguments().getSerializable(RED_PACKAGE_DATA);
        parserRedPackageBean();
    }
    private void updateView(){
        userName.setText(UserInfoHelper.getUserNickName(otherId));
        GlideUtil.loadCircleImgByNet(getContext(),UserInfoHelper.getHeadPic(otherId),headIcon);
        RedPackageStatus redPackageStatus = RedPackageStatus.getRedPackageStatus(messageRedPackageBean.getStatus());
        switch (redPackageStatus){
            case RED_PACKAGE_UN_RECEIVE:
                openIv.setVisibility(View.VISIBLE);
                redPackageTip.setVisibility(View.GONE);
                redPackageTitle.setText("恭喜发财，大吉大利");
                break;
            case RED_PACKAGE_RECEIVED:
            case RED_PACKAGE_RECEIVE_END:
                openIv.setVisibility(View.GONE);
                redPackageTitle.setText("手慢了，红包抢完了");
                redPackageTip.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void parserRedPackageBean(){
        String content = imMessageBaseModel.getSourceContent();
        messageRedPackageBean = gson.fromJson(content,MessageRedPackageBean.class);
        redPackageId = messageRedPackageBean.getCouponId();
        otherId = imMessageBaseModel.getFromUserId();
    }
    /**
     * 获取红包详情的网络数据
     */
    private void requestRedPackageDetails(String couponId){
        Log.e("YM","获取红包详情数据");
        Gson gson = new Gson();
        Map<String,Object> params = new HashMap<>();
        params.put("couponId", couponId);
        ImSdkHttpUtils.postJson(false, URLUtil.RED_PACKAGE_DETAILS,params,new HttpResultResultCallBack<HttpBaseBean>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(HttpBaseBean response, int id) {
                String content = response.data.toString();
                if (0 == response.status){
                    redPackageDetailsBean = gson.fromJson(content,RedPackageDetailsBean.class);
                    messageRedPackageBean.setStatus(RedPackageStatus.RED_PACKAGE_RECEIVED.status);
                    updateUI(messageRedPackageBean,redPackageDetailsBean.getMoney());
                    if (null != openRedPackageResultListener){
                        String redPackageContent = gson.toJson(messageRedPackageBean);
                        imMessageBaseModel.setSourceContent(redPackageContent);
                        imMessageBaseModel.setContentData(messageRedPackageBean);
                        openRedPackageResultListener.redPackageResult(imMessageBaseModel);
                    }
                }else if (1 == response.status){//失败状态下会返回信息
                    openRedPackageResultBean = gson.fromJson(content,OpenRedPackageResultBean.class);
                    messageRedPackageBean.setStatus(openRedPackageResultBean.getStatus());
                    updateUI(messageRedPackageBean,openRedPackageResultBean.getMoney());
                    if (null != openRedPackageResultListener){
                        String redPackageContent = gson.toJson(messageRedPackageBean);
                        imMessageBaseModel.setSourceContent(redPackageContent);
                        imMessageBaseModel.setContentData(messageRedPackageBean);
                        openRedPackageResultListener.redPackageResult(imMessageBaseModel);
                    }
                }else {
                    Toast.makeText(getContext(),"红包详情接口获取失败:"+response.status,Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    /**
     * 获取抢红包的网络数据
     */
    private void openRedPackageDetails(String couponId){
        Log.e("YM","打开红包详情数据");
        HashMap<String,String> paramsRetrofit = new HashMap<>();
        paramsRetrofit.put("couponId", couponId);
        RetrofitHelp.getUserApi().createCouponHistory(DqUrl.url_red_package_coupon_history,RetrofitHelp.getRequestBodyByFromData(paramsRetrofit)).enqueue(new DqCallBack<DataBean<OpenRedPackageResultBean>>(){
            @Override
            public void onSuccess(String url, int code, DataBean<OpenRedPackageResultBean> entity) {
                Log.e("YM","获取的数据结果"+entity.toString());
                if (0 != entity.result){
                    TToast.show(getContext(),entity.content);
                    dismiss();
                    return;
                }
                openRedPackageResultBean = entity.data;
                messageRedPackageBean.setStatus(RedPackageStatus.RED_PACKAGE_RECEIVED.status);
                updateUI(messageRedPackageBean,openRedPackageResultBean.getMoney());
                if (null != openRedPackageResultListener){
                    String redPackageContent = gson.toJson(messageRedPackageBean);
                    imMessageBaseModel.setSourceContent(redPackageContent);
                    imMessageBaseModel.setContentData(messageRedPackageBean);
                    openRedPackageResultListener.redPackageResult(imMessageBaseModel);
                    IntentUtils.goRedPackageDetails(getContext(),messageRedPackageBean,imMessageBaseModel);
                    dismiss();
                }

            }

            @Override
            public void onFailed(String url, int code, DataBean<OpenRedPackageResultBean> entity) {
                Log.e("YM","获取的数据失败:"+entity.toString());
                openRedPackageResultBean = entity.data;
                if (0 != entity.result){
                    TToast.show(getContext(),entity.content);
                    dismiss();
                    return;
                }
                messageRedPackageBean.setStatus(openRedPackageResultBean.getStatus());
                updateUI(messageRedPackageBean,openRedPackageResultBean.getMoney());
                if (null != openRedPackageResultListener){
                    String redPackageContent = gson.toJson(messageRedPackageBean);
                    imMessageBaseModel.setSourceContent(redPackageContent);
                    imMessageBaseModel.setContentData(messageRedPackageBean);
                    openRedPackageResultListener.redPackageResult(imMessageBaseModel);
                }
            }
        });
    }

    /**
     * 更新用户数据
     */
    private void updateUI(MessageRedPackageBean messageRedPackageBean,String money){
//        String statusContent = RedPackageStatus.getRedPackageStatusDescription(messageRedPackageBean.getStatus());
//        redPackageResult.setText(statusContent);
//        String typeContent = "";
//        if (ImType.P2P.getValue().equals(messageRedPackageBean.getType())){
//            typeContent = "个人红包";
//        }else {
//            typeContent = "群组红包";
//        }
//        redPackageType.setText(typeContent);
//        redPackageAmount.setText("金额:"+money+"元");
    }

    OpenRedPackageResultListener openRedPackageResultListener;

    public void setOpenRedPackageResultListener(OpenRedPackageResultListener openRedPackageResultListener) {
        this.openRedPackageResultListener = openRedPackageResultListener;
    }

    /**
     * 打开红包的结果接口回调
     */
    public interface OpenRedPackageResultListener{
        void redPackageResult(ImMessageBaseModel imMessageBaseModel);
    }

    /**
     * 打开红包页面配置类型
     */
    class Build{

    }

}