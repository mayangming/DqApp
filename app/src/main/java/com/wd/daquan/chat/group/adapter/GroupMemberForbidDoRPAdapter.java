package com.wd.daquan.chat.group.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.R;
import com.da.library.adapter.CommAdapter;
import com.wd.daquan.model.bean.UserBean;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.chat.group.holder.GroupMemberForbidDoRPHolder;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.glide.GlideUtils;

/**
 * fangzhi
 * 禁止收发红包适配器
 */

public class GroupMemberForbidDoRPAdapter extends CommAdapter<UserBean, GroupMemberForbidDoRPHolder> {

    public GroupMemberForbidDoRPAdapter(Context context) {
        super(context);
    }

    @Override
    public GroupMemberForbidDoRPHolder onCreateViewHolder(View convertView, ViewGroup parent, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cn_dispatcher_none_item, parent, false);
        return new GroupMemberForbidDoRPHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupMemberForbidDoRPHolder holder, int position) {
        if (null == mList || null == holder) {
            return;
        }
        UserBean userEntity = mList.get(position);
        if (null == userEntity) {
            return;
        }
        GlideUtils.loadHeader(mContext, userEntity.headpic, holder.mDispatcherHeadpicIv);

        Friend friend = FriendDbHelper.getInstance().getFriend(userEntity.uid);
        String name;
        if (!TextUtils.isEmpty(friend.getRemarks())) {
            name = friend.getRemarks();
        } else {
            name = userEntity.nickname == null ? "" : userEntity.nickname;
        }
        holder.mDispatcherNameTv.setText(name);

        holder.mDispatcherRemoveBt.setOnClickListener(v -> {
            if(null != listener) {
                listener.remove(userEntity.uid, name);
            }
        });
    }


    public interface DispatcherListener{
        void remove(String userId, String userName);
    }

    private DispatcherListener listener;

    public void setListener(DispatcherListener listener) {
        this.listener = listener;
    }
}
