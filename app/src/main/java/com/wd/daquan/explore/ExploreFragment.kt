package com.wd.daquan.explore

import android.Manifest
import android.os.Bundle
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
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.FindUserDynamicDescBean
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.model.mgr.ModuleMgr
import kotlinx.android.synthetic.main.explore_fragment.*

/**
 * 探索功能页面
 */
class ExploreFragment : BaseFragment<ExplorePresenter, DataBean<Any>>(), View.OnClickListener{
    private lateinit var mToolbar: DqToolbar

    var needPermissions = arrayOf(Manifest.permission.CAMERA)

    override fun getLayoutId() = R.layout.explore_fragment

    override fun createPresenter() = ExplorePresenter()

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        mToolbar = view!!.findViewById(R.id.toolbar)
        initTitle()
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
    }

    private fun getNewDynamicNews(){
        val params = hashMapOf<String, String>()
        params["searchUserId"] = ModuleMgr.getCenterMgr().uid
        params["searchType"] = SearchType.ALL.searchType
        params["pageNum"] = "1"
        params["pageSize"] = "1"
        presenter.getNewDynamicMessage(DqUrl.url_dynamic_findUserDynamicDesc,params)
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

            }
            else ->  DqToast.showCenterShort("")
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
        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onFailed(url, code, entity)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            getNewDynamicNews()
        }
    }

}