//package com.wd.daquan.mine;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.aides.brother.qingchat.R;
//import com.aides.brother.qingchat.bean.BaseResp;
//import com.aides.brother.qingchat.biz.personcenter.IUserLoginView;
//import com.aides.brother.qingchat.biz.personcenter.InterfacePresenter;
//import com.aides.brother.qingchat.bridge.BridgeFactory;
//import com.aides.brother.qingchat.bridge.Bridges;
//import com.aides.brother.qingchat.bridge.cache.sharePref.EBSharedPrefManager;
//import com.aides.brother.qingchat.bridge.cache.sharePref.EBSharedPrefUser;
//import com.aides.brother.qingchat.constant.Constants;
//import com.aides.brother.qingchat.ui.base.BaseActivity;
//import com.aides.brother.qingchat.util.CNToastUtil;
//import com.aides.brother.qingchat.util.NavUtils;
//import com.aides.brother.qingchat.util.Utils;
//
//public class FreezeAndUnFreezeDoneActivity extends BaseActivity implements IUserLoginView{
//    TextView tv_undone,tv_unok;
//    LinearLayout linBlockone,linBlocktwo;
//    String phone;
//    InterfacePresenter interfacePresenter;
//    String phoneNumber;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_block_done);
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void initViews() {
//        interfacePresenter=new InterfacePresenter();
//        interfacePresenter.attachView(this);
//        tv_undone= findViewById(R.id.tv_undone);
//        tv_unok= findViewById(R.id.tv_unok);
//        linBlockone= findViewById(R.id.mLinBlockone);
//        linBlocktwo= findViewById(R.id.mLinBlocktwo);
//    }
//
//    @Override
//    public void initListeners() {
//        tv_undone.setOnClickListener(this);
//        tv_unok.setOnClickListener(this);
//    }
//
//    @Override
//    public void initData() {
//        EBSharedPrefManager manager= BridgeFactory.getBridge(Bridges.SHARED_PREFERENCE);
//        phoneNumber=manager.getKDPreferenceUserInfo().getString(EBSharedPrefUser.phone,"");
//       phone=getIntent().getStringExtra(Constants.PHONE);
//    }
//    @Override
//    public void setHeader() {
//        super.setHeader();
//        ivTopBack.setVisibility(View.VISIBLE);
//        tvTopTitle.setText("冻结账号");
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.tv_undone:
//                linBlockone.setVisibility(View.GONE);
//                linBlocktwo.setVisibility(View.VISIBLE);
//                break;
//            case R.id.tv_unok:
//                interfacePresenter.getBlock(phone,"");
//                break;
//        }
//    }
//
//    @Override
//    public void onError(BaseResp s) {
//        Utils.bequit(s,this);
//    }
//
//    @Override
//    public void onSuccess(BaseResp s) {
//       if (s.getCode()==0){
//           status();
//       }
//    }
//    void status(){
//        EBSharedPrefManager manager= BridgeFactory.getBridge(Bridges.SHARED_PREFERENCE);
//        String status=manager.getKDPreferenceUserInfo().getString(Constants.STATE,"");
//        if (!TextUtils.isEmpty(status)) {
//                CNToastUtil.makeText(this,"成功冻结");
//                setResult(RESULT_OK);
//                finish();
////            if (status.equals("more")){
////                PullToRefreshUtils.quit(this);
////                NavUtils.gotoLoginPwdAgainActivity(this);
////                BroadcastManager.getInstance(this).sendBroadcast(SealConst.EXIT);
////            }else if (status.equals("safe")){
////                if (phoneNumber.equals(phone)){
////                    BroadcastManager.getInstance(this).sendBroadcast(SealConst.EXIT);
////                    PullToRefreshUtils.quit(this);
////                    NavUtils.gotoLoginPwdAgainActivity(this);
////                }else {
////                }
////            }
//        }else{
////            Utils.quit(this);
//            Utils.forceQuit();
//            Utils.clearUserInfo();
//            NavUtils.gotoLoginPwdAgainActivity(this);
//        }
//    }
//}
