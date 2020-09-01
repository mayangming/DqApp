package com.wd.daquan.explore.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.DrawableRes
import com.da.library.listener.DialogListener
import com.da.library.tools.DateUtil
import com.da.library.utils.BigDecimalUtils
import com.dq.im.constants.Constants
import com.dq.im.util.oss.AliOssUtil
import com.dq.im.util.oss.SimpleUpLoadListener
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.common.utils.DialogUtils
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.bean.ImgDetailsBean
import com.wd.daquan.explore.dialog.ImageDetailsDialog
import com.wd.daquan.explore.dialog.TaskDescriptionDialog
import com.wd.daquan.explore.presenter.MakeMoneyPresenter
import com.wd.daquan.explore.type.ImgMakeMoneyType
import com.wd.daquan.explore.type.TaskDetailsStatus
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.constant.IntentCode
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.TaskDetailsBean
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.model.mgr.ModuleMgr
import com.wd.daquan.model.rxbus.MsgMgr
import com.wd.daquan.model.rxbus.MsgType
import com.wd.daquan.util.FileUtils
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_task_details.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * 任务详情
 */
class TaskDetailsActivity : DqBaseActivity<MakeMoneyPresenter, DataBean<TaskDetailsBean>>(){
    var pic : Uri ?= null
    var taskDetailBean: TaskDetailsBean = TaskDetailsBean()
    var taskId = "";//任务Id

    companion object{
        const val KEY_TASK_ID = "taskId"
    }

    override fun createPresenter() = MakeMoneyPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_task_details)
    }

    override fun initView() {
        initTitle()
        initTeachingVideoUI()
        task_status_content_container.setOnClickListener(this)
        task_refresh.setOnClickListener(this)
        upload_photo.setOnClickListener(this)
        save_share.setOnClickListener(this)
        del_photo.setOnClickListener(this)
        task_step1.setOnClickListener(this)
        task_step2.setOnClickListener(this)
        task_step3.setOnClickListener(this)
        screenshot_result.setOnClickListener(this)
        if (ModuleMgr.getCenterMgr().isShowMakeMoneyTip){
            showDialog()
        }

    }

    fun showDialog(){
        val dialog = TaskDescriptionDialog()
        dialog.show(supportFragmentManager,"")
    }

    fun initTitle(){
        make_money_task_details_title.title = "任务详情"
        make_money_task_details_title.setRightTxt("任务投诉")
        make_money_task_details_title.leftIv.setOnClickListener { finish() }
        make_money_task_details_title.setRightVisible(View.GONE)
        make_money_task_details_title.rightTv.setOnClickListener(this)
    }

    override fun initData() {
        taskId = intent.getStringExtra(KEY_TASK_ID) ?: ""
        getTaskDetails()
    }

    private fun getTaskDetails(){
        showLoading()
        val params = hashMapOf<String,String>()
        params["taskId"] = taskId
        mPresenter.getTaskDetails(DqUrl.url_task_detail,params)
    }

    /**
     * 报名
     */
    private fun getTaskRegistration(){
        showLoading()
        val params = hashMapOf<String,String>()
        params["taskId"] = taskId
        mPresenter.getTaskRegistration(DqUrl.url_task_registration,params)
    }

    /**
     * 提交验证
     */
    private fun getTaskRefresh(url: String){
        showLoading()
        val params = hashMapOf<String,String>()
        params["taskId"] = taskId
        params["task_pic"] = url
        mPresenter.getTaskRefresh(DqUrl.url_task_refresh,params)
    }

    private fun updateUi(taskDetailBean: TaskDetailsBean){
        taskId = taskDetailBean.id.toString()
        GlideUtils.load(this,taskDetailBean.typePic,task_icon)
        task_id.text = "任务ID: ${taskId}"
        task_price.text = "奖金: ￥${getPrice(taskDetailBean.taskmoney)}"
        task_total.text = taskDetailBean.classnum.toString()
        task_remainder.text = taskDetailBean.lastnum.toString()
        task_end_time.text = DateUtil.timeToStringAboutPoint(taskDetailBean.extime)
        task_audit.text = "${taskDetailBean.reviewtime}小时"
        task_description.text = taskDetailBean.taskexplain

        val taskStatus = TaskDetailsStatus.getTaskDetailsByStatus(taskDetailBean.taskStatus)
        if(0 == taskDetailBean.expires){//未过期
            task_status_content_container.setBackgroundResource(taskStatus.color)
            task_status_content.text = taskStatus.content
        }else{
            task_status_content_container.setBackgroundResource(R.color.color_E4E4E4)
            task_status_content.text = "任务已过期"
        }
        ModuleMgr.getCenterMgr().city
        if (!TextUtils.isEmpty(taskDetailBean.taskPicNew)){
            screenshot_container.visibility = View.GONE
            screenshot_result_container.visibility = View.VISIBLE
            GlideUtils.load(this,taskDetailBean.taskPicNew,screenshot_result)
        }else{
            screenshot_result_container.visibility = View.GONE
            screenshot_container.visibility = View.VISIBLE
        }

        upload_photo.isEnabled = taskDetailBean.taskStatus == TaskDetailsStatus.UN_VERIFICATION.status || taskDetailBean.taskStatus == TaskDetailsStatus.UN_QUALIFIED.status
        if(taskDetailBean.taskStatus == TaskDetailsStatus.UN_VERIFICATION.status || taskDetailBean.taskStatus == TaskDetailsStatus.UN_QUALIFIED.status){
            del_photo.visibility = View.VISIBLE
        }else{
            del_photo.visibility = View.GONE
        }

    }
    fun getPrice(price :Long):String{
        return BigDecimalUtils.penny2Dollar(price).toPlainString()
    }

    /**
     * 初始化教学视频的UI
     */
    private fun initTeachingVideoUI(){
        val content = "做任务教学视频: 点击查看"
        val look = "点击查看"
        val startIndex = content.indexOf(look)
        val spannable = SpannableStringBuilder(content)
        spannable.setSpan(CustomClickAble(),startIndex,content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        read_demo_video.text = spannable
        read_demo_video.movementMethod = LinkMovementMethod.getInstance() //加上这句话才有效果
//        read_demo_video.highlightColor = ContextCompat.getColor(read_demo_video.context,R.color.transparent) //去掉点击后的背景颜色为透明
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when(v){
            make_money_task_details_title.rightTv -> {
                DialogUtils.showCommonDialogForBoth(this,"任务投诉","拨打${resources.getString(R.string.complaint_phone)}",object : DialogListener{
                    override fun onCancel() {
                    }

                    override fun onOk() {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${resources.getString(R.string.complaint_phone)}"))
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                })
            }
            task_status_content_container -> {
                DqLog.e("YM------------>任务状态:${taskDetailBean.taskStatus}")
                if (1 == taskDetailBean.expires){
                    DqToast.showCenterShort("任务已过期，无法报名!")
                    return
                }
                if (taskDetailBean.taskStatus == TaskDetailsStatus.UN_SIGN_UP.status){
                    if(taskDetailBean.lastnum < 1){
                        DqToast.showCenterShort("剩余任务数量已不足，无法报名!")
                        return
                    }
                    getTaskRegistration()
                }else if(taskDetailBean.taskStatus == TaskDetailsStatus.UN_VERIFICATION.status){
                    if (null != pic){
                        pic?.let {
                            upLoadPic(it)
                        }
                    }else{
                        DqToast.showCenterShort("请上传任务凭证")
                    }
                }else if(taskDetailBean.taskStatus == TaskDetailsStatus.UN_QUALIFIED.status){
                    if (null != pic){
                        pic?.let {
                            upLoadPic(it)
                        }
                    }else{
                        DqToast.showCenterShort("请上传任务凭证")
                    }
                }
            }
            upload_photo ->{
                DqLog.e("YM------------>任务状态:${taskDetailBean.taskStatus}")
                if (1 == taskDetailBean.expires){
                    DqToast.showCenterShort("任务已过期，无法报名!")
                    return
                }
                if(taskDetailBean.extime <= System.currentTimeMillis()){
                    DqToast.showCenterShort("任务时间已经结束，无法报名!")
                    return
                }
                selectPhoto(1,this)
            }
            task_refresh -> {
                getTaskDetails()
            }
            del_photo -> {
                pic = null
                screenshot_container.visibility = View.VISIBLE
                screenshot_result_container.visibility = View.GONE
            }
            save_share -> {
                saveImg()
            }
            task_step1 -> {
                showImgDetailsDialog(R.mipmap.step1)
            }
            task_step2 -> {
                showImgDetailsDialog(R.mipmap.step2)
            }
            task_step3 -> {
                showImgDetailsDialog(R.mipmap.step2)
            }
            screenshot_result -> {
                when {
                    null != pic -> {
                        pic?.let {
                            showImgDetailsDialog(it)
                        }

                    }
                    taskDetailBean.taskPicNew.isNotEmpty() -> {
                        showImgDetailsDialog(taskDetailBean.taskPicNew)
                    }
                    else -> {
                        DqToast.showCenterShort("无法检测图片")
                    }
                }

            }
        }
    }

    private fun showImgDetailsDialog(@DrawableRes resId: Int){
        val dialog = ImageDetailsDialog()
        val imgBean = ImgDetailsBean()
        imgBean.imgType = ImgMakeMoneyType.ImgRes("")
        imgBean.imgRes = resId
        val bundle = Bundle()
        bundle.putSerializable(ImageDetailsDialog.KEY_IMG,imgBean)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager,"tag")
    }
    private fun showImgDetailsDialog(any: Any){
        val dialog = ImageDetailsDialog()
        val imgBean = ImgDetailsBean()
        imgBean.imgType = ImgMakeMoneyType.ImgAny("")
        imgBean.imgAny = any
        val bundle = Bundle()
        bundle.putSerializable(ImageDetailsDialog.KEY_IMG,imgBean)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager,"tag")
    }

    /**
     * 自定义点击事件
     */
    inner class CustomClickAble() : ClickableSpan() {

        override fun onClick(widget: View) {
            NavUtils.playVideo(widget.context,taskDetailBean.teachingVideoUrl)
        }


        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
//            ds.color = ds.linkColor
//            ds.isUnderlineText = false   //去除超链接的下划线
        }
    }

    /**
     * 更新上传的图片UI
     */
    private fun updatePic(uri :Uri){
        screenshot_container.visibility = View.GONE
        screenshot_result_container.visibility = View.VISIBLE
        GlideUtils.load(this,uri,screenshot_result)
    }

    /**
     * 上传图片
     */
    private fun upLoadPic(uri: Uri) {
//        AliOssUtil.getInstance()
        val documentFile = androidx.documentfile.provider.DocumentFile.fromSingleUri(this, uri)
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val photoData = FileUtils.toByteArray(inputStream)
//            uploadFile(photoData, Constants.getImgName() + FileUtils.getFileSuffix(documentFile?.name))
        val fileName = Constants.getImgName() + FileUtils.getFileSuffix(documentFile?.name)
        AliOssUtil.getInstance().asyncPutObject(fileName, photoData, object : SimpleUpLoadListener() {
            override fun uploadComplete(url: String) {
                getTaskRefresh(url)
            }

            override fun uploadFail() {
                super.uploadFail()
            }
        })
    }
    override fun onSuccess(url: String?, code: Int, entity: DataBean<TaskDetailsBean>?) {
        super.onSuccess(url, code, entity)
        dismissLoading()
        if (null == entity){
            return
        }
        when(url){
            DqUrl.url_task_detail -> {
                val data = entity.data
                if(null == data){
                    return
                }
                taskDetailBean = data
                updateUi(data)
            }
            DqUrl.url_task_registration -> {
                val data = entity.data
                if(null == data){
                    return
                }
                taskDetailBean = data
                updateUi(data)
                MsgMgr.getInstance().sendMsg(MsgType.TASK_MAKE_MONEY_REFRESH, "")
            }
            DqUrl.url_task_refresh -> {
                val data = entity.data
                if(null == data){
                    return
                }
                taskDetailBean = data
                updateUi(data)
            }
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<TaskDetailsBean>?) {
        super.onFailed(url, code, entity)
        dismissLoading()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK){
            return
        }
        if (requestCode == IntentCode.REQUEST_CODE_CHOOSE) {
            val picturePath = Matisse.obtainResult(data) //获取uri路径
            val picturePathStr = Matisse.obtainPathResult(data)
            val tempPictures = ArrayList<Uri>()
            for (uri in picturePath){
                tempPictures.add(uri)
            }
//            pics.addAll(tempPictures)
//            updatePic(picturePath[0])
            pic = picturePath[0]
            updatePic(picturePath[0])
        }
    }

    /**
     * 保存图片到本地
     */
    private fun saveImg(){
        val inputStream = assets.open("step1.png")
        val cacheDir = externalCacheDir?.absoluteFile ?: return
        val saveFilePath = cacheDir.absolutePath + File.separator + "example.png"
        val saveFile = File(saveFilePath)
        val bmp:Bitmap
        val outputStream = FileOutputStream(saveFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) {
            outputStream.write(buf, 0, len)
        }
        inputStream.close()
        outputStream.close()
        DqToast.showCenterShort("文件保存地址为:${saveFile.absolutePath}")
        val uri = Uri.fromFile(saveFile)
        val picList = arrayListOf<Uri>()
        picList.add(uri)
        NavUtils.gotoDynamicSendActivity(this,picList)
    }
}