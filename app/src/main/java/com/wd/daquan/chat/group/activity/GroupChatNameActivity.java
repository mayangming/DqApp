package com.wd.daquan.chat.group.activity;

import android.text.TextUtils;
import android.widget.EditText;

import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.db.helper.TeamDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

/**
 * 群昵称
 * Created by Kind on 2018/9/20.
 */

public class GroupChatNameActivity extends DqBaseActivity<ChatPresenter, DataBean> {

    private EditText etGroupName;
    private String groupId;
    private String groupName;

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_group_chat_name);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        setTitle(getString(R.string.groupName));
        setBackView();
        getCommTitle().setRightTxt(getString(R.string.complete), v -> onComplete());
        etGroupName = findViewById(R.id.etGroupName);
    }

    @Override
    protected void initData() {
        groupId = getIntent().getStringExtra(KeyValue.GROUPID);
        groupName = getIntent().getStringExtra(KeyValue.GROUPNAME);
        etGroupName.setText(groupName);
        etGroupName.setSelection(etGroupName.getText().length());
    }

    @Override
    protected void initListener() {

    }

    private void onComplete() {
        String metGroupName = etGroupName.getText().toString().trim();
        if (TextUtils.isEmpty(metGroupName)) {
            DqToast.showShort("请输入群名称");
            return;
        }
        if (metGroupName.length() < 2 || metGroupName.length() > 10) {
            DqToast.showShort("群名称应为 2-10 字");
            return;
        }

        mPresenter.setGroupInfo(groupId, KeyValue.Group.GROUP_NAME, metGroupName);
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        DqToast.showShort(entity == null ? "请求出错！" : entity.getContent());
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (!entity.isSuccess()) {
            DqToast.showShort(entity.getContent());
            return;
        }

        MsgMgr.getInstance().sendMsg(MsgType.MT_GROUP_SETTING_NAME, etGroupName.getText().toString().trim());
        TeamDbHelper.getInstance().getTeam(groupId, team -> {
            team.group_name = etGroupName.getText().toString().trim();
            TeamDbHelper.getInstance().update(team, null);
            MsgMgr.getInstance().sendMsg(MsgType.HOME_UPDATE_MSG, team);//通知首页更改群组名称
            finish();
        });

    }
}