//package com.wd.daquan.imui.adapter;
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//
//import com.dq.im.model.GroupTeamUserModel;
//import com.example.qcsdk.R;
//import com.example.qcsdk.adapter.viewholder.RecycleBaseViewHolder;
//import com.example.qcsdk.util.GlideUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 创建群组的好友列表的适配器
// */
//public class GroupCreateAdapter extends RecycleBaseAdapter<GroupCreateAdapter.GroupCreateViewHolder>{
//
//    private List<GroupTeamUserModel> groupTeamUserModes = new ArrayList<>();
//
//    public GroupCreateAdapter() {
//    }
//
//    public GroupCreateAdapter(List<GroupTeamUserModel> groupTeamUserModes) {
//        this.groupTeamUserModes = groupTeamUserModes;
//    }
//
//    public void addData(List<GroupTeamUserModel> groupTeamUserModes){
//        this.groupTeamUserModes = groupTeamUserModes;
//        notifyDataSetChanged();
//    }
//
//    class GroupCreateViewHolder extends RecycleBaseViewHolder
//
//    {
//        CheckBox userCheckBtn;
//        ImageView userHeadIcon;
//        TextView userName;
//         GroupCreateViewHolder(@NonNull View itemView) {
//            super(itemView);
//             initView();
//        }
//
//        public void initView(){
//            userCheckBtn = itemView.findViewById(R.id.group_user_check_status);
//            userHeadIcon = itemView.findViewById(R.id.group_user_head_icon);
//            userName = itemView.findViewById(R.id.group_user_name);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    userCheckBtn.setChecked(!userCheckBtn.isChecked());
//                }
//            });
//        }
//
//    }
//
//    @NonNull
//    @Override
//    public GroupCreateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        super.onCreateViewHolder(parent, viewType);
//
//        View view = inflater.inflate(R.layout.item_group_create_user, parent, false);
//
//        return new GroupCreateViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull GroupCreateViewHolder holder, int position) {
//        super.onBindViewHolder(holder,position);
//        GroupTeamUserModel groupTeamUserModel = groupTeamUserModes.get(position);
//        holder.userCheckBtn.setChecked(groupTeamUserModel.isChecked());
//        holder.userName.setText(groupTeamUserModel.getUserModel().getUsername());
//        GlideUtil.loadCircleImgByNet(context,groupTeamUserModel.getUserModel().getPicUrl(),holder.userHeadIcon);
//        holder.userCheckBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                groupTeamUserModel.setChecked(isChecked);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return groupTeamUserModes.size();
//    }
//}