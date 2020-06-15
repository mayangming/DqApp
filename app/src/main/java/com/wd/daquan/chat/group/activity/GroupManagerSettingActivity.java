package com.wd.daquan.chat.group.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.chat.group.adapter.GroupSetManagersAdapter;
import com.wd.daquan.chat.group.bean.GroupManagersAllResp;
import com.wd.daquan.chat.group.bean.GroupManagersItemResp;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.SpannableStringUtils;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.log.DqToast;
import com.da.library.listener.DialogListener;
import com.da.library.view.CommDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理员设置
 * Created by Kind on 2018/9/26.
 */

public class GroupManagerSettingActivity extends DqBaseActivity<ChatPresenter, DataBean> {

    private String group_id;
    private ImageView img_master;
    private TextView txt_master;
    private TextView txt_managers;
    private ListView listview_managers;
    private LinearLayout layout_managers;
    private RelativeLayout layout_add;
    private RelativeLayout layout_master;
    private GroupSetManagersAdapter managersAdapter;
    private GroupManagersAllResp groupManagersAllResp;
    private String deleteUid;

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_group_set_manager);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        setTitle("设置管理员");
        setBackView();
        img_master = findViewById(R.id.groupSetMangerMasterAvatar);
        txt_master = findViewById(R.id.groupSetMangerMasterName);
        txt_managers = findViewById(R.id.groupSetMangerManagersTitle);
        layout_master = findViewById(R.id.groupSetMangerMasterLayout);
        layout_managers = findViewById(R.id.groupSetMangerManagersLayout);
        listview_managers = findViewById(R.id.groupSetMangerList);
        layout_add = findViewById(R.id.groupSetMangerAddLayout);
    }

    @Override
    protected void initData() {
        group_id = getIntent().getStringExtra(KeyValue.GROUPID);
        mPresenter.getGroupAdminList(group_id);
    }

    @Override
    protected void initListener() {
        layout_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   NavUtils.gotoGroupMembersActivity(GroupManagerSettingActivity.this, group_id, true, 101, 1);
            }
        });
    }


    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        DqToast.showShort(entity == null ? "请求出错！" : entity.getContent());

    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if(DqUrl.url_group_managers_list.equals(url)){
            if(0 == code){
                groupManagersAllResp = (GroupManagersAllResp) entity.data;
                updataUI(groupManagersAllResp);
            }
        }else if (url.equals(DqUrl.url_group_set_managers)) {//设置群管理员
            if(entity.isSuccess()){
                DqToast.showShort("删除成功");
                for(int i = 0;i < groupManagersAllResp.admin.size(); i ++){
                    if(groupManagersAllResp.admin.get(i).uid.equals(deleteUid)){
                        groupManagersAllResp.admin.remove(i);
                    }
                }
                updateMangersUI(groupManagersAllResp.admin);
              //  BroadcastManager.getInstance(this).sendBroadcast(Constants.GROUP_MANAGERS_NUM, groupManagersAllResp.admin.size() + "");
            }else {
                DqToast.showShort(entity.getContent());
            }
        }
    }


    private void updataUI(GroupManagersAllResp groupManagersItemResp){
        if(groupManagersItemResp == null)return;
        if(groupManagersItemResp.master != null){
            layout_master.setVisibility(View.VISIBLE);
            GlideUtils.loadHeader(this, groupManagersItemResp.master.headpic, img_master);
            txt_master.setText(groupManagersItemResp.master.nickname);
        }else{
            layout_master.setVisibility(View.GONE);
        }
        updateMangersUI(groupManagersItemResp.admin);
    }

    @SuppressLint("SetTextI18n")
    private void updateMangersUI(List<GroupManagersItemResp> list){
        if(list == null || list.size() == 0){
            txt_managers.setText(getResources().getText(R.string.group_manager_manager) + "0/5");
            layout_managers.setVisibility(View.GONE);
            layout_add.setVisibility(View.VISIBLE);
            return;
        }else {
            String size = list.size() + "";
            txt_managers.setText(getResources().getText(R.string.group_manager_manager) + size + "/5");
            if(list.size() >= 5){
                layout_add.setVisibility(View.GONE);
            }else{
                layout_add.setVisibility(View.VISIBLE);
            }
            layout_managers.setVisibility(View.VISIBLE);
            if (managersAdapter == null) {
                managersAdapter = new GroupSetManagersAdapter(this, list);
                listview_managers.setAdapter(managersAdapter);
            } else {
                managersAdapter.setNotifyData(list);
            }
        }
        managersAdapter.setOnClickItemDelete(new GroupSetManagersAdapter.OnClickItemDelete() {
            @Override
            public void onItemDelete(GroupManagersItemResp content) {
                deleteUid = content.uid;
                JSONArray jsonArray = new JSONArray();
                Friend friend = FriendDbHelper.getInstance().getFriend(content.uid);
//                Friend friend = SealUserInfoManager.getInstance().getFriendByID(content.uid);
                String nickName;
                if(friend != null && !TextUtils.isEmpty(friend.getRemarks())){
                    nickName = friend.getRemarks();
                }else{
                    if(TextUtils.isEmpty(content.remarks)){
                        nickName = content.nickname;
                    }else{
                        nickName = content.remarks;
                    }
                }

                settingGroupSetManger(content, nickName);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onGroupSetManger();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK){
            List<Friend> list = (List<Friend>) data.getSerializableExtra("members");
            if(list == null || list.size() <= 0){
                return;
            }
            for(Friend tmp : list){
                GroupManagersItemResp groupManagersItemResp = new GroupManagersItemResp();
                groupManagersItemResp.uid = tmp.uid;
                groupManagersItemResp.nickname = tmp.getName();
                groupManagersItemResp.headpic = tmp.headpic;
                groupManagersItemResp.phone = tmp.phone;
                groupManagersItemResp.sex = tmp.sex;
                if(groupManagersAllResp.admin == null){
                    groupManagersAllResp.admin = new ArrayList<>();
                }
                groupManagersAllResp.admin.add(groupManagersItemResp);
                updateMangersUI(groupManagersAllResp.admin);
            }
        }
    }


    private CommDialog mRemoveFriendDialog;
    private void onGroupSetManger() {
        if (mRemoveFriendDialog != null) {
            mRemoveFriendDialog.dismiss();
            mRemoveFriendDialog = null;
        }
    }

    private void settingGroupSetManger(GroupManagersItemResp managersItemResp, String nickName) {
        onGroupSetManger();
        mRemoveFriendDialog = new CommDialog(this);
        mRemoveFriendDialog.setTitleVisible(true);
        mRemoveFriendDialog.setTitle(getString(R.string.group_group_confirm_mark));
        mRemoveFriendDialog.setDescSpannableString(SpannableStringUtils.addTextColor(getString(R.string.group_delete_managers,
                nickName), 3, nickName.length() + 3, getResources().getColor(R.color.color_ff0000)));
        mRemoveFriendDialog.setOkTxt(getString(R.string.delete));
        mRemoveFriendDialog.setOkTxtColor(Color.RED);
        mRemoveFriendDialog.setCancelTxt(getString(R.string.comm_cancel));
        mRemoveFriendDialog.show();

        mRemoveFriendDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                mPresenter.setGroupManagers(group_id, 2, new JSONArray().put(managersItemResp.uid).toString());
            }
        });
    }
}