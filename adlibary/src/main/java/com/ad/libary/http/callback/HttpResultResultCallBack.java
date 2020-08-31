package com.ad.libary.http.callback;

import com.dq.sdk.ad.http.bean.HttpBaseBean;
import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import http.callback.Callback;
import okhttp3.Response;

public abstract class HttpResultResultCallBack<T extends HttpBaseBean> extends Callback<T> {
    Gson gson = new Gson();
    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        String content = response.body().string();
        Type mType = getSuperclassTypeParameter(getClass());
        T t = gson.fromJson(content,mType);
        return t;
    }

    /**
     * 通过反射想要的返回类型
     */
    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        //ParameterizedType参数化类型，即泛型
        ParameterizedType parameterized = (ParameterizedType) superclass;
        // getActualTypeArguments获取参数化类型的数组，泛型可能有多个
        Class c = (Class) parameterized.getActualTypeArguments()[0];
        return c;
        //原始代码  return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }
}