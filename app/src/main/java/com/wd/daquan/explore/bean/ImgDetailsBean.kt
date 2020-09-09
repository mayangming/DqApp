package com.wd.daquan.explore.bean

import androidx.annotation.DrawableRes
import com.wd.daquan.explore.type.ImgMakeMoneyType
import java.io.Serializable

class ImgDetailsBean :Serializable{
     var imgType : ImgMakeMoneyType = ImgMakeMoneyType.ImgRes("")
     @DrawableRes var imgRes = -1
     var imgAny :Any ?= null
     var imageName :String ?= ""//图片名字，主要是用于assets文件夹下面
}