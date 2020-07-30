package com.wd.daquan.chat.card;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.da.library.adapter.CommBaseSelectAdapter;
import com.wd.daquan.chat.ait.AitHolder;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.glide.GlideUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author: dukangkang
 * @date: 2018/9/25 13:43.
 * @description: todo ...
 */
public class CardAdapter extends CommBaseSelectAdapter<Friend, AitHolder> implements SectionIndexer {

    @Override
    public AitHolder onBindView(@NonNull ViewGroup parent, int viewType) {
        return new AitHolder(LayoutInflater.from(mContext).inflate(R.layout.ait_list_item, null));
    }

    @Override
    public void onBindData(@NotNull @NonNull AitHolder holder, int position) {
        Friend friend = allList.get(position);

        holder.name.setText(friend.getName());

        GlideUtils.loadHeader(DqApp.sContext, friend.headpic, holder.portrait);

        //item点击事件
        holder.rlyt.setOnClickListener(v -> {

            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(friend);
            }
        });

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
