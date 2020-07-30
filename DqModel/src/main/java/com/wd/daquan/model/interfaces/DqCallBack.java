package com.wd.daquan.model.interfaces;

import androidx.annotation.NonNull;
import android.util.Log;

import com.wd.daquan.model.BuildConfig;
import com.wd.daquan.model.ModelConfig;
import com.wd.daquan.model.R;
import com.wd.daquan.model.bean.DataBean;
import com.wd.daquan.model.log.DqLog;
import com.wd.daquan.model.utils.ModelUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class DqCallBack<T extends DataBean> implements Callback<T> {


    protected DqCallBack(){

    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        String url = getUrl(call);
        if(response.isSuccessful()) {
            T entity = response.body();
            if(entity == null) {
                onErr(url, ModelConfig.getString(R.string.request_ok_no_data));
            }else {
                if(entity.isSuccess()) {
                    onSuccess(url, entity.result, entity);
                }else {
                    onFailed(url, entity.result, entity);
                }
                Log.i("dq", "DqCallBack onResponse : " + entity.toString());
            }
        }else {
            onErr(url,  ModelConfig.getString(R.string.net_err));
        }
    }

    private String getUrl(@NonNull Call<T> call) {
        String url = call.request().url().toString();
        return url.substring(BuildConfig.SERVER.length());
    }

    private void onErr(String url, String message) {
        DataBean dataEntity = new DataBean<>();
        dataEntity.status = "err";
        if(ModelUtil.isNetworkConnected()) {
            dataEntity.content = ModelConfig.getString(R.string.request_err);
            onFailed(url, -1, (T) dataEntity);
        }else {
            //网络异常
            dataEntity.content = ModelConfig.getString(R.string.net_err);
            onFailed(url, -111, (T) dataEntity);
        }

        DqLog.e("DqCallBack onErr ： " + message);
    }

    public abstract void onSuccess(String url, int code, T entity);

    public abstract void onFailed(String url, int code, T entity);

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        t.printStackTrace();
        String url = getUrl(call);
        onErr(url, t.getMessage());
    }
}
