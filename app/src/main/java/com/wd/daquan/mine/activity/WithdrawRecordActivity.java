package com.wd.daquan.mine.activity;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.da.library.adapter.CommRecyclerViewAdapter;
import com.da.library.view.CustomBottomSheetDialog;
import com.da.library.view.pickerios.picker.LoopView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.mine.adapter.WithdrawRecordAdapter;
import com.wd.daquan.mine.presenter.WalletCloudPresenter;
import com.wd.daquan.model.bean.CloudWithdrawRecordEntity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.WithdrawRecordBean;
import com.wd.daquan.model.bean.WithrawRecordEntity;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.log.DqToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提现记录页
 */
public class WithdrawRecordActivity extends DqBaseActivity<WalletCloudPresenter, DataBean> {

    private RecyclerView withdrawRecord;
    private SmartRefreshLayout smartRefreshLayout;
    private View emptyView;
    private View titleTimeContainer;//头部时间选择布局，当点击选择后数据为空时布局显示
    private View titleWithdrawDateLayout;//时间点击区域
    private TextView titleTimeTv;//头部时间选择内容
    private WithdrawRecordAdapter withdrawRecordAdapter;
    private CustomBottomSheetDialog bottomSheetDialog;
    private List<WithdrawRecordBean> recordBeans = new ArrayList<>();
    private LoopView loopViewYear;
    private LoopView loopViewMonth;
    private List<String> yearArray;
    private List<String> monthArray;
    private int page = 1;//页数，从1开始
    private int rows = 10;//每页条数，暂定10条
    private int year = -1;//年份,第一次请求可以不写
    private int month = -1;//月份,第一次请求可以不写
    /**
     * 数据来源，
     * 1、上拉加载时候正常处理
     * 2、点击时间进行刷新的时候，倘若不为空将上次数据清空。否则保持不变(刷新结束后将状态改变为上拉加载状态)
     */
    private int sourceType = 1;
    @Override
    protected WalletCloudPresenter createPresenter() {
        return new WalletCloudPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_withdraw_record);
    }

    @Override
    protected void initView() {
        titleWithdrawDateLayout = findViewById(R.id.title_withdraw_date_layout);
        titleTimeTv = findViewById(R.id.title_withdraw_date);
        titleTimeContainer = findViewById(R.id.title_time_ll);
        mTitleDqLayout = findViewById(R.id.toolbar);
        withdrawRecord = findViewById(R.id.withdraw_record);
        smartRefreshLayout = findViewById(R.id.withdraw_refreshLayout);
        emptyView = findViewById(R.id.withdraw_empty);
        initSmartRefreshLayout();
    }

    @Override
    protected void initData() {
        withdrawRecordAdapter = new WithdrawRecordAdapter();
        withdrawRecordAdapter.addLists(recordBeans);
        withdrawRecord.setLayoutManager(new LinearLayoutManager(this));
        withdrawRecord.setAdapter(withdrawRecordAdapter);
        withdrawRecordAdapter.setItemOnClickForView(new CommRecyclerViewAdapter.ItemOnClickForView<WithdrawRecordBean>() {
            @Override
            public void itemOnClickForView(int position, WithdrawRecordBean withdrawRecordBean,@IdRes int viewId) {
                String year =  String.valueOf(withdrawRecordBean.getYear());
                String month =  String.valueOf(withdrawRecordBean.getMonth());
                int yearIndex = yearArray.indexOf(year);
                int monthIndex =monthArray.indexOf(month);
                loopViewYear.setCurrentItem(yearIndex);
                loopViewMonth.setCurrentItem(monthIndex);
                showBottomTime();
            }
        });
        titleWithdrawDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomTime();
            }
        });
        initBottomTime();
        refreshRecord(page,rows,-1,-1);
    }

    private void refreshRecord(int page,int rows,int year,int month){
        Map<String,String> params = new HashMap<>();
        params.put("page",String.valueOf(page));
        params.put("rows",String.valueOf(rows));
        if (-1 != year){
            params.put("year",String.valueOf(year));
            params.put("month",String.valueOf(month));
        }
        mPresenter.geUserCloudWalletTransactionRecord(DqUrl.url_user_cloud_wallet_transaction_record,params);
    }

    private void initSmartRefreshLayout(){
        smartRefreshLayout.setEnableRefresh(false);//禁止下拉刷新
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                refreshRecord(page,rows,year,month);
            }
        });
    }

    /**
     * 显示底部时间选择UI
     */
    private void showBottomTime(){
        bottomSheetDialog.show();
    }

    private void initBottomTime(){
        yearArray = new ArrayList<>();
        for (int i = 2010; i <= 2030; i++){
            yearArray.add(String.valueOf(i));
        }
        monthArray = new ArrayList<>();
        for (int i = 1; i <= 12; i++){
            monthArray.add(String.valueOf(i));
        }

        View content = getLayoutInflater().inflate(R.layout.dailog_bottom_time,null,false);
        loopViewYear = content.findViewById(R.id.loopView_year);
        loopViewMonth = content.findViewById(R.id.loopView_month);
        View sureBtn = content.findViewById(R.id.date_loop_sure);
        View cancelBtn = content.findViewById(R.id.date_loop_cancel);
        loopViewYear.setList(yearArray);
        loopViewYear.setNotLoop();
        loopViewYear.setCurrentItem(0);
        loopViewMonth.setList(monthArray);
        loopViewMonth.setNotLoop();
        loopViewMonth.setCurrentItem(0);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                int yearSelectIndex = loopViewYear.getCurrentItem();
                int monthSelectIndex = loopViewMonth.getCurrentItem();
                String yearSelectValue = yearArray.get(yearSelectIndex);
                String monthSelectValue = monthArray.get(monthSelectIndex);
                String content = yearSelectValue.concat("年").concat(monthSelectValue).concat("月");
                DqLog.e("YM---->选中的年月:"+content);
                sourceType = 2;
                page = 1;
                year = Integer.valueOf(yearSelectValue);
                month = Integer.valueOf(monthSelectValue);
                recordBeans.clear();
                refreshRecord(page,rows,year,month);
                bottomSheetDialog.dismiss();
                titleTimeTv.setText(year+"年"+month+"月");
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog = new CustomBottomSheetDialog(this);
        bottomSheetDialog.setContentView(content);
        bottomSheetDialog.setmBottomSheetCallback((FrameLayout)(content.getParent()));
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_user_cloud_wallet_transaction_record.equals(url)){
            CloudWithdrawRecordEntity cloudWithdrawRecordEntity = (CloudWithdrawRecordEntity) entity.data;
            List<WithrawRecordEntity> withrawRecordEntities = cloudWithdrawRecordEntity.getRecordResponseDtos();
            smartRefreshLayout.finishLoadMore();
            dismissLoading();
            if (withrawRecordEntities.size() == 0){
                DqToast.showShort("没有更多数据了");
                if (1 == page && 1 == sourceType){
                    emptyView.setVisibility(View.VISIBLE);
                    smartRefreshLayout.setVisibility(View.GONE);
                }else {
                    if (sourceType == 2){
                        titleTimeContainer.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.VISIBLE);
                        smartRefreshLayout.setVisibility(View.GONE);
                    }else {
                        emptyView.setVisibility(View.GONE);
                        smartRefreshLayout.setVisibility(View.VISIBLE);
                    }
                }
                return;
            }
            titleTimeContainer.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            smartRefreshLayout.setVisibility(View.VISIBLE);
            if (2 == sourceType){
                recordBeans.clear();
            }
            sourceType = 1;
            List<WithdrawRecordBean> recordBeanList = mPresenter.parserWalletTransactionRecord(recordBeans,cloudWithdrawRecordEntity);
            withdrawRecordAdapter.update(recordBeanList);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if (DqUrl.url_user_cloud_wallet_transaction_record.equals(url)){
            smartRefreshLayout.finishLoadMore();//停止刷新
        }
    }
}