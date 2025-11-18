package com.blue.glassesapp.core.utils

import android.app.Application
import java.io.File

/**
 * 负责管理文件的工具类
 */
object AppInternalFileUtil {
    lateinit var context: Application
    lateinit var recordPath: String

    fun init(context: Application) {
        this.context = context
        // 内部存储记录路径
        recordPath = "${AppInternalFileUtil.context.filesDir.absolutePath}/record/"
    }


    /**
     * 存储记录文件夹是否存在
     */
    fun isRecordFileExist(): Boolean {
        return File(recordPath).exists()
    }

    /**
     * 存储记录文件，创建一个文件返回路径
     * 传入文件名
     */
    fun createRecordFile(fileName: String): String {
        return "${recordPath}$fileName"
    }


}