//package com.wd.daquan.imui.adapter;
//
//import android.content.Intent;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import com.dq.im.model.UserModel;
//import com.wd.daquan.R;
//import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder;
//import com.wd.daquan.imui.type.FriendMultiType;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 好友列表多类型布局
// */
//public class FriendMultiTypeAdapter extends RecycleBaseAdapter<RecycleBaseViewHolder>{
//
//    private List<FriendMultiTypeModel> multiTypeModels = new ArrayList<>();
//
//    public FriendMultiTypeAdapter() {
//    }
//
//    public FriendMultiTypeAdapter(List<FriendMultiTypeModel> multiTypeModels) {
//        this.multiTypeModels = multiTypeModels;
//    }
//
//    public void setData(List<FriendMultiTypeModel> multiTypeModels){
//        this.multiTypeModels = multiTypeModels;
//        notifyDataSetChanged();
//    }
//
//    class FriendMultiHolderGroup extends RecycleBaseViewHolder{
//        Button groupContent;
//        FriendMultiHolderGroup(@NonNull View itemView) {
//            super(itemView);
//            initView();
//        }
//
//       public void initView(){
//            groupContent = itemView.findViewById(R.id.item_friend_multi_group);
//            groupContent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("YM","FriendMultiTypeAdapter点击事件");
//                    recycleItemOnClickListener.onItemClick(v,getAdapterPosition());
//                }
//            });
//        }
//    }
//
//    class FriendMultiHolderFriend extends RecycleBaseViewHolder{
//        RecyclerView friendContentRv;
//        FriendMultiHolderFriend(@NonNull View itemView) {
//            super(itemView);
//            initView();
//            initRecycleView(friendContentRv);
//        }
//
//        public void initView(){
//            friendContentRv = itemView.findViewById(R.id.item_friend_multi_friend);
//        }
//
//    }
//
//    @NonNull
//    @Override
//    public RecycleBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        super.onCreateViewHolder(parent, viewType);
//
//        RecycleBaseViewHolder recycleBaseViewHolder;
//
//        if (0 == viewType) {
//            View view = inflater.inflate(R.layout.item_friend_multi_group, parent, false);
//            recycleBaseViewHolder = new FriendMultiHolderGroup(view);
//        } else {
//            View view = inflater.inflate(R.layout.item_friend_multi_friend, parent, false);
//            recycleBaseViewHolder = new FriendMultiHolderFriend(view);
//        }
//
//        return recycleBaseViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecycleBaseViewHolder holder, int position) {
//        super.onBindViewHolder(holder, position);
//        int type = getItemViewType(position);
//        Object content = multiTypeModels.get(position).getFriendMultiContent();
//        if (FriendMultiType.GROUP == FriendMultiType.getFiendMultiType(type)){
//            FriendMultiHolderGroup multiHolderGroup = (FriendMultiHolderGroup) holder;
//            multiHolderGroup.groupContent.setText("我是群组");
//        }else {
//            List<UserModel> userModels = (List<UserModel>) content;
//            FriendMultiHolderFriend multiHolderFriend = (FriendMultiHolderFriend) holder;
//            FriendAdapter friendAdapter;
//            friendAdapter = new FriendAdapter();
//            friendAdapter.setRecycleItemOnClickListener(new RecycleItemOnClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    Log.e("YM","FriendAdapter点击事件");
//                    Intent intent = new Intent(context, FriendDetailsActivity.class);
//                    intent.putExtra(FriendDetailsActivity.FRIEND_ID,userModels.get(position).getUserId());
//                    context.startActivity(intent);
//                }
//            });
//            multiHolderFriend.friendContentRv.setAdapter(friendAdapter);
//            friendAdapter.setData(userModels);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return multiTypeModels.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return multiTypeModels.get(position).getFriendMultiType().getValue();
//    }
//
//    private void initRecycleView(RecyclerView recyclerView){
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(),RecyclerView.VERTICAL,false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//    }
//}