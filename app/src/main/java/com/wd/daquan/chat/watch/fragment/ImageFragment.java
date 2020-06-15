package com.wd.daquan.chat.watch.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.da.library.constant.IConstant;
import com.da.library.tools.FileUtils;
import com.da.library.tools.ThreadTools;
import com.da.library.widget.CommonListDialog;
import com.da.library.widget.MainLoading;
import com.dq.im.bean.im.MessagePhotoBean;
import com.dq.im.model.IMContentDataModel;
import com.meetqs.qingchat.carema.view.CircleProgressView;
import com.meetqs.qingchat.imagepicker.photoview.PhotoView;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.watch.PictureEntity;
import com.wd.daquan.common.alioss.AliOssHelper;
import com.wd.daquan.common.bean.ShareBean;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.fragment.BaseFragment;
import com.wd.daquan.common.helper.QrcodeHelper;
import com.wd.daquan.common.listener.QrCodeListener;
import com.wd.daquan.common.scancode.QRCodeUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.glide.progress.OnProgressListener;
import com.wd.daquan.mine.collection.QCCollectionPresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author: dukangkang
 * @date: 2018/7/5 15:35.
 * @description: 2019/5/10 fangzhi重构
 */
public class ImageFragment extends BaseFragment<QCCollectionPresenter, DataBean> implements View.OnLongClickListener {

//    public static final int MSG_QR_UPDATE = 1;
//    private static final int BASIC_PERMISSION_REQUEST_CODE = 100;
//    private String DIRRECTORY = "/qc/";
//    private int mPosition = 0;
//    private boolean isCurrent = false;

    private String mUrl = "";

    private String mPath;

    private String mThumbPath;

    private PictureEntity mPictureEntity = null;

    private PhotoView mPhotoView = null;

    private CommonListDialog mCommonListDialog;

    private MainLoading mLoadingDialog = null;
    private CircleProgressView mProgressView;
    private AliOssHelper aliOssHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.watch_image_item;
    }

    @Override
    public QCCollectionPresenter createPresenter() {
        return new QCCollectionPresenter();
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        mProgressView = view.findViewById(R.id.progress_view);
        mPhotoView = getView().findViewById(R.id.rc_photoView);
    }

    private void initIntent() {
        Bundle bundle = getArguments();
        if (null != bundle) {
//            mPosition = bundle.getInt(KeyValue.Picture.KEY_POSITION);
//            isCurrent = bundle.getBoolean(KeyValue.Picture.KEY_IS_CURRENT);
            mPictureEntity = bundle.getParcelable(KeyValue.Picture.KEY_ENTITY);
            assert mPictureEntity != null;
            mPath = mPictureEntity.path;
            mThumbPath = mPictureEntity.thumbPath;
            try {
                IMContentDataModel msgAttachment = mPictureEntity.message.getContentData();
                if (null != msgAttachment) {
                    MessagePhotoBean imageAttachment = (MessagePhotoBean) msgAttachment;
                    mUrl = imageAttachment.getDescription();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        initIntent();
        initCollection();
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePhotoView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPhotoView.setTransitionName(KeyValue.Picture.KEY_SHARE_ELEMENT_MARK + mPictureEntity.messageId);
        }
        mPhotoView.setOnLongClickListener(this);
        mPhotoView.setOnPhotoTapListener((view, x, y) -> {
            if(getActivity() == null) {
                return;
            }
            getActivity().supportFinishAfterTransition();
        });
        mPhotoView.setOnOutsidePhotoTapListener(imageView -> {
            if(getActivity() == null) {
                return;
            }
            getActivity().supportFinishAfterTransition();
        });
    }

    private void updatePhotoView() {
        if (mPictureEntity == null) {
            return;
        }
        if (TextUtils.isEmpty(mUrl)) {
            if (!TextUtils.isEmpty(mThumbPath)) {
                mUrl = mThumbPath;
            } else {
                mUrl = mPath;
            }
        }

        GlideUtils.loadProgress(DqApp.sContext, mUrl, mPhotoView, new OnProgressListener() {
            @Override
            public void onProgress(boolean isComplete, int percent, long curBytes, long totalBytes) {
                DqApp.runOnUiThread(() -> {
                    if (mProgressView == null) {
                        return;
                    }
                    mProgressView.setValue(percent);
                    mProgressView.setVisibility(isComplete ? View.GONE : View.VISIBLE);
                });
            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        showDialog();

        Bitmap tmpBitmap = getBitmap();
        if(tmpBitmap == null) {
            return true;
        }

        QRCodeUtils.getResult(tmpBitmap, new QRCodeUtils.AnalyzeCallback(){

            @Override
            public void onSuccess(Bitmap mBitmap, String result) {
                DqApp.runOnUiThread(() -> {
                    if (null != mCommonListDialog) {
                        if (isAdded()) {
                            mCommonListDialog.setItem(DqApp.getStringById(R.string.message_option_qrcode1));
                            mCommonListDialog.notifyChange();
                        }
                    }
                }, 500);
            }

            @Override
            public void onFailed() {

            }
        });

        return true;
    }

    private void showDialog() {
        List<String> list = new ArrayList<>();
        list.add(DqApp.getStringById(R.string.message_option_save_image));
        list.add(DqApp.getStringById(R.string.message_option_transfer));
        list.add(DqApp.getStringById(R.string.collection));

        if(getActivity() == null) {
            return;
        }
        mCommonListDialog = new CommonListDialog(getActivity());
        mCommonListDialog.setItems(list);
        mCommonListDialog.show();
        mCommonListDialog.setListener((String item, int position) -> {
            switch (position) {
                case 0: // 保存图片
                    saveFile();
                    break;
                case 1: // 转发
                    doTransfer();
                    break;
                case 2: // 收藏
                    DqToast.showShort(DqApp.getStringById(R.string.no_this_function));
                    //doCollection();
                    break;
                case 3: // 识别二维码
                    if (null == mLoadingDialog) {
                        MainLoading.Builder builder = new MainLoading.Builder(getActivity())
                                .setMessage("")
                                .setCancelable(false)
                                .setShowMessage(false);
                        mLoadingDialog = builder.create();
                    }
                    if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
                        mLoadingDialog.show();
                    }
                    doQrcode();
                    break;
            }
        });
    }

    /**
     * 保存图片
     *
     */
    private void saveFile() {

        Bitmap tmpBitmap = getBitmap();

        if(tmpBitmap == null) {
            DqToast.showShort("图片保存失败");
        }

        ThreadTools.newThread(() -> {
            File file = FileUtils.saveBitmap(DqApp.sContext, tmpBitmap);
            //存入相册
            FileUtils.savePhotoAlbum(DqApp.sContext, file);

            DqApp.runOnUiThread(() -> DqToast.showShort(DqApp.getStringById(R.string.message_option_save_tips)));
        });

    }

    private Bitmap getBitmap() {
        if(mPhotoView == null) {
            return null;
        }
        mPhotoView.setDrawingCacheEnabled(true);
        Bitmap tmpBitmap = Bitmap.createBitmap(mPhotoView.getDrawingCache());
        mPhotoView.setDrawingCacheEnabled(false);
        return tmpBitmap;
    }

    /**
     * 转发
     */
    private void doTransfer() {
        if (mPictureEntity == null || mPictureEntity.message == null) {
            DqToast.showShort("转发图片失败");
            return;
        }

        ShareBean shareBean = new ShareBean();
        shareBean.enterType = IConstant.Share.FORWARDING;
        shareBean.imMessage = mPictureEntity.message;

        if(getActivity() == null) {
            DqToast.showShort("转发图片失败");
            return;
        }
        NavUtils.gotoShareActivity(getActivity(), shareBean);
    }


    //初始化收藏相关
    private void initCollection(){
        aliOssHelper = new AliOssHelper();
    }

    /**
     * 收藏消息
     *  当前消息内容
     * @param content
     *  消息图片本地路径
     */
    private void collection(String sendId, String messageId, String type, String content) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("from_uid", sendId);
        hashMap.put("message_id", messageId);
        hashMap.put("type", type); //type类型：1：文本 2：语音 3：图片 4：视频 5：文件
        hashMap.put("content", content);
        hashMap.put("extra", "");
        Log.e("TAG", "collection ： " + hashMap.toString());
        mPresenter.requestAddCollection(DqUrl.url_collection_add, hashMap);
    }


    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (DqUrl.url_collection_add.equals(url)) {
            DqToast.showShort("收藏成功");
        }
    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {
        if(DqUrl.url_collection_add.equals(url)){
            //收藏
            if(entity != null && !TextUtils.isEmpty(entity.content)){
                DqToast.showShort(entity.content);
                return;
            }
            DqToast.showShort("收藏失败，请重试");
        }
    }

    /**
     * 识别二维码
     *
     */
    private void doQrcode() {
        Bitmap tmpBitmap = getBitmap();
        if(tmpBitmap == null) {
            DqToast.showShort(DqApp.getStringById(R.string.message_option_qrcode_error));
        }

        QRCodeUtils.getResult(tmpBitmap, new QRCodeUtils.AnalyzeCallback() {
            @Override
            public void onSuccess(Bitmap mBitmap, String result) {
                DqApp.runOnUiThread(() -> {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    QrcodeHelper qrcodeHelper = new QrcodeHelper(getActivity());
                    qrcodeHelper.setQrCodeListener(mQrCodeListener);
                    qrcodeHelper.distinguishQrcode(getActivity(), result);
                });
            }

            @Override
            public void onFailed() {
                DqApp.runOnUiThread(() -> {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    DqToast.showShort(DqApp.getStringById(R.string.message_option_qrcode_error));
                });
            }
        });
    }

    @Override
    public View getSharedElement() {
        return mPhotoView;
    }

//    @Override
//    public void updateShareTransition() {
//        super.updateShareTransition();
//        if (null != mPhotoView) {
//            mPhotoView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                @Override
//                public boolean onPreDraw() {
//                    if (null != mPhotoView) {
//                        mPhotoView.getViewTreeObserver().removeOnPreDrawListener(this);
//                        if (null != getActivity()) {
//                            getActivity().supportStartPostponedEnterTransition();
//                        }
//                    }
//                    return true;
//                }
//            });
//        }
//    }

    private QrCodeListener mQrCodeListener = new QrCodeListener() {
        @Override
        public void notFound() {
            ImageFragment.this.finish();
            DqToast.showShort(DqApp.getStringById(R.string.qr_code_no_find));
        }

        @Override
        public void resume() {

        }

        @Override
        public void finish() {
            ImageFragment.this.finish();
        }

        @Override
        public void retryScan() {

        }
    };

    void finish(){
        if(getActivity() == null) {
            return;
        }
        getActivity().finish();
    }
}
