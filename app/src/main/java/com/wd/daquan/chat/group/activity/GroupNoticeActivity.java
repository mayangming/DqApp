package com.wd.daquan.chat.group.activity;

import android.annotation.SuppressLint;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.db.helper.TeamDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.rxbus.MsgKey;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

import java.util.HashMap;
import java.util.Map;

/**
 * 群公告
 * Created by Kind on 2018/9/21.
 */

public class GroupNoticeActivity extends DqBaseActivity<ChatPresenter, DataBean> {

    private EditText mEdit;
    private TextView mNoDataTv;
    private TextView mNoticeModifyHintTv;
    private TextView mNoticeModifyTimeTv;

    private String groupId;

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_group_notice);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mEdit = findViewById(R.id.edit_area);
        mEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(749)});
        mNoDataTv = findViewById(R.id.tvwu);
        mNoticeModifyHintTv = findViewById(R.id.notice_modify_hint_tv);
        mNoticeModifyTimeTv = findViewById(R.id.notice_modify_time_tv);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        setBackView();
        groupId = getIntent().getStringExtra(KeyValue.GROUPID);
        boolean isCreated = getIntent().getBooleanExtra(KeyValue.GROUP_MODIFY_ANNOUNCEMENT, false);
        String time = getIntent().getStringExtra(KeyValue.GROUP_NOTICE_MODIFY_TIME);
        String content = getIntent().getStringExtra(KeyValue.GROUP_ANNOUNCEMENT);

        if (isCreated) {
            setTitle(getString(R.string.group_notice_modify));
            getCommTitle().setRightTxt(getString(R.string.complete), v -> onComplete());

            mEdit.setFocusable(true);
            mNoDataTv.setVisibility(View.GONE);
            mNoticeModifyHintTv.setVisibility(View.VISIBLE);
            mEdit.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(content)) {
                mEdit.setText(content);
                //发布时间
                mNoticeModifyTimeTv.setVisibility(View.VISIBLE);
                mNoticeModifyTimeTv.setText(getString(R.string.group_notice_modify_time) + time);
            }
        } else {
            setTitle(getString(R.string.group_announcement));
            getCommTitle().setRightGone();
            mNoticeModifyHintTv.setVisibility(View.GONE);
            mEdit.setFocusable(false);
            if (TextUtils.isEmpty(content)) {
                mNoDataTv.setVisibility(View.VISIBLE);
                mNoticeModifyTimeTv.setVisibility(View.GONE);
                mEdit.setVisibility(View.GONE);
            } else {
                mNoDataTv.setVisibility(View.GONE);
                mEdit.setVisibility(View.VISIBLE);
                mEdit.setText(content);
                //发布时间
                mNoticeModifyTimeTv.setVisibility(View.VISIBLE);
                mNoticeModifyTimeTv.setText(getString(R.string.group_notice_modify_time) + time);
            }
        }

        mEdit.setSelection(mEdit.getText().toString().trim().length());
    }

    @Override
    protected void initListener() {

    }

    private void onComplete() {
        String notice = mEdit.getText().toString().trim();
        mPresenter.setGroupInfo(groupId, KeyValue.Group.NOTICE, notice);
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        DqToast.showShort("公告修改成功");
        String notice = mEdit.getText().toString();

        TeamDbHelper.getInstance().getTeam(groupId, team -> {
            team.announcement = notice;
            TeamDbHelper.getInstance().update(team, null);
            //群公告通知
            MsgMgr.getInstance().sendMsg(MsgType.MT_GROUP_SETTING_ANNOUNCEMENT, notice);
        });

        //群主修改公告之后@所有人
        if (TextUtils.isEmpty(notice)) {//如果是空直接跳出
            finish();
            return;
        }


        //@all通知
        Map<String, String> map = new HashMap<>();
        map.put(MsgKey.GroupNotice.KEY_AIT_ALL, notice);
        MsgMgr.getInstance().sendMsg(MsgType.MT_AIT_ALL_NOTICE, map);

        finish();
    }
}
