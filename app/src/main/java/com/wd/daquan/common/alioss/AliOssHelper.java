package com.wd.daquan.common.alioss;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.wd.daquan.common.bean.AliOssResp;
import com.wd.daquan.common.bean.CNOSSFileBean;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.common.constant.KeyValue;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.common.presenter.Presenter;
import com.da.library.tools.AppInfoUtils;
import com.da.library.tools.MD5;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: dukangkang
 * @date: 2018/9/11 19:08.
 * @description: 添加文件下载路径，文件格式，修改baseCall为AliOSS.AliOssCallback，
 *              文件上传下载获取token去掉传递message
 */
public class AliOssHelper implements Presenter.IView<DataBean> {

    private String mPath = "";

    private String mFileName = "";

    private String mFileWebUrl = "";

    /**
     * 文件上传目录
     */
    private String mMediaDirectory = "";

    private long mCurDownloadSize = 0;

    private AliOSS mCNAliOSS = null;
    private AliOssPresenter mPresenter = null;

    /**
     *  默认为上传 true下载/false上传
     */
    private boolean isDownload = false;
    /**
     * 文件夹目录
     */
    private String mFileDir = "";
    /**
     * 文件的下载格式
     */
    private String mFileFormat = "";

    public AliOssHelper() {
        init();
    }

    private void init() {
        mCNAliOSS = new AliOSS();
        mPresenter = new AliOssPresenter();
        mPresenter.attachView(this);
    }

    public AliOssHelper setPath(String path) {
        this.mPath = path;
        return this;
    }

    public AliOssHelper setFileName(String fileName) {
        mFileName = fileName;
        return this;
    }

    public AliOssHelper setFileWebUrl(String fileWebUrl) {
        mFileWebUrl = fileWebUrl;
        return this;
    }

    public AliOssHelper setCurDownloadSize(long curDownloadSize) {
        mCurDownloadSize = curDownloadSize;
        return this;
    }

    public AliOssHelper setFileDir(String fileDir) {
        mFileDir = fileDir;
        return this;
    }

    public AliOssHelper setFileFormat(String fileFormat) {
        mFileFormat = fileFormat;
        return this;
    }

    /**
     * 设置上传目录
     */
    public AliOssHelper setMediaDirectory(String mediaDirectory) {
        this.mMediaDirectory = mediaDirectory;
        return this;
    }

    /**
     * 上传文件， 获取token
     */
    public AliOssHelper uploadFile() {
        isDownload = false;
        Map<String, String> hashMap = new HashMap<>();
        mPresenter.getAliOssToken(DqUrl.url_alioss_token, hashMap);
        return this;
    }

    /**
     * 下载文件, 获取token
     */
    public AliOssHelper download() {
        isDownload = true;
        Map<String, String> hashMap = new HashMap<>();
        mPresenter.getAliOssToken(DqUrl.url_alioss_token, hashMap);
        return this;
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void showLoading(String tipMessage) {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void onFailed(String url, int code, DataBean entity) {

    }

    @Override
    public void onSuccess(String url, int code, DataBean entity) {
        if (url.equals(DqUrl.url_alioss_token)) {
            AliOssResp aliOssResp = (AliOssResp) entity.data;
            if (aliOssResp == null) {
                return;
            }
            //1. 获取token
            if (isDownload) {
                download(aliOssResp);
            } else {
                upload(aliOssResp);
            }
        }
    }

    private AliOSS.AliOssCallback mCallback = null;

    public void setCallback(AliOSS.AliOssCallback callback) {
        this.mCallback = callback;
    }

    /**
     * 上传视频所需参数
     * @param aliOssResp
     *         AliOssResp {
     *                     region = 'oss-cn-hangzhou',
     *                     bucket = 'sy-pro-oss',
     *                     OSS_WEB_SITE = 'http://file.sy.qingic.com/',
     *                     AccessKeySecret = 'A17w1nMndd4XPsVyrrVYWgSCPrWenDPF625xGZDBxXTU',
     *                     AccessKeyId = 'STS.NJnNy49Ufx8iXrmngLEhKAMg8',
     *                     Expiration = '2018-12-05T04:03:03Z',
     *                     SecurityToken = 'CAISwgJ1q6Ft5B2yfSjIr4nbBcOA1IpHz/qCWlTcimcZSednrojM2jz2IHBIfnZoBuwftPU3mG1Y6vsclqpsRpUd47sO4VQ0vPpt6gqET9frcaXXhOV2qf/IMGyXDAGBq622Su7lTdTbV+6wYlTf7EFayqf7cjPQMD7INoaS29wdGbZxZASjaidcD9p7PxZrrNRgVUHcLvGwKBXn8AGyZQgagGgRoGd77rvFgq/4wxHCjVr85/YIroDqWPaNZNVtO5ENK7XTx/BtJIPayzNV5iJN8KoM+84iwzrcucyHCFR8+giPN/GukudiNwhkfKM3acUmxbre7aQi5rGDz9qplUcRYbAEDX3lKdr+kJeeKoSALc0kcLv3AXPJ3+2UO4P92wFeOiJHblwSJIZ4cC4oVUF8FmyEMMCu4EiPawynWyl4T3aBGGkfGoABrTwicCHFLRvskk3DXiI15rED2+56GmJiMBLsqRH6gWDyRPPeJVKBFisGCEw3tlRJG1CV9eluL9GSuIBP1D93vTxPslyqjcLTXoKIVLKAuzad2K5txWWMiBcVBwny22VJreMKe8Vfa81uFKU9y10TGk2UzXdARqaBeGwBbv7HuyU=',
     *                     media_directory = 'web_media/',
     *                     expire = 1543983305
     *         }
     *
     *         CNOSSFileBean {
     *                     host = 'http://file.sy.qingic.com/',
     *                     requestId = 'null',
     *                     eTag = 'null',
     *                     fileName = 'web_media/3851fddd84b537ba/android_553526_1543979154996.mp4',
     *                     localFile = '/storage/emulated/0/brother/cnrecored//android_553526_1543979154996.mp4',
     *                     collectionUploadMsgBean = null
     *         }
     *
     */
    private void upload(AliOssResp aliOssResp) {
        CNOSSFileBean fileBean = getOssFileBean(aliOssResp);
        mCNAliOSS.setOssData(aliOssResp);
        mCNAliOSS.uploadFile(aliOssResp.bucket, fileBean, mCallback);
    }

    /**
     * 下载文件
     */
    private void download(AliOssResp aliOssResp) {
        mCNAliOSS.setOssData(aliOssResp);
        mCNAliOSS.downLoadFile(aliOssResp.bucket, mFileWebUrl, mFileDir, mFileFormat, mCurDownloadSize, mCallback);
    }

    public void cancelTask() {
        if (mCNAliOSS != null) {
            mCNAliOSS.cancelTask();
        }
    }

    @NonNull
    private CNOSSFileBean getOssFileBean(AliOssResp aliOssResp) {
        int index = mPath.lastIndexOf("/") + 1;
        String fileName = mPath.substring(index);
        CNOSSFileBean fileBean = new CNOSSFileBean();
        fileBean.host = aliOssResp.OSS_WEB_SITE;
        if (TextUtils.isEmpty(mFileName)) {
            // 自定义目录为空时，使用服务端
            if (TextUtils.isEmpty(mMediaDirectory)) {
                if (TextUtils.isEmpty(aliOssResp.media_directory)) {
                    fileBean.fileName = KeyValue.OSS_DIRECTION + AppInfoUtils.getPhoneImei() + "/" + getFileName(fileName);
                } else {
                    fileBean.fileName = aliOssResp.media_directory + AppInfoUtils.getPhoneImei() + "/"+ getFileName(fileName);
                }
            } else { //
                fileBean.fileName = mMediaDirectory + AppInfoUtils.getPhoneImei() + "/"+ getFileName(fileName);
            }
        } else { // 使用外部设置的属性
            fileBean.fileName = mFileName;
        }
        fileBean.localFile = mPath;

        //Log.w("upload", "fileBean = " + fileBean.toString());
        return fileBean;
    }

    public void onDestroy(){
        cancelTask();
        if(mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     * 重命名上传文件的名称
     */
    private String getFileName(String fileName) {
        String name;
        long curTime = System.currentTimeMillis();
        int index = fileName.lastIndexOf(".");
        try {
            if (index != -1) {
                String tmpName = fileName.substring(0, index);
                String suffix = fileName.substring(index + 1);
                String target = curTime + "_" + tmpName;
                name = MD5.encrypt(target) + "." + suffix;
            } else {
                name = MD5.encrypt(curTime + "_" + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            name = MD5.encrypt(curTime + "_" + fileName);
        }
        return "android_" + name.toLowerCase();
    }

}
