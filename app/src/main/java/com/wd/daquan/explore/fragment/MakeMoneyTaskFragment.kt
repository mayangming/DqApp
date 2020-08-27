package com.wd.daquan.explore.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.wd.daquan.R
import com.wd.daquan.bean.TaskObj
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.common.fragment.BaseFragment
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.adapter.MakeMoneyTaskAdapter
import com.wd.daquan.explore.presenter.MakeMoneyPresenter
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.MakeMoneyTaskBean
import com.wd.daquan.view.RecycleViewDivider
import kotlinx.android.synthetic.main.fragment_make_money_task.*

/**
 * 赚钱中的任务分页
 */
class MakeMoneyTaskFragment : BaseFragment<MakeMoneyPresenter, DataBean<List<MakeMoneyTaskBean>>>(), View.OnClickListener {
    private var pageNum = 1 //当前页码
    private var pageSize = 10 //每页显示的条数
    private val taskAdapter = MakeMoneyTaskAdapter()
    private var taskModel = arrayListOf<MakeMoneyTaskBean>()
    private lateinit var rootView: ViewGroup;
    private var listType :Int = 1// 0:综合 1: 金额 2:时间
    private var taskObj: TaskObj ?= null
    private var rank = 0 //排序规则 0:倒序 1:正序
    companion object{
        const val KEY_LIST_TYPE = "listType"
    }

    override fun getLayoutId() = R.layout.fragment_make_money_task

    override fun createPresenter() = MakeMoneyPresenter()

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        rootView = view as ViewGroup
//        MsgMgr.getInstance().attach(this)
        initSmartRefreshLayout()
        initRecycleView()

    }

    override fun initListener() {
    }

    override fun initData() {
        listType = arguments?.getInt(KEY_LIST_TYPE,1) ?: 1
        getTaskList()
    }

    override fun onClick(v: View?) {
    }

//    override fun onMessage(key: String?, value: Any?) {
//        DqLog.e("YM--------->onMessage:${listType}")
//        if (key == MsgType.TASK_OBJ){
//            pageNum = 1
//            val taskObj = value as TaskObj
//            getTaskList(taskObj)
//        }
//    }

    private fun getTaskList(){
        val taskType = taskObj?.taskTypeBeans?.map {
            it.id
        }
        val taskTaskClassification = taskObj?.taskClassificationBeans?.map {
            it.id
        }

        val params = hashMapOf<String,Any>()
        params["type"] = taskType ?: arrayListOf<Int>()
        params["classification"] = taskTaskClassification ?: arrayListOf<Int>()
        params["pageNum"] = pageNum.toString()
        params["pageSize"] = pageSize.toString()
        params["listType"] = listType
        params["rank"] = rank
        if (null == mPresenter) return
        mPresenter.getTaskList(DqUrl.url_task_screening,params)
    }

    /**
     * 初始化上拉加载和下拉刷新
     */
    private fun initSmartRefreshLayout(){
        make_money_refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                pageNum++
                getTaskList()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                pageNum = 1
                getTaskList()
            }

        })
    }

    private fun initRecycleView(){
        make_money_task_rv.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        make_money_task_rv.emptyView = layoutInflater.inflate(R.layout.layout_empty_view,rootView,false)
        make_money_task_rv.addItemDecoration(RecycleViewDivider(activity,LinearLayoutManager.VERTICAL,10,resources.getColor(R.color.color_F8F8F8)))
        make_money_task_rv.adapter = taskAdapter
        taskAdapter.setRecycleItemOnClickListener { _, position ->
            val bean = taskModel[position]
            NavUtils.gotoTaskDetailsActivity(context,bean.id)
        }
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<List<MakeMoneyTaskBean>>?) {
        super.onSuccess(url, code, entity)
        if(null == entity){
            return
        }
        when(url){
            DqUrl.url_task_screening -> {
                if (pageNum == 1){
                    taskModel.clear()
                }
                val data = entity.data
                taskModel.addAll(data)
                taskAdapter.makeMoneyTaskBeanList = taskModel
                make_money_refreshLayout.closeHeaderOrFooter()
            }
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<List<MakeMoneyTaskBean>>) {
        super.onFailed(url, code, entity)
        make_money_refreshLayout.closeHeaderOrFooter()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()

    }

    /**
     * 更新接口数据
     */
    public fun refreshData(taskObj :TaskObj){
        pageNum = 1
        this.taskObj = taskObj
        getTaskList()
    }

    /**
     * 更改排序规则
     */
    fun updateSort(){
        rank = if (rank == 0){
            1
        }else{
            0
        }
        getTaskList()
    }
}