//package com.wd.daquan.imui.adapter;
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//
//import com.dq.im.model.UserModel;
//import com.example.qcsdk.R;
//import com.example.qcsdk.adapter.viewholder.RecycleBaseViewHolder;
//import com.example.qcsdk.util.GlideUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 群组列表详情的适配器
// */
//public class GroupDetailsAdapter extends RecycleBaseAdapter<GroupDetailsAdapter.GroupDetailsViewHolder>{
//    private int type = 0;//0 用户，1 footer
//    List<UserModel> userModels = new ArrayList<>();
//    class GroupDetailsViewHolder extends RecycleBaseViewHolder
//
//    {
//        private ImageView groupHeadPic;
//        private TextView groupName;
//        GroupDetailsViewHolder(@NonNull View itemView) {
//            super(itemView);
//            initView();
//        }
//
//        public void initView(){
//            groupHeadPic = itemView.findViewById(R.id.group_details_friend_head);
//            groupName = itemView.findViewById(R.id.group_details_friend_name);
//        }
//
//    }
//
//    public GroupDetailsAdapter() {
//    }
//
//    public GroupDetailsAdapter(List<UserModel> userModels) {
//        this.userModels = userModels;
//    }
//
//    public void setData(List<UserModel> userModels){
//        this.userModels = userModels;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public GroupDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        super.onCreateViewHolder(parent, viewType);
//        View view = inflater.inflate(R.layout.item_group_details_multi_user, parent, false);
//        return new GroupDetailsViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull GroupDetailsViewHolder holder, int position) {
//        super.onBindViewHolder(holder,position);
//        int type = getItemViewType(position);
//        if (0 == type){
//            UserModel userModel = userModels.get(position);
//            holder.groupName.setText(userModel.getUsername());
//            GlideUtil.loadCircleImgByNet(context,userModel.getPicUrl(),holder.groupHeadPic);
//        }else {
//            holder.groupName.setText("footer");
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return userModels.size()+1;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//
//        return position < userModels.size() ? 0 : 1;
//    }
//}