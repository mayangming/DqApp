package com.wd.daquan.explore.activity

import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import com.da.library.utils.AESUtil
import com.da.library.utils.BigDecimalUtils
import com.da.library.utils.DateUtil
import com.dq.im.bean.im.MessageRedPackageBean
import com.lzj.pass.dialog.PayPassDialog
import com.lzj.pass.dialog.PayPassView.OnPayClickListener
import com.meetqs.qingchat.pickerview.view.OptionsPickerView
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.common.utils.CashierInputFilter
import com.wd.daquan.common.utils.DialogUtils
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.bean.CustomTaskTypeBean
import com.wd.daquan.explore.bean.SendTaskDraftBean
import com.wd.daquan.explore.dialog.TaskTypeDialog
import com.wd.daquan.explore.presenter.SendTaskPresenter
import com.wd.daquan.mine.listener.PayDetailListener
import com.wd.daquan.model.bean.*
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.model.mgr.ModuleMgr
import com.wd.daquan.model.rxbus.MsgMgr
import com.wd.daquan.model.rxbus.MsgType
import com.wd.daquan.model.rxbus.QCObserver
import com.wd.daquan.model.utils.GsonUtils
import kotlinx.android.synthetic.main.activity_send_task.*
import java.math.BigDecimal
import java.util.*

/**
 * 发布任务页面
 */
class SendTaskActivity  : DqBaseActivity<SendTaskPresenter, DataBean<Any>>(), PayDetailListener, QCObserver{
    private var userWallet = UserCloudWallet()//用户零钱信息
    private var sendTaskBean = SendTaskBean()//发布任务后的信息
    private var mPvOptions: OptionsPickerView<*>? = null
    private var companyTypeBean :CustomTaskTypeBean = CustomTaskTypeBean() //任务类型
    private var classificationTypeBean :CustomTaskTypeBean = CustomTaskTypeBean() //厂商类型
    private var countPriceInt = 0.0 //待支付的总金额
    private var payType = MessageRedPackageBean.RED_PACKAGE_CHANGE//支付类型
    private var lastTaskSendDraft = ""//上次内容发布的草稿
    private var sendTaskDraftBean = SendTaskDraftBean()//任务草稿
    private var taskId = ""//任务ID

    companion object{
        const val KEY_TASK_ID = "keyTaskId"
    }

    override fun createPresenter() = SendTaskPresenter()
    override fun setContentView() {
        MsgMgr.getInstance().attach(this)
        setContentView(R.layout.activity_send_task)
    }

    override fun initView() {
        initTitle()
        initReleaseGuideUI()
        send_task_company_type_ll.setOnClickListener(this)
        send_task_classification_ll.setOnClickListener(this)
        send_task_end_time.setOnClickListener(this)
        task_status_content_container.setOnClickListener(this)
        update_task_status.setOnClickListener(this)
        send_task_unit_price_edt.filters = arrayOf(CashierInputFilter())

    }

    override fun initData() {
        initDateDialog()
        getUserCloudWallet()
        if (null != intent){
            taskId = intent.getStringExtra(KEY_TASK_ID)
            if (!TextUtils.isEmpty(taskId)){
                getUserTaskByTaskId()
                return
            }
        }
        lastTaskSendDraft = ModuleMgr.getCenterMgr().lastTaskSendDraft
        if (!TextUtils.isEmpty(lastTaskSendDraft)){
            sendTaskDraftBean = GsonUtils.fromJson(lastTaskSendDraft,SendTaskDraftBean::class.java)
            companyTypeBean = sendTaskDraftBean.companyTypeBean
            classificationTypeBean = sendTaskDraftBean.classificationTypeBean
            updateUIForDraft()
        }
        getTaskCompanyList()
        getClassificationList()
    }


    private fun initTitle(){
        task_send_title.title = "发布"
        task_send_title.leftIv.setOnClickListener { finish() }
    }

    /**
     * 获取厂商类型
     */
    private fun getTaskCompanyList(){
        val params = hashMapOf<String,Any>()
        mPresenter.getTaskCompanyList(DqUrl.url_task_type,params)
    }

    /**
     * 获取任务累心
     */
    private fun getClassificationList(){
        val params = hashMapOf<String,Any>()
        mPresenter.getClassificationList(DqUrl.url_task_classification,params)
    }

    /**
     * 根据草稿更新UI
     */
    private fun updateUIForDraft(){
        task_company_name.text = companyTypeBean.typeName
        task_classification_name.text = classificationTypeBean.typeName
        send_task_title_edt.text.append(sendTaskDraftBean.taskname)
        send_task_end_time.text = sendTaskDraftBean.extime
        send_task_unit_price_edt.text.append(sendTaskDraftBean.taskmoney)
        send_task_count_edt.text.append(sendTaskDraftBean.classnum)
        send_task_description_edt.text.append(sendTaskDraftBean.taskexplain)
        send_task_advance_payment_edt.text = sendTaskDraftBean.advancePayment
        send_task_description_count.text = "${sendTaskDraftBean.taskexplain.length}/40"
    }

    /**
     * 根据页面详情更改任务UI
     */
    private fun updateUIForDetails(){
        switchEdt(false)
        if(sendTaskBean.isPass == 0){
            switchEdt(true)
            task_status_content.text = "申请发布"
            update_task_status.text = "保存"
            update_task_status.setTextColor(resources.getColor(R.color.color_EF5B40))
        }else if(sendTaskBean.isPass == 1){//审核中
            task_status_content.text = "审核中"
            update_task_status.text = "撤回"
            update_task_status.setTextColor(resources.getColor(R.color.app_txt_999999))
        }else if (sendTaskBean.isPass == 2){
            task_status_content.text = "发布成功"
            update_task_status.text = "查看详情"
            update_task_status.setTextColor(resources.getColor(R.color.color_EF5B40))
        }else if(sendTaskBean.isPass == 3){
            if(sendTaskBean.isPay == 1){
                task_status_content.text = "重新编辑"
                update_task_status.text = "申请退款"
                update_task_status.setTextColor(resources.getColor(R.color.color_EF5B40))
            }else if(sendTaskBean.isPay == 2){//2:申请退款 3:退款成功 4:退款拒绝 5:退款失败
                task_status_content.text = "未通过"
                update_task_status.text = "退款中"
                update_task_status.setTextColor(resources.getColor(R.color.color_E4E4E4))
            }else if (sendTaskBean.isPay == 3){//退款成功
                task_status_content.text = "未通过"
                update_task_status.text = "退款成功"
                update_task_status.setTextColor(resources.getColor(R.color.color_E4E4E4))
            }else if(sendTaskBean.isPay == 4){
                task_status_content.text = "未通过"
                update_task_status.text = "退款拒绝"
                update_task_status.setTextColor(resources.getColor(R.color.color_E4E4E4))
            }else if(sendTaskBean.isPay == 5){
                task_status_content.text = "未通过"
                update_task_status.text = "退款失败"
                update_task_status.setTextColor(resources.getColor(R.color.color_E4E4E4))
            }else{
                task_status_content.text = "重新编辑"
                update_task_status.text = "申请退款"
                update_task_status.setTextColor(resources.getColor(R.color.color_EF5B40))
            }
        }
        if (sendTaskBean.expires == 1){//已经下架
            task_status_content.text = "已下架"
            update_task_status.text = "查看详情"
            update_task_status.setTextColor(resources.getColor(R.color.color_EF5B40))
            task_status_content_container.setBackgroundColor(resources.getColor(R.color.color_E4E4E4))
        }

        if (sendTaskBean.isPass == 3){
            review_continer.visibility = View.VISIBLE
            review_result.text = "审核不通过:${sendTaskBean.reason}"
        }else{
            review_continer.visibility = View.GONE
        }

        updateUIForTaskBean()
    }

    /**
     * 根据草稿更新UI
     */
    private fun updateUIForTaskBean(){
        val taskmoneyBigDecimal = sendTaskBean.taskmoney.toBigDecimal()
        val division = 100.toBigDecimal()
        val price = taskmoneyBigDecimal.divide(division,2,BigDecimal.ROUND_UP).toPlainString()
        task_company_name.text = sendTaskBean.className
        task_classification_name.text = sendTaskBean.typeName
        send_task_title_edt.text.clear()
        send_task_title_edt.text.append(sendTaskBean.taskname)
        send_task_end_time.text = "${com.da.library.tools.DateUtil.timeToString(sendTaskBean.extime, com.da.library.tools.DateUtil.yyyy_MM_dd)}"
        send_task_unit_price_edt.text.clear()
        send_task_unit_price_edt.text.append(price)
        send_task_count_edt.text.clear()
        send_task_count_edt.text.append(sendTaskBean.classnum.toString())
        send_task_description_edt.text.clear()
        send_task_description_edt.text.append(sendTaskBean.taskexplain)
//        send_task_advance_payment_edt.text = advancePayment
        calculationAdvancePayment()//计算预付金
        send_task_description_count.text = "${sendTaskBean.taskexplain.length}/40"
        task_release_guide.isChecked = true
        classificationTypeBean.typeName = sendTaskBean.className
        classificationTypeBean.typeId = sendTaskBean.classification

        companyTypeBean.typeName = sendTaskBean.typeName
        companyTypeBean.typeId = sendTaskBean.type
    }

    /**
     * 切换页面编辑状态
     */
    private fun switchEdt(isEdt :Boolean){
        send_task_company_type_ll.isEnabled = isEdt
        send_task_classification_ll.isEnabled = isEdt
        send_task_title_edt.isEnabled = isEdt
        send_task_end_time.isEnabled = isEdt
        send_task_unit_price_edt.isEnabled = isEdt
        send_task_count_edt.isEnabled = isEdt
        send_task_description_edt.isEnabled = isEdt
    }

    /**
     * 获取零钱信息
     */
    private fun getUserCloudWallet(){
        mPresenter.getUserCloudWallet(DqUrl.url_user_cloud_wallet, hashMapOf())
    }

    private fun createTask(){
        val isPass = checkParams()
        if (!isPass) return
        val params =createParams()
        if (null == mPresenter) return
        mPresenter.getCreateTask(DqUrl.url_task_createTask,params)
    }

    /**
     * 根据任务ID获取任务详情
     */
    private fun getUserTaskByTaskId(){
        val params = hashMapOf<String,String>()
        params["taskId"] = taskId
        if (null == mPresenter) return
        mPresenter.getCreateTask(DqUrl.url_task_userTaskSelect,params)
    }

    /**
     * 检查参数
     */
    private fun checkParams():Boolean {
        if (!task_release_guide.isChecked) {
            DqToast.showCenterShort("请同意《发布规则》!")
            return false
        }

        val taskCompanyName = task_company_name.text.toString()

        if (TextUtils.isEmpty(taskCompanyName)){
            DqToast.showCenterShort("请选择任务平台!")
            return false
        }

        val taskClassifitionName = task_classification_name.text.toString()

        if (TextUtils.isEmpty(taskClassifitionName)){
            DqToast.showCenterShort("请填写任务类别!")
            return false
        }

        val taskName = send_task_title_edt.text.toString()

        if (TextUtils.isEmpty(taskName)){
            DqToast.showCenterShort("请填写任务标题!")
            return false
        }

        val calender = Calendar.getInstance(Locale.CHINESE)
        calender.add(Calendar.DATE,1);//把日期往后增加两天.整数往后推,负数往前移动
        calender.set(Calendar.HOUR_OF_DAY, 0)//时
        calender.set(Calendar.MINUTE, 0)//分
        calender.set(Calendar.SECOND, 0)//秒
        val timeAfter2 = calender.timeInMillis
        val currentTime = DateUtil.getStringToCalendar(send_task_end_time.text.toString(),DateUtil.YMD).timeInMillis
        if(currentTime < timeAfter2){
            DqToast.showCenterShort("截止日期需要设置在两天后!")
            return false
        }

        val taskUnitPriceStr = send_task_unit_price_edt.text.toString()
        if (TextUtils.isEmpty(taskUnitPriceStr)){
            DqToast.showCenterShort("任务单价不能小于0.2元!")
            return false
        }

        val taskUnitPrice = taskUnitPriceStr.toDouble()

        if (taskUnitPrice < 0.2){
            DqToast.showCenterShort("任务单价不能小于0.2元!")
            return false
        }

        if (taskUnitPrice > 20){
            DqToast.showCenterShort("任务单价不大于20元!")
            return false
        }

        val taskNumStr = send_task_count_edt.text.toString()
        if (TextUtils.isEmpty(taskNumStr)){
            DqToast.showCenterShort("任务数量不能小于15个!")
            return false
        }

        val taskNum = taskNumStr.toInt()
        if (taskNum < 15){
            DqToast.showCenterShort("任务数量不能小于15个!")
            return false
        }

        if (taskNum > 100){
            DqToast.showCenterShort("任务数量不大于100个!")
            return false
        }

        val taskDescription = send_task_description_edt.text.toString()
        if (TextUtils.isEmpty(taskDescription)){
            DqToast.showCenterShort("任务描述不能为空!")
            return false
        }
        return true
    }

    private fun createParams(): Map<String,String>{
        val params = hashMapOf<String,String>()
        val taskMoneyStr = send_task_unit_price_edt.text.toString().toBigDecimal()
        val taskMoney = BigDecimalUtils.int2BigDecimal(100).multiply(taskMoneyStr).setScale(2, BigDecimal.ROUND_UP)//保留两位小数,直接进位处理
        params["createuserid"] = ModuleMgr.getCenterMgr().uid
        params["taskname"] = send_task_title_edt.text.toString()
        params["classification"] = classificationTypeBean.typeId.toString()
        params["type"] = companyTypeBean.typeId.toString()
        params["classnum"] = send_task_count_edt.text.toString()
        params["taskmoney"] = taskMoney.longValueExact().toString()
        params["taskexplain"] = send_task_description_edt.text.toString()
        params["extime"] = DateUtil.getStringToCalendar(send_task_end_time.text.toString(),DateUtil.YMD).timeInMillis.toString()
        return params
    }

    override fun onMessage(key: String?, value: Any?) {
        when(key){
            MsgType.TASK_PAY_RESULT -> {//微信支付后的订单信息
                getUserTaskByTaskId()
            }
            MsgType.TASK_REFUND -> {//微信支付后的订单信息
                getUserTaskByTaskId()
            }
        }
    }

    /**
     * 初始化监听
     */
    override fun initListener() {
        super.initListener()
        send_task_unit_price_edt.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                //内容输入结束
                calculationAdvancePayment()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        send_task_count_edt.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                //内容输入结束
                calculationAdvancePayment()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        send_task_description_edt.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val wordCount = s?.length ?: 0
                send_task_description_count.text = "$wordCount/40"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }


    /**
     * 计算预付金
     */
    private fun calculationAdvancePayment(){
        if (TextUtils.isEmpty(send_task_unit_price_edt.text.toString()) || TextUtils.isEmpty(send_task_count_edt.text.toString())){
            send_task_advance_payment_edt.text = "成交额10%为服务费"
            return
        }
        val cacluatRate = BigDecimalUtils.str2BigDecimal("0.1")//10%的预付金比例
        val unitPriceStr = send_task_unit_price_edt.text.toString()
        val countStr = send_task_count_edt.text.toString()

        val unitPrice = BigDecimalUtils.str2BigDecimal(unitPriceStr)
        val count = BigDecimalUtils.str2BigDecimal(countStr)
        val countPrice = unitPrice.multiply(count).setScale(2, BigDecimal.ROUND_UP)//保留两位小数,直接进位处理
        val calculationAdvancePayment = countPrice.multiply(cacluatRate).setScale(2, BigDecimal.ROUND_UP)//保留两位小数,直接进位处理
//        send_task_advance_payment_edt.text = calculationAdvancePayment.toPlainString() + "￥"
        countPriceInt = countPrice.multiply(BigDecimalUtils.int2BigDecimal(100)).setScale(2, BigDecimal.ROUND_UP).toDouble()

        send_task_advance_payment_edt.text = countPrice.add(calculationAdvancePayment).toPlainString() + "￥"

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v){
            send_task_company_type_ll -> {
                showTypeDialog(TaskTypeDialog.TYPE_COMPANY)
            }
            send_task_classification_ll -> {
                showTypeDialog(TaskTypeDialog.TYPE_CLASSIFICATION)
            }
            send_task_end_time -> {
                mPvOptions?.show()
            }
            task_status_content_container -> {

                updateTaskStatus2()
            }
            update_task_status ->{
                updateTaskStatus()
            }
        }
    }

    private fun updateTaskStatus2(){
        if (!TextUtils.isEmpty(taskId)){
            updateTaskContent()
            return
        }
        if (sendTaskBean.isPay == 0){
            createTask()
        }else if(sendTaskBean.isPay == 1){
            if(sendTaskBean.isPass == 0){
                createTask()
            }else if(sendTaskBean.isPass == 3){
                changeTaskStatus("0")//改为未提交状态
            }
        }
    }

    /**
     * 更改任务状态
     */
    private fun changeTaskStatus(status :String){
        val params = hashMapOf<String,String>()
        params["status"] = status
        params["taskId"] = sendTaskBean.id
        mPresenter.changeStatus(DqUrl.url_task_changeStatus,params)
    }

    /**
     * 根据不同状态对任务进行更改
     */
    private fun updateTaskStatus(){
        if (sendTaskBean.isPass == 0){
            DqToast.showCenterShort("保存草稿成功~")
            createTaskDraft()
            return
        }
        if (sendTaskBean.expires == 1){//已经下架
            //查看详情
            NavUtils.gotoReleaseTaskCompleteActivity(this,sendTaskBean.id)
            return
        }
        if(sendTaskBean.isPass == 1){//审核中
            changeTaskStatus("0")//改为未提交状态
        }else if (sendTaskBean.isPass == 2){//发布成功
            //查看详情
            NavUtils.gotoReleaseTaskCompleteActivity(this,sendTaskBean.id)
        }else if (sendTaskBean.isPass == 3){//拒绝
            if(sendTaskBean.isPay == 1){//已经付款
                //申请退款
                NavUtils.gotoApplyRefundActivity(this,sendTaskBean.id)
            }else if (sendTaskBean.isPay == 3){//退款成功
//                changeTaskStatus("0")//改为未提交状态
                //查看退款状态
                NavUtils.gotoRefundDetailsActivity(this,sendTaskBean.id)
            }else if (sendTaskBean.isPay == 2){//申请退款中
                //申请退款
                NavUtils.gotoRefundDetailsActivity(this,sendTaskBean.id)
            }else if(sendTaskBean.isPay == 4){
                //退款拒绝
                NavUtils.gotoRefundDetailsActivity(this,sendTaskBean.id)
            }else if(sendTaskBean.isPay == 5){
                //退款失败
                NavUtils.gotoRefundDetailsActivity(this,sendTaskBean.id)
            }
        }

    }

    private fun initDateDialog(){
        val now = Calendar.getInstance()
        val years = now[Calendar.YEAR]
        val date = now[Calendar.MONTH] + 1
        val listYear = com.da.library.tools.DateUtil.getWheelYearLayer(this, now)
        val listMonth = com.da.library.tools.DateUtil.getWheelMonth(this)
        val listDate = com.da.library.tools.DateUtil.getWheelDate(this)
        mPvOptions = DialogUtils.showYearToDate(this, listYear, listMonth
                , listDate, date, this)
    }

    /**
     * 初始化《发布规则》的内容
     */
    private fun initReleaseGuideUI(){
        val content = "我已阅读并遵守《发布规则》的全部内容"
        val look = "《发布规则》"
        val startIndex = content.indexOf(look)
        val endIndex = content.indexOf(look) + look.length
        val spannable = SpannableStringBuilder(content)
//        val foreGroundColor = ForegroundColorSpan(Color.parseColor("#EF5B40"))
//        spannable.setSpan(foreGroundColor,startIndex,endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(CustomClickAble(),startIndex,endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        send_task_release_guide.text = spannable
        send_task_release_guide.movementMethod = LinkMovementMethod.getInstance() //加上这句话才有效果
//        read_demo_video.highlightColor = ContextCompat.getColor(read_demo_video.context,R.color.transparent) //去掉点击后的背景颜色为透明
    }

    /**
     * 显示平台选择对话框
     */
    private fun showTypeDialog(type :Int){
        val taskTypeDialog = TaskTypeDialog()
        val bundle = Bundle()
        bundle.putInt(TaskTypeDialog.TYPE,type)
        taskTypeDialog.arguments = bundle
        taskTypeDialog.show(supportFragmentManager,"")
        taskTypeDialog.setOnResultCallBack(object : TaskTypeDialog.ResultCallBack{
            override fun onResult(taskTypeBean: CustomTaskTypeBean, type: Int) {
                when(type){
                    TaskTypeDialog.TYPE_COMPANY -> {//厂商渠道类型
                        companyTypeBean = taskTypeBean
                        task_company_name.text = taskTypeBean.typeName
                    }
                    TaskTypeDialog.TYPE_CLASSIFICATION -> {//任务平台类型
                        classificationTypeBean = taskTypeBean
                        task_classification_name.text = taskTypeBean.typeName
                    }
                }
            }
        })
    }

    /**
     * 检查支付类型
     */
    private fun checkPayType() {
        if (countPriceInt < userWallet.getBalance()) { //当支付金额小于零钱余额时候，直接发送出去，后台收到这条消息时候会直接根据红包消息去扣除零钱余额
            payType = MessageRedPackageBean.RED_PACKAGE_CHANGE
            //                    MsgMgr.getInstance().sendMsg(MsgType.CHAT_RED_PACKAGE, messageRedPackageBean);
//                    finish();
            smallChangePay()
        } else {
            payType = MessageRedPackageBean.RED_PACKAGE_WX
            createTaskOrder("")
        }
    }
    /**
     * 调用零钱支付功能
     */
    private fun smallChangePay(){
        Log.e("YM", "零钱支付")
        val dialog = PayPassDialog(this)
        dialog.payViewPass
                .setPayClickListener(object : OnPayClickListener {
                    override fun onPassFinish(passContent: String) {
                        //6位输入完成回调
                        var pwd: String? = ""
                        try {
                            pwd = AESUtil.encrypt(passContent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        createTaskOrder(pwd?:"")
                        dialog.dismiss()
                    }

                    override fun onPayClose() {
                        dialog.dismiss()
                        //关闭回调
                    }

                    override fun onPayForget() {
                        dialog.dismiss()
                        //点击忘记密码回调
                        DqToast.showShort("忘记密码回调")
                    }
                })
    }

    override fun payDetailClick(mYear: String?, mMonth: String?, date: String?) {
        val endTime = "$mYear-$mMonth-$date"
        send_task_end_time.text = endTime
    }


    private fun createTaskOrder(pwd :String){
        val params: MutableMap<String?, String?> = HashMap()
        params["userId"] = ModuleMgr.getCenterMgr().uid
        params["taskId"] = sendTaskBean.id
        params["pwd"] = pwd
        params["couponPayType"] = payType.toString() //付款类型 1:微信 2:余额
        val ketSet: Set<String?> = params.keys
        for (key in ketSet) {
            val value = params[key]
            DqLog.e("YM--红包->key:$key--->value:$value")
        }
        mPresenter.getWeChatPayOrderInfo(DqUrl.url_task_createTaskOrder, params)
    }

    /**
     * 创建任务草稿
     */
    private fun createTaskDraft(){
        val unitPrice = send_task_unit_price_edt.text.toString()
        sendTaskDraftBean.classificationTypeBean = classificationTypeBean
        sendTaskDraftBean.companyTypeBean = companyTypeBean
        sendTaskDraftBean.taskname = send_task_title_edt.text.toString()
        sendTaskDraftBean.classnum = send_task_count_edt.text.toString()
        sendTaskDraftBean.taskmoney = unitPrice
        sendTaskDraftBean.taskexplain = send_task_description_edt.text.toString()
        sendTaskDraftBean.extime = send_task_end_time.text.toString()
        sendTaskDraftBean.advancePayment = send_task_advance_payment_edt.text.toString()
        lastTaskSendDraft = GsonUtils.toJson(sendTaskDraftBean)
        DqLog.e("YM","任务草稿内容:$lastTaskSendDraft")
        ModuleMgr.getCenterMgr().saveLastTaskSendDraft(lastTaskSendDraft)
    }

    /**
     * 更改任务内容
     */
    private fun updateTaskContent(){
        val taskMoneyStr = send_task_unit_price_edt.text.toString().toBigDecimal()
        val taskMoney = BigDecimalUtils.int2BigDecimal(100).multiply(taskMoneyStr).setScale(2, BigDecimal.ROUND_UP)//保留两位小数,直接进位处理

        val params = hashMapOf<String,String>()
        params["id"] = sendTaskBean.id
        params["taskname"] = send_task_title_edt.text.toString()
        params["createuserid"] = sendTaskBean.createuserid
        params["classification"] = sendTaskBean.classification
        params["type"] = companyTypeBean.typeId.toString()
        params["classnum"] = send_task_count_edt.text.toString()
        params["taskmoney"] = taskMoney.longValueExact().toString()
        params["taskpic"] = ""
        params["priority"] = sendTaskBean.priority.toString()
        params["createtime"] = sendTaskBean.createtime.toString()
        params["extime"] = DateUtil.getStringToCalendar(send_task_end_time.text.toString(),DateUtil.YMD).timeInMillis.toString()
        params["expires"] = sendTaskBean.expires.toString()
        params["taskexplain"] = send_task_description_edt.text.toString()
        params["reviewtime"] = sendTaskBean.reviewtime
        params["lastnum"] = ""
        params["isPass"] = sendTaskBean.isPass.toString()
        params["isPay"] = sendTaskBean.isPay.toString()
        params["reason"] = sendTaskBean.reason ?: ""
        mPresenter.changeTask(DqUrl.url_task_changeTask,params)
    }


    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)
        if (null == entity) return
        when(url){
            DqUrl.url_user_cloud_wallet -> {//零钱信息
                userWallet = entity.data as UserCloudWallet
            }
            DqUrl.url_task_createTask -> {//创建任务订单信息
                sendTaskBean = entity.data as SendTaskBean
                taskId = sendTaskBean.id
                checkPayType()
            }
            DqUrl.url_task_changeTask -> {
                sendTaskBean = entity.data as SendTaskBean
                taskId = sendTaskBean.id
                companyTypeBean.typeName = sendTaskBean.typeName
                companyTypeBean.typeId = sendTaskBean.type
                classificationTypeBean.typeName = sendTaskBean.className
                classificationTypeBean.typeId = sendTaskBean.classification
                checkPayType()
            }
            DqUrl.url_task_userTaskSelect -> {//获取任务详情
                sendTaskBean = entity.data as SendTaskBean
                taskId = sendTaskBean.id
                updateUIForDetails()
                companyTypeBean.typeName = sendTaskBean.typeName
                companyTypeBean.typeId = sendTaskBean.type
                classificationTypeBean.typeName = sendTaskBean.className
                classificationTypeBean.typeId = sendTaskBean.classification
            }
            DqUrl.url_task_changeStatus -> {
                DqToast.showCenterShort("已经提交退款请求!")
                sendTaskBean = entity.data as SendTaskBean
                taskId = sendTaskBean.id
                updateUIForDetails()
                companyTypeBean.typeName = sendTaskBean.typeName
                companyTypeBean.typeId = sendTaskBean.type
                classificationTypeBean.typeName = sendTaskBean.className
                classificationTypeBean.typeId = sendTaskBean.classification
            }
            DqUrl.url_task_type -> {//厂商类型
                val taskTypeBean = entity.data as List<TaskTypeBean>
                val customerTaskBean = taskTypeBean.map {
                    CustomTaskTypeBean(it.id.toString(),it.typePic,it.typeName)
                }
                companyTypeBean = customerTaskBean[0]
                task_company_name.text = companyTypeBean.typeName
            }
            DqUrl.url_task_classification ->{//任务类型
                val taskTypeBean = entity.data as List<TaskClassificationBean>
                val customerTaskBean = taskTypeBean.map {
                    CustomTaskTypeBean(it.id.toString(),it.classPic,it.className)
                }
                classificationTypeBean = customerTaskBean[0]
                task_classification_name.text = classificationTypeBean.typeName
            }
            DqUrl.url_task_createTaskOrder -> {//付款状态
                //订单创建完毕后清空内容
                ModuleMgr.getCenterMgr().saveLastTaskSendDraft("")
                val wxPayBody = entity.data as WxPayBody
                if (MessageRedPackageBean.RED_PACKAGE_CHANGE == payType) {
//                    finish()
                    getUserTaskByTaskId()
                } else {
                    mPresenter.startWeChatPay(this, wxPayBody)
                }
            }
        }

    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onFailed(url, code, entity)
        DqToast.showCenterShort(entity?.content)
    }

    override fun onDestroy() {
        super.onDestroy()
        MsgMgr.getInstance().detach(this)
    }

    /**
     * 自定义点击事件
     */
    inner class CustomClickAble() : ClickableSpan() {

        override fun onClick(widget: View) {
//            NavUtils.playVideo(widget.context,taskDetailBean.teachingVideoUrl)
            NavUtils.gotoWebviewActivity(widget.context, DqUrl.url_fbgz, getString(R.string.release_guide))
        }


        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = Color.parseColor("#EF5B40")
            ds.isUnderlineText = false //去除超链接的下划线
        }
    }

}