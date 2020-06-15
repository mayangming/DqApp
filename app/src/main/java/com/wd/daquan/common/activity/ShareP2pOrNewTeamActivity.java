package com.wd.daquan.common.activity;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.da.library.listener.DialogListener;
import com.da.library.listener.ISelectListener;
import com.da.library.widget.SideBar;
import com.dq.im.type.ImType;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.wd.daquan.R;
import com.wd.daquan.common.adapter.ShareP2pAdapter;
import com.wd.daquan.common.bean.ShareItemBean;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.contacts.helper.TransformFriendInfoHelper;
import com.wd.daquan.model.bean.CreateTeamEntity;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.utils.GsonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: 方志
 * @Time: 2018/9/25 10:40
 * @Description: 分享单聊会话或创建新的群组页面
 */
public class ShareP2pOrNewTeamActivity extends ShareBaseActivity implements ISelectListener<Friend>,SideBar.OnTouchingLetterChangedListener {

    private TextView mChoiceTeamTv;
    private SideBar mSideBar;
    private TextView mLetterTv;
    private ShareP2pAdapter mAdapter;
//    private int mPage = 1;
    private List<Friend> mSelectList = new ArrayList<>();
    private List<Friend> mFriendList = new ArrayList<>();

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_share_p2p);
    }

    @Override
    protected void initView() {
        super.initView();
        mChoiceTeamTv = findViewById(R.id.share_choice_team_tv);
        mSideBar = findViewById(R.id.share_p2p_side_bar);
        mLetterTv = findViewById(R.id.share_p2p_letter_tv);

    }

    @Override
    protected void initData() {
        super.initData();
        initTitle();
        initAdapter();

        FriendDbHelper.getInstance().getAllFriend(friends -> {
            if(friends != null && friends.size() > 0) {
                updateData(friends);
            }else {
                requestData();
            }
        });
    }

    private void requestData() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.Contact.PAGE, "1");
        hashMap.put(IConstant.Contact.LENGTH, "3000");
        hashMap.put(IConstant.Contact.WHETHER_HELPER, "1");//否返回斗圈小助手
        hashMap.put(IConstant.Contact.SHOW_SELF, "1");//否返回自己
        hashMap.put(IConstant.Contact.LAST_TIME, "0");
        mPresenter.getContacts(DqUrl.url_friend_list, hashMap);
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShareP2pAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setMode(IConstant.Select.MORE);

    }

    private void initTitle() {
        setTitleName(getString(R.string.choice_contact));
        mTitleLayout.getRightTv().setText(getString(R.string.confirm));
        mTitleLayout.getRightTv().setClickable(false);
        mTitleLayout.getRightTv().setVisibility(View.VISIBLE);
        mTitleLayout.getRightTv().setTextColor(getResources().getColor(R.color.text_blue_pressed));
        mRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTitleLayout.getRightTv().setOnClickListener(this);
        mChoiceTeamTv.setOnClickListener(this);
        mAdapter.setSelectListener(this);
        mSideBar.setOnTouchingLetterChangedListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSelectList(@NonNull List<Friend> list) {
        if (list.size() > 0) {
            mTitleLayout.getRightTv().setClickable(true);
            mTitleLayout.getRightTv().setTextColor(getResources().getColor(R.color.text_blue));
            mTitleLayout.getRightTv().setText(getString(R.string.confirm) + "(" + list.size() + ")");
        } else {
            mTitleLayout.getRightTv().setClickable(false);
            mTitleLayout.getRightTv().setTextColor(getResources().getColor(R.color.text_blue_pressed));
            mTitleLayout.getRightTv().setText(getString(R.string.confirm));
        }

        mSelectList.clear();
        mSelectList.addAll(list);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId() == mTitleLayout.getRightTvId()) {
            if(mSelectList.size() == 1) {
                //选择一个人，直接分享到个人会话
                singleShare();
            }else if(mSelectList.size() > 1){
                //多人，创建一个群组，分享到群组
                CreateTeam();
            }
        }else if(v.getId() == R.id.share_choice_team_tv) {
            NavUtils.gotoShareTeamActivity(this, mShareBean);
        }
    }

    private void CreateTeam() {
        String groupUids = TransformFriendInfoHelper.getInstance().jointId(mSelectList, true);
        String groupNames = TransformFriendInfoHelper.getInstance().jointNames(mSelectList, true);
        String groupPics = TransformFriendInfoHelper.getInstance().joinUserHead(mSelectList);

        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put(KeyValue.Group.GROUP_UID, groupUids); // 成员ID
        hashMap.put(KeyValue.Group.GROUP_NAME, groupNames); // 成员昵称
        hashMap.put(KeyValue.Group.GROUP_PIC, groupPics); // 群成员头像
        mPresenter.createTeam(DqUrl.url_create_group, hashMap);
    }


    private void singleShare() {
        Friend friend = mSelectList.get(0);
        ShareItemBean shareItemBean = new ShareItemBean();
        shareItemBean.sessionId = friend.uid;
        shareItemBean.sessionName = friend.nickname;
        shareItemBean.sessionType = ImType.P2P;
        shareItemBean.sessionPortrait = friend.headpic;
        shareMoreMessage(shareItemBean);
    }


    @Override
    public void requestSuccess(String url, int code, DataBean entity) {
        if(DqUrl.url_friend_list.equals(url)) {
            String data = GsonUtils.toJson(entity.data);
            List<Friend> friendList = GsonUtils.fromJsonList(data, Friend.class);
            if(null != friendList && friendList.size() > 0) {
                updateData(friendList);
            }
        }else if(DqUrl.url_create_group.equals(url)) {
            DqToast.showShort("群组创建成功");
            CreateTeamEntity teamEntity = (CreateTeamEntity) entity.data;
            if (teamEntity.hasRefuse()) {
                DialogUtils.showRefuesInviteDialog(this, teamEntity.getRefuseName(),
                        teamEntity.getRefuseCount()+"", new DialogListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onOk() {
                        shareToTeam(teamEntity);
                    }
                });
            } else {
                shareToTeam(teamEntity);
            }
        }else {
            DqToast.showShort(entity.content);
        }
    }

    private void shareToTeam(CreateTeamEntity teamEntity) {
        ShareItemBean shareItemBean = new ShareItemBean();
        shareItemBean.sessionId = teamEntity.group_id;
        shareItemBean.sessionName = teamEntity.group_name;
        shareItemBean.sessionType = ImType.Team;
        shareItemBean.sessionPortrait = teamEntity.group_pic;
        shareMoreMessage(shareItemBean);

    }

    private void updateData(List<Friend> friendList) {


        //添加拼音
        List<Friend> list = TransformFriendInfoHelper.getInstance().setPinYin(friendList);
        if(null != list) {
            mFriendList.clear();
            mFriendList.addAll(list);
        }

        if(mFriendList.size() <= 0) {
            mNoDataTv.setVisibility(View.VISIBLE);
        }else {
            mNoDataTv.setVisibility(View.GONE);
        }
        //按拼音排序
        Collections.sort(mFriendList, (o1, o2) -> {
            if (o1.pinYin.equals("@") || o2.pinYin.equals("#")) {
                return -1;
            } else if (o1.pinYin.equals("#") || o2.pinYin.equals("@")) {
                return 1;
            } else {
                return o1.pinYin.compareTo(o2.pinYin);
            }
        });

        mAdapter.update(mFriendList);
    }

    @Override
    public void requestFailed(String url, int code, DataBean entity) {
        DqUtils.bequit(entity, this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mAdapter.clear();
        requestData();
    }

    @Override
    public void onTouchingLetterChanged(String letter) {
        mSideBar.setTextView(mLetterTv);
        //该字母首次出现的位置
        int position = mAdapter.getPositionForSection(letter.charAt(0));
        if(position != -1) {
            mRecyclerView.scrollToPosition(position);
            LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            mLayoutManager.scrollToPositionWithOffset(position, 0);
        }
    }
}
