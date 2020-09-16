package com.wd.daquan.mine.dialog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.da.library.utils.AESUtil
import com.wd.daquan.R
import com.wd.daquan.common.constant.DqUrl
import com.wd.daquan.explore.dialog.BaseFragmentDialog
import com.wd.daquan.model.BuildConfig
import com.wd.daquan.model.log.DqToast
import com.wd.daquan.model.mgr.ModuleMgr
import kotlinx.android.synthetic.main.dialog_common_tip.iv_close
import kotlinx.android.synthetic.main.dialog_dq_kouling.*

/**
 * 斗圈口令
 */
class DqKouLingDialog : BaseFragmentDialog(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_dq_kouling, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        copyLink()
        iv_close.setOnClickListener {
            dismiss()
        }
        go_wx.setOnClickListener {
            goWx()
        }
    }

    /**
     * 复制分享链接
     */
    private fun copyLink() {
        var link = "【斗圈】下载就能领红包(内测版只适用于安卓手机)\n 点击下方下载连接 \n"
        try {
            val params = AESUtil.encrypt(ModuleMgr.getCenterMgr().uid)
            link = link + BuildConfig.SERVER + DqUrl.url_vip_share_link + params
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //获取剪贴板管理器：
        val cm = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", link)
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData)
        DqToast.showShort("链接已复制进剪切板")
        invite_friend_link.text = link
    }

    private fun goWx(){
        if (activity == null){
            return
        }
        val lan: Intent? = requireActivity().packageManager.getLaunchIntentForPackage("com.tencent.mm")
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.component = lan?.component
        startActivity(intent)
    }

}