package com.wd.daquan.chat.redpacket;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.model.bean.DataBean;

/**
 * 接收红包
 * Created by Kind on 2019/5/17.
 */
public class RedPacketReceiveAct extends DqBaseActivity<RedPacketPresenter, DataBean> {

    private ImageView receiveImg, receiveOpen;
    private TextView receiveName, receiveRandom, receiveGreet, receiveHand;

    @Override
    protected RedPacketPresenter createPresenter() {
        return new RedPacketPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.redpacket_receive_act);
    }

    @Override
    protected void initView() {
        receiveImg = findViewById(R.id.redpacket_receive_img);
        receiveName = findViewById(R.id.redpacket_receive_name);
        receiveRandom = findViewById(R.id.redpacket_receive_random);
        receiveGreet = findViewById(R.id.redpacket_receive_greet);

        receiveOpen = findViewById(R.id.redpacket_receive_open);
        receiveHand = findViewById(R.id.redpacket_receive_hand);






    }

    @Override
    protected void initData() {
        findViewById(R.id.redpacket_receive_close).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.redpacket_receive_close://关闭
                this.finish();
                break;
        }
    }
}
