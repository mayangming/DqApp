package com.wd.daquan.explore.dialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.explore.type.ExploreOperatorType
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.FindUserDynamicDescBean
import com.wd.daquan.model.interfaces.DqCallBack
import com.wd.daquan.model.retrofit.RetrofitHelp
import kotlinx.android.synthetic.main.dialog_explore_comment.*

/**
 * 朋友圈操作对话框
 */
class DialogExploreComment : BaseFragmentDialog(){

    private var commentId  = ""//评论Id
    var dynamicId  = ""//动态Id
    var sourceType = ExploreOperatorType.TYPE_COMMENT.type //弹框类型
    var delCommentListener: ((FindUserDynamicDescBean) -> Unit?) ?= null
    var delDynamicListener: ((FindUserDynamicDescBean) -> Unit?) ?= null

    companion object{
        const val ACTION_COMMENT_ID = "actionCommentId"
        const val ACTION_DYNAMIC_ID = "actionDynamicId"
        const val ACTION_TYPE = "actionType"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.dialog_explore_comment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        explore_del_comment_cancel.setOnClickListener(this::onClick)
        explore_del_comment_del.setOnClickListener(this::onClick)
    }

    private fun initData(){
        commentId = arguments?.getString(ACTION_COMMENT_ID) ?: ""
        dynamicId = arguments?.getString(ACTION_DYNAMIC_ID) ?: ""
        sourceType = arguments?.getInt(ACTION_TYPE) ?: -1
    }

    fun setOnDelCommentListener(delCommentListener : (FindUserDynamicDescBean) -> Unit){
        this.delCommentListener = delCommentListener
    }

    fun setOnDelDynamicListener(delDynamicListener : (FindUserDynamicDescBean) -> Unit){
        this.delDynamicListener = delDynamicListener
    }

    private fun onClick(view :View){
        when(view){
            explore_del_comment_cancel -> {
                dismiss()
            }
            explore_del_comment_del -> {
                dismiss()
                when(sourceType){
                    ExploreOperatorType.TYPE_COMMENT.type ->   delComment()
                    ExploreOperatorType.TYPE_DYNAMIC.type ->   delDynamic(dynamicId)
                }

            }
        }
    }

    private fun delComment(){
        val params = hashMapOf<String,String>()
        params["dynamicId"] = dynamicId
        params["commentId"] = commentId
        delUserDynamicComment(DqUrl.url_dynamic_delUserDynamicComment,params)
    }

    /**
     * 删除评论
     */
    private fun delUserDynamicComment(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getDynamicApi().delUserDynamicComment(url, RetrofitHelp.getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<FindUserDynamicDescBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<FindUserDynamicDescBean>) {
//                        hideLoading();
                val dynamicDescBean = entity.data as FindUserDynamicDescBean
                delCommentListener?.invoke(dynamicDescBean)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<FindUserDynamicDescBean>) {
//                        hideLoading();
//                exploreAreaCallBack?.onFail()
            }
        })
    }

    /**
     * 删除动态
     */
    private fun delDynamic(dynamicId :String){
        val params = hashMapOf<String,String>()
        params["dynamicId"] = dynamicId
        RetrofitHelp.getDynamicApi().delUserDynamic(DqUrl.url_dynamic_delUserDynamic, RetrofitHelp.getRequestBody(params)).enqueue(object : DqCallBack<DataBean<FindUserDynamicDescBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<FindUserDynamicDescBean>) {
//                        hideLoading();
                val dynamicBean = entity.data as FindUserDynamicDescBean
                delDynamicListener?.invoke(dynamicBean)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<FindUserDynamicDescBean>) {
//                        hideLoading();
            }
        })
    }

}