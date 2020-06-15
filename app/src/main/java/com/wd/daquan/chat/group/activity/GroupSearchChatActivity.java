package com.wd.daquan.chat.group.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.da.library.listener.ITextChangedListener;
import com.da.library.tools.AESHelper;
import com.da.library.widget.CommSearchEditText;
import com.dq.im.model.ImMessageBaseModel;
import com.wd.daquan.R;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.chat.group.adapter.GroupSearchChatAdapter;
import com.wd.daquan.chat.group.bean.GroupSearchChatType;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.SpannableStringUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.model.db.helper.MemberDbHelper;
import com.wd.daquan.model.db.helper.TeamDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.third.session.SessionHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索界面
 * Created by Kind on 2018/9/25.
 */

public class GroupSearchChatActivity extends DqBaseActivity<ChatPresenter, DataBean> implements ITextChangedListener {

    private ListView mListView;
    // 转为群组类型提供
    private List<GroupMemberBean> members = null;
    private GroupSearchChatAdapter adapter;
    private TextView txt_trade;
    private String groupId;
    private List<ImMessageBaseModel> searchResultList = new ArrayList<>();
    private RelativeLayout groupSearchChatQuickLayout;
    private TextView groupSearchChatNoContent;

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.group_search_chat_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mListView = findViewById(R.id.groupSearchChatlistview);
        groupSearchChatQuickLayout = findViewById(R.id.groupSearchChatQuickLayout);
        groupSearchChatNoContent = findViewById(R.id.groupSearchChatNoContent);
        CommSearchEditText mSearchEditText = findViewById(R.id.comm_search_edittext_rlyt);
        txt_trade = findViewById(R.id.groupSearchChatTrade);
        mSearchEditText.setChangedListener(this);
        adapter = new GroupSearchChatAdapter(this);
        mListView.setAdapter(adapter);

    }

    @Override
    protected void initData() {
        groupId = getIntent().getStringExtra(KeyValue.GROUP_ID);
        reset();
    }

    @Override
    protected void initListener() {
        txt_trade.setOnClickListener(v -> {
            GroupSearchChatType chatType = new GroupSearchChatType(groupId, GroupSearchChatType.SearchChatType.Trade);
            NavUtils.gotoGroupSearchTradeActivity(GroupSearchChatActivity.this, chatType);
        });

        adapter.setOnConversationsItemClickListener(uiMessage -> {

            String content = uiMessage.getSourceContent();
            Log.w("xxxx", "content = " + AESHelper.decryptString(content));
            //查询自己在群组的信息
            SessionHelper.startTeamSession(getActivity(), groupId, uiMessage);
        });
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (url.equals(DqUrl.url_friend_list)) {//好友列表接口
            if (!entity.isSuccess()) {
                DqToast.showShort(entity.getContent());
            }
        }
    }

    private ArrayList<String> filterAccounts(String query) {
        ArrayList<String> filter = new ArrayList<>();
        if (members == null) {
            members = MemberDbHelper.getInstance().getAll(groupId);
        }

        if (members != null) {
            for (GroupMemberBean tmp : members) {
                if (tmp == null) {
                    continue;
                }
                String account = tmp.uid;
                if (match(tmp.getNickname(), query)) {
                    filter.add(account);
                    continue;
                }
                GroupInfoBean groupInfo = TeamDbHelper.getInstance().getTeam(account);
                String name = account;
                if (groupInfo != null) {
                    name = groupInfo.group_name;
                }
                if (match(name, query)) {
                    filter.add(account);
                }
            }
        }
        return filter;
    }

    private boolean match(String source, String query) {
        if (TextUtils.isEmpty(source)) {
            return false;
        }

        return source.toLowerCase().contains(query);
    }

    private void updateUI(List<ImMessageBaseModel> lists, String content) {
        if (lists.size() > 0) {
            groupSearchChatQuickLayout.setVisibility(View.GONE);
            groupSearchChatNoContent.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            adapter.setData(content);
            adapter.replace(lists);

        } else {
            groupSearchChatQuickLayout.setVisibility(View.GONE);
            groupSearchChatNoContent.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            String contentMark = "“" + content + "”";
            String allContent = getResources().getString(R.string.group_search_chat_no_content, contentMark);
            groupSearchChatNoContent.setText(SpannableStringUtils.addTextColor(allContent, 3,
                    contentMark.length() + 3, getResources().getColor(R.color.text_blue)));
        }
    }

    @Override
    public void textChanged(String content) {
        if (TextUtils.isEmpty(content)) {
            groupSearchChatQuickLayout.setVisibility(View.VISIBLE);
            groupSearchChatNoContent.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
            return;
        }
        Toast.makeText(this,"云信搜索功能暂时不可用",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeChange() {

    }


    private void reset() {
        if (members == null) {
            members = MemberDbHelper.getInstance().getAll(groupId);
        }
        searchResultList.clear();
    }
}