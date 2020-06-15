package com.wd.daquan.mine.activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.da.library.listener.DialogListener;
import com.da.library.widget.CommTitle;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.helper.UpdateHelper;
import com.wd.daquan.common.helper.UpdateListener;
import com.wd.daquan.common.utils.DialogUtils;
import com.wd.daquan.common.utils.DqUtils;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.mine.presenter.MinePresenter;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.bean.UpdateEntity;
import com.wd.daquan.model.log.DqToast;


public class AboutQSActivity extends DqBaseActivity<MinePresenter, DataBean> implements UpdateListener, View.OnClickListener {
    private TextView txt_versionName;
    private LinearLayout newVersion_layout;
    private LinearLayout user_privacy_protocol;
    private LinearLayout user_service_protocol;
    private ImageView img_redDot;
    private UpdateEntity mUpdateEntity = null;
    private UpdateHelper mUpdateHelper = null;
    private LinearLayout error_layout;
    private CommTitle mCommTitle;

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_about_qs);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mCommTitle = findViewById(R.id.aboutActivityCommtitle);
        txt_versionName = findViewById(R.id.aboutActivityTxtAppName);
        img_redDot = findViewById(R.id.aboutActivityImgRedDot);
        newVersion_layout = findViewById(R.id.aboutActivityLayoutNewVersion);
        error_layout = findViewById(R.id.aboutActivityErrorLayout);
        user_privacy_protocol = findViewById(R.id.user_privacy_protocol);
        user_service_protocol = findViewById(R.id.user_service_protocol);
        mCommTitle.setTitle(R.string.about);

        error_layout.setVisibility(View.GONE);
        mUpdateHelper = new UpdateHelper(this);
        mUpdateHelper.setUpdateListener(this);
        mUpdateHelper.setShowDialog(false);
    }

    @Override
    protected void initListener() {
        newVersion_layout.setOnClickListener(this);
        error_layout.setOnClickListener(this);
        mCommTitle.getLeftIv().setOnClickListener(this);
        user_privacy_protocol.setOnClickListener(this);
        user_service_protocol.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        String name=getString(R.string.app_name)+ DqUtils.getVersion(this);
        txt_versionName.setText(name);
        mUpdateHelper.checkVersion();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comm_left_iv:
                finish();
                break;
            case R.id.aboutActivityLayoutNewVersion://检查新版本
//                QCToast.showShort(DqApp.getStringById(R.string.no_this_function));
                if (mUpdateEntity != null) {
                    if ("1".equals(mUpdateEntity.update_status)) {
                        DialogUtils.showCheckDialogs(new DialogListener() {
                                                         @Override
                                                         public void onCancel() {}
                                                         @Override
                                                         public void onOk() {}}, this, mUpdateEntity.appVersion, mUpdateEntity.versionName,
                                mUpdateEntity.content, mUpdateEntity.upgradeType, mUpdateEntity.url).show();
                    } else if ("0".equals(mUpdateEntity.update_status)) { // 最新版本
                        DqToast.showShort(getString(R.string.about_check_current_new_version));
                    }
                }
                break;
            case R.id.aboutActivityErrorLayout:
//                LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
//                mPresenter.getAliOssToken(URLUtil.url_alioss_token, linkedHashMap);

                break;
            case R.id.user_privacy_protocol:
//                LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
//                mPresenter.getAliOssToken(URLUtil.url_alioss_token, linkedHashMap);
                NavUtils.gotoWebviewActivity(this, DqUrl.url_privacy_agreement, getString(R.string.privacyUser));
                break;
            case R.id.user_service_protocol:
                NavUtils.gotoWebviewActivity(this, DqUrl.url_register_agreement, getString(R.string.serviceUser));
//                LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
//                mPresenter.getAliOssToken(URLUtil.url_alioss_token, linkedHashMap);

                break;
        }
    }



    @Override
    public void updateError() {

    }

    @Override
    public void updateFailed(String msg) {
        DqToast.showShort(msg);
    }
    @Override
    public void updateSucceed(UpdateEntity updateEntity) {
        this.mUpdateEntity = updateEntity;
        Log.e("YM","请求结果:"+mUpdateEntity.toString());
    }

    @Override
    public void updateUI(String status) {
        if ("1".equals(status)) {
            img_redDot.setVisibility(View.VISIBLE);
        }else if ("0".equals(status)){
            img_redDot.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUpdateHelper.destory();
    }

    //报错相关
//    @Override
//    public void onSuccess(String url, int code, DataBean entity) {
//        super.onSuccess(url, code, entity);
//        if(URLUtil.url_alioss_token.equals(url)){
//            if(0 == code){
//                AliOssResp aliOssResp = (AliOssResp) entity.intentUrl;
//                CNAliOSSClient.getInstance(AboutActivity.this).setOssData(aliOssResp);
//                CNOSSFileBean cnossFileBean = new CNOSSFileBean();
//                cnossFileBean.fileName = "log/android/" + EBApplication.getInstance().getCurrentId() + "/rong";
//                if(ZipHelper.zipFiles(getFileArray(), DirConstants.DIR_ERROR_LOG)){
//                    CNLog.d("clll", "错误日志压缩成功-----");
//                    cnossFileBean.localFile = DirConstants.DIR_ERROR_LOG;
//                }
//                CNAliOSSClient.getInstance(AboutActivity.this).uploadFile(aliOssResp.bucket, cnossFileBean, mIUploadMediaCallback);
//            }
//        }
//    }
//
//    private File[] getFileArray(){
//        File[] files = null;
//        String shareFilePath = getApplicationContext().getFilesDir().getParent() + "/shared_prefs/" +
//                SharePreferenceUtils.DELETE_MESSAGE_FILENAME + ".xml";
//        File shareFile = new File(shareFilePath);
//        if(shareFile.exists()){
//            CNLog.d("clll", "shared_prefs:" + shareFilePath);
//        }
//        String rongLog_path = Environment.getExternalStorageDirectory() + "/Android/intentUrl/" + KeyValue.PACKAGE_NAME +
//                "/files/rong_log";
//        File rongLog_file = new File(rongLog_path);
//        if(rongLog_file.exists()){
//            CNLog.d("clll", "rongLog_path:" + rongLog_path.toString());
//            if(shareFile.exists()){
//                files = new File[]{shareFile, rongLog_file};
//            }else{
//                files = new File[]{rongLog_file};
//            }
//
//        }
//        return files;
//    }
//    private CNAliOSSClient.IUploadMediaCallback mIUploadMediaCallback = new CNAliOSSClient.IUploadMediaCallback() {
//        @Override
//        public void onProgress(long curSize, long totalSize) {
//
//        }
//
//        @Override
//        public void onSuccess(long totalSize, CNOSSFileBean bean) {
//            CNLog.d("clll", "错误日志上传成功");
//            mHandler.sendEmptyMessage(0);
//        }
//
//        @Override
//        public void onFailure() {
//
//        }
//    };
//
//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message content) {
//            super.handleMessage(content);
//            showConfirmDialog(AboutActivity.this);
//        }
//    };
//
//    private void showConfirmDialog(Context context){
//        //上传成功之后删除一波
//        getSharedPreferences(SharePreferenceUtils.DELETE_MESSAGE_FILENAME, Context.MODE_PRIVATE).edit().clear().commit();
//        final CommDialog confirmDialog = new CommDialog(context);
//        confirmDialog.setTitleVisible();
//        confirmDialog.setDesc(context.getString(R.string.upload_error));
//        confirmDialog.setSingleBtn();
//        confirmDialog.setOkTxt(context.getString(R.string.confirm));
//        confirmDialog.setOkTxtColor(context.getResources().getColor(R.color.red));
//        confirmDialog.show();
//
//        confirmDialog.setDialogListener(new DialogListener() {
//            @Override
//            public void onCancel() {
//                confirmDialog.dismiss();
//            }
//
//            @Override
//            public void onOk() {
//                confirmDialog.dismiss();
//            }
//        });
//    }
}
