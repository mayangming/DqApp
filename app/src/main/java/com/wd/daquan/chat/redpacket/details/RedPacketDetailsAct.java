package com.wd.daquan.chat.redpacket.details;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.chat.redpacket.RedPacketPresenter;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.view.CustomRecyclerView;
import com.da.library.widget.CommTitle;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

/**
 * 红包详情
 * Created by Kind on 2019-05-17.
 */
public class RedPacketDetailsAct extends DqBaseActivity<RedPacketPresenter, DataBean> {

    private CommTitle baseTitle;
    private RecyclerView recyclerView;

    private ImageView headAvater;
    private TextView headName, headGreet, headMoney, headMoneyContent, headMoneyContents;
    private LinearLayout headGroup;

    @Override
    protected RedPacketPresenter createPresenter() {
        return new RedPacketPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.redpacket_details_act);
    }

    @Override
    protected void initView() {
        baseTitle = findViewById(R.id.base_title);
        setTitle();

        CustomRecyclerView customRecyclerView = findViewById(R.id.redpacket_details_customrecyclerview);
        SmartRefreshLayout refreshLayout = customRecyclerView.getRefreshLayout();
        recyclerView = customRecyclerView.getRefreshRecyclerView();

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        LinearLayout layout = customRecyclerView.getRefreshLayoutHeader();
        View header = getLayoutInflater().inflate(R.layout.redpacket_send_details_head, null);
        layout.addView(header);

        headAvater =  header.findViewById(R.id.redpacket_details_head_avater);
        headName = header.findViewById(R.id.redpacket_details_head_name);
        headGreet = header.findViewById(R.id.redpacket_details_head_greet);
        headGroup = header.findViewById(R.id.redpacket_details_head_group);
        headMoney = header.findViewById(R.id.redpacket_details_head_money);

        headMoneyContent = header.findViewById(R.id.redpacket_details_head_money_content);
        headMoneyContents = header.findViewById(R.id.redpacket_details_head_money_contents);
        customRecyclerView.showRefreshlayout();
    }

    /**
     * 标题
     */
    private void setTitle(){
        baseTitle.setTitleLayoutBackgroundColor(getResources().getColor(R.color.color_d84e43));
        baseTitle.setTitle(getString(R.string.redpacket_details_title));
        baseTitle.setLeftVisible(View.VISIBLE);
        baseTitle.getLeftIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void initData() {
        RedPacketDetailsAdapter detailsAdapter = new RedPacketDetailsAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(detailsAdapter);
    }
}
