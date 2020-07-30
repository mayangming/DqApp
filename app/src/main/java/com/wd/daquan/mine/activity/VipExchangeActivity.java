package com.wd.daquan.mine.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.da.library.adapter.CommRecyclerViewAdapter;
import com.da.library.utils.AESUtil;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.mine.adapter.VipExchangeAdapter;
import com.wd.daquan.mine.dialog.ShareVipFragment;
import com.wd.daquan.mine.dialog.VipExchangeResultDialog;
import com.wd.daquan.mine.presenter.VipExchangeIView;
import com.wd.daquan.mine.presenter.VipExchangePresenter;
import com.wd.daquan.model.BuildConfig;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.ExchangeRecordBean;
import com.wd.daquan.model.bean.ExchangeVipListBean;
import com.wd.daquan.model.bean.VipExchangeResultBean;
import com.wd.daquan.model.bean.WxBindBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VIP兑换页面
 */
public class  VipExchangeActivity extends DqBaseActivity<VipExchangePresenter, DataBean> implements View.OnClickListener, VipExchangeIView<DataBean>{
    private ImageView vip_head;
    private ImageView vip_icon;
    private TextView vip_name;
    private TextView expiration_date;
    private RecyclerView vipExchangeRv;
    private View vipSaveQr;
    private View vipShareCopyLink;
    private View vipHeadOutline;
    private TextView vipShareTotalNum;
    private TextView vipShareConvertibilityNum;
    private TextView vipShareSurplusNum;
    private VipExchangeAdapter vipExchangeAdapter;
    private VipExchangeResultDialog exchangeResultDialog;
    private List<ExchangeVipListBean> vipExchangeItemBeans = new ArrayList<>();
    private ExchangeRecordBean exchangeRecordBean = new ExchangeRecordBean();
    private VipExchangeResultBean vipExchangeResultBean = new VipExchangeResultBean();
    @Override
    protected VipExchangePresenter createPresenter() {
        return new VipExchangePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_vip_exchange);
    }

    @Override
    protected void initView() {
        mTitleDqLayout = findViewById(R.id.toolbar);
        vip_head = findViewById(R.id.vip_head);
        vipHeadOutline = findViewById(R.id.vip_head_outline);
        vip_icon = findViewById(R.id.vip_icon);
        vip_name = findViewById(R.id.vip_name);
        expiration_date = findViewById(R.id.expiration_date);
        vipExchangeRv = findViewById(R.id.vip_exchange_rv);
        vipSaveQr = findViewById(R.id.vip_save_qr);
        vipShareCopyLink = findViewById(R.id.vip_share_copy_link);
        vipShareTotalNum = findViewById(R.id.vip_share_total_num);
        vipShareConvertibilityNum = findViewById(R.id.vip_share_convertibility_num);
        vipShareSurplusNum = findViewById(R.id.vip_share_surplus_num);
        vipExchangeRv.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void initListener() {
        super.initListener();
        vipSaveQr.setOnClickListener(this);
        vipShareCopyLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.vip_save_qr:
                ShareVipFragment shareVipFragment = new ShareVipFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("key","value");
//                shareVipFragment.setArguments(bundle);
                shareVipFragment.showDialog(this);
                break;
            case R.id.vip_share_copy_link:
                copyLink();
                break;
        }
    }

    @Override
    protected void initData() {
        updateUserInfo();
        vipExchangeAdapter = new VipExchangeAdapter();
        vipExchangeAdapter.addLists(vipExchangeItemBeans);
        vipExchangeRv.setAdapter(vipExchangeAdapter);
//        vipExchangeAdapter.setItemOnClickForView(new CommRecyclerViewAdapter.ItemOnClickForView(){
//            @Override
//            public void itemOnClickForView(int position, VipExchangeItemBean vipExchangeItemBean, @IdRes int viewId, View.OnClickListener onClickListener) {
//                DqToast.showShort("点击的第:"+position+"个控件");
//            }
//        });
        vipExchangeAdapter.setItemOnClickForView(new CommRecyclerViewAdapter.ItemOnClickForView<ExchangeVipListBean>() {
            @Override
            public  void itemOnClickForView(int position, ExchangeVipListBean itemBean, @IdRes int viewId) {
                Map<String,String> params = new HashMap<>();
                params.put("shareExchangeId",String.valueOf(itemBean.getId()));
                mPresenter.getExchangeVip(DqUrl.url_vip_exchange_vip,params);
            }
        });
        mPresenter.getVipExchangeRecord(DqUrl.url_vip_exchange_record,new HashMap<>());
        mPresenter.getExchangeVipList(DqUrl.url_vip_exchange_vip_list,new HashMap<>());
    }

    /**
     * 更新用户数据
     */
    private void updateUserInfo(){
        String headUrl = ModuleMgr.getCenterMgr().getAvatar();
        boolean isVip = ModuleMgr.getCenterMgr().isVip();
        String nickName = ModuleMgr.getCenterMgr().getNickName();
        String vipEndTime = ModuleMgr.getCenterMgr().getVipEndTime();
//        GlideUtils.loadCircle(this, headUrl, vip_head);
        GlideUtils.loadHeader(DqApp.sContext, headUrl, vip_head);
        vip_name.setText(nickName);
        if (!TextUtils.isEmpty(vipEndTime)){
            expiration_date.setText(vipEndTime.concat("到期"));
        }
        if (isVip){
            vip_icon.setVisibility(View.VISIBLE);
            vipHeadOutline.setVisibility(View.VISIBLE);
        }else {
            vip_icon.setVisibility(View.GONE);
            vipHeadOutline.setVisibility(View.GONE);
        }
    }

    private void refreshRecord(ExchangeRecordBean exchangeRecordBean){
        vipShareTotalNum.setText(String.valueOf(exchangeRecordBean.getShareTotalNum()).concat("人"));
        vipShareConvertibilityNum.setText(String.valueOf(exchangeRecordBean.getConvertibilityNum()).concat("人"));
        vipShareSurplusNum.setText(String.valueOf(exchangeRecordBean.getSurplusNum()).concat("人"));
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_vip_exchange_record.equals(url)){//获取邀请人数据
            exchangeRecordBean = (ExchangeRecordBean)entity.data;
            refreshRecord(exchangeRecordBean);
        }else if (DqUrl.url_vip_exchange_vip_list.equals(url)){
            vipExchangeItemBeans = (List<ExchangeVipListBean>) entity.data;
            vipExchangeAdapter.update(vipExchangeItemBeans);
        }else if (DqUrl.url_vip_exchange_vip.equals(url)){
//            DialogUtils.showCommonDialogForSure(VipExchangeActivity.this, "兑换成功", entity.content, null);
            vipExchangeResultBean = (VipExchangeResultBean) entity.data;
            showVipExchangeResultDialog(1,vipExchangeResultBean);
            MsgMgr.getInstance().sendMsg(MsgType.VIP_EXCHANGE_CHANGE, "");
            mPresenter.requestUserInfo(DqUrl.url_oauth_useWeixinInfo,new HashMap<>());
            mPresenter.getVipExchangeRecord(DqUrl.url_vip_exchange_record,new HashMap<>());
            mPresenter.getExchangeVipList(DqUrl.url_vip_exchange_vip_list,new HashMap<>());
        }else if (DqUrl.url_oauth_useWeixinInfo.equals(url)){
            WxBindBean bindBean = (WxBindBean) entity.data;
            ModuleMgr.getCenterMgr().putVipStatus(bindBean.getIsVip());
            ModuleMgr.getCenterMgr().putVipStartTime(bindBean.getVipStartTime());
            ModuleMgr.getCenterMgr().putVipEndTime(bindBean.getVipEndTime());
            ModuleMgr.getCenterMgr().putShareTotalNum(bindBean.getShareTotalNum());
            updateUserInfo();
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if (DqUrl.url_vip_exchange_vip.equals(url)){
//            DialogUtils.showCommonDialogForSure(VipExchangeActivity.this, "兑换失败", entity.content, null);
            showVipExchangeResultDialog(0,null);
        }
    }

    /**
     * 展示对话框
     */
    private void showVipExchangeResultDialog(int vipExchangeType,VipExchangeResultBean vipExchangeResultBean){
        if (null == exchangeResultDialog){
            exchangeResultDialog = new VipExchangeResultDialog();
        }
        Bundle bundle = new Bundle();
        bundle.putInt(VipExchangeResultDialog.VIP_EXCHANGE_RESULT_TYPE,vipExchangeType);
        if (null != vipExchangeResultBean){
            bundle.putInt(VipExchangeResultDialog.VIP_EXCHANGE_RESULT_COUNT,vipExchangeResultBean.getBeExchangeNum());
            bundle.putInt(VipExchangeResultDialog.VIP_EXCHANGE_RESULT_TIME,vipExchangeResultBean.getBeExchangeDay());
        }
        exchangeResultDialog.setArguments(bundle);
        exchangeResultDialog.show(getSupportFragmentManager(),"");
    }

    /**
     * 复制分享链接
     */
    private void copyLink(){
        String link = "";
        try {
            String params = AESUtil.encrypt(ModuleMgr.getCenterMgr().getUID());
            link = BuildConfig.SERVER+ DqUrl.url_vip_share_link +params;
        }catch (Exception e){
            e.printStackTrace();
        }

        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", link);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        DqToast.showShort("链接已复制进剪切板");
    }
}