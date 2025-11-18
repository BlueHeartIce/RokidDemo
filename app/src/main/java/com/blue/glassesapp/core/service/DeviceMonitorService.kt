package com.blue.glassesapp.core.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blue.armobile.R
import com.blue.glassesapp.common.model.GlassesLinkState
import com.blue.glassesapp.core.event.BluetoothConnectEvent
import com.blue.glassesapp.core.receiver.BluetoothConnectReceiver
import com.blue.glassesapp.core.utils.CommonModel
import com.blue.glassesapp.core.utils.CxrUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * <pre>
 *
</pre> *
 *
 *
 * 创建人: zxh
 *
 * 日期: 2025/11/5
 */
class DeviceMonitorService : Service() {
    private val TAG = this::class.java.simpleName

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): DeviceMonitorService = this@DeviceMonitorService
    }


    override fun onBind(intent: Intent): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        startForegroundWithNotification()

        // 注册 EventBus 以接收事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        registerReceiver(bluetoothReceiver, buildFilter())
    }

    private fun startForegroundWithNotification() {
        val channelId = "device_monitor_channel"
        val channelName = "设备监控服务"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_LOW
            )
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }

        val notification =
            NotificationCompat.Builder(this, channelId).setContentTitle("设备监控运行中")
                .setContentText("正在监听蓝牙设备状态…")
                .setSmallIcon(R.mipmap.ic_launcher) // 替换为你的图标
                .setOngoing(true).build()

        startForeground(1, notification)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onGlassesControlEvent(event: BluetoothConnectEvent) {
        LogUtils.d("DeviceMonitor", "收到指令: ${event.blueAction} for ${event.address}")
        // 如果是配对页面，则不处理
        if (!CommonModel.isInBluetoothPairingPage && event.address == CommonModel.deviceMacAddress && CommonModel.glassesInfo.glassesLinkState != GlassesLinkState.CONNECTED) {
            LogUtils.d("DeviceMonitor linkGlasses", GsonUtils.toJson(event))
            CxrUtil.initGlassesLink(application.applicationContext) {}
        } else {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 注册 EventBus 以接收事件
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        unregisterReceiver(bluetoothReceiver)
        LogUtils.d("DeviceMonitor", "onDestroy")
    }

    private val bluetoothReceiver = BluetoothConnectReceiver()

    private fun buildFilter(): IntentFilter {
        return IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        }
    }


}
