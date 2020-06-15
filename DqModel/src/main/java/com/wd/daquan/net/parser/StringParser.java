package com.wd.daquan.net.parser;

import java.io.IOException;

import okhttp3.Response;

/**
 * @author: dukangkang
 * @date: 2018/4/24 20:45.
 * @description: todo ...
 */
public class StringParser extends BaseParser {

    public StringParser() {
        super(ParseType.STRING);
    }

    @Override
    public String parse(Response response) {
        if (null == response) {
            return null;
        }

        if (response.isSuccessful()) {
            try {
                mData = response.body().string();
                return mData;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
