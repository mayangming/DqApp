package com.wd.daquan.mine.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.da.library.widget.CommTitle;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.IntentWrapper;
import com.wd.daquan.common.view.CommSwitchButton;
import com.wd.daquan.mine.mode.MessageNotifyHelper;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.mgr.ModuleMgr;

import java.util.LinkedHashMap;

/**
 * @author zht
 * @describe 新消息通知
 * @modify by chenliang
 */
public class NewMsgNotifyActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener {

    private CommSwitchButton switch_notify, switch_voice, switch_vibration;
    // 是否接受戳一下功能
    private CommSwitchButton mPokeSwitch;
    private LinearLayout layout_autoStart;
    private LinearLayout layout_backstageActive;
    //    private QCSharedPrefManager manager;
    private CommTitle mCommtitle;
    //手机自启动管理类
    private IntentWrapper mIntentWrapper = null;

    @Override
    public MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.newmsg_notify_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
//        manager = QCSharedPrefManager.getInstance();
        mCommtitle = findViewById(R.id.newMsgNotifyActivityTitle);
        switch_notify = findViewById(R.id.newMsgNotifyActivitySwitchNotify);
        switch_voice = findViewById(R.id.newMsgNotifySwitchVoice);
        switch_vibration = findViewById(R.id.newMsgNotifySwitchVibration);
        layout_autoStart = findViewById(R.id.newMsgNotifyLayoutStart);
        layout_autoStart.setVisibility(View.GONE);
        layout_backstageActive = findViewById(R.id.newMsgNotifyLayoutActive);
        layout_backstageActive.setVisibility(View.GONE);
        mCommtitle.setTitle(getString(R.string.new_message_show));

        mPokeSwitch = this.findViewById(R.id.newmsg_notify_poke_switchbtn);
    }

    @Override
    public void initListener() {
        //新消息通知开关
        switch_notify.setOnSwChangedListener(new CommSwitchButton.OnSwChangedListener() {
            @Override
            public void onChanged(int id, View v, boolean isChecked) {
                switch_notify.setCheckedNoEvent(isChecked);
                LinkedHashMap<String, String> newMessageHashMap = new LinkedHashMap<>();
                newMessageHashMap.put("msg_notify", isChecked ? KeyValue.ZERO_STRING : KeyValue.ONE_STRING);
                mPresenter.setNewMessageNotify(DqUrl.url_set_msg_notify, newMessageHashMap);
            }
        });

        //设置声音
        switch_voice.setOnSwChangedListener(new CommSwitchButton.OnSwChangedListener() {
            @Override
            public void onChanged(int id, View v, boolean isChecked) {
                switch_voice.setCheckedNoEvent(isChecked);
                MessageNotifyHelper.getInstance().setNewMessageVoice(isChecked ? KeyValue.NEW_MESSAGE_VOICE_ON : KeyValue.NEW_MESSAGE_VOICE_OFF);
                setMsgRemind(true, switch_vibration.isChecked());
            }
        });

        //设置震动
        switch_vibration.setOnSwChangedListener(new CommSwitchButton.OnSwChangedListener() {
            @Override
            public void onChanged(int id, View v, boolean isChecked) {
                switch_vibration.setCheckedNoEvent(isChecked);
                MessageNotifyHelper.getInstance().setNewMessageShake(isChecked ? KeyValue.NEW_MESSAGE_SHAKE_ON : KeyValue.NEW_MESSAGE_SHAKE_OFF);
                setMsgRemind(switch_voice.isChecked(), true);
            }
        });

        mPokeSwitch.setOnSwChangedListener(new CommSwitchButton.OnSwChangedListener() {
            @Override
            public void onChanged(int id, View v, boolean isChecked) {
                mPokeSwitch.setCheckedNoEvent(isChecked);
                LinkedHashMap<String, String> newMessageHashMap = new LinkedHashMap<>();
                if (isChecked) {
                    newMessageHashMap.put("poke", KeyValue.ZERO_STRING);
                } else {
                    newMessageHashMap.put("poke", KeyValue.ONE_STRING);
                }
                mPresenter.setPoke(DqUrl.url_set_poke_status, newMessageHashMap);
            }
        });
        mCommtitle.getLeftIv().setOnClickListener(this);
        layout_autoStart.setOnClickListener(this);
        layout_backstageActive.setOnClickListener(this);
    }

    @Override
    public void initData() {
        String msg_no = MessageNotifyHelper.getInstance().getNewMessageNotify();
        mIntentWrapper = new IntentWrapper();

        //新消息通知开关
        if (!TextUtils.isEmpty(msg_no)) {
            if (msg_no.equals(KeyValue.ZERO_STRING)) {
                switch_notify.setCheckedNoEvent(true);
//                RongIM.getInstance().setNotificationQuiteHoursConfigured(false);
            } else if (msg_no.equals(KeyValue.ONE_STRING)) {
                switch_notify.setCheckedNoEvent(false);
//                RongIM.getInstance().setNotificationQuiteHoursConfigured(true);
            }
        }
        initOnAndOff();

    }

    @Override
    public void onClick(View v) {
        if (v == mCommtitle.getLeftIv()) {
            finish();
        } else if (v.getId() == R.id.newMsgNotifyLayoutStart) {
            mIntentWrapper.setAutoStart(this);
        } else if (v.getId() == R.id.newMsgNotifyLayoutActive) {
            mIntentWrapper.setBackstageOptimization(this);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (url.equals(DqUrl.url_set_msg_notify)) {
            if (switch_notify.isChecked()) {
                //开启
                switchOn();
            } else {
                //关闭
                switchOff();
            }

            String str = switch_notify.isChecked() ? KeyValue.ZERO_STRING : KeyValue.ONE_STRING;
            MessageNotifyHelper.getInstance().setNewMessageNotify(str);
        } else if (url.equals(DqUrl.url_set_poke_status)) {
            // TODO: 2019/1/21 存储状态
            String str = mPokeSwitch.isChecked() ? KeyValue.ZERO_STRING : KeyValue.ONE_STRING;
            ModuleMgr.getCenterMgr().savePoke(str);
        }
    }

    private void initOnAndOff() {
        String btnoise = MessageNotifyHelper.getInstance().getNewMessageVoice();//提示音
        String btvib = MessageNotifyHelper.getInstance().getNewMessageShake();//震动
        //消息是否有提示音
        if (!TextUtils.isEmpty(btnoise)) {
            if (btnoise.equals(KeyValue.NEW_MESSAGE_VOICE_ON)) {
                switch_voice.setCheckedNoEvent(true);
            } else if (btnoise.equals(KeyValue.NEW_MESSAGE_VOICE_OFF)) {
                switch_voice.setCheckedNoEvent(false);
            }
        }

        //消息是否震动
        if (!TextUtils.isEmpty(btvib)) {
            if (btvib.equals(KeyValue.NEW_MESSAGE_SHAKE_ON)) {
                switch_vibration.setCheckedNoEvent(true);
            } else if (btvib.equals(KeyValue.NEW_MESSAGE_SHAKE_OFF)) {
                switch_vibration.setCheckedNoEvent(false);
            }
        }

        // 是否开启戳一下消息
        boolean enablePoke = ModuleMgr.getCenterMgr().enablePoke();
        if (enablePoke) {
            mPokeSwitch.setCheckedNoEvent(true);
        } else {
            mPokeSwitch.setCheckedNoEvent(false);
        }
    }

    private void switchOn() {
        switch_voice.setCheckedNoEvent(true);
        switch_vibration.setCheckedNoEvent(true);
        setMsgRemind(true, true);
        MessageNotifyHelper.getInstance().setNewMessageVoice(KeyValue.NEW_MESSAGE_VOICE_ON);//提示音
        MessageNotifyHelper.getInstance().setNewMessageShake(KeyValue.NEW_MESSAGE_SHAKE_ON);//震动
    }

    private void switchOff() {
        switch_voice.setCheckedNoEvent(false);
        switch_vibration.setCheckedNoEvent(false);
        setMsgRemind(false, false);
        MessageNotifyHelper.getInstance().setNewMessageVoice(KeyValue.NEW_MESSAGE_VOICE_OFF);//提示音
        MessageNotifyHelper.getInstance().setNewMessageShake(KeyValue.NEW_MESSAGE_SHAKE_OFF);//震动
    }

    private void setMsgRemind(boolean isRemindVoice, boolean isRemindVibrate) {
        Log.e("YM","应用通知设置");
    }
}
