package com.wd.daquan.login.activity;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.da.library.tools.ActivitysManager;
import com.wd.daquan.DqApp;
import com.wd.daquan.R;
import com.wd.daquan.common.activity.DqBaseActivity;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqToast;
import com.wd.daquan.model.sp.EBSharedPrefUser;
import com.wd.daquan.model.sp.QCSharedPrefManager;
import com.wd.daquan.common.utils.InstallApkUtil;
import com.wd.daquan.common.utils.NavUtils;
import com.wd.daquan.common.utils.NetWorkUtils;
import com.da.library.constant.DirConstants;
import com.da.library.constant.IConstant;
import com.da.library.listener.DialogListener;
import com.wd.daquan.login.helper.CommDialogHelper;
import com.wd.daquan.login.helper.LoginHelper;
import com.wd.daquan.login.presenter.SplashPresenter;
import com.wd.daquan.net.listener.IProgressListener;
import com.wd.daquan.net.progress.ProgressDownloader;
import com.netease.nim.uikit.common.util.file.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static android.view.View.GONE;


/**
 * @Author: 方志
 * @Time: 2018/9/19 10:27
 * @Description: app下载
 */
@SuppressLint("SetTextI18n")
public class DownAppActivity extends DqBaseActivity<SplashPresenter, DataBean> implements View.OnClickListener, IProgressListener {

    private View mDownDialogLl;
    private ProgressBar mProgressBar;
    private TextView mProgressTv;
    private TextView mCurrentTv;
    private TextView mTotalTv;
    private TextView mCancelTv;

    private String mUrl = "https://sy-pro-oss.oss-cn-hangzhou.aliyuncs.com/clients/chuiniu-1.8.3-20180912.apk";
    private long breakPoints;
    private ProgressDownloader downloader;
    private long totalBytes = 0;
    private long contentLength = 0;
    private int progressPercent = 0;
    private String mUpdateType;
    private String mVersion;
    private File file;
    private String fileName;

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_down_app);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
        mDownDialogLl = findViewById(R.id.down_dialog_ll);
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressTv = findViewById(R.id.progress_tv);
        mCurrentTv = findViewById(R.id.current_size_tv);
        mTotalTv = findViewById(R.id.total_size_tv);
        mCancelTv = findViewById(R.id.cancel_tv);
    }

    @Override
    protected void initData() {
        getIntentData();
        initProgressBar();
        //判断是否在WiFi状态下
        determineNetwork();
    }

    private void initProgressBar() {
        mProgressTv.setText(getString(R.string.current_progress) + progressPercent + " %");
        mCurrentTv.setText(FileUtil.formatFileSize(totalBytes, FileUtil.SizeUnit.MB));
        mTotalTv.setText(" / " + FileUtil.formatFileSize(contentLength, FileUtil.SizeUnit.MB));
    }

    private void getIntentData() {
        mUpdateType = getIntent().getStringExtra(IConstant.Update.UPDATETYPE);
        mUrl = getIntent().getStringExtra(IConstant.Update.URL);
        mVersion = getIntent().getStringExtra(IConstant.Update.VERSION);
    }

    private void determineNetwork() {
        initDown();
//        int networkType = NetWorkUtils.getNetworkTypeName(this);
//        if (ConnectivityManager.TYPE_WIFI == networkType) {
//            initDown();
//        } else if (-1 != networkType) {
//            mDownDialogLl.setVisibility(GONE);
//
//            CommDialogHelper.getInstance()
//                    .showNotWifiDialog(this)
//                    .setDialogListener(new DialogListener() {
//                        @Override
//                        public void onCancel() {
//                            if ("1".equals(mUpdateType)) {
//                                login();
//                            } else {
//                                ActivitysManager.getInstance().finishAll();
//                            }
//                        }
//
//                        @Override
//                        public void onOk() {
//                            initDown();
//                        }
//                    });
//        } else {
//            DqToast.showShort(getString(R.string.comm_network_error));
//        }
    }

    private void initDown() {
        breakPoints = 0L;
        fileName = mUrl.substring(mUrl.lastIndexOf("/") + 1, mUrl.length());
        if (TextUtils.isEmpty(fileName) && !fileName.contains(".apk")) {
            fileName = getPackageName() + ".apk";
        }
        File fileDir = new File(DirConstants.DIR_WORK);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        file = new File(fileDir, fileName);
        downloader = new ProgressDownloader(mUrl, file, this);
        downloader.download(0L);

        //下载统计
//        Map<String, String> hashMap = new HashMap<>();
////        hashMap.put(IConstant.Update.DEVEICE_TYPE, IConstant.Update.ANDROID);
//        hashMap.put(IConstant.Update.DECEIVE_TYPE, IConstant.Update.ANDROID);
//        hashMap.put(IConstant.Update.APP_VERSION, mVersion);
//        mPresenter.recordUpgradeData(DqUrl.url_recordUpgradeData, hashMap);
    }

    private void login() {
        EBSharedPrefUser userInfoSp = QCSharedPrefManager.getInstance().getKDPreferenceUserInfo();
        String uid = userInfoSp.getString(EBSharedPrefUser.uid, "");
        String im_token = userInfoSp.getString(EBSharedPrefUser.im_token, "");
        if (TextUtils.isEmpty(im_token)) {
            NavUtils.gotoLoginRegisterActivity(this);
            finish();
        } else {
            if (!TextUtils.isEmpty(uid)) {
                LoginHelper.login(this, uid, im_token);
            }
        }
    }

    @Override
    protected void initListener() {
        mCancelTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_tv:
                if (null != file && null != downloader) {
                    downloader.pause();
                    boolean delete = file.delete();
                    if (delete) {
                        DqToast.showShort(getString(R.string.cancel_upgrade));
                    }
                }
                login();
                break;
        }
    }

    @Override
    public void onPreExecute(long contentLength) {
        // 文件总长只需记录一次，要注意断点续传后的contentLength只是剩余部分的长度
        if (this.contentLength == 0L) {
            this.contentLength = contentLength;

            DqApp.getInstance().runInUIThread(() -> {
                mProgressBar.setMax((int) (contentLength));
                mTotalTv.setText(" / " + FileUtil.formatFileSize(contentLength, FileUtil.SizeUnit.MB));
            });
        }
    }

    @Override
    public void update(long totalBytes, boolean done) {
        // 注意加上断点的长度
        this.totalBytes = totalBytes + breakPoints;
        // 切换到主线程
        int progress = (int) (totalBytes + breakPoints);
        progressPercent = (int) ((float) progress / contentLength * 100);
        DqApp.getInstance().runInUIThread(() -> {
            mProgressBar.setProgress(progress);
            mProgressTv.setText(getString(R.string.current_progress) + progressPercent + " %");
            mCurrentTv.setText(FileUtil.formatFileSize(totalBytes, FileUtil.SizeUnit.MB));
        });
        if (done) {
            DqApp.getInstance().runInUIThread(() -> {
                DqToast.showShort("下载完成");
                InstallApkUtil.installApk(new File(DirConstants.DIR_WORK, fileName));
                finish();
            });
        }
    }

    @Override
    public void onError(Call call, IOException e) {
        DqToast.showShort(getString(R.string.down_error));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommDialogHelper.getInstance().onDestroy();
    }
}
