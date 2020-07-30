package com.meetqs.qingchat.imagepicker;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meetqs.qingchat.imagepicker.adapter.SimpleFragmentAdapter;
import com.meetqs.qingchat.imagepicker.anim.OptAnimationLoader;
import com.meetqs.qingchat.imagepicker.config.PictureConfig;
import com.meetqs.qingchat.imagepicker.entity.EventEntity;
import com.meetqs.qingchat.imagepicker.entity.LocalMedia;
import com.meetqs.qingchat.imagepicker.observable.ImagesObservable;
import com.meetqs.qingchat.imagepicker.rxbus2.RxBus;
import com.meetqs.qingchat.imagepicker.rxbus2.Subscribe;
import com.meetqs.qingchat.imagepicker.rxbus2.ThreadMode;
import com.meetqs.qingchat.imagepicker.tools.ScreenUtils;
import com.meetqs.qingchat.imagepicker.tools.ToastManage;
import com.meetqs.qingchat.imagepicker.tools.VoiceUtils;
import com.meetqs.qingchat.imagepicker.widget.PreviewViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.meetqs.qingchat.imagepicker.config.PictureConfig.SELECT_GIF_MAX_SIZE;
import static com.meetqs.qingchat.imagepicker.config.PictureConfig.SELECT_VIDEO_MAX_SIZE;


/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.ui
 * email：893855882@qq.com
 * data：16/12/31
 * modify 2018/6/28 方志，修改标题栏，底部选择栏，及其他样式背景
 * modify 2018/7/19 方志，限制视频，gif文件大小
 * 加入原图，仿微信页面。
 */
public class PicturePreviewActivity extends PictureBaseActivity implements
        View.OnClickListener, Animation.AnimationListener, SimpleFragmentAdapter.OnCallBackActivity {
    private ImageButton picture_left_back;
    private TextView tv_title;
    private PreviewViewPager viewPager;
    private int position;
    private LinearLayout ll_check;
    private List<LocalMedia> images = new ArrayList<>();
    private List<LocalMedia> selectImages = new ArrayList<>();
    private SimpleFragmentAdapter adapter;
    private Animation animation;
    private boolean refresh;
    private int index;
    private int screenWidth;
    private Handler mHandler;
    private Button btn_send;
    private LinearLayout origin_check_ll;
    private ImageView origin_check_image_iv;
    private TextView image_check;
    private View titleLayout;
    private View bottomLayout;
    private View mWholeView;
    private TextView tv_choose_text;

    /**
     * EventBus 3.0 回调
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(EventEntity obj) {
        switch (obj.what) {
            case PictureConfig.CLOSE_PREVIEW_FLAG:
                // 压缩完后关闭预览界面
                dismissDialog();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                }, 150);
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_preview);
        if (!RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().register(this);
        }
        mHandler = new Handler();
        screenWidth = ScreenUtils.getScreenWidth(this);
        animation = OptAnimationLoader.loadAnimation(this, R.anim.modal_in);
        animation.setAnimationListener(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mWholeView = findViewById(R.id.whole_view);
        titleLayout = findViewById(R.id.id_titleBar);
        picture_left_back = (ImageButton) findViewById(R.id.picture_left_back);
        tv_title = (TextView) findViewById(R.id.picture_title);
        btn_send = (Button)findViewById(R.id.btn_send);

        viewPager = (PreviewViewPager) findViewById(R.id.preview_pager);

        bottomLayout = findViewById(R.id.select_bar_layout);
        origin_check_ll = (LinearLayout) findViewById(R.id.origin_check);
        origin_check_image_iv = (ImageView) findViewById(R.id.origin_check_image_iv);
        ll_check = (LinearLayout) findViewById(R.id.ll_check);
        image_check = (TextView) findViewById(R.id.check);
        tv_choose_text = (TextView)findViewById(R.id.tv_choose_text);

        initStatusBar();
    }

    private void initStatusBar() {
        if(Build.VERSION.SDK_INT >= 11) {
            this.mWholeView.setSystemUiVisibility(1024);
        }
    }

    @TargetApi(11)
    public static int getSmartBarHeight(Context context) {
        try {
            Class c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("mz_action_button_min_height");
            int height = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(height);
        } catch (Exception var5) {
            var5.printStackTrace();
            return 0;
        }
    }

    private void initData() {
        position = getIntent().getIntExtra(PictureConfig.EXTRA_POSITION, 0);
        boolean isOriginal = getIntent().getBooleanExtra(PictureConfig.IS_ORIGINAL, false);
        this.isOriginal = isOriginal;

        if(this.isOriginal) {
            origin_check_image_iv.setImageResource(R.mipmap.checkbox_selected);
        }else {
            origin_check_image_iv.setImageResource(R.mipmap.checkbox_normal);
        }
//        tv_ok.setText(numComplete ? getString(R.string.picture_done_front_num,
//                0, config.selectionMode == PictureConfig.SINGLE ? 1 : config.maxSelectNum)
//                : getString(R.string.picture_please_select));
//
//        tv_img_num.setSelected(config.checkNumMode ? true : false);

        List<LocalMedia> selectImagesList = getIntent().getParcelableArrayListExtra(PictureConfig.EXTRA_SELECT_LIST);
        if(null != selectImagesList) {
            selectImages.clear();
            selectImages.addAll(selectImagesList);
        }

        boolean is_bottom_preview = getIntent().
                getBooleanExtra(PictureConfig.EXTRA_BOTTOM_PREVIEW, false);
        List<LocalMedia> imagesList;
        if (is_bottom_preview) {
            // 底部预览按钮过来
            imagesList = getIntent().getParcelableArrayListExtra(PictureConfig.EXTRA_PREVIEW_SELECT_LIST);
        } else {
            imagesList = ImagesObservable.getInstance().readLocalMedias();
        }

        if(null != imagesList) {
            images.clear();
            images.addAll(imagesList);
        }

        initViewPageAdapterData();
    }

    private void initListener() {

        picture_left_back.setOnClickListener(this);
        origin_check_ll.setOnClickListener(this);
        btn_send.setOnClickListener(this);

        ll_check.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void onClick(View view) {
                if (images != null && images.size() > 0) {
                    LocalMedia image = images.get(viewPager.getCurrentItem());
                    String pictureType = selectImages.size() > 0 ?
                            selectImages.get(0).getPictureType() : "";
//                    if (!TextUtils.isEmpty(pictureType)) {
//                        boolean toEqual = PictureMimeType.
//                                mimeToEqual(pictureType, image.getPictureType());
//                        if (!toEqual) {
//                            ToastManage.s(mContext,getString(R.string.picture_rule));
//                            return;
//                        }
//                    }
                    if(image.getFileSize() > SELECT_VIDEO_MAX_SIZE && pictureType.startsWith("video")) {
                        ToastManage.s(PicturePreviewActivity.this, getString(R.string.video_is_too_big));
                        return;
                    }

                    if(image.getFileSize() > SELECT_GIF_MAX_SIZE && pictureType.endsWith("gif")) {
                        ToastManage.s(PicturePreviewActivity.this, getString(R.string.image_is_too_big));
                        return;
                    }

                    // 刷新图片列表中图片状态
                    boolean isChecked;
                    if (!image_check.isSelected()) {
                        isChecked = true;
                        image_check.setSelected(true);
                        image_check.startAnimation(animation);
                    } else {
                        isChecked = false;
                        image_check.setSelected(false);
                    }
                    if (selectImages.size() >= config.maxSelectNum && isChecked) {
                        ToastManage.s(mContext, getString(R.string.picture_message_max_num, config.maxSelectNum));
                        image_check.setSelected(false);
                        return;
                    }
                    if (isChecked) {
                        VoiceUtils.playVoice(mContext, config.openClickSound);
                        // 如果是单选，则清空已选中的并刷新列表(作单一选择)
                        if (config.selectionMode == PictureConfig.SINGLE) {
                            singleRadioMediaImage();
                        }
                        selectImages.add(image);
                        image.setNum(selectImages.size());
                        if (config.checkNumMode) {
                            image_check.setText(String.valueOf(image.getNum()));
                        }
                    } else {
                        for (LocalMedia media : selectImages) {
                            if (media.getPath().equals(image.getPath())) {
                                selectImages.remove(media);
                                subSelectPosition();
                                notifyCheckChanged(media);
                                break;
                            }
                        }
                    }
                    onSelectNumChange(true);
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                isPreviewEggs(config.previewEggs, position, positionOffsetPixels);
            }

            @SuppressLint("NewApi")
            @Override
            public void onPageSelected(int i) {
                position = i;
                tv_title.setText(position + 1 + "/" + images.size());
                LocalMedia media = images.get(position);
                index = media.getPosition();
                if (!config.previewEggs) {
                    if (config.checkNumMode) {
                        image_check.setText(media.getNum() + "");
                        notifyCheckChanged(media);
                    }
                    onImageChecked(position);
                }

                setClickPermissions(media);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @SuppressLint("NewApi")
    private void setClickPermissions(LocalMedia media) {
        String pictureType = media.getPictureType();
        if (pictureType.endsWith("gif") || pictureType.startsWith("video")) {
            origin_check_ll.setVisibility(View.GONE);
        } else {
            origin_check_ll.setVisibility(View.VISIBLE);
        }

        //限制gif大小，超过200k，不可点击
        if ((media.getFileSize() > SELECT_GIF_MAX_SIZE && pictureType.endsWith("gif"))
                || (media.getFileSize() > SELECT_VIDEO_MAX_SIZE && pictureType.startsWith("video"))){
            ll_check.setClickable(false);
            ll_check.setEnabled(false);
            tv_choose_text.setTextColor(getResources().getColor(R.color.color_69));
        } else {
            ll_check.setClickable(true);
            ll_check.setEnabled(true);
            tv_choose_text.setTextColor(getResources().getColor(R.color.white));
        }
    }

    /**
     * 这里没实际意义，好处是预览图片时 滑动到屏幕一半以上可看到下一张图片是否选中了
     *
     * @param previewEggs          是否显示预览友好体验
     * @param positionOffsetPixels 滑动偏移量
     */
    private void isPreviewEggs(boolean previewEggs, int position, int positionOffsetPixels) {
        if (previewEggs) {
            if (images.size() > 0 && images != null) {
                LocalMedia media;
                int num;
                if (positionOffsetPixels < screenWidth / 2) {
                    media = images.get(position);
                    image_check.setSelected(isSelected(media));
                    if (config.checkNumMode) {
                        num = media.getNum();
                        image_check.setText(num + "");
                        notifyCheckChanged(media);
                        onImageChecked(position);
                    }
                } else {
                    media = images.get(position + 1);
                    image_check.setSelected(isSelected(media));
                    if (config.checkNumMode) {
                        num = media.getNum();
                        image_check.setText(num + "");
                        notifyCheckChanged(media);
                        onImageChecked(position + 1);
                    }
                }
            }
        }
    }

    /**
     * 单选图片
     */
    private void singleRadioMediaImage() {
        if (selectImages != null
                && selectImages.size() > 0) {
            LocalMedia media = selectImages.get(0);
            RxBus.getDefault()
                    .post(new EventEntity(PictureConfig.UPDATE_FLAG,
                            selectImages, media.getPosition()));
            selectImages.clear();
        }
    }

    /**
     * 初始化ViewPage数据
     */
    private void initViewPageAdapterData() {
        tv_title.setText(position + 1 + "/" + images.size());
        adapter = new SimpleFragmentAdapter(images, this, this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        onSelectNumChange(false);
        onImageChecked(position);
        if (images.size() > 0) {
            LocalMedia media = images.get(position);
            index = media.getPosition();
            if (config.checkNumMode) {
                //tv_img_num.setSelected(true);
                image_check.setText(media.getNum() + "");
                notifyCheckChanged(media);
            }
            setClickPermissions(media);
        }
    }

    /**
     * 选择按钮更新
     */
    private void notifyCheckChanged(LocalMedia imageBean) {
        if (config.checkNumMode) {
            image_check.setText("");
            for (LocalMedia media : selectImages) {
                if (media.getPath().equals(imageBean.getPath())) {
                    imageBean.setNum(media.getNum());
                    image_check.setText(String.valueOf(imageBean.getNum()));
                }
            }
        }
    }

    /**
     * 更新选择的顺序
     */
    private void subSelectPosition() {
        for (int index = 0, len = selectImages.size(); index < len; index++) {
            LocalMedia media = selectImages.get(index);
            media.setNum(index + 1);
        }
    }

    /**
     * 判断当前图片是否选中
     *
     * @param position
     */
    public void onImageChecked(int position) {
        if (images != null && images.size() > 0) {
            LocalMedia media = images.get(position);
            image_check.setSelected(isSelected(media));
        } else {
            image_check.setSelected(false);
        }
    }

    /**
     * 当前图片是否选中
     *
     * @param image
     * @return
     */
    public boolean isSelected(LocalMedia image) {
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 更新图片选择数量
     */

    @SuppressLint("SetTextI18n")
    public void onSelectNumChange(boolean isRefresh) {
        this.refresh = isRefresh;
        boolean enable = selectImages.size() != 0;
//        if (enable) {
//
//
//            tv_ok.setSelected(true);
//            id_ll_ok.setEnabled(true);
//            if (numComplete) {
//                tv_ok.setText(getString(R.string.picture_done_front_num, selectImages.size(),
//                        config.selectionMode == PictureConfig.SINGLE ? 1 : config.maxSelectNum));
//            } else {
//                if (refresh) {
//                    tv_img_num.startAnimation(animation);
//                }
//                tv_img_num.setVisibility(View.VISIBLE);
//                tv_img_num.setText(String.valueOf(selectImages.size()));
//                tv_ok.setText(getString(R.string.picture_completed));
//            }
//        } else {
//            id_ll_ok.setEnabled(false);
//            tv_ok.setSelected(false);
//            if (numComplete) {
//                tv_ok.setText(getString(R.string.picture_done_front_num, 0,
//                        config.selectionMode == PictureConfig.SINGLE ? 1 : config.maxSelectNum));
//            } else {
//                tv_img_num.setVisibility(View.INVISIBLE);
//                tv_ok.setText(getString(R.string.picture_please_select));
//            }
//        }

        if (enable) {
            btn_send.setEnabled(true);
            if (TextUtils.isEmpty(config.btnDesc)) {
                btn_send.setText(getString(R.string.send) + "(" + selectImages.size() + "/" + config.maxSelectNum + ")");
            } else {
                btn_send.setText(config.btnDesc+ "(" + selectImages.size() + "/" + config.maxSelectNum + ")");
            }
            btn_send.setTextColor(Color.WHITE);
        } else {
            btn_send.setEnabled(false);
            if (TextUtils.isEmpty(config.btnDesc)) {
                btn_send.setText(getString(R.string.send));
            } else {
                btn_send.setText(config.btnDesc);
            }
            btn_send.setTextColor(Color.GRAY);
        }
        updateSelector(refresh);
    }

    /**
     * 更新图片列表选中效果
     *
     * @param isRefresh
     */
    private void updateSelector(boolean isRefresh) {
        if (isRefresh) {
            EventEntity obj = new EventEntity(PictureConfig.UPDATE_FLAG, selectImages, index);
            RxBus.getDefault().post(obj);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        updateSelector(refresh);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.picture_left_back) {
            onBackPressed();
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
            // 如果设置了图片最小选择数量，则判断是否满足条件
            int size = selectImages.size();
            LocalMedia image = selectImages.size() > 0 ? selectImages.get(0) : null;
            String pictureType = image != null ? image.getPictureType() : "";
            if (config.minSelectNum > 0) {
                if (size < config.minSelectNum && config.selectionMode == PictureConfig.MULTIPLE) {
                    boolean eqImg = pictureType.startsWith(PictureConfig.IMAGE);
                    @SuppressLint("StringFormatMatches")
                    String str = eqImg ? getString(R.string.picture_min_img_num, config.minSelectNum)
                            : getString(R.string.picture_min_video_num, config.minSelectNum);
                    ToastManage.s(mContext,str);
                    return;
                }
            }
            if (config.enableCrop && pictureType.startsWith(PictureConfig.IMAGE)) {
                if (config.selectionMode == PictureConfig.SINGLE) {
                    originalPath = image.getPath();
                    //startCrop(originalPath);
                } else {
                    // 是图片和选择压缩并且是多张，调用批量压缩
                    ArrayList<String> cuts = new ArrayList<>();
                    for (LocalMedia media : selectImages) {
                        cuts.add(media.getPath());
                    }
                    //startCrop(cuts);
                }
            } else {
                onResult(selectImages);
            }
        }
    }

    @Override
    public void onResult(List<LocalMedia> images) {

        int originalTag = isOriginal ? 1 : 0;
        RxBus.getDefault().post(new EventEntity(PictureConfig.PREVIEW_DATA_FLAG, images, originalTag));
        // 如果开启了压缩，先不关闭此页面，PictureImageGridActivity压缩完在通知关闭
        if (!config.isCompress) {
            onBackPressed();
        } else {
            showPleaseDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case UCropMulti.REQUEST_MULTI_CROP:
//                    List<CutInfo> list = UCropMulti.getOutput(data);
//                    setResult(RESULT_OK, new Intent().putExtra(UCropMulti.EXTRA_OUTPUT_URI_LIST,
//                            (Serializable) list));
//                    finish();
//                    break;
//                case UCrop.REQUEST_CROP:
//                    if (data != null) {
//                        setResult(RESULT_OK, data);
//                    }
//                    finish();
//                    break;
//            }
//        } else if (resultCode == UCrop.RESULT_ERROR) {
//            Throwable throwable = (Throwable) data.getSerializableExtra(UCrop.EXTRA_ERROR);
//            ToastManage.s(mContext,throwable.getMessage());
//        }
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent().putExtra(PictureConfig.IS_ORIGINAL, isOriginal));

        closeActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().unregister(this);
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (animation != null) {
            animation.cancel();
            animation = null;
        }
    }

    private Boolean isShowTitlt = true;
    private final String bottomTranslation = "translationY";
    @Override
    public void onActivityBackPressed() {
        //onBackPressed();
        if(mHandler == null) {
            mHandler = new Handler();
        }
        if(isShowTitlt) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setFullScreenAnim(PicturePreviewActivity.this, true);
                }
            }, 300);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setFullScreen(PicturePreviewActivity.this, true);
                }
            }, 400);

            exitAnimator(titleLayout, bottomTranslation, -titleLayout.getHeight());
            exitAnimator(bottomLayout, bottomTranslation, bottomLayout.getHeight());

        }else {
            setFullScreen(PicturePreviewActivity.this, false);
            setFullScreenAnim(PicturePreviewActivity.this, false);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterAnimator(titleLayout, bottomTranslation, -titleLayout.getHeight());
                    enterAnimator(bottomLayout, bottomTranslation, bottomLayout.getHeight());
                }
            }, 100);
        }
        isShowTitlt = !isShowTitlt;

    }

    /**
     * 标题栏和底部栏进入动画
     * @param view 视图控件
     * @param propertyName 动画类型
     * @param height 位移
     */
    public void enterAnimator(View view, String propertyName, int height) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, propertyName, height, 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.play(translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    /**
     * 标题栏和底部栏退出动画
     * @param view 视图控件
     * @param propertyName 动画类型
     * @param height 位移
     */
    private void exitAnimator(View view, String propertyName, int height) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, propertyName, 0, height);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.play(translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

}
