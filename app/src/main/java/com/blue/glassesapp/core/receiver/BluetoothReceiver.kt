package com.blue.glassesapp.core.receiver

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission


/**
 * <pre>
 *
 * </pre>
 *
 * <p>创建人: zxh</p>
 * <p>日期: 2025/11/3</p>
 */
class BluetoothConnectReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onReceive(context: Context, intent: Intent) {
        when (val action = intent.action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                val device = intent.getBluetoothDevice()
                // 判断device是否是rokid眼镜，然后发起重连...
            }
        }
    }

    fun Intent.getBluetoothDevice(): BluetoothDevice?{
       return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        }
    }
}