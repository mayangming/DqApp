package com.wd.daquan.contacts.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.da.library.adapter.CommRecyclerViewAdapter;
import com.da.library.holder.NoDataHolder;
import com.wd.daquan.R;
import com.wd.daquan.contacts.holder.ContactsListFootHolder;
import com.wd.daquan.contacts.holder.ContactsListHeaderHolder;
import com.wd.daquan.contacts.holder.FriendHolder;
import com.wd.daquan.contacts.listener.IContactsFragmentAdapterListener;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.Friend;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: 方志
 * @Time: 2018/9/6 11:38
 * @Description: 联系人适配器，分类加载
 */
@SuppressLint("SetTextI18n")
public class ContactsFragmentAdapter extends CommRecyclerViewAdapter<Friend, RecyclerView.ViewHolder> {

    private final int ITEM_HEADER = 1;
    private final int ITEM_BODY = 2;
    private final int ITEM_FOOT = 3;
    private final int ITEM_NO_DATA = -1;
    private IContactsFragmentAdapterListener mListener;

    public void setListener(IContactsFragmentAdapterListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_HEADER;
        }else {
            if(allList.size() > 0) {
                if (position <= allList.size()) {
                    return ITEM_BODY;
                }
                return ITEM_FOOT;
            }else {
                return ITEM_NO_DATA;
            }
        }
    }

    @Override
    protected RecyclerView.ViewHolder onBindView(ViewGroup parent, int viewType) {
        if (ITEM_HEADER == viewType) {
            return new ContactsListHeaderHolder(mInflater.inflate(R.layout.item_contact_list_header, parent, false));
        } else if (ITEM_BODY == viewType) {
            return new FriendHolder(mInflater.inflate(R.layout.comm_adapter_item, parent, false));
        }else if(ITEM_FOOT == viewType) {
            return new ContactsListFootHolder(mInflater.inflate(R.layout.item_contact_list_footer, parent, false));
        }else {
            return new NoDataHolder(mInflater.inflate(R.layout.item_contact_list_no_data, parent, false));
        }
    }

    @Override
    protected void onBindData(@NotNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_HEADER:
                ContactsListHeaderHolder headerHolder = (ContactsListHeaderHolder) holder;
                setContactHeaderData(headerHolder);
                break;
            case ITEM_BODY:
                FriendHolder friendHolder = (FriendHolder) holder;
                setContactData(friendHolder, position);
                break;
            case ITEM_FOOT:
                ContactsListFootHolder footHolder = (ContactsListFootHolder) holder;
                footHolder.itemView.setVisibility(allList.size() > 0 ? View.VISIBLE : View.GONE);
                footHolder.itemNumber.setText(allList.size() + mContext.getString(R.string.pic_contacts));
                break;
            case ITEM_NO_DATA:
                noData((NoDataHolder) holder);
                break;
        }
    }

    private void noData(NoDataHolder holder) {
        holder.itemView.setVisibility(allList.size() > 0 ? View.GONE : View.VISIBLE);
        holder.itemView.setOnClickListener(view -> {
            if(mListener != null) {
                mListener.onFresh();
            }
        });
    }

    private void setContactHeaderData(ContactsListHeaderHolder headerHolder) {
        if(null != mListener) {
            mListener.onHeaderNewFriend(headerHolder.mNewFriendNotify);
        }
    }

    private void setContactData(FriendHolder friendHolder, int position) {
        int newPos = position - 1;
        Friend friend = allList.get(newPos);
        if (null == friend) {
            return;
        }

        friendHolder.name.setVisibility(View.VISIBLE);

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(newPos);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (newPos == getPositionForSection(section)) {
            friendHolder.letter.setVisibility(View.VISIBLE);
            friendHolder.letter.setText(friend.pinYin);
        } else {
            friendHolder.letter.setVisibility(View.GONE);
        }

        friendHolder.name.setText(friend.getName());
        GlideUtils.loadHeader(mContext, friend.headpic, friendHolder.portrait);

        if (friend.isVip()){
            friendHolder.head_outline.setVisibility(View.VISIBLE);
        }else {
            friendHolder.head_outline.setVisibility(View.GONE);
        }

        friendHolder.layout.setOnClickListener(v ->{
            if(null != mListener) {
                mListener.onItemClick(newPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allList.size() + 2;
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
        for (int i = 0; i < allList.size(); i++) {
            String sortStr = allList.get(i).getPinYin();
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
        Friend friend = allList.get(position);
        String pinYin = friend.getPinYin();
        return pinYin.charAt(0);
    }
}
