package com.wd.daquan.explore.fragment

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.wd.daquan.R
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.explore.popupwindow.CustomPopupWindow
import com.wd.daquan.explore.type.ReviewCommentType
import com.wd.daquan.model.bean.DataBean
import com.wd.daquan.model.bean.FindUserDynamicDescBean
import com.wd.daquan.model.bean.SaveUserDynamicLikeBean
import com.wd.daquan.model.bean.UserDynamicCommentDataListBean
import com.wd.daquan.model.interfaces.DqCallBack
import com.wd.daquan.model.log.DqLog
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.model.retrofit.RetrofitHelp
import kotlinx.android.synthetic.main.fragment_explore_area_review.*

/**
 * 底部评论输入框
 */
class ExploreAreaBottomFragment : BottomSheetDialogFragment(){

    private var dynamicBean = FindUserDynamicDescBean()
    private var dynamicCommentBean: UserDynamicCommentDataListBean ?= null
    private var exploreAreaCallBack : ExploreAreaCallBack ?= null
    companion object{
        const val DYNAMIC_BEAN = "dynamicBean"
        const val DYNAMIC_COMMENT_BEAN = "dynamicCommentBean"
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore_area_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView(){
        review_input_edt.isFocusable = true
        review_input_edt.isFocusableInTouchMode = true
        review_input_edt.postDelayed(Runnable {
            review_input_edt.requestFocus()
            (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)?.showSoftInput(review_input_edt, 0)
        }, 300)
        review_input_send_tv.setOnClickListener(this::onClick)
    }

    override fun onStart() {
        super.onStart()
        //去除背景
        val window = dialog?.window;
        val windowParams = window?.attributes;
        windowParams?.dimAmount = 0.0f;
        window?.attributes = windowParams;
    }

    private fun initData(){
        dynamicBean = arguments?.getSerializable(DYNAMIC_BEAN) as FindUserDynamicDescBean
        val tempBean = arguments?.getSerializable(DYNAMIC_COMMENT_BEAN)
        tempBean?.let {
            dynamicCommentBean = it as UserDynamicCommentDataListBean
            review_input_edt.hint = "回复${dynamicCommentBean?.userNick}:"
        }
    }

    private fun onClick(view :View){
        when(view){
            review_input_send_tv -> {
                dynamicComment()
                dismiss()
            }
        }
    }

    private fun dynamicComment(){
        val params = hashMapOf<String,String>()
        params["dynamicId"] = dynamicBean.dynamicId.toString()
        params["desc"] = review_input_edt.text.toString()
        params["type"] = if (null == dynamicCommentBean) ReviewCommentType.COMMENT.type else ReviewCommentType.REVIEW.type
        dynamicCommentBean?.let {
            params["friendId"] = it.userId
            params["toCommentId"] = it.commentId.toString()
        }
        saveUserDynamicComment(DqUrl.url_dynamic_saveUserDynamicComment,params)
    }

    /**
     * 评论
     */
    private fun saveUserDynamicComment(url :String, hashMap :Map<String, String>){
        RetrofitHelp.getDynamicApi().saveUserDynamicLike(url, RetrofitHelp.getRequestBody(hashMap)).enqueue(object : DqCallBack<DataBean<FindUserDynamicDescBean>>(){
            override fun onSuccess(url: String?, code: Int, entity: DataBean<FindUserDynamicDescBean>) {
//                        hideLoading();
                val dynamicDescBean = entity.data as FindUserDynamicDescBean
                exploreAreaCallBack?.onSuccess(dynamicDescBean)
            }

            override fun onFailed(url: String?, code: Int, entity: DataBean<FindUserDynamicDescBean>) {
//                        hideLoading();
                exploreAreaCallBack?.onFail()
                DqToast.showCenterShort(entity.content)
            }
        })
    }

    fun setOnExploreAreaCallBack(exploreAreaCallBack : ExploreAreaCallBack){
        this.exploreAreaCallBack = exploreAreaCallBack
    }
    /**
     * 底部评论输入框回调
     */
    interface ExploreAreaCallBack{
        fun onSuccess(dynamicDescBean: FindUserDynamicDescBean)
        fun onFail()
    }
}