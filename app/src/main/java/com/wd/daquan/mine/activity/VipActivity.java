package com.wd.daquan.mine.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.da.library.utils.AESUtil;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.redpacket.pay.PayResult;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.mine.adapter.VipCardAdapter;
import com.wd.daquan.mine.dialog.ShareVipFragment;
import com.wd.daquan.mine.presenter.ViPPresenter;
import com.wd.daquan.mine.presenter.VipIView;
import com.wd.daquan.model.BuildConfig;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.VipCommodityEntity;
import com.wd.daquan.model.bean.WxBindBean;
import com.wd.daquan.model.bean.WxPayBody;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员页面
 */
public class VipActivity extends DqBaseActivity<ViPPresenter, DataBean> implements View.OnClickListener, VipIView<DataBean>, QCObserver {
    private RecyclerView vipCardListRv;
    private View vipPayAliTv;
    private View vipExchange;
    private View vipHeadOutline;
    private VipCardAdapter vipCardAdapter;
    private int lastSelectIndex = -1;//上一个选择的位置
    private List<VipCommodityEntity> vipCardEntities = new ArrayList<>();
    private VipCommodityEntity vipCardEntity;
    private ImageView vip_head;
    private ImageView vip_icon;
    private TextView vip_name;
    private TextView expiration_date;
    private TextView shareCount;

    private View aliPayItem;
    private View weChatPayItem;
    private ImageView aliPayCheckIcon;
    private ImageView weChatPayCheckIcon;

    private View vipSaveQr;
    private View vipShareCopyLink;

    private int payCheckStatus = 1; //0：支付宝支付，1：微信支付
    @Override
    protected ViPPresenter createPresenter() {
        return new ViPPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_vip);
    }

    @Override
    protected void initView() {
        mTitleDqLayout = findViewById(R.id.toolbar);
        vipCardListRv = findViewById(R.id.vip_card_list);
        vipPayAliTv = findViewById(R.id.vip_pay_ali);
        aliPayItem = findViewById(R.id.pay_ali_pay_item);
        weChatPayItem = findViewById(R.id.pay_wechat_pay_item);
        aliPayCheckIcon = findViewById(R.id.pay_ali_pay_check_icon);
        weChatPayCheckIcon = findViewById(R.id.pay_wechat_pay_check_icon);
        vip_head = findViewById(R.id.vip_head);
        vip_icon = findViewById(R.id.vip_icon);
        vip_name = findViewById(R.id.vip_name);
        vipHeadOutline = findViewById(R.id.vip_head_outline);
        expiration_date = findViewById(R.id.expiration_date);
        vipExchange = findViewById(R.id.vip_exchange_ll);
        vipSaveQr = findViewById(R.id.vip_save_qr);
        vipShareCopyLink = findViewById(R.id.vip_share_copy_link);
        shareCount = findViewById(R.id.vip_share_count);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        vipCardListRv.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void initListener() {
        super.initListener();
        vipPayAliTv.setOnClickListener(this);
        vipExchange.setOnClickListener(this);
        aliPayItem.setOnClickListener(this);
        weChatPayItem.setOnClickListener(this);
        vipSaveQr.setOnClickListener(this);
        vipShareCopyLink.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Map<String,String> params = new HashMap<>();
        super.onClick(v);
        switch (v.getId()){
            case R.id.vip_pay_ali:
                if (null == vipCardEntity){
                    DqToast.showShort("请选择商品");
                    return;
                }
                if (payCheckStatus == 0){
                    params.put("vipCommodityId",vipCardEntity.getVipCommodityId());
                    mPresenter.getAliPayOrderInfo(DqUrl.url_pay_ali_order_info,params);
                }else if (payCheckStatus == 1){
                    params.put("vipCommodityId",vipCardEntity.getVipCommodityId());
                    mPresenter.getWeChatPayOrderInfo(DqUrl.url_pay_wechat_order_info,params);
                }
                break;
            case R.id.pay_ali_pay_item:
                payCheckStatus = 0;
                aliPayCheckIcon.setImageResource(R.mipmap.check_checked);
                weChatPayCheckIcon.setImageResource(R.mipmap.check_uncheck);
                break;
            case R.id.pay_wechat_pay_item:
                payCheckStatus = 1;
                aliPayCheckIcon.setImageResource(R.mipmap.check_uncheck);
                weChatPayCheckIcon.setImageResource(R.mipmap.check_checked);
                break;
            case R.id.vip_exchange_ll:
                NavUtils.gotoVipExchangeActivity(this);
                break;
            case R.id.vip_save_qr:
                ShareVipFragment shareVipFragment = new ShareVipFragment();
                Bundle bundle = new Bundle();
                bundle.putString("key","value");
                shareVipFragment.setArguments(bundle);
                shareVipFragment.showDialog(this);
                break;
            case R.id.vip_share_copy_link:
                copyLink();
                break;
        }
    }

    @Override
    protected void initData() {
        MsgMgr.getInstance().attach(this);
        vipCardAdapter = new VipCardAdapter();
        vipCardAdapter.addLists(vipCardEntities);
        vipCardListRv.setAdapter(vipCardAdapter);
        vipCardAdapter.setListener((o, position) -> {
            o.setSelected(true);
            if (-1 != lastSelectIndex){
                vipCardEntities.get(lastSelectIndex).setSelected(false);
            }
            lastSelectIndex = position;
            vipCardEntity = o;
            vipCardAdapter.notifyDataSetChanged();
        });
        mPresenter.requestUserInfo(DqUrl.url_oauth_useWeixinInfo,new HashMap<>());
        mPresenter.getVipCommodityList(DqUrl.url_vip_commodity_list,new HashMap<>());
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if(DqUrl.url_oauth_useWeixinInfo.equals(url)){
            WxBindBean userInfo = (WxBindBean) entity.data;
            String headPic = userInfo.headpic;
//            GlideUtils.loadCircle(this, headPic, vip_head);
            GlideUtils.loadHeader(DqApp.sContext, headPic, vip_head);
            vip_name.setText(userInfo.nickName);
            if (!TextUtils.isEmpty(userInfo.getVipEndTime())){
                expiration_date.setText(userInfo.getVipEndTime().concat("到期"));
            }
            if (userInfo.isVip()){
                vip_icon.setVisibility(View.VISIBLE);
                vipHeadOutline.setVisibility(View.VISIBLE);
            }else {
                vip_icon.setVisibility(View.GONE);
                vipHeadOutline.setVisibility(View.GONE);
            }

            shareCount.setText("我的分享人数 ".concat(userInfo.getShareTotalNum()+"人"));

            ModuleMgr.getCenterMgr().putVipStatus(userInfo.getIsVip());
            ModuleMgr.getCenterMgr().putVipStartTime(userInfo.getVipStartTime());
            ModuleMgr.getCenterMgr().putVipEndTime(userInfo.getVipEndTime());
            ModuleMgr.getCenterMgr().putShareTotalNum(userInfo.getShareTotalNum());
        }else if (DqUrl.url_pay_ali_order_info.equals(url)){
            String payInfo = entity.data.toString();
            mPresenter.startAliPay(this,payInfo);
        }else if (DqUrl.url_pay_wechat_order_info.equals(url)){
            WxPayBody wxPayBody = (WxPayBody) entity.data;
            mPresenter.startWeChatPay(this,wxPayBody);
        }else if (DqUrl.url_vip_commodity_list.equals(url)){
            List<VipCommodityEntity> vipCommodityEntities = (List<VipCommodityEntity>) entity.data;
            vipCommodityEntities.get(0).setSelected(true);
            vipCardEntities.addAll(vipCommodityEntities);
            vipCardEntity = vipCardEntities.get(0);
            vipCardAdapter.addLists(vipCommodityEntities);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
    }

    @Override
    public void aliPaySuccess(PayResult payResult) {
        mPresenter.requestUserInfo(DqUrl.url_oauth_useWeixinInfo,new HashMap<>());//支付后重新刷新用户信息
    }

    @Override
    public void weChatPaySuccess(WxPayBody payResult) {
        mPresenter.requestUserInfo(DqUrl.url_oauth_useWeixinInfo,new HashMap<>());//支付后重新刷新用户信息
    }

    /**
     * 复制分享链接
     */
    private void copyLink(){
        String link = "";
        try {
            String params = AESUtil.encrypt(ModuleMgr.getCenterMgr().getUID());
            link = BuildConfig.SERVER+DqUrl.url_vip_share_link +params;
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

    @Override
    public void onMessage(String key, Object value) {
        if (MsgType.VIP_EXCHANGE_CHANGE.equals(key)){//会员信息变动后刷新数据
            mPresenter.requestUserInfo(DqUrl.url_oauth_useWeixinInfo,new HashMap<>());
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }
}