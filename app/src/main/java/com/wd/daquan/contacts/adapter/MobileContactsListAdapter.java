package com.wd.daquan.contacts.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.contacts.bean.MobileContactBean;
import com.wd.daquan.contacts.holder.MobileContactHolder;
import com.wd.daquan.contacts.listener.IMobileContactsListAdapterClickListener;
import com.wd.daquan.glide.GlideUtils;
import com.da.library.tools.DensityUtil;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: 方志
 * @Time: 2018/9/17 13:36
 * @Description:
 */
public class MobileContactsListAdapter extends CommRecyclerViewAdapter<MobileContactBean, MobileContactHolder> {
    private IMobileContactsListAdapterClickListener mListener;

    @Override
    protected MobileContactHolder onBindView(ViewGroup parent, int viewType) {
        return new MobileContactHolder(mInflater.inflate(R.layout.comm_adapter_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindData(@NotNull MobileContactHolder holder, int position) {
        MobileContactBean item = getItem(position);
        if(item == null) return;

        holder.nameLl.setVisibility(View.VISIBLE);
        holder.add.setVisibility(View.VISIBLE);

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.letter.setVisibility(View.VISIBLE);
            holder.letter.setText(item.pinYin);
        } else {
            holder.letter.setVisibility(View.GONE);
        }

        holder.userName.setText(item.userName);
        holder.nickName.setText("手机通讯录：" + item.nickname );

        if("0".equals(item.whether_friend)) {
            setAddText(holder.add, true);
        }else {
            setAddText(holder.add, false);
        }

        GlideUtils.loadHeader(mContext, item.headpic, holder.portrait);


        holder.itemView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onItemClick(position, item);
            }
        });

        holder.add.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onAgreeClick(position, item);
            }
        });

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

    private void setAddText(TextView view, Boolean boo) {

        view.setTextColor(boo ? mContext.getResources().getColor(R.color.white)
                : mContext.getResources().getColor(R.color.text_title_black));
        if (boo) {
            view.setEnabled(true);
            view.setClickable(true);
            view.setBackgroundResource(R.drawable.comm_btn_4dp_corner_selector);
            view.setText(mContext.getString(R.string.add));
            view.setPadding(DensityUtil.dip2px(mContext, 6),0,DensityUtil.dip2px(mContext, 6),0);
        } else {
            view.setEnabled(false);
            view.setClickable(false);
            view.setBackgroundResource(R.color.white);
            view.setText(mContext.getString(R.string.added));
            view.setPadding(0,0,0,0);
        }
    }

    public void setListener(IMobileContactsListAdapterClickListener mListener) {
        this.mListener = mListener;
    }
}
