package com.wd.daquan.chat.group.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wd.daquan.R;
import com.wd.daquan.model.bean.GroupManagerEntity;
import com.wd.daquan.glide.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kind on 2018/9/26.
 */

public class GroupManagerAdapter extends BaseAdapter {

    private List<GroupManagerEntity.InvitedEntity> applist;
    private ButtonClickListener listener;

    public GroupManagerAdapter(ButtonClickListener listener) {
        applist = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return applist.size();
    }

    @Override
    public GroupManagerEntity.InvitedEntity getItem(int i) {
        return applist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.beinvite_item, viewGroup, false);
            viewHolder.iv_invite = view.findViewById(R.id.iv_invite);
            viewHolder.tv_BeInvite = view.findViewById(R.id.tv_BeInvite);
            viewHolder.tv_Invite = view.findViewById(R.id.tv_Invite);
            viewHolder.tv_over = view.findViewById(R.id.tv_over);
            viewHolder.bt_agree = view.findViewById(R.id.bt_agree);
            viewHolder.bt_refuse = view.findViewById(R.id.bt_refuse);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        GroupManagerEntity.InvitedEntity invitedEntity = applist.get(position);
        if (null == invitedEntity) {
            return view;
        }

        GlideUtils.loadHeader(view.getContext(), invitedEntity.headpic, viewHolder.iv_invite);
        viewHolder.tv_BeInvite.setText(invitedEntity.the_inviter_nickname);
        String inviter = "邀请人：" + invitedEntity.inviter_nickname;
        viewHolder.tv_Invite.setText(inviter);
        switch (invitedEntity.status) {
            case "0":
                viewHolder.bt_agree.setVisibility(View.VISIBLE);
                viewHolder.bt_refuse.setVisibility(View.VISIBLE);
                viewHolder.tv_over.setVisibility(View.GONE);
                //  manager.getKDPreferenceUserInfo().saveString(EBSharedPrefUser.TONGYI, "1");
                break;
            case "1":
                statebar(viewHolder, "已同意");
                //    manager.getKDPreferenceUserInfo().saveString(EBSharedPrefUser.TONGYI, "2");
                break;
            case "2":
                statebar(viewHolder, "已拒绝");
                //  manager.getKDPreferenceUserInfo().saveString(EBSharedPrefUser.TONGYI, "2");
                break;
            case "3":
                statebar(viewHolder, "已过期");
                //  manager.getKDPreferenceUserInfo().saveString(EBSharedPrefUser.TONGYI, "2");
                break;
            case "4":
                statebar(viewHolder, "已删除");
                //  manager.getKDPreferenceUserInfo().saveString(EBSharedPrefUser.TONGYI, "2");
                break;
        }
        viewHolder.bt_agree.setOnClickListener(view1 -> listener.click(invitedEntity.request_id, "1"));
        viewHolder.bt_refuse.setOnClickListener(view12 -> listener.click(invitedEntity.request_id, "2"));
        return view;
    }

    private void statebar(ViewHolder viewHolder, String state) {
        viewHolder.bt_agree.setVisibility(View.GONE);
        viewHolder.bt_refuse.setVisibility(View.GONE);
        viewHolder.tv_over.setVisibility(View.VISIBLE);
        viewHolder.tv_over.setText(state);
    }

    public void refreshList(List<GroupManagerEntity.InvitedEntity> list) {
        applist.clear();
        applist.addAll(list);
        notifyDataSetChanged();
    }

    public interface ButtonClickListener {
        void click(String request_id, String agree);
    }

    class ViewHolder {
        ImageView iv_invite;
        TextView tv_BeInvite;
        TextView tv_Invite;
        TextView tv_over;
        TextView bt_agree;
        TextView bt_refuse;
    }
}
