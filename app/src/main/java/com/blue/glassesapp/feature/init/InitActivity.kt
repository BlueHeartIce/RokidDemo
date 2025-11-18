package com.blue.glassesapp.feature.init

import android.content.Intent
import com.blankj.utilcode.util.LogUtils
import com.blue.armobile.R
import com.blue.armobile.databinding.ActivityInitBinding
import com.blue.glassesapp.core.base.BasePermissionActivity
import com.blue.glassesapp.core.db.DBManager
import com.blue.glassesapp.core.utils.AppInternalFileUtil
import com.blue.glassesapp.core.utils.CxrUtil
import com.blue.glassesapp.feature.home.ui.HomeActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @Description TODO
 *
 *
 * @Author liux
 * @Date 2023/7/28 14:25
 * @Version 1.0
 *
 * 初始化
 * loading
 *
 * 异常
 * 授权
 *
 * 重试
 */
class InitActivity : BasePermissionActivity<ActivityInitBinding>(R.layout.activity_init) {

    override fun onCreate() {
        super.onCreate()
        LogUtils.getConfig().setSaveDays(7).setLog2FileSwitch(true)
        binding.initView()
    }

    override fun init() {
        CxrUtil.initLocalDeviceInfo()
        DBManager.init(application)
        AppInternalFileUtil.init(application)

        MainScope().launch {
            delay(1000)
            startMainActivity()
        }
    }

    fun ActivityInitBinding.initView() {
        checkPermission()
        btnOpen.setOnClickListener {
            checkPermission()
        }
    }

    /**
     * 打开主页
     */
    private fun startMainActivity() {
        runOnUiThread {
            startActivity(Intent(mContext, HomeActivity::class.java))
            finish()
        }
    }

}