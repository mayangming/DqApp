package com.wd.daquan.mine.alipay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.DqUtils;
import com.da.library.dialog.AliPayDialog;
import com.wd.daquan.glide.GlideUtils;
import com.meetqs.qingchat.imagepicker.immersive.ImmersiveManage;
import com.da.library.listener.DialogListener;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.mine.adapter.AlipayRedListAdapter;
import com.wd.daquan.mine.bean.AlipayRedListEntity;
import com.wd.daquan.mine.listener.PayDetailListener;
import com.meetqs.qingchat.pickerview.view.OptionsPickerView;
import com.da.library.tools.DateUtil;
import com.da.library.view.CommDialog;
import com.da.library.widget.CommTitle;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlipayPayListActivity extends DqBaseActivity<MinePresenter, DataBean> implements AliPayDialog.onAliPlayClick,
        View.OnClickListener, PayDetailListener {

    private String type = "2";
    private int pageNum = 1;
    private String mData;

    private AliPayDialog mAliPayDialog = null;
    private OptionsPickerView mPvOptions;
    /**
     * 支付宝收到、发出的红包和取消授权的dialog
     */
    private CommTitle mTitleLayout;
    /**
     *支付宝红包集合
     */
    private ArrayList<AlipayRedListEntity.ListBean> mAliPayRedList = new ArrayList<>();
    private TextView mYearMonthTv;
    private ImageView mUserHeadIv;
    private TextView mUserNameIv;
    private TextView mAliPayAccountTv;
    private TextView mReceiveRedTv;
    private TextView mSendRedTv;
    private AlipayRedListAdapter mAdapter = null;
    private View NoDataTv;
    private View mReceiveAliPayRedLine;
    private View mSendAliPayRedLine;
    private CommDialog commDialog;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerview;
    private ClassicsFooter mClassicsFooter;
    private ClassicsHeader mClassicsHeader;

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
        setContentView(R.layout.alipay_pay_list_activity);
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
        mTitleLayout = findViewById(R.id.alipayPayDetailsCommtitle);

        mYearMonthTv = findViewById(R.id.year_month_tv);
        mUserHeadIv = findViewById(R.id.user_head_iv);
        mUserNameIv = findViewById(R.id.user_name_tv);
        mAliPayAccountTv = findViewById(R.id.ali_pay_account_tv);
        mReceiveRedTv = findViewById(R.id.receive_ali_pay_red_tv);
        mSendRedTv = findViewById(R.id.send_ali_pay_red_tv);
        mReceiveAliPayRedLine = findViewById(R.id.receive_ali_pay_red_line);
        mSendAliPayRedLine = findViewById(R.id.send_ali_pay_red_line);

        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerview = findViewById(R.id.comm_recycler_view);
        mClassicsHeader=  findViewById(R.id.comm_recycler_view_header);
        mClassicsFooter = findViewById(R.id.comm_recycler_view_footer);
        NoDataTv = findViewById(R.id.no_data_tv);

    }

    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getNetData(type);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                switch (type) {
                    case KeyValue.ONE_STRING:
                        getNetData(KeyValue.ONE_STRING);
                        break;
                    case KeyValue.TWO_STRING:
                        getNetData(KeyValue.TWO_STRING);
                        break;
                }
            }
        });

        mTitleLayout.getLeftIv().setOnClickListener(this);
        mTitleLayout.getRightIv().setOnClickListener(this);
        mYearMonthTv.setOnClickListener(this);
        mYearMonthTv.setOnClickListener(this);
        mReceiveRedTv.setOnClickListener(this);
        mSendRedTv.setOnClickListener(this);
    }

    @Override
    public void initData() {

        setTitleData();
        setListView();
        initCalendarData();

        getNetData(KeyValue.TWO_STRING);
    }

    private void getNetData(String type) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(KeyValue.TYPE, type);
        hashMap.put(KeyValue.YEAR, mData);
        hashMap.put(KeyValue.PAGE, String.valueOf(pageNum));
        hashMap.put(KeyValue.LENGTH, "20");
        hashMap.put(KeyValue.SERVICE_VERSION, KeyValue.TWO_STRING);
        mPresenter.getMyAliRedpacket(DqUrl.url_myAliRedpacket, hashMap);
    }


    private void setTitleData() {
        mTitleLayout.setTitle(getString(R.string.alipay_auth));
        mTitleLayout.setRightIv(R.mipmap.title_right_more);
        mTitleLayout.setTitleLayoutBackgroundColor(getResources().getColor(R.color.color_d84e43));
        mTitleLayout.setIvBackgroundColor(R.drawable.alipay_btn_selecter);
        mTitleLayout.getRightIv().setVisibility(View.VISIBLE);
        switchReceiveSend();

        QCSharedPrefManager manager = QCSharedPrefManager.getInstance();
        String headPic = manager.getKDPreferenceUserInfo().getString(EBSharedPrefUser.headpic, "");
        String nickName = manager.getKDPreferenceUserInfo().getString(EBSharedPrefUser.nickname, "");

        mUserNameIv.setText(nickName);
        GlideUtils.loadHeader(this, headPic, mUserHeadIv);
    }

    /**
     * 选择日期数据
     */
    private void initCalendarData() {
        Calendar now = Calendar.getInstance();
        int years = now.get(Calendar.YEAR);
        int date = now.get(Calendar.MONTH) + 1;
        boolean isLastYear = date <= 3;
        List<String> listYear = DateUtil.getWheelYear(this, now, isLastYear);
        List<String> listMonth = DateUtil.getWheelMonth(this, date, isLastYear);
        List<String> listDate = DateUtil.getWheelDate(this, now);
        mPvOptions = DialogUtils.showYearToDate(this, listYear, listMonth, listDate, date, this);
        String dates = DateUtil.dateFormat(String.valueOf(date));

        mAliPayDialog = new AliPayDialog(this);
        mAliPayDialog.setOnAliPlayClick(this);

        mData = String.valueOf(years) + dates;
        String timeDate = String.valueOf(years) + "-" + dates;
        mYearMonthTv.setText(timeDate);
    }

    private void setListView() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new AlipayRedListAdapter();
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnAlipayDetailsListener(new AlipayRedListAdapter.OnAlipayDetailsListener() {
            @Override
            public void onLayout(AlipayRedListEntity.ListBean data) {
                NavUtils.gotoAlipayRedDetailsActivity(AlipayPayListActivity.this, data);
            }
        });
    }


    @Override
    public void onFailed(String url, int code, DataBean entity) {
        mRefreshLayout.finishRefresh();
        DqUtils.bequit(entity, this);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        mRefreshLayout.finishRefresh();
        if(null == entity) {
            return;
        }
        if(0 == code) {
            if(DqUrl.url_myAliRedpacket.equals(url)) {
                if(entity.data instanceof AlipayRedListEntity) {
                    AlipayRedListEntity aliPayRedEntity = (AlipayRedListEntity) entity.data;
                    loadAliPayRedData(aliPayRedEntity);
                }
            }else  if (DqUrl.url_cancelBindAlipay.equals(url)) {
                DqToast.showShort(getResources().getString(R.string.auth_alipay_cancel));
                finish();
            }
        }
    }

    /**
     * 初始化红包数据
     */
    private void loadAliPayRedData(AlipayRedListEntity redEntity) {
        mAliPayAccountTv.setText(redEntity.payee_logon_id);
        if (KeyValue.ONE == redEntity.is_all_data) {
            DqToast.showShort(getResources().getString(R.string.load_alldata));
        }
        if (null != redEntity.list) {
            mAliPayRedList.clear();
            mAliPayRedList.addAll(redEntity.list);
            NoDataTv.setVisibility(mAliPayRedList.size() > 0 ? View.GONE : View.VISIBLE);
            mAdapter.setType(type);
            if (type.equals(KeyValue.TWO_STRING)) {
                if (pageNum == 1) {
                    mAdapter.update(mAliPayRedList);
                } else {
                    mAdapter.addLists(mAliPayRedList);
                }
            } else {
                if (pageNum == 1) {
                    mAdapter.update(mAliPayRedList);
                } else {
                    mAdapter.addLists(mAliPayRedList);
                }
            }
        } else {
            if (pageNum == 1) {
                NoDataTv.setVisibility(View.VISIBLE);
            } else {
                DqToast.showShort(getResources().getString(R.string.load_alldata));
            }
        }

    }

    @Override
    public void onClick(View v) {
        if(v == mTitleLayout.getLeftIv()) {
            finish();
        }else if(v == mTitleLayout.getRightIv()) {
            //取消支付宝授权
            if (null != mAliPayDialog) {
                mAliPayDialog.showDialog();
            }
        }else if(v == mYearMonthTv) {
            //选择年月日
            if(null != mPvOptions) {
                mPvOptions.show();
            }
        }else if(v == mReceiveRedTv) {
            //收到的红包
            switchReceiveSend();
            setType(KeyValue.TWO_STRING);
        }else if(v == mSendRedTv) {
            //发出的红包
            setSendTextStyle();
            setType(KeyValue.ONE_STRING);
        }
    }

    private void setType(String type) {
        pageNum = 1;
        this.type = type;
        mAdapter.clear();
        getNetData(type);
    }

    private void setSendTextStyle() {
        mReceiveRedTv.setTextColor(getResources().getColor(R.color.text_title_black));
        mReceiveAliPayRedLine.setBackgroundColor(getResources().getColor(R.color.color_efeff4));
        mSendRedTv.setTextColor(getResources().getColor(R.color.color_d84e43));
        mSendAliPayRedLine.setBackgroundColor(getResources().getColor(R.color.color_d84e43));
    }

    private void switchReceiveSend() {
        mReceiveRedTv.setTextColor(getResources().getColor(R.color.color_d84e43));
        mReceiveAliPayRedLine.setBackgroundColor(getResources().getColor(R.color.color_d84e43));
        mSendRedTv.setTextColor(getResources().getColor(R.color.text_title_black));
        mSendAliPayRedLine.setBackgroundColor(getResources().getColor(R.color.color_efeff4));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mAliPayDialog) {
            mAliPayDialog.dismiss();
            mAliPayDialog = null;
        }

        if(null != commDialog) {
            commDialog.dismiss();
            commDialog = null;
        }
    }

    /**
     * 指定日期
     */
    @Override
    public void payDetailClick(String year, String month, String date) {
        Calendar now = Calendar.getInstance();
        int curYears = now.get(Calendar.YEAR);
        int curMonth = now.get(Calendar.MONTH) + 1;
        boolean isLastYear = curMonth <= 3;

        if (isLastYear) {
            // 等于当前年份直接查询
            if (String.valueOf(curYears).equals(year)) {
                if (Integer.parseInt(month) > curMonth) {
                    DqToast.showShort("仅支持查询最近三个月记录");
                } else {
                    requestData(year, month, date);
                }
            } else {
                boolean isRequest = false;
                int targetMonth = Integer.parseInt(month);
                for (int i = 12; i > 9; i --) {
                    if (i == targetMonth) {
                        isRequest = true;
                        break;
                    }
                }

                if (isRequest) {
                    requestData(year, month, date);
                } else {
                    DqToast.showShort("仅支持查询最近三个月记录");
                }
            }
        } else {
            requestData(year, month, date);
        }

    }

    /**
     * 请求月份数据
     * @param year
     * @param month
     * @param date
     */
    private void requestData(String year, String month, String date) {
        String timedate;
        mData = year + month + date;
        if (!TextUtils.isEmpty(date)) {
            timedate = year + "-" + month + "-" + date;
        } else {
            timedate = year + "-" + month;
        }
        mYearMonthTv.setText(timedate);

        pageNum = 1;
        switch (type) {
            case KeyValue.ONE_STRING:
                getNetData(KeyValue.ONE_STRING);
                break;
            case KeyValue.TWO_STRING:
                getNetData(KeyValue.TWO_STRING);
                break;
        }
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onAuth() {
        cancelBindAliPayDialog();
    }

    /**
     * 取消阿里绑定
     */
    private void cancelBindAliPayDialog() {
        commDialog = new CommDialog(this);
        commDialog.setTitleVisible(false);
        commDialog.setDescCenter();
        commDialog.setDesc(getString(R.string.cancel_alipay_auth));
        commDialog.setOkTxt(getString(R.string.confirm));
        commDialog.setOkTxtColor(getResources().getColor(R.color.text_blue));
        commDialog.setCancelTxt(getString(R.string.cancel));
        commDialog.show();

        commDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                Map<String, String> hashMap = new HashMap<>();
                mPresenter.getCancelBindAliPay(DqUrl.url_cancelBindAlipay, hashMap);
            }
        });
    }

}
