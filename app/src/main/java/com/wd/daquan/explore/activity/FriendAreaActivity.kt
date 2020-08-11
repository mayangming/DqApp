package com.wd.daquan.explore.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.da.library.listener.DialogListener
import com.dq.im.constants.Constants
import com.dq.im.type.ImType
import com.dq.im.util.oss.AliOssUtil
import com.dq.im.util.oss.SimpleUpLoadListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.wd.daquan.R
import com.wd.daquan.common.activity.DqBaseActivity
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.common.constant.KeyValue
import com.wd.daquan.common.utils.DialogUtils
import com.wd.daquan.common.utils.DqUtils
import com.wd.daquan.common.utils.NavUtils
import com.wd.daquan.explore.adapter.AreaAdapter
import com.wd.daquan.explore.fragment.ExplorePhotoBottomFragment
import com.wd.daquan.explore.itemdecoration.DividerItemDecoration
import com.wd.daquan.explore.presenter.FriendAreaPresenter
import com.wd.daquan.explore.type.SearchType
import com.wd.daquan.glide.GlideUtils
import com.wd.daquan.imui.constant.IntentCode
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.FindUserDynamicDescBean
import com.wd.daquan.model.bean.UserDynamicBean
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.mgr.ModuleMgr
import com.wd.daquan.third.helper.UserInfoHelper
import com.wd.daquan.util.FileUtils
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_friend_area.*
import java.io.IOException
import java.io.InputStream

/**
 * 朋友圈
 */
class FriendAreaActivity : DqBaseActivity<FriendAreaPresenter, DataBean<Any>>() {
    lateinit var areaAdapter : AreaAdapter
    private var tempFlag = 0 //0 换背景 1发布动态
    private var pageNum = 1 //当前页码
    private var pageSize = 10 //每页显示的条数
    private var friendId:String ?= null;//查询朋友圈好友的Id
    private var searchType = SearchType.ALL;//搜索类型
    private val bottomSelectPhotoFragment : ExplorePhotoBottomFragment by lazy {
        initBottomSelectPhotoFragment()
    }

    companion object{
        const val REPLACE_BG = 0
        const val SEND_DYNAMIC = 1
        const val SEARCH_TYPE = "searchType"
        const val USER_ID = "userId"
    }


    override fun createPresenter() = FriendAreaPresenter()

    override fun setContentView() {
        setContentView(R.layout.activity_friend_area)
    }

    override fun initView() {
        initTitle()
        initSmartRefreshLayout()
        initRecycleView()
        initAdapter()
        area_bg.setOnClickListener(this::onClick)
        area_user_head.setOnClickListener(this::onClick)
    }

    private fun initTitle(){
        friend_area_title.title = getString(R.string.my_area_title)
        friend_area_title.setRightIv(R.mipmap.my_area_camera)
        friend_area_title.leftIv.setOnClickListener { finish() }
        friend_area_title.setRightVisible(View.VISIBLE)
        friend_area_title.rightIv.setOnClickListener(this)
    }



    /**
     * 初始化朋友圈列表
     */
    private fun initRecycleView(){
        area_record.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun initAdapter(){
        areaAdapter = AreaAdapter()
        area_record.adapter = areaAdapter
        area_record.addItemDecoration(DividerItemDecoration(this))
    }

    /**
     * 初始化上拉加载和下拉刷新
     */
    private fun initSmartRefreshLayout(){
        area_refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener{
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getDynamicMessageList()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                pageNum = 1
                getDynamicMessageList()
            }

        })
    }

    override fun initData() {
        friendId = intent.getStringExtra(USER_ID)
        searchType = intent.getSerializableExtra(SEARCH_TYPE) as SearchType
        showLoading()
        getDynamicMessageList()
        findUserDynamic()
        area_user_name.text = UserInfoHelper.getUserDisplayName(friendId)
//        GlideUtils.loadRound(this, ModuleMgr.getCenterMgr().avatar, area_user_head,5)
        DqLog.e("YM","头像连接:${UserInfoHelper.getHeadPic(friendId)}")
        GlideUtils.loadRound(this,  UserInfoHelper.getHeadPic(friendId), area_user_head,5)

    }

    /**
     * 更新朋友圈列表
     */
    private fun updateDynamicList(areaBeanList :ArrayList<FindUserDynamicDescBean>, isAdd :Boolean = true){
//        areaAdapter.userDynamicDescBeanList = areaBeanList
        areaAdapter.addDynamicDescBeanList(areaBeanList,isAdd)
    }

    private fun findUserDynamic(){
        val params = hashMapOf<String, String>()
        mPresenter.findUserDynamic(DqUrl.url_dynamic_findUserDynamic,params)
    }

    /**
     * 保存朋友圈背景图片
     */
    private fun updateUserDynamic(pic :String){
        val params = hashMapOf<String, String>()
        params["desc"] = ""
        params["pic"] = pic
        params["updateTime"] = System.currentTimeMillis().toString()
        params["status"] = "0"
        mPresenter.updateUserDynamic(DqUrl.url_dynamic_updateUserDynamic,params)
    }

    private fun getDynamicMessageList(){
        val params = hashMapOf<String, String>()
        params["searchUserId"] = friendId ?: ModuleMgr.getCenterMgr().uid
        params["searchType"] = searchType.searchType
        params["pageNum"] = pageNum.toString()
        params["pageSize"] = pageSize.toString()
        mPresenter.getNewDynamicMessage(DqUrl.url_dynamic_findUserDynamicDesc,params)
    }

    override fun onClick(v: View?) {
        super.onClick(v)

        if (bottomSelectPhotoFragment.isAdded) {//解决方法就是添加这行代码，如果已经添加了，就移除掉然后再show，就不会出现Fragment already added的错误了。
            supportFragmentManager.beginTransaction().remove(bottomSelectPhotoFragment).commit();
        }
        when(v){
            area_bg ->{
                tempFlag = 0
                val bundle = Bundle()
                bundle.putInt(ExplorePhotoBottomFragment.SOURCE_TYPE,tempFlag)
                bottomSelectPhotoFragment.arguments = bundle
                bottomSelectPhotoFragment.show(supportFragmentManager,"")
            }
            friend_area_title.rightIv ->{
                tempFlag = 1
                val bundle = Bundle()
                bundle.putInt(ExplorePhotoBottomFragment.SOURCE_TYPE,tempFlag)
                bottomSelectPhotoFragment.arguments = bundle
                bottomSelectPhotoFragment.show(supportFragmentManager,"")
            }
            area_user_head -> {
                NavUtils.gotoUserInfoActivity(this, ModuleMgr.getCenterMgr().uid, ImType.P2P.value)
            }
        }
    }


    private fun initBottomSelectPhotoFragment():ExplorePhotoBottomFragment{
        val bottomSelectPhotoFragment = ExplorePhotoBottomFragment()
//        bottomSelectPhotoFragment.setOnBottomSelectPhotoCallBack(object : ExplorePhotoBottomFragment.BottomSelectPhotoCallBack{
//            override fun result(pictureUri: ArrayList<Uri>) {
//                Log.e("YM","选择的图片数量:${pictureUri.size}")
//                bottomSelectPhotoFragment.dismiss()
//                when(tempFlag){
//                    FriendAreaActivity.REPLACE_BG -> {
//                        upLoadBg(pictureUri[0])
//                    }
//                    FriendAreaActivity.SEND_DYNAMIC -> {
//                        NavUtils.gotoDynamicSendActivity(this@FriendAreaActivity,pictureUri)
//                    }
//                }
//
//            }
//
//            override fun cancel() {
//            }
//        })
        return bottomSelectPhotoFragment
    }


    /**
     * 上传背景图片
     */
    private fun upLoadBg(uri: Uri){
        GlideUtils.load(this, uri, area_bg,R.mipmap.area_head_bg,R.mipmap.area_head_bg)
        val documentFile = androidx.documentfile.provider.DocumentFile.fromSingleUri(this, uri)
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val photoData = FileUtils.toByteArray(inputStream)
            uploadFile(photoData, Constants.getImgName() + FileUtils.getFileSuffix(documentFile?.name))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 阿里云上传文件
     * @param fileData 单个文件内容
     * @param fileName
     */
    fun uploadFile(fileData: ByteArray, fileName: String) {
        AliOssUtil.getInstance().asyncPutObject(fileName, fileData, object : SimpleUpLoadListener() {
            override fun uploadComplete(url: String) {
                updateUserDynamic(url)
            }

            override fun uploadFail() {
                super.uploadFail()
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, paramArrayOfInt: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt)
        if (requestCode == KeyValue.ZERO) {
            if (!DqUtils.verifyPermissions(paramArrayOfInt)) {
                DialogUtils.showSettingQCNumDialog(this@FriendAreaActivity, "", getString(R.string.add_authority),
                        getString(R.string.cancel), getString(R.string.chat_bg_set), object : DialogListener {
                    override fun onCancel() {}
                    override fun onOk() {
                        NavUtils.startAppSettings(this@FriendAreaActivity)
                    }
                })
            } else {
            }
        }
    }

    override fun onSuccess(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onSuccess(url, code, entity)
        if (null == entity){
            return
        }
        when(url){
            DqUrl.url_dynamic_findUserDynamicDesc -> {//获取朋友圈列表
                area_refreshLayout.finishLoadMore()
                area_refreshLayout.finishRefresh()
                val userDynamicList = entity.data as ArrayList<FindUserDynamicDescBean>
                DqLog.e("YM","数据长度:${userDynamicList.size}")
                if(userDynamicList.isNotEmpty()){
                    updateDynamicList(userDynamicList,pageNum > 1)
                }

                if(pageNum == 1){
                    dismissLoading()
                }

                pageNum++
            }

            DqUrl.url_dynamic_findUserDynamic -> {//获取背景图
                val userDynamicBean = entity.data as UserDynamicBean
                GlideUtils.load(this, userDynamicBean.pic, area_bg,R.mipmap.area_head_bg,R.mipmap.area_head_bg)
            }

        }
    }

    override fun onFailed(url: String?, code: Int, entity: DataBean<Any>?) {
        super.onFailed(url, code, entity)
        when(url){
            DqUrl.url_dynamic_findUserDynamicDesc -> {//获取朋友圈列表
                area_refreshLayout.finishLoadMore()
                area_refreshLayout.finishRefresh()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK){
            return
        }
        if (null == data){
            return
        }
        if (requestCode == IntentCode.REQUEST_DYNAMIC_SEND){
            val dynamicBean = data.getSerializableExtra(DynamicSendActivity.ACTION_DYNAMIC_RESPONSE) as FindUserDynamicDescBean
            areaAdapter.addDynamicDescBean(dynamicBean)
        }else if (requestCode == IntentCode.REQUEST_CODE_CHOOSE) {

            val picturePath = Matisse.obtainResult(data) //获取uri路径
            val picturePathStr = Matisse.obtainPathResult(data)
            val tempPictures = ArrayList<Uri>()
            for (uri in picturePath){
                tempPictures.add(uri)
            }
            Log.e("YM","选择的图片数量:${tempPictures.size}")
            bottomSelectPhotoFragment.dismiss()
            when(tempFlag){
                REPLACE_BG -> {
                    upLoadBg(tempPictures[0])
                }
                SEND_DYNAMIC -> {
                    NavUtils.gotoDynamicSendActivity(this@FriendAreaActivity,tempPictures)
                }
            }
        }
    }

}