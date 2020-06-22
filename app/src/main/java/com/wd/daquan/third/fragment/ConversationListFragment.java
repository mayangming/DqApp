package com.wd.daquan.third.fragment;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;
import com.wd.daquan.model.rxbus.QCObserver;
import com.wd.daquan.third.session.SessionHelper;

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
    protected void initVerticalRecycleView(RecyclerView recyclerView){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
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
        homeMessageViewModel.getAllMessage().observe(getViewLifecycleOwner(), new android.arch.lifecycle.Observer<List<HomeImBaseMode>>() {
            @Override
            public void onChanged(@Nullable List<HomeImBaseMode> homeImBaseModesTemp) {
                Log.e("YM","首页数据数量:"+homeImBaseModesTemp.size());
                Log.e("YM","首页数据内容:"+homeImBaseModesTemp.toString());
                homeImBaseModes.clear();
                homeImBaseModes.addAll(homeImBaseModesTemp);
                homeMessageAdapter.setData(homeImBaseModes);
                notifyDataSetChanged();
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
                homeMessageViewModel.deleteForUserId(userId);
                break;
        }
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
