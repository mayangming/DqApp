package com.wd.daquan.contacts.activity;

import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.alioss.AliOSS;
import com.wd.daquan.common.alioss.AliOssHelper;
import com.wd.daquan.common.bean.CNOSSFileBean;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.glide.GlideUtils;
import com.meetqs.qingchat.imagepicker.photoview.OnOutsidePhotoTapListener;
import com.meetqs.qingchat.imagepicker.photoview.OnPhotoTapListener;
import com.meetqs.qingchat.imagepicker.photoview.PhotoView;
import com.da.library.controls.custombuttom.CustomButtomDialog;
import com.da.library.controls.custombuttom.bean.CustomButtom;
import com.wd.daquan.model.log.DqToast;
import com.da.library.utils.CommUtil;
import com.wd.daquan.mine.collection.QCCollectionPresenter;
import com.wd.daquan.third.helper.ThirdHelper;
import com.da.library.widget.CommTitle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 备注图片放大
 * Created by Kind on 2019/4/1.
 */
public class RemarksImgZoomAct extends DqBaseActivity<QCCollectionPresenter, DataBean>
        implements View.OnLongClickListener {

    private String toUid;
    private String remarksImg;
    private PhotoView photoView;
    private CommTitle commTitle;

    protected QCCollectionPresenter createPresenter() {
        return new QCCollectionPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.remarks_img_zoom);
    }

    @Override
    protected void initView() {
        toUid = getIntent().getStringExtra(KeyValue.Remark.TO_UID);
        remarksImg = getIntent().getStringExtra(KeyValue.Remark.CARD);

        commTitle = findViewById(R.id.base_title);
        photoView = findViewById(R.id.remarks_img_zoom_photoview);
        photoView.setOnClickListener(this);

        setBackView();
        setTitle("照片详情");
        commTitle.setRightTxt("更多", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            photoView.setTransitionName(KeyValue.Picture.KEY_SHARE_ELEMENT_MARK + remarksImg);
        }
        photoView.setOnLongClickListener(this);
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                supportFinishAfterTransition();
            }
        });
        photoView.setOnOutsidePhotoTapListener(new OnOutsidePhotoTapListener() {
            @Override
            public void onOutsidePhotoTap(ImageView imageView) {
                supportFinishAfterTransition();
            }
        });

        GlideUtils.loadHeader(this, remarksImg, photoView);
    }

    @Override
    public boolean onLongClick(View v) {
        showDialog();
        return false;
    }


    public void showDialog() {
        CustomButtomDialog customButtomDialog = new CustomButtomDialog();

        List<CustomButtom> buttoms = new ArrayList<>();
        buttoms.add(new CustomButtom(0, "保存至相册"));
        buttoms.add(new CustomButtom(1, "收藏图片"));
        buttoms.add(new CustomButtom(2, "删除图片"));
        customButtomDialog.setData(buttoms, true);
        customButtomDialog.setOnItemListener(new CustomButtomDialog.OnItemListener() {
            @Override
            public void onDialogItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomButtom customButtom = (CustomButtom) parent.getAdapter().getItem(position);
                switch (customButtom.id) {
                    case 0:
                        ThirdHelper.savePermUrl(RemarksImgZoomAct.this, remarksImg);
                        break;
                    case 1:
                        addCollectionImg();
                        break;
                    case 2:
                        setResult(-1);
                        finish();
                        break;
                }
            }

            @Override
            public void onDialogClick(View v) {

            }
        });
        customButtomDialog.showDialog((FragmentActivity) getActivity());
    }

    /**
     * 收藏
     */
    private void addCollectionImg() {
        if (CommUtil.isWebHttpUrl(remarksImg)) {//网络图片
            mPresenter.addCollectionImg(toUid, remarksImg);
        } else {
            uploadImg();
        }
    }

    /**
     * 上传图片
     */
    private void uploadImg() {
        AliOssHelper mAliOssHelper = new AliOssHelper();
        mAliOssHelper.setPath(remarksImg);
        mAliOssHelper.setCallback(new AliOSS.AliOssCallback() {
            @Override
            public void onProgress(long curSize, long totalSize) {

            }

            @Override
            public void onSuccess(long totalSize, File file, CNOSSFileBean bean) {
                if (bean != null && !TextUtils.isEmpty(bean.fileName)) {
                    remarksImg = bean.host + bean.fileName;
                    mPresenter.addCollectionImg(toUid, remarksImg);
                } else {
                    DqToast.showShort("收藏失败，请重试！");
                }
            }

            @Override
            public void onFailure() {
                DqToast.showShort("收藏失败，请重试！");
            }
        });
        mAliOssHelper.uploadFile();
    }
}