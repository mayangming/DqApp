package com.wd.daquan.net.parser;

import com.google.gson.Gson;
import com.wd.daquan.net.bean.ResponseEntity;

import okhttp3.Response;

/**
 * @author: dukangkang
 * @date: 2018/5/4 18:11.
 * @description: todo ...
 */
public class ObjectParser<T extends ResponseEntity> extends BaseParser<T> {

    private Gson mGson = new Gson();

    public ObjectParser() {
        super(ParseType.OBJECT);
    }

    @Override
    public T parse(Response response) {
        if (null == response) {
            return null;
        }
        try {
            mData = response.body().string();
            //Log.d("fz", "mData : " + mData);
            T target = mGson.fromJson(mData, mType);
            String dataJson = parseData(mData);
            target.setJson(dataJson);
            return target;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
