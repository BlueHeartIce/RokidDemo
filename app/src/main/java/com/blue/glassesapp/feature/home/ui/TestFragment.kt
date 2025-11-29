package com.blue.glassesapp.feature.home.ui

import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils
import com.blue.armobile.R
import com.blue.armobile.databinding.FragmentTestBinding
import com.blue.glassesapp.core.base.BaseQMUIFragment
import com.blue.glassesapp.core.utils.AppInternalFileUtil
import com.blue.glassesapp.core.utils.CxrUtil
import com.blue.glassesapp.feature.home.vm.HomeVm
import com.rokid.cxr.client.extend.callbacks.ApkStatusCallback
import com.rokid.cxr.client.extend.callbacks.PhotoPathCallback
import com.rokid.cxr.client.extend.callbacks.PhotoResultCallback
import com.rokid.cxr.client.extend.callbacks.WifiP2PStatusCallback
import com.rokid.cxr.client.extend.infos.RKAppInfo
import com.rokid.cxr.client.utils.ValueUtil
import java.io.File


class TestFragment : BaseQMUIFragment<FragmentTestBinding>(R.layout.fragment_test),
    NetworkUtils.OnNetworkStatusChangedListener {
    private val viewModel: HomeVm by activityViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreate() {
        binding.lifecycleOwner = viewLifecycleOwner // 启用 LiveData 自动更新
        binding.model = viewModel
        LogUtils.d(TAG, viewModel.toString())
        binding.initView()
    }


    // 注册文件选择 Launcher
    private val apkFileLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val realPath = UriUtils.uri2File(uri).absolutePath
                if (realPath != null) {
                    logResult("已选择文件：$realPath")
                    // 启动 Rokid SDK 上传动作
                    startUploadApkAction(realPath)
                } else {
                    logResult("❌ 错误：无法获取文件的真实路径。您可能需要 MANAGE_EXTERNAL_STORAGE 权限。")
                    ToastUtils.showShort("无法读取文件路径，请检查权限。")
                }
            } else {
                logResult("取消了文件选择。")
            }
        }

    // 用于接收所有应用管理操作结果的回调
    private val apkStatusCallback = object : ApkStatusCallback {
        override fun onUploadApkSucceed() {
            ToastUtils.showShort("上传成功")
            logResult("上传成功")
        }

        override fun onUploadApkFailed() {
            ToastUtils.showShort("上传失败")
            logResult("上传失败")
        }

        override fun onInstallApkSucceed() {
            ToastUtils.showShort("安装成功")
            logResult("安装成功")
        }

        override fun onInstallApkFailed() {
            ToastUtils.showShort("安装失败")
            logResult("安装失败")
        }

        override fun onUninstallApkSucceed() {
            ToastUtils.showShort("卸载成功")
            logResult("卸载成功")
        }

        override fun onUninstallApkFailed() {
            ToastUtils.showShort("卸载失败")
            logResult("卸载失败")
        }

        override fun onOpenAppSucceed() {
            ToastUtils.showShort("打开成功")
            logResult("打开成功")
        }

        override fun onOpenAppFailed() {
            ToastUtils.showShort("打开失败")
            logResult("打开失败")
        }
    }

    override fun onNetworkStatusChanged(isConnected: Boolean) {
        // ... (网络状态变化处理，如果需要)
    }

    private fun FragmentTestBinding.initView() {
        // 1. 选择文件并上传/安装 APK (取代路径输入)
        btnUploadApk.setOnClickListener {
            logResult("启动文件选择器...")
            // 使用特定的 MIME 类型筛选 APK 文件
            apkFileLauncher.launch("application/vnd.android.package-archive")
        }

        btnWifiP2p.setOnClickListener {
            CxrUtil.cxrInstance.initWifiP2P(object : WifiP2PStatusCallback {
                override fun onConnected() {
                    logResult("wifi p2p 已连接")
                }

                override fun onDisconnected() {
                    logResult("wifi p2p 已断开")
                }

                override fun onFailed(p0: ValueUtil.CxrWifiErrorCode?) {
                    logResult("wifi p2p 连接失败")
                }

            })
        }

        // 2. 卸载 APK
        btnUninstallApk.setOnClickListener {
            val packageName = etInputParam.text.toString().trim()
            if (packageName.isEmpty()) {
                ToastUtils.showShort("请输入要卸载的包名")
                return@setOnClickListener
            }
            logResult("尝试启动卸载: $packageName")
            val status = CxrUtil.uninstallApk(packageName, apkStatusCallback)
            logResult("卸载启动结果 (同步): $status")
        }

        // 3. 打开 APP
        btnOpenApp.setOnClickListener {
            val packageName = etInputParam.text.toString().trim()
            val activity = etActivity.text.toString().trim()
            if (packageName.isEmpty()) {
                ToastUtils.showShort("请输入要打开应用的包名")
                return@setOnClickListener
            }
            val appInfo = RKAppInfo(packageName, activity)
            logResult("尝试打开应用: $packageName")
            val status = CxrUtil.openApp(appInfo, apkStatusCallback)
            logResult("打开应用结果 (同步): $status")
        }

        btnTakePhotoGlobal.setOnClickListener {
            val width = 1280
            val height = 720
            val quality = 80
            CxrUtil.cxrInstance.takeGlassPhotoGlobal(
                height, width, quality, object : PhotoResultCallback {
                    override fun onPhotoResult(
                        p0: ValueUtil.CxrStatus?,
                        p1: ByteArray?,
                    ) {
                        if (p0 == ValueUtil.CxrStatus.RESPONSE_SUCCEED) {
                            logResult("拍照成功")
                            ToastUtils.showShort("拍照成功")
                            // 保存照片 到 download 目录
                            val fileName = TimeUtils.getNowString() + ".jpg"

                            val file = File(
                                AppInternalFileUtil.recordPath, fileName
                            )
                            ImageUtils.bytes2Bitmap(p1).let {
                                ImageUtils.save(it, file, Bitmap.CompressFormat.JPEG)
                                ToastUtils.showShort("保存成功")
                                logResult("保存成功: ${file.absolutePath}")
                            }

                        } else {
                            logResult("拍照失败")
                            ToastUtils.showShort("拍照失败")
                        }
                    }
                })
        }
    }

    // -------------------------------------------------------------------
    // --- 辅助方法 ---
    // -------------------------------------------------------------------

    // 封装上传 Rokid SDK 调用的方法
    private fun startUploadApkAction(apkPath: String) {
        logResult("开始调用 Rokid SDK 上传/安装: $apkPath")
        val result = CxrUtil.startUploadApk(apkPath, apkStatusCallback)
        logResult("启动上传结果 (同步): $result")
    }

    // 内部日志输出方法，更新 TextView
    private fun logResult(message: String) {
        ThreadUtils.runOnUiThread {
            val currentText = binding.tvResultLog.text.toString()
            val newLog = "操作日志:\n> $message\n" + currentText.substringAfter("操作日志:\n")
            binding.tvResultLog.text = newLog
            LogUtils.d(TAG, message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CxrUtil.cxrInstance.deinitWifiP2P()
    }
}