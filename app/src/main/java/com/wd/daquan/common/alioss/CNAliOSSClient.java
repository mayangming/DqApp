package com.wd.daquan.common.alioss;

import android.content.Context;

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
import com.wd.daquan.common.bean.AliOssResp;
import com.wd.daquan.common.bean.CNOSSFileBean;
import com.da.library.tools.FileUtils;

import java.io.File;
import java.io.InputStream;

/**
 * Created by DELL on 2018/5/25.
 */

public class CNAliOSSClient{

    private Context mContext;
    private OSS mOss;
    private volatile static CNAliOSSClient mCNAliOSSClient = null;
    private OSSAsyncTask mOSSAsyncTask;

    public static CNAliOSSClient getInstance(Context context){
        if(null == mCNAliOSSClient){
            synchronized (CNAliOSSClient.class){
                if(null == mCNAliOSSClient){
                    mCNAliOSSClient = new CNAliOSSClient(context);
                    return mCNAliOSSClient;
                }else{
                    return mCNAliOSSClient;
                }
            }
        }else{
            return mCNAliOSSClient;
        }
    }

    private CNAliOSSClient(Context context){
        this.mContext = context.getApplicationContext();
//        mInterfacePresenter.getAlioosToken();
    }

    public void setOssData(AliOssResp aliOssResp){
        if(aliOssResp == null)return;
        String endpoint = "http://" + aliOssResp.region +".aliyuncs.com";
//        SharePreferenceUtils.saveString(mContext, SharePreferenceUtils.ALIOSS_FILE, SharePreferenceUtils.ALIOSS_REGION, aliOssResp.region);
        // 在移动端建议使用STS的方式初始化OSSClient，更多信息参考：[访问控制]
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(aliOssResp.AccessKeyId,
                aliOssResp.AccessKeySecret, aliOssResp.SecurityToken);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        mOss = new OSSClient(mContext, endpoint, credentialProvider, conf);
    }

    public void downLoadFile(String bucketName, String objectKey, long curSize, IDownloadMediaCallback mIDownloadMediaCallback){
        // 构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(bucketName, objectKey);
        get.setRange(new Range(curSize, Range.INFINITE));
        get.setProgressListener(new OSSProgressCallback<GetObjectRequest>() {
            @Override
            public void onProgress(GetObjectRequest request, long currentSize, long totalSize) {
                mIDownloadMediaCallback.onProgress(currentSize, totalSize);
//                CNLog.d("clll", "clll: " + curSize + "-------"+ currentSize+"  total_size: " + totalSize);
            }
        });
        mOSSAsyncTask = mOss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
//                CNLog.d("clll", "onSuccess：" + "" + result.getContentLength());
                try {
                    InputStream inputStream = result.getObjectContent();
                    File file = FileUtils.writeCNFile(objectKey, inputStream);
                    mIDownloadMediaCallback.onSuccess(result.getContentLength(), file);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                mIDownloadMediaCallback.onFailure();
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
//                    CNLog.e("clll", "ErrorCode" + serviceException.getErrorCode());
//                    CNLog.e("clll", "RequestId" + serviceException.getRequestId());
//                    CNLog.e("clll", "HostId" + serviceException.getHostId());
//                    CNLog.e("clll", "RawMessage" + serviceException.getRawMessage());
                }
            }
        });
    }

    //取消任务
    public void cancelTask(){
        if(mOSSAsyncTask == null)return;
        mOSSAsyncTask.cancel();
    }

    //等待任务完成
    public void waitTaskFinish(){
        if(mOSSAsyncTask == null)return;
        mOSSAsyncTask.waitUntilFinished();
    }

    public boolean isComplete() {
        if(mOSSAsyncTask == null) {
            return false;
        }
        return mOSSAsyncTask.isCompleted();
    }

    public void uploadFile(String bucketName, final CNOSSFileBean mCNOSSFileBean, IUploadMediaCallback mIUploadMediaCallback){
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
//                CNLog.d("clll", "oss---currentSize:" + currentSize + "--totalSize:" + totalSize);
                mIUploadMediaCallback.onProgress(currentSize, totalSize);
            }
        });
        mOSSAsyncTask = mOss.asyncPutObject(putOR, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                CNLog.d("clll", "oss---ETag:" + result.getETag() + "--RequestId:" + result.getRequestId() + "--objectKey:" +
//                        mCNOSSFileBean.fileName);

                mCNOSSFileBean.eTag = result.getETag();
                mCNOSSFileBean.requestId = result.getRequestId();
                mIUploadMediaCallback.onSuccess(0, mCNOSSFileBean);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                mIUploadMediaCallback.onFailure();
                if (clientException != null) {
                    // 本地异常如网络异常等
//                    CNLog.e("clll", "oss---clientException");
                    clientException.printStackTrace();
                }
                if (serviceException != null) {
//                    CNLog.e("clll", "oss:---ErrorCode:" + serviceException.getErrorCode() +
//                            "--RequestId:" + serviceException.getRequestId() + "--HostId:" + serviceException.getHostId() +
//                            "--RawMessage:" + serviceException.getRawMessage());
                }

            }
        });
    }

    public interface IDownloadMediaCallback {
        void onProgress(long curSize, long totalSize);
        void onSuccess(long totalSize, File file);
        void onFailure();
    }
    public interface IUploadMediaCallback {
        void onProgress(long curSize, long totalSize);
        void onSuccess(long totalSize, CNOSSFileBean bean);
        void onFailure();
    }
}
