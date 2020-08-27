package com.wd.daquan.explore.bean

import java.io.Serializable

/**
 * 自定义任务类型，用于重组厂商分类和任务分类的数据
 */
data class CustomTaskTypeBean(var typeId :String ?= "",var typePic :String ?= "", var typeName :String ?= null)