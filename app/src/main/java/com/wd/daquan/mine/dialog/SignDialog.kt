package com.wd.daquan.mine.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
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
    private var type = 0// 0:签到类型 1: 看视频得斗币类型
    companion object{
        const val KEY_ACTION = "content"
        const val KEY_TYPE = "type"
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
//        sign_close.setOnClickListener {
//            dismiss()
//        }
        sign_bg.setOnClickListener {
            dismiss()
        }
    }

    private fun initData(){
        content = arguments?.getString(KEY_ACTION,"") ?: ""
        type = arguments?.getInt(KEY_TYPE,0) ?: 0
        if (type == 0){
            sign_label.visibility = View.VISIBLE
            sign_content.text = content
            sign_content.setTextSize(TypedValue.COMPLEX_UNIT_SP,9f)
        }else{
            sign_label.visibility = View.GONE
            sign_content.text = "获得斗币+${content}"
            sign_content.setTextSize(TypedValue.COMPLEX_UNIT_SP,16f)
        }
    }

}