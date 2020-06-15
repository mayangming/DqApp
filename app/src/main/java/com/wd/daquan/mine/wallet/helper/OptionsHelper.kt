package com.wd.daquan.mine.wallet.helper

import android.app.Activity
import android.graphics.Color
import com.meetqs.qingchat.pickerview.builder.OptionsPickerBuilder
import com.wd.daquan.DqApp
import com.wd.daquan.R
import com.wd.daquan.mine.wallet.bean.ChinaAreaBean
import com.wd.daquan.model.utils.GsonUtils
import java.util.concurrent.Executors

/**
 * @Author: 方志
 * @Time: 2019/5/21 10:36
 * @Description: 省份选择辅助类
 */
class OptionsHelper {
    //饿汉式单例
    companion object {
        private var instance: OptionsHelper? = null
            get() {
                if (field == null) {
                    field = OptionsHelper()
                }
                return field
            }
        @Synchronized
        fun get(): OptionsHelper {
            return instance!!
        }
    }

    /**
     * 省份数据
     */
    private lateinit var mProvinceList : ArrayList<ChinaAreaBean>
    /**
     * 城市数据
     */
    private lateinit var mCityList : ArrayList<ArrayList<String>>
    /**
     * 县区数据
     */
    private lateinit var mAreaList : ArrayList<ArrayList<ArrayList<String>>>

    /**
     * 获取省份、城市、县区数据
     */
    fun initJsonData() {
        Executors.newCachedThreadPool().execute{
            val jsonData = GsonUtils.getJson(DqApp.sContext, R.raw.province)
            val jsonBeans = GsonUtils.fromJsonList(jsonData, ChinaAreaBean::class.java)
            mProvinceList = ArrayList()
            mCityList = ArrayList()
            mAreaList = ArrayList()

            //添加省份数据
            mProvinceList.addAll(jsonBeans)

            try {
                for (bean in mProvinceList) {
                    //该省的城市列表（第二级）
                    val cityTempList = ArrayList<String>()
                    val cityList = bean.cityList
                    val areaTempList = ArrayList<ArrayList<String>>()
                    if (null != cityList && cityList.size > 0) {
                        for (cityBean in cityList) {
                            cityTempList.add(cityBean.name)
                            areaTempList.add(cityBean.area)
                        }
                        //添加城市数据
                        mCityList.add(cityTempList)
                        //添加区域数据
                        mAreaList.add(areaTempList)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 城市选择滚轮
     */
    fun showPickerView(activity : Activity?, listener : OnCityChoiceListener?) {

        val pvOptions = OptionsPickerBuilder(activity, { options1, options2, options3, _, _ ->
            val province = mProvinceList[options1].pickerViewText
            val city = mCityList[options1][options2]
            val area = mAreaList[options1][options2][options3]
//            manager.getKDPreferenceUserInfo().saveString(EBSharedPrefUser.city, city)
//            manager.getKDPreferenceUserInfo().saveString(EBSharedPrefUser.PROVINCE, province)
            listener?.getCity(province, city, area)

        }, null)
                .setTitleText(DqApp.getStringById(R.string.choice_city))
                .setDividerColor(DqApp.getColorById(R.color.text_blue))
                .setTextColorCenter(DqApp.getColorById(R.color.text_blue)) //设置选中项文字颜色
                .setContentTextSize(20)
                .setCancelText(DqApp.getStringById(R.string.cancel))//取消按钮文字
                .setSubmitText(DqApp.getStringById(R.string.confirm1))//确认按钮文字
                .setTitleSize(16)//标题文字大小
                .setSubCalSize(18)//确定取消文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(DqApp.getColorById(R.color.text_blue))//确定按钮文字颜色
                .setCyclic(true, false, false)
                .setCancelColor(Color.BLACK)
                .isDialog(true)
                .build()

        pvOptions.setPicker(mProvinceList, mCityList, mAreaList)//三级选择器
        pvOptions.show()
    }

    interface OnCityChoiceListener{
        fun getCity(province : String, city : String, area : String)
    }

    fun clear(){
        try {
            mProvinceList.clear()
            mCityList.clear()
        } catch (e: Exception) {
        }
    }

}