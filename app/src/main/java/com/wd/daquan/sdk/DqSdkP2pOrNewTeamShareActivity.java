package com.wd.daquan.sdk;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.da.library.listener.DialogListener;
import com.da.library.listener.ISelectListener;
import com.da.library.widget.CommTitle;
import com.da.library.widget.SideBar;
import com.dq.im.type.ImType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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
 * Created by DELL on 2018/11/19.
 */

public class DqSdkP2pOrNewTeamShareActivity extends BaseDqSdkShareActivity implements View.OnClickListener,
        OnRefreshListener {
    private TextView mChoiceTeamTv;
    private SideBar mSideBar;
    private TextView mLetterTv;
    private ShareP2pAdapter mAdapter;
    //    private int mPage = 1;
    private List<Friend> mSelectList = new ArrayList<>();
    private List<Friend> mFriendList = new ArrayList<>();
    private CommTitle mCommTitle;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mNoDataTv;

    @Override
    protected void setContentView() {
        setContentView(R.layout.create_new_chat_activity);
    }

    @Override
    protected void initView() {
        mCommTitle = findViewById(R.id.share_title_layout);
        mChoiceTeamTv = findViewById(R.id.share_choice_team_tv);
        mSideBar = findViewById(R.id.share_p2p_side_bar);
        mLetterTv = findViewById(R.id.share_p2p_letter_tv);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.share_recycler_view);
        mNoDataTv = findViewById(R.id.no_data_tv);
    }

    @Override
    protected void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        mCommTitle.getRightTv().setOnClickListener(this);
        mChoiceTeamTv.setOnClickListener(this);
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                mSideBar.setTextView(mLetterTv);
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if(position != -1) {
                    mRecyclerView.scrollToPosition(position);
                    LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(position, 0);
                }
            }
        });
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShareP2pAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setMode(IConstant.Select.MORE);
        mAdapter.setSelectListener(new ISelectListener<Friend>() {
            @Override
            public void onSelectList(@NonNull List<Friend> list) {
                changeConfirmStatus(list);
            }
        });
    }
    //确认按钮的状态
    private void changeConfirmStatus(List<Friend> list){
        if (list.size() > 0) {
            mCommTitle.getRightTv().setClickable(true);
            mCommTitle.getRightTv().setTextColor(getResources().getColor(R.color.text_blue));
            mCommTitle.getRightTv().setText(getString(R.string.confirm) + "(" + list.size() + ")");
        } else {
            mCommTitle.getRightTv().setClickable(false);
            mCommTitle.getRightTv().setTextColor(getResources().getColor(R.color.text_blue_pressed));
            mCommTitle.getRightTv().setText(getString(R.string.confirm));
        }
        mSelectList.clear();
        mSelectList.addAll(list);
    }

    @Override
    protected void initData() {
        super.initData();
        mCommTitle.setTitle(getString(R.string.choice_contact));
        mCommTitle.getRightTv().setText(getString(R.string.confirm));
        mCommTitle.getRightTv().setClickable(false);
        mCommTitle.getRightTv().setVisibility(View.VISIBLE);
        mCommTitle.getRightTv().setTextColor(getResources().getColor(R.color.text_blue_pressed));

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
        hashMap.put(IConstant.Contact.WHETHER_HELPER, "1");
        hashMap.put(IConstant.Contact.SHOW_SELF, "1");
        hashMap.put(IConstant.Contact.LAST_TIME, "0");
        mPresenter.getContacts(DqUrl.url_friend_list, hashMap);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mCommTitle.getRightTvId()) {
            if(mSelectList.size() == 1) {
                //选择一个人，直接分享到个人会话
                singleShare();
            }else if(mSelectList.size() > 1){
                //多人，创建一个群组，分享到群组
                CreateTeam();
            }
        }else if(v.getId() == R.id.share_choice_team_tv) {
            NavUtils.gotoQCOpenShareTeamActivity(this, mShareBean);
        }else if(v.getId() == mCommTitle.getLeftIvId()) {
            finish();
        }
    }

    private void singleShare() {
        Friend friend = mSelectList.get(0);
        ShareItemBean shareItemBean = new ShareItemBean();
        shareItemBean.sessionId = friend.uid;
        shareItemBean.sessionName = friend.nickname;
        shareItemBean.sessionType = ImType.P2P;
        shareItemBean.sessionPortrait = friend.headpic;
        mSdkHelper.showDialog(this, shareItemBean, mShareBean);
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

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if(DqUrl.url_friend_list.equals(url)) {
            String data = GsonUtils.toJson(entity.data);
            List<Friend> friendList = GsonUtils.fromJsonList(data, Friend.class);
            if(null != friendList && friendList.size() > 0) {
                updateData(friendList);
            }
        }else if(DqUrl.url_create_group.equals(url)) {
            DqToast.showShort("群组创建成功");
            CreateTeamEntity teamEntity = (CreateTeamEntity) entity.data;
            if (teamEntity == null) {
                return;
            }
            if (teamEntity.hasRefuse()) {
                DialogUtils.showRefuesInviteDialog(this, teamEntity.getRefuseName(), teamEntity.getRefuseCount()+"", new DialogListener() {
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

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        DqUtils.bequit(entity, this);
    }
    //创建完群组之后再去分享
    private void shareToTeam(CreateTeamEntity teamEntity) {
        ShareItemBean shareItemBean = new ShareItemBean();
        shareItemBean.sessionId = teamEntity.group_id;
        shareItemBean.sessionName = teamEntity.group_name;
        shareItemBean.sessionType = ImType.Team;
        shareItemBean.sessionPortrait = teamEntity.group_pic;
        mSdkHelper.showDialog(this, shareItemBean, mShareBean);
    }

    //更新列表数据
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
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mAdapter.clear();
        requestData();
    }

}
