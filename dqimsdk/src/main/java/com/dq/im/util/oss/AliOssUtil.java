package com.dq.im.util.oss;

import android.content.ContentValues;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.CannedAccessControlList;
import com.alibaba.sdk.android.oss.model.CreateBucketRequest;
import com.alibaba.sdk.android.oss.model.CreateBucketResult;
import com.alibaba.sdk.android.oss.model.GetBucketACLRequest;
import com.alibaba.sdk.android.oss.model.GetBucketACLResult;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.dq.im.util.download.OnFileDownListener;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import http.Platform;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.os.Environment.DIRECTORY_MOVIES;
import static android.os.Environment.DIRECTORY_MUSIC;
import static android.os.Environment.DIRECTORY_PICTURES;

public class AliOssUtil {
    private final static String TAG = "AliOssUtil";
    private static OSS oss;
    private static AliOssUtil aliOssUtil;
    public static void initOss(Context context,String endpoint,String stsServer){
// 推荐使用OSSAuthCredentialsProvider。token过期可以及时更新。
//        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(stsServer);
        //特别提示这个是我们自定义的类，如果仅仅需要支持http可以使用OSS默认的OSSAuthCredentialsProvider，但是如果需要自定义token下载器或者token服务是HTTPS服务，请参考OSSAuthCredentialsProvider自定义，并导包为自定义类OSSAuthCredentialsProvider，切记
        OSSAuthCredentialsProvider credentialProvider = new OSSAuthCredentialsProvider(stsServer);
//        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(stsServer);
        // 配置类如果不设置，会有默认配置。
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒。
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒。
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个。
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次。
        oss = new OSSClient(context, endpoint, credentialProvider,conf);
        Log.e("YM","初始化OSS:"+(null == oss));
//        createBucketAcl();
    }

    public static AliOssUtil getInstance(){
        if (null == aliOssUtil){
            aliOssUtil = new AliOssUtil();
        }
        return aliOssUtil;
    }

    public static OSS getOss(){
        return oss;
    }


    public static void getAuthority(){
        GetBucketACLRequest getBucketACLRequest = new GetBucketACLRequest(OssConfig.bucketName);

// 获取存储空间访问权限。
        OSSAsyncTask getBucketAclTask = oss.asyncGetBucketACL(getBucketACLRequest, new OSSCompletedCallback<GetBucketACLRequest, GetBucketACLResult>() {
            @Override
            public void onSuccess(GetBucketACLRequest request, GetBucketACLResult result) {
                Log.d("asyncGetBucketACL", "Success!");
                Log.d("BucketAcl", result.getBucketACL());
                Log.d("Owner", result.getBucketOwner());
                Log.d("ID", result.getBucketOwnerID());
            }
            @Override
            public void onFailure(GetBucketACLRequest request, ClientException clientException, ServiceException serviceException) {
                // 请求异常。
                if (clientException != null) {
                    // 本地异常，如网络异常等。
                    clientException.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    public static void createBucketAcl(){
        CreateBucketRequest createBucketRequest = new CreateBucketRequest("android-studio-ssh");

        // 指定Bucket的ACL权限。
        createBucketRequest.setBucketACL(CannedAccessControlList.PublicReadWrite);
        // 指定Bucket所在的数据中心。
        createBucketRequest.setLocationConstraint("oss-cn-beijing");

        // 异步创建存储空间。
        OSSAsyncTask createTask = oss.asyncCreateBucket(createBucketRequest, new OSSCompletedCallback<CreateBucketRequest, CreateBucketResult>() {
            @Override
            public void onSuccess(CreateBucketRequest request, CreateBucketResult result) {
                Log.d("YM__asyncCreateBucket", "Success");
            }
            @Override
            public void onFailure(CreateBucketRequest request, ClientException clientException, ServiceException serviceException) {
                Log.d("YM__onFailure", "onFailure");
                // 请求异常。
                if (clientException != null) {
                    // 本地异常，如网络异常等。
                    clientException.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("YM__ErrorCode", serviceException.getErrorCode());
                    Log.e("YM__RequestId", serviceException.getRequestId());
                    Log.e("YM__HostId", serviceException.getHostId());
                    Log.e("YM__RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    /**
     * 异步上传结果
     */
    public void asyncPutObject(String fileName,byte[] fileData,UpLoadListener upLoadListener){
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(OssConfig.bucketName, fileName, fileData);

        // 异步上传时可以设置进度回调。
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = getOss().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {//该接口是异步的，所以返回在子线程中
//                参考链接:https://help.aliyun.com/document_detail/32049.html?spm=5176.10695662.1996646101.searchclickresult.d6217d7bz6CMDi
//                String url = AliOssUtil.getOss().presignConstrainedObjectURL(OssConfig.bucketName, fileName, 30 60);//指定有效时长
                String url = AliOssUtil.getOss().presignPublicObjectURL(OssConfig.bucketName, fileName);//永久时长
                Platform.get().execute(new Runnable() {
                    @Override
                    public void run() {
                        upLoadListener.uploadComplete(url);
                    }
                });
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                upLoadListener.uploadFail();
                // 请求异常。
                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
// task.cancel(); // 可以取消任务。
// task.waitUntilFinished(); // 等待任务完成。
    }

    /**
     * 同步上传
     * @return 上传解雇
     */
    @WorkerThread
    public boolean putObject(UpLoadBean upLoadBean){
        boolean uploadStatus = false;
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(OssConfig.bucketName, upLoadBean.getFileName(), upLoadBean.getFileData());
        try {
            PutObjectResult putResult = getOss().putObject(put);
            String url = AliOssUtil.getOss().presignPublicObjectURL(OssConfig.bucketName, upLoadBean.getFileName());
            upLoadBean.setNetUrl(url);
            Log.d("PutObject", "UploadSuccess");
            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());
            uploadStatus = true;
        } catch (ClientException e) {
            uploadStatus = false;
            // 本地异常，如网络异常等。
            e.printStackTrace();
        } catch (ServiceException e) {
            uploadStatus = false;
            // 服务异常。
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }
        return uploadStatus;
    }

    /**
     * 批量上传文件
     * 回调返回的内容信息中可以根据filName获取相应的网络地址
     */
    public void putObjectArr(List<UpLoadBean> upLoadBeans,UpLoadListener upLoadListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean uploadStatus = true;//整体上传状态
                for (UpLoadBean upLoadBean : upLoadBeans){
                    if (!putObject(upLoadBean)){//只要一个上传失败就认为是失败的
                        uploadStatus = false;
                    }
                }
                if (uploadStatus) {
                    Platform.get().execute(new Runnable() {
                        @Override
                        public void run() {
                            upLoadListener.uploadBatchComplete(upLoadBeans);
                        }
                    });
                }else {
                    Platform.get().execute(new Runnable() {
                        @Override
                        public void run() {
                            upLoadListener.uploadBatchFail(upLoadBeans);
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 文件下载
     * @param url 文件下载地址
     * @param fileType 文件类型，不同的文件类型需要做不同的保存操作
     */
    public void downLoadFile(String url,int fileType){
        //下载文件。
        //objectKey等同于objectname，表示从OSS下载文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        GetObjectRequest get = new GetObjectRequest(OssConfig.bucketName,url);
        oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                //开始读取数据。
                long length = result.getContentLength();
                byte[] buffer = new byte[(int) length];
                int readCount = 0;
                while (readCount < length) {
                    try{
                        readCount += result.getObjectContent().read(buffer, readCount, (int) length - readCount);
                    }catch (Exception e){
                        OSSLog.logInfo(e.toString());
                    }
                }
                //将下载后的文件存放在指定的本地路径。
                try {
                    FileOutputStream fout = new FileOutputStream("download_filePath");
                    fout.write(buffer);
                    fout.close();
                } catch (Exception e) {
                    OSSLog.logInfo(e.toString());
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientException,
                                  ServiceException serviceException)  {
                Log.e("YM","文件下载失败");
            }
        });
    }
    public static final int LOADING = 0;//加载中
    public static final int SUCCESS=1;
    public static final int FAIL=-1;

    private void downLoadFile(final String downPathUrl,final Context context,final String inserType,final OnFileDownListener onFileDownListener){

    }

    /**
     * 如果是要存放到沙盒外部目录，就需要使用此方法
     * @date: 创建时间:2019/12/11
     * @author: gaoxiaoxiong
     * @descripion: 保存图片，视频，音乐到公共地区，此操作需要在线程，不是我们自己的APP目录下面的
     * @param downPathUrl 下载文件的路径，需要包含后缀
     * @param inserType 存储类型，可选参数 DIRECTORY_PICTURES  ,DIRECTORY_MOVIES  ,DIRECTORY_MUSIC
     **/
    public void downMusicVideoPicFromService(final String downPathUrl,final Context context,final String inserType,final OnFileDownListener onFileDownListener){
        Log.e("YM","文件下载链接:"+downPathUrl);
        if (TextUtils.isEmpty(downPathUrl)){
            Log.e("YM","文件下载链接为空");
            return;
        }
        Observable.just(downPathUrl).subscribeOn(Schedulers.newThread()).map(new Function<String, Uri>() {
            @Override
            public Uri apply(String s) throws Exception {
                Uri uri = null;
                try {
                    URL url = new URL(downPathUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(30 * 1000);
                    InputStream is = conn.getInputStream();
                    long time = System.currentTimeMillis();
                    int code = conn.getResponseCode();
                    String prefix = downPathUrl.substring(downPathUrl.lastIndexOf(".") + 1);
                    Log.e("YM","文件后缀:"+prefix);
                    String fileName = downPathUrl.substring(downPathUrl.lastIndexOf("/") + 1);;
//                    if (code == HttpURLConnection.HTTP_OK) {
//                        fileName = conn.getHeaderField("Content-Disposition");
                    // 通过Content-Disposition获取文件名，这点跟服务器有关，需要灵活变通
//                        if (fileName == null || fileName.length() < 1) {
//                            // 通过截取URL来获取文件名
//                            URL downloadUrl = conn.getURL(); // 获得实际下载文件的URL
//                            fileName = downloadUrl.getFile();
//                            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
//                        } else {
//                            fileName = URLDecoder.decode(fileName.substring(
//                                    fileName.indexOf("filename=") + 9), "UTF-8");
//                            // 有些文件名会被包含在""里面，所以要去掉，不然无法读取文件后缀
//                            fileName = fileName.replaceAll("\"", "");
//                        }
//                    }
                    Log.e("YM","文件名字11111:"+fileName);
                    if (TextUtils.isEmpty(fileName)) {
                        fileName = time + "." + prefix;
                    }
                    Log.e("YM","文件名字22222222:"+fileName);
                    Log.e("YM","文件类型:"+inserType);
                    ContentValues contentValues = new ContentValues();
                    if (inserType.equals(DIRECTORY_PICTURES)) {
                        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                        contentValues.put(MediaStore.Images.Media.MIME_TYPE, getMIMEType(fileName));
                        if (Build.VERSION.SDK_INT>=29) {//android 10
                            contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                        }
                        //只是往 MediaStore 里面插入一条新的记录，MediaStore 会返回给我们一个空的 Content Uri
                        //接下来问题就转化为往这个 Content Uri 里面写入
                        uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    } else if (inserType.equals(DIRECTORY_MOVIES)) {
                        contentValues.put(MediaStore.Video.Media.MIME_TYPE, getMIMEType(fileName));
                        contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, fileName);
                        if (Build.VERSION.SDK_INT>=29) {//android 10
                            contentValues.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
                        }
                        //只是往 MediaStore 里面插入一条新的记录，MediaStore 会返回给我们一个空的 Content Uri
                        //接下来问题就转化为往这个 Content Uri 里面写入
                        uri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
                    } else if (inserType.equals(DIRECTORY_MUSIC)) {
                        contentValues.put(MediaStore.Audio.Media.MIME_TYPE, getMIMEType(fileName));
                        contentValues.put(MediaStore.Audio.Media.DISPLAY_NAME, fileName);
                        if (Build.VERSION.SDK_INT>=29){//android 10
                            contentValues.put(MediaStore.Audio.Media.DATE_TAKEN, System.currentTimeMillis());
                        }
                        //只是往 MediaStore 里面插入一条新的记录，MediaStore 会返回给我们一个空的 Content Uri
                        //接下来问题就转化为往这个 Content Uri 里面写入
                        uri = context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);
                    }
                    BufferedInputStream inputStream = new BufferedInputStream(is);
                    OutputStream os = context.getContentResolver().openOutputStream(uri);
                    if (os != null) {
                        byte[] buffer = new byte[1024];
                        int len;
                        int total = 0;
                        int contentLeng = conn.getContentLength();
                        while ((len = inputStream.read(buffer)) != -1) {
                            os.write(buffer, 0, len);
                            total += len;
                            if (onFileDownListener != null) {
                                onFileDownListener.onFileDownStatus(LOADING, null, (total * 100 / contentLeng), total, contentLeng);
                            }
                        }
                    }

                    //oppo手机不会出现在照片里面，但是会出现在图集里面
                    if (inserType.equals(DIRECTORY_PICTURES)){//如果是图片
                        //扫描到相册
                        String[] filePathArray = FileSDCardUtil.getInstance().getPathFromContentUri(uri,context);
                        MediaScannerConnection.scanFile(context, new String[] {filePathArray[0]}, new String[]{"image/jpeg"}, new MediaScannerConnection.OnScanCompletedListener(){
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.e(TAG,"PATH:"+path);
                            }
                        } );
                    }
                    os.flush();
                    inputStream.close();
                    is.close();
                    os.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return uri;
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Uri>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Uri uri) {
                        if (uri != null && onFileDownListener != null) {
                            onFileDownListener.onFileDownStatus(SUCCESS, uri, 0, 0, 0);
                        } else {
                            onFileDownListener.onFileDownStatus(FAIL, null, 0, 0, 0);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(TAG,"错误信息:"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    /**
     * @date :2020/3/17 0017
     * @author : gaoxiaoxiong
     * @description:根据文件后缀名获得对应的MIME类型
     * @param fileName 文件名，需要包含后缀.xml类似这样的
     **/
    public String getMIMEType(String fileName) {
        String type="*/*";
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fileName.lastIndexOf(".");
        if(dotIndex < 0){
            return type;
        }
        /* 获取文件的后缀名*/
        String end=fileName.substring(dotIndex,fileName.length()).toLowerCase();
        if(end=="")return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<getFileMiMeType().length;i++){ //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if(end.equals(getFileMiMeType()[i][0]))
                type = getFileMiMeType()[i][1];
        }
        return type;
    }

    /**
     * 参考链接
     * https://www.cnblogs.com/korea/p/11787460.html
     * @date :2020/3/17 0017
     * @author : gaoxiaoxiong
     * @description:获取文件的mimetype类型
     **/
    public String[][] getFileMiMeType() {
        String[][] MIME_MapTable = {
                //{后缀名，MIME类型}
                {".3gp", "video/3gpp"},
                {".aac", "audio/aac"},
                {".apk", "application/vnd.android.package-archive"},
                {".asf", "video/x-ms-asf"},
                {".avi", "video/x-msvideo"},
                {".bin", "application/octet-stream"},
                {".bmp", "image/bmp"},
                {".c", "text/plain"},
                {".class", "application/octet-stream"},
                {".conf", "text/plain"},
                {".cpp", "text/plain"},
                {".doc", "application/msword"},
                {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
                {".xls", "application/vnd.ms-excel"},
                {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
                {".exe", "application/octet-stream"},
                {".gif", "image/gif"},
                {".gtar", "application/x-gtar"},
                {".gz", "application/x-gzip"},
                {".h", "text/plain"},
                {".htm", "text/html"},
                {".html", "text/html"},
                {".jar", "application/java-archive"},
                {".java", "text/plain"},
                {".jpeg", "image/jpeg"},
                {".jpg", "image/jpeg"},
                {".js", "application/x-javascript"},
                {".log", "text/plain"},
                {".m3u", "audio/x-mpegurl"},
                {".m4a", "audio/mp4a-latm"},
                {".m4b", "audio/mp4a-latm"},
                {".m4p", "audio/mp4a-latm"},
                {".m4u", "video/vnd.mpegurl"},
                {".m4v", "video/x-m4v"},
                {".mov", "video/quicktime"},
                {".mp2", "audio/x-mpeg"},
                {".mp3", "audio/x-mpeg"},
                {".mp4", "video/mp4"},
                {".mpc", "application/vnd.mpohun.certificate"},
                {".mpe", "video/mpeg"},
                {".mpeg", "video/mpeg"},
                {".mpg", "video/mpeg"},
                {".mpg4", "video/mp4"},
                {".mpga", "audio/mpeg"},
                {".msg", "application/vnd.ms-outlook"},
                {".ogg", "audio/ogg"},
                {".pdf", "application/pdf"},
                {".png", "image/png"},
                {".pps", "application/vnd.ms-powerpoint"},
                {".ppt", "application/vnd.ms-powerpoint"},
                {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
                {".prop", "text/plain"},
                {".rc", "text/plain"},
                {".rmvb", "audio/x-pn-realaudio"},
                {".rtf", "application/rtf"},
                {".sh", "text/plain"},
                {".tar", "application/x-tar"},
                {".tgz", "application/x-compressed"},
                {".txt", "text/plain"},
                {".wav", "audio/x-wav"},
                {".wma", "audio/x-ms-wma"},
                {".wmv", "audio/x-ms-wmv"},
                {".wps", "application/vnd.ms-works"},
                {".xml", "text/plain"},
                {".z", "application/x-compress"},
                {".zip", "application/x-zip-compressed"},
                {"", "*/*"}
        };
        return MIME_MapTable;
    }

    /**
     * 10.0之上保存文件的方式
     */
    private void saveFileOf10Above(){

    }

}