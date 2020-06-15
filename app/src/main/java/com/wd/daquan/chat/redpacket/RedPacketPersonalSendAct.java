package com.wd.daquan.chat.redpacket;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.model.bean.DataBean;
import com.da.library.widget.CommTitle;

/**
 * 个人发送红包
 * Created by Kind on 2019/5/15.
 */
public class RedPacketPersonalSendAct extends DqBaseActivity<RedPacketPresenter, DataBean> {

    private CommTitle baseTitle;
    private EditText personalSendInAmount, personalSendGreet;
    private TextView personalSendMoney;

    @Override
    protected RedPacketPresenter createPresenter() {
        return new RedPacketPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.redpacket_personal_send_act);
    }

    @Override
    protected void initView() {
        baseTitle = findViewById(R.id.base_title);
        setTitle();

        personalSendInAmount = findViewById(R.id.redpacket_personal_send_in_amount);
        personalSendGreet = findViewById(R.id.redpacket_personal_send_greet);
        personalSendMoney = findViewById(R.id.redpacket_personal_send_money);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.redpacket_personal_send_determine).setOnClickListener(this);
    }

    /**
     * 标题
     */
    private void setTitle(){
        baseTitle.setTitleLayoutBackgroundColor(getResources().getColor(R.color.app_page_bg));
        baseTitle.setTitle(getString(R.string.redpacket_send_title));
        baseTitle.setTitleTextColor(getResources().getColor(R.color.color_252525));
        baseTitle.setLeftVisible(View.GONE);
        baseTitle.setLeftTxt(getString(R.string.comm_cancel), getResources().getColor(R.color.color_252525), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedPacketPersonalSendAct.this.finish();
            }
        });
        baseTitle.setRightVisible(View.VISIBLE);
        baseTitle.setRightImageResource(R.mipmap.icon_redzhipy, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void initData() {


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.redpacket_personal_send_determine:

                break;
        }
    }
}
