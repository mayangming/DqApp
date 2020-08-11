package com.wd.daquan.explore.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import com.wd.daquan.R
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.model.mgr.ModuleMgr
import kotlinx.android.synthetic.main.dialog_task_description.*

/**
 * 任务功能介绍对话框
 */
class TaskDescriptionDialog: AppCompatDialogFragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//需要这一行来解决对话框背景有白色的问题(颜色随主题变动)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val  window = dialog?.window;
        window?.let {
            val lp: WindowManager.LayoutParams = window.attributes
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = lp
        }
        return inflater.inflate(R.layout.dialog_task_description,container,false)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notice.setOnClickListener(this::onClick)
        start_money.setOnClickListener(this::onClick)
        next_no_tip.setOnCheckedChangeListener { _, isChecked ->
            ModuleMgr.getCenterMgr().saveIsShowMakeMoneyTip(!isChecked)
        }
    }

    fun onClick(view: View){
        when(view){
            notice -> {
                NavUtils.gotoWebviewActivity(context, DqUrl.url_new_user_tip, getString(R.string.new_user_tip))
                dismiss()
            }
            start_money -> {
                dismiss()
            }
        }
    }

}