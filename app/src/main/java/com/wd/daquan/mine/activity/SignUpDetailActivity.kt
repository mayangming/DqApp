package com.wd.daquan.mine.activity

import android.os.Bundle
import android.text.Html
import androidx.recyclerview.widget.GridLayoutManager
import com.netease.nim.uikit.common.ui.recyclerview.decoration.SpacingDecoration
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.mine.adapter.SignUpAdapter
import com.wd.daquan.mine.dialog.SignDialog
import com.wd.daquan.mine.presenter.IntegralMallPresenter
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.SignUpEntity
import kotlinx.android.synthetic.main.activity_sign_detail.*

/**
 * 签到详情页面
 */
class SignUpDetailActivity : DqBaseActivity<IntegralMallPresenter, DataBean<Any>>(){
    private var signAdapter = SignUpAdapter()
    private var signUpEntity = SignUpEntity()
    companion object{
        const val KEY_ACTION = "signUpEntity"
    }

    override fun createPresenter() = IntegralMallPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_sign_detail)
    }

    override fun initView() {
        initTitle()
        initRecycleView()
    }

    override fun initData() {
        signUpEntity = intent.getSerializableExtra(KEY_ACTION) as SignUpEntity
        updateUi()
    }

    private fun initTitle(){
        sign_detail_title.title = "签到详情"
        sign_detail_title.leftIv.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (signUpEntity.isSign){
            val currentDay = signUpEntity.dbUserSign.signNum//当前次数
            val money = signUpEntity.list[currentDay-1].dbaward.toString()
            showSignDialog(money)
        }
    }

    private fun initRecycleView() {
        sign_record_brv.apply {
            layoutManager = GridLayoutManager(this@SignUpDetailActivity, 7)
            adapter = signAdapter
        }
        sign_record_brv.addItemDecoration(SpacingDecoration(15,10,false))
    }

    private fun updateUi(){
        sign_record.text = "已签${signUpEntity.dbUserSign.signNum}/${signUpEntity.list.size}次"
        signAdapter.signUpEntity = signUpEntity
        sign_rule_content.text =  Html.fromHtml(signUpEntity.dbRule)
    }

    /**
     * 显示签到兑换
     */
    private fun showSignDialog(content :String){
        val arguments = Bundle()
        arguments.putString(SignDialog.KEY_ACTION,content)
        val taskTypeDialog = SignDialog()
        taskTypeDialog.arguments = arguments
        taskTypeDialog.show(supportFragmentManager, "")
    }

}