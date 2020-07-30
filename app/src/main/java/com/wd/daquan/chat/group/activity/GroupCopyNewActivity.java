package com.wd.daquan.chat.group.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.db.helper.TeamDbHelper;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.chat.group.bean.GroupCloseInvite;
import com.wd.daquan.chat.group.bean.GroupCopyNew;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.log.DqToast;
import com.da.library.listener.DialogListener;
import com.wd.daquan.third.session.SessionHelper;
import com.da.library.view.CommDialog;
import com.da.library.widget.CommTitle;

/**
 * 复制新群
 * Created by Kind on 2019/1/21.
 */
public class GroupCopyNewActivity extends DqBaseActivity<ChatPresenter, DataBean> {

    private ImageView groupAvater;
    private TextView groupNum;
    private Button confirmCopy;
    private CommTitle mCommTitle;
    private String groupID;

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.group_copy_new_activity);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initView() {
        mCommTitle = findViewById(R.id.base_title);
        setTitle(getString(R.string.group_manager_group_copy_new));
        setBackView();
        groupAvater = findViewById(R.id.group_avater);
        groupNum = findViewById(R.id.group_num);
        confirmCopy = findViewById(R.id.group_confirm_copy);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void initData() {
        groupID = getIntent().getStringExtra(KeyValue.GROUPID);
        GroupInfoBean groups = TeamDbHelper.getInstance().getTeam(groupID);
        if (groups == null) {
            return;
        }
        if(!TextUtils.isEmpty(groups.group_member_num )){
            groupNum.setText(groups.group_member_num + "人");
        }
        GlideUtils.load(this, groups.group_pic, groupAvater);
    }

    @Override
    protected void initListener() {
        confirmCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommDialog mCommDialog = new CommDialog(GroupCopyNewActivity.this, "", getString(R.string.group_manager_group_copy_new), new DialogListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onOk() {
                        mPresenter.reqCopyNewGroup(groupID);
                    }
                });
                mCommDialog.show();
            }
        });
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        if (DqUrl.url_group_copy.equals(url)) {
            DqToast.showShort(entity == null ? getString(R.string.net_err) : entity.content);
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (DqUrl.url_group_copy.equals(url)) {
            GroupCopyNew data = (GroupCopyNew) entity.data;
            if(data == null){
                DqToast.showShort(R.string.net_err);
                return;
            }
            if(data.refuse == null || data.refuse.size() <= 0){
                showConversation(data.group_id, data.group_name);
            }else {
                GroupCloseInvite closeInvite = data.refuse.get(0);
                if(closeInvite == null){
                    showConversation(data.group_id, data.group_name);
                    return;
                }
                DialogUtils.showGroupInviteRefuesDialog(this, data.refuse.get(0).nickname, data.refuse.size() + "",
                        new DialogListener() {
                    @Override
                    public void onCancel() {}

                    @Override
                    public void onOk() {
                        showConversation(data.group_id, data.group_name);
                    }
                });
            }
        }
    }

    private void showConversation(String group_id, String group_name){
        SessionHelper.startTeamSession(getActivity(), group_id);
    }
}
