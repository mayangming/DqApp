package com.wd.daquan.login.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wd.daquan.R;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.da.library.constant.DirConstants;
import com.da.library.constant.IConstant;
import com.wd.daquan.glide.GlideUtils;
import com.da.library.listener.DialogListener;
import com.wd.daquan.model.bean.LoginBean;
import com.da.library.tools.FileUtils;
import com.da.library.widget.AnimUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 方志
 * @Time: 2018/9/11 16:42
 * @Description: 设置用户密码
 */
public class SetUserInfoActivity extends BaseLoginActivity{

    private ImageView mPortraitIv;
    private EditText mNicknameEt;
    private Button mNextStepBtn;
    private Dialog mChooseDialog;
    private String[] needPermissions = {Manifest.permission.CAMERA};
    private File mTmpFile;
    private boolean hasPortrait = true;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_set_user_info);
    }

    @Override
    protected void initView() {
        mTitleLayout = findViewById(R.id.set_user_info_comm_title_layout);
        mScrollView = findViewById(R.id.scroll_view);
        mTranslationContainerRl = findViewById(R.id.translation_container_rl);
        mPortraitIv = findViewById(R.id.set_user_info_portrait_iv);
        mNicknameEt = findViewById(R.id.set_user_info_nickname_et);
        mNextStepBtn = findViewById(R.id.set_user_info_next_step_btn);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mPortraitIv.setOnClickListener(this);
        mNextStepBtn.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        initTitle();
        mTmpFile = FileUtils.createFile(this, DirConstants.DIR_CAMERA, "");
    }

    private void initTitle() {
        mTitleLayout.setTitleLayoutBackgroundColor(Color.WHITE);
        mTitleLayout.setTitleTextColor(getResources().getColor(R.color.text_blue));

        mTitleLayout.setTitle(getString(R.string.user_info));
        mTitleLayout.setRightVisible(View.GONE);
        mTitleLayout.setLeftGone();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_user_info_portrait_iv:
                DqToast.showShort("头像");
                setPortrait();
                break;
            case R.id.set_user_info_next_step_btn:
//                QcToastUtil.showToast(this, "头像");
                nextStep();
                break;
        }
    }

    private void setPortrait() {
        if (mChooseDialog == null) {
            mChooseDialog = DialogUtils.showBottomDialog(this, KeyValue.ONE, mChooseListener);
        }
        if (!mChooseDialog.isShowing()) {
            mChooseDialog.show();
        }
    }

    private DialogUtils.BottomDialogButtonListener mChooseListener = id -> {
        switch (id){
            case R.id.tv_message:
                if (DqUtils.checkPermissions(SetUserInfoActivity.this, needPermissions)) {
                    takePicture();
                }
                break;
            case R.id.tv_confirm:
                Activity activity = getActivity();
                if (activity == null) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, KeyValue.HeadPortrait.TYPE_PICTURE);
                AnimUtils.enterAnimForActivity(activity);
                break;
        }
    };

    /**
     * 拍照
     */
    private void takePicture() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        Uri targetUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri ,和清单文件保持一致
            targetUri = FileProvider.getUriForFile(this, "com.wd.daquan.dqprovider", mTmpFile);
        } else {
            targetUri = Uri.fromFile(mTmpFile);
        }

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        startActivityForResult(intent, KeyValue.HeadPortrait.TYPE_CAMERA);

        AnimUtils.enterAnimForActivity(activity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == KeyValue.HeadPortrait.TYPE_PICTURE) { // 相册
            if (null != intent.getData()) {
                crop(intent.getData(), Uri.fromFile(mTmpFile));
            }
        } else if (requestCode == KeyValue.HeadPortrait.TYPE_CAMERA) { // 拍照
            Uri originalUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //通过FileProvider创建一个content类型的Uri ,和清单文件保持一致
                originalUri = FileProvider.getUriForFile(this, "com.wd.daquan.dqprovider", mTmpFile);
            } else {
                originalUri = Uri.fromFile(mTmpFile);
            }
            crop(originalUri, Uri.fromFile(mTmpFile));
        } else if (requestCode == KeyValue.HeadPortrait.TYPE_CROP) { // 裁剪
            if(null != mTmpFile){
                GlideUtils.loadCircle(this, mTmpFile, mPortraitIv);
                hasPortrait = false;
            }
            //nextStep();
        }
    }


    /**
     * 裁剪图片
     */
    private void crop(Uri uri, Uri targetUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG);
        intent.putExtra("return-intentUrl", true);
        startActivityForResult(intent, KeyValue.HeadPortrait.TYPE_CROP);
    }

    private void nextStep() {
        String nickname = mNicknameEt.getText().toString();//昵称

        if(TextUtils.isEmpty(nickname)) {
            DqToast.showShort("用户昵称不能为空");
            return;
        }
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put(IConstant.UserInfo.NICKNAME, nickname);
        //Log.e("TAG", "hashMap : " + hashMap.toString());
        if(hasPortrait) {
            mPresenter.setUserInfo(DqUrl.url_set_userinfo, hashMap);
        }else {
            mPresenter.setUserInfo(DqUrl.url_set_userinfo, hashMap, IConstant.UserInfo.HEADPIC, mTmpFile);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);

        if (requestCode == KeyValue.ZERO) {
            if (!DqUtils.verifyPermissions(paramArrayOfInt)) {
                DialogUtils.showSettingQCNumDialog(SetUserInfoActivity.this, "", getString(R.string.add_authority),
                        getString(R.string.cancel), getString(R.string.chat_bg_set), new DialogListener() {
                            @Override
                            public void onCancel() {
                            }
                            @Override
                            public void onOk() {
                                NavUtils.startAppSettings(SetUserInfoActivity.this);
                            }
                        });
            } else {
                takePicture();
            }
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if(null == entity) return;
        if(IConstant.OK.equals(entity.status)) {
            if(DqUrl.url_set_userinfo.equals(url)) {
                LoginBean loginBean = (LoginBean) entity.data;
                EBSharedPrefUser userInfoSp = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo();
                userInfoSp.saveString(EBSharedPrefUser.headpic, loginBean.headpic);
                userInfoSp.saveString(EBSharedPrefUser.nickname, loginBean.nickName);
                DqToast.showShort(getString(R.string.set_success));
                NavUtils.gotoSetPassword(this);
                finish();
            }
        }
    }
}
