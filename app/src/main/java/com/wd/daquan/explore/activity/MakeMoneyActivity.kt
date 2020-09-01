package com.wd.daquan.explore.activity

import android.os.Bundle
import android.view.ActionMode
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.wd.daquan.R
import com.wd.daquan.bean.TaskObj
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.adapter.MakeMoneyPagerAdapter
import com.wd.daquan.explore.adapter.MakeMoneyTaskClassificationAdapter
import com.wd.daquan.explore.adapter.MakeMoneyTaskTypeAdapter
import com.wd.daquan.explore.fragment.MakeMoneyTaskFragment
import com.wd.daquan.explore.fragment.MakeMoneyTaskMineTaskFragment
import com.wd.daquan.explore.presenter.MakeMoneyPresenter
import com.wd.daquan.explore.viewmodel.FragmentItemViewModel
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.TaskClassificationBean
import com.wd.daquan.model.bean.TaskTypeBean
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.rxbus.MsgMgr
import com.wd.daquan.model.rxbus.MsgType
import com.wd.daquan.model.rxbus.QCObserver
import kotlinx.android.synthetic.main.activity_make_money.*

/**
 * 赚钱页面
 */
class MakeMoneyActivity: DqBaseActivity<MakeMoneyPresenter, DataBean<Any>>(), QCObserver {
    private var makeMoneyTaskTypeAdapter = MakeMoneyTaskTypeAdapter()
    private var makeMoneyTaskClassificationAdapter = MakeMoneyTaskClassificationAdapter()
    private var taskFragment1 = MakeMoneyTaskFragment()
    private var taskFragment2 = MakeMoneyTaskFragment()
    private var taskFragment3 = MakeMoneyTaskFragment()
    private var taskFragment4 = MakeMoneyTaskMineTaskFragment()
    private var lastFirst = false //是初次进入
    private lateinit var fragmentViewModel : FragmentItemViewModel
    override fun createPresenter() = MakeMoneyPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MsgMgr.getInstance().attach(this)
    }

    override fun setContentView() {
        setContentView(R.layout.activity_make_money)
    }

    override fun initView() {
        fragmentViewModel = ViewModelProvider(this)[FragmentItemViewModel::class.java]
        initTitle()
        initViewViewPager()
        initTabAndPagerListener(make_money_content_tab,make_money_content_vp)
        initTab(arrayListOf("综合","最新","佣金","我的"),make_money_content_tab)
        initRecycleView()
        make_money_drawable.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势
    }

    override fun initData() {
        showLoading()
        getTaskType()
        getTaskClassification()
    }

    override fun onMessage(key: String?, value: Any?) {
        DqLog.e("YM-------->接了新任务，开始刷新:${key}")
        when(key){
            MsgType.TASK_MAKE_MONEY_REFRESH -> {
                refreshList()
            }
        }
    }


    private fun refreshList(){
        taskFragment1.refreshList()
        taskFragment2.refreshList()
        taskFragment3.refreshList()
        taskFragment4.refreshList()
    }

    private fun getTaskType(){
        val params = hashMapOf<String,String>()
        mPresenter.getTaskType(DqUrl.url_task_type,params)
    }

    private fun getTaskClassification(){
        val params = hashMapOf<String,String>()
        mPresenter.getTaskClassificationBean(DqUrl.url_task_classification,params)
    }


    private fun initTitle(){
        make_money_title.title = "赚钱"
        make_money_title.setRightTxt("筛选")
        make_money_title.leftIv.setOnClickListener { finish() }
        make_money_title.setRightVisible(View.GONE)
        make_money_title.rightTv.setOnClickListener(this)
        make_money_title.rightTv.isSelected
    }

    private fun initRecycleView(){
        val gridTaskType = GridLayoutManager(this,3)
        make_money_task_platform_rv.layoutManager = gridTaskType
        make_money_task_platform_rv.adapter = makeMoneyTaskTypeAdapter

        val gridTaskClassification = GridLayoutManager(this,3)
        make_money_task_type_rv.layoutManager = gridTaskClassification
        make_money_task_type_rv.adapter = makeMoneyTaskClassificationAdapter
    }

    private fun initViewViewPager(){
        taskFragment1.also {
            val bundle = Bundle()
            bundle.putInt(MakeMoneyTaskFragment.KEY_LIST_TYPE,0)
            it.arguments = bundle
        }
        taskFragment2.also {
            val bundle = Bundle()
            bundle.putInt(MakeMoneyTaskFragment.KEY_LIST_TYPE,2)
            it.arguments = bundle
        }
        taskFragment3.also {
            val bundle = Bundle()
            bundle.putInt(MakeMoneyTaskFragment.KEY_LIST_TYPE,1)
            it.arguments = bundle
        }
        fragmentViewModel.addNewAt(taskFragment1)
        fragmentViewModel.addNewAt(taskFragment2)
        fragmentViewModel.addNewAt(taskFragment3)
        fragmentViewModel.addNewAt(taskFragment4)
        val pagerAdapter = MakeMoneyPagerAdapter(this)
        pagerAdapter.fragmentViewModel = fragmentViewModel
        make_money_content_vp.adapter = pagerAdapter
    }

    /**
     * 初始化Tab选项卡
     */
    private fun initTab(nameList :List<String>, tabLayout: TabLayout){
        nameList.forEach {
            val view = layoutInflater.inflate(R.layout.item_make_money_task_tab,tabLayout,false)
            val tabName = view.findViewById<TextView>(R.id.make_money_tab_tv)
            tabName.text = it
            val tab = tabLayout.newTab()
            tab.customView = view
            tabLayout.addTab(tab)
        }
        tabLayout.getTabAt(0)?.select()
    }

    private fun initTabAndPagerListener(tabLayout: TabLayout, viewPager2: ViewPager2){
        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {//再次选中
                val index = tab!!.position
                if (lastFirst){
                    lastFirst = false
                    return
                }
                if (index == 0) return
                val fragment = fragmentViewModel.getFragment(index)
                if(fragment is MakeMoneyTaskFragment){
                    fragment.updateSort()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {//没选中
                val view = tab!!.customView
                val titleTv = view!!.findViewById<TextView>(R.id.make_money_tab_tv)
                titleTv.setTextColor(resources.getColor(R.color.color_ff666666))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {//选中
                val index = tab!!.position
                lastFirst = true
                val view = tab.customView
                val titleTv = view!!.findViewById<TextView>(R.id.make_money_tab_tv)
                titleTv.setTextColor(resources.getColor(R.color.color_fff39825))
                viewPager2.setCurrentItem(index, true)
            }
        })
        viewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //选择的位置
                tabLayout.getTabAt(position)?.select()
                if(3 == position){
                    make_money_title.setRightTxt("管理")
                }else{
                    make_money_title.setRightTxt("筛选")
                }
            }
        })
    }

    override fun initListener() {
        super.initListener()
        make_money_task_screen_reset.setOnClickListener(this)
        make_money_task_screen_sure.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v){
            make_money_title.rightTv ->{
                val currentPagerIndex = make_money_content_vp.currentItem
                if (3 == currentPagerIndex){
                    NavUtils.gotoTaskMineActivity(this)
                }else{
                    make_money_drawable.openDrawer(GravityCompat.END)
                }
            }
            make_money_task_screen_sure ->{
                val taskObj = getTaskObj()
//                MsgMgr.getInstance().sendMsg(MsgType.TASK_OBJ,taskObj)
                make_money_drawable.closeDrawer(GravityCompat.END)
                taskFragment1.refreshData(taskObj)
                taskFragment2.refreshData(taskObj)
                taskFragment3.refreshData(taskObj)
            }
            make_money_task_screen_reset ->{
                make_money_drawable.closeDrawer(GravityCompat.END)
                makeMoneyTaskTypeAdapter.clearSelected()
                makeMoneyTaskClassificationAdapter.clearSelected()
                val taskObj = getTaskObj()
//                MsgMgr.getInstance().sendMsg(MsgType.TASK_OBJ,taskObj)
                taskFragment1.refreshData(taskObj)
                taskFragment2.refreshData(taskObj)
                taskFragment3.refreshData(taskObj)
            }
        }
    }

    private fun getTaskObj(): TaskObj{
        val taskObj = TaskObj()
        val taskType = makeMoneyTaskTypeAdapter.taskScreenList.filter {
            it.isSelect
        }
        val taskClassificationType = makeMoneyTaskClassificationAdapter.taskScreenList.filter {
            it.isSelect
        }
        taskObj.taskTypeBeans = taskType
        taskObj.taskClassificationBeans = taskClassificationType
        return taskObj
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)
        dismissLoading()
        if(null == entity){
            return
        }
        when(url){
            DqUrl.url_task_type -> {
                val data = entity.data as List<TaskTypeBean>
                if(data is ArrayList<TaskTypeBean>){
                    makeMoneyTaskTypeAdapter.taskScreenList = data
                }
            }
            DqUrl.url_task_classification -> {
                val data = entity.data as List<TaskClassificationBean>
                if(data is ArrayList<TaskClassificationBean>){
                    makeMoneyTaskClassificationAdapter.taskScreenList = data
                }
            }
        }
    }

    override fun onActionModeFinished(mode: ActionMode?) {
        super.onActionModeFinished(mode)
    }

    override fun onDestroy() {
        super.onDestroy()
        MsgMgr.getInstance().detach(this)
    }

}