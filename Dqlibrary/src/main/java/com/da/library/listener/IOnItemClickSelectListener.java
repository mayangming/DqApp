package com.da.library.listener;

import androidx.annotation.NonNull;

import com.wd.daquan.model.interfaces.ISelect;

/**
 * @Author: 方志
 * @Time: 2018/9/18 16:02
 * @Description:
 */
public interface IOnItemClickSelectListener<T extends ISelect> {
    void onItemClick(@NonNull T t);
}
