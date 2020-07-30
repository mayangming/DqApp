package com.wd.daquan.chat.friend;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import com.da.library.adapter.CommBaseSelectAdapter;
import com.da.library.constant.IConstant;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.bean.Friend;

import org.jetbrains.annotations.NotNull;

/**
 * @author: dukangkang
 * @date: 2018/9/18 20:51.
 * @description: todo ...
 */
public class SelectFriendAdapter extends CommBaseSelectAdapter<Friend, FriendHolder> implements SectionIndexer {

    @Override
    public FriendHolder onBindView(@NonNull ViewGroup parent, int viewType) {
        return new FriendHolder(LayoutInflater.from(mContext).inflate(R.layout.comm_adapter_item, parent, false));
    }

    @Override
    public void onBindData(@NotNull @NonNull FriendHolder holder, int position) {
        Friend item = allList.get(position);
        holder.check.setVisibility(View.VISIBLE);
        holder.name.setVisibility(View.VISIBLE);

        //不可选中更改的数据
        if(notChangeList.contains(item)) {
            holder.itemView.setClickable(false);
            holder.itemView.setEnabled(false);
            holder.check.setClickable(false);
            holder.check.setEnabled(false);
            holder.check.setSelected(true);
        }else {
            holder.itemView.setClickable(true);
            holder.itemView.setEnabled(true);
            holder.check.setClickable(true);
            holder.check.setEnabled(true);
            holder.check.setSelected(false);
        }

        holder.vipIcon.setVisibility(item.isVip.equals("1") ? View.VISIBLE : View.GONE);
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.letter.setVisibility(View.VISIBLE);
            holder.letter.setText(item.pinYin);
        } else {
            holder.letter.setVisibility(View.GONE);
        }

        holder.portrait.setVisibility(View.VISIBLE);
        holder.check.setVisibility(View.VISIBLE);
        holder.name.setText(item.getName());

        GlideUtils.loadHeader(DqApp.sContext, item.headpic, holder.portrait);

        //checkbox点击事件
        holder.check.setOnClickListener(v -> {
            if(IConstant.Select.MORE.equals(mode)) {
                item.setSelected(!item.isSelected());
                holder.check.setChecked(item.isSelected());
                more(item);
            }
        });

        //item点击事件
        holder.itemView.setOnClickListener(v -> {

           if(IConstant.Select.MORE.equals(mode)) {
                item.setSelected(!item.isSelected());
                holder.check.setChecked(item.isSelected());
                more(item);
            }
        });

        if(IConstant.Select.MORE.equals(mode)) {
            more(item);
        }
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
        for (int i = 0; i < getItemCount(); i++) {
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
