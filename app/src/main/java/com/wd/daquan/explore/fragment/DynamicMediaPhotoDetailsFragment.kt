package com.wd.daquan.explore.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.explore.dialog.PhotoOperatorDialog
import com.wd.daquan.glide.GlideUtils
import kotlinx.android.synthetic.main.item_photo_details.*

/**
 * 朋友圈多媒体图片预览功能
 */
class DynamicMediaPhotoDetailsFragment : BaseFragment(){

    /**
     * 图片网络连接
     */
    private var photoUrl :String ?= null

    companion object{
        const val ACTION_PHOTO_URL = "actionPhotoUrl"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_photo_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView(){
        photo_details.setOnClickListener(this::onClick)
        photo_details.setOnLongClickListener(this::onLongClick)
    }

    private fun initData(){
        photoUrl = arguments?.getString(ACTION_PHOTO_URL).toString()
        GlideUtils.load(context, photoUrl, photo_details)
    }

    private fun onClick(view :View){
        when(view){
            photo_details ->{
                val act = view.context as Activity
                act.finish()
            }
        }
    }

    private fun onLongClick(view: View): Boolean{
        when(view){
            photo_details -> {
                val photoOperatorDialog = PhotoOperatorDialog()
                val bundle = Bundle()
                bundle.putString(PhotoOperatorDialog.ACTION_PHOTO,photoUrl)
                photoOperatorDialog.arguments = bundle
                fragmentManager?.let { photoOperatorDialog.show(it,"operatorDialog") }
            }
        }
        return true
    }

}