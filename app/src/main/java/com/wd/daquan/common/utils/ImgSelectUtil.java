package com.wd.daquan.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.FileProvider;
import com.wd.daquan.DqApp;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.log.DqToast;
import com.da.library.tools.FileUtils;
import com.da.library.widget.AnimUtils;

import java.io.File;

/**
 * 图片选择工具类
 * Created by Kind on 2019/4/10.
 */
public class ImgSelectUtil {

    // 选择拍照
    public static final int TYPE_CAMERA = 1;
    // 选择相册
    public static final int TYPE_PICTURE = 2;
    // 图片裁剪
    public static final int TYPE_CROP = 3;

    private static File mTmpFile = FileUtils.createImg(DqApp.sContext);

    protected String[] needPermissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,};

    private static ImgSelectUtil imgSelectUtil = null;

    public static ImgSelectUtil getInstance() {
        if (imgSelectUtil == null) {
            imgSelectUtil = new ImgSelectUtil();
        }
        return imgSelectUtil;
    }

    /**
     * 相册
     *
     * @param activity
     */
    public void openPicture(FragmentActivity activity) {
        if (activity == null) {
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(intent, TYPE_PICTURE);
//            AnimUtils.enterAnimForActivity(activity);
        } catch (Exception e) {
            e.printStackTrace();
            DqToast.showShort("打开图片库错误!");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent, Activity activity, OnCompleteListener completeListener) {
        onActivityResult(requestCode, resultCode, intent, activity, true, completeListener);
    }

    /**
     * 是否裁剪，默认为true
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     * @param activity
     * @param isCrop           是否裁剪，默认是裁剪
     * @param completeListener
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent, Activity activity, boolean isCrop, OnCompleteListener completeListener) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case TYPE_PICTURE: {// 相册
                if (null == intent.getData()) {
                    DqToast.showShort("相册选取出错，请重拍！");
                    break;
                }
                if (!isCrop) {
                    if (completeListener != null) {
                        completeListener.onComplete(requestCode, intent.getData().toString());
                    }
                    break;
                }
                crop(activity, intent.getData(), Uri.fromFile(mTmpFile));
                break;
            }
            case TYPE_CROP: {// 裁剪
                if (completeListener != null) {
                    completeListener.onComplete(requestCode, mTmpFile.getAbsolutePath());
                }
                break;
            }
        }
    }

    /**
     * 裁剪图片
     *
     * @param uri
     * @param targetUri
     */
    private void crop(Activity activity, Uri uri, Uri targetUri) {
        try {
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
            activity.startActivityForResult(intent, TYPE_CROP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnCompleteListener {
        void onComplete(int requestCode, String path);
    }
}

