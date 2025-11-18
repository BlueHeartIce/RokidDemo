package com.blue.glassesapp.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlin.collections.filter
import kotlin.collections.filterValues
import kotlin.collections.toList
import kotlin.collections.toMutableList
import kotlin.collections.toTypedArray

class PermissionHelper(
    private val caller: ActivityResultCaller,
    private val context: Context,
    private val onResult: (granted: Boolean, denied: List<String>) -> Unit,
) {

    // 普通动态权限回调
    private val requestLauncher =
        caller.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val denied = permissions.filterValues { !it }.keys.toList()
            onResult(denied.isEmpty(), denied)
        }

    /**
     * 请求普通权限 + Android 11 全存储权限（如果需要）
     */
    fun request(permissions: ArrayList<String>, needManageAllFiles: Boolean = false) {
        val denied = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }.toMutableList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && needManageAllFiles) {
            if (!Environment.isExternalStorageManager()) {
                // 跳转到设置页申请 MANAGE_EXTERNAL_STORAGE
                openAllFilesAccessSettings()
                return
            }
        }

        if (denied.isEmpty()) {
            onResult(true, emptyList())
        } else {
            requestLauncher.launch(denied.toTypedArray())
        }
    }

    /**
     * 跳转应用设置页
     */
    fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
        if (context is Activity) {
            context.startActivity(intent)
        }
    }

    /**
     * 跳转到 Android 11 全部文件访问设置页
     */
    private fun openAllFilesAccessSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            context.startActivity(intent)
        }
    }

    /**
     * 检查是否拥有 Android 11 全部文件访问权限
     */
    fun hasManageAllFilesPermission(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager()
    }
}
