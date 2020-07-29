package com.wd.daquan.explore.dialog

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.BottomSheetDialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dq.im.util.download.HttpDownFileUtils
import com.dq.im.util.download.OnFileDownListener
import com.wd.daquan.R
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.model.log.DqToast
import kotlinx.android.synthetic.main.dialog_photo_operator.*
import java.io.File

/**
 * 图片操作对话框
 */
class PhotoOperatorDialog : BottomSheetDialogFragment() {
    private var url : String = ""
    companion object{
        const val ACTION_PHOTO = "actionPhoto"
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_photo_operator,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView(){
        photo_operator_save_photo.setOnClickListener(this::onClick)
        photo_operator_cancel.setOnClickListener(this::onClick)
    }

    private fun initData(){
        url = arguments?.getString(ACTION_PHOTO,"") ?: ""
    }

    fun onClick(view :View){
        when(view){
            photo_operator_save_photo -> downLoadPicture()
            photo_operator_cancel -> dismiss()
        }
    }

    private fun downLoadPicture(){
        HttpDownFileUtils.getInstance().downFileFromServiceToPublicDir(url, context, Environment.DIRECTORY_PICTURES, OnFileDownListener { status, obj, proGress, currentDownProGress, totalProGress ->
            if (status == 1) {
                var localPath = "" //10.0之上是uri，10.0之下是本地路径
                if (obj is File) {
                    val file = obj as File
                    localPath = file.absolutePath
                    DqToast.showCenterShort("文件保存在:${localPath}")
                } else if (obj is Uri) {
                    val uri = obj as Uri
                    localPath = uri.toString()
                    DqToast.showCenterShort("文件已经保存在相册中")
                }
            }
        })
    }

}