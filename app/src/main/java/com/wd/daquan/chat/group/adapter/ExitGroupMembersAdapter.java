package com.wd.daquan.chat.group.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.da.library.adapter.CommRecyclerViewAdapter;
import com.wd.daquan.chat.group.adapter.holder.ExitGroupMemberViewHolder;
import com.wd.daquan.chat.group.bean.GroupExitMemberEntity;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.glide.GlideUtils;
import com.da.library.tools.DateUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Author: 方志
 * Date:  15:25
 * Desc: 退出群成员列表适配器
 * Edit：
 */
public class ExitGroupMembersAdapter extends CommRecyclerViewAdapter<GroupExitMemberEntity, ExitGroupMemberViewHolder> {

    @Override
    protected ExitGroupMemberViewHolder onBindView(ViewGroup parent, int viewType) {
        return new ExitGroupMemberViewHolder(mInflater.inflate(R.layout.comm_adapter_item, parent, false));
    }

    /**
     * 初始化数据
     * @param t1 所以人员
     */
    public void initDatas(List<GroupExitMemberEntity> t1){
        allList.clear();
        allList.addAll(t1);
        notifyDataSetChanged();
    }


    @Override
    protected void onBindData(@NotNull ExitGroupMemberViewHolder holder, int position) {
        holder.nameLl.setVisibility(View.VISIBLE);
        holder.time.setVisibility(View.VISIBLE);

        GroupExitMemberEntity item = getItem(position);
        holder.userName.setText(item.nickname);

        //自动退出
        if(KeyValue.ONE_STRING.equals(item.type)) {
            holder.exitType.setText(mContext.getString(R.string.active_exit_group_chat));
        }else {
            String str = null;
            //管理员
            if(KeyValue.ONE_STRING.equals(item.operator_role)) {
                str = String.format(DqApp.getStringById(R.string.to_be_group_admin), item.operator_name) ;
            }else if(KeyValue.TWO_STRING.equals(item.operator_role)) {
                str = String.format(DqApp.getStringById(R.string.to_be_group_owner), item.operator_name) ;
            }
            holder.exitType.setText(str);
        }

        holder.time.setText(DateUtil.transferTimeNew(item.quit_time));
        GlideUtils.loadHeader(mContext, item.headpic, holder.portrait);
        holder.layout.setOnClickListener(v -> {
            if(null != listener) {
                listener.onClick(position);
            }
        });
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener lintener) {
        this.listener = lintener;
    }
}
