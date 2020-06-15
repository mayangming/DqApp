package com.wd.daquan.glide.progress;

public interface OnProgressListener {
    void onProgress(boolean isComplete, int percent, long curBytes, long totalBytes);
}
