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
import com.wd.daquan.explore.adapter.MakeMoneyTaskMineAdapter
import com.wd.daquan.explore.presenter.MakeMoneyPresenter
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.MakeMoneyTaskMineBean
import com.wd.daquan.model.rxbus.QCObserver
import com.wd.daquan.view.RecycleViewDivider
import kotlinx.android.synthetic.main.fragment_make_money_task.*
import kotlinx.android.synthetic.main.fragment_make_money_task.make_money_refreshLayout
import kotlinx.android.synthetic.main.fragment_make_money_task_mine.*
import kotlinx.android.synthetic.main.layout_empty_view.view.*

/**
 * 赚钱任务功能的我的功能
 */
class MakeMoneyTaskMineTaskFragment():  BaseFragment<MakeMoneyPresenter, DataBean<List<MakeMoneyTaskMineBean>>>(), View.OnClickListener, QCObserver {
    private var pageNum = 1 //当前页码
    private var pageSize = 10 //每页显示的条数
    private val taskAdapter = MakeMoneyTaskMineAdapter()
    private var taskModel = arrayListOf<MakeMoneyTaskMineBean>()
    private var taskStatus = "1"
    private lateinit var rootView: ViewGroup;
    companion object{
        const val TASK_STATUS = "taskStatus"
    }

    override fun getLayoutId() = R.layout.fragment_make_money_task_mine

    override fun createPresenter() = MakeMoneyPresenter()

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        rootView = view as ViewGroup
        initSmartRefreshLayout()
        initRecycleView()
    }

    override fun initListener() {
    }

    override fun initData() {
        taskStatus = arguments?.getString(TASK_STATUS,"1") ?: ""
        getTaskList()
    }

    override fun onClick(v: View?) {
    }

    override fun onMessage(key: String?, value: Any?) {
    }

    private fun getTaskList(){
        val params = hashMapOf<String,String>()
        params["pageNum"] = pageNum.toString()
        params["pageSize"] = pageSize.toString()
        params["taskStatus"] = taskStatus
        mPresenter.getMyTaskList(DqUrl.url_task_mine,params)
    }

    /**
     * 初始化上拉加载和下拉刷新
     */
    private fun initSmartRefreshLayout(){
        make_money_refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
//                getDynamicMessageList()
                pageNum++
                getTaskList()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                pageNum = 1
//                getDynamicMessageList()
                getTaskList()
            }

        })
    }

    private fun initRecycleView(){
        make_money_task_mine_rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        make_money_task_mine_rv.emptyView = layoutInflater.inflate(R.layout.layout_empty_view,rootView,false)
        make_money_task_mine_rv.addItemDecoration(SpacingDecoration(10,20,false))
        make_money_task_mine_rv.adapter = taskAdapter
        taskAdapter.setRecycleItemOnClickListener { _, position ->
            val bean = taskModel[position]
            NavUtils.gotoTaskDetailsActivity(context,bean.taskId)
        }
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<List<MakeMoneyTaskMineBean>>?) {
        super.onSuccess(url, code, entity)
        if(null == entity){
            return
        }

        when(url){
            DqUrl.url_task_mine -> {
                if (1 == pageNum){
                    taskModel.clear()
                }
                make_money_refreshLayout.closeHeaderOrFooter()
                val data = entity.data
                taskModel.addAll(data)
                taskAdapter.makeMoneyTaskMineList = taskModel
            }
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<List<MakeMoneyTaskMineBean>>?) {
        super.onFailed(url, code, entity)
        make_money_refreshLayout.closeHeaderOrFooter()
    }
}