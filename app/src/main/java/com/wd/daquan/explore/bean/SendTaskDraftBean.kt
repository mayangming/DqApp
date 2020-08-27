package com.wd.daquan.explore.bean

/**
 * 保存发布任务的草稿内容
 */
class SendTaskDraftBean {
    var companyTypeBean :CustomTaskTypeBean = CustomTaskTypeBean() //任务类型
    var classificationTypeBean :CustomTaskTypeBean = CustomTaskTypeBean() //厂商类型
    var taskname = ""//任务名称
    var classnum = ""//任务数量
    var taskmoney = ""//任务金额
    var taskexplain = ""//任务描述
    var extime = ""//任务结束时间
    var advancePayment = ""//预付金额
}