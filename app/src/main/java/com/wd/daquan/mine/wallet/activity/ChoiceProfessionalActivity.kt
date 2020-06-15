package com.wd.daquan.mine.wallet.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.wd.daquan.R
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.mine.wallet.adapter.KotlinAdapter
import com.wd.daquan.mine.wallet.holder.BaseKotlinHolder
import kotlinx.android.synthetic.main.choice_professional_activity.*
import kotlinx.android.synthetic.main.text_adaptet_item.view.*

/**
 * @Author: 方志
 * @Time: 2019/5/20 15:55
 * @Description: 选择职业
 */
class ChoiceProfessionalActivity : WalletBaseActivity() {


    override fun setContentView() {
        setContentView(R.layout.choice_professional_activity)
    }

    override fun initView() {
        mTitleLayout = choice_professional_title_layout
        choice_professional_recycler_view.layoutManager = LinearLayoutManager(this)
        choice_professional_recycler_view.adapter = mAdapter
    }

    override fun initData() {
        val list = listOf("公务员", "事业单位员工","企业高管", "私营业主","金融从业人员", "律师","会计师", "医护人员","学生",
                "公司员工","商业服务人员", "工人","农林牧副渔","军人武警", "文体工作者","家庭主妇",
                "退休","自由职业者", "其他","司机")

        mAdapter.update(list)

    }

    private var mAdapter = object : KotlinAdapter<String, BaseKotlinHolder>(R.layout.text_adaptet_item) {
        override fun onBindData(holder: BaseKotlinHolder, position: Int) {
            holder.itemView.item_name.text = getItem(position)
            holder.itemView.setOnClickListener {
                setResult(RESULT_OK, Intent().putExtra(KeyValue.Wallet.CHOICE_PROFESSIONAL, getItem(position)))
                finish()
            }
        }
    }

}