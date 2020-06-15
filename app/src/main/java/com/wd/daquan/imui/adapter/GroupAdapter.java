//package com.wd.daquan.imui.adapter;
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//
//import com.dq.im.model.TeamModel;
//import com.example.qcsdk.R;
//import com.example.qcsdk.adapter.viewholder.RecycleBaseViewHolder;
//import com.example.qcsdk.util.GlideUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 群组列表适配器
// */
//public class GroupAdapter extends RecycleBaseAdapter<GroupAdapter.FriendViewHolder> {
//
//    private List<TeamModel> teamModels = new ArrayList<>();
//    public GroupAdapter(List<TeamModel> teamModels) {
//        this.teamModels = teamModels;
//    }
//
//    public GroupAdapter() {
//    }
//
//    public void setData(List<TeamModel> userModels){
//        this.teamModels = userModels;
//        notifyDataSetChanged();
//    }
//
//    class FriendViewHolder extends RecycleBaseViewHolder {
//         ImageView friendHead;
//         TextView friendName;
//        FriendViewHolder(@NonNull View itemView) {
//            super(itemView);
//            initView();
//        }
//
//        public void initView(){
//            friendHead = itemView.findViewById(R.id.friend_head);
//            friendName = itemView.findViewById(R.id.friend_name);
//        }
//    }
//    @NonNull
//    @Override
//    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        super.onCreateViewHolder(parent, viewType);
//
//        View view = inflater.inflate(R.layout.item_friend, parent, false);
//
//        return new FriendViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
//        super.onBindViewHolder(holder,position);
//        TeamModel userModel = teamModels.get(position);
//        GlideUtil.loadCircleImgByNet(context,userModel.getGroupAvatarUrl(),holder.friendHead);
//        holder.friendName.setText(userModel.getGroupName());
//    }
//
//    @Override
//    public int getItemCount() {
//        return teamModels.size();
//    }
//}