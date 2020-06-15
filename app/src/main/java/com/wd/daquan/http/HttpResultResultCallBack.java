package com.wd.daquan.http;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import http.callback.Callback;
import okhttp3.Response;

/**
 * 返回的数据类
 */
//public abstract class HttpResultResultCallBack<T extends HttpBaseBean> extends Callback<T> {
//    Gson gson = new Gson();
//    @Override
//    public T parseNetworkResponse(Response response, int id) throws Exception {
//        String content = response.body().string();
//        Type mType = getSuperclassTypeParameter(getClass());
////        T t = gson.fromJson(content,mType);
////        t.obj = gson.fromJson(t.data,getSuperclassTypeParameter(t.obj.getClass()));
//        T s = null;
//        return s;
//    }
//
//    /**
//     * 通过反射想要的返回类型
//     */
//    static Type getSuperclassTypeParameter(Class<?> subclass) {
//        Type superclass = subclass.getGenericSuperclass();
//        if (superclass instanceof Class) {
//            throw new RuntimeException("Missing type parameter.");
//        }
//        //ParameterizedType参数化类型，即泛型
//        ParameterizedType parameterized = (ParameterizedType) superclass;
//        // getActualTypeArguments获取参数化类型的数组，泛型可能有多个
////        Class c = (Class) parameterized.getActualTypeArguments()[0];
//        Class c = String.class;
//        Type[] types = parameterized.getActualTypeArguments();
//        for (Type type : types){
//            Log.e("YM","类的信息:"+type.getClass().getName());
//        }
//        return c;
//        //原始代码  return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
//    }
//}
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