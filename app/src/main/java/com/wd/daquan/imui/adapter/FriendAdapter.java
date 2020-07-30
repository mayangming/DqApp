package com.wd.daquan.imui.adapter;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dq.im.model.UserModel;
import com.wd.daquan.R;
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder;
import com.wd.daquan.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 好友列表适配器
 */
public class FriendAdapter extends RecycleBaseAdapter<FriendAdapter.FriendViewHolder> {

    private List<UserModel> userModels = new ArrayList<>();
    public FriendAdapter(List<UserModel> userModels) {
        this.userModels = userModels;
    }

    public FriendAdapter() {
    }

    public void setData(List<UserModel> userModels){
        this.userModels = userModels;
        notifyDataSetChanged();
    }

    class FriendViewHolder extends RecycleBaseViewHolder

    {
         ImageView friendHead;
         TextView friendName;
        FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            initView();
        }

        public void initView(){
            friendHead = itemView.findViewById(R.id.friend_head);
            friendName = itemView.findViewById(R.id.friend_name);
        }
    }
    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        View view = inflater.inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        UserModel userModel = userModels.get(position);
        GlideUtil.loadCircleImgByNet(context,userModel.getPicUrl(),holder.friendHead);
        holder.friendName.setText(userModel.getUsername());
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }
}