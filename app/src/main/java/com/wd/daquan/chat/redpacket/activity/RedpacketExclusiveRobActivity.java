package com.wd.daquan.chat.redpacket.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.third.helper.TeamHelper;
import com.wd.daquan.third.session.extension.QcAlipayRpAttachment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: dukangkang
 * @date: 2018/9/18 13:33.
 * @description:
 * 专属红包介绍界面
 */
public class RedpacketExclusiveRobActivity extends DqBaseActivity implements View.OnClickListener{
    private TextView txt_name;
    private TextView txt_blessing;
    private ImageView img_avatar;
    private ImageView img_close;
    private String mTargetId;
//    private IMMessage mMessage = null;
    private QcAlipayRpAttachment mQcAlipayRpAttachment;
    @Override
    protected Presenter.IPresenter createPresenter() {
        return null;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.redpacket_exclusive_activity);
    }

    @Override
    protected void init() {
        mTargetId = getIntent().getStringExtra(KeyValue.RedpacktRob.TARGET_ID);
        mQcAlipayRpAttachment = (QcAlipayRpAttachment) getIntent().getSerializableExtra(KeyValue.RedpacktRob.ATTACHMENT);
//        mMessage = (IMMessage) getIntent().getSerializableExtra(KeyValue.RedpacktRob.MESSAGE);
//        if(mMessage != null){
//            mQcAlipayRpAttachment = (QcAlipayRpAttachment) mMessage.getAttachment();
//        }
    }

    @Override
    protected void initView() {
        txt_name = findViewById(R.id.redpacketExclusiveName);
        txt_blessing = findViewById(R.id.redpacketExclusiveBlessing);
        img_avatar = findViewById(R.id.redpacketExclusiveAvatar);
        img_close = findViewById(R.id.redpacketExclusiveClose);
    }

    @Override
    protected void initData() {
        GlideUtils.load(this, mQcAlipayRpAttachment.sendPic, img_avatar);
        String sendName = TeamHelper.getDisplayNameWithoutMe(mTargetId, mQcAlipayRpAttachment.sendId);
        if(TextUtils.isEmpty(sendName)){
            txt_name.setText(sendName);
        }else{
            txt_name.setText(mQcAlipayRpAttachment.sendName);
        }
        getBlessing();
    }

    private void getBlessing(){
        JSONArray jsonArray = null;
        StringBuilder stringBuilder=new StringBuilder();
        if (!TextUtils.isEmpty(mQcAlipayRpAttachment.redpacket_extra) || !mQcAlipayRpAttachment.redpacket_extra.equals("[]") ||
                !mQcAlipayRpAttachment.redpacket_extra.equals("null")) {
            try {
                jsonArray = new JSONArray(mQcAlipayRpAttachment.redpacket_extra);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                    String displayName = TeamHelper.getDisplayNameWithoutMe(mTargetId, jsonObject.getString("uid"));
                    if (TextUtils.isEmpty(displayName)) {
                        stringBuilder.append(displayName);
                    }else{
                        stringBuilder.append(jsonObject.getString("nickName"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String str;
            if (stringBuilder.toString().length()>30){
                str = stringBuilder.toString().substring(0,30)+"..";
            }else{
                str = stringBuilder.toString();
            }
            if (null != jsonArray && jsonArray.length() > 0) {
                String blessing = str + "等" + jsonArray.length() + "人的专属红包";
                txt_blessing.setText(blessing);
            }
        }
    }

    @Override
    protected void initListener() {
        img_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.redpacketExclusiveClose:
                finish();
                break;
        }
    }
}
