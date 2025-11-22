package com.blue.glassesapp.feature.home.model

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.bluetooth.BluetoothHidDeviceAppQosSettings
import android.bluetooth.BluetoothHidDeviceAppSdpSettings
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import java.util.concurrent.Executors

/**
 * 单例版 Bluetooth HID 工具类
 * - 需先调用 init()
 * - 再调用 start() / connect() / sendReport()
 */
object BluetoothHidUtil {

    private var context: Context? = null
    private var adapter: BluetoothAdapter? = null
    private var hidSettings: BluetoothHidDeviceAppSdpSettings? = null

    private var bluetoothHidDevice: BluetoothHidDevice? = null
    private var bluetoothDevice: BluetoothDevice? = null

    private val mainHandler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()

    fun init(
        ctx: Context,
        bluetoothAdapter: BluetoothAdapter?,
        settings: BluetoothHidDeviceAppSdpSettings,
    ) {
        context = ctx
        adapter = bluetoothAdapter
        hidSettings = settings
    }

    /** 启动 HID Profile */
    fun start() {
        val ctx = context ?: return
        adapter?.getProfileProxy(ctx, serviceListener, BluetoothProfile.HID_DEVICE)
    }

    /** 停止 HID Profile */
    fun stop() {
        bluetoothHidDevice?.let { hid ->
            bluetoothDevice?.let { hid.disconnect(it) }
            hid.unregisterApp()
            adapter?.closeProfileProxy(BluetoothProfile.HID_DEVICE, hid)
        }
        bluetoothHidDevice = null
        bluetoothDevice = null
    }

    /** ServiceListener */
    private val serviceListener = object : BluetoothProfile.ServiceListener {
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
            if (profile == BluetoothProfile.HID_DEVICE) {
                LogUtils.d("BluetoothHidUtil", "HID Profile connected: $bluetoothHidDevice")
                bluetoothHidDevice = proxy as? BluetoothHidDevice?
                bluetoothHidDevice?.let { hidDevice ->
                    hidDevice.registerApp(hidSettings, null, null, Runnable::run,
                        object : BluetoothHidDevice.Callback() {
                            override fun onConnectionStateChanged(device: BluetoothDevice?, state: Int) {
                                super.onConnectionStateChanged(device, state)
                                Log.d("BluetoothHidDevice.Callback", "Connection state=$state device=${device?.address}")
                            }
                        },
                        )
                }
            }
        }

        override fun onServiceDisconnected(profile: Int) {
            LogUtils.d("BluetoothHidUtil", "HID Profile disconnected")
        }
    }

    /** HID Callback */
    private val callback = object : BluetoothHidDevice.Callback() {
        override fun onAppStatusChanged(device: BluetoothDevice?, registered: Boolean) {
            super.onAppStatusChanged(device, registered)
            Log.d("BluetoothHidDevice.Callback", "HID Registered = $registered")
        }

        override fun onConnectionStateChanged(device: BluetoothDevice?, state: Int) {
            super.onConnectionStateChanged(device, state)
            Log.d("BluetoothHidDevice.Callback", "Connection state=$state device=${device?.address}")
        }
    }

    /** 连接设备 */
    fun connect(mac: String): Boolean {
        val device = adapter?.getRemoteDevice(mac) ?: return false
        bluetoothDevice = device
        if (!PermissionUtils.isGranted(Manifest.permission.BLUETOOTH_CONNECT)) {
            Log.e("BluetoothHidUtil", "BLUETOOTH_CONNECT permission missing")
            return false
        }
        if (bluetoothHidDevice==null) {
            Log.e("BluetoothHidUtil", "HID Profile not connected")
            return false
        }
        return bluetoothHidDevice!!.connect(device)
    }

    /** 断开设备 */
    fun disconnect(): Boolean {
        val hid = bluetoothHidDevice ?: return false
        val dev = bluetoothDevice ?: return false

        var ok = hid.disconnect(dev)
        ok = hid.disconnect(dev) || ok // Pixel 系列可能需要两次
        bluetoothDevice = null
        return ok
    }

    /** 发送 HID Report */
    fun sendReport(id: Int, data: ByteArray): Boolean {
        val dev = bluetoothDevice ?: return false
        val hid = bluetoothHidDevice ?: return false
        Log.d("BluetoothHidUtil", "sendReport id=$id bytes=${data.toHexString()}")
        return hid.sendReport(dev, id, data)
    }

    /** ByteArray 转 Hex 字符串 */
    private fun ByteArray.toHexString(): String = joinToString(" ") { "%02X".format(it) }
}
