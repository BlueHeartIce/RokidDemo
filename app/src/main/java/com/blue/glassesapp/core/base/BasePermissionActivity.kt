package com.blue.glassesapp.core.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import com.blue.glassesapp.core.utils.PermissionHelper

/**
 * <pre>
 * </pre>
 *
 * 创建人: zxh
 *
 * 日期: 2024/8/22
 */
abstract class BasePermissionActivity<T : ViewDataBinding>(layoutId: Int) :
    BaseActivity<T>(layoutId) {
    private val requestPermissionCode = 1001
    val mNeedPermissions = arrayListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_WIFI_STATE,
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            addAll(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        } else {
            addAll(
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }


    private lateinit var permissionHelper: PermissionHelper
    override fun onCreate() {
        permissionHelper = PermissionHelper(this, this) { granted, denied ->
            if (granted) {
                // ✅ 所有权限通过后初始化
                init()
            } else {
                // ❌ 有权限被拒绝
                showPermissionDeniedDialog(denied)
            }
        }
    }

    private var hasRequestedPermissions = false

    /**
     * 检测应用权限授权
     */
    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionHelper.request(mNeedPermissions, needManageAllFiles = true)
        } else {
            hasRequestedPermissions = true
            init()
        }
    }

    var alertDialog: AlertDialog? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //授权返回
        if (requestCode == requestPermissionCode) {
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (alertDialog != null && alertDialog!!.isShowing) {
                        return
                    }
                    alertDialog = AlertDialog.Builder(this).setTitle("系统提示")
                        .setMessage("授权拒绝，请到权限页面授予权限！")
                        .setPositiveButton("确定") { dialog, which -> //确定按钮的响应事件
                            // 打开权限设置页面
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }.setNegativeButton("") { dialog, which -> //确定按钮的响应事件
                            alertDialog?.dismiss()
                            alertDialog = null
                        }.show()
                    return
                }
            }
            init()
        }
    }

    abstract fun init()

    private fun showPermissionDeniedDialog(denied: List<String>) {
        AlertDialog.Builder(this).setTitle("权限请求")
            .setMessage("应用需要以下权限才能正常运行:\n${denied.joinToString("\n")}")
            .setPositiveButton("去设置") { _, _ ->
                permissionHelper.openAppSettings()
            }.setNegativeButton("取消", null).show()
    }

}
