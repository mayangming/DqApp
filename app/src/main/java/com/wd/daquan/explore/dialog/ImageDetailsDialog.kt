package com.wd.daquan.explore.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.wd.daquan.R
import com.wd.daquan.explore.bean.ImgDetailsBean
import com.wd.daquan.explore.type.ImgMakeMoneyType
import com.wd.daquan.glide.GlideUtils
import kotlinx.android.synthetic.main.dialog_img_res.*

/**
 * 图片放大效果
 */
class ImageDetailsDialog : BaseFragmentDialog(){

    private val imgBean : ImgDetailsBean by lazy {
        val bean = arguments?.getSerializable(KEY_IMG)
        bean?.let {
            it as ImgDetailsBean
        } ?: ImgDetailsBean()
    }
    companion object{
        const val KEY_IMG = "keyImg"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //设置全屏
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_img_res,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        dialog_photo_details.setOnClickListener(this::onClick)
        when(imgBean.imgType){
            is ImgMakeMoneyType.ImgRes -> {
                dialog_photo_details.setImageResource(imgBean.imgRes)
            }
            is ImgMakeMoneyType.ImgAny -> {
                GlideUtils.load(context, imgBean.imgAny, dialog_photo_details)
            }
        }
    }

    private fun onClick(view: View){
        when(view){
            dialog_photo_details -> {
                dismiss()
            }
        }
    }
}