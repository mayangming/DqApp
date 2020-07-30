package com.wd.daquan.contacts.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.da.library.constant.IConstant;
import com.da.library.tools.Utils;
import com.da.library.widget.CommSearchEditText;
import com.dq.im.type.ImType;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.contacts.presenter.ContactPresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.log.DqToast;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: 方志
 * @Time: 2018/9/13 17:38
 * @Description: 搜索添加好友
 */
public class SearchAddFriendActivity extends DqBaseActivity<ContactPresenter, DataBean> implements View.OnClickListener {

    private static final int REQUEST_CODE_INVITE = 11;
    private static final int CONTACTS_PERMISSION_REQUEST_CODE = 1;
    /**
     * 读取手机联系人权限
     */
    protected String[] mContactsPermission = {Manifest.permission.READ_CONTACTS};
    private EditText mSearchEt;
    private TextView mSearchTv;
    private View mMailListRl;

    @Override
    protected ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_search_add_friend);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initView() {
        CommSearchEditText mSearchLayout = findViewById(R.id.search_add_friend_title_layout);
        mSearchEt = mSearchLayout.getEditText();
        mSearchTv = mSearchLayout.getSearchConfirmTv();
        mMailListRl = findViewById(R.id.mail_list_rl);
    }

    @Override
    protected void initData() {
        showSoftKeyBoard();
    }

    private void showSoftKeyBoard(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.showSoftInput(mSearchEt, 0);
            }
        }, 500);

    }

    @Override
    protected void initListener() {
        mMailListRl.setOnClickListener(this);
        mSearchTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(Utils.isFastDoubleClick(800)) {
            return;
        }
        super.onClick(v);

        if(v.getId() == mSearchTv.getId()) {
            //搜索
            String input = mSearchEt.getText().toString().trim();
            onSearchClick(input);
        }
        switch (v.getId()) {
            case R.id.mail_list_rl:
                if(DqUtils.checkPermissions(this, CONTACTS_PERMISSION_REQUEST_CODE, mContactsPermission)) {
                    NavUtils.gotoInviteMobileContactActivity(this, "2", REQUEST_CODE_INVITE);
                }
                break;
        }
    }

    private void onSearchClick(String input) {
        if(TextUtils.isEmpty(input)) {
            DqToast.showShort(getString(R.string.please_input_phone_number_or_dq_number));
            return;
        }
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.Login.PHONE, input);
        mPresenter.getSearchFriend(DqUrl.url_find_user, hashMap);
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if(null == entity) return;
        if(null == entity.data){
            DqToast.showShort(entity.content);
        }else{
            Friend friend = (Friend)entity.data;
            NavUtils.gotoUserInfoActivity(this, friend.uid, ImType.P2P.getValue(), "",
                    false, false, false, true);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if(null == entity) return;
        DqUtils.bequit(entity, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_INVITE && null != data) {
            String phone = data.getStringExtra(IConstant.Login.PHONE);
            onSearchClick(phone);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CONTACTS_PERMISSION_REQUEST_CODE:
                if (DqUtils.verifyPermissions(grantResults)) {
                    NavUtils.gotoInviteMobileContactActivity(this, "2", REQUEST_CODE_INVITE);
                }
                break;
        }
    }
}
