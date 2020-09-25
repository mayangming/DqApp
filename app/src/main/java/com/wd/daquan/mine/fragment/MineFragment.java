package com.wd.daquan.mine.fragment;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.da.library.tools.Utils;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.fragment.BaseFragment;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.SignUpEntity;
import com.wd.daquan.model.bean.WxBindBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: dukangkang
 * @date: 2018/9/5 18:19.
 * @description:
 */
public class MineFragment extends BaseFragment<MinePresenter, DataBean> implements View.OnClickListener, QCObserver {
    private LinearLayout mHeadLl;
    private View headOutlineBg;//头像外框轮廓
    private ImageView mHeadIv;
    private TextView mNameTv;
    private TextView mDqNumTv;
    private TextView vipDate;//日期
    private TextView vipRenew;//续费开通
    private LinearLayout mScan;
    private LinearLayout mCollection;
    private LinearLayout mSetting;
    private LinearLayout mHelp;
    private LinearLayout mVip;
    private LinearLayout mShare;
    private View mineWalletCloud;
    private View mineWallet;
    private View signGiftLl;
    private View dqShop;//dq商城
    private View mInviteFriend;
    private TextView dqCurrency;//斗圈斗币积分
    protected String[] needPermissions = {Manifest.permission.CAMERA};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_drawer_header;
    }

    @Override
    public MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        headOutlineBg = view.findViewById(R.id.main_title_left_head_bg);
        mHeadLl = view.findViewById(R.id.main_title_left_head_ll);
        mHeadIv = view.findViewById(R.id.main_title_left_head_iv);
        mNameTv = view.findViewById(R.id.main_title_left_name_tv);
        mDqNumTv = view.findViewById(R.id.main_title_left_dq_num_tv);
        mInviteFriend = view.findViewById(R.id.mine_invite_friend);
        vipDate = view.findViewById(R.id.mine_vip_date);
        vipRenew = view.findViewById(R.id.mine_vip_renew);
        mineWalletCloud = view.findViewById(R.id.mine_wallet_cloud);
        mineWallet = view.findViewById(R.id.mine_wallet);

        mScan = view.findViewById(R.id.main_title_left_scan);
        mCollection = view.findViewById(R.id.main_title_left_collection);
        mSetting = view.findViewById(R.id.main_title_left_setting);
        mHelp = view.findViewById(R.id.main_title_left_help);
        mVip = view.findViewById(R.id.main_title_left_vip);
        mShare = view.findViewById(R.id.main_title_left_share);
        signGiftLl = view.findViewById(R.id.sign_gift_ll);
        dqShop = view.findViewById(R.id.dq_shop);
        dqCurrency = view.findViewById(R.id.dq_currency);
    }

    @Override
    public void initListener() {
        MsgMgr.getInstance().attach(this);
        mHeadLl.setOnClickListener(this);
        mScan.setOnClickListener(this);
        mCollection.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mHelp.setOnClickListener(this);
        mVip.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mInviteFriend.setOnClickListener(this);
        vipRenew.setOnClickListener(this);
        mineWalletCloud.setOnClickListener(this);
        mineWallet.setOnClickListener(this);
        signGiftLl.setOnClickListener(this);
        dqShop.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mPresenter.requestUserInfo(DqUrl.url_oauth_useWeixinInfo,new HashMap<>());
        updateUserInfo();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.requestUserInfo(DqUrl.url_oauth_useWeixinInfo,new HashMap<>());
    }

    private void updateUserInfo() {
        String nickName = ModuleMgr.getCenterMgr().getNickName();
        String headPic = ModuleMgr.getCenterMgr().getAvatar();
        String qingChatNum = ModuleMgr.getCenterMgr().getDqNum();
        String vipEndDate = ModuleMgr.getCenterMgr().getVipEndTime();
        boolean isVip = ModuleMgr.getCenterMgr().isVip();
        if (!TextUtils.isEmpty(nickName)) {
            mNameTv.setText("昵称:".concat(nickName));
        }
        if (!TextUtils.isEmpty(headPic)) {
            GlideUtils.loadHeader(DqApp.sContext, headPic + DqUrl.url_avatar_suffix, mHeadIv);
        }
        if (!TextUtils.isEmpty(qingChatNum)) {
            mDqNumTv.setText(String.format(DqApp.sContext.getString(R.string.mine_chuiniu_num), qingChatNum));
            mDqNumTv.setVisibility(View.VISIBLE);
        } else {
            mDqNumTv.setVisibility(View.GONE);
        }

        if (isVip){
            headOutlineBg.setVisibility(View.VISIBLE);
            vipRenew.setVisibility(View.VISIBLE);
        }else {
            headOutlineBg.setVisibility(View.GONE);
            vipRenew.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(vipEndDate)){
            vipDate.setText("您还未开通会员");
        }else {
            vipDate.setText(vipEndDate.concat("到期"));
        }

    }

    @Override
    public void onClick(View v) {
        if(Utils.isFastDoubleClick(500)) {
            return;
        }
        switch (v.getId()){
            case R.id.main_title_left_head_ll://个人信息
                NavUtils.gotoPersonalInfoActivity(getActivity());
                break;
            case R.id.main_title_left_collection://收藏
                //NavUtils.gotoCollectionActivity(mActivity);
                DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
                break;
            case R.id.main_title_left_setting://设置
                NavUtils.gotoSettingActivity(getActivity());
                break;
            case R.id.main_title_left_scan://扫一扫
                if (DqUtils.checkPermissions(getActivity(), needPermissions)) {
                    NavUtils.gotoScanQRCodeActivity(getActivity());
                }
                break;
            case R.id.mine_invite_friend:
                NavUtils.gotoInviteFriendActivity(getContext());
                break;
            case R.id.main_title_left_help://帮助
                DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
                //NavUtils.gotoWebviewActivity(mActivity, DqUrl.url_help, DqApp.getStringById(R.string.mine_help));
                break;
            case R.id.main_title_left_feedback://意见反馈
                DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
                //NavUtils.gotoFeedBack(mActivity);
                break;
//            case R.id.main_title_left_wallet://我的钱包
//                //mPresenter.getWalletStatus(DqUrl.url_wallet_status, new LinkedHashMap<>());
//                NIMRedPacketClient.startWalletActivity(getActivity());
//                break;
            case R.id.main_title_left_vip://vip会员
                //mPresenter.getWalletStatus(DqUrl.url_wallet_status, new LinkedHashMap<>());
                NavUtils.gotoVipActivity(getActivity());
                break;
            case R.id.main_title_left_share://分享页面
                //mPresenter.getWalletStatus(DqUrl.url_wallet_status, new LinkedHashMap<>());
                NavUtils.gotoVipExchangeActivity(getActivity());
                break;
            case R.id.mine_wallet_cloud://零钱
                NavUtils.gotoWalletCloudActivity(getActivity());
                break;
            case R.id.mine_wallet://我的钱包
                //mPresenter.getWalletStatus(DqUrl.url_wallet_status, new LinkedHashMap<>());
//                NIMRedPacketClient.startWalletActivity(getActivity());
                Toast.makeText(v.getContext(),"我的钱包依赖云信，暂不可用",Toast.LENGTH_SHORT).show();
                break;
            case R.id.sign_gift_ll:
                requestSign();
                break;
            case R.id.dq_shop:
                NavUtils.gotoIntegralMallActivity(getContext());
                break;
            default:
                break;
        }
    }

    private void requestSign(){
        Map<String,String> params = new HashMap<>();
        mPresenter.requestSign(DqUrl.url_dbsign_sign,params);
    }

    @Override
    public void onMessage(String key, Object value) {
        if(key.equals(MsgType.MT_CENTER_PERSONALINFO_CHANGE)){
            updateUserInfo();
//            NIMRedPacketClient.updateMyInfo(new OkHttpModelCallBack<BaseModel>() {
//                @Override
//                public void onSuccess(BaseModel baseModel) {
//                    DqLog.i("jrmf updateMyInfo : onSuccess" );
//                }
//
//                @Override
//                public void onFail(String s) {
//                    DqLog.e("jrmf updateMyInfo : onFail" + s.toString());
//                }
//            });
        } else if (MsgType.VIP_EXCHANGE_CHANGE.equals(key)){//兑换成功后数据进行变动
            mPresenter.requestUserInfo(DqUrl.url_oauth_useWeixinInfo,new HashMap<>());
        }
//        else if(MsgType.INTEGRAL_CHANGE.equals(key)){
//            mPresenter.requestUserInfo(DqUrl.url_oauth_useWeixinInfo,new HashMap<>());
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_oauth_useWeixinInfo.equals(url)){
            WxBindBean bindBean = (WxBindBean) entity.data;
            ModuleMgr.getCenterMgr().putVipStatus(bindBean.getIsVip());
            ModuleMgr.getCenterMgr().putVipStartTime(bindBean.getVipStartTime());
            ModuleMgr.getCenterMgr().putVipEndTime(bindBean.getVipEndTime());
            ModuleMgr.getCenterMgr().putShareTotalNum(bindBean.getShareTotalNum());
            ModuleMgr.getCenterMgr().putRedEnvelopedRainRemind(bindBean.getRedEnvelopedRainRemind());
            updateUserInfo();
            dqCurrency.setText(bindBean.getDbMoney());
        }

        if (DqUrl.url_dbsign_sign.equals(url)){
            SignUpEntity signUpEntity = (SignUpEntity) entity.data;
            NavUtils.gotoSignUpDetailActivity(getContext(),signUpEntity);
        }

    }
}
