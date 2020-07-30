package com.wd.daquan.mine.wallet.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.mine.listener.PayDetailListener;
import com.wd.daquan.mine.wallet.adapter.LooseListAdapter;
import com.wd.daquan.mine.wallet.bean.LooseDetailsItemBean;
import com.wd.daquan.mine.wallet.presenter.WalletPresenter;
import com.meetqs.qingchat.pickerview.view.OptionsPickerView;
import com.da.library.tools.DateUtil;
import com.da.library.widget.CommTitle;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

public class LooseChangeTransationListActivity extends DqBaseActivity<WalletPresenter, DataBean> implements PayDetailListener {

    private TextView noDataTxt;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LooseListAdapter adapter;
    private int pagenum = 1;
    private String lengths = "20";
    private String times;
    private OptionsPickerView mPvOptions;
    private CommTitle mCommTitle;

    @Override
    protected WalletPresenter createPresenter() {
        return new WalletPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.loose_change_details_activity);
    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.looseDetailsTitle);
        noDataTxt = findViewById(R.id.looseChangeNoText);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.looseChangeRecyclerView);
        mCommTitle.setRightVisible(View.VISIBLE);
        mCommTitle.getRightIv().setImageResource(R.mipmap.icon_redzhipy);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadMore(true);
    }

    @Override
    public void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        mCommTitle.getRightIv().setOnClickListener(this);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                noDataTxt.setVisibility(View.GONE);
                pagenum = 1;
                requestData();
            }
        });
         mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
             @Override
             public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                 noDataTxt.setVisibility(View.GONE);
                 ++ pagenum;
                 requestData();
             }
        });
    }

    @Override
    public void initData() {
        adapter = new LooseListAdapter(this, null);
        mRecyclerView.setAdapter(adapter);
        Calendar now = Calendar.getInstance();
        int years = now.get(Calendar.YEAR);
        int date = now.get(Calendar.MONTH) + 1;
        List<String> listYear = DateUtil.getWheelYear(this, now);
        List<String> listMonth = DateUtil.getWheelMonth(this);
        List<String> listDate = DateUtil.getWheelDate(this);
        mPvOptions = DialogUtils.showYearToDate(this, listYear, listMonth
                , listDate, date, this);
        times = String.valueOf(years) +  DateUtil.dateFormat(String.valueOf(date));
        requestData();
    }

    private void requestData(){
        LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("year", times);
        linkedHashMap.put("length", lengths);
        linkedHashMap.put("page", pagenum + "");
        mPresenter.getLooseDetail(DqUrl.url_loose_detail, linkedHashMap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.comm_right_iv://选择年月日
                mPvOptions.show();
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        mRefreshLayout.finishRefresh();
        DqUtils.bequit(entity, this);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        mRefreshLayout.finishRefresh();
        DqApp.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (0 == code) {
                    List<LooseDetailsItemBean> listLoose = (List<LooseDetailsItemBean>)entity.data;
                    if (listLoose != null) {
                        if (listLoose.size() != 0) {
                            mRecyclerView.setVisibility(View.VISIBLE);
                            noDataTxt.setVisibility(View.GONE);
                            if (pagenum == 1) {
                                adapter.refreshLoose(listLoose);
                            } else if (pagenum > 1) {
                                adapter.addLoose(listLoose);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            if (pagenum == 1) {
                                loadNoDataUi();
                            } else {
                                DqToast.showShort("已加载全部数据");
                            }
                        }
                    }
                }
            }
        });
    }

    private void loadNoDataUi(){
        DqApp.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setEnableRefresh(false);
                mRefreshLayout.setEnableLoadMore(false);
                mRecyclerView.setVisibility(View.GONE);
                noDataTxt.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void payDetailClick(String year, String month, String date) {
        times = year + month + date;
        pagenum = 1;

        requestData();
    }
}
