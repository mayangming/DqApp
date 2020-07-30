package com.meetqs.qingchat.imagepicker;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.meetqs.qingchat.imagepicker.adapter.PictureAlbumDirectoryAdapter;
import com.meetqs.qingchat.imagepicker.adapter.PictureImageGridAdapter;
import com.meetqs.qingchat.imagepicker.config.PictureConfig;
import com.meetqs.qingchat.imagepicker.config.PictureMimeType;
import com.meetqs.qingchat.imagepicker.decoration.GridSpacingItemDecoration;
import com.meetqs.qingchat.imagepicker.decoration.RecycleViewDivider;
import com.meetqs.qingchat.imagepicker.dialog.CustomDialog;
import com.meetqs.qingchat.imagepicker.entity.EventEntity;
import com.meetqs.qingchat.imagepicker.entity.LocalMedia;
import com.meetqs.qingchat.imagepicker.entity.LocalMediaFolder;
import com.meetqs.qingchat.imagepicker.model.LocalMediaLoader;
import com.meetqs.qingchat.imagepicker.observable.ImagesObservable;
import com.meetqs.qingchat.imagepicker.permissions.RxPermissions;
import com.meetqs.qingchat.imagepicker.rxbus2.RxBus;
import com.meetqs.qingchat.imagepicker.rxbus2.Subscribe;
import com.meetqs.qingchat.imagepicker.rxbus2.ThreadMode;
import com.meetqs.qingchat.imagepicker.tools.DateUtils;
import com.meetqs.qingchat.imagepicker.tools.DoubleUtils;
import com.meetqs.qingchat.imagepicker.tools.PictureFileUtils;
import com.meetqs.qingchat.imagepicker.tools.ScreenUtils;
import com.meetqs.qingchat.imagepicker.tools.StringUtils;
import com.meetqs.qingchat.imagepicker.tools.ToastManage;
import com.meetqs.qingchat.imagepicker.widget.PhotoPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author：luck
 * @data：2018/1/27 19:12
 * @描述: Media 选择页面
 * modify 2018/6/26 方志， 修改文件夹选择样式及动画，修改标题栏，底部选择栏，及其他样式背景
 * 加入原图，仿微信页面。
 *  modify 2018/7/19 方志，限制视频，gif文件大小
 */
public class PictureSelectorActivity extends PictureBaseActivity implements View.OnClickListener,
        PictureImageGridAdapter.OnPhotoSelectChangedListener, PhotoPopupWindow.OnItemClickListener {
    private final static String TAG = PictureSelectorActivity.class.getSimpleName();
    private static final int SHOW_DIALOG = 0;
    private static final int DISMISS_DIALOG = 1;
    private static final int REQUEST_CODE_PICTURE_PREVIEW = 222;
    private ImageButton picture_left_back;
    private TextView tv_empty;
    private TextView tv_PlayPause;
    private TextView tv_Stop;
    private TextView tv_Quit;
    private TextView tv_musicStatus;
    private TextView tv_musicTime;
    private TextView tv_musicTotal;
    private RecyclerView picture_recycler;
    private PictureImageGridAdapter adapter;
    private List<LocalMedia> images = new ArrayList<>();
    private List<LocalMediaFolder> foldersList = new ArrayList<>();
//    private FolderPopWindow folderWindow;
    private Animation animation = null;
    private boolean anim = false;
    private RxPermissions rxPermissions;
    private PhotoPopupWindow popupWindow;
    private LocalMediaLoader mediaLoader;
    private MediaPlayer mediaPlayer;
    private SeekBar musicSeekBar;
    private boolean isPlayAudio = false;
    private CustomDialog audioDialog;
    private int audioH;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_DIALOG:
                    showPleaseDialog();
                    break;
                case DISMISS_DIALOG:
                    dismissDialog();
                    break;
            }
        }
    };
    private TextView tv_folder_text;
    private TextView picture_id_preview;
    private ImageView origin_check_image_iv;
    private LinearLayout origin_check_ll;
    private View rl_bottom;
    private LinearLayout folder_ll;
    private RecyclerView folder_recycler_view;
    private LinearLayout folder_text_container;
    private RelativeLayout rl_picture_title;
    private Button btn_send;
    private TextView picture_title;

    /**
     * EventBus 3.0 回调
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(EventEntity obj) {
        switch (obj.what) {
            case PictureConfig.UPDATE_FLAG:
                // 预览时勾选图片更新回调
                List<LocalMedia> selectImages = obj.medias;
                anim = selectImages.size() > 0 ? true : false;
                int position = obj.position;
                //Log.i("刷新下标:", String.valueOf(position));
                adapter.bindSelectImages(selectImages);
                adapter.notifyItemChanged(position);

                break;
            case PictureConfig.PREVIEW_DATA_FLAG:
                List<LocalMedia> medias = obj.medias;
                int originalTag = obj.position;
                isOriginal = originalTag == 1;
                if (medias.size() > 0) {
                    // 取出第1个判断是否是图片，视频和图片只能二选一，不必考虑图片和视频混合
                    String pictureType = medias.get(0).getPictureType();
                    if (config.isCompress && pictureType.startsWith(PictureConfig.IMAGE)) {
                        compressImage(medias);
                    } else {
                        onResult(medias);
                    }
                }
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().register(this);
        }
        rxPermissions = new RxPermissions(this);
        if (config.camera) {
            if (savedInstanceState == null) {
                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {
                                    onTakePhoto();
                                } else {
                                    ToastManage.s(mContext, getString(R.string.picture_camera));
                                    closeActivity();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
            }
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    , WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.picture_empty);
        } else {
            setContentView(R.layout.picture_selector);
            initView(savedInstanceState);
        }
    }

    /**
     * init views
     */
    private void initView(Bundle savedInstanceState) {

        rl_picture_title = (RelativeLayout) findViewById(R.id.rl_picture_title);
        picture_left_back = (ImageButton) findViewById(R.id.picture_left_back);
        picture_title = (TextView) findViewById(R.id.picture_title);
        btn_send = (Button)findViewById(R.id.btn_send);

        rl_bottom = findViewById(R.id.rl_bottom);
        origin_check_ll = (LinearLayout) findViewById(R.id.origin_check);
        origin_check_image_iv = (ImageView) findViewById(R.id.origin_check_image_iv);
        folder_text_container = (LinearLayout) findViewById(R.id.folder_text_container);
        tv_folder_text = (TextView) findViewById(R.id.tv_folder_text);
        picture_id_preview = (TextView) findViewById(R.id.picture_id_preview);
        folder_ll = (LinearLayout) findViewById(R.id.folder_ll);
        folder_recycler_view = (RecyclerView) findViewById(R.id.folder_recycler_view);

        picture_recycler = (RecyclerView) findViewById(R.id.picture_recycler);
        tv_empty = (TextView) findViewById(R.id.tv_empty);


        initFolder();

        picture_id_preview.setOnClickListener(this);
        picture_left_back.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        origin_check_ll.setOnClickListener(this);
        folder_text_container.setOnClickListener(this);

        isNumComplete(numComplete);
        if(config.isCamera) {
            if (config.mimeType == PictureMimeType.ofAll()) {
                popupWindow = new PhotoPopupWindow(this);
                popupWindow.setOnItemClickListener(this);
            }
        }

//        if (config.mimeType == PictureMimeType.ofAudio()) {
//            picture_id_preview.setVisibility(View.GONE);
//            audioH = ScreenUtils.getScreenHeight(mContext)
//                    + ScreenUtils.getStatusBarHeight(mContext);
//        } else {
//            picture_id_preview.setVisibility(config.mimeType == PictureConfig.TYPE_VIDEO
//                    ? View.GONE : View.VISIBLE);
//        }
//        folderWindow = new FolderPopWindow(this, config.mimeType);
//        folderWindow.setOnItemClickListener(this);

        initImage(savedInstanceState);
        String titleText = picture_id_preview.getText().toString().trim();
        if (config.isCamera) {
            config.isCamera = StringUtils.isCamera(titleText);
        }

        if (!TextUtils.isEmpty(config.btnDesc)) {
            btn_send.setText(config.btnDesc);
        }
    }

    /**
     * 加载所有视图
     * @param savedInstanceState
     */
    private void initImage(Bundle savedInstanceState) {
        picture_recycler.setHasFixedSize(true);
        picture_recycler.addItemDecoration(new GridSpacingItemDecoration(config.imageSpanCount,
                ScreenUtils.dip2px(this, 2), false));
        picture_recycler.setLayoutManager(new GridLayoutManager(this, config.imageSpanCount));
        // 解决调用 notifyItemChanged 闪烁问题,取消默认动画
        ((SimpleItemAnimator) picture_recycler.getItemAnimator()).setSupportsChangeAnimations(false);
        mediaLoader = new LocalMediaLoader(this, config.mimeType, config.isGif, config.videoMaxSecond, config.videoMinSecond);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mHandler.sendEmptyMessage(SHOW_DIALOG);
                            readLocalMedia();
                        } else {
                            ToastManage.s(mContext, getString(R.string.picture_jurisdiction));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        tv_empty.setText(config.mimeType == PictureMimeType.ofAudio() ?
                getString(R.string.picture_audio_empty)
                : getString(R.string.picture_empty));
        StringUtils.tempTextFont(tv_empty, config.mimeType);
        if (savedInstanceState != null) {
            // 防止拍照内存不足时activity被回收，导致拍照后的图片未选中
            selectionMedias = PictureSelector.obtainSelectorList(savedInstanceState);
        }
        adapter = new PictureImageGridAdapter(mContext, config);
        adapter.setOnPhotoSelectChangedListener(PictureSelectorActivity.this);
        adapter.bindSelectImages(selectionMedias);
        picture_recycler.setAdapter(adapter);
    }

    private PictureAlbumDirectoryAdapter folderAdapter = null;

    /**
     * 文件夹弹窗
     */
    private void initFolder() {

        picture_title.setText(R.string.picture_camera_roll);

        folderAdapter = new PictureAlbumDirectoryAdapter(this);
        folder_recycler_view.getLayoutParams().height = (int) (ScreenUtils.getScreenHeight(this) * 0.72);
        folder_recycler_view.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL,
                ScreenUtils.dip2px(this, 0), ContextCompat.getColor(this, R.color.transparent)));
        folder_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        folder_recycler_view.setAdapter(folderAdapter);

        //先执行文件夹退出动画，修改第一次启动没有动画效果
        exitAnimator();

        folder_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAnimator();
            }
        });

        folderAdapter.setOnItemClickListener(new PictureAlbumDirectoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String folderName, List<LocalMedia> images) {
                boolean camera = StringUtils.isCamera(folderName);
                camera = config.isCamera ? camera : false;
                adapter.setShowCamera(camera);
                tv_folder_text.setText(folderName);
                adapter.bindImagesData(images);
                exitAnimator();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter != null) {
            List<LocalMedia> selectedImages = adapter.getSelectedImages();
            PictureSelector.saveSelectorList(outState, selectedImages);
        }
    }

    /**
     * none number style
     */
    private void isNumComplete(boolean numComplete) {
//        picture_tv_ok.setText(numComplete ? getString(R.string.picture_done_front_num,
//                0, config.selectionMode == PictureConfig.SINGLE ? 1 : config.maxSelectNum)
//                : getString(R.string.picture_please_select));
        if (!numComplete) {
            animation = AnimationUtils.loadAnimation(this, R.anim.modal_in);
        }
        animation = numComplete ? null : AnimationUtils.loadAnimation(this, R.anim.modal_in);
    }

    /**
     * get LocalMedia s
     */
    protected void readLocalMedia() {
        mediaLoader.loadAllMedia(new LocalMediaLoader.LocalMediaLoadListener() {
            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                if (folders.size() > 0) {
                    foldersList = folders;
                    LocalMediaFolder folder = folders.get(0);
                    folder.setChecked(true);
                    List<LocalMedia> localImg = folder.getImages();
                    // 这里解决有些机型会出现拍照完，相册列表不及时刷新问题
                    // 因为onActivityResult里手动添加拍照后的照片，
                    // 如果查询出来的图片大于或等于当前adapter集合的图片则取更新后的，否则就取本地的
                    if (localImg.size() >= images.size()) {
                        images = localImg;
//                        folderWindow.bindFolder(folders);
                        folderAdapter.setMimeType(config.mimeType);
                        folderAdapter.bindFolderData(folders);
                    }
                }
                if (adapter != null) {
                    if (images == null) {
                        images = new ArrayList<>();
                    }
                    adapter.bindImagesData(images);
                    tv_empty.setVisibility(images.size() > 0
                            ? View.INVISIBLE : View.VISIBLE);
                }
                mHandler.sendEmptyMessage(DISMISS_DIALOG);
            }

            @Override
            public void onError(Exception exception) {
                //Log.e("TAG", "exception : " + exception);
                mHandler.sendEmptyMessage(DISMISS_DIALOG);
            }
        });
    }

    /**
     * open camera
     */
    public void startCamera() {
        // 防止快速点击，但是单独拍照不管
        if (!DoubleUtils.isFastDoubleClick() || config.camera) {
            switch (config.mimeType) {
                case PictureConfig.TYPE_ALL:
                    // 如果是全部类型下，单独拍照就默认图片 (因为单独拍照不会new此PopupWindow对象)
                    if (popupWindow != null) {
                        if (popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        popupWindow.showAsDropDown(rl_picture_title);
                    } else {
                        startOpenCamera();
                    }
                    break;
                case PictureConfig.TYPE_IMAGE:
                    // 拍照
                    startOpenCamera();
                    break;
                case PictureConfig.TYPE_VIDEO:
                    // 录视频
                    startOpenCameraVideo();
                    break;
                case PictureConfig.TYPE_AUDIO:
                    // 录音
                    startOpenCameraAudio();
                    break;
            }
        }
    }

    /**
     * start to camera、preview、crop
     */
    public void startOpenCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            int type = config.mimeType == PictureConfig.TYPE_ALL ? PictureConfig.TYPE_IMAGE : config.mimeType;
            File cameraFile = PictureFileUtils.createCameraFile(this,type,
                    outputCameraPath, config.suffixType);
            cameraPath = cameraFile.getAbsolutePath();
            Uri imageUri = parUri(cameraFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
        }
    }

    /**
     * start to camera、video
     */
    public void startOpenCameraVideo() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File cameraFile = PictureFileUtils.createCameraFile(this, config.mimeType ==
                            PictureConfig.TYPE_ALL ? PictureConfig.TYPE_VIDEO : config.mimeType,
                    outputCameraPath, config.suffixType);
            cameraPath = cameraFile.getAbsolutePath();
            Uri imageUri = parUri(cameraFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, config.recordVideoSecond);
            cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, config.videoQuality);
            startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
        }
    }

    /**
     * start to camera audio
     */
    public void startOpenCameraAudio() {
        rxPermissions.request(Manifest.permission.RECORD_AUDIO).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    Intent cameraIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                    if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
                    }
                } else {
                    ToastManage.s(mContext, getString(R.string.picture_audio));
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 生成uri
     *
     * @param cameraFile 相机文件
     * @return Uri
     */
    private Uri parUri(File cameraFile) {
        Uri imageUri;
        String authority = getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(mContext, authority, cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.picture_left_back) {
            if (folder_ll.getVisibility() == View.VISIBLE) {
                exitAnimator();
            } else {
                closeActivity();
            }
        }
//        else if (id == R.id.picture_title) {
////            if (folderWindow.isShowing()) {
////                folderWindow.dismiss();
////            } else {
////                if (images != null && images.size() > 0) {
////                    folderWindow.showAsDropDown(rl_picture_title);
////                    List<LocalMedia> selectedImages = adapter.getSelectedImages();
////                    folderWindow.notifyDataCheckedStatus(selectedImages);
////                }
////            }
//        }
        else if (id == R.id.picture_id_preview) {
            List<LocalMedia> selectedImages = adapter.getSelectedImages();

            List<LocalMedia> medias = new ArrayList<>();
            medias.addAll(selectedImages);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(PictureConfig.EXTRA_PREVIEW_SELECT_LIST, (ArrayList<? extends Parcelable>) medias);
            bundle.putParcelableArrayList(PictureConfig.EXTRA_SELECT_LIST, (ArrayList<? extends Parcelable>) selectedImages);
            bundle.putBoolean(PictureConfig.EXTRA_BOTTOM_PREVIEW, true);
            bundle.putBoolean(PictureConfig.IS_ORIGINAL, isOriginal);
            startActivity(PicturePreviewActivity.class, bundle, REQUEST_CODE_PICTURE_PREVIEW);
            overridePendingTransition(R.anim.a5, 0);
        }else if(id == R.id.folder_text_container) {
            //左下角选择文件夹
            if (folder_ll.getVisibility() == View.VISIBLE) {
                exitAnimator();
            } else {
                if (images != null && images.size() > 0) {

                    List<LocalMedia> selectedImages = adapter.getSelectedImages();
                    notifyDataCheckedStatus(selectedImages);
                    folder_ll.setVisibility(View.VISIBLE);
                    enterAnimator();
                }
            }
        }else if(id == R.id.origin_check) {
            //加载原图
            isOriginal = !isOriginal;
            if(isOriginal) {
                origin_check_image_iv.setImageResource(R.mipmap.checkbox_selected);
            }else {
                origin_check_image_iv.setImageResource(R.mipmap.checkbox_normal);
            }
        }else if(id == R.id.btn_send) {
            //发送图片
            List<LocalMedia> images = adapter.getSelectedImages();
            LocalMedia image = images.size() > 0 ? images.get(0) : null;
            String pictureType = image != null ? image.getPictureType() : "";
            // 如果设置了图片最小选择数量，则判断是否满足条件
            int size = images.size();
            boolean eqImg = pictureType.startsWith(PictureConfig.IMAGE);
            if (config.minSelectNum > 0 && config.selectionMode == PictureConfig.MULTIPLE) {
                if (size < config.minSelectNum) {
                    @SuppressLint("StringFormatMatches") String str = eqImg ? getString(R.string.picture_min_img_num, config.minSelectNum)
                            : getString(R.string.picture_min_video_num, config.minSelectNum);
                    ToastManage.s(mContext, str);
                    return;
                }
            }
            //裁剪
//            if (config.enableCrop && eqImg) {
//                if (config.selectionMode == PictureConfig.SINGLE) {
//                    originalPath = image.getPath();
//                    //startCrop(originalPath);
//                } else {
//                    // 是图片和选择压缩并且是多张，调用批量压缩
//                    ArrayList<String> medias = new ArrayList<>();
//                    for (LocalMedia media : images) {
//                        medias.add(media.getPath());
//                    }
//                    //startCrop(medias);
//                }
//            } else
            if (config.isCompress && eqImg) {
                // 图片才压缩，视频不管
                compressImage(images);
            } else {
                onResult(images);
            }
        }
    }


    /**
     * 设置选中状态
     */
    private void notifyDataCheckedStatus(List<LocalMedia> medias) {
        try {
            // 获取选中图片
            List<LocalMediaFolder> folders = folderAdapter.getFolderData();
            for (LocalMediaFolder folder : folders) {
                folder.setCheckedNum(0);
            }
            if (medias.size() > 0) {
                for (LocalMediaFolder folder : folders) {
                    int num = 0;// 记录当前相册下有多少张是选中的
                    List<LocalMedia> images = folder.getImages();
                    for (LocalMedia media : images) {
                        String path = media.getPath();
                        for (LocalMedia m : medias) {
                            if (path.equals(m.getPath())) {
                                num++;
                                folder.setCheckedNum(num);
                            }
                        }
                    }
                }
            }
            folderAdapter.bindFolderData(folders);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 播放音频
     *
     * @param path
     */
    private void audioDialog(final String path) {
        audioDialog = new CustomDialog(mContext,
                LinearLayout.LayoutParams.MATCH_PARENT, audioH
                ,
                R.layout.picture_audio_dialog, R.style.Theme_dialog);
        audioDialog.getWindow().setWindowAnimations(R.style.Dialog_Audio_StyleAnim);
        tv_musicStatus = (TextView) audioDialog.findViewById(R.id.tv_musicStatus);
        tv_musicTime = (TextView) audioDialog.findViewById(R.id.tv_musicTime);
        musicSeekBar = (SeekBar) audioDialog.findViewById(R.id.musicSeekBar);
        tv_musicTotal = (TextView) audioDialog.findViewById(R.id.tv_musicTotal);
        tv_PlayPause = (TextView) audioDialog.findViewById(R.id.tv_PlayPause);
        tv_Stop = (TextView) audioDialog.findViewById(R.id.tv_Stop);
        tv_Quit = (TextView) audioDialog.findViewById(R.id.tv_Quit);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initPlayer(path);
            }
        }, 30);
        tv_PlayPause.setOnClickListener(new audioOnClick(path));
        tv_Stop.setOnClickListener(new audioOnClick(path));
        tv_Quit.setOnClickListener(new audioOnClick(path));
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        audioDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stop(path);
                    }
                }, 30);
                try {
                    if (audioDialog != null
                            && audioDialog.isShowing()) {
                        audioDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        handler.post(runnable);
        audioDialog.show();
    }

    //  通过 Handler 更新 UI 上的组件状态
    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (mediaPlayer != null) {
                    tv_musicTime.setText(DateUtils.timeParse(mediaPlayer.getCurrentPosition()));
                    musicSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                    musicSeekBar.setMax(mediaPlayer.getDuration());
                    tv_musicTotal.setText(DateUtils.timeParse(mediaPlayer.getDuration()));
                    handler.postDelayed(runnable, 200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 初始化音频播放组件
     *
     * @param path
     */
    private void initPlayer(String path) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            playAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放音频点击事件
     */
    public class audioOnClick implements View.OnClickListener {
        private String path;

        public audioOnClick(String path) {
            super();
            this.path = path;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_PlayPause) {
                playAudio();
            }
            if (id == R.id.tv_Stop) {
                tv_musicStatus.setText(getString(R.string.picture_stop_audio));
                tv_PlayPause.setText(getString(R.string.picture_play_audio));
                stop(path);
            }
            if (id == R.id.tv_Quit) {
                handler.removeCallbacks(runnable);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stop(path);
                    }
                }, 30);
                try {
                    if (audioDialog != null
                            && audioDialog.isShowing()) {
                        audioDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 播放音频
     */
    private void playAudio() {
        if (mediaPlayer != null) {
            musicSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            musicSeekBar.setMax(mediaPlayer.getDuration());
        }
        String ppStr = tv_PlayPause.getText().toString();
        if (ppStr.equals(getString(R.string.picture_play_audio))) {
            tv_PlayPause.setText(getString(R.string.picture_pause_audio));
            tv_musicStatus.setText(getString(R.string.picture_play_audio));
            playOrPause();
        } else {
            tv_PlayPause.setText(getString(R.string.picture_play_audio));
            tv_musicStatus.setText(getString(R.string.picture_pause_audio));
            playOrPause();
        }
        if (isPlayAudio == false) {
            handler.post(runnable);
            isPlayAudio = true;
        }
    }

    /**
     * 停止播放
     *
     * @param path
     */
    public void stop(String path) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 暂停播放
     */
    public void playOrPause() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onItemClick(String folderName, List<LocalMedia> images) {
//        boolean camera = StringUtils.isCamera(folderName);
//        camera = config.isCamera ? camera : false;
//        adapter.setShowCamera(camera);
//        picture_title.setText(folderName);
//        adapter.bindImagesData(images);
//        //folderWindow.dismiss();
//    }

    @Override
    public void onTakePhoto() {
        // 启动相机拍照,先判断手机是否有拍照权限
        rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    startCamera();
                } else {
                    ToastManage.s(mContext, getString(R.string.picture_camera));
                    if (config.camera) {
                        closeActivity();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onChange(List<LocalMedia> selectImages) {
        changeImageNumber(selectImages);
    }

    @Override
    public void onPictureClick(LocalMedia media, int position) {
        List<LocalMedia> images = adapter.getImages();
        startPreview(images, position);
    }

    /**
     * preview image and video
     *
     * @param previewImages
     * @param position
     */
    public void startPreview(List<LocalMedia> previewImages, int position) {
        LocalMedia media = previewImages.get(position);
        String pictureType = media.getPictureType();
        Bundle bundle = new Bundle();
        List<LocalMedia> result = new ArrayList<>();
        int mediaType = PictureMimeType.isPictureType(pictureType);
        switch (mediaType) {
            case PictureConfig.TYPE_IMAGE:
                // image
                List<LocalMedia> selectedImages = adapter.getSelectedImages();
                ImagesObservable.getInstance().saveLocalMedia(previewImages);
                bundle.putParcelableArrayList(PictureConfig.EXTRA_SELECT_LIST, (ArrayList<? extends Parcelable>) selectedImages);
                bundle.putInt(PictureConfig.EXTRA_POSITION, position);
                bundle.putBoolean(PictureConfig.IS_ORIGINAL, isOriginal);
                startActivity(PicturePreviewActivity.class, bundle, REQUEST_CODE_PICTURE_PREVIEW);
                overridePendingTransition(R.anim.a5, 0);
                break;
            case PictureConfig.TYPE_VIDEO:
                // video
                if (config.selectionMode == PictureConfig.SINGLE) {
                    result.add(media);
                    onResult(result);
                } else {
                    bundle.putString("video_path", media.getPath());
                    startActivity(PictureVideoPlayActivity.class, bundle);
                }
                break;
            case PictureConfig.TYPE_AUDIO:
                // audio
                if (config.selectionMode == PictureConfig.SINGLE) {
                    result.add(media);
                    onResult(result);
                } else {
                    audioDialog(media.getPath());
                }
                break;
        }
    }


    /**
     * change image selector state
     *
     * @param selectImages
     */
    @SuppressLint("SetTextI18n")
    public void changeImageNumber(List<LocalMedia> selectImages) {
        // 如果选择的视频没有预览功能
//        String pictureType = selectImages.size() > 0
//                ? selectImages.get(0).getPictureType() : "";
//        if (config.mimeType == PictureMimeType.ofAudio()) {
//            picture_id_preview.setVisibility(View.GONE);
//        } else {
//            boolean isVideo = PictureMimeType.isVideo(pictureType);
//            boolean eqVideo = config.mimeType == PictureConfig.TYPE_VIDEO;
//            picture_id_preview.setVisibility(isVideo || eqVideo ? View.GONE : View.VISIBLE);
//        }
        boolean enable = selectImages.size() != 0;
        if (enable) {
            btn_send.setEnabled(true);
            picture_id_preview.setEnabled(true);
            picture_id_preview.setText(getString(R.string.preview) + "(" + selectImages.size() + ")");

            if (!TextUtils.isEmpty(config.btnDesc)) {
                btn_send.setText(config.btnDesc);
            } else {
                btn_send.setText(getString(R.string.send) + "(" + selectImages.size() + "/" + config.maxSelectNum + ")");
            }
            btn_send.setTextColor(Color.WHITE);
        } else {
            picture_id_preview.setEnabled(false);
            picture_id_preview.setText(getString(R.string.preview));
            btn_send.setEnabled(false);

            if (!TextUtils.isEmpty(config.btnDesc)) {
                btn_send.setText(config.btnDesc);
            } else {
                btn_send.setText(getString(R.string.send));
            }
            btn_send.setTextColor(Color.GRAY);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && null != data) {
//            List<LocalMedia> medias = new ArrayList<>();
//            LocalMedia media;
//            String imageType;
            switch (requestCode) {
//                case UCrop.REQUEST_CROP:
//                    Uri resultUri = UCrop.getOutput(data);
//                    String cutPath = resultUri.getPath();
//                    if (adapter != null) {
//                        // 取单张裁剪已选中图片的path作为原图
//                        List<LocalMedia> mediaList = adapter.getSelectedImages();
//                        media = mediaList != null && mediaList.size() > 0 ? mediaList.get(0) : null;
//                        if (media != null) {
//                            originalPath = media.getPath();
//                            media = new LocalMedia(originalPath, media.getDuration(), false,
//                                    media.getPosition(), media.getNum(), config.mimeType);
//                            media.setCutPath(cutPath);
//                            media.setCut(true);
//                            imageType = PictureMimeType.createImageType(cutPath);
//                            media.setPictureType(imageType);
//                            medias.add(media);
//                            handlerResult(medias);
//                        }
//                    } else if (config.camera) {
//                        // 单独拍照
//                        media = new LocalMedia(cameraPath, 0, false,
//                                config.isCamera ? 1 : 0, 0, config.mimeType);
//                        media.setCut(true);
//                        media.setCutPath(cutPath);
//                        imageType = PictureMimeType.createImageType(cutPath);
//                        media.setPictureType(imageType);
//                        medias.add(media);
//                        handlerResult(medias);
//                    }
//                    break;
//                case UCropMulti.REQUEST_MULTI_CROP:
//                    List<CutInfo> mCuts = UCropMulti.getOutput(data);
//                    for (CutInfo c : mCuts) {
//                        media = new LocalMedia();
//                        imageType = PictureMimeType.createImageType(c.getPath());
//                        media.setCut(true);
//                        media.setPath(c.getPath());
//                        media.setCutPath(c.getCutPath());
//                        media.setPictureType(imageType);
//                        media.setMimeType(config.mimeType);
//                        medias.add(media);
//                    }
//                    handlerResult(medias);
//                    break;
                case PictureConfig.REQUEST_CAMERA:
                    requestCamera(data);
                    break;
                case REQUEST_CODE_PICTURE_PREVIEW :
                    updateBottom(data);
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (config.camera) {
                closeActivity();
            }
        }
//        else if (resultCode == UCrop.RESULT_ERROR) {
//            Throwable throwable = (Throwable) data.getSerializableExtra(UCrop.EXTRA_ERROR);
//            ToastManage.s(mContext, throwable.getMessage());
//        }
    }

    /**
     * 刷新底部栏
     * @param data 数据
     */
    private void updateBottom(Intent data) {
        boolean original = data.getBooleanExtra(PictureConfig.IS_ORIGINAL, false);
        isOriginal = original;
        if(isOriginal) {
            origin_check_image_iv.setImageResource(R.mipmap.checkbox_selected);
        }else {
            origin_check_image_iv.setImageResource(R.mipmap.checkbox_normal);
        }
    }

    /**
     * 拍照回调
     * @param data 数据
     */
    private void requestCamera(Intent data) {
        List<LocalMedia> medias = new ArrayList<>();
        LocalMedia media;
        if (config.mimeType == PictureMimeType.ofAudio()) {
            cameraPath = getAudioPath(data);
        }
        // on take photo success
        final File file = new File(cameraPath);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        String toType = PictureMimeType.fileToType(file);
        if (config.mimeType != PictureMimeType.ofAudio()) {
            int degree = PictureFileUtils.readPictureDegree(file.getAbsolutePath());
            rotateImage(degree, file);
        }
        // 生成新拍照片或视频对象
        media = new LocalMedia();
        media.setPath(cameraPath);

        boolean eqVideo = toType.startsWith(PictureConfig.VIDEO);
        int duration = eqVideo ? PictureMimeType.getLocalVideoDuration(cameraPath) : 0;
        String pictureType = "";
        if (config.mimeType == PictureMimeType.ofAudio()) {
            pictureType = "audio/mpeg";
            duration = PictureMimeType.getLocalVideoDuration(cameraPath);
        } else {
            pictureType = eqVideo ? PictureMimeType.createVideoType(cameraPath)
                    : PictureMimeType.createImageType(cameraPath);
        }
        media.setPictureType(pictureType);
        media.setDuration(duration);
        media.setMimeType(config.mimeType);

        // 因为加入了单独拍照功能，所有如果是单独拍照的话也默认为单选状态
        if (config.camera) {
            // 如果是单选 拍照后直接返回
            boolean eqImg = toType.startsWith(PictureConfig.IMAGE);
//                        if (config.enableCrop && eqImg) {
//                            // 去裁剪
//                            originalPath = cameraPath;
//                            startCrop(cameraPath);
//                        } else
                if (config.isCompress && eqImg) {
                // 去压缩
                medias.add(media);
                compressImage(medias);
                if (adapter != null) {
                    images.add(0, media);
                    adapter.notifyDataSetChanged();
                }
            } else {
                // 不裁剪 不压缩 直接返回结果
                medias.add(media);
                onResult(medias);
            }
        } else {
            // 多选 返回列表并选中当前拍照的
            images.add(0, media);
            if (adapter != null) {
                List<LocalMedia> selectedImages = adapter.getSelectedImages();
                // 没有到最大选择量 才做默认选中刚拍好的
                if (selectedImages.size() < config.maxSelectNum) {
                    pictureType = selectedImages.size() > 0 ? selectedImages.get(0).getPictureType() : "";
                    boolean toEqual = PictureMimeType.mimeToEqual(pictureType, media.getPictureType());
                    // 类型相同或还没有选中才加进选中集合中
                    if (toEqual || selectedImages.size() == 0) {
                        if (selectedImages.size() < config.maxSelectNum) {
                            // 如果是单选，则清空已选中的并刷新列表(作单一选择)
                            if (config.selectionMode == PictureConfig.SINGLE) {
                                singleRadioMediaImage();
                            }
                            selectedImages.add(media);
                            adapter.bindSelectImages(selectedImages);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
        if (adapter != null) {
            // 解决部分手机拍照完Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
            // 不及时刷新问题手动添加
            manualSaveFolder(media);
            tv_empty.setVisibility(images.size() > 0
                    ? View.INVISIBLE : View.VISIBLE);
        }

        if (config.mimeType != PictureMimeType.ofAudio()) {
            int lastImageId = getLastImageId(eqVideo);
            if (lastImageId != -1) {
                removeImage(lastImageId, eqVideo);
            }
        }
    }

    /**
     * 单选图片
     */
    private void singleRadioMediaImage() {
        if (adapter != null) {
            List<LocalMedia> selectImages = adapter.getSelectedImages();
            if (selectImages != null
                    && selectImages.size() > 0) {
                selectImages.clear();
            }
        }
    }


    /**
     * 手动添加拍照后的相片到图片列表，并设为选中
     *
     * @param media
     */
    private void manualSaveFolder(LocalMedia media) {
        try {
            createNewFolder(foldersList);
            LocalMediaFolder folder = getImageFolder(media.getPath(), foldersList);
            LocalMediaFolder cameraFolder = foldersList.size() > 0 ? foldersList.get(0) : null;
            if (cameraFolder != null && folder != null) {
                // 相机胶卷
                cameraFolder.setFirstImagePath(media.getPath());
                cameraFolder.setImages(images);
                cameraFolder.setImageNum(cameraFolder.getImageNum() + 1);
                // 拍照相册
                int num = folder.getImageNum() + 1;
                folder.setImageNum(num);
                folder.getImages().add(0, media);
                folder.setFirstImagePath(cameraPath);
//                folderWindow.bindFolder(foldersList);
                folderAdapter.setMimeType(config.mimeType);
                folderAdapter.bindFolderData(foldersList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().unregister(this);
        }
        ImagesObservable.getInstance().clearLocalMedia();

        if (animation != null) {
            animation.cancel();
            animation = null;
        }
        if (mediaPlayer != null && handler != null) {
            handler.removeCallbacks(runnable);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                // 拍照
                startOpenCamera();
                break;
            case 1:
                // 录视频
                startOpenCameraVideo();
                break;
        }
    }

    /**
     * 文件夹选择进入动画
     */
    public void enterAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(folder_ll, "alpha", 0, 1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(folder_recycler_view, "translationY", folder_recycler_view.getHeight(), 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.playTogether(alpha, translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    /**
     * 文件夹选择退出动画
     */
    private void exitAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(folder_ll, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(folder_recycler_view, "translationY", 0, folder_recycler_view.getHeight());
        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.playTogether(alpha, translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                folder_ll.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        set.start();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (folder_ll.getVisibility() == View.VISIBLE) {
            exitAnimator();
            return false;
        } else {
            closeActivity();
            return super.onKeyUp(keyCode, event);
        }
    }
}
