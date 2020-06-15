package com.wd.daquan.common.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wd.daquan.R;
import com.da.library.view.custom.CustomFrameLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * Created by Kind on 2018/10/29.
 */

public class CustomRecyclerView extends LinearLayout {

    private Context context;
    private CustomFrameLayout customFrameLayout;

    public CustomRecyclerView(Context context) {
        this(context, null, 0);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    private void initView() {
        inflate(context, R.layout.custom_status_recyclerview, this);
        customFrameLayout = findViewById(R.id.custom_FrameLayout);
        customFrameLayout.setList(new int[]{R.id.common_refreshlayout, R.id.common_nodata,
                R.id.common_loading, R.id.common_net_error});

        setRefreshDefault();
    }

    /**
     * RefreshLayout
     *
     * @return
     */
    public SmartRefreshLayout getRefreshLayout() {
        return (SmartRefreshLayout) customFrameLayout.findViewById(R.id.common_refreshlayout);
    }


    public LinearLayout getRefreshLayoutHeader() {
        return (LinearLayout) customFrameLayout.findViewById(R.id.common_refreshlayout_recyclerView_header);
    }

    /**
     * RecyclerView
     * @return
     */
    public RecyclerView getRefreshRecyclerView() {
        return (RecyclerView) customFrameLayout.findViewById(R.id.common_refreshlayout_recyclerView);
    }

    /**
     * 设置默认头部，底部
     */
    private void setRefreshDefault() {
        RefreshLayout refreshLayout = getRefreshLayout();
        //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        //设置 Footer 为 球脉冲 样式
        //refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Translate));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
    }

    /**
     * 显示loading
     */
    public void showLoading() {
        customFrameLayout.show(R.id.common_loading);
    }

    /**
     * 显示网络错误
     */
    public void showNetError() {
        customFrameLayout.show(R.id.common_net_error);
    }

    public void showNetError(String btn_text, OnClickListener listener) {
        customFrameLayout.show(R.id.common_net_error);
        TextView btn = (TextView) findViewById(R.id.error_btn);
        btn.setVisibility(VISIBLE);
        btn.setText(btn_text);
        btn.setOnClickListener(listener);
    }

    /**
     * 显示无数据状态
     */
    public void showNoData() {
        customFrameLayout.show(R.id.common_nodata);
    }

    public void showNoData(String str) {
        customFrameLayout.show(R.id.common_nodata);
        TextView nodata_txt = (TextView) findViewById(R.id.nodata_txt);
        nodata_txt.setText(str);
    }

    public void showNoData(String content, String btn_txt, OnClickListener listener) {
        customFrameLayout.show(R.id.common_nodata);
        TextView nodata_txt = (TextView) findViewById(R.id.nodata_txt);
        nodata_txt.setText(content);
        TextView btn = (TextView) findViewById(R.id.nodata_btn);
        btn.setText(btn_txt);
        btn.setVisibility(VISIBLE);
        btn.setOnClickListener(listener);
    }

    /**
     * 显示 Refreshlayout
     */
    public void showRefreshlayout() {
        customFrameLayout.show(R.id.common_refreshlayout);
    }


    public void finishRefreshOrLoadMore(){
        getRefreshLayout().finishRefresh();
        getRefreshLayout().finishLoadMore();

    }

    /**
     * 隐藏所有
     */
    public void GoneAll() {
        customFrameLayout.GoneAll();
    }
}
