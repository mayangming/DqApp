package com.wd.daquan.contacts.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import com.wd.daquan.R;
import com.da.library.adapter.CommBaseSelectAdapter;
import com.da.library.constant.IConstant;
import com.wd.daquan.contacts.bean.MobileContactBean;
import com.wd.daquan.contacts.holder.SelectMobileContactHolder;
import com.da.library.tools.DensityUtil;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: 方志
 * @Time: 2018/8/30 14:52
 * @Description:
 */
public class SelectMobileContactAdapter extends CommBaseSelectAdapter<MobileContactBean, SelectMobileContactHolder> implements SectionIndexer {

    @Override
    public SelectMobileContactHolder onBindView(@NonNull ViewGroup parent, int viewType) {
        return new SelectMobileContactHolder(LayoutInflater.from(mContext).inflate(R.layout.comm_adapter_item, parent, false));
    }

    @Override
    public void onBindData(@NotNull @NonNull SelectMobileContactHolder holder, int position) {
        MobileContactBean item = allList.get(position);
        holder.checkbox.setVisibility(View.VISIBLE);
        holder.nameLl.setVisibility(View.VISIBLE);

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.letter.setVisibility(View.VISIBLE);
            holder.letter.setText(item.pinYin);
        } else {
            holder.letter.setVisibility(View.GONE);
        }

        isShowHeader(holder);

        holder.userName.setText(item.userName);
        holder.nickName.setText(item.phone);

          //checkbox点击事件
        holder.checkbox.setOnClickListener(v -> {
           if(IConstant.Select.MORE.equals(mode)) {
                item.setSelected(!item.isSelected());
                holder.checkbox.setChecked(item.isSelected());
                more(item);
            }
        });

        //item点击事件
        holder.itemView.setOnClickListener(v -> {

            if(IConstant.Select.SINGLE.equals(mode)) {
                //此处不使用单选，点击item直接获取数据
                //single(holder, position);
                if(null != mItemClickListener) {
                    mItemClickListener.onItemClick(item);
                }
            }else if(IConstant.Select.MORE.equals(mode)) {
                item.setSelected(!item.isSelected());
                holder.checkbox.setChecked(item.isSelected());
                more(item);
            }
        });

        if(IConstant.Select.MORE.equals(mode)) {
            more(item);
        }
        holder.checkbox.setChecked(item.isSelected());
    }

    private void isShowHeader(@NonNull SelectMobileContactHolder holder) {
        if(IConstant.Select.SINGLE.equals(mode)) {
            holder.portrait.setVisibility(View.VISIBLE);
            holder.checkbox.setVisibility(View.GONE);
            holder.userName.setPadding(0,0,0,0);
            holder.nickName.setPadding(0,0,0,0);
        }else {
            holder.portrait.setVisibility(View.GONE);
            holder.checkbox.setVisibility(View.VISIBLE);
            int width = DensityUtil.dip2px(mContext, 9);
            holder.userName.setPadding(width,0,0,0);
            holder.nickName.setPadding(width,0,0,0);
        }
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
