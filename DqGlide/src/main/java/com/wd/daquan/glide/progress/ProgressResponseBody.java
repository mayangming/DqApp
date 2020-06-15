package com.wd.daquan.glide.progress;


import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressResponseBody extends ResponseBody {

    private String mUrl;

    private ResponseBody mResponseBody;
    private BufferedSource mBufferedSource;
    private ProgressResponseBody.InternalProgressListener mInternalProgressListener;

    private Handler mHandler = new Handler(Looper.getMainLooper());


    public ProgressResponseBody(String url, InternalProgressListener internalProgressListener, ResponseBody body) {
        this.mUrl = url;
        this.mInternalProgressListener = internalProgressListener;
        this.mResponseBody = body;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(new ProgressSource(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private class ProgressSource extends ForwardingSource {
        long totalBytes;
        long lastTotalBytes;


        public ProgressSource(Source delegate) {
            super(delegate);
        }

        @Override
        public long read(Buffer sink, long byteCount) throws IOException {
            long curBytes = super.read(sink, byteCount);
            totalBytes += (curBytes == -1) ? 0 : curBytes;

            if (mInternalProgressListener != null && lastTotalBytes != totalBytes) {
                lastTotalBytes = totalBytes;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mInternalProgressListener.onProgress(mUrl, totalBytes, contentLength());
                    }
                });
            }
            return curBytes;
        }
    }


    interface InternalProgressListener {
        void onProgress(String url, long curBytes, long totalBytes);
    }
}
