package com.wd.daquan.explore.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import com.netease.nim.uikit.common.ui.recyclerview.decoration.SpacingDecoration
import com.wd.daquan.R
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.explore.adapter.SendTaskTypeAdapter
import com.wd.daquan.explore.bean.CustomTaskTypeBean
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.TaskClassificationBean
import com.wd.daquan.model.bean.TaskTypeBean
import com.wd.daquan.model.interfaces.DqCallBack
import com.wd.daquan.model.retrofit.RetrofitHelp
import kotlinx.android.synthetic.main.activity_unread.*
import kotlinx.android.synthetic.main.dialog_task_type.*

/**
 * 任务类型对话框
 */
class TaskTypeDialog :BaseFragmentDialog(){
    private var type = 1
    private var taskTypeAdapter = SendTaskTypeAdapter()
    private var resultCallBack :ResultCallBack ?= null
    companion object{
        const val TYPE = "taskType"//任务类型
        const val TYPE_COMPANY = 1//厂商类型
        const val TYPE_CLASSIFICATION = 2 //任务类型
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //设置全屏
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//需要这一行来解决对话框背景有白色的问题(颜色随主题变动)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_task_type,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        initData()
    }

    private fun initData(){
        type = arguments?.getInt(TYPE,TYPE_COMPANY) ?: TYPE_COMPANY
        getData(type)
    }

    private fun initRecycleView(){
        val gridLayoutManager = GridLayoutManager(context,4)
        dialog_task_type_rv.layoutManager = gridLayoutManager
        dialog_task_type_rv.emptyView = layoutInflater.inflate(R.layout.layout_empty_view,friend_unread_refreshLayout,false)
        dialog_task_type_rv.addItemDecoration(SpacingDecoration(10,20,false))
        dialog_task_type_rv.adapter = taskTypeAdapter
        taskTypeAdapter.setRecycleItemOnClickListener { _, position ->
            resultCallBack?.onResult(taskTypeAdapter.customTaskTypeBeanList[position],type)
            dismiss()
        }
    }

    /**
     * 获取数据
     */
    private fun getData(type: Int){
        val params = hashMapOf<String,String>()
        when(type){
            TYPE_COMPANY -> {
                getTaskCompanyList(DqUrl.url_task_type,params)
            }
            TYPE_CLASSIFICATION -> {
                getClassificationList(DqUrl.url_task_classification,params)
            }
        }
    }

    /**
     * 获取任务厂商类型列表
     */
    private fun getTaskCompanyList(url :String, hashMap :Map<String, Any>){
        RetrofitHelp.getTaskApi().findTaskType(url, RetrofitHelp.getRequestBodyByObject(hashMap)).enqueue(object : DqCallBack<DataBean<List<TaskTypeBean>>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<List<TaskTypeBean>>) {
                val data = entity.data
                val customerTaskBean = data.map {
                    CustomTaskTypeBean(it.id.toString(),it.typePic,it.typeName)
                }
                updateUi(customerTaskBean)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<List<TaskTypeBean>>) {

            }
        })
    }

    /**
     * 获取任务类型列表
     */
    private fun getClassificationList(url :String, hashMap :Map<String, Any>){
        RetrofitHelp.getTaskApi().findTaskClassification(url, RetrofitHelp.getRequestBodyByObject(hashMap)).enqueue(object : DqCallBack<DataBean<List<TaskClassificationBean>>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<List<TaskClassificationBean>>) {
                val data = entity.data
                val customerTaskBean = data.map {
                    CustomTaskTypeBean(it.id.toString(),it.classPic,it.className)
                }
                updateUi(customerTaskBean)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<List<TaskClassificationBean>>) {

//                        hideLoading();
            }
        })
    }

    private fun updateUi(customTaskBeanList :List<CustomTaskTypeBean>){
        taskTypeAdapter.customTaskTypeBeanList = customTaskBeanList
    }

    fun setOnResultCallBack(resultCallBack :ResultCallBack){
        this.resultCallBack = resultCallBack
    }

    interface ResultCallBack{
        fun onResult(taskTypeBean :CustomTaskTypeBean, type :Int)
    }
}