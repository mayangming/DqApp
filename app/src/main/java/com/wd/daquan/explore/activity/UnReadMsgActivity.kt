package com.wd.daquan.explore.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.da.library.listener.DialogListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.common.utils.DialogUtils
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.adapter.AreaUnReadAdapter
import com.wd.daquan.explore.presenter.FriendAreaPresenter
import com.wd.daquan.explore.type.SearchType
import com.wd.daquan.model.bean.AreaUnReadBean
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.rxbus.MsgMgr
import com.wd.daquan.model.rxbus.MsgType
import kotlinx.android.synthetic.main.activity_unread.*

/**
 * 未读消息列表
 */
class UnReadMsgActivity : DqBaseActivity<FriendAreaPresenter, DataBean<Any>>(){
    private var pageNum = 1 //当前页码
    private var pageSize = 10 //每页显示的条数
    private val unReadAdapter = AreaUnReadAdapter()

    override fun createPresenter() = FriendAreaPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_unread)
    }

    override fun initView() {
        initTitle()
        initRecycleView()
        initSmartRefreshLayout()
    }

    override fun initData() {
        findUserDynamicMsg()
        userDynamicMsgMongoDao()
    }


    private fun initTitle(){
        friend_unread_title.title = "消息"
        friend_unread_title.setRightTxt("清空")
        friend_unread_title.setRightTxtColor(R.color.color_666666)
        friend_unread_title.leftIv.setOnClickListener { finish() }
        friend_unread_title.setRightVisible(View.GONE)
        friend_unread_title.rightTv.setOnClickListener(this)
    }
    
    private fun initRecycleView() {
        friend_new_unread_rv.apply {
            layoutManager = LinearLayoutManager(this@UnReadMsgActivity,RecyclerView.VERTICAL,false)
            adapter = unReadAdapter
            emptyView = layoutInflater.inflate(R.layout.layout_empty_view,friend_unread_refreshLayout,false)
        }
        unReadAdapter.setRecycleItemOnClickListener { _, position ->
            val bean = unReadAdapter.areaUnReadBeanList[position]
            NavUtils.gotoFriendAreaActivity(this, bean.userId, SearchType.PERSON,bean.dynamicId.toString())
        }
    }

    /**
     * 初始化上拉加载和下拉刷新
     */
    private fun initSmartRefreshLayout(){
        friend_unread_refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                findUserDynamicMsg()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                pageNum = 1
                findUserDynamicMsg()
            }

        })
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v){
            friend_unread_title.rightTv -> {
                DialogUtils.showCommonDialogForBoth(this,"提示","确定清空吗?",object : DialogListener {
                    override fun onCancel() {
                    }

                    override fun onOk() {
                        delUserDynamicMsg()
                    }
                })
            }
        }
    }

    private fun userDynamicMsgMongoDao(){
        val params = hashMapOf<String, String>()
        mPresenter.commonRequest(DqUrl.url_dynamic_userDynamicMsgMongoDao,params)
    }

    private fun delUserDynamicMsg(){
        val params = hashMapOf<String, String>()
        mPresenter.delUserDynamicMsg(DqUrl.url_dynamic_delUserDynamicMsg,params)
    }

    private fun findUserDynamicMsg(){
        val params = hashMapOf<String, String>()
        params["pageNum"] = pageNum.toString()
        params["pageSize"] = pageSize.toString()
        params["status"] = "0" //状态 1：未读 2：已读  0:所有
        mPresenter.findUserDynamicMsg(DqUrl.url_dynamic_findUserDynamicMsg,params)
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)
        when(url){
            DqUrl.url_dynamic_findUserDynamicMsg -> {
                if (null == entity){
                    return
                }
                friend_unread_refreshLayout.closeHeaderOrFooter()
                val dataList = entity.data as ArrayList<AreaUnReadBean>
                if(dataList.isEmpty()){
                    return
                }
                if (pageNum == 1){
                    unReadAdapter.areaUnReadBeanList = dataList
                }else{
                    unReadAdapter.addData(dataList)
                }
                pageNum++
            }
            DqUrl.url_dynamic_delUserDynamicMsg -> {
                pageNum = 1
                unReadAdapter.areaUnReadBeanList = arrayListOf()
            }
            DqUrl.url_dynamic_userDynamicMsgMongoDao -> {
                MsgMgr.getInstance().sendMsg(MsgType.CLEAR_UNREAD_AREA,null);
            }
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onFailed(url, code, entity)
        friend_unread_refreshLayout.closeHeaderOrFooter()
    }

}