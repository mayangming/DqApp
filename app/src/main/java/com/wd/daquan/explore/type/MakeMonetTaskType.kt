package com.wd.daquan.explore.type

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.wd.daquan.R

/**
 * 任务状态 1:待提交 2:审核中 3:已完成 5:不合格
 */
enum class MakeMonetTaskType(val taskType: Int = 1,@DrawableRes val drawableRes: Int,@ColorRes val textColor: Int,val content: String) {
    TASK_COMPLETE_SUCCESS(3, R.drawable.btn_border_1fa23e,R.color.color_1FA23E,"已完成"),//完成
    TASK_COMPLETE_FAIL(4, R.drawable.btn_border_1fa23e,R.color.color_1FA23E,"已完成"),//完成
    TASK_AUDIT(2, R.drawable.btn_border_f39825,R.color.color_F39825,"审核中"),//审核
    TASK_SUBMIT_ING(1, R.drawable.btn_border_f39825,R.color.color_F39825,"待提交"),//审核
//    TASK_EXPIRED(4, R.drawable.btn_border_999999,R.color.color_grey_999999,"已过期"),//过期
    TASK_GIVE_UP(5, R.drawable.btn_border_999999,R.color.color_grey_999999,"已放弃");//放弃

    companion object{
        fun getMakeMoneyTaskTypeByType(type: Int) = values().first {
            it.taskType == type
        }
    }
}