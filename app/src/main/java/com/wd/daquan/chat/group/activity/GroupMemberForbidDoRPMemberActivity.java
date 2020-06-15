package com.wd.daquan.chat.group.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.chat.ChatPresenter;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.sp.QCSharedPreTeamInfo;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.common.utils.CNLog;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.PinyinComparator;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.contacts.utils.CharacterParser;
import com.wd.daquan.glide.GlideUtils;
import com.da.library.listener.ITextChangedListener;
import com.da.library.widget.CommSearchEditText;
import com.da.library.widget.CommTitle;
import com.da.library.widget.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 群成员列表(禁止收发红包)
 */
public class GroupMemberForbidDoRPMemberActivity extends DqBaseActivity<ChatPresenter, DataBean> implements View.OnClickListener {

    public Map<Integer, Boolean> mCBFlag;
    private List<Friend> mSelectedFriend;
    private List<Friend> sourceDataList = new ArrayList<>();
    private String groupId;

    public ListView mDispatcherMemberLv;
    public TextView mDispatcherGroupDialog;

    private PinyinComparator pinyinComparator;
    private DispatcherMembersListAdapter membersListAdapter;
    private String groupOwner;
    private CommTitle mCommTitle;
    private EditText mSearchMemberEt;
    private CommSearchEditText mSearchLayout;

    @Override
    protected ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.group_member_forbid_do_rp_selectlist_activity);
    }

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mSelectedFriend = new ArrayList<>();
        mCommTitle = findViewById(R.id.forbidDoRPSelectListActivityTitle);
//        mSearchMemberEt = findViewById(R.id.search_member_et);
        mSearchLayout = findViewById(R.id.comm_search_view_layout);
        mSearchMemberEt = mSearchLayout.getEditText();
        mDispatcherMemberLv = findViewById(R.id.dispatcher_member_lv);
        mDispatcherGroupDialog = findViewById(R.id.dispatcher_group_dialog);
        SideBar mSideBar = findViewById(R.id.dispatcher_side_bar);
        mSideBar.setTextView(mDispatcherGroupDialog);
        pinyinComparator = PinyinComparator.getInstance();
        mSideBar.setOnTouchingLetterChangedListener(s -> {
            if (membersListAdapter == null) {
                return;
            }
            //该字母首次出现的位置
            int position = membersListAdapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                mDispatcherMemberLv.setSelection(position);
            }
        });
        mCommTitle.setTitle(getString(R.string.dispatcher_red));
        mCommTitle.getRightTv().setText(getString(R.string.confirm));
        mCommTitle.getRightTv().setTextColor(getResources().getColor(R.color.color_8c8c8c));
        mCommTitle.getRightTv().setEnabled(false);
    }

    @Override
    public void initListener() {
        //搜索
        mSearchLayout.setChangedListener(new ITextChangedListener() {
            @Override
            public void textChanged(String content) {
                if (membersListAdapter == null) {
                    return;
                }
                String etStr = mSearchMemberEt.getText().toString().trim();
                membersListAdapter.setData(filter(etStr));
            }

            @Override
            public void beforeChange() {

            }
        });

        mCommTitle.getRightTv().setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
    }

    @Override
    public void initData() {
        groupId = getIntent().getStringExtra(KeyValue.GROUP_ID);
        QCSharedPrefManager manager = QCSharedPrefManager.getInstance();
        QCSharedPreTeamInfo groupInfoSP = manager.getKDPreferenceTeamInfo();
        if(null != groupId) {
            groupOwner = groupInfoSP.getString(QCSharedPreTeamInfo.GROUP_OWNER + groupId, "");
            CNLog.e("fz", "groupOwner ： " + groupOwner);
            LinkedHashMap<String,String> map = new LinkedHashMap<>();
            map.put("group_id", groupId);
            mPresenter.getFrobidRedPacketSelectMember(DqUrl.url_forbidRedpacket_selectMember, map);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.comm_right_tv://确定添加
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < mSelectedFriend.size(); i++) {
                    String userId = mSelectedFriend.get(i).uid + ",";
                    stringBuilder.append(userId);
                }
                String group_uid = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
                DialogUtils.showPurseDialog(this, 7, jointNickName(mSelectedFriend),
                        id -> {
                            if (R.id.tv_confirm == id) {
                                LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();
                                linkedHashMap.put("action", "1");
                                linkedHashMap.put("group_id", groupId);
                                linkedHashMap.put("group_uid", group_uid);
                                mPresenter.gotoForbidRedPacketJoinAndRemove(DqUrl.url_forbidRedpacket_joinAndRemove, linkedHashMap);
                            }
                        }).show();
                break;
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        super.onFailed(url, code, entity);
        DqUtils.bequit(entity, this);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        //获取群成员列表，[仅限禁止收发红包功能使用]
        if (DqUrl.url_forbidRedpacket_selectMember.equals(url)) {
            if (0 == code) {
                List<Friend> groupMemberList = (List<Friend>)entity.data;
                fillSourceDataList(groupMemberList);
            }
        } else if (DqUrl.url_forbidRedpacket_joinAndRemove.equals(url)) {//加入群员到禁止收发红包名单
            if (0 == code) {
                DqToast.showShort(entity.content);
                setResult(RESULT_OK);
                finish();
            }
        }
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
            sourceDataList.get(i).is_join = data_list.get(i).is_join;
        }
        // 根据a-z进行排序源数据
        Collections.sort(sourceDataList, pinyinComparator);

        updateAdapter();
    }

    private void updateAdapter() {
        if (sourceDataList == null) return;
        if (membersListAdapter == null) {
            membersListAdapter = new DispatcherMembersListAdapter(this, sourceDataList);
            mDispatcherMemberLv.setAdapter(membersListAdapter);
        } else {
            membersListAdapter.setData(sourceDataList);
        }
    }

    //群成员列表
    class DispatcherMembersListAdapter extends BaseAdapter implements SectionIndexer {
        private Context context;
        private List<Friend> list;

        @SuppressLint("UseSparseArrays")
        private DispatcherMembersListAdapter(Context context, List<Friend> list) {
            this.context = context;
            mCBFlag = new HashMap<>();
            this.list = list;
            init();
        }

        public void setData(List<Friend> friends) {
            this.list = friends;
            notifyDataSetChanged();
        }

        private void init() {//存到map标记选中的成员
            for (int i = 0; i < list.size(); i++) {
                Friend friend = list.get(i);
                mCBFlag.put(Integer.parseInt(friend.uid), false);
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            final Friend friend = list.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_start_discussion, parent, false);
                viewHolder.tvLayout = convertView.findViewById(R.id.dis_frienditem);
                viewHolder.tvTitle = convertView.findViewById(R.id.dis_friendname);
                viewHolder.tvLetter = convertView.findViewById(R.id.dis_catalog);
                viewHolder.mImageView = convertView.findViewById(R.id.dis_frienduri);
                viewHolder.isSelect = convertView.findViewById(R.id.dis_select);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);
            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                viewHolder.tvLetter.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(friend.letters);
            } else {
                viewHolder.tvLetter.setVisibility(View.GONE);
            }
            if ("1".equals(friend.is_join)) {
                mCBFlag.put(Integer.parseInt(friend.uid), true);
                //viewHolder.tvLayout.setBackgroundColor(getResources().getColor(R.color.colormain_8C8C8C));
            }
//            else {
//                //viewHolder.tvLayout.setBackgroundColor(getResources().getColor(R.color.white));
//            }

            if(groupOwner.equals(friend.uid)) {
                viewHolder.tvLayout.setEnabled(false);
                viewHolder.isSelect.setSelected(true);
            }else {
                viewHolder.tvLayout.setEnabled(true);
                viewHolder.isSelect.setSelected(false);
            }

            viewHolder.isSelect.setEnabled(false);
            viewHolder.tvLayout.setOnClickListener(v -> {
                if (mCBFlag.get(Integer.parseInt(friend.uid))) {
                    viewHolder.isSelect.setChecked(false);
                    mCBFlag.put(Integer.parseInt(friend.uid), false);
                } else {
                    viewHolder.isSelect.setChecked(true);
                    mCBFlag.put(Integer.parseInt(friend.uid), true);
                }
                addGroupMember(friend, viewHolder.isSelect.isChecked());

            });

            Boolean flag = mCBFlag.get(Integer.parseInt(friend.uid));
            if (flag != null) {
                viewHolder.isSelect.setChecked(flag);
            }
            Friend friend1 = FriendDbHelper.getInstance().getFriend(list.get(position).uid);
            if (friend1 != null && !TextUtils.isEmpty(friend1.getRemarks())) {
                viewHolder.tvTitle.setText(friend1.getRemarks());
            } else {
                if (TextUtils.isEmpty(list.get(position).getRemarks())) {
                    viewHolder.tvTitle.setText(list.get(position).getName());
                } else {
                    viewHolder.tvTitle.setText(list.get(position).getRemarks());
                }
            }
            GlideUtils.load(DqApp.sContext, friend1.headpic, viewHolder.mImageView);
            return convertView;
        }

        private void addGroupMember(Friend friend, boolean isCheck) {
            if (isCheck) {
                mSelectedFriend.add(friend);
            } else {
                for (int i = 0; i < mSelectedFriend.size(); i++) {
                    if (friend.uid.equals(mSelectedFriend.get(i).uid)) {
                        mSelectedFriend.remove(i);
                    }
                }
            }
            if (mSelectedFriend.size() > 0) {
                mCommTitle.getRightTv().setTextColor(getResources().getColor(R.color.white));
                mCommTitle.getRightTv().setEnabled(true);
            } else {
                mCommTitle.getRightTv().setTextColor(getResources().getColor(R.color.color_8c8c8c));
                mCommTitle.getRightTv().setEnabled(false);
            }
            mSearchMemberEt.setText("");
        }

        @Override
        public Object[] getSections() {
            return new Object[0];
        }

        /**
         * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
         */
        @Override
        public int getPositionForSection(int sectionIndex) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = list.get(i).letters;
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == sectionIndex) {
                    return i;
                }
            }

            return -1;
        }

        /**
         * 根据ListView的当前位置获取分类的首字母的Char ascii值
         */
        @Override
        public int getSectionForPosition(int position) {
            return list.get(position).letters.charAt(0);
        }


        final class ViewHolder {
            /**
             * 布局
             */
            LinearLayout tvLayout;
            /**
             * 首字母
             */
            TextView tvLetter;
            /**
             * 昵称
             */
            TextView tvTitle;
            /**
             * 头像
             */
            ImageView mImageView;
            /**
             * 是否被选中的checkbox
             */
            CheckBox isSelect;
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDispatcherMemberLv = null;
        membersListAdapter = null;
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
    //拼接昵称
    public static String jointNickName(List<Friend> lists) {
        if(lists == null)return null;
        StringBuilder sb = new StringBuilder();
        for (Friend fri : lists) {
            Friend friend = FriendDbHelper.getInstance().getFriend(fri.uid);
            sb.append(friend.getName()).append("、");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);

    }
}
