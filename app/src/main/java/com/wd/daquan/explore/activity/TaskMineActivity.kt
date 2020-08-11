package com.wd.daquan.explore.activity

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.dq.im.viewmodel.TeamMessageViewModel
import com.google.android.material.tabs.TabLayout
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.explore.adapter.MakeMoneyPagerAdapter
import com.wd.daquan.explore.fragment.MakeMoneyTaskMineTaskFragment
import com.wd.daquan.explore.presenter.MakeMoneyPresenter
import com.wd.daquan.explore.type.MakeMonetTaskType
import com.wd.daquan.explore.viewmodel.FragmentItemViewModel
import com.wd.daquan.model.bean.DataBean
import kotlinx.android.synthetic.main.activity_make_money.*

/**
 * 我的任务页面
 */
class TaskMineActivity : DqBaseActivity<MakeMoneyPresenter, DataBean<Any>>(){
    private lateinit var fragmentViewModel : FragmentItemViewModel
    override fun createPresenter() = MakeMoneyPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_task_mine)
    }

    override fun initView() {
        fragmentViewModel = ViewModelProviders.of(this)[FragmentItemViewModel::class.java]
        initTitle()
        initViewViewPager()
        initTabAndPagerListener(make_money_content_tab,make_money_content_vp)
        initTab(arrayListOf("全部","待提交","审核中","不合格","已完成"),make_money_content_tab)
    }

    override fun initData() {
    }

    private fun initTitle(){
        make_money_title.title = "任务管理"
        make_money_title.leftIv.setOnClickListener { finish() }
    }

    private fun initViewViewPager(){
        val taskMineFragmentAll = MakeMoneyTaskMineTaskFragment()
        val taskMineFragmentAudit = MakeMoneyTaskMineTaskFragment()
        val bundle1 = Bundle()
        bundle1.putString(MakeMoneyTaskMineTaskFragment.TASK_STATUS, MakeMonetTaskType.TASK_AUDIT.taskType.toString())
        taskMineFragmentAudit.arguments = bundle1
        val taskMineFragmentSubmitIng = MakeMoneyTaskMineTaskFragment()
        val bundle2 = Bundle()
        bundle2.putString(MakeMoneyTaskMineTaskFragment.TASK_STATUS, MakeMonetTaskType.TASK_SUBMIT_ING.taskType.toString())
        taskMineFragmentSubmitIng.arguments = bundle2
        val taskMineFragmentGiveUp = MakeMoneyTaskMineTaskFragment()
        val bundle3 = Bundle()
        bundle3.putString(MakeMoneyTaskMineTaskFragment.TASK_STATUS, MakeMonetTaskType.TASK_GIVE_UP.taskType.toString())
        taskMineFragmentGiveUp.arguments = bundle3

        val taskMineFragmentComplete = MakeMoneyTaskMineTaskFragment()
        val bundle4 = Bundle()
        bundle4.putString(MakeMoneyTaskMineTaskFragment.TASK_STATUS, MakeMonetTaskType.TASK_COMPLETE_SUCCESS.taskType.toString())
        taskMineFragmentComplete.arguments = bundle4

        fragmentViewModel.addNewAt(taskMineFragmentAll)
        fragmentViewModel.addNewAt(taskMineFragmentSubmitIng)
        fragmentViewModel.addNewAt(taskMineFragmentAudit)
        fragmentViewModel.addNewAt(taskMineFragmentGiveUp)
        fragmentViewModel.addNewAt(taskMineFragmentComplete)
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
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {//再次选中
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {//没选中
                val view = tab!!.customView
                val titleTv = view!!.findViewById<TextView>(R.id.make_money_tab_tv)
                titleTv.setTextColor(resources.getColor(R.color.color_666666))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {//选中
                val index = tab!!.position
                val view = tab.customView
                val titleTv = view!!.findViewById<TextView>(R.id.make_money_tab_tv)
                titleTv.setTextColor(resources.getColor(R.color.color_F39825))
                viewPager2.setCurrentItem(index, true)
            }
        })
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //选择的位置
                tabLayout.getTabAt(position)?.select()
            }
        })
    }
}