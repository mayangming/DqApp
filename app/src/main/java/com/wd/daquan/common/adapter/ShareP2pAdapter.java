package com.wd.daquan.common.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.R;
import com.da.library.adapter.CommBaseSelectAdapter;
import com.wd.daquan.chat.friend.FriendHolder;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.glide.GlideUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: 方志
 * @Time: 2018/9/21 10:48
 * @Description: 分享到个人
 */
public class ShareP2pAdapter extends CommBaseSelectAdapter<Friend, FriendHolder> {

    @Override
    public FriendHolder onBindView(ViewGroup parent, int viewType) {
        return new FriendHolder(mInflater.inflate(R.layout.comm_adapter_item, parent, false));
    }

    @Override
    public void onBindData(@NotNull FriendHolder holder, int position) {
        Friend item = allList.get(position);
        if (null == item) return;

        holder.name.setVisibility(View.VISIBLE);
        holder.check.setVisibility(View.VISIBLE);

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.letter.setVisibility(View.VISIBLE);
            holder.letter.setText(item.pinYin);
        } else {
            holder.letter.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.remarks)) {
            holder.name.setText(item.remarks);
            item.nickname = item.remarks;
        } else {
            holder.name.setText(item.nickname);
        }

        GlideUtils.loadHeader(mContext, item.headpic, holder.portrait);

        //checkbox点击事件
        holder.check.setOnClickListener(v -> {
            item.setSelected(!item.isSelected());
            holder.check.setChecked(item.isSelected());
            more(item);
        });

        //item点击事件
        holder.itemView.setOnClickListener(v -> {
            item.setSelected(!item.isSelected());
            holder.check.setChecked(item.isSelected());
            more(item);
        });

        more(item);
        holder.check.setChecked(item.isSelected());
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
            String sortStr = allList.get(i).pinYin;
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
        return allList.get(position).pinYin.charAt(0);
    }


}
