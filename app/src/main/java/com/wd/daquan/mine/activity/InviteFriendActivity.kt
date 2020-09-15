package com.wd.daquan.mine.activity

import android.os.Bundle
import android.view.View
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.mine.dialog.CommonTipDialog
import com.wd.daquan.mine.dialog.DqKouLingDialog
import com.wd.daquan.mine.presenter.MineChildCommentPresenter
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.DqIntviteRewardEntity
import kotlinx.android.synthetic.main.activity_invite_friend.*

/**
 * 邀请好友的界面
 */
class InviteFriendActivity: DqBaseActivity<MineChildCommentPresenter, DataBean<Any>>() {

    private var data = DqIntviteRewardEntity()

    override fun createPresenter() = MineChildCommentPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_invite_friend)
    }

    override fun initView() {
        initTitle()
    }

    override fun initData() {
        val params = hashMapOf<String,String>()
        mPresenter.getIntviteReward(DqUrl.url_get_intviteReward,params)
    }

    private fun initTitle(){
        invite_friend_title.title = "邀请好友"
        invite_friend_title.leftIv.setOnClickListener {
            finish()
        }
    }

    override fun initListener() {
        super.initListener()
        invite_friend_guide.setOnClickListener(this)
        invite_friend_now.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v){
            invite_friend_guide -> {
                showInviteFriendGuideDialog()
            }
            invite_friend_now -> {
                showInviteFriendKouLingDialog()
            }
        }
    }

    private fun showInviteFriendGuideDialog(){
        val bundle = Bundle()
        bundle.putString(CommonTipDialog.KEY_CONTENT,data.result)
        val dialog = CommonTipDialog()
        dialog.arguments = bundle
        dialog.show(supportFragmentManager,"")
    }

    private fun showInviteFriendKouLingDialog(){
        val dialog = DqKouLingDialog()
        dialog.show(supportFragmentManager,"")
    }

    private fun updateUI(){
        val min = data.min.toDouble() / 100
        val max = data.max.toDouble() / 100
        invite_price.text = "${min}~${max}"
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)

        if (DqUrl.url_get_intviteReward == url){
            data = entity!!.data as DqIntviteRewardEntity
            updateUI()
        }
    }

}