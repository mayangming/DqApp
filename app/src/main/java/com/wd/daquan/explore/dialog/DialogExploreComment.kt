package com.wd.daquan.explore.dialog

import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wd.daquan.R
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.FindUserDynamicDescBean
import com.wd.daquan.model.bean.UserDynamicCommentDataListBean
import com.wd.daquan.model.interfaces.DqCallBack
import com.wd.daquan.model.retrofit.RetrofitHelp
import kotlinx.android.synthetic.main.dialog_explore_comment.*

/**
 * 朋友圈操作对话框
 */
class DialogExploreComment : AppCompatDialogFragment(){

    var userDynamicCommentDataListBean  = UserDynamicCommentDataListBean()

    var delCommentListener: ((FindUserDynamicDescBean) -> Unit?) ?= null

    companion object{
        const val ACTION_DEL_COMMENT_BEAN = "actionDelCommentBean"
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
        userDynamicCommentDataListBean = arguments?.getSerializable(ACTION_DEL_COMMENT_BEAN) as UserDynamicCommentDataListBean
    }

    fun setOnDelCommentListener(delCommentListener : (FindUserDynamicDescBean) -> Unit){
        this.delCommentListener = delCommentListener
    }

    private fun onClick(view :View){
        when(view){
            explore_del_comment_cancel -> {
                dismiss()
            }
            explore_del_comment_del -> {
                dismiss()
                delComment()
            }
        }
    }

    private fun delComment(){
        val params = hashMapOf<String,String>()
        params["dynamicId"] = userDynamicCommentDataListBean.dynamicId.toString()
        params["commentId"] = userDynamicCommentDataListBean.commentId.toString()
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

}