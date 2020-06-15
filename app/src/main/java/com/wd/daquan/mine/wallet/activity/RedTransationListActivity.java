package com.wd.daquan.mine.wallet.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.mine.listener.PayDetailListener;
import com.wd.daquan.mine.wallet.adapter.RedDetailsListAdapter;
import com.wd.daquan.mine.wallet.bean.RedDetailsBean;
import com.wd.daquan.mine.wallet.holder.RedDetailsHeaderHolder;
import com.wd.daquan.mine.wallet.presenter.WalletPresenter;
import com.meetqs.qingchat.pickerview.view.OptionsPickerView;
import com.da.library.tools.DateUtil;
import com.da.library.widget.CommTitle;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;


public class RedTransationListActivity extends DqBaseActivity<WalletPresenter, DataBean> implements PayDetailListener {

    private int pagenum = 1;
    private int pagenumOut = 1;
    private String lengths = "20";
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private ImageView icon_user_head;
    private TextView tv_name;
    private TextView tv_blessing;
    private TextView tvNumRed;
    private TextView noDataTxt;
    private String str;
    private String type;
    private String nickName;
    private LinearLayout linshijian;
    private TextView tv_shijian;
    private TextView tvshoudao;
    private RedDetailsListAdapter mAdapter;
    private OptionsPickerView mPvOptions;
    private List<RedDetailsBean> mPayLists = new ArrayList<>();
    private CommTitle mCommTitle;
    private String timeDate;

    @Override
    protected WalletPresenter createPresenter() {
        return new WalletPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.red_details_activity);
    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.redDetailsTitle);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.redDetailsRecyclerView);
        noDataTxt = findViewById(R.id.redDetailsNoDataTxt);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadMore(true);

        mCommTitle.setTitleLayoutBackgroundColor(getResources().getColor(R.color.color_d84e43));
        mCommTitle.setLeftImageResource(R.mipmap.cn_comm_back);
        mCommTitle.setTitleTextColor(getResources().getColor(R.color.white));
        mCommTitle.setRightVisible(View.VISIBLE);
        mCommTitle.getRightIv().setImageResource(R.mipmap.icon_change);
    }

    @Override
    public void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        mCommTitle.getRightIv().setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (type.equals("out")) {
                    pagenum = 1;
                    requestInData();
                } else if (type.equals("in")) {
                    pagenumOut = 1;
                    requestOutData();
                }
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (type.equals("out")) {
                    ++ pagenum;
                    requestInData();
                } else if (type.equals("in")) {
                    ++pagenumOut;
                    requestOutData();
                }
            }
        });

        mAdapter.setOnRedDetailsListListener(new RedDetailsListAdapter.OnRedDetailsListListener() {
            @Override
            public void headerListener(RedDetailsHeaderHolder headerHolder) {
//                GlideUtils.load(this, headPic, headerHolder.avatar);
                String named = ModuleMgr.getCenterMgr().getNickName() + "共收到的红包";
                headerHolder.name.setText(named);

                headerHolder.time.setText(timeDate);
                headerHolder.time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPvOptions.show();
                    }
                });
            }

            @Override
            public void itemListener() {

            }
        });
    }

    @Override
    public void initData() {
        mAdapter = new RedDetailsListAdapter(this, mPayLists);
        mRecyclerView.setAdapter(mAdapter);

        Calendar now = Calendar.getInstance();
        int years = now.get(Calendar.YEAR);
        int date = now.get(Calendar.MONTH) + 1;
        List<String> listYear = DateUtil.getWheelYear(this, now);
        List<String> listMonth = DateUtil.getWheelMonth(this);
        List<String> listDate = DateUtil.getWheelDate(this);
        OptionsPickerView mPvOptions = DialogUtils.showYearToDate(this, listYear, listMonth
                , listDate, date, this);
        String time = String.valueOf(years) +  DateUtil.dateFormat(String.valueOf(date));
        timeDate = String.valueOf(years) + "-" + DateUtil.dateFormat(String.valueOf(date));

        requestInData();
    }

    private void requestInData(){
        LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("year", str);
        linkedHashMap.put("length", lengths);
        linkedHashMap.put("page", pagenum + "");
        mPresenter.getIn_redpacket_record(DqUrl.url_in_redpacket_record, linkedHashMap);
    }

    private void requestOutData(){
        LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("year", str);
        linkedHashMap.put("length", lengths);
        linkedHashMap.put("page", pagenum + "");
        mPresenter.getOut_redpacket_record(DqUrl.url_out_redpacket_record, linkedHashMap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.comm_right_iv:
                if(TextUtils.isEmpty(type)) return;
                switch (type) {
                    case "out":
                        mAdapter.replaceData(mPayLists);
                        mCommTitle.setTitle("发出的红包");
                        tvshoudao.setText("发出的红包");
                        String named = nickName + "共发出的红包";
                        tv_name.setText(named);
                        pagenumOut = 1;
                        pagenum = 1;
                        requestOutData();
                        break;
                    case "in":
                        mAdapter.replaceData(mPayLists);
                        mCommTitle.setTitle("收到的红包");
                        tvshoudao.setText("收到的红包");
                        String names = nickName + "共收到的红包";
                        tv_name.setText(names);
                        pagenum = 1;
                        pagenumOut = 1;
                        requestInData();
                        break;
                }
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        mRefreshLayout.finishRefresh();
        DqUtils.bequit(entity, this);
        if (DqUrl.url_in_redpacket_record.equals(url)) {
            type = "out";
        } else if (DqUrl.url_out_redpacket_record.equals(url)) {
            type = "in";
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        mRefreshLayout.finishRefresh();
        if (DqUrl.url_in_redpacket_record.equals(url)) {
            type = "out";
            if (0 == code) {
                RedDetailsBean rdpPayBean = (RedDetailsBean)entity.data;
                success(rdpPayBean);
            } else {
                DqToast.showShort(entity.msg);
            }
        } else if (DqUrl.url_out_redpacket_record.equals(url)) {
            type = "in";
            if (0 == code) {
                RedDetailsBean rdpPayBean = (RedDetailsBean)entity.data;
                success(rdpPayBean);
            } else {
                DqToast.showShort(entity.msg);
            }
        }
    }

    private void success(RedDetailsBean rdpPayBean) {
        if(rdpPayBean == null) return;
        DqApp.runOnUiThread(() -> {
//            String sume = "￥" + rdpPayBean.getSum();
//            String numRed = rdpPayBean.getNum() + "个";
//            tv_blessing.setText(sume);
//            tvNumRed.setText(numRed);
//
//            List<RedDetailsBean> list = rdpPayBean.getList();
//            if (type.equals("out")) {
//                if (list.size() > 0) {
//                    tvStepSum.setVisibility(View.GONE);
//                    if (pagenum == 1) {
//                        mAdapterIn.replaceAll(list);
//                    } else if (pagenum > 1) {
//                        mAdapterIn.addAll(list);
//                    }
//                } else {
//                    numberone();
//                }
//            } else if (type.equals("in")) {
//                if (list.size() > 0) {
//                    tvStepSum.setVisibility(View.GONE);
//                    if (pagenumOut == 1) {
//                        mAdapter.replaceData(list);
//                    } else if (pagenumOut > 1) {
//                        mAdapterIn.addAll(list);
//                    }
//                } else {
//                    numberone();
//                }
//            }
        });
    }

    private void numberone() {
        if (pagenum == 1 && pagenumOut == 1) {
            noDataTxt.setVisibility(View.VISIBLE);
        } else {
            DqToast.showShort("已加载全部数据");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void payDetailClick(String year, String month, String date) {
        str = year + month + date;
        String timedate;
        if (!TextUtils.isEmpty(date)) {
            timedate = year + "-" + DateUtil.dateFormat(String.valueOf(month)) + "-" + DateUtil.dateFormat(String.valueOf(date)) ;
        } else {
            timedate = year + "-" + DateUtil.dateFormat(String.valueOf(month)) + DateUtil.dateFormat(String.valueOf(date)) ;
        }
        tv_shijian.setText(timedate);
        if (type.equals("out")) {
            mAdapter.clearData();
            pagenum = 1;
            requestInData();
        } else if (type.equals("in")) {
            mAdapter.clearData();
            pagenumOut = 1;
            requestOutData();
        }
    }
}
