package com.wd.daquan.explore.type

import androidx.annotation.ColorRes
import com.wd.daquan.R

/**
 * 任务详情状态
 */
enum class TaskDetailsStatus(val status :Int, @ColorRes val color :Int, val content :String) {
    UN_SIGN_UP(0, R.color.color_F39825,"点击报名"),
    UN_VERIFICATION(1, R.color.color_F39825,"提交验证"),
    UN_AUDIT(2, R.color.color_F39825,"正在审核"),
    UN_COMPLETE(3, R.color.color_F39825,"已经完成"),
    UN_EXPIRED(4, R.color.color_E4E4E4,"已经完成"),
    UN_QUALIFIED(5, R.color.color_F39825,"不合格请重新提交");
    //    UN_QUALIFIED(4, R.color.color_F39825,"不合格请重新提交")
    companion object{
        fun getTaskDetailsByStatus(status : Int) = values().first {
            status == it.status
        }
    }
}