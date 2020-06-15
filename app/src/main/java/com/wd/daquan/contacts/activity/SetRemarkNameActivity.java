package com.wd.daquan.contacts.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.da.library.constant.IConstant;
import com.da.library.controls.custombuttom.CustomButtomDialog;
import com.da.library.controls.custombuttom.bean.CustomButtom;
import com.da.library.dialog.LoadingDialog;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.alioss.AliOSS;
import com.wd.daquan.common.alioss.AliOssHelper;
import com.wd.daquan.common.bean.CNOSSFileBean;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.ImgSelectUtil;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.contacts.presenter.ContactPresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/18 13:31
 * @Description: 设置好友备注
 */
public class SetRemarkNameActivity extends DqBaseActivity<ContactPresenter, DataBean>
        implements View.OnClickListener, CustomButtomDialog.OnItemListener {

    private EditText mNameEt;
    private ImageView mNameClearIv;
//    private EditText remarkPhoneEt, remarkDescEt;
//    private ImageView mNameClearIv, mPhoneClearIv, remarkDescClearIv, setRemarkImg;
//    private TextView mDescTextCountTv;
//    private CustomFrameLayout setRemarkFrame;

    private int mSessionType; // 群组1/个人0
    private String mUserId;
    private String mNickName;
    private String mGroupId;

    private String mRemarkName;
//    private String mRemarkDesc;

//    private ArrayList<String> mPhones = new ArrayList<>();
//    private String mDescriptions, mCard;


    private String localUrl = null;
    private String webHttpUrl = null;

    private boolean isClickConfirm = false;//是否保存

    @Override
    protected ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_set_remark);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mNameEt = findViewById(R.id.remark_name_et);
        mNameClearIv = findViewById(R.id.remark_name_clear_iv);

//        remarkPhoneEt = findViewById(R.id.remark_phone_et);
//        mPhoneClearIv = findViewById(R.id.remark_phone_clear_iv);
//
//        remarkDescEt = findViewById(R.id.remark_desc_et);
//        remarkDescClearIv = findViewById(R.id.remark_desc_clear_iv);
//        mDescTextCountTv = findViewById(R.id.remark_desc_text_count_tv);
//
//
//        setRemarkFrame = findViewById(R.id.set_remark_frame);
//        setRemarkImg = findViewById(R.id.set_remark_img);
    }

    @Override
    protected void initData() {
        getIntentData();
        initTitle();

        if(!TextUtils.isEmpty(mNickName)){
            mNameClearIv.setVisibility(View.VISIBLE);
            mNameEt.setText(mNickName);
        }else{
            mNameClearIv.setVisibility(View.GONE);
            mNameEt.setText("");
        }

//        String phoneNumber = "";
//        if (mPhones.size() > 0) {
//            phoneNumber = mPhones.get(0);
//        }

        mNameEt.setText(mNickName);
        mNameClearIv.setVisibility(TextUtils.isEmpty(mNickName) ? View.GONE : View.VISIBLE);
//        remarkPhoneEt.setText(phoneNumber);
//        remarkDescEt.setText(mDescriptions);
//        mPhoneClearIv.setVisibility(TextUtils.isEmpty(phoneNumber) ? View.GONE : View.VISIBLE);
//        mDescTextCountTv.setText(mDescriptions.length() + "/400");
//        //mDescClearIv.setVisibility(descriptions.length() == 0 ? View.GONE : View.VISIBLE);
//
//        webHttpUrl = mCard;
//
//        if (!TextUtils.isEmpty(webHttpUrl)) {
//            setRemarkFrame.show(R.id.set_remark_img);
//            GlideUtils.load(this, webHttpUrl, setRemarkImg);
//        } else {
//            setRemarkFrame.show(R.id.set_remark_lin);
//        }
    }

    private void initTitle() {
//        mTitleLayout.setTitle(getString(R.string.set_remark));
//        mTitleLayout.getRightTv().setVisibility(View.VISIBLE);
//        mTitleLayout.getRightTv().setText(getString(R.string.save));
//        mTitleLayout.getRightTv().setTextColor(Color.WHITE);
    }

    private void getIntentData() {
        mSessionType = getIntent().getIntExtra(IConstant.UserInfo.SESSION_TYPE, -1);
        mUserId = getIntent().getStringExtra(IConstant.UserInfo.UID);
        mNickName = getIntent().getStringExtra(IConstant.UserInfo.UNREMARK);
        mGroupId = getIntent().getStringExtra(IConstant.UserInfo.GROUP_ID);

//        mPhones = getIntent().getStringArrayListExtra(KeyValue.Remark.PHONES);
//        mDescriptions = getIntent().getStringExtra(KeyValue.Remark.DESCRIPTIONS);
//        mCard = getIntent().getStringExtra(KeyValue.Remark.CARD);
    }


    @Override
    protected void initListener() {
        toolbarBack();
        toolbarRightTvOnClick(view -> onSava());
//        mTitleLayout.getLeftIv().setOnClickListener(this);
//        mTitleLayout.getRightTv().setOnClickListener(this);
        mNameEt.addTextChangedListener(mTextWatcher);
        mNameClearIv.setOnClickListener(this);
//        findViewById(R.id.set_remark_lin).setOnClickListener(this);
//        mPhoneClearIv.setOnClickListener(this);
//        setRemarkImg.setOnClickListener(this);
    }

    /**
     * 保存
     */
    private void onSava() {
        //保存
//        if (TextUtils.isEmpty(localUrl)) {
//            saveRemarkData();
//        } else {
//            //uploadImg();
//        }
        saveRemarkData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remark_name_clear_iv:
                //清空备注名
                mNameEt.setText("");
                mNameClearIv.setVisibility(View.GONE);
                break;
//            case R.id.remark_phone_clear_iv:
//                //清空手机号码
//                remarkPhoneEt.setText("");
//                mPhoneClearIv.setVisibility(View.GONE);
//                break;
////            case R.id.remark_desc_clear_iv:
////                //清空描述
////                mDescEt.setText("");
////                mDescClearIv.setVisibility(View.GONE);
////                break;
//            case R.id.set_remark_lin:
//                CustomButtomDialog customButtomDialog = new CustomButtomDialog();
//
//                List<CustomButtom> buttoms = new ArrayList<>();
//                buttoms.add(new CustomButtom(0, "拍照"));
//                buttoms.add(new CustomButtom(1, "从手机相册选择"));
//                customButtomDialog.setData(buttoms, true);
//                customButtomDialog.setOnItemListener(this);
//                customButtomDialog.showDialog((FragmentActivity) getActivity());
//                break;
//            case R.id.set_remark_img:
//                if ((TextUtils.isEmpty(localUrl) && TextUtils.isEmpty(webHttpUrl))) {
//                    return;
//                }
//
//                NavUtils.gotoRemarksImgZoomAct(this, mUserId, TextUtils.isEmpty(localUrl) ? webHttpUrl : localUrl);
//                break;
        }
    }

    @Override
    public void onDialogItemClick(AdapterView<?> parent, View view, int position, long id) {
        CustomButtom customButtom = (CustomButtom) parent.getAdapter().getItem(position);
        switch (customButtom.id) {
            case 0:
                ImgSelectUtil.getInstance().openCameraAct(this, ModuleMgr.getCenterMgr().getUID());
                break;
            case 1:
                ImgSelectUtil.getInstance().openPicture(this);
                break;
        }
    }

    @Override
    public void onDialogClick(View v) {

    }

    /**
     * 上传图片
     */
    private void uploadImg() {
        LoadingDialog.show(this, "");
        AliOssHelper mAliOssHelper = new AliOssHelper();
        mAliOssHelper.setPath(localUrl);
        mAliOssHelper.setCallback(new AliOSS.AliOssCallback() {
            @Override
            public void onProgress(long curSize, long totalSize) {

            }

            @Override
            public void onSuccess(long totalSize, File file, CNOSSFileBean bean) {
                MsgMgr.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadingDialog.closeLoadingDialog();
//                        LoadingHelper.getInstance(SetRemarkNameActivity.this).destroy();
                        if (!TextUtils.isEmpty(bean.fileName)) {
                            localUrl = null;
                            webHttpUrl = bean.host + bean.fileName;
                            saveRemarkData();
                        } else {
                            DqToast.showShort("保存失败，请重试！");
                        }
                    }
                });
            }

            @Override
            public void onFailure() {
                MsgMgr.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadingDialog.closeLoadingDialog();
                        DqToast.showShort("保存失败，请重试！");
                    }
                });

            }
        });
        mAliOssHelper.uploadFile();
    }

    private void saveRemarkData() {
        mRemarkName = mNameEt.getText().toString().trim();
//        String remarkPhone = remarkPhoneEt.getText().toString().trim();
//        if(mPhones == null){
//            mPhones = new ArrayList<>();
//        }
//        mPhones.clear();
//        mPhones.add(remarkPhone);
//        mRemarkDesc = remarkDescEt.getText().toString().trim();
//
        Map<String, String> hashMap = new HashMap<>();
//        String type;
//        if(mSessionType == 1){//判断是修改备注还是修改群组内的昵称 1群组/2个人
//            hashMap.put(IConstant.Login.TYPE, "3");
//            type = IConstant.UserInfo.GROUP_ID;
//        }else {
//            hashMap.put(IConstant.Login.TYPE, "1");
//            type = IConstant.UserInfo.TO_UID;
//        }
        hashMap.put(KeyValue.Remark.TYPE, "1");

        hashMap.put(KeyValue.Remark.TO_UID, mUserId);
        hashMap.put(KeyValue.Remark.REMARKS, mRemarkName);
//        hashMap.put(KeyValue.Remark.PHONES, GsonUtils.toJson(mPhones));
//        hashMap.put(KeyValue.Remark.DESCRIPTIONS, mRemarkDesc);
//
//        if (!TextUtils.isEmpty(webHttpUrl)) {
//            hashMap.put(KeyValue.Remark.CARD, webHttpUrl);
//        }

        mPresenter.setRemarkName(DqUrl.url_set_remark, hashMap);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length()==0){
                mNameClearIv.setVisibility(View.GONE);
            }else{
                mNameClearIv.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (null == entity) return;
        if(entity.isSuccess()){
            if (DqUrl.url_set_remark.equals(url)) {
//                DqToast.showShort(entity.content);

                String userName = mNameEt.getText().toString();

                Intent intent = new Intent();
                intent.putExtra(KeyValue.Remark.REMARKS, userName);
//                intent.putExtra(KeyValue.Remark.PHONES, mPhones);
//                intent.putExtra(KeyValue.Remark.DESCRIPTIONS, mRemarkDesc);
//                intent.putExtra(KeyValue.Remark.CARD, webHttpUrl);

                setResult(RESULT_OK, intent);
                //数据库存数据
                Friend friend = FriendDbHelper.getInstance().getFriend(mUserId);
                friend.friend_remarks = userName;
                FriendDbHelper.getInstance().update(friend, null);
                //更新用户备注通知
                MsgMgr.getInstance().sendMsg(MsgType.MT_FRIEND_REMARKS_CHANGE, userName);

                finish();
            }
        }else {
            DqToast.showShort(entity.content);
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if(null == entity) return;
        DqUtils.bequit(entity, this);
    }

    @Override
    public void finish() {
//        String mName = mNameEt.getText().toString().trim();
//        String mPhone = remarkPhoneEt.getText().toString().trim();
//        String mDesc = remarkDescEt.getText().toString().trim();

//        String phoneNumber = "";
//        if (mPhones.size() > 0) {
//            phoneNumber = mPhones.get(0);
//        }

//        boolean isNameModify = !mName.equals(mNickName);
//        boolean isPhoneModify = !mPhone.equals(phoneNumber);
//        boolean isDescModify = !mDesc.equals(mDescriptions);
//
//        String url = "";
//        if (TextUtils.isEmpty(localUrl)) {
//            if (!TextUtils.isEmpty(webHttpUrl)) {
//                url = webHttpUrl;
//            }
//        } else {
//            url = localUrl;
//        }
//
//        boolean isUrlModify = false;
//        if (!TextUtils.isEmpty(url) || !TextUtils.isEmpty(mCard)) {
//            isUrlModify = !url.equals(mCard);
//        }

//        if (!isClickConfirm && (isNameModify
////                || isPhoneModify || isDescModify
//                || isUrlModify)) {
//            showNoSetingDialog();
//        } else {
//            super.finish();
//        }
        super.finish();
    }

    /**
     * 放弃编辑的dialog
     */
    private Dialog mCancleEditDialog = null;

//    /**
//     * 未编辑
//     */
//    private void showNoSetingDialog() {
//        if (mCancleEditDialog == null) {
//            mCancleEditDialog = DialogUtils.showPurseDialog(this, KeyValue.NINE, null, mCancleEditListener);
//        }
//
//        if (!mCancleEditDialog.isShowing()) {
//            mCancleEditDialog.show();
//        }
//    }
//
//    /**
//     * 放弃编辑的dialog监听器
//     */
//    private DialogUtils.BottomDialogListener mCancleEditListener = new DialogUtils.BottomDialogListener() {
//
//        @Override
//        public void onClick(int type) {
//            switch (type){
//                case R.id.tv_cancel:
//                    isClickConfirm = true;
//                    SetRemarkNameActivity.this.finish();
//                    break;
//
//                case R.id.tv_confirm:
//                    isClickConfirm = true;
//                    onSava();
//                    break;
//            }
//        }
//    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
//        ImgSelectUtil.getInstance().onActivityResult(requestCode, resultCode, intent, this, new ImgSelectUtil.OnCompleteListener() {
//            @Override
//            public void onComplete(int requestCode, String path) {
//                if (KeyValue.HeadPortrait.TYPE_CROP == requestCode) {
//                    GlideUtils.load(SetRemarkNameActivity.this, path, setRemarkImg, R.mipmap.user_avatar, R.mipmap.user_avatar);
//                    setRemarkFrame.show(R.id.set_remark_img);
//                    localUrl = path;
//                }
//            }
//        });
//        if (resultCode != Activity.RESULT_OK) {
//            return;
//        }
//        if (requestCode == KeyValue.HeadPortrait.TYPE_DEL_IMG) {//删除图片
//            setRemarkFrame.show(R.id.set_remark_lin);
//            localUrl = null;
//            webHttpUrl = null;
//        }
    }
}
