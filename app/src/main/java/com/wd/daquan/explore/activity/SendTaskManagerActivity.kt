package com.wd.daquan.explore.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.adapter.SendTaskManagerPagerAdapter
import com.wd.daquan.explore.fragment.SendTaskManagerFragment
import com.wd.daquan.explore.presenter.SendTaskPresenter
import com.wd.daquan.explore.viewmodel.FragmentItemViewModel
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.TaskDetailsBean
import kotlinx.android.synthetic.main.activity_task_manager.*

/**
 * 任务管理页面
 */
class SendTaskManagerActivity : DqBaseActivity<SendTaskPresenter, DataBean<TaskDetailsBean>>(){
    private lateinit var fragmentViewModel : FragmentItemViewModel
    override fun createPresenter() = SendTaskPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_task_manager)
    }

    override fun initView() {
        fragmentViewModel = ViewModelProvider(this).get(FragmentItemViewModel::class.java)
        initTitle()
        initViewViewPager()
        initTabAndPagerListener(task_manager_content_tab,task_manager_content_vp)
        initTab(arrayListOf("全部","待审核","已上架","未通过","已下架"),task_manager_content_tab)
    }

    override fun initData() {
    }

    private fun initTitle(){
        task_manager_title.title = "发布管理"
        task_manager_title.rightIv.setImageResource(R.mipmap.send_task_icon)
        task_manager_title.setRightVisible(View.VISIBLE)
        task_manager_title.rightIv.setOnClickListener(this::onClick)
        task_manager_title.leftIv.setOnClickListener { finish() }
    }

    private fun initViewViewPager(){
        val taskMineFragmentAll = SendTaskManagerFragment().apply {
            val bundle = Bundle()
            bundle.putInt(SendTaskManagerFragment.KEY_LIST_TYPE,SendTaskManagerFragment.SEND_TASK_ALL)
            arguments = bundle
        }
        val taskMineFragmentReviewing = SendTaskManagerFragment().apply {
            val bundle = Bundle()
            bundle.putInt(SendTaskManagerFragment.KEY_LIST_TYPE,SendTaskManagerFragment.SEND_TASK_REVIEWING)
            arguments = bundle
        }
        val taskMineFragmentShangJia = SendTaskManagerFragment().apply {
            val bundle = Bundle()
            bundle.putInt(SendTaskManagerFragment.KEY_LIST_TYPE,SendTaskManagerFragment.SEND_TASK_SHANGJIA)
            arguments = bundle
        }
        val taskMineFragmentNoPass = SendTaskManagerFragment().apply {
            val bundle = Bundle()
            bundle.putInt(SendTaskManagerFragment.KEY_LIST_TYPE,SendTaskManagerFragment.SEND_TASK_NO_PASS)
            arguments = bundle
        }
        val taskMineFragmentOffSelf = SendTaskManagerFragment().apply {
            val bundle = Bundle()
            bundle.putInt(SendTaskManagerFragment.KEY_LIST_TYPE,SendTaskManagerFragment.SEND_TASK_OFF_SELF)
            arguments = bundle
        }

        fragmentViewModel.addNewAt(taskMineFragmentAll)
        fragmentViewModel.addNewAt(taskMineFragmentReviewing)
        fragmentViewModel.addNewAt(taskMineFragmentShangJia)
        fragmentViewModel.addNewAt(taskMineFragmentNoPass)
        fragmentViewModel.addNewAt(taskMineFragmentOffSelf)
        val pagerAdapter = SendTaskManagerPagerAdapter(this)
        pagerAdapter.fragmentViewModel = fragmentViewModel
        task_manager_content_vp.adapter = pagerAdapter
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
        tabLayout.tabMode = TabLayout.MODE_AUTO
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

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v){
            task_manager_title.rightIv -> {
                NavUtils.gotoSendTaskActivity(this)
            }
        }
    }

}