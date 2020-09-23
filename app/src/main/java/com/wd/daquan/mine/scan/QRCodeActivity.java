package com.wd.daquan.mine.scan;

import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.da.library.constant.DirConstants;
import com.da.library.constant.IConstant;
import com.da.library.tools.DensityUtil;
import com.da.library.tools.FileUtils;
import com.da.library.tools.Utils;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.chat.group.bean.QrEntity;
import com.wd.daquan.common.GetImageUtils;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.bean.ShareBean;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.common.presenter.Presenter;
import com.wd.daquan.common.scancode.QRCodeUtils;
import com.wd.daquan.common.utils.CNLog;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.ShareUtil;
import com.wd.daquan.glide.GlideUtils;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.mgr.ModuleMgr;
import com.wd.daquan.model.utils.UIUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;

/**
 * @author: dukangkang
 * @date: 2018/5/16 15:44.
 * @description: 我的二维码页面
 */
public class QRCodeActivity extends DqBaseActivity implements View.OnClickListener {

    // 名称
    private TextView mNameTv;
    // 警告被开启群认证
    private TextView mWarningTv;
    // 底部提示信息
    private TextView mTipsTv;
    private TextView mSaveTv;
    private TextView mShareQSTv;
    private TextView mShareWeixinTv;
    // 头像
    private ImageView mPortraitIv;
    // 性别
    private ImageView mGenderIv;
    // 二维码图片
    private ImageView mQrIv;

    // 分享相关组件
    private View mShareView;
    private TextView mShareNameTv;
    private TextView mShareTipsTv;
    private ImageView mShareQrIv;
    private ImageView mShareGenderIv;
    private ImageView mSharePortraitIv;
    private View mBottomLine;

    private Bitmap mShareBitmap = null;
    private ProgressBar mProgressBar = null;

    // 二维码图片布局
    private LinearLayout mQrLlyt;
    // 按钮父布局
    private LinearLayout mOptionLlyt;

    private RelativeLayout mRootRlyt;

    private QrEntity mQrEntity = null;

    private MediaScannerConnection mMediaScannerConnection = null;

    @Override
    protected void init() {

    }

    @Override
    public Presenter.IPresenter createPresenter() {
        return null;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.qrcode_activity);
    }

    @Override
    public void initView() {
        initIntent();
        mRootRlyt = this.findViewById(R.id.qrcode_rootview);
        mNameTv = this.findViewById(R.id.qrcode_header_name);
        mWarningTv = this.findViewById(R.id.qrcode_warning);
        mTipsTv = this.findViewById(R.id.qrcode_tips);
        mPortraitIv = this.findViewById(R.id.qrcode_header_portrait);
        mGenderIv = this.findViewById(R.id.qrcode_header_gender);
        mQrIv = this.findViewById(R.id.qrcode_image);

        mSaveTv = this.findViewById(R.id.qrcode_save);
        mShareQSTv = this.findViewById(R.id.qrcode_share_qingchat);
        mShareWeixinTv = this.findViewById(R.id.qrcode_share_weixin);

        mQrLlyt = this.findViewById(R.id.qrcode_llyt);
        mOptionLlyt = this.findViewById(R.id.qrcode_option_llyt);

        mBottomLine = this.findViewById(R.id.qrcode_bottom_line);

        mProgressBar = this.findViewById(R.id.qrcode_image_progress);

        initShareView();
    }


    private void initShareView() {
        mShareView = this.findViewById(R.id.qrcode_share_llyt);
        mShareQrIv = mShareView.findViewById(R.id.qrcode_share_image);
        mShareNameTv = mShareView.findViewById(R.id.qrcode_share_header_name);
        mShareGenderIv = mShareView.findViewById(R.id.qrcode_share_header_gender);
        mSharePortraitIv = mShareView.findViewById(R.id.qrcode_share_header_portrait);
        mShareTipsTv = mShareView.findViewById(R.id.qrcode_share_tips);
    }

    private void initIntent() {
        mQrEntity = (QrEntity) getIntent().getSerializableExtra(KeyValue.QR_ENTITY);
    }

    @Override
    public void initListener() {
        toolbarBack();
        mRootRlyt.setOnClickListener(this);
        mSaveTv.setOnClickListener(this);
        mShareQSTv.setOnClickListener(this);
        mShareWeixinTv.setOnClickListener(this);
        mQrLlyt.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (null == mQrEntity) {
            // 单人二维码
            updateSingle();
            updateShareSingle();
            updateQr(getSingleQr());
        } else { // 群聊二维码
            // 未开启群认证
            updateGroup();
        }

        mMediaScannerConnection = new MediaScannerConnection(this, new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                mMediaScannerConnection.scanFile(DirConstants.DIR_CAMERA, "image/png");
            }

            @Override
            public void onScanCompleted(String s, Uri uri) {
                mMediaScannerConnection.disconnect();
            }
        });
    }

    private void updateQr(Bitmap bitmap) {
        mProgressBar.setVisibility(View.GONE);
        if (null != bitmap) {
            mShareQrIv.setImageBitmap(bitmap);
            mQrIv.setImageBitmap(bitmap);
        } else {
            DqToast.showShort(getString(R.string.qrcode_create_fail));
        }
    }

    /**
     * 跟新单人二维码UI
     */
    private void updateSingle() {
        String headPic = ModuleMgr.getCenterMgr().getAvatar();
        String nickName = ModuleMgr.getCenterMgr().getNickName();
        String gender = ModuleMgr.getCenterMgr().getSex();
        mNameTv.setText(nickName);
        if ("1".equals(gender)) {
            mShareGenderIv.setImageResource(R.mipmap.icon_male);
        } else if("2".equals(gender)) {
            mShareGenderIv.setImageResource(R.mipmap.icon_female);
        }else {
            mShareGenderIv.setVisibility(View.GONE);
        }
        GlideUtils.loadHeader(this, headPic + DqUrl.url_avatar_suffix, mPortraitIv);
    }

    /**
     * 单人分享内容
     */
    private void updateShareSingle() {
        String headPic = ModuleMgr.getCenterMgr().getAvatar();
        String nickName = ModuleMgr.getCenterMgr().getNickName();
        String gender = ModuleMgr.getCenterMgr().getSex();

        mShareTipsTv.setText(getString(R.string.qrcode_share_add));
        mShareNameTv.setText(nickName);
        if ("1".equals(gender)) {
            mShareGenderIv.setImageResource(R.mipmap.icon_male);
        } else if("2".equals(gender)) {
            mShareGenderIv.setImageResource(R.mipmap.icon_female);
        }else {
            mShareGenderIv.setVisibility(View.GONE);
        }
        GlideUtils.loadHeader(this, headPic + DqUrl.url_avatar_suffix, mSharePortraitIv);
    }

    /**
     * 更新群组UI
     */
    private void updateGroup() {
        if (TextUtils.isEmpty(mQrEntity.examine) || "1".equals(mQrEntity.examine)) {
            mTipsTv.setText(getString(R.string.qrcode_group_add));
            mQrIv.setVisibility(View.VISIBLE);
            mWarningTv.setVisibility(View.GONE);
            mOptionLlyt.setVisibility(View.VISIBLE);
            mBottomLine.setVisibility(View.VISIBLE);
            updateQr(getGroupQr());
        } else {
            mProgressBar.setVisibility(View.GONE);
            mQrIv.setVisibility(View.GONE);
            mWarningTv.setVisibility(View.VISIBLE);
            mTipsTv.setText("");
            mOptionLlyt.setVisibility(View.GONE);
            mBottomLine.setVisibility(View.GONE);
        }

        mNameTv.setText(mQrEntity.groupName);
        GlideUtils.loadHeader(this, mQrEntity.groupPic, mPortraitIv);

        // 分享内容
        mShareTipsTv.setText(getString(R.string.qrcode_share_group_add));
        mShareNameTv.setText(mQrEntity.groupName);
        GlideUtils.loadHeader(this, mQrEntity.groupPic, mSharePortraitIv);
    }

    /**
     * 获取单人二维码
     *
     * @return
     *  返回二维码图片
     */
    private Bitmap getSingleQr() {
        String result = "";

        String uid = ModuleMgr.getCenterMgr().getUID();
        if(TextUtils.isEmpty(uid)) {
            return null;
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put(KeyValue.QR.UID, uid);
            obj.put(KeyValue.QR.TYPE, KeyValue.QRType.PERSION);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String json = obj.toString();
        Log.i("fz", "单人信息：json : " + json);
        try {
            result = KeyValue.RECEIPT_QR_CODE + URLEncoder.encode(json, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(result)) {
            return null;
        }
        int width = UIUtil.dip2px(234);
        Log.i("fz", "单人信息：result : " + result);
        //EBLog.w("xxxx", "width: " + width);
        return QRCodeUtils.createQRImage(result, width, width);
    }

    /**
     * 获取群组二维码
     *
     * @return
     *  返回二维码图片
     */
    private Bitmap getGroupQr() {
        String uid = ModuleMgr.getCenterMgr().getUID();
        String result = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put(KeyValue.QR.TYPE, KeyValue.QRType.GROUP);
            if (!TextUtils.isEmpty(mQrEntity.targetId)) {
                obj.put(KeyValue.QR.GROUP_ID, mQrEntity.targetId);
            }
            if (!TextUtils.isEmpty(uid)) {
                obj.put(KeyValue.QR.SOURCE_UID, uid);
            }
            CNLog.w("xxxx", "群组信息：" + obj.toString());
//            String json = EasyAES.encryptString(obj.toString());
            String json = obj.toString();
            result = KeyValue.RECEIPT_QR_CODE + URLEncoder.encode(json, "utf-8");
//            EBLog.w("xxxx", "群组密文：" + result);
            CNLog.w("YM", "群组密文：" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        int width = DensityUtil.dip2px(this, 234);
        return QRCodeUtils.createQRImage(result, width, width);
    }

    /**
     * 分享斗圈
     *
     * @param uri
     *  分享文件
     */
    private void shareQingChat(Uri uri) {
        if (null == uri) {
            return;
        }

        ShareBean shareBean = new ShareBean();
        shareBean.enterType = IConstant.Share.SHARE;
        shareBean.textOrImage = IConstant.Share.IMAGE;
        shareBean.path = uri.toString();
        NavUtils.gotoShareActivity(this, shareBean);
    }

    /**
     * 分享到微信
     */
    private void shareWeixin() {
        if (null == mShareBitmap) {
            mShareBitmap = GetImageUtils.convertViewToBitmap(mShareView);
        }
//        ShareUtil.shareWx(this, mShareBitmap);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mRootRlyt.getId()) {
            finish();
        } else if (id == mSaveTv.getId()) {
            if (Utils.isFastDoubleClick(1000)) {
                CNLog.w("xxxx", "重复点击");
                return;
            }
            File file = saveQrCodeImage();
            if (null == file) {
                DqToast.showShort(getString(R.string.save_fail));
                return;
            }

            DqToast.showShort(getString(R.string.pic_save_local) + file.getPath());
        } else if (id == mShareQSTv.getId()) {
            Uri uri = saveQrCodeUri();
            shareQingChat(uri);
        } else if (id == mShareWeixinTv.getId()) {
            shareWeixin();
        }
    }

    /**
     * 保存二维码图片
     */
    private File saveQrCodeImage() {
        Bitmap bitmap = GetImageUtils.getBtimap(mShareView);

        File file = FileUtils.saveBitmap(DqApp.sContext, bitmap);
        //存入相册
        FileUtils.savePhotoAlbum(DqApp.sContext, file);

        bitmap.recycle();
        return file;
    }

    private Uri saveQrCodeUri(){
        Bitmap bitmap = GetImageUtils.getBtimap(mShareView);
        return com.wd.daquan.util.FileUtils.saveBitmap(bitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
