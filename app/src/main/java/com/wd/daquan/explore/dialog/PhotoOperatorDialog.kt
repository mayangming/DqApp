package com.wd.daquan.explore.dialog

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dq.im.util.download.HttpDownFileUtils
import com.dq.im.util.download.OnFileDownListener
import com.wd.daquan.R
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.log.DqToast
import kotlinx.android.synthetic.main.dialog_photo_operator.*
import java.io.File
import java.io.FileOutputStream

/**
 * 图片操作对话框
 */
class PhotoOperatorDialog : BottomSheetDialogFragment() {
    private var url : String = ""
    private var photoType = 0;
    companion object{
        const val PHOTO_ACTION = "actionPhoto"
        const val PHOTO_TYPE = "photoType"//图片类型 0 :assets来源的图片
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
        url = arguments?.getString(PHOTO_ACTION,"") ?: ""
        photoType = arguments?.getInt(PHOTO_TYPE,-1) ?: -1
        DqLog.e("YM------>文件类型:$photoType")
    }

    fun onClick(view :View){
        when(view){
            photo_operator_save_photo -> {
                if (photoType == 0){
                    saveAssetsFile()
                }else{
                    downLoadPicture()
                }
            }
            photo_operator_cancel -> dismiss()
        }
    }

    /**
     * 保存assets文件到本地临时文件
     */
    private fun saveAssetsFile(){
        val inputStream = context?.assets?.open(url)
        val cacheDir = context?.externalCacheDir?.absoluteFile ?: return
        val saveFilePath = cacheDir.absolutePath + File.separator + url
        val saveFile = File(saveFilePath)
        val bmp: Bitmap
        val outputStream = FileOutputStream(saveFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream!!.read(buf).also { len = it } > 0) {
            outputStream.write(buf, 0, len)
        }
        inputStream.close()
        outputStream.close()
        DqToast.showCenterShort("文件保存地址为:${saveFile.absolutePath}")
        dismiss()
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