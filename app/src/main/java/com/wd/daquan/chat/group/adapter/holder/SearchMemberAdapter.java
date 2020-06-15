package com.wd.daquan.chat.group.adapter.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wd.daquan.model.bean.GroupMemberBean;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.R;
import com.da.library.adapter.CommAdapter;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.glide.GlideUtils;

/**
 * @author: dukangkang
 * @date: 2018/4/20 11:19.
 * @description: 搜索群组成员适配器
 */
public class SearchMemberAdapter extends CommAdapter<GroupMemberBean, SearchMemberHolder> {

    private String mNickName = null;

    public SearchMemberAdapter(Context context) {
        super(context);
    }

    public void setNickName(String nickName) {
        mNickName = nickName;
    }

    @Override
    public SearchMemberHolder onCreateViewHolder(View convertView, ViewGroup parent, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cn_comm_search_item, parent, false);
        return new SearchMemberHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchMemberHolder memberHolder, int position) {
        if (mList == null || null == memberHolder) {
            return;
        }

        GroupMemberBean groupMember = mList.get(position);
        if (null == groupMember) {
            return;
        }


        // 设置群内显示名称
       // Friend friend = SealUserInfoManager.getInstance().getFriendByID(groupMember.getUserId());
        Friend friend = FriendDbHelper.getInstance().getFriend(groupMember.getUid());
        if (friend != null && !TextUtils.isEmpty(friend.getName()) && !TextUtils.isEmpty(friend.getRemarks())) {
           // String userId = mSharedPreference.getKDPreferenceUserInfo().getString(EBSharedPrefUser.uid, "0");
            String userId = ModuleMgr.getCenterMgr().getUID();
            if (friend.uid.equals(userId)) {
                if (!TextUtils.isEmpty(mNickName)) {
                    memberHolder.title.setText(mNickName);
                } else {
                    memberHolder.title.setText(friend.getName());
                }
            } else {
                if (TextUtils.isEmpty(friend.getRemarks())) {//是否有备注
                    memberHolder.title.setText(friend.getName());
                } else {
                    memberHolder.title.setText(friend.getRemarks());
                }
            }
        } else {
            if (TextUtils.isEmpty(groupMember.getRemarks())) {
                memberHolder.title.setText(groupMember.getNickname());
            } else {
                memberHolder.title.setText(groupMember.getRemarks());
            }
        }

        GlideUtils.loadHeader(mContext, friend.headpic, memberHolder.portrait);

//        String portraitUrl = SealUserInfoManager.getInstance().getPortraitUri(groupMember);
//        BaseImgView.ImageLoaderLoadimg(memberHolder.portrait, portraitUrl, R.mipmap.user_avatar, R.mipmap.user_avatar, R.mipmap.user_avatar, 60, 60, 4);

    }
}
