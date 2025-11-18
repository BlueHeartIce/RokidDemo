package com.blue.glassesapp.core.utils

import com.blankj.utilcode.util.TimeUtils

object AppTimeUtils {
    const val DATE_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm"
    const val DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    const val DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    const val HH_MM = "HH:mm"
    const val FILE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyyMMdd_HHmmss"

    /***
     * 获取当前时间
     */
    fun getCurrentTime(format: String = FILE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS): String {
        return TimeUtils.millis2String(System.currentTimeMillis(), format)
    }
}