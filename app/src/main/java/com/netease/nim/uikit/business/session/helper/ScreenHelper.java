package com.netease.nim.uikit.business.session.helper;

import android.app.Activity;

import com.github.piasy.rxscreenshotdetector.RxScreenshotDetector;
import com.wd.daquan.common.constant.DqUrl;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.net.RequestHelper;
import com.wd.daquan.net.callback.ObjectCallback;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import java.util.LinkedHashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.Call;

/**
 * @author: dukangkang
 * @date: 2019/2/19 16:21.
 * @description: todo ...
 */
public class ScreenHelper {

    private long nextTime = 0;
    private String mTargetId = "";
    private boolean isGroup = false;

    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    private ScreenHelper() {

    }

    private static class ScreenHolder {
        public static ScreenHelper INSTANCE = new ScreenHelper();
    }

    public static ScreenHelper getInstance() {
        return ScreenHolder.INSTANCE;
    }

    public void init(Activity activity, String targetId, boolean isGroup) {
        this.mTargetId = targetId;
        this.isGroup = isGroup;
        RxScreenshotDetector.start(activity)
                .compose(RxLifecycleAndroid.bindActivity(lifecycleSubject))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(path -> sendScreenShortMessage(), Throwable::printStackTrace);
    }

    public void setTargetId(String targetId) {
        mTargetId = targetId;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    /**
     * 发送截屏消息发到服务端
     */
    public void sendScreenShortMessage() {
        long curTime = System.currentTimeMillis();
        if ((curTime - nextTime) > 500) {
            nextTime = curTime;
        } else {
            return;
        }

        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        if (isGroup) {
            hashMap.put("type", "2");
            hashMap.put("groupId", "");
            hashMap.put("group_id", mTargetId);
        } else {
            hashMap.put("type", "1");
            hashMap.put("groupId", mTargetId);
            hashMap.put("group_id", "");
        }

        sendScreenShortMessage(DqUrl.url_screenshot_push_msg, hashMap);
    }

    public void start() {
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    public void resume() {
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    public void pause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
    }

    public void stop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
    }

    public void destory() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
    }

    /**
     * 发送截屏通知
     * @param url
     * @param hashMap
     */
    public void sendScreenShortMessage(String url, LinkedHashMap<String, String> hashMap) {
        RequestHelper.request(url, hashMap, new ObjectCallback<DataBean>() {
            @Override
            public void onSuccess(Call call, String url, int code, DataBean result) {
                super.onSuccess(call, url, code, result);
            }

            @Override
            public void onFailed(Call call, String url, int code, DataBean result, Exception e) {
                super.onFailed(call, url, code, result, e);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

}
