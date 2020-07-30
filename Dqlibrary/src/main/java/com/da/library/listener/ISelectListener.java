package com.da.library.listener;

import androidx.annotation.NonNull;

import com.wd.daquan.model.interfaces.ISelect;

import java.util.List;

/**
 * @Author: 方志
 * @Time: 2018/8/30 9:43
 * @Description: 选择数据回调接口
 */
public interface ISelectListener<T extends ISelect> {
    void onSelectList(@NonNull List<T> list);
}
