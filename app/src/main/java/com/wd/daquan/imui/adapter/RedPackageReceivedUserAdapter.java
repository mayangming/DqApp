package com.wd.daquan.imui.adapter;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.da.library.utils.DateUtil;
import com.dq.im.viewmodel.UserViewModel;
import com.wd.daquan.R;
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder;
import com.wd.daquan.imui.bean.CouponBean;
import com.wd.daquan.third.helper.UserInfoHelper;
import com.wd.daquan.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 红包用户列表
 */
public class RedPackageReceivedUserAdapter extends RecycleBaseAdapter<RedPackageReceivedUserAdapter.RedPackageReceivedUserViewHolder>{

    private List<CouponBean> userModels = new ArrayList<>();
    private FragmentActivity fragmentActivity;
    private UserViewModel userViewModel;

    public RedPackageReceivedUserAdapter(List<CouponBean> userModels) {
        this.userModels = userModels;
    }

    public RedPackageReceivedUserAdapter(FragmentActivity activity) {
        this.fragmentActivity = activity;
        userViewModel = ViewModelProviders.of(activity).get(UserViewModel.class);
    }

    public void setData(List<CouponBean> userModels){
        this.userModels = userModels;
        notifyDataSetChanged();
    }

    class RedPackageReceivedUserViewHolder extends RecycleBaseViewHolder {
        ImageView friendHead;
        TextView friendName;
        TextView redPackageMoney;
        TextView redPackageTime;
        RedPackageReceivedUserViewHolder(@NonNull View itemView) {
            super(itemView);
            initView();
        }

        public void initView(){
            friendHead = itemView.findViewById(R.id.friend_head);
            friendName = itemView.findViewById(R.id.friend_name);
            redPackageMoney = itemView.findViewById(R.id.friend_money);
            redPackageTime = itemView.findViewById(R.id.red_package_time);
        }
    }

    @NonNull
    @Override
    public RedPackageReceivedUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        View view = inflater.inflate(R.layout.item_red_package_receive_user, parent, false);
        return new RedPackageReceivedUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RedPackageReceivedUserViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        CouponBean openRedPackageResultBean = userModels.get(position);
//        GlideUtil.loadCircleImgByNet(context,userModel.getPicUrl(),holder.friendHead);
//        holder.friendName.setText(userModel.getUsername());
        holder.redPackageMoney.setText(openRedPackageResultBean.getMoney()+"元");
        holder.redPackageTime.setText(DateUtil.getDateToString(openRedPackageResultBean.getCreateTime(),DateUtil.YMDHM));
        initUserData(holder,openRedPackageResultBean.getUserId());
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    /**
     * 更新右侧用户数据
     */
    protected void initUserData(RedPackageReceivedUserViewHolder holder, String userId){
//        userViewModel.getUserByUserId(userId).observe(fragmentActivity, new Observer<UserModel>() {
//            @Override
//            public void onChanged(UserModel userModel) {
//                if (null != userModel){
//                    GlideUtil.loadCircleImgByNet(context,userModel.getPicUrl(),holder.friendHead);
//                    holder.friendName.setText(userModel.getUsername());
//                }else {
//                    holder.friendHead.setImageResource(R.mipmap.ic_launcher);
//                }
//            }
//        });
        String userInfoHeadPic = UserInfoHelper.getHeadPic(userId);
        GlideUtil.loadRectHeadByNet(context,userInfoHeadPic,holder.friendHead);
        holder.friendName.setText(UserInfoHelper.getUserNickName(userId));
    }

}