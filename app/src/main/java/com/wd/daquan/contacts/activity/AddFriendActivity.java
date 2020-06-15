package com.wd.daquan.contacts.activity;

import android.Manifest;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.contacts.presenter.ContactPresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;

/**
 * @Author: 方志
 * @Time: 2018/9/13 15:28
 * @Description: 添加朋友页面
 */
public class AddFriendActivity extends DqBaseActivity<ContactPresenter, DataBean> implements View.OnClickListener {


    private static final int CONTACTS_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    private TextView mSearchTv;
    private TextView mQrcodeTv;
//    private LinearLayout mContactsLl;
    private LinearLayout mScanLl;
//    private LinearLayout mWXLl;
    private LinearLayout mPhoneLl;
    private ShareAction mShareAction;

    /**
     * 相机权限
     */
    protected String[] mCameraPermission = {Manifest.permission.CAMERA};
    /**
     * 读取手机联系人权限
     */
    protected String[] mContactsPermission = {Manifest.permission.READ_CONTACTS};

    @Override
    protected ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_add_friend);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initView() {
        mSearchTv = findViewById(R.id.add_friend_search_tv);
        mQrcodeTv = findViewById(R.id.add_friend_qrcode);
//        mContactsLl = findViewById(R.id.add_friend_contacts_ll);
        mScanLl = findViewById(R.id.add_friend_scan_ll);
//        mWXLl = findViewById(R.id.add_friend_wx_ll);
        mPhoneLl = findViewById(R.id.add_friend_phone_ll);
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void initListener() {
        toolbarBack();
        mSearchTv.setOnClickListener(this);
        mQrcodeTv.setOnClickListener(this);
//        mContactsLl.setOnClickListener(this);
        mScanLl.setOnClickListener(this);
//        mWXLl.setOnClickListener(this);
        mPhoneLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (DqUtils.isFastDoubleClick(500)) {
            return;
        }
        switch (v.getId()) {
            case R.id.add_friend_search_tv:
                //QcToastUtil.showToast(this, "搜索好友");
                NavUtils.gotoSearchAddFriendActivity(this);
                break;
            case R.id.add_friend_qrcode:
                //QcToastUtil.showToast(this, "我的二维码");
                NavUtils.gotoQRCodeActivity(getActivity());
                break;
//            case R.id.add_friend_contacts_ll:
//                //QcToastUtil.showToast(this, "手机通讯录匹配");
//                DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
//                if(DqUtils.checkPermissions(this, CONTACTS_REQUEST_CODE, mContactsPermission)) {
//                    NavUtils.gotoMobileContactsListActivity(this);
//                }
//                break;
            case R.id.add_friend_scan_ll:
                //QcToastUtil.showToast(this, "扫一扫");
                if(DqUtils.checkPermissions(this, CAMERA_REQUEST_CODE, mCameraPermission)) {
                    NavUtils.gotoScanQRCodeActivity(this);
                }
                break;
//            case R.id.add_friend_wx_ll:
                //QcToastUtil.showToast(this, "邀请微信联系人");
//                DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
//                mShareAction = ShareUtil.openWEIXINShare(this, DqUrl.url_weChat_share,
//                        getString(R.string.app_name), getString(R.string.we_chat_share), DqUrl.url_CN_logo);
//                break;
            case R.id.add_friend_phone_ll:
                DqToast.showLong("邀请手机联系人");
                if(DqUtils.checkPermissions(this, CONTACTS_REQUEST_CODE, mContactsPermission)) {
                    NavUtils.gotoInviteMobileContactActivity(this, "1", -1);
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mShareAction) {
            mShareAction.close();
            mShareAction = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CONTACTS_REQUEST_CODE:
                if (DqUtils.verifyPermissions(grantResults)) {
                    NavUtils.gotoInviteMobileContactActivity(this, "1", -1);
                }
                break;
            case CAMERA_REQUEST_CODE:
                if (DqUtils.verifyPermissions(grantResults)) {
                    NavUtils.gotoRegisterActivity(this);
                }
                break;
        }
    }
}
