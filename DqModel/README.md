支持断点下载
case R.id.start:// 开始下载
    // 新下载前清空断点信息
    breakPoints = 0L;
    file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "sample.apk");
    downloader = new ProgressDownloader(url, file, this);
    downloader.download(0L);

    break;
case R.id.pause: //暂停
    if (null != downloader) {

        downloader.pause();
        Toast.makeText(this, "下载暂停", Toast.LENGTH_SHORT).show();
        // 存储此时的totalBytes，即断点位置。
        breakPoints = totalBytes;
    }
    break;
case R.id.continues: //继续
    if (null != downloader) {

        downloader.download(breakPoints);
    }
    break;

数据回调
@Override
public void onPreExecute(long contentLength) {
    // 文件总长只需记录一次，要注意断点续传后的contentLength只是剩余部分的长度
    if (this.contentLength == 0L) {
        this.contentLength = contentLength;
        mProgressBar.setMax((int) (contentLength / 1024));
    }
}

@Override
public void update(long totalBytes, boolean done) {
    // 注意加上断点的长度
    this.totalBytes = totalBytes + breakPoints;
    // 切换到主线程
    mProgressBar.setProgress((int) (totalBytes + breakPoints) / 1024);
    if (done) {
        QcApplication.getInstance().runInUIThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(QcApplication.sContext, "下载完成", Toast.LENGTH_SHORT).show();
            }
        });
    }
}