package com.wd.daquan.imui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.da.library.utils.BigDecimalUtils;
import com.da.library.widget.CommTitle;
import com.dq.im.constants.URLUtil;
import com.dq.im.model.ImMessageBaseModel;
import com.dq.im.type.ImType;
import com.google.gson.Gson;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.http.HttpBaseBean;
import com.wd.daquan.http.HttpResultResultCallBack;
import com.wd.daquan.http.ImSdkHttpUtils;
import com.wd.daquan.imui.adapter.RedPackageReceivedUserAdapter;
import com.wd.daquan.imui.bean.CouponBean;
import com.wd.daquan.imui.bean.CouponHistoryBean;
import com.wd.daquan.imui.presentter.RedPackagePresenter;
import com.wd.daquan.imui.type.RedPackageStatus;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.UserCloudWallet;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.third.helper.UserInfoHelper;
import com.wd.daquan.util.GlideUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 红包详情页面
 */
public class RedPackageDetailsActivity extends DqBaseActivity<RedPackagePresenter, DataBean>  {
    public static final String RED_PACKAGE_ID = "redPackageId";//红包ID
    public static final String MESSAGE_DATA = "messageData";//消息内容
    //    private TextView redPackageTypeTv;//红包类型
    private View redPackageDetailsGroup;//群组布局
    private TextView redPackageAmountTv;//红包金额
    //    private TextView redPackageCountTv;//红包数量
    private CommTitle commTitle;
    private ImageView headIcon;//头像
    private View redPackageType;//红包标识
    private TextView userName;//用户名字
    private TextView redPackageDestination;//红包简介
    private TextView redPackageDetailsProgress;//领取进度
    private TextView red_package_details_description;
    private RecyclerView recyclerView;//好友列表
    private String redPackageId = "";//红包ID
    private List<CouponBean> openRedPackageResultBeans = new ArrayList<>();
    private CouponHistoryBean.TioCouponBean messageRedPackageBean = new CouponHistoryBean.TioCouponBean();
    private RedPackageReceivedUserAdapter redPackageReceivedUserAdapter;
    private ImMessageBaseModel imMessageBaseModel;
    private UserCloudWallet userCloudWallet = new UserCloudWallet();
    private ImType imType;
    public void initView(){
        red_package_details_description = findViewById(R.id.red_package_details_description);
        redPackageDestination = findViewById(R.id.red_package_destination);
        commTitle = findViewById(R.id.red_package_details_title);
        redPackageDetailsProgress = findViewById(R.id.red_package_details_progress);
        headIcon = findViewById(R.id.head_icon);
        userName = findViewById(R.id.user_name);
        redPackageType = findViewById(R.id.user_package_type);
//        redPackageTypeTv = findViewById(R.id.red_package_details_type);
        redPackageAmountTv = findViewById(R.id.red_package_details_amount);
//        redPackageCountTv = findViewById(R.id.red_package_details_count);
        recyclerView = findViewById(R.id.red_package_user_rv);
        redPackageDetailsGroup = findViewById(R.id.red_package_details_group);
        initRecycleView(recyclerView);
        commTitle.setTitleLayoutBackgroundColor(Color.TRANSPARENT);
        commTitle.setLeftImageResource(R.mipmap.arrow_white_left);
        commTitle.getLeftIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initData(){
        redPackageId = getIntent().getStringExtra(RED_PACKAGE_ID);
        imMessageBaseModel = (ImMessageBaseModel)getIntent().getSerializableExtra(MESSAGE_DATA);
        initRecycleData();
        mPresenter.getUserCloudWallet(DqUrl.url_user_cloud_wallet,new HashMap<>());
        requestRedPackageDetails(redPackageId);
        requestRedPackageUser(redPackageId);
        setStatusBarColor(getResources().getColor(R.color.color_F23939),this);
    }
    protected void initRecycleView(RecyclerView recyclerView){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    private void initRecycleData(){
        redPackageReceivedUserAdapter = new RedPackageReceivedUserAdapter(this);
        recyclerView.setAdapter(redPackageReceivedUserAdapter);
    }

    /**
     * 更新UI
     * @param userCloudWallet
     */
    private void upDataUI(UserCloudWallet userCloudWallet){
        String accumulate = "红包余额 %s元";
        String balanceValue = BigDecimalUtils.penny2Dollar(userCloudWallet.getBalance()).toPlainString();
    }

    @Override
    protected RedPackagePresenter createPresenter() {
        return new RedPackagePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_red_package_details);
    }

    private void updateRedPackage(CouponHistoryBean.TioCouponBean messageRedPackageBean){
        imType = ImType.typeOfValue(imMessageBaseModel.getType());
//        redPackageAmountTv.setText(messageRedPackageBean.getMoney());

        if (imType == ImType.P2P){
            redPackageDetailsGroup.setVisibility(View.GONE);
            redPackageType.setVisibility(View.GONE);
        }else {
            redPackageDetailsGroup.setVisibility(View.VISIBLE);
            redPackageType.setVisibility(View.VISIBLE);
        }
        GlideUtil.loadNormalImgByNet(this, UserInfoHelper.getHeadPic(imMessageBaseModel.getFromUserId()),headIcon);
        userName.setText(UserInfoHelper.getUserNickName(imMessageBaseModel.getFromUserId()));
        RedPackageStatus redPackageStatus = RedPackageStatus.getRedPackageStatus(messageRedPackageBean.getStatus());
        redPackageDestination.setText(redPackageStatus.description);
        String userId = ModuleMgr.getCenterMgr().getUID();
        if (userId.equals(messageRedPackageBean.getFromUserId())){
            Log.e("YM","是同一个用户");
        }else {
            Log.e("YM","不是同一个用户");
        }
        red_package_details_description.setText(messageRedPackageBean.getDescription());
        String redPackageProgress = "领取".concat(openRedPackageResultBeans.size()+"").concat("/").concat(messageRedPackageBean.getCount()+"").concat("个");
        redPackageDetailsProgress.setText(redPackageProgress);
    }

    /**
     * 获取网络数据
     */
    private void requestRedPackageDetails(String redPackageId){

        Gson gson = new Gson();
        Map<String,Object> params = new HashMap<>();
        params.put("couponId", redPackageId);
        ImSdkHttpUtils.postJson(false, URLUtil.RED_PACKAGE_DETAILS,params,new HttpResultResultCallBack<HttpBaseBean>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(HttpBaseBean response, int id) {
                String content = response.data.toString();
//                List<TeamModel> userModels = gson.fromJson(content,new TypeToken<List<TeamModel>>(){}.getType());
                if (0 == response.status){
                    Log.e("YM","红包详情内容:"+content);
                    CouponHistoryBean couponHistoryBean = gson.fromJson(content, CouponHistoryBean.class);
//                    messageRedPackageBean = gson.fromJson(content,MessageRedPackageBean.class);
                    messageRedPackageBean = couponHistoryBean.getTioCoupon();
                    updateRedPackage(messageRedPackageBean);
                }else {
                    Toast.makeText(getBaseContext(),"获取不到红包消息:"+response.status,Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * 获取网络数据
     */
    private void requestRedPackageUser(String redPackageId){

        Gson gson = new Gson();
        Map<String,Object> params = new HashMap<>();
        params.put("couponId", redPackageId);
        ImSdkHttpUtils.postJson(false, URLUtil.RED_PACKAGE_HISTORY,params,new HttpResultResultCallBack<HttpBaseBean>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(HttpBaseBean response, int id) {
                String content = response.data.toString();
                Log.e("YM","获取到的红包列表消息:"+content);
                if (0 == response.status){
                    CouponHistoryBean couponHistoryBean = gson.fromJson(content, CouponHistoryBean.class);
                    openRedPackageResultBeans = couponHistoryBean.getCouponHistory();
                    String redPackageProgress = "领取".concat(openRedPackageResultBeans.size()+"").concat("/").concat(messageRedPackageBean.getCount()+"").concat("个");
                    redPackageDetailsProgress.setText(redPackageProgress);
                    redPackageReceivedUserAdapter.setData(openRedPackageResultBeans);
                    String showMoney = "";
                    for (CouponBean couponBean : openRedPackageResultBeans){
                        if (ModuleMgr.getCenterMgr().getUID().equals(couponBean.getUserId())){
                            showMoney = couponBean.getMoney();
                            break;
                        }
                    }
                    if (TextUtils.isEmpty(showMoney)){
                        redPackageAmountTv.setVisibility(View.GONE);
                    }else {
                        redPackageAmountTv.setVisibility(View.VISIBLE);
                        redPackageAmountTv.setText(showMoney);
                    }
                }else {
                    Toast.makeText(getBaseContext(),"获取不到红包列表消息:"+response.status,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_user_cloud_wallet.equals(url)){
            userCloudWallet = (UserCloudWallet)entity.data;
            upDataUI(userCloudWallet);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if (DqUrl.url_user_cloud_wallet.equals(url)){
            if (entity.result != 16001){
                DqToast.showShort("网络异常~");
            }
//            updateData(null);
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(int statusColor, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏为透明
            window.setStatusBarColor(statusColor);
        }
    }
}