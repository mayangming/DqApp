package com.wd.daquan.mine.user;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import com.da.library.constant.DirConstants;
import com.da.library.listener.DialogListener;
import com.da.library.tools.FileUtils;
import com.da.library.tools.Utils;
import com.da.library.widget.AnimUtils;
import com.da.library.widget.CommTitle;
import com.meetqs.qingchat.imagepicker.photoview.PhotoView;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.Friend;
import com.wd.daquan.model.bean.UserBean;
import com.wd.daquan.model.db.helper.FriendDbHelper;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.rxbus.MsgMgr;
import com.wd.daquan.model.rxbus.MsgType;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: dukangkang
 * @date: 2018/8/2 10:33.
 * @description: todo ...
 */
public class PersonalAvatarSettingActivity extends DqBaseActivity<MinePresenter, DataBean> implements View.OnClickListener {
    public final String FILE_KEY = "headpic";

    private String[] needPermissions = {Manifest.permission.CAMERA};

    private CommTitle mCommTitle = null;
    private PhotoView mPhotoView = null;
    // 底部弹出选择框
    private Dialog mChooseDialog = null;
    // 权限询问
    private Dialog mDialog = null;

    private File mTmpFile;
    private Uri takePictureUri;
    Uri uriTempFile;
    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.personal_avatar_setting_activity);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initView() {
        mCommTitle = this.findViewById(R.id.head_portrait_commtitle);
        mCommTitle.setTitle(this.getResources().getString(R.string.personal_info_setting_avatar));
        mCommTitle.setRightVisible(View.VISIBLE);
        mCommTitle.setRightImageResource(R.mipmap.title_right_more);
        mPhotoView = this.findViewById(R.id.head_portrait_photoview);
    }

    @Override
    protected void initListener() {
        mCommTitle.getLeftIv().setOnClickListener(this);
        mCommTitle.getRightIv().setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mTmpFile = FileUtils.createFile(this, DirConstants.DIR_CAMERA, "");
        String headPic = ModuleMgr.getCenterMgr().getAvatar();
        GlideUtils.loadHeader(this, headPic, mPhotoView);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mCommTitle.getRightIvId()) {
            showChooseDialog();
        } else if (id == mCommTitle.getLeftIvId()) {
            finish();
        }
    }

    private void showChooseDialog() {
        if (mChooseDialog == null) {
            mChooseDialog = DialogUtils.showBottomDialog(this, KeyValue.ONE, mChooseListener);
        }
        if (!mChooseDialog.isShowing()) {
            mChooseDialog.show();
        }
    }

    /**
     * 裁剪图片
     * @param uri
     */
    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        uriTempFile = Uri.fromFile(mTmpFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTempFile);
        intent.putExtra("noFaceDetection", true);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
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

    /**
     * 上传文件
     * @param file
     */
    private void updateFile(File file) {
        if (!Utils.isNetworkConnected(DqApp.sContext)) {
            DqToast.showShort(getString(R.string.comm_network_error));
            return;
        }

        if(file == null || !file.exists()) {
            DqToast.showShort("文件上传失败");
            return;
        }

//        Map<String, String> hashMap = new LinkedHashMap<>();
//        hashMap.put(FILE_KEY, "");
//        mPresenter.setUserInfoAvatar(DqUrl.url_set_userinfo, hashMap, file, FILE_KEY);
        Map<String, File> hashMap = new LinkedHashMap<>();
        hashMap.put(FILE_KEY, file);
        mPresenter.setUserInfoAvatar(DqUrl.url_upload_headpic, hashMap);
    }

    /**
     * 拍照
     */
    private void takePicture() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri ,和清单文件保持一致
            takePictureUri = FileProvider.getUriForFile(this, "com.wd.daquan.dqprovider", mTmpFile);
        } else {
            takePictureUri = Uri.fromFile(mTmpFile);
        }

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, takePictureUri);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        startActivityForResult(intent, KeyValue.HeadPortrait.TYPE_CAMERA);

        AnimUtils.enterAnimForActivity(activity);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
        Log.e("YM","头像requestCode:"+requestCode);
        if (requestCode == KeyValue.ZERO) {
            if (!DqUtils.verifyPermissions(paramArrayOfInt)) {
                DialogUtils.showSettingQCNumDialog(PersonalAvatarSettingActivity.this, "", getString(R.string.add_authority),
                        getString(R.string.cancel), getString(R.string.chat_bg_set), new DialogListener() {
                            @Override
                            public void onCancel() {
                            }
                            @Override
                            public void onOk() {
                                NavUtils.startAppSettings(PersonalAvatarSettingActivity.this);
                            }
                        });
//                mDialog = DialogUtil.showMessageDialog(this,
//                        null,
//                        getString(R.string.add_authority),
//                        getString(R.string.cancel),
//                        getString(R.string.set_set),
//                        v -> mDialog.dismiss(),
//                        v -> {
//                            mDialog.dismiss();
//                            NavUtils.startAppSettings(PersonalAvatarSettingActivity.this);
//                        });
//                mDialog.show();
            } else {
                takePicture();
            }
        }
    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        super.onSuccess(url, code, entity);
        if (url.contains(DqUrl.url_upload_headpic)) {
            UserBean userEntity = (UserBean) entity.data;

            ModuleMgr.getCenterMgr().saveAvatar(userEntity.headpic);
            MsgMgr.getInstance().sendMsg(MsgType.MT_CENTER_PERSONALINFO_CHANGE, true);
            GlideUtils.load(this, userEntity.headpic, mPhotoView);

            String uid = ModuleMgr.getCenterMgr().getUID();
            Friend friend = FriendDbHelper.getInstance().getFriend(uid);
            if(friend != null){
                friend.headpic = userEntity.headpic;
                FriendDbHelper.getInstance().update(friend, null);
            }
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        DqUtils.bequit(entity, this);
    }

    private DialogUtils.BottomDialogButtonListener mChooseListener = id -> {
        switch (id){
            case R.id.tv_message:
                if (DqUtils.checkPermissions(PersonalAvatarSettingActivity.this, needPermissions)) {
                    takePicture();
                }
                break;
            case R.id.tv_confirm:
                Activity activity = getActivity();
                if (activity == null) {
                    return;
                }
                Log.e("YM","拍照");
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, KeyValue.HeadPortrait.TYPE_PICTURE);
                AnimUtils.enterAnimForActivity(activity);
                break;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == KeyValue.HeadPortrait.TYPE_PICTURE) { // 相册
            if (null != intent.getData()) {
                crop(intent.getData());
            }
        } else if (requestCode == KeyValue.HeadPortrait.TYPE_CAMERA) { // 拍照
            crop(takePictureUri);
        } else if (requestCode == KeyValue.HeadPortrait.TYPE_CROP) { // 裁剪
            File file = null;
            try{
                file = new File(new URI(uriTempFile.toString()));
            }catch (URISyntaxException e){
                e.printStackTrace();
            }
            //Log.e("TAG", "path ： " + file.getPath() + "," + file.exists());
            updateFile(file);
        }
    }
}
