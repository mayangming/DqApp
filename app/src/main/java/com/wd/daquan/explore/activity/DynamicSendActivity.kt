package com.wd.daquan.explore.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.dq.im.constants.Constants
import com.dq.im.util.oss.AliOssUtil
import com.dq.im.util.oss.SimpleUpLoadListener
import com.dq.im.util.oss.UpLoadBean
import com.wd.daquan.BuildConfig
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.explore.adapter.DynamicSendAdapter
import com.wd.daquan.explore.bean.DynamicSendPhotoBean
import com.wd.daquan.explore.itemdecoration.SpacesItemDecoration
import com.wd.daquan.explore.presenter.DynamicSendPresenter
import com.wd.daquan.explore.type.DynamicSendPhotoType
import com.wd.daquan.imui.constant.IntentCode
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.FindUserDynamicDescBean
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.util.FileUtils
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.activity_dynamic_send.*
import java.io.InputStream

/**
 * 动态发布页
 */
class DynamicSendActivity: DqBaseActivity<DynamicSendPresenter, DataBean<Any>>() {
    var pics : ArrayList<Uri> ?= null
    var dynamicSendAdapter: DynamicSendAdapter ?= null
    private var isSending = false //动态是否在发送中
    companion object{
        const val ACTION_PICS :String = "actionPics"
        const val ACTION_DYNAMIC_RESPONSE :String = "actionDynamicsResponse"
    }
    override fun createPresenter() = DynamicSendPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_dynamic_send)
    }

    override fun initView() {
        dynamic_send_title.leftIv.setOnClickListener { finish() }
        dynamic_send_title.setRightVisible(View.VISIBLE)
        dynamic_send_title.setRightIv(R.mipmap.dynamic_send_icon)
        dynamic_send_title.rightIv.setOnClickListener(this)
        initRecycleView()
    }

    override fun initData() {
        pics = intent.getSerializableExtra(ACTION_PICS) as ArrayList<Uri>
        initRecycleViewData()
    }

    private fun initRecycleViewData(){
        val picBeanList = arrayListOf<DynamicSendPhotoBean>()
        pics?.forEach { value ->
            val model = DynamicSendPhotoBean(value)
            picBeanList.add(model)
        }

        when {
            picBeanList.size < 9 -> {
                val modelAdd = DynamicSendPhotoBean()
                modelAdd.dynamicSendPhotoType = DynamicSendPhotoType.ADD
                picBeanList.add(modelAdd)
            }
        }

        dynamicSendAdapter?.photos = picBeanList
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (isSending){
            return
        }
        when(v){
            dynamic_send_title.rightIv ->{
                pics = dynamicSendAdapter?.photos?.filter {
                    it.dynamicSendPhotoType == DynamicSendPhotoType.NORMAL
                }?.map {
                    it.uri
                } as ArrayList<Uri>
                val dynamicDesc = dynamic_send_content.text.toString()
                if (dynamicDesc.isEmpty()){
                    DqToast.showCenterShort("动态不能为空!")
                    return
                }
                isSending = true
                dynamic_send_title.rightIv.isEnabled = false
                showLoading("正在发布动态...")
                if (pics.isNullOrEmpty()){
                    sendDynamic(arrayListOf())
                    return
                }
                createUploadPhotos()
            }
        }
    }

    /**
     * 发送动态
     */
    private fun sendDynamic(pics : ArrayList<String>){
        val params = hashMapOf<String,Any>()
        params["desc"] = dynamic_send_content.text.toString()
        params["status"] = "1"
        params["pics"] = pics
        params["type"] = "1"
        mPresenter.saveUserDynamicDesc(DqUrl.url_dynamic_saveUserDynamicDesc,params);
    }

    private fun initRecycleView(){
        dynamic_send_rv.layoutManager = GridLayoutManager(this, 3)
        dynamicSendAdapter = DynamicSendAdapter().apply {
            setOnAddPhotoListener {//添加图片的回调
                val maxSelectCount = 9 - getPhotoCount()
                selectPhoto(maxSelectCount,this@DynamicSendActivity)
            }
        }
        dynamic_send_rv.adapter = dynamicSendAdapter
        dynamic_send_rv.addItemDecoration(SpacesItemDecoration(20))
    }

    /**
     * 创建上传集合
     */
    private fun createUploadPhotos(){
        pics?.let {
            val upLoadBeanList = arrayListOf<UpLoadBean>()
            for (uri in it){
                val documentFile = DocumentFile.fromSingleUri(this, uri)
                val inputStream: InputStream? = contentResolver.openInputStream(uri)
                val photoData = FileUtils.toByteArray(inputStream)
                val uploadBean = UpLoadBean()

                DqLog.e("YM--->文件是否为空:${null == documentFile}")
                DqLog.e("YM--->文件名字:${documentFile?.name}")
                uploadBean.fileName = Constants.getImgName() + FileUtils.getFileSuffix(documentFile?.name)
                uploadBean.localPath = uri.toString()
                uploadBean.fileData = photoData
                upLoadBeanList.add(uploadBean)
            }
            AliOssUtil.getInstance().putObjectArr(upLoadBeanList,object : SimpleUpLoadListener(){
                override fun uploadBatchComplete(upLoadBeans: MutableList<UpLoadBean>?) {//上传完毕
                    val pics = arrayListOf<String>()
                    upLoadBeans?.let {
                        for (upLoadBean in upLoadBeans){
                            pics.add(upLoadBean.netUrl)
                        }
                    }
                    sendDynamic(pics)
                }
                override fun uploadBatchFail(upLoadBeans: MutableList<UpLoadBean>?) {//上传失败

                }
            })
        }

    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)
        if (null == entity) return
        when(url){
            DqUrl.url_dynamic_saveUserDynamicDesc ->{
                isSending = false
                dynamic_send_title.rightIv.isEnabled = true
                dismissLoading()
                val dynamicBean = entity.data as FindUserDynamicDescBean
                intent.putExtra(ACTION_DYNAMIC_RESPONSE,dynamicBean)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onFailed(url, code, entity)
        dismissLoading()
        when(url) {
            DqUrl.url_dynamic_saveUserDynamicDesc -> {
                isSending = false
                DqToast.showCenterShort(entity?.content)
                dismissLoading()
                dynamic_send_title.rightIv.isEnabled = true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK){
            return
        }
        if (null == data){
            return
        }
        if (requestCode == IntentCode.REQUEST_CODE_CHOOSE) {
            val picturePath = Matisse.obtainResult(data) //获取uri路径
            val picturePathStr = Matisse.obtainPathResult(data)
            val tempPictures = ArrayList<Uri>()
            for (uri in picturePath){
                tempPictures.add(uri)
            }
            pics?.addAll(tempPictures)
            initRecycleViewData()
        }
    }

}