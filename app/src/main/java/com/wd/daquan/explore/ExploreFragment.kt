package com.wd.daquan.explore

import android.Manifest
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.da.library.view.DqToolbar
import com.wd.daquan.R
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.common.fragment.BaseFragment
import com.wd.daquan.common.utils.DqUtils
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.presenter.ExplorePresenter
import com.wd.daquan.explore.type.DynamicReadStatus
import com.wd.daquan.explore.type.SearchType
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.model.bean.AreaUnReadSimpleBean
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.FindUserDynamicDescBean
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.model.mgr.ModuleMgr
import com.wd.daquan.model.rxbus.MsgMgr
import com.wd.daquan.model.rxbus.MsgType
import com.wd.daquan.model.rxbus.QCObserver
import kotlinx.android.synthetic.main.activity_friend_area.*
import kotlinx.android.synthetic.main.explore_fragment.*
import q.rorbin.badgeview.Badge
import q.rorbin.badgeview.QBadgeView

/**
 * 探索功能页面
 */
class ExploreFragment : BaseFragment<ExplorePresenter, DataBean<Any>>(), View.OnClickListener, QCObserver {
    private lateinit var mToolbar: DqToolbar
    lateinit var badgeView: Badge
    var needPermissions = arrayOf(Manifest.permission.CAMERA)

    override fun getLayoutId() = R.layout.explore_fragment

    override fun createPresenter() = ExplorePresenter()

    override fun initView(viewExplore: View?, savedInstanceState: Bundle?) {
        MsgMgr.getInstance().attach(this)
        mToolbar = viewExplore!!.findViewById(R.id.toolbar)
        initTitle()
        badgeView = QBadgeView(context).run {
            hide(true)
            bindTarget(my_explore).badgeNumber = 1
            badgeGravity = Gravity.CENTER or Gravity.END
            setBadgeTextSize(12f,true)
            setGravityOffset(2f,0f,true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MsgMgr.getInstance().detach(this)
    }
    fun initTitle(){
        mToolbar.backIv.visibility = View.GONE
    }
    override fun initListener() {
        explore_my_area_ll.setOnClickListener(this)
        explore_scan_ll.setOnClickListener(this)
        explore_money_ll.setOnClickListener(this)
    }

    override fun initData() {
        getNewDynamicNews()
        findUserDynamicMsgSum()
    }

    private fun getNewDynamicNews(){
        val params = hashMapOf<String, String>()
        params["searchUserId"] = ModuleMgr.getCenterMgr().uid
        params["searchType"] = SearchType.ALL.searchType
        params["pageNum"] = "1"
        params["pageSize"] = "1"
        presenter.getNewDynamicMessage(DqUrl.url_dynamic_findUserDynamicDesc,params)
    }

    /**
     * 获取朋友圈未读消息数量
     */
    private fun findUserDynamicMsgSum(){
        val params = hashMapOf<String, String>()
        presenter.findUserDynamicMsgSum(DqUrl.url_dynamic_findUserDynamicMsgSum,params)
    }

    override fun onClick(v: View?) {
        when (v){
            explore_my_area_ll -> {
                dynamic_unread_container.visibility = View.GONE
                ModuleMgr.getCenterMgr().saveLastDynamicReadStatus(DynamicReadStatus.READED.status)
                NavUtils.gotoFriendAreaActivity(activity,ModuleMgr.getCenterMgr().uid,SearchType.ALL)
            }
            explore_scan_ll ->
                if (DqUtils.checkPermissions(activity, *needPermissions)) {
                    NavUtils.gotoScanQRCodeActivity(activity)
                }
            explore_money_ll -> {
                NavUtils.gotoMakeMoneyActivity(activity)
            }
            else ->  DqToast.showCenterShort("")
        }
    }

    override fun onMessage(key: String?, value: Any?) {
        when(key){
            MsgType.CLEAR_UNREAD_AREA -> {
                badgeView.hide(true)
            }
        }
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)
        if (entity == null) return
        when(url){
            DqUrl.url_dynamic_findUserDynamicDesc -> {
                val userDynamicList = entity.data as List<FindUserDynamicDescBean>
                if(userDynamicList.isNotEmpty()){
                    val userDynamic = userDynamicList[0]
                    if (ModuleMgr.getCenterMgr().lastDynamicUid == ModuleMgr.getCenterMgr().uid){
                        dynamic_unread_container.visibility = View.GONE
                        return
                    }

                    if (ModuleMgr.getCenterMgr().lastDynamicTime > userDynamic.createTime){
                        dynamic_unread_container.visibility = View.GONE
                        return
                    }

                    if(ModuleMgr.getCenterMgr().lastDynamicReadStatus == DynamicReadStatus.READED.status){
                        dynamic_unread_container.visibility = View.GONE
                        return
                    }

                    dynamic_unread_container.visibility = View.VISIBLE
                    GlideUtils.loadRound(context, userDynamic.userHeadPic, dynamic_unread, 5)
                    ModuleMgr.getCenterMgr().saveLastDynamicUid(userDynamic.userId)
                    ModuleMgr.getCenterMgr().saveLastDynamicTime(userDynamic.createTime)
                    ModuleMgr.getCenterMgr().saveLastDynamicReadStatus(DynamicReadStatus.UNREAD.status)
                }
            }
            DqUrl.url_dynamic_findUserDynamicMsgSum -> {
                val bean = entity.data as AreaUnReadSimpleBean
                if (bean.count <= 0){
                    badgeView.hide(true)
                    return
                }
                badgeView.hide(false)
                badgeView.badgeNumber = bean.count

            }
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onFailed(url, code, entity)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            getNewDynamicNews()
            findUserDynamicMsgSum()
        }
    }

}