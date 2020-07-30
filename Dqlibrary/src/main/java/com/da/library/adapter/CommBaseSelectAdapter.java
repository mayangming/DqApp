package com.da.library.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.da.library.listener.IOnItemClickSelectListener;
import com.wd.daquan.model.interfaces.ISelect;
import com.da.library.listener.ISelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 方志
 * @Time: 2018/8/29 18:52
 * @Description: 单选多选的适配器基类
 */
public abstract class CommBaseSelectAdapter<T extends ISelect, VH extends RecyclerView.ViewHolder> extends
        CommRecyclerViewAdapter<T, VH> {

    /**
     * 单选/多选
     */
    public String mode;
    /**
     * 选好的数据,状态可以修改
     */
    private List<T> selectList = new ArrayList<>();
    /**
     * 默认选中的人员，不可更改状态
     */
    protected List<T> notChangeList = new ArrayList<>();
    /**
     * 设置已经选中不可点击的位置 默认
     */
    protected int mSelectPosition = -1;
    /**
     * 单选局部刷新
     */
    protected int mSelectPos = -1;

    /**
     * 选中的数据监听器
     */
    private ISelectListener<T> mSelectListener;
    /**
     * 选中的数据监听器
     */
    protected IOnItemClickSelectListener<T> mItemClickListener;
    /**
     * 设置管理员最大选择数量
     */
    protected int mSelectAdminMax = -1;
    /**
     * 设置人员员最大选择数量
     */
    protected int mSelectMax = -1;

    public List<T> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<T> data) {
        if (data != null) {
            selectList.clear();
            selectList.addAll(data);
            for (T t : selectList) {
                t.setSelected(true);
            }
        }
    }

    public int getSelectListCount() {
        return selectList.size();
    }

    /**
     * 单选
     */
    public void single(VH holder, final int position) {
        selectList.clear();
        //全部刷新
        for (T t : allList) {
            t.setSelected(false);
        }

        T t = allList.get(position);
        t.setSelected(!t.isSelected());
        selectList.add(t);

        holder.itemView.post(() -> notifyDataSetChanged());

//        //局部刷新
//        if(mSelectPos == -1) {
//            mSelectPos = position;
//        }
//        T t = allList.get(mSelectPos);
//        if(mSelectPos != position){
//
//            t.setSelected(false);
//
//            holder.itemView.post(new Runnable() {
//                @Override
//                public void run() {
//                    notifyItemChanged(mSelectPos);
//
//                    mSelectPos = position;
//
//                    T newT = allList.get(mSelectPos);
//                    newT.setSelected(true);
//                    if(!selectList.contains(newT)) {
//                        selectList.add(newT);
//                    }
//                }
//            });
//
//        }else {
//            t.setSelected(!t.isSelected());
//
//            if(t.isSelected()) {
//                if(!selectList.contains(t)) {
//                    selectList.add(t);
//                }
//            }else {
//                selectList.clear();
//            }
//        }

        holder.itemView.postDelayed(() -> {
            notifyItemChanged(mSelectPos);
            if (mSelectListener != null) {
                mSelectListener.onSelectList(selectList);
            }
        }, 50);
    }

    /**
     * 多选
     */
    public void more(T t) {
        if (t.isSelected()) {
            if (!selectList.contains(t)) {
                selectList.add(t);
            }
        } else {
            if (selectList.contains(t)) {
                selectList.remove(t);
            }
        }

        if (mSelectListener != null) {
            mSelectListener.onSelectList(selectList);
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setSelectPosition(int position) {
        mSelectPosition = position;
    }

    public void setNotSelectMember(List<T> data){
        if(null != data) {
            notChangeList.clear();
            notChangeList.addAll(data);
        }
    }
    public void setNotSelectMember(T data){
        if(null != data) {
            notChangeList.add(data);
        }
    }

    public void setSelectListener(ISelectListener<T> selectListener) {
        this.mSelectListener = selectListener;
    }

    @Override
    public void clear() {
        super.clear();
        selectList.clear();
        notChangeList.clear();
    }

    public void setSelectAdminMax(int selectAdminMax) {
        this.mSelectAdminMax = selectAdminMax;
    }
    public void setSelectMax(int selectMax) {
        this.mSelectMax = selectMax;
    }

    public void setItemClickListener(IOnItemClickSelectListener<T> itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }
}
