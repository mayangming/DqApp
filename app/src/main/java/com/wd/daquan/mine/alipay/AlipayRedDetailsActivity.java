package com.wd.daquan.mine.alipay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.QCBroadcastManager;
import com.wd.daquan.common.utils.DqUtils;
import com.meetqs.qingchat.imagepicker.immersive.ImmersiveManage;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.mine.adapter.AlipayRedDetailsAdapter;
import com.wd.daquan.mine.bean.AlipayRedDetailsEntity;
import com.wd.daquan.mine.bean.AlipayRedListEntity;
import com.da.library.widget.CommTitle;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.LinkedHashMap;

/**
 * @author  zht
 * @describe 群组支付宝红包详情
 *      @modify by zht
 */
public class AlipayRedDetailsActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener {


    private int pageNum = 1;
    private int pageSize = 20;
    private CommTitle mCommTitle;
    private TextView txt_mine;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerview;
    private ClassicsFooter mClassicsFooter;
    private ClassicsHeader mClassicsHeader;
    private QCSharedPrefManager qcSharedPrefManager;
    private AlipayRedDetailsAdapter mAdapter;
    private AlipayRedListEntity.ListBean payRedListEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ImmersiveManage.immersiveAboveAPI19(this, getResources().getColor(R.color.color_d84e43), Color.BLACK, false);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.alipay_red_details_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        initData();
        initListener();
    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.alipayRedDetailsCommTitle);
        txt_mine = findViewById(R.id.alipayRedDetailsMine);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerview = findViewById(R.id.comm_recycler_view);
        mClassicsHeader = findViewById(R.id.comm_recycler_view_header);
        mClassicsFooter = findViewById(R.id.comm_recycler_view_footer);
        mRefreshLayout.setEnableRefresh(false);
        mClassicsFooter.setVisibility(View.GONE);
        mClassicsHeader.setVisibility(View.GONE);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommTitle.setTitle(getString(R.string.alipay_auth));
    }

    @Override
    public void initListener() {
        txt_mine.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void initData() {
        setTitleData();
        qcSharedPrefManager = QCSharedPrefManager.getInstance();

        String redId = "0";
//        redid = getIntent().getIntExtra(Constants.RED_ID,-1);
//        if (KeyValue.ONE_STRING.equals(redId)) {//会话中进入
////            mRedMessage =  getIntent().getParcelableExtra(Constants.RedMessage);
////            BaseImgView.ImageLoaderLoadimg(icon_user_head,
////                    mRedMessage.getSendPic(), R.mipmap.user_avatar, R.mipmap.user_avatar, R.mipmap.user_avatar, 60, 60, 4);
////            tv_name.setText(mRedMessage.getSendName());
////            tv_blessing.setText(mRedMessage.getBlessing());
////            mInterfacePresenter.getRedPacketDetail(mRedMessage.getRedpacketId(),mRedMessage.getSignature());
//            AlipayRedListEntity.ListBean payRedListEntity = (AlipayRedListEntity.ListBean) getIntent().
//                    getSerializableExtra("redDetails");
//            if(payRedListEntity == null)return;
//            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
//            linkedHashMap.put("redpacket_id", payRedListEntity.redpacket_id);
//            linkedHashMap.put("signature", payRedListEntity.signature);
//            mPresenter.getRedPacketDetail(DqUrl.url_redPacketDetail, linkedHashMap);
//        }else{//明细中进入
//            AlipayRedListEntity.ListBean payRedListEntity = (AlipayRedListEntity.ListBean) getIntent().
//                    getSerializableExtra("redDetails");
////            EBSharedPrefManager manager= BridgeFactory.getBridge(Bridges.SHARED_PREFERENCE);
////            String headPic = manager.getKDPreferenceUserInfo().getString(EBSharedPrefUser.headpic, "");
////            tv_blessing.setText(amount);
////            BaseImgView.ImageLoaderLoadimg(icon_user_head,
////                    headPic, R.mipmap.user_avatar, R.mipmap.user_avatar, R.mipmap.user_avatar, 60, 60, 4);
//            if(payRedListEntity == null)return;
//            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
//            linkedHashMap.put("redpacket_id", payRedListEntity.redpacket_id);
//            linkedHashMap.put("signature", payRedListEntity.signature);
//            mPresenter.getRedPacketDetail(DqUrl.url_redPacketDetail, linkedHashMap);
//        }
        payRedListEntity = (AlipayRedListEntity.ListBean) getIntent().
                getSerializableExtra("redDetails");
        if(payRedListEntity == null)return;
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("redpacket_id", payRedListEntity.redpacket_id);
        linkedHashMap.put("signature", payRedListEntity.signature);
        mPresenter.getRedPacketDetail(DqUrl.url_redPacketDetail, linkedHashMap);
    }


    private void setTitleData() {
        mCommTitle.setTitle(getString(R.string.alipay_auth));
        mCommTitle.setTitleLayoutBackgroundColor(getResources().getColor(R.color.color_d84e43));
        mCommTitle.setIvBackgroundColor(R.drawable.alipay_btn_selecter);
        mCommTitle.getRightIv().setVisibility(View.GONE);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.alipayRedDetailsMine:
                NavUtils.gotoAlipayPayListActivity(this);
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        DqUtils.bequit(entity, this);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_redPacketDetail.equals(url)) {//红包记录
            if (0 == code) {
                AlipayRedDetailsEntity data = (AlipayRedDetailsEntity) entity.data;

                DqApp.getInstance().runInUIThread(() -> {
                    if (data != null) {
                        if (data.list != null) {
                            if (data.list.size()!=0) {
                                QCBroadcastManager manager = new QCBroadcastManager();
                                manager.sendBroadcast(KeyValue.REDTYPE, payRedListEntity.redpacket_id);
                            }
                            String curUid = qcSharedPrefManager.getKDPreferenceUserInfo().getString(EBSharedPrefUser.uid, "0");
                            if(data.create_uid.equals(curUid)){
                                txt_mine.setVisibility(View.VISIBLE);
                            }else{
                                txt_mine.setVisibility(View.VISIBLE);
                            }
                        }
                        if(mAdapter == null){
                            mAdapter = new AlipayRedDetailsAdapter();
                        }
                        mRecyclerview.setAdapter(mAdapter);
                        data.sessionType = payRedListEntity.sessionType;
                        mAdapter.setHeaderData(data);
                        mAdapter.update(data.list);
                    }
                });


            }
        }
    }
}
