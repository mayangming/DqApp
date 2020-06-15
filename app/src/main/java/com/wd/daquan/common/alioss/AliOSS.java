package com.wd.daquan.common.alioss;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.oss.model.Range;
import com.wd.daquan.DqApp;
import com.wd.daquan.common.bean.AliOssResp;
import com.wd.daquan.common.bean.CNOSSFileBean;
import com.wd.daquan.common.utils.CNLog;
import com.da.library.tools.FileUtils;

import java.io.File;
import java.io.InputStream;


/**
 * @author  dukangkang
 * @date    2018/12/4 :
 * @describe todo ...
 */
public class AliOSS {

    private OSS mOss;
    private OSSAsyncTask mOSSAsyncTask;

    public AliOSS() {
    }

    public void setOssData(AliOssResp aliOssResp) {
        if (aliOssResp == null) {
            return;
        }
        String endpoint = "http://" + aliOssResp.region + ".aliyuncs.com";
//        SharePreferenceUtils.saveString(mContext, SharePreferenceUtils.ALIOSS_FILE, SharePreferenceUtils.ALIOSS_REGION, aliOssResp.region);
        // 在移动端建议使用STS的方式初始化OSSClient，更多信息参考：[访问控制]
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(aliOssResp.AccessKeyId,
                aliOssResp.AccessKeySecret, aliOssResp.SecurityToken);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        mOss = new OSSClient(DqApp.sContext, endpoint, credentialProvider, conf);
    }

    /**
     * 下载文件
     * @param bucketName 阿里云空间名
     * @param objectKey 文件地址
     * @param fileDir 存储的本地文件路径
     * @param fileFormat 文件格式
     * @param curSize 当前大小
     * @param aliOssCallback 回调接口
     */
    public void downLoadFile(String bucketName, String objectKey, String fileDir, String fileFormat, long curSize, AliOssCallback aliOssCallback) {
        // 构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(bucketName, objectKey);
        get.setRange(new Range(curSize, Range.INFINITE));
        get.setProgressListener(new OSSProgressCallback<GetObjectRequest>() {
            @Override
            public void onProgress(GetObjectRequest request, long currentSize, long totalSize) {
                aliOssCallback.onProgress(currentSize, totalSize);
            }
        });
        mOSSAsyncTask = mOss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                try {
                    InputStream inputStream = result.getObjectContent();
                    File file;
                    if(!TextUtils.isEmpty(fileDir) && !TextUtils.isEmpty(fileFormat)) {
                        file = FileUtils.writeCNFile(objectKey, fileDir, fileFormat, inputStream);
                    }else {
                        file = FileUtils.writeCNFile(objectKey, inputStream);
                    }

                    aliOssCallback.onSuccess(result.getContentLength(), file, null);

                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                aliOssCallback.onFailure();
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }

                if (serviceException != null) {
                    Log.e("qc_log", "down:---ErrorCode:" + serviceException.getErrorCode() +
                            "--RequestId:" + serviceException.getRequestId() + "--HostId:" + serviceException.getHostId() +
                            "--RawMessage:" + serviceException.getRawMessage());
                }

            }
        });
    }

    /**
     * 上传文件
     * @param bucketName
     * @param mCNOSSFileBean
     * @param aliOssCallback
     */
    public void uploadFile(String bucketName, final CNOSSFileBean mCNOSSFileBean, AliOssCallback aliOssCallback) {
        PutObjectRequest putOR = new PutObjectRequest(bucketName, mCNOSSFileBean.fileName, mCNOSSFileBean.localFile);
        // 开启crc效验后。如果在传输中数据不一致，会直接抛出ClientException异常。提示InconsistentException: inconsistent object
        putOR.setCRC64(OSSRequest.CRC64Config.YES);
        ObjectMetadata metadata = new ObjectMetadata();
        // 指定Content-Type
        metadata.setContentType("application/octet-stream");
        putOR.setMetadata(metadata);

        putOR.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {//上传进度
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                aliOssCallback.onProgress(currentSize, totalSize);
            }
        });
        mOSSAsyncTask = mOss.asyncPutObject(putOR, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                //Log.e("TAG", "onSuccess ： " + result.getRequestId());
                mCNOSSFileBean.eTag = result.getETag();
                mCNOSSFileBean.requestId = result.getRequestId();
                aliOssCallback.onSuccess(0, null, mCNOSSFileBean);

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                //Log.e("TAG", "onFailure ： " + serviceException.getRawMessage());
                aliOssCallback.onFailure();
                if (clientException != null) {
                    // 本地异常如网络异常等
//                    CNLog.e("clll", "oss---clientException");
                    clientException.printStackTrace();
                }

                if (serviceException != null) {
                    CNLog.e("qc_log", "oss:---ErrorCode:" + serviceException.getErrorCode() +
                            "--RequestId:" + serviceException.getRequestId() + "--HostId:" + serviceException.getHostId() +
                            "--RawMessage:" + serviceException.getRawMessage());
                }

            }
        });
    }

    //取消任务
    public void cancelTask() {
        if (mOSSAsyncTask == null) return;
        mOSSAsyncTask.cancel();
    }

    //等待任务完成
    public void waitTaskFinish() {
        if (mOSSAsyncTask == null) return;
        mOSSAsyncTask.waitUntilFinished();
    }

    public boolean isComplete() {
        if (mOSSAsyncTask == null) {
            return false;
        }
        return mOSSAsyncTask.isCompleted();
    }

    public interface AliOssCallback {
        /**
         * 操作进度
         * @param curSize
         * @param totalSize
         */
        void onProgress(long curSize, long totalSize);

        /**
         * 操作成功
         * @param totalSize
         *  文件总大小
         * @param file
         *  下载文件
         * @param bean
         *  上传文件
         */
        void onSuccess(long totalSize, File file, CNOSSFileBean bean);

        /**
         * 操作失败
         */
        void onFailure();
    }
}
