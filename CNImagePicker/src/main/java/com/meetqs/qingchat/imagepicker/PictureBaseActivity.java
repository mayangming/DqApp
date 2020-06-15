package com.meetqs.qingchat.imagepicker;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.meetqs.qingchat.imagepicker.compress.Luban;
import com.meetqs.qingchat.imagepicker.compress.OnCompressListener;
import com.meetqs.qingchat.imagepicker.config.PictureConfig;
import com.meetqs.qingchat.imagepicker.config.PictureMimeType;
import com.meetqs.qingchat.imagepicker.config.PictureSelectionConfig;
import com.meetqs.qingchat.imagepicker.dialog.PictureDialog;
import com.meetqs.qingchat.imagepicker.entity.EventEntity;
import com.meetqs.qingchat.imagepicker.entity.LocalMedia;
import com.meetqs.qingchat.imagepicker.entity.LocalMediaFolder;
import com.meetqs.qingchat.imagepicker.immersive.ImmersiveManage;
import com.meetqs.qingchat.imagepicker.rxbus2.RxBus;
import com.meetqs.qingchat.imagepicker.tools.AttrsUtils;
import com.meetqs.qingchat.imagepicker.tools.DateUtils;
import com.meetqs.qingchat.imagepicker.tools.DoubleUtils;
import com.meetqs.qingchat.imagepicker.tools.PictureFileUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author：luck
 * @data：2018/3/28 下午1:00
 * @描述: Activity基类
 * modify 2018/6/27 方志， 加入原图数据回调
 * modify 2018/7/25 方志， 解决InputMethodManager内存泄露
 * 加入原图，仿微信页面
 */
public class PictureBaseActivity extends FragmentActivity {
    protected Context mContext;
    protected PictureSelectionConfig config;
    protected boolean openWhiteStatusBar, numComplete;
    protected int colorPrimary, colorPrimaryDark;
    protected String cameraPath, outputCameraPath;
    protected String originalPath;
    protected PictureDialog dialog;
    protected PictureDialog compressDialog;
    protected List<LocalMedia> selectionMedias;

    /**
     * 是否使用沉浸式，子类复写该方法来确定是否采用沉浸式
     *
     * @return 是否沉浸式，默认true
     */
    @Override
    public boolean isImmersive() {
        return true;
    }

    /**
     * 具体沉浸的样式，可以根据需要自行修改状态栏和导航栏的颜色
     */
    public void immersive() {
        ImmersiveManage.immersiveAboveAPI23(this
                , colorPrimaryDark
                , colorPrimary
                , openWhiteStatusBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            config = savedInstanceState.getParcelable(PictureConfig.EXTRA_CONFIG);
            cameraPath = savedInstanceState.getString(PictureConfig.BUNDLE_CAMERA_PATH);
            originalPath = savedInstanceState.getString(PictureConfig.BUNDLE_ORIGINAL_PATH);
        } else {
            config = PictureSelectionConfig.getInstance();
        }
        int themeStyleId = config.themeStyleId;
        setTheme(themeStyleId);
        super.onCreate(savedInstanceState);
        mContext = this;
        initConfig();
        if (isImmersive()) {
            immersive();
        }
    }

    /**
     * 获取配置参数
     */
    private void initConfig() {
        outputCameraPath = config.outputCameraPath;
        // 是否开启白色状态栏
        openWhiteStatusBar = AttrsUtils.getTypeValueBoolean
                (this, R.attr.picture_statusFontColor);
        // 是否是0/9样式
        numComplete = AttrsUtils.getTypeValueBoolean(this,
                R.attr.picture_style_numComplete);
        // 是否开启数字勾选模式
        config.checkNumMode = AttrsUtils.getTypeValueBoolean
                (this, R.attr.picture_style_checkNumMode);
        // 标题栏背景色
        colorPrimary = AttrsUtils.getTypeValueColor(this, R.attr.colorPrimary);
        // 状态栏背景色
        colorPrimaryDark = AttrsUtils.getTypeValueColor(this, R.attr.colorPrimaryDark);
        // 已选图片列表
        selectionMedias = config.selectionMedias;
        if (selectionMedias == null) {
            selectionMedias = new ArrayList<>();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PictureConfig.BUNDLE_CAMERA_PATH, cameraPath);
        outState.putString(PictureConfig.BUNDLE_ORIGINAL_PATH, originalPath);
        outState.putParcelable(PictureConfig.EXTRA_CONFIG, config);
    }

    protected void startActivity(Class clz, Bundle bundle) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Intent intent = new Intent();
            intent.setClass(this, clz);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    protected void startActivity(Class clz, Bundle bundle, int requestCode) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Intent intent = new Intent();
            intent.setClass(this, clz);
            intent.putExtras(bundle);
            startActivityForResult(intent, requestCode);
        }
    }

    /**
     * loading dialog
     */
    protected void showPleaseDialog() {
        if (!isFinishing()) {
            dismissDialog();
            dialog = new PictureDialog(this);
            dialog.show();
        }
    }

    /**
     * dismiss dialog
     */
    protected void dismissDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * compress loading dialog
     */
    protected void showCompressDialog() {
        if (!isFinishing()) {
            dismissCompressDialog();
            compressDialog = new PictureDialog(this);
            compressDialog.show();
        }
    }

    /**
     * dismiss compress dialog
     */
    protected void dismissCompressDialog() {
        try {
            if (!isFinishing()
                    && compressDialog != null
                    && compressDialog.isShowing()) {
                compressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * compressImage
     */
    protected void compressImage(final List<LocalMedia> result) {
        showCompressDialog();
        if (config.synOrAsy) {
            try {
                Flowable.just(result)
                        .observeOn(Schedulers.io())
                        .map(new Function<List<LocalMedia>, List<File>>() {
                            @Override
                            public List<File> apply(@NonNull List<LocalMedia> list) throws Exception {
                                List<File> files = Luban.with(mContext)
                                        .setTargetDir(config.compressSavePath)
                                        .ignoreBy(config.minimumCompressSize)
                                        .loadLocalMedia(list).get();
                                if (files == null) {
                                    files = new ArrayList<>();
                                }
                                return files;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<File>>() {
                            @Override
                            public void accept(@NonNull List<File> files) throws Exception {
                                handleCompressCallBack(result, files);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Luban.with(this)
                    .loadLocalMedia(result)
                    .ignoreBy(config.minimumCompressSize)
                    .setTargetDir(config.compressSavePath)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(List<LocalMedia> list) {
                            RxBus.getDefault().post(new EventEntity(PictureConfig.CLOSE_PREVIEW_FLAG));
                            onResult(list);
                        }

                        @Override
                        public void onError(Throwable e) {
                            RxBus.getDefault().post(new EventEntity(PictureConfig.CLOSE_PREVIEW_FLAG));
                            onResult(result);
                        }
                    }).launch();
        }
    }

    /**
     * 重新构造已压缩的图片返回集合
     *
     * @param images
     * @param files
     */
    private void handleCompressCallBack(List<LocalMedia> images, List<File> files) {
        if (files.size() == images.size()) {
            for (int i = 0, j = images.size(); i < j; i++) {
                // 压缩成功后的地址
                String path = files.get(i).getPath();
                LocalMedia image = images.get(i);
                // 如果是网络图片则不压缩
                boolean http = PictureMimeType.isHttp(path);
                boolean eqTrue = !TextUtils.isEmpty(path) && http;
                image.setCompressed(eqTrue ? false : true);
                image.setCompressPath(eqTrue ? "" : path);
            }
        }
        RxBus.getDefault().post(new EventEntity(PictureConfig.CLOSE_PREVIEW_FLAG));
        onResult(images);
    }

//    /**
//     * 去裁剪
//     *
//     * @param originalPath
//     */
//    protected void startCrop(String originalPath) {
//        UCrop.Options options = new UCrop.Options();
//        int toolbarColor = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_toolbar_bg);
//        int statusColor = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_status_color);
//        int titleColor = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_title_color);
//        options.setToolbarColor(toolbarColor);
//        options.setStatusBarColor(statusColor);
//        options.setToolbarWidgetColor(titleColor);
//        options.setCircleDimmedLayer(config.circleDimmedLayer);
//        options.setShowCropFrame(config.showCropFrame);
//        options.setShowCropGrid(config.showCropGrid);
//        options.setDragFrameEnabled(config.isDragFrame);
//        options.setScaleEnabled(config.scaleEnabled);
//        options.setRotateEnabled(config.rotateEnabled);
//        options.setCompressionQuality(config.cropCompressQuality);
//        options.setHideBottomControls(config.hideBottomControls);
//        options.setFreeStyleCropEnabled(config.freeStyleCropEnabled);
//        boolean isHttp = PictureMimeType.isHttp(originalPath);
//        String imgType = PictureMimeType.getLastImgType(originalPath);
//        Uri uri = isHttp ? Uri.parse(originalPath) : Uri.fromFile(new File(originalPath));
//        UCrop.of(uri, Uri.fromFile(new File(PictureFileUtils.getDiskCacheDir(this),
//                System.currentTimeMillis() + imgType)))
//                .withAspectRatio(config.aspect_ratio_x, config.aspect_ratio_y)
//                .withMaxResultSize(config.cropWidth, config.cropHeight)
//                .withOptions(options)
//                .start(this);
//    }
//
//    /**
//     * 多图去裁剪
//     *
//     * @param list
//     */
//    protected void startCrop(ArrayList<String> list) {
//        UCropMulti.Options options = new UCropMulti.Options();
//        int toolbarColor = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_toolbar_bg);
//        int statusColor = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_status_color);
//        int titleColor = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_title_color);
//        options.setToolbarColor(toolbarColor);
//        options.setStatusBarColor(statusColor);
//        options.setToolbarWidgetColor(titleColor);
//        options.setCircleDimmedLayer(config.circleDimmedLayer);
//        options.setShowCropFrame(config.showCropFrame);
//        options.setDragFrameEnabled(config.isDragFrame);
//        options.setShowCropGrid(config.showCropGrid);
//        options.setScaleEnabled(config.scaleEnabled);
//        options.setRotateEnabled(config.rotateEnabled);
//        options.setHideBottomControls(true);
//        options.setCompressionQuality(config.cropCompressQuality);
//        options.setCutListData(list);
//        options.setFreeStyleCropEnabled(config.freeStyleCropEnabled);
//        String path = list.size() > 0 ? list.get(0) : "";
//        boolean isHttp = PictureMimeType.isHttp(path);
//        String imgType = PictureMimeType.getLastImgType(path);
//        Uri uri = isHttp ? Uri.parse(path) : Uri.fromFile(new File(path));
//        UCropMulti.of(uri, Uri.fromFile(new File(PictureFileUtils.getDiskCacheDir(this),
//                System.currentTimeMillis() + imgType)))
//                .withAspectRatio(config.aspect_ratio_x, config.aspect_ratio_y)
//                .withMaxResultSize(config.cropWidth, config.cropHeight)
//                .withOptions(options)
//                .start(this);
//    }


    /**
     * 判断拍照 图片是否旋转
     *
     * @param degree
     * @param file
     */
    protected void rotateImage(int degree, File file) {
        if (degree > 0) {
            // 针对相片有旋转问题的处理方式
            try {
                BitmapFactory.Options opts = new BitmapFactory.Options();//获取缩略图显示到屏幕上
                opts.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
                Bitmap bmp = PictureFileUtils.rotaingImageView(degree, bitmap);
                PictureFileUtils.saveBitmapFile(bmp, file);
                bitmap.recycle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * compress or callback
     *
     * @param result
     */
    protected void handlerResult(List<LocalMedia> result) {
        if (config.isCompress) {
            compressImage(result);
        } else {
            onResult(result);
        }
    }


    /**
     * 如果没有任何相册，先创建一个最近相册出来
     *
     * @param folders
     */
    protected void createNewFolder(List<LocalMediaFolder> folders) {
        if (folders.size() == 0) {
            // 没有相册 先创建一个最近相册出来
            LocalMediaFolder newFolder = new LocalMediaFolder();
            String folderName = config.mimeType == PictureMimeType.ofAudio() ?
                    getString(R.string.picture_all_audio) : getString(R.string.picture_camera_roll);
            newFolder.setName(folderName);
            newFolder.setPath("");
            newFolder.setFirstImagePath("");
            folders.add(newFolder);
        }
    }

    /**
     * 将图片插入到相机文件夹中
     *
     * @param path
     * @param imageFolders
     * @return
     */
    protected LocalMediaFolder getImageFolder(String path, List<LocalMediaFolder> imageFolders) {
        File imageFile = new File(path);
        File folderFile = imageFile.getParentFile();

        for (LocalMediaFolder folder : imageFolders) {
            if (folder.getName().equals(folderFile.getName())) {
                return folder;
            }
        }
        LocalMediaFolder newFolder = new LocalMediaFolder();
        newFolder.setName(folderFile.getName());
        newFolder.setPath(folderFile.getAbsolutePath());
        newFolder.setFirstImagePath(path);
        imageFolders.add(newFolder);
        return newFolder;
    }


    protected Boolean isOriginal = false;
    /**
     * return image result
     *
     * @param images
     */
    protected void onResult(List<LocalMedia> images) {
        dismissCompressDialog();
        if (config.camera
                && config.selectionMode == PictureConfig.MULTIPLE
                && selectionMedias != null) {
            images.addAll(images.size() > 0 ? images.size() - 1 : 0, selectionMedias);
        }
        Intent intent = PictureSelector.putIntentResult(images);
        intent.putExtra(PictureConfig.IS_ORIGINAL, isOriginal);
        setResult(RESULT_OK, intent);
        closeActivity();
    }

    /**
     * Close Activity
     */
    protected void closeActivity() {
        finish();
        if (config.camera) {
            overridePendingTransition(0, R.anim.fade_out);
        } else {
            overridePendingTransition(0, R.anim.a3);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissCompressDialog();
        dismissDialog();

        fixInputMethodManagerLeak(this);
    }


    /**
     * 获取DCIM文件下最新一条拍照记录
     *
     * @return
     */
    protected int getLastImageId(boolean eqVideo) {
        try {
            //selection: 指定查询条件
            String absolutePath = PictureFileUtils.getDCIMCameraPath();
            String ORDER_BY = MediaStore.Files.FileColumns._ID + " DESC";
            String selection = eqVideo ? MediaStore.Video.Media.DATA + " like ?" :
                    MediaStore.Images.Media.DATA + " like ?";
            //定义selectionArgs：
            String[] selectionArgs = {absolutePath + "%"};
            Cursor imageCursor = this.getContentResolver().query(eqVideo ?
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            : MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    selection, selectionArgs, ORDER_BY);
            if (imageCursor.moveToFirst()) {
                int id = imageCursor.getInt(eqVideo ?
                        imageCursor.getColumnIndex(MediaStore.Video.Media._ID)
                        : imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
                long date = imageCursor.getLong(eqVideo ?
                        imageCursor.getColumnIndex(MediaStore.Video.Media.DURATION)
                        : imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                int duration = DateUtils.dateDiffer(date);
                imageCursor.close();
                // DCIM文件下最近时间30s以内的图片，可以判定是最新生成的重复照片
                return duration <= 30 ? id : -1;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 删除部分手机 拍照在DCIM也生成一张的问题
     *
     * @param id
     * @param eqVideo
     */
    protected void removeImage(int id, boolean eqVideo) {
        try {
            ContentResolver cr = getContentResolver();
            Uri uri = eqVideo ? MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    : MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String selection = eqVideo ? MediaStore.Video.Media._ID + "=?"
                    : MediaStore.Images.Media._ID + "=?";
            cr.delete(uri,
                    selection,
                    new String[]{Long.toString(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 录音
     *
     * @param data
     */
    protected String getAudioPath(Intent data) {
        boolean compare_SDK_19 = Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
        if (data != null && config.mimeType == PictureMimeType.ofAudio()) {
            try {
                Uri uri = data.getData();
                final String audioPath;
                if (compare_SDK_19) {
                    audioPath = uri.getPath();
                } else {
                    audioPath = getAudioFilePathFromUri(uri);
                }
                return audioPath;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 获取刚录取的音频文件
     *
     * @param uri
     * @return
     */
    protected String getAudioFilePathFromUri(Uri uri) {
        String path = "";
        try {
            Cursor cursor = getContentResolver()
                    .query(uri, null, null, null, null);
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
            path = cursor.getString(index);
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 设置全屏退出动画
     *
     * @param activity 上下文
     */
    public void setFullScreenAnim(Activity activity, boolean isFullscreen) {
        Window window = activity.getWindow();
        if(Build.VERSION.SDK_INT < 16) {
            window.setFlags(1024, 1024);
        } else {
            View decorView = window.getDecorView();
            int uiOptions = isFullscreen ? 4 : 0;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     *  此方法是适配华为部分机型，一般直接调用setFullScreenAnim即可
     * @param activity 上下文
     * @param isFullscreen 是否全屏
     */
    public void setFullScreen(Activity activity, boolean isFullscreen) {
        Window window = activity.getWindow();
        if(isFullscreen) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


    // 解决InputMethodManager内存泄露
    public void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String [] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView", "mLastSrvView"};
        Field f;
        Object obj_get;
        for (int i = 0;i < arr.length;i ++) {
            String param = arr[i];
            try{
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
//                        if (QLog.isColorLevel()) {
//                            QLog.d(ReflecterHelper.class.getSimpleName(), QLog.CLR, "fixInputMethodManagerLeak break, context is not suitable, get_context=" + v_get.getContext()+" dest_context=" + destContext);
//                        }
                        break;
                    }
                }
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
    }

}
