package com.wd.daquan.contacts.helper;

import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.wd.daquan.contacts.bean.MobileContactBean;
import com.wd.daquan.contacts.listener.IQueryMobileContactListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 方志
 * @Time: 2018/9/17 14:43
 * @Description: 查询手机联系人数据
 */
public class QueryMobileContactHelper {

    private IQueryMobileContactListener mListener;

    private MyAsyncQueryHandler mQueryHandler;

    public QueryMobileContactHelper(Context context) {
        mQueryHandler = new MyAsyncQueryHandler(context.getContentResolver());
        init();
    }

    /**
     * 初始化数据库查询参数
     */
    private void init() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
        // 查询的字段
        String[] projection = {ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY};
        // 按照sort_key升序查詢
        mQueryHandler.startQuery(0, null, uri, projection, null, null,
                "sort_key COLLATE LOCALIZED asc");

    }

    public void setListener(IQueryMobileContactListener mListener) {
        this.mListener = mListener;
    }

    @SuppressLint("HandlerLeak")
    private class MyAsyncQueryHandler extends AsyncQueryHandler {

        MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            List<MobileContactBean> contactBeanList = new ArrayList<>();
            try {
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst(); // 游标移动到第一项
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToPosition(i);
                        String name = cursor.getString(1);
                        String number = cursor.getString(2).replace(" ", "");

                        MobileContactBean bean = new MobileContactBean();
                        bean.userName = name;
                        bean.phone = number;
                        contactBeanList.add(bean);
                    }
                    //请求接口
                    if (null != mListener) {
                        mListener.onQueryComplete(contactBeanList);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    public void onDestroy(){
        if(null != mQueryHandler) {
            mQueryHandler.removeCallbacksAndMessages(null);
            mQueryHandler = null;
        }

        mListener = null;
    }
}
