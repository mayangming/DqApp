package com.wd.daquan.explore.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netease.nim.uikit.common.ui.recyclerview.decoration.SpacingDecoration
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.wd.daquan.R
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.common.fragment.BaseFragment
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.adapter.SendTaskAdapter
import com.wd.daquan.explore.presenter.SendTaskPresenter
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.SendTaskBean
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.rxbus.MsgMgr
import com.wd.daquan.model.rxbus.MsgType
import com.wd.daquan.model.rxbus.QCObserver
import kotlinx.android.synthetic.main.fragment_send_task.*

class SendTaskManagerFragment: BaseFragment<SendTaskPresenter, DataBean<List<SendTaskBean>>>(), View.OnClickListener, QCObserver{
    private var pageNum = 1 //当前页码
    private var pageSize = 10 //每页显示的条数
    private val taskAdapter = SendTaskAdapter()
    private var taskModel = arrayListOf<SendTaskBean>()
    private lateinit var rootView: ViewGroup;
    private var listType :Int = 0// 0:全部 1:待审核 2:已上架 3:未通过 4:已下架
    private var rank = 0 //排序规则 0:倒序 1:正序
    companion object{
        const val KEY_LIST_TYPE = "listType"
        const val SEND_TASK_ALL = 0
        const val SEND_TASK_REVIEWING = 1
        const val SEND_TASK_SHANGJIA = 2
        const val SEND_TASK_NO_PASS = 3
        const val SEND_TASK_OFF_SELF = 4
    }

    override fun getLayoutId() = R.layout.fragment_send_task

    override fun createPresenter() = SendTaskPresenter()

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        rootView = view as ViewGroup
//        MsgMgr.getInstance().attach(this)
        initSmartRefreshLayout()
        initRecycleView()
    }

    override fun initListener() {
    }

    override fun initData() {
        listType = arguments?.getInt(MakeMoneyTaskFragment.KEY_LIST_TYPE,1) ?: 1
        MsgMgr.getInstance().attach(this)
        getTaskList()
    }

    override fun onMessage(key: String?, value: Any?) {
        when(key){
            MsgType.TASK_PAY_RESULT -> {
                getTaskList()
            }
        }
    }

    override fun onClick(v: View?) {
    }

    fun refreshList(){
        pageNum = 1
        getTaskList()
    }

    private fun getTaskList(){

        val params = hashMapOf<String,Any>()
        params["pageNum"] = pageNum.toString()
        params["pageSize"] = pageSize.toString()
        params["listStyle"] = listType
        if (null == mPresenter) return
        mPresenter.getSendTaskBeanList(DqUrl.url_task_getUserTask,params)
    }

    /**
     * 初始化上拉加载和下拉刷新
     */
    private fun initSmartRefreshLayout(){
        send_task_refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
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
        send_task_rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        send_task_rv.emptyView = layoutInflater.inflate(R.layout.layout_empty_view,rootView,false)
        send_task_rv.addItemDecoration(SpacingDecoration(10,20,false))
        send_task_rv.adapter = taskAdapter
        taskAdapter.setRecycleItemOnClickListener { _, position ->
            val bean = taskModel[position]
            NavUtils.gotoSendTaskActivity(context,bean.id)
        }
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<List<SendTaskBean>>?) {
        super.onSuccess(url, code, entity)
        if(null == entity){
            return
        }
        when(url){
            DqUrl.url_task_getUserTask -> {
                if (pageNum == 1){
                    taskModel.clear()
                }
                val data = entity.data
                taskModel.addAll(data)
                taskAdapter.makeMoneyTaskBeanList = taskModel
                send_task_refreshLayout.closeHeaderOrFooter()
            }
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<List<SendTaskBean>>) {
        super.onFailed(url, code, entity)
        send_task_refreshLayout.closeHeaderOrFooter()
    }

    override fun onDestroy() {
        super.onDestroy()
        MsgMgr.getInstance().detach(this)
    }

    override fun onResume() {
        super.onResume()

    }

    /**
     * 更新接口数据
     */
    public fun refreshData(){
        pageNum = 1
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