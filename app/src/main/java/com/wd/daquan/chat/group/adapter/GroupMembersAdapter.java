package com.wd.daquan.chat.group.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.wd.daquan.R;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.chat.group.activity.GroupMembersActivity;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.log.DqToast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kind on 2018/9/28.
 */

public class GroupMembersAdapter extends BaseAdapter implements SectionIndexer {

    private GroupMembersActivity membersActivity;
    private boolean isShowSelected;//初始化的时候是否显示选中状态
    private List<Friend> list;
    private Map<String, Boolean> mCBFlag;
    private List<Friend> mSelectedFriend = new ArrayList<>();

    @SuppressLint("UseSparseArrays")
    public GroupMembersAdapter(GroupMembersActivity membersActivity, boolean isShowSelected, List<Friend> list) {
        this.membersActivity = membersActivity;
        this.isShowSelected = isShowSelected;
        mCBFlag = new HashMap<>();
        this.list = list;
        init();
    }

    public void setData(List<Friend> friends) {
        this.list = friends;
        notifyDataSetChanged();
    }

    public List<Friend> getmSelectedFriend() {
        return mSelectedFriend;
    }

    private void init() {//存到map标记选中的成员
        for (int i = 0; i < list.size(); i++) {
            mCBFlag.put(list.get(i).uid, false);
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

    @SuppressLint({"ViewHolder", "ClickableViewAccessibility"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Friend friend = list.get(position);
        ViewHolder viewHolder = new ViewHolder();
        convertView = LayoutInflater.from(membersActivity).inflate(R.layout.item_start_discussion, parent, false);
        viewHolder.tvLayout = convertView.findViewById(R.id.dis_frienditem);
        viewHolder.tvTitle = convertView.findViewById(R.id.dis_friendname);
        viewHolder.tvLetter = convertView.findViewById(R.id.dis_catalog);
        viewHolder.mImageView = convertView.findViewById(R.id.dis_frienduri);
        viewHolder.isSelect = convertView.findViewById(R.id.dis_select);

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(friend.letters);
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.isSelect.setOnClickListener(v -> {
            if (isShowSelected) {
                if (mSelectedFriend.size() >= (5 - membersActivity.managerNum) && viewHolder.isSelect.isChecked()) {
                    viewHolder.isSelect.setChecked(false);
                    DqToast.showShort("最多只能添加5个管理员");
                    return;
                }
                mCBFlag.put(friend.uid, viewHolder.isSelect.isChecked());
                addGroupMember(friend, viewHolder.isSelect.isChecked());
            } else {
                checkPosition(position);
            }
        });
        viewHolder.tvLayout.setOnClickListener(v -> {
            if (isShowSelected) {
                if (mCBFlag.get(friend.uid)) {
                    viewHolder.isSelect.setChecked(false);
                    mCBFlag.put(friend.uid, false);
                } else {
                    viewHolder.isSelect.setChecked(true);
                    if (mSelectedFriend.size() >= (5 - membersActivity.managerNum) && viewHolder.isSelect.isChecked()) {
                        viewHolder.isSelect.setChecked(false);
                        DqToast.showShort("最多只能添加5个管理员");
                        return;
                    }
                    mCBFlag.put(friend.uid, true);
                }
                addGroupMember(friend, viewHolder.isSelect.isChecked());
            } else {
                checkPosition(position);
            }
        });
        if (isShowSelected) {
            if (friend.isSelected()) {
                viewHolder.tvLayout.setClickable(false);
                viewHolder.tvLayout.setOnTouchListener((v, event) -> {
                    viewHolder.isSelect.setPressed(true);
                    return true;
                });
                viewHolder.isSelect.setClickable(false);
                viewHolder.isSelect.setChecked(false);
                viewHolder.isSelect.setSelected(true);
            } else {
                viewHolder.tvLayout.setClickable(true);
                viewHolder.tvLayout.setOnTouchListener((v, event) -> false);
                viewHolder.isSelect.setClickable(true);
                viewHolder.isSelect.setChecked(mCBFlag.get(friend.uid));
                viewHolder.isSelect.setSelected(false);
            }
        } else {
            viewHolder.isSelect.setChecked(membersActivity.checkList.get(position));
        }
        // Friend friend1 = SealUserInfoManager.getInstance().getFriendByID(list.get(position).uid);
        Friend friend1 = FriendDbHelper.getInstance().getFriend(friend.uid);
        if (friend1 != null && !TextUtils.isEmpty(friend1.getRemarks())) {
            viewHolder.tvTitle.setText(friend1.getRemarks());
        } else {
            if (TextUtils.isEmpty(list.get(position).getRemarks())) {
                viewHolder.tvTitle.setText(list.get(position).getName());
            } else {
                viewHolder.tvTitle.setText(list.get(position).getRemarks());
            }
        }
        //String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(list.get(position));
        String headpic = "";
        Friend temp = FriendDbHelper.getInstance().getFriend(friend.uid);
        if (temp != null) {
            headpic = temp.headpic + "";
        }
        GlideUtils.loadHeader(membersActivity, headpic, viewHolder.mImageView);
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

        if(onClickListener != null){
            onClickListener.addGroupMember(friend, isCheck);
        }
    }

    //将其他位置设置为未选 当前位置为已选
    private void checkPosition(int position) {
        if(onClickListener != null){
            onClickListener.checkPosition(position);
        }
    }

    private OnClickListener onClickListener;
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }


    public interface OnClickListener {
        void checkPosition(int position);
        void addGroupMember(Friend friend, boolean isCheck);
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
