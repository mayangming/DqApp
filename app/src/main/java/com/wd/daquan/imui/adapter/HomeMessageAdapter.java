package com.wd.daquan.imui.adapter;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dq.im.bean.im.MessageTextBean;
import com.dq.im.model.HomeImBaseMode;
import com.dq.im.type.ImType;
import com.dq.im.type.MessageType;
import com.dq.im.util.TimeUtils;
import com.dq.im.viewmodel.TeamViewModel;
import com.dq.im.viewmodel.UserViewModel;
import com.google.gson.Gson;
import com.wd.daquan.R;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.imui.adapter.viewholder.RecycleBaseViewHolder;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.db.DbSubscribe;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.third.helper.TeamHelper;
import com.wd.daquan.third.helper.UserInfoHelper;
import com.wd.daquan.util.AESUtil;
import com.wd.daquan.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 首页列表适配器
 */
public class HomeMessageAdapter extends RecycleBaseAdapter<HomeMessageAdapter.HomeMessageViewHolder> {

    private List<HomeImBaseMode> homeImBaseModes = new ArrayList<>();
    private Fragment fragment;
    private UserViewModel userViewModel;
    private TeamViewModel teamViewModel;
    private Gson gson = new Gson();
    public HomeMessageAdapter(Fragment fragment) {
        this.fragment = fragment;
        userViewModel = ViewModelProviders.of(fragment).get(UserViewModel.class);
        teamViewModel = ViewModelProviders.of(fragment).get(TeamViewModel.class);
    }

    public HomeMessageAdapter(List<HomeImBaseMode> homeImBaseModes) {
        this.homeImBaseModes = homeImBaseModes;
    }

    public void setData(List<HomeImBaseMode> homeImBaseModes){
        this.homeImBaseModes = homeImBaseModes;
        notifyDataSetChanged();
    }

    public List<HomeImBaseMode> getData(){
        return homeImBaseModes;
    }

    class HomeMessageViewHolder extends RecycleBaseViewHolder {
        private ImageView headIcon;
        private View headContain;
        private View headOutLine;
        private TextView title;
        private TextView message;
        private TextView time;
        private Badge badge;
        HomeMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            initView();
        }

        public void initView(){
            headOutLine = itemView.findViewById(R.id.item_home_message_head_bg);
            headContain = itemView.findViewById(R.id.item_home_message_head_contain);
            headIcon = itemView.findViewById(R.id.item_home_message_head_icon);
            title = itemView.findViewById(R.id.item_home_message_name);
            message = itemView.findViewById(R.id.item_home_message_content);
            time = itemView.findViewById(R.id.item_home_message_time);
            badge = new QBadgeView(context).bindTarget(headContain);
            badge.setBadgeGravity(Gravity.TOP | Gravity.END);
            badge.setBadgeTextSize(12, true);
            badge.setBadgePadding(6, true);
            badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                @Override
                public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                    if (dragState == STATE_SUCCEED) {
                        Toast.makeText(context, String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public HomeMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        View view = inflater.inflate(R.layout.item_home_message, parent, false);
        return new HomeMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeMessageViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        HomeImBaseMode homeImBaseMode = homeImBaseModes.get(position);
//        holder.message.setText(parserContentData(homeImBaseMode));
        long time;
        try {
            time = homeImBaseMode.getCreateTime();
            holder.time.setText(TimeUtils.getTimeShowString(time,true));
        }catch (NumberFormatException e){
            holder.time.setText(homeImBaseMode.getCreateTime()+"");
        }
        holder.badge.setBadgeNumber(homeImBaseMode.getUnReadNumber());
        initUserData(homeImBaseMode,holder);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeMessageViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        //局部刷新 https://blog.csdn.net/LVXIANGAN/article/details/91867671

        if (!payloads.isEmpty()){
            Object object = payloads.get(0);
            String title = "";
            String headPic = "";
            if (object instanceof Friend){
                Friend friend = (Friend) object;
                title = UserInfoHelper.getUserDisplayName(friend.uid);
                headPic = UserInfoHelper.getHeadPic(friend.uid);
            }else if (object instanceof GroupInfoBean){
                GroupInfoBean groupInfoBean = (GroupInfoBean) object;
                title = TeamHelper.getTeamName(groupInfoBean.group_id);
                headPic = TeamHelper.getTeamHeadPic(groupInfoBean.group_id);
            }
            holder.title.setText(title);
            GlideUtils.loadHeader(context,headPic,holder.headIcon);
        }

    }

    @Override
    public int getItemCount() {
        return homeImBaseModes.size();
    }

    /**
     * 根据聊天类型解析具体的内容
     */
    private String parserContentData(HomeImBaseMode homeImBaseMode){
        String content = "";
        if (MessageType.SYSTEM.getValue().equals(homeImBaseMode.getMsgType())){
            content = "系统消息";
        }else if (MessageType.TEXT.getValue().equals(homeImBaseMode.getMsgType())){
            MessageTextBean messageTextBean = gson.fromJson(homeImBaseMode.getSourceContent(),MessageTextBean.class);
            if (null != messageTextBean){
                content = messageTextBean.getDescription();
//                content = AESHelper.decryptString(content);
                content = AESUtil.decode(content);
                String friendId = homeImBaseMode.getFromUserId();
                if (!friendId.equals(ModuleMgr.getCenterMgr().getUID())){//对方发的消息
                    content = StringUtils.matcherContent(content);
                }
            }
        }else if (MessageType.VOICE.getValue().equals(homeImBaseMode.getMsgType())){
            content = "[音频]";
        }else if (MessageType.PICTURE.getValue().equals(homeImBaseMode.getMsgType())){
            content = "[图片]";
        }else if (MessageType.FILE.getValue().equals(homeImBaseMode.getMsgType())){
            content = "[文件]";
        }else if (MessageType.VIDEO.getValue().equals(homeImBaseMode.getMsgType())){
            content = "[视频]";
        }else if (MessageType.RED_PACKAGE.getValue().equals(homeImBaseMode.getMsgType())){
            content = "[红包]";
        }else if (MessageType.LOCATION.getValue().equals(homeImBaseMode.getMsgType())){
            content = "[位置]";
        }else if (MessageType.PERSON_CARD.getValue().equals(homeImBaseMode.getMsgType())){
            content = "[名片]";
        }else if (MessageType.EMOJI.getValue().equals(homeImBaseMode.getMsgType())){
            content = "[表情]";
        }else if (MessageType.ANONYMOUS.getValue().equals(homeImBaseMode.getMsgType())){
            content = "[匿名消息]";
        }else{
            content = "[未知消息]";
        }
        return content;
    }

    /**
     * 更新用户数据
     * @param homeImBaseMode
     */
    private void initUserData(HomeImBaseMode homeImBaseMode, HomeMessageViewHolder homeMessageViewHolder){
        String type = homeImBaseMode.getType();
        if (ImType.P2P.getValue().equals(type)){
            String userId = ModuleMgr.getCenterMgr().getUID();
            String friendId = "";
            if (userId.equals(homeImBaseMode.getFromUserId())){
                friendId = homeImBaseMode.getToUserId();
            }else {
                friendId = homeImBaseMode.getFromUserId();
            }
            UserInfoHelper.getUserInfo(friendId, new DbSubscribe<Friend>() {
                @Override
                public void complete(Friend friend) {
                    homeMessageViewHolder.title.setText(UserInfoHelper.getUserDisplayName(friend.uid));
//                    GlideUtil.loadRectHeadByNet(context,friend.headpic,homeMessageViewHolder.headIcon);
                    GlideUtils.loadHeader(context,friend.headpic,homeMessageViewHolder.headIcon);
                }
            });
            if (UserInfoHelper.getUserVipStatus(friendId)){
                homeMessageViewHolder.headOutLine.setVisibility(View.VISIBLE);
            }else {
                homeMessageViewHolder.headOutLine.setVisibility(View.INVISIBLE);
            }
            homeMessageViewHolder.message.setText(parserContentData(homeImBaseMode));
        }else {
            String groupId = homeImBaseMode.getGroupId();
            homeMessageViewHolder.headOutLine.setVisibility(View.INVISIBLE);
            GlideUtils.loadHeader(context,TeamHelper.getTeamHeadPic(groupId),homeMessageViewHolder.headIcon);
            homeMessageViewHolder.title.setText(TeamHelper.getTeamName(groupId));
//            TeamHelper.getTeamMsg(groupId, new DbSubscribe<GroupInfoBean>() {
//                @Override
//                public void complete(GroupInfoBean groupInfoBean) {
//                    Log.e("YM","群消息内容:"+groupInfoBean.toString());
//                    GlideUtils.loadHeader(context,groupInfoBean.group_pic,homeMessageViewHolder.headIcon);
//                    homeMessageViewHolder.title.setText(groupInfoBean.group_name);
//                }
//            });
            String name;
            if (ModuleMgr.getCenterMgr().getUID().equals(homeImBaseMode.getFromUserId())){
                name = "我:";
            }else {
                name = UserInfoHelper.getUserDisplayName(homeImBaseMode.getFromUserId()).concat(":");
            }
            String content = name.concat(parserContentData(homeImBaseMode));
            homeMessageViewHolder.message.setText(content);
        }
    }

}