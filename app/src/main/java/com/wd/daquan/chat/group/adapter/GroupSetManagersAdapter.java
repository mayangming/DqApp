package com.wd.daquan.chat.group.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.chat.group.bean.GroupManagersItemResp;
import com.wd.daquan.glide.GlideUtils;

import java.util.List;

/**
 * Created by Kind on 2018/9/26.
 */

public class GroupSetManagersAdapter extends BaseAdapter {

    private Context context;
    private List<GroupManagersItemResp> list;
    private OnClickItemDelete mOnClickItemDelete;
    public GroupSetManagersAdapter(Context context, List<GroupManagersItemResp> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public GroupManagersItemResp getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        GroupManagersItemResp content = getItem(position);
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.group_managers_item, parent, false);
            viewHolder.nickName = (TextView) convertView.findViewById(R.id.groupSetMangerListName);
            viewHolder.mAvatar = (ImageView) convertView.findViewById(R.id.groupSetMangerListAvatar);
            viewHolder.mDelete = (ImageView) convertView.findViewById(R.id.groupSetMangerListDelete);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        GlideUtils.loadHeader(convertView.getContext(), content.headpic, viewHolder.mAvatar);
        viewHolder.nickName.setText(content.nickname);
        viewHolder.mDelete.setOnClickListener(v -> mOnClickItemDelete.onItemDelete(content));
        return convertView;
    }

    public void setNotifyData(List<GroupManagersItemResp> list){
        this.list = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView nickName;
        ImageView mAvatar;
        ImageView mDelete;
    }

    public interface OnClickItemDelete{
        void onItemDelete(GroupManagersItemResp content);
    }

    public void setOnClickItemDelete(OnClickItemDelete mOnClickItemDelete){
        this.mOnClickItemDelete = mOnClickItemDelete;
    }
}
