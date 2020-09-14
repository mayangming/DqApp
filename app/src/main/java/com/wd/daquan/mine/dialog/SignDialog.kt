package com.wd.daquan.mine.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.wd.daquan.R
import com.wd.daquan.explore.dialog.BaseFragmentDialog
import kotlinx.android.synthetic.main.dialog_sign.*

/**
 * 签到对话框
 */
class SignDialog : BaseFragmentDialog(){
    private var content = ""
    companion object{
        const val KEY_ACTION = ""
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //设置全屏
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//需要这一行来解决对话框背景有白色的问题(颜色随主题变动)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_sign,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        sign_close.setOnClickListener {
            dismiss()
        }
    }

    private fun initData(){
        content = arguments?.getString(KEY_ACTION,"") ?: ""
        sign_content.text = "签到成功+${content}斗币"
    }

}