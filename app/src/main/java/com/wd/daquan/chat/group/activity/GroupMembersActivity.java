package com.wd.daquan.chat.group.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.chat.group.adapter.GroupMembersAdapter;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.PinyinComparator;
import com.wd.daquan.common.utils.SpannableStringUtils;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.contacts.utils.CharacterParser;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.da.library.listener.DialogListener;
import com.da.library.tools.Utils;
import com.da.library.view.CommDialog;
import com.da.library.widget.CommSearchEditText;
import com.da.library.widget.SideBar;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 选择列表
 * Created by Kind on 2018/9/26.
 */

public class GroupMembersActivity extends DqBaseActivity<ChatPresenter, DataBean> {

    public TextView dialog;
    private ListView mListView;
    private GroupMembersAdapter membersAdapter;
    private PinyinComparator pinyinComparator;
    private List<Friend> sourceDataList = new ArrayList<>();
    private RelativeLayout selectGroupLayout;
    private EditText et_search;
    private String groupId;
    private boolean isShowSelected;//初始化的时候是否显示选中状态
    public List<Boolean> checkList = new ArrayList();
    private Friend transfer_friend = null;//转让新的群主
    private int type;//功能类型
    public int managerNum = 0;
    private CommSearchEditText mSearchEditText;

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_select_friends);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        setBackView();
        //实例化汉字转拼音类
        pinyinComparator = PinyinComparator.getInstance();
        mListView = findViewById(R.id.listview);
        selectGroupLayout = findViewById(R.id.selectGroupLayout);

        SideBar mSidBar = findViewById(R.id.side_bar);
        dialog = findViewById(R.id.group_dialog);
        mSidBar.setTextView(dialog);
        //设置右侧触摸监听
        mSidBar.setOnTouchingLetterChangedListener(s -> {
            //该字母首次出现的位置
            int position = membersAdapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                mListView.setSelection(position);
            }
        });

        mSearchEditText = findViewById(R.id.search_layout);
        et_search = mSearchEditText.getEditText();
    }

    @Override
    protected void initData() {
        groupId = getIntent().getStringExtra(KeyValue.GROUPID);
        isShowSelected = getIntent().getBooleanExtra(KeyValue.FROM_TYPE_FRIENDS, false);
        selectGroupLayout.setVisibility(View.GONE);
        type = getIntent().getIntExtra("type", -1);
        //请求群成员
        mPresenter.getSelectGroupUser(groupId);
//        setClickable(isShowSelected);
        if (1 == type) {
            setTitle("添加管理员");
//            setClickable(false);
        } else if (2 == type) {
            setTitle("选择新群主");
//            setClickable(false);
        }

        DqApp.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setClickable(false);
            }
        });
        getCommTitle().setRightTxt(getString(R.string.complete), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }

    @Override
    protected void initListener() {
        /**
         * 功能隐藏了，暂时用不上
         */
        selectGroupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     NavUtils.gotoMyGroupsActivity(this, null, false);
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = et_search.getText().toString();
                membersAdapter.setData(filter(str));

            }
        });
    }

    private void onSubmit() {
        if (isShowSelected) {
            Utils.hideSoftInput(this, et_search);
            JSONArray jsonArray = new JSONArray();
            List<Friend> friends = membersAdapter.getmSelectedFriend();
            for (Friend tmp : friends) {
                if (jsonArray.toString().contains(tmp.uid)) {
                    continue;
                }
                jsonArray.put(tmp.uid);
            }
            onGroupSetManger(jointNickName(friends), jsonArray.toString());
        } else {//转让群主
            transferMaster();
        }
    }

    private CommDialog onGroupSetManger;

    private void onGroupSetMangerDismiss() {
        if (onGroupSetManger != null) {
            onGroupSetManger.dismiss();
            onGroupSetManger = null;
        }
    }

    private void onGroupSetManger(String nickName, String jsonArray) {
        onGroupSetMangerDismiss();
        onGroupSetManger = new CommDialog(this);
        onGroupSetManger.setTitleVisible(false);
        onGroupSetManger.setTitle(getString(R.string.group_group_confirm_mark));
        onGroupSetManger.setDescSpannableString(SpannableStringUtils.addTextColor(getString(R.string.group_set_managers,
                nickName), 3, nickName.length() + 3, getResources().getColor(R.color.color_ff0000)));
        onGroupSetManger.setOkTxt(getString(R.string.comm_ok));
        onGroupSetManger.setOkTxtColor(Color.RED);
        onGroupSetManger.setCancelTxt(getString(R.string.comm_cancel));
        onGroupSetManger.show();

        onGroupSetManger.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                mPresenter.setGroupManagers(groupId, 1, jsonArray);
            }
        });

    }

    //转让群主
    private void transferMaster() {
        if (transfer_friend == null) return;
        Friend friend = FriendDbHelper.getInstance().getFriend(transfer_friend.uid);
        String nickName;
        if (friend != null && !TextUtils.isEmpty(friend.getRemarks())) {
            nickName = friend.getRemarks();
        } else {
            if (TextUtils.isEmpty(transfer_friend.getRemarks())) {
                nickName = transfer_friend.getName();
            } else {
                nickName = transfer_friend.getRemarks();
            }
        }

        onGroupSetManger();
        mRemoveFriendDialog = new CommDialog(this);
        mRemoveFriendDialog.setTitleVisible(true);
        mRemoveFriendDialog.setTitle(getString(R.string.group_group_confirm_mark));
        mRemoveFriendDialog.setDescSpannableString(SpannableStringUtils
                .addTextColor(getString(R.string.group_master_transfer, nickName),
                        4, nickName.length() + 4, getResources().getColor(R.color.color_ff0000)));
        mRemoveFriendDialog.setOkTxt(getString(R.string.comm_ok));
        mRemoveFriendDialog.setOkTxtColor(Color.RED);
        mRemoveFriendDialog.setCancelTxt(getString(R.string.comm_cancel));
        mRemoveFriendDialog.show();

        mRemoveFriendDialog.setDialogListener(new DialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                mPresenter.transferGroupMaster(groupId, transfer_friend.uid);
            }
        });
    }

    private CommDialog mRemoveFriendDialog;

    private void onGroupSetManger() {
        if (mRemoveFriendDialog != null) {
            mRemoveFriendDialog.dismiss();
            mRemoveFriendDialog = null;
        }
    }

    //设置管理员拼接昵称
    private String jointNickName(List<Friend> lists) {
        if (lists == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lists.size(); i++) {
            Friend tmp = lists.get(i);
            Friend friend = FriendDbHelper.getInstance().getFriend(tmp.uid);
            if (friend != null && !TextUtils.isEmpty(friend.getRemarks())) {//判断是否有备注
                sb.append(friend.getRemarks());
            } else {
                sb.append(TextUtils.isEmpty(tmp.getRemarks()) ? tmp.getName() : tmp.getRemarks());
            }
            if(i < (lists.size() - 1)){
                sb.append("、");
            }
        }
        return sb.toString();
    }

    private void fillSourceDataList(List<Friend> data_list) {
        if (data_list != null && data_list.size() > 0) {
            sourceDataList = filledData(data_list); //过滤数据为有字母的字段  现在有字母 别的数据没有
        }

        //还原除了带字母字段的其他数据
        assert data_list != null;
        for (int i = 0; i < data_list.size(); i++) {
            sourceDataList.get(i).nickname = data_list.get(i).nickname;
            sourceDataList.get(i).uid = data_list.get(i).uid;
            sourceDataList.get(i).headpic = data_list.get(i).headpic;
            sourceDataList.get(i).setRemarks(data_list.get(i).getRemarks());
            sourceDataList.get(i).phone = data_list.get(i).phone;
            sourceDataList.get(i).setSelected(data_list.get(i).isSelected());

            checkList.add(false);//赋值
        }
        // 根据a-z进行排序源数据
        Collections.sort(sourceDataList, pinyinComparator);
        updateAdapter();
    }

    private void updateAdapter() {
        if (sourceDataList == null) return;
        if (membersAdapter == null) {
            membersAdapter = new GroupMembersAdapter(GroupMembersActivity.this, isShowSelected, sourceDataList);
            mListView.setAdapter(membersAdapter);
            membersAdapter.setOnClickListener(new GroupMembersAdapter.OnClickListener() {
                @Override
                public void checkPosition(int position) {
                    for (int i = 0; i < checkList.size(); i++) {
                        if (position == i) {// 设置已选位置
                            checkList.set(i, true);
                            transfer_friend = (Friend) membersAdapter.getItem(i);
                        } else {
                            checkList.set(i, false);
                        }
                    }
                    setClickable(true);
                    membersAdapter.notifyDataSetChanged();
                }

                @Override
                public void addGroupMember(Friend friend, boolean isCheck) {
                    setClickable(membersAdapter.getmSelectedFriend().size() > 0);
                    et_search.setText("");
                }
            });
        } else {
            membersAdapter.setData(sourceDataList);
        }
    }

    private void setClickable(boolean clickable) {
        getCommTitle().getRightTv().setTextColor(getResources().getColor(clickable ? R.color.white : R.color.color_8C8C8C));
        getCommTitle().getRightTv().setClickable(clickable);
    }

    /**
     * 为ListView填充数据
     */
    private List<Friend> filledData(List<Friend> list) {
        List<Friend> mFriendList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Friend friendModel = new Friend(list.get(i).uid, list.get(i).getName(),
                    list.get(i).headpic, list.get(i).getRemarks(), list.get(i).phone, list.get(i).isSelected());
            //汉字转换成拼音
            String pinyin = null;
            CharacterParser mCharacterParser = new CharacterParser();
            if (!TextUtils.isEmpty(list.get(i).getRemarks())) {
                pinyin = mCharacterParser.getSpelling(list.get(i).getRemarks());
            } else if (!TextUtils.isEmpty(list.get(i).getName())) {
                pinyin = mCharacterParser.getSpelling(list.get(i).getName());
            } else {
//                UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(list.get(i).getUserId());
//                if (userInfo != null) {
//                    pinyin = mCharacterParser.getSpelling(userInfo.getName());
//                }
            }
            if (mCharacterParser.isHaveInterpunction) {
                pinyin = "00000";
            }
            String sortString;
            if (!TextUtils.isEmpty(pinyin)) {
                sortString = pinyin.substring(0, 1).toUpperCase();
            } else {
                sortString = "#";
            }
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                friendModel.letters = sortString;
            } else {
                friendModel.letters = "#";
            }

            mFriendList.add(friendModel);
        }
        return mFriendList;

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

        if (DqUrl.url_select_group_user.equals(url)) {
            List<Friend> groupMemberList = (List<Friend>) entity.data;
            if(groupMemberList == null || groupMemberList.size() <= 0) return;
            if (isShowSelected) {//显示已选中的状态
                for (int i = 0; i < groupMemberList.size(); i++) {
                    Friend friend = groupMemberList.get(i);
                    if (friend.isHighRole()) {
                        groupMemberList.get(i).setSelected(true);
                        if (friend.isAdmin()) {
                            managerNum++;
                        }
                    } else {
                        groupMemberList.get(i).setSelected(false);
                    }
                }
            } else {//去除当前用户
                Friend temp = null;
                for (Friend tmp : groupMemberList) {
                    if (ModuleMgr.getCenterMgr().getUID().equals(tmp.uid)) {
                        temp = tmp;
                        break;
                    }
                }
                if (temp != null) {
                    groupMemberList.remove(temp);
                }
            }
            fillSourceDataList(groupMemberList);
            //设置群管理员
        } else if (DqUrl.url_group_set_managers.equals(url)) {
            DqToast.showShort("设置成功");
            MsgMgr.getInstance().sendMsg(MsgType.MT_GROUP_SETTING_ADMIN_CHANGE, membersAdapter.getmSelectedFriend());
            Intent intent = new Intent();
            intent.putExtra("members", (Serializable) membersAdapter.getmSelectedFriend());
            setResult(RESULT_OK, intent);
            finish();
        } else if (DqUrl.url_group_transfer_master.equals(url)) {
            MsgMgr.getInstance().sendMsg(MsgType.MT_GROUP_SETTING_NEW_LORD_CHANGE, null);
            DqToast.showShort("设置成功");
            setResult(RESULT_OK);
            finish();
        }
    }

    //搜索
    private List<Friend> filter(String str) {
        List<Friend> lists = new ArrayList<>();
        for (int i = 0; i < sourceDataList.size(); i++) {
            Friend mFriend = sourceDataList.get(i);
            if (mFriend.getName().contains(str) || mFriend.phone.contains(str) || mFriend.getRemarks().contains(str)) {
                lists.add(mFriend);
            }
        }
        return lists;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.hideSoftInput(this, et_search);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mListView = null;
        onGroupSetMangerDismiss();
    }
}