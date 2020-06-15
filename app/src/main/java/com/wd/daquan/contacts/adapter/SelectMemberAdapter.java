//package com.wd.daquan.contacts.adapter;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.SectionIndexer;
//
//import com.aides.brother.brotheraides.R;
//import com.aides.brother.brotheraides.base.BaseSelectAdapter;
//import com.aides.brother.brotheraides.constant.KeyValue;
//import com.aides.brother.brotheraides.entity.GroupMemberEntity;
//import com.aides.brother.brotheraides.glide.GlideUtils;
//import com.aides.brother.brotheraides.holder.SelectMemberHolder;
//import com.aides.brother.brotheraides.listener.IConstant;
//import com.aides.brother.brotheraides.util.CNToastUtil;
//
///**
// * @Author: 方志
// * @Time: 2018/8/30 14:52
// * @Description:单选多选适配器
// */
//public class SelectMemberAdapter extends CommBaseSelectAdapter<GroupMemberEntity, SelectMemberHolder> implements SectionIndexer {
//
//    public SelectMemberAdapter(Context context) {
//        super(context);
//    }
//
//    @Override
//    public SelectMemberHolder onBindView(@NonNull ViewGroup parent, int viewType) {
//        return new SelectMemberHolder(LayoutInflater.from(mContext).inflate(R.layout.cn_comm_item_adapter, parent, false));
//    }
//
//    @Override
//    public void onBindData(@NonNull SelectMemberHolder holder, int position) {
//        GroupMemberEntity entity = allList.get(position);
//        holder.checkbox.setVisibility(View.VISIBLE);
//        holder.name.setVisibility(View.VISIBLE);
//
//        //根据position获取分类的首字母的Char ascii值
//        int section = getSectionForPosition(position);
//        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
//        if (position == getPositionForSection(section)) {
//            holder.letter.setVisibility(View.VISIBLE);
//            holder.letter.setText(entity.pinYin);
//        } else {
//            holder.letter.setVisibility(View.GONE);
//        }
//
//        if(!TextUtils.isEmpty(entity.remarks)) {
//            holder.name.setText(entity.remarks);
//        }else {
//            holder.name.setText(entity.nickName);
//        }
//
//        GlideUtils.loadHeader(mContext, entity.headpic, holder.portrait);
//        //不可点击的item
//        if(position == mSelectPosition || notSelectList.contains(entity)) {
//            holder.checkbox.setSelected(true);
//            holder.checkbox.setEnabled(false);
//            holder.checkbox.setClickable(false);
//            holder.itemRl.setClickable(false);
//            holder.itemRl.setEnabled(false);
//        }else {
//            holder.checkbox.setSelected(false);
//            holder.checkbox.setEnabled(true);
//            holder.checkbox.setClickable(true);
//            holder.itemRl.setClickable(true);
//            holder.itemRl.setEnabled(true);
//        }
//          //checkbox点击事件
//        holder.checkbox.setOnClickListener(v -> {
//            if(IConstant.Select.SINGLE.equals(mode)) {
//                single(holder, position);
//            }else if(IConstant.Select.MORE.equals(mode)) {
//                if(isSelectMax(entity) || isSelectAdminMax(entity)) {
//                    return;
//                }
//                entity.setSelected(!entity.isSelected());
//                holder.checkbox.setChecked(entity.isSelected());
//                more(entity);
//            }
//        });
//
        //item点击事件
//        holder.itemRl.setOnClickListener(v -> {
//
//            if(IConstant.Select.SINGLE.equals(mode)) {
//                single(holder, position);
//            }else if(IConstant.Select.MORE.equals(mode)) {
//                if(isSelectMax(entity) || isSelectAdminMax(entity)) {
//                    return;
//                }
//                entity.setSelected(!entity.isSelected());
//                holder.checkbox.setChecked(entity.isSelected());
//                more(entity);
//            }
//        });
//
//        if(IConstant.Select.MORE.equals(mode)) {
//            more(entity);
//        }
//
//        holder.checkbox.setChecked(entity.isSelected());
//    }
//    //设置选择管理员数
//    private boolean isSelectAdminMax(GroupMemberEntity entity) {
//        //优先选择最大人数
//        if(mSelectAdminMax == -1 || mSelectMax != -1) {
//            return false;
//        }
//        int selectMaxCount = getSelectMaxCount();
//
//        if(selectMaxCount >= mSelectAdminMax && !getSelectList().contains(entity)) {
//            String hint = mContext.getString(R.string.choose_members_max) + selectMaxCount + mContext.getString(R.string.pieces_admin);
//            CNToastUtil.showToast(mContext, hint);
//            return true;
//        }
//        return false;
//    }
//    //设置选择最大人数
//    private boolean isSelectMax(GroupMemberEntity entity) {
//
//        if(mSelectMax == -1) {
//            return false;
//        }
//
//        if(getSelectListCount() >= mSelectMax && !getSelectList().contains(entity)) {
//            String hint = mContext.getString(R.string.choose_members_max) + getSelectListCount() + mContext.getString(R.string.pieces_);
//            CNToastUtil.showToast(mContext, hint);
//            return true;
//        }
//        return false;
//    }
//
//    private int getSelectMaxCount() {
//        int adminCount = 0;
//        for (GroupMemberEntity entity: notSelectList){
//            if(KeyValue.Role.ADMIN.equals(entity.role)) {
//                adminCount++;
//            }
//        }
//        return getSelectListCount() + adminCount;
//    }
//
//    @Override
//    public Object[] getSections() {
//        return new Object[0];
//    }
//
//    /**
//     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
//     */
//    @Override
//    public int getPositionForSection(int sectionIndex) {
//        for (int i = 0; i < getItemCount(); i++) {
//            String sortStr = allList.get(i).pinYin;
//            char firstChar = sortStr.toUpperCase().charAt(0);
//            if (firstChar == sectionIndex) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    /**
//     * 根据ListView的当前位置获取分类的首字母的Char ascii值
//     */
//    @Override
//    public int getSectionForPosition(int position) {
//        return allList.get(position).pinYin.charAt(0);
//    }
//}
