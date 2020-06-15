package com.wd.daquan.chat.group.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.utils.GsonUtils;
import com.wd.daquan.third.session.SessionHelper;
import com.da.library.widget.CommTitle;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AddGroupActivity extends DqBaseActivity<TeamPresenter, DataBean> implements View.OnClickListener {
    private CommTitle mCommTitle;
    private String mSourceUid;
    private ImageView mGroupAvatar;
    private TextView txt_Groupname;
    private TextView txt_Groupnum;
    private TextView txt_addGroup;
    private GroupInfoBean bean;
    private LinearLayout layout_manager;
    private RelativeLayout layout_groupIns;

    @Override
    protected TeamPresenter createPresenter() {
        return new TeamPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.add_group_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mCommTitle = findViewById(R.id.addGroupActivityCommtitle);
        mGroupAvatar = findViewById(R.id.addGroupActivityAvatar);
        txt_Groupname = findViewById(R.id.addGroupActivityGroupName);
        txt_Groupnum = findViewById(R.id.addGroupActivityGroupNum);
        txt_addGroup = findViewById(R.id.addGroupActivityTxtAddGroup);
        layout_manager =findViewById(R.id.addGroupActivityLayout);
        layout_groupIns = findViewById(R.id.addGroupActivityLayoutIns);
        mCommTitle.setTitle(R.string.add_group_add);
    }

    @Override
    public void initListener() {
        txt_addGroup.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void initData() {
        bean = getIntent().getParcelableExtra(KeyValue.QR.Add_GROUP_BEAN);
        mSourceUid = getIntent().getStringExtra(KeyValue.QR.SOURCE_UID);
        if(bean == null) {
            return;
        }
        if (bean.isExamine()){
            mGroupAvatar.setVisibility(View.GONE);
            txt_addGroup.setVisibility(View.GONE);
            txt_Groupname.setVisibility(View.GONE);
            txt_Groupnum.setVisibility(View.GONE);
            layout_manager.setVisibility(View.GONE);
            layout_groupIns.setVisibility(View.VISIBLE);
            return;
        }

        GlideUtils.load(this, bean.group_pic, mGroupAvatar);
        txt_Groupname.setText(bean.group_name);
        String memberNum = bean.group_member_num + "人";
        txt_Groupnum.setText(memberNum);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.addGroupActivityTxtAddGroup:
                Map<String, String> linkedHashMap = new LinkedHashMap<>();
                List<String> list = new ArrayList<>();
                list.add(ModuleMgr.getCenterMgr().getUID());
                linkedHashMap.put("group_uid", GsonUtils.toJson(list));
                linkedHashMap.put("group_id", bean.group_id);
                linkedHashMap.put("type", KeyValue.TWO_STRING);
//                linkedHashMap.put("source_uid", mSourceUid);
                mPresenter.addGroup(DqUrl.url_join_group, linkedHashMap);
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        DqUtils.bequit(entity, this);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if(DqUrl.url_join_group.equals(url)){
            SessionHelper.startTeamSession(this, bean.group_id);
            finish();
        }
    }

}
