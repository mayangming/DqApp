package com.wd.daquan.explore.fragment

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wd.daquan.BuildConfig
import com.wd.daquan.R
import com.wd.daquan.explore.activity.FriendAreaActivity
import com.wd.daquan.imui.constant.IntentCode
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.fragment_select_photo_fragment.*

/**
 * 底部图片选择Fragment
 */
class ExplorePhotoBottomFragment : BottomSheetDialogFragment(){
//    private var bottomSelectPhotoCallBack : BottomSelectPhotoCallBack?= null
    private var sourceType = FriendAreaActivity.REPLACE_BG //0 换背景 1，发动态

    companion object{
        const val SOURCE_TYPE = "sourceType"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_select_photo_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView(){
        bottom_select_photo_confirm.setOnClickListener(this::onClick)
        bottom_select_photo_cancel.setOnClickListener(this::onClick)
    }

    private fun onClick(view :View){
        when(view){
            bottom_select_photo_confirm -> {
                when(sourceType){
                    FriendAreaActivity.REPLACE_BG -> {
                        selectPhoto(1)
                        dismiss()
                    }
                    FriendAreaActivity.SEND_DYNAMIC ->{
                        selectPhoto(9)
                        dismiss()
                    }
                }

            }
            bottom_select_photo_cancel -> dismiss()
        }
    }

    private fun initData(){
        sourceType = arguments!!.getInt(SOURCE_TYPE,0)
        when(sourceType){
            FriendAreaActivity.REPLACE_BG -> {
                bottom_select_photo_confirm.text = "设置背景图片"
            }
            FriendAreaActivity.SEND_DYNAMIC ->{
                bottom_select_photo_confirm.text = "从相册中选择"
            }
        }
    }

    /**
     * 选择图片
     */
    private fun selectPhoto(maxInt: Int) {
        Matisse.from(activity)
                .choose(MimeType.ofImage()) //                .choose(MimeType.of(MimeType.JPEG,MimeType.PNG))//gif暂时不支持显示
                .capture(true)
                .captureStrategy(
                        CaptureStrategy(true, BuildConfig.APPLICATION_ID + ".dqprovider", "capture")
                )
                .countable(true) //最大选择数量为9
                .maxSelectable(maxInt)
                .gridExpectedSize(
                        context!!.resources.getDimensionPixelSize(R.dimen.grid_expected_size)
                )
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .forResult(IntentCode.REQUEST_CODE_CHOOSE)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == IntentCode.REQUEST_CODE_CHOOSE) {
//            Log.e("YM","Fragment1111111111111")
//            if (null == data){
//                return
//            }
//            Log.e("YM","Fragment2222222222222222")
//            val picturePath = Matisse.obtainResult(data) //获取uri路径
//            val picturePathStr = Matisse.obtainPathResult(data)
//            //            for (String path : picturePathStr){
////                Log.e("YM","图片的文件路径:"+path);
////            }
////            MsgMgr.getInstance().sendMsg(MsgType.CHAT_PICTURE, picturePath.get(0));
////            MsgMgr.getInstance().sendMsg(MsgType.CHAT_PICTURE, picturePath)
//            val tempPictures = ArrayList<Uri>()
//            for (uri in picturePath){
//                tempPictures.add(uri)
//            }
//            bottomSelectPhotoCallBack?.result(tempPictures)
//        }
//    }

//    fun setOnBottomSelectPhotoCallBack(bottomSelectPhotoCallBack : BottomSelectPhotoCallBack){
//        this.bottomSelectPhotoCallBack = bottomSelectPhotoCallBack
//    }
//    interface BottomSelectPhotoCallBack{
//        fun result(pictureUri: ArrayList<Uri>)
//        fun cancel()
//    }

}