package com.wd.daquan.third.fragment;

import android.app.Activity;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dq.im.model.HomeImBaseMode;
import com.dq.im.type.ImType;
import com.dq.im.viewmodel.HomeMessageViewModel;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.wd.daquan.R;
import com.wd.daquan.imui.adapter.HomeMessageAdapter;
import com.wd.daquan.imui.adapter.RecycleItemOnClickListener;
import com.wd.daquan.imui.adapter.RecycleItemOnLongClickListener;
import com.wd.daquan.imui.dialog.DeleteHomeMsgDialog;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.GroupInfoBean;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.third.reminder.ReminderManager;
import com.wd.daquan.third.session.SessionHelper;
import com.wd.daquan.view.CenterLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页消息列表
 */
public class ConversationListFragment extends Fragment implements QCObserver {

    public static final String TAG = "qc_log";

    // view
    private RecyclerView recyclerView;

    private View emptyBg;

    private TextView emptyHint;

    //    private RecentContactAdapter adapter;

    private UserInfoObserver userInfoObserver;
    private HomeMessageAdapter homeMessageAdapter;
    private HomeMessageViewModel homeMessageViewModel;
    private List<HomeImBaseMode> homeImBaseModes = new ArrayList<>();
    int scrollTargetIndex = 0;//上次滑动定位的未读消息位置
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        initListener();
        initVerticalRecycleView(recyclerView);
        initRecycleData();
        initViewModel();
        queryHomeMessage();
        MsgMgr.getInstance().attach(this);
    }
    private CenterLayoutManager linearLayoutManager;
    protected void initVerticalRecycleView(RecyclerView recyclerView){
        linearLayoutManager = new CenterLayoutManager(recyclerView.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    scrollTargetIndex = 0;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        registerForContextMenu(recyclerView);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recent_contacts_fragment, container, false);
    }

    private void notifyDataSetChanged() {
//        adapter.notifyDataSetChanged();
        boolean empty = homeImBaseModes.isEmpty();
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
        emptyHint.setHint("还没有会话，在通讯录中找个人聊聊吧！");
    }
    public void goChatUnReadMessage(){
        boolean isHasUnReadMessage = false;
        if (null != homeImBaseModes && !homeImBaseModes.isEmpty()){//假如集合不为空
            for (int i = scrollTargetIndex; i < homeImBaseModes.size(); i++){
                HomeImBaseMode model = homeImBaseModes.get(i);
                if (model.getUnReadNumber() > 0){
                    isHasUnReadMessage = true;
                    scrollTargetIndex = i;
                    break;
                }
            }
            if (isHasUnReadMessage)
                linearLayoutManager.smoothScrollToPosition(recyclerView,new RecyclerView.State(),scrollTargetIndex);

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }

    /**
     * 查找页面控件
     */
    private void findViews() {
        recyclerView = getView().findViewById(R.id.recycler_view);
        emptyBg = getView().findViewById(R.id.emptyBg);
        emptyHint = getView().findViewById(R.id.message_list_empty_hint);
    }

    private void initListener() {
    }

    private void initRecycleData(){
        homeMessageAdapter = new HomeMessageAdapter(this);
        homeMessageAdapter.setRecycleItemOnClickListener(new RecycleItemOnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startChat(position);
            }
        });
        homeMessageAdapter.setRecycleItemOnLongClickListener(new RecycleItemOnLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
//               homeMessageViewModel.deleteForServerMessageId(homeImBaseModes.get(position).getMsgIdServer());
                Bundle bundle = new Bundle();
                bundle.putSerializable(DeleteHomeMsgDialog.MSG,homeImBaseModes.get(position));
                DeleteHomeMsgDialog deleteHomeMsgDialog = new DeleteHomeMsgDialog();
                deleteHomeMsgDialog.setArguments(bundle);
                deleteHomeMsgDialog.show(getChildFragmentManager(),"");
                return false;
            }
        });
        recyclerView.setAdapter(homeMessageAdapter);
    }

    private void initViewModel(){
        homeMessageViewModel = ViewModelProviders.of(this).get(HomeMessageViewModel.class);
    }

    private void queryHomeMessage(){
        homeMessageViewModel.getAllMessage().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<List<HomeImBaseMode>>() {
            @Override
            public void onChanged(@Nullable List<HomeImBaseMode> homeImBaseModesTemp) {
                scrollTargetIndex = 0;
                Log.e("YM","首页数据数量:"+homeImBaseModesTemp.size());
                Log.e("YM","首页数据内容:"+homeImBaseModesTemp.toString());
                homeImBaseModes.clear();
                homeImBaseModes.addAll(homeImBaseModesTemp);
                homeMessageAdapter.setData(homeImBaseModes);
                notifyDataSetChanged();
                int unReadNumber = 0;
                for (HomeImBaseMode mode : homeImBaseModesTemp){
                    unReadNumber += mode.getUnReadNumber();
                }
                ReminderManager.getInstance().updateSessionUnreadNum(unReadNumber);
            }
        });
    }

    private void startChat(int position){
        HomeImBaseMode homeImBaseMode = homeImBaseModes.get(position);
        if (ImType.Team == ImType.typeOfValue(homeImBaseMode.getType())){
            startTeamChat(homeImBaseMode);
        }else {
            startP2PChat(homeImBaseMode);
        }
    }
    private void startP2PChat(HomeImBaseMode homeImBaseMode){
        String userId = ModuleMgr.getCenterMgr().getUID();
        String friendId = "";
        if (userId.equals(homeImBaseMode.getFromUserId())){
            friendId = homeImBaseMode.getToUserId();
        }else {
            friendId = homeImBaseMode.getFromUserId();
        }
        SessionHelper.startP2PSession(getActivity(), friendId);
    }

    private void startTeamChat(HomeImBaseMode homeImBaseMode){
        SessionHelper.startTeamSession(getActivity(), homeImBaseMode.getGroupId());
    }


    @Override
    public void onMessage(String key, Object value) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        switch (key){
            case MsgType.MT_FRIEND_REMOVE_FRIEND:
                String userId = (String) value;
//                homeMessageViewModel.deleteForFriendId(userId);
                break;
            case MsgType.HOME_UPDATE_MSG://更新消息内容，比如头像、昵称
                findHomeListIndex(value);
                break;

        }
    }

    /**
     * 根据修改的内容确定列表位置，进行动态更新
     * @param object
     */
    private void findHomeListIndex(Object object){
        List<HomeImBaseMode> modes = homeMessageAdapter.getData();
        int index = -1;
        if (object instanceof Friend){//个人消息
            Friend friend = (Friend) object;
            for (HomeImBaseMode mode : modes){
                ImType imType = ImType.typeOfValue(mode.getType());
                if (imType == ImType.P2P){
                    if (friend.uid.equals(mode.getFromUserId()) || friend.uid.equals(mode.getToUserId())){
                        index = modes.indexOf(mode);
                        break;
                    }
                }
            }
        }else if (object instanceof GroupInfoBean){//群组消息
            GroupInfoBean friend = (GroupInfoBean) object;
            for (HomeImBaseMode mode : modes){
                ImType imType = ImType.typeOfValue(mode.getType());
                if (imType == ImType.Team){
                    if (friend.group_id.equals(mode.getGroupId())){
                        index = modes.indexOf(mode);
                        break;
                    }
                }
            }
        }
        if (-1 == index){//没有找到数据
            return;
        }
        homeMessageAdapter.notifyItemChanged(index,object);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }
}
