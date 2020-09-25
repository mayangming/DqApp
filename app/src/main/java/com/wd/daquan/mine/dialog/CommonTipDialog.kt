package com.wd.daquan.mine.dialog

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.explore.dialog.BaseFragmentDialog
import kotlinx.android.synthetic.main.dialog_common_tip.*

/**
 * 通用提示对话框
 */
class CommonTipDialog : BaseFragmentDialog(){

    private var dialogContent = ""

    companion object{
        const val KEY_CONTENT = "keyContent"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_common_tip,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        iv_close.setOnClickListener {
            dismiss()
        }
    }

    private fun initView(){
        dialogContent = arguments?.getString(KEY_CONTENT,"") ?: ""
        tip_content.text = Html.fromHtml(dialogContent)
    }

}